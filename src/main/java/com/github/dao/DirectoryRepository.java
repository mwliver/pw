package com.github.dao;

import com.github.model.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {
}
