package script.utilities;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public class Locations {
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
