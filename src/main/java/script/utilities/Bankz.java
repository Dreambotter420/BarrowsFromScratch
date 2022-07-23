package script.utilities;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;

import script.quest.varrockmuseum.Timing;

public class Bankz {
	public static boolean openClosest()
	{
		if(Bank.isOpen()) return true;
		
		final double dist = BankLocation.getNearest(Players.localPlayer()).getTile().distance();
		if(dist >= 100)
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
			if(Bank.openClosest()) return true;
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
			if(Bank.openClosest()) return true;
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
