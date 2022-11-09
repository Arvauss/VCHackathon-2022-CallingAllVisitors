package com.example.callingallvisitors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.callingallvisitors.Models.Visitor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Dictionary;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private StorageReference storRef;
    private FirebaseApp mAuth;
    protected static HashMap<String, String> FaceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().setReorderingAllowed(true).add(R.id.WelcomeFrag, Welcome.class,null).commitNow();
        
        InitDBListener();
    }

    private void InitDBListener() {
        GetDBFacesTask getTask = new GetDBFacesTask();
        mAuth = FirebaseApp.getInstance();
        storRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Visitors");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getTask.execute(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getTask.cancel(true);
            }
        });

    }

    private static class GetDBFacesTask extends AsyncTask<DataSnapshot, Void, Void>{

        @Override
        protected Void doInBackground(DataSnapshot... snapshot) {
            if (!FaceMap.isEmpty())
                FaceMap.clear();

            for (DataSnapshot dataSnapshot : snapshot[0].getChildren()){
                Visitor v = dataSnapshot.getValue(Visitor.class);

                if (v.isHasCheckedIn() && v.getUrl() != null) {
                    if (!this.isCancelled()) {
                        FaceMap.put(dataSnapshot.getKey(), v.getUrl());
                        Log.d("123456", "Key: " + FaceMap.get(dataSnapshot.getKey()));
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("123456", FaceMap.toString());
        }
    }

    public static HashMap<String, String> getFaceMap(){
        return FaceMap;
    }


}