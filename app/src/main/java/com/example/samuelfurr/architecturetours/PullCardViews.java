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
 */
public class PullCardViews extends AsyncTask<String, Void, ListCard> {

    ListCard listCard = null;
    String website = "";
    /*
    public PullCardViews(RecyclerView rcv , RecyclerView.Adapter rva, RecyclerView.LayoutManager rvl)
    {

    }
    */

    public void setWebsite(String s)
    {
        website = s;
    }

    protected ListCard doInBackground(String... s)
    {
        HttpURLConnection urlConnection = null;
        String html = "";

        try
        {
            //Create new URL from our building card's website
            URL url = new URL(website);

            //open the url connection
            urlConnection = (HttpURLConnection) url.openConnection();

            //Get html response code to make sure the connection succeeded
            int code = urlConnection.getResponseCode();

            //Pull html into string
            if(code == 200)
            {
                InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
                if (inStream != null)
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

                    String line = "";
                    while ( (line = bufferedReader.readLine()) != null)
                    {
                        html += line;
                    }
                }
                inStream.close();
            }

            //Parse html into doc
            Document doc = Jsoup.parseBodyFragment(html);

            //Block to create text for the second text view
            String text = doc.title();
            String building_name = "";
            String arr[] = text.split("\\s+");
            for(int i = 0; i < arr.length - 2; i++)
            {
                building_name = building_name + arr[i] + " ";
            }

            Elements paragraphs = doc.select("p");
            String description = paragraphs.get(0).text();

            //Block to create bitmap for imageview
            Elements images = doc.select("img[src$=.jpg]");
            String image_url = "https:" + images.get(0).attr("src");

            URL url2 = new URL(image_url);
            Bitmap building_pic = BitmapFactory.decodeStream(url2.openConnection().getInputStream());

            listCard = new ListCard(building_name, description, building_pic);

            return listCard;
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

        return listCard;
    }

    @Override
    protected void onPostExecute(ListCard result)
    {
        listCard.initializeData(result);
        super.onPostExecute(result);
        CardList.mAdapter = new MyAdapter(ListCard.cardList);
        CardList.rv.setAdapter(CardList.mAdapter);
    }


}
