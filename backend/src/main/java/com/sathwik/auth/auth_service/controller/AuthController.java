package com.sathwik.auth.auth_service.controller;

import com.sathwik.auth.auth_service.dto.LoginRequest;
import com.sathwik.auth.auth_service.dto.RegisterRequest;
import com.sathwik.auth.auth_service.entity.UserEntity;
import com.sathwik.auth.auth_service.repository.UserRepository;
import com.sathwik.auth.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepo;

    public AuthController(JwtService jwtService,UserRepository userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    // POST maps..
    @PostMapping("/register")
    public ResponseEntity<?> register(
           @RequestBody RegisterRequest dto) {
        try {
            UserEntity savedUser =
                    userRepo.save(new UserEntity(
                            dto.getUserName(),
                            dto.getEmail(),
                            dto.getPassword()
                    ));

            String token = jwtService.generateToken(savedUser.getUserId());
            return ResponseEntity.status(201).body(Map.of("token",token));
        } catch (IllegalArgumentException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Registration unsuccessful! "+e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        try {
            // 1. Fetch user by email
            UserEntity user = userRepo.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 2. Check if the raw password matches the encoded password from DB
            if(!dto.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            // 3. If password matches, generate token
            String token = jwtService.generateToken(user.getUserId());
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            // Return 401 for any failed authentication attempt
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping("/ping")
    public String ping() {
        return "Auth service running";
    }
}

