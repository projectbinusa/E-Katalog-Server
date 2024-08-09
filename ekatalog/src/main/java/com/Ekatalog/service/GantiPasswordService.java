package com.Ekatalog.service;

import com.Ekatalog.model.UserModel;
import com.Ekatalog.model.PasswordChangeRequest;
import com.Ekatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class GantiPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void gantiPassword(Long userId, PasswordChangeRequest passwordChangeRequest) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak di temukan"));

        if (!passwordEncoder.matches(passwordChangeRequest.getPasswordLama(), userModel.getPassword())) {
            throw new RuntimeException("Password lama salah");
        }

        if (!passwordChangeRequest.getPasswordBaru().equals(passwordChangeRequest.getKonfirmasiPassword())) {
            throw new RuntimeException("Password baru dan Konfirmasi password tidak sama");
        }

        userModel.setPassword(passwordEncoder.encode(passwordChangeRequest.getPasswordBaru()));
        userRepository.save(userModel);
    }
}
