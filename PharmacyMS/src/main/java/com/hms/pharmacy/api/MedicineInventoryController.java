package com.hms.pharmacy.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.service.MedicineInventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pharmacy/inventory")
@Validated
@RequiredArgsConstructor
public class MedicineInventoryController {
	private final MedicineInventoryService miService;
	
	@PostMapping("/add")
	public ResponseEntity<MedicineInventoryDTO> add(@RequestBody MedicineInventoryDTO dto) throws HmsException{
		return ResponseEntity.status(HttpStatus.CREATED).body(miService.addMedicine(dto));
	}
	
	@PutMapping("/update")
	public ResponseEntity<MedicineInventoryDTO> update(@RequestBody MedicineInventoryDTO dto) throws HmsException{
		return ResponseEntity.ok(miService.updateMedicine(dto));
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<MedicineInventoryDTO> getById(@PathVariable Long id) throws HmsException{
		return ResponseEntity.ok(miService.getMedicineById(id));
	}
	
	@DeleteMapping("/deleteMedicine/{id}")
	public ResponseEntity<String> deleteMedicine(@PathVariable Long id) throws HmsException{
		miService.deleteMedicine(id);
		return ResponseEntity.ok("Medicine Deleted");
	}
	
	@GetMapping("/getAllInventories")
	public ResponseEntity<List<MedicineInventoryDTO>> getAllMedicines() throws HmsException{
		return ResponseEntity.ok(miService.getAllMedicines());
	}
}
