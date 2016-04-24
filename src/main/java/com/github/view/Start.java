package com.github.view;

import javax.swing.*;
import java.awt.*;

/**
 * Copyright (C) Coderion sp. z o.o
 */
public class Start {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (IllegalAccessException | ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException e) {
                e.printStackTrace();
            }
            mainForm.setContentPane(mainForm.getContentPane());
            mainForm.pack();
            mainForm.setSize(700, 500);
            mainForm.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - mainForm.getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - mainForm.getSize().height) / 2);
            mainForm.setVisible(true);
            mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}
