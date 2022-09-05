package script;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;

import script.framework.Leaf;
import script.utilities.Locations;
import script.utilities.Sleep;

public class HardCodedWildyFixes extends Leaf {

	@Override
	public boolean isValid() {
		return (Locations.stuckInFerox.contains(p.l) || 
				Locations.stuckInFerox2.contains(p.l)) || 
				(Widgets.getWidgetChild(475,11,1) != null && 
	    		Widgets.getWidgetChild(475,11,1).isVisible() &&
	    		Widgets.getWidgetChild(475,11,1).getText().contains("Enter Wilderness"));
	}

	@Override
	public int onLoop() 
	{
		//stuck in ferox enclave
		if (Locations.stuckInFerox.contains(p.l) || 
				Locations.stuckInFerox2.contains(p.l))
	    {
	        if(Widgets.getWidgetChild(229, 1) != null && 
	        		Widgets.getWidgetChild(229, 1).isVisible() &&
	        		Widgets.getWidgetChild(229, 1).getText().contains("When returning to the Enclave, if you are teleblocked, you will not"))
	        {
	        	Dialogues.continueDialogue();
	        	return Sleep.calculate(694, 111);
	        }
	        else if(Dialogues.areOptionsAvailable())
	        {
	        	Dialogues.chooseOption("Yes, and don\'t ask again.");
	        	return Sleep.calculate(694, 111);
	        }
	    	Sleep.sleep(111, 111);
	    	if(p.l.isMoving()) return Sleep.calculate(111, 111);
	    	GameObject barrier = GameObjects.closest("Barrier");
	    	if(barrier == null) MethodProvider.log("Barrier null in Ferox Enclave! :-(");
	    	else barrier.interact("Pass-through");
	    	return Sleep.calculate(1111, 111);
	    }
		 //check wildy ditch cross sign
		if(Widgets.getWidgetChild(475,11,1) != null && 
	    		Widgets.getWidgetChild(475,11,1).isVisible() &&
	    		Widgets.getWidgetChild(475,11,1).getText().contains("Enter Wilderness"))
	    {
	    	Widgets.getWidgetChild(475,11,1).interact();
	    	Sleep.sleep(3000, 111);
	    }
		return Sleep.calculate(694, 111);
	}
}