package script.behaviour;

import org.dreambot.api.methods.settings.PlayerSettings;

import script.framework.Branch;


public class OffTutorialIsland extends Branch
{

    @Override
    public boolean isValid() {
    	return PlayerSettings.getConfig(281) == 1000;
    }

}
