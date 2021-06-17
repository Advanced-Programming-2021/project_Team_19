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
    boolean isHidden = true;
    public static double height = 614;
    public static double width = 423;
    public double sizeInverse;

    public CardView(Card card, double sizeInverse){
        super(width / sizeInverse, height / sizeInverse);
        this.sizeInverse = sizeInverse;
        this.card = card;
        if(isHidden || card == null){
            setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        } else{
            setCardImage();
        }
    }

    public void setCardImage(){
        String model =  card instanceof Monster ? "Monsters/" : "SpellTrap/";
        setFill(new ImagePattern(new Image("/Assets/Cards/" + model +
                Utils.getPascalCase(card.getName()) +".jpg")));
    }

    public Card getCard(){
        return card;
    }
}

