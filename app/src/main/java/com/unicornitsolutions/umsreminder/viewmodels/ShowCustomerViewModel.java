package com.unicornitsolutions.umsreminder.viewmodels;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.unicornitsolutions.umsreminder.adapter.MachineModelAdapter;
import com.unicornitsolutions.umsreminder.models.CustomerModel;
import com.unicornitsolutions.umsreminder.models.MachineModel;

public class ShowCustomerViewModel extends ViewModel {
    public MachineModelAdapter adapter;
    public CustomerModel customerModel;
    public String companyName = "Company Name";
    public String address = "Company Address";
    public String contactPerson = "Contact Person";
    public String loaction = "Company Location";
    public void onClick(View view){

    }

}
