package com.unicornitsolutions.umsreminder.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.adapter.CustomerListAdapter;
import com.unicornitsolutions.umsreminder.databinding.ActivityCustomerListBinding;
import com.unicornitsolutions.umsreminder.functions.Functions;
import com.unicornitsolutions.umsreminder.models.CustomerModel;
import com.unicornitsolutions.umsreminder.viewmodels.CustomerListViewModel;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    ActivityCustomerListBinding binding;
    CustomerListViewModel model;
    ArrayList<CustomerModel>models;
    CustomerListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_customer_list);
        model = new ViewModelProvider(this).get(CustomerListViewModel.class);
        binding.setViewModel(model);
        binding.setLifecycleOwner(this);
        models = new ArrayList<>();
        adapter = new CustomerListAdapter(this,models);
        initViewModel();

        Functions.showLoading(this);
        FirebaseDatabase.getInstance().getReference().child("customerList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Functions.dismissLoading();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    CustomerModel yourClass = ds.getValue(CustomerModel.class);
                    models.add(yourClass);
                }
                adapter.notifyItemRangeInserted(0,models.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerListActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initViewModel() {
        model.setAdapter(adapter);
    }
}