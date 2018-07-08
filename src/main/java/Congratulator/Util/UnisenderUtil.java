package Congratulator.Util;

import Congratulator.Entity.EmailEntity;
import Congratulator.gui.GUI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by User2 on 05.03.2018.
 */
public class UnisenderUtil {
    private static final Settings properties;

    static {
        properties = Settings.getInstance();
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(properties.getDateTimeFormatTemplate());
    private static DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern(properties.getShortDateTimeFormatter());

    private UnisenderUtil() {

    }

    public static int createEmptyList(String name) {
        Logger.log(Level.INFO, "Creating empty contact list", true);

        String request = null;
        request = String.format(properties.getCreateListRequest(), name);

        String response = sendRequest(request, "GET");

        int responseCode = 0;
        try {
            responseCode = (Integer) new JSONObject(response).getJSONObject("result").get("id");
        } catch (Exception e) {
            Logger.log(Level.WARNING, "Cannot parse response " + response + " Additional info: " + e.toString(), true);
        }

        return responseCode;
    }

    public static String loadEmailsToList(int listID, LinkedList<String> emails) {
        Logger.log(Level.INFO, "loading emails to list", true);

        String request = properties.getUpdateListRequest();

        Iterator<String> iterator = emails.iterator();
        int count = 0;

        while (iterator.hasNext()) {
            String email = iterator.next().replaceAll("\\s+", "");
            if (EmailEntity.checkValid(email)) {
                request += String.format("&data[%d][0]=%s&data[%d][1]=%d", count, email, count, listID);
                count++;
            } else Logger.log(Level.INFO, "Invalid email: " + email, true);
        }

        return sendRequest(request, "POST");
    }

    public static int createAndFillListForBDay(LocalDateTime dateTime) {
        Logger.log(Level.INFO, "Creating and filling contact list for BDay " + dateTime.format(shortFormatter), true);

        String listName = properties.getClientListTitlePrefix() + " for " + dateTime.format(shortFormatter)
                + " created " + LocalDateTime.now().format(GUI.isRepeatedListCreationAllowed() ? formatter : shortFormatter);
        try {
            listName = URLEncoder.encode(listName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String request = String.format(properties.getCreateListRequest(), listName);
        String response = sendRequest(request, "GET");

        int listID = 0;
        try {
            listID = (Integer) new JSONObject(response).getJSONObject("result").get("id");
        } catch (JSONException e) {
            String logMessage = "Cannot parse response " + response + " Additional info: " + e.toString();

            if (response.contains("already exists"))
                logMessage = "Email list for " + dateTime.format(shortFormatter) + " already exists";

            Logger.log(Level.WARNING, logMessage, true);
            throw new IllegalArgumentException();
        }

        loadEmailsToList(listID, DBUtil.getEmailsForBDay(dateTime.getDayOfMonth(), dateTime.getMonthValue()));

        return listID;
    }

    public static int createAndFillListForBDayInAWeekForDay(LocalDateTime dateTime) {
        Logger.log(Level.INFO, "Creating and filling contact list for BDay in a week from " + dateTime.format(shortFormatter), true);

        LocalDateTime dateTimeInAWeek = dateTime;
        dateTimeInAWeek = dateTimeInAWeek.plus(1, ChronoUnit.WEEKS);
        return createAndFillListForBDay(dateTimeInAWeek);
    }

    public static int createAndFillListForBDayInAMonthForDay(LocalDateTime dateTime) {
        Logger.log(Level.INFO, "Creating and filling contact list for BDay in a month from " + dateTime.format(shortFormatter), true);

        LocalDateTime dateTimeInAMonth = dateTime;
        dateTimeInAMonth = dateTimeInAMonth.plus(1, ChronoUnit.MONTHS);
        return createAndFillListForBDay(dateTimeInAMonth);
    }

    public static int[] createAndFillAllListsForDay(LocalDateTime dateTime) {
        Logger.log(Level.INFO, "Creating and filling all contact lists for BDay " + dateTime.format(shortFormatter), true);

        int[] result = new int[3];

        result[0] = createAndFillListForBDay(dateTime);
        result[1] = createAndFillListForBDayInAWeekForDay(dateTime);
        result[2] = createAndFillListForBDayInAMonthForDay(dateTime);

        return result;
    }

    public static int[] createAndFillAllListsForToday() {
        Logger.log(Level.INFO, "Creating and filling all contact lists for today", true);
        return createAndFillAllListsForDay(LocalDateTime.now());
    }

    public static String[] createAndFillAllListsAndSendAllMessagesForDay(LocalDateTime dateTime) {
        Logger.log(Level.INFO, "Creating, filling and sending all contact lists for day " + dateTime.format(shortFormatter), true);

        int[] listsIDS = createAndFillAllListsForDay(dateTime);
        int[] messagesIDs = new int[]{
                presetMessage(listsIDS[0], properties.getBdayTodayMessageTemplateID()),
                presetMessage(listsIDS[1], properties.getBdayInAWeekMessageTemplateID()),
                presetMessage(listsIDS[2], properties.getBdayInAMonthMessageTemplateID())};

        String[] responses = new String[3];
        for (int i = 0; i < 3; i++) {
            responses[i] = sendMessage(messagesIDs[i]);
        }

        return responses;
    }

    public static String[] createAndFillAllListsAndSendAllMessagesForToday() {
        Logger.log(Level.INFO, "Creating, filling and sending all contact lists for today", true);
        return createAndFillAllListsAndSendAllMessagesForDay(LocalDateTime.now());
    }

    public static Map<String, String> getTemplateLists() {
        String response = sendRequest(properties.getTemplatesListRequest(), "GET");
        JSONArray array = new JSONObject(response).getJSONArray("result");
        Map<String, String> result = new HashMap<String, String>();

        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;
            result.put((String) jsonObject.get("list_id"), (String) jsonObject.get("title"));
        }

        return result;
    }

    public static void showLists() {
        for (Map.Entry<String, String> pair : getTemplateLists().entrySet())
            System.out.println(pair.getKey() + " : " + pair.getValue());
    }

    public static int presetMessage(int listID, String messageTemplate) {
        Logger.log(Level.INFO, "Preseting message for list " + listID + " by template " + messageTemplate, true);

        String fromEmail = "public@example.ru";
        String fromName = null;

        try {
            fromName = URLEncoder.encode("", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.log(Level.WARNING, "Cannot encode " + fromName + " Additional info: " + e.toString(), true);
        }

        String request = String.format(properties.getPresetMessageRequestForList(listID), fromName, fromEmail, messageTemplate);

        String response = sendRequest(request, "GET");
        int messageID = (Integer) new JSONObject(response).getJSONObject("result").get("message_id");

        return messageID;
    }

    public static String sendMessage(int messageID) {
        Logger.log(Level.INFO, "Sending message " + messageID, true);
        String request = properties.getSendMessageRequestForMessage(messageID);
        return sendRequest(request, "GET");
    }

    public static String sendRequest(String request, String requestType) {
        Logger.log(Level.INFO, "Sending " + requestType + " request " + request.replace(properties.getAPIkey(), "APIKey"), true);

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(request).openConnection();
            con.setRequestMethod(requestType);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Logger.log(Level.INFO, "Response: " + response, true);

            return response.toString();
        } catch (IOException e) {
            Logger.log(Level.SEVERE, "Failed to send request. Additional info: " + e.toString(), true);
        }

        return null;
    }
}
