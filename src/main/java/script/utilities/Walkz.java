package script.utilities;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
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
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

public class Walkz {
	public static boolean goToGE()
	{
		//check if already in good GE area
		if(Locations.clickableGEArea.contains(Players.localPlayer())) return true;
		//check if within walkable area to GE
		if(Locations.dontTeleToGEAreaJustWalk.contains(Players.localPlayer()))
		{
			if(Walking.walk(BankLocation.GRAND_EXCHANGE.getTile()))
			{
				Sleep.sleep(666, 1111);
			}
			return false;
		}
		//check if have ring of wealth equipped, use it
		if(Equipment.contains("Ring of wealth (1)","Ring of wealth (2)",
				"Ring of wealth (3)","Ring of wealth (4)","Ring of wealth (5)"))
		{
			if(Tabs.isOpen(Tab.EQUIPMENT))
			{
				if(Equipment.interact(EquipmentSlot.RING, "Grand Exchange"))
				{
					MethodProvider.sleepUntil(() -> Locations.teleGE.contains(Players.localPlayer()), Sleep.calculate(3333,2222));
				}
			}
			else
			{
				if(Widgets.isOpen()) Widgets.closeAll();
				Tabs.openWithFKey(Tab.EQUIPMENT);
			}
			return false;
		}
		//check inventory for ring of wealth, use it
		if(Inventory.contains("Ring of wealth (1)"))
		{
			InventoryEquipment.equipItem("Ring of wealth (1)");
		}
		else if(Inventory.contains("Ring of wealth (2)"))
		{
			InventoryEquipment.equipItem("Ring of wealth (2)");
		}
		else if(Inventory.contains("Ring of wealth (3)"))
		{
			InventoryEquipment.equipItem("Ring of wealth (3)");
		}
		else if(Inventory.contains("Ring of wealth (4)"))
		{
			InventoryEquipment.equipItem("Ring of wealth (4)");
		}
		else if(Inventory.contains("Ring of wealth (5)"))
		{
			InventoryEquipment.equipItem("Ring of wealth (5)");
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
