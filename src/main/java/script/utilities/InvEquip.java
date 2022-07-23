package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.depositbox.DepositBox;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;

import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.skills.woodcutting.TrainWoodcutting;

public class InvEquip {
	public static Map<EquipmentSlot, Integer> equipmentMap = new LinkedHashMap<EquipmentSlot, Integer>();
	public static Map<Integer,InventoryItem> inventoryList = new LinkedHashMap<Integer,InventoryItem>();
	public static List<Integer> optionalItems = new ArrayList<Integer>();
	

	
	public static void clearAll()
	{
		clearInventoryList();
		clearEquipmentSlots();
	}
	
	public static void clearInventoryList()
	{
		inventoryList.clear();
		optionalItems.clear();
	}
	public static void addOptionalItem(int itemID)
	{
		optionalItems.add(itemID);
	}
	public static void shuffleFulfillOrder()
	{
		List<Integer> list = new ArrayList<>(inventoryList.keySet());
	    Collections.shuffle(list);
	    Map<Integer, InventoryItem> shuffleInv = new LinkedHashMap<>();
	    list.forEach(k->shuffleInv.put(k, inventoryList.get(k)));
	    
	    List<EquipmentSlot> equipList = new ArrayList<>(equipmentMap.keySet());
	    Collections.shuffle(equipList);
	    Map<EquipmentSlot, Integer> shuffleEquip = new LinkedHashMap<>();
	    equipList.forEach(k->shuffleEquip.put(k, equipmentMap.get(k)));
	    
	    clearAll();
	    equipmentMap = shuffleEquip;
	    inventoryList = shuffleInv;
	}
	public static void clearEquipmentSlots()
	{
		equipmentMap.put(EquipmentSlot.HAT, 0);
		equipmentMap.put(EquipmentSlot.CAPE, 0);
		equipmentMap.put(EquipmentSlot.ARROWS, 0);
		equipmentMap.put(EquipmentSlot.AMULET, 0);
		equipmentMap.put(EquipmentSlot.WEAPON, 0);
		equipmentMap.put(EquipmentSlot.CHEST, 0);
		equipmentMap.put(EquipmentSlot.LEGS, 0);
		equipmentMap.put(EquipmentSlot.SHIELD, 0);
		equipmentMap.put(EquipmentSlot.HANDS, 0);
		equipmentMap.put(EquipmentSlot.RING, 0);
		equipmentMap.put(EquipmentSlot.FEET, 0);
	}
	
	public static void setEquipItem(EquipmentSlot slot, int ID)
	{
		equipmentMap.put(slot, ID);
	}
	public static void addInvyItem(int ID, int minQty, int maxQty, boolean noted, int refillQty)
	{
		inventoryList.put(ID, InventoryItem.createItem(ID,minQty,maxQty,noted, refillQty));
		//MethodProvider.log("Added item to inventoryList: " + inventoryList.get(ID).itemRef.getName()
				//+" in qty: " + inventoryList.get(ID).minQty + " - " + inventoryList.get(ID).maxQty
				//+" and noted: " + inventoryList.get(ID).noted
				//+" and refill qty: " + inventoryList.get(ID).refillQty);
	}
	
	public static boolean checkedBank()
	{
		//return true;
		if(Bank.getLastBankHistoryCacheTime() <= 0)
		{
			if(Bank.isOpen()) 
			{
				Bank.contains(coins);
			}
			else
			{
				if(!closeBankEquipment()) return false;
				if(!Bankz.openClosest()) Sleep.sleep(666,666);
			}
		}
		if(Bank.getLastBankHistoryCacheTime() > 0)
		{
			return true;
		}
		return false;
	}
	
	public static int getFirstInBank(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Bank.contains(i)) return i;
		}
		return -1;
	}
	public static int getFirstInEquipment(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Equipment.contains(i)) return i;
		}
		return -1;
	}
	public static int getFirstInInventory(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Inventory.contains(i)) return i;
		}
		return -1;
	}
	public static boolean invyContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Inventory.contains(i)) return true;
		}
		return false;
	}
	public static int getInvyItem(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Inventory.contains(i)) return i;
		}
		return 0;
	}
	public static boolean equipmentContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Equipment.contains(i)) return true;
		}
		return false;
	}
	public static int getEquipmentItem(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Equipment.contains(i)) return i;
		}
		return 0;
	}
	public static int getBankItem(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Bank.contains(i)) return i;
		}
		return 0;
	}
	public static boolean bankContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Bank.contains(i)) return true;
		}
		return false;
	}
	public static void depositExtraJunk()
	{
		Item i = Inventory.get(TrainRanged.jug);
		if(i == null) i = Inventory.get(id.vial);
		if(i == null) i = Inventory.get(InvEquip.waterskin0);
		if(i == null) i = Inventory.get(InvEquip.waterskin1);
		if(i == null) i = Inventory.get(InvEquip.waterskin2);
		if(i == null) i = Inventory.get(InvEquip.waterskin3);
		if(i == null) i = Inventory.get(TrainWoodcutting.logs);
		if(i == null) i = Inventory.get(TrainWoodcutting.oakLogs);
		if(i == null) i = Inventory.get(InvEquip.skills0);
		if(i == null) i = Inventory.get(InvEquip.wealth0);
		if(i == null) i = Combat.getFood();
		if(i == null) i = Inventory.get(InvEquip.getInvyItem(ItemsOnGround.allSlayerLoot));
		if(i != null)
		{
			if(i.isNoted() || i.isStackable())
			{
				if(Bank.depositAll(i))Sleep.sleep(696, 420);
			} else if(Bank.deposit(i,1)) Sleep.sleep(696, 420);
			return;
		}
		MethodProvider.log("Could not find any un-valuable things to deposit to make room in invy :-( ");
		return;
	}
	public static boolean withdrawOne(int itemID, long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 69);

			MethodProvider.log("In WithdrawOne loop for itemID: "+ itemID + " / name: "+new Item(itemID,1).getName());
			if(Inventory.count(itemID) > 0) return true;
			if(!closeBankEquipment()) continue;
			if(Bankz.openClosest())
			{
				if(Inventory.emptySlotCount() < 2)
				{
					depositExtraJunk();
					continue;
				}
				if(Bank.withdraw(itemID,1))
				{
					MethodProvider.sleepUntil(() -> Inventory.count(itemID) > 0, Sleep.calculate(2222, 2222));
				}
			} else Sleep.sleep(666, 666);
		}
		return false;
	}
	public static void withdrawAll(int itemID, boolean noted, long timeout)
	{
		Timer timer = new Timer(timeout);
		final int count = Bank.count(itemID);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 69);
			if(Bank.count(itemID) < count || Inventory.isFull()) return;
			if(closeBankEquipment()) continue;
			if(Bankz.openClosest())
			{
				if(noted)
				{
					if(Bank.getWithdrawMode() != BankMode.NOTE)
					{
						if(Bank.setWithdrawMode(BankMode.NOTE))
						{
							MethodProvider.sleepUntil(() -> Bank.getWithdrawMode() == BankMode.NOTE, Sleep.calculate(2222, 2222));
						}
					}
				}
				else
				{
					if(Bank.getWithdrawMode() != BankMode.ITEM)
					{
						if(Bank.setWithdrawMode(BankMode.ITEM))
						{
							MethodProvider.sleepUntil(() -> Bank.getWithdrawMode() == BankMode.ITEM, Sleep.calculate(2222, 2222));
						}
					}
				}
				if((noted && Bank.getWithdrawMode() == BankMode.NOTE) || 
						(!noted && Bank.getWithdrawMode() == BankMode.ITEM))
				{
					
					if(Bank.withdrawAll(itemID))
					{
						MethodProvider.sleepUntil(() -> Bank.count(itemID) < count, Sleep.calculate(2222, 2222));
					}
				}
			} else Sleep.sleep(666, 666);
		}
	}
	
	/**
	 * Checks for Inventory and Equipment fulfilled. If strict, returns false if anything else in equipment or inventory.
	 */
	public static boolean fulfilled(boolean strict)
	{
		return fulfilledEquipment(strict) && fulfilledInventory(strict);
	}
	public static boolean fulfilledInventory(boolean strict)
	{
		if(inventoryList.isEmpty())
		{
			if(!strict) return true; //nothing to check against, dont care, true
			else if(Inventory.isEmpty()) return true; //if strict, and nothing to check against, and empty invy, true
			else return false; //if strict and stuff in inv, false
		}
		
		if(strict)
		{
			//create list of inventoryList and optional IDs to check all items in invy against
			List<Integer> validIDs = new ArrayList<Integer>();
			List<Integer> notOKItems = new ArrayList<Integer>();
			for(Entry<Integer, InventoryItem> entry : inventoryList.entrySet())
			{
				final int validID = entry.getValue().noted ? new Item(entry.getKey(),1).getNotedItemID() : entry.getKey();
				validIDs.add(validID);
			}
			if(!optionalItems.isEmpty())
			{
				for(int optionalItemID : optionalItems)
				{
					validIDs.add(optionalItemID);
				}
			}
			
			for(Item i : Inventory.all())
			{
				if(i == null) continue;
				else 
				{
					boolean ok = false;
					for(int validID : validIDs)
					{
						if(validID == i.getID())
						{
							ok = true;
						}
					}
					if(!ok) notOKItems.add(i.getID());
				}
			}
			if(!notOKItems.isEmpty()) //have some not OK items in strict mode, false
			{
				return false;
			}
		}
		//here no more extra items, or not strict mode
		for(Entry<Integer,InventoryItem> listedItem : inventoryList.entrySet())
		{
			Item itemRef = listedItem.getValue().itemRef;
			int itemID = listedItem.getValue().noted ? itemRef.getNotedItemID() : listedItem.getKey();
			int min = listedItem.getValue().minQty;
			int max = listedItem.getValue().maxQty;
			int count = Inventory.count(itemID);
			if(count >= min && count <= max) continue;
			else return false;
		}
		return false;
	}
	public static boolean fulfilledEquipment(boolean strict)
	{
		if(strict)
		{
			//here we gather all potential OK items (specified in equipmentMap / optionalItems) to check against
			List<Integer> validIDs = new ArrayList<Integer>();
			List<Integer> notOKItems = new ArrayList<Integer>();
			for(Entry<EquipmentSlot,Integer> equipEntry : equipmentMap.entrySet())
			{
				final int equipID = equipEntry.getValue();
				if(equipID == wealth)
				{
					validIDs.add(wealth1); validIDs.add(wealth2); validIDs.add(wealth3);
					validIDs.add(wealth4); validIDs.add(wealth5);
				}
				else if(equipID == glory)
				{
					validIDs.add(glory1); validIDs.add(glory2); validIDs.add(glory3);
					validIDs.add(glory4); validIDs.add(glory5); validIDs.add(glory6);
				}
				else if(equipID == skills)
				{
					validIDs.add(skills1); validIDs.add(skills2); validIDs.add(skills3);
					validIDs.add(skills4); validIDs.add(skills5); validIDs.add(skills6);
				}
				else if(equipID == duel)
				{
					validIDs.add(duel1); validIDs.add(duel2); validIDs.add(duel3);
					validIDs.add(duel4); validIDs.add(duel5); validIDs.add(duel6);
					validIDs.add(duel7); validIDs.add(duel8);
				}
				else if(equipID == games)
				{
					validIDs.add(games1); validIDs.add(games2); validIDs.add(games3);
					validIDs.add(games4); validIDs.add(games5); validIDs.add(games6);
					validIDs.add(games7); validIDs.add(games8);
				}
				else if(equipID == passage)
				{
					validIDs.add(passage1); validIDs.add(passage2); validIDs.add(passage3);
					validIDs.add(passage4); validIDs.add(passage5);
				} 
				else if(equipID == combat)
				{
					validIDs.add(combat1); validIDs.add(combat2); validIDs.add(combat3);
					validIDs.add(combat4); validIDs.add(combat5); validIDs.add(combat6);
				} 
				else if(equipID == jewelry)
				{
					for(Entry<EquipmentSlot,Integer> jewelryEntry : allJewelry.entrySet()) 
					{
						validIDs.add(jewelryEntry.getValue());
					}
				}
				validIDs.add(equipID);
			}
			if(!optionalItems.isEmpty())
			{
				for(int optionalItemOK : optionalItems)
				{
					validIDs.add(optionalItemOK);
				}
			}
			boolean exit = false;
			for(Item i : Equipment.all())
			{
				if(exit) break;
				if(i == null) continue;
				else 
				{
					boolean ok = false;
					for(int validID : validIDs)
					{
						if(validID == i.getID()) ok = true;
					}
					if(!ok) notOKItems.add(i.getID());
				}
			}
			
			if(!notOKItems.isEmpty())
			{
				return false;
			}
		}
		//done checking strict mode equipment items - nothing extra
		Map<EquipmentSlot, Integer> missingItems = new LinkedHashMap<EquipmentSlot, Integer>();
		for(Entry<EquipmentSlot, Integer> slot : equipmentMap.entrySet())
		{
			if(slot.getValue() == 0) continue;
			Item item = Equipment.getItemInSlot(slot.getKey());
			if(item == null) 
			{
				missingItems.put(slot.getKey(),slot.getValue());
				continue;
			}
			int equipID = item.getID();
			int listItemID = slot.getValue();
			switch(listItemID)
			{
			case(-2):
			{
				if(slot.getKey() != EquipmentSlot.RING) continue;
				if(wearableWealth.contains(equipID)) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-3):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(wearableGlory.contains(equipID)) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-4):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(wearableSkills.contains(equipID)) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-5):
			{
				if(slot.getKey() != EquipmentSlot.RING) continue;
				if(wearableDuel.contains(equipID)) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-6):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(wearableGames.contains(equipID)) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}
			case(-7):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(wearablePassages.contains(equipID)) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}
			case(-8):
			{
				if(slot.getKey() != EquipmentSlot.HANDS) continue;
				if(wearableCombats.contains(equipID)) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}
			default:
			{
				if(equipID == listItemID) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}
			}
		}
		//nothing missing from required equipment, so all good
		if(missingItems.isEmpty())
		{
			MethodProvider.log("Have fulfilled equipment! (strict: "+strict+")");
			return true;
		}
		//otherwise not good
		MethodProvider.log("Not fulfilled equipment! (strict: "+strict+")");
		return false;
	}
	/**
	 * Buys a thing at the GE for ever increasing price until we have total qty in bank + invy
	 */
	public static void buyItem(int itemID, int qty, long timeout)
	{
		
		if(qty <= 0) 
		{
			MethodProvider.log("Invoked Buy function with qty of 0! Returning ~~"+new Item(itemID,1).getName()+"~~");
			Sleep.sleep(69, 69);
			return;
		}
		final int initItemCount = Bank.count(itemID) + Inventory.count(itemID) + Inventory.count(new Item(itemID,1).getNotedItemID());
		MethodProvider.log("Starting buy function for item: " + new Item(itemID,1).getName());
		Timer timer = new Timer(timeout);
		double priceIncrease = 1;
		collectBank = false;
		boolean justCollected = false;
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 69);
			if(!InvEquip.checkedBank())
			{
				continue;
			}
			if(Bank.isOpen()) Bank.count(coins); //random API call to update bank cache ...
			final int totalItemCount = Bank.count(itemID) + Inventory.count(itemID);
			if(totalItemCount >= (initItemCount + qty)) 
			{
				if(GrandExchange.isOpen()) GrandExchange.close();
				else return;
				continue;
			}
			if(Bank.contains(coins) || Inventory.emptySlotCount() <= 0 || 
					(justCollected && !collectBank) ||
					Inventory.count(new Item(itemID,1).getNotedItemID()) > 0)
			{
				if(Bank.isOpen())
				{
					if(Inventory.count(new Item(itemID,1).getNotedItemID()) > 0)
					{
						if(Bank.depositAll(new Item(itemID,1).getNotedItemID())) Sleep.sleep(696, 420);
						continue;
					}
					if(Bank.getWithdrawMode() == BankMode.ITEM)
					{
						if(Inventory.emptySlotCount() <= 0)
						{
							depositExtraJunk();
							MethodProvider.sleep(Timing.sleepLogNormalInteraction());
							continue;
						}
						justCollected = false;
						
						if(Bank.withdrawAll(coins))
						{
							MethodProvider.sleepUntil(() -> !Bank.contains(coins), Sleep.calculate(2222, 2222));
						}
						continue;
					}
					else
					{
						Bank.setWithdrawMode(BankMode.ITEM);
					}
					continue;
				}
				if(!closeBankEquipment()) continue;
				if(GrandExchange.isOpen() && GrandExchange.close()) 
				{
					Sleep.sleep(696, 420);
					continue;
				}
				if(BankLocation.GRAND_EXCHANGE.distance(Players.localPlayer().getTile()) < 50 && 
						Bank.open(BankLocation.GRAND_EXCHANGE) )
				{
					Sleep.sleep(420, 696);
					continue;
				}
				Walkz.goToGE(timer.remaining());
				continue;
			}
			if(!GrandExchange.isOpen())
			{
				if(Bank.isOpen()) 
				{
					Bank.close();
					continue;
				}
				if(Walkz.goToGE(timer.remaining()))
				{
					
					if(GrandExchange.open())
					{
						MethodProvider.sleepUntil(() -> GrandExchange.isOpen(), Sleep.calculate(2222, 2222));
					}
				}
				continue;
			}
			else
			{
				if(GrandExchange.isReadyToCollect())
				{
					if(collectBank)
					{
						if(GrandExchange.collectToBank())
						{
							justCollected = true;
							MethodProvider.sleepUntil(() -> !GrandExchange.isReadyToCollect(), Sleep.calculate(2222, 2222));
						}
					}
					else if(GrandExchange.collect())
					{
						justCollected = true;
						MethodProvider.sleepUntil(() -> collectBank || !GrandExchange.isReadyToCollect(), Sleep.calculate(2222, 2222));
					}
					continue;
				}
				boolean wait = false;
				//check existing offers for item
				for(int i = 0; i< 8; i++)
				{
					if(GrandExchange.slotContainsItem(i))
					{
						wait = true;
						if(GrandExchange.cancelAll())
						{
							MethodProvider.sleepUntil(() -> GrandExchange.isReadyToCollect(), Sleep.calculate(2222, 2222));
						}
						break;
					}
				}
				if(wait) 
				{
					Sleep.sleep(420, 1111);
					continue;
				}
				
				int pricePer = (int)(priceIncrease * ((LivePrices.getHigh(itemID) * 1.25) + (LivePrices.getHigh(itemID) * (Calculations.random(25.0, 50.0) / 100))));
				int totalPrice = qty * pricePer;
				if(Inventory.count(coins) < totalPrice)
				{
					MethodProvider.log("Account needs more GP to buy item: " + new Item(itemID,1).getName() +" in qty: " + qty+" at total price: " + totalPrice+" gp, stopping script...");
					ScriptManager.getScriptManager().stop();
					return;
				}
				if(GrandExchange.buyItem(itemID, qty, pricePer))
				{
					MethodProvider.sleepUntil(() -> GrandExchange.isReadyToCollect(), Sleep.calculate(16666, 5555));
					double tmp = priceIncrease + 0.2;
					priceIncrease = tmp;
					Sleep.sleep(111, 111);
					continue;
				}
			}
		}
		
	}
	
	/**
	 * fulfills equipment setup within a timed limit (in milliseconds). 
	 * Strict mode is to deposit everything else except what is in setup, otherwise OK to have random junk in addition to specified items.
	 * Checks inventory if have items and equips it.
	 * Goes to bank, opens bank, and withdraws equipment items.
	 * if any items are found to not be in bank, checks GE.
	 * buys any items in GE for 25-50% over price
	 * @param timerLimit
	 * @return
	 */
	public static boolean fulfillSetup(boolean strict, long timerLimit)
	{
		Timer timeout = new Timer(timerLimit);
		boolean equippedItems = false;
		do
		{
			if(Bank.isOpen()) Bank.count(coins); //random API call to update bank cache ...
			MethodProvider.log("Starting fulfill function");
			Sleep.sleep(69, 69);
			if(!equipmentMap.isEmpty())
			{
				if(strict)
				{
					//here we gather all potential OK items (specified in equipmentMap / optionalItems) to check against
					List<Integer> validIDs = new ArrayList<Integer>();
					List<Integer> notOKItems = new ArrayList<Integer>();
					for(Entry<EquipmentSlot,Integer> equipEntry : equipmentMap.entrySet())
					{
						final int equipID = equipEntry.getValue();
						if(equipID == wealth)
						{
							validIDs.add(wealth1); validIDs.add(wealth2); validIDs.add(wealth3);
							validIDs.add(wealth4); validIDs.add(wealth5);
						}
					
						else if(equipID == glory)
						{
							validIDs.add(glory1); validIDs.add(glory2); validIDs.add(glory3);
							validIDs.add(glory4); validIDs.add(glory5); validIDs.add(glory6);
						}
						else if(equipID == skills)
						{
							validIDs.add(skills1); validIDs.add(skills2); validIDs.add(skills3);
							validIDs.add(skills4); validIDs.add(skills5); validIDs.add(skills6);
						}
						else if(equipID == duel)
						{
							validIDs.add(duel1); validIDs.add(duel2); validIDs.add(duel3);
							validIDs.add(duel4); validIDs.add(duel5); validIDs.add(duel6);
							validIDs.add(duel7); validIDs.add(duel8);
						}
						else if(equipID == games)
						{
							validIDs.add(games1); validIDs.add(games2); validIDs.add(games3);
							validIDs.add(games4); validIDs.add(games5); validIDs.add(games6);
							validIDs.add(games7); validIDs.add(games8);
						}
						else if(equipID == passage)
						{
							validIDs.add(passage1); validIDs.add(passage2); validIDs.add(passage3);
							validIDs.add(passage4); validIDs.add(passage5);
						} 
						else if(equipID == combat)
						{
							validIDs.add(combat1); validIDs.add(combat2); validIDs.add(combat3);
							validIDs.add(combat4); validIDs.add(combat5); validIDs.add(combat6);
						} 
						else if(equipID == jewelry)
						{
							for(Entry<EquipmentSlot,Integer> jewelryEntry : allJewelry.entrySet()) 
							{
								validIDs.add(jewelryEntry.getValue());
							}
						}
						else validIDs.add(equipID);
					}
					if(!optionalItems.isEmpty())
					{
						for(int optionalItemOK : optionalItems)
						{
							if(optionalItemOK == jewelry)
							{
								for(Entry<EquipmentSlot,Integer> jewelryEntry : allJewelry.entrySet()) 
								{
									validIDs.add(jewelryEntry.getValue());
								}
							} else validIDs.add(optionalItemOK);
						}
					}
					for(Item i : Equipment.all())
					{
						if(i == null) continue;
						else 
						{
							boolean found = false;
							final int equippedID = i.getID();
							for(int validID : validIDs)
							{
								if(validID == equippedID) 
								{
									found = true;
									break;
								}
							}
							if(!found) notOKItems.add(equippedID);
						}
					}
					
					if(!notOKItems.isEmpty())
					{
						MethodProvider.log("Have some extra equipment items! Going to bank equipment menu to directly bank them~~");
						for(int i : notOKItems)
						{
							MethodProvider.log("~~" + new Item(i,1).getName()+"~~");
						}
						if(openBankEquipment())
						{
							for(int i = 76; i <= 86; i++)
							{
								if(Widgets.getWidgetChild(12,i,1) == null || 
										!Widgets.getWidgetChild(12,i,1).isVisible())
								{
									MethodProvider.log("Lost visibility of equipment bank widgets");
									break;
								}
								final int slotID = Widgets.getWidgetChild(12,i,1).getItemId();
								if(slotID == -1) continue;
								for(int notOKItem : notOKItems)
								{
									if(notOKItem == slotID)
									{
										if(Widgets.getWidgetChild(12,i,1).interact("Bank"))
										{
											MethodProvider.log("removing not OK item: " + new Item(notOKItem,1).getName());
											MethodProvider.sleepUntil(() -> !Equipment.contains(slotID), Sleep.calculate(2222, 2222));
										}
										break;
									}
								}
							}
						}
						continue;
					}
				}
				//done checking strict mode equipment items - all OK
				Map<EquipmentSlot, Integer> missingItems = new LinkedHashMap<EquipmentSlot, Integer>();
				for(Entry<EquipmentSlot, Integer> slot : equipmentMap.entrySet())
				{
					if(slot.getValue() == 0) continue;
					Item item = Equipment.getItemInSlot(slot.getKey());
					if(item == null) 
					{
						missingItems.put(slot.getKey(),slot.getValue());
						continue;
					}
					int equipID = item.getID();
					int listItemID = slot.getValue();
					switch(listItemID)
					{
					case(-2):
					{
						if(slot.getKey() != EquipmentSlot.RING) continue;
						if(wearableWealth.contains(equipID)) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-3):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(wearableGlory.contains(equipID)) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-4):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(wearableSkills.contains(equipID)) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-5):
					{
						if(slot.getKey() != EquipmentSlot.RING) continue;
						if(wearableDuel.contains(equipID)) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-6):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(wearableGames.contains(equipID)) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}
					case(-7):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(wearablePassages.contains(equipID)) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}
					case(-8):
					{
						if(slot.getKey() != EquipmentSlot.HANDS) continue;
						if(wearableCombats.contains(equipID)) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}
					default:
					{
						if(equipID == listItemID) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}
					}
				}
				if(missingItems.isEmpty())
				{
					equippedItems = true;
				}
				if(!equippedItems)
				{
					for(Entry<EquipmentSlot,Integer> itemToEquip : missingItems.entrySet())
					{
						int itemID = itemToEquip.getValue();
						EquipmentSlot slot = itemToEquip.getKey();
						
						while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN
								&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
						{
							if(Bank.isOpen()) Bank.count(coins); //random API call to update bank cache ...
							Sleep.sleep(69, 69);
							Item item = Equipment.getItemInSlot(slot);
							
							//if item is equipped, continue to next item
							if(item != null)
							{
								MethodProvider.log("Found item in slot: " + slot.toString()+", " + item.getName());
								int equipID = item.getID();
								boolean breakOrNot = false;
								switch(itemID)
								{
								case(-2):
								{
									if(wearableWealth.contains(equipID)) breakOrNot = true;
									break;
								}case(-3):
								{
									if(wearableGlory.contains(equipID)) breakOrNot = true;
									break;
								}case(-4):
								{
									if(wearableSkills.contains(equipID)) breakOrNot = true;
									break;
								}case(-5):
								{
									if(wearableDuel.contains(equipID)) breakOrNot = true;
									break;
								}case(-6):
								{
									if(wearableGames.contains(equipID)) breakOrNot = true;
									break;
								}
								case(-7):
								{
									if(wearablePassages.contains(equipID)) breakOrNot = true;
									break;
								}
								case(-8):
								{
									if(wearableCombats.contains(equipID)) breakOrNot = true;
									break;
								}
								default:
								{
									if(equipID == itemID) 
									{
										breakOrNot = true;
										break;
									}
									
								}
								}
								if(breakOrNot) break;
							} else MethodProvider.log("NOT Found item in slot: " + slot.toString());
							//not have item in equipment, so handle inventory equipping
							MethodProvider.log("Not equipped: " + new Item(itemID,1).getName());
							boolean continueOrNot = false;
							int invyID = 0;
							switch(itemID)
							{
							case(-2):
							{
								for(int jewelry : wearableWealth)
								{
									if(Inventory.contains(jewelry))
									{
										continueOrNot = true;
										invyID = jewelry;
										break;
									}
								}
								break;
							}case(-3):
							{
								for(int jewelry : wearableGlory)
								{
									if(Inventory.contains(jewelry))
									{
										continueOrNot = true;
										invyID = jewelry;
										break;
									}
								}
								break;
							}case(-4):
							{
								for(int jewelry : wearableSkills)
								{
									if(Inventory.contains(jewelry))
									{
										continueOrNot = true;
										invyID = jewelry;
										break;
									}
								}
								break;
							}case(-5):
							{
								for(int jewelry : wearableDuel)
								{
									if(Inventory.contains(jewelry))
									{
										continueOrNot = true;
										invyID = jewelry;
										break;
									}
								}
								break;
							}case(-6):
							{
								for(int jewelry : wearableGames)
								{
									if(Inventory.contains(jewelry))
									{
										continueOrNot = true;
										invyID = jewelry;
										break;
									}
								}
								break;
							}
							case(-7):
							{
								for(int jewelry : wearablePassages)
								{
									if(Inventory.contains(jewelry))
									{
										continueOrNot = true;
										invyID = jewelry;
										break;
									}
								}
								break;
							}
							case(-8):
							{
								for(int jewelry : wearableCombats)
								{
									if(Inventory.contains(jewelry))
									{
										continueOrNot = true;
										invyID = jewelry;
										break;
									}
								}
								break;
							}
							default:
							{
								if(Inventory.contains(itemID)) 
								{
									continueOrNot = true;
									invyID = itemID;
								}
								break;
							}
							}
							if(continueOrNot)
							{
								if(GrandExchange.isOpen()) 
								{
									if(GrandExchange.close())
									{
										MethodProvider.sleepUntil(() -> !GrandExchange.isOpen(), Sleep.calculate(2222,2222));
									}
									Sleep.sleep(111, 111);
									continue;
								}
								
								if(Bank.isOpen() || Tabs.isOpen(Tab.INVENTORY) || Widgets.getWidgetChild(12, 76) != null &&
										Widgets.getWidgetChild(12, 76).isVisible())
								{
									equipItem(invyID);
								}
								else
								{
									Tabs.open(Tab.INVENTORY);
								}
								Sleep.sleep(69, 69);
								continue;
							}
							
							//check bank first
							if(!checkedBank()) continue;
							
							//check bank for item
							boolean continueOrNot2 = false;
							int bankID = 0;
							switch(itemID)
							{
							case(-2):
							{
								for(int jewelry : wearableWealth)
								{
									if(Bank.contains(jewelry))
									{
										continueOrNot2 = true;
										bankID = jewelry;
										break;
									}
								}
								break;
							}case(-3):
							{
								for(int jewelry : wearableGlory)
								{
									if(Bank.contains(jewelry))
									{
										continueOrNot2 = true;
										bankID = jewelry;
										break;
									}
								}
								break;
							}case(-4):
							{
								for(int jewelry : wearableSkills)
								{
									if(Bank.contains(jewelry))
									{
										continueOrNot2 = true;
										bankID = jewelry;
										break;
									}
								}
								break;
							}case(-5):
							{
								for(int jewelry : wearableDuel)
								{
									if(Bank.contains(jewelry))
									{
										continueOrNot2 = true;
										bankID = jewelry;
										break;
									}
								}
								break;
							}case(-6):
							{
								for(int jewelry : wearableGames)
								{
									if(Bank.contains(jewelry))
									{
										continueOrNot2 = true;
										bankID = jewelry;
										break;
									}
								}
								break;
							}
							case(-7):
							{
								for(int jewelry : wearablePassages)
								{
									if(Bank.contains(jewelry))
									{
										continueOrNot2 = true;
										bankID = jewelry;
										break;
									}
								}
								break;
							}
							case(-8):
							{
								for(int jewelry : wearableCombats)
								{
									if(Bank.contains(jewelry))
									{
										continueOrNot2 = true;
										bankID = jewelry;
										break;
									}
								}
								break;
							}
							default:
							{
								if(Bank.contains(itemID)) 
								{
									continueOrNot2 = true;
									bankID = itemID;
								}
								break;
							}
							}
							if(continueOrNot2)
							{
								if(!closeBankEquipment()) continue;
								if(Bankz.openClosest())
								{
									if(Bank.getWithdrawMode() == BankMode.ITEM)
									{
										if(Inventory.emptySlotCount() < 1)
										{
											depositExtraJunk();
											continue;
										}
										if(Bank.withdraw(bankID, 1))
										{
											final int tmp = bankID;
											MethodProvider.sleepUntil(() -> Inventory.contains(tmp), Sleep.calculate(2222, 2222));
										}
										continue;
									}
									else
									{
										Bank.setWithdrawMode(BankMode.ITEM);
									}
									continue;
								}
								else
								{
									Sleep.sleep(666, 1111);
								}
								continue;
							}
							
							//check GE for item
							
							
							/**
							 * Time to buy stuff on GE, handle teles
							 */
							int itemQty = 0;
							switch(itemID)
							{
							case(-2):
							{
								itemID = wealth5;
								itemQty = 2;
								break;
							}
							case(-3):
							{
								itemID = glory6;
								itemQty = 2;
								break;
							}
							case(-4):
							{
								itemID = skills6;
								itemQty = 1;
								break;
							}
							case(-5):
							{
								itemID = duel8;
								itemQty = 2;
								break;
							}
							case(-6):
							{
								itemID = games8;
								itemQty = 2;
								break;
							}
							case(-7):
							{
								itemID = passage5;
								itemQty = 1;
								break;
							}
							case(-8):
							{
								itemID = combat6;
								itemQty = 1;
								break;
							}
							default: 
							{
								itemQty = 1;
								break;
							}
							}
							
							buyItem(itemID,1,timeout.remaining());
						}
						continue;
					}
				}
				//have all correct items equipped
			}
			//equipmentMap is empty - nothing to equip
			else
			{
				if(strict)
				{
					if(!Equipment.isEmpty())
					{
						List<Integer> validIDs = new ArrayList<>();
						List<Integer> notOKItems = new ArrayList<>();
						if(!optionalItems.isEmpty())
						{
							for(int optionalItemOK : optionalItems)
							{
								if(optionalItemOK == jewelry)
								{
									for(Entry<EquipmentSlot,Integer> jewelryEntry : allJewelry.entrySet()) 
									{
										validIDs.add(jewelryEntry.getValue());
									}
								} else validIDs.add(optionalItemOK);
							}
						}
						for(Item i : Equipment.all())
						{
							if(i == null) continue;
							else 
							{
								boolean found = false;
								final int equippedID = i.getID();
								for(int validID : validIDs)
								{
									if(validID == equippedID) 
									{
										found = true;
										break;
									}
								}
								if(!found) notOKItems.add(equippedID);
							}
						}
						if(!notOKItems.isEmpty())
						{
							if(!closeBankEquipment()) continue;
							if(Bankz.openClosest())
							{
								if(Bank.depositAllEquipment())
								{
									MethodProvider.sleepUntil(() -> Equipment.isEmpty(), Sleep.calculate(2222, 2222));
								}
							} else {
								Sleep.sleep(666, 1111);
							}
							continue;
						}
					}
				}
			}
			if(!closeBankEquipment()) continue;
			//have all items equipped - now check inventory
			if(inventoryList.isEmpty())
			{
				if(!strict) return true;
				else if(Inventory.isEmpty()) return true;
				else
				{
					List<Integer> validIDs = new ArrayList<>();
					List<Integer> notOKItems = new ArrayList<>();
					if(!optionalItems.isEmpty())
					{
						for(int optionalItemOK : optionalItems)
						{
							if(optionalItemOK == jewelry)
							{
								for(Entry<EquipmentSlot,Integer> jewelryEntry : allJewelry.entrySet()) 
								{
									validIDs.add(jewelryEntry.getValue());
								}
							} else validIDs.add(optionalItemOK);
						}
					}
					for(Item i : Equipment.all())
					{
						if(i == null) continue;
						else 
						{
							boolean found = false;
							final int equippedID = i.getID();
							for(int validID : validIDs)
							{
								if(validID == equippedID) 
								{
									found = true;
									break;
								}
							}
							if(!found) notOKItems.add(equippedID);
						}
					}
					if(!notOKItems.isEmpty())
					{
						if(!closeBankEquipment()) continue;
						if(Bankz.openClosest())
						{
							if(Bank.depositAllItems()) MethodProvider.sleepUntil(Inventory::isEmpty, Sleep.calculate(2222, 2222));
						}
						else Sleep.sleep(666, 696);
						continue;
					}
					
				}
			}
			
			if(strict)
			{
				//create list of inventoryList and optional IDs to check all items in invy against
				List<Integer> validIDs = new ArrayList<Integer>();
				List<Integer> notOKItems = new ArrayList<Integer>();
				
				for(Entry<Integer, InventoryItem> entry : inventoryList.entrySet())
				{
					List<Integer> possibleJewelry = new ArrayList<Integer>();
					if(entry.getKey() == wealth) possibleJewelry.addAll(wearableWealth);
					if(entry.getKey() == glory) possibleJewelry.addAll(wearableGlory);
					if(entry.getKey() == games) possibleJewelry.addAll(wearableGames);
					if(entry.getKey() == passage) possibleJewelry.addAll(wearablePassages);
					if(entry.getKey() == duel) possibleJewelry.addAll(wearableDuel);
					if(entry.getKey() == combat) possibleJewelry.addAll(wearableCombats);
					if(entry.getKey() == skills) possibleJewelry.addAll(wearableSkills);
					if(possibleJewelry != null && !possibleJewelry.isEmpty())
					{
						for(int i : possibleJewelry)
						{
							validIDs.add(i);
						}
						continue;
					}
					final int validID = (entry.getValue().noted ? new Item(entry.getKey(),1).getNotedItemID() : entry.getKey());
					validIDs.add(validID);
				}
				if(!optionalItems.isEmpty())
				{
					for(int optionalItemID : optionalItems)
					{
						validIDs.add(optionalItemID);
					}
				}
				
				for(Item i : Inventory.all())
				{
					if(i == null) continue;
					else 
					{
						boolean ok = false;
						for(int validID : validIDs)
						{
							if(validID == i.getID())
							{
								ok = true;
							}
						}
						if(!ok) notOKItems.add(i.getID());
					}
				}
				if(!notOKItems.isEmpty())
				{
					MethodProvider.log("Have some extra items in inventory!~~ ");
					for(int i : notOKItems)
					{
						MethodProvider.log("~~"+new Item(i,1).getName()+"~~");
					}
					if(!closeBankEquipment()) continue;
					if(Bankz.openClosest())
					{
						for(int depositItem : notOKItems)
						{
							if(Inventory.count(depositItem) <= 0) continue;
							if(Bank.depositAll(depositItem))
							{
								MethodProvider.sleepUntil(() -> Inventory.count(depositItem) <= 0, Sleep.calculate(2222, 2222));
							}
							Sleep.sleep(69,696);
						}
					} else Sleep.sleep(696, 1111);
					continue;
				}
			}
			
			//here all extra items have been deposited or not strict mode
			Map<Integer,InventoryItem> missingInvyItems = new LinkedHashMap<Integer,InventoryItem>();
			
			for(Entry<Integer,InventoryItem> listedItem : inventoryList.entrySet())
			{
				List<Integer> possibleJewelry = new ArrayList<Integer>();
				if(listedItem.getKey() == wealth) possibleJewelry.addAll(wearableWealth);
				if(listedItem.getKey() == glory) possibleJewelry.addAll(wearableGlory);
				if(listedItem.getKey() == games) possibleJewelry.addAll(wearableGames);
				if(listedItem.getKey() == passage) possibleJewelry.addAll(wearablePassages);
				if(listedItem.getKey() == duel) possibleJewelry.addAll(wearableDuel);
				if(listedItem.getKey() == combat) possibleJewelry.addAll(wearableCombats);
				if(listedItem.getKey() == skills) possibleJewelry.addAll(wearableSkills);
				if(possibleJewelry != null && !possibleJewelry.isEmpty())
				{
					boolean shouldBreak = false;
					for(int i : possibleJewelry)
					{
						int itemID = i;
						int min = listedItem.getValue().minQty;
						int max = listedItem.getValue().maxQty;
						int count = Inventory.count(itemID);
						if(count >= min)
						{
							if(count <= max)
							{
								shouldBreak = true;
								break;
							}
						}
					}
					if(shouldBreak) continue;
					List<Integer> reversed = possibleJewelry;
					Collections.reverse(reversed);
					MethodProvider.log("Missing jewelry: "+new Item(reversed.get(0),1).getName());
					missingInvyItems.put(reversed.get(0),new InventoryItem(reversed.get(0),listedItem.getValue().minQty, listedItem.getValue().maxQty, false, listedItem.getValue().refillQty));
					continue;
				}
				Item itemRef = listedItem.getValue().itemRef;
				int itemID = listedItem.getValue().noted ? itemRef.getNotedItemID() : listedItem.getKey();
				int min = listedItem.getValue().minQty;
				int max = listedItem.getValue().maxQty;
				int count = Inventory.count(itemID);
				if(count >= min)
				{
					if(count <= max)
					{
						continue;
					}
				}
				MethodProvider.log("Missing inventory item: " + itemRef.getName()+", have " + count+" and need between " + min +" - " + max+"!");
				missingInvyItems.put(itemID,listedItem.getValue());
			}
			
			if(missingInvyItems.isEmpty()) return true;
			for(Entry<Integer,InventoryItem> itemToGet : missingInvyItems.entrySet())
			{
				Item itemRef = itemToGet.getValue().itemRef;
				String itemName = itemRef.getName();
				boolean noted = itemToGet.getValue().noted;
				int unnotedID = itemRef.getID();
				int notedID = itemRef.getNotedItemID();
				int requestedID = noted ? notedID : unnotedID;
				int minQty = itemToGet.getValue().minQty;
				int maxQty = itemToGet.getValue().maxQty;
				int refillQty = itemToGet.getValue().refillQty;
				
				while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN
						&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
				{
					Sleep.sleep(69, 69);
					
					//if item is in inventory in correct qty and form, continue to next item
					Item item = Inventory.get(requestedID);
					int invCount = Inventory.count(requestedID);
					if(item == null || invCount <= 0)
					{
						if(minQty <= 0) break;
					}
					else 
					{
						if(invCount >= minQty && invCount <= maxQty) break;
					}
					
					int swapDepositID = -1;
					//have noted but need unnoted
					if(noted && Inventory.contains(unnotedID))
					{
						swapDepositID = unnotedID;
					}
					
					//have unnoted but need noted
					if(!noted && Inventory.contains(notedID))
					{
						swapDepositID = notedID;
					}
					
					BankMode correctMode = null;
					if(noted) correctMode = BankMode.NOTE;
					else correctMode = BankMode.ITEM;
					//check bank
					if(!checkedBank()) continue;
					
					//need to deposit if maxQty set to 0
					if(maxQty <= 0)
					{
						if(!closeBankEquipment()) continue;
						if(Bankz.openClosest())
						{
							if(Bank.depositAll(requestedID))
							{
								final int tmp = requestedID;
								MethodProvider.sleepUntil(() -> !Inventory.contains(tmp),Sleep.calculate(2222, 2222));
							}
						}
						else Sleep.sleep(666, 1111);
						continue;
					}
					
					//need to swap noted for items or vice versa
					if(swapDepositID != -1)
					{
						MethodProvider.log("Have something to swap noted <--> item: " + swapDepositID);
						if(!closeBankEquipment()) continue;
						if(Bankz.openClosest())
						{
							if(Bank.depositAll(swapDepositID))
							{
								final int tmp = swapDepositID;
								MethodProvider.sleepUntil(() -> !Inventory.contains(tmp),Sleep.calculate(2222, 2222));
							}
						}
						else Sleep.sleep(666, 1111);
						continue;
					}
					int bankCount = Bank.count(unnotedID);
					MethodProvider.log("requested / unnoted / noted IDs: " + requestedID + " / " + unnotedID+ " / " + notedID +"~~~ noted: "+ noted);
					int tooMuch = invCount - maxQty;
					//check bank for item
					if(bankCount > 0 || tooMuch > 0) 
					{
						if(!closeBankEquipment()) continue;
						if(Bankz.openClosest())
						{
							//have too much in inventory (over max)
							if(tooMuch > 0)
							{
								MethodProvider.log("Depositing some items extra: " + itemName);
								if(Bank.deposit(requestedID,tooMuch))
								{
									final int tmp = requestedID;
									MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
								}
								continue;
							}
							final int neededForMax = maxQty - invCount;
							if(noted || itemRef.isStackable())
							{
								//bank contains all needed for max qty - withdraw max qty
								if(bankCount >= neededForMax)
								{
									if(Bank.getWithdrawMode() != correctMode) Bank.setWithdrawMode(correctMode);
									else  
									{
										if(Inventory.emptySlotCount() < 1)
										{
											depositExtraJunk();
											continue;
										}
										if(Bank.withdraw(unnotedID,neededForMax))
										{
											final int tmp = requestedID;
											MethodProvider.sleepUntil(() -> (Inventory.count(tmp) == maxQty || Inventory.isFull()), Sleep.calculate(2222, 2222));
										}
									}
									continue;
								}
								
								//bank contains some but not all for max qty - withdraw all
								if(bankCount > 0)
								{
									if(Bank.getWithdrawMode() != correctMode) Bank.setWithdrawMode(correctMode);
									else {
										if(Inventory.emptySlotCount() < 1)
										{
											depositExtraJunk();
											MethodProvider.sleep(Timing.sleepLogNormalInteraction());
											continue;
										}
										if(Bank.withdrawAll(unnotedID))
										{
											final int tmp = unnotedID;
											MethodProvider.sleepUntil(() -> Bank.count(tmp) <= 0, Sleep.calculate(2222, 2222));
										}
									}
								}
								continue;
							}
							//here we must withdraw an item which is not stackable like sharks
							if(bankCount >= neededForMax)
							{
								if(Bank.getWithdrawMode() != correctMode) Bank.setWithdrawMode(correctMode);
								else 
								{
									if(Inventory.emptySlotCount() < 1)
									{
										depositExtraJunk();
										MethodProvider.sleep(Timing.sleepLogNormalInteraction());
										continue;
									}
									if(Bank.withdraw(unnotedID,neededForMax))
									{
										final int tmp = requestedID;
										MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
									}
								}
									
								continue;
							}
							if(Bank.getWithdrawMode() != correctMode) Bank.setWithdrawMode(correctMode);
							else
							{
								if(Inventory.emptySlotCount() < 1)
								{
									depositExtraJunk();
									MethodProvider.sleep(Timing.sleepLogNormalInteraction());
									continue;
								}
								if(Bank.withdrawAll(unnotedID))
								{
									final int tmp = unnotedID;
									MethodProvider.sleepUntil(() -> Bank.count(tmp) <= 0, Sleep.calculate(2222, 2222));
								}
							}
							continue;
						}
						else
						{
							Sleep.sleep(666, 1111);
						}
						continue;
					}
					
					//check GE for item
					MethodProvider.log("Buying item at GE! ~~" + itemRef.getName()+"~~");
					buyItem(unnotedID, refillQty, timeout.remaining());
				}
			}
		}
		while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused());
		MethodProvider.log("Ending fulfill function");
		return false;
	}
	
	public static boolean equipItem(int ID)
	{
		MethodProvider.log("Equipping item: " + new Item(ID,1).getName());
		if(Equipment.contains(ID)) return true;
		
		if(Tabs.isOpen(Tab.INVENTORY) || Bank.isOpen() ||
				(Widgets.getWidgetChild(12, 76) != null && Widgets.getWidgetChild(12, 76).isVisible()))
		{
			Item wearItem = Inventory.get(ID);
			if(wearItem == null) return false;
			String action = null;
			for(String act : wearItem.getActions())
			{
				if(act == null) continue;
				if(act.equals("Wield")) 
				{
					action = "Wield";
					break;
				}
				else if(act.equals("Wear")) 
				{
					action = "Wear";
					break;
				}
			}
			final int tmp = ID;
			if(Inventory.interact(ID, action))
			{
				MethodProvider.sleepUntil(() -> {
					if(Combat.shouldEatFood(8)) Combat.eatFood();
					return Equipment.contains(tmp);
				}, Sleep.calculate(2222, 2222));
			}
			if(Equipment.contains(ID)) return true;
			return false;
		}
		else
		{
			if(Shop.isOpen()) Shop.close();
			else if(Bank.isOpen()) Bank.close();
			else if(GrandExchange.isOpen()) GrandExchange.close();
			else if(DepositBox.isOpen()) DepositBox.close();
			else Tabs.open(Tab.INVENTORY);
		}
		return false;
	}
	public static boolean equipItem(String itemName)
	{
		if(Equipment.contains(itemName)) return true;
		if(Tabs.isOpen(Tab.INVENTORY) || Bank.isOpen() ||
				(Widgets.getWidgetChild(12, 76) != null && Widgets.getWidgetChild(12, 76).isVisible()))
		{
			Item wearItem = Inventory.get(itemName);
			if(wearItem == null) return false;
			String action = null;
			for(String act : wearItem.getActions())
			{
				if(act == null) continue;
				if(act.equals("Wield")) 
				{
					action = "Wield";
					break;
				}
				else if(act.equals("Wear")) 
				{
					action = "Wear";
					break;
				}
			}
			final String tmp = itemName;
			if(Inventory.interact(itemName, action))
			{
				MethodProvider.sleepUntil(() -> {
					if(Combat.shouldEatFood(8)) Combat.eatFood();
					return Equipment.contains(tmp);
				}, Sleep.calculate(2222, 2222));
			}
			if(Equipment.contains(itemName)) return true;
			return false;
		}
		else
		{
			if(Widgets.isOpen())
			{
				Widgets.closeAll();
				Sleep.sleep(111, 111);
			}
			else Tabs.open(Tab.INVENTORY);
		}
		return false;
	}
	/**
	 * returns false if Equipment can be closed, true if equipment bank tab closed already
	 * @return
	 */
	public static boolean closeBankEquipment()
	{
		if(Widgets.getWidgetChild(12, 76) != null &&
				Widgets.getWidgetChild(12, 76).isVisible())
		{
			if(Widgets.getWidgetChild(12, 113).interact("Hide worn items"))
			{
				MethodProvider.sleepUntil(Bank::isOpen, Sleep.calculate(2222, 2222));
			}
			if(Bank.isOpen())
			{
				Bank.count(coins); //random API call to update bank cache ...
				return true;
			}
			return false;
		} else return true;
	}
	/**
	 * returns false if Equipment can be opened, true if equipment bank tab open already
	 * @return
	 */
	public static boolean openBankEquipment()
	{
		if(Widgets.getWidgetChild(12, 76) != null &&
				Widgets.getWidgetChild(12, 76).isVisible())
		{
			return true;
		}
		
		if(Bank.isOpen())
		{
			Bank.count(coins); //random API call to update bank cache ...
			if(Widgets.getWidgetChild(12, 113).interact("Show worn items"))
			{
				MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(12, 76) != null &&
						Widgets.getWidgetChild(12, 76).isVisible(), Sleep.calculate(2222, 2222));
			}
			if(Widgets.getWidgetChild(12, 76) != null &&
					Widgets.getWidgetChild(12, 76).isVisible())
			{
				return true;
			}
			return false;
		} 
		
		else if(GrandExchange.isOpen()) GrandExchange.close();
		else if(Bankz.openClosest()) Sleep.sleep(666, 666);
		
		return false;
	}
	public static int coins = 995;
	
	public static void initializeIntLists ()
	{
		wearablePassages.add(passage1);wearablePassages.add(passage2);
		wearablePassages.add(passage3);wearablePassages.add(passage4);wearablePassages.add(passage5);
		
		wearableGames.add(games1);wearableGames.add(games2);wearableGames.add(games3);wearableGames.add(games4);
		wearableGames.add(games5);wearableGames.add(games6);wearableGames.add(games7);wearableGames.add(games8);
		
		wearableDuel.add(duel1);wearableDuel.add(duel2);wearableDuel.add(duel3);wearableDuel.add(duel4);
		wearableDuel.add(duel5);wearableDuel.add(duel6);wearableDuel.add(duel7);wearableDuel.add(duel8);
		
		wearableSkills.add(skills1);wearableSkills.add(skills2);wearableSkills.add(skills3);
		wearableSkills.add(skills4);wearableSkills.add(skills5);wearableSkills.add(skills6);

		wearableGlory.add(glory1);wearableGlory.add(glory2);wearableGlory.add(glory3);
		wearableGlory.add(glory4);wearableGlory.add(glory5);wearableGlory.add(glory6);
		
		wearableWealth.add(wealth1);wearableWealth.add(wealth2);
		wearableWealth.add(wealth3);wearableWealth.add(wealth4);wearableWealth.add(wealth5);
		
		wearableCombats.add(combat1);wearableCombats.add(combat2);wearableCombats.add(combat3);
		wearableCombats.add(combat4);wearableCombats.add(combat5);wearableCombats.add(combat6);
		
		for(int jewelry : wearablePassages)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableGames)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableSkills)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableGlory)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableCombats)
		{
			allJewelry.put(EquipmentSlot.HANDS, jewelry);
		}
		for(int jewelry : wearableDuel)
		{
			allJewelry.put(EquipmentSlot.RING, jewelry);
		}
		for(int jewelry : wearableWealth)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		allJewelryIDs.addAll(wearableWealth);
		allJewelryIDs.addAll(wearablePassages);
		allJewelryIDs.addAll(wearableGames);
		allJewelryIDs.addAll(wearableSkills);
		allJewelryIDs.addAll(wearableGlory);
		allJewelryIDs.addAll(wearableCombats);
		allJewelryIDs.addAll(wearableDuel);
		staminas.add(stamina1);staminas.add(stamina2);staminas.add(stamina3);staminas.add(stamina4);
		antidotes.add(antidote1);antidotes.add(antidote2);antidotes.add(antidote3);antidotes.add(antidote4);
	}
	public static final int iceCooler = 6696;
	public static final int shantayPass = 1854;
	public static final int waterskin0 = 1831;
	public static final int waterskin1 = 1829;
	public static final int waterskin2 = 1827;
	public static final int waterskin3 = 1825;
	public static final int waterskin4 = 1823;
	public static int getWaterskinCharges()
	{
		int count = 0;
		if(Inventory.count(waterskin1) > 0) 
		{
			int tmp = ((Inventory.count(waterskin1) * 1) + count);
			count = tmp;
		}
		if(Inventory.count(waterskin2) > 0) 
		{
			int tmp = ((Inventory.count(waterskin2) * 2) + count);
			count = tmp;
		}
		if(Inventory.count(waterskin3) > 0) 
		{
			int tmp = ((Inventory.count(waterskin3) * 3) + count);
			count = tmp;
		}
		if(Inventory.count(waterskin4) > 0) 
		{
			int tmp = ((Inventory.count(waterskin4) * 4) + count);
			count = tmp;
		}
		MethodProvider.log("Waterskin total charges in invy: " + count);
		return count;
	}
	public static final int antidote1 = 5958;
	public static final int antidote2 = 5956;
	public static final int antidote3 = 5954;
	public static final int antidote4 = 5952;
	public static List<Integer> antidotes = new ArrayList<Integer>();
	public static List<Integer> allJewelryIDs = new ArrayList<Integer>();
	public static final int stamina4 = 12625;
	public static final int stamina3 = 12627;
	public static final int stamina2 = 12629;
	public static final int stamina1 = 12631;
	public static List<Integer> staminas = new ArrayList<Integer>();
	
	public static int jewelry = -10;

	public static Map<EquipmentSlot,Integer> allJewelry = new LinkedHashMap<EquipmentSlot,Integer>();
	//-8 represents the value of any charge of combat bracelet
	public static int combat = -8; 
	public static List<Integer> wearableCombats = new ArrayList<Integer>();
	public static int combat1 = 11124;
	public static int combat2 = 11122;
	public static int combat3 = 11120;
	public static int combat4 = 11118;
	public static int combat5 = 11974;
	public static int combat6 = 11972;
		
	//-7 represents the value of any charge of necklace of glory
	public static int passage = -7; 
	public static List<Integer> wearablePassages = new ArrayList<Integer>();
	public static int passage1 = 21155;
	public static int passage2 = 21153;
	public static int passage3 = 21151;
	public static int passage4 = 21149;
	public static int passage5 = 21146;
	
	//-6 represents the value of any charge of games necklace
	public static int games = -6; 
	public static List<Integer> wearableGames = new ArrayList<Integer>();
	public static int games1 = 3867;
	public static int games2 = 3865;
	public static int games3 = 3863;
	public static int games4 = 3861;
	public static int games5 = 3859;
	public static int games6 = 3857;
	public static int games7 = 3855;
	public static int games8 = 3853;
	
	//-5 represents the value of any charge of dueling ring
	public static int duel = -5; 
	public static List<Integer> wearableDuel = new ArrayList<Integer>();
	public static int duel1 = 2566;
	public static int duel2 = 2564;
	public static int duel3 = 2562;
	public static int duel4 = 2560;
	public static int duel5 = 2558;
	public static int duel6 = 2556;
	public static int duel7 = 2554;
	public static int duel8 = 2552;
	
	//-4 represents the value of any charge of skills necklace
	public static int skills = -4; 
	public static List<Integer> wearableSkills = new ArrayList<Integer>();
	public static int skills0 = 11113;
	public static int skills1 = 11111;
	public static int skills2 = 11109;
	public static int skills3 = 11107;
	public static int skills4 = 11105;
	public static int skills5 = 11970;
	public static int skills6 = 11968;
	
	//-3 represents the value of any charge of glory
	public static int glory = -3; 
	public static List<Integer> wearableGlory = new ArrayList<Integer>();
	public static int glory0 = 1704;
	public static int glory1 = 1706;
	public static int glory2 = 1708;
	public static int glory3 = 1710;
	public static int glory4 = 1712;
	public static int glory5 = 11976;
	public static int glory6 = 11978;
	
	//-2 represents the value of any charge of wealth
	public static int wealth = -2; 
	public static List<Integer> wearableWealth = new ArrayList<Integer>();
	public static int wealth0 = 2572;
	public static int wealth1 = 11988;
	public static int wealth2 = 11986;
	public static int wealth3 = 11984;
	public static int wealth4 = 11982;
	public static int wealth5 = 11980;
	public static boolean collectBank = false;
	
	public static final int ironDart = 807;
	public static final int steelDart = 808;
	public static final int mithDart = 809;
}
