package view.graphic.Animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import view.graphic.CardView;

public class Translation {

    public CardView cardView;
    public TranslateTransition transition;
    public Timeline timeline;

    public Translation(CardView cardView, double x, double y, double time){

        setCardView(cardView);
        transition = new TranslateTransition();
        transition.setCycleCount(1);
        transition.setNode(cardView);
        transition.setByX(x);
        transition.setByY(y);
        transition.setDuration(Duration.millis(time));
    }

    public Translation(CardView cardView, double fromX, double toX, double fromY, double toY, double time){
        setCardView(cardView);
        double XZero = cardView.getLayoutX();
        double YZero = cardView.getLayoutY();

        toX -= fromX - XZero;
        toY -= fromY - YZero;
        fromX = XZero;
        fromY = YZero;

        transition = new TranslateTransition();
        transition.setCycleCount(1);
        transition.setNode(cardView);

        transition.setFromX(fromX);
        transition.setFromY(fromY);
        transition.setToX(toX);
        transition.setToY(toY);
        transition.setDuration(Duration.millis(time));
    }


    public Translation(boolean isTimeLine, CardView cardView, double X, double Y, double time){

        setCardView(cardView);

        timeline = new Timeline();

        KeyFrame end = new KeyFrame(Duration.millis(time),
                new KeyValue(cardView.xProperty(), X),
                new KeyValue(cardView.yProperty(), Y));

        timeline.getKeyFrames().add(end);
    }


    public Translation(CardView cardView, double toY, double time){
        setCardView(cardView);
        transition = new TranslateTransition();
        transition.setCycleCount(1);
        transition.setNode(cardView);
        transition.setToY(toY);
        transition.setDuration(Duration.millis(time));
    }

    public Translation(CardView cardView, double toX, double time, boolean bool){
        setCardView(cardView);
        transition = new TranslateTransition();
        transition.setCycleCount(1);
        transition.setNode(cardView);
//        transition.setToY(toY);
        transition.setToX(toX);
        transition.setDuration(Duration.millis(time));
    }



    public void start(){
        if(timeline != null){
            timeline.play();
        } else{
            transition.play();
        }
    }

    public void setCardView(CardView cardView){
        this.cardView = cardView;
    }
}
