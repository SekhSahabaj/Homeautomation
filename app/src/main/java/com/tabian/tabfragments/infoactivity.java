package com.tabian.tabfragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import maes.tech.intentanim.CustomIntent;


public class infoactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoactivity);
        ActionBar actionBar =getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle("About");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(infoactivity.this, MainActivity.class);
startActivity(i);
this.finish();
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(infoactivity.this,"right-to-left");

    }
}
