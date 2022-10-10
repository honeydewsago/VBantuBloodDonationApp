package com.example.vbantublooddonationapp.Model;

public class LoginParams {
    String email;
    String password;

    public LoginParams(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
