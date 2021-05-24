package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Data.ActivationData;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;
import model.Gamer;

import java.util.HashMap;

public class ClosedForest extends FieldSpell {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    {
        typesAndAmountToChangeAttackAndDefence = new HashMap<>();
        Integer[] numbers = {0, 0};
        typesAndAmountToChangeAttackAndDefence.put(MonsterType.BEAST, numbers);

    }

    @Override
    public int attackDifference(MonsterType monsterType, GameData gameData) {
        Gamer gamer = gameData.getCurrentGamer();
        if (gameData.getSecondGamer().getGameBoard().getFieldZone().getId(this) != -1) {
            gamer = gameData.getSecondGamer();
        }

        if (!typesAndAmountToChangeAttackAndDefence.containsKey(monsterType) ||
                !gamer.getGameBoard().getMonsterCardZone().containsCard(gameData.getSelectedCard()))
            return 0;

        return monstersInGraveyard(gamer) * 100;
    }

    private int monstersInGraveyard(Gamer gamer) {
        int toReturn = 0;
        for (Card card : gamer.getGameBoard().getGraveYard().getCardsInGraveYard()) {
            if (card.getCardFamily().equals(CardFamily.MONSTER)) {
                toReturn++;
            }
        }
        return toReturn;
    }
    public ClosedForest(String name, String description, int price, Type type, SpellTypes spellType, Status status){
        super(name,description,price,type, spellType, status);
    }
}
