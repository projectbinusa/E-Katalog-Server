package com.Ekatalog.controller;

import com.Ekatalog.auth.PenggunaService;
import com.Ekatalog.dto.LoginRequest;
import com.Ekatalog.dto.UserDTO;
import com.Ekatalog.exception.CommonResponse;
import com.Ekatalog.exception.ResponseHelper;
import com.Ekatalog.model.UserModel;
import com.Ekatalog.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public CommonResponse<?> authenticatePengguna(@RequestBody LoginRequest loginRequest) {
        return ResponseHelper.ok(penggunaService.login(loginRequest));
    }
    @PostMapping("/register")
    public CommonResponse<UserModel> register(@RequestBody UserDTO userModel){
        return ResponseHelper.ok(penggunaService.addPengguna(userModel));
    }
}