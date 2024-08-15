package com.Ekatalog.controller;

import com.Ekatalog.auth.PenggunaService;
import com.Ekatalog.auth.UserDetailService;
import com.Ekatalog.dto.LoginRequest;
import com.Ekatalog.dto.UserDTO;
import com.Ekatalog.exception.CommonResponse;
import com.Ekatalog.exception.NotFoundException;
import com.Ekatalog.exception.ResponseHelper;
import com.Ekatalog.model.UserModel;
import com.Ekatalog.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    PenggunaService penggunaService;

    @PutMapping("/users/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long userId, @RequestBody UserDTO userDTO) {
        try {
            UserModel updatedUser = penggunaService.updateUser(userId, userDTO);
            if (updatedUser != null) {
                updatedUser.setPassword("");
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to update user: " + e.getMessage()));
        }
    }

    @GetMapping("/users/by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long userId) {
        try {
            UserModel user = penggunaService.findById(userId);
            if (user != null) {
                user.setPassword(null); // Clear password for security reasons
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to retrieve user: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public CommonResponse<?> authenticatePengguna(@RequestBody LoginRequest loginRequest) {
        return ResponseHelper.ok(penggunaService.login(loginRequest));
    }
    @PostMapping("/register")
    public CommonResponse<UserModel> register(@RequestBody UserDTO userModel){
        return ResponseHelper.ok(penggunaService.addPengguna(userModel));
    }

    @PostMapping("/upload/image/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestPart("image") MultipartFile image) {
        try {
            UserModel updatedUser = penggunaService.uploadImage(id, image);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/edit/image/{id}")
    public ResponseEntity<?> updateImage(@PathVariable Long id, @RequestPart("image") MultipartFile image) {
        try {
            UserModel updatedUser = penggunaService.uploadImage(id, image);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}