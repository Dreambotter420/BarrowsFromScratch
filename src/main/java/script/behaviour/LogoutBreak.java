package script.behaviour;

import org.dreambot.api.methods.MethodProvider;

import script.ScriptReloader;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleep;


public class LogoutBreak extends Leaf
{

    @Override
    public boolean isValid() {
    	return API.mode == API.modes.BREAK;
    }

	@Override
	public int onLoop() {
		int sleep = Sleep.calculate(5000,5555);
		ScriptReloader reloader = new ScriptReloader(sleep);
		MethodProvider.log("Attempting to break script, will unpause automatically after: "+ sleep + "ms");
		Thread t = new Thread(reloader);
		t.start();
		API.mode = null;
		return 3000;
	}

}
