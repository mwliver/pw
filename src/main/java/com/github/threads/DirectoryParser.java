package com.github.threads;

import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DirectoryParser extends Thread {

    private final List<File> files;
    private boolean noDirectory = false;

    public DirectoryParser(File file) {
        this.files = Arrays.asList(file.listFiles());
    }

    @Override
    public void run() {

        CopyOnWriteArrayList<File> fileList = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<File> directoryList = new CopyOnWriteArrayList<>();

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
                for (File directory : directoryList) {
                    DirectoryParser directoryParser = new DirectoryParser(directory);
                    directoryParser.start();
                }
            }

            // czy jest jeszcze katalog do parsowania w ścieżce
            // jeśli nie to jesteśmy w liściach
            noDirectory = directoryList.size() == 0;
        }
    }
}

