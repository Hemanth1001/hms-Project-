package com.hms.ProfileMS.service;
import com.hms.ProfileMS.dto.DoctorDTO;
import com.hms.ProfileMS.exception.HMSException;

public interface DoctorService {

    public Long addDoctor(DoctorDTO doctorDTO) throws HMSException;
    public DoctorDTO getDoctorById(Long id) throws HMSException;
    public DoctorDTO updatePatient(DoctorDTO doctorDTO) throws HMSException ;
    public Boolean doctorExists(Long id) throws HMSException;

    
    
} 
