package script.utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;

import script.p;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;
import script.quest.varrockmuseum.Timing;

public class Bankz {
	public static boolean openClosest(int distToTryTeleport)
	{
		if(Bank.isOpen()) return true;
		MethodProvider.log("Entering Bankz.openClosest(int) function");
		if(Locations.mageArenaBank.contains(p.l))
		{
			GameObject bank = GameObjects.closest(g -> 
					g!=null && 
					g.getName().equals("Bank chest") && 
					g.hasAction("Use"));
			if(bank == null) 
			{
				MethodProvider.log("Bank null in Mage Arena Bank!");
			}
			if(bank.interact("Use"))
			{
				MethodProvider.sleepUntil(Bank::isOpen,() ->p.l.isMoving(), Sleep.calculate(2222, 2222),69);
			}
			return false;
		}
		final double dist = Bank.getClosestBankLocation().getTile().distance();
		MethodProvider.log("Distance to closest bank: " + dist);
		if(dist >= distToTryTeleport || p.l.getZ() > 0)
		{
			if(Walkz.useJewelry(InvEquip.wealth,"Grand Exchange") || 
					Walkz.useJewelry(InvEquip.glory,"Edgeville") ||
					Walkz.useJewelry(InvEquip.duel,"Castle Wars") || 
					Walkz.useJewelry(InvEquip.games,"Barbarian Outpost") ||
					Walkz.useJewelry(InvEquip.combat,"Ranging Guild") || 
					Walkz.useJewelry(InvEquip.skills,"Fishing Guild") || 
					Walkz.useJewelry(InvEquip.passage,"Wizards\' Tower") ) 
			{
				MethodProvider.log("Teleported to another place using jewelry to get close to bank!");
				MethodProvider.sleep(Timing.sleepLogNormalSleep());
				return false;
			}
		}
		
		if(dist <= 8)
		{
			if(Bank.open(Bank.getClosestBankLocation())) return true;
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
			return false;
		}
		
		if(Walking.shouldWalk(6) && Walking.walk(BankLocation.getNearest(p.l)))
		{
			MethodProvider.log("Walked towards banklocation closest!");
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
		}
		return false;
	}
	
	public static boolean openClosestNoJewelry()
	{
		if(Bank.isOpen()) return true;
		
		final double dist = Bank.getClosestBankLocation().getTile().distance();
		if(dist <= 8)
		{
			if(Bank.open(Bank.getClosestBankLocation())) return true;
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
			return false;
		}
		
		if(Walking.shouldWalk(6) && Walking.walk(BankLocation.getNearest(p.l)))
		{
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
		}
		return false;
	}
	
	public static boolean close()
	{
		if(UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE)) return Keyboard.closeInterfaceWithESC();
		return Widgets.getWidgetChild(12, 2 , 11) != null && 
				Widgets.getWidgetChild(12, 2 , 11).interact("Close");
	}
}
