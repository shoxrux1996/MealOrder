package com.example.shoxrux.mealorder.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoxrux.mealorder.Adapter.AdminUserRecyclerViewAdapter;
import com.example.shoxrux.mealorder.Model.Client;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1AdminUsers extends Fragment {


    private AdminUserRecyclerViewAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<Client> clients;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_tab1_users, container, false);
        clients = new ArrayList<>();
        //Get Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Add listener to retrieve or change of the users
        databaseReference.child("users").addValueEventListener(new AdminUserValueEventListener());

        //Set up Recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.admin_users_recycle_view);
        //Set layout for recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        //Create Custom Adapter for Recycler View
        mAdapter = new AdminUserRecyclerViewAdapter(rootView.getContext(), clients);
        //And set this adapter to recycler view
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }
    //Listener for retrieving users and if any change occurs it will clear the list and get all users from Firebase again
    class AdminUserValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //If any change clear list
            clients.clear();
            //Iterate for all users and push each to the orders list
            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                Client client = dataSnapshot1.getValue(Client.class);
                client.setKey(dataSnapshot1.getKey());
                clients.add(0, client);
            }
            //After getting users, notify it to Adapter
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


}
