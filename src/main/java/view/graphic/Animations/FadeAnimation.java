package view.graphic.Animations;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class FadeAnimation {

    FadeTransition fadeTransition = new FadeTransition();

    public FadeAnimation(CardView cardView, double time, double fromValue, double toValue){
        fadeTransition.setDuration(Duration.millis(time));
        fadeTransition.setNode(cardView);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
    }

    public FadeTransition getAnimation() {
        return fadeTransition;
    }
}
