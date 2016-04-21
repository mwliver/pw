package com.github.view;

import com.github.dao.DirectoryDao;
import com.github.model.Directory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copyright (C) Coderion sp. z o.o
 */
public class Application extends JFrame {

    public Application() throws HeadlessException {
        ApplicationPanel panel = new ApplicationPanel();
        panel.parsujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Directory directory = new Directory();
                directory.setName("/blebleble/add");
                DirectoryDao.createDirectory(directory);
            }
        });

        add(panel.$$$getRootComponent$$$());

    }

}
