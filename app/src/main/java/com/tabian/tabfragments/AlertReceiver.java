package com.tabian.tabfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;




public class AlertReceiver extends BroadcastReceiver {
    public static String Plug = "p";
    public static String Light1 = "lightalarm1";
    public static String Light2 = "lightalarm2";
    public static String Fan = "F";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getIntExtra( Plug,0)==1){
            Intent p = new Intent("Turnonplug");
            context.sendBroadcast(p);
        }

        if(intent.getIntExtra( Light1,0)==3){
            Intent l = new Intent("Turnonlight1");
            context.sendBroadcast(l);
        }

        if(intent.getIntExtra( Light2,0)==5){
            Intent L = new Intent("Turnonlight2");
            context.sendBroadcast(L);
        }
        if(intent.getIntExtra( Fan,0)==7){
            Intent F = new Intent("Turnonfan");
            context.sendBroadcast(F);
        }


        if(intent.getIntExtra( Light1,0)==4){
            Intent A = new Intent("Turnofflight1");
            context.sendBroadcast(A);
        }
        if(intent.getIntExtra(Light2 ,0)==6){
            Intent B = new Intent("Turnofflight2");
            context.sendBroadcast(B);
        }
        if(intent.getIntExtra( Fan,0)==8){
            Intent C= new Intent("Turnofffan");
            context.sendBroadcast(C);
        }
        if(intent.getIntExtra( Plug,0)==2){
            Intent D = new Intent("Turnoffplug");
            context.sendBroadcast(D);
        }


    }

}