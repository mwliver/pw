package com.github.threads;

import com.github.dao.DirectoryRepository;
import com.github.dao.FileRepository;
import com.github.model.Directory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
public class FileParser extends Thread {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private DirectoryRepository directoryRepository;
    private List<File> files;

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public void run() {
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
                        "ini"
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

                        // jeżeli nie ma w bazie to zrób nowy w przeciwnym wypadku nadpisz istniejący
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
            }
        }
    }
}
