package com.hms.user.entity;

import java.time.LocalDateTime;

import com.hms.user.dto.Roles;
import com.hms.user.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(unique=true)
	private String email;
	private String password;
	private Roles role;
	private Long profileId;
	
	@Column(updatable=false,nullable=false)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@PrePersist
	protected void prePersist() {
		this.createdAt=LocalDateTime.now();
	}
	
	@PreUpdate
	protected void preUpdate() {
		this.updatedAt=LocalDateTime.now();
	}
	public UserDTO toDTO() {
		return new UserDTO(this.id,this.name,this.email,this.password,this.role,this.profileId,this.createdAt,this.updatedAt);
	}
}
