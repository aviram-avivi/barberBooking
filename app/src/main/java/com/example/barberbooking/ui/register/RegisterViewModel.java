package com.example.barberbooking.ui.register;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.barberbooking.Barber;
import com.example.barberbooking.Client;
import com.example.barberbooking.R;
import com.example.barberbooking.User;
import com.example.barberbooking.utils.Constants;
import com.example.barberbooking.utils.OnFirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class RegisterViewModel extends ViewModel {

    private String userType = "client";
    private FirebaseAuth myAuthentication;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isThereEmptyData(String[] fields) {
        for (String field : fields) {
            if (field.trim().isEmpty())
                return true;
        }
        return false;
    }

    public RegisterViewModel() {
        myAuthentication = FirebaseAuth.getInstance();
    }

    public void register(User user,
                         String password,
                         String otherPassword,
                         Context context,
                         Activity requireActivity,
                         OnFirebaseCallback<User> callback) {
        if (otherPassword.trim().equals(password.trim()))
            myAuthentication.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String userId = task.getResult().getUser().getUid();
                        user.setUserId(userId);
                        if (user instanceof Barber) {
                            createNewBarber((Barber) user,context,requireActivity);
                        } else if (user instanceof Client) {
                            createNewClient((Client) user);
                        }
                        callback.onComplete(user);
                    } else
                        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
                }
            });
        else Toast.makeText(context, "Passwords do not equal", Toast.LENGTH_SHORT).show();
    }

    private void createNewClient(Client user) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getName())
                .build();
        assert fUser != null;
        fUser.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                FirebaseDatabase.getInstance()
                        .getReference(Constants.CLIENTS_TABLE)
                        .child(user.getUserId())
                        .setValue(user);
            }
        });
    }

    private String getFileExtension(Uri mUri, Activity requireActivity) {
        ContentResolver cr = requireActivity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void createNewBarber(Barber user, Context context, Activity requireActivity) {
        final StorageReference reference = FirebaseStorage.getInstance().getReference();

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(Uri.parse(user.getUrlPhoto()),requireActivity));
        fileRef.putFile(Uri.parse(user.getUrlPhoto())).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(url -> {

            user.setUrlPhoto(url.toString());
            FirebaseDatabase.getInstance()
                    .getReference(Constants.BARBERS_TABLE)
                    .child(user.getUserId())
                    .setValue(user);
        })).addOnProgressListener(snapshot -> {}).addOnFailureListener(e -> {});
    }
}
