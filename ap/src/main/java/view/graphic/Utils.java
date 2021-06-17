package view.graphic;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.Card.Card;
import model.Card.Monster;

public class Utils {

    public static Image getImageByCard(Card card){

        if(card != null){
            String model =  card instanceof Monster ? "Monsters/" : "SpellTrap/";
            return new Image("/Assets/Cards/" + model +
                    controller.Utils.getPascalCase(card.getName()) +".jpg");

        }
        return new Image("/Assets/Cards/Monsters/Unknown.jpg");
    }
}
