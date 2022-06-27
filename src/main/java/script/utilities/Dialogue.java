package script.utilities;

import org.dreambot.api.methods.dialogues.Dialogues;

public class Dialogue {
	public static boolean continu()
	{
		if(!Dialogues.canContinue()) return false;
		else if(Dialogues.spaceToContinue()) return true;
		return false;
	}
}
