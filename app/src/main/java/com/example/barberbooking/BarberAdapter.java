package com.example.barberbooking;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.ui.dashboard.DashboardFragmentDirections;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.MiniBarberHolder> implements RecyclerView.OnItemTouchListener {

    public ArrayList<Barber> mList;
    private Client mCurrentClient;
    private final Context context;

    public BarberAdapter(Context context, ArrayList<Barber> mList, Client currentClient) {
        this.context = context;
        this.mList = mList;
        this.mCurrentClient = currentClient;
    }

    @NonNull
    @Override
    public MiniBarberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mini_barber_tile, parent, false);
        return new MiniBarberHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MiniBarberHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(mList.get(position).getUrlPhoto()).into(holder.imageViewProfile);
        holder.textViewName.setText(mList.get(position).getName());

        holder.imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragmentDirections.ActionNavigationDashboardToShowBarberFragment action =
                        DashboardFragmentDirections.actionNavigationDashboardToShowBarberFragment(mList.get(position),
                                mCurrentClient);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MiniBarberHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProfile;
        TextView textViewName;

        public MiniBarberHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewName = itemView.findViewById(R.id.textViewName);

        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
