package script.skills.magic;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
/**
 * Trains magic 1-83
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainMagic extends Leaf {
	public static boolean initialized = false;
	public static boolean completedMagic = false;
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
        	MethodProvider.log("[UNSCRIPTED] -> 1 - 83 magic!");
        	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Magic!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
    	if (Skills.getRealLevel(Skill.MAGIC) >= DecisionLeaf.mageSetpoint) {
            MethodProvider.log("[COMPLETE] -> lvl "+DecisionLeaf.mageSetpoint+" magic!");
            completedMagic = true;
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_MAGIC;
	}
}
