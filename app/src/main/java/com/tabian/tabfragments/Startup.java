package com.tabian.tabfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.Toast;

import java.lang.invoke.LambdaConversionException;

public class Startup extends AppCompatActivity {
private static int Time_out=300;
    int currentDayNight;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        sharedPreferences=getSharedPreferences("Nightmode", Context.MODE_PRIVATE);
        CheckNightModeActive();
        currentDayNight = AppCompatDelegate.getDefaultNightMode();
        new Handler().postDelayed(new Runnable() {
       @Override
       public void run() {
           Intent start = new Intent(Startup.this,DeviceList.class);
           startActivity(start);
           finish();

       }
   },Time_out);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentDayNight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();

        }
    }
    public void CheckNightModeActive() {
        if(sharedPreferences.getString("active","2").equals("1")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
           // Toast.makeText(this,"nightmoderestored",Toast.LENGTH_SHORT).show();

        }
        else if (sharedPreferences.getString("active","2").equals("2")){

            AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO));
            //Toast.makeText(this,"lightmode",Toast.LENGTH_SHORT).show();
        }
        else if (sharedPreferences.getString("active","2").equals("3")){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
           // Toast.makeText(this,"Automode",Toast.LENGTH_SHORT).show();
        }
    }
}
