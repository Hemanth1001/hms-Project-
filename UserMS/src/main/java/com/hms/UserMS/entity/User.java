package com.hms.UserMS.entity;
import com.hms.UserMS.dto.Roles;
import com.hms.UserMS.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data; 
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")            
@Data
@AllArgsConstructor 
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private Roles role;
    private Long profileId;

    public UserDTO toDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.id);
        userDTO.setName(this.name);
        userDTO.setEmail(this.email);
        userDTO.setRole(this.role);
        userDTO.setPassword(this.password);
        userDTO.setProfileId(this.profileId);
        return userDTO;
    }
}