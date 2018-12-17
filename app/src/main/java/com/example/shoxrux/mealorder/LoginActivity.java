package com.example.shoxrux.mealorder;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth myAuth;


    private EditText email;
    private EditText password;
    private static int REGISTRATION_SUCCEED = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        myAuth = FirebaseAuth.getInstance();

    }
    public void goToLogin(View view) {
        if (emailValidation(email) & passwordValidation(password)) {
            myAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Entered wrong credentials", Toast.LENGTH_SHORT).show();
        }

    }

    public void goToRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerIntent, REGISTRATION_SUCCEED);
    }

    public static boolean emailValidation(EditText editText) {
        String text = editText.getText().toString();
        if (text.equals(null) || text.equals("")) {
            return false;
        }
        //if(editText.getInputType() != InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS){}
        return true;
    }

    public static boolean passwordValidation(EditText editText) {
        String text = editText.getText().toString();
        if (text.equals(null) || text.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTRATION_SUCCEED) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }
    }
}
