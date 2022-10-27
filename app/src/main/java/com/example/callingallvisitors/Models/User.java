package com.example.callingallvisitors.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

public class User {

    private String name;
    private String surname;
    private String email;
    private String password;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = User.encryptPassword(password);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public User(String email, String password)
    {
        this.email = email;
        this.password = User.encryptPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptPassword(String pass)
    {
        String inputString = pass;
        //below is the way to get the encoding string by using Base64
        String encodedString = Base64.getEncoder().encodeToString(inputString.getBytes());

        return encodedString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decryptPassword(String pass)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(pass);
        String decodedString = new String(decodedBytes);

        return decodedString;
    }

    public static String hidePasswordWithAsterix(String password){
        String asterix = "";
        for (int i = 0; i < password.length(); i++){
            asterix += "*";
        }
        password = asterix;
        return password;
    }
}
