package com.example.samuelfurr.architecturetours;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Samuel Furr on 11/6/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListCard> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView info;
        ImageView imageView;
        CardView cview;
        static String name = "";

        public TextView getLabel()
        {
            return label;
        }

        // each data item is just a string in this case
        public LinearLayout cardView;
        public ViewHolder(View v) {
            super(v);
            cardView = (LinearLayout) v;
            label = (TextView) v.findViewById(R.id.title_text);
            imageView = (ImageView)v.findViewById(R.id.card_image);
            info = (TextView)v.findViewById(R.id.info_text);
            cview = (CardView)v.findViewById(R.id.card_view);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    BuildingCard.setID(ListCard.cardList.get(getAdapterPosition()).getIdNumber());
                    view.getContext().startActivity(new Intent(view.getContext(),BuildingCard.class));
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<ListCard> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.label.setText(mDataset.get(position).getBuildingName());
        holder.imageView.setImageBitmap(mDataset.get(position).getImage());
        holder.info.setLines(5);
        holder.info.setText(mDataset.get(position).getInfo());

        //int height = holder.label.getHeight() + holder.info.getHeight();
        //holder.cview.setMinimumHeight(height);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}