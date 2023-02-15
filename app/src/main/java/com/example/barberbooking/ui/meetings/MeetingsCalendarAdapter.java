package com.example.barberbooking.ui.meetings;

import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.R;
import com.example.barberbooking.utils.BarberCalendarHour;
import com.example.barberbooking.utils.IMeetingsCalendar;

import java.util.List;


public class MeetingsCalendarAdapter extends RecyclerView.Adapter<MeetingsCalendarAdapter.MeetingsCalendarViewHolder> {

    private List<BarberCalendarHour> calendarHours;
    private IMeetingsCalendar iMeetingsCalendar;

    public MeetingsCalendarAdapter(List<BarberCalendarHour> calendarHours, IMeetingsCalendar iMeetingsCalendar) {
        this.calendarHours = calendarHours;
        this.iMeetingsCalendar = iMeetingsCalendar;
    }

    @NonNull
    @Override
    public MeetingsCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_calendar_item,parent,false);
        return new MeetingsCalendarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingsCalendarViewHolder holder, int position) {
        BarberCalendarHour barberCalendarHour = calendarHours.get(position);
        holder.bind(barberCalendarHour);

        if(barberCalendarHour.isAvailable()) {
            holder.itemView.setOnClickListener((View v) -> {
                if(holder.hourCard.getCardBackgroundColor().getDefaultColor() == Color.LTGRAY) {
                    holder.hourCard.setCardBackgroundColor(Color.WHITE);
                    // set unavailable
                    iMeetingsCalendar.onSelectHour(null);
                } else {
                    iMeetingsCalendar.onSelectHour(barberCalendarHour);
                    holder.hourCard.setCardBackgroundColor(Color.LTGRAY);
                }
            });
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setOnClickListener((View v) ->  {
                iMeetingsCalendar.onSelectHour(null);
                holder.hourCard.setCardBackgroundColor(Color.WHITE);
            });
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
    }

    public void setUnavailable(BarberCalendarHour hour) {
        for(BarberCalendarHour h : calendarHours) {
            if(h.equals(hour))
                    h.setAvailable(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return calendarHours.size();
    }

    class MeetingsCalendarViewHolder extends RecyclerView.ViewHolder {

        private TextView hoursTv,availableTv;
        private CardView hourCard;
        public MeetingsCalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            hoursTv = itemView.findViewById(R.id.dateTimeTv);
            availableTv = itemView.findViewById(R.id.availableTv);
            hourCard = itemView.findViewById(R.id.hourCard);
        }

        public void bind(BarberCalendarHour barberCalendarHour) {
            hoursTv.setText(barberCalendarHour.getStartHour() + "-" + barberCalendarHour.getEndHour());
            availableTv.setText(barberCalendarHour.isAvailable() ? "Available" : "Unavailable");
        }

    }
}
