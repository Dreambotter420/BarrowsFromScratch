package script.quest.fremenniktrials;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.settings.PlayerSettings;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;

import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Fremennik Trials
 * @author Dreambotter420
 * ^_^
 */
public class FremennikTrials extends Leaf {
	public static boolean started = false;
	public static boolean completedFremennikTrials = false;
    public void onStart() {
        
        started = true;
    }
    public static boolean onExit()
    {
    	return true;
    }
  
    @Override
	public boolean isValid() {
		return API.mode == API.modes.FREMENNIK_TRIALS;
	}
    @Override
    public int onLoop() {
    	if (completedFremennikTrials) {
            MethodProvider.log("[COMPLETED] -> Fremennik Trials!");
            if(onExit())
            {
            	API.mode = null;
            }
            return Timing.sleepLogNormalSleep();
        }
    	if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Fremennik Trials");
            if(onExit())
            {
            	API.mode = null;
            }
            return Timing.sleepLogNormalSleep();
        }
    	
        return Timing.sleepLogNormalSleep();
    }

	
}
