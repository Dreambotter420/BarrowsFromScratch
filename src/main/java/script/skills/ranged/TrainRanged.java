package script.skills.ranged;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
/**
 * Trains ranged 1-75
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainRanged extends Leaf {
	public static boolean initialized = false;
	public static boolean completedRanged = false;
    public void onStart() {
        
        instantiateTree();
        initialized = true;
    }

    private final Tree tree = new Tree();
    private void instantiateTree() {
    	
    }
    @Override
    public int onLoop() {
        if(true)
        {
        	MethodProvider.log("[UNSCRIPTED] -> 1 - 75 ranged!");
        	API.mode = null;
        	return Timing.sleepLogNormalSleep();
        }
    	if (Skills.getRealLevel(Skill.RANGED) >= 75) {
            MethodProvider.log("[COMPLETE] -> lvl 75 ranged!");
            completedRanged = true;
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_RANGE;
	}
}
