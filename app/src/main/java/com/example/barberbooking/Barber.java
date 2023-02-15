package com.example.barberbooking;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.barberbooking.utils.Appointment;

import java.util.ArrayList;
import java.util.List;

public class Barber extends User implements Parcelable {
    private String urlPhoto;
    private int price;
    private String address;
    private List<Appointment> appointments; // todo
    private List<String> unavailableDays = new ArrayList<>(); // 1-7

    public Barber(String userId, String email,
                  String name, String phone, String urlPhoto, int price, String address) {
        super(userId, email, name, phone);
        this.urlPhoto = urlPhoto;
        this.price = price;
        this.address = address;
    }

    public Barber() {

    }

    protected Barber(Parcel in) {
        urlPhoto = in.readString();
        price = in.readInt();
        address = in.readString();
        setUserId(in.readString());
        setEmail(in.readString());
        setName(in.readString());
        setPhone(in.readString());
    }

    public static final Creator<Barber> CREATOR = new Creator<Barber>() {
        @Override
        public Barber createFromParcel(Parcel in) {
            return new Barber(in);
        }

        @Override
        public Barber[] newArray(int size) {
            return new Barber[size];
        }
    };

    public List<String> getUnavailableDays() {
        return unavailableDays;
    }

    public void setUnavailableDays(List<String> unavailableDays) {
        this.unavailableDays = unavailableDays;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(getPhone());
        dest.writeString(urlPhoto);
        dest.writeInt(price);
        dest.writeString(address);
        dest.writeString(getUserId());
        dest.writeString(getEmail());
        dest.writeString(getName());
    }

}
