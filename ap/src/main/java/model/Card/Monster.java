package model.Card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import model.Card.Spells.EquipSpell;
import model.Card.Spells.FieldSpell;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;
import view.Printer.Printer;

import java.util.ArrayList;

public class Monster extends Card {

    @SerializedName("Atk")
    public int attack;
    @SerializedName("Def")
    public int defence;
    @SerializedName("Level")
    private int level;
    @SerializedName("Attribute")
    private Attribute attribute;
    @SerializedName("Monster Type")
    private MonsterType monsterType;
    @SerializedName("Card Type")
    private MonsterTypesForEffects monsterTypesForEffects;

    @Expose
    private CardMod cardMod;
    @Expose
    private int lastTurnAttacked = 0;
    @Expose
    private int lastTurnHasChangedPosition = 0;
    @Expose
    private int turnWasPutInMonsterZone = 0;
    @Expose
    private Trap callOfTheHauntedTrap = null;
    @Expose
    private ArrayList<EquipSpell> equippedSpells = new ArrayList<>();

    public Monster(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price);
        this.attack = attack;
        this.defence = defence;
        this.level = level;
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.monsterTypesForEffects = monsterTypesForEffects;

        setCardFamily(CardFamily.MONSTER);
        setEffectType(MonsterTypesForEffects.EFFECT);
    }

    public void setCallOfTheHauntedTrap(Trap trap) {
        callOfTheHauntedTrap = trap;
    }


    public int getAttack(GameData gameData) {
        int attackChangeFromSelf = 0;
        int attackChangeFromRival = 0;
        int attackChangeFromEquippedSpells = 0;
        if (gameData.getCurrentGamer().getGameBoard().getFieldZone().getCard(0) != null) {
            attackChangeFromSelf = ((FieldSpell) gameData.getCurrentGamer().getGameBoard()
                    .getFieldZone().getCard(0)).attackDifference(getMonsterType(), gameData);
        }
        if (gameData.getSecondGamer().getGameBoard().getFieldZone().getCard(0) != null) {
            attackChangeFromRival = ((FieldSpell) gameData.getSecondGamer().getGameBoard()
                    .getFieldZone().getCard(0)).attackDifference(getMonsterType(), gameData);
        }
        for (EquipSpell equippedSpell : equippedSpells) {
            attackChangeFromEquippedSpells += equippedSpell.changeInAttack(gameData);
        }

        return attack + attackChangeFromRival + attackChangeFromSelf + attackChangeFromEquippedSpells;
    }

    public int getDefence(GameData gameData) {
        int defenceChangeFromSelf = 0;
        int defenceChangeFromRival = 0;
        int defenceChangeFromEquippedSpells = 0;
        if (gameData.getCurrentGamer().getGameBoard().getFieldZone().getCard(0) != null) {
            defenceChangeFromSelf = ((FieldSpell) gameData.getCurrentGamer().getGameBoard()
                    .getFieldZone().getCard(0)).defenceDifference(getMonsterType());
        }
        if (gameData.getSecondGamer().getGameBoard().getFieldZone().getCard(0) != null) {
            defenceChangeFromRival = ((FieldSpell) gameData.getSecondGamer().getGameBoard()
                    .getFieldZone().getCard(0)).defenceDifference(getMonsterType());
        }
        for (EquipSpell equippedSpell : equippedSpells) {
            defenceChangeFromEquippedSpells += equippedSpell.changeInDefence(gameData);
        }

        return defence + defenceChangeFromRival + defenceChangeFromSelf + defenceChangeFromEquippedSpells;
    }


    public MonsterTypesForEffects getEffectType() {
        return monsterTypesForEffects;
    }

    public void setEffectType(MonsterTypesForEffects monsterTypesForEffects) {
        this.monsterTypesForEffects = monsterTypesForEffects;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }


    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public CardMod getCardMod() {
        return cardMod;
    }

    public void setCardMod(CardMod cardMod) {
        this.cardMod = cardMod;
    }

    public int getLastTurnAttacked() {
        return lastTurnAttacked;
    }

    public int getTurnWasPutInMonsterZone() {
        return turnWasPutInMonsterZone;
    }

    public int getLastTurnHasChangedPosition() {
        return lastTurnHasChangedPosition;
    }

    public void setLastTurnHasChangedPosition(int lastTurnHasChangedPosition) {
        this.lastTurnHasChangedPosition = lastTurnHasChangedPosition;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public void setTurnWasPutInMonsterZone(int turnWasPutInMonsterZone) {
        this.turnWasPutInMonsterZone = turnWasPutInMonsterZone;
    }

    public boolean handleFlip(GameData gameData, CardMod modeToSet) {
        setCardMod(modeToSet);
        return true;
    }

    public void handleAttack(GameData gameData, int enemyId) {

        Monster defendingMonster = (Monster) gameData.getSecondGamer()
                .getGameBoard().getMonsterCardZone().getCardById(enemyId);

        setLastTurnAttacked(gameData);

        switch (defendingMonster.getCardMod()) {
            case OFFENSIVE_OCCUPIED -> attackOffensiveMonster(defendingMonster, gameData);
            case DEFENSIVE_OCCUPIED -> attackDefensiveMonster(defendingMonster, gameData);
            case DEFENSIVE_HIDDEN -> attackDefensiveHiddenMonster(defendingMonster, gameData);
        }

    }

    public void setLastTurnAttacked(GameData gameData) {
        this.lastTurnAttacked = gameData.getTurn();
    }

    public void attackDefensiveHiddenMonster(Monster defendingMonster, GameData gameData) {
        System.out.print("opponent’s monster card was " + defendingMonster.getName() + " and ");
        defendingMonster.handleFlip(gameData, CardMod.DEFENSIVE_OCCUPIED);
        attackDefensiveMonster(defendingMonster, gameData);
    }

    public void attackDefensiveMonster(Monster defendingMonster, GameData gameData) {
        int damage;
        if (getAttack(gameData) > defendingMonster.getDefence(gameData)) {
            defendingMonster.handleDestroy(gameData);
            Printer.print("the defence position monster is destroyed");
        } else if (getAttack(gameData) < defendingMonster.getDefence(gameData)) {
            handleDestroy(gameData);
            damage = defendingMonster.getDefence(gameData) - getAttack(gameData);
            gameData.getCurrentGamer().decreaseLifePoint(damage);
            Printer.print("no card is destroyed and you received " + damage + " battle damage");
        } else {
            Printer.print("no card is destroyed");
        }
    }

    public void attackOffensiveMonster(Monster defendingMonster, GameData gameData) {

        int damage;

        if (getAttack(gameData) > defendingMonster.getAttack(gameData)) {
            damage = getAttack(gameData) - defendingMonster.getAttack(gameData);
            gameData.getSecondGamer().decreaseLifePoint(damage);
            defendingMonster.handleDestroy(gameData);
            Printer.print("your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage");
        } else if (getAttack(gameData) < defendingMonster.getAttack(gameData)) {
            damage = defendingMonster.getAttack(gameData) - getAttack(gameData);
            gameData.getCurrentGamer().decreaseLifePoint(damage);
            handleDestroy(gameData);
            Printer.print("Your monster card is destroyed and you received " + damage + " battle damage");
        } else {
            defendingMonster.handleDestroy(gameData);
            handleDestroy(gameData);
            Printer.print("both you and your opponent monster cards are destroyed and no one receives damage");
        }
    }

    public void handleSet(GameData gameData) {
        setCardMod(CardMod.DEFENSIVE_HIDDEN);
        gameData.moveCardFromOneZoneToAnother(this,
                gameData.getCurrentGamer().getGameBoard().getHand(),
                gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
        setTurnWasPutInMonsterZone(gameData.getTurn());
    }

    public void handleChangePosition(GameData gameData, CardMod newCardMod) {
        setCardMod(newCardMod);
        setLastTurnHasChangedPosition(gameData.getTurn());
    }


    public void handleDirectAttack(GameData gameData) {
        setLastTurnAttacked(gameData);
        gameData.getSecondGamer().decreaseLifePoint(getAttack(gameData));
        Printer.print("your opponent receives " + getAttack(gameData) + " battle damage");
    }

    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        gameData.moveCardFromOneZoneToAnother(this,
                gameData.getCurrentGamer().getGameBoard().getHand(),
                gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
        this.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
        setTurnWasPutInMonsterZone(gameData.getTurn());
    }

    public void sacrifice(GameData gameData, Gamer gamer) {
        gameData.moveCardFromOneZoneToAnother(this,
                gamer.getGameBoard().getMonsterCardZone(),
                gamer.getGameBoard().getGraveYard());
    }


    public boolean attackIsNormal(GameData gameData) {
        return true;
    }


    public void handleDestroy(GameData gameData) {

        if (callOfTheHauntedTrap != null) {
            new Destroy(gameData).run(callOfTheHauntedTrap, false);
        }
        if (!equippedSpells.isEmpty()){
            for (EquipSpell equippedSpell : equippedSpells) {
                equippedSpell.handleDestroy(gameData);
            }
        }

        super.handleDestroy(gameData);
    }

    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed, GameData gameData) {
        if (level <= 4) {
            return 0;
        } else if (level <= 6) {
            return 1;
        }
        return 2;
    }

    public void addEquipSpell(EquipSpell equipSpell) {
        equippedSpells.add(equipSpell);
    }

}
