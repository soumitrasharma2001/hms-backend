package com.hms.user.dto;

import java.time.LocalDateTime;

import com.hms.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long id;
	
	@NotBlank(message="Name is mandatory")
	private String name;
	
	@Email
	@NotBlank(message="Email is mandatory")
	private String email;
	
	@NotBlank(message="Password is mandatory")
	@Pattern(
		    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-=\\[\\]{}|;:',.<>?/~`\"])[A-Za-z\\d@$!%*?&#^()_+\\-=\\[\\]{}|;:',.<>?/~`\"]{8,128}$",
		    message = "Password must contain:\n" +
		              "- At least 1 uppercase letter\n" +
		              "- At least 1 lowercase letter\n" +
		              "- At least 1 digit\n" +
		              "- At least 1 special character\n" +
		              "- Length between 8-128 characters")
	private String password;
	private Roles role;
	private Long profileId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	public User toEntity() {
		return new User(this.id,this.name,this.email,this.password,this.role,this.profileId,this.createdAt,this.updatedAt);
	}
}
