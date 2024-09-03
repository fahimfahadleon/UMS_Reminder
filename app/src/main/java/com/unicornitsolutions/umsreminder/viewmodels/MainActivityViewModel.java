package com.unicornitsolutions.umsreminder.viewmodels;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.utils.Constants;

public class MainActivityViewModel extends ViewModel {
    public MutableLiveData<String>getAction = new MutableLiveData<>();

    public void onClick(View view){
        int id = view.getId();
        Log.e("called","onLick");
        if(id == R.id.customerlist){
            getAction.setValue(Constants.ACTION_CUSTOMER_LIST);
        }else if(id == R.id.addNewCustomer){
            getAction.setValue(Constants.ACTION_ADD_NEW_CUSTOMER);
        }else if(id == R.id.reminders){
            getAction.setValue(Constants.ACTION_REMINDER_LIST);
        }else if(id == R.id.notification){
            getAction.setValue(Constants.ACTION_NOTIFICATION);
        }
    }

}
