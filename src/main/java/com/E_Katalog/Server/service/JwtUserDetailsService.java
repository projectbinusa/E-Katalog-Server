package com.E_Katalog.Server.service;

import com.E_Katalog.Server.dto.UserDTO;
import com.E_Katalog.Server.model.UserModel;
import com.E_Katalog.Server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public UserModel save(UserDTO user) {
        try {
            if (user.getUsername() != null && user.getPassword() != null && user.getPassword().length() >= 8) {
                if (userRepository.findByUsername(user.getUsername()) == null) {
                    UserModel newUser = new UserModel();
                    newUser.setUsername(user.getUsername());
                    newUser.setEmail(user.getEmail());
                    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
                    return userRepository.save(newUser);
                } else {
                    throw new IllegalArgumentException("Username Telah Di Gunakan");
                }
            } else {
                throw new IllegalArgumentException("Password Harus Lebih Dari 8 Karakter");
            }
        } catch (Exception e) {
            // Catch any other exceptions and convert them to a meaningful error message
            throw new IllegalArgumentException("Pendaftaran Gagal: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username);
        if (user != null) {
            List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
            return new User(username, user.getPassword(), roles);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
