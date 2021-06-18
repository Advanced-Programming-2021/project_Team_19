package view.graphic;

import controller.Utils;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.Card.Card;
import model.Card.Monster;

public class CardView extends Rectangle {

    Card card;
    public boolean isHidden = false;
    public static double height = 614;
    public static double width = 423;
    public double sizeInverse;

    public CardView(double sizeInverse){
        super(width / sizeInverse, height / sizeInverse);
        this.sizeInverse = sizeInverse;
        hideCard();
    }

    public CardView(Card card, double sizeInverse, boolean isHidden){
        super(width / sizeInverse, height / sizeInverse);
        this.sizeInverse = sizeInverse;
        this.card = card;
        this.isHidden = isHidden;
        if(isHidden){
            setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        } else{
            setCardImage();
        }
    }

    public void setHidden(){
        isHidden = true;
    }

    public void hideCard(){
        setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        isHidden = true;
    }

    public void setCardImage(){
        String model =  card instanceof Monster ? "Monsters/" : "SpellTrap/";
        setFill(new ImagePattern(new Image("/Assets/Cards/" + model +
                Utils.getPascalCase(card.getName()) +".jpg")));
        isHidden = false;
    }

    public Card getCard(){
        return card;
    }
}

