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

    @ColumnInfo(name = "appointmentTime")
    public String appointmentTime;

    @ColumnInfo(name = "appointmentDate")
    public String appointmentDate;

    @ColumnInfo(name = "donationBefore")
    public String donationBefore;

    @ColumnInfo(name = "bloodAmt")
    public String bloodAmt;

    @ColumnInfo(name = "status")
    public int status;
}
