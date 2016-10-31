package com.nataliacabral.openweathermapapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nataliacabral.openweathermapapp.R;
import com.nataliacabral.openweathermapapp.models.City;
import com.nataliacabral.openweathermapapp.models.CityFactory;
import com.nataliacabral.openweathermapapp.respositories.CitiesRepository;
import com.nataliacabral.openweathermapapp.services.CheckCityWeatherService;
import com.nataliacabral.openweathermapapp.tasks.OnTaskCompleted;
import com.nataliacabral.openweathermapapp.tasks.RESTClientTask;
import com.nataliacabral.openweathermapapp.utils.DialogUtils;
import com.nataliacabral.openweathermapapp.utils.OpenWeatherAPIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nataliacabral on 10/30/16.
 * Launcher acitity taht presents the map to the user.
 * The user can long click in any point to add a marker and search for cities close to that point.
 */


public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener, OnTaskCompleted {
    private Button searchButton;
    private GoogleMap googleMap;
    private Marker selectedPointMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Present instructions
        Toast.makeText(this, getString(R.string.instructions), Toast.LENGTH_SHORT).show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsActivity.this.googleMap = googleMap;
                googleMap.setOnMapLongClickListener(MapsActivity.this);
            }
        });

        searchButton = (Button) findViewById(R.id.btSearch);
        searchButton.setEnabled(false); // button is only enabled when one marker is added
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng position = selectedPointMarker.getPosition();
                double latitude = position.latitude;
                double longitude = position.longitude;
                String url = OpenWeatherAPIUtils.getCitiesURL(latitude, longitude);
                new RESTClientTask(MapsActivity.this).execute(url);
            }
        });

        // Start service to track for changes in selected cities
        startService(new Intent(MapsActivity.this, CheckCityWeatherService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MapsActivity.this, CheckCityWeatherService.class));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        searchButton.setEnabled(true);

        if (selectedPointMarker != null) {
            selectedPointMarker.remove();
        }
        selectedPointMarker = googleMap.addMarker(new MarkerOptions().position(latLng));
    }
    @Override
    public void onTaskCompleted(String json) {
        JSONObject jsonObject = null;
        try {
            ArrayList<City> cities = new ArrayList<City>();

            JSONArray jsonList = new JSONObject(json).optJSONArray("list");

            if (jsonList.length() == 0) {
                DialogUtils.presentError(this, getString(R.string.no_cities_returned));

            } else {
                for (int i = 0; i < jsonList.length(); i++) {
                    JSONObject jsonChildNode = jsonList.getJSONObject(i);
                    cities.add(CityFactory.getCity(jsonChildNode));
                }

                Intent intent = new Intent(MapsActivity.this, CityListActivity.class);
                intent.putExtra(CityListActivity.EXTRA_CITY_LIST, cities);
                MapsActivity.this.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogUtils.presentError(this, getString(R.string.json_parser_failed));
        }
    }

}

