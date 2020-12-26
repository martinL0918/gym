package com.comps413f.gym;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Recyclerbase extends AppCompatActivity {
    private FirebaseAuth mAuth;
    static final String EXTRA_DAY = "routineDay"; // extra key
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerbase);
        mAuth = FirebaseAuth.getInstance();
        final List<Action> actionList = new ArrayList<>();
        //For testing
        //actionList.add(new Action("Sit up","description","organs","times","usage","references","days"));
        //actionList.add(new Action("Push up","description","organs","times","usage","references","days"));

        //-----------
        // get the extra value
        String day = getIntent().getStringExtra(EXTRA_DAY);

        System.out.println("extra day: "+day);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Recyclerbase.this));
        final ActionAdapter adapter = new ActionAdapter(this,actionList);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        System.out.println(mAuth.getCurrentUser().getUid());
        DatabaseReference ref = database.getReference(mAuth.getCurrentUser().getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    /*To get data with specified address
                    Action temp001 = dataSnapshot.child("-MPRsPmAx_V0KdZ2N8oz").getValue(Action.class);
                    System.out.println(temp001.getActionName());*/
                    Action temp = datas.getValue(Action.class);
                    actionList.add(temp);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item_logout = menu.findItem(R.id.item_logout);
        item_logout.setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_setting:
                System.out.println("Buton pressed");
                return true;
            case R.id.item_about:
                ReturnToAbout();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    public void ReturnToAbout(){
        Intent intent = new Intent();
        intent.setClass(Recyclerbase.this,AboutActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            System.out.println("User Logined");
        }
        else{
            ReturnToLogin();
        }
    }

    public void ReturnToLogin(){
        Intent intent = new Intent();
        intent.setClass(Recyclerbase.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
