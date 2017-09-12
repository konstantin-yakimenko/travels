package ru.travels.repositories.load;

import ru.travels.model.Visit;

import java.util.List;

public class LoadVisits {
    private List<Visit> visits;

    public LoadVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}