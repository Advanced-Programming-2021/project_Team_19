package model.Card.Monsters;

import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import view.Printer.Printer;

import java.util.ArrayList;

public class TerratigerTheEmpoweredWarrior extends Monster {
    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        super.handleSummon(gameData, numberOfSacrifices);
        Hand hand = gameData.getCurrentGamer().getGameBoard().getHand();
        if (getLevel4OrLessMonstersInHand(hand).size() != 0 && !gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            specialSummonLevel4OrLessCard(gameData, getLevel4OrLessMonstersInHand(hand));
        } else {
            Printer.print("your monster zone is full and you cannot special summon a monster");
        }
    }

    private void specialSummonLevel4OrLessCard(GameData gameData, ArrayList<Card> level4OrLessMonstersInHand) {
        Card selectedCard = Utils.askUserToSelectCard(level4OrLessMonstersInHand,
                "choose a monster to special summon:",
                null);

        if (selectedCard == null)
            return;

        new SpecialSummon(gameData).run(selectedCard);
        ((Monster) selectedCard).setCardMod(CardMod.DEFENSIVE_OCCUPIED);

    }

    private ArrayList<Card> getLevel4OrLessMonstersInHand(Hand hand) {
        ArrayList<Card> toReturn = new ArrayList<>();
        for (Card card : hand.getCardsInHand()) {
            if (card.getCardFamily().equals(CardFamily.MONSTER) && ((Monster) card).getLevel() <= 4)
                toReturn.add(card);
        }
        return toReturn;
    }

}
