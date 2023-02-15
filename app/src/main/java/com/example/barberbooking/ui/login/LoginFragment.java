package com.example.barberbooking.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.barberbooking.Client;
import com.example.barberbooking.R;
import com.example.barberbooking.databinding.FragmentLoginBinding;
import com.example.barberbooking.ui.dashboard.DashboardFragmentDirections;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    LoginViewModel loginViewModel;
    TextView textViewRegister, textViewForgetPassword;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.setData(getActivity(), getContext(), getView(), "", "");

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        textViewRegister = view.findViewById(R.id.textViewRegister);
        textViewForgetPassword = view.findViewById(R.id.textViewForgetPassword);
        textViewForgetPassword.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                v.setEnabled(false);
                loginViewModel.setData(getActivity(),
                        getContext(),
                        getView(),
                        editTextEmail.getText().toString().trim(),
                        editTextPassword.getText().toString().trim());
                loginViewModel.login(unused -> v.setEnabled(true)); // Call the login method of the LoginViewModel
                break;
            case R.id.textViewRegister:
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
                break;
            case R.id.textViewForgetPassword:
                loginViewModel.forgetPassword();
                break;
        }
    }
}

