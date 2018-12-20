package com.example.shoxrux.mealorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminMenuCreateActivity extends AppCompatActivity {

    public static int PICKER_CODE = 100;
    public static int MENU_CREATED = 105;
    @BindView(R.id.admin_menu_image)
    ImageView imageView;
    @BindView(R.id.admin_menu_title)
    EditText titleView;
    @BindView(R.id.admin_menu_price)
    EditText priceView;
    @BindView(R.id.admin_menu_description)
    EditText descriptionView;
    @BindView(R.id.admin_menu_ingredients)
    EditText ingredientsView;
    @BindView(R.id.admin_menu_save_button)
    Button orderButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Uri selectedImage = null;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu_create);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Create Order");

        progressDialog = new ProgressDialog(this);
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
                    Toast.makeText(AdminMenuCreateActivity.this, "All input fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean validateInputs(){
        if(titleView.getText().toString().isEmpty() || descriptionView.getText().toString().isEmpty() ||
                ingredientsView.getText().toString().isEmpty() || priceView.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
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
                        writeMenuToDatabase(key, downloadUri.toString());
                    }else{
                        writeMenuToDatabase(key, null);
                    }
                }
            });
        }else{
            writeMenuToDatabase(key,null);
        }
    }
    public void writeMenuToDatabase(String key, String imageURL){
        Menu menu = new Menu();
        menu.setTitle(titleView.getText().toString());
        menu.setDescription(descriptionView.getText().toString());
        menu.setPrice(Double.parseDouble(priceView.getText().toString()));
        menu.setIngredients(ingredientsView.getText().toString());
        if(imageURL != null){
            menu.setImageURL(imageURL);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
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
        if(requestCode == PICKER_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    progressDialog.setMessage("Uploading image...");
                    progressDialog.show();
                    selectedImage = data.getData();
                    try {
                        final InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        final Bitmap selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
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
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
