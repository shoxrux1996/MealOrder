package com.example.shoxrux.mealorder.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoxrux.mealorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth myAuth;
    private ProgressDialog progressDialog;
    @BindView(R.id.login_email)
    public EditText email;
    @BindView(R.id.login_password)
    public EditText password;
    private static int REGISTRATION_SUCCEED = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Get instance of firebase auth
        myAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);

    }
    //After Button clicked
    public void goToLogin(View view)
    {
        //Validate inputs
        if (validate()) {
            //Process dialog with input Registration
            progressDialog.setMessage("Log In ...");
            progressDialog.setCancelable(false);
            //activate it (starts to show the dialog)
            progressDialog.show();

            //SignIn with Firebase using email and password
            myAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //If task completes successfully finishing the activity with ok status
                            if (task.isSuccessful()) {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                //hiding the process dialog
                                progressDialog.dismiss();
                                finish();
                            } else {
                                //hiding the process dialog
                                progressDialog.dismiss();
                                //Show exception message of firebase excepton
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            //Validation error message
            Toast.makeText(this, "Entered wrong credentials", Toast.LENGTH_SHORT).show();
        }

    }
    //Go to Register Activity after register button clicked
    public void goToRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerIntent, REGISTRATION_SUCCEED);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If registration succeeded, finish this activity with success/ok result
        if (requestCode == REGISTRATION_SUCCEED) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }
    }
    //Validation of all the inputs
    public boolean validate(){
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty())
        {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
