package script.skills.slayer;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.behaviour.DecisionLeaf;
import script.framework.Branch;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;

public class TrainSlayer extends Leaf{
	public static int slayerGoal = 18;
	public static boolean started = false;
	private final static Tree tree = new Tree();
	
	public static void initializeTree()
	{
		tree.addBranches(new BuyWealth());
	}
	
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_SLAYER &&
				Skills.getRealLevel(Skill.SLAYER) <= slayerGoal;
	}

	@Override
	public int onLoop() {
		if(true)
		{
			MethodProvider.log("[SLAYER] -> unscripted!");
			return 5;
		}
		if(!started) initializeTree();
		if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Slayer!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
		return tree.onLoop();
	}
	
}
