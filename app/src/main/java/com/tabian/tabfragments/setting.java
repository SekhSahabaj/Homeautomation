package com.tabian.tabfragments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import maes.tech.intentanim.CustomIntent;


public class setting extends AppCompatActivity {
    public String nightmode="";
    private static final String TAG = "SettingsActivity";
    SharedPreferences nightmodedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        nightmodedata = getSharedPreferences("Nightmode", Context.MODE_PRIVATE);
        ActionBar actionBar =getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);
}


        Spinner spinner = findViewById(R.id.spinner1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: " + position );


                handleNightMode(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: ");

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
      onBackPressed();

        return true;
    }

    private void handleNightMode(int positions) {

        switch (positions) {
            case 0:
            Log.e(TAG, "Nothing Selected");
            break;
            case 1:
                Log.e(TAG, "YES");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                getDelegate().applyDayNight();
                nightmode="1";
                saveNightMode();

                break;
            case 2:
                Log.e(TAG, "NO");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                getDelegate().applyDayNight();
                nightmode="2";
                saveNightMode();
                break;
            case 3:
                Log.e(TAG, "AUTO");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
                getDelegate().applyDayNight();
                nightmode="3";
                saveNightMode();
                break;
            default:
                Log.e(TAG, "Nothing Selected");
                break;
        }
    }








    @Override
    public void onBackPressed() {
       Intent I =new  Intent (setting.this,MainActivity.class);

       startActivity(I);
        this.finish();

    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");

    }

    private void saveNightMode(){
        SharedPreferences.Editor editor =nightmodedata.edit();
        editor.putString("active",nightmode);

        editor.apply();
      // Toast.makeText(this,"store",Toast.LENGTH_SHORT).show();
    }

}
