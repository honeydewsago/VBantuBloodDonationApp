package com.example.vbantublooddonationapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vbantublooddonationapp.DAO.UserDao;
import com.example.vbantublooddonationapp.Model.User;

@Database(entities = {User.class}, version = 1, exportSchema = true)
public abstract class BloodRoomDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public static BloodRoomDatabase INSTANCE;

    public static BloodRoomDatabase getINSTANCE(Context context){

        if(INSTANCE == null){
            synchronized (BloodRoomDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        BloodRoomDatabase.class, "blood_database")
                        .build();
            }
        }
        return INSTANCE;
    }
}
