package com.hms.UserMS.api;
import org.springframework.web.bind.annotation.RestController;

import com.hms.UserMS.dto.LoginDTO;
import com.hms.UserMS.dto.ResponseDTO;
import com.hms.UserMS.dto.UserDTO;
import com.hms.UserMS.exception.HMSException;
import com.hms.UserMS.jwt.JwtUtil;
import com.hms.UserMS.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/users")
@Validated
@CrossOrigin // Allow cross-origin requests CrossOrigin in the sense of any request from any origin
public class UserAPI {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);

        return new ResponseEntity<>(new ResponseDTO("Account Created"),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> PostMethodName(@RequestBody LoginDTO loginDTO) throws HMSException 
    { 
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()
                )
            );
        }
        catch(AuthenticationException e){
            throw new HMSException("INVALID_CREDENTIALS");
        }   
        final var userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> getUserDetails() {
        // Implementation for fetching user details
        return new ResponseEntity<>("User details fetched successfully", HttpStatus.OK);
    }
}