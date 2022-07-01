package script.quest.fremenniktrials;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.settings.PlayerSettings;

import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;

import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Fremennik Trials
 * @author Dreambotter420
 * ^_^
 */
public class FremennikTrials extends Leaf {
	public static boolean started = false;
	public static boolean completedFremennikTrials = false;
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
            MethodProvider.log("[UNSCRIPTED] -> Fremennik Trials");
            completedFremennikTrials = true;
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.FREMENNIK_TRIALS;
	}
}
