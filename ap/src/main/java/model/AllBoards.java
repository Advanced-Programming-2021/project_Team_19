package model;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.Utils;
import model.Board.*;
import model.Card.Card;

public class AllBoards {

    private DeckZone deckZone;
    private FieldZone fieldZone = new FieldZone();
    private GraveYard graveYard = new GraveYard();
    private MonsterCardZone monsterCardZone = new MonsterCardZone();
    private SpellAndTrapCardZone spellAndTrapCardZone = new SpellAndTrapCardZone();
    private Hand hand = new Hand();


    public AllBoards(User user) {
        Deck deck = DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName());
        deckZone = new DeckZone(deck);
        initializeHand();
    }

    private void initializeHand() {
        for (int i = 0; i < 5; i++) {
            hand.addCard(deckZone.removeCard(0));
        }
    }

    public DeckZone getDeckZone() {
        return deckZone;
    }

    public void setDeckZone(DeckZone deckZone) {
        this.deckZone = deckZone;
    }

    public FieldZone getFieldZone() {
        return fieldZone;
    }

    public void setFieldZone(FieldZone fieldZone) {
        this.fieldZone = fieldZone;
    }

    public GraveYard getGraveYard() {
        return graveYard;
    }

    public void setGraveYard(GraveYard graveYard) {
        this.graveYard = graveYard;
    }

    public MonsterCardZone getMonsterCardZone() {
        return monsterCardZone;
    }

    public void setMonsterCardZone(MonsterCardZone monsterCardZone) {
        this.monsterCardZone = monsterCardZone;
    }

    public SpellAndTrapCardZone getSpellAndTrapCardZone() {
        return spellAndTrapCardZone;
    }

    public void setSpellAndTrapCardZone(SpellAndTrapCardZone spellAndTrapCardZone) {
        this.spellAndTrapCardZone = spellAndTrapCardZone;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }


    public void sendFromOneZoneToOther(Zones firstZone, Zones secondZone, int id) {
        secondZone.addCard(firstZone.removeCard(id));
    }

    public Zones getZone(Card card) {
        if (fieldZone.getId(card) != -1) {
            return fieldZone;
        } else if (hand.getId(card) != -1) {
            return hand;
        } else if (monsterCardZone.getId(card) != -1) {
            return monsterCardZone;
        } else if (spellAndTrapCardZone.getId(card) != -1) {
            return spellAndTrapCardZone;
        } else if (deckZone.getId(card) != -1) {
            return deckZone;
        } else if (graveYard.getId(card) != -1) {
            return graveYard;
        } else {
            return null;
        }
    }


    public String selfToString() {
        return "\n" +
                fieldZone.toString() + "\t".repeat(6) + graveYard.getSize() + "\n" +
                monsterCardZone.getStringForSelf() + "\n" +
                spellAndTrapCardZone.getStringForSelf() + "\n" +
                "\t".repeat(6) + deckZone.getSize() + "\n" +
                hand.selfToString() + "\n";
    }

    public String rivalToString() {
        return hand.rivalToString() + "\n" +
                deckZone.getSize() + "    ".repeat(6) + "\n" +
                spellAndTrapCardZone.getStringForRival() + "\n" +
                monsterCardZone.getStringForRival() + "\n" +
                graveYard.getSize() + "\t".repeat(6) + fieldZone.toString() + "\n";
    }



    //test

    private AllBoards(){}

    public static AllBoards getTestBoard(User user){

        AllBoards board = new AllBoards();

        Deck deck = DeckDataBaseController.getDeckByName(user.getActiveDeckName());
        board.deckZone = new DeckZone(deck);

        board.initializeHand();

        Card card = Utils.getCardByName("Advanced ritual art");
        board.getHand().getCardsInHand().set(1, card);
        card = Utils.getCardByName("skull guardian");
        board.getHand().getCardsInHand().set(0, card);

        board.setSpellAndTrapCardZone(SpellAndTrapCardZone.getTestZone());
        board.setMonsterCardZone(MonsterCardZone.getTestZone());
        board.setGraveYard(GraveYard.getTestGraveYard());
        return board;
    }
}
