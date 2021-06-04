package edu.lysak.railwaytickets.model;

import java.util.Objects;

public class Station {
    private Long id;
    private String city;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return city.equals(station.city) && name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, name);
    }
}
