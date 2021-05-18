package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardMod;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class ManEaterBug extends Monster {

    public boolean handleFlip(GameData gameData) {

        setCardMod(CardMod.OFFENSIVE_OCCUPIED);

        if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getNumberOfCards() == 0) {
            return false;
        }
        ArrayList<Card> cards = new ArrayList<>(gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCards());

        Card monsterToDestroy = Utils.askUserToSelectCard(cards, "select an enemy id to destroy:");

        if (monsterToDestroy == null)
            return false;

        monsterToDestroy.handleDestroy(gameData);

        return true;
    }

}
