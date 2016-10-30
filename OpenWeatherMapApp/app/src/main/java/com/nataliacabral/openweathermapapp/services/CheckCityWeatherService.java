package com.nataliacabral.openweathermapapp.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nataliacabral.openweathermapapp.R;
import com.nataliacabral.openweathermapapp.activities.CityDetailsActivity;
import com.nataliacabral.openweathermapapp.models.City;
import com.nataliacabral.openweathermapapp.models.CityFactory;
import com.nataliacabral.openweathermapapp.respositories.CitiesRepository;
import com.nataliacabral.openweathermapapp.utils.HttpRequestor;
import com.nataliacabral.openweathermapapp.utils.OpenWeatherAPIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nataliacabral on 10/30/16.
 */

public class CheckCityWeatherService extends Service {
    public Handler handler = null;
    public static Runnable runnable = null;
    private static int INTERVAL = 2000;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper());
        runnable = new Runnable() {
            public void run() {
                checkCities();
                handler.postDelayed(runnable, INTERVAL);
            }
        };

        handler.postDelayed(runnable, INTERVAL);

    }

    private void checkCities() {
        Log.i("SERVICE", "Checking cities" );
        ArrayList<City> cities = CitiesRepository.getInstance().getCities();
        for (int i = 0 ; i < cities.size() ; i++) {
            City city = cities.get(i);

            String url = OpenWeatherAPIUtils.getWeatherInfoURL(city.getId());

            try {
                String json = HttpRequestor.process(url);
                City updatedCity = CityFactory.getCity(new JSONObject(json));

                if (city.weatherChanged(updatedCity)) {
                    city.updateWeather(updatedCity);
                    sendNotification(city);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendNotification(City city){
        Log.i("SERVICE", "Send notification: " + city.getName() );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(city.getName())
                        .setSmallIcon(R.drawable.icon)
                        .setContentText(getString(R.string.weather_changed));

        Intent resultIntent = new Intent(this, CityDetailsActivity.class);
        resultIntent.putExtra(CityDetailsActivity.EXTRA_CITY, city);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
