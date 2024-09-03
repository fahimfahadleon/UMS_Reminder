package com.unicornitsolutions.umsreminder.models;

import java.util.ArrayList;

public class MachineModel {
    String machineModel;
    String power;
    String visitDate;
    String nextVisitDate;
    ArrayList<PartModel> models;

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(String nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    public ArrayList<PartModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<PartModel> models) {
        this.models = models;
    }
}
