package com.example.vbantublooddonationapp.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "organiser_table")
public class Organiser {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "organiserID")
    public int organiserID;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "companyName")
    public String companyName;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "picName")
    public String picName;

    @ColumnInfo(name = "contact")
    public String contact;

    @ColumnInfo(name = "picICNo")
    public String picICNo;

    @ColumnInfo(name = "address")
    public String address;
}
