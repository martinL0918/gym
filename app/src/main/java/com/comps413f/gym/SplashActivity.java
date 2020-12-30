package com.comps413f.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString(getString(R.string.pref_color),getString(R.string.pref_color_default));
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
        setContentView(R.layout.splash2);
        final Runnable r = new Runnable() {
            public void run() {
                showMainActivity();
            }
        };
        final Handler handler = new Handler();
        handler.postDelayed(r, 1500);
    }
/*
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            showMainActivity();
        return true;
    }
*/
    private void showMainActivity() {
        Intent intent = new Intent(this, Routine.class);
        startActivity(intent);
        finish();
    }

}