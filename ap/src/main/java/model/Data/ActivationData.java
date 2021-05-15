package model.Data;

import model.Card.Card;

public class ActivationData {

    public Card activatedCard;
    public String message;

    public ActivationData(Card activatedCard, String message){
        this.activatedCard = activatedCard;
        this.message = message;
    }

}
