package script.skills.herblore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.dreambot.api.Client;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.bank.BankQuantitySelection;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.GrandExchangeItem;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.grandexchange.Status;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.widget.helpers.ItemProcessing;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.API.modes;
import script.utilities.Combatz;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;




public class ProfitableHerblore extends Leaf
{
	
	public static int undercuttingBuyGrimy = 5;
	public static int undercuttingSellUnf = 5;
	public static int maxHerbBuyQty = 700;
	public static boolean useGuam = true;
	public static boolean useHarralander = true;
	public static boolean useRanarr = true;
	public static boolean useToadflax = true;
	public static boolean useIrit = true;
	public static boolean useAvantoe = true;
	public static boolean useKwuarm = true;
	public static boolean useSnapdragon = true;
	public static boolean useCadantine = true;
	public static boolean useLantadyme = true;
	public static boolean useDwarf = true;
	public static boolean useTorstol = true;
	
	public static void onStart()
	{
		if(useGuam) acceptableHerbs.add(Herb.GUAM);
		if(useHarralander) acceptableHerbs.add(Herb.HARRALANDER);
		if(useRanarr) acceptableHerbs.add(Herb.RANARR);
		if(useToadflax) acceptableHerbs.add(Herb.TOADFLAX);
		if(useIrit) acceptableHerbs.add(Herb.IRIT);
		if(useAvantoe) acceptableHerbs.add(Herb.AVANTOE);
		if(useKwuarm) acceptableHerbs.add(Herb.KWUARM);
		if(useSnapdragon) acceptableHerbs.add(Herb.SNAPDRAGON);
		if(useCadantine) acceptableHerbs.add(Herb.CADANTINE);
		if(useLantadyme) acceptableHerbs.add(Herb.LANTADYME);
		if(useDwarf) acceptableHerbs.add(Herb.DWARF_WEED);
		if(useTorstol) acceptableHerbs.add(Herb.TORSTOL);
		
		maxHerbBuyQty = (int)Calculations.nextGaussianRandom(750, 100);
		
		int tmp = (int)Calculations.nextGaussianRandom(8, 4);
		if(tmp < 4) tmp = 4;
		if(tmp > 12) tmp = 12;
		undercuttingSellUnf = tmp;

		tmp = (int)Calculations.nextGaussianRandom(8, 4);
		if(tmp < 4) tmp = 4;
		if(tmp > 12) tmp = 12;
		undercuttingBuyGrimy = tmp;
		
		Logger.log("~~ Starting to train herblore: ~~");
		Logger.log("~~ Minimum profit margin: " + minProfitMargin+" ~~");
		Logger.log("~~ Maximum quantity of grimy herbs to buy at once: " + maxHerbBuyQty+" ~~");
		Logger.log("~~ Undercut sell unf potions by: " + undercuttingSellUnf+"gp ~~");
		Logger.log("~~ Undercut buy grimy herbs by: " + undercuttingBuyGrimy+"gp ~~");
		initialized = true;
	}
	
	public static boolean initialized = false;
	public static List<Herb> acceptableHerbs = new ArrayList<Herb>();
	public static boolean xpMode = false;
	public static int minProfitMargin = 50;
	public static int profit = 0;
	//selectedHerb
	public static HerbPrice selectedHerbPrice = null;
	public static Player l = null;
	public static boolean invyContainsAnythingNoted()
	{
		for(Item i: Inventory.all())
		{
			if(i == null || i.getID() <= 0 || i.getName() == null || i.getName().equalsIgnoreCase("null")) continue;
			if(i.isNoted()) return true;
		}
		return false;
	}
	public static boolean loggedNLoaded()
	{
		if(Client.getGameState() == GameState.LOADING || 
        		Client.getGameState() == GameState.GAME_LOADING)
		{
			Sleep.sleepUntil(() -> (Client.getGameState() != GameState.LOADING && 
        		Client.getGameState() != GameState.GAME_LOADING), 10000);
			Sleepz.sleep(1111, 1111);
			return false;
		}
		else if(Client.getGameState() != GameState.LOGGED_IN)
		{
			Sleep.sleepUntil(() -> Client.getGameState() == GameState.LOGGED_IN, 7000);
			Sleepz.sleep(1111, 1111);
			return false;
		}
		return true;
	}
	public static void priceCheck()
	{
		if(selectedHerbPrice == null)
		{
			priceCheckAvailableHerbs();
			return;
		}
		priceCheckSelectedHerb();
	}
	public static boolean needSelectedHerbPriceCheck = false;
	@Override
	public int onLoop() 
	{
		if(!initialized) onStart();
		if(!loggedNLoaded()) return Sleepz.calculate(1111, 1111);
		l = Players.getLocal();
		if(!completedDruidicRitual())
		{
			doDruidicRitual();
			return Sleepz.calculate(420,696);
		}
		if(GrandExchange.isOpen() && isGEActuallyReadyToCollect())
		{
			Logger.log("Have availability to collect GE!");
			collect();
			return Sleepz.calculate(111, 420);
		}
		
		//check if we have chosen an herb to process already
		if(selectedHerbPrice == null)
		{
			//choose random existing one, or sell all herbs and then price check all available 
			if(checkForAvailableExistingHerbs()) return Sleepz.calculate(420,696);
			priceCheck();
			return Sleepz.calculate(420, 696);
		}
		final int grimy = selectedHerbPrice.herb.grimy;
		final int clean = selectedHerbPrice.herb.clean;
		final int unf = selectedHerbPrice.herb.unf;
		final int notedGrimy = Inventory.count(new Item(grimy,1).getNotedItemID());
		final int notedClean = Inventory.count(new Item(clean,1).getNotedItemID());
		final int invyGrimy = Inventory.count(grimy);
		final int invyClean = Inventory.count(clean);
		final int bankCleans = Bank.count(clean);
		final int bankGrimys = Bank.count(grimy);
		final int bankVials = Bank.count(vial);
		final int totalHerbCount = invyGrimy + notedGrimy + invyClean + notedClean + bankCleans + bankGrimys;
		if(bankVials <= 0)
		{
			buyMoreVials();
			return Sleepz.calculate(420, 696);
		}
		//check if nothing left to process of selectedHerb
		if(totalHerbCount <= 0)
		{
			if(checkForAvailableExistingHerbs()) return Sleepz.calculate(420,696);
			if(needSelectedHerbPriceCheck)
			{
				priceCheck();
				return Sleepz.calculate(420, 696);
			}
			buyABunchOfHerbs();
			return Sleepz.calculate(420, 696);
		}
		
		
		
		if(!Bank.isOpen() && invyContainsAnythingNoted())
		{
			Filter<GameObject> bankFilter = g-> g!=null && 
					g.getName().equals("Grand Exchange booth") && 
					g.hasAction("Bank");
			GameObject bank = GameObjects.closest(bankFilter);
			if(bank != null && bank.interact("Bank")) Sleep.sleepUntil(() -> Bank.isOpen(),Sleepz.calculate(2222, 2222));
			return Sleepz.calculate(111, 420);
		}
		cleansPerHour = (int) ((double) cleans / ((double) DecisionLeaf.taskTimer.elapsed() / 3600000));
		
		if(Bank.isOpen())
		{
			if(clean == guam && Inventory.count(unf) > 0 && Bank.count(eyeOfNewt) > 0)
			{
				Main.paint_task = "Withdrawing 14 eyes of newt";
				if(Bank.withdraw(eyeOfNewt,14)) Sleepz.sleep(69, 111);
				Bank.close();
				Sleep.sleepUntil(() -> Inventory.contains(eyeOfNewt), Sleepz.calculate(3333, 3333));
				return Sleepz.calculate(69, 696);
			}
			Main.paint_task = "Depositing all items";
			Logger.log("Depositing all items");
			if(Bank.depositAllItems()) 
			{
				Sleepz.sleep(69, 111);
			}
			if(bankGrimys > 0) 
			{
				if(bankVials > 0)
				{
					Main.paint_task = "Withdrawing grimy/vial 14/14";
					if(Bank.getDefaultQuantity() != BankQuantitySelection.X)
					{
						if(Bank.setDefaultQuantity(BankQuantitySelection.X)) Sleep.sleepTick();
						return Sleepz.calculate(69, 696);
					}
					if(Bank.withdraw(grimy,14)) Sleepz.sleep(69, 111);
					if(Bank.withdraw(vial,14)) Sleepz.sleep(69, 111);
					Bank.close();
					Sleep.sleepUntil(() -> Inventory.contains(grimy) && Inventory.contains(vial), Sleepz.calculate(3333, 3333));
					return Sleepz.calculate(69, 696);
				}
				if(Bank.withdrawAll(grimy)) Sleepz.sleep(69, 111);
				Sleep.sleepUntil(() -> Inventory.contains(grimy), Sleepz.calculate(3333, 3333));
				Bank.close();
				return Sleepz.calculate(69, 696);
			}
			if(bankCleans > 0)
			{
				if(bankVials > 0)
				{
					if(Bank.getDefaultQuantity() != BankQuantitySelection.X)
					{
						if(Bank.setDefaultQuantity(BankQuantitySelection.X)) Sleep.sleepTick();
						return Sleepz.calculate(69, 696);
					}
					if(Bank.withdraw(clean,14)) Sleepz.sleep(69, 111);
					if(Bank.withdraw(vial,14)) Sleepz.sleep(69, 111);
					Sleep.sleepUntil(() -> Inventory.contains(clean) && Inventory.contains(vial), Sleepz.calculate(3333, 3333));
					Bank.close();
					return Sleepz.calculate(69, 696);
				}
			}
			Sleep.sleepUntil(() -> Inventory.isEmpty(), Sleepz.calculate(3333, 3333));
			Sleepz.sleep(696, 1111);
			if(Inventory.isEmpty())
			{
				final int vials2 = Bank.count(vial);
				final int cleans2 = Bank.count(clean);
				final int grimys2 = Bank.count(grimy);
				if(vials2 != bankVials || 
						cleans2 != bankCleans || 
						grimys2 != bankGrimys) return Sleepz.calculate(111,111);
				return -1;
			}
			return Sleepz.calculate(111, 696);
		}
		if(Inventory.contains(grimy))
		{
			Main.paint_task = "Cleaning grimys";
			Filter<Item> filter = i -> i!=null && i.getID() == grimy;
			for(Item i : Inventory.all(filter))
			{
				if(i == null) continue;
				if(Inventory.slotInteract(i.getSlot(), "Clean")) 
				{
					cleans++;
					Sleep.sleep((int) Calculations.nextGaussianRandom(50, 33));
				}
				else if(i.interact("Clean")) Sleepz.sleep(42,69);
			}
			if(Inventory.contains(vial))
			{
				Sleep.sleepUntil(() -> Inventory.contains(clean), Sleepz.calculate(2222, 2222));
			}
		}
		
		if(ItemProcessing.isOpen())
		{
			Condition c;
			if(Inventory.contains(vial) && Inventory.contains(clean)) c = () -> !l.exists() || !Inventory.contains(clean) || Dialogues.canContinue();
			else c = () -> !l.exists() || !Inventory.contains(eyeOfNewt) || Dialogues.canContinue();
			Keyboard.typeSpecialKey(32);
			Sleep.sleepUntil(c,() -> l.isAnimating(),Sleepz.calculate(2222, 2222),69);
			return Sleepz.calculate(111, 1111);
		}
		Condition itemProcessing = () -> ItemProcessing.isOpen();
		int sleepTimeout = Sleepz.calculate(3333, 3333);
		if(Inventory.contains(vial) && Inventory.contains(clean))
		{
			Main.paint_task = "Using vial -> clean";
			if(Inventory.get(vial).useOn(clean)) 
			{
				Keyboard.holdSpace(itemProcessing, sleepTimeout);
				Sleep.sleepUntil(itemProcessing, sleepTimeout);
			}
			return Sleepz.calculate(111, 1111);
		}
		if(Inventory.contains(eyeOfNewt) && Inventory.contains(unf))
		{
			Main.paint_task = "Using unf -> eye of newt";
			if(Inventory.get(unf).useOn(eyeOfNewt))
			{
				Keyboard.holdSpace(itemProcessing, sleepTimeout);
				Sleep.sleepUntil(itemProcessing, sleepTimeout);
			}
			return Sleepz.calculate(111, 1111);
		}
		clickBank();
		return Sleepz.calculate(111, 1111);
	}
	public static int profitPerHour = 0;
	public static Timer lastUnfSellTimer;
 	/**
 	 * Returns true if no unf in invy or bank, otherwise sets 3 min timer to try to sell.
	 * Empties inventory, withdraws all unf pots (all of them).
	 * If the price of unf pot is pre-recorded, we sell it for unfLow price.
	 * If the price is not recorded of existing unf, 
	 * do another pricecheck regardless of lvl so we can know how much to sell it,
	 * then sell it for that much.
	 */
	public static void sellAllUnf()
	{
		
		Main.paint_task = "Selling all unf potions";
		Logger.log("[sellAllUnf] Start");
		
		needSelectedHerbPriceCheck = true;
		if(!checkedBank()) return;
		Timer timer = new Timer((int) Calculations.nextGaussianRandom(80000,20000));
		int randUnfID = -1;
		int randUnfOfferCount = -1;
		HerbPrice tempHerbPrice = null;
		boolean putOffer = false;
		while(!timer.finished() && !ScriptManager.getScriptManager().isPaused() && ScriptManager.getScriptManager().isRunning())
		{
			Sleepz.sleep(420, 696);
			if(putOffer)
			{
				if(lastUnfSellTimer == null)
				{
					lastUnfSellTimer = new Timer((int) Calculations.nextGaussianRandom(300000,111111));
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				if(!isGEEmpty()) {
					if(!havePendingSellOfferOfAnyUnf()) 
					{
						tempHerbPrice = null;
						putOffer = false;
					}
					if(!completedGEOfferWithQty(randUnfID,randUnfOfferCount,false))
					{
						Sleepz.sleep(111, 1111);
					}
					continue;
				}
				tempHerbPrice = null;
				putOffer = false;
			}
			List<Integer> okUnf = new ArrayList<Integer>();
			List<Integer> unfFound = new ArrayList<Integer>();
			for(Herb herb : Herb.values())
			{
				if(Bank.contains(herb.unf)) unfFound.add(herb.unf);
				okUnf.add(herb.unf);
				okUnf.add(new Item(herb.unf,1).getNotedItemID());
			}
			if(unfFound.isEmpty())
			{
				Logger.log("Bank empty of unf!");
				if(Bank.isOpen())
				{
					if(Bank.contains(coins)) 
					{
						Bank.withdrawAll(coins);
						continue;
					}
					if(Bank.close()) Sleep.sleepUntil(() -> !Bank.isOpen(), Sleepz.calculate(2222, 2222));
					continue;
				}
				for(Herb herb : Herb.values())
				{
					if(Inventory.contains(herb.unf) || Inventory.contains(new Item(herb.unf,1).getNotedItemID())) unfFound.add(herb.unf);
				}
				//no unf in bank or invy
				if(unfFound.isEmpty())
				{
					if(isGEActuallyReadyToCollect()) 
					{
						collect();
						continue;
					}
					Logger.log("[sellAllUnf] End");
					if(GrandExchange.isOpen() && isGEActuallyReadyToCollect())
					{
						collect();
						continue;
					}
					return;
				}
				//check unf for existing prices, otherwise sell for 1 gp to check price
				if(tempHerbPrice == null)
				{
					Collections.shuffle(unfFound);
					for(int i : unfFound)
					{
						tempHerbPrice = priceCheck1Unf(getHerbFromID(i));
						break;
					}
				}
				
				if(!GrandExchange.isOpen())
				{
					GrandExchange.open();
					continue;
				}
				if(GrandExchange.getFirstOpenSlot() == -1)
				{
					GrandExchange.cancelAll();
					Sleep.sleepUntil(() -> GrandExchange.isReadyToCollect(),Sleepz.calculate(2222, 2222));
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				randUnfID = tempHerbPrice.herb.unf;
				int sellPrice = tempHerbPrice.unfLow - undercuttingSellUnf;
				if(sellPrice <= 1) 
				{
					Logger.log("Not selling unf with unfLow price: "+ sellPrice);
				}
				randUnfOfferCount = Inventory.count(randUnfID) + Inventory.count(new Item(randUnfID,1).getNotedItemID());
				if(GrandExchange.sellItem(new Item(randUnfID,1).getName(), randUnfOfferCount, sellPrice))
				{
					Logger.log("Put sell offer for item: " + new Item(randUnfID,1).getName() + " in qty: " + randUnfOfferCount + " at pricePer: " +sellPrice);
					putOffer = true;
					Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
				}
				continue;
			}
			Filter<Item> unfCoinsFilter = i -> i!=null && (okUnf.contains(i.getID()) || i.getID() == coins);
			//have some unf pots to withdraw
			if(Bank.contains(unfCoinsFilter))
			{
				if(!Bank.isOpen())
				{
					Bank.open();
					continue;
				}
				if(!Inventory.isEmpty() && !Inventory.onlyContains(unfCoinsFilter))
				{
					Bank.depositAllExcept(unfCoinsFilter);
					continue;
				}
				if(Bank.getWithdrawMode() == BankMode.NOTE)
				{
					Bank.withdrawAll(unfCoinsFilter);
					continue;
				}
				Bank.setWithdrawMode(BankMode.NOTE);
			}
		}
		Logger.log("[sellAllUnf] Timeout!");
		return;
	}
	/**
	 * Sells 1 unf pot that should already be in invy by time of this method call.
	 */
	public static HerbPrice priceCheck1Unf(Herb herb)
	{
		Main.paint_task = "Checking sell price of 1 "+new Item(herb.unf,1).getName();
		Logger.log("[priceCheck1UnfHerb] Starting check price: " + new Item(herb.unf,1).getName());
		
		//check bank, get coins, deposit everything else
		boolean putOffer = false;
		boolean soldUnf = false;
		Timer timeout = new Timer(180000);
		while(!timeout.finished() && ScriptManager.getScriptManager().isRunning() && 
				!ScriptManager.getScriptManager().isPaused())
		{
			//need to buy grimy, then sell grimy, then buy unf, sell unf,
			//then check history and search from top down for item id and qty = 1 of sold
			Sleepz.sleep(69, 420);
			if(soldUnf)
			{
				if(isGEHistoryOpen())
				{
					final int soldUnfFor = getGEHistoryMostRecentTradePrice(herb.unf, false);
					if(soldUnfFor == -1) 
					{
						Logger.log("Failed to observe existing sold amount for unf potion! Trying again...");
						soldUnf = false;
						continue;
					}
					Logger.log("Found unf " + herb.toString() + " with sell price: " + soldUnfFor);
					return new HerbPrice(herb,0,0,soldUnfFor,0);
				}
				if(!GrandExchange.isOpen())
				{
					NPC GETeller = NPCs.closest("Grand Exchange Clerk");
					if(GETeller!=null)
					{
						if(GETeller.interact("History"))
						{
							Sleep.sleepUntil(() -> !l.exists() || isGEHistoryOpen(),
									()->l.isMoving(),Sleepz.calculate(2222,2222),69);
						}
					}
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				WidgetChild historyButton = Widgets.get(465, 3);
				if(historyButton != null && historyButton.isVisible())
				{
					if(historyButton.interact("History"))
					{
						Sleep.sleepUntil(() -> isGEHistoryOpen(), Sleepz.calculate(2222, 2222));
					}
					continue;
				}
				continue;
			}
			if(!soldUnf)
			{
				if(!GrandExchange.isOpen())
				{
					if(isGEHistoryOpen())
					{
						WidgetChild exchangeButton = Widgets.getChildWidget(383, 2);
						if(exchangeButton != null && exchangeButton.isVisible())
						{
							if(exchangeButton.interact("Exchange"))
							{
								Sleep.sleepUntil(GrandExchange::isOpen, Sleepz.calculate(2222, 2222));
							}
						}
						continue;
					}
					GrandExchange.open();
					continue;
				}
				if(putOffer)
				{
					if(completedGEOfferWithQty(herb.unf,1,false))
					{
						soldUnf = true;
						putOffer = false;
					}
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				if(GrandExchange.sellItem(new Item(herb.unf,1).getName(), 1, 1))
				{
					putOffer = true;
					Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
				}
				continue;
			}
		}
		Logger.log("Timeout after 3 minutes of unf sold pricecheck!");
		return null;
	}
	/**
	 * Check price of our selected herb, sets it to selectedHerbPrice.
	 */
	public static void priceCheckSelectedHerb()
	{
		Main.paint_task = "Checking prices of selected herb: "+selectedHerbPrice.herb.toString();
		
		//check bank, get coins, deposit everything else
		if(!checkedBank()) return;
		Logger.log("[priceCheckSelectedHerb] Starting check price of selected herb: " + selectedHerbPrice.herb.toString());
		
		int grimyOfferPrice = (int) Calculations.nextGaussianRandom((LivePrices.getHigh(selectedHerbPrice.herb.grimy) * 5), 50);
		if(grimyOfferPrice >= 20000) grimyOfferPrice = (int) Calculations.nextGaussianRandom(20000, 1000);
		int unfOfferPrice = (int) Calculations.nextGaussianRandom((LivePrices.getHigh(selectedHerbPrice.herb.unf) * 5), 50);
		if(unfOfferPrice >= 20000) unfOfferPrice = (int) Calculations.nextGaussianRandom(20000, 1000);
		
		boolean boughtGrimy = false;
		boolean soldGrimy = false;
		boolean boughtUnf = false;
		boolean soldUnf = false;
		boolean putOffer = false;
		Timer timeout = new Timer(180000);
		while(!timeout.finished() && ScriptManager.getScriptManager().isRunning() && 
				!ScriptManager.getScriptManager().isPaused())
		{
			//need to buy grimy, then sell grimy, then buy unf, sell unf,
			//then check history and search from top down for item id and qty = 1 of sold
			Sleepz.sleep(69, 420);
			
			if(boughtGrimy && soldGrimy && boughtUnf && soldUnf)
			{
				if(isGEHistoryOpen())
				{
					final int boughtGrimyFor = getGEHistoryMostRecentTradePrice(selectedHerbPrice.herb.grimy, true);
					final int soldGrimyFor = getGEHistoryMostRecentTradePrice(selectedHerbPrice.herb.grimy, false);
					final int boughtUnfFor = getGEHistoryMostRecentTradePrice(selectedHerbPrice.herb.unf, true);
					final int soldUnfFor = getGEHistoryMostRecentTradePrice(selectedHerbPrice.herb.unf, false);
					if(boughtGrimyFor == -1 || soldGrimyFor == -1 || boughtUnfFor == -1 || soldUnfFor == -1) 
					{
						Logger.log("Failed to observe existing bought/sold qtys for grimy/unf herb! Trying again...");
						boughtGrimy = soldGrimy = boughtUnf = soldUnf = false;
						continue;
					}
					HerbPrice foundHerbPrice = new HerbPrice(selectedHerbPrice.herb,soldGrimyFor,boughtGrimyFor,soldUnfFor,boughtUnfFor);
					foundHerbPrice.printHerbPrices();
					if(foundHerbPrice.profitMargin < minProfitMargin)
					{
						Logger.log("Profit margin of less than "+minProfitMargin+"gp! Price-checking all herbs again...");
						selectedHerbPrice = null;
						return;
					}
					selectedHerbPrice = foundHerbPrice;
					Logger.log("Setting herb to process: " + selectedHerbPrice.herb.toString());
					needSelectedHerbPriceCheck = false;
					return;
				}
				if(!GrandExchange.isOpen())
				{
					NPC GETeller = NPCs.closest("Grand Exchange Clerk");
					if(GETeller!=null)
					{
						if(GETeller.interact("History"))
						{
							Sleep.sleepUntil(() -> !l.exists() || isGEHistoryOpen(),
									()->l.isMoving(),Sleepz.calculate(2222,2222),69);
						}
					}
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				WidgetChild historyButton = Widgets.get(465, 3);
				if(historyButton != null && historyButton.isVisible())
				{
					if(historyButton.interact("History"))
					{
						Sleep.sleepUntil(() -> isGEHistoryOpen(), Sleepz.calculate(2222, 2222));
					}
					continue;
				}
				continue;
			}
			//need to buy/sell all items starting with grimy then unf
			if(!boughtGrimy)
			{
				if(Inventory.count(coins) <= 20000)
				{
					if(Bank.count(coins) >= 1)
					{
						if(!Bank.isOpen()) 
						{
							Bank.open();
							continue;
						}
						if(Bank.withdrawAll(coins))
						{
							Sleep.sleepUntil(() -> !Bank.contains(coins), Sleepz.calculate(2222, 2222));
						}
						continue;
					}
					if(!GrandExchange.isOpen())
					{
						if(isGEHistoryOpen())
						{
							WidgetChild exchangeButton = Widgets.getChildWidget(383, 2);
							if(exchangeButton != null && exchangeButton.isVisible())
							{
								if(exchangeButton.interact("Exchange"))
								{
									Sleep.sleepUntil(GrandExchange::isOpen, Sleepz.calculate(2222, 2222));
								}
							}
							continue;
						}
						GrandExchange.open();
						continue;
					}
					if(isGEActuallyReadyToCollect()) {
						collect();;
						continue;
					}
					if(!isGEEmpty()) continue;
					Logger.log("No more coins! Need more than 20k...");
					continue;
				}
				if(!GrandExchange.isOpen())
				{
					if(isGEHistoryOpen())
					{
						WidgetChild exchangeButton = Widgets.getChildWidget(383, 2);
						if(exchangeButton != null && exchangeButton.isVisible())
						{
							if(exchangeButton.interact("Exchange"))
							{
								Sleep.sleepUntil(GrandExchange::isOpen, Sleepz.calculate(2222, 2222));
							}
						}
						continue;
					}
					GrandExchange.open();
					continue;
				}
				if(putOffer)
				{
					if(completedGEOfferWithQty(selectedHerbPrice.herb.grimy,1,true))
					{
						boughtGrimy = true;
						putOffer = false;
					}
					continue;
				}
				if(GrandExchange.getFirstOpenSlot() == -1)
				{
					GrandExchange.cancelAll();
					Sleep.sleepUntil(() -> GrandExchange.isReadyToCollect(),Sleepz.calculate(2222, 2222));
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				if(GrandExchange.buyItem(selectedHerbPrice.herb.grimy, 1, grimyOfferPrice))
				{
					putOffer = true;
					Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
				}
				continue;
			}
			if(!soldGrimy)
			{
				if(!GrandExchange.isOpen())
				{
					GrandExchange.open();
					continue;
				}
				if(putOffer)
				{
					if(completedGEOfferWithQty(selectedHerbPrice.herb.grimy,1,false))
					{
						soldGrimy = true;
						putOffer = false;
					}
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				if(GrandExchange.sellItem(new Item(selectedHerbPrice.herb.grimy,1).getName(), 1, 1))
				{
					putOffer = true;
					Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
				}
				continue;
			}
			if(!boughtUnf)
			{
				if(!GrandExchange.isOpen())
				{
					GrandExchange.open();
					continue;
				}
				if(putOffer)
				{
					if(completedGEOfferWithQty(selectedHerbPrice.herb.unf,1,true))
					{
						boughtUnf = true;
						putOffer = false;
					}
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				if(GrandExchange.buyItem(selectedHerbPrice.herb.unf, 1, unfOfferPrice))
				{
					putOffer = true;
					Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
				}
				continue;
			}
			if(!soldUnf)
			{
				if(!GrandExchange.isOpen())
				{
					GrandExchange.open();
					continue;
				}
				if(putOffer)
				{
					if(completedGEOfferWithQty(selectedHerbPrice.herb.unf,1,false))
					{
						soldUnf = true;
						putOffer = false;
					}
					continue;
				}
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				if(GrandExchange.sellItem(new Item(selectedHerbPrice.herb.unf,1).getName(), 1, 1))
				{
					putOffer = true;
					Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
				}
				continue;
			}
		}
		Logger.log("Timeout after 3 minutes of pricecheck selected herb! Resetting selected herb...");
		selectedHerbPrice = null;
	}
	public static void buyMoreVials()
	{
		Main.paint_task = "Buying 13k vials";
		int buyPrice = (int) Calculations.nextGaussianRandom(9, 2);
		int totalQty = 13000; //buy up as many as can afford
		if(Inventory.count(coins) + Bank.count(coins) <= 50000)
		{
			sellAllUnf();
			return;
		}
		if(Bank.contains(coins) || !Inventory.onlyContains(coins))
		{
			Logger.log("[buyMoreVials] Init - Withdrawing coins / depositing everything else");
			if(!Bank.isOpen())
			{
				clickBank();
				return;
			}
			if(Bank.depositAllExcept(995) && Bank.withdrawAll(coins))
			{
				Sleep.sleepUntil(() -> !Bank.contains(coins),Sleepz.calculate(2222, 2222));
			}
			return;
		}
		
		if(Bank.isOpen())
		{
			if(Bank.close()) Sleep.sleepUntil(() -> !Bank.isOpen(), Sleepz.calculate(2222,2222));
			return;
		}
		
		Logger.log("[buyMoreVials] Start");
		
		Timer timer = new Timer(180000);
		boolean putOffer = false;
		while(!timer.finished() && !ScriptManager.getScriptManager().isPaused() && ScriptManager.getScriptManager().isRunning())
		{
			Sleepz.sleep(420, 696);
			if(!GrandExchange.isOpen())
			{
				GrandExchange.open();
				continue;
			}
			if(putOffer)
			{
				if(isGEEmpty()) break;
				if(isGEActuallyReadyToCollect())
				{
					collect();
					continue;
				}
				Logger.log("Waiting for offer to complete...");
				Sleepz.sleep(2222, 2222);
				continue;
			}
			if(GrandExchange.getFirstOpenSlot() == -1)
			{
				GrandExchange.cancelAll();
				Sleep.sleepUntil(() -> GrandExchange.isReadyToCollect(),Sleepz.calculate(2222, 2222));
				continue;
			}
			if(isGEActuallyReadyToCollect())
			{
				collect();
				continue;
			}
			if(Inventory.count(coins) < (totalQty * buyPrice))
			{
				totalQty = (int)(coins/buyPrice);
				if(totalQty <= 0)
				{
					Logger.log("Not enough coins to buy vials! attempting sell any unf pots");
					sellAllUnf();
					return;
				}
			}
			if(GrandExchange.buyItem(vial, totalQty, buyPrice))
			{
				putOffer = true;
				Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
			}
			continue;
		}
	}
	public static void collect()
	{
		WidgetChild collect = Widgets.get(465,6,0);
		if(collect != null && collect.interact()) Sleep.sleepUntil(() -> !isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
	}
	
	public static Timer lastOfferTimer;
	/**
	 * Must call function after price checking!
	 * Grabs coins, opens GE, cancels all offers if no open slots, collects all offers, then sets buy qty = afforded amount, else capped at 5000 and generated randomly around 4500
	 */
	public static Timer profitTimer = null;
	public static void buyABunchOfHerbs()
	{
		Main.paint_task = "Buying a bunch of "+new Item(selectedHerbPrice.herb.grimy,1).getName();
		
		if(Bank.contains(coins) || !Inventory.onlyContains(coins))
		{
			Logger.log("[buyABunchOfHerbs] Init - Withdrawing coins / depositing everything else");
			if(!Bank.isOpen())
			{
				clickBank();
				return;
			}
			if(Bank.depositAllExcept(995) && Bank.withdrawAll(coins))
			{
				Sleep.sleepUntil(() -> !Bank.contains(coins),Sleepz.calculate(2222, 2222));
			}
			return;
		}
		if(Bank.isOpen())
		{
			if(Bank.close()) Sleep.sleepUntil(() -> !Bank.isOpen(), Sleepz.calculate(2222,2222));
			return;
		}
		Logger.log("[buyABunchOfHerbs] Starting check price of selected herb: " + selectedHerbPrice.herb.toString()+" after seeing out of all grimy/clean/unf! Updating profit/hr...");
		if(isGEEmpty())
		{
			if(initCoins == -1)
			{
				initCoins = Bank.count(coins) + Inventory.count(coins);
			}
			if(profitTimer == null)
			{
				profitTimer = new Timer(2000000000);
			}
			profit = Bank.count(coins) + Inventory.count(coins) - initCoins;
			profitPerHour = (int) ((double) profit / ((double)profitTimer.elapsed() / 3600000));
		}
		Timer timer = new Timer((int) Calculations.nextGaussianRandom(80000, 20000));
		int offerCount = -1;
		boolean putOffer = false;
		while(!timer.finished() && !ScriptManager.getScriptManager().isPaused() && ScriptManager.getScriptManager().isRunning())
		{
			Sleepz.sleep(420, 696);
			if(!GrandExchange.isOpen())
			{
				GrandExchange.open();
				continue;
			}
			if(havePendingBuyOfferOfAnyGrimyOfLvl()) putOffer = true;
			if(putOffer)
			{
				if(lastOfferTimer == null)
				{
					lastOfferTimer = new Timer((int) Calculations.nextGaussianRandom(450000, 222222));
				}
				if(isGEActuallyReadyToCollect())
				{
					if(!GrandExchange.isOpen()) GrandExchange.open();
					collect();
					continue;
				}
				if(isGEEmpty()) break;
				if(!completedGEOfferWithQty(selectedHerbPrice.herb.grimy,offerCount,true))
				{
					Sleepz.sleep(1111, 1111);
					continue;
				}
				break;
			}
			if(GrandExchange.getFirstOpenSlot() == -1)
			{
				GrandExchange.cancelAll();
				Sleep.sleepUntil(() -> GrandExchange.isReadyToCollect(),Sleepz.calculate(2222, 2222));
				continue;
			}
			if(isGEActuallyReadyToCollect())
			{
				collect();
				continue;
			}
			int buyPrice = selectedHerbPrice.grimyHigh + undercuttingBuyGrimy;
			offerCount = (int) Math.floor(Inventory.count(coins) / buyPrice); //buy up as many as can afford
			if(offerCount >= maxHerbBuyQty) offerCount = (int) Calculations.nextGaussianRandom((maxHerbBuyQty - 100), 25);
			int totalPrice = buyPrice * offerCount;
			if(Inventory.count(coins) < totalPrice)
			{
				Logger.log("Not enough coins to buy a bunch of herbs!");
				return;
			}
			if(GrandExchange.buyItem(selectedHerbPrice.herb.grimy, offerCount, buyPrice))
			{
				putOffer = true;
				Sleep.sleepUntil(() -> isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
			}
			continue;
		}
	}
	public static void clickBank()
	{
		Filter<GameObject> bankFilter = g-> g!=null && 
				g.getName().equals("Grand Exchange booth") && 
				g.hasAction("Bank");
		GameObject bank = GameObjects.closest(bankFilter);
		if(bank != null && bank.interact("Bank")) Sleep.sleepUntil(() -> Bank.isOpen() || !l.exists(),
				() -> l.isMoving(),
				Sleepz.calculate(3333, 3333),69);
	}
	/**
	 * checks bank for existing herbs, looks for all existing that have lvl for, randomize, set herb.
	 * If only find unf pots then sell all unf pots.
	 * Returns true if not done checking, false if out of everything.
	 * @return
	 */
	public static boolean checkForAvailableExistingHerbs()
	{
		Logger.log("Checking for available existing herbs");
		if(!checkedBank()) return true;
		final int herbLvl = Skills.getRealLevel(Skill.HERBLORE);
		List<Herb> existingHerbs = new ArrayList<Herb>();
		List<Integer> existingUnf = new ArrayList<Integer>();
		for(Herb herb : acceptableHerbs)
		{
			if(herb.lvl > herbLvl) continue;
			if(Bank.contains(herb.clean) || Bank.contains(herb.grimy) ||  
					Inventory.contains(herb.clean) || Inventory.contains(herb.grimy) || Inventory.contains(new Item(herb.grimy,1).getNotedItemID())) existingHerbs.add(herb);
		}
		if(existingHerbs.isEmpty()) 
		{
			for(Herb herb : Herb.values())
			{
				if(Bank.contains(herb.unf) || Inventory.contains(herb.unf)  || Inventory.contains(new Item(herb.unf,1).getName())) existingUnf.add(herb.unf);
			}
			if(existingUnf.isEmpty()) 
			{
				if(isGEEmpty()) return false;
				if(havePendingBuyOfferOfAnyGrimyOfLvl() || 
						havePendingSellOfferOfAnyUnf())
				{
					Logger.log("Have pending offers");
					if(!GrandExchange.isOpen())
					{
						if(isGEHistoryOpen())
						{
							WidgetChild exchangeButton = Widgets.getChildWidget(383, 2);
							if(exchangeButton != null && exchangeButton.isVisible())
							{
								if(exchangeButton.interact("Exchange"))
								{
									Sleep.sleepUntil(GrandExchange::isOpen, Sleepz.calculate(2222, 2222));
								}
							}
							return true;
						}
						GrandExchange.open();
						return true;
					}
					if(havePendingBuyOfferOfAnyGrimyOfLvl())
					{
						if(lastOfferTimer == null)
						{
							if(GrandExchange.cancelAll()) 
							{
								lastUnfSellTimer = null;
								lastOfferTimer = null;
							}
						}
						else if(lastOfferTimer.finished()) 
						{
							if(GrandExchange.cancelAll())
							{
								needSelectedHerbPriceCheck = true;
								lastUnfSellTimer = null;
								lastOfferTimer = null;
							}
						}
					}
					if(havePendingSellOfferOfAnyUnf())
					{
						if(lastUnfSellTimer == null)
						{
							if(GrandExchange.cancelAll())
							{
								lastUnfSellTimer = null;
								lastOfferTimer = null;
							}
						}
						else if(lastUnfSellTimer.finished()) 
						{
							if(GrandExchange.cancelAll())
							{
								lastUnfSellTimer = null;
								lastOfferTimer = null;
							}
						}
					}
					return true;
				}
				return false;
			}
			//sell all existing unf if no more clean or grimy of our lvl
			sellAllUnf();
			return true;
		}
		Collections.shuffle(existingHerbs);
		selectedHerbPrice = new HerbPrice(existingHerbs.get(0),0,0,0,0);
		Logger.log("Selected random existing herb to process: " + selectedHerbPrice.herb.toString());
		
		return true;
	}
	public static boolean havePendingBuyOfferOfAnyGrimyOfLvl()
	{
		final int herbLvl = Skills.getRealLevel(Skill.HERBLORE);
		for(GrandExchangeItem i : GrandExchange.getItems())
		{
			boolean found = false;
			for(Herb herb : acceptableHerbs)
			{
				if(herbLvl < herb.lvl) continue;
				if(herb.grimy == i.getID()) 
				{
					found = true;
					break;
				}
			}
			if(!found) continue;
			//have offer- check status
			if(i.getStatus() == Status.BUY || i.getStatus() == Status.BUY_COLLECT) return true;
		}
		return false;
	}
	public static boolean havePendingSellOfferOfAnyUnf()
	{
		for(GrandExchangeItem i : GrandExchange.getItems())
		{
			boolean found = false;
			for(Herb herb : Herb.values())
			{
				if(herb.unf == i.getID()) 
				{
					found = true;
					break;
				}
			}
			if(!found) continue;
			//have offer- check status
			if(i.getStatus() == Status.SELL || i.getStatus() == Status.SELL_COLLECT) return true;
		}
		return false;
	}
	/**
	 * sets a timer for 3 minutes for each herb to buy/sell 1 grimy and 1 unf pot of lvl high enough to process.
	 * after recording successfully it will set the highest profit margin herb to selectedHerbPrice to start buying via buyABunchOfHerbs().	
	 */
	public static void priceCheckAvailableHerbs()
	{
		Main.paint_task = "Price checking herbs";
		
		//check bank, get coins, deposit everything else
		if(!checkedBank()) return;
		List<HerbPrice> validHerbPrices = new ArrayList<HerbPrice>();
		if(Bank.contains(coins) || !Inventory.onlyContains(coins))
		{
			Logger.log("[priceCheckAvailableHerbs] Init - withdrawing all coins / depositing everything else");
			
			if(!Bank.isOpen())
			{
				clickBank();
				return;
			}
			if(Bank.depositAllExcept(995) && Bank.withdrawAll(coins))
			{
				Sleep.sleepUntil(() -> !Bank.contains(coins),Sleepz.calculate(2222, 2222));
			}
			return;
		}
		if(Bank.isOpen())
		{
			if(Bank.close()) Sleep.sleepUntil(() -> !Bank.isOpen(), Sleepz.calculate(2222,2222));
			return;
		}
		Logger.log("[priceCheckAvailableHerbs] Starting");
		
		final int herblvl = Skills.getRealLevel(Skill.HERBLORE);
		//invy only contains coins now
		for(Herb herb : acceptableHerbs)
		{
			if(herblvl < herb.lvl) continue;
			Logger.log("Price checking herb: " + herb.toString());
			int grimyOfferPrice = (int) Calculations.nextGaussianRandom((LivePrices.getHigh(herb.grimy) * 5), 50);
			if(grimyOfferPrice >= 20000) grimyOfferPrice = (int) Calculations.nextGaussianRandom(20000, 1000);
			int unfOfferPrice = (int) Calculations.nextGaussianRandom((LivePrices.getHigh(herb.unf) * 5), 50);
			if(unfOfferPrice >= 20000) unfOfferPrice = (int) Calculations.nextGaussianRandom(20000, 1000);
			
			boolean boughtGrimy = false;
			boolean soldGrimy = false;
			boolean boughtUnf = false;
			boolean soldUnf = false;
			boolean putOffer = false;
			Timer timeout = new Timer(180000);
			boolean notTimeout = false;
			while(!timeout.finished() && ScriptManager.getScriptManager().isRunning() && 
					!ScriptManager.getScriptManager().isPaused())
			{
				//need to buy grimy, then sell grimy, then buy unf, sell unf,
				//then check history and search from top down for item id and qty = 1 of sold
				Sleepz.sleep(69, 420);
				if(boughtGrimy && soldGrimy && boughtUnf && soldUnf)
				{
					if(isGEHistoryOpen())
					{
						final int boughtGrimyFor = getGEHistoryMostRecentTradePrice(herb.grimy, true);
						final int soldGrimyFor = getGEHistoryMostRecentTradePrice(herb.grimy, false);
						final int boughtUnfFor = getGEHistoryMostRecentTradePrice(herb.unf, true);
						final int soldUnfFor = getGEHistoryMostRecentTradePrice(herb.unf, false);
						if(boughtGrimyFor == -1 || soldGrimyFor == -1 || boughtUnfFor == -1 || soldUnfFor == -1) 
						{
							Logger.log("Failed to observe existing bought/sold qtys for grimy/unf herb! Trying again...");
							boughtGrimy = soldGrimy = boughtUnf = soldUnf = false;
							continue;
						}
						HerbPrice foundHerbPrice = new HerbPrice(herb,soldGrimyFor,boughtGrimyFor,soldUnfFor,boughtUnfFor);
						foundHerbPrice.printHerbPrices();
						if(foundHerbPrice.profitMargin < minProfitMargin)
						{
							Logger.log("Profit margin of less than "+minProfitMargin+"gp! Skipping this herb...");
							notTimeout = true;
							break;
						}
						validHerbPrices.add(foundHerbPrice);
						notTimeout = true;
						break;
					}
					if(!GrandExchange.isOpen())
					{
						NPC GETeller = NPCs.closest("Grand Exchange Clerk");
						if(GETeller!=null)
						{
							if(GETeller.interact("History"))
							{
								Sleep.sleepUntil(() -> !l.exists() || isGEHistoryOpen(),
										()->l.isMoving(),Sleepz.calculate(2222,2222),69);
							}
						}
						continue;
					}
					if(isGEActuallyReadyToCollect())
					{
						collect();
						continue;
					}
					WidgetChild historyButton = Widgets.get(465, 3);
					if(historyButton != null && historyButton.isVisible())
					{
						if(historyButton.interact("History"))
						{
							Sleep.sleepUntil(() -> !l.exists() || isGEHistoryOpen(), Sleepz.calculate(2222, 2222));
						}
						continue;
					}
					continue;
				}
				//need to buy/sell all items starting with grimy then unf
				if(!boughtGrimy)
				{
					if(!GrandExchange.isOpen())
					{
						if(isGEHistoryOpen())
						{
							WidgetChild exchangeButton = Widgets.getChildWidget(383, 2);
							if(exchangeButton != null && exchangeButton.isVisible())
							{
								if(exchangeButton.interact("Exchange"))
								{
									Sleep.sleepUntil(() -> !l.exists() || GrandExchange.isOpen(), Sleepz.calculate(2222, 2222));
								}
							}
							continue;
						}
						GrandExchange.open();
						continue;
					}
					if(putOffer)
					{
						if(completedGEOfferWithQty(herb.grimy,1,true))
						{
							boughtGrimy = true;
							putOffer = false;
						}
						continue;
					}
					if(GrandExchange.getFirstOpenSlot() == -1)
					{
						GrandExchange.cancelAll();
						Sleep.sleepUntil(() -> !l.exists() || GrandExchange.isReadyToCollect(),Sleepz.calculate(2222, 2222));
						continue;
					}
					if(isGEActuallyReadyToCollect())
					{
						collect();
						continue;
					}
					if(GrandExchange.buyItem(herb.grimy, 1, grimyOfferPrice))
					{
						putOffer = true;
						Sleep.sleepUntil(() -> !l.exists() || isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
					}
					continue;
				}
				if(!soldGrimy)
				{
					if(!GrandExchange.isOpen())
					{
						GrandExchange.open();
						continue;
					}
					if(putOffer)
					{
						if(completedGEOfferWithQty(herb.grimy,1,false))
						{
							soldGrimy = true;
							putOffer = false;
						}
						continue;
					}
					if(isGEActuallyReadyToCollect())
					{
						collect();
						continue;
					}
					if(GrandExchange.sellItem(new Item(herb.grimy,1).getName(), 1, 1))
					{
						putOffer = true;
						Sleep.sleepUntil(() -> !l.exists() || isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
					}
					continue;
				}
				if(!boughtUnf)
				{
					if(!GrandExchange.isOpen())
					{
						GrandExchange.open();
						continue;
					}
					if(putOffer)
					{
						if(completedGEOfferWithQty(herb.unf,1,true))
						{
							boughtUnf = true;
							putOffer = false;
						}
						continue;
					}
					if(isGEActuallyReadyToCollect())
					{
						collect();
						continue;
					}
					if(GrandExchange.buyItem(herb.unf, 1, unfOfferPrice))
					{
						putOffer = true;
						Sleep.sleepUntil(() -> !l.exists() || isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
					}
					continue;
				}
				if(!soldUnf)
				{
					if(!GrandExchange.isOpen())
					{
						GrandExchange.open();
						continue;
					}
					if(putOffer)
					{
						if(completedGEOfferWithQty(herb.unf,1,false))
						{
							soldUnf = true;
							putOffer = false;
						}
						continue;
					}
					if(isGEActuallyReadyToCollect())
					{
						collect();
						continue;
					}
					if(GrandExchange.sellItem(new Item(herb.unf,1).getName(), 1, 1))
					{
						putOffer = true;
						Sleep.sleepUntil(() -> !l.exists() || isGEActuallyReadyToCollect(), Sleepz.calculate(2222, 2222));
					}
					continue;
				}
			}
			if(!notTimeout)	Logger.log("Timeout after 3 minutes of herb pricecheck!");
		}
		if(validHerbPrices.isEmpty())
		{
			Logger.log("Failed to obtain any acceptable herb prices after checking all available herbs for lvl!");
			return;
		}
		if(xpMode)
		{
			Logger.log("Sorting herb list according to highest lvl");
			Collections.sort(validHerbPrices, new Comparator<HerbPrice>() {
		          @Override
		          public int compare(HerbPrice o1, HerbPrice o2) {
		        	  return o2.herb.lvl - o1.herb.lvl;
		          }
		    });
		}
		else
		{
			Logger.log("Sorting herb list according to highest profit margin");
			Collections.sort(validHerbPrices, new Comparator<HerbPrice>() {
		          @Override
		          public int compare(HerbPrice o1, HerbPrice o2) {
		        	  return o2.profitMargin - o1.profitMargin;
		          }
		    });
		}
		for(HerbPrice herbPrice : validHerbPrices)
		{
			herbPrice.printHerbPrices();
		}
		selectedHerbPrice = validHerbPrices.get(0);
		Logger.log("~Choosing herb: "+selectedHerbPrice.herb.toString()+"~");
		needSelectedHerbPriceCheck = false;
	}
	public static boolean isGEHistoryOpen()
	{
		WidgetChild historyHeader = Widgets.get(383, 1 , 1);
		if(historyHeader != null && historyHeader.isVisible() && historyHeader.getText().contains("Grand Exchange Trade History")) return true;
		return false;
	}
	/**
	 * Call after GE History open pls. 
	 * Scans top down for the price that the latest offer was fulfilled at, returns -1 if not found.
	 * Gets price per item, not total offer price.
	 * When searching sell offers, accounts for the stupid GE tax to return the price that the buyer paid for it.
	 * @param itemID
	 * @param bought
	 * @return
	 */
	private final static String yellowColour = "<col=ffb83f>";
	private final static String greyColour = "<col=9f9f9f>";
	//orange colour is lack of yellow
	public static int getGEHistoryMostRecentTradePrice(int itemID, boolean bought)
	{
		for(int i = 0; i < 240; i = i+6)
		{
			WidgetChild itemIDStackSize = Widgets.get(383,3,(i+4));
			final int ID = itemIDStackSize.getItemId();
			if(ID != itemID) continue;
			WidgetChild boughtOrSold = Widgets.get(383,3,(i+2));
			WidgetChild pricingWidget = Widgets.get(383,3,(i+5));
			final int stackSize = itemIDStackSize.getItemStack();
			final boolean buy = (boughtOrSold.getText().contains("Bought:") ? true : false);
			if(buy != bought) continue;
			String priceText = pricingWidget.getText();
			if(bought)
			{
				if(stackSize > 1)
				{
					return Integer.parseInt(priceText.split("</col><br>= ")[1].split(" each")[0].replace(",", ""));
				}
				else return Integer.parseInt(priceText.replace(yellowColour, "").split(" coins")[0].replace(",", ""));
			}
			//sold with GE tax
			if(priceText.contains(greyColour))
			{
				//must find the total offer price paid in grey, divide by stacksize, round up
				return (int) Math.ceil((((double)Integer.parseInt(priceText.split("</col><br><col=9f9f9f>\\(")[1].split(" - ")[0].replace(",", ""))) / stackSize));
			}
			//sold no GE tax
			if(stackSize > 1)
			{
				return Integer.parseInt(priceText.split("</col><br>= ")[1].split(" each")[0].replace(",", ""));
			}
			else return Integer.parseInt(priceText.replace(yellowColour, "").split(" ")[0]);
		}
		return -1;
	}
	/**
	 * Checks GE offers to see if an offer slot has bought at least the qty of item, if so returns true, else false.
	 * @param itemID
	 * @param minQty
	 * @param buy
	 * @return
	 */
	public static boolean completedGEOfferWithQty(int itemID,int minQty, boolean buy)
	{
		for(GrandExchangeItem i : GrandExchange.getItems())
		{
			if(i.getID() <=  0 || (buy ? !i.isBuyOffer() : !i.isSellOffer()))  continue;
			if(i.getID() == itemID && i.getTransferredAmount() >= minQty)
			{
				Logger.log("Found offer fulfilled for "+(buy ? "buy" : "sell")+" item: " + new Item(itemID,1).getName() + " in minQty: " + minQty);
				return true;
			}
		}
		//return as completed offer if none exist
		return true;
	}
	public static boolean isGEEmpty()
	{
		for(GrandExchangeItem i : GrandExchange.getItems())
		{
			if(i.getID() ==  0)  continue;
			return false;
		}
		return true;
	}
	public static boolean isGEActuallyReadyToCollect()
	{
		if(!GrandExchange.isOpen()) return false;
		WidgetChild collect = Widgets.get(465,6,0);
		if(collect != null)
		{
			if(collect.isHidden()) return false;
			return true;
		}
		return false;
	}
	public static int initCoins = -1;
	public static boolean checkedBank()
	{
		//return true;
		if(Bank.getLastBankHistoryCacheTime() <= 0)
		{
			if(Bank.isOpen()) 
			{
				Bank.count(coins);
			}
			else clickBank();
		}
		if(Bank.getLastBankHistoryCacheTime() > 0)
		{
			return true;
		}
		return false;
	}
	private static final int coins = 995;
	private static final int grimyGuam = 199;
	private static final int guamUnf = 91;
	private static final int guam = 249;
	private static final int grimyRanarr = 207;
	private static final int ranarr = 257;
	private static final int ranarrUnf = 99;
	private static final int grimyHarra = 205;
	private static final int harra = 255;
	private static final int harraUnf = 97;
	private static final int combatPot3 = 9741;
	private static final int goatHornDust = 9736;
	private static final int grimyAvantoe = 211;
	private static final int avantoe = 261;
	private static final int avantoeUnf = 103;
	private static final int irit = 259;
	private static final int iritUnf = 101;
	private static final int grimyIrit = 209;
	private static final int toadflax = 2998;
	private static final int toadflaxUnf = 3002;
	private static final int toadflaxGrimy = 3049;
	private static final int kwuarm = 263;
	private static final int kwuarmUnf = 105;
	private static final int kwuarmGrimy = 213;
	private static final int snapdragon = 3000;
	private static final int snapdragonUnf = 3004;
	private static final int snapdragonGrimy = 3051;
	private static final int cadantine = 265;
	private static final int cadantineUnf = 107;
	private static final int cadantineGrimy = 215;
	private static final int lantadyme = 2481;
	private static final int lantadymeUnf = 2483;
	private static final int lantadymeGrimy = 2485;
	private static final int dwarfweed = 267;
	private static final int dwarfweedUnf = 109;
	private static final int dwarfweedGrimy = 217;

	private static final int torstol = 269;
	private static final int torstolUnf = 111;
	private static final int torstolGrimy = 219;
	private static final int attPot3 = 121;
	private static final int eyeOfNewt = 221;
	private static final int vial = 227;
	public static int cleans = 0;
	public static int cleansPerHour = 0;

	//data gathered 9/1/2022 from GE-tracker.com for historical average from the last week
	private static final int guamDailyTraded = 200000;
	private static final int ranarrDailyTraded = 600000;
	private static final int toadflaxDailyTraded = 700000;
	private static final int iritDailyTraded = 400000;
	private static final int kwuarmDailyTraded = 400000;
	private static final int avantoeDailyTraded = 400000;
	private static final int snapdragonDailyTraded = 450000;
	private static final int cadantineDailyTraded = 250000;
	private static final int dwarfweedDailyTraded = 450000;
	
	public static enum Herb
	{
		GUAM(grimyGuam,guam,guamUnf,3),
		RANARR(grimyRanarr,ranarr,ranarrUnf,30),
		AVANTOE(grimyAvantoe,avantoe,avantoeUnf,50),
		IRIT(grimyIrit,irit,iritUnf,45),
		TOADFLAX(toadflaxGrimy,toadflax,toadflaxUnf,34),
		KWUARM(kwuarmGrimy,kwuarm,kwuarmUnf,55),
		SNAPDRAGON(snapdragonGrimy,snapdragon,snapdragonUnf,63),
		DWARF_WEED(dwarfweedGrimy,dwarfweed,dwarfweedUnf,72),
		CADANTINE(cadantineGrimy,cadantine,cadantineUnf,66),
		LANTADYME(lantadymeGrimy,lantadyme,lantadymeUnf,69),
		HARRALANDER(grimyHarra,harra,harraUnf,22),
		TORSTOL(torstolGrimy,torstol,torstolUnf,78);
		public int grimy;
		public int clean;
		public int unf;
		public int lvl;
		Herb(int grimy, int clean, int unf, int lvl) {
			this.grimy = grimy;
			this.clean = clean;
			this.unf = unf;
			this.lvl = lvl;
		}
	}
	public static Herb getHerbFromID(int anyCleanGrimyUnfID)
	{
		for(Herb herb : Herb.values())
		{
			if(herb.grimy == anyCleanGrimyUnfID || 
					herb.clean == anyCleanGrimyUnfID || 
					herb.unf == anyCleanGrimyUnfID) return herb;
		}
		return null;
	}
	public static boolean closeQuestCompletion()
	{
		if(Widgets.get(153,16) != null && 
    			Widgets.get(153,16).isVisible())
    	{
			if(ClientSettings.isEscInterfaceClosingEnabled()) Keyboard.closeInterfaceWithESC();
			else if(Widgets.get(153,16).interact("Close")) Sleepz.sleep(696, 666);
    		return true;
    	}
		return false;
	}
	public static boolean completedDruidicRitual()
	{
		if(getProgressStep() == 4)
		{
			if(handleDialogues()) return false;
			if(closeQuestCompletion()) return false;
			if(Locations.clickableGEArea.contains(l)) return true;
			Walkz.useJewelry(InvEquip.wealth, "Grand Exchange");
		}
		return false;
	}
	public static int getProgressStep()
	{
		return PlayerSettings.getConfig(80);
	}
	public static void doDruidicRitual()
	{
		if(handleDialogues()) return;
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
			if(fulfilledStep0()) talkToSanfew();
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
		if(!Inventory.contains(id.enchantedBear,id.rawBear) || 
				!Inventory.contains(id.enchantedBeef,id.rawBeef) || 
				!Inventory.contains(id.enchantedChikken,id.rawChikken) || 
				!Inventory.contains(id.enchantedRat,id.rawRat))
		{
			fulfilledStep0();
			return false;
		}
		if(Locations.druidicRitual_cauldron.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Using meats -> Cauldron";
			Item i = Inventory.get(id.rawBeef,id.rawChikken,id.rawRat,id.rawBear);
			GameObject cauldron = GameObjects.closest("Cauldron of Thunder");
			if(i != null && cauldron != null)
			{
				if(i.useOn(cauldron))
				{
					Sleep.sleepUntil(() -> !l.exists() || !Inventory.contains(i), 
							() -> l.isMoving(),
							Sleepz.calculate(3333, 3333),69);
				}
			}
			return false;
		}
		if(Locations.druidicRitual_skeletonsHallway.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Chucking and jiving on these skellies";
			if(Combat.isAutoRetaliateOn())
			{
				Combatz.toggleAutoRetaliate(false);
				return false;
			}
			if(Combatz.shouldEatFood(9)) Combatz.eatFood();
			API.interactWithGameObject("Prison door","Open", Locations.druidicRitual_cauldron);
			return false;
		}
		if(Locations.allBurthropeTaverly.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Walking -> Climb-down -> Ladder";
			API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.druidicRitual_abovegroundLadder, () -> !l.exists() || Locations.druidicRitual_undergroundLadder.contains(l));
			return false;
		}
		if(Locations.druidicRitual_sanfewAbove.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Walking -> Climb-down -> Staircase";
			API.walkInteractWithGameObject("Staircase", "Climb-down", Locations.druidicRitual_sanfewAbove, () -> !l.exists() || Locations.druidicRitual_sanfewbelow.contains(l));
			return false;
		}
		Walkz.useJewelry(InvEquip.games, "Burthorpe");
		return false;
	}
	public static void talkToSanfew()
	{
		if(Locations.druidicRitual_sanfewAbove.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Talk-to -> Sanfew";
			API.walkTalkToNPC("Sanfew", "Talk-to", true,Locations.druidicRitual_sanfewAbove);
			return;
		}
		if(Locations.allBurthropeTaverly.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Walking -> Climb-down -> Staircase";
			API.walkInteractWithGameObject("Staircase", "Climb-up", Locations.druidicRitual_sanfewbelow, () -> !l.exists() || Locations.druidicRitual_sanfewAbove.contains(l));
			return;
		}
		if(Locations.druidicRitual_cauldron.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Getting out of this musty witch's kitchen";
			API.walkInteractWithGameObject("Prison door","Open", Locations.druidicRitual_cauldron, () -> !l.exists() || Locations.druidicRitual_skeletonsHallway.contains(l));
			return;
		}
		if(Locations.druidicRitual_skeletonsHallway.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Twerking on they skelly asses";
			if(Combatz.shouldEatFood(9)) Combatz.eatFood();
			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.druidicRitual_undergroundLadder, () -> !l.exists() || Locations.druidicRitual_abovegroundLadder.contains(l));
			return;
		}

		Main.paint_task = "[Druidic Ritual] Games necklace -> Burthrope";
		Walkz.useJewelry(InvEquip.games, "Burthorpe");
	}
	public static void talkToKaqemeex()
	{
		if(Locations.allBurthropeTaverly.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Walking -> Talk-to -> Kaqemeex";
			API.walkTalkToNPC("Kaqemeex", "Talk-to", Locations.kaqemeex);
			return;
		}
		if(Locations.druidicRitual_sanfewAbove.contains(l))
		{
			Main.paint_task = "[Druidic Ritual] Climb-down -> Staircase";
			API.interactWithGameObject("Staircase", "Climb-down", () -> !l.exists() || Locations.druidicRitual_sanfewbelow.contains(l));
			return;
		}
		Main.paint_task = "[Druidic Ritual] Games necklace -> Burthrope";
		Walkz.useJewelry(InvEquip.games, "Burthorpe");
	}
	public static boolean fulfilledStep0()
	{
		Main.paint_task = "[Druidic Ritual] Fulfilling inventory / equipment for quest start";
		if(Inventory.containsAll(id.rawBear,id.rawBeef,id.rawChikken,id.rawRat) && 
				InvEquip.equipmentContains(InvEquip.wearableWealth) && 
				InvEquip.equipmentContains(InvEquip.wearableGames)) return true;
		if(!InvEquip.checkedBank()) return false;
		InvEquip.clearAll();
		InvEquip.addInvyItem(Combatz.lowFood, 5, (int) Calculations.nextGaussianRandom(10, 3), false, (int) Calculations.nextGaussianRandom(20, 5));
		int newbHerbBoostQty = (int) Calculations.nextGaussianRandom(350, 33);
		if(Bank.count(eyeOfNewt) < newbHerbBoostQty) InvEquip.addInvyItem(eyeOfNewt, 250, newbHerbBoostQty, true, newbHerbBoostQty);
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
	public static String dialog = "";
	public static void updateLastNPCDialog()
	{
		if(Widgets.get(193, 2) != null && 
				Widgets.get(193,2).isVisible())
		{
			String txt = Widgets.get(193, 2).getText();
			if(txt != null && !txt.isEmpty() && !txt.equalsIgnoreCase("null")) 
			{
				dialog = txt;
				return;
			}
		}
		String dialoge = Dialogues.getNPCDialogue();
		if(dialoge != null && !dialoge.isEmpty() && !dialoge.equalsIgnoreCase("null") && !dialoge.equals(dialog))
		{
			Logger.log("NPC Dialogue: " + dialoge);
			dialog = dialoge;
		}
	}
	public static boolean handleDialogues()
	{
		if(Dialogues.isProcessing()) 
		{
			Sleep.sleepTick();
			return true;
		}
		if(Dialogues.canContinue())
		{
			final int timeout = Sleepz.calculate(3333,3333);
			final Condition condition = () -> {
			 	updateLastNPCDialog();
			 	return Dialogues.areOptionsAvailable() || 
						!Dialogues.inDialogue();
			};
			Keyboard.holdSpace(condition,timeout);
			Sleep.sleepUntil(condition, timeout);
			return true;
		}
		if(Dialogues.areOptionsAvailable())
		{
			return Dialogues.chooseOption("I\'m in search of a quest.") || 
					Dialogues.chooseOption("Okay, I will try and help.") ||
					Dialogues.chooseOption("Yes.") || 
					Dialogues.chooseOption("I\'ve been sent to help purify the Varrock stone circle.") || 
					Dialogues.chooseOption("Ok, I\'ll do that then.");
		}
		return false;
	}
	@Override
	public boolean isValid() {
		return API.mode == modes.TRAIN_HERBLORE;
	}
}