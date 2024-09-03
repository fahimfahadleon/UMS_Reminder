package com.unicornitsolutions.umsreminder.utils;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.adapter.PartModelAdapter;
import com.unicornitsolutions.umsreminder.callbacks.OnAlertDialogEventListener;
import com.unicornitsolutions.umsreminder.databinding.AddMachineBinding;
import com.unicornitsolutions.umsreminder.databinding.AddNewPartBinding;
import com.unicornitsolutions.umsreminder.models.MachineModel;
import com.unicornitsolutions.umsreminder.models.PartModel;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AlertDialogViewer {

    OnAlertDialogEventListener listener;
    String[] contents;
    Context context;

    AlertDialog ad;
    AlertDialog.Builder builder;
    LayoutInflater inflater;

    public static final String ALERTDIALOG_TYPE_ADD_MACHINE = "add_machine";
    public static final String ALERTDIALOG_TYPE_ADD_PART = "add_part";
    public static final String REPLAY_TYPE_SAVE = "save_data";
    public static final String REPLAY_TYPE_SAVE_PART = "save_part";
    public static final String REPLAY_TYPE_CANCEL_PART = "cancel_part";

    public AlertDialogViewer(Context context, OnAlertDialogEventListener listener, String... content) {
        this.contents = content;
        this.context = context;
        this.listener = listener;

        builder = new AlertDialog.Builder(context, R.style.Base_Theme_UMSReminder);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            init();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws JSONException {
        switch (contents[0]) {
            case ALERTDIALOG_TYPE_ADD_MACHINE: {
                openAddNewMachineDialog();
//                openAddNewMachineDialog(contents[1]);
                break;
            }
            case ALERTDIALOG_TYPE_ADD_PART: {
                openAddNewPartDialog();
//                openAddNewMachineDialog(contents[1]);
                break;
            }

        }
    }

    private void openAddNewMachineDialog() {
        AddMachineBinding binding = AddMachineBinding.inflate(inflater);
        ArrayList<PartModel>models = new ArrayList<>();
        PartModelAdapter adapter = new PartModelAdapter(context,models);
        binding.partRecyclerView.setAdapter(adapter);

        binding.saveButton.setOnClickListener(v -> {

            if(!binding.machineModel.getText().toString().isEmpty() &&
                    !binding.power.getText().toString().isEmpty() &&
                    !binding.visitDate.getText().toString().isEmpty() &&
                    !binding.nextVisitDate.getText().toString().isEmpty() &&
            !models.isEmpty()){
                MachineModel machineModel = new MachineModel();
                machineModel.setMachineModel(binding.machineModel.getText().toString());
                machineModel.setPower(binding.power.getText().toString());
                machineModel.setVisitDate(binding.visitDate.getText().toString());
                machineModel.setNextVisitDate(binding.nextVisitDate.getText().toString());
                machineModel.setModels(models);
                listener.onEvent(REPLAY_TYPE_SAVE, new Gson().toJson(machineModel));
                ad.dismiss();
            }else {
                Toast.makeText(context, "Data is Missing or Part is Empty!", Toast.LENGTH_SHORT).show();
            }



        });
        binding.visitDate.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager manager = activity.getSupportFragmentManager();
            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select date")
                            .build();
            datePicker.show(manager,"something");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(selection));
                binding.visitDate.setText(dateString);
            });
        });
        binding.nextVisitDate.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager manager = activity.getSupportFragmentManager();
            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select date")
                            .build();
            datePicker.show(manager,"something2");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(selection));
                Log.e("nextdate",dateString);
                binding.nextVisitDate.setText(dateString);
            });
        });
        binding.fabIcon.setOnClickListener(v -> new AlertDialogViewer(context, event -> {
            if (event[0].equals(REPLAY_TYPE_SAVE_PART)) {
                PartModel partModel = new Gson().fromJson(event[1], PartModel.class);
                models.add(partModel);
                adapter.notifyItemInserted(models.indexOf(partModel));
            }
        },ALERTDIALOG_TYPE_ADD_PART));


        builder.setView(binding.getRoot());
        ad = builder.create();
        ad.show();
        performPostAction();
    }

    private void openAddNewPartDialog() {
        AddNewPartBinding binding = AddNewPartBinding.inflate(inflater);
        binding.save.setOnClickListener(v -> {

            if(!binding.name.getText().toString().isEmpty() && !binding.brand.getText().toString().isEmpty() && !binding.partNumber.getText().toString().isEmpty()){
                PartModel partModel = new PartModel();
                partModel.setName(binding.name.getText().toString());
                partModel.setBrand(binding.brand.getText().toString());
                partModel.setPartNumber(binding.partNumber.getText().toString());

                listener.onEvent(REPLAY_TYPE_SAVE_PART, new Gson().toJson(partModel));
                ad.dismiss();
            }else {
                Toast.makeText(context, "Required Fields Are Missing.", Toast.LENGTH_SHORT).show();
            }



        });


        binding.cancel.setOnClickListener(v -> {
            listener.onEvent(REPLAY_TYPE_CANCEL_PART);
            ad.dismiss();
        });


        builder.setView(binding.getRoot());
        ad = builder.create();
        ad.show();
        performPostAction();
    }


    void performPostAction() {
        Objects.requireNonNull(ad.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ad.getWindow().setGravity(Gravity.CENTER);
        ad.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ad.getWindow().setDimAmount(0.5f);
    }

}
