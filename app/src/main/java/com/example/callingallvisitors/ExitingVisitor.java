package com.example.callingallvisitors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.callingallvisitors.Models.Visitor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;
import com.regula.facesdk.enums.ImageType;
import com.regula.facesdk.model.MatchFacesImage;
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit;
import com.regula.facesdk.request.MatchFacesRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ExitingVisitor extends Fragment {

    View exitingVisitor;
    Button scanEVface,cancelEVBtn, exitEV, exitID;
    ImageView visitorIV;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    static Bitmap bitmapImage;
    static Bitmap dbFace;
    Uri imageUri;
    protected Visitor matchedVisitor = null;

    static double highestSim = 0;
    static String highestSimID = "";

    private HashMap<String, String> FacesMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        exitingVisitor = inflater.inflate(R.layout.fragment_exiting_visitor, container, false);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
        date = dateFormat.format(calendar.getTime());

        visitorIV = exitingVisitor.findViewById(R.id.exitImageView);
        scanEVface = exitingVisitor.findViewById(R.id.scanEVface);
        cancelEVBtn = exitingVisitor.findViewById(R.id.cancelExitVBtn);
        exitEV = exitingVisitor.findViewById(R.id.confirmEVBtn);
        exitID = exitingVisitor.findViewById(R.id.confirmExitingIDBtn);
        return exitingVisitor;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scanEVface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacesMap = MainActivity.getFaceMap();
                startFaceCaptureActivity();
                new MatchTask().execute(FacesMap);
            }
        });

        cancelEVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Home.class,null).addToBackStack(null).commit();
            }
        });

        exitEV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: update firebase matchedVisitor hasCheckedIn = false
            }
        });

        exitID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void startFaceCaptureActivity() {
        FaceCaptureConfiguration config = new FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build();

        FaceSDK.Instance().presentFaceCaptureActivity(getActivity(), config, res ->{
            if (res.getImage() == null) return;
            bitmapImage = res.getImage().getBitmap();
            //v.setImageBitmap(res.getImage().getBitmap());
            // v.setTag(ImageType.LIVE);
            //imageUri = saveImage(bitmapImage, getActivity());
            Toast.makeText(getContext().getApplicationContext(),"Image Saved",Toast.LENGTH_SHORT).show();
        });
    }

    private  class MatchTask extends AsyncTask<HashMap<String,String>, Void, Void>{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(HashMap<String, String>... hashMaps) {

            HashMap<String, String> FacesMap =hashMaps[0];

            List<MatchFacesImage> iList = new ArrayList<>();

            for (Map.Entry<String, String> face: FacesMap.entrySet()){
                if (!iList.isEmpty()) iList.clear();

                try {
                    URL faceUrl = new URL(face.getValue());
                    dbFace = BitmapFactory.decodeStream(faceUrl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (dbFace != null && bitmapImage != null) {
                    iList.add(new MatchFacesImage(bitmapImage, (ImageType) ImageType.LIVE, true));
                    iList.add(new MatchFacesImage(dbFace, (ImageType) ImageType.LIVE, true));

                    MatchFacesRequest mReq = new MatchFacesRequest(iList);

                    FaceSDK.Instance().matchFaces(mReq, mfRes -> {
                        MatchFacesSimilarityThresholdSplit split =
                                new MatchFacesSimilarityThresholdSplit(mfRes.getResults(), 0.75d);

                        if (split.getMatchedFaces().size() > 0){
                            double sim = split.getMatchedFaces().get(0).getSimilarity();
                            if (sim > 0.98){
                                highestSim = sim;
                                highestSimID = face.getKey();
                                return;
                            }
                            if (sim > highestSim){
                                highestSim = sim;
                                highestSimID = face.getKey();
                            }
                        }
                    });

                } else {
                    Log.d("123456", "MatchTask : Something went wrong \n" + face.getValue());
                }
                Log.d("123456", "doInBackground: One Loop");
            }

            return null;
        }

        @Override
        protected void onPreExecute(){
            //TODO: Setup Loading Symbol
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            Visitor v = GetMatchedUserInfo();

            if (v != null){
                Toast.makeText(getActivity().getApplicationContext(), "User found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private Visitor GetMatchedUserInfo() {
        if (highestSim > 0.97){
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            dbref.child("Visitors").child(highestSimID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        matchedVisitor = snapshot.getValue(Visitor.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return matchedVisitor;
    }

}