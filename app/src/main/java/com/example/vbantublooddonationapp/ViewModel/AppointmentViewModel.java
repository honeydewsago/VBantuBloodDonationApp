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
    //insert appointment to database
    public void insertAppointment(Appointment appointment) {
        mRepository.insert(appointment);
    }
    //getting live data of appointment with orgraniser id
    public LiveData<List<Appointment>> getAppointmentByOrganiserID(int id) {
        return mRepository.getAppointmentByOrganiserID(id);
    }

    //update date to the database
    public void updateAppointment(Appointment appointment) {
        mRepository.update(appointment);
    }

    //getting live data of appointment with user id
    public LiveData<List<Appointment>> getRequestByUserId(int id) {
        return mRepository.getRequestByUserId(id);
    }

    //getting appointment by appointment i
    public List<Appointment> getAppointmentById(int id) {
        return mRepository.getRequestById(id);
    }

    //getting all appointment tag "Completed"
    public List<Appointment> getAllCompletedAppointment() {
        return mRepository.getAllCompletedAppointment();
    }
}
