package com.comps413f.gym;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Routine extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine);
        mAuth = FirebaseAuth.getInstance();
        user_id = findViewById(R.id.user_id);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            user_id.setText(currentUser.getUid());
        }
        else{
            ReturnToLogin();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_logout:
                FirebaseAuth.getInstance().signOut();
                ReturnToLogin(); //登出功能
                return true;
            case R.id.item_setting:
                System.out.println("Buton pressed");
                return true;
            case R.id.item_about:
                ReturnToAbout();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    public void ReturnToLogin(){
        Intent intent = new Intent();
        intent.setClass(Routine.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void ReturnToAbout(){
        Intent intent = new Intent();
        intent.setClass(Routine.this,AboutActivity.class);
        startActivity(intent);
        finish();
    }
}
