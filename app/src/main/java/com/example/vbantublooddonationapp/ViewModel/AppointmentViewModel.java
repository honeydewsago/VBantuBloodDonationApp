package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vbantublooddonationapp.Model.Appointment;
import com.example.vbantublooddonationapp.Repository.AppointmentRepository;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {

    private AppointmentRepository mRepository;
    public AppointmentViewModel(Application application) {
        super(application);
        mRepository = new AppointmentRepository(application);
    }

    public void insertAppointment(Appointment appointment){
        mRepository.insert(appointment);
    }

    public LiveData<List<Appointment>> getAppointmentByOrganiserID(int id){
        return mRepository.getAppointmentByOrganiserID(id);
    }

    public void updateAppointment(Appointment appointment){
        mRepository.update(appointment);
    }


    public LiveData<List<Appointment>> getRequestByUserId(int id){
        return mRepository.getRequestByUserId(id);
    }

    public List<Appointment> getAppointmentById(int id) {
        return mRepository.getRequestById(id);
    }

    public LiveData<List<Appointment>> getAllCompletedAppointment() {
        return mRepository.getAllCompletedAppointment();
    }
}
