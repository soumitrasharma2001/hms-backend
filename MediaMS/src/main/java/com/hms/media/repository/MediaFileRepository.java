package com.hms.media.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hms.media.entity.MediaFile;

@Repository
public interface MediaFileRepository extends CrudRepository<MediaFile, Long>{
	
}
