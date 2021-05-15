package model.Data;

import model.Card.Card;

public class TriggerActivationData extends ActivationData {

    public boolean hasActionStopped;
    public String message;

    public TriggerActivationData(boolean hasActionStopped, String message,
                                 Card activatedCard){

        super(activatedCard);
        this.hasActionStopped = hasActionStopped;
        this.message = message;
    }

}