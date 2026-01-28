package com.hms.UserMS.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.UserMS.clients.ProfileClient;
import com.hms.UserMS.dto.Roles;
import com.hms.UserMS.dto.UserDTO;
import com.hms.UserMS.entity.User;
import com.hms.UserMS.exception.HMSException;
import com.hms.UserMS.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileClient profileClient;

    @Override
    public void registerUser(UserDTO userDTO) throws HMSException {
        Optional<User> opt = userRepository.findByEmail(userDTO.getEmail());
        if (opt.isPresent()) {
            throw new HMSException("USER_ALREADY_EXISTS");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Long profileId = null;
        if (userDTO.getRole().equals(Roles.DOCTOR)) {
            profileId = profileClient.addDoctor(userDTO);
        } else if (userDTO.getRole().equals(Roles.PATIENT)) {
            profileId = profileClient.addPatient(userDTO);
        }
        userDTO.setProfileId(profileId);
        userRepository.save(userDTO.toEntity());
    }

    @Override
    public UserDTO loginUser(UserDTO userDTO) throws HMSException {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new HMSException("USER_NOT_FOUND"));
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new HMSException("INVALID_CREDENTIALS");
        }
        user.setPassword(null);
        return user.toDTO();
    }

    @Override
    public UserDTO getUserById(Long id) throws HMSException {
        return userRepository.findById(id).orElseThrow(() -> new HMSException("USER_NOT_FOUND")).toDTO();
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public UserDTO getUser(String email) throws HMSException {
        return userRepository.findByEmail(email).orElseThrow(() -> new HMSException("USER_NOT_FOUND")).toDTO();
    }

    @Override
    public Long getProfile(Long id) throws HMSException {
        User user = userRepository.findById(id).orElseThrow(() -> new HMSException("USER_NOT_FOUND"));
        if (user.getRole().equals(Roles.DOCTOR)) {
            return profileClient.getDoctor(user.getProfileId());
        } else if (user.getRole().equals(Roles.PATIENT)) {
            return profileClient.getPatient(user.getProfileId());
        }
        throw new HMSException("INVALID_USER_ROLE");
    }
}
