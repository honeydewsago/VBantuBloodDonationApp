package com.example.vbantublooddonationapp.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.BloodRoomDatabase;
import com.example.vbantublooddonationapp.DAO.AppointmentDao;
import com.example.vbantublooddonationapp.Model.Appointment;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppointmentRepository {
    private AppointmentDao mAppointmentDao;

    public AppointmentRepository(Application application) {
        BloodRoomDatabase db = BloodRoomDatabase.getINSTANCE(application);
        mAppointmentDao = db.appointmentDao();
    }

    public void insert(Appointment appointment) {
        new AppointmentRepository.insertAsyncTask(mAppointmentDao).execute(appointment);
    }

    public LiveData<List<Appointment>> getRequestByUserId(int id) {
        LiveData<List<Appointment>> list = null;

        try {
            list = new AppointmentRepository.getAppointmentByUserAsyncTask(mAppointmentDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> getRequestById(int id) {
        List<Appointment> list = null;

        try {
            list = new AppointmentRepository.getAppointmentByIdAsyncTask(mAppointmentDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public class insertAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private AppointmentDao mSyncTaskDao;

        insertAsyncTask(AppointmentDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Appointment... appointments) {
            mSyncTaskDao.insert(appointments[0]);
            return null;
        }
    }

    public LiveData<List<Appointment>> getAppointmentByOrganiserID(int id) {
        LiveData<List<Appointment>> list = null;

        try {
            list = new AppointmentRepository.getAppointmentByOrganiserAsyncTask(mAppointmentDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getAppointmentByOrganiserAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Appointment>>> {
        private AppointmentDao mSyncTaskDao;

        getAppointmentByOrganiserAsyncTask(AppointmentDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<Appointment>> doInBackground(Integer... id) {
            LiveData<List<Appointment>> appointmentList = mSyncTaskDao.getAppointmentByOrganiserID(id[0]);
            return appointmentList;
        }
    }

    private static class getAppointmentByUserAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Appointment>>> {
        private AppointmentDao mSyncTaskDao;

        getAppointmentByUserAsyncTask(AppointmentDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<Appointment>> doInBackground(Integer... id) {
            LiveData<List<Appointment>> appointmentList = mSyncTaskDao.getAppointmentByUserID(id[0]);
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
        protected Void doInBackground(Appointment... appointments) {
            mSyncTaskDao.update(appointments[0]);
            return null;
        }
    }

    public List<Appointment> getAppointmentById(int id) {
        List<Appointment> list = null;

        try {
            list = new getAppointmentByIdAsyncTask(mAppointmentDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getAppointmentByIdAsyncTask extends AsyncTask<Integer, Void, List<Appointment>> {
        private AppointmentDao mSyncTaskDao;

        getAppointmentByIdAsyncTask(AppointmentDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<Appointment> doInBackground(Integer... id) {
            List<Appointment> appointmentList = mSyncTaskDao.getAppointmentById(id[0]);
            return appointmentList;
        }
    }

    public List<Appointment> getAllCompletedAppointment() {
        List<Appointment> list = null;
        try {
            list = new getAllCompletedAppointmentAsyncTask(mAppointmentDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class getAllCompletedAppointmentAsyncTask extends AsyncTask<Void, Void, List<Appointment>> {
        private AppointmentDao mSyncTaskDao;

        getAllCompletedAppointmentAsyncTask(AppointmentDao dao) {
            mSyncTaskDao = dao;
        }

        @Override
        protected List<Appointment> doInBackground(Void... params) {
            List<Appointment> appointmentList = mSyncTaskDao.getAllCompletedAppointment();
            return appointmentList;
        }
    }
}
