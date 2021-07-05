package view.graphic.CardViewAnimations;

import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class ScaleAnimation implements CardViewAnimationMaker {

    CardView cardView;
    ScaleTransition animation;

    public ScaleAnimation(CardView cardView, double size, double time){
        this.cardView = cardView;
        animation = new ScaleTransition();
        animation.setNode(cardView);
        animation.setDuration(Duration.millis(time));
        animation.setByY(size);
        animation.setByX(size);
//        transition.setDelay(Duration.millis(1000));
//        transition.setFromX(1);
//        transition.setFromY(1);
//        transition.setToX(0.5);
//        transition.setToY(0.5);
    }

    public void start(){
        animation.play();
    }

    public ScaleTransition getAnimation(){
        return animation;
    }
}
