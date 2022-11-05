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
    public String bloodAmt;

    @ColumnInfo(name = "status")
    public String status;

    public Appointment(int userID, int organiserID, String address, String appointmentTime, String appointmentDate, String donationBefore, String bloodAmt, String status) {
        this.userID = userID;
        this.organiserID = organiserID;
        this.address = address;
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
        this.donationBefore = donationBefore;
        this.bloodAmt = bloodAmt;
        this.status = status;
    }
}
