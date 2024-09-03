package com.unicornitsolutions.umsreminder.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.adapter.ReminderListAdapter;
import com.unicornitsolutions.umsreminder.callbacks.SetAlarmCallback;
import com.unicornitsolutions.umsreminder.databinding.ActivityReminderListBinding;
import com.unicornitsolutions.umsreminder.functions.Functions;
import com.unicornitsolutions.umsreminder.models.MachineModel;
import com.unicornitsolutions.umsreminder.viewmodels.ReminderListViewModel;

import java.util.ArrayList;

public class ReminderListActivity extends AppCompatActivity {
    ActivityReminderListBinding binding;
    ReminderListViewModel model;
    ReminderListAdapter adapter;
    ArrayList<String>reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_reminder_list);
       model = new ViewModelProvider(this).get(ReminderListViewModel.class);
        binding.setViewModel(model);
        binding.setLifecycleOwner(this);
        reminders = new ArrayList<>();
        adapter = new ReminderListAdapter(this,reminders);
        model.setAdapter(adapter);
        setUpViewModel();


    }

    private void setUpViewModel() {

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
                adapter.notifyItemRangeInserted(0,reminders.size());


                for(String s: reminders){
                    String [] ar = s.split("_");
                    new Functions().setAlarm(ReminderListActivity.this, ar[2], ar[0], ar[1], new SetAlarmCallback() {
                        @Override
                        public void onSuccessful(String time) {

                        }

                        @Override
                        public void onFailed(String resone) {

                        }

                        @Override
                        public void onPassed(String s) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReminderListActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}