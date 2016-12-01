/**
 * This is the real code behind the app - PullNearbyPlaces pulls the places of architectural interest in the user's city
 * (and neighborhood if the city is big enough to have one).
 *
 * The app then checks if each place of interest is on Wikipedia
 *
 * If the place of interest is on Wikipedia then the app pulls the photo of the place and its name (from Google),
 * as well as the place's description (from Wikipedia), it also pulls its latitude and longitude from Google
 *
 * The place's are then added to an array list, and a ListCard is created in the CardList for each place
 */

package com.example.samuelfurr.architecturetours;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;
/**
 * Created by Samuel Furr on 11/27/2016.
 */

public class PullNearbyPlaces extends  AsyncTask<String, Void, ArrayList<ListCard> >{

    private ArrayList<String> nameArray = new ArrayList<>(); //List of Names
    private ArrayList<String> descriptionArray = new ArrayList<>(); //List of Descriptions
    private ArrayList<Bitmap> photoArray = new ArrayList<>(); //List of Photos
    private ArrayList<ListCard> listCardList = new ArrayList<>(); //List of ListCards
    private ArrayList<LatLng> latLngs = new ArrayList<>(); //List of LatLng objects

        protected ArrayList<ListCard> doInBackground(String... s)
        {
            String u = CardList.PLACES_SEARCH_URL; //URL for places to search
            String vicinity = "";
            HttpURLConnection urlConnection;
            try
            {
                URL url = new URL(u);
                urlConnection = (HttpURLConnection) url.openConnection();

                //Get html response code to make sure the connection succeeded
                int code = urlConnection.getResponseCode();

                //Pull html into string
                if(code == 200)
                {
                    InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
                    if (inStream != null)
                    {
                        BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream, "utf-8"), 8);
                        StringBuilder sBuilder = new StringBuilder();

                        String line;
                        while ((line = bReader.readLine()) != null) {
                            sBuilder.append(line + "\n");
                        }

                        inStream.close();
                        vicinity = sBuilder.toString();
                    }
                    inStream.close();
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

            String buildings = "";

            try
            {
                JSONObject urlObject = new JSONObject(vicinity); //Creates a new JSONObject from the string that our google Place query returned
                JSONArray vicinityArray = urlObject.getJSONArray("results"); //Pulls the results from the JSON object - these should be the city and neighboorhod the user is in
                String query = "";
                for(int i = vicinityArray.length() -1; i >=0; i-=1)
                {
                    //This loop pulls the city and neighboorhood names and adds them to a string in reverse order
                    //This is important to do because a places text search for "famous buildings chicago south side" gives you nothing,
                    //but a search for "famous buildings south side chicago" yeilds results
                    JSONObject vicinityObject = vicinityArray.getJSONObject(i);
                    String name = vicinityObject.getString("name");
                    name = name.replaceAll(" ", "+");
                    name = name + "+";
                    query = query + name;
                }
                query = query.substring(0, query.length() -1);
                //This URL asks google places for famous buildings in the user's neighboorhood and city, preferably in a 1000m radius - though super important results outside the radius WILL be included
                String buildings_url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=famous+buildings+" + query + "&radius=1000&key=AIzaSyCZthYKl5KHAFezBEhB2luPuoqCzjNFz6A";

                URL url = new URL(buildings_url);
                HttpURLConnection building_connection = (HttpURLConnection)url.openConnection();

                int code = building_connection.getResponseCode();

                if(code == 200)
                {
                    InputStream inStream = new BufferedInputStream(building_connection.getInputStream());
                    if (inStream != null)
                    {
                        BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream, "utf-8"), 8);
                        StringBuilder sBuilder = new StringBuilder();

                        String line;
                        while ((line = bReader.readLine()) != null) {
                            sBuilder.append(line + "\n");
                        }

                        inStream.close();
                        buildings = sBuilder.toString();
                    }
                    inStream.close();
                }
            }
            catch (JSONException joe)
            {
                joe.printStackTrace();
            }
            catch (MalformedURLException moe)
            {
                moe.printStackTrace();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }

            try{
                //Same formatting of the JSON String as before
                JSONObject urlObject = new JSONObject(buildings);
                JSONArray jsonArray = urlObject.getJSONArray("results");

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String name = jsonObject.getString("name"); //This makes sure that we don't waste a LOT of time and data asking for info about buildings that we already have cards for
                    if(!(CardList.listed.contains(name))) {
                        CardList.listed.add(name);
                        name = name.replaceAll(" ", "_");

                        //pulls the photo reference string for the building from the array - this string is how we get the photo for the building
                        JSONArray photos = jsonObject.getJSONArray("photos");
                        JSONObject jsonPhoto = photos.getJSONObject(0);
                        String photoreference = jsonPhoto.getString("photo_reference");

                        try {
                            URL wiki_url = new URL("https://en.wikipedia.org/wiki/" + name);
                            HttpURLConnection wiki_urlConnection = (HttpURLConnection) wiki_url.openConnection();
                            int wiki_code = wiki_urlConnection.getResponseCode();

                            if (wiki_code == 200) { //This ensures that we only spend time requesting data for buildings that actually have wikipedia pages

                            String html = "";

                            InputStream inStream = new BufferedInputStream(wiki_urlConnection.getInputStream());
                            if (inStream != null) {
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

                                String line;
                                while ((line = bufferedReader.readLine()) != null) {
                                    html += line;
                                }
                            }
                            inStream.close();

                            //Parse html into doc
                            Document doc = Jsoup.parseBodyFragment(html);

                            //Block to create text for the second text view
                            String text = doc.title();
                            String building_name = "";
                            String arr[] = text.split("\\s+");
                            for (int x = 0; x < arr.length - 2; x++) {
                                building_name = building_name + arr[x] + " ";
                            }

                            nameArray.add(building_name);

                            Elements paragraphs = doc.select("p");
                            String description = paragraphs.get(0).text();

                            descriptionArray.add(description);

                                String geometry = jsonObject.getString("geometry"); //gets the location of the building
                                String latString = geometry.substring(geometry.indexOf("\"lat\":") + 6, geometry.indexOf(","));
                                String lngString = geometry.substring(geometry.indexOf("\"lng\":") + 6, geometry.indexOf("},"));
                                Double lat = Double.valueOf(latString);
                                Double lng = Double.valueOf(lngString);


                                latLngs.add(new LatLng(lat,lng));

                                //This pulls our photo from google places
                                URL places_url = new URL("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference=" + photoreference + "&key=AIzaSyCZthYKl5KHAFezBEhB2luPuoqCzjNFz6A");
                                HttpURLConnection places_urlConnection = (HttpURLConnection) places_url.openConnection();
                                int places_code = places_urlConnection.getResponseCode();

                                if (places_code == 200) {
                                    Bitmap building_pic = BitmapFactory.decodeStream(places_urlConnection.getInputStream());
                                    photoArray.add(building_pic);
                                }
                            }
                        } catch (MalformedURLException moe) {
                            moe.printStackTrace();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }
            }
            catch(JSONException joe)
            {
                joe.printStackTrace();
            }
            for(int z = 0; z < nameArray.size(); z ++) //creates the array of ListCard objects
            {
                listCardList.add(new ListCard(nameArray.get(z), descriptionArray.get(z), photoArray.get(z)));
                listCardList.get(z).setLatLng(latLngs.get(z)); //the constructor doesn't have a field for a LatLng... because I was to lazy to re-write it
            }
            return listCardList;
        }

        protected void onPostExecute(ArrayList<ListCard> result)
        {
            super.onPostExecute(result);
            for(ListCard l : result)
            {
                l.initializeData(l); //Adds a new ListCard to a Static ArrayList  - the explanation for why this is necessary is in ListCard.class
                CardList.mAdapter = new MyAdapter(ListCard.cardList); //this is the part that actually creates a ListCard in the CardList
                CardList.rv.setAdapter(CardList.mAdapter);
            }
        }
}
