package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.AppointmentDao;
import com.example.vbantublooddonationapp.DAO.BloodRequestDao;
import com.example.vbantublooddonationapp.DAO.OrganiserDao;
import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Model.BloodRequest;
import com.example.vbantublooddonationapp.Model.Organiser;

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

    public LiveData<List<Appointment>> getRequestByUserId(int id) {
        LiveData<List<Appointment>> list =null;

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

    public LiveData<List<Appointment>> getAppointmentByOrganiserID(int id) {
        LiveData<List<Appointment>> list = null;

        try {
            list = new AppointmentRepository.getAppointmentAsyncTask(mAppointmentDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getAppointmentAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Appointment>>> {
        private AppointmentDao mSyncTaskDao;

        getAppointmentAsyncTask(AppointmentDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<Appointment>> doInBackground(Integer...id) {
            LiveData<List<Appointment>> appointmentList = mSyncTaskDao.getAppointmentByOrganiserID(id[0]);
            return appointmentList;
        }
    }

    public void update(Appointment appointment) {
        new updateAsyncTask(mAppointmentDao).execute(appointment);
    }

    private static class updateAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private AppointmentDao mSyncTaskDao;

        updateAsyncTask(AppointmentDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Appointment...appointments) {
            mSyncTaskDao.update(appointments[0]);
            return null;
        }
    }

}
