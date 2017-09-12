package ru.travels.repositories;

import org.restexpress.exception.BadRequestException;
import ru.travels.model.User;
import ru.travels.model.UserVisit;
import ru.travels.repositories.load.LoadUsers;

import java.util.*;

import static ru.travels.utils.Utils.toInt;
import static ru.travels.utils.Utils.toLong;

public class UserRepository {

    private final Tarantool tarantool;

    private final Map<String, Integer> keys = createKeys();

    private static final Set<Integer> addedUserId = new HashSet<>();

    public UserRepository(Tarantool tarantool) {
        this.tarantool = tarantool;
    }

    public User get(Integer userId) {
        List<?> user = tarantool.getClient().syncOps().select(
                Tarantool.SPACE_USER,
                0,
                Arrays.asList(userId),
                0, 1024, 0);

        if (user == null
                || user.size() == 0
                || user.get(0) == null) {
            return null;
        }

        ArrayList<?> us = (ArrayList<?>)user.get(0);
        return new User(
                toInt(us.get(0)),
                (String)us.get(1),
                (String)us.get(2),
                (String)us.get(3),
                (String)us.get(4),
                toLong(us.get(5)));
    }

    public boolean isExists(Integer userId) {
        List<?> user = tarantool.getClient().syncOps().select(
                Tarantool.SPACE_USER,
                0,
                Arrays.asList(userId),
                0, 1024, 0);

        if (user == null
                || user.size() == 0
                || user.get(0) == null) {
            return false;
        }

        ArrayList<?> us = (ArrayList<?>)user.get(0);
        return us.get(0) != null;
    }

    public void add(Map<String, Object> u) throws Exception {
        tarantool.getClient().asyncOps().insert(Tarantool.SPACE_USER,
                Arrays.asList(
                        u.get("id"),
                        u.get("email"),
                        u.get("first_name"),
                        u.get("last_name"),
                        u.get("gender"),
                        u.get("birth_date")));
    }

    public void add(User user) throws Exception {
//        addedUserId.add(user.getId());
        tarantool.getClient().asyncOps().insert(Tarantool.SPACE_USER, user.simpleList());
    }

    public void update(Integer userId, Map<String, Object> user) throws BadRequestException {
        List<List<?>> op = new ArrayList<>();
        for (Map.Entry<String, Object> field : user.entrySet()) {
            if (field.getValue() == null) {
                throw new BadRequestException();
            }
            op.add(Arrays.asList("=", keys.get(field.getKey()), field.getValue()));
        }
        if (op.size() == 0) {
            return;
        }
        tarantool.getClient().asyncOps().update(Tarantool.SPACE_USER,
                Arrays.asList(userId),
                op.toArray());
    }

    public void addBatch(LoadUsers users) {
        for (User user : users.getUsers()) {
            try {
                add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<UserVisit> getVisits(Integer userId, Long fromDate, Long toDate, String country, Integer toDistance) throws Exception {
        country = country == null || country.isEmpty() ? null : country;
        List<?> visits = tarantool.getClient().syncOps().call("getVisitsForUser", userId, fromDate, toDate, country, toDistance);
        List<UserVisit> res = new ArrayList<>();
        for (Object visit : visits) {
            List<Object> list = (List)visit;
            res.add(new UserVisit(toInt(list.get(0)), toLong(list.get(1)), (String)list.get(2)));
        }
        return res;
    }

    private Map<String, Integer> createKeys() {
        Map<String, Integer> res = new HashMap<>();
        res.put("email", 1);
        res.put("first_name", 2);
        res.put("last_name", 3);
        res.put("gender", 4);
        res.put("birth_date", 5);
        return res;
    }

}
