package com.comps413f.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

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