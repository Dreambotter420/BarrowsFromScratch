package script.utilities;

import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
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
import script.skills.ranged.TrainRanged;

public class Combat {
	public static List<Integer> foods = new ArrayList<Integer>();
	public static List<Integer> highFoods = new ArrayList<Integer>();
	public static Timer foodEatTimer = null;
	public static Timer foodAttemptTimer = null;
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
	public static boolean drinkAntidote()
	{
		if(antidoteDrinkTimer != null && !antidoteDrinkTimer.finished()) return true;
		if(InvEquip.invyContains(InvEquip.antidotes))
		{
			if(Inventory.interact(InvEquip.getInvyItem(InvEquip.antidotes), "Drink"))
			{
				MethodProvider.log("Drank antidote!");
				antidoteDrinkTimer = new Timer(Sleep.calculate(2222,2222));
				return true;
			}
			
			if(!Tabs.isOpen(Tab.INVENTORY))
			{
				Tabs.open(Tab.INVENTORY);
			}
			return true;
		}
		return false;
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
    	Main.customPaintText1 = "Eating next praypot sip at pray lvl: " + nextPrayerPotLvl;
    	if(Skills.getBoostedLevels(Skill.PRAYER) <= nextPrayerPotLvl)
    	{
    		return true;
    	}
    	return false;
    }
	public static Timer potionAttemptTimer = null;
	public static boolean drinkPrayPot()
	{
		if(potionAttemptTimer != null && !potionAttemptTimer.isPaused() && !potionAttemptTimer.finished())
		{
			MethodProvider.log("attempted to sip pray pot " +potionAttemptTimer.elapsed()+"ms ago, continuing");
			return false;
		}
		Main.customPaintText4 = "~~sipping Praypot~~";
		Timer timer = new Timer(5000);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 69);
			if(Tabs.isOpen(Tab.INVENTORY))
			{
				Item prayPot = Inventory.get(InvEquip.getInvyItem(id.prayPots));
				if(prayPot == null) return false;
				final int prayerPotID = prayPot.getID();
				Main.customPaintText3 = ("Drinking prayer potion: " + new Item(prayerPotID,1).getName());
				if(Inventory.interact(prayerPotID, "Drink"))
				{
					MethodProvider.log("Attempted to drink praypot!");
					nextFoodHP = 0;
					potionAttemptTimer = new Timer(1200); 
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
				else Tabs.open(Tab.INVENTORY);
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
				else Tabs.open(Tab.INVENTORY);
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
