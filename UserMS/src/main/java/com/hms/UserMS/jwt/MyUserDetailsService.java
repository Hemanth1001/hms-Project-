package com.hms.UserMS.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hms.UserMS.dto.UserDTO;
import com.hms.UserMS.exception.HMSException;
import com.hms.UserMS.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            UserDTO userDTO = userService.getUser(email);
            return new CustomUserDetails(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRole(),
                userDTO.getName(),
                userDTO.getProfileId(),
                null // You can set authorities here if needed
            );

        }
        catch(HMSException e){
            e.printStackTrace();
        }
        return null;
    }
    
}
