package view.graphic.Animations;

import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class ScaleAnimation {

    CardView cardView;
    ScaleTransition transition;

    public ScaleAnimation(CardView cardView, double size, double time){
        this.cardView = cardView;
        transition = new ScaleTransition();
        transition.setNode(cardView);
        transition.setDuration(Duration.millis(time));
        transition.setByY(size);
        transition.setByX(size);
//        transition.setDelay(Duration.millis(1000));
//        transition.setFromX(1);
//        transition.setFromY(1);
//        transition.setToX(0.5);
//        transition.setToY(0.5);
    }

    public void start(){
        transition.play();
    }
}
