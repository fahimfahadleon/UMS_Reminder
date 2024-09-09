package com.unicornitsolutions.umsreminder.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;


import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.adapter.MachineModelAdapter;
import com.unicornitsolutions.umsreminder.callbacks.SetAlarmCallback;
import com.unicornitsolutions.umsreminder.databinding.ActivityAddNewCustomerBinding;
import com.unicornitsolutions.umsreminder.functions.Functions;
import com.unicornitsolutions.umsreminder.models.CustomerModel;
import com.unicornitsolutions.umsreminder.models.MachineModel;
import com.unicornitsolutions.umsreminder.utils.AlertDialogViewer;
import com.unicornitsolutions.umsreminder.utils.Constants;
import com.unicornitsolutions.umsreminder.utils.SingleShotLocationProvider;
import com.unicornitsolutions.umsreminder.viewmodels.AddNewCustomerViewModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewCustomerActivity extends AppCompatActivity {
    ActivityAddNewCustomerBinding binding;
    AddNewCustomerViewModel model;
    CustomerModel customerModel;
    ArrayList<MachineModel> machineModels;
    FirebaseDatabase database;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_new_customer);
        model = new ViewModelProvider(this).get(AddNewCustomerViewModel.class);
        binding.setViewModel(model);
        binding.setLifecycleOwner(this);
        customerModel = new CustomerModel();
        machineModels = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        initModel();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it again
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5000);
            return;
        }


        SingleShotLocationProvider.requestSingleUpdate(this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        binding.location.setText(location.latitude+","+location.longitude);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location
                getCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openGoogleMaps(double latitude, double longitude) {
        String geoUri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
        Uri gmmIntentUri = Uri.parse(geoUri);

        // Create an intent to open Google Maps
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        // Check if there's an app that can handle the intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void initModel() {
        MachineModelAdapter adapter = new MachineModelAdapter(this,machineModels);
        model.setAdapter(adapter);
        model.getAction.observe(this,s->{
            switch (s){
                case Constants.ACTION_SAVE_CUSTOMER_DATA:{
                    makeRequest();
                    break;
                }
                case Constants.ACTION_OPEN_MAP:{
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Request location permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5000);
                    } else {
                        // Get the current location if permission is already granted
                        getCurrentLocation();
                    }
                    break;
                }
                case Constants.ACTION_SET_LOCATION:{
                        if(binding.location.getText().toString().isEmpty()){
                            return;
                        }else {
                            String latlong = binding.location.getText().toString();
                            String[]loc = latlong.split(",");
                            openGoogleMaps(Double.parseDouble(loc[0].trim()),Double.parseDouble(loc[1].trim()));
                        }
                    break;
                }
                case Constants.ACTION_ADD_NEW_MACHINE:{
                    new AlertDialogViewer(AddNewCustomerActivity.this, event -> {
                        if(event[0].equals(AlertDialogViewer.REPLAY_TYPE_SAVE)){
                            MachineModel model1 =new Gson().fromJson(event[1], MachineModel.class);
                            machineModels.add(model1);
                            adapter.notifyItemInserted(machineModels.indexOf(model1));
                        }

                    },AlertDialogViewer.ALERTDIALOG_TYPE_ADD_MACHINE);
                    break;
                }
            }
        });
    }
    boolean bo = true;
    private void makeRequest() {
        if(!binding.companyname.getText().toString().isEmpty()&&
        !binding.address.getText().toString().isEmpty() &&
        !binding.location.getText().toString().isEmpty() &&
        !binding.contactperson.getText().toString().isEmpty() &&
        !machineModels.isEmpty()){

            String companyName = binding.companyname.getText().toString();

            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(binding.companyname.getText().toString());
            boolean b = m.find();


            if (b){
                Toast.makeText(this, "Company Name must not contain special character or other language than english!", Toast.LENGTH_SHORT).show();
            }else {
                customerModel =new CustomerModel();
                customerModel.setCompanyName(companyName);
                customerModel.setAddress(binding.address.getText().toString());
                customerModel.setLocation(binding.location.getText().toString());
                customerModel.setContactPerson(binding.contactperson.getText().toString());
                customerModel.setModels(machineModels);

                Functions.showLoading(this);

                reference.child("customerList").child(companyName).setValue(customerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Functions.dismissLoading();
                    }
                }).addOnFailureListener(e -> {
                    Functions.dismissLoading();
                    Toast.makeText(AddNewCustomerActivity.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                    bo = false;
                });
                if(!bo){
                    return;
                }

                for(MachineModel machineModel: customerModel.getModels()){
                    reference.child("reminderList").child(companyName+"_"+machineModel.getMachineModel()).setValue(machineModel.getNextVisitDate());
                    new Functions().setAlarm(this, machineModel.getNextVisitDate(), companyName, machineModel.getMachineModel(), new SetAlarmCallback() {
                        @Override
                        public void onSuccessful(String time) {
                            binding.address.setText(null);
                            binding.companyname.setText(null);
                            binding.contactperson.setText(null);
                            binding.location.setText(null);
                            int size = machineModels.size();
                            machineModels.clear();
                            model.getAdapter().notifyItemRangeRemoved(0, size);

                        }

                        @Override
                        public void onFailed(String resone) {
                            Toast.makeText(AddNewCustomerActivity.this, "Failed To Upload Data!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPassed(String s) {
                            Toast.makeText(AddNewCustomerActivity.this, "Alarm Already Passed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            finish();
        }else {
            Toast.makeText(this, "Data is not filled", Toast.LENGTH_SHORT).show();
        }
    }





}