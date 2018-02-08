package com.example.tonytuan.foodplace.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tony Tuan on 01/24/2018.
 */

public class Image implements Parcelable {
    public long id;
    public String name;
    public String path;
    public boolean isSelected;

    protected Image(Parcel in) {
        id = in.readLong();
        name = in.readString();
        path = in.readString();
    }

    public Image(long id, String name, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
    }
}
