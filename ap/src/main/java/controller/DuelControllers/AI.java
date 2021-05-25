package controller.DuelControllers;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.Attack;
import controller.DuelControllers.Actoins.AttackMonster;
import controller.DuelControllers.Actoins.Summon;
import model.Board.MonsterCardZone;
import model.Card.*;
import model.Card.Monsters.EffectMonster;
import model.Card.Monsters.YomiShip;
import model.Card.Traps.CallOfTheHaunted;
import model.Card.Traps.TorrentialTribute;
import model.Enums.CardMod;
import model.Enums.GameEvent;
import model.Enums.SpellCardMods;
import model.Gamer;
import model.Phase;
import view.GetInput;

import java.util.*;

public class AI {

    public static GameData gameData;
    public static Gamer khalafi;
    public static Gamer rival;

    //data
    public static Monster summoningMonster = null;
    public static Monster settingMonster = null;

    public static void run(GameData gameData) {

        AI.gameData = gameData;
        AI.khalafi = gameData.getCurrentGamer();
        AI.rival = gameData.getSecondGamer();

        if (gameData.getEvent().equals(GameEvent.ASK_FOR_ACTIVATE_TRAP)) {
            GetInput.initializeAIScanner(new Scanner("yes"), 1);
            return;
        } else if (gameData.getEvent().equals(GameEvent.NORMAL)) {
            summoningMonster = null;
            settingMonster = null;
            handleBotTurn();
        } else if (gameData.getEvent().equals(GameEvent.ACTIVATE_TRAP)) {
            handleActivateTrap();
        } else if (gameData.getEvent().equals(GameEvent.ASK_FOR_CONFIRMATION_FOR_ACTIVATE_ANOTHER_SPELL)) {
            GetInput.initializeAIScanner(new Scanner("2"), 1);
        } else if (gameData.getEvent().equals(GameEvent.ASK_FOR_CONFIRMATION_FOR_CHAIN)) {
            GetInput.initializeAIScanner(new Scanner("2"), 1);
        } else if (gameData.getEvent().equals(GameEvent.ASK_FOR_SIDE_DECK)) {
            GetInput.initializeAIScanner(new Scanner("2"), 1);
        } else if (gameData.getEvent().equals(GameEvent.SACRIFICE_FOR_SUMMON_SET)) {
            handleSacrifice();
        }
    }


    private static void handleSacrifice(){
        if(summoningMonster != null){
            int num = summoningMonster.numberOfSacrifices
                    (false, khalafi.getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData);
            if(runSacrifice(num)){
                return;
            }
        }else if(settingMonster != null){
            int num = settingMonster.numberOfSacrifices
                    (true, khalafi.getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData);
            if(runSacrifice(num)){
                return;
            }
        }

        initScanner("cancel\nnext phase", 2);
    }

    private static boolean runSacrifice(int num){

        if(num > khalafi.getGameBoard().getMonsterCardZone().getNumberOfCards()){
            return false;
        }else{
            sacrifice(getIndexForSacrifice(num));
            return true;
        }
    }

    private static void sacrifice(ArrayList<Integer> indexes){
        StringBuilder scanData = new StringBuilder();
        for(int index : indexes){
            scanData.append(" " + index);
        }
        scanData.deleteCharAt(0);
        initScanner(scanData.toString(), 1);
    }

    private static ArrayList<Integer> getIndexForSacrifice(int numSacrifice){
        ArrayList<Monster> monsters = khalafi.getGameBoard().getMonsterCardZone().getCards();
        monsters = deleteNulls(monsters);
        monsters.sort(new sort2());
        ArrayList<Integer> answer = new ArrayList<>();

        for(int i = 0; i < numSacrifice; i++){
            answer.add(khalafi.getGameBoard().getMonsterCardZone().getId(monsters.get(i)));
        }
        
        return answer;
    }

    private static <obj> ArrayList<obj> deleteNulls (ArrayList<obj> list){
        for(Object object : (ArrayList<obj>)list.clone()){
            if(object == null){
                list.remove(object);
            }
        }
        return list;
    }


    static class sort2 implements Comparator<Monster>{

        @Override
        public int compare(Monster o1, Monster o2) {
            return getMosterValue(o1) - getMosterValue(o2);
        }
    }



    private static void handleBotTurn() {
        if (gameData.getCurrentPhase().equals(Phase.MAIN1)) {
            handleMainPhase1();
        } else if (gameData.getCurrentPhase().equals(Phase.BATTLE)) {
            handleBattlePhase();
        } else if (gameData.getCurrentPhase().equals(Phase.MAIN2)){
            initScanner("next phase", 1);
        }
    }

    private static void handleMainPhase1() {

        if (handleSetTrap()) {
            return;
        }
        if (handleSpell()) {
            return;
        }
        handleSummon();
    }

    private static int getNumFreePlaceOfSpellZone() {
        int ans = 0;
        for (Card card : khalafi.getGameBoard().getSpellAndTrapCardZone().getAllCards()) {
            if (card == null) {
                ans++;
            }
        }
        return ans;
    }

    private static void handleActivateTrap() {

        if (gameData.getActionIndexForTriggerActivation() == -1) {
            handleSpeedTrap();
        } else {
            handleTriggerTraps();
        }
    }

    private static void handleTriggerTraps() {

        Action action = gameData.getCurrentActions().get(gameData.getActionIndexForTriggerActivation());

        ArrayList<Card> spellCards = new ArrayList<>();

        spellCards.addAll(khalafi.getGameBoard().getSpellAndTrapCardZone().getAllCards());


        if (action instanceof Summon) {
            handleSummonTraps(action, spellCards);
        } else if (action instanceof Attack) {
            handleAttackTraps(action, spellCards);
        } else {
            initScanner("cancel", 1);
        }
    }

    private static void handleAttackTraps(Action action, ArrayList<Card> spellCards) {

        Monster attackingMonster = (Monster) ((Attack) action).getAttackingMonster();

        int LpDecreasing = 0;

        if (action instanceof AttackMonster) {

            Monster myMonster = (Monster) ((Attack) action).getAttackingMonster();

            if (attackingMonster instanceof YomiShip) {
            } else if (myMonster.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
                LpDecreasing = myMonster.getAttack(gameData) - attackingMonster.getAttack(gameData);
            } else {
                LpDecreasing = Math.max(0,
                        myMonster.getDefence(gameData) - attackingMonster.getAttack(gameData));
            }
        } else {
            LpDecreasing = attackingMonster.getAttack(gameData);
        }

        boolean isLPNecessary = false;
        if (LpDecreasing > 0) {
            if (khalafi.getLifePoint() - LpDecreasing < 500) {
                isLPNecessary = true;
            }
            Trap trap1 = (Trap) getCardInArrayListByName(spellCards, "Negate Attack");
            Trap trap2 = (Trap) getCardInArrayListByName(spellCards, "Magic Cylinder");
            Trap trap3 = (Trap) getCardInArrayListByName(spellCards, "Mirror Force");

            if (trap2 != null) {
                if (((Monster) ((Attack) action).getAttackingMonster()).getAttack(gameData) >= 1000 || isLPNecessary) {
                    activateSpell(trap2);
                    return;
                }
            }
            if (trap1 != null) {
                activateSpell(trap1);
                return;
            }
            if (trap3 != null) {
                activateSpell(trap3);
                return;
            }
        } else {
            initScanner("cancel", 1);
        }
    }


    private static void handleSummonTraps(Action action, ArrayList<Card> spellCards) {

        Trap trap1 = (Trap) getCardInArrayListByName(spellCards, "Trap Hole");
        Trap trap2 = (Trap) getCardInArrayListByName(spellCards, "Torrential tribute");

        if (trap2 != null) {
            if (trap2.canActivateBecauseOfAnAction(action)) {

                int rivalValue, myValue;

                myValue = getMonsterZoneValue(khalafi.getGameBoard().getMonsterCardZone());
                rivalValue = getMonsterZoneValue(rival.getGameBoard().getMonsterCardZone());

                if (getMaxAttackOfGamerMonsters(khalafi, false) >
                        getMaxAttackOfGamerMonsters(rival, true)) {
                    myValue += 200;
                } else {
                    rivalValue += 200;
                }
                if (myValue < rivalValue) {
                    activateSpell(trap1);
                    return;
                }
            }
        }

        if (trap1 != null) {
            if (trap1.canActivateBecauseOfAnAction(action)) {
                activateSpell(trap1);
                return;
            }
        }

        initScanner("cancel", 1);
    }

    private static int getMaxAttackOfGamerMonsters(Gamer gamer, boolean canCheckOHs) {

        int attack = 0;

        for (Monster card : gamer.getGameBoard().getMonsterCardZone().getCards()) {
            if (card.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                if (!canCheckOHs) {
                    continue;
                }
            }
            if (attack < card.getAttack(gameData)) {
                attack = card.getAttack(gameData);
            }
        }
        return attack;
    }


    private static int getMonsterZoneValue(MonsterCardZone zone) {

        int ans = 0;

        for (Monster monster : zone.getCards()) {

            if (monster.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                ans += 75;
            } else {
                ans += monster.getAttack(gameData) / 100 + monster.getDefence(gameData) / 100;
                if (monster instanceof EffectMonster) {
                    ans += 10;
                }
            }
        }
        return ans;
    }

    private static void handleSpeedTrap() {

        ArrayList<Card> spellCards = new ArrayList<>();

        spellCards.addAll(khalafi.getGameBoard().getSpellAndTrapCardZone().getAllCards());

        Trap trap = (Trap) getCardInArrayListByName(spellCards, "call of the haunted");

        if (trap != null) {
            if (trap.canActivate(gameData)) {
                ArrayList<Monster> monsters = new ArrayList<>();
                monsters.addAll((Collection<? extends Monster>) ((CallOfTheHaunted) trap).getGraveYardMonsters(gameData));
                int monsterIndex = monsters.indexOf(getMaxAttack(monsters)) + 1;
                initScanner("select --spell " +
                        khalafi.getGameBoard().getSpellAndTrapCardZone().getId(trap) + "\n" +
                        "activate" + "\n" + monsterIndex, 3);

                return;
            }
        }

        trap = (Trap) getCardInArrayListByName(spellCards, "time seal");
        if (trap != null) {
            activateSpell(trap);
            return;
        }

        initScanner("cancel", 1);
    }

    private static boolean handleSetTrap() {

        if (getNumFreePlaceOfSpellZone() < 3) {
            return false;
        }
        for (Card card : khalafi.getGameBoard().getHand().getCardsInHand()) {
            if (card instanceof Trap) {
                setTrap(card);
                return true;
            }
        }
        return false;
    }

    private static void setTrap(Card card) {
        initScanner("select --hand " + khalafi.getGameBoard().getHand().getId(card) + "\n" +
                "set", 2);
    }

    private static boolean handleSpell() {

        for (Card spell : getSpellsInHand()) {

            if (spell.getName().equals("Dark Hole")) {
                if ((rival.getGameBoard().getMonsterCardZone().getNumberOfCards() == 0)) {
                    continue;
                }
            }
            activateSpell((Spell) spell);
            return true;
        }

        return false;

    }


    private static void activateSpell(SpellAndTraps spell) {
        initScanner("select --spell " + khalafi.getGameBoard().getSpellAndTrapCardZone().getId(spell) +
                "\n" + "activate", 2);
    }

    private static Card getCardInArrayListByName(ArrayList<Card> cards, String cardName) {
        for (Card card : cards) {
            if (card == null) {
                continue;
            }
            if (card.getName().equalsIgnoreCase(cardName)) {
                return card;
            }
        }
        return null;
    }

    private static ArrayList<Card> getSpellsInHand() {
        ArrayList<Card> spells = new ArrayList<>();
        for (Card card : khalafi.getGameBoard().getHand().getCardsInHand()) {
            if (card instanceof Spell) {
                spells.add(card);
            }
        }
        return spells;
    }

    private static Monster getMonsterForSummonFromHand(){

        ArrayList<Monster> hand = getMonstersInHand();
        Monster handMonster = getMaxAttack(hand);

        ArrayList<Monster> myMonsters1 = khalafi.getGameBoard().getMonsterCardZone().getCards();
        ArrayList<Monster> myMonsters2 = (ArrayList<Monster>) myMonsters1.clone();

        while (true){
            if(handMonster != null &&
                    handMonster.numberOfSacrifices(false, myMonsters1.size(), gameData) >
                            myMonsters1.size()){
                hand.remove(handMonster);
                handMonster = getMaxAttack(hand);
            }
            else{
                break;
            }
        }

        return handMonster;
    }

    private static void handleSummon() {

        if (khalafi.getLastTurnHasSummonedOrSet() == gameData.getTurn() || isMYMonsterZoneFull()) {
            initScanner("next phase", 1);
            return;
        }

        ArrayList<Monster> myMonsters1 = khalafi.getGameBoard().getMonsterCardZone().getCards();
        ArrayList<Monster> myMonsters2 = (ArrayList<Monster>) myMonsters1.clone();

        Monster handMonster = getMonsterForSummonFromHand();
        myMonsters2.add(handMonster);

        ArrayList<Monster> rivalMonsters = rival.getGameBoard().getMonsterCardZone().getCards();

        if (new Random().nextInt() % 2 == 1) {

            int index1 = getMaxMaxIReturnedTrue(getAttacks(myMonsters1), getAttacks(rivalMonsters));

            int index2 = getMaxMaxIReturnedTrue(getAttacks(myMonsters2), getAttacks(rivalMonsters));

            if (index2 > index1) {
                summon(handMonster);
            } else {
                handleSet();
            }

        } else {

            if (isAttacksGood2(getAttacks(myMonsters2), getAttacks(rivalMonsters))) {
                summon(handMonster);
            } else {
                handleSet();
            }

        }
    }

    private static void handleBattlePhase() {

        for (SpellAndTraps spell : rival.getGameBoard().getSpellAndTrapCardZone().getAllCards()) {
            if (spell instanceof TorrentialTribute) {
                if (spell.getSpellCardMod().equals(SpellCardMods.OFFENSIVE)) {
                    initScanner("next phase", 1);
                    return;
                }
            }
        }

        if (getMaxAttackOfMyMonsterZone() == null) {
            initScanner("next phase", 1);
            return;
        }

        if (getRivalOOMonsters().size() == 0 && getRivalDOMonsters().size() == 0) {
            if (getOneHOMonsterOfRival() == null) {
                handleDirectAttack();
            } else {
                attack(getMaxAttackOfMyMonsterZone(), getOneHOMonsterOfRival());
            }
        }

        handleAttackMonster(getMaxAttackOfMyMonsterZone());
    }

    private static void handleDirectAttack() {
        directAttack(getMaxAttackOfMyMonsterZone());
    }

    private static void directAttack(Monster monster) {
        initScanner("select --monster " + khalafi.getGameBoard().getMonsterCardZone().getId(monster) + "\n"
                + "direct attack", 2);
    }

    private static Monster getOneHOMonsterOfRival() {

        ArrayList<Monster> monsters = rival.getGameBoard().getMonsterCardZone().getCards();

        for (Monster monster : (ArrayList<Monster>) monsters.clone()) {
            if (monster == null) {
                monsters.remove(monster);
            }
        }

        if (monsters.size() == 0) {
            return null;
        }

        int rand = Math.abs(new Random().nextInt() % monsters.size());
        return monsters.get(rand);
    }

    private static void handleAttackMonster(Monster monster) {

        ArrayList<Monster> rivalOOMonsters = getRivalOOMonsters();
        rivalOOMonsters.sort(new sort1());
        Collections.reverse(rivalOOMonsters);

        ArrayList<Monster> rivalDOMonsters = getRivalDOMonsters();
        rivalDOMonsters.sort(new sort1());
        Collections.reverse(rivalDOMonsters);

        if (rivalOOMonsters.size() == 0 || rivalOOMonsters.get(rivalOOMonsters.size() - 1).
                getAttack(gameData) > monster.getAttack(gameData)) {
            if (rivalDOMonsters.size() == 0 || rivalDOMonsters.get(rivalDOMonsters.size() - 1)
                    .getDefence(gameData) > monster.getAttack(gameData)) {
                initScanner("next phase", 1);
            } else {
                for (Monster monster1 : rivalOOMonsters) {
                    if (monster.getAttack(gameData) > monster1.getDefence(gameData)) {
                        attack(monster, monster1);
                        break;
                    }
                }
            }

        } else {
            for (Monster monster1 : rivalOOMonsters) {
                if (monster.getAttack(gameData) >= monster1.getAttack(gameData)) {
                    attack(monster, monster1);
                    break;
                }
            }
        }
    }

    private static void attack(Monster monster1, Monster monster2) {
        initScanner("select --monster " + khalafi.getGameBoard().getMonsterCardZone().getId(monster1) +
                " \n" + "attack " + rival.getGameBoard().getMonsterCardZone().getId(monster2), 2);
    }

    private static void handleSet() {
        Monster monster = getBestForSet(getMonstersInHand());
        if (monster == null) {
            return;
        }
        set(monster);
    }

    private static void set(Monster monster) {
        settingMonster = monster;

        initScanner("select --hand " + khalafi.getGameBoard().getHand().getId(monster) + "\n" +
                "set", 2);
    }

    private static Monster getBestForSet(ArrayList<Monster> monsters) {
        Monster returnedMonster = null;
        for (Monster monster : monsters) {
            if (monster == null) {
                continue;
            }
            if (returnedMonster == null && getMosterValue(monster) > 0) {
                returnedMonster = monster;
            } else if (getMosterValue(monster) > getMosterValue(returnedMonster)) {
                returnedMonster = monster;
            }
        }
        return returnedMonster;
    }

    private static int getMosterValue(Monster monster) {

        int value = monster.getDefence(gameData) / 10;
        if (monster.getName().equalsIgnoreCase("yomi ship")) {
            value += 100;
        }

        if (monster.numberOfSacrifices
                (true, khalafi.getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData)
                > khalafi.getGameBoard().getMonsterCardZone().getNumberOfCards()) {
            value = -1;
        }

        if (monster.getName().equalsIgnoreCase("man-eater bug")) {
            value += 150;
        }
        return value;
    }

    private static int getMaxMaxIReturnedTrue(ArrayList<Integer> myAttacks, ArrayList<Integer> rivalAttacks) {

        for (int i = 1; i <= 5; i++) {
            if (!isAttacksGood1(myAttacks, rivalAttacks, i)) {
                return i - 1;
            }
        }
        return 5;
    }


    private static boolean isAttacksGood1(ArrayList<Integer> myAttacks, ArrayList<Integer> rivalAttacks, int maxI) {

        Collections.sort(myAttacks);
        Collections.reverse(myAttacks);

        Collections.sort(rivalAttacks);
        Collections.reverse(rivalAttacks);

        int i = 0;

        for (int attack : myAttacks) {
            i++;
            if (i > maxI) {
                break;
            }
            if (rivalAttacks.size() - 1 < i) {
                break;
            }
            if (attack < rivalAttacks.get(i)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAttacksGood2(ArrayList<Integer> myAttacks, ArrayList<Integer> rivalAttacks) {

        int j = 0;
        int OKs = 0;
        for (int attack : myAttacks) {

            for (; j < rivalAttacks.size(); j++) {
                if (attack > rivalAttacks.get(j)) {
                    j++;
                    OKs++;
                    break;
                }
            }
        }

        return OKs == myAttacks.size();
    }

    private static void summon(Monster monster) {
        summoningMonster = monster;
        initScanner("select --monster " + khalafi.getGameBoard().getHand().getId(monster) + "\n"
                + "summon", 2);
    }


    private static ArrayList<Monster> getRivalOOMonsters() {
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Monster monster : rival.getGameBoard().getMonsterCardZone().getCards()) {
            if (monster != null && monster.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
                monsters.add(monster);
            }
        }
        return monsters;
    }

    private static ArrayList<Monster> getRivalDOMonsters() {
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Monster monster : rival.getGameBoard().getMonsterCardZone().getCards()) {
            if (monster != null && monster.getCardMod().equals(CardMod.DEFENSIVE_OCCUPIED)) {
                monsters.add(monster);
            }
        }
        return monsters;
    }

    private static ArrayList<Integer> getAttacks(ArrayList<Monster> monsters) {
        ArrayList<Integer> attacks = new ArrayList<>();
        for (Monster monster : monsters) {
            if (monster != null &&
                    (monster.getCardMod() == null || monster.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED))) {
                attacks.add(monster.getAttack(gameData));
            }
        }

        Collections.sort(attacks);
        Collections.reverse(attacks);

        return attacks;
    }

    private static boolean isMYMonsterZoneFull() {
        return khalafi.getGameBoard().getMonsterCardZone().getCards().size() == 5;
    }

    private static Monster getMaxAttackOfMyMonsterZone() {
        return getMaxAttack(khalafi.getGameBoard().getMonsterCardZone().getCards());
    }

    private static Monster getMaxAttack(ArrayList<Monster> monsters) {

        Monster returnedMonster = null;
        for (Monster monster : monsters) {
            if (monster == null) {
                continue;
            }
            if (monster.getLastTurnAttacked() == gameData.getTurn()) {
                continue;
            }
            if (returnedMonster == null) {
                returnedMonster = monster;
            } else if (monster.getAttack(gameData) > returnedMonster.getAttack(gameData)) {
                returnedMonster = monster;
            }
        }
        return returnedMonster;
    }


    private static ArrayList<Monster> getMonstersInHand() {
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Card card : khalafi.getGameBoard().getHand().getCardsInHand()) {
            if (card instanceof Monster) {
                monsters.add((Monster) card);
            }
        }
        return monsters;
    }


    private static void initScanner(String str, int counter) {
        GetInput.initializeAIScanner(new Scanner(str), counter);
    }


    static class sort1 implements Comparator<Monster> {

        @Override
        public int compare(Monster o1, Monster o2) {

            if (o1 == null) {
                if (o2 == null) {
                    return 0;
                }
                return -1;
            }

            int value1 = setValue(o1);
            int value2 = setValue(o2);
            return value1 - value2;
        }

        private int setValue(Monster monster) {
            if (monster.getCardMod().equals(CardMod.DEFENSIVE_OCCUPIED)) {
                return monster.getDefence(AI.gameData);
            }
            if (monster.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
                return monster.getAttack(AI.gameData);
            } else return -1;
        }
    }
}