package com.ivoryartwork.plugin.folivora;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class MainDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField classNameField;
    private JTextArea sqlScriptField;
    private VirtualFile currentVF;

    public MainDialog(VirtualFile currentVF) {
        this.currentVF = currentVF;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        final String className = classNameField.getText();
        final String sqlScript = sqlScriptField.getText();

        Application application = ApplicationManager.getApplication();
        application.runWriteAction(new Runnable() {
            @Override
            public void run() {
                SQLScriptResolver sqlScriptResolver = new SQLScriptResolver();
                Table table = sqlScriptResolver.getTable(sqlScript);
                FileGenerator fileGenerator = new FileGenerator();
                try {
                    fileGenerator.generateModelClass(table, currentVF, className);
                    fileGenerator.generateMapperXml(table, currentVF, className);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dispose();
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
