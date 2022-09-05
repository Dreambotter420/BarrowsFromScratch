package script;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.wrappers.widgets.message.Message;
import org.dreambot.api.wrappers.widgets.message.MessageType;

import script.actionz.ReportSpammer;
import script.behaviour.CustomizeSettings;
import script.quest.alfredgrimhand.AlfredGrimhandsBarcrawl;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.ernestthechicken.ErnestTheChicken;
import script.quest.fremenniktrials.FremennikTrials;
import script.quest.fremenniktrials.FremennikTrials.Direction;
import script.quest.magearena1.MageArena1;
import script.quest.naturespirit.NatureSpirit;
import script.quest.waterfallquest.WaterfallQuest;
import script.skills.prayer.TrainPrayer;
import script.skills.ranged.Mobs;
import script.utilities.API;
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
		if(txt.contains("You put some ectoplasm and bonemeal into the Ectofunctus, and worship it.")) AnimalMagnetism.worshipped = true;
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
		if(API.mode == API.modes.NATURE_SPIRIT)
		{
			if(txt.contains("The stone seems to absorb the fungus.") || 
					txt.contains("This stone seems to be complete already.")) NatureSpirit.placedFungus = true;
			if(txt.contains("The stone seems to absorb the used spell scroll.")) NatureSpirit.placedScroll = true;
		}
		if(API.mode == API.modes.ALFRED_GRIMHANDS_BARCRAWL)
		{
			if(txt.contains("The barmaid signs your card.") || 
					txt.contains("The bartender signs your card.") || 
					txt.contains("Zambo signs your card.") || 
					txt.contains("Blurberry signs your card.") || 
					txt.contains("You think you see 2 bartenders signing 2 barcrawl cards.") || 
					txt.contains("signing your barcrawl card") || 
					txt.contains("The bartender scrawls his signature on your card.") || 
					txt.contains("You can just about make out the bartender")) AlfredGrimhandsBarcrawl.checkedCard = false;
			if(txt.contains("You are too drunk to be able to read the barcrawl card."))
			{
				AlfredGrimhandsBarcrawl.finishedCard = true;
			}
		}
		
		if(API.mode == API.modes.FREMENNIK_TRIALS)
		{
			if(txt.contains("You empty the keg and refill it with low alcohol beer")) FremennikTrials.filledLowAlcoholKeg = true;
			if(txt.contains("The Draugen has moved elsewhere")) 
			{
				FremennikTrials.currentDirection = null;
				FremennikTrials.hoppedToRightDraugenWorld = false;
			}
			if(txt.contains("You have solved the riddle!")) FremennikTrials.solvedRiddle = true;
			
			if(txt.contains("The talisman guides you north-east")) FremennikTrials.currentDirection = Direction.NORTHEAST;
			if(txt.contains("The talisman guides you south")) FremennikTrials.currentDirection = Direction.SOUTH;
			if(txt.contains("The talisman guides you south-east")) FremennikTrials.currentDirection = Direction.SOUTHEAST;
			if(txt.contains("The talisman guides you south-west")) FremennikTrials.currentDirection = Direction.SOUTHWEST;
			if(txt.contains("The talisman guides you north-west")) FremennikTrials.currentDirection = Direction.NORTHWEST;
			if(txt.contains("The talisman guides you north")) FremennikTrials.currentDirection = Direction.NORTH;
			if(txt.contains("The talisman guides you east")) FremennikTrials.currentDirection = Direction.EAST;
			if(txt.contains("The talisman guides you west")) FremennikTrials.currentDirection = Direction.WEST;
		}
		String tx = txt.toLowerCase();
		if(msg.getType() == MessageType.PLAYER &&
				(txt.contains("sëriöüsly") || 
				txt.contains("fütürë") || 
				txt.contains("yöü") || 
				txt.contains("cärëfül") ||
				txt.contains("scäm") || 
				tx.contains("provably") ||
				tx.contains("has been paid") ||
				tx.contains("reported") ||
				tx.contains("venezuelan") ||
				tx.contains("venni") ||
				tx.contains("venny") ||
				tx.contains("scambot") ||
				tx.contains("bet") || 
				tx.contains("gambl") || 
				tx.contains("jackpot") ||
				tx.contains("scammer") ||
				tx.contains("buying tbow") ||
				tx.contains("2b for?") ||
				tx.contains("doubling") ||
				tx.contains("wagers")))
		{
	    	//filter ironman etc stuffs from the name string
	    	String username = msg.getUsername();
			if(username.contains(">")) username = username.split(">",2)[1];
			if(ReportSpammer.spammerToReport == null) ReportSpammer.spammerToReport = username;
		}
	}
}
