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
	public static boolean unlockedLumbyCave()
	{
		return PlayerSettings.getBitValue(279) == 1;
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
	 * 5: Birds
	 */
	
	public static String getTaskName()
	{
		if(slayerMonsterID == 1) return "Monkey";
		if(slayerMonsterID == 2) return "Goblin";
		if(slayerMonsterID == 3) return "Giant rat";
		if(slayerMonsterID == 4) return "Giant spider";
		if(slayerMonsterID == 5) return "Seagull";
		if(slayerMonsterID == 6) return "Cow";
		if(slayerMonsterID == 7) return "Scorpion";
		if(slayerMonsterID == 8) return "Bat";
		if(slayerMonsterID == 9) return "Wolf";
		if(slayerMonsterID == 10) return "Zombie";
		if(slayerMonsterID == 11) return "Skeleton";
		if(slayerMonsterID == 12) return "Forgotten Soul";
		if(slayerMonsterID == 13) return "Bear Cub";
		if(slayerMonsterID == 22) return "Guard dog";
		if(slayerMonsterID == 26) return "Bat";
		if(slayerMonsterID == 37) return "Cave crawler";
		if(slayerMonsterID == 53) return "Kalphite Worker";
		if(slayerMonsterID == 57) return "Dwarf";
		if(slayerMonsterID == 62) return "Cave slime";
		if(slayerMonsterID == 63) return "Cave bug";
		if(slayerMonsterID == 68) return "Lizard";
		if(slayerMonsterID == 75) return "Icefiend";
		if(slayerMonsterID == 76) return "Minotaur";
		return null;
	}
}
