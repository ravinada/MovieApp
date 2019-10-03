package com.ravinada.moviemania.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerData implements Parcelable {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    protected TrailerData(Parcel in) {
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    public static final Creator<TrailerData> CREATOR = new Creator<TrailerData>() {
        @Override
        public TrailerData createFromParcel(Parcel in) {
            return new TrailerData(in);
        }

        @Override
        public TrailerData[] newArray(int size) {
            return new TrailerData[size];
        }
    };

    public String getKey() {
        return key;
    }

    public String getImageURL() {
        return "https://img.youtube.com/vi/"+key+"/0.jpg";
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(type);
    }
}