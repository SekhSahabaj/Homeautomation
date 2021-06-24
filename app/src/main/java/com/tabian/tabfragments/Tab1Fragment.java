package com.tabian.tabfragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Created by User on 2/28/2017.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private   Button lg1on,lg1off,lgt2on,lgt2off,btncnlg1,btncnlg2;
    private TextView lg1time,lg2time;
    String slg1time,slg2time;
    private Calendar calendar = Calendar.getInstance();
    private final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private final int minute = calendar.get(Calendar.MINUTE);
    private Calendar calendartwo = Calendar.getInstance();
    private final int hourtwo = calendartwo.get(Calendar.HOUR_OF_DAY);
    private final int minutetwo = calendartwo.get(Calendar.MINUTE);
    private int light1val,light2val;
    private boolean light1state;
    private boolean light2state;


    onstatechange onstatechange;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        lg1on = (Button) view.findViewById(R.id.btnTEST);
        lg1off = (Button) view.findViewById(R.id.off);
        lgt2off=(Button)view.findViewById(R.id.lgt2off);
        lgt2on=(Button)view.findViewById(R.id.lgt2on);
        btncnlg1=(Button)view.findViewById(R.id.cancellight1);
        btncnlg2=(Button)view.findViewById(R.id.cancellight2);
       // btncnlg1.setVisibility(View.INVISIBLE);
        //btncnlg2.setVisibility(View.INVISIBLE);
        lg1time = (TextView) view.findViewById(R.id.light1alarmtext);
        lg2time = (TextView) view.findViewById(R.id.light2alarmtext);


        lg1on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onstatechange.statechange("on1");
                Toasty.success(getActivity(), "Light1 Enable",Toast.LENGTH_SHORT,true).show();

            }
        });

        lg1off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.error(getActivity(), "Light1 Disable",Toast.LENGTH_SHORT,true).show();
                onstatechange.statechange("off1");
            }
        });

        lgt2off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.error(getActivity(), "Light2 Disable",Toast.LENGTH_SHORT,true).show();
                onstatechange.statechange("Off2");
            }
        });

        lgt2on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.success(getActivity(), "Light2 Enable",Toast.LENGTH_SHORT,true).show();
                onstatechange.statechange("On2");
            }
        });
        btncnlg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {cancelAlarm();
            }
        });
        btncnlg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              cancelAlarmtwo();
            }
        });

        lg1off.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                timepickerone();
                light1val=4;
                slg1time="Light Off At:- ";
                return true;
            }
        });
        lg1on.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                timepickerone();
                light1val=3;
                slg1time="Light On At:- ";
                return true;
            }
        });
        lgt2off.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                timepickertwo();
                light2val=6;
                slg2time="Light2 Off At:- ";
                return true;
            }
        });
        lgt2on.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                timepickertwo();
                light2val=5;
                slg2time="Light2 On At:- ";
                return true;
            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            onstatechange = (onstatechange) context;
        }
        catch (Exception ex){


        }
    }

    public  interface onstatechange {

         void statechange(String state);
    }

    ////////////////////////////timer/////////////
    private void timepickerone() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar= Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                startAlarm(calendar);
                updateTimeTextone(calendar);
                btncnlg1.setVisibility(View.VISIBLE);
                light1state=true;
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

    private void updateTimeTextone(Calendar calendar) {

        slg1time += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        lg1time.setText(slg1time);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlertReceiver.class).putExtra("lightalarm1",light1val);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        slg1time="Timer canceled";
        lg1time.setText(slg1time);
        btncnlg1.setVisibility(View.INVISIBLE);
        light1state=false;
    }

    ////////////////////////////timertwo/////////////
    private void timepickertwo() {

        TimePickerDialog timePickerDialogtwo = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendartwo= Calendar.getInstance();
                calendartwo.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendartwo.set(Calendar.MINUTE, minute);
                calendartwo.set(Calendar.SECOND, 0);
                startAlarmtwo(calendartwo);
                updateTimeTexttwo(calendartwo);
                btncnlg2.setVisibility(View.VISIBLE);
                light2state=true;
            }
        }, hourtwo, minutetwo, android.text.format.DateFormat.is24HourFormat(getContext()));
        timePickerDialogtwo.show();
    }

    private void updateTimeTexttwo(Calendar calendartwo) {

        slg2time += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendartwo.getTime());
        lg2time.setText(slg2time);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarmtwo(Calendar calendartwo) {
        AlarmManager alarmManagertwo = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent2 = new Intent(getContext(), AlertReceiver.class).putExtra("lightalarm2",light2val);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendartwo.before(Calendar.getInstance())) {
            calendartwo.add(Calendar.DATE, 1);
        }

        alarmManagertwo.setExact(AlarmManager.RTC_WAKEUP, calendartwo.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarmtwo() {
        AlarmManager alarmManagertwo = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 2, intent2, 0);

        alarmManagertwo.cancel(pendingIntent);
        slg2time="Timer canceled";
        lg2time.setText(slg2time);
        btncnlg2.setVisibility(View.INVISIBLE);
        light2state=false;
    }



    @Override
    public void onResume() {

        super.onResume();
        lg1time.setText(slg1time);
        lg2time.setText(slg2time);

        if (light1state==true){
            btncnlg1.setVisibility(View.VISIBLE);
        }
        else btncnlg1.setVisibility(View.INVISIBLE);
        if (light2state==true){
            btncnlg2.setVisibility(View.VISIBLE);
        }
        else btncnlg2.setVisibility(View.INVISIBLE);


    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savTextlgone", slg1time);
        outState.putString("savTextlgtwo", slg1time);
        outState.putBoolean("btnkeylgone",light1state);
        outState.putBoolean("btnkeylgtwo",light2state);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null) {
            slg1time=(savedInstanceState.getString("savTextlgone",slg1time));
            light1state=(savedInstanceState.getBoolean("btnkeylgone",light1state));
            slg2time=(savedInstanceState.getString("savTextlgtwo",slg2time));
            light2state=(savedInstanceState.getBoolean("btnkeylgtwo",light2state));


        }

    }

}
