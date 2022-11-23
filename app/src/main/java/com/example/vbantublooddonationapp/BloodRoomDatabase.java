package com.example.vbantublooddonationapp;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vbantublooddonationapp.DAO.AppointmentDao;
import com.example.vbantublooddonationapp.DAO.BloodRequestDao;
//import com.example.vbantublooddonationapp.DAO.CommunityPostDao;
import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.DAO.UserDao;
import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.BloodRequest;
//import com.example.vbantublooddonationapp.Model.CommunityPost;
import com.example.vbantublooddonationapp.Model.Organiser;
import com.example.vbantublooddonationapp.Model.User;

@Database(entities = {User.class, Organiser.class, Appointment.class, BloodRequest.class}, version = 1, exportSchema = true)
public abstract class BloodRoomDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract OrganiserDao organiserDao();
    public abstract AppointmentDao appointmentDao();
    public abstract BloodRequestDao bloodRequestDao();
    //public abstract CommunityPostDao communityPostDao();

    public static BloodRoomDatabase INSTANCE;

    public static BloodRoomDatabase getINSTANCE(Context context){

        if(INSTANCE == null){
            synchronized (BloodRoomDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        BloodRoomDatabase.class, "blood_database")
                        .createFromAsset("database/vbantu_database.db")
                        .build();
            }
        }
        return INSTANCE;
    }
}