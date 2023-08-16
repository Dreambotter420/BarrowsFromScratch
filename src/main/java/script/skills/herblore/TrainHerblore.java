package script.skills.herblore;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;

public class TrainHerblore extends Leaf {

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_HERBLORE;
	}
	@Override
	public int onLoop() {
		if(DecisionLeaf.taskTimer.finished())
		{
			Logger.log("[TIMEOUT] -> Train herblore!");
			if(!Inventory.isEmpty())
			{
				if(Bankz.openClosestNoJewelry())
				{
					if(Bank.depositAllItems())
					{
						Sleep.sleepUntil(() -> Inventory.isEmpty(), Sleepz.calculate(3333, 3333));
					}
				}
				return Sleepz.calculate(420, 699);
			}
			API.mode = null;
			return Sleepz.calculate(69, 111);
		}
		if(Dialoguez.handleDialogues()) return Sleepz.calculate(420, 696);
		if(!completedDruidicRitual())
		{
			doDruidicRitual();
			return Sleepz.calculate(420,696);
		}
		return Sleepz.calculate(420,696);
	}
	public static void makeAttPotions()
	{
		
	}
	public static void checkPrices()
	{
		Logger.log("Entering pricecheck function for all available herbs!");
	}
	public static boolean completedDruidicRitual()
	{
		if(getProgressStep() == 4)
		{
			if(Questz.checkCloseQuestCompletion()) return false;
			return true;
		}
		return false;
	}
	public static int getProgressStep()
	{
		return PlayerSettings.getConfig(80);
	}
	public static void doDruidicRitual()
	{
		switch(getProgressStep())
		{
		case(3):
		{
			talkToKaqemeex();
			break;
		}
		case(2):
		{
			if(preparedMeats()) talkToSanfew();
			break;
		}
		case(1):
		{
			talkToSanfew();
			break;
		}
		case(0):
		{
			if(fulfilledStep0()) talkToKaqemeex();
			break;
		}
		default:break;
		}
	}
	public static boolean preparedMeats()
	{
		if(Inventory.containsAll(id.enchantedBear,id.enchantedBeef,id.enchantedChikken,id.enchantedRat)) return true;
		if(Locations.druidicRitual_cauldron.contains(Players.getLocal()))
		{
			Item i = Inventory.get(id.rawBeef,id.rawChikken,id.rawRat,id.rawBear);
			GameObject cauldron = GameObjects.closest("Cauldron of Thunder");
			if(i != null && cauldron != null)
			{
				if(i.useOn(cauldron))
				{
					Sleep.sleepUntil(() -> !Inventory.contains(i), 
							() -> Players.getLocal().isMoving(),
							Sleepz.calculate(3333, 3333),69);
				}
			}
			return false;
		}
		if(Locations.druidicRitual_skeletonsHallway.contains(Players.getLocal()))
		{
			if(Combatz.shouldEatFood(9)) Combatz.eatFood();
			API.interactWithGameObject("Prison door","Open", Locations.druidicRitual_cauldron);
			return false;
		}
		if(Locations.allBurthropeTaverly.contains(Players.getLocal()))
		{
			API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.druidicRitual_abovegroundLadder, () -> Locations.druidicRitual_undergroundLadder.contains(Players.getLocal()));
			return false;
		}
		if(Locations.druidicRitual_sanfewAbove.contains(Players.getLocal()))
		{
			API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.druidicRitual_sanfewAbove, () -> Locations.druidicRitual_sanfewbelow.contains(Players.getLocal()));
			return false;
		}
		return false;
	}
	public static void talkToSanfew()
	{
		if(Locations.druidicRitual_sanfewAbove.contains(Players.getLocal()))
		{
			API.walkTalkToNPC("Sanfew", "Talk-to", true,Locations.druidicRitual_sanfewAbove);
			return;
		}
		if(Locations.allBurthropeTaverly.contains(Players.getLocal()))
		{
			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.druidicRitual_sanfewbelow, () -> Locations.druidicRitual_sanfewAbove.contains(Players.getLocal()));
			return;
		}
		if(Locations.druidicRitual_cauldron.contains(Players.getLocal()))
		{
			API.walkInteractWithGameObject("Prison door","Open", Locations.druidicRitual_cauldron, () -> Locations.druidicRitual_skeletonsHallway.contains(Players.getLocal()));
			return;
		}
		if(Locations.druidicRitual_skeletonsHallway.contains(Players.getLocal()))
		{
			if(Combatz.shouldEatFood(9)) Combatz.eatFood();
			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.druidicRitual_undergroundLadder, () -> Locations.druidicRitual_abovegroundLadder.contains(Players.getLocal()));
			return;
		}
		Walkz.useJewelry(InvEquip.games, "Burthorpe");
	}
	public static void talkToKaqemeex()
	{
		if(Locations.allBurthropeTaverly.contains(Players.getLocal()))
		{
			API.walkTalkToNPC("Kaqemeex", "Talk-to", Locations.kaqemeex);
			return;
		}
		if(Locations.druidicRitual_sanfewAbove.contains(Players.getLocal()))
		{
			API.interactWithGameObject("Staircase", "Climb-down", () -> Locations.druidicRitual_sanfewbelow.contains(Players.getLocal()));
			return;
		}
		Walkz.useJewelry(InvEquip.games, "Burthorpe");
	}
	public static boolean fulfilledStep0()
	{
		if(Inventory.containsAll(id.rawBear,id.rawBeef,id.rawChikken,id.rawRat) && 
				InvEquip.equipmentContains(InvEquip.wearableWealth) && 
				InvEquip.equipmentContains(InvEquip.wearableGames)) return true;
		if(!InvEquip.checkedBank()) return false;
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.rawBear, 1, 1, false, 1);
		InvEquip.addInvyItem(id.rawRat, 1, 1, false, 1);
		InvEquip.addInvyItem(id.rawBeef, 1, 1, false, 1);
		InvEquip.addInvyItem(id.rawChikken, 1, 1, false, 1);
		int staminaID = id.stamina4;
		if(InvEquip.bankContains(id.staminas)) staminaID = InvEquip.getBankItem(id.staminas);
		else if(InvEquip.invyContains(id.staminas)) staminaID = InvEquip.getInvyItem(id.staminas);
		InvEquip.addInvyItem(staminaID, 1, 1, false, 1);
		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.games);
		InvEquip.shuffleFulfillOrder();
		InvEquip.fulfillSetup(true, 240000);
		return Inventory.containsAll(id.rawBear,id.rawBeef,id.rawChikken,id.rawRat) && 
				InvEquip.equipmentContains(InvEquip.wearableWealth) && 
				InvEquip.equipmentContains(InvEquip.wearableGames);
	}
}
