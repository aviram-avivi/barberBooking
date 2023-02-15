package com.example.barberbooking.ui.meetings;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.R;
import com.example.barberbooking.ui.dashboard.DashboardFragmentDirections;
import com.example.barberbooking.utils.Appointment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


interface IMeetingItem {
    void cancelMeeting(Appointment appointment);
}

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MiniMeetingsHolder> {
    public List<Appointment> mList;
    private final boolean isBarber;
    private final IMeetingItem iMeeting;
    public MeetingsAdapter(IMeetingItem iMeeting, List<Appointment> mList,
                           boolean isBarber) {
        this.mList = mList;
        this.isBarber = isBarber;
        this.iMeeting = iMeeting;
    }

    @NonNull
    @Override
    public MiniMeetingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false);
        return new MiniMeetingsHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MiniMeetingsHolder holder, @SuppressLint("RecyclerView") int position) {

        Appointment appointment = mList.get(position);

        if (isBarber) holder.meeting_name_text_view.setText(mList.get(position).getClient_name());
        else holder.meeting_name_text_view.setText(mList.get(position).getBarber_name());

        holder.meeting_time_text_view.setText(mList.get(position).getDate() + " " + mList.get(position).getBarberCalendarHour().getStartHour());
        holder.cancel_button.setOnClickListener(v -> iMeeting.cancelMeeting(appointment));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MiniMeetingsHolder extends RecyclerView.ViewHolder {

        TextView meeting_name_text_view, meeting_time_text_view;
        AppCompatButton cancel_button;

        public MiniMeetingsHolder(@NonNull View itemView) {
            super(itemView);
            cancel_button = itemView.findViewById(R.id.cancel_button);
            meeting_name_text_view = itemView.findViewById(R.id.meeting_name_text_view);
            meeting_time_text_view = itemView.findViewById(R.id.meeting_time_text_view);
        }
    }

}
