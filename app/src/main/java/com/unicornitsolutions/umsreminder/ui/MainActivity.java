package com.unicornitsolutions.umsreminder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.callbacks.SetAlarmCallback;
import com.unicornitsolutions.umsreminder.databinding.ActivityMainBinding;
import com.unicornitsolutions.umsreminder.functions.Functions;
import com.unicornitsolutions.umsreminder.models.NotificationModel;
import com.unicornitsolutions.umsreminder.utils.Constants;
import com.unicornitsolutions.umsreminder.viewmodels.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainActivityViewModel model;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        model = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setViewModel(model);
        binding.setLifecycleOwner(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        if(savedInstanceState == null){
            return;
        }
        initViewModel();
        setUpRestOfdata();

    }

    private void setUpRestOfdata() {
        ArrayList<String>reminders =new ArrayList<>();

        Functions.showLoading(this);
        FirebaseDatabase.getInstance().getReference().child("reminderList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Functions.dismissLoading();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String s = ds.getKey();
                    String v = ds.getValue(String.class);
                    reminders.add(s+"_"+v);
                }
                for(String s: reminders){
                    String [] ar = s.split("_");
                    new Functions().setAlarm(MainActivity.this, ar[2], ar[0], ar[1], new SetAlarmCallback() {
                        @Override
                        public void onSuccessful(String time) {
                            //Toast.makeText(MainActivity.this, "New Alarm Set at "+time, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(String resone) {
                            Log.e("checking",resone);
                        }

                        @Override
                        public void onPassed(String s) {
                            Log.e("alarmPassed2",s);
                            String []child = s.split("_");
                            String key = child[0]+"_"+child[1];

                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setName(child[0]);
                            notificationModel.setMachine(child[1]);
                            notificationModel.setDate(child[2]);

                            reference.child("notificationList").child(key).setValue(notificationModel);
                            reference.child("reminderList").child(key).removeValue();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error",error.toString());
            }
        });
    }

    private void initViewModel() {
        model.getAction.observe(this,s->{
            switch (s){
                case Constants.ACTION_CUSTOMER_LIST:{
                    startActivity(CustomerListActivity.class);
                    break;
                }
                case Constants.ACTION_REMINDER_LIST:{
                    startActivity(ReminderListActivity.class);
                    break;
                }
                case Constants.ACTION_ADD_NEW_CUSTOMER:{
                    startActivity(AddNewCustomerActivity.class);
                    break;
                }
                case Constants.ACTION_NOTIFICATION:{
                    startActivity(NotificationActivity.class);
                    break;
                }
            }
        });
    }
    <T extends AppCompatActivity> void startActivity(Class<T> s){
        startActivity(new Intent(this,s));
    }
}