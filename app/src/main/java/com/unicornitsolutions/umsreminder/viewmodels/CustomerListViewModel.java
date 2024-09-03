package com.unicornitsolutions.umsreminder.viewmodels;

import androidx.lifecycle.ViewModel;

import com.unicornitsolutions.umsreminder.adapter.CustomerListAdapter;

public class CustomerListViewModel extends ViewModel {
    CustomerListAdapter adapter;
    public void setAdapter(CustomerListAdapter adapter){
        this.adapter = adapter;
    }
    public CustomerListAdapter getAdapter(){
        return adapter;
    }

}
