package com.example.samuelfurr.architecturetours;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Samuel Furr on 11/3/2016.
 */
public class PullTitle extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... s)
    {

        HttpURLConnection urlConnection = null;
        String building_name = "";

        try
        {
            URL url = new URL(BuildingCard.website);
            urlConnection = (HttpURLConnection) url.openConnection();


            int code = urlConnection.getResponseCode();
            String text = "";

            if(code == 200)
            {
                InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
                if (inStream != null)
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

                    String line = "";
                    while ( (line = bufferedReader.readLine()) != null)
                    {
                        text += line;
                    }
                }
                inStream.close();
            }

            String html = text;

            Document doc = Jsoup.parseBodyFragment(html);
            text = doc.title();

            String arr[] = text.split("\\s+");
            for(int i = 0; i < arr.length - 2; i++)
            {
                building_name = building_name + arr[i] + " ";
            }


        }
        catch (MalformedURLException mue)
        {
            mue.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return building_name;
    }

    @Override
    protected void onPostExecute(String result)
    {
        BuildingCard.tx2.setText(result);
        super.onPostExecute(result);
    }



}
