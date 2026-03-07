package com.hms.media.service;

import java.io.IOException;


import java.util.Optional;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;


import com.hms.media.clients.ProfileClient;
import com.hms.media.dto.MediaFileDTO;
import com.hms.media.entity.MediaFile;
import com.hms.media.enums.Storage;
import com.hms.media.repository.MediaFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService{
	private final MediaFileRepository mediaFileRepo;
	private final ProfileClient profileClient;
	@Override
	@Transactional
	public MediaFileDTO storeFile(MultipartFile file) throws IOException {
		MediaFile mediaFile = MediaFile.builder()
					.name(file.getOriginalFilename())
					.type(file.getContentType())
					.size(file.getSize())
					.data(file.getBytes())
					.storage(Storage.DB).build();
		MediaFile savedFile=mediaFileRepo.save(mediaFile);
		ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String role = request.getHeader("X-Role");
        String profileId = request.getHeader("X-Profile-Id");
        Boolean status=false;
        if(role.equals("DOCTOR"))
        	status=profileClient.setDoctorProfilePicture(Long.parseLong(profileId),savedFile.getId());
        else if(role.equals("PATIENT"))
        	status=profileClient.setPatientProfilePicture(Long.parseLong(profileId),savedFile.getId());
        if(!status)
        	throw new RuntimeException("PROFILE_PICTURE_NOT_SET");
		return MediaFileDTO.builder()
				.id(savedFile.getId())
				.name(savedFile.getName())
				.type(savedFile.getType())
				.size(savedFile.getSize())
				.build();
	}

	@Override
	public Optional<MediaFile> getFile(Long id) throws IOException {
		return mediaFileRepo.findById(id);
	}

	@Override
	public MediaFileDTO updateFile(Long id,MultipartFile file) throws IOException {
		MediaFile mediaFile=mediaFileRepo.findById(id)
				.orElseThrow(()->new RuntimeException("FILE_NOT_FOUND"));
		mediaFile=MediaFile.builder()
				.name(file.getOriginalFilename())
				.type(file.getContentType())
				.size(file.getSize())
				.data(file.getBytes())
				.storage(Storage.DB).build();
		return mediaFileRepo.save(mediaFile).toDTO();
	}
}
