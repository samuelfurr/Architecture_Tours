package com.example.samuelfurr.architecturetours;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Samuel Furr on 11/4/2016.
 * pulls and parses the wikipdeia page for our current url
 */
public class PullViews extends AsyncTask<String, Void, ThreePieceView> {

    ThreePieceView threePieceView = null;
    ListCard listCard = null;

    protected ThreePieceView doInBackground(String... s)
    {


        return threePieceView;
    }

    @Override
    protected void onPostExecute(ThreePieceView result)
    {
        threePieceView.setViews(result);
        super.onPostExecute(result);
    }


}
