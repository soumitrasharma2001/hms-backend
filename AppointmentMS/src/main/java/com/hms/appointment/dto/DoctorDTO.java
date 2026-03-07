package com.hms.appointment.dto;

import java.time.LocalDate;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
	private Long id;
	private String name;
	private String email;
	private LocalDate dob;
	private String phoneNo;
	private String address;
	private String licenseNo;
	private String specialization;
	private String department;
	private Integer totalExp;
	
}
