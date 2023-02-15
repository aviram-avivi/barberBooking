package com.example.barberbooking.ui.show_barber;


import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barberbooking.Barber;
import com.example.barberbooking.Client;
import com.example.barberbooking.R;
import com.example.barberbooking.databinding.FragmentShowBarberBinding;
import com.example.barberbooking.ui.meetings.MeetingsCalendarAdapter;
import com.example.barberbooking.utils.Appointment;
import com.example.barberbooking.utils.BarberCalendarHour;
import com.example.barberbooking.utils.Constants;
import com.example.barberbooking.utils.IMeetingsCalendar;
import com.example.barberbooking.utils.OnFirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ShowBarberFragment extends Fragment implements IMeetingsCalendar {


    private ShowBarberViewModel showBarberViewModel;
    private Barber selectedBarber;
    private Client currentClient;
    ImageView imageViewProfile;
    TextView textViewName, textViewPhone, textViewAddress;
    CalendarView calendarView;
    private Button reserveButton;
    private BarberCalendarHour barberCalendarHour;
    private MeetingsCalendarAdapter adapter;
    private RecyclerView barberHoursRv;

    public ShowBarberFragment() {
        // Required empty public constructor
    }


    private void createBarberCalendarHours(String date, OnFirebaseCallback<List<BarberCalendarHour>> callback) {
        HashMap<String, Boolean> seen = new HashMap<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS_TABLE);
        List<BarberCalendarHour> allDateHours = new ArrayList<>();
        for (int i = 9; i <= 20; i++) {
            BarberCalendarHour hour = new BarberCalendarHour((i < 10 ? ("0" + i) : i) + ":00", (i < 10 ? ("0" + i) : i) + ":30", true);
            allDateHours.add(hour);
        }

        databaseReference.get()
                .addOnSuccessListener(dataSnapshot -> {
                    boolean hasAtLeast1Hour = false;
                    for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                        if(!appointmentSnapshot.child("date").getValue(String.class).equals(date))continue;
                        BarberCalendarHour hours = appointmentSnapshot.child("barberCalendarHour").getValue(BarberCalendarHour.class);
                        seen.put(hours.getStartHour(), true);
                        hasAtLeast1Hour = true;
                    }

                    if (!hasAtLeast1Hour)
                        return;
                    for (BarberCalendarHour h : allDateHours)
                        h.setAvailable(!seen.containsKey(h.getStartHour()));

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                    }
                }).addOnCompleteListener(task -> {
                    callback.onComplete(allDateHours);
                    System.out.println("Complete");
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        showBarberViewModel = new ViewModelProvider(this).get(com.example.barberbooking.ui.show_barber.ShowBarberViewModel.class);

        View view = inflater.inflate(R.layout.fragment_show_barber, container, false);

        selectedBarber = ShowBarberFragmentArgs.fromBundle(getArguments()).getBarber();
        currentClient = ShowBarberFragmentArgs.fromBundle(getArguments()).getClient();
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        textViewName = view.findViewById(R.id.textViewName);
        barberHoursRv = view.findViewById(R.id.timePickerRv);
        barberHoursRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        calendarView = view.findViewById(R.id.calendarView);
        reserveButton = view.findViewById(R.id.reserveButton);
        Button setUnavailableBtn = view.findViewById(R.id.setUnavailableBtn);
        Picasso.get().load(selectedBarber.getUrlPhoto()).into(imageViewProfile);
        textViewName.setText(selectedBarber.getName());
        textViewPhone.setText(selectedBarber.getPhone());
        textViewAddress.setText(selectedBarber.getAddress());
        // Get the current date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

// Set the minimum date to the current date
        c.set(year, month, day);
        calendarView.setMinDate(c.getTimeInMillis());
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.spinner_time, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final StringBuilder dateStr = new StringBuilder();
        barberHoursRv.setVisibility(View.GONE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String uid = FirebaseAuth.getInstance().getUid();
                if(selectedBarber.getUserId().equals(uid)) {
                    if(selectedBarber.getUnavailableDays().contains(year + "-" + month + "-" + dayOfMonth)) {
                        setUnavailableBtn.setText("Set Date Available");
                    }
                    setUnavailableBtn.setOnClickListener(v -> {
                        if(setUnavailableBtn.getText().toString().equals("Set Date Available")) {
                            setUnavailableBtn.setText("Set Date Unavailable");
                        } else {
                            setUnavailableBtn.setText("Set Date Available");
                        }
                        showBarberViewModel.toggleUnavailableDate(selectedBarber, year + "-" + month + "-" + dayOfMonth);
                    });
                    setUnavailableBtn.setVisibility(View.VISIBLE);
                    return;
                } else {
                    barberHoursRv.setVisibility(View.VISIBLE);
                }
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (selectedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || selectedBarber.getUnavailableDays().contains(year + "-" + month + "-" + dayOfMonth))  {
                    // Show a message to the user that selection of this date is not allowed
                    Toast.makeText(getContext(), "Selection of this date is not allowed!", Toast.LENGTH_SHORT).show();
                } else {

                    dateStr.setLength(0);
                    dateStr.append(String.format("%02d", month + 1));
                    dateStr.append("-");
                    dateStr.append(String.format("%02d", dayOfMonth));
                    dateStr.append("-");
                    dateStr.append(year);

                    createBarberCalendarHours(dateStr.toString(), value -> {
                        System.out.println("Deciding hours");
                        adapter = new MeetingsCalendarAdapter(value,
                                ShowBarberFragment.this);
                        barberHoursRv.setAdapter(adapter);

                    });
                }
            }
        });


        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate if the user has selected a time
                if (barberCalendarHour == null) {
                    Toast.makeText(getContext(), "Please select an hour", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!barberCalendarHour.isAvailable()) {
                    Toast.makeText(getContext(), "Selected hour is unavailable", Toast.LENGTH_LONG).show();
                    return;
                }

                Appointment appointment = new Appointment("",
                        selectedBarber.getUserId(),
                        selectedBarber.getName(),
                        currentClient.getUserId(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        dateStr.toString(),
                        barberCalendarHour
                );
                showBarberViewModel.addAppointment(appointment);
                Toast.makeText(getContext(), "Your reservation added successfully.", Toast.LENGTH_LONG).show();
                adapter.setUnavailable(barberCalendarHour);
            }
        });
        return view;

    }

    public ArrayList<Appointment> setFirstViewList(String selectedDateTime) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.APPOINTMENTS_TABLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment model = dataSnapshot.getValue(Appointment.class);
                    if (!model.getDate().equals(selectedDateTime)) {
                        appointments.add(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return appointments;
    }

    @Override
    public void onSelectHour(BarberCalendarHour hour) {
        barberCalendarHour = hour;
    }
}