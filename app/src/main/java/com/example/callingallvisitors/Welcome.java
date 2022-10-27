package com.example.callingallvisitors;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Welcome extends Fragment {


    Button login;
    Button register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View welcome = inflater.inflate(R.layout.fragment_welcome, container, false);

        login = welcome.findViewById(R.id.welcomeLogin);
        register = welcome.findViewById(R.id.welcomeRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Register());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Login());
            }
        });

        return welcome;
    }

    private void replaceFragment(Register fragment) {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Register.class,null).addToBackStack(null).commit();
    }

    private void replaceFragment(Login fragment) {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Login.class,null).addToBackStack(null).commit();
    }
}