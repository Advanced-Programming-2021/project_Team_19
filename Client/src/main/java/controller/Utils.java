package controller;

import controller.DataBaseControllers.CSVDataBaseController;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Enums.CardFamily;
import model.Enums.GameEvent;
import model.Enums.MessageType;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Matcher getMatcher(String matchingStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(matchingStr);
    }

    public static String getFirstGroupInMatcher(Matcher matcher) {
        matcher.reset();
        matcher.find();
        return matcher.group(1);
    }

    public static <key, value> HashMap<key, value> getHashMap(Object... keysAndValues) {

        HashMap<key, value> hashMap = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            hashMap.put((key) keysAndValues[i], (value) keysAndValues[i + 1]);
        }
        return hashMap;
    }

    public static String getDataInCommandByKey(String command, String key) {
        Matcher matcher = Utils.getMatcher(command, key + " (.+?)(:? --|$)");
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    public static boolean isPasswordWeak(String password) {

        return (password.length() < 5 ||
                !password.matches(".*?\\d.*") ||
                !password.matches(".*?[a-z].*") ||
                !password.matches(".*?[A-Z].*"));
    }

    public static DataForClientFromServer checkFormatValidity(HashMap<String, String> userData) {

        for (String dataKey : userData.keySet()) {
            if (!isFormatValid(userData.get(dataKey))) {

                return new DataForClientFromServer
                        (dataKey + " format is not valid", MessageType.ERROR);
            }
        }

        return null;
    }

    public static boolean isFormatValid(String data) {
        return data.matches("\\w+");
    }

    public static DataForClientFromServer getDataSendToClientForInvalidInput() {
        return new DataForClientFromServer("invalid input", MessageType.ERROR);
    }

    public static DataForClientFromServer getDataSendToClientForOperationFailed() {
        return new DataForClientFromServer("operation failed", MessageType.ERROR);
    }

    public static void shuffle(ArrayList<Card> arrayList) {
        Collections.shuffle(arrayList);
    }


    public static Card getCardByName(String cardName) {

        return CSVDataBaseController.getCardByCardName(cardName);
    }


    public static boolean isInstance(Object object, Class<?> type) {
        return type.isInstance(object);
    }

    public static void printArrayListOfCards(ArrayList<Card> cards) {
        int cnt = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards) {
            stringBuilder.append(cnt).append(".").append(" ").append(card).append("\n");
            cnt++;
        }
        Printer.print(stringBuilder.toString().trim());
    }

    public static ArrayList<String> getCommandsExceptActivation() {

        ArrayList<String> answer = new ArrayList<>();

        answer.add("attack ([1-5])");
        answer.add("attack direct");
        answer.add("summon");
        answer.add("set");
        answer.add("set --position (attack|defence)");
        answer.add("flip summon");
        answer.add("next phase");

        return answer;
    }

    public static String getPascalCase(String string) {
        string = string.split(",")[0];
        string = string.replaceAll("-", " ");
        boolean wasLastSpace = true;
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < string.length(); i++) {
            if (wasLastSpace) {
                stringBuilder.append(String.valueOf(string.charAt(i)).toUpperCase());
                wasLastSpace = false;
            }
            else if (string.charAt(i) == ' ') {
                wasLastSpace = true;
            }
            else {
                stringBuilder.append(String.valueOf(string.charAt(i)).toLowerCase());
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args){
        System.out.println(getPascalCase("Swords of Revealing Light"));
    }

}
