package com.github.dao;

import com.github.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, Long> {
    @Query("select f from #{#entityName} f where f.name=:name and f.directory.id=:directoryId")
    File getFileByPath(@Param("name") String name, @Param("directoryId") long directoryId);
}

