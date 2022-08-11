package script.utilities;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;

import script.quest.varrockmuseum.Timing;

public class Bankz {
	public static boolean openClosest(int distToTryTeleport)
	{
		if(Bank.isOpen()) return true;
		if(Locations.mageArenaBank.contains(Players.localPlayer()))
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
				MethodProvider.sleepUntil(Bank::isOpen,() ->Players.localPlayer().isMoving(), Sleep.calculate(2222, 2222),69);
			}
			return false;
		}
		final double dist = BankLocation.getNearest(Players.localPlayer()).getTile().distance();
		if(dist >= distToTryTeleport)
		{
			MethodProvider.log("Distance to closest bank: " + dist);
			if(Walkz.useJewelry(InvEquip.wealth,"Grand Exchange") || 
					Walkz.useJewelry(InvEquip.glory,"Edgeville") || 
					Walkz.useJewelry(InvEquip.combat,"Ranging Guild") || 
					Walkz.useJewelry(InvEquip.skills,"Fishing Guild") || 
					Walkz.useJewelry(InvEquip.duel,"Castle Wars") || 
					Walkz.useJewelry(InvEquip.passage,"Wizards\' Tower") || 
					Walkz.useJewelry(InvEquip.games,"Barbarian Outpost")) 
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
		
		if(Walking.shouldWalk(6) && Walking.walk(BankLocation.getNearest(Players.localPlayer())))
		{
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
		}
		return false;
	}
	
	public static boolean openClosestNoJewelry()
	{
		if(Bank.isOpen()) return true;
		
		final double dist = BankLocation.getNearest(Players.localPlayer()).getTile().distance();
		if(dist <= 8)
		{
			if(Bank.open(Bank.getClosestBankLocation())) return true;
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
			return false;
		}
		
		if(Walking.shouldWalk(6) && Walking.walk(BankLocation.getNearest(Players.localPlayer())))
		{
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
		}
		return false;
	}
}
