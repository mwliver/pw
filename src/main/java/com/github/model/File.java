package com.github.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class File implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Lob
    private String content;

    @ManyToOne
    private Directory directory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }
}
