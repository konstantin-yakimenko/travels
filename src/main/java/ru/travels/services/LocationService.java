package ru.travels.services;

import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.NotFoundException;
import ru.travels.model.Location;
import ru.travels.repositories.LocationRepository;

import java.util.Map;

public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location get(Integer locationId) {
        return locationRepository.get(locationId);
    }

    public void update(Integer locationId, Map<String, Object> location) throws NotFoundException, BadRequestException {
        if (!locationRepository.isExists(locationId)) {
            throw new NotFoundException();
        }
        locationRepository.update(locationId, location);
    }

    public void add(Map<String, Object> location) throws BadRequestException {
        for (Object fieldValue : location.values()) {
            if (fieldValue == null) {
                throw new BadRequestException();
            }
        }
        try {
            locationRepository.add(location);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    public Double getAvg(Integer locationId, Long fromDate, Long toDate, Long fromAge, Long toAge, String gender) throws NotFoundException, BadRequestException {
        try {
            if (!locationRepository.isExists(locationId)) {
                throw new NotFoundException();
            }
            return locationRepository.getAvgFromList(locationId, fromDate, toDate, fromAge, toAge, gender);
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw (BadRequestException)e;
            }
            throw new NotFoundException();
        }
    }

}
