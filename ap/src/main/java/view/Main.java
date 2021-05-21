package view;

import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;


public class Main {

    public static void main(String[] arg) {


        new Game().run(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();

//        LoginMenu.getInstance().run();
    }

}
