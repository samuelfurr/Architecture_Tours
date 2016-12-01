package com.example.samuelfurr.architecturetours;

import android.graphics.Bitmap;
import android.widget.TextView;

/**
 * Created by samuelfurr on 11/4/16.
 *This class is necessary so that our pullviews class can change three different views at once.  The previous code used three different async classes, one for each view, which of course took far longer than necessary
 */
public class ThreePieceView{

    String tx;
    String tx2;
    Bitmap bm;

    public ThreePieceView(String t1, String t2, Bitmap b)
    {
        tx = t1;
        tx2 = t2;
        bm = b;
    }

    public void setViews(ThreePieceView tpv)
    {
        BuildingCard.tx.setText(tx);
        BuildingCard.tx2.setText(tx2);
        BuildingCard.iv.setImageBitmap(bm);
    }

}
