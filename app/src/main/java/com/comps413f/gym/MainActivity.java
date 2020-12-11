package com.comps413f.gym;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        DatabaseReference newRef = database.getReference("Peter");
        myRef.child("name").setValue("martin");
        myRef.child("message").setValue("hello world!");
        myRef.child("id").setValue("12345678");
        myRef.child("user").child("username").setValue("Martin");
        newRef.child("user").setValue("apple");
        //"i am soka haha"
    }


}