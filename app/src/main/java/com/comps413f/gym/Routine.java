package com.comps413f.gym;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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

import java.util.Locale;

public class Routine extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private TextView background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString(getString(R.string.pref_color),getString(R.string.pref_color_default));
        String language = prefs.getString(getString(R.string.pref_language),getString(R.string.pref_language_default));
        System.out.println(theme);
        System.out.println(language);
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
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale localeZH = new Locale(language);
        Locale.setDefault(localeZH);
        config.locale = localeZH;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.routine);
        mAuth = FirebaseAuth.getInstance();
        background = findViewById(R.id.background);


        final Button day1 = (Button)findViewById(R.id.day1);
        final Button day2 = (Button)findViewById(R.id.day2);
        final Button day3 = (Button)findViewById(R.id.day3);
        final Button day4 = (Button)findViewById(R.id.day4);
        final Button day5 = (Button)findViewById(R.id.day5);
        final Button day6 = (Button)findViewById(R.id.day6);
        final Button day7 = (Button)findViewById(R.id.day7);

        final Button[] btnArray = new Button[] {day1,day2,day3,day4,day5,day6,day7};


        /*
        if (theme.equals("Green")){
            background.setBackground(getResources().getDrawable(R.drawable.green_gradient));
        }
        else if (theme.equals("Purple")){
            background.setBackground(getResources().getDrawable(R.drawable.purple_gradient));
        }
        else{
            background.setBackground(getResources().getDrawable(R.drawable.orange_gradient));
        }
        */

        switch(theme){
            case "Green":
                for(Button aBtn : btnArray){
                    aBtn.setBackground(getResources().getDrawable(R.drawable.green_circle_background_routine));
                }
                background.setBackground(getResources().getDrawable(R.drawable.green_gradient));
                break;

            case "Purple":
                for(Button aBtn : btnArray){
                    aBtn.setBackground(getResources().getDrawable(R.drawable.purple_circle_background_routine));
                }
                background.setBackground(getResources().getDrawable(R.drawable.purple_gradient));
                break;

            default:
                for(Button aBtn : btnArray){
                    aBtn.setBackground(getResources().getDrawable(R.drawable.orange_circle_background_routine));
                }
                background.setBackground(getResources().getDrawable(R.drawable.orange_gradient));
                break;
        }




        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRightTranslate(day1);
            }
        });


        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRightTranslate(day2);
            }
        });


        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRightTranslate(day3);
            }
        });



        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRightTranslate(day4);
            }
        });


        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLeftTranslate(day5);
            }
        });


        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLeftTranslate(day6);
            }
        });



        day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLeftTranslate(day7);
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
                finish();
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

    public boolean doRightTranslate(final Button button) {
        int distance = ((View)button.getParent()).getWidth() - button.getWidth();
        Animation animation = new TranslateAnimation(0, distance, 0, 0); // TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
        animation.setDuration(500);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE); // going backward
        button.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(Routine.this,
                        Recyclerbase.class);
                intent.putExtra(Recyclerbase.EXTRA_DAY,
                        button.getText().toString());
                startActivity(intent);
            }
        });

        return true;
    }
    public boolean doLeftTranslate(final Button button) {
        int distance = ((View)button.getParent()).getWidth() - button.getWidth();
        Animation animation = new TranslateAnimation(0, -distance, 0, 0); // TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
        animation.setDuration(500);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE); // going backward
        button.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(Routine.this,
                        Recyclerbase.class);
                intent.putExtra(Recyclerbase.EXTRA_DAY,
                        button.getText().toString());
                startActivity(intent);
            }
        });

        return true;
    }

    public boolean doRotation(final Button button) {
        float x,y ;
        x = (float)(button.getWidth()*0.5);
        y = (float)(button.getHeight()*0.5);
        RotateAnimation animation = new RotateAnimation(0, 75, x, y); // TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE); // going backward
        animation.setDuration(500);
        button.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(Routine.this,
                        AddActionActivity.class);
                intent.putExtra(Recyclerbase.EXTRA_DAY,
                        button.getText().toString());
                startActivity(intent);
            }
        });
        return  true;
    }
}
