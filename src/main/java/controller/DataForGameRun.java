package controller;
import java.util.*;
import model.*;
import model.Card.*;

public class DataForGameRun {
    String command;
    Gamer gamer;
    Card card;

    public DataForGameRun(String command, Gamer gamer){
        this.command = command;
        this.gamer = gamer;
    }

    public DataForGameRun(Card card, Gamer gamer){
        this.card = card;
        this.gamer = gamer;
    }

    public Card getCard() {
        return card;
    }

    public String getCommand() {
        return command;
    }

    public Gamer getGamer() {
        return gamer;
    }
}
