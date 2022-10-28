package com.example.callingallvisitors;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.callingallvisitors.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    EditText name, surname, email, password, confirmPassword;
    Button registerBtn, cancelBtn;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View register = inflater.inflate(R.layout.fragment_register, container, false);

        name = register.findViewById(R.id.edtName);
        surname = register.findViewById(R.id.edtSurname);
        email = register.findViewById(R.id.edtEmail);
        password = register.findViewById(R.id.edtPassword);
        confirmPassword = register.findViewById(R.id.edtConPass);
        registerBtn = register.findViewById(R.id.btnRegister);
        cancelBtn = register.findViewById(R.id.btnCancel);

        return register;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String uName = name.getText().toString();
                String uSurname = surname.getText().toString();
                String uEmail = email.getText().toString();
                String uPassword = password.getText().toString();
                String uCPassword = confirmPassword.getText().toString();

                User user;

                user = new User(uName, uSurname, uEmail, uPassword);

                if(uPassword.equals(uCPassword))
                {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //user.setPassword(User.hidePasswordWithAsterix(user.getPassword()));
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference();
                                        myRef.child("Users").child(mAuth.getUid()).setValue(user);
                                        Toast.makeText(getActivity(), "Registration Successful.",
                                                Toast.LENGTH_SHORT).show();
                                        FragmentManager fm = getParentFragmentManager();
                                        fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Welcome.class,null).addToBackStack(null).commit();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getActivity(), "Registration failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(getActivity(), "Please make sure your password is typed correctly in both spaces.",
                            Toast.LENGTH_SHORT).show();

                }





            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, Welcome.class,null).addToBackStack(null).commit();
            }
        });
    }
}