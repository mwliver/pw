package com.github.view;

import com.github.dao.DirectoryRepository;
import com.github.model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@Scope("prototype")
public class MainForm extends JFrame {
    private JTextField tfCatalog;
    private JButton btnParse;
    private JProgressBar progressBar1;
    private JLabel labCatalog;
    private JPanel panel1;

    @Autowired
    private DirectoryRepository directoryRepository;

    public MainForm() {
        add(panel1);

        btnParse.addActionListener(e -> {
            Directory directory = new Directory();
            directory.setName(tfCatalog.getText());
            directoryRepository.save(directory);
            directoryRepository.flush();
        });
    }
}
