package com.unicornitsolutions.umsreminder.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.adapter.NotificationListAdapter;
import com.unicornitsolutions.umsreminder.databinding.ActivityNotificationBinding;
import com.unicornitsolutions.umsreminder.functions.Functions;
import com.unicornitsolutions.umsreminder.models.NotificationModel;
import com.unicornitsolutions.umsreminder.viewmodels.NotificationActivityViewModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    ActivityNotificationBinding binding;
    NotificationActivityViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        model = new ViewModelProvider(this).get(NotificationActivityViewModel.class);
        binding.setViewModel(model);
        binding.setLifecycleOwner(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("notificationList");
        ArrayList<NotificationModel>data = new ArrayList<>();

        NotificationListAdapter adapter = new NotificationListAdapter(NotificationActivity.this,data);
        model.setAdapter(adapter);

        Functions.showLoading(this);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Functions.dismissLoading();
                for(DataSnapshot ds: snapshot.getChildren()){
                    NotificationModel s = ds.getValue(NotificationModel.class);
                    data.add(s);
                }
                if(!data.isEmpty()){
                    model.getAdapter().notifyItemRangeChanged(0,data.size());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NotificationActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });



    }
}