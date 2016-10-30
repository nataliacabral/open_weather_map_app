package com.nataliacabral.openweathermapapp.models;

import java.io.Serializable;

/**
 * Created by nataliacabral on 10/27/16.
 */

public class City implements Serializable {
    private int id;
    private String name;
    private double minTemperature;
    private double maxTemperature;
    private String description;
    private String iconCode;

    public City(int id, String name, String description, double temp_max, double temp_min, String iconCode) {
        this.id = id;
        this.name = name;
        this.maxTemperature = temp_max;
        this.minTemperature = temp_min;
        this.description = description;
        this.iconCode = iconCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public String getIconCode() {
        return iconCode;
    }

    //FIXME: only to validate notifications
    public void setTemperature(double newTemperature) {
        this.maxTemperature = newTemperature;
        this.minTemperature = newTemperature;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((City)obj).getId();
    }

    public boolean weatherChanged(City updatedCity) {
        return !(this.maxTemperature == updatedCity.getMaxTemperature() && this.minTemperature == updatedCity.getMinTemperature());
    }

    public void updateWeather(City updatedCity) {
        this.maxTemperature = updatedCity.getMaxTemperature();
        this.minTemperature = updatedCity.getMinTemperature();
    }
}
