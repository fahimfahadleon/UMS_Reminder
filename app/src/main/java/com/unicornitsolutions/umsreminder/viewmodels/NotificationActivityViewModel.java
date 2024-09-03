package com.unicornitsolutions.umsreminder.viewmodels;

import androidx.lifecycle.ViewModel;

import com.unicornitsolutions.umsreminder.adapter.NotificationListAdapter;

public class NotificationActivityViewModel extends ViewModel {
    NotificationListAdapter adapter;
    public void setAdapter(NotificationListAdapter adapter){
        this.adapter = adapter;
    }
    public NotificationListAdapter getAdapter(){
        return adapter;
    }
}
