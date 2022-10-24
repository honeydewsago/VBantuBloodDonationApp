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

    public int getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(int organiserID) {
        this.organiserID = organiserID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPicIcNo() {
        return picIcNo;
    }

    public void setPicIcNo(String picIcNo) {
        this.picIcNo = picIcNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
