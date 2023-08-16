package script.quest.priestinperil;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;
import script.utilities.Tabz;

import java.util.ArrayList;
import java.util.List;
/**
 * Completes Priest in Peril
 * @author Dreambotter420
 * ^_^
 */
public class PriestInPeril extends Leaf {
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
    public static boolean completed()
    {
    	if(getProgressValue() == 61)
        {
        	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") &&
        				!Walkz.useJewelry(InvEquip.glory, "Edgeville"))
        		{
        			Walkz.teleportVarrock(30000);
        		}
        		return false;
        	}
        	return true;
        }
    	return false;
    }
    @Override
    public int onLoop() {
    	if (DecisionLeaf.taskTimer.finished()) {
            Logger.log("[TIMEOUT] -> The Restless Ghost");
           	API.mode = null;
            return Sleepz.sleepTiming();
        }
        if (completed()) {
            Logger.log("[COMPLETED] -> Priest in Peril!");
           	API.mode = null;
           	Main.paint_task = "~~~~~~~~~~";
    		Main.paint_itemsCount = "~Quest Complete~";
    		Main.paint_subTask = "~Priest in Peril~";
    		Main.paint_levels = "~~~~~~~~~~";
            return Sleepz.sleepTiming();
        }

    	if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("Priest in Peril");
        if(Dialogues.getNPCDialogue() != null && !Dialogues.getNPCDialogue().isEmpty())
    	{
    		Logger.log("NPC Dialogue: " + Dialogues.getNPCDialogue());
    	}
        if(Dialoguez.handleDialogues()) return Sleepz.interactionTiming();
        if(Inventory.contains(TrainRanged.getBestDart()))
        {
        	InvEquip.equipItem(TrainRanged.getBestDart());
        	return Sleepz.sleepTiming();
        }
        
        if(getProgressValue() == 60)
        {
        	//quest done widget - not really have access to morytania until progress value is 61 ... talk to drezel some more...
        	if(Widgets.get(153,16) != null &&
        			Widgets.get(153,16).isVisible())
        	{
        		if(Widgets.get(153,16).interact("Close")) Sleepz.sleep(696, 666);
        		return Sleepz.interactionTiming();
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
        	{
        		API.walkTalkToNPC("Drezel", "Talk-to", Locations.PiP_undergroundPassDrezel);
        		return Sleepz.sleepTiming();
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		return Sleepz.sleepTiming();
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		return Sleepz.sleepTiming();
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()))
        	{
        		Logger.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        		Sleepz.sleep(696, 1111);
        		return Sleepz.sleepTiming();
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		return Sleepz.sleepTiming();
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleepz.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.getLocal()));
        	Sleepz.sleep(696, 666);
        	return Sleepz.sleepTiming();
        }
        if(getProgressValue() >= 8)
        {
        	deliverRunes();
        	return Sleepz.sleepTiming();
        }
        switch(getProgressValue())
        {
        case(7):
        {
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()))
        	{
        		API.interactNPC("Drezel", "Talk-to",Locations.PiP_pastramiChurchLvl3, false,() -> Dialogues.inDialogue());
    			Sleepz.sleepInteraction();
    			break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()))
        	{
        		Logger.log("Interacting staircase");
    			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_undergroundPassLadder, () -> Locations.PiP_trapdoor.contains(Players.getLocal()));
        		break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
    		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
    		break;
        }
        case(6):
        {
        	if(!Inventory.contains(id.bucket) && !Inventory.contains(unblessedBucket) && !Inventory.contains(blessedBucket))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()))
        	{
        		if(Inventory.contains(unblessedBucket))
        		{
        			API.interactNPC("Drezel", "Talk-to",Locations.PiP_pastramiChurchLvl3, false,() -> Dialogues.inDialogue());
        			Sleepz.sleepInteraction();
        			break;
        		}
        		if(Inventory.contains(blessedBucket))
        		{
        			GameObject coffin = GameObjects.closest("Coffin");
        			if(coffin.canReach())
        			{
        				if(Inventory.get(blessedBucket).useOn(GameObjects.closest("Coffin")))
            			{
            				Sleep.sleepUntil(() -> !Inventory.contains(blessedBucket),
            						() -> Players.getLocal().isMoving(),
            						Sleepz.calculate(2222, 2222),69);
            			}
            			Sleepz.sleepInteraction();
            			break;
        			}
        			if(GameObjects.closest("Cell door").interact("Open")) 
        			{
        				Sleep.sleepUntil(() -> coffin.canReach(), Sleepz.calculate(3333, 3333));
        			}
        			break;
        		}
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()))
        	{
        		if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        		{
        			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()));
            		Sleepz.sleepInteraction();
            		break;
        		}
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()))
        	{
        		if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        		{
            		Logger.log("Interacting staircase");
        			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
            		Sleepz.sleepInteraction();
            		break;
        		}
        		Logger.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        		Sleepz.sleep(696, 1111);
        		break;
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
        	{
        		if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        		{
        			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_undergroundPassLadder, () -> Locations.PiP_trapdoor.contains(Players.getLocal()));
            		break;
        		}
        		if(Locations.PiP_undergroundPassMonuments.contains(Players.getLocal()))
    			{
    				if(Inventory.get(id.bucket).useOn(GameObjects.closest(g->g!=null&&g.getName().contains("Well")&&g.hasAction("Search"))))
    				{
    					Sleep.sleepUntil(() -> Inventory.contains(unblessedBucket),
    							() ->Players.getLocal().isMoving(),
    							Sleepz.calculate(2222,2222),69);
    				}
    				break;
    			}
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassMonuments.getCenter())) Sleepz.sleep(696, 666);
    			break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	if(Inventory.contains(unblessedBucket) || Inventory.contains(blessedBucket))
        	{
        		Logger.log("Interacting doors");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        		break;
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleepz.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.getLocal()));
        	Sleepz.sleep(696, 666);
        	break;
        }
        case(5):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(Combatz.lowFood))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()))
        	{
        		if(Inventory.contains(ironKey))
        		{
        			API.walkInteractWithGameObject("Cell door", "Open", Locations.PiP_pastramiChurchLvl3, () -> Dialogues.inDialogue());
        			Sleepz.sleepInteraction();
        			break;
        		}
        		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()))
        	{
        		if(Inventory.contains(ironKey))
        		{
        			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()));
            		Sleepz.sleepInteraction();
            		break;
        		}
        		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()))
        	{
        		Logger.log("Inside pastrami church lvl 1");
        		if(Inventory.contains(ironKey))
        		{
        			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
            		Sleepz.sleepInteraction();
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
            				Sleep.sleepUntil(() -> Inventory.contains(goldenKey), Sleepz.calculate(2222, 2222));
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
                			Tabz.open(Tab.PRAYER);
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
                			Tabz.open(Tab.PRAYER);
        					Prayers.toggle(true, Prayer.HAWK_EYE);
        					break;
        				}
        			}
        			if(!Players.getLocal().isInCombat())
        			{
        				Sleepz.sleep(1111, 1111);
        				if(Players.getLocal().isInCombat()) break;
        				Logger.log("Attacking monk of zammy!");
        				API.interactNPC("Monk of Zamorak","Attack", 30,Locations.PiP_pastramiChurchLvl1,true, () -> Players.getLocal().isInCombat());
            			Sleepz.sleep(696, 666);
        			}
        			break;
        		}

        		Logger.log("Interacting doors! church lvl 1");
        		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        		Sleepz.sleep(696, 666);
    			break;
        	}
        	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
        	{
        		if(Inventory.contains(goldenKey))
        		{
        			if(Locations.PiP_undergroundPassMonuments.contains(Players.getLocal()))
        			{
        				//close item looking interface
        				if(Widgets.get(272, 1 , 11) != null && 
        						Widgets.get(272, 1 , 11).isVisible())
        				{
        					if(Widgets.get(272, 1 , 11).interact("Close"))
    						{
    							Sleepz.sleep(111, 696);
    						}
    						break;
        				}
        				if(correctMonument != null)
        				{
        					GameObject correctOne = GameObjects.closest(g -> g!=null && 
        							g.getName().equals("Monument") && 
        							g.hasAction("Take-from") && 
        							g.getTile().equals(correctMonument));
        					if(correctOne == null)
        					{
        						Logger.log("Found correct monument but its friggen null!!!!");
        						break;
        					}
        					if(Inventory.get(goldenKey).useOn(correctOne))
        					{
        						Sleep.sleepUntil(() -> Inventory.contains(ironKey),() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),69);
        					}
        					break;
        				}
        				correctMonument = studyMonumentsForCorrectTile();
        				
        				break;
        			}
        			if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassMonuments.getCenter())) Sleepz.sleep(696, 666);
        			break;
        		}
        		if(Inventory.contains(id.bucket))
        		{
        			if(Locations.PiP_undergroundPassMonuments.contains(Players.getLocal()))
        			{
        				if(Inventory.get(id.bucket).useOn(GameObjects.closest(g->g!=null&&g.getName().contains("Well")&&g.hasAction("Search"))))
        				{
        					Sleep.sleepUntil(() -> Inventory.contains(unblessedBucket),
        							() ->Players.getLocal().isMoving(),
        							Sleepz.calculate(2222,2222),69);
        				}
        				break;
        			}
        			if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassMonuments.getCenter())) Sleepz.sleep(696, 666);
        			break;
        		}
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_undergroundPassLadder, () -> Locations.PiP_trapdoor.contains(Players.getLocal()));
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
            	Sleepz.sleep(696, 666);
            	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.getLocal()));
            	Sleepz.sleep(696, 666);
            	break;
        	}
        	API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
    		break;
        }
        case(4):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(Combatz.lowFood))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()))
        	{
        		API.interactNPC("Drezel", "Talk-to", Locations.PiP_pastramiChurchLvl3, false,() -> Dialogues.inDialogue());
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	if(Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.PiP_pastramiChurchLvl1, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
        		Sleepz.sleepInteraction();
        		break;
        	}
        	
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
        	break;
        }
        case(3):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(Combatz.lowFood))
        	{
        		fulfillPriestInPerilFight();
        		break;
        	}
        	final int pray = Skills.getRealLevel(Skill.PRAYER);
        	if(pray >= 44)
			{
        		if(Prayers.isActive(Prayer.EAGLE_EYE))
				{
        			Tabz.open(Tab.PRAYER);
					Prayers.toggle(false, Prayer.EAGLE_EYE);
					break;
				}
			}
			else if(pray >= 26)
			{
				if(Prayers.isActive(Prayer.HAWK_EYE))
				{
        			Tabz.open(Tab.PRAYER);
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
            		API.walkInteractWithGameObject("Ladder", "Climb-up", ladderTile.getArea(6), () -> Locations.PiP_trapdoor.contains(Players.getLocal()));
            	}
        		Sleepz.sleep(420, 1111);
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
        			!Inventory.contains(Combatz.lowFood))
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
        		if(safespotFromLadder.equals(Players.getLocal().getTile()))
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
                			Tabz.open(Tab.PRAYER);
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
                			Tabz.open(Tab.PRAYER);
        					Prayers.toggle(true, Prayer.HAWK_EYE);
        					break;
        				}
        			}
        			if(!Players.getLocal().isInCombat())
        			{
        				API.interactNPC("Temple guardian","Attack", safespotFromLadder.getArea(15),true, () -> Players.getLocal().isInCombat());
            			Sleepz.sleep(696, 666);
        			}
        			break;
        		}
        		if(Walking.walk(safespotFromLadder))
        		{
        			Logger.log("Walking towards safespot in dog arena!");
        			Sleepz.sleep(696, 1111);
        		}
        		break;
        	}
        	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
        	{
        		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
        		break;
        	}
        	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
        	Sleepz.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Dialogues.inDialogue());
        	Sleepz.sleep(696, 666);
        	break;
        }
        case(1):
        {
        	if(!Equipment.contains(TrainRanged.getBestDart()) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(Combatz.lowFood))
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
        			!Inventory.contains(Combatz.lowFood))
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
        return Sleepz.sleepTiming();
    }
    public static void deliverRunes()
    {
    	if(!Inventory.contains(id.pureEss))
    	{
    		fulfillPriestInPerilRunes();
    		return;
    	}
    	if(Locations.PiP_pastramiChurchLvl3.contains(Players.getLocal()))
    	{
    		API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.PiP_pastramiChurchLvl3, () -> Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()));
    		Sleepz.sleepInteraction();
    		return;
    	}
    	if(Locations.PiP_pastramiChurchLvl2.contains(Players.getLocal()))
    	{
    		API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.PiP_pastramiChurchLvl2, () -> Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
    		Sleepz.sleepInteraction();
    		return;
    	}
    	if(Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()))
    	{
    		Logger.log("Interacting doors! church lvl 1");
    		API.walkInteractWithGameObject("Large door", "Open", Locations.PiP_largeDoors, () -> !Locations.PiP_pastramiChurchLvl1.contains(Players.getLocal()));
    		Sleepz.sleep(696, 1111);
    		return;
    	}
    	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
    	{
    		API.walkTalkToNPC("Drezel", "Talk-to", Locations.PiP_undergroundPassDrezel);
    		return;
    	}
    	if(Locations.PiP_largeDoors.getCenter().distance() > 265)
    	{
    		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) Walkz.teleportVarrock(180000);
    		return;
    	}
    	API.walkInteractWithGameObject("Trapdoor", "Open", Locations.PiP_trapdoor, () -> GameObjects.closest(g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down")) != null);
    	Sleepz.sleep(696, 666);
    	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.PiP_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.getLocal()));
    	Sleepz.sleep(696, 666);
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
    	Logger.log("Starting study function");
    	List<Tile> monuments = new ArrayList<Tile>();
    	if((int) Calculations.nextGaussianRandom(100, 50) > 90)
    	{
    		Logger.log("Searching monuments clockwise!");
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
    		Logger.log("Searching monuments counter-clockwise!");
    		monuments.add(monument7);
    		monuments.add(monument6);
    		monuments.add(monument5);
    		monuments.add(monument4);
    		monuments.add(monument3);
    		monuments.add(monument2);
    		monuments.add(monument1);
    	}
    	for(Tile t : monuments)
    	{
    		Logger.log("Studying new monument!");
    		Timer timeout = new Timer(30000);
    		while(!timeout.finished() && !timeout.isPaused() && 
    				ScriptManager.getScriptManager().isRunning() && 
    				!ScriptManager.getScriptManager().isPaused())
    		{
    			Sleepz.sleep(420, 696);
    			if(Widgets.get(272, 8) != null && 
    					Widgets.get(272, 8).isVisible())
    			{
    				if(Widgets.get(272, 8).getItemId() == ironKey)
    				{
    					return t;
    				}
    				if(Widgets.get(272, 1 , 11).interact("Close"))
    				{
    					Sleepz.sleep(420, 696);
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
    				Logger.log("Monument is null in iron key checking function!");
    				continue;
    			}
    			if(monument.interact("Study"))
    			{
    				Logger.log("Studying monument");
    				Sleep.sleepUntil(() -> Widgets.get(272, 8) != null && 
    					Widgets.get(272, 8).isVisible(), 
    					() -> Players.getLocal().isMoving(),
    					Sleepz.calculate(2222, 2222),69);
    			}
				continue;
    		}
    	}
    	return null;
    }
    public static boolean isInDogArena()
    {
    	return !Locations.PiP_undergroundPass.contains(Players.getLocal()) &&
    			GameObjects.closest(g -> g!=null && g.getName().equals("Ladder") && g.hasAction("Climb-up")) != null &&
    			GameObjects.closest(g -> g!=null && g.getName().equals("Monument") && g.hasAction("Study")) != null;
    }
    public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(302);
    }
  
    public static boolean fulfillPriestInPerilFight()
    {
    	InvEquip.clearAll();
    	TrainRanged.setBestRangedEquipment();
    	InvEquip.addInvyItem(id.rangePot4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
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
    	for(int r : id.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 5, 10, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			Logger.log("[INVEQUIP] -> Fulfilled equipment correctly! (Priest in Peril fight)");
			return true;
		} else 
		{
			Logger.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (Priest in Peril fight)");
			return false;
		}
    	
    }
    public static boolean fulfillPriestInPerilRunes()
    {
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
    	InvEquip.addInvyItem(id.stamina4, 1, 2, false, 10);
    	InvEquip.addInvyItem(id.varrockTele, 1, 2, false, 10);
    	InvEquip.addInvyItem(id.pureEss, 25, 25, false, 50);
    	InvEquip.shuffleFulfillOrder();
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			Logger.log("[INVEQUIP] -> Fulfilled equipment correctly! (Priest in Peril runes)");
			return true;
		} else 
		{
			Logger.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (Priest in Peril runes)");
			return false;
		}
    	
    }
}
