package script.skills.slayer;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.helpers.ItemProcessing;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;

import script.skills.crafting.TrainCrafting;
import script.utilities.Bankz;
import script.utilities.InvEquip;
import script.utilities.Paths;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;

public class Candlez {
	private final static Area lightableArea = new Area(3175, 3483, 3152, 3482, 0);
	
	
	public static boolean getLantern()
	{
		Timer timer = new Timer((int) Calculations.nextGaussianRandom(2000000, 100000));
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69, 69);
			Logger.log("In getLantern loop");
			if(trainCrafting() && trainFiremaking())
			{
				if(Inventory.count(id.tinderbox) > 0 && Inventory.count(id.litCandleLantern) > 0)
				{
					return true;
				}
				if(!InvEquip.checkedBank()) return false;
				if(Inventory.count(id.tinderbox) <= 0)
				{
					if(!InvEquip.withdrawOne(id.tinderbox, 180000)) InvEquip.buyItem(id.tinderbox,1,180000);
					continue;
				}
				final int invunlitCandleLanterns = Inventory.count(id.unlitCandleLantern);
				final int bankunlitCandleLanterns = Bank.count(id.unlitCandleLantern);
				final int invlitCandleLanterns = Inventory.count(id.litCandleLantern);
				final int banklitCandleLanterns = Bank.count(id.litCandleLantern);
				final int invemptyCandleLanterns = Inventory.count(id.emptyCandleLantern);
				final int bankemptyCandleLanterns = Bank.count(id.emptyCandleLantern);
				final int invlitCandles = Inventory.count(id.litCandle);
				final int banklitCandles = Bank.count(id.litCandle);
				final int invunlitCandles = Inventory.count(id.unlitCandle);
				final int bankunlitCandles = Bank.count(id.unlitCandle);
				final int invglassblowingPipes = Inventory.count(id.glassblowingPipe);
				final int bankglassblowingPipes = Bank.count(id.glassblowingPipe);
				final int invmoltenGlass = Inventory.count(id.moltenGlass);
				final int bankmoltenGlass = Bank.count(id.moltenGlass);
				if(Inventory.emptySlotCount() <= 6)
				{
					if(Bankz.openClosest(25))
					{
						Bank.depositAllItems();
					}
					continue;
				}
				//start backwards - finish what we can first ...
				if(ItemProcessing.isOpen())
				{
					if(ItemProcessing.makeAll(id.emptyCandleLantern)) Sleepz.sleep(696, 420);
					continue;
				}
				if(banklitCandleLanterns > 0)
				{
					InvEquip.withdrawOne(id.litCandleLantern, 180000);
					continue;
				}
				if(invunlitCandleLanterns > 0)
				{
					if(Bank.isOpen())
					{
						if(Bankz.close()) Sleepz.sleep(69, 420);
						continue;
					}
					if(Inventory.get(id.tinderbox).useOn(Inventory.get(id.unlitCandleLantern)))
					{
						Sleep.sleepUntil(() -> Inventory.count(id.litCandleLantern) > invlitCandleLanterns, Sleepz.calculate(2222, 2222));
					}
					continue;
				}
				if(bankunlitCandleLanterns > 0)
				{
					if(InvEquip.withdrawOne(id.unlitCandleLantern,180000))
					{
						Sleepz.sleep(420, 696);
					}
					continue;
				}
				if(invemptyCandleLanterns > 0)
				{
					if(invlitCandles > 0)
					{
						if(Bank.isOpen())
						{
							if(Bankz.close()) Sleepz.sleep(69, 420);
							continue;
						}
						if(Inventory.get(id.litCandle).useOn(Inventory.get(id.emptyCandleLantern)))
						{
							Sleep.sleepUntil(() -> Inventory.count(id.litCandleLantern) > invlitCandleLanterns, Sleepz.calculate(2222, 2222));
						}
						continue;
					}
					if(invunlitCandles > 0)
					{
						if(Bank.isOpen())
						{
							if(Bankz.close()) Sleepz.sleep(69, 420);
							continue;
						}
						if(Inventory.get(id.unlitCandle).useOn(Inventory.get(id.emptyCandleLantern)))
						{
							Sleep.sleepUntil(() -> Inventory.count(id.unlitCandleLantern) > invunlitCandleLanterns, Sleepz.calculate(2222, 2222));
						}
						continue;
					}
					if(banklitCandles > 0)
					{
						InvEquip.withdrawOne(id.litCandle, 180000);
						continue;
					}
					if(bankunlitCandles > 0)
					{
						InvEquip.withdrawOne(id.unlitCandle, 180000);
						continue;
					}
					InvEquip.buyItem(id.unlitCandle, 5, 180000);
					continue;
				}
				if(bankemptyCandleLanterns > 0)
				{
					InvEquip.withdrawOne(id.emptyCandleLantern, 180000);
					continue;
				}
				if(invmoltenGlass > 0)
				{
					if(invglassblowingPipes > 0)
					{
						if(Bank.isOpen())
						{
							if(Bankz.close()) Sleepz.sleep(69, 420);
							continue;
						}
						if(Inventory.get(id.glassblowingPipe).useOn(Inventory.get(id.moltenGlass)))
						{
							Sleep.sleepUntil(() -> ItemProcessing.isOpen(), Sleepz.calculate(2222, 2222));
						}
						continue;
					}
					if(bankglassblowingPipes > 0)
					{
						InvEquip.withdrawOne(id.glassblowingPipe, 180000);
						continue;
					}
					InvEquip.buyItem(id.glassblowingPipe, 1, 180000);
					continue;
				}
				if(bankmoltenGlass > 0)
				{
					InvEquip.withdrawOne(id.moltenGlass, 180000);
					continue;
				}
				InvEquip.buyItem(id.moltenGlass, 5, 180000);
			}
		}
		
		return false;
	}
	
	public static boolean trainCrafting()
	{
		if(Skills.getRealLevel(Skill.CRAFTING) >= 4)
		{
			return true;
		}
		TrainCrafting.doCrafting();
		return false;
	}
	
	public static boolean trainFiremaking()
	{
		if(Skills.getRealLevel(Skill.FIREMAKING) >= 4)
		{
			return true;
		}
		if(Inventory.count(id.logs) > 0 && 
				Inventory.count(id.tinderbox) > 0)
		{
			if(lightableArea.contains(Players.getLocal()))
			{
				if(Players.getLocal().isMoving() || Players.getLocal().isAnimating()) {
					Sleepz.sleep();
					return false;
				}
				GameObject fireUnderMe = GameObjects.closest(g -> g!=null && 
						g.getName().equals("Fire") && 
						g.getTile().equals(Players.getLocal().getTile()));
				if(fireUnderMe == null)
				{
					if(Inventory.get(id.logs).useOn(Inventory.get(id.tinderbox)))
					{
						Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), Sleepz.calculate(2222, 2222));
					}
					return false;
				}
				for(Tile t : Paths.lane1Tiles)
				{
					GameObject fireOnTranslatedTile = GameObjects.closest(g -> g!=null && 
							g.getName().equals("Fire") && 
							g.getTile().equals(t));
					if(fireOnTranslatedTile == null)
					{
						if(Walking.walkExact(t)) Sleepz.sleepInteraction();
						return false;
					}
				}
				for(Tile t : Paths.lane2Tiles)
				{
					GameObject fireOnTranslatedTile = GameObjects.closest(g -> g!=null && 
							g.getName().equals("Fire") && 
							g.getTile().equals(t));
					if(fireOnTranslatedTile != null)
					{
						if(Walking.walkExact(t)) Sleepz.sleepInteraction();
						return false;
					}
				}
				return false;
			}
			if(Walkz.goToGE(180000))
			{
				Logger.log("Went to GE!");
				if(Walking.shouldWalk(6) && Walking.walk(Paths.lane1Tiles[0])) Sleepz.sleep(420, 696);
				return false;
			}
		}
		if(Walkz.goToGE(180000))
		{
			InvEquip.clearAll();
			InvEquip.addInvyItem(id.tinderbox, 1, 1, false, 1);
			InvEquip.addInvyItem(id.logs, 1, 27, false, (int) Calculations.nextGaussianRandom(100, 20));
			InvEquip.shuffleFulfillOrder();
			InvEquip.fulfillSetup(true, 180000);
		}
		return false;
	}
}
