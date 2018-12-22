package com.example.shoxrux.mealorder.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koushikdutta.ion.Ion;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminMenuInfoActivity extends AppCompatActivity {
    public static int PICKER_CODE = 100;
    public static int MENU_UPDATED = 103;
    public static int MENU_DELETED = 104;
    @BindView(R.id.admin_menu_image)
    public ImageView imageView;
    @BindView(R.id.admin_menu_title)
    public EditText titleView;
    @BindView(R.id.admin_menu_price)
    public EditText priceView;
    @BindView(R.id.admin_menu_description)
    public EditText descriptionView;
    @BindView(R.id.admin_menu_ingredients)
    public EditText ingredientsView;
    @BindView(R.id.admin_menu_save_button)
    public Button orderButton;
    @BindView(R.id.admin_menu_remove_button)
    public FloatingActionButton removeButton;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private Uri selectedImage = null;
    private ProgressDialog progressDialog;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu_info);

        ButterKnife.bind(this);
        //and set Toolbar to Action Bar
        setSupportActionBar(toolbar);
        //Set arrow back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        //Get Menu object from intent
        menu = (Menu) intent.getSerializableExtra("menuInfo");
        setUI(menu);

        //Deleting the menu
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMenuInfoActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Call Delete function to delete the Menu
                        deleteMenu();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //Set click listener and perform new
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICKER_CODE);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()){
                    progressDialog.setMessage("Editing menu...");
                    progressDialog.show();
                    uploadPhotoAndStore();
                }else{
                    Toast.makeText(AdminMenuInfoActivity.this, "All input fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Removing the menu from Database and removing related favorites from users
    public void deleteMenu(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Boolean> users = menu.getUsers();
        //Remove favorites if the key equals to menu key
        for(String key : users.keySet()){
            databaseReference.child("users").child(key).child("favorites").child(menu.getKey()).removeValue();
        }
        //Remove menu from DB
        databaseReference.child("menus").child(menu.getKey()).removeValue();
        //If deleting menu finished ok, we will finish this activity with success result
        Intent returnIntent = new Intent();
        setResult(MENU_DELETED,returnIntent);
        finish();
    }

    //Validation of all inputs
    public boolean validateInputs(){
        if(titleView.getText().toString().isEmpty() || descriptionView.getText().toString().isEmpty() ||
                ingredientsView.getText().toString().isEmpty() || priceView.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
    //Saving image photo to storage and after success update Menu object to Firebase Database
    public void uploadPhotoAndStore(){
        //If photo of the menu picked up successfully
        if (selectedImage != null) {
            //Upload photo file to Firebase Storage
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("images/menus").child(menu.getKey()).child(selectedImage.getLastPathSegment());
            UploadTask uploadTask = filepath.putFile(selectedImage);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        //update Menu in database with image
                        updateMenuToDatabase(downloadUri.toString());
                    }else{
                        //update Menu in database without image
                        updateMenuToDatabase(null);
                    }
                }
            });
        }else{
            //update Menu in database without image

            updateMenuToDatabase(null);
        }
    }

    //Updating the menu
    public void updateMenuToDatabase(String imageURL){
        HashMap<String, Boolean> users = menu.getUsers();
        //Set menu object
        menu.setTitle(titleView.getText().toString());
        menu.setDescription(descriptionView.getText().toString());
        menu.setPrice(Double.parseDouble(priceView.getText().toString()));
        menu.setIngredients(ingredientsView.getText().toString());
        if(imageURL != null){
            menu.setImageURL(imageURL);
        }
        //Get Reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("menus").child(menu.getKey()).setValue(menu);
        //Updating favorites if key equals to menu key
        for(String key : users.keySet()){
            databaseReference.child("users").child(key).child("favorites").child(menu.getKey()).setValue(menu);
        }
        //hide the process dialog
        progressDialog.dismiss();
        //If creating menu finished ok, we will finish this activity with success result
        Intent returnIntent = new Intent();
        setResult(MENU_UPDATED,returnIntent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Request from Image Picker
        if(requestCode == PICKER_CODE){
            //if result is success and data not null
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    progressDialog.setMessage("Uploading image...");
                    progressDialog.show();
                    //get data from request
                    selectedImage = data.getData();
                    try {
                        final InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        final Bitmap selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                        //set image to ImageView
                        imageView.setImageBitmap(selectedImageBitmap);
                        progressDialog.dismiss();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            }
        }
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
    public void setUI(Menu menu){
        //Load image from the server (internet) using Ion
        Ion.with(this).load(menu.getImageURL()).withBitmap()
                .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
        titleView.setText(menu.getTitle());
        priceView.setText(menu.getPrice()+"");
        descriptionView.setText(menu.getDescription());
        ingredientsView.setText(menu.getIngredients());

    }
}
