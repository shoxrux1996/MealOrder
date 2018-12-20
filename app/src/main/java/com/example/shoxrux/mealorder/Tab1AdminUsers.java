package com.example.shoxrux.mealorder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addValueEventListener(new AdminUserValueEventListener());

        RecyclerView recyclerView = rootView.findViewById(R.id.admin_users_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mAdapter = new AdminUserRecyclerViewAdapter(rootView.getContext(), clients);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }
    class AdminUserValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            clients.clear();
            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                Client client = dataSnapshot1.getValue(Client.class);
                client.setKey(dataSnapshot1.getKey());
                clients.add(0, client);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


}
