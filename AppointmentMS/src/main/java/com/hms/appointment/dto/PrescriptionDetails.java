package com.hms.appointment.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDetails {
	private Long id;
	private Long patientId;
	private String patientName;
	private Long doctorId;
	private String doctorName;
	private Long appointmentId;
	private LocalDate prescriptionDate;
	private String notes;
	private List<MedicineDTO> medicines;
}
