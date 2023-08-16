package script.skills.magic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.GrandExchangeItem;
import org.dreambot.api.methods.grandexchange.Status;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;

import script.utilities.Bankz;
import script.utilities.GrandExchangg;
import script.utilities.InvEquip;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;

public class BuyHighAlchs {
	

	public static LinkedHashMap<Integer, Timer> HABuyTimeouts = new LinkedHashMap<Integer,Timer>();
	
	/**
	 * Buys a thing at the GE for specified profit price of 100 until timer runs out of 10 minutes
	 */
	public static void buyItems(boolean longbowsOrNot)
	{
		InvEquip.clearAll();
		InvEquip.addInvyItem(id.natureRune,200,5000, false, (int) Calculations.nextGaussianRandom(2000,100));
		InvEquip.addInvyItem(InvEquip.coins,1,9999999, false, 0);
		InvEquip.addInvyItem(InvEquip.duel,1,1,false,3);
		InvEquip.addOptionalItem(id.airRune);
		InvEquip.addOptionalItem(id.lawRune);
		InvEquip.setEquipItem(EquipmentSlot.WEAPON,id.staffOfFire);
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		if(!InvEquip.fulfillSetup(true, 180000)) return;
		final int timeout = 600000;
		Logger.log("Starting HA buy function with staff of fire equipped");
		List<Integer> keys = new ArrayList(id.approvedAlchs.keySet());
		if(longbowsOrNot) keys = new ArrayList(id.xpAlchs.keySet());
		Collections.shuffle(keys);
		Timer timer = new Timer(600000);
		for (Integer o : keys) {
			Logger.log("Entering fulfill method for HA Item: " + new Item(o, 1).getName());
			final int initItemCount = Bank.count(o) + Inventory.count(o) + Inventory.count(new Item(o,1).getNotedItemID());
			
			Timer timer2 = new Timer(timer.remaining());
			while(!timer2.finished() && Client.getGameState() == GameState.LOGGED_IN
					&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
			{
				Sleepz.sleep(69, 69);
				if(Magic.isSpellSelected())
		    	{
		    		Magic.deselect();
		    		continue;
		    	}
				int buyLimit = 0;
				if(longbowsOrNot) buyLimit = id.xpAlchs.get(o);
				else buyLimit = id.approvedAlchs.get(o);
				final int itemID = o;
				final Item item = new Item(itemID, 1);
				final int itemPrice;
				if(longbowsOrNot) itemPrice = (item.getHighAlchValue() - (new Item(id.natureRune,1).getLivePrice()) - 10);
				else itemPrice = (item.getHighAlchValue() - (new Item(id.natureRune,1).getLivePrice()) - 100);
				if(Bank.isOpen()) Bank.count(InvEquip.coins); //random API call to update bank cache ...
				final int totalItemCount = Bank.count(itemID) + Inventory.count(itemID);
				if(totalItemCount >= (initItemCount + buyLimit))  break;
				int alchCount = 0;
				boolean containsAlch = false;
				for(Integer i : keys)
				{
					if(Bank.count(i) > 0) 
					{
						Logger.log("Bank contains alch: " + new Item(i,1).getName());
						containsAlch = true;
					}
					alchCount += Inventory.count(i) + Inventory.count(new Item(i,1).getNotedItemID()) + Bank.count(i);
				}
				if(longbowsOrNot)
				{
					if(alchCount > 400) return;
				}
				else if(alchCount > 200) return;
				if(Bank.contains(InvEquip.coins) || 
						Bank.contains(id.natureRune) || 
						containsAlch ||
						Inventory.emptySlotCount() <= 0 || 
						(!new Item(itemID,1).isStackable() && Inventory.count(new Item(itemID,1).getUnnotedItemID()) > 0))
				{
					if(Bank.isOpen())
					{
						boolean continue1 = false;
						for(Integer i : keys)
						{
							if(new Item(i,1).isStackable()) continue;
							if(Inventory.count(new Item(i,1).getUnnotedItemID()) > 0)
							{
								if(Bank.depositAll(new Item(i,1).getUnnotedItemID())) 
								{
									Logger.log("Deposited some unnoted High Alchs");
									Sleepz.sleep(696, 420);
								}
								continue1 = true;
							}
						}
						if(continue1) continue;
						if(Bank.getWithdrawMode() == BankMode.NOTE)
						{
							if(Inventory.emptySlotCount() <= 0)
							{
								InvEquip.depositExtraJunk();
								Sleepz.sleepInteraction();
								continue;
							}
							
							if(Bank.withdrawAll(InvEquip.coins))
							{
								Sleep.sleepUntil(() -> !Bank.contains(InvEquip.coins), Sleepz.calculate(2222, 2222));
								continue;
							}

							if(Bank.withdrawAll(id.natureRune))
							{
								Sleep.sleepUntil(() -> !Bank.contains(id.natureRune), Sleepz.calculate(2222, 2222));
								continue;
							}
							boolean withdrewSome = false;
							for(Integer i : keys)
							{
								if(Bank.withdrawAll(i)) 
								{
									withdrewSome = true;
									Sleepz.sleep(696, 420);
								}
							}
							if(withdrewSome) continue;
							Logger.log("Test1");
						}
						else
						{
							Bank.setWithdrawMode(BankMode.NOTE);
						}
						continue;
					}
					if(GrandExchange.isOpen() && GrandExchangg.close()) 
					{
						Sleepz.sleep(696, 420);
						continue;
					}
					if(BankLocation.GRAND_EXCHANGE.distance(Players.getLocal().getTile()) < 50 &&
							Bank.open(BankLocation.GRAND_EXCHANGE) )
					{
						Sleepz.sleep(420, 696);
						continue;
					}
					Walkz.goToGE(timer.remaining());
					continue;
				}
				if(!GrandExchange.isOpen())
				{
					if(Bank.isOpen()) 
					{
						Bankz.close();
						continue;
					}
					if(Walkz.goToGE(timer.remaining()))
					{
						
						if(GrandExchange.open())
						{
							Sleep.sleepUntil(() -> GrandExchange.isOpen(), Sleepz.calculate(2222, 2222));
						}
					}
					continue;
				}
				else
				{
					if(GrandExchange.isReadyToCollect())
					{
						if(GrandExchange.collect())
						{
							Sleep.sleepUntil(() -> !GrandExchange.isReadyToCollect(), Sleepz.calculate(2222, 2222));
						}
						continue;
					}
					if(GrandExchange.isBuyOpen())
					{
						Logger.log("Buy menu open");
						
						if(GrandExchange.goBack())
						{
							Logger.log("Went back on GE");
						}
						
						continue;
					}
					Logger.log("|| Printing timers || ");
					boolean breakalso = false;
					for(Entry<Integer,Timer> entry : HABuyTimeouts.entrySet())
					{
						if(entry.getValue().finished())
						{
							Logger.log("Item: " +new Item(entry.getKey(),1).getName()+ " || Timer: finished");
						}
						else
						{
							Logger.log("Item: " +new Item(entry.getKey(),1).getName()+ " || Timer: "+Timer.formatTime(entry.getValue().remaining()));
							if(entry.getKey() == itemID) 
								{
								breakalso = true;
								break;
								}
						}
					} 
					if(breakalso) break;
					boolean collect = false;
					boolean wait = false;
					boolean breakWhileLoop = false;
					//check existing offers for items with timers ran out
					for(GrandExchangeItem geItem : GrandExchange.getItems())
					{
						if(itemID == geItem.getID()) breakWhileLoop = true;
						if(HABuyTimeouts.containsKey(geItem.getID()))
						{
							if(HABuyTimeouts.get(geItem.getID()).finished())
							{
								if(Widgets.get(465,(geItem.getSlot() + 7),16) != null && 
										Widgets.get(465,(geItem.getSlot() + 7),16).isVisible())
								{
									if(Widgets.get(465,(geItem.getSlot() + 7),16).interact("Abort offer"))
									{
										Logger.log("Aborted offer via right click");
										collect = true;
										HABuyTimeouts.remove(geItem.getID());
										Sleepz.sleep(696, 696);
									}
								}
							}
							else 
							{
								wait = true; //have some timer running on an item
							}
						} 
						else
						{
							if(geItem == null || geItem.getStatus() == Status.EMPTY || geItem.getID() == -1 || 
									geItem.getName() == null || 
									geItem.getName().isEmpty()) continue;
							HABuyTimeouts.put(geItem.getID(), new Timer((int) (Calculations.nextGaussianRandom(50000,15000))));
						}
					}
					if(breakWhileLoop) 
					{
						Logger.log("Have current alch item in offers already!");
						Sleepz.sleep(420, 696);
						break;
					}
					if(collect) 
					{
						Logger.log("Have canceled some old offers!");
						Sleepz.sleep(420, 696);
						continue;
					}
					
					int pricePer = itemPrice;
					int buyQty = buyLimit;
					int totalPrice = buyLimit * pricePer;
					if(Inventory.count(InvEquip.coins) < 100000)
					{
						if(wait) 
						{
							Sleepz.sleep(420, 696);
							continue;
						}
						Logger.log("Account needs more gp (100k) to continue HA function, exiting with no offer timers left");
						return;
					}
					if(Inventory.count(InvEquip.coins) < totalPrice)
					{
						Logger.log("Account needs more GP to buy item: " + new Item(itemID,1).getName() +" in qty: " + buyLimit+" at total price: " + totalPrice+" gp, adjusting to gp amt...");
						buyQty =(int) (Inventory.count(InvEquip.coins) / pricePer);
						totalPrice = buyQty * pricePer;
						if(Inventory.count(InvEquip.coins) < totalPrice)
						{
							Logger.log("Account needs more GP to buy item: " + new Item(itemID,1).getName() +" in qty: " + buyQty+" at total price: " + totalPrice+" gp, stopping HA buying function...");
							break;
						}
						buyLimit = buyQty;
					}
					if(GrandExchange.getFirstOpenSlot() == -1)
					{
						Logger.log("Waiting, all offers full...");
						continue;
					}
					if(buyLimit > 1000)
					{
						buyLimit = (int) Calculations.nextGaussianRandom(1000, 222);
					}
					if(GrandExchange.buyItem(itemID, buyLimit, pricePer))
					{
						HABuyTimeouts.put(o, new Timer((int) Calculations.nextGaussianRandom(15000, 1000)));
						Sleep.sleepUntil(() -> GrandExchange.isReadyToCollect(), Sleepz.calculate(5000, 5555));
						Sleepz.sleep(111, 111);
						continue;
					}
				}
			}
		}
		Logger.log("Done with HA Buy function!");
	}
}
