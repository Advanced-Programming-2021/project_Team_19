package view.Menu;

import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import view.GetInput;
import view.Printer.Printer;
import view.Utils;

import java.util.regex.Matcher;

public class DeckMenu extends Menu {

    boolean commandIsDone = false;

    private static DeckMenu instance = null;

    private DeckMenu() {
        super("Deck Menu");
    }

    public static DeckMenu getInstance() {
        if (instance == null)
            return new DeckMenu();
        return instance;
    }

    public void run(String username) {
        Menu.username = username;

        String command = GetInput.getString();

        while (!command.equals("menu exit")) {

            commandIsDone = false;

            createDeck(Utils.getMatcher(command, "deck create (\\S+)"));

            deleteDeck(Utils.getMatcher(command, "deck delete (\\S+)"));

            setActiveDeck(Utils.getMatcher(command, "deck set-active (\\S+)"));

            addCardToDeck(false, Utils.getMatcher(command,
                    "deck add-card" +
                            "(?=.*?--side)(?=.*?--card .+?)(?=.*--deck \\S+)" +
                            "( --((card .+?)|(deck \\S+)|(side))){3}"), true);

            addCardToDeck(true, Utils.getMatcher(command, "deck add-card" +
                    "(?=.*?-s)(?=.*?-c .+?)(?=.*-d \\S+)" +
                    "( -((c .+?)|(d \\S+)|(s))){3}"), true);

            addCardToDeck(false, Utils.getMatcher(command,
                    "deck add-card" +
                            "(?=.*?--card .+?)(?=.*--deck \\S+)" +
                            "( --((card .+?)|(deck \\S+))){2}"), false);

            addCardToDeck(true, Utils.getMatcher(command,
                    "deck add-card" +
                            "(?=.*?-c .+?)(?=.*-d \\S+)" +
                            "( -((c .+?)|(d \\S+))){2}"), false);


            deleteCardFromDeck(false, Utils.getMatcher(command,
                    "deck rm-card" +
                            "(?=.*?--side)(?=.*?--card .+?)(?=.*--deck \\S+)" +
                            "( --((card .+?)|(deck \\S+)|(side))){3}"), true);

            deleteCardFromDeck(true, Utils.getMatcher(command,
                    "deck rm-card" +
                            "(?=.*?-s)(?=.*?-c .+?)(?=.*-d \\S+)" +
                            "( -((c .+?)|(d \\S+)|(s))){3}"), true);

            deleteCardFromDeck(false, Utils.getMatcher(command, "deck rm-card" +
                    "(?=.*?--card .+?)(?=.*--deck \\S+)" +
                    "( --((card .+?)|(deck \\S+))){2}"), false);

            deleteCardFromDeck(true, Utils.getMatcher(command, "deck rm-card" +
                    "(?=.*?-c .+?)(?=.*-d \\S+)" +
                    "( -((c .+?)|(d \\S+))){2}"), false);

            showUserDecks(Utils.getMatcher(command, "deck show --all"));

            showUserDecks(Utils.getMatcher(command, "deck show -a"));

            showSingleDeck(false, Utils.getMatcher(command, "deck show(?=.*?--deck-name \\S+)(?=.*--side)" +
                            "(( --deck-name (\\S+))|( --side)){2}"),
                    true);

            showSingleDeck(true, Utils.getMatcher(command, "deck show(?=.*?-dn \\S+)(?=.*-s)" +
                            "(( --dn (\\S+))|( -s)){2}"),
                    true);

            showSingleDeck(false, Utils.getMatcher(command, "deck show --deck-name (\\S+)"),
                    false);

            showSingleDeck(true, Utils.getMatcher(command, "deck show -dn (\\S+)"), false);


            showAllCards(Utils.getMatcher(command, "deck show --cards"));

            showAllCards(Utils.getMatcher(command, "deck show -c"));


            help(command);

            if (!commandIsDone) {
                Printer.printInvalidCommand();
            }

            command = GetInput.getString();
        }

    }

    private void createDeck(Matcher matcher) {
        if (matcher.matches()) {
            commandIsDone = true;

            sendCommandToServer2(matcher);
        }

    }

    private void deleteDeck(Matcher matcher) {
        if (matcher.matches()) {


            commandIsDone = true;//

            sendCommandToServer2(matcher);

        }

    }

    private void setActiveDeck(Matcher matcher) {

        if (matcher.matches()) {

            commandIsDone = true;


            sendCommandToServer2(matcher);
        }

    }

    private void addCardToDeck(boolean isAbbreviated, Matcher matcher, boolean isSideDeck) {


        if (matcher.matches()) {

            commandIsDone = true;

            String command = matcher.group(0);

            Matcher matcher1 = Utils.getMatcher(command, "deck add-card (.+)");

            String[] cardAndDeckName = getDeckAndCardNameFromCommand(matcher1, isAbbreviated);
            String cardName = cardAndDeckName[0];
            String deckName = cardAndDeckName[1];

            String commandToSendToServer = "deck add-card --card " + cardName + " --deck " + deckName;
            if (isSideDeck) {
                commandToSendToServer = commandToSendToServer + " --side";
            }

            DataForClientFromServer data = sendDataToServer
                    (new DataForServerFromClient(commandToSendToServer, username, menuName));
            Printer.print(data.getMessage());
        }
    }


    private void deleteCardFromDeck(boolean isAbbreviated, Matcher matcher, boolean isSideDeck) {

        if (matcher.matches()) {


            commandIsDone = true;

            String command = matcher.group(0);

            Matcher matcher1 = Utils.getMatcher(command, "deck rm-card (.+)");

            String[] cardAndDeckName = getDeckAndCardNameFromCommand(matcher1, isAbbreviated);
            String cardName = cardAndDeckName[0];
            String deckName = cardAndDeckName[1];


            if (!Utils.checkFormatValidity(null, Utils.getHashMap(
                    "deckName", deckName))) {
                return;
            }

            String commandToSendToServer = "deck rm-card --card " + cardName + " --deck " + deckName;

            if (isSideDeck) {
                commandToSendToServer = commandToSendToServer + " --side";
            }

            DataForClientFromServer data = sendDataToServer
                    (new DataForServerFromClient(commandToSendToServer, username, menuName));

            Printer.print(data.getMessage());
        }
    }

    private String[] getDeckAndCardNameFromCommand(Matcher matcher, boolean isAbbreviated) {
        matcher.find();

        String cardName;
        String deckName;
        if (isAbbreviated) {
            cardName = Utils.getDataInCommandByKey(matcher.group(1), "-c");
            deckName = Utils.getDataInCommandByKey(matcher.group(1), "-d");
        } else {
            cardName = Utils.getDataInCommandByKey(matcher.group(1), "--card");
            deckName = Utils.getDataInCommandByKey(matcher.group(1), "--deck");
        }

        return new String[]{cardName, deckName};
    }

    private void showUserDecks(Matcher matcher) {

        if (matcher.matches()) {

            commandIsDone = true;

            sendCommandToServer1(Utils.getMatcher("deck show --all", "deck show --all"));
        }

    }


    private void showSingleDeck(boolean isAbbreviated, Matcher matcher, boolean isDeckSideDeck) {

        if (matcher.matches()) {


            commandIsDone = true;

            Matcher matcher1 = Utils.getMatcher(matcher.group(0),
                    "deck show (.*)");

            matcher1.find();
            String name;
            if (isAbbreviated) {
                name = Utils.getDataInCommandByKey(matcher1.group(1), "-d-n");
            } else {
                name = Utils.getDataInCommandByKey(matcher1.group(1), "--deck-name");
            }


            if (Utils.isFormatInvalid(name)) {
                Printer.print("name format is invalid");
                return;
            }

            String commandToSendToServer = "deck show --deck-name " + name;

            if (isDeckSideDeck) {
                commandToSendToServer = commandToSendToServer + " --side";
            }

            DataForClientFromServer data = sendDataToServer
                    (new DataForServerFromClient(commandToSendToServer, username, menuName));

            Printer.print(data.getMessage());
        }

    }

    private void showAllCards(Matcher matcher) {

        if (!matcher.matches()) {
            return;
        }

        commandIsDone = true;

        sendCommandToServer1(Utils.getMatcher("deck show --cards", "deck show --cards"));

    }


    private void help(String command) {
        if (command.equals("help")) {
            commandIsDone = true;
            System.out.println("""
                    deck create <deck name>
                    deck delete <deck name>
                    deck set-active <deck name>
                    deck add-card --card <card name> --deck <deck name> --side(optional)
                    deck add-card -c <card name> -d <deck name> -s(optional)
                    deck rm-card --card <card name> --deck <deck name> --side(optional)
                    deck add-card -c <card name> -d <deck name> -s(optional)
                    deck show --all
                    deck show -a
                    deck show --deck-name <deck name> --side(optional)
                    deck show -dn <deck name> -s(optional)
                    deck show --cards
                    deck show -c
                    help
                    menu show-current
                    menu [menu name]
                    menu exit""");
        }
    }


}
