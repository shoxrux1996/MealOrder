package com.example.shoxrux.mealorder.Adapter;


import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.shoxrux.mealorder.Activity.MenuInfoActivity;
import com.example.shoxrux.mealorder.ItemClickListener;
import com.example.shoxrux.mealorder.Model.Client;
import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.jackandphantom.circularimageview.RoundedImage;
import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shoxrux on 12/14/18.
 */

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.MenuItemViewHolder> {
    private List<Menu> menus;
    private List<String> favorites;
    private Context context;
    private Client user;

    public MenuRecyclerViewAdapter(Context context, List<Menu> menus, List<String> favorites, Client client) {
        this.menus = menus;
        this.favorites = favorites;
        this.context = context;
        this.user = client;
    }
    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_recycler_view_item, parent,false);
        //Create Custom Item View holder
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuItemViewHolder holder,final int position) {
        //Call populate function to set UI of the holder
        holder.populate(menus.get(position));
        //If view holder clicks, we will create MenuInfoActivity for menu info
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Intent menuInfo = new Intent(context, MenuInfoActivity.class);
                menuInfo.putExtra("menuInfo", menus.get(position));
                context.startActivity(menuInfo);

            }
        });
        //Add or remove favorite from user
        holder.favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get menu from list by position
                Menu menu = menus.get(position);
                DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference();
                //If this menu in user favorites, remove it
                if(holder.checkKeyExists(menu.getKey())){
                    //Remove from Firebase Database
                    databaseReference.child("users").child(user.getKey()).child("favorites").child(menu.getKey()).removeValue();
                    databaseReference.child("menus").child(menu.getKey()).child("users").child(user.getKey()).removeValue();
                    //set Image Icon to White Star
                    holder.favoriteImageView.setButtonDrawable(R.drawable.star_white);

                }
                //Else add it to user favorites
                else{
                    //Add to Firebase Database
                    databaseReference.child("users").child(user.getKey()).child("favorites").child(menu.getKey()).setValue(menu);
                    databaseReference.child("menus").child(menu.getKey()).child("users").child(user.getKey()).setValue(true);
                    //set Image Icon to Blue Star
                    holder.favoriteImageView.setButtonDrawable(R.drawable.star_blue);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.menu_rounded_image)
        RoundedImage imageView;
        @BindView(R.id.menu_title)
        TextView titleView;
        @BindView(R.id.menu_price)
        TextView priceView;
        @BindView(R.id.menu_favorite_image)
        RadioButton favoriteImageView;

        private ItemClickListener itemClickListener;

        public MenuItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //bind the listener
            itemView.setOnClickListener(this);

        }
        //Set Item Click listener interface
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        // function to set UI of the holder
        void populate(Menu menu){
            Ion.with(context).load(menu.getImageURL()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            titleView.setText(menu.getTitle());
            priceView.setText(menu.getPrice()+" sum");
            if(checkKeyExists(menu.getKey()))
                favoriteImageView.setButtonDrawable(R.drawable.favorite_blue);

        }
        //Check whether this menu exists in user favorites
        public boolean checkKeyExists(String menuKey){
            for(String key: favorites){
                if(key.equals(menuKey)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.OnClick(view, getAdapterPosition());
        }
    }
}
