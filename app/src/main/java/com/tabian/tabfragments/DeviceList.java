package com.tabian.tabfragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;


public class DeviceList extends AppCompatActivity
{
    //widgets
    Button btnPaired;
    ListView devicelist;
    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    int currentDayNight;
  SharedPreferences sharedPreferencesaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        //Calling widgets
       sharedPreferencesaddress =getSharedPreferences("adu",MODE_PRIVATE);////address save id,mode should be same

        btnPaired = (Button)findViewById(R.id.button);
        devicelist = (ListView)findViewById(R.id.listView);

        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (! myBluetooth.isEnabled()) {
            myBluetooth.enable();
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList();
            }
        });
        currentDayNight = AppCompatDelegate.getDefaultNightMode();
    }


    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toasty.warning(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG,true).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }


    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            SharedPreferences.Editor editor = sharedPreferencesaddress.edit();
            editor.putString("adukey",address); /// key,value
            editor.apply(); ///apply changes
            // Make an intent to start next activity.
            Intent i = new Intent(DeviceList.this, MainActivity.class);
            //Change the activity.
            startActivity(i);
            CustomIntent.customType(DeviceList.this,"left-to-right");
            finish(); //close this activity

        }
    };




    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DeviceList.this);
        builder.setMessage("Are You Sure To Quit This App");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myBluetooth.disable();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentDayNight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();

        }
    }

}
