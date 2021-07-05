package model.Data;

import model.Card.Card;

public class graphicDataForServerToNotifyOtherClient {
    public String command;
    public Card card;
    public int index;

    public graphicDataForServerToNotifyOtherClient(String command, Card card, int index) {
        this.command = command;
        this.card = card;
        this.index = index;
    }
}
