package script.skills.slayer;

import org.dreambot.api.methods.settings.PlayerSettings;

public class SlayerSettings {
	public static int config661 = 0;
	public static int slayerMonsterID = 0;
	public static int slayerTaskQty = 0;
	public static int var4076 = 0;
	
	public static void getConfigs()
	{
		config661 = PlayerSettings.getConfig(661);
		slayerMonsterID = PlayerSettings.getConfig(395);
		slayerTaskQty = PlayerSettings.getConfig(394);
		var4076 = PlayerSettings.getBitValue(4076);
	}
	
	
	/**
	 * My data so far
	 * 
	 * -init slayer task: 34 zombies from Turael
	 * 661 0>4
	 * 395 0>10 -> slayerMonster confirmed #
	 * 394 0>34 -> killcount confirmed #
	 * 4067 ->1
	 * 
	 * slayer task monster IDs:
	 * 10: Zombie
	 * 12: Ghosts
	 * 22: Dog
	 */
	
	public static String getTaskName()
	{
		if(slayerMonsterID == 10) return "Zombie";
		if(slayerMonsterID == 22) return "Jackal";
		return null;
	}
}
