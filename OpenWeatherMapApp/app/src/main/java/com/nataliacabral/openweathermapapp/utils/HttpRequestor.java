package com.nataliacabral.openweathermapapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by nataliacabral on 10/30/16.
 */

public class HttpRequestor {
    public static String process(String api_url) throws IOException {
        StringBuilder total = new StringBuilder();

        URL url = new URL(api_url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "text/json");
        InputStream in = urlConnection.getInputStream();
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        urlConnection.disconnect();

        return total.toString();
    }
}
