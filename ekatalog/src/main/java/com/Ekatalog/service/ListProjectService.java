package com.Ekatalog.service;

import com.Ekatalog.model.ListProjectModel;
import com.Ekatalog.repository.ListProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListProjectService {
    @Autowired
    private ListProjectRepository listProjectRepository;

    public List<ListProjectModel> getAllProjects() {
        return listProjectRepository.findAll();
    }

    public Optional<ListProjectModel> getProjectById(Long id) {
        return listProjectRepository.findById(id);
    }

    public ListProjectModel addProject(ListProjectModel listProject) {
        return listProjectRepository.save(listProject);
    }

    public ListProjectModel updateProject(Long id, ListProjectModel projectDetails) {
        ListProjectModel project = listProjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        project.setNama_project(projectDetails.getNama_project());
        project.setTeknologi(projectDetails.getTeknologi());
        project.setDeveloper(projectDetails.getDeveloper());
        project.setLogo(projectDetails.getLogo());
        project.setLink(projectDetails.getLink());

        return listProjectRepository.save(project);
    }

    public void deleteProject(Long id) {
        listProjectRepository.deleteById(id);
    }
}
