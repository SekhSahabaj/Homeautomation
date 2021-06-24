package com.tabian.tabfragments;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements Tab1Fragment.onstatechange,
        Tab2Fragment.onstatechange2,Tab3Fragment.onstatechange3,Tab2Fragment.onstatestring{

    int currentDayNight;

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    String address = null;
    String Address;///////save new address here from prev. activity

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaddata();/////////////load bt address

        registerReceiver(alertReceiver, new IntentFilter("Turnoffplug"));
        registerReceiver(alertReceiver, new IntentFilter("Turnonplug"));

        registerReceiver(alertReceiver, new IntentFilter("Turnonlight1"));
        registerReceiver(alertReceiver, new IntentFilter("Turnofflight1"));

        registerReceiver(alertReceiver, new IntentFilter("Turnonlight2"));
        registerReceiver(alertReceiver, new IntentFilter("Turnofflight2"));

        registerReceiver(alertReceiver, new IntentFilter("Turnonfan"));
        registerReceiver(alertReceiver, new IntentFilter("Turnofffan"));

        new ConnectBT().execute(); //Call the class to connect

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        currentDayNight = AppCompatDelegate.getDefaultNightMode();

    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Light");
        adapter.addFragment(new Tab2Fragment(), "Fan");
        adapter.addFragment(new Tab3Fragment(), "Plug");
        viewPager.setAdapter(adapter);
    }


    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("F".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("O".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOnLed2()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("A".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOffLed2()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("B".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOfffan()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("E".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOnfan()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("C".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOnplug()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("P".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOffplug()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".getBytes());

                btSocket.getOutputStream().write("D".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    private void msginfo(String i)
    {
        Toasty.info(getApplicationContext(),i,Toast.LENGTH_LONG,true).show();
    }

    private void msgwarning(String w)
    {
        Toasty.warning(getApplicationContext(),w,Toast.LENGTH_LONG,true).show();
    }

    private void msgerror(String e)
    {
        Toasty.error(getApplicationContext(),e,Toast.LENGTH_LONG,true).show();
    }
    private void msgsuccess(String e)
    {
        Toasty.success(getApplicationContext(),e,Toast.LENGTH_LONG,true).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dis_id:
                Disconnect();
                myBluetooth.disable();
           msginfo("Bluetooth Disconnected");
           return true;
            case R.id.action_settings:

                Intent s = new Intent(MainActivity.this, setting.class);
                startActivity(s);
                CustomIntent.customType(this,"left-to-right");

                Disconnect();
                    this.finish();
                return true;
            case R.id.information:
                //Toast.makeText(this,"Info",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, infoactivity.class);
                startActivity(i);
                CustomIntent.customType(this,"left-to-right");
                return true;
                default:
                return super.onOptionsItemSelected(item);


        }


    }

    @Override
    public void statechange(String state) {
        if(state.equals("on1")){

            turnOnLed();
        }
        else if (state.equals("off1")){

            turnOffLed();
        }
        else if (state.equals("Off2")){

            turnOffLed2();
        }
        else if (state.equals("On2")){

            turnOnLed2();
        }
    }


    @Override
    public void statechange3(String state3) {
        if(state3.equals("plugon")){
          turnOnplug();
        }
      else if(state3.equals("plugoff")){
          turnOffplug();
        }
    }

    @Override
    public void statechange2(int state2) {
        try {
            btSocket.getOutputStream().write("2".getBytes());

            btSocket.getOutputStream().write(String.valueOf(state2).getBytes());
        } catch (IOException e) {
        }
    }
    @Override
    public void statestatestring(String state3) {
        if (state3.equals("fanoff")){
            turnOfffan();
        }
        else if (state3.equals("fanon")){
            turnOnfan();
        }
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msgwarning("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msginfo("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        msginfo("Bluetooth Disconnected.");
        Disconnect();
        myBluetooth.disable();
        this.finish();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentDayNight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();
        }
    }


    public void loaddata(){

        SharedPreferences sharedPreferences=getSharedPreferences("adu",MODE_PRIVATE);////address save id,mode should be same
        address = sharedPreferences.getString("adukey",Address);/// key,value

    }
    AlertReceiver alertReceiver =  new AlertReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            if("Turnonplug".equals(intent.getAction())){ turnOnplug();
            msgsuccess("Plug Turned On");    }

            if("Turnonlight1".equals(intent.getAction())){ turnOnLed();
                msgsuccess("Light1 Turned On" );}

            if("Turnonlight2".equals(intent.getAction())){ turnOnLed2();
                msgsuccess("Light2 Turned On");}

            if("Turnonfan".equals(intent.getAction())){ turnOnfan();
            msgsuccess("Fan Turned On");    }

            ///////////////////////////off states//////////////////////////////
           if("Turnoffplug".equals(intent.getAction())){ turnOffplug();
        msgerror("Plug Turned Off");   }

            if("Turnofflight1".equals(intent.getAction())){ turnOffLed();
          msgerror("Light1 Turned Off");    }

            if("Turnofflight2".equals(intent.getAction())){ turnOffLed2();
          msgerror("Light2 Turned Off");  }

            if("Turnofffan".equals(intent.getAction())){ turnOfffan();
             msgerror("Fan Turned Off");}

        }
    };


}
