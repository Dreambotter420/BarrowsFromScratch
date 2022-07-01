package script.skills.agility;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.behaviour.DecisionLeaf;
import script.framework.Branch;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;

public class TrainAgility extends Leaf{
	public static boolean started = false;
	private final static Tree tree = new Tree();
	
	public static void initializeTree()
	{
		
	}
	
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_AGILITY;
	}

	@Override
	public int onLoop() {
		if(!started) initializeTree();
		if(true)
		{
			MethodProvider.log("[UNSCRIPTED] -> Agility training!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
		}
		final int agility = Skills.getRealLevel(Skill.AGILITY);
		final int agilityGoal = DecisionLeaf.agilitySetpoint;
		if(agility >= agilityGoal)
		{
			MethodProvider.log("[FINISHED] -> Agility lvl: "+ agilityGoal +"!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
		}
		if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Agility training!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
		return tree.onLoop();
	}
	
}
