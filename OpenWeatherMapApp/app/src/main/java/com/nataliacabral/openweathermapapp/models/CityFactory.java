package com.nataliacabral.openweathermapapp.models;

import com.nataliacabral.openweathermapapp.utils.OpenWeatherAPIUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nataliacabral on 10/27/16.
 */

public class CityFactory {

    public static City getCity(JSONObject json) throws JSONException {
        int id = Integer.parseInt(json.optString(OpenWeatherAPIUtils.JSON_ID_KEY).toString());
        String name = json.optString(OpenWeatherAPIUtils.JSON_NAME_KEY).toString();

        JSONObject mainNode = json.getJSONObject(OpenWeatherAPIUtils.JSON_TEMP_MAIN_KEY);
        double temp_min = Double.parseDouble(mainNode.optString(OpenWeatherAPIUtils.JSON_TEMP_MIN_KEY).toString());
        double temp_max = Double.parseDouble(mainNode.optString(OpenWeatherAPIUtils.JSON_TEMP_MAX_KEY).toString());

        JSONObject weatherObject = json.getJSONArray(OpenWeatherAPIUtils.JSON_TEMP_WEATHER_KEY).getJSONObject(0);
        String description = weatherObject.optString(OpenWeatherAPIUtils.JSON_TEMP_DESCRIPTION_KEY).toString();
        String icon = weatherObject.optString(OpenWeatherAPIUtils.JSON_TEMP_ICON_KEY).toString();

        City city = new City(id, name, description, temp_max, temp_min, icon);
        return city;
    }
}
