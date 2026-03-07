package com.hms.media.clients;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.hms.media.config.FeignClientInterceptor;

@FeignClient(name="ProfileMS",url= "${profilems.url}",configuration=FeignClientInterceptor.class)
public interface ProfileClient {
	@PutMapping("profile/doctor/setProfilePicture/{profileId}/{profilePictureId}")
	Boolean setDoctorProfilePicture(@PathVariable Long profileId,@PathVariable Long profilePictureId);
	
	@PutMapping("profile/patient/setProfilePicture/{profileId}/{profilePictureId}")
	Boolean setPatientProfilePicture(@PathVariable Long profileId,@PathVariable Long profilePictureId);
}
