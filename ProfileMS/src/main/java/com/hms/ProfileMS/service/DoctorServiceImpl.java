package com.hms.ProfileMS.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hms.ProfileMS.dto.DoctorDTO;
import com.hms.ProfileMS.exception.HMSException;
import com.hms.ProfileMS.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService{

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public Long addDoctor(DoctorDTO doctorDTO) {
        if(doctorDTO.getEmail()!=null && doctorRepository.findByEmail(doctorDTO.getEmail()).isPresent()) throw new HMSException("DOCTOR_ALREADY_EXISTS");
         //if(doctorDTO.getEmail()!=null && doctorRepository.findByLicenseNo(doctorDTO.getLicenseNo()).isPresent()) throw new HMSException("DOCTOR_ALREADY_EXISTS");
       return doctorRepository.save(doctorDTO.toEntity()).getId();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow(()-> new HMSException("DOCTOR_NOT_FOUND")).toDTO();
    }

    @Override
    public DoctorDTO updatePatient(DoctorDTO doctorDTO) {
        doctorRepository.findById(doctorDTO.getId()).orElseThrow(()-> new HMSException("DOCTOR_NOT_FOUND"));
        return doctorRepository.save(doctorDTO.toEntity()).toDTO();

    }

    @Override
    public Boolean doctorExists(Long id) throws HMSException {
       return doctorRepository.existsById(id);
    }
    
}
