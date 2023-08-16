package script;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.interactive.GameObject;

import script.framework.Leaf;
import script.utilities.Locations;
import script.utilities.Sleepz;

public class HardCodedWildyFixes extends Leaf {

	@Override
	public boolean isValid() {
		return (Locations.stuckInFerox.contains(Players.getLocal()) ||
				Locations.stuckInFerox2.contains(Players.getLocal())) ||
				(Widgets.get(475,11,1) != null && 
	    		Widgets.get(475,11,1).isVisible() &&
	    		Widgets.get(475,11,1).getText().contains("Enter Wilderness"));
	}

	@Override
	public int onLoop() 
	{
		//stuck in ferox enclave
		if (Locations.stuckInFerox.contains(Players.getLocal()) ||
				Locations.stuckInFerox2.contains(Players.getLocal()))
	    {
	        if(Widgets.get(229, 1) != null && 
	        		Widgets.get(229, 1).isVisible() &&
	        		Widgets.get(229, 1).getText().contains("When returning to the Enclave, if you are teleblocked, you will not"))
	        {
	        	Dialogues.continueDialogue();
	        	return Sleepz.calculate(694, 111);
	        }
	        else if(Dialogues.areOptionsAvailable())
	        {
	        	Dialogues.chooseOption("Yes, and don\'t ask again.");
	        	return Sleepz.calculate(694, 111);
	        }
	    	Sleepz.sleep(111, 111);
	    	if(Players.getLocal().isMoving()) return Sleepz.calculate(111, 111);
	    	GameObject barrier = GameObjects.closest("Barrier");
	    	if(barrier == null) Logger.log("Barrier null in Ferox Enclave! :-(");
	    	else barrier.interact("Pass-through");
	    	return Sleepz.calculate(1111, 111);
	    }
		 //check wildy ditch cross sign
		if(Widgets.get(475,11,1) != null && 
	    		Widgets.get(475,11,1).isVisible() &&
	    		Widgets.get(475,11,1).getText().contains("Enter Wilderness"))
	    {
	    	Widgets.get(475,11,1).interact();
	    	Sleepz.sleep(3000, 111);
	    }
		return Sleepz.calculate(694, 111);
	}
}