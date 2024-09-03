package com.unicornitsolutions.umsreminder.models;

import java.util.ArrayList;

public class CustomerModel {
    String companyName;
    String address;
    String contactPerson;
    String location;
    ArrayList<MachineModel>models;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<MachineModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<MachineModel> models) {
        this.models = models;
    }
}
