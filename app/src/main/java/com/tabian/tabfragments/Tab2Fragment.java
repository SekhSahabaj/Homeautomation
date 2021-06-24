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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

import static java.lang.String.valueOf;

/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private Button fanon, fanoff, btncancelfrag2;
    onstatechange2 onstatechange2;
    onstatestring onstatestring;
    SeekBar speed;
    TextView setspeed, Timetextfrag2;
    String timeTextfrag2;
    private Calendar calendar = Calendar.getInstance();
    private final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private final int minute = calendar.get(Calendar.MINUTE);
    private int fanalarmset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        fanoff = (Button) view.findViewById(R.id.fanoff);
        fanon = (Button) view.findViewById(R.id.fanon);
        btncancelfrag2 = (Button) view.findViewById(R.id.btncancelfrag2);
        btncancelfrag2.setVisibility(View.INVISIBLE);
        speed = (SeekBar) view.findViewById(R.id.seekBar);
        setspeed = (TextView) view.findViewById(R.id.setspeed);
        Timetextfrag2 = (TextView) view.findViewById(R.id.timetextfrag2);
        seek_bar();

        fanoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.error(getActivity(), "Fan Disable", Toast.LENGTH_SHORT, true).show();
                onstatestring.statestatestring("fanoff");
            }
        });
        fanon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.success(getActivity(), "Fan Enable", Toast.LENGTH_SHORT, true).show();
                onstatestring.statestatestring("fanon");
            }
        });
        btncancelfrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
        fanoff.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                timepicker();
                fanalarmset = 8;
                timeTextfrag2 = "Fan Off At:- ";
                return true;
            }
        });
        fanon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                timepicker();
                fanalarmset = 7;
                timeTextfrag2 = "Fan On At:- ";
                return true;
            }
        });


        return view;


    }

    private void seek_bar() {

        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    setspeed.setText(valueOf(progress));
                    onstatechange2.statechange2(seekBar.getProgress());

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            onstatechange2 = (onstatechange2) context;
            onstatestring = (onstatestring) context;
        } catch (Exception ex) {


        }
    }

    public interface onstatechange2 {

        void statechange2(int state2);
    }

    public interface onstatestring {

        void statestatestring(String state3);
    }


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
                btncancelfrag2.setVisibility(View.VISIBLE);
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

    private void updateTimeText(Calendar calendar) {

        timeTextfrag2 += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        Timetextfrag2.setText(timeTextfrag2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlertReceiver.class).putExtra("F", fanalarmset);
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
        Timetextfrag2.setText("Timer canceled");
        btncancelfrag2.setVisibility(View.INVISIBLE);
    }


}

