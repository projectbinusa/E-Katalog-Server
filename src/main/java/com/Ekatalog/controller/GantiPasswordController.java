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

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {
            // Call service to handle the password change logic
            gantiPasswordService.gantiPassword(userId, passwordChangeRequest);
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            // Return error message in case of an exception
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
