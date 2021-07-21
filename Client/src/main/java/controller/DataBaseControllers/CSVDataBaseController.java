package controller.DataBaseControllers;

import model.Card.Card;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;
import model.Pair;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CSVDataBaseController {

    public static Card getMonsterCardByName(String searchingName) throws IOException
            , NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BufferedReader csvReader = new BufferedReader(new FileReader("Resource/Cards/Monster.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            if (row.startsWith("Name")) {
                continue;
            }
            if (row.startsWith("Wattaildragon")) {
                row = row + "\n" + csvReader.readLine();
            }
            row = row.replaceAll("\"", "");
            String[] data = row.split(",");
            int temp = 0;
            int level;
            int cnt = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    level = Integer.parseInt(data[temp]);
                    break;
                } catch (NumberFormatException e) {
                    if (cnt >= 1) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(data[temp]);
                    temp++;
                    cnt++;
                }
            }
            String name = stringBuilder.toString().trim().replaceAll("\\s+", " ");
            if (searchingName.equalsIgnoreCase(name)) {
                //change

                stringBuilder = new StringBuilder();
                String description;
                int price;
                cnt = 0;
                while (true) {
                    try {
                        price = Integer.parseInt(data[temp + 6]);
                        break;
                    } catch (NumberFormatException e) {
                        if (cnt >= 1) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(data[temp + 6]);
                        temp++;
                        cnt++;
                    }
                }
                description = stringBuilder.toString().trim();

                return new Card(name, description, price);
            }


        }
        return null;
    }

    public static Card getSpellOrTrapCardByName(String searchingName) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("Resource/Cards/SpellTrap.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            if (row.startsWith("Name")) {
                continue;
            }
            if (row.startsWith("Magnum Shield")) {
                row = row + '\n' + csvReader.readLine();
                row = row + '\n' + csvReader.readLine();
            }
            row = row.replaceAll("\"", "");
            String[] data = row.split(",");
            String name = data[0].trim().replaceAll("\\s+", " ");
            if (searchingName.equalsIgnoreCase(name)) {
                //change
                String description;
                int price;
                int temp = 0;
                String current = data[3].trim();
                StringBuilder stringBuilder = new StringBuilder();
                int cnt = 0;
                while (!(current.equals("Unlimited") || current.equals("Limited"))) {
                    if (cnt >= 1) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(data[temp + 3]);
                    temp++;
                    cnt++;
                    current = data[temp + 3];
                }
                description = stringBuilder.toString().trim();
                price = Integer.parseInt(data[4 + temp]);

                return new Card(name, description, price);
            }

        }
        return null;
    }

    public static Card getCardByCardName(String name) {
        try {
            Card card = getMonsterCardByName(name);
            if (card == null) {
                card = getSpellOrTrapCardByName(name);
            }
            return card;
        } catch (Exception e) {
            return null;
        }
    }

}
