package com.github.view;

import com.github.dao.DirectoryRepository;
import com.github.dao.FileRepository;
import com.github.model.Directory;
import com.github.threads.FileParser;
import com.github.threads.FileWalker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Scope("prototype")
public class MainForm extends JFrame {
    public JFileChooser fileChooser;
    private JButton btnIndexFiles;
    private JProgressBar progressBar;
    private JLabel labContent;
    private JPanel panel1;
    private JButton btnFindByContent;
    private JTextArea taContent;
    private JLabel labResult;
    private JTextArea taResult;
    private SwingWorker<Integer, Integer> worker;

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ApplicationContext context;

    public MainForm() {
        add(panel1);

        taResult.setEnabled(false);

        fileChooser.setVisible(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        progressBar.setVisible(false);

        btnIndexFiles.addActionListener(e -> {
            fileChooser.setVisible(true);
            progressBar.setVisible(true);
            progressBar.setValue(0);
            progressBar.setIndeterminate(false);
            progressBar.setStringPainted(true);
            repaint();

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFile().listFiles();

                worker = new SwingWorker<Integer, Integer>() {
                    @Override
                    protected void process(List<Integer> chunks) {
                        for (Integer chunk : chunks) {
                            progressBar.setValue(chunk);
                            repaint();
                        }
                    }

                    @Override
                    protected void done() {
                        progressBar.setValue(100);
                        repaint();
                    }

                    @Override
                    public Integer doInBackground() {
                        Random random = new Random();
                        int progress = 0;
                        setProgress(0);
                        while (progress < 100) {
                            progress += random.nextInt(10);
                            setProgress(Math.min(progress, 100));
                            publish(getProgress());
                        }
                        return getProgress();
                    }
                };
                worker.execute();

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
                    StringBuilder pathSb = new StringBuilder()
                            .append(file.getDirectory().getPath())
                            .append("/")
                            .append(file.getName());
                    File fileSys = new File(pathSb.toString());
                    // porównaj to co w bazie z tym co w systemie i wyswietl tylko aktualne pliki
                    if (fileSys.exists()) {
                        try {
                            String contents = new String(Files.readAllBytes(Paths.get(fileSys.getAbsolutePath())), StandardCharsets.UTF_8);
                            if (contents.toLowerCase().contains(file.getContent())) {
                                sb.append(pathSb.append("\n"));
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setVisible(true);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel1.add(progressBar, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fileChooser = new JFileChooser();
        panel1.add(fileChooser, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnFindByContent = new JButton();
        btnFindByContent.setText("Szukaj wg treści");
        btnFindByContent.setMnemonic('S');
        btnFindByContent.setDisplayedMnemonicIndex(0);
        btnFindByContent.setVisible(true);
        panel1.add(btnFindByContent, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labContent = new JLabel();
        labContent.setText("Treść");
        labContent.setDisplayedMnemonic('T');
        labContent.setDisplayedMnemonicIndex(0);
        panel1.add(labContent, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        taContent = new JTextArea();
        panel1.add(taContent, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        btnIndexFiles = new JButton();
        btnIndexFiles.setText("Indeksuj pliki");
        btnIndexFiles.setMnemonic('I');
        btnIndexFiles.setDisplayedMnemonicIndex(0);
        btnIndexFiles.setVisible(true);
        panel1.add(btnIndexFiles, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labResult = new JLabel();
        labResult.setText("Rezultat");
        labResult.setDisplayedMnemonic('R');
        labResult.setDisplayedMnemonicIndex(0);
        panel1.add(labResult, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        taResult = new JTextArea();
        panel1.add(taResult, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        labContent.setLabelFor(taContent);
        labResult.setLabelFor(taResult);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
