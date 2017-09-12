package ru.travels.services;

import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.NotFoundException;
import ru.travels.model.Visit;
import ru.travels.repositories.VisitRepository;

import java.util.Map;

public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit get(Integer visitId) {
        return visitRepository.get(visitId);
    }

    public void update(Integer visitId, Map<String, Object> visit) throws NotFoundException, BadRequestException {
        if (!visitRepository.isExists(visitId)) {
            throw new NotFoundException();
        }
        visitRepository.update(visitId, visit);
    }

    public void add(Map<String, Object> visit) throws BadRequestException {
        for (Object fieldValue : visit.values()) {
            if (fieldValue == null) {
                throw new BadRequestException();
            }
        }
        try {
            visitRepository.add(visit);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }
}
