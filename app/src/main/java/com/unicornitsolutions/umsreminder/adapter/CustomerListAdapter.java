package com.unicornitsolutions.umsreminder.adapter;

import static com.unicornitsolutions.umsreminder.utils.Constants.DATA;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.unicornitsolutions.umsreminder.databinding.SingleCustomerModelBinding;
import com.unicornitsolutions.umsreminder.models.CustomerModel;
import com.unicornitsolutions.umsreminder.ui.ShowCustomer;

import java.util.ArrayList;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerListViewModel> {
    ArrayList<CustomerModel> models;
    Context context;
    public CustomerListAdapter(Context context, ArrayList<CustomerModel> models){
        this.models = models;
        this.context = context;
    }
    @NonNull
    @Override
    public CustomerListViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerListViewModel(SingleCustomerModelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull CustomerListViewModel holder, int position) {
        holder.setUpData(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class CustomerListViewModel extends RecyclerView.ViewHolder{
        SingleCustomerModelBinding binding;
        CustomerModel machineModel;
        public void setUpData(CustomerModel machineModel){
            this.machineModel = machineModel;
            binding.name.setText(machineModel.getCompanyName());
            binding.getRoot().setOnClickListener(v -> {
                Intent i = new Intent(context, ShowCustomer.class);
                i.putExtra(DATA,new Gson().toJson(machineModel));
                context.startActivity(i);
            });
        }

        public CustomerListViewModel(@NonNull SingleCustomerModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
