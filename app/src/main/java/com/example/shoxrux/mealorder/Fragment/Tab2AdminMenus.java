package com.example.shoxrux.mealorder.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoxrux.mealorder.Activity.AdminActivity;
import com.example.shoxrux.mealorder.Activity.AdminMenuCreateActivity;
import com.example.shoxrux.mealorder.Adapter.AdminMenuRecyclerViewAdapter;
import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2AdminMenus extends Fragment {

    private AdminMenuRecyclerViewAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<Menu> menus;

    @BindView(R.id.admin_menu_add_button)
    public FloatingActionButton addButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.admin_tab2_menus, container, false);
        ButterKnife.bind(this, rootView);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminActivity adminActivity = (AdminActivity)rootView.getContext();
                Intent createIntent = new Intent(adminActivity, AdminMenuCreateActivity.class);
                adminActivity.startActivityForResult(createIntent, AdminActivity.MENU_CREATE);
            }
        });
        //Create empty Array List of menus
        menus = new ArrayList<>();
        //Get Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Add listener to retrieve or catch any change of menus
        databaseReference.child("menus").addValueEventListener(new AdminMenuValueEventListener());
        //Set up Recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.admin_menus_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        //Create Custom Adapter for Recycler View, pass menus list to adapter
        mAdapter = new AdminMenuRecyclerViewAdapter(rootView.getContext(), menus);
        //And set this adapter to recycler view
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    //Listener for retrieving menus and if any change occurs it will clear the list and get all menus from Firebase again
    class AdminMenuValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //If any change clear list
            menus.clear();
            //Iterate for all menus and push each to the menus list
            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                Menu menu = dataSnapshot1.getValue(Menu.class);
                menu.setKey(dataSnapshot1.getKey());
                menus.add(0, menu);
            }
            //After getting menus, notify it to Adapter
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
