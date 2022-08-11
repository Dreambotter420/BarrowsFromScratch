package script.skills.ranged;

import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.horrorfromthedeep.HorrorFromTheDeep;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.Mobs.Mob;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Trains prayer 1-45
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainRanged extends Leaf {
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_RANGE;
	}
	public static final int jugOfWine = 1993;
	public static final int jug = 1935;
	public static final int seaTurtle = 397;
	public static final int rangePot4 = 2444;
	public static final int rangePot3 = 169;
	public static final int rangePot2 = 171;
	public static final int rangePot1 = 173;
	public static List<Integer> rangedPots = new ArrayList<Integer>();
	public static boolean started = false;
	public static List<Integer> randCapes = new ArrayList<Integer>();
    public void onStart() {
        Main.customPaintText1 = "~~~ Training Ranged ~~~";
        started = true;
    }
    public static void initialize()
    {
        int tmp = (int) Calculations.nextGaussianRandom(10, 5);
        if(tmp > 15) randCape = darkRedCape;
        if(tmp > 12) randCape = lightRedCape;
        if(tmp > 10) randCape = yellowCape;
        if(tmp > 8) randCape = lightGreenCape;
        if(randCape == 0) randCape = lightBlueCape;

        
        randCapes.add(darkRedCape);
        randCapes.add(lightRedCape);
        randCapes.add(yellowCape);
        randCapes.add(lightGreenCape);
        randCapes.add(lightBlueCape);
        rangedPots.add(rangePot1);
        rangedPots.add(rangePot2);
        rangedPots.add(rangePot3);
        rangedPots.add(rangePot4);
    }
    public boolean onExit() {
        Main.clearCustomPaintText();
        if(!Walkz.exitGiantsCave()) return false;
        if(Locations.isInIsleOfSouls())
		{
			if(!Walkz.useJewelry(InvEquip.glory, "Edgeville") && 
					!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") && 
					!Walkz.useJewelry(InvEquip.combat, "Ranging Guild"))
			{
				MethodProvider.log("Appear to be stuck on isle of souls, teleporting home??");
				if(Players.localPlayer().isAnimating()) 
				{
					Sleep.sleep(2222, 2222);
					return false;
				}
				Magic.castSpell(Normal.HOME_TELEPORT);
				Sleep.sleep(3333,3333);
			}
			return false;
		}
        Mobs.mob = null;
    	return true;
    }

    @Override
    public int onLoop() {
        if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Ranged!");
    		if(onExit())
    		{
    			API.mode = null;
    		}
    		return Timing.sleepLogNormalSleep();
    	}
    	if(Skills.getRealLevel(Skill.RANGED) >= 75) {
            MethodProvider.log("[COMPLETE] -> lvl 75 ranged!");
            if(onExit())
    		{
                API.mode = null;
    		}
            return Timing.sleepLogNormalSleep();
        }
    	if(!started) 
    	{
    		onStart();
    		return Timing.sleepLogNormalSleep();
    	}
    	
    	if(Mobs.mob == null) Mobs.chooseMob(false);
        return Mobs.trainMob(false);
    }
    
    
   
    public static boolean fulfillRangedDarts()
    {
    	InvEquip.clearAll();

    	setBestRangedEquipment();
    	
    	InvEquip.addInvyItem(rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(jugOfWine, 15, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[TRAIN RANGED] -> Fulfilled equipment correctly!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN RANGED] -> NOT fulfilled equipment correctly!");
			return false;
		}
    	
    }
    public static void setBestRangedEquipment()
    {
    	InvEquip.setEquipItem(EquipmentSlot.SHIELD, getBestShieldSlot());
    	InvEquip.setEquipItem(EquipmentSlot.HAT, getBestHeadSlot());
    	InvEquip.setEquipItem(EquipmentSlot.CHEST, getBestBodySlot());
    	InvEquip.setEquipItem(EquipmentSlot.HANDS, getBestHandSlot());
    	InvEquip.setEquipItem(EquipmentSlot.FEET, getBestBootSlot());
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, getBestAmuletSlot());
    	InvEquip.setEquipItem(EquipmentSlot.LEGS, getBestLegSlot());
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	boolean foundCape = false;
    	for(int capeID : randCapes)
    	{
    		if(Equipment.contains(capeID)) 
    		{
    			InvEquip.setEquipItem(EquipmentSlot.CAPE, capeID);
    			foundCape = true;
    			break;
    		}
    	}
    	if(!foundCape) InvEquip.setEquipItem(EquipmentSlot.CAPE, getBestCapeSlot());
    	InvEquip.addInvyItem(getBestDart(), 500, 1000, false, 1000);
		if(getNextBestDart() != getBestDart()) InvEquip.addInvyItem(getNextBestDart(), 500, 1000, false, 1000);
		if(getNextNextBestDart() != getBestDart() && 
				getNextNextBestDart() != getNextBestDart()) InvEquip.addInvyItem(getNextNextBestDart(), 500, 1000, false, 1000);
    	
    }
    public static boolean fulfillRangedDartsStamina()
    {
    	InvEquip.clearAll();
    	setBestRangedEquipment();
    	InvEquip.addInvyItem(rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 5);
    	InvEquip.addInvyItem(id.antidote4, 1, 1, false, 5);
    	if(InvEquip.bankContains(id.staminas))
    	{
    		InvEquip.addInvyItem(InvEquip.getBankItem(id.staminas), 1, 1, false, 0);
    	}
    	else InvEquip.addInvyItem(id.stamina4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(jugOfWine, 15, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (antidote + stamina strict)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (antidote + stamina strict)");
			return false;
		}
    	
    }
    public static boolean fulfillRangedDartsStaminaAntidote()
    {
    	InvEquip.clearAll();
    	setBestRangedEquipment();
    	InvEquip.addInvyItem(rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 5);
    	InvEquip.addInvyItem(id.antidote4, 1, 1, false, 5);
    	if(InvEquip.bankContains(id.staminas))
    	{
    		InvEquip.addInvyItem(InvEquip.getBankItem(id.staminas), 1, 1, false, 0);
    	}
    	else InvEquip.addInvyItem(id.stamina4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(jugOfWine, 15, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (antidote + stamina strict)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (antidote + stamina strict)");
			return false;
		}
    }
    
    
    public static final int dorgBow = 8880;
    public static final int boneBolts = 8882;

	public static final int zamorakCoif = 10374;
	public static final int snakeskinBandana = 6326;
	public static final int coif = 1169;
	public static final int cowl = 1167;
    public static int getBestHeadSlot()
    {
    	final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40 && ranged >= 70) return zamorakCoif;
    	if(def >= 30 && ranged >= 30) return snakeskinBandana;
    	if(ranged >= 20) return coif;
    	return cowl;
    }
    public static final int blackBody = 2503;
	public static final int redBody = 2501;
	public static final int blueBody = 2499;
	public static final int snakeskinBody = 6322;
	public static final int studdedBody = 1133;
	public static final int hardLeatherBody = 1131;
	public static final int leatherBody = 1129;
	public static int getBestBodySlot()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40 && ranged >= 70) return blackBody;
    	if(def >= 40 && ranged >= 60) return redBody;
    	if(def >= 40 && ranged >= 50) return blueBody;
    	if(def >= 30 && ranged >= 30) return snakeskinBody;
    	if(def >= 20 && ranged >= 20) return studdedBody;
    	if(def >= 10) return hardLeatherBody;
    	return leatherBody;
    }
	

    public static final int blackLegs = 2497;
	public static final int redLegs = 2495;
	public static final int blueLegs = 2493;
	public static final int snakeskinChaps = 6324;
	public static final int studdedChaps = 1097;
	public static final int leatherChaps = 1095;
	public static int getBestLegSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(ranged >= 70) return blackLegs;
    	if(ranged >= 60) return redLegs;
    	if(ranged >= 50) return blueLegs;
    	if(def >= 30 && ranged >= 30) return snakeskinChaps;
    	if(ranged >= 20) return studdedChaps;
    	return leatherChaps;
	}
	public static final int darkRedCape = 4397;
	public static final int lightRedCape = 4317;
	public static final int yellowCape = 4337;
	public static final int lightBlueCape = 4357;
	public static final int lightGreenCape = 4377;
	public static int randCape = 0;
	public static final int avasAccumulator	= 10499;
	public static int getBestCapeSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
		if(ranged >= 50 && AnimalMagnetism.completedAnimalMagnetism) return avasAccumulator;
    	if(InvEquip.equipmentContains(randCapes)) return InvEquip.getEquipmentItem(randCapes);
    	if(InvEquip.invyContains(randCapes)) return InvEquip.getInvyItem(randCapes);
    	if(InvEquip.bankContains(randCapes)) return InvEquip.getBankItem(randCapes);
    	return randCape;
	}
	
	public static final int snakeskinBoots = 6328;
	public static final int leatherBoots = 1061;
	public static int getBestBootSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(def >= 30 && ranged >= 30) return snakeskinBoots;
    	return leatherBoots;
	}
	public static int getNextBestBootSlot()
	{
    	return snakeskinBoots;
	}

	public static final int guthixBracers = 807;
	public static final int blueVambs = 2487;
	public static final int redVambs = 2489;
	public static final int greenVambs = 1065;
	public static int getBestHandSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
		if(ranged >= 70) return guthixBracers;
		if(ranged >= 60) return redVambs;
		if(ranged >= 50) return blueVambs;
		if(ranged >= 40) return greenVambs;
    	return InvEquip.combat;
	}
	
	public static int getBestAmuletSlot()
	{
		return InvEquip.glory;
	}
	
	public static final int bookOfLaw = 12610;
	public static final int blackShield = 22284;
	public static final int redShield = 22281;
	public static final int blueShield = 22278;
	public static final int greenShield = 22275;
	public static final int snakeskinShield = 22272;
	public static final int hardleatherShield = 22269;
	public static final int woodenShield = 1171;
	public static int getBestShieldSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(HorrorFromTheDeep.completedHorrorFromTheDeep) return bookOfLaw;
		if(def >= 40 && ranged >= 70) return blackShield;
		//skipping green, blue, and red dhide shields cuz less than 200 traded per day. ...
		if(def >= 30 && ranged >= 30) return snakeskinShield;
		if(def >= 10 && ranged >= 20) return hardleatherShield;
		return woodenShield;
	}
	
    public static final int ironDart = 807;
	public static final int steelDart = 808;
	public static final int mithDart = 809;
	public static final int addyDart = 810;
    public static int getBestDart()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	if(ranged >= 30) return addyDart;
    	if(ranged >= 20) return mithDart;
    	if(ranged >= 5) return steelDart;
    	return ironDart;
    }
    public static int getNextBestDart()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	if(ranged >= 20) return addyDart;
    	if(ranged >= 5) return mithDart;
    	return steelDart;
    }
    public static int getNextNextBestDart()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	if(ranged >= 5) return addyDart;
    	return mithDart;
    }
    
    
}
