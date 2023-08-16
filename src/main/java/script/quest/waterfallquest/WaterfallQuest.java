package script.quest.waterfallquest;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Questz;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Completes Waterfall Quest
 * @author Dreambotter420
 * ^_^
 */
public class WaterfallQuest extends Leaf {
	
    public static final int key1 = 293;
    public static final int key2 = 298;
    public static final int glarialsPebble = 294;
    public static final int glarialsUrn = 296;
    public static final int glarialsAmulet = 295;
    public static Timer spiderHitTimer = null;
    
    public static boolean completed()
    {
	    return getProgressValue() == 10;
    }
    @Override
    public int onLoop() {
    	if(completed())
    	{
    		if(exitQuest())
    		{
    			Logger.log("[COMPLETED] -> Waterfall Quest!");
                API.mode = null;
                Main.paint_task = "~~~~~~~~~~";
        		Main.paint_itemsCount = "~Quest Complete~";
        		Main.paint_subTask = "~Waterfall Quest~";
        		Main.paint_levels = "~~~~~~~~~~";
    		}
    		
            return Sleepz.sleepTiming();
    	}
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		Logger.log("[TIMEOUT] -> Waterfall Quest!");
    		if(exitQuest())
    		{
    			API.mode = null;
    		}
            return Sleepz.sleepTiming();
    	}

    	if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("Waterfall Quest");
    	if(Locations.isInKourend())
    	{
    		Walkz.exitKourend(180000);
    		return Sleepz.interactionTiming();
    	}
    	
    	if(!Walkz.isStaminated()) Walkz.drinkStamina();
    	if(!Walking.isRunEnabled() && Walkz.isStaminated() && Walking.getRunEnergy() >= 10) Walking.toggleRun();
    	if(!InvEquip.checkedBank()) return Sleepz.sleepTiming();
    	
    	switch(getProgressValue())
        { 
        
        case(8): //have completed all runes putting and amulet putting
        {
        	doLastStep();
        	break;
        }
        case(6): //entered last room at least once
        {
        	doLastStep();
        	break;
        }
        case(5): //entered last dungeon but not gotten a key yet
        {
        	doLastStep();
        	break;
        }
        case(4): //have put da pebble on da tombstone, do this until entered last dungeon
        {
        	doLastStep();
        	break;
        }
        case(3): //have read da book
        {
        	if(Inventory.contains("Book on baxtorian"))
        	{
        		if(Inventory.drop("Book on baxtorian"))
        		{
        			Sleepz.sleep(696, 696);
        		}
        		break;
        	}
        	if(Inventory.count(Combatz.lowFood) <= 0)
        	{
        		if(Combatz.shouldEatFood(8)) 
        		{
        			fulfillStart();
        			break;
        		}
        	}
        	if(Combatz.shouldEatFood(8)) Combatz.eatFood();
        	if(Inventory.count(glarialsPebble) <= 0 && Bank.contains(glarialsPebble))
        	{
        		InvEquip.withdrawOne(glarialsPebble, 180000);
        		break;
        	}
        	if(Inventory.count(glarialsPebble) > 0)
        	{
        		walkEnterGlarialsGrave();
        		break;
        	}
        	getGlarialsPebble();
        	break;
        }
        case(2): //have talked to hudon
        {
        	if(Inventory.contains("Book on baxtorian"))
        	{
        		if(Inventory.interact("Book on baxtorian","Read"))
        		{
        			Sleep.sleepUntil(() -> getProgressValue() == 3, Sleepz.calculate(4444,3333));
        		}
        		break;
        	}
        	if(Locations.hadley2.contains(Players.getLocal()))
        	{
        		Filter<GameObject> bookcaseFilter = g -> 
					g != null && 
					g.getName().equals("Bookcase") && 
					g.hasAction("Search") && 
					g.getTile().equals(new Tile(2520,3426,1));
				GameObject bookcase = GameObjects.closest(bookcaseFilter);
				if(bookcase == null)
				{
					Logger.log("bookcase is null in hadleys house 2nd floor!!");
				}
				if(bookcase.interact("Search"))
				{
					Sleep.sleepUntil(() -> Inventory.contains("Book on baxtorian"), Sleepz.calculate(4444,3333));
				}
        		break;
        	}

        	if(Locations.hadleyStairs.contains(Players.getLocal()))
        	{
        		Filter<GameObject> stairsFilter = g -> 
					g != null && 
					g.getName().equals("Staircase") && 
					g.hasAction("Climb-up");
				GameObject stairs = GameObjects.closest(stairsFilter);
				if(stairs == null)
				{
					Logger.log("stairs is null in hadleys house ground floor!!");
				}
				if(stairs.interact("Climb-up"))
				{
					Sleep.sleepUntil(() -> Locations.hadley2.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
				}
        		break;
        	}
        	if(Locations.hadleySurrounding.contains(Players.getLocal()))
        	{
        		if(Walking.shouldWalk(6) && Walking.walk(Locations.hadleyStairs.getCenter())) Sleepz.sleep(420,696);
        		break;
        	}
        	if(Locations.waterfallLedge.equals(Players.getLocal().getTile()))
        	{
        		Filter<GameObject> barrelFilter = g -> 
					g != null && 
					g.getName().equals("Barrel") && 
					g.hasAction("Get in");
				GameObject barrel = GameObjects.closest(barrelFilter);
				if(barrel == null)
				{
					Logger.log("tree is null in island2!!");
				}
				if(barrel.interact("Get in"))
				{
					Sleep.sleepUntil(() -> Locations.barrelDestination.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
				}
        		break;
        	}
        	if(Locations.waterfallIsland2.contains(Players.getLocal()))
        	{
        		Filter<GameObject> treeFilter = g -> 
					g != null && 
					g.getName().equals("Dead Tree") && 
					g.hasAction("Climb");
				GameObject tree = GameObjects.closest(treeFilter);
				if(tree == null)
				{
					Logger.log("tree is null in island2!!");
					break;
				}
				if(Inventory.get(id.rope).useOn(tree))
				{
					Sleep.sleepUntil(() -> Locations.waterfallLedge.equals(Players.getLocal().getTile()), Sleepz.calculate(8888,3333));
				}
        		break;
        	}
        	if(Locations.waterfallIsland1.contains(Players.getLocal()))
        	{
        		if(Locations.waterfallIsland1SouthTile.equals(Players.getLocal().getTile()))
        		{
        			Filter<GameObject> rockFilter = g -> 
        				g!= null && 
        				g.getName().equals("Rock") && 
        				g.hasAction("Swim to");
        			GameObject rock = GameObjects.closest(rockFilter);
        			if(rock == null)
        			{
        				Logger.log("Rock is null in island south tile!!");
        			}
        			if(Inventory.get(id.rope).useOn(rock))
        			{
        				Sleep.sleepUntil(() -> Locations.waterfallIsland2.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
        			}
        			break;
        		}
        		if(!Players.getLocal().isMoving() && Walking.walk(Locations.waterfallIsland1SouthTile)) Sleepz.sleep(420, 696);
        	}
        	break;
        }
        case(1): //have talked to almera
        {
        	if(!Inventory.contains(id.rope) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGames) ||
        			!InvEquip.invyContains(id.staminas) ||
        			!Inventory.contains(Combatz.lowFood))
        	{
        		fulfillStart();
        		break;
        	}
        	if(Dialoguez.handleDialogues())
        	{
        		Sleepz.sleep(420,696);
        		break;
        	}
    		if(Locations.waterfallIsland1.contains(Players.getLocal()))
    		{
    			NPC hudon = NPCs.closest("Hudon");
        		if(hudon == null)
        		{
        			Logger.log("Inside river island and hudon is null!");
        			Sleepz.sleep(2222,2222);
        			break;
        		}
        		if(hudon.interact("Talk-to"))
        		{
        			Sleep.sleepUntil(Dialogues::inDialogue,() -> Players.getLocal().isMoving(),Sleepz.calculate(2222,2222),50);
        		}
    		}
    		teleWalkBoardRaft();
        	Sleepz.sleep(420,696);
        	break;
        }
        case(0): //haven't started quest yet
        {
        	if(fulfillStart())
        	{
        		if(Dialoguez.handleDialogues())
            	{
            		Sleepz.sleep(420,696);
            		break;
            	}
            	if(Locations.almera.contains(Players.getLocal()))
            	{
            		NPC almera = NPCs.closest("Almera");
            		if(almera == null)
            		{
            			Logger.log("Inside almera house and almera is null!");
            			Sleepz.sleep(2222,2222);
            			break;
            		}
            		if(almera.interact("Talk-to"))
            		{
            			Sleep.sleepUntil(Dialogues::inDialogue,() -> Players.getLocal().isMoving(),Sleepz.calculate(2222,2222),50);
            		}
            	}
            	walkAlmera();
        	}
        	Sleepz.sleep(420,696);
        	break;
        }
        
        }
        return Sleepz.sleepTiming();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.WATERFALL_QUEST;
	}
	
	
	public static void walkEnterGlarialsGrave()
	{
		if(!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
				Inventory.contains(id.earthRune) || 
				Inventory.contains(id.waterRune) || 
				Inventory.contains(id.airRune))
		{
			fulfillGlarialsPebble();
		}
		if(Locations.waterfallGlarialsGravestone.contains(Players.getLocal()))
		{
			Filter<GameObject> doorF = g -> 
				g != null && 
				g.getName().equals("Glarial\'s Tombstone") && 
				g.hasAction("Read");
			GameObject door = GameObjects.closest(doorF);
			if(door == null)
			{
				Logger.log("Glarial's tombstone is null in tombstone entrance (Waterfall Quest)!!");
			}
			if(Inventory.get(glarialsPebble).useOn(door))
			{
				Sleep.sleepUntil(() -> Locations.waterfallDungeon2.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
			} 
			else if(Walking.walk(door)) Sleepz.sleep(420, 696);
			return;
		}
		if(!Walkz.walkPath(Paths.fishingGuildToGlarialsTomb) && 
				!Walkz.useJewelry(InvEquip.skills,"Fishing Guild") && 
				!Walkz.walkPath(Paths.barbarianOutpostToGlarialsTomb) && 
				!Walkz.useJewelry(InvEquip.games, "Barbarian Outpost"))
    	{
			Logger.log("Cannot tele to fishing guild or barbarian outpost :-( Trying to walk to Glarials tomb...");
			Walking.walk(Locations.waterfallGlarialsGravestone.getCenter());
    	}
    	return;	
	}
	
	public static void getGlarialsPebble()
	{
		if(Combat.isAutoRetaliateOn())
		{
			Combatz.toggleAutoRetaliate(false);
			Sleepz.sleep(111, 420);
			return;
		}
    	if(Locations.waterfallDungeon1.contains(Players.getLocal()))
    	{
    		if(Locations.waterfallDungeonGolrieRoom.contains(Players.getLocal()))
    		{
    			if(Dialoguez.handleDialogues()) return;
    			if(Inventory.isItemSelected()) Inventory.deselect();
    			NPC golrie = NPCs.closest("Golrie");
    			if(golrie == null)
    			{
    				Logger.log("Golrie is null in golrie room (Waterfall Quest)!!");
    			}
    			if(golrie.interact("Talk-to"))
    			{
    				Sleep.sleepUntil(Dialogues::inDialogue,Sleepz.calculate(8888, 3333));
    			}
    			return;
    		}
    		if(Inventory.count(key1) >= 1)
    		{
    			if(Locations.waterfallDungeonBeforeGolrieRoom.contains(Players.getLocal()))
    			{
    				Filter<GameObject> doorF = g -> 
						g != null && 
						g.getName().equals("Door") && 
						g.hasAction("Open");
					GameObject door = GameObjects.closest(doorF);
					if(door == null)
					{
						Logger.log("Door to golrie is null in dungeon (Waterfall Quest)!!");
					}
					if(Inventory.get(key1).useOn(door))
					{
						Sleep.sleepUntil(() -> Locations.waterfallDungeonGolrieRoom.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
					} else if(Walking.walk(door)) Sleepz.sleep(420, 696);
					return;
    			}
    			if(Walkz.walkPath(Paths.waterfallQuestDungeonPath1))
    			{
    				Sleepz.sleep(696, 420);
    				return;
    			}
    			Logger.log("Stuck in waterfall quest with key ...");
    			return;
    		}
    		
    		//dont have key, need key to speak to golrie
    		if(Locations.waterfallDungeonKeyRoom.contains(Players.getLocal()))
    		{
    			Filter<GameObject> boxF = g -> 
					g != null && 
					g.getName().equals("Crate") && 
					g.hasAction("Search") && 
					g.getID() == 1990 && 
					Locations.waterfallDungeonKeyRoom.contains(g);
				GameObject box = GameObjects.closest(boxF);
				if(box == null)
				{
					Logger.log("crate with key is null in key room (Waterfall Quest)!!");
					return;
				}
				if(InvEquip.free1InvySpace())
				{
					if(box.interact("Search"))
					{
						Sleep.sleepUntil(() -> Inventory.count(key1) >= 1, Sleepz.calculate(8888,3333));
					} else if(Walking.walk(box)) Sleepz.sleep(420, 696);
				}
				return;
    		}
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallDungeonKeyRoom.getCenter())) Sleepz.sleep(520,696);
    		return;
    	}
    	
    	if(Locations.gnomeStrongholdDungeonEntrance.contains(Players.getLocal()))
    	{
        	Filter<GameObject> ladderf = g -> 
				g != null && 
				g.getName().equals("Ladder") && 
				g.hasAction("Climb-down");
			GameObject ladder = GameObjects.closest(ladderf);
			if(ladder == null)
			{
				Logger.log("ladder is null in gnome stronghold maze entrance!!");
			}
			if(ladder.interact("Climb-down"))
			{
				Sleep.sleepUntil(() -> Locations.waterfallDungeon1.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
			}
			return;
    	}
    	if(Locations.hadley2.contains(Players.getLocal()))
    	{
    		Filter<GameObject> stairsFilter = g -> 
				g != null && 
				g.getName().equals("Staircase") && 
				g.hasAction("Climb-down");
			GameObject stairs = GameObjects.closest(stairsFilter);
			if(stairs == null)
			{
				Logger.log("stairs is null in hadleys house 2nd floor!!");
			}
			if(stairs.interact("Climb-down"))
			{
				Sleep.sleepUntil(() -> Locations.hadleyStairs.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
			}
    		return;
    	}
    	if(Widgets.get(392, 7) != null && 
    			Widgets.get(392, 7).isVisible())
    	{
    		if(Widgets.get(392, 7).interact("Close")) Sleepz.sleep(696,420);
    		return;
    	}

    	if(Walkz.walkPath(Paths.gnomeStrongholdMaze)) return;
    	
    	if(!Walkz.useJewelry(InvEquip.skills,"Fishing Guild"))
    	{
    		if(InvEquip.bankContains(InvEquip.wearableSkills))
    		{
    			InvEquip.withdrawOne(InvEquip.getBankItem(InvEquip.wearableSkills), 180000);
    			return;
    		}
    		InvEquip.buyItem(InvEquip.skills6, 1, 180000);
    	}
	}
	
	public static boolean exitQuest()
	{
		if(Dialoguez.handleDialogues()) return false;
		if(Widgets.get(153,16) != null && 
				Widgets.get(153,16).isVisible() && 
				Widgets.get(153,16).hasAction("Close"))
		{
			if(Widgets.get(153,16).interact("Close"))
			{
				Sleepz.sleep(2222, 3333);
			}
			return false;
		}
		if(Locations.waterfallDungeonLastAreaChanged.contains(Players.getLocal()) || 
				Locations.waterfallDungeon1.contains(Players.getLocal()) || 
				Locations.waterfallDungeon2.contains(Players.getLocal()) || 
				Locations.waterfallIsland1.contains(Players.getLocal()) || 
				Locations.waterfallIsland2.contains(Players.getLocal()) || 
				Locations.waterfallLedge.equals(Players.getLocal().getTile()))
		{
			if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") && 
					!Walkz.useJewelry(InvEquip.games, "Barbarian Outpost") && 
					!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
			{
				Logger.log("Failed to bring enough jewelry to tele out :-( Need to change tha skrept logic");
			}
			return false;
		}
		return true;
	}
	
	public static void doLastStep()
	{
		if(Inventory.count(Combatz.lowFood) <= 0)
    	{
    		if(Combatz.shouldEatFood(11)) 
    		{
    			fulfillLastStep();
        		return;
    		}
    	}
    	if(Combatz.shouldEatFood(11)) Combatz.eatFood();
    	if(Locations.waterfallDungeonLastAreaChanged.contains(Players.getLocal()))
    	{
    		if(InvEquip.freeInvySpaces(5))
    		{
    			if(Dialoguez.handleDialogues()) return;
    			GameObject chalice = GameObjects.closest("Chalice");
        		if(chalice == null)
        		{
        			Logger.log("Chalice is null in changed last room waterfall quest!");
        			return;
        		}
        		if(Inventory.get(glarialsUrn).useOn(chalice))
        		{
        			Sleep.sleepUntil(() -> chalice.distance() <= 1,Sleepz.calculate(2222,2222));
        			Sleepz.sleep(696, 2222);
        		}
    		}
			return;
    	}
    	
    	if(Bank.count(glarialsUrn) > 0)
		{
			InvEquip.withdrawOne(glarialsUrn, 180000);
			return;
		}
		if(Bank.count(glarialsAmulet) > 0)
		{
			InvEquip.withdrawOne(glarialsAmulet, 180000);
			return;
		}
		if(Inventory.count(glarialsUrn) > 0 && 
				Inventory.count(glarialsAmulet) > 0)
		{
			if(Inventory.count(id.waterRune) < 6 || 
					Inventory.count(id.earthRune) < 6 || 
					Inventory.count(id.airRune) < 6)
			{
				fulfillLastStep();
			}
			if(Locations.waterfallDungeon.contains(Players.getLocal()))
			{
				if(Locations.waterfallDungeonLastArea.contains(Players.getLocal()))
				{
					if(Camera.getPitch() >= 250) placeRunesOnPillars();
					else Camera.rotateToPitch((int) Calculations.nextGaussianRandom(308, 50));
					return;
				}
				if(Locations.waterfallDungeonLastRoomAirlock.contains(Players.getLocal()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallDungeonLastArea))
					{
						Sleepz.sleep(666, 666);
					}
					return;
				}
				if(Combat.isAutoRetaliateOn())
	    		{
	    			Combatz.toggleAutoRetaliate(false);
	    			Sleepz.sleep(111, 420);
	    			return;
	    		}
				if(Inventory.count(key2) > 0)
				{
					if(Combat.isAutoRetaliateOn())
			    	{
			    		Combatz.toggleAutoRetaliate(false);
			    	}
					if(Skills.getRealLevel(Skill.HITPOINTS) > 11)
					{
						Walking.walk(Locations.waterfallDungeonLastArea.getCenter());
						return;
					}
					if(spiderHitTimer == null || spiderHitTimer.finished())
					{
						if(Locations.waterfallDungeonFoyer.contains(Players.getLocal()))
						{
							NPC spider = NPCs.closest("Shadow spider");
							if(spider == null)
							{
								Logger.log("No spider found in region of foyer of Waterfall Dungeon (last step)");
								return;
							}
							if(spider.interact("Attack"))
							{
								Sleep.sleepUntil(() -> Players.getLocal().getInteractingCharacter() != null && 
										Players.getLocal().getInteractingCharacter().getName().contains("Shadow spider"), Sleepz.calculate(4444,2222));
								
								Sleepz.sleep(1111, 1111);
								spiderHitTimer = new Timer(10000);
							}
							return;
						}
						Walking.walk(Locations.waterfallDungeonFoyer.getCenter());
						return;
					}
					Walking.walk(Locations.waterfallDungeonLastArea.getCenter());
					return;
				}
				if(Locations.waterfallLastCrateArea.contains(Players.getLocal()))
				{
					if(!InvEquip.free1InvySpace()) return;
					GameObject crate = GameObjects.closest(g -> 
							g!=null && 
							g.getTile().equals(Locations.waterfallLastCrateTile) &&
							g.getName().contains("Crate") && 
							g.hasAction("Search"));
					if(crate == null)
					{
						Logger.log("Last key2 crate in waterfall dungeon is null!");
						return;
					}
					if(crate.interact("Search"))
					{
						Sleep.sleepUntil(() -> Inventory.count(key2) > 0, 
								() -> Players.getLocal().isMoving(),
								Sleepz.calculate(2222, 2222),50);
					}
					return;
				}
				if(Walking.walk(Locations.waterfallLastCrateArea.getCenter())) Sleepz.sleep(666, 666);
				return;
			}
			if(Locations.waterfallLedge.equals(Players.getLocal().getTile()))
        	{
        		Filter<GameObject> barrelFilter = g -> 
					g != null && 
					g.getName().equals("Door") && 
					g.hasAction("Open");
				GameObject Door = GameObjects.closest(barrelFilter);
				if(Door == null)
				{
					Logger.log("Door is null in waterallLedge!!");
				}
				if(Door.interact("Open"))
				{
					Sleep.sleepUntil(() -> Locations.barrelDestination.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
				}
        		return;
        	}
        	if(Locations.waterfallIsland2.contains(Players.getLocal()))
        	{
        		Filter<GameObject> treeFilter = g -> 
					g != null && 
					g.getName().equals("Dead Tree") && 
					g.hasAction("Climb");
				GameObject tree = GameObjects.closest(treeFilter);
				if(tree == null)
				{
					Logger.log("tree is null in island2!!");
					return;
				}
				if(Inventory.get(id.rope).useOn(tree))
				{
					Sleep.sleepUntil(() -> Locations.waterfallLedge.equals(Players.getLocal().getTile()), Sleepz.calculate(8888,3333));
				}
        		return;
        	}
			if(Locations.waterfallIsland1.contains(Players.getLocal()))
        	{
        		if(Locations.waterfallIsland1SouthTile.equals(Players.getLocal().getTile()))
        		{
        			Filter<GameObject> rockFilter = g -> 
        				g!= null && 
        				g.getName().equals("Rock") && 
        				g.hasAction("Swim to");
        			GameObject rock = GameObjects.closest(rockFilter);
        			if(rock == null)
        			{
        				Logger.log("Rock is null in island south tile!!");
        			}
        			if(Inventory.get(id.rope).useOn(rock))
        			{
        				Sleep.sleepUntil(() -> Locations.waterfallIsland2.contains(Players.getLocal()), Sleepz.calculate(8888,3333));
        			}
        			return;
        		}
        		if(!Players.getLocal().isMoving() && Walking.walk(Locations.waterfallIsland1SouthTile)) Sleepz.sleep(420, 696);
        		return;
        	}
			teleWalkBoardRaft();
			return;
		}
		
		//we do not have urn or amulet - go get them - check first to see if in the dungeon to get them
    	if(Locations.waterfallDungeon2.contains(Players.getLocal()))
    	{
    		if(Combat.isAutoRetaliateOn())
    		{
    			Combatz.toggleAutoRetaliate(false);
    			Sleepz.sleep(111, 420);
    			return;
    		}
    		if(Inventory.count(glarialsUrn) > 0 && 
    				Inventory.count(glarialsAmulet) > 0)
    		{
    			walkAlmera();
    			return;
    		}
    		if(Inventory.count(glarialsUrn) <= 0)
    		{
    			if(!InvEquip.free1InvySpace()) return;
    			if(Locations.glarialsTomb.contains(Players.getLocal()))
        		{
        			Filter<GameObject> doorF = g -> 
						g != null && 
						g.getName().equals("Glarial\'s Tomb") && 
						g.hasAction("Search");
					GameObject door = GameObjects.closest(doorF);
					if(door == null)
					{
						Logger.log("Glarial's tomb is null in tomb spot dungeon (Waterfall Quest)!!");
					}
					if(door.interact("Search"))
					{
						Sleep.sleepUntil(() -> Inventory.count(glarialsUrn) > 0, Sleepz.calculate(8888,3333));
					} else if(Walking.walk(door)) Sleepz.sleep(420, 696);
					return;
	        	}
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.glarialsTomb.getCenter())) Sleepz.sleep(696,420);
    			return;
    		}
    		if(Inventory.count(glarialsAmulet) <= 0)
    		{
    			if(!InvEquip.free1InvySpace()) return;
    			if(Locations.glarialsChest.contains(Players.getLocal()))
        		{
        			Filter<GameObject> doorF = g -> 
						g != null && 
						g.getName().equals("Chest") && 
						(g.hasAction("Search") || 
						g.hasAction("Open"));
					GameObject door = GameObjects.closest(doorF);
					if(door == null)
					{
						Logger.log("Glarial's Chest is null in chest dungeon spot (Waterfall Quest)!!");
					}
					if(door.interact("Search"))
					{
						Sleep.sleepUntil(() -> Inventory.count(glarialsAmulet) > 0, Sleepz.calculate(8888,3333));
					}
					else if(door.interact("Open"))
					{
						Sleepz.sleep(696, 420);
					}
					else if(Walking.walk(door)) Sleepz.sleep(420, 696);
					return;
	        	}
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.glarialsChest.getCenter())) Sleepz.sleep(696,420);
    			return;
    		}
    		return;
    	}
    	if(Inventory.count(glarialsPebble) > 0)
    	{
    		walkEnterGlarialsGrave();
        	return;	
    	} 
    	getGlarialsPebble();
	}
	
	public static void teleWalkBoardRaft()
	{
		if(Locations.almera.getCenter().distance() < 155)
    	{
			GameObject raft = GameObjects.closest("Log raft");
    		if(raft == null || !raft.canReach())
    		{
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallRaftTile1)) Sleepz.sleep(696,420);
    			return;
    		}
    		if(raft.interact("Board"))
    		{
    			
    			Sleep.sleepUntil(() -> Locations.waterfallIsland1.contains(Players.getLocal()),() -> Players.getLocal().isMoving(),Sleepz.calculate(2222,2222),50);
    		}
    		else if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallRaftTile1)) Sleepz.sleep(696,420);
			return;
    	}
    	if(!Walkz.useJewelry(InvEquip.games,"Barbarian Outpost"))
    	{
    		if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
    		{
    			fulfillStart();
    		}
    	}
	}

	public static final Tile pillar1 = new Tile(2569,9910,0);
	public static final Tile pillar2 = new Tile(2569,9912,0);
	public static final Tile pillar3 = new Tile(2569,9914,0);
	public static final Tile pillar4 = new Tile(2562,9910,0);
	public static final Tile pillar5 = new Tile(2562,9912,0);
	public static final Tile pillar6 = new Tile(2562,9914,0);
	
	public static void placeRunesOnPillars()
	{
		List<Integer> runes = new ArrayList<Integer>();
		List<Tile> pillars = new ArrayList<Tile>();
		runes.add(id.earthRune);
		runes.add(id.airRune);
		runes.add(id.waterRune);
		int tmp = Calculations.random(0, 100);
		if(tmp > 50) 
		{
			pillars.add(pillar1);
			pillars.add(pillar2);
			pillars.add(pillar3);
			pillars.add(pillar4);
			pillars.add(pillar5);
			pillars.add(pillar6);
		} else if(tmp > 30) 
		{
			pillars.add(pillar1);
			pillars.add(pillar2);
			pillars.add(pillar3);
			pillars.add(pillar6);
			pillars.add(pillar5);
			pillars.add(pillar4);
		} else if(tmp > 20) 
		{
			pillars.add(pillar6);
			pillars.add(pillar5);
			pillars.add(pillar4);
			pillars.add(pillar1);
			pillars.add(pillar2);
			pillars.add(pillar3);
		} else if(tmp > 5) 
		{
			pillars.add(pillar6);
			pillars.add(pillar5);
			pillars.add(pillar4);
			pillars.add(pillar3);
			pillars.add(pillar2);
			pillars.add(pillar1);
		} else if(tmp >= 0) 
		{
			pillars.add(pillar3);
			pillars.add(pillar2);
			pillars.add(pillar1);
			pillars.add(pillar4);
			pillars.add(pillar5);
			pillars.add(pillar6);
		}
		Sleepz.sleep(69, 696);
		for(Tile currentPillarTile : pillars)
		{
			GameObject currentPillar = GameObjects.closest(g -> 
					g != null && 
					g.getTile().equals(currentPillarTile) && 
					g.getName().equals("Pillar"));
			if(currentPillar != null)
			{
				Collections.shuffle(runes);
				for(int i : runes)
				{
					Timer timeout2 = new Timer(15000);
					while(!timeout2.isPaused() && !timeout2.finished() && 
							!ScriptManager.getScriptManager().isPaused() && 
							ScriptManager.getScriptManager().isRunning())
					{
						Sleepz.sleep(69, 696);
						if(Inventory.get(i).useOn(currentPillar))
						{
							Logger.log("Used rune on pillar: " + new Item(i,1).getName());
							Sleep.sleepUntil(() -> currentPillarTile.distance() <= 1, Sleepz.calculate(2222, 2222));
							Sleepz.sleep(69, 696);
							break;
						}
					}
				}
			}
		}
		Timer timeout2 = new Timer(15000);
		while(!timeout2.isPaused() && !timeout2.finished() && 
				!ScriptManager.getScriptManager().isPaused() && 
				ScriptManager.getScriptManager().isRunning())
		{
			Sleepz.sleep(69, 696);
			GameObject glarialsStatue = GameObjects.closest("Statue of Glarial");
			if(glarialsStatue == null)
			{
				Logger.log("Glarials statue is null!");
				Sleepz.sleep(69, 696);
				continue;
			}
			if(Inventory.get(glarialsAmulet).useOn(glarialsStatue))
			{
				Logger.log("Used glarials amulet on glarial's statue");
				Sleep.sleepUntil(() -> glarialsStatue.distance() <= 1, Sleepz.calculate(2222, 2222));
				Sleepz.sleep(696, 1111);
				Sleep.sleepUntil(() -> Locations.waterfallDungeonLastAreaChanged.contains(Players.getLocal()), Sleepz.calculate(3333, 2222));
				Sleepz.sleep(69, 696);
				if(Locations.waterfallDungeonLastAreaChanged.contains(Players.getLocal()))
				{
					Logger.log("Entered changed area of last room of Waterfall Quest!!");
				}
				break;
			}
		}
	}
	public static void walkAlmera()
	{
		if(Locations.almera.getCenter().distance() < 155)
    	{
			if(Walking.shouldWalk(6) && Walking.walk(Locations.almera.getCenter())) Sleepz.sleep(696,420);
    		return;
    	}
    	if(!Walkz.useJewelry(InvEquip.games,"Barbarian Outpost"))
    	{
    		if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
    		{
    			fulfillStart();
    		}
    	}
	}
	
	public static boolean fulfillStart()
	{
		Logger.log("Initiating Fulfill Start");
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.rope,1,1, false,5);
		InvEquip.addInvyItem(id.stamina4, 1, 2, false, 5);
		InvEquip.addInvyItem(InvEquip.skills, 1, 1, false, 1);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.games);

		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(Combatz.lowFood,5,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	public static boolean fulfillGlarialsPebble()
	{
		Logger.log("Initiating Fulfill GlarialsPebble");
		if(!Inventory.contains(glarialsPebble) && 
				!Bank.contains(glarialsPebble))
		{
			getGlarialsPebble();
			return false;
		}
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.stamina4, 1, 1, false, 5);
		InvEquip.addInvyItem(glarialsPebble, 1, 1, false, 0);

		InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 1);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.skills);
		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(Combatz.lowFood,3,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	
	public static boolean fulfillLastStep()
	{
		Logger.log("Initiating Fulfill LastStep");
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.rope,1,1, false,5);
		InvEquip.addInvyItem(id.stamina4, 1, 1, false, 5);
		InvEquip.addInvyItem(id.airRune, 6, (int) Calculations.nextGaussianRandom(25,10), false, 100);
		InvEquip.addInvyItem(id.waterRune, 6, (int) Calculations.nextGaussianRandom(25,10), false, 100);
		InvEquip.addInvyItem(id.earthRune, 6, (int) Calculations.nextGaussianRandom(25,10), false, 100);
		InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 1);
		InvEquip.addInvyItem(glarialsAmulet, 1, 1, false, 0);
		InvEquip.addInvyItem(key1, 1, 1, false, 0);
		InvEquip.addInvyItem(glarialsPebble, 1, 1, false, 0);
		InvEquip.addInvyItem(glarialsUrn, 1, 1, false, 0);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.skills);
		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(Combatz.lowFood,3,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	public static int getProgressValue()
	{
		return PlayerSettings.getConfig(65);
	}
	
}
