package com.github.threads;

import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by Coderion
 */
public class DirectoryParser extends Thread {

    private final List<File> files;
    private boolean noDirectory = false;

    public DirectoryParser(List<File> files) {
        this.files = files;
    }

    @Override
    public void run() {

        ArrayList<File> fileList = new ArrayList<>();
        ArrayList<File> directoryList = new ArrayList<>();

        // dla każdego katalogu weż pliki i odpal dla nich wątki
        while (!noDirectory) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    fileList.add(file);
                } else {
                    directoryList.add(file);
                }
            }

            if (!CollectionUtils.isEmpty(fileList)) {
                FileParser fileParser = new FileParser(fileList);
                fileParser.start();
            }

            if (!CollectionUtils.isEmpty(directoryList)) {
                DirectoryParser directoryParser = new DirectoryParser(directoryList);
                directoryParser.start();
            }

            // czy jest jeszcze katalog do parsowania w ścieżce
            // jeśli nie to jesteśmy w liściach
            noDirectory = directoryList.size() == 0;
        }
    }
}
