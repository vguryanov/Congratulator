package Congratulator.gui;

import Congratulator.Util.Logger;
import Congratulator.Util.Settings;
import Congratulator.Util.UnisenderUtil;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

/**
 * Created by User2 on 21.03.2018.
 */
public class MainForm extends JFrame {
    private JPanel panel1;
    private JTextArea textArea1;
    private JButton createListsButton;
    private JButton startButton;
    private JCheckBox allowRepeatedListCreation;
    private JTextField dateField;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Settings.getInstance().getDateTimeFormatTemplate());
    private static DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    private LocalDate dateTime = LocalDate.now();

    public MainForm() {
//        setSystemLookAndFeel();
        setTitle("Congratulator");
        setContentPane(panel1);

        dateField.setText(dateTime.format(shortFormatter));

//        bdayTodayButton.setFocusPainted(false);

        setVisible(true);
        setSize(440, 400);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        startButton.addActionListener(GUI.getFullStartButtonListener());
        createListsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate inputDate = null;

                try {
                    inputDate = LocalDate.parse(dateField.getText(), shortFormatter);
                } catch (Exception e1) {
                    Logger.log(Level.WARNING, "Date parse error. Additional info: " + e1.toString(), false);
                    GUI.showMessage("Введиту дату в верном формате", "Ошибка ввода");
                }

                if (inputDate != null) UnisenderUtil.createAndFillAllListsForDay(inputDate.atStartOfDay());
            }
        });

//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GUI.exit(true);
            }
        });

        DefaultCaret caret = (DefaultCaret) textArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public void print(String message) {
        textArea1.append(LocalDateTime.now().format(formatter) + ": " + message + "\n");
    }

    public boolean isRepeatedListCreationAllowed() {
        return allowRepeatedListCreation.isSelected();
    }
}
