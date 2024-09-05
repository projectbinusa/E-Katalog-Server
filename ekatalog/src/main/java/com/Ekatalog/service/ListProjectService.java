package com.Ekatalog.service;

import com.Ekatalog.exception.NotFoundException;
import com.Ekatalog.model.ListProjectModel;
import com.Ekatalog.model.UserModel;
import com.Ekatalog.repository.ListProjectRepository;
import com.Ekatalog.repository.UserRepository;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ListProjectService {

    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/e-katalog-8a0a0.appspot.com/o/%s?alt=media";

    @Autowired
    private ListProjectRepository listProjectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ListProjectModel> getAllProjects() {
        return listProjectRepository.findAll();
    }

    public Optional<ListProjectModel> getProjectById(Long id) {
        return listProjectRepository.findById(id);
    }

    public ListProjectModel addProject(Long id , ListProjectModel listProject , MultipartFile image) throws IOException {
        Optional<UserModel> userModel = userRepository.findById(id);
        if (!userModel.isPresent()){
            throw new NotFoundException("id tidak ditemukan : " + id);
        }
        String fileUrl = uploadFotoListProject(image , "ListProject");
        listProject.setNama_project(listProject.getNama_project());
        listProject.setTeknologi(listProject.getTeknologi());
        listProject.setImage(fileUrl);
        listProject.setLink(listProject.getLink());
        listProject.setDeskripsi_project(listProject.getDeskripsi_project());
        listProject.setDeveloper(listProject.getDeveloper());

        return listProjectRepository.save(listProject);
    }

    public ListProjectModel updateProject(Long id, ListProjectModel projectModel, MultipartFile image) throws IOException {
        Optional<ListProjectModel> optionalListProjectModel = listProjectRepository.findById(id);

        // Memeriksa apakah project dengan ID yang diberikan ada
        if (!optionalListProjectModel.isPresent()) {
            throw new NotFoundException("ID tidak ditemukan: " + id);
        }

        // Mengambil model yang sudah ada dari database
        ListProjectModel existingProject = optionalListProjectModel.get();

        // Mengunggah gambar baru jika ada
        String fileUrl = uploadFotoListProject(image, "ListProject");

        // Memperbarui data yang ada dengan data baru
        existingProject.setNama_project(projectModel.getNama_project());
        existingProject.setTeknologi(projectModel.getTeknologi());

        // Hanya memperbarui gambar jika file baru diunggah
        if (fileUrl != null && !fileUrl.isEmpty()) {
            existingProject.setImage(fileUrl);
        }

        existingProject.setLink(projectModel.getLink());
        existingProject.setDeskripsi_project(projectModel.getDeskripsi_project());
        existingProject.setDeveloper(projectModel.getDeveloper());

        // Menyimpan perubahan pada project yang sudah ada
        return listProjectRepository.save(existingProject);
    }

//    public ListProjectModel updateProject(Long id, ListProjectModel projectDetails) {
//        ListProjectModel project = listProjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
//
//        project.setNama_project(projectDetails.getNama_project());
//        project.setTeknologi(projectDetails.getTeknologi());
//        project.setDeveloper(projectDetails.getDeveloper());
//        project.setImage(projectDetails.getImage());
//        project.setLink(projectDetails.getLink());
//        project.setDeskripsi_project(projectDetails.getDeskripsi_project());
//
//        return listProjectRepository.save(project);
//    }

    public void deleteProject(Long id) {
        listProjectRepository.deleteById(id);
    }

    private String uploadFotoListProject(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "list_project/";
        String fullPath = folderPath + timestamp + "_" + fileName;

        String contentType = multipartFile.getContentType();
        if (contentType == null || contentType.equals("application/octet-stream")) {
            contentType = Files.probeContentType(Paths.get(multipartFile.getOriginalFilename()));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        }

        BlobId blobId = BlobId.of("e-katalog-8a0a0.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("FbConfig.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource file FbConfig.json tidak ditemukan di classpath");
        }

        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        storage.create(blobInfo, multipartFile.getBytes());

        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }



    public ListProjectModel uploadImageListProject(Long id , MultipartFile image ) throws NotFoundException, IOException {
        ListProjectModel listProjectModel = listProjectRepository.findById(id)
                .orElseThrow(()  -> new NotFoundException("Id tidak ditemukan"));

        String fileUrl = uploadFotoListProject(image , "fotolistproject_" + id);
        listProjectModel.setImage(fileUrl);

        return listProjectRepository.save(listProjectModel);
    }
}
