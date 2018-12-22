package com.example.shoxrux.mealorder.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoxrux.mealorder.Adapter.FavoriteRecyclerViewAdapter;
import com.example.shoxrux.mealorder.Model.Favorite;
import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Tab1Favorite extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Favorite> favorites;

    private String uID;
    private DatabaseReference databaseReference;
    private  FavoriteRecyclerViewAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1favorite, container, false);

        //Set up Recycler view
        mRecyclerView = rootView.findViewById(R.id.favorite_recycle_view);
        //Set layout for recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        favorites = new ArrayList<>();
        //Get Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //Get current user id
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myQuery = databaseReference.child(uID).child("favorites");
        //Add listener to retrieve or change of this user orders
        myQuery.addValueEventListener(new FavoritesValueEventListener());
        //Create Custom Adapter for Recycler View, pass favorites list to adapter
        mAdapter = new FavoriteRecyclerViewAdapter(rootView.getContext(), favorites);
        //And set this adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    class FavoritesValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //clearing all previous favorites list
            favorites.clear();
            //iterating through all the nodes
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                //getting favorite
                Favorite favorite = dataSnapshot1.getValue(Favorite.class);
                favorite.setKey(dataSnapshot1.getKey());
                favorites.add(favorite);

            }
            //After iteration, notify it to Adapter
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
