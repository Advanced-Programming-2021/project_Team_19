package view.Menu;

import controller.ClientDataController;
import controller.DuelControllers.DuelMenuController;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import view.Printer.Printer;
import view.Utils;
import view.graphic.menuGraphic;

import java.util.HashMap;
import java.util.regex.Matcher;

public class Menu extends menuGraphic {

    protected static final HashMap<String, Integer> menuLevels;

    protected final String menuName;

    protected static String username;

    static {

        menuLevels = new HashMap<>();

        menuLevels.put("Login Menu", 0);
        menuLevels.put("Main Menu", 1);
        menuLevels.put("Duel Menu", 2);
        menuLevels.put("Deck Menu", 2);
        menuLevels.put("Scoreboard Menu", 2);
        menuLevels.put("Profile Menu", 2);
        menuLevels.put("Shop Menu", 2);
        menuLevels.put("Import/Export Menu", 2);

    }

    public Menu(String menuName) {
        this.menuName = menuName;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected static DataForClientFromServer sendDataToServer(DataForServerFromClient data) {

        //function of server
        return ClientDataController.handleMessageOfClientAndGetFeedback(data);
    }

    protected void sendCommandToServer1(Matcher matcher) {
        matcher.reset();
        matcher.find();


        DataForClientFromServer data = sendDataToServer
                (new DataForServerFromClient(matcher.group(0), username, menuName));

        Printer.print(data.getMessage());
    }

    protected void sendCommandToServer2(Matcher matcher) {
        matcher.reset();
        matcher.find();

        String tempName = matcher.group(1);


        if (view.Utils.isFormatInvalid(tempName)) {
            Printer.print("deck name format is invalid");
            return;
        }

        DataForClientFromServer data = sendDataToServer
                (new DataForServerFromClient(matcher.group(0), username, menuName));

        Printer.print(data.getMessage());
    }

}
