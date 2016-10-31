package com.nataliacabral.openweathermapapp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * AsyncTask to download image from a URL and present in a imageView.
 * It is needed to avoid downloading it in the UI thread.
 * Created by nataliacabral on 10/29/16.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String iconUrl = urls[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(iconUrl).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result)
    {
        if (result != null) {
            imageView.setImageBitmap(result);
        }
    }
}