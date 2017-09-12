package ru.travels.repositories.load;

import ru.travels.model.User;

import java.util.List;

public class LoadUsers {
    private List<User> users;

    public LoadUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}