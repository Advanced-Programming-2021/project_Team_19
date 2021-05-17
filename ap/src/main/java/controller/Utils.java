package controller;

import controller.DataBaseControllers.CardDataBaseController;
import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.DataForClientFromServer;
import model.Enums.CardNames;
import model.Enums.MessageType;
import view.GetInput;
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

    public static CardNames getCardEnumByName(String cardNameStr) {

        try {
            return CardNames.valueOf(getStringInSnaleUpperCase(cardNameStr));

        } catch (IllegalArgumentException e) {

//            e.printStackTrace();
            return null;
        }
    }

    public static String getStringInSnaleUpperCase(String string) {

        String tempString = string;

        tempString = tempString.replaceAll(", ", "___");
        tempString = tempString.replaceAll("-", "__");
        tempString = tempString.replaceAll(" ", "_");

        tempString = tempString.toUpperCase();

        return tempString;
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

        return CardDataBaseController.getCardObjectByCardName(Utils.getCardEnumByName(cardName));
    }

    public static boolean isCurrentGamerActionDoer(GameData gameData, Action action) {
        return gameData.getCurrentGamer().equals(action.getActionDoer());
    }

    public static boolean IsSelectedCardIsNull(GameData gameData) {

        if (gameData.getSelectedCard() == null) {
            return false;
        }
        return true;
    }

    public static <obj> Action getLastActionOfSpecifiedAction(ArrayList<Action> actions, Class myClass) {

        for (int i = actions.size() - 1; i >= 0; i--) {

            if (isInstance(actions.get(i), myClass)) {
                return actions.get(i);
            }
        }

        return null;
    }

    public static boolean isInstance(Object object, Class<?> type) {
        return type.isInstance(object);
    }

    public static void printArrayListOfCards(ArrayList<Card> cards) {
        int cnt = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards) {
            stringBuilder.append(cnt).append(".").append(" ").append(card.toString()).append("\n");
            cnt++;
        }
        Printer.print(stringBuilder.toString().trim());
    }

    public static void printArrayListOfMonsters(ArrayList<Monster> monsters) {
        int cnt = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Monster monster : monsters) {
            stringBuilder.append(cnt).append(".").append(" ").append(monster.toString()).append("\n");
            cnt++;
        }
        Printer.print(stringBuilder.toString().trim());
    }

    public static Card askUserToSelectCard(ArrayList<Card> listOfCards, String message) {
        String command;
        while (true){
            Printer.print(message);
            printArrayListOfCards(listOfCards);
            command = GetInput.getString();
            if (command.matches("cancel")) {
                return null;
            }else if (command.matches("\\d+")){
                int id = Integer.parseInt(command);
                if (id >= listOfCards.size()){
                    Printer.print("please enter a valid id:");
                }
                else {
                    return listOfCards.get(id - 1);
                }
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

}
