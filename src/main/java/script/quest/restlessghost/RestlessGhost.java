package script.quest.restlessghost;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.NPC;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Restless Ghost
 * @author Dreambotter420
 * ^_^
 */
public class RestlessGhost extends Leaf {
	public static boolean started = false;
	public static boolean completedRestlessGhost = false;
	public static final int ghostspeakAmulet = 552;
	public static final int ghostSkull = 553;
    public void onStart() {
        
        started = true;
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.RESTLESS_GHOST;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> The Restless Ghost");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if (completedRestlessGhost) {
            MethodProvider.log("[COMPLETED] -> The Restless Ghost!");
           	API.mode = null;
           	Main.customPaintText1 = "~~~~~~~~~~";
    		Main.customPaintText2 = "~Quest Complete~";
    		Main.customPaintText3 = "~The Restless Ghost~";
    		Main.customPaintText4 = "~~~~~~~~~~";
            return Timing.sleepLogNormalSleep();
        }
        if(Dialogues.getNPCDialogue() != null && !Dialogues.getNPCDialogue().isEmpty())
    	{
    		MethodProvider.log("NPC Dialogue: " + Dialogues.getNPCDialogue());
    	}
        if(handleDialogues()) return Timing.sleepLogNormalInteraction();
       
        
        switch(getProgressValue())
        {
        case(5):
        {
        	if(Dialogues.getNPCDialogue() != null && 
        			Dialogues.getNPCDialogue().contains("Release! Thank you stranger.."))
        	{
        		Sleep.sleep(696,696);
        		API.randomAFK(50);
        		return Timing.sleepLogNormalInteraction();
        	}
        	if(Widgets.getWidgetChild(153,16) != null &&
        			Widgets.getWidgetChild(153,16).isVisible())
        	{
        		if(Widgets.getWidgetChild(153,16).interact("Close")) Sleep.sleep(696, 666);
        		return Timing.sleepLogNormalInteraction();
        	}
        	completedRestlessGhost = true;
        	return Timing.sleepLogNormalInteraction();
        }
        case(4):
        {
        	walkSearchCoffin();
    		return Timing.sleepLogNormalInteraction();
        }
        case(3):
        {
        	if(haveSkull())
        	{
        		walkSearchCoffin();
        		return Timing.sleepLogNormalInteraction();
        	}
        	getSkull();
    		return Timing.sleepLogNormalInteraction();
        }
        case(2):
        {
        	 if((!Equipment.contains(ghostspeakAmulet) && !InvEquip.equipmentContains(InvEquip.wearablePassages)) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
         			!Inventory.contains(id.lumbridgeTele))
         	{
         		fulfillStart();
         		return Timing.sleepLogNormalInteraction();
         	}
        	if(Equipment.contains(ghostspeakAmulet))
        	{
        		if(Locations.restlessGhostUrhneyHut.contains(Players.localPlayer()) ||
        				Locations.restlessGhostSkeletonCoffin.getCenter().distance() > 125)
        		{
        			Walkz.teleportLumbridge(30000);
            		return Timing.sleepLogNormalSleep();
        		}
        		NPC ghost = NPCs.closest(n -> 
        		n!=null && 
        		n.getName().equals("Restless ghost") && 
        		n.hasAction("Talk-to"));
        		if(ghost == null)
        		{
        			Condition ghostAppear = () -> NPCs.closest(n -> 
            			n!=null && 
            			n.getName().equals("Restless ghost") && 
            			n.hasAction("Talk-to")) != null;
            		API.walkInteractWithGameObject("Coffin","Search", Locations.restlessGhostSkeletonCoffin,ghostAppear);
        			API.walkInteractWithGameObject("Coffin","Open", Locations.restlessGhostSkeletonCoffin,ghostAppear);
            		return Timing.sleepLogNormalSleep();
        		}
        		if(ghost.interact("Talk-to"))
        		{
        			MethodProvider.sleepUntil(Dialogues::inDialogue,
        					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
        		}
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Inventory.contains(ghostspeakAmulet))
        	{
        		InvEquip.equipItem(ghostspeakAmulet);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Bank.contains(ghostspeakAmulet))
        	{
        		InvEquip.withdrawOne(ghostspeakAmulet, 180000);
        		return Timing.sleepLogNormalSleep();
        	}
        	getGhostspeakAmulet();
        	return Timing.sleepLogNormalSleep();
        }
        case(1):
        {
        	 if((!Equipment.contains(ghostspeakAmulet) && !InvEquip.equipmentContains(InvEquip.wearablePassages)) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
         			!Inventory.contains(id.lumbridgeTele))
         	{
         		fulfillStart();
         		return Timing.sleepLogNormalInteraction();
         	}
        	getGhostspeakAmulet();
        	return Timing.sleepLogNormalSleep();
        }
        case(0):
        {
        	 if((!Equipment.contains(ghostspeakAmulet) && !InvEquip.equipmentContains(InvEquip.wearablePassages)) || 
         			!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
         			!Inventory.contains(id.lumbridgeTele))
         	{
         		fulfillStart();
         		return Timing.sleepLogNormalInteraction();
         	}
        	if(Locations.restlessGhostLumbyChurch.getCenter().distance() <= 100)
        	{
        		API.walkTalkToNPC("Father Aereck","Talk-to",Locations.restlessGhostLumbyChurch);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.restlessGhostLumbyChurch.getCenter().distance() > 100)
        	{
        		if(Walkz.teleportLumbridge(180000))
        		{
        			Sleep.sleep(696, 420);
        		}
        		return Timing.sleepLogNormalInteraction();
        	}
        }
        default:break;
        }
        return Timing.sleepLogNormalSleep();
    }
    public static void getGhostspeakAmulet()
    {
    	if(Bank.contains(ghostspeakAmulet))
    	{
    		InvEquip.withdrawOne(ghostspeakAmulet, 180000);
    		return;
    	}
    	if(Locations.restlessGhostUrhneyHut.getCenter().distance() <= 125)
    	{
    		if(InvEquip.free1InvySpace())
    		{
    			API.walkTalkToNPC("Father Urhney","Talk-to",Locations.restlessGhostUrhneyHut);
    		}
    		return;
    	}
    	if(Locations.restlessGhostUrhneyHut.getCenter().distance() > 125)
    	{
    		if(Walkz.teleportLumbridge(180000))
    		{
    			Sleep.sleep(696, 420);
    		}
    	}
    }
    public static void fulfillStart()
    {
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.passage);
    	InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);

    	InvEquip.addInvyItem(ghostspeakAmulet, 1, 1, false, 0);
    	InvEquip.addInvyItem(id.lumbridgeTele, 2, (int) Calculations.nextGaussianRandom(7, 2), false, 10);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (restless ghost)");
			return;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (restless ghost)");
			return;
		}
    }
    public static void walkSearchCoffin()
    {
    	if(Locations.wizardTowerBasement.contains(Players.localPlayer()))
    	{
			Walkz.teleportLumbridge(180000);
			return;
    	}
		if(Locations.restlessGhostSkeletonCoffin.getCenter().distance() < 100)
		{
			Condition release = () -> Dialogues.getNPCDialogue() != null && Dialogues.getNPCDialogue().contains("Release! Thank you stranger..");
			API.walkInteractWithGameObject("Coffin","Open", Locations.restlessGhostSkeletonCoffin,release);
			API.walkInteractWithGameObject("Coffin","Search", Locations.restlessGhostSkeletonCoffin,release);
		}
    }
    public static boolean haveSkull()
    {
    	return PlayerSettings.getBitValue(2130) == 1;
    }
    public static void getSkull()
    {
    	if((!Equipment.contains(ghostspeakAmulet) && !InvEquip.equipmentContains(InvEquip.wearablePassages)) || 
     			!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
     			!Inventory.contains(id.lumbridgeTele))
     	{
     		fulfillStart();
     		return;
     	}
    	if(Locations.wizardTowerBasement.contains(Players.localPlayer()))
    	{
    		API.walkInteractWithGameObject("Altar", "Search", Locations.wizardTowerBasementAltar,
    				() -> haveSkull());
    		return;
    	}
    	if(Locations.wizardTowerGroundFloorLadder.getCenter().distance() > 100)
    	{
    		Walkz.useJewelry(InvEquip.passage, "Wizards\' Tower");
    		return;
    	}
    	API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.wizardTowerGroundFloorLadder,
    			() -> Locations.wizardTowerBasement.contains(Players.localPlayer()));
    }
    public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(107);
    }
    public static boolean handleDialogues()
	{
		if(Dialogues.canContinue())
		{
			if(Dialogues.continueDialogue()) Sleep.sleep(69,696);
			return true;
		}
		if(Dialogues.isProcessing())
		{
			Sleep.sleep(420,696);
			return true;
		}
		 
		if(Dialogues.areOptionsAvailable())
		{
			return Dialogues.chooseOption("Yes.") || 
					Dialogues.chooseOption("I\'m looking for a quest!") || 
					Dialogues.chooseOption("Yep, now tell me what the problem is.") || 
					Dialogues.chooseOption("Father Aereck sent me to talk to you.") || 
					Dialogues.chooseOption("He\'s got a ghost haunting his graveyard.") || 
					Dialogues.chooseOption("I\'ve lost the Amulet of Ghostspeak.");
		}
		return false;
	}
}
