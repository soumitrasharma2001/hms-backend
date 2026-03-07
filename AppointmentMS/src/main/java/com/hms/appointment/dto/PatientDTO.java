package com.hms.appointment.dto;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
	private Long id;
	private String name;
	private String email;
	private LocalDate dob;
	private String phoneNo;
	private String address;
	private String aadharNo;
	private BloodGroup bloodGroup;
	private String allergies;
	private String chronicDisease;
	
}
