package com.example.vbantublooddonationapp.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blood_request_table")
public class BloodRequest {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "requestID")
    public int requestID;

    @ColumnInfo(name = "organiserID")
    public int organiserID;

    @ColumnInfo(name = "requestInfo")
    public String requestInfo;

    @ColumnInfo(name = "shortageType")
    public String shortageType;

    @ColumnInfo(name = "dateTime")
    public String dateTime;

    @ColumnInfo(name = "active")
    public int active;
}
