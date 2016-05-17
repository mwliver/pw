package com.github.view;

import com.github.dao.DirectoryRepository;
import com.github.dao.FileRepository;
import com.github.model.Directory;
import com.github.threads.FileParser;
import com.github.threads.FileWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Scope("prototype")
public class MainForm extends JFrame {
    public JFileChooser fileChooser;
    private JButton btnIndexFiles;
    private JProgressBar progressBar1;
    private JLabel labContent;
    private JPanel panel1;
    private JButton btnFindByContent;
    private JTextArea taContent;
    private JLabel labResult;
    private JTextArea taResult;

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ApplicationContext context;

    public MainForm() {
        add(panel1);

        fileChooser.setVisible(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        btnIndexFiles.addActionListener(e -> {
            fileChooser.setVisible(true);
            repaint();

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFile().listFiles();

                if (files != null) {
                    CopyOnWriteArrayList<File> fileList = new CopyOnWriteArrayList<>(files);

                    for (File file : files) {
                        if (file.isDirectory())
                            FileWalker.getFiles(file, fileList);
                        else
                            fileList.add(file);
                    }

                    if (!CollectionUtils.isEmpty(fileList)) {
                        for (File file : fileList) {
                            Directory directory = directoryRepository.getDirectoryByPath(file.getParentFile().getAbsolutePath());

                            if (directory == null) {
                                directory = new Directory();

                                directory.setName(file.getParentFile().getName());
                                directory.setPath(file.getParentFile().getAbsolutePath());

                                directoryRepository.save(directory);
                                directoryRepository.flush();
                            }
                        }

                        FileParser fileParser = context.getBean(FileParser.class);
                        fileParser.setFiles(fileList);
                        fileParser.start();
                    }
                }
            } else {
                System.out.println("No Selection");
            }
        });

        btnFindByContent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String content = taContent.getText();

                List<com.github.model.File> files = fileRepository.getByContent(content);

                StringBuilder sb = new StringBuilder();

                for (com.github.model.File file : files) {
                    sb.append(file.getDirectory().getPath());
                    sb.append(file.getName());
                    sb.append("\n");
                }

                taResult.setText(sb.toString());
            }
        });
    }
}
