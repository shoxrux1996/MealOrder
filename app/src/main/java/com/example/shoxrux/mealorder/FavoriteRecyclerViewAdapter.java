package com.example.shoxrux.mealorder;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteItemViewHolder> {
    private List<Favorite> favorites;
    private Context context;


    public FavoriteRecyclerViewAdapter(Context context) {
        this.favorites = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myQuery = databaseReference.child(uID).child("favorites");
        myQuery.addChildEventListener(new FavoritesChildEventListener());
        this.context = context;

    }

    class FavoritesChildEventListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Favorite favorite = dataSnapshot.getValue(Favorite.class);
            favorite.setKey(dataSnapshot.getKey());
            favorites.add(0,favorite);
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Favorite favorite = dataSnapshot.getValue(Favorite.class);
            favorite.setKey(dataSnapshot.getKey());
            for(int i = 0; i< favorites.size(); i++){
                if(favorites.get(i).getKey() == favorite.getKey())
                    favorites.remove(i);
            }
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
    public FavoriteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_recycler_view_item, parent,false);
        return new FavoriteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteItemViewHolder holder, int position) {
        holder.populate(favorites.get(position));
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

        public FavoriteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void populate(Favorite favorite){
            //Load image from the server (internet) using Ion
            Ion.with(context).load(favorite.getImageURL()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            titleView.setText(favorite.getTitle()+" - "+ favorite.getPrice()+"$");

        }
    }
}
