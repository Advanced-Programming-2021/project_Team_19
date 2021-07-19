package controller.DataBaseControllers;

import model.Card.*;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
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
    public static TreeMap<String, Class> getClassByName = new TreeMap<>();

    private static final HashMap<String, Pair<Integer, Boolean>> cardState = new HashMap<>();

    public static String addCard(Card card, String cardName, int attack, int defense, int level,  String description) {
        if (getClassByName.containsKey(cardName)) {
            return "A card with this name already exists!";
        }
        else {
            Class clazz = getClassByName(card.getName());
            getClassByName.put(cardName, clazz);
            try {
                if (card instanceof Monster) {
                    Monster monster = (Monster) card;
                    FileWriter csvWriter = new FileWriter("Resource/Cards/Monster.csv", true);
                    csvWriter.append("\n");
                    csvWriter.append(cardName);
                    csvWriter.append(",");
                    csvWriter.append(Integer.toString(level));
                    csvWriter.append(",");
                    csvWriter.append(monster.getAttribute().toString());
                    csvWriter.append(",");
                    csvWriter.append(monster.getMonsterType().toString());
                    csvWriter.append(",");
                    csvWriter.append(monster.getEffectType().toString());
                    csvWriter.append(",");
                    csvWriter.append(Integer.toString(attack));
                    csvWriter.append(",");
                    csvWriter.append(Integer.toString(defense));
                    csvWriter.append(",");
                    csvWriter.append(description);
                    csvWriter.append(",");
                    csvWriter.append(Integer.toString(card.getPrice()));
                    csvWriter.flush();
                    csvWriter.close();
                } else {
                    SpellAndTraps spellAndTrap = (SpellAndTraps) card;
                    FileWriter csvWriter = new FileWriter("Resource/Cards/SpellTrap.csv", true);
                    csvWriter.append("\n");
                    csvWriter.append(cardName);
                    csvWriter.append(",");
                    csvWriter.append(card instanceof Trap ? "Trap" : "Spell");
                    csvWriter.append(",");
                    if (card instanceof Trap) {
                        csvWriter.append(((Trap)card).getTrapType().toString());
                    } else {
                        csvWriter.append(((Spell)card).getSpellType().toString());
                    }
                    csvWriter.append(",");
                    csvWriter.append(description);
                    csvWriter.append(",");
                    csvWriter.append(spellAndTrap.getStatus().toString());
                    csvWriter.append(",");
                    csvWriter.append(Integer.toString(spellAndTrap.getPrice()));
                }
                FileWriter fileWriter = new FileWriter("Resource/Cards/getClassByName.txt", true);
                fileWriter.append(cardName).append(":").append(clazz.toString().substring(6)).append("\n");
                fileWriter.flush();
                fileWriter.close();
                return "Successful!";
            } catch (IOException e) {
                e.printStackTrace();
                return "Something bad happened!";
            }
        }
    }

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
                Attribute attribute = Attribute.valueOf(data[temp + 1].trim().toUpperCase());
                MonsterType monsterType = MonsterType.valueOf(data[temp + 2].toUpperCase().replaceAll("-", "_").replaceAll(" ", "_"));
                MonsterTypesForEffects monsterTypesForEffects = MonsterTypesForEffects.valueOf(data[temp + 3].toUpperCase().replaceAll("-", "_"));
                int attack = Integer.parseInt(data[temp + 4].trim());
                int defense = Integer.parseInt(data[temp + 5].trim());
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

                Class<?> clazz = getClassByName.get(name);
                Constructor<?> ctor = clazz.getConstructor(String.class, String.class, int.class,
                        int.class, int.class, int.class, Attribute.class, MonsterType.class, MonsterTypesForEffects.class);

                return (Card) ctor.newInstance
                        (name, description, price, attack, defense, level, attribute, monsterType, monsterTypesForEffects);
            }


        }
        return null;
    }

    public static Card getSpellOrTrapCardByName(String searchingName) throws IOException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
                Type type = Type.valueOf(data[1].trim().toUpperCase());
                String description;
                Status status;
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
                status = Status.valueOf(current.trim().toUpperCase().replaceAll("-", "_"));
                price = Integer.parseInt(data[4 + temp]);
                Class<?> clazz = getClassByName.get(name);
                if (type.equals(Type.SPELL)) {
                    SpellTypes spellType = SpellTypes.valueOf(data[2].trim().toUpperCase().replaceAll("-", "_"));
                    Constructor<?> constructor = clazz.getConstructor(String.class, String.class, int.class, Type.class, SpellTypes.class, Status.class);
                    return (Card) constructor.newInstance(name, description, price, type, spellType, status);
                } else {
                    TrapTypes trapType = TrapTypes.valueOf(data[2].trim().toUpperCase().replaceAll("-", "_"));
                    Constructor<?> constructor = clazz.getConstructor(String.class, String.class, int.class, Type.class, TrapTypes.class, Status.class);
                    return (Card) constructor.newInstance(name, description, price, type, trapType, status);
                }
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

    public static Class getClassByName(String cardName) {
        return getClassByName.get(cardName);
    }



    public static String getAllCardPrices() {
        StringBuilder returnedData = new StringBuilder();
        for (String cardName : getClassByName.keySet()) {
            Card card = getCardByCardName(cardName);
            try {
                returnedData.append(card.getName()).append(":").append(card.getPrice()).append("\n");
            } catch (NullPointerException e){
                System.out.println(cardName + "_______________________________________________________");
            }
        }
        return returnedData.substring(0, returnedData.length() - 1);
    }

    public static void load() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Resource/Cards/getClassByName.txt"));
            String row;
            while ((row = bufferedReader.readLine()) != null) {
                String[] temp = row.split(":");
                String name = temp[0];
                String clazz = temp[1];
                getClassByName.put(name, Class.forName(clazz));
            }
            bufferedReader.close();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setNumberOfCard() {
        File file = new File("Resource/Cards/getCardCountsAndRules.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file, false);
                for (String cardName : CSVDataBaseController.getClassByName.keySet()) {
                    fileWriter.append(cardName).append(",").append(Integer.toString(100))
                            .append(",").append("false").append("\n");
                }
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String row;
            while ((row = bufferedReader.readLine()) != null) {
                String[] content = row.split(",");
                cardState.put(content[0], new Pair<>(Integer.parseInt(content[1]), Boolean.parseBoolean(content[2])));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void updateCardData() {
        File file = new File("Resource/Cards/getCardCountsAndRules.txt");
        try {
            cardState.clear();
            FileWriter fileWriter = new FileWriter(file, false);
            for (Map.Entry<String, Pair<Integer, Boolean>> entry : cardState.entrySet()) {
                fileWriter.append(entry.getKey()).append(",").append(Integer.toString(entry.getValue().getFirst()))
                        .append(",").append(entry.getValue().getSecond().toString()).append("\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean setCardState(String cardName) {
        return cardState.get(cardName).getSecond();
    }

    public static int getCardNumber(String cardName) {
        return cardState.get(cardName).getFirst();
    }

    public static void setCardState(String cardName, boolean state) {
        Pair<Integer, Boolean> status = cardState.get(cardName);
        status.setSecond(state);
        //just for safety
        cardState.put(cardName, status);
    }

    public static void increaseCardCount(String cardName, int add) {
        Pair<Integer, Boolean> status = cardState.get(cardName);
        status.setFirst(status.getFirst() + add);
        //just for safety
        cardState.put(cardName, status);
        updateCardData();
    }

    public static void main(String[] arg) {
        load();
    }

}
