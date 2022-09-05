package script.skills.herblore;

import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;

import script.p;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Sleep;
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
			MethodProvider.log("[TIMEOUT] -> Train herblore!");
			if(!Inventory.isEmpty())
			{
				if(Bankz.openClosestNoJewelry())
				{
					if(Bank.depositAllItems())
					{
						MethodProvider.sleepUntil(() -> Inventory.isEmpty(), Sleep.calculate(3333, 3333));
					}
				}
				return Sleep.calculate(420, 699);
			}
			API.mode = null;
			return Sleep.calculate(69, 111);
		}
		if(Dialoguez.handleDialogues()) return Sleep.calculate(420, 696);
		if(!completedDruidicRitual())
		{
			doDruidicRitual();
			return Sleep.calculate(420,696);
		}
		return Sleep.calculate(420,696);
	}
	public static void makeAttPotions()
	{
		
	}
	public static void checkPrices()
	{
		MethodProvider.log("Entering pricecheck function for all available herbs!");
	}
	public static boolean completedDruidicRitual()
	{
		if(getProgressStep() == 4)
		{
			if(Questz.closeQuestCompletion()) return false;
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
		if(Locations.druidicRitual_cauldron.contains(p.l))
		{
			Item i = Inventory.get(id.rawBeef,id.rawChikken,id.rawRat,id.rawBear);
			GameObject cauldron = GameObjects.closest("Cauldron of Thunder");
			if(i != null && cauldron != null)
			{
				if(i.useOn(cauldron))
				{
					MethodProvider.sleepUntil(() -> !Inventory.contains(i), 
							() -> p.l.isMoving(),
							Sleep.calculate(3333, 3333),69);
				}
			}
			return false;
		}
		if(Locations.druidicRitual_skeletonsHallway.contains(p.l))
		{
			if(Combatz.shouldEatFood(9)) Combatz.eatFood();
			API.interactWithGameObject("Prison door","Open", Locations.druidicRitual_cauldron);
			return false;
		}
		if(Locations.allBurthropeTaverly.contains(p.l))
		{
			API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.druidicRitual_abovegroundLadder, () -> Locations.druidicRitual_undergroundLadder.contains(p.l));
			return false;
		}
		if(Locations.druidicRitual_sanfewAbove.contains(p.l))
		{
			API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.druidicRitual_sanfewAbove, () -> Locations.druidicRitual_sanfewbelow.contains(p.l));
			return false;
		}
		return false;
	}
	public static void talkToSanfew()
	{
		if(Locations.druidicRitual_sanfewAbove.contains(p.l))
		{
			API.walkTalkToNPC("Sanfew", "Talk-to", true,Locations.druidicRitual_sanfewAbove);
			return;
		}
		if(Locations.allBurthropeTaverly.contains(p.l))
		{
			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.druidicRitual_sanfewbelow, () -> Locations.druidicRitual_sanfewAbove.contains(p.l));
			return;
		}
		if(Locations.druidicRitual_cauldron.contains(p.l))
		{
			API.walkInteractWithGameObject("Prison door","Open", Locations.druidicRitual_cauldron, () -> Locations.druidicRitual_skeletonsHallway.contains(p.l));
			return;
		}
		if(Locations.druidicRitual_skeletonsHallway.contains(p.l))
		{
			if(Combatz.shouldEatFood(9)) Combatz.eatFood();
			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.druidicRitual_undergroundLadder, () -> Locations.druidicRitual_abovegroundLadder.contains(p.l));
			return;
		}
		Walkz.useJewelry(InvEquip.games, "Burthorpe");
	}
	public static void talkToKaqemeex()
	{
		if(Locations.allBurthropeTaverly.contains(p.l))
		{
			API.walkTalkToNPC("Kaqemeex", "Talk-to", Locations.kaqemeex);
			return;
		}
		if(Locations.druidicRitual_sanfewAbove.contains(p.l))
		{
			API.interactWithGameObject("Staircase", "Climb-down", () -> Locations.druidicRitual_sanfewbelow.contains(p.l));
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
