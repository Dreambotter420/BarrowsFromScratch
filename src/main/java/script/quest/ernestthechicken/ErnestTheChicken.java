package script.quest.ernestthechicken;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Completes Ernest The Chikken
 * @author Dreambotter420
 * ^_^
 */
public class ErnestTheChicken extends Leaf {
	public static boolean completedErnestTheChikken = false;
	public static final int poison = 273;
	public static final int fishfood = 272;
	public static final int poisonedFood = 274;
	public static final int key = 275;
	public static final int rubberTube = 276;
	public static final int gauge = 271;
	public static final int oilCan = 277;
	
	public static boolean poisonedFountain = false;
    
	@Override
	public boolean isValid() {
		return API.mode == API.modes.ERNEST_THE_CHIKKEN;
	}
	public static boolean onExit()
	{
		if(Widgets.getWidgetChild(153,16) != null && 
    			Widgets.getWidgetChild(153,16).isVisible())
    	{
    		if(Widgets.getWidgetChild(153,16).interact("Close")) Sleep.sleep(696, 666);
    		return false;
    	}
    	if(Locations.ernest_3rdfloorMaynor.contains(Players.localPlayer()) || 
    			Locations.ernest_2ndfloorMaynor.contains(Players.localPlayer()) ||
    			Locations.ernest_SkellyTube.contains(Players.localPlayer()) ||
    			Locations.ernest_westWing.contains(Players.localPlayer()) ||
    			Locations.ernest_basementMaynor.contains(Players.localPlayer()))
    	{
    		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") && 
    				!Walkz.useJewelry(InvEquip.glory,"Edgeville"))
    		{
    			MethodProvider.log("Stuck in here in Ernest The Chikken :-( Attempting walking to GE...");
    			Walking.walk(BankLocation.GRAND_EXCHANGE);
    		}
    		return false;
    	}
    	return true;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Ernest the Chikken");
            if(onExit())
            {
                API.mode = null;
            }
            return Timing.sleepLogNormalSleep();
        }
        if (completedErnestTheChikken) {
            if(onExit())
            {
            	MethodProvider.log("[COMPLETED] -> Ernest the Chikken!");
                Main.customPaintText1 = "~~~~~~~~~~~~~~~";
        		Main.customPaintText2 = "~Quest Complete~";
        		Main.customPaintText3 = "~Ernest The Chikken~";
        		Main.customPaintText4 = "~~~~~~~~~~~~~~~";
               	API.mode = null;
            }
        	
            return Timing.sleepLogNormalSleep();
        }
        
        if(Dialogues.getNPCDialogue() != null && !Dialogues.getNPCDialogue().isEmpty())
        {
        	MethodProvider.log("NPC Dialogue: " + Dialogues.getNPCDialogue());
		}
        if(handleDialogues()) return Timing.sleepLogNormalSleep();
        switch(getProgressValue())
        {
        case(3):
        {
        	completedErnestTheChikken = true;
        	break;
        }
        case(2):
        {
        	if(!InvEquip.equipmentContains(InvEquip.wearableGlory) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth))
         	{
         		fulfillStart();
         		return Timing.sleepLogNormalSleep();
         	}
        	gatherSuppliesThenTalkToOddenstein();
        	break;
        }
        case(1):
        {
        	if(!InvEquip.equipmentContains(InvEquip.wearableGlory) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth))
         	{
         		fulfillStart();
         		return Timing.sleepLogNormalSleep();
         	}
        	gatherSuppliesThenTalkToOddenstein();
        	break;
        }
        case(0):
        {
        	if(!InvEquip.equipmentContains(InvEquip.wearableGlory) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth))
         	{
         		fulfillStart();
         		return Timing.sleepLogNormalSleep();
         	}
        	if(Locations.ernest_veronica.getCenter().distance() > 100)
            {
            	Walkz.useJewelry(InvEquip.glory, "Draynor Village");
            	break;
            }
        	API.walkTalkToNPC("Veronica", "Talk-to", Locations.ernest_veronica);
        }
        default:break;
        }
        
        return Timing.sleepLogNormalSleep();
    }
    public static void fulfillStart()
    {
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
    	InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);

    	InvEquip.addInvyItem(TrainRanged.jugOfWine, 1, 5, false, (int) Calculations.nextGaussianRandom(500, 209));
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (ErnestTheChikken)");
			return;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (ErnestTheChikken)");
			return;
		}
    }
    public static boolean isAUp() 
    {
    	return PlayerSettings.getBitValue(1788) == 0;
    }
    public static boolean isBUp() 
    {
    	return PlayerSettings.getBitValue(1789) == 0;
    	}
    public static boolean isCUp() 
    {
    	return PlayerSettings.getBitValue(1790) == 0;
    	}
    public static boolean isDUp() 
    {
    	return PlayerSettings.getBitValue(1791) == 0;
    }
    public static boolean isEUp() 
    {
    	return PlayerSettings.getBitValue(1792) == 0;
    }
    public static boolean isFUp() 
    {
    	return PlayerSettings.getBitValue(1793) == 0;
    }
    public static void gatherSuppliesThenTalkToOddenstein()
    {
    	if(!Locations.ernest_2ndfloorMaynor.contains(Players.localPlayer()) && 
    			!Locations.ernest_3rdfloorMaynor.contains(Players.localPlayer()) && 
    			!Locations.ernest_basementMaynor.contains(Players.localPlayer()) &&
    			Locations.ernest_veronica.getCenter().distance() > 100)
        {
        	Walkz.useJewelry(InvEquip.glory, "Draynor Village");
        	return;
        }
    	if(!Inventory.contains(gauge))
    	{
    		if(!poisonedFountain)
    		{
    			if(!Inventory.contains(poisonedFood))
            	{
            		if(!Inventory.contains(poison))
                	{
                		API.walkPickupGroundItem("Poison", "Take", Locations.ernest_poison);
                    	return;
                	}
                	if(!Inventory.contains(fishfood))
                	{
                		API.walkPickupGroundItem("Fish food", "Take", Locations.ernest_fishfood);
                    	return;
                	}
                	if(Inventory.get(fishfood).useOn(poison))Sleep.sleep(696, 1111);
                	return;
            	}
    			if(!Inventory.contains(rubberTube) && !Inventory.contains(key))
            	{
    				if(Locations.ernest_2ndfloorMaynor.contains(Players.localPlayer()))
    				{
    					API.interactWithGameObject("Staircase","Climb-down",Locations.ernest_2ndStairs);
    					return;
    				}
        			if(!Inventory.contains(id.spade))
                	{
                		API.walkPickupGroundItem("Spade", "Take", Locations.ernest_spade);
                    	return;
                	}
        			API.walkInteractWithGameObject("Compost heap", "Search", Locations.ernest_compost, () -> Inventory.contains(key));
                	return;
            	}
    		}
    		if(Locations.ernest_2ndfloorMaynor.contains(Players.localPlayer()))
			{
				API.interactWithGameObject("Staircase","Climb-down",Locations.ernest_2ndStairs);
				return;
			}
        	if(Walkz.walkToArea(Locations.ernest_fountain))
        	{
        		if(poisonedFountain) API.walkInteractWithGameObject("Fountain", "Search",Locations.ernest_fountain, () -> Dialogues.inDialogue() || Inventory.contains(gauge));
        		else if(Inventory.get(poisonedFood).useOn(GameObjects.closest("Fountain"))) Sleep.sleep(696, 666);
        	}
        	return;
    	}
    	if(!Inventory.contains(rubberTube))
    	{
    		if(Locations.ernest_SkellyTube.contains(Players.localPlayer()))
    		{
    			if(Combat.shouldEatFood(6)) Combat.eatFood();
    			API.walkPickupGroundItem("Rubber tube","Take",Locations.ernest_SkellyTube);
    			return;
    		}
			if(!Inventory.contains(key))
			{
				if(!Inventory.contains(id.spade))
            	{
            		API.walkPickupGroundItem("Spade", "Take", Locations.ernest_spade);
                	return;
            	}
    			API.walkInteractWithGameObject("Compost heap", "Search", Locations.ernest_compost, () -> Inventory.contains(key));
            	return;
			}
			API.walkInteractWithGameObject("Door", "Open", Locations.ernest_SkellyDoorArea, () -> Locations.ernest_SkellyTube.contains(Players.localPlayer()));
			return;
    	}
    	if(Locations.ernest_SkellyTube.contains(Players.localPlayer()))
    	{
			API.walkInteractWithGameObject("Door", "Open", Locations.ernest_SkellyDoorArea, () -> !Locations.ernest_SkellyTube.contains(Players.localPlayer()));
			return;
    	}
    	if(!Inventory.contains(oilCan))
    	{
    		if(Locations.ernest_basementMaynor.contains(Players.localPlayer()))
    		{
    			if(isAUp() && isBUp() && !isCUp() && !isDUp() && isEUp() && !isFUp())
    			{
    				API.walkPickupGroundItem("Oil can", "Take", Locations.ernest_puzzleLast);
    				return;
    			}
    			if(isAUp() && isBUp() && !isCUp() && !isDUp() && !isFUp())
    			{
    				if(!isEUp())
    				{
    					API.walkInteractWithGameObject("Lever E","Pull",Locations.ernest_puzzle5);
						Sleep.sleep(666, 1111);
						return;
    				} 
    				return;
    			}
    			if(isAUp() && isBUp() && !isDUp() && !isEUp() && !isFUp())
    			{
    				if(isCUp())
    				{
    					API.walkInteractWithGameObject("Lever C","Pull",Locations.ernest_puzzle2);
						Sleep.sleep(666, 1111);
						return;
    				} 
    				return;
    			}
    			if(isAUp() && isBUp() && isCUp() && !isDUp())
    			{
    				if(isFUp())
    				{
    					API.walkInteractWithGameObject("Lever F","Pull",Locations.ernest_puzzle5);
						Sleep.sleep(666, 1111);
						return;
    				} 
    				if(isEUp())
    				{
    					API.walkInteractWithGameObject("Lever E","Pull",Locations.ernest_puzzle5);
 						Sleep.sleep(666, 1111);
 						return;
    				}
    				return;
    			}
    			if(isCUp() && !isDUp() && isEUp() && isFUp())
    			{
    				//flip up A + B
    				if(!isBUp())
					{
						API.walkInteractWithGameObject("Lever B","Pull",Locations.ernest_puzzle1);
						Sleep.sleep(666, 1111);
						return;
					}
					if(!isAUp())
					{
						API.walkInteractWithGameObject("Lever A","Pull",Locations.ernest_puzzle1);
						Sleep.sleep(666, 1111);
						return;
					}
    				return;
    			}
    			if(isCUp() && isDUp() && isEUp() && isFUp())
    			{
    				if(!isAUp() && !isBUp())
    				{
    					//go flip D down
    					API.walkInteractWithGameObject("Lever D", "Pull", Locations.ernest_puzzle2);
    					Sleep.sleep(666, 1111);
    					return;
    				}
    				//flip down A + B
    				if(isBUp())
					{
						API.walkInteractWithGameObject("Lever B","Pull",Locations.ernest_puzzle1);
						Sleep.sleep(666, 1111);
						return;
					}
					if(isAUp())
					{
						API.walkInteractWithGameObject("Lever A","Pull",Locations.ernest_puzzle1);
						Sleep.sleep(666, 1111);
						return;
					}
    				Walking.walk(Locations.ernest_puzzle1.getCenter());
    				return;
    			}
    		}
    		if(Locations.ernest_westWing.contains(Players.localPlayer()))
        	{
    			API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.ernest_westWing, () -> Locations.ernest_basementMaynor.contains(Players.localPlayer()));
            	return;
        	}
    		API.walkInteractWithGameObject("Bookcase", "Search", Locations.ernest_westWingAnd, () -> Locations.ernest_westWing.contains(Players.localPlayer()));
        	return;
    	}
    	if(Inventory.contains(oilCan))
    	{
    		if(Locations.ernest_basementMaynor.contains(Players.localPlayer()))
    		{
    			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.ernest_puzzle1, () -> Locations.ernest_westWing.contains(Players.localPlayer()));
    			return;
    		}
    		if(Locations.ernest_westWing.contains(Players.localPlayer()))
    		{
    			API.walkInteractWithGameObject("Lever","Pull",Locations.ernest_westWing,() -> !Locations.ernest_westWing.contains(Players.localPlayer()));
    			return;
    		}
    		API.walkTalkToNPC("Professor Oddenstein","Talk-to", Locations.ernest_3rdfloorMaynor);
    		Sleep.sleep(696, 666);
    	}
    }
    public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(32);
    }
    public static boolean handleDialogues()
	{
		if(Dialogues.canContinue())
		{
			if(Dialogues.getNPCDialogue() != null && 
					Dialogues.getNPCDialogue().contains("... and a lot of piranhas!"))
			{
				poisonedFountain = false;
				MethodProvider.log("Saw not poisoned fountain, switching to un-poisoned fountain mode");
			}
			if(Dialogues.getNPCDialogue() != null && 
					Dialogues.getNPCDialogue().contains("Give \'em here then."))
			{
				if(Dialogues.continueDialogue()) Sleep.sleep(3333,3333);
			}
			if(Dialogues.continueDialogue()) Sleep.sleep(69,696);
			return true;
		}
		if(Dialogues.isProcessing())
		{
			Sleep.sleep(69,696);
			return true;
		}
		
		if(Dialogues.areOptionsAvailable())
		{
			return Dialogues.chooseOption("Yes.") || 
					Dialogues.chooseOption("I\'m looking for a guy called Ernest.") || 
					Dialogues.chooseOption("Change him back this instant!");
		}
		return false;
	}
}
