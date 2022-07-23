package script.utilities;

import java.util.ArrayList;
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
	public static List<Integer> minotaurLoot = new ArrayList<Integer>();
	
	public static List<Integer> allSlayerLoot = new ArrayList<Integer>();
	public static final int grimyRanarr = 207;
	public static final int grimyAvantoe = 211;
	public static final int grimyKwuarm = 213;
	public static final int grimyCadantine = 215;
	public static final int grimyLantadyme = 2485;

	public static final int snapdragonSeed = 5300;
	public static final int ranarrSeed = 5295;
	public static final int torstolSeed = 5304;
	public static final int toadflaxSeed = 5296;
	public static final int snapegrassSeed = 22879;
	
	public static final int giantKey = 20754;
	public static final int natureRune = 561;
	public static final int chaosRune = 562;
	public static final int bigBones = 532;

	public static final int ensouledKalphiteHead1 = 13489;
	public static final int ensouledKalphiteHead2 = 13490;
	
	public static final int mithBolts = 9142;
	public static final int lawRune = 563;
	public static final int fireBattlestaff = 1393;
	public static final int earthBattlestaff = 1399;
	public static final int airBattlestaff = 1397;
	public static final int waterBattlestaff = 1395;
	
	public static final int shieldLeftHalf = 2366;
	public static final int dragonSpear = 1249;
	public static final int runeSpear = 1247;
	public static final int keyLoopHalf = 987;
	public static final int keyToothHalf = 985;
	public static final int uncutDiamond = 1617;
	
	public static final int fireTalisman = 1442;
	
	
	public static void initializeLists()
	{
		herbTable.add(grimyRanarr);
		herbTable.add(grimyAvantoe);
		herbTable.add(grimyKwuarm);
		herbTable.add(grimyCadantine);
		herbTable.add(grimyLantadyme);
		
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

		forgottenSoulsLoot.add(mithBolts);
		forgottenSoulsLoot.add(lawRune);
		forgottenSoulsLoot.add(fireBattlestaff);
		forgottenSoulsLoot.add(earthBattlestaff);
		forgottenSoulsLoot.add(airBattlestaff);
		forgottenSoulsLoot.add(waterBattlestaff);
		
		skeletonLoot.add(natureRune);
		skeletonLoot.add(fireTalisman);
		
		dwarfLoot.add(natureRune);
		
		caveCrawlersLoot.add(natureRune);

		kalphiteWorkerLoot.add(natureRune);
		kalphiteWorkerLoot.add(lawRune);
		kalphiteWorkerLoot.add(chaosRune);
		kalphiteWorkerLoot.add(ensouledKalphiteHead1);
		kalphiteWorkerLoot.add(ensouledKalphiteHead2);
		
		caveBugsLoot.add(natureRune);
		caveBugsLoot.add(waterBattlestaff);
		
		hillGiantsLoot.add(giantKey);
		hillGiantsLoot.add(natureRune);
		hillGiantsLoot.add(bigBones);
		
		//create list of all loot from slayer monsters 
		allSlayerLoot.addAll(kalphiteWorkerLoot);
		allSlayerLoot.addAll(forgottenSoulsLoot);
		allSlayerLoot.addAll(herbTable);
		allSlayerLoot.addAll(seedTable);
		allSlayerLoot.addAll(rareDropTable);
		allSlayerLoot.add(fireTalisman);
		allSlayerLoot.add(bigBones);
		
		//add each monster to the generic tables which can be shared

		minotaurLoot.addAll(rareDropTable);
		
		kalphiteWorkerLoot.addAll(herbTable);
		kalphiteWorkerLoot.addAll(rareDropTable);
		
		icefiendLoot.addAll(rareDropTable);
		
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
			if(Inventory.count(TrainRanged.jug) > 0)
			{
				if(Inventory.dropAll(TrainRanged.jug))
				{
					MethodProvider.log("Dropped a jug");
				}
			}
			for(int food : Combat.foods)
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
			return;
		}
		
		MethodProvider.log("Attempting to grab ground item: "+g.getName() +" in amount: " + g.getAmount());
		if(!Walking.isRunEnabled() && Walking.getRunEnergy() > 3) Walking.toggleRun(); 
		final int count = Inventory.count(g.getID());
		if(g.interact("Take")) 
		{
			MethodProvider.sleepUntil(() -> Inventory.count(g.getID()) > count, Sleep.calculate(2222, 2222));
		}
	}
}
