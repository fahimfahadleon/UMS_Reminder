package com.unicornitsolutions.umsreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unicornitsolutions.umsreminder.databinding.SinglePartModelBinding;
import com.unicornitsolutions.umsreminder.models.PartModel;

import java.util.ArrayList;

public class PartModelAdapter extends RecyclerView.Adapter<PartModelAdapter.PartViewHolder> {
    ArrayList<PartModel> models;
    Context context;
    public PartModelAdapter(Context context, ArrayList<PartModel> models){
        this.models = models;
        this.context = context;
    }
    @NonNull
    @Override
    public PartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PartViewHolder(SinglePartModelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PartViewHolder holder, int position) {
        holder.setUpData(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class PartViewHolder extends RecyclerView.ViewHolder{
        SinglePartModelBinding binding;
        PartModel partModel;
        public void setUpData(PartModel machineModel){
            this.partModel = machineModel;
            binding.name.setText(partModel.getName());
            binding.brand.setText(partModel.getBrand());
            binding.partnumber.setText(partModel.getPartNumber());
        }

        public PartViewHolder(@NonNull SinglePartModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
