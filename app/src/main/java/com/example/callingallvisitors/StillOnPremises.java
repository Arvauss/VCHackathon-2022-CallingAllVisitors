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
import android.widget.Toast;

import com.example.callingallvisitors.Adapters.VisitorListAdapter;
import com.example.callingallvisitors.Models.Visitor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StillOnPremises extends Fragment {

    View visitorOP;
    ListView visitorListLV;
    private DatabaseReference mDatabase;
    Context mContext;
// ...

    /*public StillOnPremises()
    {

    }
    public StillOnPremises(Context c)
    {
        this.mContext = c;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        visitorOP = inflater.inflate(R.layout.fragment_still_on_premises, container, false);
        visitorListLV = visitorOP.findViewById(R.id.itemListViewVisitors);
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
                    if(v.isHasCheckedIn())
                    {
                        visitorsList.add(v);
                    }

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        Log.d("Premises", "Before adapter");
        VisitorListAdapter vla = new VisitorListAdapter(getActivity(), R.layout.visitor_list_template,visitorsList);
        visitorListLV.setAdapter(vla);

    }
}