package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;

import java.util.ArrayList;

public abstract class ActivationChecker {

    protected GameData gameData;
    protected Card card;

    protected ActivationChecker(GameData gameData, Card card) {
        this.gameData = gameData;
        this.card = card;
    }

    public abstract String check();


    public static String multipleCheck(ArrayList<ActivationChecker> checkers) {

        for (ActivationChecker checker : checkers) {
            String checkerResult = checker.check();
            if (checkerResult != null) {
                return checkerResult;
            }
        }
        return null;
    }

}
