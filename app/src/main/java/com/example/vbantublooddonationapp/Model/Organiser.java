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

    @ColumnInfo(name = "picIcNo")
    public String picIcNo;

    @ColumnInfo(name = "address")
    public String address;

    public Organiser(String email, String companyName, String password, String picName, String contact, String picIcNo, String address) {
        this.email = email;
        this.companyName = companyName;
        this.password = password;
        this.picName = picName;
        this.contact = contact;
        this.picIcNo = picIcNo;
        this.address = address;
    }
}
