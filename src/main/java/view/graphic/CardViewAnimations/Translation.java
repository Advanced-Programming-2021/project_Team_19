package view.graphic.CardViewAnimations;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class Translation implements CardViewAnimationMaker{

    private TranslateTransition transition;

    public Translation(CardView cardView, double toY, double time){

        transition = new TranslateTransition();
        transition.setCycleCount(1);
        transition.setNode(cardView);
        transition.setToY(toY);
        transition.setDuration(Duration.millis(time));
    }

    public TranslateTransition getAnimation(){
        return transition;
    }
}
