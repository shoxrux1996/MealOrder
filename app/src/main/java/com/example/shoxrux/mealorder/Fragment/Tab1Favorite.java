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


/**
 * Created by shoxrux on 12/13/18.
 */

public class Tab1Favorite extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Favorite> favorites;

    private String uID;
    private DatabaseReference databaseReference;
    private  FavoriteRecyclerViewAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1favorite, container, false);
        mRecyclerView = rootView.findViewById(R.id.favorite_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        favorites = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        FirebaseDatabase.getInstance().getReference().child("menus").addChildEventListener(new MenuValueEventListener());
        Query myQuery = databaseReference.child(uID).child("favorites");
        myQuery.addValueEventListener(new FavoritesValueEventListener());

        mAdapter = new FavoriteRecyclerViewAdapter(rootView.getContext(), favorites);

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
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    class MenuValueEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Menu menu =dataSnapshot.getValue(Menu.class);
            for(int i=0;i<favorites.size(); i++) {
                if(favorites.get(i).getKey().equals(menu.getKey())){
                    databaseReference.child(uID).child("favorites").child(dataSnapshot.getKey()).setValue(menu);
                    System.out.println(dataSnapshot.getKey()+ "changed");
                }
            }

        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            for(int i=0;i<favorites.size(); i++) {
                if(favorites.get(i).getKey().equals(dataSnapshot.getKey())){
                    databaseReference.child(uID).child("favorites").child(dataSnapshot.getKey()).removeValue();
                    System.out.println(dataSnapshot.getKey()+ "removed");
                }
            }
        }
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }

}
