package ru.travels.repositories;

import org.restexpress.exception.BadRequestException;
import ru.travels.model.Visit;
import ru.travels.repositories.load.LoadVisits;

import java.util.*;

import static ru.travels.utils.Utils.toInt;
import static ru.travels.utils.Utils.toLong;

public class VisitRepository {

    private final Tarantool tarantool;

    private final Map<String, Integer> keys = createKeys();

    private static final Set<Integer> addedVisitId = new HashSet<>();

    public VisitRepository(Tarantool tarantool) {
        this.tarantool = tarantool;
    }

    public Visit get(Integer visitId) {
        List<?> visit = tarantool.getClient().syncOps().select(
                Tarantool.SPACE_VISIT,
                0,
                Arrays.asList(visitId),
                0, 1024, 0);
        if (visit == null
                || visit.size() == 0
                || visit.get(0) == null) {
            return null;
        }
        ArrayList<?> visits = (ArrayList<?>) visit.get(0);
        return new Visit(
                toInt(visits.get(0)),    // id
                toInt(visits.get(1)),    // location
                toInt(visits.get(2)),    // user
                toLong(visits.get(3)),
                (Integer) visits.get(4));   // mark
    }

    public boolean isExists(Integer visitId) {
        List<?> visit = tarantool.getClient().syncOps().select(
                Tarantool.SPACE_VISIT,
                0,
                Arrays.asList(visitId),
                0, 1024, 0);
        if (visit == null
                || visit.size() == 0
                || visit.get(0) == null) {
            return false;
        }
        ArrayList<?> visits = (ArrayList<?>) visit.get(0);
        return visits.get(0) != null;
    }

    public void update(Integer visitId, Map<String, Object> visit) throws BadRequestException {
        List<List<?>> op = new ArrayList<>();
        for (Map.Entry<String, Object> field : visit.entrySet()) {
            if (field.getValue() == null) {
                throw new BadRequestException();
            }
            op.add(Arrays.asList("=", keys.get(field.getKey()), field.getValue()));
        }
        if (op.size() == 0) {
            return;
        }
        tarantool.getClient().asyncOps().update(Tarantool.SPACE_VISIT,
                Arrays.asList(visitId),
                op.toArray());
    }

    public void add(Map<String, Object> v) throws Exception {
        tarantool.getClient().asyncOps().insert(Tarantool.SPACE_VISIT,
                Arrays.asList(
                        v.get("id"),
                        v.get("location"),
                        v.get("user"),
                        v.get("visited_at"),
                        v.get("mark")));
    }

    public void add(Visit visit) throws Exception {
        tarantool.getClient().asyncOps().insert(Tarantool.SPACE_VISIT, visit.simpleList());
    }

    public void addBatch(LoadVisits visits) {
        for (Visit visit : visits.getVisits()) {
            try {
                add(visit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Integer> createKeys() {
        Map<String, Integer> res = new HashMap<>();
        res.put("location", 1);
        res.put("user", 2);
        res.put("visited_at", 3);
        res.put("mark", 4);
        return res;
    }

}
