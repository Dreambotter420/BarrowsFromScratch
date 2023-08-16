package script.quest.ernestthechicken;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Completes Ernest The Chikken
 * @author Dreambotter420
 * ^_^
 */
public class ErnestTheChicken extends Leaf {
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
		if(Questz.checkCloseQuestCompletion()) return false;
    	if (Locations.GE.distance() > 20){
    		if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) {
    			Logger.log("Not able to teleport to GE after Ernest The Chikken :-( Attempting walking to GE...");
    			Walking.walk(BankLocation.GRAND_EXCHANGE);
    		}
    		return false;
    	}
    	return true;
	}
	public static boolean completed()
	{
		return getProgressValue() == 3;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            Logger.log("[TIMEOUT] -> Ernest the Chikken");
            if(onExit())
            {
                API.mode = null;
            }
            return Sleepz.sleepTiming();
        }
        if (completed()) {
            if(onExit())
            {
            	Logger.log("[COMPLETED] -> Ernest the Chikken!");
                Main.paint_task = "~~~~~~~~~~~~~~~";
        		Main.paint_itemsCount = "~Quest Complete~";
        		Main.paint_subTask = "~Ernest The Chikken~";
        		Main.paint_levels = "~~~~~~~~~~~~~~~";
               	API.mode = null;
            }
        	
            return Sleepz.sleepTiming();
        }
        if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("Ernest the Chicken");
        
        
        if(Dialogues.getNPCDialogue() != null && !Dialogues.getNPCDialogue().isEmpty())
        {
        	Logger.log("NPC Dialogue: " + Dialogues.getNPCDialogue());
		}
        if(Dialoguez.handleDialogues()) return Sleepz.sleepTiming();
        switch(getProgressValue())
        {
        case(2): case(1): {
        	if(!InvEquip.equipmentContains(InvEquip.wearableGlory) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth))
         	{
         		fulfillStart();
         		return Sleepz.sleepTiming();
         	}
        	gatherSuppliesThenTalkToOddenstein();
        	break;
        }
		case(0): {
        	if(!InvEquip.equipmentContains(InvEquip.wearableGlory) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth)) {
         		fulfillStart();
         		return Sleepz.sleepTiming();
         	}
        	if(Locations.ernest_veronica.getCenter().distance() > 100) {
            	Walkz.useJewelry(InvEquip.glory, "Draynor Village");
            	break;
            }
        	API.walkTalkToNPC("Veronica", "Talk-to", Locations.ernest_veronica);
        }
        default:break;
        }
        
        return Sleepz.sleepTiming();
    }
    public static void fulfillStart()
    {
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
    	InvEquip.addInvyItem(id.stamina4, 1, 1, false, 5);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);

    	InvEquip.addInvyItem(Combatz.lowFood, 1, 5, false, (int) Calculations.nextGaussianRandom(500, 209));
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			Logger.log("[INVEQUIP] -> Fulfilled equipment correctly! (ErnestTheChikken)");
			return;
		} else 
		{
			Logger.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (ErnestTheChikken)");
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
    	if(!Locations.ernest_2ndfloorMaynor.contains(Players.getLocal()) && 
    			!Locations.ernest_3rdfloorMaynor.contains(Players.getLocal()) && 
    			!Locations.ernest_basementMaynor.contains(Players.getLocal()) &&
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
            			if(!InvEquip.free1InvySpace()) return;
                		API.walkPickupGroundItem("Poison", "Take", true, Locations.ernest_poison);
                    	return;
                	}
                	if(!Inventory.contains(fishfood))
                	{
                		if(!InvEquip.free1InvySpace()) return;
                		API.walkPickupGroundItem("Fish food", "Take", Locations.ernest_fishfood);
                    	return;
                	}
                	if(Inventory.get(fishfood).useOn(poison))Sleepz.sleep(696, 1111);
                	return;
            	}
    			if(!Inventory.contains(rubberTube) && !Inventory.contains(key))
            	{
    				if(Locations.ernest_2ndfloorMaynor.contains(Players.getLocal()))
    				{
    					API.interactWithGameObject("Staircase","Climb-down",Locations.ernest_2ndStairs);
    					return;
    				}
        			if(!Inventory.contains(id.spade))
                	{
        				if(!InvEquip.free1InvySpace()) return;
                		API.walkPickupGroundItem("Spade", "Take", Locations.ernest_spade);
                    	return;
                	}
        			if(!InvEquip.free1InvySpace()) return;
        			API.walkInteractWithGameObject("Compost heap", "Search", Locations.ernest_compost, () -> Inventory.contains(key));
                	return;
            	}
    		}
    		if(Locations.ernest_2ndfloorMaynor.contains(Players.getLocal()))
			{
				API.interactWithGameObject("Staircase","Climb-down",Locations.ernest_2ndStairs);
				return;
			}
        	if(Walkz.walkToArea(Locations.ernest_fountain))
        	{
        		if(!InvEquip.free1InvySpace()) return;
        		if(poisonedFountain) API.walkInteractWithGameObject("Fountain", "Search",Locations.ernest_fountain, () -> Dialogues.inDialogue() || Inventory.contains(gauge));
        		else if(Inventory.get(poisonedFood).useOn(GameObjects.closest("Fountain"))) Sleepz.sleep(696, 666);
        	}
        	return;
    	}
    	if(!Inventory.contains(rubberTube))
    	{
    		if(Locations.ernest_SkellyTube.contains(Players.getLocal()))
    		{
    			if(Combatz.shouldEatFood(6)) Combatz.eatFood();
    			if(!InvEquip.free1InvySpace()) return;
    			API.walkPickupGroundItem("Rubber tube","Take",Locations.ernest_SkellyTube);
    			return;
    		}
			if(!Inventory.contains(key))
			{
				if(!Inventory.contains(id.spade))
            	{
					if(!InvEquip.free1InvySpace()) return;
            		API.walkPickupGroundItem("Spade", "Take", Locations.ernest_spade);
                	return;
            	}
				if(!InvEquip.free1InvySpace()) return;
    			API.walkInteractWithGameObject("Compost heap", "Search", Locations.ernest_compost, () -> Inventory.contains(key));
            	return;
			}
			API.walkInteractWithGameObject("Door", "Open", Locations.ernest_SkellyDoorArea, () -> Locations.ernest_SkellyTube.contains(Players.getLocal()));
			return;
    	}
    	if(Locations.ernest_SkellyTube.contains(Players.getLocal()))
    	{
			API.walkInteractWithGameObject("Door", "Open", Locations.ernest_SkellyDoorArea, () -> !Locations.ernest_SkellyTube.contains(Players.getLocal()));
			return;
    	}
    	if(!Inventory.contains(oilCan)) {
    		if(Locations.ernest_basementMaynor.contains(Players.getLocal())) {
    			if(isAUp() && isBUp() && !isCUp() && !isDUp() && isEUp() && !isFUp()) {
    				if(!InvEquip.free1InvySpace()) return;
    				API.walkPickupGroundItem("Oil can", "Take", Locations.ernest_puzzleLast);
    				return;
    			}
    			if(isAUp() && isBUp() && !isCUp() && !isDUp() && !isFUp()) {
    				if(!isEUp()) {
    					API.walkInteractWithGameObject("Lever E","Pull",Locations.ernest_puzzle5, () -> isEUp());
						Sleepz.sleep(666, 1111);
						return;
    				} 
    				return;
    			}
    			if(isAUp() && isBUp() && !isDUp() && !isEUp() && !isFUp()) {
    				if(isCUp()) {
    					API.walkInteractWithGameObject("Lever C","Pull",Locations.ernest_puzzle2, () -> !isCUp());
						Sleepz.sleep(666, 1111);
						return;
    				} 
    				return;
    			}
    			if(isAUp() && isBUp() && isCUp() && !isDUp()) {
    				if(isFUp()) {
    					API.walkInteractWithGameObject("Lever F","Pull",Locations.ernest_puzzle5, () -> !isFUp());
						Sleepz.sleep(666, 1111);
						return;
    				} 
    				if(isEUp()) {
    					API.walkInteractWithGameObject("Lever E","Pull",Locations.ernest_puzzle5, () -> !isEUp());
 						Sleepz.sleep(666, 1111);
 						return;
    				}
    				return;
    			}
    			if(isCUp() && !isDUp() && isEUp() && isFUp())
    			{
    				//flip up A + B
    				if(!isBUp())
					{
						API.walkInteractWithGameObject("Lever B","Pull",Locations.ernest_puzzle1, () -> !isBUp());
						Sleepz.sleep(666, 1111);
						return;
					}
					if(!isAUp())
					{
						API.walkInteractWithGameObject("Lever A","Pull",Locations.ernest_puzzle1, () -> !isAUp());
						Sleepz.sleep(666, 1111);
						return;
					}
    				return;
    			}
    			if(isCUp() && isDUp() && isEUp() && isFUp())
    			{
    				if(!isAUp() && !isBUp()) {
    					//go flip D down
    					API.walkInteractWithGameObject("Lever D", "Pull", Locations.ernest_puzzle2, () -> !isDUp());
    					Sleepz.sleep(666, 1111);
    					return;
    				}
    				//flip down A + B
    				if(isBUp())
					{
						API.walkInteractWithGameObject("Lever B","Pull",Locations.ernest_puzzle1, () -> !isBUp());
						Sleepz.sleep(666, 1111);
						return;
					}
					if(isAUp())
					{
						API.walkInteractWithGameObject("Lever A","Pull",Locations.ernest_puzzle1, () -> !isAUp());
						Sleepz.sleep(666, 1111);
						return;
					}
    				Walking.walk(Locations.ernest_puzzle1.getCenter());
    				return;
    			}
    		}
    		if(Locations.ernest_westWing.contains(Players.getLocal()))
        	{
    			API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.ernest_westWing, () -> Locations.ernest_basementMaynor.contains(Players.getLocal()));
            	return;
        	}
    		API.walkInteractWithGameObject("Bookcase", "Search", Locations.ernest_westWingAnd, () -> Locations.ernest_westWing.contains(Players.getLocal()));
        	return;
    	}
    	if(Inventory.contains(oilCan))
    	{
    		if(Locations.ernest_basementMaynor.contains(Players.getLocal()))
    		{
    			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.ernest_puzzle1, () -> Locations.ernest_westWing.contains(Players.getLocal()));
    			return;
    		}
    		if(Locations.ernest_westWing.contains(Players.getLocal()))
    		{
    			API.walkInteractWithGameObject("Lever","Pull",Locations.ernest_westWing,() -> !Locations.ernest_westWing.contains(Players.getLocal()));
    			return;
    		}
    		API.walkTalkToNPC("Professor Oddenstein","Talk-to",true, Locations.ernest_3rdfloorMaynor);
    		Sleepz.sleep(696, 666);
    	}
    }
    public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(32);
    }
    
}
