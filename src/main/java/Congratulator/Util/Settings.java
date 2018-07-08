package Congratulator.Util;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by User2 on 19.03.2018.
 */
public class Settings extends Properties {
    private static Settings instance;
    private static String propertiesFilePath;
    private static String APIkey;
    private static String dbLogin;
    private static String dbPass;
    private static boolean passwordsInitCompleted = false;

    static {
        initPasswordsFromLaunchKey();
    }

    private String dbURL;
    private String templatesListRequest;
    private String createListRequest;
    private String updateListRequest;
    private String presetMessageRequest;
    private String sendMessageRequest;
    private String bdayInAMonthMessageTemplateID, bdayInAWeekMessageTemplateID, bdayTodayMessageTemplateID;
    private String clientListTitlePrefix;
    private String dateTimeFormatTemplate;
    private String shortDateTimeFormatter = "dd.MM.yy";

    private Settings() {
    }

    public static Settings getInstance() {
        if (instance == null) loadFromFile();
        return instance;
    }

    public static void loadFromFile() {
        File propertiesFile = new File(propertiesFilePath);

        Settings properties = new Settings();
        InputStream input = null;

        try {
            input = new FileInputStream(propertiesFile);

            properties.load(input);

            properties.dbURL = (String) properties.get("dbURL");
            properties.templatesListRequest = (String) properties.get("templatesListRequest");
            properties.createListRequest = (String) properties.get("createListRequest");
            properties.updateListRequest = (String) properties.get("updateListRequest");
            properties.presetMessageRequest = (String) properties.get("presetMessageRequest");
            properties.sendMessageRequest = (String) properties.get("sendMessageRequest");
            properties.bdayInAMonthMessageTemplateID = (String) properties.get("bdayInAMonthMessageTemplateID");
            properties.bdayInAWeekMessageTemplateID = (String) properties.get("bdayInAWeekMessageTemplateID");
            properties.bdayTodayMessageTemplateID = (String) properties.get("bdayTodayMessageTemplateID");
            properties.clientListTitlePrefix = (String) properties.get("clientListTitlePrefix");
            properties.dateTimeFormatTemplate = (String) properties.get("dateTimeFormatTemplate");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            instance = properties;
            checkAndRestore();

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveToFile() throws Exception {
        checkAndRestore();

        File propertiesFile = new File(propertiesFilePath);
        OutputStream output = null;

        try {
            output = new FileOutputStream(propertiesFile);

            instance.put("dbURL", instance.dbURL);
            instance.put("templatesListRequest", instance.templatesListRequest);
            instance.put("createListRequest", instance.createListRequest);
            instance.put("updateListRequest", instance.updateListRequest);
            instance.put("presetMessageRequest", instance.presetMessageRequest);
            instance.put("sendMessageRequest", instance.sendMessageRequest);
            instance.put("bdayInAMonthMessageTemplateID", instance.bdayInAMonthMessageTemplateID);
            instance.put("bdayInAWeekMessageTemplateID", instance.bdayInAWeekMessageTemplateID);
            instance.put("bdayTodayMessageTemplateID", instance.bdayTodayMessageTemplateID);
            instance.put("clientListTitlePrefix", instance.clientListTitlePrefix);
            instance.put("dateTimeFormatTemplate", instance.dateTimeFormatTemplate);

            instance.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void checkAndRestore() {

    }

    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    public static String getAPIkey() {
        return APIkey;
    }

    public static void setAPIkey(String APIkey) {
        Settings.APIkey = APIkey;
    }

    public static String getDbLogin() {
        return dbLogin;
    }

    public static void setDbLogin(String dbLogin) {
        Settings.dbLogin = dbLogin;
    }

    public static String getDbPass() {
        return dbPass;
    }

    public static void setDbPass(String dbPass) {
        Settings.dbPass = dbPass;
    }

    public static boolean isPasswordsInitCompleted() {
        return passwordsInitCompleted;
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getTemplatesListRequest() {
        return templatesListRequest + APIkey;
    }

    public void setTemplatesListRequest(String templatesListRequest) {
        this.templatesListRequest = templatesListRequest;
    }

    public String getCreateListRequest() {
        return String.format(createListRequest, APIkey, "%s");
    }

    public void setCreateListRequest(String createListRequest) {
        this.createListRequest = createListRequest;
    }

    public String getUpdateListRequest() {
        return String.format(updateListRequest, APIkey);
    }

    public void setUpdateListRequest(String updateListRequest) {
        this.updateListRequest = updateListRequest;
    }

    public String getPresetMessageRequestForList(int listID) {
        return String.format(presetMessageRequest, APIkey, "%s", "%s", listID, "%s");
    }

    public void setPresetMessageRequest(String presetMessageRequest) {
        this.presetMessageRequest = presetMessageRequest;
    }

    public String getSendMessageRequestForMessage(int messageID) {
        return String.format(sendMessageRequest, APIkey, messageID);
    }

    public void setSendMessageRequest(String sendMessageRequest) {
        this.sendMessageRequest = sendMessageRequest;
    }

    public String getBdayInAMonthMessageTemplateID() {
        return bdayInAMonthMessageTemplateID;
    }

    public void setBdayInAMonthMessageTemplateID(String bdayInAMonthMessageTemplateID) {
        this.bdayInAMonthMessageTemplateID = bdayInAMonthMessageTemplateID;
    }

    public String getBdayInAWeekMessageTemplateID() {
        return bdayInAWeekMessageTemplateID;
    }

    public void setBdayInAWeekMessageTemplateID(String bdayInAWeekMessageTemplateID) {
        this.bdayInAWeekMessageTemplateID = bdayInAWeekMessageTemplateID;
    }

    public String getBdayTodayMessageTemplateID() {
        return bdayTodayMessageTemplateID;
    }

    public void setBdayTodayMessageTemplateID(String bdayTodayMessageTemplateID) {
        this.bdayTodayMessageTemplateID = bdayTodayMessageTemplateID;
    }

    public String getClientListTitlePrefix() {
        return clientListTitlePrefix;
    }

    public void setClientListTitlePrefix(String clientListTitlePrefix) {
        this.clientListTitlePrefix = clientListTitlePrefix;
    }

    public String getDateTimeFormatTemplate() {
        return dateTimeFormatTemplate;
    }

    public void setDateTimeFormatTemplate(String dateTimeFormatTemplate) {
        this.dateTimeFormatTemplate = dateTimeFormatTemplate;
    }

    public String getShortDateTimeFormatter() {
        return shortDateTimeFormatter;
    }

    public void setShortDateTimeFormatter(String shortDateTimeFormatter) {
        this.shortDateTimeFormatter = shortDateTimeFormatter;
    }

    public static void initPasswordsFromLaunchKey() {
        try {
            String launchKey = CryptoWizard.getLaunchKey();

            setAPIkey(launchKey.split("___")[0]);
            setDbLogin(launchKey.split("___")[1]);
            setDbPass(launchKey.split("___")[2]);

            Logger.log(Level.INFO, "Passwords initialized", false);

            passwordsInitCompleted = true;
        } catch (Exception e) {
            Logger.log(Level.INFO, "Launch key error", false);
        }
    }
}
