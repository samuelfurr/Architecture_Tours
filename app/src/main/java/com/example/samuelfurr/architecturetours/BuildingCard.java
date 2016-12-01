package com.example.samuelfurr.architecturetours;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class BuildingCard extends AppCompatActivity {

    //we need to create our text and image views here, as static variables so that we can set them with the other classes
    public static TextView tx;
    public static TextView tx2;
    public static ImageView iv;
    public static String name = "";
    public static LatLng buildingLatLng;
    public static int id;

    public static String website = "";
    public static void setID(int i)
    {
        id = i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_card);

        //assign values to our views
        tx = (TextView)findViewById(R.id.building_info);
        tx2 = (TextView)findViewById(R.id.building_name);
        iv = (ImageView)findViewById(R.id.building_view);

        //set the typeface for our body to roboto slab, because its pretty
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/RobotoSlab-Light.ttf");
        tx.setTypeface(custom_font);

        //create and execute a pullviews object in order to get and parse our wikipedia info
        //PullViews pv = new PullViews();
        //pv.execute();

        ListCard listCard = ListCard.cardList.get(BuildingCard.id); //gets the ListCard that the BuildingCard corresponds to
        buildingLatLng = listCard.getLatLng(); //okay, using a static variable is janky - but this is how we are going to get the location of the building on a map later
        name = listCard.getBuildingName(); //see above
        String description = listCard.getInfo();
        description = "\n" + "     " + description + "\n\n" + "Source: Wikipedia.org" + "\n\n";
        //This is what sets the views for our BuildingCard
        ThreePieceView threePieceView = new ThreePieceView(description, listCard.getBuildingName(), listCard.getImage());
        threePieceView.setViews(threePieceView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //This fab will start a new map fragment activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(),Building_Map.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_building_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public String getName(){return name;}

    public LatLng getBuildingLatLng() {return buildingLatLng;}
}
