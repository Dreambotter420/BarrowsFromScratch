package script.quest.magearena2;

import org.dreambot.api.utilities.Logger;

import script.framework.Leaf;
import script.framework.Tree;
import script.utilities.Sleepz;
import script.utilities.API;

/**
 * Completes Mage Arena 2
 * @author Dreambotter420
 * ^_^
 */
public class MageArena2 extends Leaf {
	public static boolean started = false;
	public static boolean completedMageArena2 = false;
    public void onStart() {
        
        instantiateTree();
        started = true;
    }

    private final Tree tree = new Tree();
    private void instantiateTree() {
        
    }
    @Override
    public int onLoop() {
        if (true) {
            Logger.log("[UNSCRIPTED] -> Mage Arena 2");
            completedMageArena2 = true;
           	API.mode = null;
            return Sleepz.sleepTiming();
        }
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.MAGE_ARENA_2;
	}
}
