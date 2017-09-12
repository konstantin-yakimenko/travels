package ru.travels.model;

public enum Entity {
    USERS("users"),
    LOCATIONS("locations"),
    VISITS("visits");

    private final String name;

    Entity(String name) {
        this.name = name;
    }

    public static Entity byName(String name) throws Exception {
        for (Entity entity: values()) {
            if (entity.name.equalsIgnoreCase(name)) {
                return entity;
            }
        }
        throw new IllegalArgumentException(String.format("Unknow file name %s", name));
    }
}
