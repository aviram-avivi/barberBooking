package com.example.barberbooking.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.Barber;
import com.example.barberbooking.BarberAdapter;
import com.example.barberbooking.Client;
import com.example.barberbooking.R;
import com.example.barberbooking.ui.login.LoginFragment;
import com.example.barberbooking.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private View view;
    private Client currentClient;
    DashboardViewModel dashboardViewModel;
    BarberAdapter barberAdapter;
    ArrayList<Barber> barbersList;
    RecyclerView recyclerViewBarbers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        currentClient = DashboardFragmentArgs.fromBundle(getArguments()).getClient();
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Button signOutButton = view.findViewById(R.id.buttonSignout);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(v);
            }
        });

        setUpView();
        showViewPosts(); //display the list of barbers

        return view;
    }

    public void showViewPosts() {
        barbersList = setFirstViewList();
        barberAdapter = new BarberAdapter(getContext(), barbersList, currentClient);
        recyclerViewBarbers.setAdapter(barberAdapter);
    }

    public ArrayList<Barber> setFirstViewList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.BARBERS_TABLE);
        ArrayList<Barber> newList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Barber model = dataSnapshot.getValue(Barber.class);
                    if(model.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        model.setName("Myself");
                    }
                    newList.add(model);
                }
                barberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return newList;
    }

    public void setUpView() {
        recyclerViewBarbers = view.findViewById(R.id.recyclerViewBarbers);
        recyclerViewBarbers.setHasFixedSize(false);
        recyclerViewBarbers.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void signOut(View view) {
        // Sign out of Firebase
        FirebaseAuth.getInstance().signOut();
        Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_loginFragment);
    }
}
