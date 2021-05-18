package model.Enums;

import model.Card.Spells.*;
import model.Card.Traps.*;
import org.testng.internal.Yaml;

import java.util.HashMap;

public enum CardNames {


//    {
//        HashMap <String , Class> hehe = new HashMap<>();
//        hehe.put("Trap Hole", TrapHole.class);
//        hehe.put("Mirror Force", MirrorForce.class);
//        hehe.put("Magic Cylinder", MagicCylinder.class);
//        hehe.put("Mind Crush", MindCrush.class);
//        hehe.put("Torrential Tribute", TorrentialTribute.class);
//        hehe.put("Time Seal", TimeSeal.class);
//        hehe.put("Negate attack", NegateAttack.class);
//        hehe.put("Solemn Warning", SolemnWarning.class);
//        hehe.put("Magic Jammer", MagicJammer.class);
//        hehe.put("Call Of The Haunted", CallOfTheHaunted.class);
////        hehe.put("Vanitys Emptiness", )
////        hehe.put("Wall Of Revealing Light", )
//        hehe.put("Monster Reborn", MonsterReborn.class);
//        hehe.put("Terraforming", Terraforming.class);
//        hehe.put("Pof Of Greed", PotOfGreed.class);
//        hehe.put("Raigeki", Raigeki.class);
//        hehe.put("Change Of Heart", ChangeOfHeart.class);
//        hehe.put("Swords Of Revealing Light", SwordsOfRevealingLight.class);
//        hehe.put("Harpies Feather Duster", HarpiesFeatherDuster.class);
//        hehe.put("Dark Hole", DarkHole.class);
//        hehe.put("Supply Squad", SupplySquad.class);
//        hehe.put("Spell Absorption", SpellAbsorption.class);
//        hehe.put("Messenger Of Peace", MessangerOfPeace.class);
//        hehe.put("Twin Twisters", TwinTwisters.class);
//        hehe.put("Mystical Speace Typhoon", MysticalSpaceTyphoon.class);
//        hehe.put("Ring Of Deffence", RingOfDefence.class);
//        hehe.put("Yami", FieldSpell.class);
//        hehe.put("Forest", FieldSpell.class);
//        hehe.put("Closed Forest", ClosedForest.class);
//        hehe.put("UmiiRuka", FieldSpell.class);
//        hehe.put("Sword Of Dark Destruction", SwordOfDarkDestruction.class);
//        hehe.put("Black Pendant", BlackPendant.class);
//        hehe.put("United We Stand", UnitedWeStand.class);
//        hehe.put("Magnum Shield", MagnumShield.class);
//
//    }

    TIME_SEAL("model.Card.Traps.TimeSeal"),

    ADVANCED_RITUAL_ART("model.Card.Monsters.AdvancedRitualArt"),
    BATTLE_OX("model.Card.Monsters.NormalMonster"),
    AXE_RAIDER("model.Card.Monsters.NormalMonster"),
    YOMI_SHIP("model.Card.Monsters.YomiShip"),
    HORN_IMP("model.Card.Monsters.NormalMonster"),
    SILVER_FANG("model.Card.Monsters.NormalMonster"),
    SUIJIN("model.Card.Monsters.Suijin"),
    FIREYAROU("model.Card.Monsters.NormalMonster"),
    CURTAIN_OF_THE_DARK_ONES("model.Card.Monsters.NormalMonster"),
    FERAL_IMP("model.Card.Monsters.NormalMonster"),
    DARK_MAGICIAN("model.Card.Monsters.NormalMonster"),
    WATTKID("model.Card.Monsters.NormalMonster"),
    BABY_DRAGON("model.Card.Monsters.NormalMonster"),
    HERO_OF_THE_EAST("model.Card.Monsters.NormalMonster"),
    BATTLE_WARRIOR("model.Card.Monsters.NormalMonster"),
    CRAWLING_DRAGON("model.Card.Monsters.NormalMonster"),
    FLAME_MANIPULATOR("model.Card.Monsters.NormalMonster"),
    BLUE__EYES_WHITE_DRAGON("model.Card.Monsters.NormalMonster"),
    CRAB_TURTLE("model.Card.Monsters.RitualMonster"),
    SKULL_GUARDIAN("model.Card.Monsters.RitualMonster"),
    SLOT_MACHINE("model.Card.Monsters.NormalMonster"),
    HANIWA("model.Card.Monsters.NormalMonster"),
    MAN__EATER_BUG("model.Card.Monsters.ManEaterBug"),
    GATE_GUARDIAN("model.Card.Monsters.GateGuardian"),
    SCANNERMonster("model.Card.Monsters.ScannerMonster"),
    BITRON("model.Card.Monsters.NormalMonster"),
    MARSHMALLON("model.Card.Monsters.Marshmallon"),
    BEAST_KING_BARBAROS("model.Card.Monsters.BeastKingBarbaros"),
    TEXCHANGER("model.Card.Monsters.Texchanger"),
    LEOTRON("model.Card.Monsters.NormalMonster"),
    THE_CALCULATOR("model.Card.Monsters.TheCalculator"),
    ALEXANDRITE_DRAGON("model.Card.Monsters.NormalMonster"),
    MIRAGE_DRAGON("model.Card.Monsters.MirageDragon"),
    HERALD_OF_CREATION("model.Card.Monsters.HeraldOfCreation"),
    EXPLODER_DRAGON("model.Card.Monsters.ExploderDragon"),
    WARRIOR_DAI_GREPHER("model.Card.Monsters.NormalMonster"),
    DARK_BLADE("model.Card.Monsters.NormalMonster"),
    WATTAILDRAGON("model.Card.Monsters.NormalMonster"),
    TERRATIGER___THE_EMPOWERED_WARRIOR("model.Card.Monsters.TerratigerTheEmpoweredWarrior"),
    THE_TRICKY("model.Card.Monsters.TheTricky"),
    SPIRAL_SERPENT("model.Card.Monsters.NormalMonster");

    String className;

    CardNames(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

}
