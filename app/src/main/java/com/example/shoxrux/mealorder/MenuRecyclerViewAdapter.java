package com.example.shoxrux.mealorder;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jackandphantom.circularimageview.RoundedImage;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
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
    private DatabaseReference databaseReference;
    private String uID;


    public MenuRecyclerViewAdapter(Context context) {
        this.menus = new ArrayList<>();
        this.favorites = new ArrayList<>();
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("menus").addChildEventListener(new MenusChildEventListener());
        databaseReference.child("users").child(uID).child("favorites").addChildEventListener(new FavoritesChildEventListener());
        this.context = context;

    }

    class MenusChildEventListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Menu menu = dataSnapshot.getValue(Menu.class);
            menu.setKey(dataSnapshot.getKey());
            menus.add(0,menu);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    class FavoritesChildEventListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            favorites.add(0,dataSnapshot.getKey());
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            favorites.remove(dataSnapshot.getKey());
            notifyDataSetChanged();

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }
    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_recycler_view_item, parent,false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuItemViewHolder holder,final int position) {
        holder.populate(menus.get(position));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Intent menuInfo = new Intent(context, MenuInfoActivity.class);
                menuInfo.putExtra("menuInfo", menus.get(position));
                context.startActivity(menuInfo);

            }
        });

        holder.favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = menus.get(position);
                if(holder.checkKeyExists(menu.getKey())){
                    databaseReference.child("users").child(uID).child("favorites").child(menu.getKey()).removeValue();
                    holder.favoriteImageView.setButtonDrawable(R.drawable.favorite);

                }else{
                    databaseReference.child("users").child(uID).child("favorites").child(menu.getKey()).setValue(menu);
                    holder.favoriteImageView.setButtonDrawable(R.drawable.favorite_blue);
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

            itemView.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        void populate(Menu menu){
            //Load image from the server (internet) using Ion
            Ion.with(context).load(menu.getImageURL()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            titleView.setText(menu.getTitle());
            priceView.setText(menu.getPrice()+" sum");
            if(checkKeyExists(menu.getKey()))
                favoriteImageView.setButtonDrawable(R.drawable.favorite_blue);

        }
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
