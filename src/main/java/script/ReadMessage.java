package script;

import org.dreambot.api.wrappers.widgets.message.Message;

public class ReadMessage {
	public static void readGameMessage(Message msg)
	{
		
	}
	
	public static void readPlayerMessage(Message msg)
    {
    	String username = msg.getUsername();
    	String txt = msg.getMessage();
    }
}
