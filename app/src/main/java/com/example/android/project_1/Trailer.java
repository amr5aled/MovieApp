package com.example.android.project_1;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by amr5aled on 3/10/2018.
 */

public class Trailer implements Parcelable{


    private String key;

    private String name;

    public Trailer(String key, String name){
        this.key = key;
        this.name = name;
    }

    protected Trailer(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
    }
}