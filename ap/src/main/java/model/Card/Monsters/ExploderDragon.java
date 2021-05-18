package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardMod;
import view.Printer.Printer;

public class ExploderDragon extends Monster {


    @Override
    public boolean attackIsNormal(GameData gameData) {
        Monster attackingMonster = (Monster) gameData.getSelectedCard();
        if (getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED) && attackingMonster.getAttack(gameData) < getAttack(gameData)) {
            return true;
        }
        if (!getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED) && attackingMonster.getAttack(gameData) < getDefence(gameData)) {
            return true;
        }
        attackingMonster.handleDestroy(gameData);
        this.handleDestroy(gameData);
        Printer.print("you attacked exploder dragon and both cards were destroyed");
        return false;
    }

    @Override
    public void attackOffensiveMonster(Monster defendingMonster, GameData gameData) {
        if (getAttack(gameData) < defendingMonster.getAttack(gameData)) {
            defendingMonster.handleDestroy(gameData);
            this.handleDestroy(gameData);
            Printer.print("both cards were destroyed and no one received any damage");
            return;
        }
        super.attackOffensiveMonster(defendingMonster, gameData);
    }
}
