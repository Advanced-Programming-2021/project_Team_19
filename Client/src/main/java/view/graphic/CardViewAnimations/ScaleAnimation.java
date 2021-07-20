package view.graphic.CardViewAnimations;

import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class ScaleAnimation implements CardViewAnimationMaker{

    CardView cardView;
    ScaleTransition animation;

    public ScaleAnimation(CardView cardView, double size, double time){
        this.cardView = cardView;
        animation = new ScaleTransition();
        animation.setNode(cardView);
        animation.setDuration(Duration.millis(time));
        animation.setByY(size);
        animation.setByX(size);
    }

    public void start(){
        animation.play();
    }

    public ScaleTransition getAnimation(){
        return animation;
    }
}
