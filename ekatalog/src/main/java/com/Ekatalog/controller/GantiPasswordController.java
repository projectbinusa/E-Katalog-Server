package com.Ekatalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ekatalog.service.GantiPasswordService;
import com.Ekatalog.model.PasswordChangeRequest;

@RestController
@RequestMapping("/api")
public class GantiPasswordController {

    @Autowired
    private GantiPasswordService gantiPasswordService;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        gantiPasswordService.gantiPassword(passwordChangeRequest.getUserId(), passwordChangeRequest);

        return ResponseEntity.ok("Password changed successfully");
    }
}
