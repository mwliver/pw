package com.github.threads;

import com.github.dao.DirectoryRepository;
import com.github.dao.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class FileParser extends Thread {

    private final List<File> files;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    DirectoryRepository directoryRepository;

    public FileParser(List<File> files) {
        this.files = files;
    }

    @Override
    public void run() {
        for (java.io.File file : files) {
            if (!file.isDirectory()) {
                Scanner sc = null;
                try {
                    sc = new Scanner(file);
//                    List<String> content = new CopyOnWriteArrayList<>();
//                    while (sc.hasNextLine()) {
//                        String str = sc.nextLine();
//                        content.add(str);
//                    }

//                    com.github.model.File fileDto = new com.github.model.File();
//                    fileDto.setContentWords(content);
//                    fileDto.setName(file.getName());
//
//                    Directory directory = new Directory();
//                    directory.setName(file.getParent());
//                    directory.setPath(file.getPath());
//
//                    directory = directoryRepository.save(directory);
//                    directoryRepository.flush();
//
//                    fileDto.setDirectory(directory);
//                    fileRepository.save(fileDto);
//                    fileRepository.flush();

                    System.out.println(file.getAbsolutePath());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
