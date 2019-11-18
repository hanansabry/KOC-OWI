package com.android.kocowi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GC implements Parcelable {

    private String id;
    private String code;
    private String filedHeader;
    private String notes;

    public GC() {
    }

    protected GC(Parcel in) {
        id = in.readString();
        code = in.readString();
        filedHeader = in.readString();
        notes = in.readString();
    }

    public static final Creator<GC> CREATOR = new Creator<GC>() {
        @Override
        public GC createFromParcel(Parcel in) {
            return new GC(in);
        }

        @Override
        public GC[] newArray(int size) {
            return new GC[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFiledHeader() {
        return filedHeader;
    }

    public void setFiledHeader(String filedHeader) {
        this.filedHeader = filedHeader;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(code);
        dest.writeString(filedHeader);
        dest.writeString(notes);
    }
}
