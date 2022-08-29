package script;

import org.dreambot.api.methods.MethodProvider;

import script.actionz.UniqueActions;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Dialoguez;
import script.utilities.Sleep;
import script.utilities.API.modes;

public class Test extends Leaf {

	@Override
	public boolean isValid() {
		return API.mode == modes.TEST;
	}
	public static boolean test = false;
	@Override
	public int onLoop() {
		UniqueActions.examineRandomObect();
		return Sleep.calculate(2222,2222);
	}
	
}
