package ru.travels.repositories.load;

import ru.travels.model.Location;

import java.util.List;

public class LoadLocations {
    private List<Location> locations;

    public LoadLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}