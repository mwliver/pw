package com.github.view;

import javax.swing.*;
import java.awt.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml", "beans-datasource.xml");

        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = (MainForm)context.getBean("mainForm");
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
