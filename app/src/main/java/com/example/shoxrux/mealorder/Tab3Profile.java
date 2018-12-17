package com.example.shoxrux.mealorder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shoxrux on 12/13/18.
 */

public class Tab3Profile extends Fragment {
    private Client client;
    private ImageView imageView;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3profile, container, false);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        List<String> userInfos = Arrays.asList("+998908082443",currentUser.getEmail(),"www.inha.uz","Order List");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(currentUser.getUid()).addValueEventListener(new UserInfoEventListener());

        ListView listView = rootView.findViewById(R.id.profile_list_view);
        ProfileAdapter adapter = new ProfileAdapter(rootView.getContext(), userInfos);
        imageView = rootView.findViewById(R.id.profile_image);
        textView = rootView.findViewById(R.id.profile_name);

        listView.setAdapter(adapter);
        return rootView;
    }
    class UserInfoEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            client = dataSnapshot.getValue(Client.class);
            client.setKey(dataSnapshot.getKey());
            Log.e("shoxError",client.getEmail());
            Ion.with(getContext()).load(client.getImage()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            textView.setText(client.getFirstName()+" "+client.getLastName());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
