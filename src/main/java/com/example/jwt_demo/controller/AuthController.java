package com.example.jwt_demo.controller;

import com.example.jwt_demo.model.Image;
import com.example.jwt_demo.model.User;
import com.example.jwt_demo.repository.ImageRepository;
import com.example.jwt_demo.repository.UserRepository;
import com.example.jwt_demo.role.Role;
import com.example.jwt_demo.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;  // ✅ Image repository ekledik
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtils;

    @PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }
    @PostMapping("/signup")
    @Transactional
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("phonenumber") String phonenumber,
                               @RequestParam("adress") String adress,
                               @RequestParam("password") String password,
                               @RequestParam(value = "image", required = false) MultipartFile image) {
        if (userRepository.existsByUsername(username)) {
            return "Error: Username is already taken!";
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPhonenumber(phonenumber);
        newUser.setAdress(adress);
        newUser.setPassword(encoder.encode(password));
        newUser.setRole(Role.USER);
        userRepository.save(newUser);
        return "User registered successfully!";
    }
}
