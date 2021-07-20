package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardMod;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;
import java.util.Collections;

public class UnitedWeStand extends EquipSpell {


    @Override
    public int changeInAttack(GameData gameData) {
        ArrayList<Monster> monstersInMonsterCardZone;
        if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().containsCard(monsterEquippedWithThisSpell)) {
            monstersInMonsterCardZone = gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards();
        } else {
            monstersInMonsterCardZone = gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCards();
        }

        monstersInMonsterCardZone.removeAll(Collections.singleton(null));

        int faceUpMonsters = 0;

        for (Monster monster : monstersInMonsterCardZone) {
            if (!monster.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                faceUpMonsters++;
            }
        }

        return faceUpMonsters * 800;
    }

    public UnitedWeStand(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }
}
