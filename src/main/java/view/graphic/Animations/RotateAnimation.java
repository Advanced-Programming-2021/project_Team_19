package view.graphic.Animations;

import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import view.graphic.CardView;

public class RotateAnimation {

    private RotateTransition animation;

    public RotateAnimation(CardView cardView, double time, double angle){

        javafx.animation.RotateTransition rotate1 = new javafx.animation.RotateTransition();
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
