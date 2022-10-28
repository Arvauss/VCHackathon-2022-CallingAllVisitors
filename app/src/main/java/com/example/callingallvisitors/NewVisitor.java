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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.callingallvisitors.Models.Visitor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NewVisitor extends Fragment {

    private FirebaseAuth mAuth;
    View visitor;
    EditText name, surname, idNumber, resident, residentPhonenumebr, edtScanID, edtScanCar;
    Button scanFace, processVisitor,btnOkID, btnOkCar;
    LinearLayout DataNewVisitor, DataCar;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        visitor = inflater.inflate(R.layout.fragment_new_visitor, container, false);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
        date = dateFormat.format(calendar.getTime());

        mAuth = FirebaseAuth.getInstance();

        name = visitor.findViewById(R.id.name);
        surname = visitor.findViewById(R.id.surname);
        idNumber = visitor.findViewById(R.id.idNumber);
        resident = visitor.findViewById(R.id.resident);
        residentPhonenumebr = visitor.findViewById(R.id.residentPhonenumebr);
        edtScanID = visitor.findViewById(R.id.edtScanID);
        edtScanCar = visitor.findViewById(R.id.edtScanCar);
        DataNewVisitor = visitor.findViewById(R.id.DataNewVisitor);
        DataCar = visitor.findViewById(R.id.DataCar);
        btnOkID = visitor.findViewById(R.id.btnOkID);
        btnOkCar = visitor.findViewById(R.id.btnOkCar);
        scanFace = visitor.findViewById(R.id.scanFace);
        processVisitor = visitor.findViewById(R.id.processVisitor);
        return visitor;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mAuth = FirebaseAuth.getInstance();
        scanFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FirebaseUser currUser = mAuth.getCurrentUser();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Visitors");
        processVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vName = name.getText().toString();
                String vSurname = surname.getText().toString();
                String vID = idNumber.getText().toString();
                String vResident = resident.getText().toString();
                String vResidentPhoneNumber = residentPhonenumebr.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                DatabaseReference visitorPush = myRef.child("Visitors").push();
                String currentUserID = currUser.getUid();

                Visitor nv = new Visitor(vName, vSurname, vID, vResident, vResidentPhoneNumber, currentUserID, date);

                visitorPush.setValue(nv);

                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Home.class,null).addToBackStack(null).commit();
            }
        });

        scanFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edtScanID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataNewVisitor.setVisibility(View.VISIBLE);

            }
        });
        btnOkID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataNewVisitor.setVisibility(View.GONE);

            }
        });
        edtScanCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCar.setVisibility(View.VISIBLE);

            }
        });
        btnOkCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCar.setVisibility(View.GONE);

            }
        });


    }
}