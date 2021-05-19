package controller.DataBaseControllers;

import model.Card.Card;

import java.io.File;

public class CardDataBaseController extends DataBaseController {

    public static String getCardFilePathByCardName(String cardName) {
        return DataBaseController.getCardsPath() + "\\" + getCardNameStr(cardName) + ".json";
    }

    public static String getCardNameStr(String cardName){

        String ans;
        ans = cardName.toString();

        ans = ans.replaceAll("___", ", ");
        ans = ans.replaceAll("__", "-");
        ans = ans.replaceAll("_", " ");

        return ans;
    }

    public static Card getCardObjectByCardName(String cardName){

        if(cardName == null){
            return null;
        }

        return (Card) getObjectByGsonFile(getCardFilePathByCardName(cardName),
                getClassByClassName(cardName));
    }

    public static String getCardNamesAndPrices(){
        return CSVDataBaseController.getAllCardPrices();
    }


}
