package script.quest.waterfallquest;

import org.dreambot.api.methods.settings.PlayerSettings;

public class WaterfallConfigs {
	public static int getProgressValue()
	{
		return PlayerSettings.getConfig(65);
	}
	
}
