package script.end.obor;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;

import script.utilities.*;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.skills.ranged.TrainRanged;

/**
 * Completes Fight Arena
 * @author Dreambotter420
 * ^_^
 */
public class Obor extends Leaf {
	public static boolean onExit() {
		if(Locations.hillGiantsF2PCave.contains(Players.getLocal()))
		{
			if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
			{
				Logger.log("Not able to teleport to grand exchange! Walking..");
				Walkz.walkToTileInRadius(BankLocation.GRAND_EXCHANGE.getCenter(), 10);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		return API.mode == API.modes.FIGHT_ARENA;
	}
	public static boolean completed()
	{
		if(InvEquip.checkedBank())
		{
			if(!Inventory.contains(id.giantKey) && !Bank.contains(id.giantKey) && !Locations.isInstanced())
			{
				return true;
			}
		}
    	return false;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            Logger.log("[TIMEOUT] -> Killing Obor");
            if(onExit()) API.mode = null;
            return Sleepz.sleepTiming();
        }
        if (completed()) {
            Logger.log("[FINISHED] -> Killing Obor!");
            if(onExit()) API.mode = null;
            return Sleepz.sleepTiming();
        }
        if(!Equipment.contains(TrainRanged.getBestAmuletSlot()) || 
        		!Equipment.contains(TrainRanged.getBestBodySlot()) || 
        		!Equipment.contains(TrainRanged.getBestBootSlot()) || 
        		!Equipment.contains(TrainRanged.getBestLegSlot()) || 
        		(!Equipment.contains(TrainRanged.getBestDart()) && !Inventory.contains(TrainRanged.getBestDart())) || 
        		!Equipment.contains(TrainRanged.getBestHandSlot()) || 
        		!Equipment.contains(TrainRanged.getBestHeadSlot()) || 
        		!InvEquip.equipmentContains(InvEquip.wearableWealth))
        {
        	InvEquip.clearAll();
        	InvEquip.setEquipItem(EquipmentSlot.AMULET, TrainRanged.getBestAmuletSlot());
        	InvEquip.setEquipItem(EquipmentSlot.FEET, TrainRanged.getBestBootSlot());
        	InvEquip.setEquipItem(EquipmentSlot.CHEST, TrainRanged.getBestBodySlot());
        	InvEquip.setEquipItem(EquipmentSlot.LEGS, TrainRanged.getBestLegSlot());
        	InvEquip.setEquipItem(EquipmentSlot.HANDS, TrainRanged.getBestHandSlot());
        	InvEquip.setEquipItem(EquipmentSlot.HAT, TrainRanged.getBestHeadSlot());
        	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
        	
        }
        return Sleepz.sleepTiming();
    }
    /**
     * returns true if only 3 spots left (including food, empty pots)
     * @return
     */
    public static boolean canFightObor()
    {
    	int totalJunkEmptySlots = 0;
    	for(Item i : Inventory.all(i -> i!=null && i.getID() > 0 && i.getName() != null && !i.getName().equals("null")))
    	{
    		final int iD = i.getID();
    		if(id.rangedPots.contains(iD) || id.vial == iD || id.bigBones == iD) 
    		{
    			totalJunkEmptySlots++;
    			continue;
    		}
    		if(Combatz.highFoods.contains(iD))
    		{
    			//if(Skills.getBoostedLevel(Skill.HITPOINTS))
    		}
    	}
    	totalJunkEmptySlots = totalJunkEmptySlots + Inventory.emptySlotCount();
    	return totalJunkEmptySlots <= 4; //we want at least 4 slots open to start another fight
    }
    
    /**
     * returns true if inside obor's cave (instanced)
     * @return
     */
    public static boolean walkToObor()
    {
    	if(Locations.isInstanced()) return true;
    	if(Dialoguez.handleDialogues()) return false;
    	if(Locations.hillGiantsF2PCave.contains(Players.getLocal()))
    	{
    		API.interactWithGameObject("Gate", "Open", Locations.oborGate, Dialogues::inDialogue);
    		Sleep.sleepTick();
    		return false;
    	}
    	if(Locations.hillGiantsShedAbove.contains(Players.getLocal()))
    	{
    		API.interactWithGameObject("Ladder", "Climb-down");
    		Sleep.sleepTick();
    		return false;
    	}
    	if(Locations.hillGiantsShedAbove.getCenter().distance() < 10)
    	{
    		API.interactWithGameObject("Door", "Open", Locations.hillGiantsShedDoor);
    		Sleep.sleepTick();
    		return false;
    	}
    	if(Locations.hillGiantsShedAbove.getCenter().distance() < 125)
    	{
    		Walkz.walkToArea(Locations.hillGiantsShedAbove);
    		return false;
    	}
    	Walkz.goToGE(180000);
    	return false;
    }
    /**
     * returns true if outside obor's cave (not instanced), otherwise climbs rocks, clicks gate, handles dialogues
     * @return
     */
    public static boolean exitLair()
    {
    	if(!Locations.isInstanced()) return true;
    	if(Dialoguez.handleDialogues()) return false;
    	if(isInPitOfLair() && !Players.getLocal().isMoving() && !Players.getLocal().isAnimating())
    	{
    		API.interactWithGameObject("Rocks", "Climb",() -> !isInPitOfLair() && !Players.getLocal().isMoving() && !Players.getLocal().isAnimating());
    		return false;
    	}
    	API.interactWithGameObject("Gate", "Open",() -> Dialogues.inDialogue());
    	return false;
    }
    public static boolean isInPitOfLair()
    {
    	if(!Locations.isInstanced()) return false;
    	GameObject gate = GameObjects.closest("Gate");
    	if(gate != null && gate.exists() && gate.canReach()) return false;
    	return true;
    }
}
