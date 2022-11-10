package com.example.callingallvisitors;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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
    /*List<Visitor> existingVisitors = new ArrayList<Visitor>();
    DatabaseReference databaseReference;*/

    TextView txtVisitors;
    View visitorOP;
    ListView visitorListLV;
    private DatabaseReference mDatabase;
    Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        visitorOP = inflater.inflate(R.layout.fragment_still_on_premises, container, false);
        //visitorListLV = visitorOP.findViewById(R.id.itemListViewVisitors);
        txtVisitors = visitorOP.findViewById(R.id.txtVisitors);
        mDatabase = FirebaseDatabase.getInstance().getReference("Visitors");

        return visitorOP;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Visitor> visitorsList = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                visitorsList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Visitor v = postSnapshot.getValue(Visitor.class);

                        visitorsList.add(v);
                        Log.d("123456", "onDataChange: User added: " + v.getName());



                }
                String out = "";
                for (Visitor v: visitorsList
                ) {
                    out +="\n" + v.getName();
                    out+= "\n" + v.getDateVisited() + "\n";
                }
                txtVisitors.setText(out);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    /*public void loadVisitorFirebaseData(){

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
    }*/
}