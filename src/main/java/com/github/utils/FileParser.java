package com.github.utils;

import com.github.dao.DirectoryRepository;
import com.github.dao.FileRepository;
import com.github.model.Directory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class FileParser extends SwingWorker<Void, Integer> {
    private JProgressBar progressBar;
    private JFileChooser fileChooser;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private DirectoryRepository directoryRepository;
    private List<File> files;

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    protected void done() {
        progressBar.setValue(100);
        progressBar.setString("Gotowe!");
        progressBar.setVisible(false);
    }

    @Override
    public Void doInBackground() {
        this.progressBar.setString("");
        this.progressBar.setVisible(true);
        this.progressBar.setValue(0);
        this.progressBar.setIndeterminate(false);
        this.progressBar.setStringPainted(true);

        double count = files.size();
        int counter = 0;

        progressBar.setString("Zapis plików...");

        for (java.io.File file : files) {
            if (!file.isDirectory()) {

                String extension = FilenameUtils.getExtension(file.getAbsolutePath());

                String[] extensions = new String[]{
                        "txt",
                        "sql",
                        "xml",
                        "html",
                        "c",
                        "cpp",
                        "h",
                        "hpp",
                        "m",
                        "java",
                        "cs",
                        "vb",
                        "vbs",
                        "js",
                        "json",
                        "yaml",
                        "xml",
                        "ini",
                        "conf"
                };

                if (Arrays.asList(extensions)
                        .stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList())
                        .contains(extension)) {
                    try {
                        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);

                        Directory directory = directoryRepository.getDirectoryByPath(file.getParentFile().getAbsolutePath());

                        com.github.model.File fileDto = fileRepository.getFileByPath(file.getName(), directory.getId());

                        if (fileDto == null) {
                            fileDto = new com.github.model.File();
                        }
                        fileDto.setContent(content);
                        fileDto.setName(file.getName());

                        fileDto.setDirectory(directory);
                        fileRepository.save(fileDto);
                        fileRepository.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                progressBar.setValue((int) (100 * counter / count));
                System.out.println(progressBar.getValue());

                counter++;
            }
        }

        return null;
    }
}
