package script;

import org.dreambot.api.wrappers.widgets.message.Message;

import script.skills.prayer.TrainPrayer;

public class ReadMessage {
	public static void readGameMessage(Message msg)
	{
		String txt = msg.getMessage();
		if(txt.contains("That player is offline, or has privacy mode enabled."))
		{
			TrainPrayer.visitedLast = false;
		}
	}
	
	public static void readPlayerMessage(Message msg)
    {
    	String username = msg.getUsername();
    	String txt = msg.getMessage();
    }
}
