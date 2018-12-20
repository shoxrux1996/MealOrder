package com.example.shoxrux.mealorder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

        mRecyclerView = rootView.findViewById(R.id.order_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        orders = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        databaseReference.addValueEventListener(new AdminOrderValueEventListener());

        mAdapter = new AdminOrderRecyclerViewAdapter(rootView.getContext(), orders);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
    class AdminOrderValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            orders.clear();
            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                Order order = dataSnapshot1.getValue(Order.class);
                order.setKey(dataSnapshot1.getKey());
                orders.add(0, order);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
