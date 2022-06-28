package script.utilities;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
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

public class Walkz {

	public static final int cammyTele = 8010;
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
					MethodProvider.sleepUntil(() -> Locations.clickableGEArea.contains(Players.localPlayer()), () -> Players.localPlayer().isAnimating(), Sleep.calculate(4444,2222),50);
				}
				continue;
			}
			
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank)
			{
				if(Bank.isOpen()) InvEquip.checkedBank = true;
				else if(Walking.shouldWalk(6) && !Bank.openClosest())
				{
					Sleep.sleep(666,666);
				}
				continue;
			}
			
			//check bank for wealth / varrock tabs
			int ring = 0;
			for(int ring2 : InvEquip.wearableWealth)
			{
				if(Bank.contains(ring2)) 
				{
					ringFound = true;
					ring = ring2;
					break;
				}	
			}
			
			//if no ring found in equip OR invy OR bank, check bank for varrock tab
			if(!ringFound) 
			{
				if(Bank.contains(varrockTele))
				{
					if(Bank.openClosest())
					{
						if(Bank.withdraw(varrockTele))
						{
							final int tmp = varrockTele;
							MethodProvider.sleepUntil(() -> Inventory.count(tmp) > 0, Sleep.calculate(2222, 2222));
						}
					} 
					else Sleep.sleep(666, 666);
				} 
				//no options left - go to GE by walking
				else if(Walking.shouldWalk(6) && Walking.walk(Locations.GE)) Sleep.sleep(666, 666);
			}
				
			
			//found item in bank - withdraw it
			else
			{
				if(Bank.openClosest())
				{
					if(Inventory.emptySlotCount() < 2)
					{
						Item i = Inventory.getItemInSlot(Inventory.getFirstFullSlot());
						int iD = i.getID();
						if(Bank.depositAll(i))
						{
							MethodProvider.sleepUntil(() -> Inventory.count(iD) <= 0,Sleep.calculate(2222, 2222));
						}
						continue;
					}
					if(Bank.withdraw(ring))
					{
						final int tmp = ring;
						MethodProvider.sleepUntil(() -> Inventory.count(tmp) > 0, Sleep.calculate(2222, 2222));
					}
				} else Sleep.sleep(666, 666);
			}
		}
		
		return false;
	}
	public static boolean useDuelRingCastleWars()
	{
		if(InvEquip.equipmentContains(InvEquip.wearableDuel))
		{
			if(Tabs.isOpen(Tab.EQUIPMENT))
			{
				if(Equipment.interact(EquipmentSlot.RING, "Castle Wars"))
				{
					MethodProvider.sleepUntil(() -> Locations.castleWars.contains(Players.localPlayer()), () -> Players.localPlayer().isAnimating(),Sleep.calculate(3333,2222),50);
				}
			}
			else
			{
				if(Widgets.isOpen()) Widgets.closeAll();
				Tabs.openWithFKey(Tab.EQUIPMENT);
			}
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
			if(dist > 25)
			{
				if(useDuelRingCastleWars()) continue;
			}
			if(dist <= 150)
			{
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.castleWars.getCenter()))
				{
					MethodProvider.log("Distance to castle wars is: " + dist + "... walking...");
					Sleep.sleep(666, 1111);
				}
				continue;
			}
			//check if have ring of dueling equipped, use it
			if(useDuelRingCastleWars()) continue;
		
			//check inventory for ring of duel, use it
			for(int ring : InvEquip.wearableDuel)
			{
				if(Inventory.contains(ring))
				{
					InvEquip.equipItem(ring);
					ringFound = true;
					break;
				}
			}
			if(ringFound) continue;
			
			
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank)
			{
				if(Bank.isOpen()) InvEquip.checkedBank = true;
				else if(Walking.shouldWalk(6) && !Bank.openClosest())
				{
					Sleep.sleep(666,666);
				}
				continue;
			}
			
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
				if(Bank.openClosest())
				{
					if(Inventory.emptySlotCount() < 2)
					{
						Item i = Inventory.getItemInSlot(Inventory.getFirstFullSlot());
						int iD = i.getID();
						if(Bank.depositAll(i))
						{
							MethodProvider.sleepUntil(() -> Inventory.count(iD) <= 0,Sleep.calculate(2222, 2222));
						}
						continue;
					}
					if(Bank.withdraw(ring))
					{
						final int tmp = ring;
						MethodProvider.sleepUntil(() -> Inventory.count(tmp) > 0, Sleep.calculate(2222, 2222));
					}
				} else Sleep.sleep(666, 666);
			}
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
			if(!InvEquip.checkedBank)
			{
				if(Bank.isOpen()) InvEquip.checkedBank = true;
				else if(Walking.shouldWalk(6) && !Bank.openClosest())
				{
					Sleep.sleep(666,666);
				}
				continue;
			}
			
			//if no ring found in invy OR bank, buy at GE
			if(!Bank.contains(cammyTele)) InvEquip.buyItem(InvEquip.duel8, 1, timeout);
			
			//found item in bank - withdraw it
			else
			{
				if(Bank.openClosest())
				{
					if(Inventory.emptySlotCount() < 2)
					{
						Item i = Inventory.getItemInSlot(Inventory.getFirstFullSlot());
						int iD = i.getID();
						if(Bank.depositAll(i))
						{
							MethodProvider.sleepUntil(() -> Inventory.count(iD) <= 0,Sleep.calculate(2222, 2222));
						}
						continue;
					}
					if(Bank.withdraw(cammyTele,1))
					{
						MethodProvider.sleepUntil(() -> Inventory.count(cammyTele) > 0, Sleep.calculate(2222, 2222));
					}
				} else Sleep.sleep(666, 666);
			}
		}
		
		return false;
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
