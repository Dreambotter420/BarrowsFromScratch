package script.skills.ranged;

import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.wrappers.items.GroundItem;

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
	public static boolean nearbyGroundItemExists(List<Integer> listToCheck)
	{
		if(listToCheck == null)
		{
			MethodProvider.log("Attempted to check ground items list which is null");
			return false;
		}
		if(listToCheck.isEmpty())
		{
			MethodProvider.log("Attempted to check ground items list which is empty");
			return false;
		}
		Filter<GroundItem> nearbyGroundItemFilter = g -> 
			g != null && 
			g.exists() &&
			g.distance() <= 15 &&
			g.canReach() && 
			groundIDMatchesList(g.getID(), listToCheck);
		return false;
	}
	public static void grabNearbyGroundItem()
	{
		
	}
}
