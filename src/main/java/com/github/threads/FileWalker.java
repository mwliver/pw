package com.github.threads;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileWalker {
    public static void getFiles(File root, CopyOnWriteArrayList<File> files) {
        File[] fileArr = root.listFiles();
        if (fileArr != null) {
            for (File file : fileArr) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    getFiles(file, files);
                }
            }
        }
    }
}
