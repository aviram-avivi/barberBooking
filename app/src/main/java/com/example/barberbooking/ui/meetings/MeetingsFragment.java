package com.example.barberbooking.ui.meetings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.R;
import com.example.barberbooking.utils.Appointment;
import com.example.barberbooking.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MeetingsFragment extends Fragment implements IMeetingItem {

    private RecyclerView recyclerViewMeetings;
    MeetingsViewModel meetingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        meetingsViewModel = new ViewModelProvider(this).get(MeetingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_meetings, container, false);
        setUpView(root);
        showData();
        return root;
    }

    public void startListeningAppointments(boolean isBarber) {
        meetingsViewModel.getAppointmentsLive().observe(this, appointments ->
                recyclerViewMeetings.setAdapter(
                        new MeetingsAdapter(MeetingsFragment.this, appointments, isBarber)));

        meetingsViewModel.listenAppointments(isBarber);
    }

    public void showData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.BARBERS_TABLE);

        myRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    startListeningAppointments(task.getResult().getValue()!=null);
                } else {
                    Log.e("firebase - false", task.getResult().toString());
                }
            }
        });
    }

    public void setUpView(View root) {
        recyclerViewMeetings = root.findViewById(R.id.recyclerViewMeetings);
        recyclerViewMeetings.setHasFixedSize(false);
        recyclerViewMeetings.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void cancelMeeting(Appointment appointment) {
        meetingsViewModel.cancelAppointment(appointment);
    }
}
