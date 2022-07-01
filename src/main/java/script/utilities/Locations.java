package script.utilities;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public class Locations {
	public static final Area camelotTrees = new Area(
			new Tile(2752, 3452, 0),
			new Tile(2756, 3451, 0),
			new Tile(2760, 3451, 0),
			new Tile(2770, 3455, 0),
			new Tile(2777, 3457, 0),
			new Tile(2781, 3467, 0),
			new Tile(2776, 3480, 0),
			new Tile(2754, 3480, 0));
	public static final Area fallyTeleSpot = new Area(2961, 3384, 2969, 3376, 0);
	public static final Area houseTeleSpot = new Area(2953, 3227, 2958, 3221, 0);
	public static final Area varrockTeleSpot = new Area(3210, 3425, 3215, 3422, 0);
	public static final Area cammyTeleSpot = new Area(2755, 3480, 2759, 3476, 0);
	public static final Area edgevilleTeleTile = new Area(3087, 3496, 3087, 3496, 0);
	public static final Area rimmington = new Area(2944, 3209, 2960, 3229, 0);
	public static final Area estateRoom = new Area(2981, 3370, 2984, 3368, 0);
	public static final Area castleWars = new Area(2446, 3081, 2435, 3098, 0);
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
