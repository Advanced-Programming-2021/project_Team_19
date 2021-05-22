package view;

import controller.DataBaseControllers.DataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import view.Printer.Printer;

import java.util.Random;


public class Main {

    public static void main(String[] arg) {



//        Printer.print(((Monster)Utils.getCardByName("battle warrior")).getAttack(GameData.getTestGameData()));

        new Game().run(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();

//        LoginMenu.getInstance().run();
    }

}
