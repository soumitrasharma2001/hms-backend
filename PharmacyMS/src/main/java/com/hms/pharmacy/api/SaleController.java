package com.hms.pharmacy.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.dto.SaleRequest;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.service.SaleItemService;
import com.hms.pharmacy.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pharmacy/sales")
@Validated
@RequiredArgsConstructor
public class SaleController {
	private final SaleService saleService;
	private final SaleItemService saleItemService;
	
	@PostMapping("/create")
	public ResponseEntity<Long> createSale(@RequestBody SaleRequest dto ) throws HmsException{
		return new ResponseEntity<>(saleService.createSale(dto),HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateSale(@RequestBody SaleDTO dto ) throws HmsException{
		saleService.updateSale(dto);
		return ResponseEntity.ok("Sale Updated");
	}
	
	@GetMapping("/getSale/{id}")
	public ResponseEntity<SaleDTO> getSale(@PathVariable Long id ) throws HmsException{
		return ResponseEntity.ok(saleService.getSale(id));
	}
	
	@GetMapping("/getSaleByPrescriptionId/{prescriptionId}")
	public ResponseEntity<SaleDTO> getSaleByPrescriptionId(@PathVariable Long prescriptionId ) throws HmsException{
		return ResponseEntity.ok(saleService.getSale(prescriptionId));
	}
	
	@GetMapping("/getSaleItems/{saleId}")
	public ResponseEntity<List<SaleItemDTO>> getSaleItems(@PathVariable Long saleId) throws HmsException{
		return ResponseEntity.ok(saleItemService.getSaleItemsBySaleId(saleId));
	}
	
	@GetMapping("/getAllSales")
	public ResponseEntity<List<SaleDTO>> getAllSales() throws HmsException{
		return ResponseEntity.ok(saleService.getSaleItems());
	}
}
