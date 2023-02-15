package com.example.barberbooking.ui.show_barber;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.barberbooking.Barber;
import com.example.barberbooking.Client;
import com.example.barberbooking.utils.Appointment;
import com.example.barberbooking.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowBarberViewModel extends ViewModel {


    public ShowBarberViewModel() {

    }



    public void toggleUnavailableDate(Barber barber, String day) {
        if(barber.getUnavailableDays().contains(day)) {
            barber.getUnavailableDays().remove(day);
        } else {
            barber.getUnavailableDays().add(day);
        }
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.BARBERS_TABLE)
                .child(barber.getUserId())
                .child("unavailableDays")
                .setValue(barber.getUnavailableDays());
    }

    private void createNewAppointment(Appointment appointment) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = ref.child(Constants.APPOINTMENTS_TABLE).push().getKey();
        appointment.setAppointmentId(key);
        ref.child(Constants.APPOINTMENTS_TABLE).child(key).setValue(appointment);

    }

    public void addAppointment(Appointment appointment)
    {
        createNewAppointment(appointment);
    }

}