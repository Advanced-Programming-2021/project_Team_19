package model.Card.Spells;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class EquipSpell extends Spell {

    Monster monsterEquippedWithThisSpell;

    static HashMap<String, ArrayList<MonsterType>> spellToProperties = new HashMap<>();


    static {
        ArrayList<MonsterType> magnumShield = new ArrayList<>();
        ArrayList<MonsterType> swordsOfDarkDestruction = new ArrayList<>();

        magnumShield.add(MonsterType.WARRIOR);
        swordsOfDarkDestruction.add(MonsterType.SPELLCASTER);
        swordsOfDarkDestruction.add(MonsterType.FIEND);

        spellToProperties.put("Magnum Shield", magnumShield);
        spellToProperties.put("United We Stand", new ArrayList<>());
        spellToProperties.put("Black Pendant", new ArrayList<>());
        spellToProperties.put("Sword of dark destruction", swordsOfDarkDestruction);
    }


    public EquipSpell(String name, String description, int price, Type type, SpellTypes spellType, Status status){
        super(name,description,price,type, spellType, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {

        ArrayList<Card> monstersOnBoard =
                new ArrayList<>(gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards());
        monstersOnBoard.removeAll(Collections.singleton(null));

        ArrayList<Card> monstersToEquip = new ArrayList<>();

        if (spellToProperties.get(getName()).isEmpty()) {
            monstersToEquip = monstersOnBoard;
        } else {
            for (Card card : monstersOnBoard) {
                for (MonsterType monsterType : spellToProperties.get(getName())) {
                    if (((Monster) card).getMonsterType().equals(monsterType)) {
                        monstersToEquip.add(card);
                    }
                }
            }
        }

        Card selectedCard = Utils.askUserToSelectCard(monstersToEquip, "select a card from your monster zone to equip with this spell", null);

        if (selectedCard == null) {
            return new ActivationData(null, "you cancelled the spell activation");
        }

        monsterEquippedWithThisSpell = (Monster) selectedCard;
        monsterEquippedWithThisSpell.addEquipSpell(this);

        return new ActivationData(this, "you successfully equipped " + monsterEquippedWithThisSpell.getName() + " with spell " + this.getName());
    }

    @Override
    public boolean canActivate(GameData gameData) {
        ArrayList<Card> monstersOnBoard =
                new ArrayList<>(gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards());
        monstersOnBoard.removeAll(Collections.singleton(null));

        if (monstersOnBoard.isEmpty()) {
            return false;
        }

        if (spellToProperties.get(getName()).isEmpty()) {
            return true;
        }

        for (Card card : monstersOnBoard) {
            for (MonsterType monsterType : spellToProperties.get(getName())) {
                if (((Monster) card).getMonsterType().equals(monsterType)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int changeInAttack(GameData gameData) {
        return 0;
    }

    public int changeInDefence(GameData gameData) {
        return 0;
    }
}
