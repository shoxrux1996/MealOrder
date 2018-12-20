package com.example.shoxrux.mealorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
    private DatabaseReference databaseReference;

    private MenuRecyclerViewAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2menu, container, false);


        RecyclerView mRecyclerView = rootView.findViewById(R.id.menu_recycle_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        menus = new ArrayList<>();
        favorites = new ArrayList<>();
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("menus").addValueEventListener(new MenusChildEventListener());
        databaseReference.child("users").child(uID).child("favorites").addValueEventListener(new FavoritesChildEventListener());

        mAdapter = new MenuRecyclerViewAdapter(rootView.getContext(), menus, favorites, uID);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    class MenusChildEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            menus.clear();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                Menu menu = dataSnapshot1.getValue(Menu.class);
                menu.setKey(dataSnapshot1.getKey());
                menus.add(0,menu);
            }
            mAdapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    class FavoritesChildEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            favorites.clear();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                favorites.add(0,dataSnapshot1.getKey());
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
