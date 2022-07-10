package script.behaviour;

import java.time.LocalTime;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.widget.Widgets;
import script.framework.Leaf;
import script.quest.varrockmuseum.VarrockQuiz;
import script.skills.ranged.ItemsOnGround;
import script.utilities.API;
import script.utilities.InvEquip;
import script.utilities.Sleep;


public class Initialize extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return !API.initialized;
	 }
   
    @Override
    public int onLoop() {
    	API.rand2.setSeed(LocalTime.now().getNano());
    	Sleep.initSleepMod = 1.2 + (API.rand2.nextDouble()/1.25);
    	Sleep.initSleepMod = Sleep.initSleepMod * Sleep.initSleepMod;
    	//all initial randomizations that depend on new random seed go here
		InvEquip.clearEquipmentSlots();
		InvEquip.initializeIntLists();
		DecisionLeaf.initialize();
		ItemsOnGround.initializeLists();
		MethodProvider.log("Initialized");
		API.initialized = true;
        return 5;
    }
}
