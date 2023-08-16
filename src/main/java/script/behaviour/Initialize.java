package script.behaviour;

import java.time.LocalTime;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.utilities.Logger;

import script.framework.Leaf;
import script.quest.fremenniktrials.FremennikTrials;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.InvEquip;
import script.utilities.ItemsOnGround;
import script.utilities.Locations;
import script.utilities.Sleepz;
import script.utilities.id;


public class Initialize extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return !API.initialized;
	 }
   
    @Override
    public int onLoop() {
    	API.rand2.setSeed(LocalTime.now().getNano());
    	Sleepz.initSleepMod = 1.2 + (API.rand2.nextDouble()/1.25);
    	Sleepz.initSleepMod = Sleepz.initSleepMod * Sleepz.initSleepMod;
    	//all initial randomizations that depend on new random seed go here
		InvEquip.clearEquipmentSlots();
		InvEquip.initializeIntLists();
		id.initializeIDLists();
		Combatz.initializeFoods();
		Locations.initialize();
		FremennikTrials.initialize();
		ItemsOnGround.initializeLists();

		DecisionLeaf.initialize();
		if((int) Calculations.nextGammaRandom(100,50) >= 120) Locations.scorpions = Locations.scorpionsKharidNorth;
		else Locations.scorpions = Locations.scorpionsKharidSouth;
		if((int) Calculations.nextGammaRandom(100,50) >= 120) Locations.kalphiteWorkersArea = Locations.kalphiteWorkers2;
		else Locations.scorpions = Locations.kalphiteWorkers1;
		Logger.log("Initialized");
		API.initialized = true;
        return 5;
    }
}
