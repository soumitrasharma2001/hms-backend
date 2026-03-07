package com.hms.media.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hms.media.dto.MediaFileDTO;
import com.hms.media.entity.MediaFile;

@Service
public interface MediaService {
	MediaFileDTO storeFile(MultipartFile file) throws IOException;
	Optional<MediaFile> getFile(Long id) throws IOException;
	MediaFileDTO updateFile(Long id,MultipartFile file) throws IOException;
}
