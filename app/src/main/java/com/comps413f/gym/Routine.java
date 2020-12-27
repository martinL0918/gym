package com.comps413f.gym;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Routine extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private TextView background;
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
        setContentView(R.layout.routine);
        mAuth = FirebaseAuth.getInstance();
        background = findViewById(R.id.background);
        if (theme.equals("Green")){
            background.setBackground(getResources().getDrawable(R.drawable.green_gradient));
        }
        else if (theme.equals("Purple")){

        }
        else{
            background.setBackground(getResources().getDrawable(R.drawable.orange_gradient));
        }
        //////////////

        final Button day1 = (Button)findViewById(R.id.day1);
        Button[] dayArray = new Button[]{day1};
        for (Button aBtn : dayArray){
            aBtn.setBackground(getResources().getDrawable(R.drawable.green_circle_background_routine));
        }
        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTranslate(day1)) {
                    Intent intent = new Intent(Routine.this,
                            Recyclerbase.class);
                    intent.putExtra(Recyclerbase.EXTRA_DAY,
                            "day1");
                    startActivity(intent);
                }
            }
        });

        final Button day2 = (Button)findViewById(R.id.day2);

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTranslate(day2)) {
                    Intent intent = new Intent(Routine.this,
                            Recyclerbase.class);
                    intent.putExtra(Recyclerbase.EXTRA_DAY,
                            "day2");
                    startActivity(intent);
                }
            }
        });

        final Button day3 = (Button)findViewById(R.id.day3);

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTranslate(day3)) {
                    Intent intent = new Intent(Routine.this,
                            Recyclerbase.class);
                    intent.putExtra(Recyclerbase.EXTRA_DAY,
                            "day3");
                    startActivity(intent);
                }
            }
        });


        final Button day4 = (Button)findViewById(R.id.day4);

        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTranslate(day4)) {
                    Intent intent = new Intent(Routine.this,
                            Recyclerbase.class);
                    intent.putExtra(Recyclerbase.EXTRA_DAY,
                            "day4");
                    startActivity(intent);
                }
            }
        });

        final Button day5 = (Button)findViewById(R.id.day5);

        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTranslate(day5)) {
                    Intent intent = new Intent(Routine.this,
                            Recyclerbase.class);
                    intent.putExtra(Recyclerbase.EXTRA_DAY,
                            "day5");
                    startActivity(intent);
                }
            }
        });

        final Button day6 = (Button)findViewById(R.id.day6);

        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTranslate(day6)) {
                    Intent intent = new Intent(Routine.this,
                            Recyclerbase.class);
                    intent.putExtra(Recyclerbase.EXTRA_DAY,
                            "day6");
                    startActivity(intent);
                }
            }
        });


        final Button day7 = (Button)findViewById(R.id.day7);

        day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTranslate(day7)) {
                    Intent intent = new Intent(Routine.this,
                            Recyclerbase.class);
                    intent.putExtra(Recyclerbase.EXTRA_DAY,
                            "day7 ");
                    startActivity(intent);
                }
            }
        });



        final Button add = (Button)findViewById(R.id.addDayButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRotation(add);
            }
        });








    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
        }
        else{
            ReturnToLogin();
            Toast.makeText(Routine.this,"You have not signed in",Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(Routine.this,PreferenceActivity.class);
                startActivity(intent);
                break;
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

    public boolean doTranslate(Button button) {
        int distance = ((View)button.getParent()).getWidth() - button.getWidth();
        Animation animation = new TranslateAnimation(0, distance, 0, 0); // TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
        animation.setDuration(50);
        // animation.setRepeatCount(1);
        // animation.setRepeatMode(Animation.REVERSE); // going backward
        button.startAnimation(animation);
        return true;
    }

    public boolean doRotation(Button button) {
        float x,y ;
        x = (float)(button.getWidth()*0.5);
        y = (float)(button.getHeight()*0.5);
        RotateAnimation animation = new RotateAnimation(0, 75, x, y); // TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE); // going backward
        animation.setDuration(500);
        button.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(Routine.this,
                        AddActionActivity.class);
                intent.putExtra(AddActionActivity.EXTRA_DATA,
                        "addDay");
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return  true;
    }
}
