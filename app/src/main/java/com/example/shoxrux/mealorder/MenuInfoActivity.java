package com.example.shoxrux.mealorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuInfoActivity extends AppCompatActivity {
    public static int MENU_ORDER = 243;
    @BindView(R.id.info_menu_image)
    ImageView imageView;
    @BindView(R.id.info_menu_price)
    TextView priceView;
    @BindView(R.id.info_menu_description)
    TextView descriptionView;
    @BindView(R.id.info_menu_ingredients)
    TextView ingredientsView;
    @BindView(R.id.info_menu_order_button)
    FloatingActionButton orderButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        final Menu menu = (Menu) intent.getSerializableExtra("menuInfo");

        setUI(menu);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderIntent = new Intent(MenuInfoActivity.this, MakeOrderActivity.class);
                orderIntent.putExtra("menuInfo", menu);
                startActivityForResult(orderIntent, MENU_ORDER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MENU_ORDER){
            if(resultCode == Activity.RESULT_OK){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Successfully ordered!");
                dialog.setMessage("Your order was received, it will be confirmed soon. Please be in touch");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setUI(Menu menu){
        toolbar.setTitle(menu.getTitle());
        //Load image from the server (internet) using Ion
        Ion.with(this).load(menu.getImageURL()).withBitmap()
                .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
        priceView.setText(menu.getPrice()+" sum");
        descriptionView.setText(menu.getDescription());
        ingredientsView.setText(menu.getIngredients());

    }
}
