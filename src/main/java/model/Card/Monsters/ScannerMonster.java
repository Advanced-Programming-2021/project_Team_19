package model.Card.Monsters;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;

import java.util.ArrayList;

import static view.Printer.Printer.print;

public class ScannerMonster extends EffectMonster {

    private Monster scannedMonster;

    public ScannerMonster(String name, String description, int price, int attack,
                          int defence, int level, Attribute attribute, MonsterType monsterType,
                          MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
    }

    public void setScannedMonster(Monster monster, GameData gameData) {
        this.scannedMonster = monster;
    }

    public ArrayList<Card> getMonstersForScan(GameData gameData) {

        ArrayList<Card> monsters = new ArrayList<>();

        for (Card card : gameData.getOtherGamer(gameData.getCardController(this))
                .getGameBoard().getGraveYard().getCardsInGraveYard()) {
            if (card instanceof Monster) {
                monsters.add(card);
            }
        }

        return monsters;
    }

    public boolean scanMonster(GameData gameData) {

        ArrayList<Card> monsters = new ArrayList<>();

        monsters = getMonstersForScan(gameData);

        if (monsters.size() == 0) {
            if (scannedMonster == null) {
                print("There is no monster card in opponent graveYard!");
            }
            return false;
        } else {
            Monster monster = getChosenMonster(gameData,
                    "Please choose a card From GraveYard!(Just a digit telling the position)");
            setScannedMonster(monster, gameData);
            return true;
        }
    }

    public Monster getChosenMonster(GameData gameData, String message) {

        ArrayList<Card> monsters = getMonstersForScan(gameData);

        Monster monster = (Monster) Utils.askUserToSelectCard(monsters
                , message,
                null);

        if (monster == null) {
            return getChosenMonster(gameData, "you should chose one monster");
        } else {
            return getSuitableMonster(monster);
        }
    }

    private Monster getSuitableMonster(Monster monster) {
        if(monster.getCardMod() == null){
            if(getCardMod() == null){
                monster.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
            } else{
                monster.setCardMod(getCardMod());
            }
        }
        monster.removeEquipSpellsAndCallOfTheHaunted();
        return monster;
    }

    public void resetMonster(Monster monster) {
        monster = null;
    }

    public boolean isScannedMonsterSet() {
        return scannedMonster != null;
    }

    public Card getScannedMonster() {
        return scannedMonster;
    }

    @Override
    public int getAttack(GameData gameData) {
        if (scannedMonster == null) {
            return super.getAttack(gameData);
        } else {
            return scannedMonster.getAttack(gameData);
        }
    }

    @Override
    public MonsterTypesForEffects getEffectType() {
        if (scannedMonster == null) {
            return super.getEffectType();
        } else {
            return scannedMonster.getEffectType();
        }
    }

    @Override
    public void setAttack(int attack) {
        if (scannedMonster == null) {
            super.setAttack(attack);
        } else {
            scannedMonster.setAttack(attack);
        }
    }

    @Override
    public int getDefence(GameData gameData) {
        if (scannedMonster == null) {
            return super.getDefence(gameData);
        } else {
            return scannedMonster.getDefence(gameData);
        }
    }

    @Override
    public void setDefence(int defence) {
        if (scannedMonster == null) {
            super.setDefence(defence);
        } else {
            scannedMonster.setDefence(defence);
        }
    }

    @Override
    public int getLevel() {
        if (scannedMonster == null) {
            return super.getLevel();
        } else {
            return scannedMonster.getLevel();
        }
    }

    @Override
    public void setLevel(int level) {
        if (scannedMonster == null) {
            super.setLevel(level);
        } else {
            scannedMonster.setLevel(level);
        }
    }

    @Override
    public Attribute getAttribute() {
        if (scannedMonster == null) {
            return super.getAttribute();
        } else {
            return scannedMonster.getAttribute();
        }
    }

    @Override
    public void setAttribute(Attribute attribute) {
        if (scannedMonster == null) {
            super.setAttribute(attribute);
        } else {
            scannedMonster.setAttribute(attribute);
        }
    }

    @Override
    public CardMod getCardMod() {
        if (scannedMonster == null) {
            return super.getCardMod();
        } else {
            return scannedMonster.getCardMod();
        }
    }

    @Override
    public void setCardMod(CardMod cardMod) {
        if (scannedMonster == null) {
            super.setCardMod(cardMod);
        } else {
            scannedMonster.setCardMod(cardMod);
        }
    }

    @Override
    public boolean handleFlip(GameData gameData, CardMod cardMod) {
        if (scannedMonster == null) {
            return super.handleFlip(gameData, cardMod);
        } else {
            return scannedMonster.handleFlip(gameData, cardMod);
        }
    }

    @Override
    public String handleAttack(GameData gameData, int enemyId) {
        if (scannedMonster == null) {
            return super.handleAttack(gameData, enemyId);
        } else {
            return scannedMonster.handleAttack(gameData, enemyId);
        }

    }

    @Override
    public String attackDefensiveHiddenMonster(Monster defendingMonster, GameData gameData) {
        if (scannedMonster == null) {
            return super.attackDefensiveHiddenMonster(defendingMonster, gameData);
        } else {
            return scannedMonster.attackDefensiveHiddenMonster(defendingMonster, gameData);
        }
    }

    @Override
    public String attackDefensiveMonster(Monster defendingMonster, GameData gameData) {
        if (scannedMonster == null) {
            return super.attackDefensiveMonster(defendingMonster, gameData);
        } else {
            return scannedMonster.attackDefensiveMonster(defendingMonster, gameData);
        }
    }

    @Override
    public String attackOffensiveMonster(Monster defendingMonster, GameData gameData) {
        if (scannedMonster == null) {
            return super.attackOffensiveMonster(defendingMonster, gameData);
        } else {
            return scannedMonster.attackOffensiveMonster(defendingMonster, gameData);
        }

    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {

        if(getMonstersForScan(gameData).size() == 0){
            print("there is no monster in your opponent graveyard to scan");
            return;
        }

        super.handleSummon(gameData, numberOfSacrifices);

        gameData.getCurrentGamer().addEffectLabel
                (new EffectLabel(gameData, gameData.getCurrentGamer(), this));
    }

    @Override
    public TriggerActivationData runEffect(EffectLabel label) {
        label.gamer.removeLabel(label);
        scanMonster(label.gameData);
        return new TriggerActivationData(false, "card has been scanned", this);
    }

    @Override
    public boolean shouldEffectRun(EffectLabel label) {
        return true;
    }

    @Override
    public void handleSet(GameData gameData) {
        if (scanMonster(gameData)) {
            super.handleSet(gameData);
        }
    }

    @Override
    public void handleChangePosition(GameData gameData, CardMod newCardMod) {
        if (scannedMonster == null) {
            super.handleChangePosition(gameData, newCardMod);
        } else {
            scannedMonster.handleChangePosition(gameData, newCardMod);
        }
    }

    @Override
    public String handleDirectAttack(GameData gameData) {
        if (scannedMonster == null) {
            return super.handleDirectAttack(gameData);
        } else {
            return scannedMonster.handleDirectAttack(gameData);
        }
    }


    @Override
    public boolean attackIsNormal(GameData gameData) {
        if (scannedMonster == null) {
            return super.attackIsNormal(gameData);
        } else {
            return scannedMonster.attackIsNormal(gameData);
        }
    }

    @Override
    public void handleDestroy(GameData gameData) {
        if (scannedMonster == null) {
            super.handleDestroy(gameData);
        } else {
            scannedMonster.handleDestroy(gameData);
        }
    }

    @Override
    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed, GameData gameData) {
        if (scannedMonster == null) {
            return super.numberOfSacrifices(isForSetting, cardsThatCanBeSacrificed, gameData);
        } else {
            return scannedMonster.numberOfSacrifices(isForSetting, cardsThatCanBeSacrificed, gameData);
        }
    }


    @Override
    public void setName(String name) {
        if (scannedMonster == null) {
            super.setName(name);
        } else {
            scannedMonster.setName(name);
        }
    }

    @Override
    public String getDescription() {
        if (scannedMonster == null) {
            return super.getDescription();
        } else {
            return scannedMonster.getName();
        }
    }

    @Override
    public void setDescription(String description) {
        if (scannedMonster == null) {
            super.setDescription(description);
        } else {
            scannedMonster.setDescription(description);
        }
    }

    @Override
    public String getName() {
        if (scannedMonster == null) {
            return super.getName();
        } else {
            return scannedMonster.getName();
        }
    }

    @Override
    public String toString() {
        if (scannedMonster == null) {
            return super.toString();
        } else {
            return scannedMonster.getName();
        }
    }

}
