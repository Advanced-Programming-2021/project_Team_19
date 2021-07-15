package view.graphic.CardViewAnimations;

import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import view.graphic.CardView;

public class FlipAnimation implements CardViewAnimationMaker{

    private SequentialTransition animation;

    public FlipAnimation(CardView cardView, double time){

        RotateTransition rotate1 = new RotateTransition();

        rotate1.setByAngle(90);
        setRotateAxis(cardView, rotate1);
        rotate1.setNode(cardView);
        rotate1.setDuration(Duration.millis(time / 2));
        rotate1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(cardView.isHidden){
                    cardView.setCardImage();
                } else{
                    cardView.hideCard();
                }

            }
        });

        RotateTransition rotate2 = new RotateTransition();

        rotate2.setByAngle(-90);
        setRotateAxis(cardView, rotate2);
        rotate2.setNode(cardView);
        rotate2.setDuration(Duration.millis(time / 2));

        animation = new SequentialTransition(rotate1, rotate2);

    }

    private void setRotateAxis(CardView cardView, RotateTransition tr){
        if(cardView.isVertical){
            tr.setAxis(Rotate.Y_AXIS);
        } else {
            tr.setAxis(Rotate.X_AXIS);
        }
    }

    public SequentialTransition getAnimation(){
        return animation;
    }

    public void start(){
        animation.play();
    }
}
