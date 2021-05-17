package model.Card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;
import view.Printer.Printer;

public class Monster extends Card {

    @SerializedName("Atk")
    private int attack;
    @SerializedName("Def")
    private int defence;
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

    {
        setCardFamily(CardFamily.MONSTER);
        setEffectType(MonsterTypesForEffects.Effect);
    }

    public void setCallOfTheHauntedTrap(Trap trap){
        callOfTheHauntedTrap = trap;
    }


    public int getAttack(GameData gameData) {
        return attack;
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

    public int getDefence() {
        return defence;
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

    public boolean handleFlip(GameData gameData) {
        setCardMod(CardMod.OFFENSIVE_OCCUPIED);
        return true;
    }

    public void handleAttack(GameData gameData, int enemyId) {

        Monster defendingMonster = (Monster) gameData.getSecondGamer()
                .getGameBoard().getMonsterCardZone().getCardById(enemyId);

        lastTurnAttacked = gameData.getTurn();

        switch (defendingMonster.getCardMod()) {
            case OFFENSIVE_OCCUPIED -> attackOffensiveMonster(defendingMonster, gameData);
            case DEFENSIVE_OCCUPIED -> attackDefensiveMonster(defendingMonster, gameData);
            case DEFENSIVE_HIDDEN -> attackDefensiveHiddenMonster(defendingMonster, gameData);
        }

    }

    public void attackDefensiveHiddenMonster(Monster defendingMonster, GameData gameData) {
        System.out.print("opponent’s monster card was " + defendingMonster.getName() + " and ");
        defendingMonster.setCardMod(CardMod.DEFENSIVE_OCCUPIED);
        attackDefensiveMonster(defendingMonster, gameData);
    }

    public  void attackDefensiveMonster(Monster defendingMonster, GameData gameData) {
        int damage;
        if (getAttack(gameData) > defendingMonster.getDefence()) {
            defendingMonster.handleDestroy(gameData);
            Printer.print("the defense position monster is destroyed");
        } else if (getAttack(gameData) < defendingMonster.getDefence()) {
            handleDestroy(gameData);
            damage = defendingMonster.getDefence() - getAttack(gameData);
            gameData.getCurrentGamer().decreaseLifePoint(damage);
            Printer.print("no card is destroyed and you received " + damage + " battle damage");
        } else {
            Printer.print("no card is destroyed");
        }
    }

    public  void attackOffensiveMonster(Monster defendingMonster, GameData gameData) {

        int damage;

        if (getAttack(gameData) > defendingMonster.getAttack(gameData)) {
            defendingMonster.handleDestroy(gameData);
            damage = getAttack(gameData) - defendingMonster.getAttack(gameData);
            gameData.getSecondGamer().decreaseLifePoint(damage);
            Printer.print("your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage");
        } else if (getAttack(gameData) < defendingMonster.getAttack(gameData)) {
            handleDestroy(gameData);
            damage = defendingMonster.getAttack(gameData) - getAttack(gameData);
            gameData.getCurrentGamer().decreaseLifePoint(damage);
            Printer.print("Your monster card is destroyed and you received " + damage + " battle damage");
        } else {
            handleDestroy(gameData);
            defendingMonster.handleDestroy(gameData);
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
        lastTurnAttacked = gameData.getTurn();
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
        super.handleDestroy(gameData);
    }

    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed){
        if (level <= 4) {
            return  0;
        } else if (level <= 6) {
            return 1;
        }
        return 2;
    }


}
