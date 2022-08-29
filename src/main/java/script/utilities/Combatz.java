package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.Main;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;
import script.skills.ranged.TrainRanged;

public class Combatz {
	public static List<Integer> foods = new ArrayList<Integer>();
	public static List<Integer> highFoods = new ArrayList<Integer>();
	public static Timer foodEatTimer = null;
	public static Timer foodAttemptTimer = null;
	public static int lowFood = -1;
	public static int nextRandMeleeBoostLvl = 0;
	public static int nextRandRangedBoostLvl = 0;
	public static void initializeFoods()
	{
		if(UniqueActions.isActionEnabled(Actionz.BASS_OR_LOBSTER_INSTEAD_OF_JUGSOFWINE)) lowFood = id.bass;
		else lowFood = id.jugOfWine;
		foods.add(id.pineapplePizza1);
		foods.add(id.jugOfWine);
		foods.add(id.bass);
		foods.add(id.swordfish);
		foods.add(id.monkfish);
		foods.add(id.pineapplePizza2);
		foods.add(id.seaTurtle);
	}
	public static Item getFood()
	{
		return Inventory.get(InvEquip.getInvyItem(foods));
	}
	public static boolean hasAntidoteProtection()
	{
		if(PlayerSettings.getConfig(102) < 0 && PlayerSettings.getConfig(102) >= -40)
		{
			return true;
		}
		return false;
	}
	public static Timer antidoteDrinkTimer = null;
	
	public static void toggleAutoRetaliate(boolean on)
	{
		if(Combat.isAutoRetaliateOn() == on) return;
		if(!Tabs.isOpen(Tab.COMBAT))
		{
			if(Tabz.open(Tab.COMBAT)) MethodProvider.sleepUntil(() -> Tabs.isOpen(Tab.COMBAT),Sleep.calculate(420, 696));
			return;
		}
		if(Combat.toggleAutoRetaliate(on)) MethodProvider.sleepUntil(() -> Combat.isAutoRetaliateOn() == on, Sleep.calculate(2222, 2222));
	}
	
	public static boolean drinkAntidote()
	{
		if(antidoteDrinkTimer != null && !antidoteDrinkTimer.finished()) return true;
		if(InvEquip.invyContains(id.antidotes))
		{
			if(Inventory.interact(InvEquip.getInvyItem(id.antidotes), "Drink"))
			{
				MethodProvider.log("Drank antidote!");
				antidoteDrinkTimer = new Timer(Sleep.calculate(2222,2222));
				return true;
			}
			
			if(!Tabs.isOpen(Tab.INVENTORY))
			{
				Tabz.open(Tab.INVENTORY);
			}
			return true;
		}
		return false;
	}
	
	private static Timer drinkDelayTimer = null;
	public static int randBoostFactor = 0;
	public static void initializeBoostFactor()
	{
        randBoostFactor = (int) Calculations.nextGaussianRandom(1.5, 3);
        if(randBoostFactor > 3) randBoostFactor = 3;
        if(randBoostFactor < 0) randBoostFactor = 0;
	}
    public static boolean drinkRangeBoost()
    {
    	for(int rangePot : id.rangedPots)
    	{
    		if(Inventory.count(rangePot) <= 0) continue;
    		if(Tabs.isOpen(Tab.INVENTORY) || Bank.isOpen())
    		{
    			if(Inventory.interact(rangePot, "Drink")) 
    			{
    				nextRandRangedBoostLvl = 0;
    				drinkDelayTimer = new Timer(1200);
    				return true;
    			}
    			return false;
    		}
    		if(Widgets.isOpen()) Widgets.closeAll();
    		else Tabz.open(Tab.INVENTORY);
    		return false;
    	}
    	return false;
    }
    public static boolean shouldDrinkRangedBoost()
    {
    	final int ranged = Skills.getRealLevel(Skill.RANGED);
    	if(nextRandRangedBoostLvl == 0 || nextRandRangedBoostLvl < ranged)
    	{
    		double median = 0;
    		double sigma = calculateMaxRangedBoost() / 2;
    		if(randBoostFactor == 0) median = ranged;
        	else if(randBoostFactor == 1) median = ranged + (calculateMaxRangedBoost() / 7);
        	else if(randBoostFactor == 2) median = ranged + (3 * calculateMaxRangedBoost() / 9);
        	else if(randBoostFactor == 3) median = ranged + (5 * calculateMaxRangedBoost() / 11);
        	int tmp = (int) Calculations.nextGaussianRandom(median, sigma);
    		if(tmp > calculateMaxRangedBoost() + ranged) nextRandRangedBoostLvl = calculateMaxRangedBoost() + ranged - 1;
    		else if(tmp < ranged) nextRandRangedBoostLvl = ranged;
            else nextRandRangedBoostLvl = tmp;
    	}
    	Main.customPaintText2 = "Next ranged pot at boostLvl (baseLvL): " + nextRandRangedBoostLvl+" ("+ranged+")";
    	if(drinkDelayTimer != null && !drinkDelayTimer.finished() && !drinkDelayTimer.isPaused()) return false;
    	if(Skills.getBoostedLevels(Skill.RANGED) <= nextRandRangedBoostLvl) return true;
    	return false;
    }
    public static boolean shouldDrinkMeleeBoost()
    {
    	final int att = Skills.getRealLevel(Skill.ATTACK);
    	final int str = Skills.getRealLevel(Skill.STRENGTH);
    	int lowest = str;
    	Skill lowestSkill = Skill.STRENGTH;
    	if(att < str) 
    	{
    		lowest = att;
    		lowestSkill = Skill.ATTACK;
    	}
    	
    	if(nextRandMeleeBoostLvl == 0 || nextRandMeleeBoostLvl < lowest)
    	{
    		double median = 0;
    		int maxMeleeBoost = calculateMaxMeleeBoost(lowestSkill);
    		double sigma = calculateMaxMeleeBoost(lowestSkill) / 2;
    		if(randBoostFactor == 0) median = lowest;
        	else if(randBoostFactor == 1) median = lowest + (maxMeleeBoost / 7);
        	else if(randBoostFactor == 2) median = lowest + (3 * maxMeleeBoost / 9);
        	else if(randBoostFactor == 3) median = lowest + (5 * maxMeleeBoost / 11);
        	int tmp = (int) Calculations.nextGaussianRandom(median, sigma);
    		if(tmp > maxMeleeBoost + lowest) nextRandMeleeBoostLvl = maxMeleeBoost + lowest - 1;
    		else if(tmp < lowest) nextRandMeleeBoostLvl = lowest;
            else nextRandMeleeBoostLvl = tmp;
    	}
    	Main.customPaintText2 = "Next "+lowestSkill.getName()+" pot at boostLvl (baseLvl): " + nextRandMeleeBoostLvl +" ("+lowest+")";
    	if(drinkDelayTimer != null && !drinkDelayTimer.finished() && !drinkDelayTimer.isPaused()) return false;
    	if(Skills.getBoostedLevels(lowestSkill) <= nextRandMeleeBoostLvl) return true;
    	return false;
    }
    public static void drinkMeleeBoost()
    {
    	List<List<Integer>> karma = new ArrayList<List<Integer>>();
    	karma.add(id.superAtts);
    	karma.add(id.superStrs);
    	Collections.shuffle(karma);
    	karma.add(0,id.superCombats);
    	for(List<Integer> superPots : karma)
    	{
    		Timer timeout = new Timer(7000);
    		while(!timeout.isPaused() && !timeout.finished() && 
    				ScriptManager.getScriptManager().isRunning() && 
    				!ScriptManager.getScriptManager().isPaused())
    		{
    			if(!InvEquip.invyContains(superPots)) break;
    			Sleep.sleep(69, 69);
    			if(drinkDelayTimer != null && !drinkDelayTimer.finished() && !drinkDelayTimer.isPaused()) 
    			{
    				
					Main.customPaintText3 = "~~Time until potion delay finish: " + drinkDelayTimer.remaining() +"ms~~";
					continue;
    			}
    			if(!Tabs.isOpen(Tab.INVENTORY)) 
    			{
    				Tabz.open(Tab.INVENTORY);
    				Sleep.sleep(69, 420);
    				continue;
    			}
    			if(Inventory.get(InvEquip.getInvyItem(superPots)).interact("Drink"))
    			{
    				if(superPots.equals(id.superCombats))
    				{
    					MethodProvider.log("Drank supercombat pot!");
    					int delay = (int) Calculations.nextGaussianRandom(2000,400);
        				if(delay<1400) delay = 1500;
        				drinkDelayTimer = new Timer();
        				nextRandMeleeBoostLvl = 0;
    					Main.customPaintText3 = "";
    					Main.customPaintText4 = "";
    					Sleep.sleep(666, 696);
        				return;
    				}
    				MethodProvider.log("Drank "+(id.superStrs == superPots ? "Strength" : "Attack")+" pot!");
    				int delay = (int) Calculations.nextGaussianRandom(2000,400);
    				if(delay<1400) delay = 1500;
    				drinkDelayTimer = new Timer();
    				nextRandMeleeBoostLvl = 0;
					Main.customPaintText3 = "";
					Main.customPaintText4 = "";
    				break;
    			}
    		}
    	}
    	Sleep.sleep(696, 666);
    }
    
    public static int calculateMaxMeleeBoost(Skill attOrStr)
    {
    	return ((int)(((double)Skills.getRealLevel(attOrStr)) * 0.15)) + 5;
    }
    public static int calculateMaxRangedBoost()
    {
    	return ((int)(((double)Skills.getRealLevel(Skill.RANGED)) * 0.1)) + 4;
    }
	public static int nextPrayerPotLvl = 0;
	public static boolean shouldDrinkPrayPot()
    {
    	if(nextPrayerPotLvl == 0)
    	{
    		int tmp = (int) Calculations.nextGaussianRandom((15),8);
    		if(tmp > Skills.getRealLevel(Skill.PRAYER)) nextPrayerPotLvl = (Skills.getRealLevel(Skill.PRAYER) - 18);
    		else if(tmp < 5) nextPrayerPotLvl = 5;
    		else nextPrayerPotLvl = tmp;
    	}
    	Main.customPaintText2 = "Eating next praypot sip at pray lvl: " + nextPrayerPotLvl;
    	if(Skills.getBoostedLevels(Skill.PRAYER) <= nextPrayerPotLvl)
    	{
    		return true;
    	}
    	return false;
    }
	public static boolean drinkPrayPot()
	{
		Main.customPaintText4 = "~~sipping Praypot~~";
		Timer timer = new Timer(5000);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 69);
			if(Tabs.isOpen(Tab.INVENTORY))
			{
				if(drinkDelayTimer != null && !drinkDelayTimer.finished())
				{
					MethodProvider.log("attempted to sip pray pot " +drinkDelayTimer.elapsed()+"ms ago, continuing");
					Main.customPaintText3 = "~~Time until potion delay finish: " + drinkDelayTimer.remaining() +"ms~~";
					continue;
				}
				Item prayPot = Inventory.get(InvEquip.getInvyItem(id.prayPots));
				if(prayPot == null) return false;
				final int prayerPotID = prayPot.getID();
				Main.customPaintText3 = ("Drinking prayer potion: " + new Item(prayerPotID,1).getName());
				if(Inventory.interact(prayerPotID, "Drink"))
				{
					MethodProvider.log("Attempted to drink praypot!");
					nextPrayerPotLvl = 0;
					drinkDelayTimer = new Timer(1200); 
					Main.customPaintText3 = "";
					Main.customPaintText4 = "";
					return true;
				}
			}
			else
			{
				if(Widgets.isOpen())
				{
					Widgets.closeAll();
					Sleep.sleep(111, 111);
				}
				else Tabz.open(Tab.INVENTORY);
			}
		}
		
		return false;
	}
	
	public static int nextFoodHP = 0;
	public static boolean shouldEatFood(int maxHit)
    {
    	if(nextFoodHP == 0 || nextFoodHP < maxHit)
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
	public static boolean eatFood()
	{
		if(foodAttemptTimer != null && !foodAttemptTimer.isPaused() && !foodAttemptTimer.finished())
		{
			MethodProvider.log("attempted to eat food: " +foodAttemptTimer.elapsed()+"ms ago, continuing");
			return false;
		}
		Timer timer = new Timer(5000);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Main.customPaintText4 = "~~Eating food until: "+timer.remaining()+"ms~~";
			Sleep.sleep(69, 69);
			if(Tabs.isOpen(Tab.INVENTORY))
			{
				if(foodEatTimer != null && !foodEatTimer.finished())
				{
					MethodProvider.log("Called eatFood function, but food after-eat-timer still running! Waiting...");
					Main.customPaintText3 = "~~Time until eat delay finish: " + foodEatTimer.remaining() +"ms~~";
					continue;
				}
				Item food = getFood();
				if(food == null) return false;
				final int foodID = food.getID();
				String action = null;
				for(String act : food.getActions())
				{
					if(act == null) continue;
					if(act.equals("Eat")) 
					{
						action = "Eat";
						break;
					}
					else if(act.equals("Drink")) 
					{
						action = "Drink";
						break;
					}
				}
				Main.customPaintText3 = ("Eating food: " + new Item(foodID,1).getName());
				
				if(Inventory.interact(foodID, action))
				{
					MethodProvider.log("Attempted to eat food!");
					nextFoodHP = 0;
					foodAttemptTimer = new Timer(600);
					foodEatTimer = new Timer(1200); // when u eat a food, it waits until next tick to actually eat it.
					//once food is eaten then the game needs to wait 2 more ticks until you can attempt to eat another food
					//if u attempt to eat another food on the 2nd tick after, you will eat the food on the end of the 2nd tick
					//after 2nd tick after 1st eat completes, any more food eating is OK but will only register on end of tick
					Main.customPaintText3 = "";
					Main.customPaintText4 = "";
					return true;
				}
			}
			else
			{
				if(Widgets.isOpen())
				{
					Widgets.closeAll();
					Sleep.sleep(111, 111);
				}
				else Tabz.open(Tab.INVENTORY);
			}
		}
		return false;
	}
	public static final int quickPrayVar = 4102;
	public static final int eagleEye_protectMelee = 4210688;
	public static final int protectMelee = 16384;
	public static final int eagleEye = 4194304;
	public static boolean setQuickPrayEagleEyeProtectMelee()
	{
		final WidgetChild eagleEyeButton = Widgets.getWidgetChild(77,4,22);
		final WidgetChild protectMeleeButton = Widgets.getWidgetChild(77,4,14);
		if(PlayerSettings.getBitValue(4102) == eagleEye_protectMelee) //setting for both Eagle Eye + Protekk Melee
		{
			if(Widgets.getWidgetChild(77,5) != null && 
					Widgets.getWidgetChild(77,5).isVisible())
			{
				if(Widgets.getWidgetChild(77,5).interact("Done"))
				{
					MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(77,5) == null || !Widgets.getWidgetChild(77,5).isVisible(), Sleep.calculate(2222, 2222));
				}
				return false;
			}
			return true;
		}
		if(Widgets.getWidgetChild(77,5) == null || 
				!Widgets.getWidgetChild(77,5).isVisible())
		{
			if(Widgets.getWidgetChild(160, 19) != null && 
					Widgets.getWidgetChild(160, 19).isVisible())
			{
				if(Widgets.getWidgetChild(160, 19).interact("Setup"))
				{
					MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(77,5) != null && Widgets.getWidgetChild(77,5).isVisible(), Sleep.calculate(2222, 2222));
				}
			}
			return false;
		}
		if(PlayerSettings.getBitValue(4102) == eagleEye)
		{
			if(protectMeleeButton.interact("Toggle"))
			{
				MethodProvider.sleepUntil(() -> PlayerSettings.getBitValue(4102) == eagleEye_protectMelee, Sleep.calculate(2222, 2222));
			}
			return false;
		}
		if(PlayerSettings.getBitValue(4102) == protectMelee)
		{
			if(eagleEyeButton.interact("Toggle"))
			{
				MethodProvider.sleepUntil(() -> PlayerSettings.getBitValue(4102) == eagleEye_protectMelee, Sleep.calculate(2222, 2222));
			}
			return false;
		}
		if(PlayerSettings.getBitValue(4102) == 0) //none
		{
			if((int) Calculations.nextGaussianRandom(100, 50) > 120)
			{
				if(eagleEyeButton.interact("Toggle"))
				{
					MethodProvider.sleepUntil(() -> PlayerSettings.getBitValue(4102) == eagleEye, Sleep.calculate(2222, 2222));
				}
			}
			else 
			{
				if(protectMeleeButton.interact("Toggle"))
				{
					MethodProvider.sleepUntil(() -> PlayerSettings.getBitValue(4102) == protectMelee, Sleep.calculate(2222, 2222));
				}
			}
			return false;
		}
		
		
		return false;
	}
	
}
