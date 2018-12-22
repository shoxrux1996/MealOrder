package com.example.shoxrux.mealorder.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoxrux.mealorder.Model.Client;
import com.example.shoxrux.mealorder.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.register_first_name)
    public EditText firstName;
    @BindView(R.id.register_last_name)
    public EditText lastName;
    @BindView(R.id.register_email)
    public EditText email;
    @BindView(R.id.register_phone)
    public EditText phone;
    @BindView(R.id.register_password)
    public EditText password;
    @BindView(R.id.register_password_confirm)
    public EditText confirmPassword;
    @BindView(R.id.register_image)
    public ImageView image;


    public static int PICKER_CODE = 100;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase Storage reference to upload the file
        storageReference = FirebaseStorage.getInstance().getReference();
        //Firebase Databasee reference to write or read data
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Progress Dialog for pop up information
        progressDialog = new ProgressDialog(this);
        ButterKnife.bind(this);
    }

    public void goToLogin(View view) {
        finish();
    }

    //After click event on imageView, we will create Picker Intent with Picker Code
    public void setImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICKER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If the request from Image Picker
        if (requestCode == PICKER_CODE) {
            //And if data isn't null and  result was ok (image selected)
            if (resultCode == Activity.RESULT_OK && data != null) {
                //Show Dialog with uploading message
                progressDialog.setMessage("Uploading image...");
                progressDialog.show();
                //Get selected Image Uri
                selectedImage = data.getData();
                try {
                    //Decoding the Uri to bitmap, in order to set it to imageView
                    final InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                    final Bitmap selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                    //Set image to ImageView
                    image.setImageBitmap(selectedImageBitmap);
                    progressDialog.dismiss();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            } else {
                image.setImageResource(R.drawable.contact);
                selectedImage = null;
            }
        }
    }
    //Registration function
    public void register(View view) {
        //Validating all inputs
        if(validate()){
            final FirebaseAuth myAuth = FirebaseAuth.getInstance();
            //Process dialog with input Registration
            progressDialog.setMessage("Registration ...");
            progressDialog.setCancelable(false);
            //activate it (starts to show the dialog)
            progressDialog.show();

            //Creating new User using Firebase Auth function
            myAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        final FirebaseUser currentUser = myAuth.getCurrentUser();
                        //If photo of the user picked up successfully
                        if (selectedImage != null) {
                            //Creating storage path reference using uid and image path
                            final StorageReference filepath = storageReference.child("images/users/"+currentUser.getUid()).child(selectedImage.getLastPathSegment());

                            //Upload photo file to Firebase Storage
                            UploadTask uploadTask = filepath.putFile(selectedImage);

                            // Register observers to listen for when the download is done or if it fails
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                        Log.e("shoxURL", "Success");
                                        writeNewUser(""+currentUser.getUid(), ""+currentUser.getEmail(),
                                                ""+firstName.getText().toString(), ""+lastName.getText().toString(), ""+downloadUri.toString(), phone.getText().toString()+"");
                                    } else {
                                        // Handle unsuccessful (Failed) upload
                                        Log.e("shoxURL", task.getException().getMessage());
                                        //storing new user to database
                                        writeNewUser(""+currentUser.getUid(), ""+currentUser.getEmail(),
                                                firstName.getText().toString()+"", lastName.getText().toString()+"", phone.getText().toString()+"");
                                    }
                                    //hiding the process dialog
                                    progressDialog.dismiss();
                                    //If creating user finished ok, we will finish this activity with success result
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK,returnIntent);
                                    finish();
                                }
                            });
                        }else {
                            Log.e("shoxURL", "image Null");
                            //storing new user to database
                            writeNewUser(""+currentUser.getUid(), ""+currentUser.getEmail(),
                                    firstName.getText().toString()+"", lastName.getText().toString()+"", phone.getText().toString()+"");

                            //hiding the process dialog
                            progressDialog.dismiss();
                            //If creating user finished ok, we will finish this activity with success result
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }

                    }else {
                        //hiding the process dialog
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //This function is stores the new user to database of Firebase
    private void writeNewUser(String userId,String email, String firstName, String lastName, String image, String phone) {
        Client client = new Client(email, firstName, lastName, image, phone);
        databaseReference.child("users").child(userId).setValue(client);
    }
    //This function is stores the new user to database of Firebase without image
    private void writeNewUser(String userId,String email, String firstName, String lastName, String phone) {
        Client client = new Client(email, firstName, lastName, phone);
        databaseReference.child("users").child(userId).setValue(client);
    }
    //Validation of all the inputs
    public boolean validate(){
        if(firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty()
                || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty()
                || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            Toast.makeText(this, "Passwords should match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
