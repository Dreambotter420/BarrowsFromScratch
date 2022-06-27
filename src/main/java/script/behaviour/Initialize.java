package script.behaviour;

import java.time.LocalTime;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.widget.Widgets;
import script.framework.Leaf;
import script.quest.varrockmuseum.VarrockQuiz;
import script.utilities.API;
import script.utilities.InventoryEquipment;
import script.utilities.Sleep;


public class Initialize extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return !API.initialized;
	 }
   
    @Override
    public int onLoop() {
    	Widgets.closeAll();
    	API.rand2.setSeed(LocalTime.now().getNano());
    	Sleep.initSleepMod = 1.2 + (API.rand2.nextDouble()/1.25);
    	Sleep.initSleepMod = Sleep.initSleepMod * Sleep.initSleepMod;
    	//all initial randomizations that depend on new random seed go here
		InventoryEquipment.clearEquipmentSlots();
		DecisionLeaf.initializeSetpoints();
		VarrockQuiz.onStart();
		MethodProvider.log("Initialized");
		API.initialized = true;
        return 5;
    }
}
