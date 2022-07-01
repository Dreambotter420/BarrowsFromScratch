package script.behaviour;

import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;


public class CustomizeSettings extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return !API.customizeSettings();
	 }
   
    @Override
    public int onLoop() {
    	return Timing.sleepLogNormalSleep();
    }
}
