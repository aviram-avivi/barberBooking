package com.example.barberbooking.ui.register;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.barberbooking.Barber;
import com.example.barberbooking.Client;
import com.example.barberbooking.R;
import com.example.barberbooking.User;
import com.example.barberbooking.databinding.FragmentRegisterBinding;
import com.example.barberbooking.utils.Constants;
import com.example.barberbooking.utils.OnFirebaseCallback;

import java.io.ByteArrayOutputStream;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    //private FragmentRegisterBinding binding;
    private EditText editTextEmail, editTextPassword, editTextUserName, editTextPasswordAuth, editTextPhone;
    private EditText editTextPrice, editTextAddress;
    private RadioGroup radioGroupUserType, radioGroupGender;
    private Button buttonRegister;
    RegisterViewModel registerViewModel;
    TextView textViewLogin;
    private ImageButton imageButtonProfile;
    private final int MY_GALLERY_PERMISSION_CODE = 2;
    private Uri myImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel = new ViewModelProvider(this).get(com.example.barberbooking.ui.register.RegisterViewModel.class);

        //binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        radioGroupUserType = view.findViewById(R.id.userTypeRg);
        radioGroupGender = view.findViewById(R.id.userGender);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextUserName = view.findViewById(R.id.editTextUserName);
        editTextPasswordAuth = view.findViewById(R.id.editTextPasswordAuth);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        imageButtonProfile = view.findViewById(R.id.imageButtonProfile);
        imageButtonProfile.setOnClickListener(this);

        textViewLogin = view.findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(this);

        buttonRegister.setOnClickListener(this);
        showClientFields(); // default
        radioGroupUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedOption = group.getCheckedRadioButtonId();
                if (checkedOption == R.id.barberUserType) {
                    showBarberFields();
                } else {
                    showClientFields();
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                if (registerViewModel.getUserType().equals(Constants.CLIENTS_TABLE)) { // Client register
                    if (registerViewModel.isThereEmptyData(new String[]{
                            editTextEmail.getText().toString(),
                            editTextUserName.getText().toString(),
                            editTextPhone.getText().toString()
                    })) {
                        Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String gender = radioGroupGender.getCheckedRadioButtonId() == R.id.userGenderMale ? "male" : "female";
                    registerViewModel.register(new Client("",
                                    editTextEmail.getText().toString(),
                                    editTextUserName.getText().toString(),
                                    editTextPhone.getText().toString(),
                                    gender),
                            editTextPassword.getText().toString(),
                            editTextPasswordAuth.getText().toString(),
                            getContext(),
                            getActivity(),
                            new OnFirebaseCallback<User>() {
                                @Override
                                public void onComplete(User value) {
                                    NavHostFragment.findNavController(RegisterFragment.this)
                                            .navigate(R.id.action_registerFragment_to_loginFragment);
                                }
                            }
                    );
                } else { // Barber register
                    if (registerViewModel.isThereEmptyData(new String[]{
                            editTextEmail.getText().toString(),
                            editTextUserName.getText().toString(),
                            editTextPhone.getText().toString(),
                            editTextAddress.getText().toString(),
                            editTextPrice.getText().toString(),
                            myImageUri.toString(),
                    })) {
                        Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    registerViewModel.register(new Barber("",
                                    editTextEmail.getText().toString(),
                                    editTextUserName.getText().toString(),
                                    editTextPhone.getText().toString(),
                                    myImageUri.toString(),
                                    Integer.parseInt(editTextPrice.getText().toString()),
                                    editTextAddress.getText().toString()),
                            editTextPassword.getText().toString(),
                            editTextPasswordAuth.getText().toString(),
                            getContext(),
                            getActivity(),
                            new OnFirebaseCallback<User>() {
                                @Override
                                public void onComplete(User value) {
                                    NavHostFragment.findNavController(RegisterFragment.this)
                                            .navigate(R.id.action_registerFragment_to_loginFragment);
                                }
                            }
                    );
                }
                break;
            case R.id.textViewLogin:
                Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_loginFragment);
                break;
            case R.id.imageButtonProfile:
                addImage();
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void addImage() {

            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, MY_GALLERY_PERMISSION_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_GALLERY_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            myImageUri = data.getData();
            imageButtonProfile.setImageURI(myImageUri);
        }

    }

    private void showBarberFields() {
        // hide client fields
        radioGroupGender.setVisibility(View.GONE);
        // show bar fields
        editTextPrice.setVisibility(View.VISIBLE);
        editTextAddress.setVisibility(View.VISIBLE);
        imageButtonProfile.setVisibility(View.VISIBLE);
        registerViewModel.setUserType(Constants.BARBERS_TABLE);
    }

    private void showClientFields() {
        // hide barber fields
        editTextPrice.setVisibility(View.GONE);
        editTextAddress.setVisibility(View.GONE);
        imageButtonProfile.setVisibility(View.GONE);

        // show client fields
        radioGroupGender.setVisibility(View.VISIBLE);
        registerViewModel.setUserType(Constants.CLIENTS_TABLE);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

}
