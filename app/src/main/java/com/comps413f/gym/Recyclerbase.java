package com.comps413f.gym;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
    private SharedPreferences prefs;
    static final String EXTRA_DAY = "routineDay"; // extra key
    private TextView timerTextView;
    private AlertDialog diag = null;
    long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    final Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString(getString(R.string.pref_color),getString(R.string.pref_color_default));
        System.out.println(theme);

        if (theme.equals("Green")){
            setTheme(R.style.AppThemeGreen);
        }
        else if (theme.equals("Purple")){
            setTheme(R.style.AppThemePurple);
        }
        else{
            System.out.println("Orange");
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.recyclerbase);
        mAuth = FirebaseAuth.getInstance();
        final List<Action> actionList = new ArrayList<>();
        //For testing
        //actionList.add(new Action("Sit up","description","organs","times","usage","references","days"));
        //actionList.add(new Action("Push up","description","organs","times","usage","references","days"));

        // get the extra value
        final String day = getIntent().getStringExtra(EXTRA_DAY);
        System.out.println("day : "+day);

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
                actionList.clear();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    /*To get data with specified address
                    Action temp001 = dataSnapshot.child("-MPRsPmAx_V0KdZ2N8oz").getValue(Action.class);
                    System.out.println(temp001.getActionName());*/
                    if (dataSnapshot.child(datas.getKey()).child("days").getValue().toString().contains(day)){
                        Action temp = datas.getValue(Action.class);
                        actionList.add(temp);
                        adapter.notifyDataSetChanged();
                    }
                }
                if (actionList.size() == 0){
                    adapter.notifyDataSetChanged();
                    timerTextView.setText("");

                    diag = createDialog(day);
                    if (!isFinishing()){
                        diag.show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read data failed: " + databaseError.getCode());
            }
        });
            timerTextView = findViewById(R.id.timerTextView);
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            ReturnToLogin();
        }
        timerHandler.postDelayed(timerRunnable, 0);
    }
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(diag != null)
            diag.dismiss();
            System.out.println("onStop Destroyed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(diag != null)
            diag.dismiss();
        System.out.println("onDestr Destroyed");
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
                Intent intent = new Intent(Recyclerbase.this,PreferenceActivity.class);
                startActivity(intent);
                break;
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


    public void ReturnToLogin(){
        Intent intent = new Intent();
        intent.setClass(Recyclerbase.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public AlertDialog createDialog(String day){
        AlertDialog.Builder builder = new AlertDialog.Builder(Recyclerbase.this);
        builder.setTitle("Empty");
        // Add a checkbox list
        builder.setMessage(getString(R.string.not_data_in_database)+" " + day);
        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Recyclerbase.this,Routine.class);
                startActivity(intent);
            }
        });
        // Create and show the alert dialog
        final AlertDialog dialog = builder.create();

        return dialog;
    }


}
