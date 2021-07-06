package view.graphic;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import model.Card.Card;
import model.Data.graphicDataForServerToNotifyOtherClient;
import view.graphic.CardViewAnimations.*;

import java.util.ArrayList;

public class ActionsAnimationHandler {

    static double runAddCardsToHandFromDeckAnimation
            (GameView gameView, ArrayList<CardView> cardViews) {

        gameView.setBooleanForShowActions(gameView.selfHand, false);
        getHandAnimation(gameView, cardViews, 0).play();
        return 500 * cardViews.size();
    }

    static Animation getHandAnimation(GameView gameView, ArrayList<CardView> cardViews, int index) {

        CardView cardView = cardViews.get(index);

        Animation tr = getTransitionForAddCardFromDeckToHand(gameView, cardView);

        tr.setOnFinished(actionEvent -> {
            if (index < cardViews.size() - 1) {
                gameView.setBooleanForShowActions(gameView.selfHand, false);
                getHandAnimation(gameView, cardViews, index + 1).play();
            } else {
                gameView.setBooleanForShowActions(gameView.selfHand, true);
            }
        });

        return tr;
    }


    static void addCardToRivalHandFromDeck(GameView gameView, Card card) {
        getTransitionForAddCardFromRivalDeckToRivalHand(gameView,
                new CardView(card, 8, true, true)).play();
    }

    static ParallelTransition getTransitionForAddCardFromRivalDeckToRivalHand(GameView gameView, CardView cardView) {

        cardView.setX(40);
        cardView.setY(92);
        gameView.gamePane.getChildren().add(cardView);
        CardView newCardView = gameView.getCardForRivalHand(cardView.getCard());
        gameView.rivalHand.add(newCardView);
        newCardView.setX(gameView.getCardInRivalHandX(newCardView));
        newCardView.setY(gameView.getCardinRivalHandY());
        newCardView.setVisible(false);
        gameView.gamePane.getChildren().add(newCardView);

        KeyFrame createNewCardAnimation = new KeyFrame(Duration.millis(500), actionEvent -> {
            newCardView.setVisible(true);
            gameView.gamePane.getChildren().remove(cardView);
        });

        Timeline timeline = new Timeline(createNewCardAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 500).getAnimation(),
                new TimelineTranslate(cardView, gameView.getCardInRivalHandX(newCardView) + 15,
                        gameView.getCardinRivalHandY() + 23, 500).getAnimation(), timeline

        );

        transitions.getChildren().add(getHandAnimationForCardsWasInRivalHand(gameView));

        return transitions;
    }

    static ParallelTransition getTransitionForAddCardFromDeckToHand
            (GameView gameView, CardView cardView) {

        cardView.setX(530);
        cardView.setY(425);
        gameView.gamePane.getChildren().add(cardView);

        CardView newCardView = gameView.getCardForHand(cardView.getCard());
        gameView.selfHand.add(newCardView);
        newCardView.setX(gameView.getCardInHandX(newCardView));
        newCardView.setY(gameView.getCardinHandY());
        newCardView.setVisible(false);
        gameView.gamePane.getChildren().add(newCardView);

        KeyFrame createNewCardAnimation = new KeyFrame(new Duration(500), actionEvent -> {
            newCardView.setVisible(true);
            gameView.gamePane.getChildren().remove(cardView);
        });

        KeyFrame notifyRivalAnimation = new KeyFrame(Duration.millis(1), actionEvent ->
                gameView.gameController.notifyOtherGameViewToDoSomething(gameView,
                        new graphicDataForServerToNotifyOtherClient
                                ("add card from deck to hand", cardView.card, -1))
        );

        Timeline createCardTimeline = new Timeline(createNewCardAnimation);

        Timeline notifyRivalTimeLine = new Timeline(notifyRivalAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 500).getAnimation(),
                new TimelineTranslate(cardView, gameView.getCardInHandX(newCardView) + 15,
                        gameView.getCardinHandY() + 23, 500).getAnimation(),
                new FlipAnimation(cardView, 500).getAnimation(), createCardTimeline, notifyRivalTimeLine
        );


        transitions.getChildren().add(getHandAnimationForCardsWasInHand(gameView));

        return transitions;
    }


    //hand animations

    //zone -> 0 for monsterZone and 1 for spellZone
    //mode -> 0 for summon monster and 1 for set monster and 2 for activate spell and 3 for set spell

    static double runMoveCardFromHandToFieldGraphic(GameView gameView,
                                                    CardView cardView, int mode, int zone, int index) {

        CardView newCardView = gameView.getCardViewForField(cardView.getCard(), mode);

        addCardToCorrectCardListZone(gameView, newCardView, zone, index);

        newCardView.setVisible(false);
        gameView.gamePane.getChildren().add(newCardView);
        newCardView.setX(gameView.getCardInFieldX(newCardView, mode));
        newCardView.setY(gameView.getCardInFieldY(mode));
        gameView.selfHand.remove(cardView);

        ParallelTransition transition =
                getTransitionForMovingCardFromHandToField(gameView, cardView, newCardView, mode, zone, index);
        transition.play();
        return 500;
    }

    static ParallelTransition getTransitionForMovingCardFromHandToField
            (GameView gameView, CardView cardView, CardView newCardView, int mode, int zone, int index) {

        Timeline addNewCardAnimation = new Timeline
                (new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gameView.gamePane.getChildren().remove(cardView);
                        newCardView.setVisible(true);
                    }
                }));
        ParallelTransition transitions;

        if (mode == 0 || mode == 2) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    addNewCardAnimation);
        } else if (mode == 1) {

            Timeline hideCardAnimation = new Timeline(new KeyFrame(new Duration(250),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            cardView.hideCard();
                        }
                    }));

            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 8,
                            newCardView.getY() - 38, 500).getAnimation(),
                    new RotateAnimation(cardView, 500, 90).getAnimation(),
                    addNewCardAnimation, hideCardAnimation);
        } else if (mode == 3) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    new FlipAnimation(cardView, 500).getAnimation(),
                    addNewCardAnimation);
        } else {
            transitions = new ParallelTransition();
        }

        transitions.getChildren().add(getHandAnimationForCardsWasInHand(gameView));

        transitions.play();
        return transitions;
    }

    static void addCardToCorrectCardListZone(GameView gameView, CardView cardView, int zone, int index) {
        if (zone == 0) {
            gameView.monsterZoneCards.set(index, cardView);
        } else if (zone == 1) {
            gameView.spellZoneCards.set(index, cardView);
        }
    }


    //rival
    static double runMoveRivalCardFromHandToFiledGraphic
    (GameView gameView, Card card, int mode, int zone, int index) {

        CardView cardView = gameView.searchCardInRivalHand(card);

        CardView newCardView = gameView.getCardViewForField(cardView.getCard(), mode);

        addNewCardViewToCorrectRivalZone(gameView, newCardView, zone, index);

        newCardView.setVisible(false);
        gameView.gamePane.getChildren().add(newCardView);
        newCardView.setX(gameView.getCardInRivalFieldX(newCardView, mode));
        newCardView.setY(gameView.getCardInRivalFieldY(mode));
        gameView.rivalHand.remove(cardView);

        getAnimationForMoveCardFromRivalHandToRivalField(gameView, cardView, newCardView, mode).play();
        return 500;
    }


    static Animation getAnimationForMoveCardFromRivalHandToRivalField
            (GameView gameView, CardView cardView, CardView newCardView, int mode) {

        Timeline addNewCardAnimation = new Timeline
                (new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gameView.gamePane.getChildren().remove(cardView);
                        newCardView.setVisible(true);
                    }
                }));


        ParallelTransition transitions;

        if (mode == 0 || mode == 2) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    new FlipAnimation(cardView, 500).getAnimation(),
                    addNewCardAnimation);
        } else if (mode == 1) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 8,
                            newCardView.getY() - 38, 500).getAnimation(),
                    new RotateAnimation(cardView, 500, 90).getAnimation(),
                    addNewCardAnimation);
        } else if (mode == 3) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    addNewCardAnimation);
        } else {
            transitions = new ParallelTransition();
        }

        transitions.getChildren().add(getHandAnimationForCardsWasInRivalHand(gameView));
        return transitions;
    }

    static void addNewCardViewToCorrectRivalZone
            (GameView gameView, CardView cardView, int zone, int index) {
        if (zone == 0) {
            gameView.rivalMonsterZoneCards.set(index, cardView);
        } else if (zone == 1) {
            gameView.rivalSpellZoneCards.set(index, cardView);
        }
    }


    static double runRemoveCardFromHandGraphic(GameView gameView, CardView cardView) {
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 1, 0).getAnimation());
        gameView.selfHand.remove(cardView);
        transition.getChildren().add(getHandAnimationForCardsWasInHand(gameView));
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameView.gamePane.getChildren().remove(cardView);
            }
        });
        transition.play();
        runRemoveCardFromRivalHandGraphic(gameView.rivalGameView, cardView.getCard());
        return 500;
    }

    static double runRemoveCardFromRivalHandGraphic(GameView gameView, Card card) {
        CardView cardView = gameView.searchCardInRivalHand(card);
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 1, 0).getAnimation());
        gameView.rivalHand.remove(cardView);
        transition.getChildren().add(getHandAnimationForCardsWasInRivalHand(gameView));
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameView.gamePane.getChildren().remove(cardView);
            }
        });
        transition.play();
        return 500;
    }

    static double runAddCardToHandGraphic(GameView gameView, Card card) {
        CardView cardView = gameView.getCardForHand(card);
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 0, 1).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gameView.gamePane.getChildren().add(cardView);
                    }
                })));

        gameView.selfHand.add(cardView);
        cardView.setX(gameView.getCardInHandX(cardView));
        cardView.setY(gameView.getCardinHandY());
        transition.getChildren().add(getHandAnimationForCardsWasInHand(gameView));
        transition.play();
        runAddCardToRivalHandGraphic(gameView.rivalGameView, card);
        return 500;
    }

    static double runAddCardToRivalHandGraphic(GameView gameView, Card card) {
        CardView cardView = gameView.getCardForRivalHand(card);
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 0, 1).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gameView.gamePane.getChildren().add(cardView);
                    }
                })));

        gameView.rivalHand.add(cardView);
        cardView.setX(gameView.getCardInRivalHandX(cardView));
        cardView.setY(gameView.getCardinRivalHandY());

        transition.getChildren().add(getHandAnimationForCardsWasInRivalHand(gameView));
        transition.play();
        return 500;
    }

    static ParallelTransition getHandAnimationForCardsWasInHand(GameView gameView) {
        ParallelTransition transition = new ParallelTransition();
        for (CardView cardView : gameView.selfHand) {
            transition.getChildren().add(new TimelineTranslate
                    (cardView, gameView.getCardInHandX(cardView), gameView.getCardinHandY(), 500)
                    .getAnimation());
        }
        return transition;
    }

    static ParallelTransition getHandAnimationForCardsWasInRivalHand(GameView gameView) {
        ParallelTransition transition = new ParallelTransition();
        for (CardView cardView : gameView.rivalHand) {
            transition.getChildren().add(new TimelineTranslate
                    (cardView, gameView.getCardInRivalHandX(cardView), gameView.getCardinRivalHandY(), 500)
                    .getAnimation());
        }
        return transition;
    }
    //field animations

    //zone -> 0 for monsterZone and 1 for spellZone
    //mode -> 0 for summon monster and 1 for set monster and 2 for set spell and 3 for activate spell
    //mode -> 4 for DO monster

    static double putCardIntoFiled(GameView gameView, Card card, int mode, int zone, int index) {
        CardView cardView = gameView.getCardViewForField(card, mode);
        addCardToCorrectCardListZone(gameView, cardView, zone, index);
        cardView.setX(gameView.getCardInFieldX(cardView, mode));
        cardView.setY(gameView.getCardInFieldY(mode));
        new FadeAnimation(cardView, 800, 0, 1).getAnimation().play();
        gameView.gamePane.getChildren().add(cardView);
        putCardIntoRivalFiled(gameView.rivalGameView, card, mode, zone, 4 - index);
        return 800;
    }

    static double putCardIntoRivalFiled(GameView gameView, Card card, int mode, int zone, int index) {
        CardView cardView = gameView.getCardViewForField(card, mode);
        addNewCardViewToCorrectRivalZone(gameView, cardView, zone, index);
        cardView.setX(gameView.getCardInRivalFieldX(cardView, mode));
        cardView.setY(gameView.getCardInRivalFieldY(mode));
        new FadeAnimation(cardView, 800, 0, 1).getAnimation().play();
        gameView.gamePane.getChildren().add(cardView);
        return 800;
    }

    //flip animations

    static double runFlipSummonGraphic(GameView gameView, CardView cardView) {

        CardView newCardView = gameView.getCardViewForField(cardView.getCard(), 0);
        gameView.monsterZoneCards.set(gameView.monsterZoneCards.indexOf(cardView), newCardView);
        newCardView.setX(gameView.getCardInFieldX(newCardView, 0));
        newCardView.setY(gameView.getCardInFieldY(0));
        newCardView.setVisible(false);
        gameView.gamePane.getChildren().add(newCardView);

        Timeline addNewCardTimeline = new Timeline(new KeyFrame(Duration.millis(400),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gameView.gamePane.getChildren().remove(cardView);
                        newCardView.setVisible(true);
                    }
                }));

        ParallelTransition transition = new ParallelTransition(
                new RotateAnimation(cardView, 400, -90).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        cardView.setCardImage();
                    }
                })),
                addNewCardTimeline);

        transition.play();
        return 400;
    }

    static double runRivalFlipSummonGraphic(GameView gameView, Card card) {

        CardView cardView = gameView.searchCardInRivalField(card);
        CardView newCardView = gameView.getCardViewForField(cardView.getCard(), 0);
        gameView.rivalMonsterZoneCards.set(gameView.rivalMonsterZoneCards.indexOf(cardView), newCardView);
        newCardView.setX(gameView.getCardInRivalFieldX(newCardView, 0));
        newCardView.setY(gameView.getCardInRivalFieldY(0));
        newCardView.setVisible(false);
        gameView.gamePane.getChildren().add(newCardView);

        Timeline addNewCardTimeline = new Timeline(new KeyFrame(Duration.millis(400),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gameView.gamePane.getChildren().remove(cardView);
                        newCardView.setVisible(true);
                    }
                }));

        ParallelTransition transition = new ParallelTransition(
                new RotateAnimation(cardView, 400, -90).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        cardView.setCardImage();
                    }
                })),
                addNewCardTimeline);

        transition.play();
        return 400;
    }

    static double runFlipCardGraphic(GameView gameView, CardView cardView) {
        new FlipAnimation(cardView, 500).getAnimation().play();
        return 500;
    }

    static double runFlipRivalCardGraphic(GameView gameView, Card card) {
        CardView cardView = gameView.searchCardInRivalField(card);
        new FlipAnimation(cardView, 500).getAnimation().play();
        return 500;
    }


    //lp animations

    static double runIncreaseLpGraphic(GameView gameView, double lp, boolean isSelf) {
        getIncreaseLpTransition(gameView, lp, isSelf).play();
        return lp;
    }

    static void increaseLpInLabel(GameView gameView, double lp) {
        gameView.selfLp += lp;
        gameView.selfLpLabel.setText(gameView.selfLp + "");
    }

    static double runIncreaseLpGraphicBecauseOfRivalNotification(GameView gameView, double lp, boolean isSelf) {
        getIncreaseLpTransition(gameView, lp, isSelf).play();
        return lp;
    }

    static void increaseRivalLpInLabel(GameView gameView, double lp) {
        gameView.rivalLp += lp;
        gameView.rivalLpLabel.setText(gameView.rivalLp + "");
    }

    static SequentialTransition getIncreaseLpTransition(GameView gameView, double lp, boolean isSelf) {

        SequentialTransition transition = new SequentialTransition();

        double increasingSize = 1 * Math.signum(lp);

        for (int i = 0; i < Math.ceil(lp / increasingSize); i++) {
            int finalI = i;
            transition.getChildren().add(new Timeline(new KeyFrame(Duration.millis(1),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            double increasingLp = increasingSize;
                            if (finalI >= Math.floor(lp / increasingSize)) {
                                increasingLp = lp % increasingSize;
                            }
                            if (isSelf) {
                                increaseLpInLabel(gameView, increasingLp);
                            } else {
                                increaseRivalLpInLabel(gameView, increasingLp);
                            }
                        }
                    })));
        }
        return transition;
    }
}
