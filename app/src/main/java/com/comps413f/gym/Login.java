package com.comps413f.gym;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {
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
                login_tab.setTextColor(ContextCompat.getColor(Login.this, R.color.colorPrimary));
                regigser_tab.setTextColor(Color.BLACK);
                confirmpassword.setVisibility(View.VISIBLE);
                confirmpassword_editText.setVisibility(View.VISIBLE);
                login_button.setText(getString(R.string.register));
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
                login_tab.setTextColor(Color.BLACK);
                login_button.setText(getString(R.string.login));
                regigser_tab.setTextColor(ContextCompat.getColor(Login.this, R.color.colorPrimary));
                confirmpassword.setVisibility(View.GONE);
                confirmpassword_editText.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
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
                            Toast.makeText(Login.this, "createUserWithEmail.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, task.getException().getMessage(),
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
}