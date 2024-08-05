package com.Ekatalog.controller;

import com.Ekatalog.dto.ListProjectDTO;
import com.Ekatalog.model.ListProjectModel;
import com.Ekatalog.service.ListProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/list_project")
public class ListProjectController {

    @Autowired
    private ListProjectService listProjectService;

    @GetMapping("/all")
    public List<ListProjectModel> getAllProjects() {
        return listProjectService.getAllProjects();
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<ListProjectModel> getProjectById(@PathVariable Long id) {
        Optional<ListProjectModel> project = listProjectService.getProjectById(id);
        if (project.isPresent()) {
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ListProjectModel addProject(@RequestBody ListProjectModel listProject) {
        return listProjectService.addProject(listProject);
    }

    @PutMapping("/ubah/{id}")
    public ResponseEntity<ListProjectModel> updateProject(@PathVariable Long id, @RequestBody ListProjectModel projectDetails) {
        try {
            ListProjectModel updatedProject = listProjectService.updateProject(id, projectDetails);
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/hapus/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        listProjectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
