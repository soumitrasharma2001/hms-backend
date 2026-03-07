package com.hms.pharmacy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
	private Long prescriptionId;
	private String buyerName;
	private String buyerContact;
	private Double totalAmount;
	private List<SaleItemDTO> saleItems;
}
