package com.hms.media.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.hms.media.dto.MediaFileDTO;
import com.hms.media.enums.Storage;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MediaFile {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String type;
	private Long size;
	
	@Lob
	private byte[] data;
	
	@Enumerated(EnumType.STRING)
	private Storage storage;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	public MediaFile build() {
		return this;
	}

	public MediaFileDTO toDTO() {
		return new MediaFileDTO(id,name,type,size);
	}
}

