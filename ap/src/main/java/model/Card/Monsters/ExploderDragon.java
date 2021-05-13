package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardMod;
import view.Printer.Printer;

public class ExploderDragon extends Monster {


    @Override
    public boolean attackIsNormal(GameData gameData) {
        Monster AttackingMonster = (Monster) gameData.getSelectedCard();
        if (getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED) && AttackingMonster.getAttack() < getAttack()) {
            return true;
        }
        new Destroy(gameData).run(AttackingMonster);
        new Destroy(gameData).run(this);
        Printer.print("you attacked exploder dragon and both cards were destroyed");
        return false;
    }

    @Override
    protected void attackOffensiveMonster(Monster defendingMonster, GameData gameData) {
        if (getAttack() < defendingMonster.getAttack()) {
            new Destroy(gameData).run(defendingMonster);
            new Destroy(gameData).run(this);
            Printer.print("both cards were destroyed and no one received any damage");
            return;
        }
        super.attackOffensiveMonster(defendingMonster, gameData);
    }
}
