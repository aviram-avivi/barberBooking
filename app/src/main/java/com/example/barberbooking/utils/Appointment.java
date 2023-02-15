package com.example.barberbooking.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class Appointment implements Parcelable {
    private String appointmentId;
    private String barber_uid;
    private String barber_name;
    private String client_uid;
    private String client_name;
    private String date;
    private BarberCalendarHour barberCalendarHour;

    public Appointment(String appointmentId, String barber_uid, String barber_name,String client_uid, String client_name,String date, BarberCalendarHour barberCalendarHour) {
        this.appointmentId = appointmentId;
        this.barber_uid = barber_uid;
        this.barber_name = barber_name;
        this.client_uid = client_uid;
        this.client_name = client_name;
        this.barberCalendarHour = barberCalendarHour;
        this.date = date;
    }

    public Appointment() {}
    // constructor to recreate the Appointment instance from the Parcel object
    protected Appointment(Parcel in) {
        appointmentId = in.readString();
        barber_uid = in.readString();
        barber_name  =in.readString();
        client_uid = in.readString();
        client_name = in.readString();
        date = in.readString();
    }

    // getter method to return the BarberCalendarHour object
    public BarberCalendarHour getBarberCalendarHour() {
        return barberCalendarHour;
    }

    // CREATOR object required by Parcelable to deserialize the Parcel object to an Appointment object
    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public String getBarber_uid() {
        return barber_uid;
    }
    public String getClient_uid() {
        return client_uid;
    }
    public String getDate() {
        return date;
    }
    public String getAppointmentId() {
        return appointmentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(appointmentId);
        dest.writeString(barber_uid);
        dest.writeString(barber_name);
        dest.writeString(client_uid);
        dest.writeString(client_name);
    }

    public void setAppointmentId(String key)
    {
        this.appointmentId = key;
    }

    public void setBarber_uid(String id)
    {
        this.barber_uid = id;
    }

    public void setClient_uid(String id)
    {
        this.client_uid = id;
    }

    public String getBarber_name() {
        return barber_name;
    }

    public String getClient_name() {
        return client_name;
    }
}