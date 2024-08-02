package com.Ekatalog.repository;

import com.Ekatalog.model.ListProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListProjectRepository extends JpaRepository<ListProjectModel, Long> {
}
