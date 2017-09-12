package ru.travels.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.List;

@JsonSerialize
public class Visit {
    private Integer id;
    private Integer location;
    private Integer user;
    private Long visited_at;
    private Integer mark;

    public Visit() {
    }

    public Visit(Integer id, Integer location, Integer user, Long visited_at, Integer mark) {
        this.id = id;
        this.location = location;
        this.user = user;
        this.visited_at = visited_at;
        this.mark = mark;
    }

    public List<Object> simpleList() {
        return Arrays.asList(id, location, user, visited_at, mark);
    }

    public Integer getId() {
        return id;
    }

    public Integer getLocation() {
        return location;
    }

    public Integer getUser() {
        return user;
    }

    public Long getVisited_at() {
        return visited_at;
    }

    public Integer getMark() {
        return mark;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public void setVisited_at(Long visited_at) {
        this.visited_at = visited_at;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Visit{");
        sb.append("id=").append(id);
        sb.append(", location=").append(location);
        sb.append(", user=").append(user);
        sb.append(", visited_at=").append(visited_at);
        sb.append(", mark=").append(mark);
        sb.append('}');
        return sb.toString();
    }
}