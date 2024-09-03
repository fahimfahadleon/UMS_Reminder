package com.unicornitsolutions.umsreminder.functions;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.callbacks.SetAlarmCallback;
import com.unicornitsolutions.umsreminder.databinding.LoadingBinding;
import com.unicornitsolutions.umsreminder.utils.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Functions {
    public Calendar getAlarmAt9Time(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.HOUR_OF_DAY, 9);  // Set hour to 9 AM
            calendar.set(Calendar.MINUTE, 0);       // Set minutes to 00
            calendar.set(Calendar.SECOND, 0);       // Set seconds to 00
            calendar.set(Calendar.MILLISECOND, 0);  // Set milliseconds to 00
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
//        calendar.add(Calendar.MINUTE, 5);
        return calendar;
    }
    static Dialog dialog;
    public static void showLoading(Context context){
        dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LoadingBinding binding = LoadingBinding.inflate(inflater);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
        }



        dialog.show();
    }
    public static void dismissLoading(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void setAlarm(Context context, String dateInMillis, String companyName, String machineName, SetAlarmCallback setAlarmCallback) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Get the alarm time
        Calendar alarmTime = getAlarmAt9Time(dateInMillis);

        if(alarmTime.getTimeInMillis()<System.currentTimeMillis()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            try {
                setAlarmCallback.onPassed(companyName+"_"+machineName+"_"+dateFormat.format(new Date(dateInMillis)));
            }catch (Exception e){
                e.fillInStackTrace();
            }
            return;
        }

        // Create an Intent to broadcast when the alarm goes off
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("companyName", companyName);
        intent.putExtra("machineName", machineName);
        // Create a PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if(pendingIntent!=null){
            // Set the alarm
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager != null && alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                    setAlarmCallback.onSuccessful("");
                }else {
                    setAlarmCallback.onFailed("else statement line 58");
                }
            }else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                setAlarmCallback.onSuccessful("");
            }
            Toast.makeText(context, "Alarm is set at :"+dateInMillis+" 9.00 am.", Toast.LENGTH_SHORT).show();
        }else {
            setAlarmCallback.onFailed("alarm already set");
        }

    }
}
