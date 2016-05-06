package com.github.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class File implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ElementCollection
    private List<String> contentWords = new ArrayList<>();

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

    public List<String> getContentWords() {
        return contentWords;
    }

    public void setContentWords(List<String> contentWords) {
        this.contentWords = contentWords;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }
}
