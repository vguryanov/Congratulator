package Congratulator.gui;

import Congratulator.Util.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.logging.Level;

/**
 * Created by User2 on 24.03.2018.
 */
public class GUI {
    private static MainForm mainForm;
    private static PasswordDialog passwordDialog;

    private GUI() {

    }

    public static MainForm getMainForm() {
        return mainForm;
    }

    public static boolean isRepeatedListCreationAllowed() {
        return getMainForm().isRepeatedListCreationAllowed();
    }

    public static void start() {
        initPasswordDialog();
    }

    public static PasswordDialog initPasswordDialog() {
        PasswordDialog passwordDialog = new PasswordDialog();
        GUI.passwordDialog = passwordDialog;
        return passwordDialog;
    }

    public static MainForm initMainForm() {
        if (Settings.isPasswordsInitCompleted()) {
            GUI.mainForm = new MainForm();
            Logger logger = new Logger();
            logger.setForm(getMainForm());

            logger.log(Level.FINE, "Main form initialized", false);
        } else {
            showMessage("Введите верный пароль", "Ошибка авторизации");
            initPasswordDialog();
        }

        return mainForm;
    }

    public static void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(passwordDialog, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void exit(boolean saveSettings) {
        if(saveSettings){
            try {
                Settings.saveToFile();
                DBUtil.closeSession();
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    public static void takeLaunchKey(String launchkey) {
        CryptoWizard.setLaunchKey(launchkey);
    }

    public static ActionListener getBDayTodayButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnisenderUtil.createAndFillListForBDay(LocalDateTime.now());
            }
        };
    }

    public static ActionListener getBDayInAWeekButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnisenderUtil.createAndFillListForBDayInAWeekForDay(LocalDateTime.now());
            }
        };
    }

    public static ActionListener getBDayInAMonthButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnisenderUtil.createAndFillListForBDayInAMonthForDay(LocalDateTime.now());
            }
        };
    }

    public static ActionListener getAllBDayButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnisenderUtil.createAndFillAllListsForToday();
            }
        };
    }

    public static ActionListener getFullStartButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnisenderUtil.createAndFillAllListsAndSendAllMessagesForToday();
            }
        };
    }
}
