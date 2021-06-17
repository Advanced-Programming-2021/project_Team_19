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

    public static double height = 614;
    public static double width = 423;

    public CardView(Card card, double sizeInverse){
        super(width / sizeInverse, height / sizeInverse);
        this.card = card;
        if(card != null){
            String model =  card instanceof Monster ? "Monsters/" : "SpellTrap/";
            setFill(new ImagePattern(new Image("/Assets/Cards/" + model +
                    Utils.getPascalCase(card.getName()) +".jpg")));
        } else{
            setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        }
    }
}

