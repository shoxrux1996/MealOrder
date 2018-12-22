package com.example.shoxrux.mealorder.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminMenuCreateActivity extends AppCompatActivity {

    public static int PICKER_CODE = 100;
    public static int MENU_CREATED = 105;
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
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private Uri selectedImage = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu_create);

        ButterKnife.bind(this);
        //and set Toolbar to Action Bar
        setSupportActionBar(toolbar);
        //Set arrow back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Set title of toolbar
        toolbar.setTitle("Create Menu");

        progressDialog = new ProgressDialog(this);
        //Set click listener and perform new
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICKER_CODE);
            }
        });
        //Save new menu to database
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validate all inputs
                if(validateInputs()){
                    progressDialog.setMessage("Creating menu...");
                    progressDialog.show();
                    //Function to store menu to Firebase with Image
                    uploadPhotoAndStore();
                }else{
                    Toast.makeText(AdminMenuCreateActivity.this, "All input fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Validation of all inputs
    public boolean validateInputs(){
        if(titleView.getText().toString().isEmpty() || descriptionView.getText().toString().isEmpty() ||
                ingredientsView.getText().toString().isEmpty() || priceView.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
    //Uploading selected Photo to Firebase Storage and calling writeMenuToDatabase to store the menu
    public void uploadPhotoAndStore(){
        //If photo of the menu picked up successfully
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String key = databaseReference.child("menus").push().getKey();
        if (selectedImage != null) {
            //Upload photo file to Firebase Storage
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("images/menus").child(key).child(selectedImage.getLastPathSegment());
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
                        //Call the function to store new Menu with key and image
                        writeMenuToDatabase(key, downloadUri.toString());
                    }else{
                        //Call the function to store new Menu with key and without image
                        writeMenuToDatabase(key, null);
                    }
                }
            });
        }else{
            //Call the function to store new Menu with key and without image
            writeMenuToDatabase(key,null);
        }
    }

    //Storing new Menu to Firebase Database with key which retrieved from Push in uploadPhotoAndStore function
    public void writeMenuToDatabase(String key, String imageURL){
        Menu menu = new Menu();
        //Set Menu object
        menu.setTitle(titleView.getText().toString());
        menu.setDescription(descriptionView.getText().toString());
        menu.setPrice(Double.parseDouble(priceView.getText().toString()));
        menu.setIngredients(ingredientsView.getText().toString());
        if(imageURL != null){
            menu.setImageURL(imageURL);
        }

        //Get Firebase initial reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //Store
        databaseReference.child("menus").child(key).setValue(menu);

        //hide the process dialog
        progressDialog.dismiss();
        //If creating menu finished ok, we will finish this activity with success result
        Intent returnIntent = new Intent();
        setResult(MENU_CREATED,returnIntent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If request Image Picker
        if(requestCode == PICKER_CODE){
            //And result is success and data not null
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

}
