package script.quest.waterfallquest;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
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
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

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
import script.utilities.Paths;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Waterfall Quest
 * @author Dreambotter420
 * ^_^
 */
public class WaterfallQuest extends Leaf {
	public static boolean started = false;
	public static boolean completedWaterfallQuest = false;
    public void onStart() {
        
        instantiateTree();
        started = true;
    }

    private final Tree tree = new Tree();
    private void instantiateTree() {
        
    }
    
    public static final int key1 = 293;
    public static final int key2 = 298;
    public static final int glarialsPebble = 294;
    public static final int glarialsUrn = 296;
    public static final int glarialsAmulet = 295;
    public static Timer spiderHitTimer = null;
    
    @Override
    public int onLoop() {
    	if(completedWaterfallQuest)
    	{
    		if(exitQuest())
    		{
    			MethodProvider.log("[COMPLETED] -> Waterfall Quest!");
                API.mode = null;
                Main.customPaintText1 = "~~~~~~~~~~";
        		Main.customPaintText2 = "~Quest Complete~";
        		Main.customPaintText3 = "~Waterfall Quest~";
        		Main.customPaintText4 = "~~~~~~~~~~";
    		}
    		
            return Timing.sleepLogNormalSleep();
    	}
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Waterfall Quest!");
    		if(exitQuest())
    		{
    			API.mode = null;
    		}
            return Timing.sleepLogNormalSleep();
    	}
    	if(Locations.isInKourend())
    	{
    		Walkz.leaveKourend(180000);
    		return Timing.sleepLogNormalInteraction();
    	}
    	
    	if(!Walkz.isStaminated()) Walkz.drinkStamina();
    	if(!Walking.isRunEnabled() && Walking.getRunEnergy() >= 20) Walking.toggleRun();
    	if(!InvEquip.checkedBank()) return Timing.sleepLogNormalSleep();
    	switch(getProgressValue())
        { 
        case(10): //have completed quest - still maybe in room
        {
        	completedWaterfallQuest = true;
        	break;
        }
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
        			Sleep.sleep(696, 696);
        		}
        		break;
        	}
        	if(Inventory.count(TrainRanged.jugOfWine) <= 0)
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
        			MethodProvider.sleepUntil(() -> getProgressValue() == 3, Sleep.calculate(4444,3333));
        		}
        		break;
        	}
        	if(Locations.hadley2.contains(Players.localPlayer()))
        	{
        		Filter<GameObject> bookcaseFilter = g -> 
					g != null && 
					g.getName().equals("Bookcase") && 
					g.hasAction("Search") && 
					g.getTile().equals(new Tile(2520,3426,1));
				GameObject bookcase = GameObjects.closest(bookcaseFilter);
				if(bookcase == null)
				{
					MethodProvider.log("bookcase is null in hadleys house 2nd floor!!");
				}
				if(bookcase.interact("Search"))
				{
					MethodProvider.sleepUntil(() -> Inventory.contains("Book on baxtorian"), Sleep.calculate(4444,3333));
				}
        		break;
        	}

        	if(Locations.hadleyStairs.contains(Players.localPlayer()))
        	{
        		Filter<GameObject> stairsFilter = g -> 
					g != null && 
					g.getName().equals("Staircase") && 
					g.hasAction("Climb-up");
				GameObject stairs = GameObjects.closest(stairsFilter);
				if(stairs == null)
				{
					MethodProvider.log("stairs is null in hadleys house ground floor!!");
				}
				if(stairs.interact("Climb-up"))
				{
					MethodProvider.sleepUntil(() -> Locations.hadley2.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
				}
        		break;
        	}
        	if(Locations.hadleySurrounding.contains(Players.localPlayer()))
        	{
        		if(Walking.shouldWalk(6) && Walking.walk(Locations.hadleyStairs.getCenter())) Sleep.sleep(420,696);
        		break;
        	}
        	if(Locations.waterfallLedge.equals(Players.localPlayer().getTile()))
        	{
        		Filter<GameObject> barrelFilter = g -> 
					g != null && 
					g.getName().equals("Barrel") && 
					g.hasAction("Get in");
				GameObject barrel = GameObjects.closest(barrelFilter);
				if(barrel == null)
				{
					MethodProvider.log("tree is null in island2!!");
				}
				if(barrel.interact("Get in"))
				{
					MethodProvider.sleepUntil(() -> Locations.barrelDestination.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
				}
        		break;
        	}
        	if(Locations.waterfallIsland2.contains(Players.localPlayer()))
        	{
        		Filter<GameObject> treeFilter = g -> 
					g != null && 
					g.getName().equals("Dead Tree") && 
					g.hasAction("Climb");
				GameObject tree = GameObjects.closest(treeFilter);
				if(tree == null)
				{
					MethodProvider.log("tree is null in island2!!");
					break;
				}
				if(Inventory.get(id.rope).useOn(tree))
				{
					MethodProvider.sleepUntil(() -> Locations.waterfallLedge.equals(Players.localPlayer().getTile()), Sleep.calculate(8888,3333));
				}
        		break;
        	}
        	if(Locations.waterfallIsland1.contains(Players.localPlayer()))
        	{
        		if(Locations.waterfallIsland1SouthTile.equals(Players.localPlayer().getTile()))
        		{
        			Filter<GameObject> rockFilter = g -> 
        				g!= null && 
        				g.getName().equals("Rock") && 
        				g.hasAction("Swim to");
        			GameObject rock = GameObjects.closest(rockFilter);
        			if(rock == null)
        			{
        				MethodProvider.log("Rock is null in island south tile!!");
        			}
        			if(Inventory.get(id.rope).useOn(rock))
        			{
        				MethodProvider.sleepUntil(() -> Locations.waterfallIsland2.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
        			}
        			break;
        		}
        		if(!Players.localPlayer().isMoving() && Walking.walk(Locations.waterfallIsland1SouthTile)) Sleep.sleep(420, 696);
        	}
        	break;
        }
        case(1): //have talked to almera
        {
        	if(!Inventory.contains(id.rope) || 
        			!InvEquip.equipmentContains(InvEquip.wearableWealth) ||
        			!InvEquip.equipmentContains(InvEquip.wearableGames) ||
        			!InvEquip.invyContains(id.staminas) ||
        			!Inventory.contains(TrainRanged.jugOfWine))
        	{
        		fulfillStart();
        		break;
        	}
        	if(handleDialogues())
        	{
        		Sleep.sleep(420,696);
        		break;
        	}
    		if(Locations.waterfallIsland1.contains(Players.localPlayer()))
    		{
    			NPC hudon = NPCs.closest("Hudon");
        		if(hudon == null)
        		{
        			MethodProvider.log("Inside river island and hudon is null!");
        			Sleep.sleep(2222,2222);
        			break;
        		}
        		if(hudon.interact("Talk-to"))
        		{
        			MethodProvider.sleepUntil(Dialogues::inDialogue,() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222),50);
        		}
    		}
    		teleWalkBoardRaft();
        	Sleep.sleep(420,696);
        	break;
        }
        case(0): //haven't started quest yet
        {
        	if(fulfillStart())
        	{
        		if(handleDialogues())
            	{
            		Sleep.sleep(420,696);
            		break;
            	}
            	if(Locations.almera.contains(Players.localPlayer()))
            	{
            		NPC almera = NPCs.closest("Almera");
            		if(almera == null)
            		{
            			MethodProvider.log("Inside almera house and almera is null!");
            			Sleep.sleep(2222,2222);
            			break;
            		}
            		if(almera.interact("Talk-to"))
            		{
            			MethodProvider.sleepUntil(Dialogues::inDialogue,() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222),50);
            		}
            	}
            	walkAlmera();
        	}
        	Sleep.sleep(420,696);
        	break;
        }
        
        }
        return Timing.sleepLogNormalSleep();
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
		if(Locations.waterfallGlarialsGravestone.contains(Players.localPlayer()))
		{
			Filter<GameObject> doorF = g -> 
				g != null && 
				g.getName().equals("Glarial\'s Tombstone") && 
				g.hasAction("Read");
			GameObject door = GameObjects.closest(doorF);
			if(door == null)
			{
				MethodProvider.log("Glarial's tombstone is null in tombstone entrance (Waterfall Quest)!!");
			}
			if(Inventory.get(glarialsPebble).useOn(door))
			{
				MethodProvider.sleepUntil(() -> Locations.waterfallDungeon2.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
			} 
			else if(Walking.walk(door)) Sleep.sleep(420, 696);
			return;
		}
		if(!Walkz.walkPath(Paths.fishingGuildToGlarialsTomb) && 
				!Walkz.useJewelry(InvEquip.skills,"Fishing Guild") && 
				!Walkz.walkPath(Paths.barbarianOutpostToGlarialsTomb) && 
				!Walkz.useJewelry(InvEquip.games, "Barbarian Outpost"))
    	{
			MethodProvider.log("Cannot tele to fishing guild or barbarian outpost :-( Trying to walk to Glarials tomb...");
			Walking.walk(Locations.waterfallGlarialsGravestone.getCenter());
    	}
    	return;	
	}
	
	public static void getGlarialsPebble()
	{
    	if(Locations.waterfallDungeon1.contains(Players.localPlayer()))
    	{
    		if(Locations.waterfallDungeonGolrieRoom.contains(Players.localPlayer()))
    		{
    			if(handleDialogues()) return;
    			if(Inventory.isItemSelected()) Inventory.deselect();
    			NPC golrie = NPCs.closest("Golrie");
    			if(golrie == null)
    			{
    				MethodProvider.log("Golrie is null in golrie room (Waterfall Quest)!!");
    			}
    			if(golrie.interact("Talk-to"))
    			{
    				MethodProvider.sleepUntil(Dialogues::inDialogue,Sleep.calculate(8888, 3333));
    			}
    			return;
    		}
    		if(Inventory.count(key1) >= 1)
    		{
    			if(Locations.waterfallDungeonBeforeGolrieRoom.contains(Players.localPlayer()))
    			{
    				Filter<GameObject> doorF = g -> 
						g != null && 
						g.getName().equals("Door") && 
						g.hasAction("Open");
					GameObject door = GameObjects.closest(doorF);
					if(door == null)
					{
						MethodProvider.log("Door to golrie is null in dungeon (Waterfall Quest)!!");
					}
					if(Inventory.get(key1).useOn(door))
					{
						MethodProvider.sleepUntil(() -> Locations.waterfallDungeonGolrieRoom.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
					} else if(Walking.walk(door)) Sleep.sleep(420, 696);
					return;
    			}
    			if(Walkz.walkPath(Paths.waterfallQuestDungeonPath1))
    			{
    				Sleep.sleep(696, 420);
    				return;
    			}
    			MethodProvider.log("Stuck in waterfall quest with key ...");
    			return;
    		}
    		
    		//dont have key, need key to speak to golrie
    		if(Locations.waterfallDungeonKeyRoom.contains(Players.localPlayer()))
    		{
    			Filter<GameObject> boxF = g -> 
					g != null && 
					g.getName().equals("Crate") && 
					g.hasAction("Search");
				GameObject box = GameObjects.closest(boxF);
				if(box == null)
				{
					MethodProvider.log("crate with key is null in key room (Waterfall Quest)!!");
				}
				if(box.interact("Search"))
				{
					MethodProvider.sleepUntil(() -> Inventory.count(key1) >= 1, Sleep.calculate(8888,3333));
				} else if(Walking.walk(box)) Sleep.sleep(420, 696);
				return;
    		}
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallDungeonKeyRoom.getCenter())) Sleep.sleep(520,696);
    		return;
    	}
    	
    	if(Locations.gnomeStrongholdDungeonEntrance.contains(Players.localPlayer()))
    	{
        	Filter<GameObject> ladderf = g -> 
				g != null && 
				g.getName().equals("Ladder") && 
				g.hasAction("Climb-down");
			GameObject ladder = GameObjects.closest(ladderf);
			if(ladder == null)
			{
				MethodProvider.log("ladder is null in gnome stronghold maze entrance!!");
			}
			if(ladder.interact("Climb-down"))
			{
				MethodProvider.sleepUntil(() -> Locations.waterfallDungeon1.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
			}
			return;
    	}
    	if(Locations.hadley2.contains(Players.localPlayer()))
    	{
    		Filter<GameObject> stairsFilter = g -> 
				g != null && 
				g.getName().equals("Staircase") && 
				g.hasAction("Climb-down");
			GameObject stairs = GameObjects.closest(stairsFilter);
			if(stairs == null)
			{
				MethodProvider.log("stairs is null in hadleys house 2nd floor!!");
			}
			if(stairs.interact("Climb-down"))
			{
				MethodProvider.sleepUntil(() -> Locations.hadleyStairs.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
			}
    		return;
    	}
    	if(Widgets.getWidgetChild(392, 7) != null && 
    			Widgets.getWidgetChild(392, 7).isVisible())
    	{
    		if(Widgets.getWidgetChild(392, 7).interact("Close")) Sleep.sleep(696,420);
    		return;
    	}

    	if(Walkz.walkPath(Paths.gnomeStrongholdMaze))
    	{
    		return;
    	}
    	
    	if(!Walkz.useJewelry(InvEquip.skills,"Fishing Guild"))
    	{
    		if(!InvEquip.checkedBank()) return;
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
		if(handleDialogues()) return false;
		if(Widgets.getWidgetChild(153,16) != null && 
				Widgets.getWidgetChild(153,16).isVisible() && 
				Widgets.getWidgetChild(153,16).hasAction("Close"))
		{
			if(Widgets.getWidgetChild(153,16).interact("Close"))
			{
				Sleep.sleep(2222, 3333);
			}
			return false;
		}
		if(Locations.waterfallDungeonLastAreaChanged.contains(Players.localPlayer()) || 
				Locations.waterfallDungeon1.contains(Players.localPlayer()) || 
				Locations.waterfallDungeon2.contains(Players.localPlayer()) || 
				Locations.waterfallIsland1.contains(Players.localPlayer()) || 
				Locations.waterfallIsland2.contains(Players.localPlayer()) || 
				Locations.waterfallLedge.equals(Players.localPlayer().getTile()))
		{
			if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") && 
					!Walkz.useJewelry(InvEquip.games, "Barbarian Outpost") && 
					!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
			{
				MethodProvider.log("Failed to bring enough jewelry to tele out :-( Need to change tha skrept logic");
			}
			return false;
		}
		return true;
	}
	
	public static void doLastStep()
	{
		if(Inventory.count(TrainRanged.jugOfWine) <= 0)
    	{
    		if(Combatz.shouldEatFood(11)) 
    		{
    			fulfillLastStep();
        		return;
    		}
    	}
    	if(Combatz.shouldEatFood(11)) Combatz.eatFood();
    	
    	if(Locations.waterfallDungeonLastAreaChanged.contains(Players.localPlayer()))
    	{
    		if(InvEquip.freeInvySpaces(5))
    		{
    			if(handleDialogues()) return;
    			GameObject chalice = GameObjects.closest("Chalice");
        		if(chalice == null)
        		{
        			MethodProvider.log("Chalice is null in changed last room waterfall quest!");
        			return;
        		}
        		if(Inventory.get(glarialsUrn).useOn(chalice))
        		{
        			MethodProvider.sleepUntil(() -> chalice.distance() <= 1,Sleep.calculate(2222,2222));
        			Sleep.sleep(696, 2222);
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
			if(Locations.waterfallDungeon.contains(Players.localPlayer()))
			{
				if(Locations.waterfallDungeonLastArea.contains(Players.localPlayer()))
				{
					if(Camera.getPitch() >= 250) placeRunesOnPillars();
					else Camera.rotateToPitch((int) Calculations.nextGaussianRandom(308, 50));
					return;
				}
				if(Locations.waterfallDungeonLastRoomAirlock.contains(Players.localPlayer()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallDungeonLastArea))
					{
						Sleep.sleep(666, 666);
					}
					return;
				}
				if(Inventory.count(key2) > 0)
				{
					if(Combat.isAutoRetaliateOn())
			    	{
			    		Combat.toggleAutoRetaliate(false);
			    	}
					if(Skills.getRealLevel(Skill.HITPOINTS) > 11)
					{
						Walking.walk(Locations.waterfallDungeonLastArea.getCenter());
						return;
					}
					if(spiderHitTimer == null || spiderHitTimer.finished())
					{
						if(Locations.waterfallDungeonFoyer.contains(Players.localPlayer()))
						{
							NPC spider = NPCs.closest("Shadow spider");
							if(spider == null)
							{
								MethodProvider.log("No spider found in region of foyer of Waterfall Dungeon (last step)");
								return;
							}
							if(spider.interact("Attack"))
							{
								MethodProvider.sleepUntil(() -> Players.localPlayer().getInteractingCharacter() != null && 
										Players.localPlayer().getInteractingCharacter().getName().contains("Shadow spider"), Sleep.calculate(4444,2222));
								
								Sleep.sleep(1111, 1111);
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
				if(Locations.waterfallLastCrateArea.contains(Players.localPlayer()))
				{
					if(!InvEquip.free1InvySpace()) return;
					GameObject crate = GameObjects.closest(g -> 
							g!=null && 
							g.getTile().equals(Locations.waterfallLastCrateTile) &&
							g.getName().contains("Crate") && 
							g.hasAction("Search"));
					if(crate == null)
					{
						MethodProvider.log("Last key2 crate in waterfall dungeon is null!");
						return;
					}
					if(crate.interact("Search"))
					{
						MethodProvider.sleepUntil(() -> Inventory.count(key2) > 0, 
								() -> Players.localPlayer().isMoving(),
								Sleep.calculate(2222, 2222),50);
					}
					return;
				}
				if(Walking.walk(Locations.waterfallLastCrateArea.getCenter())) Sleep.sleep(666, 666);
				return;
			}
			if(Locations.waterfallLedge.equals(Players.localPlayer().getTile()))
        	{
        		Filter<GameObject> barrelFilter = g -> 
					g != null && 
					g.getName().equals("Door") && 
					g.hasAction("Open");
				GameObject Door = GameObjects.closest(barrelFilter);
				if(Door == null)
				{
					MethodProvider.log("Door is null in waterallLedge!!");
				}
				if(Door.interact("Open"))
				{
					MethodProvider.sleepUntil(() -> Locations.barrelDestination.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
				}
        		return;
        	}
        	if(Locations.waterfallIsland2.contains(Players.localPlayer()))
        	{
        		Filter<GameObject> treeFilter = g -> 
					g != null && 
					g.getName().equals("Dead Tree") && 
					g.hasAction("Climb");
				GameObject tree = GameObjects.closest(treeFilter);
				if(tree == null)
				{
					MethodProvider.log("tree is null in island2!!");
					return;
				}
				if(Inventory.get(id.rope).useOn(tree))
				{
					MethodProvider.sleepUntil(() -> Locations.waterfallLedge.equals(Players.localPlayer().getTile()), Sleep.calculate(8888,3333));
				}
        		return;
        	}
			if(Locations.waterfallIsland1.contains(Players.localPlayer()))
        	{
        		if(Locations.waterfallIsland1SouthTile.equals(Players.localPlayer().getTile()))
        		{
        			Filter<GameObject> rockFilter = g -> 
        				g!= null && 
        				g.getName().equals("Rock") && 
        				g.hasAction("Swim to");
        			GameObject rock = GameObjects.closest(rockFilter);
        			if(rock == null)
        			{
        				MethodProvider.log("Rock is null in island south tile!!");
        			}
        			if(Inventory.get(id.rope).useOn(rock))
        			{
        				MethodProvider.sleepUntil(() -> Locations.waterfallIsland2.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
        			}
        			return;
        		}
        		if(!Players.localPlayer().isMoving() && Walking.walk(Locations.waterfallIsland1SouthTile)) Sleep.sleep(420, 696);
        		return;
        	}
			teleWalkBoardRaft();
			return;
		}
    	if(Locations.waterfallDungeon2.contains(Players.localPlayer()))
    	{
    		if(Inventory.count(glarialsUrn) > 0 && 
    				Inventory.count(glarialsAmulet) > 0)
    		{
    			walkAlmera();
    			return;
    		}
    		if(Inventory.count(glarialsUrn) <= 0)
    		{
    			if(!InvEquip.free1InvySpace()) return;
    			if(Locations.glarialsTomb.contains(Players.localPlayer()))
        		{
        			Filter<GameObject> doorF = g -> 
						g != null && 
						g.getName().equals("Glarial\'s Tomb") && 
						g.hasAction("Search");
					GameObject door = GameObjects.closest(doorF);
					if(door == null)
					{
						MethodProvider.log("Glarial's tomb is null in tomb spot dungeon (Waterfall Quest)!!");
					}
					if(door.interact("Search"))
					{
						MethodProvider.sleepUntil(() -> Inventory.count(glarialsUrn) > 0, Sleep.calculate(8888,3333));
					} else if(Walking.walk(door)) Sleep.sleep(420, 696);
					return;
	        	}
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.glarialsTomb.getCenter())) Sleep.sleep(696,420);
    			return;
    		}
    		if(Inventory.count(glarialsAmulet) <= 0)
    		{
    			if(!InvEquip.free1InvySpace()) return;
    			if(Locations.glarialsChest.contains(Players.localPlayer()))
        		{
        			Filter<GameObject> doorF = g -> 
						g != null && 
						g.getName().equals("Chest") && 
						(g.hasAction("Search") || 
						g.hasAction("Open"));
					GameObject door = GameObjects.closest(doorF);
					if(door == null)
					{
						MethodProvider.log("Glarial's Chest is null in chest dungeon spot (Waterfall Quest)!!");
					}
					if(door.interact("Search"))
					{
						MethodProvider.sleepUntil(() -> Inventory.count(glarialsAmulet) > 0, Sleep.calculate(8888,3333));
					}
					else if(door.interact("Open"))
					{
						Sleep.sleep(696, 420);
					}
					else if(Walking.walk(door)) Sleep.sleep(420, 696);
					return;
	        	}
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.glarialsChest.getCenter())) Sleep.sleep(696,420);
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
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallRaftTile1)) Sleep.sleep(696,420);
    			return;
    		}
    		if(raft.interact("Board"))
    		{
    			
    			MethodProvider.sleepUntil(() -> Locations.waterfallIsland1.contains(Players.localPlayer()),() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222),50);
    		}
    		else if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallRaftTile1)) Sleep.sleep(696,420);
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
		Sleep.sleep(69, 696);
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
						Sleep.sleep(69, 696);
						if(Inventory.get(i).useOn(currentPillar))
						{
							MethodProvider.log("Used rune on pillar: " + new Item(i,1).getName());
							MethodProvider.sleepUntil(() -> currentPillarTile.distance() <= 1, Sleep.calculate(2222, 2222));
							Sleep.sleep(69, 696);
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
			Sleep.sleep(69, 696);
			GameObject glarialsStatue = GameObjects.closest("Statue of Glarial");
			if(glarialsStatue == null)
			{
				MethodProvider.log("Glarials statue is null!");
				Sleep.sleep(69, 696);
				continue;
			}
			if(Inventory.get(glarialsAmulet).useOn(glarialsStatue))
			{
				MethodProvider.log("Used glarials amulet on glarial's statue");
				MethodProvider.sleepUntil(() -> glarialsStatue.distance() <= 1, Sleep.calculate(2222, 2222));
				Sleep.sleep(696, 1111);
				MethodProvider.sleepUntil(() -> Locations.waterfallDungeonLastAreaChanged.contains(Players.localPlayer()), Sleep.calculate(3333, 2222));
				Sleep.sleep(69, 696);
				if(Locations.waterfallDungeonLastAreaChanged.contains(Players.localPlayer()))
				{
					MethodProvider.log("Entered changed area of last room of Waterfall Quest!!");
				}
				break;
			}
		}
	}
	public static void walkAlmera()
	{
		if(Locations.almera.getCenter().distance() < 155)
    	{
			if(Walking.shouldWalk(6) && Walking.walk(Locations.almera.getCenter())) Sleep.sleep(696,420);
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
		MethodProvider.log("Initiating Fulfill Start");
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.rope,1,1, false,5);
		InvEquip.addInvyItem(id.stamina4, 1, 2, false, 5);
		InvEquip.addInvyItem(InvEquip.skills, 1, 1, false, 1);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.games);

		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(TrainRanged.jugOfWine,5,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	public static boolean fulfillGlarialsPebble()
	{
		MethodProvider.log("Initiating Fulfill GlarialsPebble");
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
		InvEquip.addInvyItem(TrainRanged.jugOfWine,3,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	
	public static boolean fulfillLastStep()
	{
		MethodProvider.log("Initiating Fulfill LastStep");
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.rope,1,1, false,5);
		InvEquip.addInvyItem(id.stamina4, 1, 1, false, 5);
		InvEquip.addInvyItem(id.airRune, 6, (int) Calculations.nextGaussianRandom(25,10), false, 100);
		InvEquip.addInvyItem(id.waterRune, 6, (int) Calculations.nextGaussianRandom(25,10), false, 100);
		InvEquip.addInvyItem(id.earthRune, 6, (int) Calculations.nextGaussianRandom(25,10), false, 100);
		InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 1);
		InvEquip.addInvyItem(glarialsAmulet, 1, 1, false, 0);
		InvEquip.addInvyItem(key1, 1, 1, false, 0);
		InvEquip.addInvyItem(key2, 1, 1, false, 0);
		InvEquip.addInvyItem(glarialsPebble, 1, 1, false, 0);
		InvEquip.addInvyItem(glarialsUrn, 1, 1, false, 0);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.skills);
		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(TrainRanged.jugOfWine,3,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	public static int getProgressValue()
	{
		return PlayerSettings.getConfig(65);
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
			return chooseQuestOption();
		}
		return false;
	}
	public static boolean chooseQuestOption()
	{
		if(Dialogues.chooseOption("Yes.")) return true;
		return false;
	}
}
