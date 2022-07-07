package script.utilities;

import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.quest.varrockmuseum.Timing;

public class Walkz {

	public static final int cammyTele = 8010;
	public static final int houseTele = 8013;
	public static final int fallyTele = 8009;
	public static final int varrockTele = 8007;
	
	public static boolean goToGE(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69,69);
			//check if already in good GE area
			if(Locations.clickableGEArea.contains(Players.localPlayer())) return true;
			//check if within walkable area to GE
			if(Locations.dontTeleToGEAreaJustWalk.contains(Players.localPlayer()))
			{
				if(Walking.shouldWalk(6) && Walking.walk(BankLocation.GRAND_EXCHANGE.getTile()))
				{
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//check if have ring of wealth equipped, use it
			if(InvEquip.equipmentContains(InvEquip.wearableWealth))
			{
				if(Tabs.isOpen(Tab.EQUIPMENT))
				{
					if(Equipment.interact(EquipmentSlot.RING, "Grand Exchange"))
					{
						MethodProvider.sleepUntil(() -> Locations.teleGE.contains(Players.localPlayer()), () -> Players.localPlayer().isAnimating(),Sleep.calculate(3333,2222),50);
					}
				}
				else
				{
					if(Widgets.isOpen()) Widgets.closeAll();
					Tabs.openWithFKey(Tab.EQUIPMENT);
				}
				continue;
			}
		
			//check inventory for ring of wealth / tablet, use it
			boolean ringFound = false;
			for(int ring : InvEquip.wearableWealth)
			{
				if(Inventory.contains(ring))
				{
					InvEquip.equipItem(ring);
					ringFound = true;
					break;
				}
			}
			if(ringFound) continue;
			if(Inventory.contains(varrockTele))
			{
				if(Bank.isOpen()) Bank.close();
				else if(Inventory.interact(varrockTele, "Break"))
				{
					MethodProvider.sleepUntil(() -> Locations.dontTeleToGEAreaJustWalk.contains(Players.localPlayer()), () -> Players.localPlayer().isAnimating(), Sleep.calculate(4444,2222),50);
				}
				continue;
			}
			
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//check bank for wealth / varrock tabs
			int ring = InvEquip.getFirstInBank(InvEquip.wearableWealth);
			
			//if no ring found in equip OR invy OR bank, check bank for varrock tab
			if(ring == -1) 
			{
				if(Bank.contains(varrockTele))
				{
					InvEquip.withdrawOne(varrockTele, timeout);
					continue;
				} 
				//no options left - go to GE by walking
				else if(Locations.isInKourend()) leaveKourend(240000);
				else if(Walking.shouldWalk(6) && Walking.walk(Locations.GE)) Sleep.sleep(666, 666);
			}
			//found item in bank - withdraw it
			else
			{
				InvEquip.withdrawOne(ring, timeout);
			}
		}
		
		return false;
	}
	public static boolean leaveKourend(long timeout)
	{
		Timer timer = new Timer(timeout);
		boolean foundShortest = false;
		Area area = null;
		String veos = null;
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69,69);
			if(Locations.isInKourend() || Locations.isInDestinationShip())
			{
				if(Locations.isInDestinationShip()) 
				{
					Locations.leaveDestinationShip();
					MethodProvider.sleep(Timing.sleepLogNormalSleep());
					continue;
				}
				if(useJewelry(InvEquip.wealth,"Grand Exchange")) continue;
				if(useJewelry(InvEquip.glory,"Edgeville")) continue;
				if(useJewelry(InvEquip.combat,"Ranging Guild")) continue;
				if(useJewelry(InvEquip.skills,"Fishing Guild")) continue;
				if(useJewelry(InvEquip.duel,"Castle Wars")) continue;
				if(useJewelry(InvEquip.passage,"Wizards\' Tower")) continue;
				if(useJewelry(InvEquip.games,"Barbarian Outpost")) continue;
				if(Locations.kourendGiantsCaveArea.contains(Players.localPlayer()))
				{
					exitCave();
					continue;
				}
				if(!foundShortest)
				{
					double distLandsEnd = Locations.veosLandsEnd.distance(Players.localPlayer().getTile());
					double distPisc = Locations.veosPisc.distance(Players.localPlayer().getTile());
					if(true) 
					{
						//faster maybe to go to Piscarillius
						area = Locations.veosPisc;
						veos = "Veos";
					}
					else
					{
						area = Locations.veosLandsEnd;
						veos = "Captain Magoro";
					}
					foundShortest = true;
				}
				NPC closestVeos = NPCs.closest(veos);
				if(closestVeos != null)
				{
					if(closestVeos.interact("Port Sarim"))
					{
						MethodProvider.sleepUntil(() -> Locations.shipSarimVeos.contains(Players.localPlayer()),
								() -> Players.localPlayer().isMoving(), 
								Sleep.calculate(3333, 2222),50);
					} else if(Walking.shouldWalk(5) && Walking.walk(closestVeos)) Sleep.sleep(69, 420);
					continue;
				}
				MethodProvider.log("Walking to Veos: "+area.getCenter().toString());
				if(Walking.shouldWalk(6) && Walking.walk(area.getCenter())) Sleep.sleep(69, 420);
			}
			else return true;
		}
		return false;
	}
	public static void exitCave()
	{

		MethodProvider.log("Test");
		Filter<GameObject> caveFilter = c -> 
		c != null && 
		c.exists() && 
		c.getName().contains("Cave") && 
		c.hasAction("Exit");
		GameObject cave = GameObjects.closest(caveFilter);
		if(cave != null)
		{
			if(cave.interact("Exit"))
			{
				MethodProvider.sleepUntil(() -> Locations.kourendGiantsCaveEntrance.contains(Players.localPlayer()),
						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222), 50);
			}
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
			return;
		}
		else 
		{
			if(Walking.shouldWalk(6) && Walking.walk(Locations.kourendGiantsCaveEntrance.getCenter())) Sleep.sleep(69, 420);
		}
	}
	/**
	 * returns true if have jewelry in invy or equipment. If in invy, equips and then teleports
	 * both to avoid having to handle tele menu interface, and to ensure it teleports in combat
	 * @param jewelry
	 * @param teleName
	 * @return
	 */
	public static boolean useJewelry(int jewelry, String teleName)
	{
		List<Integer> wearableJewelry = new ArrayList<Integer>();
		EquipmentSlot equipSlot = null;
		if(jewelry == InvEquip.wealth) 
			{
			equipSlot = EquipmentSlot.RING;
			wearableJewelry = InvEquip.wearableWealth;
			}
		if(jewelry == InvEquip.glory) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableGlory;
			}
		if(jewelry == InvEquip.combat) 
			{
			equipSlot = EquipmentSlot.HANDS;
			wearableJewelry = InvEquip.wearableCombats;
			}
		if(jewelry == InvEquip.skills) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableSkills;
			}
		if(jewelry == InvEquip.games) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableGames;
			}
		if(jewelry == InvEquip.duel) 
			{
			equipSlot = EquipmentSlot.RING;
			wearableJewelry = InvEquip.wearableDuel;
			}
		if(jewelry == InvEquip.passage) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearablePassages;
			}
		if(InvEquip.equipmentContains(wearableJewelry))
		{
			
			if(Tabs.isOpen(Tab.EQUIPMENT))
			{
				if(Equipment.interact(equipSlot, teleName))
				{
					MethodProvider.log("Just used Jewelry teleport: " + teleName +" in slot: " + equipSlot);
					MethodProvider.sleepUntil(() -> Players.localPlayer().isAnimating(),Sleep.calculate(1111,1111));
					MethodProvider.sleepWhile(() -> Players.localPlayer().isAnimating(),Sleep.calculate(3333,3333));
					Sleep.sleep(69,696);
				}
			}
			else
			{
				if(Widgets.isOpen()) Widgets.closeAll();
				Tabs.openWithFKey(Tab.EQUIPMENT);
			}
			return true;
		}
		if(InvEquip.invyContains(wearableJewelry))
		{
			final int jewelryID = InvEquip.getInvyItem(wearableJewelry);
			InvEquip.equipItem(jewelryID);
			return true;
		}
		return false;
	}
	public static boolean goToCastleWars(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69,69);
			//check if already in good castle wars area
			if(Locations.castleWars.contains(Players.localPlayer())) return true;
			//check if within reasonable walking distance
			double dist = Locations.castleWars.distance(Players.localPlayer().getTile());
			//if have ring of dueling already equipped, use it
			boolean ringFound = false;
			if(dist > 100)
			{
				if(useJewelry(InvEquip.duel,"Castle Wars")) continue;
			}
			if(dist <= 100)
			{
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.castleWars.getCenter()))
				{
					MethodProvider.log("Distance to castle wars is: " + dist + "... walking...");
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//check if have ring of dueling equipped or in invy, use it
			if(useJewelry(InvEquip.duel,"Castle Wars")) continue;
		
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//check bank for dueling rings
			int ring = 0;
			for(int ring2 : InvEquip.wearableDuel)
			{
				if(Bank.contains(ring2)) 
				{
					ringFound = true;
					ring = ring2;
					break;
				}	
			}
			
			//if no ring found in invy OR bank, buy at GE
			if(!ringFound) InvEquip.buyItem(InvEquip.duel8, 1, timeout);
			
			//found item in bank - withdraw it
			else
			{
				InvEquip.withdrawOne(ring, timeout);
			}
		}
		
		return false;
	}
	public static boolean teleportDraynor(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69,69);
			//check if already in good draynor teleport area
			double dist = Locations.draynorTeleSpot.distance();
			if(dist <= 4) return true;
			//check if within reasonable walking distance
			if(dist < 25) 
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.draynorTeleSpot))
				{
					MethodProvider.log("Distance to draynor tele spot is: " + dist + "... walking...");
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//if have glory already equipped, use it
			if(dist > 75)
			{
				if(useJewelry(InvEquip.glory,"Draynor Village")) continue;
			}
			if(dist <= 75)
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.draynorTeleSpot))
				{
					MethodProvider.log("Distance to draynorTeleSpot is: " + dist + "... walking...");
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//check if have ring of dueling equipped, use it
			if(useJewelry(InvEquip.glory,"Draynor Village")) continue;
		
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//check bank for glory
			int jewelry = InvEquip.getBankItem(InvEquip.wearableGlory);
			
			//if no ring found in invy OR bank, buy at GE
			if(jewelry == 0) 
			{
				InvEquip.buyItem(InvEquip.glory6, 1, timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
				
			
			//found item in bank - withdraw it
			else InvEquip.withdrawOne(jewelry, timeout);
		}
		
		return false;
	}
	public static boolean teleportWoodcuttingGuild(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69,69);
			if(!Locations.unlockedKourend)
			{
				MethodProvider.log("Exiting woodcutting tele loop, do not have kourend unlocked");
				break;
			}
			MethodProvider.log("In teleportWoodcuttingGuild loop");
			//check if already in good draynor teleport area
			double dist = Locations.woodcuttingGuildTeleSpot.distance(Players.localPlayer().getTile());
			if(dist <= 10) return true;
			//check if within reasonable walking distance
			if(useJewelry(InvEquip.skills,"Woodcutting Guild")) continue;
			
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//check bank 
			int jewelry = InvEquip.getBankItem(InvEquip.wearableSkills);
			
			//if nothing found in invy OR bank, buy at GE
			if(jewelry == 0) 
			{
				InvEquip.buyItem(InvEquip.skills6, 1, timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
			
			//found item in bank - withdraw it
			else InvEquip.withdrawOne(jewelry, timeout);
		}
		
		return false;
	}
	public static boolean goToCamelotTrees(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69,69);
			//check if already in good area
			if(Locations.camelotTrees.contains(Players.localPlayer())) return true;
			//check if within reasonable walking distance
			double dist = Locations.camelotTrees.distance(Players.localPlayer().getTile());
			
			if(dist <= 150)
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.camelotTrees.getCenter()))
				{
					MethodProvider.log("Distance to camelot trees area is: " + dist + "... walking...");
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//check if have cammy tab in invy, use it
			if(Inventory.contains(cammyTele))
			{
				if(Bank.isOpen()) Bank.close();
				else if(Inventory.interact(cammyTele, "Break"))
				{
					MethodProvider.sleepUntil(() -> Locations.camelotTrees.contains(Players.localPlayer()), () -> Players.localPlayer().isAnimating(), Sleep.calculate(4444,2222),50);
				}
				continue;
			}
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//if no ring found in invy OR bank, buy at GE
			if(!Bank.contains(cammyTele)) 
			{
				InvEquip.buyItem(cammyTele, 1, timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
				
			
			//found item in bank - withdraw it
			else
			{
				InvEquip.withdrawOne(cammyTele, timeout);
			}
		}
		
		return false;
	}
	public static boolean teleport(int tabID, Area teleSpot, long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			if(teleSpot.contains(Players.localPlayer())) return true;
			
			Sleep.sleep(69,69);
			//check if within reasonable walking distance
			double dist = teleSpot.distance(Players.localPlayer().getTile());
			
			if(dist <= 30)
			{
				if(Walking.shouldWalk(6) && Walking.walk(teleSpot.getCenter()))
				{
					MethodProvider.log("Distance to tele area is: " + dist + "... walking...");
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//check if have fally tab in invy, use it
			if(Inventory.contains(tabID))
			{
				if(Bank.isOpen()) Bank.close();
				else if(Inventory.interact(tabID, "Break"))
				{
					MethodProvider.sleepUntil(() -> teleSpot.contains(Players.localPlayer()), () -> Players.localPlayer().isAnimating(), Sleep.calculate(4444,2222),50);
				}
				continue;
			}
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//if none found in invy OR bank, stop
			if(!Bank.contains(tabID)) 
			{
				InvEquip.buyItem(houseTele, (int) Calculations.nextGaussianRandom(12,5),timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
			
			//found item in bank - withdraw it
			else InvEquip.withdrawOne(tabID, timeout);
		}
		return false;
	}
	public static boolean teleportFalador(long timeout)
	{
		return teleport(fallyTele,Locations.fallyTeleSpot,timeout);
	}
	public static boolean teleportHouse(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			if(Locations.isInstanced() || Locations.houseTeleSpot.contains(Players.localPlayer())) return true;
			Sleep.sleep(69,69);
			final double dist = Locations.houseTeleSpot.distance(Players.localPlayer().getTile());
			if(dist <= 30)
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.houseTeleSpot.getCenter()))
				{
					MethodProvider.log("Distance to tele area is: " + dist + "... walking...");
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//check if have fally tab in invy, use it
			if(Inventory.contains(houseTele))
			{
				if(Bank.isOpen()) Bank.close();
				else if(Inventory.interact(houseTele, "Break"))
				{
					MethodProvider.sleepUntil(() -> Locations.isInstanced() || Locations.houseTeleSpot.contains(Players.localPlayer()), () -> Players.localPlayer().isAnimating(), Sleep.calculate(4444,2222),50);
				}
				continue;
			}
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//if none found in invy OR bank, stop
			if(!Bank.contains(houseTele)) 
			{
				InvEquip.buyItem(houseTele, (int) Calculations.nextGaussianRandom(12,5),timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
			
			//found item in bank - withdraw it
			else InvEquip.withdrawOne(houseTele, timeout);
		}
		return false;
	}
	public static boolean teleportVarrock(long timeout)
	{
		return teleport(varrockTele,Locations.varrockTeleSpot,timeout);
	}
	public static boolean teleportCamelot(long timeout)
	{
		return teleport(cammyTele,Locations.cammyTeleSpot,timeout);
	}
	public static boolean walkToArea(Area area,Tile walkableTile)
	{
		if(area.contains(Players.localPlayer())) return true;
		if(!Walking.isRunEnabled() &&
				Walking.getRunEnergy() > Sleep.calculate(15, 20)) 
		{
			Walking.toggleRun();
		}
		if(Walking.shouldWalk())
		{
			Walking.walk(walkableTile);
		}
		Sleep.sleep(666, 1111);
		return area.contains(Players.localPlayer());
	}
	public static boolean walkToTileInRadius(Tile walkableTile,int radius)
	{
		Area area = walkableTile.getArea(radius);
		
		if(area.contains(Players.localPlayer())) return true;
		if(!Walking.isRunEnabled() &&
				Walking.getRunEnergy() > Sleep.calculate(15, 20)) 
		{
			if(Walking.toggleRun()) Sleep.sleep(69, 111);
		}
		if(Walking.shouldWalk())
		{
			if(Walking.walk(walkableTile)) Sleep.sleep(666, 1111);
		}
		return area.contains(Players.localPlayer());
	}
	public static boolean turnOnRun()
	{
		if(Walking.isRunEnabled()) return true;
		else if(Walking.getRunEnergy() > Sleep.calculate(15, 20) && Walking.toggleRun()) return true;
		return false;
	}
	public static boolean turnOffRun()
	{
		if(!Walking.isRunEnabled()) return true;
		else if(Walking.toggleRun()) return true;
		return false;
	}
	
	public static boolean walkToEntityInArea(String thingName,Area area)
	{
		if(area.contains(Players.localPlayer()))
		{
			//first check NPCs
			NPC npc = NPCs.closest(thingName);
			if(npc != null && npc.exists())
			{
				if(npc.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(npc.getTile()))
				{
					Sleep.sleep(666, 1111);
				}
				return false;
			}
			//next GameObjects
			GameObject object = GameObjects.closest(thingName);
			if(object != null && object.exists())
			{
				if(object.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(object.getTile()))
				{
					Sleep.sleep(666, 1111);
				}
				return false;
			}
			//next GroundItems
			GroundItem item = GroundItems.closest(thingName);
			if(item != null && item.exists())
			{
				if(item.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(item.getTile()))
				{
					Sleep.sleep(666, 1111);
				}
				return false;
			}
		}
		else if(Walking.shouldWalk() && Walking.walk(area.getRandomTile()))
		{
			Sleep.sleep(666, 1111);
		}
		return false;
	}
}
