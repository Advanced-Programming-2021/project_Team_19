package view.graphic;

import controller.DataForGameRun;
import controller.DuelControllers.Game;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import model.Card.Card;
import model.Enums.CardFamily;
import model.Gamer;

import java.util.ArrayList;

public class CardView extends Rectangle {

    Card card;
    String cardName;
    boolean isMonster;
    public boolean isHidden = false;
    public boolean isVertical = true;
    public static double height = 614;
    public static double width = 423;
    public double sizeInverse;
    private ArrayList<String> validActionNamesForShow;
    private ArrayList<String> validActionNames;
    private int validActionIndex = 0;
    public boolean canShowValidActions = true;
    public Popup tempPopup;
    public EventHandler filter;
    public boolean hasBeenClicked;

    public CardView(double sizeInverse) {
        super(width / sizeInverse, height / sizeInverse);
        this.sizeInverse = sizeInverse;
        hideCard();
    }

    public CardView(Card card, double sizeInverse, boolean isHidden, boolean isVertical) {

        setWidth(isVertical ? width / sizeInverse : height / sizeInverse);
        setHeight(isVertical ? height / sizeInverse : width / sizeInverse);

        this.sizeInverse = sizeInverse;
        this.card = card;
        this.isHidden = isHidden;
        this.isVertical = isVertical;
        cardName = card.getName();
        isMonster = card.getCardFamily().equals(CardFamily.MONSTER);

        if (isHidden) {
            setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        } else {
            setCardImage();
        }
    }

    public Image getImage() {

        if (cardName != null) {
            String model = isMonster ? "Monsters/" : "SpellTrap/";
            return new Image("/Assets/Cards/" + model +
                    controller.Utils.getPascalCase(cardName) + ".jpg");

        }
        return new Image("/Assets/Cards/Monsters/Unknown.jpg");
    }

    public void setCanShowValidActions(boolean canShowValidActions) {
        this.canShowValidActions = canShowValidActions;
    }

    public void hideCard() {
        setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        isHidden = true;
    }

    public void setCardImage() {
        if (isVertical) {
            ImagePattern imagePattern = new ImagePattern(getImage());
            setFill(imagePattern);
        } else {
            setCardImage2();
        }
        isHidden = false;
    }

    private void setCardImage2() {
        ImageView imageView = new ImageView(getImage());
        imageView.setRotate(90);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Image rotatedImage = imageView.snapshot(params, null);

        setFill(new ImagePattern(rotatedImage));
    }

    public Card getCard() {
        return card;
    }

    public String getFirstValidAction(Game game, Gamer gamer) throws Exception {

        validActionNamesForShow = new ArrayList<>();
        validActionNames = game.getValidCommandsForCard(new DataForGameRun(card, gamer));

        for (String validAction : validActionNames) {
            if (validAction.equals("summon with sacrifice")) {
                validActionNamesForShow.add("summon with sacrifice");
            } else if (validAction.startsWith("summon")) {
                validActionNamesForShow.add("summon");
            } else if (validAction.startsWith("set with sacrifice")) {
                validActionNamesForShow.add("set with sacrifice");
            } else if (validAction.equals("attack")) {
                validActionNamesForShow.add("attack");
            } else if (validAction.equals("attack monster")) {
                validActionNamesForShow.add("attack monster");
            } else if (validAction.equals("attack direct")) {
                validActionNamesForShow.add("attack direct");
            } else if (validAction.matches("set position (attack|defence)")) {
                validActionNamesForShow.add(validAction);
            } else if (validAction.equals("set")) {
                validActionNamesForShow.add("set");
            } else if (validAction.equals("activate spell normally")) {
                validActionNamesForShow.add("activate spell");
            } else if (validAction.equals("activate effect monster")) {
                validActionNamesForShow.add("activate effect");
            } else if (validAction.equals("select")) {
                validActionNamesForShow.add("select");
            } else if (validAction.equals("flip summon")) {
                validActionNamesForShow.add("flip summon");
            } else if (validAction.equals("sacrifice")) {
                validActionNamesForShow.add("sacrifice");
            } else if (validAction.equals("activate trap")){
                validActionNamesForShow.add("activate trap");
            }
            else {
                validActionNamesForShow.add(validAction);
            }
        }

        validActionIndex = 0;

        throwExceptionIfHasNoValidAction();

        return validActionNamesForShow.get(0);
    }

    public String getNextValidAction() throws Exception {
        throwExceptionIfHasNoValidAction();

        return validActionNamesForShow.get
                (++validActionIndex % validActionNamesForShow.size());
    }


    public String getCurrentAction() {
        return validActionNamesForShow.get(validActionIndex % validActionNamesForShow.size());
    }

    public void throwExceptionIfHasNoValidAction() throws Exception {
        if (validActionNamesForShow.size() == 0) {
            throw new Exception("no valid actions");
        }
    }

    public void setShowCardAndShowValidActions(GameView gameView) {

        setOnMouseEntered(mouseEvent -> {
            hasBeenClicked = false;
            gameView.game.gameData.setSelectedCard(card);
            gameView.showCard(this);

            if (this.canShowValidActions) {
                try {
                    gameView.showValidActionForCard(getFirstValidAction(gameView.game, gameView.self), this);
                } catch (Exception ignored) {
                }
            }
        });

        setOnMouseExited(mouseEvent -> {

            Popup popup = tempPopup;
            if (popup != null) {
                popup.hide();
            }
            if (filter != null) {
                gameView.stage.getScene().removeEventFilter(MouseEvent.MOUSE_MOVED, filter);
            }
        });

        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                try {
                    gameView.showValidActionForCard(getNextValidAction(), this);
                } catch (Exception ignored) {
                }
            }else {
                try {
                    throwExceptionIfHasNoValidAction();
                    gameView.cardOnLeftClick(this);
                } catch (Exception ignored) {
                }

            }
        });
    }
}

