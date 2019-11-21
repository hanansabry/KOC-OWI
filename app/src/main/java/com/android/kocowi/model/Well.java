package com.android.kocowi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Well implements Parcelable {

    private String id;
    private String name;
    private String notes;
    private double latitude;
    private double longitude;
    private String gcCode;


    public Well() {
    }

    protected Well(Parcel in) {
        id = in.readString();
        name = in.readString();
        notes = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        gcCode = in.readString();
    }

    public static final Creator<Well> CREATOR = new Creator<Well>() {
        @Override
        public Well createFromParcel(Parcel in) {
            return new Well(in);
        }

        @Override
        public Well[] newArray(int size) {
            return new Well[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGcCode() {
        return gcCode;
    }

    public void setGcCode(String gcCode) {
        this.gcCode = gcCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(notes);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(gcCode);
    }
}
