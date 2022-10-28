package com.example.callingallvisitors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
public class ReturningVisitor extends Fragment {


    View rVisitor;
    Button sf, cancelV, confrimV;
    CardView vinfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rVisitor = inflater.inflate(R.layout.fragment_returning_visitor, container, false);
        sf = rVisitor.findViewById(R.id.scanRVface);
        cancelV = rVisitor.findViewById((R.id.cancelRVBtn));
        confrimV = rVisitor.findViewById(R.id.visitorConfirmedBtn);
        vinfo = rVisitor.findViewById(R.id.visitorInfo);

        return rVisitor;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancelV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Home.class,null).addToBackStack(null).commit();
            }
        });

        confrimV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}