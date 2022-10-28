package com.example.callingallvisitors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.callingallvisitors.Models.Visitor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StillOnPremises extends Fragment {
    List<Visitor> existingVisitors = new ArrayList<Visitor>();
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_still_on_premises, container, false);
    }
    public void loadVisitorFirebaseData(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Visitors");
        //Query query = databaseReference.orderByChild("userID").equalTo(userID);
        //database to read data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear the list
                //For each dataSnapshot  in the children of categories(AndroidJSon, 2017).
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //Get the category and add it to a list
                    Visitor vistor = dataSnapshot.getValue(Visitor.class);


                    existingVisitors.add(vistor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast if the reading of data fails
                Toast.makeText(getContext().getApplicationContext(),"Error:" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}