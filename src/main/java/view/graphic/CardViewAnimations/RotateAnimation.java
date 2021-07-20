package view.graphic.CardViewAnimations;

import javafx.animation.RotateTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class RotateAnimation implements CardViewAnimationMaker{

    private RotateTransition animation;

    public RotateAnimation(CardView cardView, double time, double angle){

        RotateTransition rotate1 = new RotateTransition();
        rotate1.setByAngle(angle);
        rotate1.setNode(cardView);
        rotate1.setDuration(Duration.millis(time));
        animation = rotate1;
    }

    public RotateTransition getAnimation(){
        return animation;
    }

    public void start(){
        animation.play();
    }
}
