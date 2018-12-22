package com.example.shoxrux.mealorder.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.shoxrux.mealorder.Model.Order;
import com.example.shoxrux.mealorder.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koushikdutta.ion.Ion;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminOrderInfoActivity extends AppCompatActivity {
    public static int ORDER_STATUS_CHANGED = 108;
    @BindView(R.id.admin_order_image)
    public ImageView imageView;
    @BindView(R.id.admin_order_title)
    public TextView titleView;
    @BindView(R.id.admin_order_amount)
    public TextView amountView;
    @BindView(R.id.admin_order_price)
    public TextView priceView;
    @BindView(R.id.admin_order_total_price)
    public TextView totalPriceView;
    @BindView(R.id.admin_order_address)
    public TextView addressView;
    @BindView(R.id.admin_order_name)
    public TextView nameView;
    @BindView(R.id.admin_order_radio_group)
    public RadioGroup radioGroup;
    @BindView(R.id.admin_order_radio_accept)
    public RadioButton acceptButton;
    @BindView(R.id.admin_order_radio_deny)
    public RadioButton denyButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_info);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //and set Toolbar to Action Bar
        setSupportActionBar(toolbar);
        //Set arrow back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        //Get order object from previous intent
        final Order order = (Order) intent.getSerializableExtra("orderInfo");
        //setting UI
        setUI(order);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //If accept Radio Button clicked
                if(i == acceptButton.getId()){
                    order.setStatus(Order.STATE_APPROVED);
                }
                //If deny Radio Button clicked
                if(i == denyButton.getId()){
                    order.setStatus(Order.STATE_REJECTED);
                }
                //Updating order status in database
                changeOrderStatus(order);
            }
        });
    }

    //Set user interface while on Create
    public void setUI(Order order){
        //Load image from the server (internet) using Ion
        Ion.with(this).load(order.getImage()).withBitmap()
                .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
        titleView.setText(order.getTitle());
        amountView.setText(order.getAmount()+"");
        priceView.setText(order.getPrice()+"");
        totalPriceView.setText(order.getTotalPrice()+"");
        addressView.setText(order.getAddress());
        nameView.setText(order.getName());

    }

    //updating order status to Database
    public void changeOrderStatus(Order order){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //Update order
        databaseReference.child("orders").child(order.getKey()).setValue(order);
        //Update order of the user
        databaseReference.child("users").child(order.getUserID()).child("orders").child(order.getKey()).setValue(order);
        //If changing order status finished ok, we will finish this activity with success result code
        Intent returnIntent = new Intent();
        setResult(ORDER_STATUS_CHANGED,returnIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
