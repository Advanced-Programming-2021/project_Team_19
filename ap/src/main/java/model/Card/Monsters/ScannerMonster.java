package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Card.Trap;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;
import view.Printer.Printer;

public class ScannerMonster extends Monster {
    private Monster tempMonster;
    private int monsterSetTurn;

    public void setMonster(Monster monster,GameData gameData){
        this.tempMonster=monster;
        monsterSetTurn = gameData.getTurn();
    }

    public void setValidity(GameData gameData){
        if(gameData.getTurn() == monsterSetTurn){
        }
        else{
            tempMonster = null;
        }
    }

    public void resetMonster(Monster monster){
        monster = null;
    }

    public boolean isMonsterSet(){
        return tempMonster != null;
    }

    @Override
    public int getAttack(GameData gameData) {
        if(tempMonster == null){
            return super.getAttack(gameData);
        }
        else{
            return tempMonster.getAttack(gameData);
        }
    }

    @Override
    public MonsterTypesForEffects getEffectType() {
        if(tempMonster == null){
            return super.getEffectType();
        }
        else{
            return tempMonster.getEffectType();
        }
    }

    @Override
    public void setAttack(int attack) {
        if(tempMonster == null){
            super.setAttack(attack);
        }
        else{
            tempMonster.setAttack(attack);
        }
    }

    @Override
    public int getDefence(GameData gameData) {
        if(tempMonster == null){
            return super.getDefence(gameData);
        }
        else{
            return tempMonster.getDefence(gameData);
        }
    }

    @Override
    public void setDefence(int defence) {
        if (tempMonster == null) {
            super.setDefence(defence);
        }
        else{
            tempMonster.setDefence(defence);
        }
    }

    @Override
    public int getLevel() {
        if(tempMonster == null){
            return super.getLevel();
        }
        else{
            return tempMonster.getLevel();
        }
    }

    @Override
    public void setLevel(int level) {
        if(tempMonster == null){
            super.setLevel(level);
        }
        else{
            tempMonster.setLevel(level);
        }
    }

    @Override
    public Attribute getAttribute() {
        if(tempMonster == null){
            return super.getAttribute();
        }
        else{
            return tempMonster.getAttribute();
        }
    }

    @Override
    public void setAttribute(Attribute attribute) {
        if(tempMonster == null){
            super.setAttribute(attribute);
        }
        else{
            tempMonster.setAttribute(attribute);
        }
    }

    @Override
    public CardMod getCardMod() {
        if(tempMonster == null){
            return super.getCardMod();
        }
        else{
            return tempMonster.getCardMod();
        }
    }

    @Override
    public void setCardMod(CardMod cardMod) {
        if(tempMonster == null){
            super.setCardMod(cardMod);
        }
        else{
            tempMonster.setCardMod(cardMod);
        }
    }

    @Override
    public boolean handleFlip(GameData gameData, CardMod cardMod) {
        if(tempMonster == null){
            return super.handleFlip(gameData, cardMod);
        }
        else{
            return tempMonster.handleFlip(gameData, cardMod);
        }
    }

    @Override
    public void handleAttack(GameData gameData, int enemyId) {
        if(tempMonster == null){
            super.handleAttack(gameData,enemyId);
        }
        else{
            tempMonster.handleAttack(gameData,enemyId);
        }

    }

    @Override
    public  void attackDefensiveHiddenMonster(Monster defendingMonster, GameData gameData) {
        if(tempMonster == null){
            super.attackDefensiveHiddenMonster(defendingMonster, gameData);
        }
        else{
            tempMonster.attackDefensiveHiddenMonster(defendingMonster,gameData);
        }
    }

    @Override
    public  void attackDefensiveMonster(Monster defendingMonster, GameData gameData) {
        if(tempMonster == null){
            super.attackDefensiveMonster(defendingMonster, gameData);
        }
        else{
            tempMonster.attackDefensiveMonster(defendingMonster, gameData);
        }
    }
    @Override
    public  void attackOffensiveMonster(Monster defendingMonster, GameData gameData) {
        if(tempMonster == null){
            super.attackOffensiveMonster(defendingMonster, gameData);
        }
        else{
            tempMonster.attackOffensiveMonster(defendingMonster, gameData);
        }

    }

    @Override
    public void handleSet(GameData gameData) {
        if(tempMonster == null){
            super.handleSet(gameData);
        }
        else{
            tempMonster.handleSet(gameData);
        }
    }

    @Override
    public void handleChangePosition(GameData gameData, CardMod newCardMod) {
        if(tempMonster == null){
            super.handleChangePosition(gameData, newCardMod);
        }
        else{
            tempMonster.handleChangePosition(gameData, newCardMod);
        }
    }

    @Override
    public void handleDirectAttack(GameData gameData) {
        if(tempMonster == null){
            super.handleDirectAttack(gameData);
        }
        else{
            tempMonster.handleDirectAttack(gameData);
        }
    }



    @Override
    public boolean attackIsNormal(GameData gameData) {
        if(tempMonster == null){
            return super.attackIsNormal(gameData);
        }
        else{
            return tempMonster.attackIsNormal(gameData);
        }
    }

    @Override
    public void handleDestroy(GameData gameData) {
        if(tempMonster == null){
            super.handleDestroy(gameData);
        }
        else{
            tempMonster.handleDestroy(gameData);
        }
    }

    @Override
    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed, GameData gameData){
        if(tempMonster == null){
            return super.numberOfSacrifices(isForSetting, cardsThatCanBeSacrificed, gameData);
        }
        else{
            return tempMonster.numberOfSacrifices(isForSetting, cardsThatCanBeSacrificed, gameData);
        }
    }

    @Override
    public int getPrice() {
        if(tempMonster == null){
            return super.getPrice();
        }
        else{
            return tempMonster.getPrice();
        }
    }

    @Override
    public void setPrice(int price) {
        if(tempMonster == null){
            super.setPrice(price);
        }
        else{
            tempMonster.setPrice(price);
        }
    }

    @Override
    public void setName(String name) {
        if(tempMonster == null){
            super.setName(name);
        }
        else{
            tempMonster.setName(name);
        }
    }

    @Override
    public String getDescription() {
        if(tempMonster == null){
            return super.getDescription();
        }
        else{
            return tempMonster.getName();
        }
    }

    @Override
    public void setDescription(String description) {
        if(tempMonster == null){
            super.setDescription(description);
        }
        else{
            tempMonster.setDescription(description);
        }
    }

    @Override
    public CardFamily getCardFamily() {
        if(tempMonster == null){
            return super.getCardFamily();
        }
        else{
            return tempMonster.getCardFamily();
        }
    }

    @Override
    public void setCardFamily(CardFamily cardFamily) {
        if(tempMonster == null){
            super.setCardFamily(cardFamily);
        }
        else{
            tempMonster.setCardFamily(cardFamily);
        }
    }

    @Override
    public String getName() {
        if(tempMonster == null){
            return super.getName();
        }
        else{
            return tempMonster.getName();
        }
    }

    @Override
    public String toString(){
        if(tempMonster == null){
            return super.toString();
        }
        else{
            return tempMonster.getName();
        }
    }

    public ScannerMonster(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }

}
