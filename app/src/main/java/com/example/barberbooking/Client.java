package com.example.barberbooking;

import android.icu.text.MessagePattern;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.barberbooking.utils.Appointment;

import java.util.List;

public class Client extends User implements Parcelable {

    private String gender;  // stores the gender of the client
    private List<Appointment> appointments;  // a list to store appointments of the client,

    // constructor to initialize the properties of the Client object
    public Client(String userId, String email, String name, String phone, String gender) {
        // calling the parent class constructor to initialize the properties of the User class
        super(userId, email, name, phone);
        this.gender = gender;
    }

    // an empty constructor to create an instance of the Client object
    public Client() {}

    // a constructor that is called when the Client object is being retrieved from a parcel
    protected Client(Parcel in) {
        gender = in.readString();
    }

    // a CREATOR field that generates instances of the Client class from a parcel
    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    // a getter method to get the gender of the client
    public String getGender() {
        return gender;
    }

    // a setter method to set the gender of the client
    public void setGender(String gender) {
        this.gender = gender;
    }

    // a method that returns 0, as per the requirement of the Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    // a method that writes the contents of the Client object to a parcel
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(gender);
    }
}

