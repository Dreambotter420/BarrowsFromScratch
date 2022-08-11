package script;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.wrappers.widgets.message.Message;
import org.dreambot.api.wrappers.widgets.message.MessageType;

import script.behaviour.CustomizeSettings;
import script.behaviour.ReportSpammer;
import script.quest.ernestthechicken.ErnestTheChicken;
import script.quest.magearena1.MageArena1;
import script.quest.waterfallquest.WaterfallQuest;
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
		if(txt.contains("... then die and float to the surface.") || 
				txt.contains("You pour the poisoned fish food into the fountain.") || 
				txt.contains("The piranhas start eating the food..."))
		{
			ErnestTheChicken.poisonedFountain = true;
		}
		if(txt.contains("You fail to cut through it.") || 
				txt.contains("You slash the web apart"))
		{
			MageArena1.slashed = true;
		}
		if(txt.contains("Thank-you, your abuse report has been received."))
		{
			ReportSpammer.spammerToReport = null;
		}
		if(msg.getType() == MessageType.PLAYER &&
				txt.contains("sëriöüsly") || 
				txt.contains("fütürë") || 
				txt.contains("yöü") || 
				txt.contains("cärëfül") ||
				txt.contains("scäm"))
		{
			if(ReportSpammer.spammerToReport == null) ReportSpammer.spammerToReport = msg.getUsername();
		}
	}
}
