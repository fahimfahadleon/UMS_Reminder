package com.unicornitsolutions.umsreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unicornitsolutions.umsreminder.databinding.SingleMachineModelBinding;
import com.unicornitsolutions.umsreminder.models.MachineModel;

import java.util.ArrayList;

public class MachineModelAdapter extends RecyclerView.Adapter<MachineModelAdapter.MachineViewHolder> {
    ArrayList<MachineModel> models;
    Context context;
    public MachineModelAdapter(Context context,ArrayList<MachineModel> models){
        this.models = models;
        this.context = context;
    }
    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MachineViewHolder(SingleMachineModelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, int position) {
        holder.setUpData(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MachineViewHolder extends RecyclerView.ViewHolder{
        SingleMachineModelBinding binding;
        MachineModel machineModel;
        public void setUpData(MachineModel machineModel){
            this.machineModel = machineModel;
            binding.name.setText(machineModel.getMachineModel());
            binding.power.setText(machineModel.getPower());
            binding.visitDate.setText(machineModel.getNextVisitDate());

            binding.getRoot().setOnClickListener(v -> {
                if(binding.invisibleLayout.getVisibility() == View.VISIBLE){
                    binding.invisibleLayout.setVisibility(View.GONE);
                }else binding.invisibleLayout.setVisibility(View.VISIBLE);
            });

            binding.machineModeltxt.setText("Machine Model: "+machineModel.getMachineModel());
            binding.powertxt.setText("Power(KW): "+machineModel.getPower());
            binding.visitDatetxt.setText("Visit Date: "+machineModel.getVisitDate());
            binding.nextVisitDatetxt.setText("Next Visit Date: "+machineModel.getNextVisitDate());
            PartModelAdapter adapter = new PartModelAdapter(context,machineModel.getModels());
            binding.partRecyclerView.setAdapter(adapter);
        }

        public MachineViewHolder(@NonNull SingleMachineModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
