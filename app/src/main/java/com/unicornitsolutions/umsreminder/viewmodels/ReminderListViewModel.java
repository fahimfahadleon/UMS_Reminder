package com.unicornitsolutions.umsreminder.viewmodels;

import androidx.lifecycle.ViewModel;

import com.unicornitsolutions.umsreminder.adapter.ReminderListAdapter;

public class ReminderListViewModel extends ViewModel {
    public ReminderListAdapter adapter;
    public void setAdapter(ReminderListAdapter adapter){
        this.adapter = adapter;
    }
    public ReminderListAdapter getAdapter(){
        return adapter;
    }

}
