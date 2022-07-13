package script;

import org.dreambot.api.wrappers.widgets.message.Message;

import script.skills.prayer.TrainPrayer;
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
