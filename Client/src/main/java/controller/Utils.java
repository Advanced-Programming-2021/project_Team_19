package controller;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.DuelControllers.Actions.Action;
import controller.DuelControllers.Actions.Select;
import controller.DuelControllers.GameData;
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

    public static boolean isCareOwnerActionDoer(GameData gameData, Action action, Card card) {
        return gameData.getCardController(card).equals(action.getActionDoer());
    }

    public static boolean IsSelectedCardNull(GameData gameData) {

        return gameData.getSelectedCard() == null;
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
            stringBuilder.append(cnt).append(".").append(" ").append(card).append("\n");
            cnt++;
        }
        Printer.print(stringBuilder.toString().trim());
    }

    public static Card askUserToSelectCard(ArrayList<Card> listOfCards, String message, CardFamily cardFamily) {
        String command;
        while (true) {
            Printer.print(message);
            printArrayListOfCards(listOfCards);
            command = GetInput.getString();

            if (command.matches("cancel")) {
                return null;
            } else if (command.matches("\\d+")) {
                int id = Integer.parseInt(command);
                if (id > listOfCards.size() || id < 1) {
                    Printer.print("please enter a valid id:");
                } else {
                    Card returnedCard = listOfCards.get(id - 1);

                    if (cardFamily == null) {
                        return returnedCard;
                    } else {
                        if (returnedCard.getCardFamily().equals(cardFamily)) {
                            return returnedCard;
                        } else {
                            Printer.print("pleas enter " + cardFamily.toString().toLowerCase() + " id");
                        }
                    }

                }
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

    public static boolean askForConfirmation(String message) {
        Printer.print(message);
        Printer.print("""
                1- yes
                2- no""");
        while (true) {
            String command = GetInput.getString();
            switch (command) {
                case "1":
                    return true;
                case "2":
                    return false;
                default:
                    Printer.printInvalidCommand();
            }
        }
    }


    public static boolean askForActivate(String event) {

        GameData.getGameData().setEvent(GameEvent.ASK_FOR_ACTIVATE_TRAP);

        if (askForConfirmation(event + "\ndo you want to activate your trap and spell?")) {
            Printer.print("So please do that :)");
            return true;
        }

        GameData.getGameData().setEvent(null);
        return false;

    }

    public static void changeTurn(GameData gameData) {

        gameData.changeTurn();
        Printer.print("now it will be " + gameData.getCurrentGamer().getUsername() + "’s turn");
    }

    public static boolean handleSelect(GameData gameData, String command) {

        if (command.startsWith("select")) {
            new Select(gameData).select(command);
        } else if (command.matches("card show --selected")) {
            new Select(gameData).select(command);
        } else if (command.equals("show board")) {
            gameData.showBoard();
        } else {
            return false;
        }
        return true;
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
