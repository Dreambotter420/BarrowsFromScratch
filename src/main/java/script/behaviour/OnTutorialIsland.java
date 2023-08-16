package script.behaviour;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.utilities.Logger;

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
		Logger.log("Please complete tutorial island, preferrably legit on RuneLite or Mobile ;-) it reduces ban-rates ya know? K thx bye");
		return -1;
	}
    
}
