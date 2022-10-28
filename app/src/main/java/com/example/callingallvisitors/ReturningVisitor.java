package com.example.callingallvisitors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.regula.facesdk.model.results.matchfaces.MatchFacesComparedFacesPair;
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit;
import com.regula.facesdk.request.MatchFacesRequest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ReturningVisitor extends Fragment {


    View rVisitor;
    Button sf, cancelV, confrimV;
    TextView name;
    ImageView imageview;
    List<Visitor> existingVisitors = new ArrayList<Visitor>();
    List<MatchFacesImage> imageList = new ArrayList<>();
    HashMap<MatchFacesImage,Visitor> imageNames = new HashMap<>();
    DatabaseReference databaseReference;
    Bitmap bitmap;
    private boolean matched;
    String nameMatch ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rVisitor = inflater.inflate(R.layout.fragment_returning_visitor, container, false);
        sf = rVisitor.findViewById(R.id.scanRVface);
        cancelV = rVisitor.findViewById((R.id.cancelRVBtn));
        confrimV = rVisitor.findViewById(R.id.visitorConfirmedBtn);
        imageview = rVisitor.findViewById(R.id.ivVisitorPhoto);
        //vinfo = rVisitor.findViewById(R.id.visitorInfo);
        name = rVisitor.findViewById(R.id.visitorNametxtv);

        return rVisitor;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadVisitorFirebaseData();
        sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFaceCaptureActivity(imageview);
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
                    Bitmap mp = DownloadImageFromPath(vistor.getUrl());
                    MatchFacesImage m = new MatchFacesImage(mp, ImageType.PRINTED, true);
                    imageNames.put(m,vistor);
                    imageList.add(m);
                    //imageNames.put()
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast if the reading of data fails
                Toast.makeText(getContext().getApplicationContext(),"Error:" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void matchFaces() {
        List<MatchFacesImage> imageListTemp = new ArrayList<>();
        boolean matchBool = false;
        Log.d("Size : ","" + imageList.size());
        for(int num1 = 0; num1< imageList.size()-1; num1++){
            imageListTemp.clear();
            for(int z = num1+1; z <imageList.size(); z++) {
                imageListTemp.clear();
                matchBool = matchFaces(imageList.get(num1),imageList.get(z));
                //imageListTemp.add(imageList.get(num1));
                //imageListTemp.add(imageList.get(z));
                Log.d("Images","" +imageList.get(z).getImageType().toString());
                Log.d("Images","" +imageList.get(num1).getImageType().toString());
                //MatchFacesRequest matchRequest = new MatchFacesRequest(imageListTemp);
                /*
                int finalNum = num1;
                int finalZ = z;
                FaceSDK.Instance().matchFaces(matchRequest, matchFacesResponse -> {
                    MatchFacesSimilarityThresholdSplit split =
                            new MatchFacesSimilarityThresholdSplit(matchFacesResponse.getResults(), 0.60d);
                    if (split.getMatchedFaces().size() > 0) {
                        double similarity = split.getMatchedFaces().get(0).getSimilarity();
                        matched = true;
                        List<MatchFacesComparedFacesPair> match = matchFacesResponse.getResults();
                        MatchFacesImage face =  match.get(0).getFirst().getMatchesFaceImage();
                        nameMatch = imageNames.get(face);
                        //info.setText("Match is " + name);
                        Log.d("MatchFacesSDK","Name : " + nameMatch);
                        Log.d("MatchFacesSDK","Name : " + matched);
                        Log.d("MatchFacesSDK","TempListSize : " + imageListTemp.size());
                        Toast.makeText(MainActivity.this,"Name : " + nameMatch,Toast.LENGTH_LONG).show();
                        Log.d("MatchFacesSDK","Similarity: " + String.format("%.2f", similarity * 100) + "%");
                    } else {
                        Log.d("MatchFacesSDK","No Match at i = " + finalNum + " int z = " + finalZ);
                        //info.setText("Similarity: null");
                    }

                });

                 */

                if(matchBool){
                    break;
                }
            }
            if(matchBool){
                break;
            }

        }

    }

    public boolean matchFaces(MatchFacesImage bitmap1, MatchFacesImage bitmap2) {
        List<MatchFacesImage> iList = new ArrayList<>();
        iList.add(bitmap1);
        iList.add(bitmap2);

        MatchFacesRequest mReq = new MatchFacesRequest(iList);

        FaceSDK.Instance().matchFaces(mReq, mfRes -> {
            MatchFacesSimilarityThresholdSplit split =
                    new MatchFacesSimilarityThresholdSplit(mfRes.getResults(), 0.75d);

            if (split.getMatchedFaces().size() > 0){
                List<MatchFacesComparedFacesPair> match = mfRes.getResults();
                MatchFacesImage face =  match.get(0).getFirst().getMatchesFaceImage();
                nameMatch = imageNames.get(face).getName();
                //info.setText("Match is " + name);
                matched = true;

                Log.d("MatchFacesSDK","Name : " + nameMatch);
                Log.d("MatchFacesSDK","MatchedFace? : " + matched);
                Log.d("MatchFacesSDK","TempListSize : " + iList.size());
                name.setText("Match is " + nameMatch);
                double sim = split.getMatchedFaces().get(0).getSimilarity();
            } else {
                Log.d("MatchFacesSDK","No match");
            }
        });
        return matched;
    }

    public Bitmap DownloadImageFromPath(String path){
        InputStream in =null;
        Bitmap bmp=null;
        //ImageView iv = (ImageView)findViewById(R.id.img1);
        int responseCode = -1;
        try{

            URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
            }

        }
        catch(Exception ex){
            Log.e("Exception",ex.toString());
        }
        return bmp;

    }

    private void startFaceCaptureActivity(ImageView v) {
        FaceCaptureConfiguration config = new FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build();

        FaceSDK.Instance().presentFaceCaptureActivity(getActivity(), config, res ->{
            if (res.getImage() == null) return;
            bitmap = res.getImage().getBitmap();
            v.setImageBitmap(res.getImage().getBitmap());
            v.setTag(ImageType.LIVE);
            MatchFacesImage faceImage = new MatchFacesImage(bitmap, ImageType.LIVE, true);
            imageList.add(faceImage);
            //imageUri = saveImage(bitmapImage, getActivity());
            matchFaces();

            //Toast.makeText(getContext().getApplicationContext(),"Image Saved",Toast.LENGTH_SHORT).show();
        });
    }


}