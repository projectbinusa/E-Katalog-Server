package com.Ekatalog.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email wajib di isi")
    private String email;
    @NotBlank(message = "Password wajib di isi")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
