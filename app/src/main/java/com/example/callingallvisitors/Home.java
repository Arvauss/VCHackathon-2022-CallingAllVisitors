package com.example.callingallvisitors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
public class Home extends Fragment {

    View home;
    Button newVisitor, existsingVisitor, exitingVisitor, visiorsOnPrem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home = inflater.inflate(R.layout.fragment_home, container, false);

        newVisitor = home.findViewById(R.id.newVisitorBtn);
        existsingVisitor = home.findViewById(R.id.returningVisitorBtn);
        exitingVisitor = home.findViewById(R.id.exitingVisitor);
        visiorsOnPrem = home.findViewById(R.id.visitorsOnPremises);

        return home;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, NewVisitor.class,null).addToBackStack(null).commit();
            }
        });

        existsingVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Home.class,null).addToBackStack(null).commit();
            }
        });

        exitingVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Home.class,null).addToBackStack(null).commit();
            }
        });

        visiorsOnPrem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Home.class,null).addToBackStack(null).commit();
            }
        });

    }
}