package script.skills.woodcutting;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
/**
 * Trains woodcutting 35-40
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainWoodcutting extends Leaf {
	public static boolean initialized = false;
	public static boolean completedWoodcutting = false;
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
        	MethodProvider.log("[UNSCRIPTED] -> 1 - 35-40 woodcutting!");
        	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
    	if (Skills.getRealLevel(Skill.WOODCUTTING) >= DecisionLeaf.wcSetpoint) {
            MethodProvider.log("[COMPLETE] -> lvl "+DecisionLeaf.wcSetpoint+" woodcutting!");
            completedWoodcutting = true;
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_WOODCUTTING;
	}
}
