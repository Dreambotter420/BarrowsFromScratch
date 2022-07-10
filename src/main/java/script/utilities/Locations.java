package script.utilities;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.GameObject;

public class Locations {
	public static boolean unlockedKourend = true;
	public static final Area camelotTrees = new Area(
			new Tile(2752, 3452, 0),
			new Tile(2756, 3451, 0),
			new Tile(2760, 3451, 0),
			new Tile(2770, 3455, 0),
			new Tile(2777, 3457, 0),
			new Tile(2781, 3467, 0),
			new Tile(2776, 3480, 0),
			new Tile(2754, 3480, 0));
	public static final Area museumArea = new Area(1725, 4991, 1793, 4928);
	public static final Area fallyTeleSpot = new Area(2961, 3384, 2969, 3376, 0);
	public static final Area houseTeleSpot = new Area(2953, 3227, 2958, 3221, 0);
	public static final Area varrockTeleSpot = new Area(3210, 3425, 3215, 3422, 0);
	public static final Area cammyTeleSpot = new Area(2755, 3480, 2759, 3476, 0);
	public static final Area edgevilleTeleSpot = new Area(3087, 3496, 3087, 3496, 0);
	public static final Area farmingGuildTeleSpot = new Area(1245, 3720, 1252, 3714, 0);
	public static final Area woodcuttingGuildTeleSpot = new Area(1658, 3508, 1663, 3502, 0);
	public static final Area rangingGuildTeleSpot = new Area(2657, 3443, 2652, 3438, 0);
	public static final Tile draynorTeleSpot = new Tile(3105,3251,0);
	public static final Area rimmington = new Area(2944, 3209, 2960, 3229, 0);
	public static final Area estateRoom = new Area(2981, 3370, 2984, 3368, 0);
	public static final Area castleWars = new Area(2446, 3081, 2435, 3098, 0);
	public static final Area veosPisc = new Area(1829,3688, 3048, 3252, 0);
	public static final Area veosSarim = new Area(3055, 3249, 3049, 3245, 0);
	public static final Area veosLandsEnd = new Area(1502, 3399, 1506, 3409, 0);
	public static final Area shipLandsEnd = new Area(1500, 3396, 1510, 3394, 1);
	public static final Area shipSarimVeos = new Area(3050, 3240, 3060, 3242, 1);
	public static final Area shipPiscVeos = new Area(1818, 3694, 1828, 3696, 1);
	public static final Area kourendCastle2ndFloor = new Area(1643, 3701, 1587, 3653, 1);
	public static final Area kourendCastle3rdFloor = new Area(1627, 3654, 1590, 3697, 2);
	public static final Area entireKourend = new Area(1969, 4073, 1151, 3345, 0);
	public static final Area kourendGiantsCaveEntrance = new Area(1420, 3587, 1415, 3592, 0);
	public static final Area kourendGiantsCaveExit = new Area(1429, 9906, 1436, 9914, 0);
	public static final Area kourendGiantsCaveArea = new Area(1408, 9916, 1471, 9856, 0);
	public static final Area kourendGiantsSafeSpot_Hill = new Area(1430, 9876, 1430, 9876, 0);
	public static final Area kourendGiantsKillingArea_Hill = new Area(1420, 9878, 1440, 9892, 0);
	public static final Area dreambotFuckedWCGuildSouth = new Area(1614, 3438, 1670, 3513, 0);
	public static final Tile dreambotFuckedWCGuildDestSouth = new Tile(1609, 3438, 0);
	public static final Area dreambotFuckedShayzien = new Area(
			new Tile(1460, 3691, 0),
			new Tile(1470, 3689, 0),
			new Tile(1470, 3678, 0),
			new Tile(1468, 3669, 0),
			new Tile(1468, 3656, 0),
			new Tile(1480, 3651, 0),
			new Tile(1480, 3648, 0),
			new Tile(1458, 3647, 0),
			new Tile(1461, 3665, 0));
	public static final Tile dreambotFuckedShayzienDest = new Tile(1490, 3649, 0); 
	public static final Area dreambotFuckedShayzien2 = new Area(1531, 3640, 1470, 3667, 0);
	public static final Tile dreambotFuckedShayzienDest2 = new Tile(1533,3642, 0); 
	public static final Tile dreambotFuckedShayzienDest3 = new Tile(1562,3664, 0); 
	public static final Area dreambotFuckedShayzien3 = new Area(1535, 3629, 1560, 3657, 0);
	
	public static void leaveDestinationShip()
	{
		Filter<GameObject> gangplankFilter = g ->
			g != null && 
			g.exists() && 
			g.getName().equals("Gangplank") && 
			g.hasAction("Cross") && 
			g.canReach();
		GameObject gangplank = GameObjects.closest(gangplankFilter);
		if(gangplank != null)
		{
			if(gangplank.interact("Cross"))
			{
				MethodProvider.sleepUntil(() -> !isInDestinationShip(), 
					() -> Players.localPlayer().isMoving(),
					Sleep.calculate(2222,2222), 50);
			}
		}
	}
	public static boolean isInDestinationShip() {
		if(shipLandsEnd.contains(Players.localPlayer())) return true;
		if(shipSarimVeos.contains(Players.localPlayer())) return true;
		if(shipPiscVeos.contains(Players.localPlayer())) return true;
		return false;
	}
	public static boolean isInKourend() {
		if(kourendGiantsCaveArea.contains(Players.localPlayer()) || 
				entireKourend.contains(Players.localPlayer()) || 
				kourendCastle2ndFloor.contains(Players.localPlayer()) || 
				kourendCastle3rdFloor.contains(Players.localPlayer())) 
		{
			unlockedKourend = true;
			return true;
		}
		return false;
	}
	public static final Area boarZone = new Area(
			new Tile(1563, 3664, 0),
			new Tile(1562, 3660, 0),
			new Tile(1546, 3664, 0),
			new Tile(1540, 3669, 0),
			new Tile(1535, 3676, 0),
			new Tile(1546, 3682, 0),
			new Tile(1563, 3678, 0));
	public static final int edgeOfTheWorldX = 3904;
	public static boolean isInstanced() {
		if(Players.localPlayer().getX() >= edgeOfTheWorldX) return true;
		return false;
	}
	public static final Area castleWarsTrees = new Area(
			new Tile(2475, 3104, 0),
			new Tile(2465, 3120, 0),
			new Tile(2442, 3129, 0),
			new Tile(2438, 3114, 0),
			new Tile(2436, 3096, 0),
			new Tile(2436, 3081, 0),
			new Tile(2452, 3080, 0),
			new Tile(2469, 3080, 0),
			new Tile(2476, 3089, 0));
	public static final Area dontTeleToGEAreaJustWalk = new Area(
		    new Tile[] {
		        new Tile(3138, 3513, 0),
		        new Tile(3142, 3517, 0),
		        new Tile(3158, 3517, 0),
		        new Tile(3161, 3514, 0),
		        new Tile(3167, 3514, 0),
		        new Tile(3170, 3517, 0),
		        new Tile(3189, 3517, 0),
		        new Tile(3199, 3507, 0),
		        new Tile(3199, 3499, 0),
		        new Tile(3200, 3495, 0),
		        new Tile(3201, 3459, 0),
		        new Tile(3213, 3458, 0),
		        new Tile(3228, 3431, 0),
		        new Tile(3228, 3420, 0),
		        new Tile(3149, 3421, 0),
		        new Tile(3125, 3464, 0)
		    }
		);
	public static final Area teleGE = new Area(3160, 3474, 3169, 3482, 0);
	public static final Area clickableGEArea = new Area(
			new Tile(3169, 3480, 0),
			new Tile(3160, 3480, 0),
			new Tile(3155, 3486, 0),
			new Tile(3155, 3493, 0),
			new Tile(3161, 3498, 0),
			new Tile(3168, 3498, 0),
			new Tile(3174, 3493, 0),
			new Tile(3174, 3486, 0));
	public static Tile GE = new Tile(3165,3487,0);
	
}
