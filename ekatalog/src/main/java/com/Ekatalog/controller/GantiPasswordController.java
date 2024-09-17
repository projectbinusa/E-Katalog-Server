package com.Ekatalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Ekatalog.service.GantiPasswordService;
import com.Ekatalog.model.PasswordChangeRequest;

@RestController
@RequestMapping("/api")
public class GantiPasswordController {

    @Autowired
    private GantiPasswordService gantiPasswordService;

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {
            // Call service to handle the password change logic
            gantiPasswordService.gantiPassword(passwordChangeRequest.getUserId(), passwordChangeRequest);
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            // Return error message in case of an exception
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
