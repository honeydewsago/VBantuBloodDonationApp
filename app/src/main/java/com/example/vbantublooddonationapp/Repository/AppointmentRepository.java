package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.AppointmentDao;
import com.example.vbantublooddonationapp.Model.Appointment;

public class AppointmentRepository {
    private AppointmentDao mAppointmentDao;

    public AppointmentRepository(Application application){
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mAppointmentDao = db.appointmentDao();
    }

    public void insert(Appointment appointment){
        new AppointmentRepository.insertAsyncTask(mAppointmentDao).execute(appointment);
    }

    public class insertAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private AppointmentDao mSyncTaskDao;

        insertAsyncTask(AppointmentDao dao){ mSyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Appointment... appointments) {
            mSyncTaskDao.insert(appointments[0]);
            return null;
        }
    }
}
