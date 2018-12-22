package com.example.shoxrux.mealorder.Activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.shoxrux.mealorder.Model.Order;
import com.example.shoxrux.mealorder.Adapter.OrderRecyclerViewAdapter;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private OrderRecyclerViewAdapter mAdapter;
    private List<Order> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //and set Toolbar to Action Bar
        setSupportActionBar(toolbar);
        //Set arrow back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Create empty Array List of orders
        orders = new ArrayList<>();
        //Get Firebase Database Reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query myQuery = databaseReference.child(uID).child("orders");
        //Add listener to retrieve or catch change of this user orders
        myQuery.addValueEventListener(new OrderValueEventListener());

        //Set up Recycler view
        mRecyclerView = findViewById(R.id.order_recycler_view);
        //Set layout for recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Create Custom Adapter for Recycler View
        mAdapter = new OrderRecyclerViewAdapter(this, orders);
        //And set this adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //If back button pressed, finish activity
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Listener for retrieving orders and if any change occurs it will clear the list and get all orders from Firebase
    class OrderValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //If any change clear list
            orders.clear();
            //Iterate for all orders and push each to the orders list
            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                Order order = dataSnapshot1.getValue(Order.class);
                order.setKey(dataSnapshot1.getKey());
                orders.add(0,order);

            }
            //After getting orders, notify it to Adapter
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }
}
