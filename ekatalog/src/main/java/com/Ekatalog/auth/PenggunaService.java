package com.Ekatalog.auth;

import com.Ekatalog.dto.LoginRequest;
import com.Ekatalog.dto.UserDTO;
import com.Ekatalog.exception.BadRequestException;
import com.Ekatalog.exception.NotFoundException;
import com.Ekatalog.model.UserModel;
import com.Ekatalog.repository.UserRepository;
import com.Ekatalog.security.JwtUtils;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PenggunaService {

    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/e-katalog-8a0a0.appspot.com/o/%s?alt=media";

    @Autowired
    private UserRepository penggunaRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Melakukan proses login pengguna berdasarkan username dan password yang diberikan
    public Map<Object, Object> login(LoginRequest loginRequest) {
        Optional<UserModel> userOptional = penggunaRepository.findByEmail(loginRequest.getEmail());
        UserModel user = userOptional.orElseThrow(() -> new NotFoundException("Username not found"));

        if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);
            Map<Object, Object> response = new HashMap<>();
            response.put("data", user);
            response.put("token", jwt);
            response.put("type_token", user.getRole());
            return response;
        }
        throw new NotFoundException("Password not found");
    }

    // Menambahkan pengguna baru ke dalam sistem berdasarkan data DTO pengguna
    public UserModel addPengguna(UserDTO user) {
        UserModel userModel = new UserModel();
        if (penggunaRepository.findByEmail(user.getEmail()).isPresent()){
        throw new BadRequestException("Username Pengguna sudah digunakan");
        }
        String userPass = user.getPassword().trim();
        boolean PasswordIsNotValid = !userPass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}");
        if (PasswordIsNotValid) throw new BadRequestException("Password not valid!");
        String encodedPassword = encoder.encode(user.getPassword());
        userModel.setPassword(encodedPassword);
        userModel.setUsername(user.getUsername());
        userModel.setEmail(user.getEmail());
        userModel.setRole(user.getRole());
        return penggunaRepository.save(userModel);
    }

    // Mengambil data pengguna berdasarkan ID pengguna

    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "admin/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("e-katalog-8a0a0.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        // Mengakses file FbConfig.json dari classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("FbConfig.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource file FbConfig.json tidak ditemukan di classpath");
        }

        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());

        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }


    public UserModel uploadImage(Long id , MultipartFile image ) throws NotFoundException, IOException {
        UserModel userModelOptional = penggunaRepository.findById(id)
                .orElseThrow(()  -> new NotFoundException("Id tidak ditemukan"));

        String fileUrl = uploadFoto(image , "fotoRoleAdmin_" + id);
        userModelOptional.setImage(fileUrl);

        return penggunaRepository.save(userModelOptional);
    }

}
