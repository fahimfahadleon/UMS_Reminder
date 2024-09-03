package com.unicornitsolutions.umsreminder.adapter;



import static com.unicornitsolutions.umsreminder.utils.Constants.DATA;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.unicornitsolutions.umsreminder.databinding.SingleNotificationItemBinding;
import com.unicornitsolutions.umsreminder.models.CustomerModel;
import com.unicornitsolutions.umsreminder.models.NotificationModel;
import com.unicornitsolutions.umsreminder.ui.ShowCustomer;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationListViewHolder> {
    ArrayList<NotificationModel> models;
    Context context;
    public NotificationListAdapter(Context context, ArrayList<NotificationModel> models){
        this.models = models;
        this.context = context;
    }
    @NonNull
    @Override
    public NotificationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationListViewHolder(SingleNotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListViewHolder holder, int position) {
        holder.setUpData(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class NotificationListViewHolder extends RecyclerView.ViewHolder{
        SingleNotificationItemBinding binding;
        NotificationModel machineModel;
        CustomerModel customerModel = null;

        public void setUpData(NotificationModel machineModel){
            this.machineModel = machineModel;
            Log.e("Items",machineModel.toString());
            binding.companyname.setText(machineModel.getName());
            binding.machineName.setText(machineModel.getMachine());
            binding.time.setText(machineModel.getDate());
            binding.getRoot().setOnClickListener(v -> FirebaseDatabase.getInstance().getReference().child("customerList").child(machineModel.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    customerModel = snapshot.getValue(CustomerModel.class);
                    if(customerModel!=null){
                        Intent i = new Intent(context, ShowCustomer.class);
                        i.putExtra(DATA,new Gson().toJson(customerModel));
                        context.startActivity(i);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        public NotificationListViewHolder(@NonNull SingleNotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
