package com.comps413f.gym;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

public class PreferenceActivity extends android.preference.PreferenceActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        String theme = prefs.getString(getString(R.string.pref_color),getString(R.string.pref_color_default));
        String language = prefs.getString(getString(R.string.pref_language),getString(R.string.pref_language_default));
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
        addPreferencesFromResource(R.xml.preferences);

    }
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Intent intent = new Intent(PreferenceActivity.this,Routine.class);
            startActivity(intent);
            finish();
        }
    };

}
