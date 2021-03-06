package model.Card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import model.Enums.CardFamily;
import model.Enums.Status;

import java.util.Comparator;

public class Card {

    @SerializedName("Price")
    private int price;
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("Status")
    public Status status = Status.UNLIMITED;

    @Expose
    private CardFamily cardFamily;
    @Expose
    private int effectSpeed = 0;
    @Expose
    public boolean hasActivationEffectCanceledInChain = false;

    public int getEffectSpeed() {
        return effectSpeed;
    }

    public void setEffectSpeed(int speed) {
        effectSpeed = speed;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CardFamily getCardFamily() {
        return cardFamily;
    }

    public void setCardFamily(CardFamily cardFamily) {
        this.cardFamily = cardFamily;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + " : " + description;
    }

    public Card(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static class CardComp implements Comparator<Card> {

        @Override
        public int compare(Card o1, Card o2) {
            if (o1.getName().compareTo(o2.getName()) == 0) {
                return 1;
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        }
    }
}
