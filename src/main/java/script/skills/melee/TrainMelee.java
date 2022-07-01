package script.skills.melee;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.behaviour.DecisionLeaf;
import script.framework.Branch;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;

public class TrainMelee extends Leaf{
	public static boolean started = false;
	private final static Tree tree = new Tree();
	
	public static void initializeTree()
	{
		tree.addBranches(new BuyWealth());
	}
	
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_MELEE;
	}

	@Override
	public int onLoop() {
		if(!started) initializeTree();
		if(true)
		{
			MethodProvider.log("[UNSCRIPTED] -> Melee training!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
		}
		final int att = Skills.getRealLevel(Skill.ATTACK);
		final int attGoal = DecisionLeaf.attSetpoint;
		final int str = Skills.getRealLevel(Skill.STRENGTH);
		final int strGoal = DecisionLeaf.strSetpoint;
		if(att >= attGoal && str >= strGoal)
		{
			MethodProvider.log("[FINISHED] -> att lvl: "+ attGoal +" and str lvl: "+strGoal+"!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
		}
		if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Melee training!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
		return tree.onLoop();
	}
	
}
