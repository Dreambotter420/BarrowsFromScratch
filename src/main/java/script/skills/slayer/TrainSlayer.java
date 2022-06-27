package script.skills.slayer;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.framework.Branch;
import script.utilities.API;

public class TrainSlayer extends Branch{
	public static int slayerGoal = 18;

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_SLAYER &&
				Skills.getRealLevel(Skill.SLAYER) <= slayerGoal;
	}
	
}
