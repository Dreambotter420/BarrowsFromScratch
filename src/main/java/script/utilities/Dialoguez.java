package script.utilities;

import java.util.Arrays;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.impl.Condition;

import script.Main;
import script.Test;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.animalmagnetism.AnimalMagnetism.Lover;
import script.quest.ernestthechicken.ErnestTheChicken;
import script.quest.fremenniktrials.FremennikTrials;
import script.quest.naturespirit.NatureSpirit;
import script.utilities.API.modes;

public class Dialoguez {
	public static String dialog = "";
	public static boolean foundDialogue = false;
	public static boolean foundWait = false;
	public static void updateLastNPCDialog()
	{
		if(Widgets.getWidgetChild(193, 2) != null && 
				Widgets.getWidgetChild(193,2).isVisible())
		{
			String txt = Widgets.getWidgetChild(193, 2).getText();
			if(txt != null && !txt.isEmpty() && !txt.equalsIgnoreCase("null")) 
			{
				dialog = txt;
				return;
			}
		}
		String dialoge = Dialogues.getNPCDialogue();
		if(dialoge != null && !dialoge.isEmpty() && !dialoge.equalsIgnoreCase("null") && !dialoge.equals(dialog))
		{
			MethodProvider.log("NPC Dialogue: " + dialoge);
			dialog = dialoge;
		}
	}
	 public static boolean handleDialogues()
		{
		 	foundWait = false;
		 	updateLastNPCDialog();
		 	observeQuestDialogue(dialog);
		 	if(Dialogues.canContinue())
			{
		 		if(doStepsWhileDialogOpen()) return true;
				
				if(UniqueActions.isActionEnabled(Actionz.HOLD_SPACE_WHEN_QUESTING))
				{
					final int timeout = Sleep.calculate(2222,2222);
					final Condition condition = () -> {
					 	updateLastNPCDialog();
					 	return observeQuestDialogue(Dialogues.getNPCDialogue()) || 
					 			Dialogues.areOptionsAvailable() || 
								!Dialogues.inDialogue();
					};
					Keyboard.holdSpace(condition,timeout);
					MethodProvider.sleepUntil(condition, timeout);
					if(foundWait) Sleep.sleep(5555, 4444);
					return true;
				}
				if(continueDialogue()) 
				{
					Sleep.sleep(420,696);
					if(foundWait) Sleep.sleep(5555, 4444);
				}
				return true;
			}
			if(Dialogues.isProcessing())
			{
				Sleep.sleep(420,696);
				return true;
			}
			 
			if(Dialogues.areOptionsAvailable()) return chooseQuestOptions();
			
			if(Dialogues.inDialogue()) Sleep.sleep(2222, 2222);
			return false;
		}
	 /**
	  * Returns true if there is some action that is necessary to perform while dialogue is open.
	  * There is a list of dialogs that can be checked against to skip this action if those specific dialogs are visible.
	  */
	public static boolean doStepsWhileDialogOpen()
	{
		switch(API.mode)
		{
		case NATURE_SPIRIT:
		{
			
			if(dialog.contains("You use the mirror on the spirit of the dead") || 
					dialog.contains("Here take a look at this, perhaps you can see that") ||
					dialog.contains("The spirit of Filliman reaches forward and takes") ||
					dialog.contains("Well, that is the most peculiar thing I\'ve ever") ||
					dialog.contains("visage apparent.") ||
					dialog.contains("Deader in fact... You bear a remarkable resemblance to") ||
					dialog.contains("I think you might be right my friend, though I still feel") ||
					dialog.contains("It must be a sign... Yes a sign... I must try to find") ||
					dialog.contains("You give the journal to Filliman") ||
					dialog.contains("Hey, I found this, maybe you can use it?") ||
					dialog.contains("My journal! That should help to collect my thoughts") ||
					dialog.contains("The spirit starts leafing through the journal.") ||
					dialog.contains("It\'s all coming back to me now. It looks like I came"))
			{
				return false;
			}
			if(NatureSpirit.getProgressValue() == 55 && 
					(NatureSpirit.placedFungus || !Inventory.contains(id.mortFungus)) && 
					(NatureSpirit.placedScroll || !Inventory.contains(id.natureSpirit_usedSpell)))
			{
				if(Locations.natureSpirit_finalPuzzleTile.contains(Players.localPlayer())) return false;
				if(Walking.walk(Locations.natureSpirit_finalPuzzleTile.getCenter())) Sleep.sleep(696,666);
				return true;
			}
			if(NatureSpirit.getProgressValue() == 25 && 
					Locations.natureSpiritGrotto.contains(Players.localPlayer()) && 
					Inventory.contains(id.natureSpirit_fillimansDiary))
			{
				NatureSpirit.fillimansDiary();
				return true;
			}
			if(NatureSpirit.getProgressValue() == 20)
			{
				NatureSpirit.takeBowlMirror();
				return true;
			}
			break;
		}
		default:break;
		}
		return false;
	}
	 public static boolean hasOption(String option)
	 {
		 if(Dialogues.areOptionsAvailable())
		 {
			 for(String dialogOption : Dialogues.getOptions())
			 {
				 if(dialogOption == null) continue;
				 if(dialogOption.equals(option)) return true;
			 }
		 }
		 return false;
	 }
	
	public static boolean continueDialogue()
	{
		if(UniqueActions.isActionEnabled(Actionz.SPACE_TO_CONTINUE))
		{
			if(Dialogues.spaceToContinue()) 
			{
				Sleep.sleep(111, 1111);
				return true;
			}
			return false;
		}
		if(Dialogues.clickContinue()) 
		{
			Sleep.sleep(111, 1111);
			return true;
		}
		return false;
	}
	public static boolean chooseOption(String option)
	{
		if(UniqueActions.isActionEnabled(Actionz.TYPE_OPTION_DIALOGUE)) 
		{
			if(Dialogues.typeOption(option))
			{
				MethodProvider.log("Typed option: "+option);
				return true;
			}
			return false;
		}
		if(Dialogues.clickOption(option))
		{
			MethodProvider.log("Clicked option: "+option);
			return true;
		}
		
		return false;
	}
	public static boolean chooseOption(String... options)
	{
		if(UniqueActions.isActionEnabled(Actionz.TYPE_OPTION_DIALOGUE)) 
		{
			for(String option : options)
			{
				if(Dialogues.typeOption(option))
				{
					MethodProvider.log("Typed option: "+option);
					return true;
				}
			}
			return false;
		}
		for(String option : options)
		{
			if(Dialogues.clickOption(option))
			{
				MethodProvider.log("Clicked option: "+option);
				return true;
			}
		}
		return false;
	}
	public static boolean chooseFirstOptionContaining(String partialOption)
	{
		String[] options = Dialogues.getOptions();
		for(int i = 1; i <= options.length; i++)
		{
			if(options[i] == null || options[i].isEmpty()) continue;
			
			if(options[i].contains(partialOption))
			{
				if(UniqueActions.isActionEnabled(Actionz.TYPE_OPTION_DIALOGUE))
				{
					if(Dialogues.typeOption(i))
					{
						MethodProvider.log("Typed option index "+i+" containing: "+partialOption);
						return true;
					}
				} else if(Dialogues.clickOption(i)) 
				{
					MethodProvider.log("Clicked option index "+i+" containing: "+partialOption);
					return true;
				}
			}
		}
		return false;
	}
	public static boolean chooseOptionIndex(int index)
	{
		if(UniqueActions.isActionEnabled(Actionz.TYPE_OPTION_DIALOGUE))
		{
			if(Dialogues.typeOption(index))
			{
				MethodProvider.log("Typed option index "+index);
				return true;
			}
		} else if(Dialogues.clickOption(index)) 
		{
			MethodProvider.log("Clicked option index "+index);
			return true;
		}
		return false;
	}
	public static boolean chooseQuestOptions()
	{
		if(Locations.strongholdLvl1.contains(Players.localPlayer()))
		{
			return chooseOption("Politely tell them no and then use the \'Report Abuse\' button.") || 
					chooseOption("No way! You\'ll just take my gold for your own! Reported!") || 
					chooseOption("Report the incident and do not click any links.") || 
					chooseOption("No, you should never allow anyone to level your account.") || 
					chooseOption("Report the player for phising.") || 
					chooseOption("Do not visit the website and report the player who messaged you.") || 
					chooseOption("Delete it - it\'s a fake!") || 
					chooseOption("The birthday of a famous person or event.") || 
					chooseOption("Don\'t give out your password to anyone. Not even close friends.") || 
					chooseOption("Read the text and follow the advice given.") || 
					chooseOption("Report the stream as a scam. Real Jagex streams have a \'verified\' mark.") || 
					chooseOption("Authenticator and two-step login on my registered email.") || 
					chooseOption("Secure my device and reset my password.") || 
					chooseOption("Don\'t give them the information and send an \'Abuse report\'.") || 
					chooseOption("No.") || 
					chooseOption("Don\'t give them my password.") || 
					chooseOption("Nobody.") || 
					chooseOption("Through account settings on oldschool.runescape.com.") || 
					chooseOption("No, you should never buy an account.") || 
					chooseOption("Only on the Old School Runescape website.") || 
					chooseOption("Talk to any banker.") || 
					chooseOption("Set up 2 step authentication with my email provider.") || 
					chooseOption("Don\'t share your information and report the player.") || 
					chooseOption("Virus scan my device then change my password.") || 
					chooseOption("Don\'t type in my password backwards and report the player.") || 
					chooseOption("Use the Account Recovery System.") || 
					chooseOption("Me.") || 
					chooseOption("No way! I\'m reporting you to Jagex!") || 
					chooseOption("Nothing, it\'s a fake.") || 
					chooseOption("Decline the offer and report that player.");
		}
		if(!Locations.unlockedKourend)
		{
			return chooseOption("That\'s great, can you take me there please?");
		}
		switch(API.mode)
		{
		case TRAIN_SLAYER:
			return chooseOption("What\'s a slayer?") || 
					chooseOption("Wow, can you teach me?");
		case WATERFALL_QUEST: return chooseOption("Yes.");
		case NATURE_SPIRIT: 
		{
			for(String option : Dialogues.getOptions())
			{
				if(option == null || option.isEmpty() || option.equalsIgnoreCase("null")) continue;
				if(option.contains("What will you do to the silver sickle?"))
				{
					API.talkToNPC("Nature Spirit", "Talk-to");
					return true;
				}
			}
			return chooseOption("I\'ve lost the Amulet of Ghostspeak.") || 
					chooseOption("Well, what is it, I may be able to help?") ||
					chooseOption("Yes.") ||
					chooseOption("How long have you been a ghost?") ||
					chooseOption("I think I\'ve solved the puzzle!") ||
					chooseOption("How can I help?") ||
					chooseOption("How long have you been a ghost?") ||
					chooseOption("Is there anything else interesting to do around here?");
		}
		case ANIMAL_MAGNETISM:
		{
			for(String option : Dialogues.getOptions())
			{
				if(option == null || option.isEmpty() || option.equalsIgnoreCase("null")) continue;
				if(option.equals("Okay"))
				{
					if(chooseOption("Okay")) 
					{
						MethodProvider.sleepUntil(() -> Locations.portPhasmatysCharterShip.contains(Players.localPlayer()), Sleep.calculate(5555,5555));
					}
					Sleep.sleep(420,2222);
					return true;
				}
			}
			return chooseOption("I would be happy to make your home into a better place.") || 
					chooseOption("I\'m here about the farmers east of here.") || 
					chooseOption("Yes.") || 
					chooseOption("Yes") || 
					chooseOption("Okay, you need it more than I do, I suppose.") || 
					chooseOption("Could I buy those chickens now, then?") || 
					chooseOption("Could I buy those chickens now?") || 
					chooseOption("Could I buy 2 chickens?") || 
					chooseOption("Could I buy two chickens?") || 
					chooseOption("Hello, I\'m here about those trees again.") || 
					chooseOption("I\'d love one, thanks.") || 
					chooseOption("I\'m here about a quest.");
		}
			
		case RESTLESS_GHOST:
			return chooseOption("Yes.") || 
					chooseOption("I\'m looking for a quest!") || 
					chooseOption("Yep, now tell me what the problem is.") || 
					chooseOption("Father Aereck sent me to talk to you.") || 
					chooseOption("He\'s got a ghost haunting his graveyard.") || 
					chooseOption("I\'ve lost the Amulet of Ghostspeak.");
		case PRIEST_IN_PERIL:
			return chooseOption("Yes.") || 
					chooseOption("Roald sent me to check on Drezel.") ||
					chooseOption("Sure. I\'m a helpful person!") ||
					chooseOption("So, what now?") ||
					chooseOption("Yes, of course.") ||
					chooseOption("Could you tell me more about this temple?") ||
					chooseOption("I\'m looking for a quest!");
		case MAGE_ARENA_1:
			return chooseOption("Yes, and don\'t show this warning again.") || 
					 chooseOption("Can I fight here?") || 
					 chooseOption("Yes indeedy.") || 
					 chooseOption("Okay, let\'s fight.") || 
					 chooseOption("I think I\'ve had enough for now.") || 
					chooseOption("Yes, I\'m brave.");
		case FIGHT_ARENA:
			return chooseOption("Yes.") || 
					chooseOption("I\'d like a Khali brew please.");
		case TEST:
			return chooseOption("I\'m fine, thanks.");
		case FREMENNIK_TRIALS:
			return (FremennikTrials.riddleAnswer != null && chooseOption("Solve the riddle")) ||
					chooseOption("Why will no-one talk to me?") || 
					chooseOption("Yes.") || 
					chooseOption("What\'s a Draugen?") || 
					chooseOption("Read the riddle") || 
					chooseOption("Present offering of a single fish.") || 
					chooseOption("Raw shark.") || 
					chooseOption("Talk about the Fremennik Trials") || 
					chooseOption("What do I have to do again?") || 
					chooseOption("Ask about the Merchant\'s trial") || 
					chooseOption("Yes");
		case ERNEST_THE_CHIKKEN:
			return chooseOption("Yes.") || 
					chooseOption("I\'m looking for a guy called Ernest.") || 
					chooseOption("Change him back this instant!");
		default:break;
		}
		return false;
	}
	public static void observeQuestOptions()
	{
		switch(API.mode)
		{
		case FREMENNIK_TRIALS:
		{
			if(hasOption("I don\'t need a reminder")) FremennikTrials.talkedToOlafTwice = true;
			break;
		}
		default:break;
		}
		
	}
	/**
	 * return true if see a phrase to indicate waiting after seeing it,
	 * otherwise scans for quest steps that are dependent on npc dialogue 
	 * and sets local quest variables accordingly
	 * @param npcDialogue
	 * @return
	 */
	public static boolean observeQuestDialogue(String npcDialogue)
	{
		if(API.mode == modes.FREMENNIK_TRIALS && Widgets.getWidgetChild(229, 1) != null && 
	 			Widgets.getWidgetChild(229,1).isVisible())
	 	{
	 		String dialogue = Widgets.getWidgetChild(229,1).getText();
	 		if(dialogue.contains("My first is in mage, but not in wizard"))
	 		{
	 			FremennikTrials.riddleAnswer = "mind";
	 		}
	 		if(dialogue.contains("My first is in tar, but not in a swamp"))
	 		{
	 			FremennikTrials.riddleAnswer = "tree";
	 		}
	 		if(dialogue.contains("My first is in the well, but not at sea"))
	 		{
	 			FremennikTrials.riddleAnswer = "life";
	 		}
	 		if(dialogue.contains("My first is in fish, but not in the sea"))
	 		{
	 			FremennikTrials.riddleAnswer = "fire";
	 		}
	 		if(dialogue.contains("My first is in water, and also in tea"))
	 		{
	 			FremennikTrials.riddleAnswer = "time";
	 		}
	 		if(dialogue.contains("My first is in wizard, but not in a mage"))
	 		{
	 			FremennikTrials.riddleAnswer = "wind";
	 		}
	 	}
		if(npcDialogue == null || npcDialogue.isEmpty()) return false;
		switch(API.mode)
		{
		case MAGE_ARENA_1:
		{
			if(npcDialogue.contains("You step into the pool of sparkling water."))
			{
				foundWait = true;
			}
			break;
		}
		case TEST:
		{
			if(npcDialogue.contains("Welcome to the Grand Exchange."))
			{
				Test.test = true;
			}
			break;
		}
		case ANIMAL_MAGNETISM:
		{
			if(npcDialogue.contains("Talk to my wife and I\'ll think about it.") || 
					npcDialogue.contains("Go tell \'er now, if you\'re not a double-dealin\' scammer,") || 
					npcDialogue.contains("Any luck wiv me wife?") ||
					npcDialogue.contains("A warning to ya, too: annoy her and I\'ll haunt ya til"))
			{
				AnimalMagnetism.talkTo = Lover.ALICE;
			}
			
			if(npcDialogue.contains("Can\'t you see, he is dead. I can\'t talk to the dead.") || 
					npcDialogue.contains("Have you spoken to my husband yet?") ||
					npcDialogue.contains("I tried that once, but all those other ghosts - and even") || 
					npcDialogue.contains("Have you asked him about the bank pass?") ||
					npcDialogue.contains("I\'ll have a word with him, then; magic has its uses I") )
			{
				AnimalMagnetism.talkTo = Lover.HUSBAND;
			}
			if(npcDialogue.contains("ridiculous fashion accessories. Those earmuffs he sells"))
			{
				AnimalMagnetism.needTalkToTurael = true;
			}
		}
		case NATURE_SPIRIT:
		{
			if(npcDialogue.contains("Oh really.. Have you placed all the items on the stones?") || 
					npcDialogue.contains("My friend, I will bless it for you and you will then be") || 
					npcDialogue.contains("sanctuary! And forever will it now be an Altar of") || 
					npcDialogue.contains("Very well my friend, prepare yourself for the blessings") || 
					npcDialogue.contains("there and watch the show, apparently it\'s quite good!"))
	 		{
				foundWait = true;
	 		}
			break;
		}
		case FREMENNIK_TRIALS:
		{
			
			if(npcDialogue.contains("As you wish outerlander; I will drink first, then you will"))
	 		{
				foundWait = true;
	 		}
	 		if(npcDialogue.contains("A maze? Is that all? Sure, it sounds simple") || 
	 				npcDialogue.contains("Hahahaha it is the most complex route I have ever") || 
	 				npcDialogue.contains("myself, and is one of the most fiendish complexity!"))
	 		{
	 			FremennikTrials.canEnterSwensenMaze = true;
	 		}
	 		if(npcDialogue.contains("(hic) I canna drink another drop! I alsho") || 
	 				npcDialogue.contains("I guessh I win then ouddaladder! (hic) Niche try,"))
	 		{
	 			FremennikTrials.participatedInKegContest = true;
	 		} 
	 		if(npcDialogue.contains("Okay. I don\'t think this will be too difficult. Any") || 
	 				npcDialogue.contains("town. A good merchant will find exactly what their") ||
	 				npcDialogue.contains("rare flower from across the sea, do you?") ||
	 				npcDialogue.contains("We are a very insular clan, so I would not expect you"))
	 		{
	 			FremennikTrials.merchant_talkedSigmund1 = true;
	 		} 
	 		if(npcDialogue.contains("That sounds like a fair deal to me, outlander.") || 
	 				npcDialogue.contains("love ballad, do you?") || 
	 				npcDialogue.contains("Well, the only musician I know of in these parts would"))
	 		{
	 			FremennikTrials.merchant_talkedSailor1 = true;
	 		} 
	 		if(npcDialogue.contains("If you can find me a pair of sturdy boots to replace") || 
	 				npcDialogue.contains("some custom sturdy boots, do you?") || 
	 				npcDialogue.contains("I\'m sorry outerlander... If I did, I would not trouble"))
	 		{
	 			FremennikTrials.merchant_talkedOlaf1 = true;
	 		} 
	 		if(npcDialogue.contains("you must have the ear of the chieftain for him to") ||  
	 				npcDialogue.contains("guarantee of a reduction on sales taxes, do you?") || 
	 				npcDialogue.contains("I will make you a pair of sturdy boots for Olaf if you"))
	 		{
	 			FremennikTrials.merchant_talkedYrsa1 = true;
	 		} 
	 		if(npcDialogue.contains("Speak to Sigli then, and you may have my promise to") ||  
	 				npcDialogue.contains("map to unspoiled hunting grounds, do you?") || 
	 				npcDialogue.contains("Sigli the hunter is the only one who knows of such"))
	 		{
	 			FremennikTrials.merchant_talkedBrundt1 = true;
	 		}
	 		if(npcDialogue.contains("I have no idea. But then again, I\'m happy with my old") || 
	 				npcDialogue.contains("finely balanced custom bowstring, do you?"))
	 		{
	 			FremennikTrials.merchant_talkedSigli1 = true;
	 		} 
	 		if(npcDialogue.contains("You get me that fish, I give you the bowstring. What") ||
	 				npcDialogue.contains("an exotic and extremely rare fish, do you?"))
	 		{
	 			FremennikTrials.merchant_talkedSkulgrimen1 = true;
	 		} 
	 		if(npcDialogue.contains("By getting me his copy of that map, I will finally be self") ||
	 				npcDialogue.contains("map of deep sea fishing spots do you?"))
	 		{ 
	 			FremennikTrials.merchant_talkedFisherman1 = true;
	 		} 
	 		if(npcDialogue.contains("I just told you: from the Seer. You will need to") ||
	 				npcDialogue.contains("weather forecast from the Fremennik Seer do you?"))
	 		{
	 			FremennikTrials.merchant_talkedSwensen1 = true;
	 		} 
	 		if(npcDialogue.contains("Do not fret, outerlander; it is a fairly simple matter. I") ||
	 				npcDialogue.contains("brave and powerful warrior to act as a bodyguard?"))
	 		{ 
	 			FremennikTrials.merchant_talkedPeerTheSeer1 = true;
	 		} 
	 		if(npcDialogue.contains("Do not fret, outerlander; it is a fairly simple matter. I"))
	 		{ 
	 			FremennikTrials.merchant_talkedPeerTheSeer1 = true;
	 		} 
	 		if(npcDialogue.contains("If you can persuade one of the Revellers to give up") ||
	 				npcDialogue.contains("the token to allow your seat at the champions table?") ||
	 				npcDialogue.contains("token to allow a seat at the champions table, do you?"))
	 		{ 
	 			FremennikTrials.merchant_talkedThorvald1 = true;
	 		} 
	 		if(npcDialogue.contains("If you can persuade her to make me her legendary") ||
	 				npcDialogue.contains("the longhall barkeeps\' legendary cocktail, do you?"))
	 		{ 
	 			FremennikTrials.merchant_talkedManni1 = true;
	 		} 
	 		if(npcDialogue.contains("Knowing that little horror, he\'ll probably be willing to in")||
	 				npcDialogue.contains("written promise from Askeladden to stay out of the"))
	 		{ 
	 			FremennikTrials.merchant_talkedThora1 = true;
	 		} 
	 		if(npcDialogue.contains("Grab a keg of beer<br>from that table near the bar"))
	 		{
	 			FremennikTrials.canPickupBeer = true;
	 		}
	 		if(npcDialogue.contains("You may not enter the battleground with any armour") || 
	 				npcDialogue.contains("combat I do not trust completely. You must go down"))
	 		{
	 			FremennikTrials.talkedToThorvaldForKoschei = true;
	 		}
	 		if(npcDialogue.contains("longer! This time you lose your prayer however, and") || 
	 				npcDialogue.contains("You show some skill at combat... I will hold back no"))
	 		{ 
	 			FremennikTrials.koschei4thForm = true;
	 		} 
			break;
		}
		case ERNEST_THE_CHIKKEN:
		{
			if(npcDialogue.contains("... and a lot of piranhas!"))
	 		{ 
				ErnestTheChicken.poisonedFountain = false;
				MethodProvider.log("Saw not poisoned fountain, switching to un-poisoned fountain mode");
	 		} 
			if(npcDialogue.contains("Give \'em here then."))
	 		{ 
				foundWait = true;
	 		} 
			break;
		}
		default: break;
		}
		return false;
	}
}
