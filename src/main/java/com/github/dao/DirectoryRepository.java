package com.github.dao;

import com.github.model.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {
    @Query("select d from #{#entityName} d where d.path=:path")
    Directory getDirectoryByPath(@Param("path") String path);
}
