package com.example.samuelfurr.architecturetours;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel Furr on 11/5/2016.
 */
public class ListCard {
    String buildingName;
    String info;
    Bitmap image;
    LatLng latLng;
    int idNumber = 0;
    static int idTracker = 0;
    public static List<ListCard> cardList = new ArrayList<>();

    public ListCard(String name, String i, Bitmap img)
    {
        buildingName = name;
        info = i;
        image = img;
        idNumber = idTracker;
        idTracker ++;
    }

    public void setLatLng(LatLng l)
    {
        latLng = l;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public String getBuildingName()
    {
        return buildingName;
    }

    public String getInfo()
    {
        return info;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public int getIdNumber()
    {
        return idNumber;
    }

    public void initializeData(ListCard listCard)
    {
        //keeps track of the ListCards - this is also necessary because the ListCard class can't create cards in CardList -
        cardList.add(listCard);
    }
}


