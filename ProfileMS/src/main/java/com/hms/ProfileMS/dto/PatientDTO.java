package com.hms.ProfileMS.dto;

import java.time.LocalDate;

import com.hms.ProfileMS.entity.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {

    public Long id;
    public String name;
    public String email;
    public LocalDate dob;
    public String phone;    
    public String aadharNo;
    public String address;
    public BloodGroup bloodGroup;
    private String allergies;
    private String chronicDisease;

     public Patient toEntity(){
        return new Patient(this.id,this.name,this.email,this.dob,this.phone,this.address,this.aadharNo,this.bloodGroup,this.allergies,this.chronicDisease);

    }
}
