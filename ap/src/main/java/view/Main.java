package view;

import controller.DataBaseControllers.CardDataBaseController;
import controller.DataBaseControllers.DataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import org.junit.jupiter.api.Test;
import view.Menu.LoginMenu;

import java.io.FileNotFoundException;


public class Main {

    public static void main(String[] arg) {

        String test;

        new Game().run(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();
//
//        LoginMenu.getInstance().run();
    }

}
