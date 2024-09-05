package com.Ekatalog.controller;

import com.Ekatalog.dto.ListProjectDTO;
import com.Ekatalog.exception.NotFoundException;
import com.Ekatalog.model.ListProjectModel;
import com.Ekatalog.service.ListProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/list_project")
public class ListProjectController {

    @Autowired
    private ListProjectService listProjectService;

    @GetMapping("/all")
    public ResponseEntity<List<ListProjectModel>> getAllProjects() {
        List<ListProjectModel> projects = listProjectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<ListProjectModel> getProjectById(@PathVariable Long id) {
        Optional<ListProjectModel> project = listProjectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add/byId/{id}")
    public ResponseEntity<String> addListProject(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image,
            @RequestPart("listProject") String listProjectJson) {

        try {
            // Konversi JSON ke dalam Object
            ObjectMapper objectMapper = new ObjectMapper();
            ListProjectModel listProject = objectMapper.readValue(listProjectJson, ListProjectModel.class);

            ListProjectModel listProjectModel = listProjectService.addProject(id, listProject, image);
            return ResponseEntity.ok("Data project berhasil ditambahkan.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Gagal memproses data.");
        }
    }

//    @PutMapping("/ubah/{id}")
//    public ResponseEntity<ListProjectModel> updateProject(@PathVariable Long id, @RequestBody ListProjectModel projectDetails) {
//        try {
//            ListProjectModel updatedProject = listProjectService.updateProject(id, projectDetails);
//            return ResponseEntity.ok(updatedProject);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PutMapping("/ubah/{id}")
    public ResponseEntity<String> updateListProject(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image,
            @RequestPart("listProject") MultipartFile listProjectJson) {

        try {
            // Convert MultipartFile to String
            String jsonContent = new String(listProjectJson.getBytes());

            // Convert JSON String to Object
            ObjectMapper objectMapper = new ObjectMapper();
            ListProjectModel listProject = objectMapper.readValue(jsonContent, ListProjectModel.class);

            // Update the project
            listProjectService.updateProject(id, listProject, image);
            return ResponseEntity.ok("Data project berhasil diperbarui.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Gagal memproses data: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found: " + e.getMessage());
        }
    }

    @DeleteMapping("/hapus/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        listProjectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload/imagelistproject/{id}")
    public ResponseEntity<?> uploadImageListProject(@PathVariable Long id, @RequestPart("image") MultipartFile image) {
        try {
            ListProjectModel updatedProject = listProjectService.uploadImageListProject(id, image);
            return ResponseEntity.ok(updatedProject);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project tidak ditemukan.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal mengunggah gambar.");
        }
    }

    @PutMapping("/edit/imagelistproject/{id}")
    public ResponseEntity<?> updateImageListProject(@PathVariable Long id, @RequestPart("image") MultipartFile image) {
        try {
            ListProjectModel updatedProject = listProjectService.uploadImageListProject(id, image);
            return ResponseEntity.ok(updatedProject);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project tidak ditemukan.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal mengunggah gambar.");
        }
    }
}
