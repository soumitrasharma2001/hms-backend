package com.hms.appointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.DoctorName;
import com.hms.appointment.dto.MedicineDTO;
import com.hms.appointment.dto.PatientName;
import com.hms.appointment.dto.PrescriptionDTO;
import com.hms.appointment.dto.PrescriptionDetails;
import com.hms.appointment.entity.Prescription;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.PrescriptionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService{
	private final PrescriptionRepository prescriptionRepo;
	private final MedicineService medicineService;
	private final ProfileClient profileClient;
	@Override
	public Long savePrescription(PrescriptionDTO request) {
		request.setPrescriptionDate(LocalDate.now());
		Long prescriptionId= prescriptionRepo.save(request.toEntity()).getId();
		request.getMedicines().forEach(medicine->medicine.setPrescriptionId(prescriptionId));
		medicineService.saveAllMedicines(request.getMedicines());
		return prescriptionId;
	}

	@Override
	public PrescriptionDTO getPrescriptionByAppointmentId(Long appointmentId) throws HmsException{
		PrescriptionDTO prescriptionDTO=prescriptionRepo.findByAppointment_Id(appointmentId)
				.orElseThrow(()->new HmsException("PRESCRIPTION_NOT_FOUND")).toDTO();
		prescriptionDTO.setMedicines(medicineService.getAllMedicinesByPrescriptionId(prescriptionDTO.getId()));
		return prescriptionDTO;
	}

	@Override
	public PrescriptionDTO getPrescriptionById(Long prescriptionId) throws HmsException {
		PrescriptionDTO prescriptionDTO=prescriptionRepo.findById(prescriptionId)
				.orElseThrow(()->new HmsException("PRESCRIPTION_NOT_FOUND")).toDTO();
		prescriptionDTO.setMedicines(medicineService.getAllMedicinesByPrescriptionId(prescriptionDTO.getId()));
		return prescriptionDTO;
	}

	@Override
	public List<PrescriptionDetails> getPrescriptionsByPatientId(Long patientId) throws HmsException {
		List<Prescription> prescriptions = prescriptionRepo.findAllByPatientId(patientId);
		List<PrescriptionDetails> prescriptionDetails = prescriptions.stream()
				.map(Prescription::toPrescriptionDetails).toList();
		prescriptionDetails.forEach(details->{
			details.setMedicines(medicineService.getAllMedicinesByPrescriptionId(details.getId()));
		});
		List<Long> doctorIds = prescriptionDetails.stream().map(PrescriptionDetails::getDoctorId).toList();
		List<DoctorName> doctors = profileClient.getDoctorsById(doctorIds);
		Map<Long,String> doctorMap = doctors.stream()
				.collect(Collectors.toMap(DoctorName::getId, DoctorName::getName));
		prescriptionDetails.stream().forEach(detail->{
			String doctorName = doctorMap.get(detail.getDoctorId());
			if(doctorName!=null)
				detail.setDoctorName(doctorName);
			else
				detail.setDoctorName("Unknown Doctor");
	 	});
		return prescriptionDetails;
	}

	@Override
	public List<PrescriptionDetails> getPrescriptions() throws HmsException {
		List<Prescription> prescriptions = (List<Prescription>) prescriptionRepo.findAll();
		List<PrescriptionDetails> prescriptionDetails = prescriptions.stream()
				.map(Prescription::toPrescriptionDetails).toList();
		List<Long> doctorIds = prescriptionDetails.stream().map(PrescriptionDetails::getDoctorId).toList();
		List<Long> patientIds = prescriptionDetails.stream().map(PrescriptionDetails::getPatientId).toList();
		List<DoctorName> doctors = profileClient.getDoctorsById(doctorIds);
		List<PatientName> patients = profileClient.getPatientsById(patientIds);
		Map<Long,String> doctorMap = doctors.stream()
				.collect(Collectors.toMap(DoctorName::getId, DoctorName::getName));
		Map<Long,String> patientMap = patients.stream()
				.collect(Collectors.toMap(PatientName::getId, PatientName::getName));
		prescriptionDetails.stream().forEach(detail->{
			String doctorName = doctorMap.get(detail.getDoctorId());
			String patientName = patientMap.get(detail.getPatientId());
			detail.setDoctorName(doctorName!=null? doctorName:"Unknown Doctor");
			detail.setPatientName(patientName!=null? patientName:"Unknown Patient");
	 	});
		return prescriptionDetails;
		
	}

	@Override
	public List<MedicineDTO> getMedicinesByPatientId(Long patientId) throws HmsException {
		List<Long> pIds=prescriptionRepo.findAllPrescriptionIdsByPatient(patientId);
		return medicineService.getAllMedicinesByPrescriptionIds(pIds);
	}
}
