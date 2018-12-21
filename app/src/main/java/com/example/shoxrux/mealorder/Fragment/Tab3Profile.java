package com.example.shoxrux.mealorder.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shoxrux.mealorder.Activity.OrderListActivity;
import com.example.shoxrux.mealorder.Adapter.ProfileAdapter;
import com.example.shoxrux.mealorder.Model.Client;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoxrux on 12/13/18.
 */

public class Tab3Profile extends Fragment {
    private Client client;
    private ImageView imageView;
    private TextView textView;
    private View rootView;
    private List<String> userInfos;
    private ProfileAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab3profile, container, false);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userInfos = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //Get current user info to Object and complete user interface with user information
        databaseReference.child(currentUser.getUid()).addValueEventListener(new UserValueEventListener());

        ListView listView = rootView.findViewById(R.id.profile_list_view);
        adapter = new ProfileAdapter(rootView.getContext(), userInfos);
        imageView = rootView.findViewById(R.id.profile_image);
        textView = rootView.findViewById(R.id.profile_name);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ProfileOnItemClickListener());
        return rootView;
    }
    class ProfileOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(i == 3){
                Intent orderList = new Intent(view.getContext(), OrderListActivity.class);
                startActivity(orderList);
            }
        }
    }
    class UserValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userInfos.clear();
            client = dataSnapshot.getValue(Client.class);
            client.setKey(dataSnapshot.getKey());

            Ion.with(rootView.getContext()).load(client.getImage()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            textView.setText(client.getFirstName() + " " + client.getLastName());
            userInfos.add(client.getPhone()+"");
            userInfos.add(client.getEmail()+"");
            userInfos.add("www.inha.uz");
            userInfos.add("Order List");
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }
}
