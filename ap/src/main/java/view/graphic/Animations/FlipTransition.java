package view.graphic.Animations;

import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import view.graphic.CardView;
import view.graphic.Utils;

import static view.Printer.Printer.print;

public class FlipTransition {

    private CardView cardView;
    boolean hasImageSet = false;

    public FlipTransition(CardView cardView){
        setCardView(cardView);
    }

    public void start(){
        RotateTransition rotate = new RotateTransition();
        rotate.setByAngle(90);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setNode(cardView);
        rotate.setDuration(Duration.millis(500));
        rotate.play();

        rotate.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!hasImageSet){
                    cardView.setCardImage();
                    hasImageSet = true;
                    rotate.setByAngle(-90);
                    rotate.play();
                }
            }
        });

    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }
}
