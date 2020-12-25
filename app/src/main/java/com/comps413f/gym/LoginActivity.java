package com.comps413f.gym;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    Button regigser_tab;
    Button login_tab;
    EditText email_editText;
    EditText password_editText;
    TextView confirmpassword;
    EditText confirmpassword_editText;
    Button login_button;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
        regigser_tab = findViewById(R.id.register_tab);
        login_tab = findViewById(R.id.login_tab);
        confirmpassword = findViewById(R.id.confirmpassword);
        confirmpassword_editText = findViewById(R.id.confirmpassword_editText);
        login_button = findViewById(R.id.login_button);

        regigser_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_tab.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                regigser_tab.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_fill));
                regigser_tab.setTextColor(Color.WHITE);
                confirmpassword.setVisibility(View.VISIBLE);
                confirmpassword_editText.setVisibility(View.VISIBLE);
                login_button.setText(getString(R.string.register));
                login_tab.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_outline));
                login_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Register();
                    }
                });
            }
        });

        login_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_tab.setTextColor(Color.WHITE);
                login_button.setText(getString(R.string.login));
                login_tab.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_fill));
                regigser_tab.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_outline));
                regigser_tab.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                confirmpassword.setVisibility(View.GONE);
                confirmpassword_editText.setVisibility(View.GONE);
                login_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Login();
                    }
                });
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Name, email address, and profile photo Url
            System.out.println("You are signed in as "+ currentUser.getEmail());
        }
        else{
           Toast.makeText(LoginActivity.this,"You have not signed in",Toast.LENGTH_LONG).show();
        }
    }

    private void Register() {
        final EditText email_editText = findViewById(R.id.email_editText);
        final EditText password_editText = findViewById(R.id.password_editText);
        EditText confirmpassword_editText = findViewById(R.id.confirmpassword_editText);
        String email = email_editText.getText().toString();
        String password = password_editText.getText().toString();
        String confirmpassword = confirmpassword_editText.getText().toString();
        if (TextUtils.isEmpty(email)){
            email_editText.setError(getString(R.string.empty_email));
            return;
        }
        if (TextUtils.isEmpty(password)){
            password_editText.setError(getString(R.string.empty_password));
            return;
        }
        if(!confirmpassword.equals(password) ){
            confirmpassword_editText.setError(getString(R.string.not_matched_password));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "createUserWithEmail.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference(user.getUid());
                            myRef.child("nickname").setValue("");



                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            String error_code = ((FirebaseAuthException)task.getException()).getErrorCode();
                            switch (error_code){
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    email_editText.setError(getString(R.string.ERROR_EMAIL_ALREADY_IN_USE));
                                    break;
                                case "ERROR_INVALID_EMAIL":
                                    email_editText.setError(getString(R.string.ERROR_INVALID_EMAIL));
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    password_editText.setError(getString(R.string.ERROR_WEAK_PASSWORD));
                            }

                        }
                    }
                });

    }

    private void Login() {
        final EditText email_editText = findViewById(R.id.email_editText);
        final EditText password_editText = findViewById(R.id.password_editText);
        String email = email_editText.getText().toString();
        String password = password_editText.getText().toString();
        if (TextUtils.isEmpty(email)){
            email_editText.setError(getString(R.string.empty_email));
            return;
        }
        if (TextUtils.isEmpty(password)){
            password_editText.setError(getString(R.string.empty_password));
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,Routine.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            email_editText.setError("Email / Password is wrong. Please try again");
                        }


        }
        });
    }
    /*protected void displayAlertDialog(){
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Choose Days");
        // Add a checkbox list
       builder.setMessage();
        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                int i =0;
                String result = "";
                result = result.substring(0,result.length() - 1);
            }
        });
        builder.setNegativeButton("Cancel", null);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/
}