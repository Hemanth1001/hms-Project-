package com.hms.UserMS.service;
import com.hms.UserMS.dto.UserDTO;
import com.hms.UserMS.exception.HMSException;

public interface UserService {
    public void registerUser(UserDTO userDTO) throws HMSException;

    public UserDTO loginUser(UserDTO userDTO) throws HMSException;

    public UserDTO getUserById(Long id) throws HMSException;

    public void updateUser(UserDTO userDTO);

    public UserDTO getUser(String email) throws HMSException;

    public Long getProfile(Long id) throws HMSException;
    
}