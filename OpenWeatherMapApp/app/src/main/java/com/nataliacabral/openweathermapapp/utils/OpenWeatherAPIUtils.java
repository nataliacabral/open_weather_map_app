package com.nataliacabral.openweathermapapp.utils;

/**
 * Created by nataliacabral on 10/30/16.
 */

public class OpenWeatherAPIUtils {

    // API URL
    private static final String OPEN_WEATHER_API_BASE = "http://api.openweathermap.org/";
    private static final String OPEN_WEATHER_API_CITIES_ENDPOINT = OPEN_WEATHER_API_BASE + "data/2.5/find?units=metric";
    private static final String OPEN_WEATHER_API_CITY_ENDPOINT = OPEN_WEATHER_API_BASE + "data/2.5//weather?units=metric";
    private static final String OPEN_WEATHER_ID = "d5e5e7bf0036493556227d17d41219bd";
    private static final String OPEN_WEATHER_COUNT = "15";
    private static final String OPEN_WEATHER_ICON_ENDPOINT = OPEN_WEATHER_API_BASE + "img/w/";


    // RESPONSE JSON
    public static final String JSON_ID_KEY = "id";
    public static final String JSON_NAME_KEY = "name";
    public static final String JSON_TEMP_MAX_KEY = "temp_max";
    public static final String JSON_TEMP_MAIN_KEY = "main";
    public static final String JSON_TEMP_MIN_KEY = "temp_min";
    public static final String JSON_TEMP_WEATHER_KEY = "weather";
    public static final String JSON_TEMP_ICON_KEY = "icon";
    public static final String JSON_TEMP_DESCRIPTION_KEY = "description";

    public static String getCitiesURL(double latitude, double longitude) {
        String url = OPEN_WEATHER_API_CITIES_ENDPOINT + "&lat=" + latitude +"&lon=" + longitude + "&cnt=" + OPEN_WEATHER_COUNT + "&APPID=" + OPEN_WEATHER_ID;
        return url;
    }

    public static String getWeatherInfoURL(int cityID) {
        String url = OPEN_WEATHER_API_CITY_ENDPOINT + "&id=" + cityID + "&APPID=" + OPEN_WEATHER_ID;;
        return url;
    }

    public static String getIconURL(String iconIdentifier) {
        String url = OPEN_WEATHER_ICON_ENDPOINT + iconIdentifier + ".png";
        return url;
    }
}
