package controller.DataBaseControllers;

import model.Card.Card;
import model.Card.Monsters.*;
import model.Card.Spells.*;
import model.Card.Traps.*;
import model.Enums.Icon;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Enums.Status;
import model.Enums.Type;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class CSVDataBaseController {
    static TreeMap<String,Class> getClassByName = new TreeMap<>();
    static {
        getClassByName.put("Battle OX",NormalMonster.class);
        getClassByName.put("Axe Raider",NormalMonster.class);
        getClassByName.put("Yomi Ship", YomiShip.class);
        getClassByName.put("Horn Imp",NormalMonster.class);
        getClassByName.put("Silver Fang",NormalMonster.class);
        getClassByName.put("Suijin", Suijin.class);
        getClassByName.put("Fireyarou",NormalMonster.class);
        getClassByName.put("Curtain of the dark ones",NormalMonster.class);
        getClassByName.put("Feral Imp",NormalMonster.class);
        getClassByName.put("Dark magician",NormalMonster.class);
        getClassByName.put("Wattkid",NormalMonster.class);
        getClassByName.put("Baby dragon",NormalMonster.class);
        getClassByName.put("Hero of the east",NormalMonster.class);
        getClassByName.put("Battle warrior",NormalMonster.class);
        getClassByName.put("Crawling dragon",NormalMonster.class);
        getClassByName.put("Flame manipulator",NormalMonster.class);
        getClassByName.put("Blue-Eyes white dragon",NormalMonster.class);
        getClassByName.put("Crab Turtle", RitualMonster.class);
        getClassByName.put("Skull Guardian",RitualMonster.class);
        getClassByName.put("Slot Machine",NormalMonster.class);
        getClassByName.put("Haniwa",NormalMonster.class);
        getClassByName.put("Man-Eater Bug", ManEaterBug.class);
        getClassByName.put("Gate Guardian",GateGuardian.class);
        getClassByName.put("Scanner", ScannerMonster.class);
        getClassByName.put("Bitron",NormalMonster.class);
        getClassByName.put("Marshmallon",Marshmallon.class);
        getClassByName.put("Beast King Barbaros",BeastKingBarbaros.class);
        getClassByName.put("Texchanger",Texchanger.class);
        getClassByName.put("Leotron",NormalMonster.class);
        getClassByName.put("The Calculator",TheCalculator.class);
        getClassByName.put("Alexandrite Dragon",NormalMonster.class);
        getClassByName.put("Mirage Dragon",MirageDragon.class);
        getClassByName.put("Herald of Creation",HeraldOfCreation.class);
        getClassByName.put("Exploder Dragon",ExploderDragon.class);
        getClassByName.put("Warrior Dai Grepher",NormalMonster.class);
        getClassByName.put("Dark Blade",NormalMonster.class);
        getClassByName.put("Wattaildragon",NormalMonster.class);
        getClassByName.put("Terratiger, the Empowered Warrior",TerratigerTheEmpoweredWarrior.class);
        getClassByName.put("The Tricky",TheTricky.class);
        getClassByName.put("Spiral Serpent",NormalMonster.class);
        getClassByName.put("Command Knight",NormalMonster.class);
        getClassByName.put("Trap Hole", TrapHole.class);
        getClassByName.put("Mirror Force", MirrorForce.class);
        getClassByName.put("Magic Cylinder", MagicCylinder.class);
        getClassByName.put("Mind Crush", MindCrush.class);
        getClassByName.put("Torrential Tribute", TorrentialTribute.class);
        getClassByName.put("Time Seal", TimeSeal.class);
        getClassByName.put("Negate Attack", NegateAttack.class);
        getClassByName.put("Solemn Warning", SolemnWarning.class);
        getClassByName.put("Magic Jamamer", MagicJammer.class);
        getClassByName.put("Call of The Haunted", CallOfTheHaunted.class);
//        getClassByName.put("Vanitys Emptiness", )
//        getClassByName.put("Wall Of Revealing Light", )
        getClassByName.put("Monster Reborn", MonsterReborn.class);
        getClassByName.put("Terraforming", Terraforming.class);
        getClassByName.put("Pot of Greed", PotOfGreed.class);
        getClassByName.put("Raigeki", Raigeki.class);
        getClassByName.put("Change of Heart", ChangeOfHeart.class);
        getClassByName.put("Swords of Revealing Light", SwordsOfRevealingLight.class);
        getClassByName.put("Harpie's Feather Duster", HarpiesFeatherDuster.class);
        getClassByName.put("Dark Hole", DarkHole.class);
        getClassByName.put("Supply Squad", SupplySquad.class);
        getClassByName.put("Spell Absorption", SpellAbsorption.class);
        getClassByName.put("Messenger of peace", MessangerOfPeace.class);
        getClassByName.put("Twin Twisters", TwinTwisters.class);
        getClassByName.put("Mystical space typhoon", MysticalSpaceTyphoon.class);
        getClassByName.put("Ring of defense", RingOfDefence.class);
        getClassByName.put("Yami", FieldSpell.class);
        getClassByName.put("Forest", FieldSpell.class);
        getClassByName.put("Closed Forest", ClosedForest.class);
        getClassByName.put("Umiiruka", FieldSpell.class);
        getClassByName.put("Sword of dark destruction", SwordOfDarkDestruction.class);
        getClassByName.put("Black Pendant", BlackPendant.class);
        getClassByName.put("United We Stand", UnitedWeStand.class);
        getClassByName.put("Magnum Shield", MagnumShield.class);
        getClassByName.put("Advanced Ritual Art", AdvancedRitualArt.class);
    }
    public static Card getMonsterCardByName(String searchingName) throws IOException
            , NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BufferedReader csvReader = new BufferedReader(new FileReader("Resource/Cards/Monster.csv"));
        String row;
        while((row = csvReader.readLine()) != null){
            if(row.startsWith("Name")){
                continue;
            }
            if(row.startsWith("Wattaildragon")){
                row = row + "\n" + csvReader.readLine();
            }
            row = row.replaceAll("\"","");
            String[] data = row.split(",");
            int temp=0;
            int level;
            int cnt=0;
            StringBuilder stringBuilder = new StringBuilder();
            while(true){
                try{
                    level = Integer.parseInt(data[temp]);
                    break;
                }catch(NumberFormatException e){
                    if(cnt>=1){
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(data[temp]);
                    temp++;
                    cnt++;
                }
            }
            String name = stringBuilder.toString().trim().replaceAll("\\s+"," ");
            if(searchingName.equalsIgnoreCase(name)) {
                //change
                Attribute attribute = Attribute.valueOf(data[temp+1].trim().toUpperCase());
                MonsterType monsterType = MonsterType.valueOf(data[temp+2].toUpperCase().replaceAll("-", "_").replaceAll(" ","_"));
                MonsterTypesForEffects monsterTypesForEffects = MonsterTypesForEffects.valueOf(data[temp+3].toUpperCase().replaceAll("-", "_"));
                int attack = Integer.parseInt(data[temp+4].trim());
                int defense = Integer.parseInt(data[temp+5].trim());
                stringBuilder = new StringBuilder();
                String description;
                int price ;
                cnt=0;
                while(true){
                    try{
                        price = Integer.parseInt(data[temp+6]);
                        break;
                    }catch(NumberFormatException e){
                        if(cnt>=1){
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(data[temp+6]);
                        temp++;
                        cnt++;
                    }
                }
                description = stringBuilder.toString().trim();

                Class<?> clazz = getClassByName.get(name);
                Constructor<?> ctor = clazz.getConstructor(String.class,String.class,int.class,
                        int.class,int.class,int.class,Attribute.class,MonsterType.class, MonsterTypesForEffects.class);

                return (Card)ctor.newInstance
                        (name,description,price,attack,defense,level,attribute,monsterType, monsterTypesForEffects );
            }


        }
        return null;
    }

    public static Card getSpellOrTrapCardByName(String searchingName) throws IOException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BufferedReader csvReader = new BufferedReader(new FileReader("Resource/Cards/SpellTrap.csv"));
        String row;
        while((row = csvReader.readLine()) != null){
            if(row.startsWith("Name")){
                continue;
            }
            if(row.startsWith("Magnum Shield")){
                row = row +'\n'+ csvReader.readLine();
                row = row +'\n'+ csvReader.readLine();
            }
            row = row.replaceAll("\"","");
            String[] data = row.split(",");
            String name = data[0].trim().replaceAll("\\s+"," ");
            if(searchingName.equalsIgnoreCase(name)) {
                //change
                Type type = Type.valueOf(data[1].trim().toUpperCase());
                Icon icon = Icon.valueOf(data[2].trim().toUpperCase().replaceAll("-","_"));
                String description;
                Status status;
                int price;
                int temp=0;
                String current = data[3].trim();
                StringBuilder stringBuilder = new StringBuilder();
                int cnt=0;
                while(!(current.equals("Unlimited")||current.equals("Limited"))){
                    if(cnt>=1){
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(data[temp+3]);
                    temp++;
                    cnt++;
                    current = data[temp+3];
                }
                description = stringBuilder.toString().trim();
                status = Status.valueOf(current.trim().toUpperCase().replaceAll("-","_"));
                price = Integer.parseInt(data[4+temp]);
                Class<?> clazz = getClassByName.get(name);
                Constructor<?> constructor = clazz.getConstructor(String.class,String.class,int.class, Type.class,Icon.class,Status.class);
                return (Card)constructor.newInstance(name,description,price,type,icon,status);
            }


        }
        return null;
    }

    public static Card getCardByCardName(String name){
        try {
            Card card = getMonsterCardByName(name);
            if (card == null) {
                card = getSpellOrTrapCardByName(name);
            }
            return card;
        }catch(Exception e){
            return null;
        }
    }

    public static Class getClassByName(String cardName){
        return getClassByName.get(cardName);
    }

    public static String getAllCardPrices(){
        StringBuilder returnedData = new StringBuilder();
        for(String cardName : getClassByName.keySet()){
            Card card = getCardByCardName(cardName);
            returnedData.append(card.getName()).append(":").append(card.getPrice()).append("\n");
        }
        return returnedData.substring(0,returnedData.length()-1);
    }

}
