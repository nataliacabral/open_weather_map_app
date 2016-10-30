package com.nataliacabral.openweathermapapp.respositories;

import com.nataliacabral.openweathermapapp.models.City;

import java.util.ArrayList;

/**
 * Created by nataliacabral on 10/30/16.
 * Singleton class. Used as repository. Does not uses Database, saves object in memory.
 */
public class CitiesRepository {

    private ArrayList<City> cities;
    private static CitiesRepository instance;

    private CitiesRepository() {
        this.cities = new ArrayList<City>();
    }

    public static CitiesRepository getInstance() {
        if (instance == null) {
            instance = new CitiesRepository();
        }

        return instance;
    }

    public void insert(City city) {
        if (this.cities == null) {
            this.cities = new ArrayList<>();
        }

        this.cities.add(city);
    }

    public City getCityByName(String name) {
        City result = null;
        for (City city : cities) {
            if (city.getName().trim().equalsIgnoreCase(name)) {
                result = city;
                break;
            }
        }

        return result;
    }

    public City getCityById(int id) {
        City result = null;
        for (City city : cities) {
            if (city.getId() == id) {
                result = city;
                break;
            }
        }

        return result;
    }

    public void remove(City city){
        cities.remove(city);
    }

    public boolean contains(City city) {
        return cities.contains(city);
    }

    public ArrayList<City> getCities() {
        return this.cities;
    }
}
