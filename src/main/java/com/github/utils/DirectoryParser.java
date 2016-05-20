package com.github.utils;

import com.github.dao.DirectoryRepository;
import com.github.dao.FileRepository;
import com.github.model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Scope("prototype")
public class DirectoryParser extends SwingWorker<Void, Integer> {
    private JProgressBar progressBar;
    private JFileChooser fileChooser;
    @Autowired
    private DirectoryRepository directoryRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ApplicationContext context;

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    @Override
    protected void done() {
        progressBar.setValue(100);
        progressBar.setString("Gotowe!");
        progressBar.setVisible(false);
    }

    @Override
    public Void doInBackground() {
        File[] files = fileChooser.getSelectedFile().listFiles();

        if (files != null) {
            CopyOnWriteArrayList<File> fileList = new CopyOnWriteArrayList<>(files);

            progressBar.setString("Wczytywanie plików...");

            for (File file : files) {
                if (file.isDirectory())
                    FileWalker.getFiles(file, fileList);
                else
                    fileList.add(file);
            }

            if (!CollectionUtils.isEmpty(fileList)) {
                double count = fileList.size();
                int counter = 0;

                progressBar.setString("Zapis...");

                for (File file : fileList) {
                    Directory directory = directoryRepository.getDirectoryByPath(file.getParentFile().getAbsolutePath());

                    if (directory == null) {
                        directory = new Directory();

                        directory.setName(file.getParentFile().getName());
                        directory.setPath(file.getParentFile().getAbsolutePath());

                        directoryRepository.save(directory);
                        directoryRepository.flush();
                    }

                    progressBar.setValue((int) (100 * counter / count));

                    counter++;
                }

                FileParser fileParser = context.getBean(FileParser.class);
                fileParser.setFiles(fileList);
                fileParser.execute();

                progressBar.setVisible(false);
            }
        }

        return null;
    }
}
