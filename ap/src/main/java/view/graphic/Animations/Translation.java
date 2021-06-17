package view.graphic.Animations;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class Translation {

    public CardView cardView;
    private TranslateTransition transition;

    public Translation(CardView cardView, int x, int y, double time){
        setCardView(cardView);
        transition = new TranslateTransition();
        transition.setCycleCount(1);
        transition.setNode(cardView);
        transition.setByX(x);
        transition.setByY(y);
        transition.setDuration(Duration.millis(time));
    }

    public void start(){
        transition.play();
    }

    public void setCardView(CardView cardView){
        this.cardView = cardView;
    }
}
