package com.example.barberbooking.ui.meetings;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.barberbooking.utils.Appointment;
import com.example.barberbooking.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
// meetingsViewModel is responsible for managing the
// appointments data and ensuring that the data is
// updated in real-time with changes made in the RealTime database.

public class MeetingsViewModel extends ViewModel {
    //declaring MutableLiveData to store the appointments and access it from the view
    private MutableLiveData<List<Appointment>> appointmentsLive = new MutableLiveData<>();
    //declaring a ValueEventListener for real-time updates on appointments
    private ValueEventListener appointmentsListener;


    //method to cancel an appointment by removing it from the database
    public void cancelAppointment(Appointment appointment) {
        //getting the reference to the appointments table in the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS_TABLE);
        databaseReference.child(appointment.getAppointmentId()).removeValue(); //Removing the appointment with the given id
    }
    //method to listen for appointments either for barber or client
    public void listenAppointments(boolean isBarber) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS_TABLE);
        appointmentsListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Appointment> appointments = new ArrayList<>(); //store the appointments in ArrayList

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {   /* iterating each appointment in the database*/
                    Appointment model = dataSnapshot.getValue(Appointment.class);
                    //check if the user is the client for this appointment
                    if (!isBarber && FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getClient_uid()))
                        appointments.add(model);
                        //check if the user is the barber..
                    else if (isBarber && FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getBarber_uid()))
                        appointments.add(model);
                }
                appointmentsLive.postValue(appointments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addValueEventListener(appointmentsListener); //start listening for updates
    }

    public LiveData<List<Appointment>> getAppointmentsLive() {
        return appointmentsLive;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(appointmentsListener != null) { /*removing the appointmentsListener when the viewmodel is cleared*/
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS_TABLE);
            databaseReference.removeEventListener(appointmentsListener);
        }
    }
}