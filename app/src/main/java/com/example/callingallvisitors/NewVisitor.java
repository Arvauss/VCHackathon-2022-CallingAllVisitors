package com.example.callingallvisitors;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.callingallvisitors.Models.Visitor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.regula.facesdk.enums.ImageType;
import com.regula.facesdk.model.MatchFacesImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NewVisitor extends Fragment {

    private FirebaseAuth mAuth;
    View visitor;
    EditText name, surname, idNumber, resident, residentPhonenumebr;
    Button scanFace, processVisitor;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    InputImage image;
    Bitmap bitmapImage;
    ImageView imageView;
    FaceDetector detector;
    Uri imageUri;
    StorageReference storageReference;
    protected static final int CAMERA_REQUEST_CODE = 2;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        visitor = inflater.inflate(R.layout.fragment_new_visitor, container, false);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        name = visitor.findViewById(R.id.edtvName);
        surname = visitor.findViewById(R.id.edtvSurname);
        idNumber = visitor.findViewById(R.id.edtID);
        resident = visitor.findViewById(R.id.edtResidentName);
        residentPhonenumebr = visitor.findViewById(R.id.edtResidentPhoneNumber);

        scanFace = visitor.findViewById(R.id.scanFaceBtn);
        processVisitor = visitor.findViewById(R.id.processVisitorBtn);
        return visitor;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mAuth = FirebaseAuth.getInstance();
        scanFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(requireActivity().getPackageManager())
                            != null) {
                        activityResultLauncher.launch(
                                intent);
                    } else {
                        // if the image is not captured, set
                        // a toast to display an error image.
                        Toast
                                .makeText(
                                        getContext().getApplicationContext(),
                                        "Something went wrong",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle extra = result.getData().getExtras();
                bitmapImage = (Bitmap)extra.get("data");
                //WeakReference<Bitmap> result1 = new WeakReference<>(Bitmap.createScaledBitmap(
                //      bitmap,bitmap.getHeight(),bitmap.getWidth(),false).copy(Bitmap.Config.ARGB_8888,true));
                //imageUri = data.getData();
                //Bitmap bm = result1.get();
                imageView.setImageURI(null);
                imageUri = saveImage(bitmapImage, getActivity());
                //collectionImage.setImageURI(imageUri);
                imageView.setImageURI(imageUri);
                MatchFacesImage faceImage = new MatchFacesImage(bitmapImage, ImageType.PRINTED, true);

            }
        });

        FirebaseUser currUser = mAuth.getCurrentUser();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Visitors");
        processVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference fileRef = storageReference.child(System.currentTimeMillis()+ "." + getFileExtension(imageUri));
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext().getApplicationContext(),"Upload of image failed.",Toast.LENGTH_LONG).show();
                    }
                });
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
                Toast.makeText(getActivity(), "Date: " + date ,
                        Toast.LENGTH_SHORT).show();

                visitorPush.setValue(nv);
            }
        });


    }

    private void detectFace(Bitmap bitmap)
    {
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        // we need to create a FirebaseVisionImage object
        // from the above mentioned image types(bitmap in
        // this case) and pass it to the model.
        try {
            image = InputImage.fromBitmap(bitmap,0);
            detector = FaceDetection.getClient(highAccuracyOpts);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // It’s time to prepare our Face Detection model.
        detector.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        String resultText = "";
                        int i = 1;
                        for (Face face :
                                faces) {
                            resultText
                                    = resultText
                                    .concat("\nFACE NUMBER. "
                                            + i + ": ")
                                    .concat(
                                            "\nSmile: "
                                                    + face.getSmilingProbability()
                                                    * 100
                                                    + "%")
                                    .concat(
                                            "\nleft eye open: "
                                                    + face.getLeftEyeOpenProbability()
                                                    * 100
                                                    + "%")
                                    .concat(
                                            "\nright eye open "
                                                    + face.getRightEyeOpenProbability()
                                                    * 100
                                                    + "%");
                            i++;
                        }

                        // if no face is detected, give a toast
                        // message.
                        if (faces.size() == 0) {
                            Toast.makeText(getContext().getApplicationContext(),"No Face Detected", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(),"Face Detected and File uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast
                                .makeText(
                                        getContext().getApplicationContext(),
                                        "Oops, Something went wrong",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private Uri saveImage(Bitmap image, Context context) {
        // Create an image file name(Projects, 2022)
        File imageFolder = new File(context.getCacheDir(),"images");
        Uri uri = null;
        try{
            imageFolder.mkdirs();
            File file = new File(imageFolder,"captured_image.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            //Compress the image taken
            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
            //flush and close the the FileOutputStream
            stream.flush();
            stream.close();
            //Get uri from the FileProvider(Projects, 2022)
            uri = FileProvider.getUriForFile(context.getApplicationContext(),"com.company.callingallvisitors" + ".provider",file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }



        //Return uri.
        return uri;
    }
    private String  getFileExtension(Uri uriImage) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(cr.getType(uriImage));
    }

}