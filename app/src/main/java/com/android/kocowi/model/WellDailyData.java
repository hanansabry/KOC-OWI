package com.android.kocowi.model;

public class WellDailyData {

    public enum WellStatus {
        OPEN, CLOSE
    }

    private double whp;
    private double flp;
    private double temp;
    private String status;
    private String day;
    private String time;
    private Well well;
    private boolean approved;

    public WellDailyData(double whp, double flp, double temp, String status, String day, String time) {
        this.whp = whp;
        this.flp = flp;
        this.temp = temp;
        this.status = status;
        this.day = day;
        this.time = time;
    }

    public double getWhp() {
        return whp;
    }

    public void setWhp(double whp) {
        this.whp = whp;
    }

    public double getFlp() {
        return flp;
    }

    public void setFlp(double flp) {
        this.flp = flp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Well getWell() {
        return well;
    }

    public void setWell(Well well) {
        this.well = well;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
