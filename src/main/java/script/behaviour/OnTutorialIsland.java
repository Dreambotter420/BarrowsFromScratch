package script.behaviour;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.settings.PlayerSettings;

import script.framework.Branch;
import script.framework.Leaf;

public class OnTutorialIsland extends Leaf
{

    @Override
    public boolean isValid() {
        return PlayerSettings.getConfig(281) < 1000;
    }

	@Override
	public int onLoop() {
		MethodProvider.log("Please complete tutorial island manually thanks ;-) it reduces ban-rates ya know? K thx bye");
		
		return -1;
	}
    
}