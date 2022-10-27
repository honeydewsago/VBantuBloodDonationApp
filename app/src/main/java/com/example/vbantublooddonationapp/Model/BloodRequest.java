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

    public BloodRequest(int organiserID, String requestInfo, String shortageType, String dateTime, int active) {
        this.organiserID = organiserID;
        this.requestInfo = requestInfo;
        this.shortageType = shortageType;
        this.dateTime = dateTime;
        this.active = active;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getOrganiserID() {
        return organiserID;
    }

    public void setOrganiserID(int organiserID) {
        this.organiserID = organiserID;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getShortageType() {
        return shortageType;
    }

    public void setShortageType(String shortageType) {
        this.shortageType = shortageType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
