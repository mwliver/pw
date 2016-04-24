package com.github.view;

import com.github.dao.DirectoryDao;
import com.github.model.Directory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {
    private JTextField tfCatalog;
    private JButton btnParse;
    private JProgressBar progressBar1;
    private JLabel labCatalog;
    private JPanel panel1;

    public MainForm() {
        add(panel1);

        btnParse.addActionListener(e -> {
            Directory directory = new Directory();
            System.out.println(tfCatalog.getText());
            directory.setName(tfCatalog.getText());
            DirectoryDao.createDirectory(directory);
        });
    }
}
