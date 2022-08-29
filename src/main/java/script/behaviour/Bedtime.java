package script.behaviour;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.utilities.Timer;

import script.ScriptReloader;
import script.actionz.UniqueActions;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleep;


public class Bedtime extends Leaf
{
	public static int bedtimeMinute = -1;
	public static int bedtimeHour = -1;
	public static void initialize()
	{
		bedtimeMinute = (int) Calculations.nextGaussianRandom(30, 20);
		if(UniqueActions.bedtime = UniqueActions.Bedtime.AMERICA)
	}
	public static Timer bedTimer = null;
    @Override
    public boolean isValid() {
    	return bedTimer != null;
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
