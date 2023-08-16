package script;

import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleepz;
import script.utilities.API.modes;

public class Test extends Leaf {

	@Override
	public boolean isValid() {
		return API.mode == modes.TEST;
	}
	public static boolean test = false;
	@Override
	public int onLoop() {
		//test a thing here
		return Sleepz.calculate(2222,2222);
	}
	
}
