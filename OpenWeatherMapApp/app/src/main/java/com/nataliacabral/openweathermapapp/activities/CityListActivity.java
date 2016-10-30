package com.nataliacabral.openweathermapapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nataliacabral.openweathermapapp.R;
import com.nataliacabral.openweathermapapp.models.City;
import com.nataliacabral.openweathermapapp.views.CityAdapter;

import java.util.ArrayList;

/**
 * Created by nataliacabral on 10/27/16.
 */

public class CityListActivity extends AppCompatActivity {
    public static final String EXTRA_CITY_LIST = "com.nataliacabral.openweathermap.CITY_LIST";

    private ListView listView;
    private ArrayList<City> cities;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Intent intent = getIntent();
        cities = (ArrayList<City>) intent.getSerializableExtra(EXTRA_CITY_LIST);

        listView = (ListView) findViewById(R.id.listView);
        CityAdapter adapter = new CityAdapter(this, cities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentCityDetails = new Intent(CityListActivity.this, CityDetailsActivity.class);
                intentCityDetails.putExtra(CityDetailsActivity.EXTRA_CITY, cities.get(i));
                startActivity(intentCityDetails);
            }
        });

    }
}
