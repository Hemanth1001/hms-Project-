package com.hms.ProfileMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.ProfileMS.dto.PatientDTO;
import com.hms.ProfileMS.entity.Patient;
import com.hms.ProfileMS.exception.HMSException;
import com.hms.ProfileMS.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Long addPatient(PatientDTO patientDTO) throws HMSException {
        
         if(patientDTO.getEmail()!=null && patientRepository.findByEmail(patientDTO.getEmail()).isPresent()) throw new HMSException("PATIENT_ALREADY_EXISTS");
         if(patientDTO.getAadharNo()!=null &&patientRepository.findByAadharNo(patientDTO.getEmail()).isPresent()) throw new HMSException("PATIENT_ALREADY_EXISTS");
        return patientRepository.save(patientDTO.toEntity()).getId();
    }

    @Override
    public PatientDTO getPatientById(Long id) throws HMSException {

        return patientRepository.findById(id).orElseThrow(()-> new HMSException("PATIENT_NOT_FOUND")).toDTO();
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO) {
       patientRepository.findById(patientDTO.getId()).orElseThrow(()-> new HMSException("PATIENT_NOT_FOUND"));
        return patientRepository.save(patientDTO.toEntity()).toDTO();
    }

    @Override
    public Boolean patientExists(Long id) throws HMSException {
        // TODO Auto-generated method stub
        return patientRepository.existsById(id);
    }
}
