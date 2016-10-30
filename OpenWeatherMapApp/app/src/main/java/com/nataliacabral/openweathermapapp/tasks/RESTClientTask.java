package com.nataliacabral.openweathermapapp.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.nataliacabral.openweathermapapp.R;
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
        processDialog.setMessage("Loading cities ...");
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
            presentError();
        }
    }

    private void presentError(){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(activity.getString(R.string.error));
        alertDialog.setMessage(activity.getString(R.string.request_failed));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, activity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}