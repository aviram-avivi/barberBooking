package com.example.barberbooking.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.barberbooking.Client;
import com.example.barberbooking.R;
import com.example.barberbooking.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginViewModel extends ViewModel {

    Activity requiredActivity;
    Context context;
    View view;
    String email;
    String password;
    FirebaseAuth myAuthentication;
    DatabaseReference mDatabaseReference;

    public void setData(Activity requiredActivity, Context context, View view, String email, String password) {
        this.requiredActivity = requiredActivity;
        this.context = context;
        this.view = view;
        this.email = email;
        this.password = password;
    }
    //checks if the email or password is empty or not.
    //returns true if any one of them is empty, else it returns false
    public boolean isThereEmptyData() {
        return email.trim().isEmpty() ||
                password.trim().isEmpty();
    }

    public LoginViewModel() {
        myAuthentication = FirebaseAuth.getInstance();
    }

    public void login(OnSuccessListener<Boolean> listener) {
        if(!isThereEmptyData()) {
            myAuthentication.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        String id = myAuthentication.getCurrentUser().getUid();
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                        mDatabaseReference.child(Constants.BARBERS_TABLE).child(id)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    LoginFragmentDirections.ActionLoginFragmentToNavigationDashboard action =
                                            LoginFragmentDirections.actionLoginFragmentToNavigationDashboard(getClient());
                                    Navigation.findNavController(view).navigate(action);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else Toast.makeText(context,"Email or password is incorrect",Toast.LENGTH_LONG).show();
                    listener.onSuccess(true);
                }
            });

        } else  {
            Toast.makeText(context,"Enter all data",Toast.LENGTH_SHORT).show();
            listener.onSuccess(true);
        }
    }
    //creating a new client of getting the current client info from the database
    private Client getClient()
    {
        FirebaseUser firebaseUser = myAuthentication.getCurrentUser();
        return new Client(firebaseUser.getUid(), firebaseUser.getEmail(), "", firebaseUser.getPhoneNumber(), "");
    }

    public void forgetPassword() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.forget_password_dialog);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.setCancelable(true);

        final EditText etEmailForgot = d.findViewById(R.id.etEmailForget);
        final Button btnSendForget = d.findViewById(R.id.btnForget);

        btnSendForget.setOnClickListener(v -> {
            if (etEmailForgot.getText().toString().trim().length() != 0) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(etEmailForgot.getText().toString().trim() + "")
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "A mail with a reset password was sent to your email", Toast.LENGTH_SHORT).show();
                                d.dismiss();
                            } else {
                                Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else
                Toast.makeText(context, "Enter your email!", Toast.LENGTH_SHORT).show();
        });

        int width = (int) (requiredActivity.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (ViewGroup.LayoutParams.WRAP_CONTENT);
        d.getWindow().setLayout(width, height);

        d.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        d.show();
    }
}
