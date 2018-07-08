package Congratulator.Util;

import Congratulator.gui.MainForm;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 * Created by User2 on 27.03.2018.
 */
public class Logger {
    private final static java.util.logging.Logger logger;
    private final static FileHandler filehandler;
    private static MainForm form;

    static {
        String logRootPath = "C:\\ProgramData\\Congratulator\\logs\\";
        if (logRootPath == null || logRootPath.equals("")) {
            throw new IllegalArgumentException();
        }

        File logDirectory = new File(logRootPath + System.getProperty("user.name"));
        if (!logDirectory.exists()) logDirectory.mkdirs();

        logger = java.util.logging.Logger.getLogger(Logger.class.getSimpleName());
        FileHandler tempFileHandler = null;

        try {
            tempFileHandler = new FileHandler(logDirectory.getAbsolutePath() + "\\" + logger.getName() + ".log",
                    100 * 300, 10, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            filehandler = tempFileHandler;
        }

        try {
            logger.addHandler(filehandler);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            filehandler.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(Level level, String message, boolean printToForm) {
        message = message.replaceAll(Settings.getAPIkey(),"-API_STRING-");
        logger.log(level, message);
//        System.out.println(message);
        try {
            if (printToForm) form.print(message);
        } catch (Exception e) {
            log(Level.WARNING, "Cannot print to form", false);
        }
    }

    public static void setForm(MainForm form) {
        Logger.form = form;
    }
}
