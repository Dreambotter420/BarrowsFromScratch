package script.quest.waterfallquest;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

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
    
    public static final int key = 293;
    public static final int glarialsPebble = 294;
    public static final int glarialsUrn = 296;
    public static final int glarialsAmulet = 295;
    
    @Override
    public int onLoop() {
    	if(true)
    	{
    		MethodProvider.log("Waterfall quest unscripted!");
    		 API.mode = null;
             return Timing.sleepLogNormalSleep();
    	}
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Waterfall Quest!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
    	if(Locations.isInKourend())
    	{
    		Walkz.leaveKourend(180000);
    		return Timing.sleepLogNormalInteraction();
    	}
    	if(!Walkz.isStaminated()) Walkz.drinkStamina();
    	if(!Walking.isRunEnabled() && Walking.getRunEnergy() >= 20) Walking.toggleRun();
        switch(WaterfallConfigs.getProgressValue())
        { 
        case(4): //have put da pebble on da tombstone
        {
        	if(Inventory.count(TrainRanged.jugOfWine) <= 0)
        	{
        		if(Combat.shouldEatFood(8)) 
        		{
        			fulfillStart();
            		break;
        		}
        	}
        	if(Combat.shouldEatFood(8)) Combat.eatFood();
        	if(Locations.waterfallDungeon2.contains(Players.localPlayer()))
        	{
        		if(Inventory.count(glarialsUrn) > 0 && 
        				Inventory.count(glarialsAmulet) > 0)
        		{
        			if(Inventory.count(id.waterRune) < 6 || 
        					Inventory.count(id.earthRune) < 6 || 
        					Inventory.count(id.airRune) < 6)
        			{
        				
        			}
        		}
        		if(Inventory.count(glarialsUrn) <= 0)
        		{
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
    					break;
    	        	}
        			if(Walking.shouldWalk(6) && Walking.walk(Locations.glarialsTomb.getCenter())) Sleep.sleep(696,420);
        			break;
        		}
        		if(Inventory.count(glarialsAmulet) <= 0)
        		{
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
    					break;
    	        	}
        			if(Walking.shouldWalk(6) && Walking.walk(Locations.glarialsChest.getCenter())) Sleep.sleep(696,420);
        			break;
        		}
        		break;
        	}
        	break;
        }
        case(3): //have read da book
        {
        	if(Inventory.count(TrainRanged.jugOfWine) <= 0)
        	{
        		if(Combat.shouldEatFood(8)) 
        		{
        			fulfillStart();
            		break;
        		}
        	}
        	if(Combat.shouldEatFood(8)) Combat.eatFood();
        	if(Bank.contains(glarialsPebble))
        	{
        		InvEquip.withdrawOne(glarialsPebble, 180000);
        		break;
        	}
        	if(Inventory.count(glarialsPebble) > 0)
        	{
        		if(!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
        				!InvEquip.equipmentContains(InvEquip.wearableWealth))
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
					} else if(Walking.walk(door)) Sleep.sleep(420, 696);
					break;
        		}
        		if(Walkz.walkPath(Paths.waterfallQuestToGlarialsTomb))
            	{
            		break;
            	}
            	
            	Walkz.useJewelry(InvEquip.skills,"Fishing Guild");
            	break;	
        	}
        	if(Locations.waterfallDungeon1.contains(Players.localPlayer()))
        	{
        		if(Locations.waterfallDungeonGolrieRoom.contains(Players.localPlayer()))
        		{
        			if(handleDialogues()) break;
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
        			break;
        		}
        		if(Inventory.count(key) >= 1)
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
						if(Inventory.get(key).useOn(door))
						{
							MethodProvider.sleepUntil(() -> Locations.waterfallDungeonGolrieRoom.contains(Players.localPlayer()), Sleep.calculate(8888,3333));
						} else if(Walking.walk(door)) Sleep.sleep(420, 696);
						break;
        			}
        			if(Walkz.walkPath(Paths.waterfallQuestDungeonPath1))
        			{
        				Sleep.sleep(696, 420);
        				break;
        			}
        			MethodProvider.log("Stuck in waterfall quest with key ...");
        			break;
        		}
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
						MethodProvider.sleepUntil(() -> Inventory.count(key) >= 1, Sleep.calculate(8888,3333));
					} else if(Walking.walk(box)) Sleep.sleep(420, 696);
					break;
        		}
        		break;
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
				break;
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
        		break;
        	}
        	if(Widgets.getWidgetChild(392, 7) != null && 
        			Widgets.getWidgetChild(392, 7).isVisible())
        	{
        		if(Widgets.getWidgetChild(392, 7).interact("Close")) Sleep.sleep(696,420);
        		break;
        	}

        	if(Walkz.walkPath(Paths.gnomeStrongholdMaze))
        	{
        		break;
        	}
        	
        	if(!Walkz.useJewelry(InvEquip.skills,"Fishing Guild"))
        	{
        		if(!InvEquip.checkedBank()) break;
        		if(InvEquip.bankContains(InvEquip.wearableSkills))
        		{
        			InvEquip.withdrawOne(InvEquip.getBankItem(InvEquip.wearableSkills), 180000);
        			break;
        		}
        		InvEquip.buyItem(InvEquip.skills6, 1, 180000);
        	}
        	break;
        }
        case(2): //have talked to hudon
        {
        	if(Inventory.contains("Book on baxtorian"))
        	{
        		if(Inventory.interact("Book on baxtorian","Read"))
        		{
        			MethodProvider.sleepUntil(() -> WaterfallConfigs.getProgressValue() == 3, Sleep.calculate(4444,3333));
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
					g.getName().equals("Dead tree") && 
					g.hasAction("Climb");
				GameObject tree = GameObjects.closest(treeFilter);
				if(tree == null)
				{
					MethodProvider.log("tree is null in island2!!");
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
        		}
        	}
        	break;
        }
        case(1): //have talked to almera
        {
        	if(fulfillStart())
        	{
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
        		GameObject raft = GameObjects.closest("Log raft");
        		if(raft == null)
        		{
        			if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallRaftTile1)) Sleep.sleep(696,420);
        			break;
        		}
        		if(raft.interact("Board"))
        		{
        			MethodProvider.sleepUntil(Dialogues::inDialogue,() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222),50);
        		}
        		if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallRaftTile1)) Sleep.sleep(696,420);
    			if(Locations.almera.getCenter().distance() < 85)
            	{
            		if(Walking.shouldWalk(6) && Walking.walk(Locations.waterfallRaftTile1)) Sleep.sleep(696,420);
            		break;
            	}
            	if(!Walkz.useJewelry(InvEquip.games,"Barbarian Outpost"))
            	{
            		fulfillStart();
            	}
        	}
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
            	if(Locations.almera.getCenter().distance() < 85)
            	{
            		if(Walking.shouldWalk(6) && Walking.walk(Locations.almera.getCenter())) Sleep.sleep(696,420);
            		break;
            	}
            	if(!Walkz.useJewelry(InvEquip.games,"Barbarian Outpost"))
            	{
            		fulfillStart();
            	}
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
	
	public static boolean fulfillStart()
	{
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.rope,1,1, false,5);
		InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.games);
		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(TrainRanged.jugOfWine,5,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	public static boolean fulfillGlarialsPebble()
	{
		InvEquip.clearAll();
		InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);
		InvEquip.addInvyItem(glarialsPebble, 1, 1, false, 0);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.skills);
		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(TrainRanged.jugOfWine,3,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	public static boolean fulfillLastStep()
	{
		InvEquip.clearAll();
		InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);
		InvEquip.addInvyItem(id.airRune, 6, 6, false, 6);
		InvEquip.addInvyItem(id.waterRune, 6, 6, false, 6);
		InvEquip.addInvyItem(id.earthRune, 6, 6, false, 6);
		InvEquip.addInvyItem(glarialsAmulet, 1, 1, false, 0);
		InvEquip.addInvyItem(glarialsUrn, 1, 1, false, 0);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.skills);
		InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(TrainRanged.jugOfWine,3,23, false, (int) Calculations.nextGaussianRandom(500,100));
		return InvEquip.fulfillSetup(true, 180000);
	}
	public static boolean handleDialogues()
	{
		if(Dialogues.canContinue())
		{
			if(Dialogues.continueDialogue()) Sleep.sleep(420,696);
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
