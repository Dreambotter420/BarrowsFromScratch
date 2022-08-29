package script.actionz;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.utilities.Timer;
import script.actionz.UniqueActions.Actionz;
import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;


public class PushCameraUp extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return UniqueActions.isActionEnabled(Actionz.PREFER_CAMERA_UP) &&
	    		(pushupTimer == null || pushupTimer.finished());
	 }
   public static Timer pushupTimer = null;
    @Override
    public int onLoop() {
    	if(pushupTimer == null)
    	{
    		int timer = (int) Calculations.nextGaussianRandom(600000, 300000) + Calculations.random(1000,100000);
    		MethodProvider.log("Setting Camera Push-up timer: " + Timer.formatTime(timer));
    		pushupTimer = new Timer(timer);
    		return 10;
    	}
    	if(pushupTimer.finished())
    	{
    		if(Camera.getPitch() >= 250) 
    		{
    			pushupTimer = null;
        		return 10;
    		}
    		Camera.keyboardRotateToPitch((int) Calculations.nextGaussianRandom(340,30));
    	}
    	return Timing.sleepLogNormalSleep();
    }
}
