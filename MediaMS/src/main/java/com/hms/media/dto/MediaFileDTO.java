package com.hms.media.dto;

import com.hms.media.entity.MediaFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaFileDTO {
	private Long id;
	private String name;
	private String type;
	private Long size;
	
	public MediaFile toEntity() {
		return new MediaFile(id,name,type,size,null,null,null);
	}
	
}
