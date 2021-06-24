package com.tabian.tabfragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Created by User on 2/28/2017.
 */

public class Tab3Fragment extends Fragment {

    private static final String TAG = "Tab3Fragment";
    private Button plugoff, plugon, btncancel;
    private static onstatechange3 onstatechange3;




    private TextView mtext;
    private Calendar calendar = Calendar.getInstance();
    private final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private final int minute = calendar.get(Calendar.MINUTE);
    private int plugalarmset;
    String timeText;
    private boolean aBoolean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment, container, false);
        plugoff = (Button) view.findViewById(R.id.plugoff);
        plugon = (Button) view.findViewById(R.id.plugon);

        mtext = (TextView) view.findViewById(R.id.Timetext);
        btncancel = (Button) view.findViewById(R.id.btncancel);
        //btncancel.setVisibility(View.INVISIBLE);


        plugoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onstatechange3.statechange3("plugoff");
                Toasty.error(getActivity(), "Plug Disable", Toast.LENGTH_SHORT, true).show();
            }
        });
        plugon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onstatechange3.statechange3("plugon");
                Toasty.success(getActivity(), "Plug Enable", Toast.LENGTH_SHORT, true).show();
            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        plugoff.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                timepicker();
                plugalarmset = 2;
                timeText = "Plug Off At:- ";
                return true;
            }
        });
        plugon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                timepicker();
                plugalarmset = 1;
                timeText = "Plug On At:- ";
                return true;
            }
        });


        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onstatechange3 = (Tab3Fragment.onstatechange3) context;
        } catch (Exception ex) {

        }
    }

    public interface onstatechange3 {

        void statechange3(String state3);
    }


    ////////////////////////////timer/////////////
    private void timepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                startAlarm(calendar);
                updateTimeText(calendar);
                btncancel.setVisibility(View.VISIBLE);
                aBoolean = true;
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

    private void updateTimeText(Calendar calendar) {

        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        mtext.setText(timeText);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlertReceiver.class).putExtra("p", plugalarmset);
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
        timeText = "Timer canceled";
        mtext.setText(timeText);
        btncancel.setVisibility(View.INVISIBLE);
        aBoolean = false;
    }


    @Override
    public void onResume() {

        super.onResume();
        mtext.setText(timeText);
        if (aBoolean == true) {
            btncancel.setVisibility(View.VISIBLE);
        } else btncancel.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savText", timeText);
        outState.putBoolean("btnkey", aBoolean);
        //Toast.makeText(getContext(),"onsavevisible",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            timeText = (savedInstanceState.getString("savText", timeText));
            aBoolean = (savedInstanceState.getBoolean("btnkey", aBoolean));

        }

    }


}