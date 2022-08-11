package script.quest.priestinperil;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Priest in Peril
 * @author Dreambotter420
 * ^_^
 */
public class PriestInPeril extends Leaf {
	public static boolean completedPriestInPeril = false;
	public static final int ironKey = 2945;
	public static final int goldenKey = 2944;
	public static final int unblessedBucket = 2953;
	public static final int blessedBucket = 2954;
	public static Tile correctMonument = null;
    
    @Override
	public boolean isValid() {
		return API.mode == API.modes.PRIEST_IN_PERIL;
	}
    public static boolean onExit()
    {
    	correctMonument = null;
    	return true;
    }
    @Override
    public int onLoop() {
    	if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> The Restless Ghost");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if (completedPriestInPeril) {
            MethodProvider.log("[COMPLETED] -> Priest in Peril!");
           	API.mode = null;
           	Main.customPaintText1 = "~~~~~~~~~~";
    		Main.customPaintText2 = "~Quest Complete~";
    		Main.customPaintText3 = "~Priest in Peril~";
    		Main.customPaintText4 = "~~~~~~~~~~";
            return Timing.sleepLogNormalSleep();
        }
        
        if(Dialogues.getNPCDialogue() != null && !Dialogues.getNPCDialogue().isEmpty())
    	{
    		MethodProvider.log("NPC Dialogue: " + Dialogues.getNPCDialogue());
    	}
        if(handleDialogues()) return Timing.sleepLogNormalInteraction();
        if(Inventory.contains(TrainRanged.getBestDart()))
        {
        	InvEquip.equipItem(TrainRanged.getBestDart());
        	return Timing.sleepLogNormalSleep();
        }
        if(getProgressValue() == 61)
        {
        	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") &&
        				!Walkz.useJewelry(InvEquip.glory, "Edgeville"))
        		{
        			Walkz.teleportVarrock(30000);
        		}
        		return Timing.sleepLogNormalInteraction();
        	}
        	completedPriestInPeril = true;
    		return Timing.sleepLogNormalInteraction();
        }
        if(getProgressValue() == 60)
        {
        	//quest done widget - not really have access to morytania until progress value is 61 ... talk to drezel some more...
        	if(Widgets.getWidgetChild(153,16) != null &&
        			Widgets.getWidgetChild(153,16).isVisible())
        	{
        		if(Widgets.getWidgetChild(153,16).interact("Close")) Sleep.sleep(696, 666);
        		return Timing.sleepLogNormalInteraction();
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
        	{
        		API.walkTalkToNPC("Drezel", "Talk-to", Locations.PiP_undergroundPassDrezel);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()))
        	{
        		MethodProvider.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		Sleep.sleep(696, 1111);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		return Timing.sleepLogNormalSleep();
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleep.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.localPlayer()));
        	Sleep.sleep(696, 666);
        	return Timing.sleepLogNormalSleep();
        }
        if(getProgressValue() >= 10)
        {
        	if(!Inventory.contains(id.pureEss))
        	{
        		if(Bank.count(id.pureEss) >= 25 && BankLocation.VARROCK_EAST.getCenter().distance() > 100)
        		{
        			if(Inventory.count(id.varrockTele) > 0)
        			{
        				Walkz.teleportVarrock(15000);
            			return Timing.sleepLogNormalSleep();
        			}
        		}
        		fulfillPriestInPerilRunes();
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()))
        	{
        		MethodProvider.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		Sleep.sleep(696, 1111);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
        	{
        		API.walkTalkToNPC("Drezel", "Talk-to", Locations.PiP_undergroundPassDrezel);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		return Timing.sleepLogNormalSleep();
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleep.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.localPlayer()));
        	Sleep.sleep(696, 666);
        	return Timing.sleepLogNormalSleep();
        }
        switch(getProgressValue())
        {
        case(8):
        {
        	if(!Inventory.contains(id.pureEss))
        	{
        		fulfillPriestInPerilRunes();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()))
        	{
        		MethodProvider.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		Sleep.sleep(696, 1111);
        		break;
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
        	{
        		API.walkTalkToNPC("Drezel", "Talk-to", Locations.PiP_undergroundPassDrezel);
        		break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleep.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.localPlayer()));
        	Sleep.sleep(696, 666);
        	break;
        }
        case(7):
        {
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()))
        	{
        		API.interactNPC("Drezel", "Talk-to",Locations.PiP_pastramiChurchLvl3, false,() -> Dialogues.inDialogue());
    			MethodProvider.sleep(Timing.sleepLogNormalInteraction());
    			break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()))
        	{
        		MethodProvider.log("Interacting staircase");
    			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_undergroundPassLadder, () -> Locations.PiP_trapdoor.contains(Players.localPlayer()));
        		break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
    		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
    		break;
        }
        case(6):
        {
        	if(!Inventory.contains(id.bucket) && !Inventory.contains(unblessedBucket) && !Inventory.contains(blessedBucket))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(unblessedBucket))
        		{
        			API.interactNPC("Drezel", "Talk-to",Locations.PiP_pastramiChurchLvl3, false,() -> Dialogues.inDialogue());
        			MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        			break;
        		}
        		if(Inventory.contains(blessedBucket))
        		{
        			GameObject coffin = GameObjects.closest("Coffin");
        			if(coffin.canReach())
        			{
        				if(Inventory.get(blessedBucket).useOn(GameObjects.closest("Coffin")))
            			{
            				MethodProvider.sleepUntil(() -> !Inventory.contains(blessedBucket),
            						() -> Players.localPlayer().isMoving(),
            						Sleep.calculate(2222, 2222),69);
            			}
            			MethodProvider.sleep(Timing.sleepLogNormalInteraction());
            			break;
        			}
        			if(GameObjects.closest("Cell door").interact("Open")) 
        			{
        				MethodProvider.sleepUntil(() -> coffin.canReach(), Sleep.calculate(3333, 3333));
        			}
        			break;
        		}
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        		{
        			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()));
            		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
            		break;
        		}
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        		{
            		MethodProvider.log("Interacting staircase");
        			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
            		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
            		break;
        		}
        		MethodProvider.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		Sleep.sleep(696, 1111);
        		break;
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        		{
        			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_undergroundPassLadder, () -> Locations.PiP_trapdoor.contains(Players.localPlayer()));
            		break;
        		}
        		if(Locations.PiP_undergroundPassMonuments.contains(Players.localPlayer()))
    			{
    				if(Inventory.get(id.bucket).useOn(GameObjects.closest(g->g!=null&&g.getName().contains("Well")&&g.hasAction("Search"))))
    				{
    					MethodProvider.sleepUntil(() -> Inventory.contains(unblessedBucket),
    							() ->Players.localPlayer().isMoving(),
    							Sleep.calculate(2222,2222),69);
    				}
    				break;
    			}
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassMonuments.getCenter())) Sleep.sleep(696, 666);
    			break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        	{
        		MethodProvider.log("Interacting doors");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		break;
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleep.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.localPlayer()));
        	Sleep.sleep(696, 666);
        	break;
        }
        case(5):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(TrainRanged.jugOfWine))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(ironKey))
        		{
        			API.walkInteractWithGameObject("Cell door", "Open", Locations.PiP_pastramiChurchLvl3, () -> Dialogues.inDialogue());
        			MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        			break;
        		}
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(ironKey))
        		{
        			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()));
            		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
            		break;
        		}
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()))
        	{
        		MethodProvider.log("Inside pastrami church lvl 1");
        		if(Inventory.contains(ironKey))
        		{
        			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
            		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
            		break;
        		}
        		if(!Inventory.contains(goldenKey))
        		{
        			GroundItem goldenKeyG = GroundItems.closest(g -> g!=null && 
        					g.exists() &&
        					g.getID() == goldenKey);
        			if(goldenKeyG != null)
            		{
            			if(goldenKeyG.interact("Take"))
            			{
            				MethodProvider.sleepUntil(() -> Inventory.contains(goldenKey), Sleep.calculate(2222, 2222));
            			}
            			break;
            		}
            		if(Combatz.shouldEatFood(8)) 
            		{
            			Combatz.eatFood();
            			break;
            		}
            		if(Combatz.shouldDrinkRangedBoost()) Combatz.drinkRangeBoost();
        			final int pray = Skills.getRealLevel(Skill.PRAYER);
        			if(pray >= 44)
        			{
        				if(Combatz.shouldDrinkPrayPot())
        				{
        					Combatz.drinkPrayPot();
        					break;
        				}
        				if(!Prayers.isActive(Prayer.EAGLE_EYE))
        				{
        					Prayers.toggle(true, Prayer.EAGLE_EYE);
        					break;
        				}
        			}
        			else if(pray >= 26)
        			{
        				if(Combatz.shouldDrinkPrayPot())
        				{
        					Combatz.drinkPrayPot();
        					break;
        				}
        				if(!Prayers.isActive(Prayer.HAWK_EYE))
        				{
        					Prayers.toggle(true, Prayer.HAWK_EYE);
        					break;
        				}
        			}
        			if(!Players.localPlayer().isInCombat())
        			{
        				Sleep.sleep(1111, 1111);
        				if(Players.localPlayer().isInCombat()) break;
        				MethodProvider.log("Attacking monk of zammy!");
        				API.interactNPC("Monk of Zamorak","Attack", 30,Locations.PiP_pastramiChurchLvl1,false, () -> Players.localPlayer().isInCombat());
            			Sleep.sleep(696, 666);
        			}
        			break;
        		}

        		MethodProvider.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        		Sleep.sleep(696, 666);
    			break;
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(goldenKey))
        		{
        			if(Locations.PiP_undergroundPassMonuments.contains(Players.localPlayer()))
        			{
        				if(correctMonument != null)
        				{
        					GameObject correctOne = GameObjects.closest(g -> g!=null && 
        							g.getName().equals("Monument") && 
        							g.hasAction("Take-from") && 
        							g.getTile().equals(correctMonument));
        					if(correctOne == null)
        					{
        						MethodProvider.log("Found correct monument but its friggen null!!!!");
        						break;
        					}
        					if(Inventory.get(goldenKey).useOn(correctOne))
        					{
        						MethodProvider.sleepUntil(() -> Inventory.contains(ironKey),() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),69);
        					}
        					break;
        				}
        				correctMonument = studyMonumentsForCorrectTile();
        				break;
        			}
        			if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassMonuments.getCenter())) Sleep.sleep(696, 666);
        			break;
        		}
        		if(Inventory.contains(id.bucket))
        		{
        			if(Locations.PiP_undergroundPassMonuments.contains(Players.localPlayer()))
        			{
        				if(Inventory.get(id.bucket).useOn(GameObjects.closest(g->g!=null&&g.getName().contains("Well")&&g.hasAction("Search"))))
        				{
        					MethodProvider.sleepUntil(() -> Inventory.contains(unblessedBucket),
        							() ->Players.localPlayer().isMoving(),
        							Sleep.calculate(2222,2222),69);
        				}
        				break;
        			}
        			if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassMonuments.getCenter())) Sleep.sleep(696, 666);
        			break;
        		}
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_undergroundPassLadder, () -> Locations.PiP_trapdoor.contains(Players.localPlayer()));
        		break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	if(Inventory.contains(goldenKey))
        	{
        		API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
            	Sleep.sleep(696, 666);
            	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.localPlayer()));
            	Sleep.sleep(696, 666);
            	break;
        	}
        	API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
    		break;
        }
        case(4):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(TrainRanged.jugOfWine))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()))
        	{
        		API.interactNPC("Drezel", "Talk-to", Locations.PiP_pastramiChurchLvl3, false,() -> Dialogues.inDialogue());
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.localPlayer()));
        		MethodProvider.sleep(Timing.sleepLogNormalInteraction());
        		break;
        	}
        	
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.localPlayer()));
        	break;
        }
        case(3):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(TrainRanged.jugOfWine))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	final int pray = Skills.getRealLevel(Skill.PRAYER);
        	if(pray >= 44)
			{
        		if(Prayers.isActive(Prayer.EAGLE_EYE))
				{
					Prayers.toggle(false, Prayer.EAGLE_EYE);
					break;
				}
			}
			else if(pray >= 26)
			{
				if(Prayers.isActive(Prayer.HAWK_EYE))
				{
					Prayers.toggle(false, Prayer.HAWK_EYE);
					break;
				}
			}
        	if(isInDogArena())
        	{
        		if(Inventory.contains(id.varrockTele))
        		{
        			Walkz.teleportVarrock(30000);
        			break;
        		}
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
        		{
        			Tile ladderTile = GameObjects.closest("Ladder").getTile();
            		API.walkInteractWithGameObject("Ladder", "Climb-up", ladderTile.getArea(6), () -> Locations.PiP_trapdoor.contains(Players.localPlayer()));
            	}
        		Sleep.sleep(420, 1111);
        		break;
        	}
        	if(Locations.PiP_kingRoald.getCenter().distance() > 100)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkTalkToNPC("King Roald", "Talk-to", Locations.PiP_kingRoald);
        	break;
        }
        case(2):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(TrainRanged.jugOfWine))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(isInDogArena())
        	{
        		Tile ladderTile = GameObjects.closest("Ladder").getTile();
        		final Tile safespotFromLadder = new Tile((ladderTile.getX() + 1),(ladderTile.getY()-1),0);
        		if(Combatz.shouldEatFood(8)) 
        		{
        			Combatz.eatFood();
        			break;
        		}
        		if(safespotFromLadder.equals(Players.localPlayer().getTile()))
        		{
        			if(Combatz.shouldDrinkRangedBoost()) Combatz.drinkRangeBoost();
        			final int pray = Skills.getRealLevel(Skill.PRAYER);
        			if(pray >= 44)
        			{
        				if(Combatz.shouldDrinkPrayPot())
        				{
        					Combatz.drinkPrayPot();
        					break;
        				}
        				if(!Prayers.isActive(Prayer.EAGLE_EYE))
        				{
        					Prayers.toggle(true, Prayer.EAGLE_EYE);
        					break;
        				}
        			}
        			else if(pray >= 26)
        			{
        				if(Combatz.shouldDrinkPrayPot())
        				{
        					Combatz.drinkPrayPot();
        					break;
        				}
        				if(!Prayers.isActive(Prayer.HAWK_EYE))
        				{
        					Prayers.toggle(true, Prayer.HAWK_EYE);
        					break;
        				}
        			}
        			if(!Players.localPlayer().isInCombat())
        			{
        				API.interactNPC("Temple guardian","Attack", safespotFromLadder.getArea(15),true, () -> Players.localPlayer().isInCombat());
            			Sleep.sleep(696, 666);
        			}
        			break;
        		}
        		if(Walking.walk(safespotFromLadder))
        		{
        			MethodProvider.log("Walking towards safespot in dog arena!");
        			Sleep.sleep(696, 1111);
        		}
        		break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleep.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Dialogues.inDialogue());
        	Sleep.sleep(696, 666);
        	break;
        }
        case(1):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(TrainRanged.jugOfWine))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Dialogues.inDialogue());
        	break;
        }
        case(0):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(TrainRanged.jugOfWine))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_kingRoald.getCenter().distance() > 100)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkTalkToNPC("King Roald", "Talk-to", Locations.PiP_kingRoald);
        	break;
        }
        default:break;
        }
        return Timing.sleepLogNormalSleep();
    }
    private static Tile monument1 = new Tile(3417,9890,0);
    private static Tile monument2 = new Tile(3418,9894,0);
    private static Tile monument3 = new Tile(3423,9895,0);
    private static Tile monument4 = new Tile(3427,9894,0);
    private static Tile monument5 = new Tile(3428,9890,0);
    private static Tile monument6 = new Tile(3427,9885,0);
    private static Tile monument7 = new Tile(3423,9884,0);
    public static Tile studyMonumentsForCorrectTile()
    {
    	List<Tile> monuments = new ArrayList<Tile>();
    	if((int) Calculations.nextGaussianRandom(100, 50) > 90)
    	{
    		monuments.add(monument1);
    		monuments.add(monument2);
    		monuments.add(monument3);
    		monuments.add(monument4);
    		monuments.add(monument5);
    		monuments.add(monument6);
    		monuments.add(monument7);
    	} 
    	else
    	{
    		monuments.add(monument7);
    		monuments.add(monument6);
    		monuments.add(monument5);
    		monuments.add(monument4);
    		monuments.add(monument3);
    		monuments.add(monument2);
    		monuments.add(monument1);
    	}
    	MethodProvider.log("Started study function");
    	for(Tile t : monuments)
    	{
    		MethodProvider.log("New monument to study");
    		Timer timeout = new Timer(30000);
    		while(!timeout.finished() && !timeout.isPaused() && 
    				ScriptManager.getScriptManager().isRunning() && 
    				!ScriptManager.getScriptManager().isPaused())
    		{
    			Sleep.sleep(420, 696);
    			if(Widgets.getWidgetChild(272, 8) != null && 
    					Widgets.getWidgetChild(272, 8).isVisible())
    			{
    				if(Widgets.getWidgetChild(272, 8).getItemId() == ironKey)
    				{
    					if(Widgets.getWidgetChild(272, 1 , 11).interact("Close"))
        				{
    						return t;
        				}
    					continue;
    				}
    				if(Widgets.getWidgetChild(272, 1 , 11).interact("Close"))
    				{
    					Sleep.sleep(420, 696);
    					break;
    				}
    				continue;
    			}
    			GameObject monument = GameObjects.closest(g -> g != null && 
    					g.getName().contains("Monument") && 
    					g.hasAction("Study") && 
    					g.getTile().equals(t));
    			if(monument == null)
    			{
    				MethodProvider.log("Monument is null in iron key checking function!");
    				continue;
    			}
    			if(monument.interact("Study"))
    			{
    				MethodProvider.log("Studying monument");
    				MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(272, 8) != null && 
    					Widgets.getWidgetChild(272, 8).isVisible(), 
    					() -> Players.localPlayer().isMoving(),
    					Sleep.calculate(2222, 2222),69);
    			}
				continue;
    		}
    	}
    	return null;
    }
    public static boolean isInDogArena()
    {
    	return !Locations.PiP_undergroundPass.contains(Players.localPlayer()) &&
    			GameObjects.closest(g -> g!=null && g.getName().equals("Ladder") && g.hasAction("Climb-up")) != null &&
    			GameObjects.closest(g -> g!=null && g.getName().equals("Monument") && g.hasAction("Study")) != null;
    }
    public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(302);
    }
    public static boolean handleDialogues()
	{
		if(Dialogues.canContinue())
		{
			if(Dialogues.continueDialogue()) Sleep.sleep(69,696);
			return true;
		}
		if(Dialogues.isProcessing())
		{
			Sleep.sleep(420,696);
			return true;
		}
		 
		if(Dialogues.areOptionsAvailable())
		{
			return Dialogues.chooseOption("Yes.") || 
					Dialogues.chooseOption("Roald sent me to check on Drezel.") ||
					Dialogues.chooseOption("Sure. I\'m a helpful person!") ||
					Dialogues.chooseOption("So, what now?") ||
					Dialogues.chooseOption("Yes, of course.") ||
					Dialogues.chooseOption("Could you tell me more about this temple?") ||
					Dialogues.chooseOption("I\'m looking for a quest!");
		}
		return false;
	}
    public static boolean fulfillPriestInPerilFight()
    {
    	InvEquip.clearAll();
    	TrainRanged.setBestRangedEquipment();
    	InvEquip.addInvyItem(TrainRanged.rangePot4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(id.bucket, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.varrockTele, 5, (int) Calculations.nextGammaRandom(10, 2), false, (int) Calculations.nextGaussianRandom(20, 5));
    	if(Skills.getRealLevel(Skill.PRAYER) >= 26) //minimum pray for Hawk Eye, then eagle eye, then protekk melee
    	{
    		InvEquip.addInvyItem(id.prayPot4, 1, Calculations.random(2, 5), false, (int) Calculations.nextGaussianRandom(20, 5));
        }
    	InvEquip.addInvyItem(id.stamina4, 1, 2, false, 5);
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : TrainRanged.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(TrainRanged.jugOfWine, 5, 10, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (Priest in Peril fight)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (Priest in Peril fight)");
			return false;
		}
    	
    }
    public static boolean fulfillPriestInPerilRunes()
    {
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
    	InvEquip.addInvyItem(id.stamina4, 1, 2, false, 10);
    	InvEquip.addInvyItem(id.pureEss, 25, 25, false, 50);
    	InvEquip.shuffleFulfillOrder();
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (Priest in Peril runes)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (Priest in Peril runes)");
			return false;
		}
    	
    }
}
