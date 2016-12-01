package com.example.samuelfurr.architecturetours;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;

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
 * Created by samuelfurr on 11/4/16.
 */
public class PullImage extends AsyncTask<String, Void, Bitmap> {

    protected Bitmap doInBackground(String... s)
    {

        HttpURLConnection urlConnection = null;
        String image_url = "";

        Bitmap building_pic = null;

        try
        {
            URL url = new URL(BuildingCard.website);
            urlConnection = (HttpURLConnection) url.openConnection();


            int code = urlConnection.getResponseCode();


            if(code == 200)
            {
                InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
                if (inStream != null)
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

                    String line = "";
                    while ( (line = bufferedReader.readLine()) != null)
                    {
                        image_url += line;
                    }
                }
                inStream.close();
            }

            String html = image_url;

            Document doc = Jsoup.parseBodyFragment(html);
            Elements images = doc.select("img[src$=.jpg]");
            image_url = "https:" + images.get(0).attr("src");

            URL url2 = new URL(image_url);
            building_pic = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
            return building_pic;

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
        return building_pic;
    }



    @Override
    protected void onPostExecute(Bitmap result)
    {
        BuildingCard.iv.setImageBitmap(result);
        super.onPostExecute(result);
    }
}
