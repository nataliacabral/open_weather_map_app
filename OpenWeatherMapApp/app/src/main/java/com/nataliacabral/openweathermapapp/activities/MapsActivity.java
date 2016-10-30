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
import com.nataliacabral.openweathermapapp.utils.OpenWeatherAPIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener, OnTaskCompleted {
    private Button searchButton;
    private GoogleMap googleMap;
    private Marker selectedPointMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsActivity.this.googleMap = googleMap;
                googleMap.setOnMapLongClickListener(MapsActivity.this);
            }
        });

        searchButton = (Button) findViewById(R.id.btSearch);
        searchButton.setEnabled(false);
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

        startService(new Intent(MapsActivity.this, CheckCityWeatherService.class));
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
                presentError(getString(R.string.no_cities_returned));

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
            presentError(getString(R.string.json_parser_failed));
        }
    }
    private void presentError(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.error));
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

