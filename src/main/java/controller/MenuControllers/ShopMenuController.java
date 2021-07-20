package controller.MenuControllers;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.DataBaseControllers.DataBaseController;
import controller.DataBaseControllers.UserDataBaseController;
import controller.DuelControllers.CheatCodes;
import controller.Utils;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.User;

import java.util.regex.Matcher;

public class  ShopMenuController {


    private ShopMenuController() {
    }

    private static ShopMenuController instance = null;

    public static ShopMenuController getInstance() {

        if (instance == null) {
            instance = new ShopMenuController();
        }
        return instance;
    }


    public DataForClientFromServer run(User user, String command) {


        if (command.matches("shop buy (.+)")) {
            return manageBuyCards(user, Utils.getMatcher(command, "shop buy (.+)"));
        } else if (command.matches("shop show --all")) {
            return showAllCards();
        } else if (command.equals("increase --money \\d+")) {
            CheatCodes.increaseMoney(user, Utils.getFirstGroupInMatcher(Utils.getMatcher(command, "increase --money (\\d+)")));
        } else if (command.equals("shop show --money")) {
            return showMoney(user);
        }
        return Utils.getDataSendToClientForInvalidInput();

    }

    private DataForClientFromServer manageBuyCards(User user, Matcher matcher) {

        matcher.find();
        String cardName = matcher.group(1);

        Card card = Utils.getCardByName(cardName);

        if (card == null) {
            return new DataForClientFromServer("there is no card with this name",
                    MessageType.ERROR);
        }

        if (CSVDataBaseController.getCardState(card.getName())) {
            return new DataForClientFromServer("This card is forbidden by the admin", MessageType.ERROR);
        }

        if (CSVDataBaseController.getCardNumber(card.getName()) == 0) {
            return new DataForClientFromServer("There is not enough card available", MessageType.ERROR);
        }

        if (user.getCredit() < card.getPrice()) {
            return new DataForClientFromServer("not enough money", MessageType.ERROR);
        }

        user.addCard(cardName);
        user.decreaseCredit(card.getPrice());
        DataBaseController.rewriteFileOfObjectGson(UserDataBaseController.
                getUserFilePathByUsername(user.getUsername()), user);
        CSVDataBaseController.increaseCardCount(cardName, -1);
        //Remeber to add cardCreating to CSV
        return new DataForClientFromServer("you successfully bought the card",
                MessageType.SUCCESSFUL);

    }

    private DataForClientFromServer showAllCards() {

        return new DataForClientFromServer(CSVDataBaseController.getAllCardPrices(),
                MessageType.Card);
    }

    private DataForClientFromServer showMoney(User user) {
        return new DataForClientFromServer(Integer.toString(user.getCredit()), MessageType.SUCCESSFUL);
    }
}
