package com.example.shoxrux.mealorder.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoxrux.mealorder.Adapter.MenuRecyclerViewAdapter;
import com.example.shoxrux.mealorder.Model.Client;
import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoxrux on 12/13/18.
 */

public class Tab2Menu extends Fragment {
    private List<Menu> menus;
    private List<String> favorites;
    private Client user;
    private DatabaseReference databaseReference;

    private MenuRecyclerViewAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2menu, container, false);
        //Create empty Object of User
        user = new Client();
        //Create empty Array List of menus
        menus = new ArrayList<>();
        //Create empty Array List of favorites
        favorites = new ArrayList<>();
        //Get Current user ID
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Get Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Get current user info to Object and complete user interface with user information
        databaseReference.child("users").child(uID).addValueEventListener(new ClientValueEventListener());
        databaseReference.child("menus").addValueEventListener(new MenusChildEventListener());
        databaseReference.child("users").child(uID).child("favorites").addValueEventListener(new FavoritesChildEventListener());
        //Set up Recycler view
        RecyclerView mRecyclerView = rootView.findViewById(R.id.menu_recycle_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        //Create Custom Adapter for Recycler View, pass menus list, favorites list and user object to adapter
        mAdapter = new MenuRecyclerViewAdapter(rootView.getContext(), menus, favorites, user);
        //And set this adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
    //Listener for retrieving menus and if any change occurs it will clear the list and get all menus from Firebase again
    class MenusChildEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //If any change clear list
            menus.clear();
            //Iterate for all menus and push each to the menus list
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                Menu menu = dataSnapshot1.getValue(Menu.class);
                menu.setKey(dataSnapshot1.getKey());
                menus.add(0,menu);
            }
            //After getting menus, notify it to Adapter
            mAdapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    //Listener for retrieving favorites and if any change occurs it will clear the list and get all favorites again
    class FavoritesChildEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //If any change, clear list
            favorites.clear();
            //Iterate for this user favorites and push each to the favorites list
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                favorites.add(0,dataSnapshot1.getKey());
            }
            //After getting menus, notify it to Adapter
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    //Listener: Get current user and assign to user object
    class ClientValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //Get User from DB
            Client client = dataSnapshot.getValue(Client.class);
            //set key
            client.setKey(dataSnapshot.getKey());
            user.setKey(client.getKey());
            user.setEmail(client.getEmail());
            user.setFirstName(client.getFirstName());
            user.setLastName(client.getLastName());
            user.setImage(client.getImage());
            Log.e("Client", user.getEmail()+"");
            //and notify to adapter
            mAdapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }
}
