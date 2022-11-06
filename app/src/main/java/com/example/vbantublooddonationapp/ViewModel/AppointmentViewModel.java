package com.example.vbantublooddonationapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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

    public List<Appointment> getRequestByUserId(int id){
        return mRepository.getRequestByUserId(id);
    }
}
