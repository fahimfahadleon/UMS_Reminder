package com.unicornitsolutions.umsreminder.viewmodels;



import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.adapter.MachineModelAdapter;
import com.unicornitsolutions.umsreminder.utils.Constants;

import javax.crypto.Mac;

public class AddNewCustomerViewModel extends ViewModel {
    MachineModelAdapter adapter;
    public MutableLiveData<String> getAction = new MutableLiveData<>();
    public MachineModelAdapter getAdapter(){
        return adapter;
    }
    public void setAdapter(MachineModelAdapter adapter){
        this.adapter = adapter;
    }

    public void onClick(View vi) {
        int id = vi.getId();
        if(id == R.id.saveButton){
            getAction.setValue(Constants.ACTION_SAVE_CUSTOMER_DATA);
        }else if(id == R.id.location){
            getAction.setValue(Constants.ACTION_SET_LOCATION);
        }else if(id == R.id.fabIcon){
            getAction.setValue(Constants.ACTION_ADD_NEW_MACHINE);
        }else if(id == R.id.openMap){
            getAction.setValue(Constants.ACTION_OPEN_MAP);
        }
    }
}
