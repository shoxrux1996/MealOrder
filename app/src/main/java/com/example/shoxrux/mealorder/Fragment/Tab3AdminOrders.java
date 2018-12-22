package com.example.shoxrux.mealorder.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoxrux.mealorder.Adapter.AdminOrderRecyclerViewAdapter;
import com.example.shoxrux.mealorder.Model.Order;
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
public class Tab3AdminOrders extends Fragment {
    private RecyclerView mRecyclerView;
    private AdminOrderRecyclerViewAdapter mAdapter;
    private List<Order> orders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_tab3_orders, container, false);
        //Set up Recycler view
        mRecyclerView = rootView.findViewById(R.id.order_recycler_view);
        //Set layout for recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        orders = new ArrayList<>();
        //Get Firebase Database Reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders");
        //Add listener to retrieve or catch change of the orders
        databaseReference.addValueEventListener(new AdminOrderValueEventListener());
        //Create Custom Adapter for Recycler View, pass orders list to adapter
        mAdapter = new AdminOrderRecyclerViewAdapter(rootView.getContext(), orders);
        //And set this adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
    //Listener for retrieving orders and if any change occurs it will clear the list and get all orders from Firebase
    class AdminOrderValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //If any change clear list
            orders.clear();
            //Iterate for all orders and push each to the orders list
            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                Order order = dataSnapshot1.getValue(Order.class);
                order.setKey(dataSnapshot1.getKey());
                orders.add(0, order);
            }
            //After getting orders, notify it to Adapter
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
