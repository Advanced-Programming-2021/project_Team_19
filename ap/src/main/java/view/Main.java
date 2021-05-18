package view;

import controller.DataBaseControllers.CardDataBaseController;
import controller.DataBaseControllers.DataBaseController;
import controller.DataBaseControllers.UserDataBaseController;
import controller.DuelControllers.DeckModifierBetweenGames;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.User;
import org.junit.jupiter.api.Test;
import view.Menu.LoginMenu;
import view.Printer.Printer;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] arg) {

        new DeckModifierBetweenGames(UserDataBaseController.getUserByUsername("Taha")).run();

//        Printer.print(Utils.getCardByName("battle warrior").status);

//        new Game().run(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();
//
//        LoginMenu.getInstance().run();
    }

}
