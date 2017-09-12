package ru.travels.model;

public final class UserVisit {
    private final Integer mark;
    private final Long visited_at;
    private final String place;

    public UserVisit(Integer mark, Long visited_at, String place) {
        this.mark = mark;
        this.visited_at = visited_at;
        this.place = place;
    }

    public Integer getMark() {
        return mark;
    }

    public Long getVisited_at() {
        return visited_at;
    }

    public String getPlace() {
        return place;
    }
}