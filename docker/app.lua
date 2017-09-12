#!/usr/bin/env tarantool

box.cfg {
}

local function bootstrap()
    box.schema.space.create('users')
    box.space.users:create_index('primary',
        { type = 'hash', parts = {1, 'unsigned'}})

    box.schema.space.create('locations')
    box.space.locations:create_index('primary',
        { type = 'hash', parts = {1, 'unsigned'}})

    box.schema.space.create('visits')
    box.space.visits:create_index('primary',
        { type = 'hash', parts = {1, 'unsigned'}})
    box.space.visits:create_index('locations',
        { unique=false, type = 'tree', parts = {2, 'unsigned'}})
    box.space.visits:create_index('users',
        { unique=false, type = 'tree', parts = {3, 'unsigned'}})
end

getAvgLocation = function(locationId, fromDate, toDate, fromAge, toAge, gender, now)
    local sum = 0
    local count = 0
    local user
    local usergender
    local userage, birthday
    for k, tuple in box.space.visits.index.locations:pairs(locationId, {iterator = 'EQ'}) do
        repeat
            if fromDate ~= nil and tuple[4] <= fromDate then break end
            if toDate ~= nil and tuple[4] >= toDate then break end
            user = box.space.users.index.primary:select{tuple[3]}
            usergender = user[1][5]
            birthday = user[1][6]
            userage = now - birthday
            if gender ~= nil and usergender ~= gender then break end
            if fromAge ~= nil and userage <= fromAge then break end
            if toAge ~= nil and userage >= toAge then break end

            sum = sum + tuple[5]
            count = count + 1
            break
        until true
    end
    if count == 0 then return 0 end
    return sum / count
end

getAvgList = function(locationId, fromDate, toDate, gender)
    local ret = {}
    local newtuple
    local user
    local usergender
    local birthday
    for k, tuple in box.space.visits.index.locations:pairs(locationId, {iterator = 'EQ'}) do
        repeat
            if fromDate ~= nil and tuple[4] <= fromDate then break end
            if toDate ~= nil and tuple[4] >= toDate then break end
            user = box.space.users.index.primary:select{tuple[3]}
            usergender = user[1][5]
            birthday = user[1][6]
            if gender ~= nil and usergender ~= gender then break end

            newtuple = box.tuple.new{tuple[5], birthday}
            table.insert(ret, newtuple)
            break
        until true
    end
    return unpack(ret)
end

getVisitsForUser = function(userId, fromDate, toDate, country, toDistance)
    local ret = {}
    local newtuple
    local location
    local locationCountry
    local locationToDistance
    for k, tuple in box.space.visits.index.users:pairs(userId, {iterator = 'EQ'}) do
        repeat
            if fromDate ~= nil and tuple[4] <= fromDate then break end
            if toDate ~= nil and tuple[4] >= toDate then break end
            location = box.space.locations.index.primary:select{tuple[2]}
            locationCountry = location[1][3]
            locationToDistance = location[1][5]
            if country ~= nil and country ~= locationCountry then break end
            if toDistance ~= nil and toDistance <= locationToDistance then break end
            newtuple = box.tuple.new{tuple[5], tuple[4], location[1][2] }
            table.insert(ret, newtuple)
            break
        until true
    end
    return unpack(ret)
end

box.once('bootstrap', bootstrap)
