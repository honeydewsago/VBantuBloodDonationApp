package com.example.vbantublooddonationapp.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "appointment_table")
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "appointmentID")
    public int appointmentID;

    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "organiserID")
    public int organiserID;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "appointmentTime")
    public String appointmentTime;

    @ColumnInfo(name = "appointmentDate")
    public String appointmentDate;

    @ColumnInfo(name = "donationBefore")
    public String donationBefore;

    @ColumnInfo(name = "bloodAmt")
    public int bloodAmt;

    @ColumnInfo(name = "status")
    public String status;

    public Appointment(int userID, int organiserID, String address, String appointmentTime, String appointmentDate, String donationBefore, int bloodAmt, String status) {
        this.userID = userID;
        this.organiserID = organiserID;
        this.address = address;
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
        this.donationBefore = donationBefore;
        this.bloodAmt = bloodAmt;
        this.status = status;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(int organiserID) {
        this.organiserID = organiserID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDonationBefore() {
        return donationBefore;
    }

    public void setDonationBefore(String donationBefore) {
        this.donationBefore = donationBefore;
    }

    public int getBloodAmt() {
        return bloodAmt;
    }

    public void setBloodAmt(int bloodAmt) {
        this.bloodAmt = bloodAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
