package com.example.shoxrux.mealorder.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoxrux.mealorder.Activity.MainActivity;
import com.example.shoxrux.mealorder.Activity.MakeOrderActivity;
import com.example.shoxrux.mealorder.Model.Favorite;
import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.R;
import com.jackandphantom.circularimageview.RoundedImage;
import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shoxrux on 12/14/18.
 */

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteItemViewHolder> {
    private List<Favorite> favorites;
    private Context context;
    public static int MENU_ORDER;

    public FavoriteRecyclerViewAdapter(Context context, List<Favorite> favorites) {
        this.favorites = favorites;
        //Create Custom Item View holder
        this.context = context;

    }

    @Override
    public FavoriteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_recycler_view_item, parent,false);
        return new FavoriteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteItemViewHolder holder, final int position) {
        //Call populate function to set UI of the holder
        holder.populate(favorites.get(position));
        //If view holder order button clicks, we will create MakeOrderActivity for order
        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderIntent = new Intent(context, MakeOrderActivity.class);
                Menu menu = Favorite.castToMenu(favorites.get(position));
                orderIntent.putExtra("menuInfo", menu);
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.startActivityForResult(orderIntent, MENU_ORDER);
            }
        });
    }


    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class FavoriteItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.favorite_rounded_image)
        RoundedImage imageView;
        @BindView(R.id.favorite_title_price)
        TextView titleView;
        @BindView(R.id.favorite_description)
        TextView descriptionView;
        @BindView(R.id.favorite_image_button)
        FloatingActionButton orderButton;
        public FavoriteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void populate(Favorite favorite){
            //Load image from the server (internet) using Ion
            Ion.with(context).load(favorite.getImageURL()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            titleView.setText(favorite.getTitle()+" - "+ favorite.getPrice()+"$");
            descriptionView.setText(favorite.getDescription());

        }
    }
}
