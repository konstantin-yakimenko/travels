package ru.travels.services;

import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.NotFoundException;
import ru.travels.model.User;
import ru.travels.model.UserVisit;
import ru.travels.model.UserVisits;
import ru.travels.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(Integer userId) {
        return userRepository.get(userId);
    }

    public void update(Integer userId, Map<String, Object> user) throws BadRequestException, NotFoundException {
        if (!userRepository.isExists(userId)) {
            throw new NotFoundException();
        }
        userRepository.update(userId, user);
    }

    public void add(Map<String, Object> user) throws BadRequestException {
        for (Object fieldValue : user.values()) {
            if (fieldValue == null) {
                throw new BadRequestException();
            }
        }
        try {
            userRepository.add(user);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    public UserVisits getVisits(Integer userId, Long fromDate, Long toDate, String country, Integer toDistance) throws BadRequestException, NotFoundException {
        try {
            if (!userRepository.isExists(userId)) {
                throw new NotFoundException();
            }
            List<UserVisit> visits = userRepository.getVisits(userId, fromDate, toDate, country, toDistance);
            if (visits == null || visits.isEmpty()) {
                return new UserVisits(new ArrayList<>());
            }
            return new UserVisits(
                    visits
                            .stream()
                            .sorted(Comparator.comparing(UserVisit::getVisited_at))
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw (NotFoundException)e;
            }
            e.printStackTrace();
            throw new BadRequestException();
        }
    }
}
