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
		if(DecisionLeaf.taskTimer.finished()) 
		{
			API.mode = null;
			return 10;
		}
		long sleep = DecisionLeaf.taskTimer.remaining();
		ScriptReloader reloader = new ScriptReloader(sleep);
		MethodProvider.log("Attempting to break script, will unpause automatically after: "+ sleep + "ms ("+((double)sleep / 60000)+" minutes)");
		Thread t = new Thread(reloader);
		t.start();
		return 1000;
	}

}
