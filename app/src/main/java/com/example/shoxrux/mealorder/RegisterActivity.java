package com.example.shoxrux.mealorder;

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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class RegisterActivity extends AppCompatActivity {
     EditText firstName;
     EditText lastName;
     EditText email;
     EditText password;

    private EditText confirmPassword;
    private ImageView image;
    public static int PICKER_CODE = 100;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        init();
    }

    private void init() {
        firstName = findViewById(R.id.register_first_name);
        lastName = findViewById(R.id.register_last_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_password_confirm);
        image = findViewById(R.id.register_image);
    }

    public void goToLogin(View view) {
        finish();
    }

    public void setImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICKER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKER_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                progressDialog.setMessage("Uploading image...");
                progressDialog.show();
                selectedImage = data.getData();
                try {
                    final InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                    final Bitmap selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
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

    public void register(View view) {

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
                                    writeNewUser(""+currentUser.getUid(), ""+currentUser.getEmail(),""+
                                            ""+firstName.getText().toString(), ""+lastName.getText().toString(), ""+downloadUri.toString());

                                } else {
                                    // Handle unsuccessful (Failed) upload
                                    Log.e("shoxURL", task.getException().getMessage());
                                    //storing new user to database
                                    writeNewUser(""+currentUser.getUid(), ""+currentUser.getEmail(),
                                            firstName.getText().toString(), lastName.getText().toString());
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
                                firstName.getText().toString(), lastName.getText().toString());

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

    //This function is stores the new user to database of Firebase
    private void writeNewUser(String userId,String email, String firstName, String lastName, String image) {
        Client client = new Client(email, firstName, lastName, image);
        databaseReference.child("users").child(userId).setValue(client);
    }
    //This function is stores the new user to database of Firebase without image
    private void writeNewUser(String userId,String email, String firstName, String lastName) {
        Client client = new Client(email, firstName, lastName);
        databaseReference.child("users").child(userId).setValue(client);
    }
}
