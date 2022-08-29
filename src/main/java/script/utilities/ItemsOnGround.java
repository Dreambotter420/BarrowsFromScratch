package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;

public class ItemsOnGround {
	public static List<Integer> herbTable = new ArrayList<Integer>();
	public static List<Integer> seedTable = new ArrayList<Integer>();
	public static List<Integer> rareDropTable = new ArrayList<Integer>();
	public static List<Integer> hillGiantsLoot = new ArrayList<Integer>();
	public static List<Integer> caveCrawlersLoot = new ArrayList<Integer>();
	public static List<Integer> forgottenSoulsLoot = new ArrayList<Integer>();
	public static List<Integer> caveBugsLoot = new ArrayList<Integer>();
	public static List<Integer> goblinLoot = new ArrayList<Integer>();
	public static List<Integer> skeletonLoot = new ArrayList<Integer>();
	public static List<Integer> caveSlimeLoot = new ArrayList<Integer>();
	public static List<Integer> dwarfLoot = new ArrayList<Integer>();
	public static List<Integer> icefiendLoot = new ArrayList<Integer>();
	public static List<Integer> kalphiteWorkerLoot = new ArrayList<Integer>();
	public static List<Integer> lanzigLoot = new ArrayList<Integer>();
	public static List<Integer> minotaurLoot = new ArrayList<Integer>();
	public static List<Integer> sandcrabsLoot = new ArrayList<Integer>();
	
	public static List<Integer> allSlayerLoot = new ArrayList<Integer>();
	
	
	public static void initializeLists()
	{
		herbTable.add(id.grimyRanarr);
		herbTable.add(id.grimyAvantoe);
		herbTable.add(id.grimyKwuarm);
		herbTable.add(id.grimyCadantine);
		herbTable.add(id.grimyLantadyme);
		
		seedTable.add(id.snapdragonSeed);
		seedTable.add(id.torstolSeed);
		seedTable.add(id.toadflaxSeed);
		seedTable.add(id.snapegrassSeed);
		
		rareDropTable.add(id.shieldLeftHalf);
		rareDropTable.add(id.dragonSpear);
		rareDropTable.add(id.runeSpear);
		rareDropTable.add(id.keyLoopHalf);
		rareDropTable.add(id.keyToothHalf);
		rareDropTable.add(id.uncutDiamond);

		forgottenSoulsLoot.add(id.mithBolts);
		forgottenSoulsLoot.add(id.lawRune);
		forgottenSoulsLoot.add(id.fireBattlestaff);
		forgottenSoulsLoot.add(id.earthBattlestaff);
		forgottenSoulsLoot.add(id.airBattlestaff);
		forgottenSoulsLoot.add(id.waterBattlestaff);
		
		skeletonLoot.add(id.natureRune);
		skeletonLoot.add(id.fireTalisman);
		
		dwarfLoot.add(id.natureRune);
		
		lanzigLoot.add(id.fremmy_lyre);
		lanzigLoot.add(id.grimyRanarr);
		lanzigLoot.addAll(rareDropTable);
		
		sandcrabsLoot.add(id.casket);

		caveSlimeLoot.add(id.ironBoots);
		
		caveCrawlersLoot.add(id.natureRune);

		kalphiteWorkerLoot.add(id.natureRune);
		kalphiteWorkerLoot.add(id.lawRune);
		kalphiteWorkerLoot.add(id.chaosRune);
		kalphiteWorkerLoot.add(id.ensouledKalphiteHead1);
		kalphiteWorkerLoot.add(id.ensouledKalphiteHead2);
		
		caveBugsLoot.add(id.natureRune);
		caveBugsLoot.add(id.waterBattlestaff);
		
		hillGiantsLoot.add(id.giantKey);
		hillGiantsLoot.add(id.natureRune);
		hillGiantsLoot.add(id.deathRune);
		hillGiantsLoot.add(id.chaosRune);
		hillGiantsLoot.add(id.cosmicRune);
		hillGiantsLoot.add(id.lawRune);
		hillGiantsLoot.add(id.bodyTalisman);
		
		//create list of all loot from slayer monsters 
		allSlayerLoot.addAll(kalphiteWorkerLoot);
		allSlayerLoot.addAll(forgottenSoulsLoot);
		allSlayerLoot.addAll(herbTable);
		allSlayerLoot.addAll(seedTable);
		allSlayerLoot.addAll(rareDropTable);
		allSlayerLoot.add(id.casket);
		allSlayerLoot.add(id.fireTalisman);
		allSlayerLoot.add(id.bigBones);
		
		//add each monster to the generic tables which can be shared

		minotaurLoot.addAll(rareDropTable);
		
		kalphiteWorkerLoot.addAll(herbTable);
		kalphiteWorkerLoot.addAll(rareDropTable);
		
		icefiendLoot.addAll(rareDropTable);
		
		sandcrabsLoot.addAll(rareDropTable);
		
		dwarfLoot.addAll(rareDropTable);
		
		caveSlimeLoot.addAll(rareDropTable);
		
		goblinLoot.addAll(herbTable);
		
		skeletonLoot.addAll(herbTable);
		skeletonLoot.addAll(rareDropTable);
		
		caveBugsLoot.addAll(herbTable);
		caveBugsLoot.addAll(rareDropTable);
		
		hillGiantsLoot.addAll(herbTable);
		hillGiantsLoot.addAll(seedTable);
		hillGiantsLoot.addAll(rareDropTable);
		
		caveCrawlersLoot.addAll(herbTable);
		caveCrawlersLoot.addAll(seedTable);
		caveCrawlersLoot.addAll(rareDropTable);
		
		forgottenSoulsLoot.addAll(herbTable);
		forgottenSoulsLoot.addAll(rareDropTable);
		
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
			(killingArea.contains(g) || g.getID() != id.bigBones) &&
			g.canReach() && 
			listToCheck.contains(g.getID());
		GroundItem g = GroundItems.closest(nearbyGroundItemFilter);
		return g;
	}
	public static GroundItem getNearbyGroundItem(int itemID, Area area)
	{
		if(itemID <= 0)
		{
			MethodProvider.log("Attempted to check ground itemID list which is less than or equal to 0!");
			return null;
		}
		
		Filter<GroundItem> nearbyGroundItemFilter = g -> 
			g != null && 
			g.exists() &&
			g.distance() <= 15 &&
			(area.contains(g) || g.getID() != id.bigBones) &&
			g.canReach() && 
			itemID == g.getID();
		GroundItem g = GroundItems.closest(nearbyGroundItemFilter);
		return g;
	}
	/**
	 * Gets a nearby ground item according to most valuable above a given threshold. 
	 * Set boolean shouldBeReachable to false if you intend to telegrab items, true if walking to loot
	 * 
	 */
	public static GroundItem getValuableNearbyItem(int valueThreshold, Area killingArea, boolean shouldBeReachable)
	{
		if(valueThreshold <= 0)
		{
			MethodProvider.log("Attempted to check ground items value which is 0 or less...");
			return null;
		}
		
		Filter<GroundItem> nearbyGroundItemFilter = g -> 
			g != null && 
			g.exists() &&
			g.distance() <= 15 &&
			(killingArea.contains(g) || g.getID() != id.bigBones) &&
			((shouldBeReachable && g.canReach()) || !shouldBeReachable) &&
			g.getItem().getLivePrice() >= valueThreshold;
		List<GroundItem> g = GroundItems.all(nearbyGroundItemFilter);
		if(g == null || g.isEmpty()) return null;
		Collections.sort(g, new Comparator<GroundItem>() {
			@Override
			public int compare(GroundItem o1, GroundItem o2) {
				Integer i = o1.getItem().getValue();
				Integer i2 = o2.getItem().getValue();
				return i.compareTo(i2);
			}
			});
		Collections.reverse(g);
		return g.get(0);
	}
	/**
	 * returns false if inventory is full and no more food or junk to drop,
	 * returns true otherwise
	 * @param g
	 * @return
	 */
	public static boolean grabNearbyGroundItem(GroundItem g)
	{
		if(g == null) return true;
		if(Inventory.isFull())
		{
			if(Inventory.count(id.jug) > 0)
			{
				if(Inventory.dropAll(id.jug))
				{
					MethodProvider.log("Dropped a jug");
				}
			}
			for(int food : Combatz.foods)
			{
				if(Inventory.count(food) > 0)
				{
					if(Inventory.drop(food))
					{
						MethodProvider.log("Dropped a food: " + new Item(food, 1).getName());
					}
				}
			}
		}
		if(Inventory.isFull())
		{
			MethodProvider.log("Inventory full but found nearby ground item, confused wat do?? : "+g.getName());
			return false;
		}
		
		MethodProvider.log("Attempting to grab ground item: "+g.getName() +" in amount: " + g.getAmount());
		if(!Walking.isRunEnabled() && Walking.getRunEnergy() > 3) Walking.toggleRun(); 
		final int count = Inventory.count(g.getID());
		if(g.interact("Take")) 
		{
			MethodProvider.sleepUntil(() -> Inventory.count(g.getID()) > count, Sleep.calculate(2222, 2222));
		}
		return true;
	}
}
