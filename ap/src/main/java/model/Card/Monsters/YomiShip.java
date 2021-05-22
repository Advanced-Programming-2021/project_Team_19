package model.Card.Monsters;
import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Phase;
import view.Printer.Printer;

public class YomiShip extends Monster {



    @Override
    public void handleDestroy(GameData gameData) {
        super.handleDestroy(gameData);
        if (gameData.getCurrentPhase().equals(Phase.BATTLE) && !gameData.getSelectedCard().equals(this) && gameData.getSelectedCard().getCardFamily().equals(CardFamily.MONSTER)) {
            gameData.getSelectedCard().handleDestroy(gameData);
            Printer.print("you destroyed Yomi ship and your card was destroyed");
        }
    }

    public YomiShip(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }
}
