package ru.travels.model;

import java.util.List;

public final class UserVisits {
    private final List<UserVisit> visits;

    public UserVisits(List<UserVisit> visits) {
        this.visits = visits;
    }

    public List<UserVisit> getVisits() {
        return visits;
    }
}
