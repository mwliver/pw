package com.github.view;

import javax.swing.*;
import java.awt.*;

/**
 * Copyright (C) Coderion sp. z o.o
 */
public class Start {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Application application = new Application();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (IllegalAccessException | ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException e) {
                e.printStackTrace();
            }
            application.setSize(700, 500);
            application.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - application.getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - application.getSize().height) / 2);
            application.setVisible(true);
            application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}
