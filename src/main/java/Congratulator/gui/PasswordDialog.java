package Congratulator.gui;

import Congratulator.Util.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField passwordField1;

    public PasswordDialog() {
        setSystemLookAndFeel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Congratulator");
        setSize(300, 100);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

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

        pack();
        setVisible(true);
//        System.exit(0);
    }

    private void onOK() {
        GUI.takeLaunchKey(passwordField1.getText());
        Settings.initPasswordsFromLaunchKey();
        setVisible(false);
        GUI.initMainForm();
//        dispose();

    }

    private void onCancel() {
        // add your code here if necessary
        GUI.exit(false);
    }

    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
//            MainLogger.log(Level.WARNING, e.toString());
            e.printStackTrace();
        } catch (InstantiationException e) {
//            MainLogger.log(Level.WARNING, e.toString());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            MainLogger.log(Level.WARNING, e.toString());
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
//            MainLogger.log(Level.WARNING, e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PasswordDialog dialog = new PasswordDialog();
    }
}
