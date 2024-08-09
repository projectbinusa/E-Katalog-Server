package com.Ekatalog.auth;


import com.Ekatalog.exception.NotFoundException;
import com.Ekatalog.model.UserModel;
import com.Ekatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    UserRepository penggunaRepository;

    // Metode untuk memuat detail pengguna berdasarkan username/email dari repository Pengguna
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserModel user;
        if (penggunaRepository.findByEmail(username).isPresent()) {
            user = penggunaRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("Username tidak di temukan"));
        }else {
            throw new NotFoundException("Pengguna tidak ditemukan dengan nama pengguna atau email: " + username);
        }
        return UserDetail.buildUser(user);
    }

}
