package script.skills.ranged;

import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.quest.varrockmuseum.Timing;
import script.utilities.Combat;
import script.utilities.Sleep;

public class ItemsOnGround {
	public static List<Integer> herbTable = new ArrayList<Integer>();
	public static List<Integer> seedTable = new ArrayList<Integer>();
	public static List<Integer> rareDropTable = new ArrayList<Integer>();
	public static List<Integer> hillGiantsLoot = new ArrayList<Integer>();
	public static final int grimyRanarr = 207;

	public static final int snapdragonSeed = 5300;
	public static final int ranarrSeed = 5295;
	public static final int torstolSeed = 5304;
	public static final int toadflaxSeed = 5296;
	public static final int snapegrassSeed = 22879;
	
	public static final int giantKey = 20754;
	public static final int natureRune = 561;
	public static final int bigBones = 532;

	public static final int shieldLeftHalf = 2366;
	public static final int dragonSpear = 1249;
	public static final int runeSpear = 1247;
	public static final int keyLoopHalf = 987;
	public static final int keyToothHalf = 985;
	public static final int uncutDiamond = 1617;
	
	
	public static void initializeLists()
	{
		herbTable.add(grimyRanarr);
		seedTable.add(snapdragonSeed);
		seedTable.add(torstolSeed);
		seedTable.add(toadflaxSeed);
		seedTable.add(snapegrassSeed);
		rareDropTable.add(shieldLeftHalf);
		rareDropTable.add(dragonSpear);
		rareDropTable.add(runeSpear);
		rareDropTable.add(keyLoopHalf);
		rareDropTable.add(keyToothHalf);
		rareDropTable.add(uncutDiamond);
		hillGiantsLoot.add(giantKey);
		hillGiantsLoot.add(natureRune);
		hillGiantsLoot.add(bigBones);
		for(int i : herbTable)
		{
			hillGiantsLoot.add(i);
		}
		for(int i: seedTable)
		{
			hillGiantsLoot.add(i);
		}
		for(int i: rareDropTable)
		{
			hillGiantsLoot.add(i);
		}
	}
	public static boolean groundIDMatchesList(int ID, List<Integer> listToCheck)
	{
		for(int i : listToCheck)
		{
			if(i == ID) return true;
		}
		return false;
	}
	public static GroundItem getNearbyGroundItem(List<Integer> listToCheck, Area killingArea)
	{
		if(listToCheck == null)
		{
			MethodProvider.log("Attempted to check ground items list which is null");
			return null;
		}
		if(listToCheck.isEmpty())
		{
			MethodProvider.log("Attempted to check ground items list which is empty");
			return null;
		}
		Filter<GroundItem> nearbyGroundItemFilter = g -> 
			g != null && 
			g.exists() &&
			g.distance() <= 15 &&
			(killingArea.contains(g) || g.getID() != bigBones) &&
			g.canReach() && 
			groundIDMatchesList(g.getID(), listToCheck);
		GroundItem g = GroundItems.closest(nearbyGroundItemFilter);
		return g;
	}
	public static void grabNearbyGroundItem(GroundItem g)
	{
		if(g == null) return;
		if(Inventory.isFull())
		{
			for(int food : Combat.foods)
			{
				if(Inventory.count(food) > 0)
				{
					if(Inventory.drop(food))
					{
						MethodProvider.log("Dropped a food: " + new Item(food, 1).getName());
						MethodProvider.sleep(Timing.sleepLogNormalSleep());
					}
				}
			}
		}
		if(Inventory.isFull())
		{
			MethodProvider.log("Inventory full but found nearby ground item, confused wat do?? : "+g.getName());
			return;
		}
		MethodProvider.log("Attempting to grab ground item: "+g.getName() +" in amount: " + g.getAmount());
		final int count = Inventory.count(g.getID());
		if(g.interact("Take")) 
		{
			MethodProvider.sleepUntil(() -> Inventory.count(g.getID()) > count, Sleep.calculate(2222, 2222));
		}
	}
}
