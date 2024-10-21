package com.Ekatalog.model;

public class PasswordChangeRequest {
    private Long userId;
    private String passwordLama;
    private String passwordBaru;
    private String konfirmasiPassword;

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPasswordLama() {
        return passwordLama;
    }

    public void setPasswordLama(String passwordLama) {
        this.passwordLama = passwordLama;
    }

    public String getPasswordBaru() {
        return passwordBaru;
    }

    public void setPasswordBaru(String passwordBaru) {
        this.passwordBaru = passwordBaru;
    }

    public String getKonfirmasiPassword() {
        return konfirmasiPassword;
    }

    public void setKonfirmasiPassword(String konfirmasiPassword) {
        this.konfirmasiPassword = konfirmasiPassword;
    }
}
