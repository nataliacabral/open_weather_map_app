package com.nataliacabral.openweathermapapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nataliacabral.openweathermapapp.R;
import com.nataliacabral.openweathermapapp.models.City;
import com.nataliacabral.openweathermapapp.tasks.DownloadImageTask;
import com.nataliacabral.openweathermapapp.utils.OpenWeatherAPIUtils;

import java.util.ArrayList;

/**
 * Created by nataliacabral on 10/29/16.
 */

public class CityAdapter extends ArrayAdapter<City>  {

    public CityAdapter(Context context, ArrayList<City> cities) {
        super(context, 0, cities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);

        String icon_url = OpenWeatherAPIUtils.getIconURL(city.getIconCode());
        new DownloadImageTask(imgIcon).execute(icon_url);
        tvName.setText(city.getName());
        return convertView;
    }

}
