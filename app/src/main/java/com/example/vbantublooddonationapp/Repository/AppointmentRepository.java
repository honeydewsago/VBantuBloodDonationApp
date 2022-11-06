package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.AppointmentDao;
import com.example.vbantublooddonationapp.Model.Appointment;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppointmentRepository {
    private AppointmentDao mAppointmentDao;

    public AppointmentRepository(Application application){
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mAppointmentDao = db.appointmentDao();
    }

    public void insert(Appointment appointment){
        new AppointmentRepository.insertAsyncTask(mAppointmentDao).execute(appointment);
    }

    public List<Appointment> getRequestByUserId(int id) {
        List<Appointment> list =null;

        try{
            list = new AppointmentRepository.getAppointmentAsyncTask(mAppointmentDao).execute(id).get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return list;
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

    public static class getAppointmentAsyncTask extends AsyncTask<Integer, Void, List<Appointment>>{
        private AppointmentDao mSyncTaskDao;

        getAppointmentAsyncTask(AppointmentDao dao){mSyncTaskDao = dao;}

        @Override
        protected List<Appointment> doInBackground(Integer... id) {
            List<Appointment> appointmentList = mSyncTaskDao.getRequestByUserId(id[0]);

            return appointmentList;
        }
    }
}
