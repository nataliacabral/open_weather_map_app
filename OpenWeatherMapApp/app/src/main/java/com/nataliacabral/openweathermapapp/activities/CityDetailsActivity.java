package com.nataliacabral.openweathermapapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.nataliacabral.openweathermapapp.R;
import com.nataliacabral.openweathermapapp.models.City;
import com.nataliacabral.openweathermapapp.respositories.CitiesRepository;

/**
 * Created by nataliacabral on 10/28/16.
 */

public class CityDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_CITY = "com.nataliacabral.openweathermap.CITY";
    private static final String CELSIUS_DEGREE = (char) 0x00B0 + "C";

    private TextView tvName;
    private TextView tvMaxTemp;
    private TextView tvMinTemp;
    private TextView tvDescription;
    private Switch switchTrackChanges;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);

        tvName = (TextView) findViewById(R.id.tvName);
        tvMaxTemp = (TextView) findViewById(R.id.tvMaxTemp);
        tvMinTemp = (TextView) findViewById(R.id.tvMinTemp);
        tvDescription = (TextView) findViewById(R.id.tvDetails);
        switchTrackChanges = (Switch) findViewById(R.id.switchTrackChanges);
        final City city = (City) getIntent().getSerializableExtra(EXTRA_CITY);

        tvName.setText(city.getName());
        tvMaxTemp.setText(String.valueOf(city.getMaxTemperature()) + CELSIUS_DEGREE);
        tvMinTemp.setText(String.valueOf(city.getMinTemperature()) + CELSIUS_DEGREE);
        tvDescription.setText(city.getDescription());
        switchTrackChanges.setChecked(CitiesRepository.getInstance().contains(city));
        switchTrackChanges.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    //FIXME: only to validate the notification
                    city.setTemperature(city.getMaxTemperature() + 1);
                    CitiesRepository.getInstance().insert(city);
                } else {
                    CitiesRepository.getInstance().remove(city);

                }
            }
        });
    }
}
