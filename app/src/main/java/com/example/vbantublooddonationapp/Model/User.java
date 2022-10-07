package com.example.vbantublooddonationapp.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "fullName")
    public String fullName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "icNo")
    public String icNo;

    @ColumnInfo(name = "dateOfBirth")
    public String dateOfBirth;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "contact")
    public String contact;

    @ColumnInfo(name = "bloodType")
    public String bloodType;

    public User(String username, String password, String fullName, String email, String icNo, String dateOfBirth, String gender, String contact, String bloodType) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.icNo = icNo;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contact = contact;
        this.bloodType = bloodType;
    }
}
