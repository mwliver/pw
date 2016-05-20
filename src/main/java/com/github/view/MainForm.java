package com.github.view;

import com.github.dao.DirectoryRepository;
import com.github.dao.FileRepository;
import com.github.utils.DirectoryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ApplicationContext context;

    public MainForm() {
        add(panel1);

        taResult.setEditable(false);

        fileChooser.setVisible(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        progressBar1.setVisible(false);

        btnIndexFiles.addActionListener(e -> {
            fileChooser.setVisible(true);

            progressBar1.setString("");
            progressBar1.setVisible(true);
            progressBar1.setValue(0);
            progressBar1.setIndeterminate(false);
            progressBar1.setStringPainted(true);

            repaint();

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                DirectoryParser directoryParser = context.getBean(DirectoryParser.class);
                directoryParser.setProgressBar(progressBar1);
                directoryParser.setFileChooser(fileChooser);
                directoryParser.execute();
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
                    sb.append(File.separator);
                    sb.append(file.getName());
                    sb.append("\n");

                    File fileSys = new File(sb.toString());
                    // porównaj to co w bazie z tym co w systemie i wyswietl tylko aktualne pliki
                    if (fileSys.exists()) {
                        try {
                            String contents = new String(Files.readAllBytes(Paths.get(fileSys.getAbsolutePath())), StandardCharsets.UTF_8);
                            if (contents.toLowerCase().contains(file.getContent())) {
                                sb.append(sb.append("\n"));
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                taResult.setText(sb.toString());
            }
        });
    }
}
