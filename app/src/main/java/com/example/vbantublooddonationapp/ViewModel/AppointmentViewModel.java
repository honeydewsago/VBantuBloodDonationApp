package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Repository.AppointmentRepository;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {

    private AppointmentRepository mRepository;
    //constructure
    public AppointmentViewModel(Application application) {
        super(application);
        mRepository = new AppointmentRepository(application);
    }

    //getting live data of appointment with orgraniser id


    //send to repo to insert appoinment
    public void insertAppointment(Appointment appointment) {
        mRepository.insert(appointment);
    }

    //send to repo to retrieve appointment by organiser id

    public LiveData<List<Appointment>> getAppointmentByOrganiserID(int id) {
        return mRepository.getAppointmentByOrganiserID(id);
    }


    //send to repo to update appointment details

    public void updateAppointment(Appointment appointment) {
        mRepository.update(appointment);
    }

    //send to repo and get request by user id
    public LiveData<List<Appointment>> getRequestByUserId(int id) {
        return mRepository.getRequestByUserId(id);
    }


    //send to repo and get appointment by id
    public List<Appointment> getAppointmentById(int id) {
        return mRepository.getRequestById(id);
    }



    //send to repo and get all completed appointment
    public List<Appointment> getAllCompletedAppointment() {
        return mRepository.getAllCompletedAppointment();
    }
}
