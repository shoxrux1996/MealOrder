package com.example.shoxrux.mealorder.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoxrux.mealorder.Model.Client;
import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.Model.Order;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakeOrderActivity extends AppCompatActivity {
    private Client client;
    @BindView(R.id.make_order_image)
    public ImageView imageView;
    @BindView(R.id.make_order_amount)
    public EditText amountEditText;
    @BindView(R.id.make_order_address)
    public EditText addressEditText;
    @BindView(R.id.make_order_button)
    public Button makeOrderButton;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);

        //and set Toolbar to Action Bar
        setSupportActionBar(toolbar);
        //Set arrow back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        //get Object from the intent
        menu = (Menu) intent.getSerializableExtra("menuInfo");
        //set all Views
        setUI(menu);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Get current user info to Object and complete user interface with user information
        //Adding listener to retrieve the user from Database of Firebase
        databaseReference.child("users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get client object
                client = dataSnapshot.getValue(Client.class);
                //Set key
                client.setKey(dataSnapshot.getKey());
                System.out.println(client.getEmail()+dataSnapshot.getKey()+" client");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //Creating order after order button clicks
        makeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!amountEditText.getText().toString().equals("") && !addressEditText.getText().toString().equals("")) {
                    progressDialog.setMessage("Ordering ...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    //Create an order with menu, address and amount
                    makeOrder(menu, amountEditText.getText().toString(), addressEditText.getText().toString());
                    progressDialog.dismiss();

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                } else {
                    Toast.makeText(MakeOrderActivity.this, "Address and amount are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Setting the views while on create
    public void setUI(Menu menu) {
        //Download image from menu image url
        Ion.with(this).load(menu.getImageURL()).withBitmap()
                .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
        toolbar.setTitle("Order " + menu.getTitle());
    }
    //This function stores new order to Firebase
    public void makeOrder(Menu menu, String amount, String address) {
        //if user is not null, create order object and store to Firebase
        if (client != null) {
            int amountOfOrder = Integer.parseInt(amount);
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(currentTime.getTime());
            Order order = new Order(client.getFirstName() + " " + client.getLastName(),
                    menu.getTitle(), time, menu.getPrice(), amountOfOrder,
                    amountOfOrder * menu.getPrice(), address,Order.STATE_WAITING, menu.getImageURL(), client.getKey());
            String key = databaseReference.child(currentUser.getUid()).child("orders").push().getKey();
            //Storing in orders referens
            databaseReference.child("users").child(currentUser.getUid()).child("orders").child(key).setValue(order);
            //Storing in users-> orders referens too
            databaseReference.child("orders").child(key).setValue(order);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //if toolbar arrow button back clicked
            case android.R.id.home:
                //finish this activity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
