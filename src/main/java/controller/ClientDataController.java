package controller;


import AnythingIWant.GameGraphicController;
import AnythingIWant.LobbyHandler;
import AnythingIWant.Network;
import controller.DataBaseControllers.UserDataBaseController;
import controller.MenuControllers.*;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.User;

import java.util.ArrayList;


public class ClientDataController {

    private final static ArrayList<String> menusNeedUserForRequest = new ArrayList<>();

    static {
        menusNeedUserForRequest.add("Profile Menu");
        menusNeedUserForRequest.add("Deck Menu");
        menusNeedUserForRequest.add("Shop Menu");
    }

    public static DataForClientFromServer handleMessageOfClientAndGetFeedback
            (DataForServerFromClient data) {

        String menuName = data.getMenuName();

        User user = Network.getUserByToken(data.getToken());

        if (user == null) {
            if (menusNeedUserForRequest.contains(data.getMenuName())) {
                return Utils.getDataSendToClientForInvalidInput();
            }
        }

        if (menuName.matches("Login Menu")) {
            return LoginMenuController.getInstance().run(data.getMessage(), data.token);
        } else if (menuName.matches("Profile Menu")) {
            return ProfileMenuController.getInstance().run(user, data.getMessage());
        } else if (menuName.matches("Deck Menu")) {
            return DeckMenuController.getInstance().run(user, data.getMessage());
        } else if (menuName.matches("Scoreboard Menu")) {
            return ScoreBoardMenuController.getInstance().run(data.getMessage());
        } else if (menuName.matches("Shop Menu")) {
            return ShopMenuController.getInstance().run(user, data.getMessage());
        } else if (menuName.matches("Import/Export Menu")) {
            return ImportAndExportMenuController.getInstance().run(data.getMessage());
        } else if (menuName.matches("Lobby Menu")) {
            return LobbyHandler.getInstance().run(user, data.getMessage());
        } else if (menuName.matches("duel")){
            return GameGraphicController.run(user, data.getMessage());
        }

        return Utils.getDataSendToClientForInvalidInput();

    }

}
