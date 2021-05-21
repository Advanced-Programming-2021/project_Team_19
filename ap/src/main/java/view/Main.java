package view;

import controller.DataBaseControllers.CardDataBaseController;
import controller.DataBaseControllers.DataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import view.Menu.LoginMenu;
import view.Printer.Printer;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] arg) {

//        Printer.print(Utils.getCardByName("Battle OX"));
//        Printer.print(Utils.getCardByName("Negate Attack"));

        new Game().run(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();

//        LoginMenu.getInstance().run();
    }

}
