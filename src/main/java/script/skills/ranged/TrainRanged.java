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
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
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
	public static int nextFoodHP = 0;
	public static List<Integer> rangedPots = new ArrayList<Integer>();
	public static int randRangedBoostFactor = 0;
	public static int nextRandBoostLvl = 0;
	public static boolean started = false;
	public static List<Integer> randCapes = new ArrayList<Integer>();
	public static int ranged = 0;
	public static int def = 0;
	public static int bringMoreGear = 0;
    public void onStart() {
    	if(Mobs.mob == null) Mobs.chooseMob();
    	Combat.foods.clear();
        Combat.foods.add(jugOfWine);
        Combat.highFoods.clear();
        Combat.highFoods.add(seaTurtle);
        randRangedBoostFactor = (int) Calculations.nextGaussianRandom(1.5, 3);
        if(randRangedBoostFactor > 3) randRangedBoostFactor = 3;
        if(randRangedBoostFactor < 0) randRangedBoostFactor = 0;
        int tmp = (int) Calculations.nextGaussianRandom(10, 5);
        if(tmp > 15) randCape = darkRedCape;
        if(tmp > 12) randCape = lightRedCape;
        if(tmp > 10) randCape = yellowCape;
        if(tmp > 8) randCape = lightGreenCape;
        if(randCape == 0) randCape = lightBlueCape;

        int rand = (int) Calculations.nextGaussianRandom(1, 2);
        if(rand <= 0) rand = 0;
        if(rand >= 2) rand = 2;
        bringMoreGear = rand;
        randCapes.add(darkRedCape);
        randCapes.add(lightRedCape);
        randCapes.add(yellowCape);
        randCapes.add(lightGreenCape);
        randCapes.add(lightBlueCape);
        rangedPots.add(rangePot1);
        rangedPots.add(rangePot2);
        rangedPots.add(rangePot3);
        rangedPots.add(rangePot4);
        instantiateTree();
        Main.customPaintText1 = "~~~ Training Ranged ~~~";
        started = true;
        
    	
    }
    public boolean onExit() {
        Main.clearCustomPaintText();
        Mobs.mob = null;
    	return true;
    }

    private final Tree tree = new Tree();
    private void instantiateTree() {
    	
    }
    @Override
    public int onLoop() {
    	ranged = Skills.getRealLevel(Skill.RANGED);
    	def = Skills.getRealLevel(Skill.DEFENCE);
        if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Ranged!");
    		if(onExit())
    		{
    			 API.mode = null;
    	            return Timing.sleepLogNormalSleep();
    		}
           
    	}
    	if (Skills.getRealLevel(Skill.RANGED) >= 75) {
            MethodProvider.log("[COMPLETE] -> lvl 75 ranged!");
            if(onExit())
    		{
                API.mode = null;
                return Timing.sleepLogNormalSleep();
    		}
           
        }
    	if(!started) 
    	{
    		onStart();
    		return Timing.sleepLogNormalSleep();
    	}
    	Mobs.trainMob(Mobs.mob);
        return tree.onLoop();
    }
    
    
   
    public static boolean fulfillRangedDarts()
    {
    	InvEquip.clearAll();
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
    	
    	InvEquip.addInvyItem(jugOfWine, 1, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combat.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[TRAIN RANGED] -> Fulfilled equipment correctly for Boars!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN RANGED] -> NOT fulfilled equipment correctly for Boars!");
			return false;
		}
    	
    }
    public static Timer drankTimer = null;
    public static Timer drinkDelayTimer = null;
    public static boolean drankRangedPotion()
    {
    	for(int rangePot : rangedPots)
    	{
    		if(Inventory.count(rangePot) <= 0) continue;
    		if(Tabs.isOpen(Tab.INVENTORY) || Bank.isOpen())
    		{
    			if(Inventory.interact(rangePot, "Drink")) 
    			{
    				nextRandBoostLvl = 0;
    				drinkDelayTimer = new Timer(1200);
    				return true;
    			}
    			return false;
    		}
    		if(Widgets.isOpen()) Widgets.closeAll();
    		else Tabs.open(Tab.INVENTORY);
    		return false;
    	}
    	return false;
    }
    public static boolean shouldDrinkBoost()
    {
    	if(nextRandBoostLvl == 0)
    	{
    		if(randRangedBoostFactor == 0)
        	{
        		int tmp = (int) Calculations.nextGaussianRandom(ranged, 10);
        		if(tmp > calculateMaxRangedBoost() + ranged) nextRandBoostLvl = calculateMaxRangedBoost() + ranged - 1;
        		else if(tmp < ranged) nextRandBoostLvl = ranged;
        		else nextRandBoostLvl = tmp;
        	}
        	else if(randRangedBoostFactor == 1)
        	{
        		double median = ranged + (calculateMaxRangedBoost() / 7);
        		int tmp = (int) Calculations.nextGaussianRandom(median, 10);
        		if(tmp > calculateMaxRangedBoost() + ranged) nextRandBoostLvl = calculateMaxRangedBoost() + ranged - 1;
        		else if(tmp < ranged) nextRandBoostLvl = ranged;
                else nextRandBoostLvl = tmp;
        	}
        	else if(randRangedBoostFactor == 2)
        	{
        		double median = ranged + (2 * calculateMaxRangedBoost() / 9);
        		int tmp = (int) Calculations.nextGaussianRandom(median, 10);
        		if(tmp > calculateMaxRangedBoost() + ranged) nextRandBoostLvl = calculateMaxRangedBoost() + ranged - 1;
        		else if(tmp < ranged) nextRandBoostLvl = ranged;
        		else nextRandBoostLvl = tmp;
        	}
        	else if(randRangedBoostFactor == 3)
        	{
        		double median = ranged + (3 * calculateMaxRangedBoost() / 11);
        		int tmp = (int) Calculations.nextGaussianRandom(median, 10);
        		if(tmp > calculateMaxRangedBoost() + ranged) nextRandBoostLvl = calculateMaxRangedBoost() + ranged - 1;
        		else if(tmp < ranged) nextRandBoostLvl = ranged;
        		else nextRandBoostLvl = tmp;
        	}
    	}
    	Main.customPaintText2 = "Drinking next ranged pot at boosted lvl: " + nextRandBoostLvl;
    	if(drinkDelayTimer != null && !drinkDelayTimer.finished() && !drinkDelayTimer.isPaused()) return false;
    	if(Skills.getBoostedLevels(Skill.RANGED) <= nextRandBoostLvl)
    	{
    		return true;
    	}
    	return false;
    }
    public static boolean shouldEatFood(int maxHit)
    {
    	if(nextFoodHP == 0)
    	{
    		int tmp = (int) Calculations.nextGaussianRandom((maxHit + 3), maxHit);
    		if(tmp > Skills.getRealLevel(Skill.HITPOINTS)) nextFoodHP = (Skills.getRealLevel(Skill.HITPOINTS) - 2);
    		else if(tmp < maxHit) nextFoodHP = maxHit;
    		else nextFoodHP = tmp;
    	}
    	Main.customPaintText1 = "Eating next food at HP lvl: " + nextFoodHP;
    	if(Skills.getBoostedLevels(Skill.HITPOINTS) <= nextFoodHP)
    	{
    		return true;
    	}
    	return false;
    }
    
    public static int calculateMaxRangedBoost()
    {
    	return ((int)(((double)ranged) * 0.1)) + 4;
    }
    
    public static final int dorgBow = 8880;
    public static final int boneBolts = 8882;

	public static final int zamorakCoif = 10374;
	public static final int snakeskinBandana = 6326;
	public static final int coif = 1169;
	public static final int cowl = 1167;
    public static int getBestHeadSlot()
    {
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
		if(def >= 40 && ranged >= 70) return blackLegs;
    	if(def >= 40 && ranged >= 60) return redLegs;
    	if(def >= 40 && ranged >= 50) return blueLegs;
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
		if(ranged >= 50 && AnimalMagnetism.completedAnimalMagnetism) return avasAccumulator;
    	return randCape;
	}
	
	public static final int snakeskinBoots = 6328;
	public static final int leatherBoots = 1061;
	public static int getBestBootSlot()
	{
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
		if(HorrorFromTheDeep.completedHorrorFromTheDeep) return bookOfLaw;
		if(def >= 40 && ranged >= 70) return blackShield;
		if(def >= 40 && ranged >= 60) return redShield;
		if(def >= 40 && ranged >= 50) return blueShield;
		if(def >= 40 && ranged >= 40) return greenShield;
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
    	if(ranged >= 30) return addyDart;
    	if(ranged >= 20) return mithDart;
    	if(ranged >= 5) return steelDart;
    	return ironDart;
    }
    public static int getNextBestDart()
    {
    	if(ranged >= 20) return addyDart;
    	if(ranged >= 5) return mithDart;
    	return steelDart;
    }
    public static int getNextNextBestDart()
    {
    	if(ranged >= 5) return addyDart;
    	return mithDart;
    }
    
    
}
