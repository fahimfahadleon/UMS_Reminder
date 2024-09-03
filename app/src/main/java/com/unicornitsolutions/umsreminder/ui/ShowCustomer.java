package com.unicornitsolutions.umsreminder.ui;

import static com.unicornitsolutions.umsreminder.utils.Constants.DATA;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.adapter.MachineModelAdapter;
import com.unicornitsolutions.umsreminder.databinding.ActivityShowCustomerBinding;
import com.unicornitsolutions.umsreminder.models.CustomerModel;
import com.unicornitsolutions.umsreminder.models.MachineModel;
import com.unicornitsolutions.umsreminder.viewmodels.ShowCustomerViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShowCustomer extends AppCompatActivity {

    ActivityShowCustomerBinding binding;
    ShowCustomerViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_show_customer);
        model = new ViewModelProvider(this).get(ShowCustomerViewModel.class);
        binding.setViewModel(model);
        binding.setLifecycleOwner(this);
        setUpViewModel();
        CustomerModel customerModel = new Gson().fromJson(getIntent().getStringExtra(DATA), CustomerModel.class);
        model.customerModel = customerModel;
        model.companyName = "Company Name: "+customerModel.getCompanyName();
        model.address = "Address: "+customerModel.getAddress();
        model.contactPerson = "Contact Person: "+customerModel.getContactPerson();
        model.loaction = "Location: "+customerModel.getLocation();
        model.adapter = new MachineModelAdapter(this,customerModel.getModels());




    }

    private void setUpViewModel() {

    }
}