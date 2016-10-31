package com.nataliacabral.openweathermapapp.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.nataliacabral.openweathermapapp.R;
import com.nataliacabral.openweathermapapp.utils.DialogUtils;
import com.nataliacabral.openweathermapapp.utils.HttpRequestor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by nataliacabral on 10/26/16.
 * Task to perform a GET in a endpoint and return it JSON.
 * It is generic to make it reusable.
 */

public class RESTClientTask extends AsyncTask<String, Object, String> {
    private Activity activity;
    private ProgressDialog processDialog;

    public RESTClientTask(Activity activity) {
        this.activity = activity;
    }

    protected String doInBackground(String... urls) {
        String result = null;
        try {
            result = HttpRequestor.process(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        processDialog = new ProgressDialog((Context) activity);
        processDialog.setMessage(activity.getString(R.string.loading_data));
        processDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        processDialog.dismiss();
        if (result != null) {
            if (activity instanceof OnTaskCompleted) {
                ((OnTaskCompleted) activity).onTaskCompleted(result);
            }
        } else {
            DialogUtils.presentError(activity, activity.getString(R.string.request_failed));
        }
    }
}