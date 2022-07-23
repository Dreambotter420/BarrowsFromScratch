package script;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.wrappers.widgets.message.Message;

import script.skills.prayer.TrainPrayer;
import script.skills.ranged.Mobs;
import script.utilities.InvEquip;
import script.utilities.Locations;

public class ReadMessage {
	public static void readMessage(Message msg)
	{
		String txt = msg.getMessage();
		if(txt.contains("That player is offline, or has privacy mode enabled"))
		{
			TrainPrayer.visitedLast = false;
		}
		if(txt.contains("have to be a member to log into that world."))
		{
			MethodProvider.log("[ACCOUNT] -> account not members! Need P2P acc for this script :-)");
			ScriptManager.getScriptManager().stop();
		}
		if(txt.contains("I can\'t reach that!"))
		{
			MethodProvider.log("[MOBS] -> Cannot reach NPC!");
			Mobs.cantReachThat = true;
		}
		if(txt.contains("You must travel to Great Kourend before you can use this teleport."))
		{
			Locations.unlockedKourend = false;
		}
		if(txt.contains("You should talk to the estate agent to get a house first."))
		{
			Locations.unlockedHouse = false;
		}
		if(txt.contains("Your inventory is too full to take everything") || 
				txt.contains("Your inventory is too full to take everything"))
		{
			InvEquip.collectBank = true;
		}
	}
}
