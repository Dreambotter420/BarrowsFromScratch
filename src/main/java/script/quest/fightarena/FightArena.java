package script.quest.fightarena;

import org.dreambot.api.methods.MethodProvider;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
/**
 * Completes Fight Arena
 * @author Dreambotter420
 * ^_^
 */
public class FightArena extends Leaf {
	public static boolean started = false;
	public static boolean completedFightArena = false;
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
            MethodProvider.log("[UNSCRIPTED] -> Fight Arena");
            completedFightArena = true;
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.FIGHT_ARENA;
	}
}
