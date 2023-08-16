package script.utilities;

import org.dreambot.api.input.Keyboard;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Bankz {
	public static boolean openClosest(int distToTryTeleport)
	{
		if(Bank.isOpen()) return true;
		Logger.log("Entering Bankz.openClosest(int) function");
		if(Magic.isSpellSelected())
		{
			Magic.deselect();
			Sleep.sleepTick();
			return false;
		}
		if(Inventory.isItemSelected())
		{
			Inventory.deselect();
			Sleep.sleepTick();
			return false;
		}
		if(Locations.mageArenaBank.contains(Players.getLocal()))
		{
			GameObject bank = GameObjects.closest(g -> 
					g!=null && 
					g.getName().equals("Bank chest") && 
					g.hasAction("Use"));
			if(bank == null) 
			{
				Logger.log("Bank null in Mage Arena Bank!");
			}
			if(bank.interact("Use"))
			{
				Sleep.sleepUntil(Bank::isOpen,() ->Players.getLocal().isMoving(), Sleepz.calculate(2222, 2222),69);
			}
			return false;
		}
		BankLocation.getSortedValidLocations(Players.getLocal()).stream().forEach((bl) -> Logger.log("Found valid, sorted bank location: " +bl.name() + " - " + bl.getTile().toString()));
		final double dist = BankLocation.getSortedValidLocations(Players.getLocal()).get(0).distance(Players.getLocal().getTile());
		Logger.log("Distance to closest bank: " + dist);
		if(dist >= distToTryTeleport || Players.getLocal().getZ() > 0)
		{
			if(Walkz.useJewelry(InvEquip.wealth,"Grand Exchange") || 
					Walkz.useJewelry(InvEquip.glory,"Edgeville") ||
					Walkz.useJewelry(InvEquip.duel,"Castle Wars") || 
					Walkz.useJewelry(InvEquip.games,"Barbarian Outpost") ||
					Walkz.useJewelry(InvEquip.combat,"Ranging Guild") || 
					Walkz.useJewelry(InvEquip.skills,"Fishing Guild") || 
					Walkz.useJewelry(InvEquip.passage,"Wizards\' Tower") ) 
			{
				Logger.log("Teleported to another place using jewelry to get close to bank!");
				Sleepz.sleep();
				return false;
			}
		}
		return openClosestNoJewelry();
	}
	
	public static boolean openClosestNoJewelry()
	{
		if(Bank.isOpen()) return true;
		
		final double dist = Bank.getClosestBankLocation().getTile().distance();
		if(dist <= 8)
		{
			if(Bank.open()) return true;
			Sleepz.sleep();
			return false;
		}
		
		if(Walking.shouldWalk(6) && Walking.walk(BankLocation.getNearest(Players.getLocal())))
		{
			Logger.log("Walked towards banklocation closest!");
			Sleepz.sleep();
		}
		return false;
	}
	
	public static boolean close() {
		if (Bank.isOpen()) {
			Sleepz.sleepTiming();
			Keyboard.closeInterfaceWithEsc();
		}
		return !Bank.isOpen();
	}

	/**
	 * returns false if Equipment can be closed, true if equipment bank tab closed already
	 * @return
	 */
	public static boolean closeBankEquipment() {
		Logger.log("Close Bank Equipment");
		WidgetChild hideWornItemsButton = Widgets.get(w -> w.hasAction("Show worn items") && w.getParentID() == 12);
		if(hideWornItemsButton != null &&
				hideWornItemsButton.isVisible()) {
			if(hideWornItemsButton.interact("Hide worn items")) {
				return Sleep.sleepUntil(Bank::isOpen, Sleepz.calculate(3333, 2222));
			}
			if(Bank.isOpen()) {
				Bank.count(InvEquip.coins); //random API call to update bank cache ...
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * returns false if Equipment can be opened, true if equipment bank tab open already
	 * @return
	 */
	public static boolean openBankEquipment() {
		Logger.log("Open Bank Equipment");
		WidgetChild hideWornItemsButton = Widgets.get(w -> w.hasAction("Hide worn items") && w.getParentID() == 12);
		if(hideWornItemsButton != null &&
				hideWornItemsButton.isVisible()) {
			return true;
		}
		if(Bank.isOpen()) {
			Bank.count(InvEquip.coins); //random API call to update bank cache ...
			WidgetChild showWornItemsButton = Widgets.get(w -> w.hasAction("Show worn items") && w.getParentID() == 12);
			if (showWornItemsButton == null || !showWornItemsButton.isVisible()) {
				Logger.log("Error with finding Show worn items button");
			}
			Sleepz.sleep();
			if(showWornItemsButton.interact("Show worn items")) {
				return Sleep.sleepUntil(() -> !Bank.isOpen(), Sleepz.calculate(3333, 2222));
			}
			return false;
		}
		else if(GrandExchange.isOpen()) GrandExchangg.close();
		else openClosest(75);
		return false;
	}
}
