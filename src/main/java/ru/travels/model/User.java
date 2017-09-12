package ru.travels.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.List;

@JsonSerialize
public class User {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private String gender;
    private Long birth_date;

    public User() {
    }

    public User(Integer id, String email, String first_name, String last_name, String gender, Long birth_date) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.birth_date = birth_date;
    }

    public List<Object> simpleList() {
        return Arrays.asList(id, email, first_name, last_name, gender, birth_date);
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public Long getBirth_date() {
        return birth_date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirth_date(Long birth_date) {
        this.birth_date = birth_date;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append(", first_name='").append(first_name).append('\'');
        sb.append(", last_name='").append(last_name).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", birth_date=").append(birth_date);
        sb.append('}');
        return sb.toString();
    }
}