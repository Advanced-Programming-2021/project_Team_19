package view;

import controller.DataBaseControllers.DataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import view.Printer.Printer;

import java.util.Random;


public class Main {

    public static void main(String[] arg) {


        Printer.print("");

        new Game().run(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();

//        LoginMenu.getInstance().run();
    }

}
