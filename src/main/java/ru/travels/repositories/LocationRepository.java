package ru.travels.repositories;

import org.restexpress.exception.BadRequestException;
import ru.travels.model.Location;
import ru.travels.repositories.load.LoadLocations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;

import static ru.travels.utils.Utils.toInt;
import static ru.travels.utils.Utils.toLong;

public class LocationRepository {

    private final Tarantool tarantool;

    private ZonedDateTime now;

    private final Map<String, Integer> keys = createKeys();

    private static final Set<Integer> addedLocationId = new HashSet<>();

    public LocationRepository(Tarantool tarantool) {
        this.tarantool = tarantool;
    }

    public void setNow(ZonedDateTime now) {
        this.now = now;
    }

    public ZonedDateTime getNow() {
        return now;
    }

    public Location get(Integer locationId) {
        List<?> locations = tarantool.getClient().syncOps().select(
                Tarantool.SPACE_LOCATION,
                0,
                Arrays.asList(locationId),
                0, 1024, 0);
        if (locations == null
                || locations.size() == 0
                || locations.get(0) == null) {
            return null;
        }
        ArrayList<?> location = (ArrayList<?>)locations.get(0);
        return new Location(
                toInt(location.get(0)),   // id
                (String)location.get(1),    // place
                (String)location.get(2),    // country
                (String)location.get(3),    // city
                toInt(location.get(4)));  // distance
    }

    public boolean isExists(Integer locationId) {
        List<?> locations = tarantool.getClient().syncOps().select(
                Tarantool.SPACE_LOCATION,
                0,
                Arrays.asList(locationId),
                0, 1024, 0);
        if (locations == null
                || locations.size() == 0
                || locations.get(0) == null) {
            return false;
        }
        ArrayList<?> location = (ArrayList<?>)locations.get(0);
        return location.get(0) != null;
    }

    public void update(Integer locationId, Map<String, Object> location) throws BadRequestException {
        List<List<?>> op = new ArrayList<>();
        for (Map.Entry<String, Object> field : location.entrySet()) {
            if (field.getValue() == null) {
                throw new BadRequestException();
            }
            op.add(Arrays.asList("=", keys.get(field.getKey()), field.getValue()));
        }
        if (op.size() == 0) {
            return;
        }
        tarantool.getClient().asyncOps().update(Tarantool.SPACE_LOCATION,
                Arrays.asList(locationId),
                op.toArray());
    }

    public void add(Map<String, Object> l) throws Exception {
        tarantool.getClient().asyncOps().insert(Tarantool.SPACE_LOCATION,
                Arrays.asList(
                        l.get("id"),
                        l.get("place"),
                        l.get("country"),
                        l.get("city"),
                        l.get("distance")));
    }

    public void add(Location location) throws Exception {
//        addedLocationId.add(location.getId());
        tarantool.getClient().asyncOps().insert(Tarantool.SPACE_LOCATION, location.simpleList());
    }

    public void addBatch(LoadLocations locations) {
        for (Location location : locations.getLocations()) {
            try {
                add(location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Double getAvgFromList(Integer locationId, Long fromDate, Long toDate, Long fromAge, Long toAge, String gender) throws Exception {
        if (gender == null || gender.isEmpty()) {
            gender = null;
        } else if (! (Objects.equals(gender, "m") || Objects.equals(gender, "f"))) {
            throw new BadRequestException();
        }

        List<?> avgList = tarantool.getClient().syncOps().call("getAvgList",
                locationId, fromDate, toDate, gender);
        List<AvgItem> res = new ArrayList<>();
        for (Object visit : avgList) {
            List<Object> list = (List)visit;
            res.add(new AvgItem(toInt(list.get(0)), toLong(list.get(1))*1000));
        }
        Integer sum = 0, count = 0;
        for (AvgItem avgItem : res) {

            if (fromAge != null) {
                ZonedDateTime nowMinus = getNow().minusYears(fromAge);
                long nowMinusT = nowMinus.toInstant().toEpochMilli();
                if (avgItem.birtday >= nowMinusT) {
                    continue;
                }
            }
            if (toAge != null) {
                ZonedDateTime nowMinus = getNow().minusYears(toAge);
                long nowMinusT = nowMinus.toInstant().toEpochMilli();
                if (avgItem.birtday <= nowMinusT) {
                    continue;
                }
            }
            sum += avgItem.mark;
            count++;
        }
        if (count > 0) {
            double d = ((double)sum)/count;
            return new BigDecimal(d).setScale(5, RoundingMode.HALF_UP).doubleValue();
        } else {
            return 0.0;
        }
    }

    class AvgItem {
        public final Integer mark;
        public final Long birtday;

        public AvgItem(Integer mark, Long birtday) {
            this.mark = mark;
            this.birtday = birtday;
        }
    }

    private Map<String, Integer> createKeys() {
        Map<String, Integer> res = new HashMap<>();
        res.put("place", 1);
        res.put("country", 2);
        res.put("city", 3);
        res.put("distance", 4);
        return res;
    }

}
