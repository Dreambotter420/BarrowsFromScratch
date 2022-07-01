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
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;

public class InvEquip {
	public static Map<EquipmentSlot, Integer> equipmentMap = new LinkedHashMap<EquipmentSlot, Integer>();
	public static boolean checkedBank = false;
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
		if(!checkedBank)
		{
			if(Bank.isOpen()) checkedBank = true;
			else
			{
				Bank.openClosest();
				Sleep.sleep(666,666);
			}
		}
		return checkedBank;
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
	public static boolean equipmentContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Equipment.contains(i)) return true;
		}
		return false;
	}
	public static boolean bankContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Bank.contains(i)) return true;
		}
		return false;
	}
	
	public static void withdrawOne(int itemID, long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 69);
			if(Inventory.count(itemID) > 0) return;
			if(Bank.openClosest())
			{
				if(Inventory.emptySlotCount() < 2)
				{
					Item i = Inventory.getItemInSlot(Inventory.getFirstFullSlot());
					int iD = i.getID();
					if(Bank.depositAll(i))
					{
						MethodProvider.sleepUntil(() -> Inventory.count(iD) <= 0,Sleep.calculate(2222, 2222));
					}
					continue;
				}
				if(Bank.withdraw(itemID,1))
				{
					MethodProvider.sleepUntil(() -> Inventory.count(itemID) > 0, Sleep.calculate(2222, 2222));
				}
			} else Sleep.sleep(666, 666);
		}
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
			if(Bank.openClosest())
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
				else if(equipID == jewelry)
				{
					for(int jewel : allJewelry)
					{
						validIDs.add(jewel);
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
				if(equipID == wealth1 || equipID == wealth2 || 
						equipID == wealth3 || equipID == wealth4 || 
						equipID == wealth5) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-3):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(equipID == glory1 || equipID == glory2 || 
						equipID == glory3 || equipID == glory4 || 
						equipID == glory5 || equipID == glory6) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-4):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(equipID == skills1 || equipID == skills2 || 
						equipID == skills3 || equipID == skills4 || 
						equipID == skills5 || equipID == skills6) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-5):
			{
				if(slot.getKey() != EquipmentSlot.RING) continue;
				if(equipID == duel1 || equipID == duel2 || 
						equipID == duel3 || equipID == duel4 || 
						equipID == duel5 || equipID == duel6 ||
						equipID == duel7 || equipID == duel8) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}case(-6):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(equipID == games1 || equipID == games2 || 
						equipID == games3 || equipID == games4 || 
						equipID == games5 || equipID == games6 ||
						equipID == games7 || equipID == games8) break;
				else missingItems.put(slot.getKey(),slot.getValue());
				break;
			}
			case(-7):
			{
				if(slot.getKey() != EquipmentSlot.AMULET) continue;
				if(equipID == passage1 || equipID == passage2 || 
						equipID == passage3 || equipID == passage4 || 
						equipID == passage5) break;
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
			return true;
		}
		//otherwise not good
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
		Timer timer = new Timer(timeout);
		double priceIncrease = 1;
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 69);
			final int totalItemCount = Bank.count(itemID) + Inventory.count(itemID) + Inventory.count(new Item(itemID,1).getNotedItemID());
			if(totalItemCount >= (initItemCount + qty)) 
			{
				if(GrandExchange.isOpen()) GrandExchange.close();
				else return;
				continue;
			}
			if(Bank.contains(coins) || (Bank.contains(coins) && Inventory.emptySlotCount() < 2))
			{
				if(Bank.isOpen())
				{
					if(Bank.getWithdrawMode() == BankMode.ITEM)
					{
						if(Inventory.emptySlotCount() < 2)
						{
							Item i = Inventory.getItemInSlot(Inventory.getFirstFullSlot());
							int iD = i.getID();
							if(Bank.depositAll(i))
							{
								MethodProvider.sleepUntil(() -> Inventory.count(iD) <= 0,Sleep.calculate(2222, 2222));
							}
							continue;
						}
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
				}
				else
				{
					Walkz.goToGE(timeout);
				}
				continue;
			}
			if(!GrandExchange.isOpen())
			{
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
					if(GrandExchange.collect())
					{
						MethodProvider.sleepUntil(() -> !GrandExchange.isReadyToCollect(), Sleep.calculate(2222, 2222));
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
						else if(equipID == jewelry)
						{
							for(int jewel : allJewelry)
							{
								validIDs.add(jewel);
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
							final int equippedID = i.getID();
							for(int validID : validIDs)
							{
								if(validID == equippedID) ok = true;
								break;
							}
							if(!ok) notOKItems.add(equippedID);
						}
					}
					
					if(!notOKItems.isEmpty())
					{
						MethodProvider.log("Have some extra equipment items! Going to bank equipment menu to directly bank them~~");
						for(int i : notOKItems)
						{
							MethodProvider.log("~~" + new Item(i,1).getName()+"~~");
						}
						if(Widgets.getWidgetChild(12, 76) != null &&
								Widgets.getWidgetChild(12, 76).isVisible())
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
											MethodProvider.log("removing not OK item: " + notOKItem);
											final int tmp = i;
											MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(12,tmp,1).getItemId() == -1, Sleep.calculate(2222, 2222));
										}
										break;
									}
								}
							}
						}
						else if(Bank.openClosest())
						{
							if(Widgets.getWidgetChild(12, 113).interact("Show worn items"))
							{
								MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(12, 76) != null &&
										Widgets.getWidgetChild(12, 76).isVisible(), Sleep.calculate(2222, 2222));
							}
						} else Sleep.sleep(696, 1111);
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
						if(equipID == wealth1 || equipID == wealth2 || 
								equipID == wealth3 || equipID == wealth4 || 
								equipID == wealth5) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-3):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(equipID == glory1 || equipID == glory2 || 
								equipID == glory3 || equipID == glory4 || 
								equipID == glory5 || equipID == glory6) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-4):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(equipID == skills1 || equipID == skills2 || 
								equipID == skills3 || equipID == skills4 || 
								equipID == skills5 || equipID == skills6) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-5):
					{
						if(slot.getKey() != EquipmentSlot.RING) continue;
						if(equipID == duel1 || equipID == duel2 || 
								equipID == duel3 || equipID == duel4 || 
								equipID == duel5 || equipID == duel6 ||
								equipID == duel7 || equipID == duel8) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}case(-6):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(equipID == games1 || equipID == games2 || 
								equipID == games3 || equipID == games4 || 
								equipID == games5 || equipID == games6 ||
								equipID == games7 || equipID == games8) break;
						else missingItems.put(slot.getKey(),slot.getValue());
						break;
					}
					case(-7):
					{
						if(slot.getKey() != EquipmentSlot.AMULET) continue;
						if(equipID == passage1 || equipID == passage2 || 
								equipID == passage3 || equipID == passage4 || 
								equipID == passage5) break;
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
							Item item = Equipment.getItemInSlot(slot);
							
							//if item is equipped, continue to next item
							if(item != null)
							{
								int equipID = item.getID();
								boolean breakOrNot = false;
								switch(itemID)
								{
								case(-2):
								{
									if(equipID == wealth1 || equipID == wealth2 || 
											equipID == wealth3 || equipID == wealth4 || 
											equipID == wealth5) breakOrNot = true;
									break;
								}case(-3):
								{
									if(equipID == glory1 || equipID == glory2 || 
											equipID == glory3 || equipID == glory4 || 
											equipID == glory5 || equipID == glory6) breakOrNot = true;
									break;
								}case(-4):
								{
									if(equipID == skills1 || equipID == skills2 || 
											equipID == skills3 || equipID == skills4 || 
											equipID == skills5 || equipID == skills6) breakOrNot = true;
									break;
								}case(-5):
								{
									if(equipID == duel1 || equipID == duel2 || 
											equipID == duel3 || equipID == duel4 || 
											equipID == duel5 || equipID == duel6 ||
											equipID == duel7 || equipID == duel8) breakOrNot = true;
									break;
								}case(-6):
								{
									if(equipID == games1 || equipID == games2 || 
											equipID == games3 || equipID == games4 || 
											equipID == games5 || equipID == games6 ||
											equipID == games7 || equipID == games8) breakOrNot = true;
									break;
								}
								case(-7):
								{
									if(equipID == passage1 || equipID == passage2 || 
											equipID == passage3 || equipID == passage4 || 
											equipID == passage5) breakOrNot = true;
									break;
								}
								default:
								{
									if(equipID == itemID) breakOrNot = true;
									break;
								}
								}
								if(breakOrNot) break;
							}
							//have item in inventory, so handle inventory equipping
							boolean continueOrNot = false;
							int invyID = 0;
							switch(itemID)
							{
							case(-2):
							{
								if(Inventory.contains(wealth1))
								{
									continueOrNot = true;
									invyID = wealth1;
								} else if(Inventory.contains(wealth2))
								{
									continueOrNot = true;
									invyID = wealth2;
								} else if(Inventory.contains(wealth3))
								{
									continueOrNot = true;
									invyID = wealth3;
								} else if(Inventory.contains(wealth4))
								{
									continueOrNot = true;
									invyID = wealth4;
								} else if(Inventory.contains(wealth5))
								{
									continueOrNot = true;
									invyID = wealth5;
								}
								break;
							}case(-3):
							{
								if(Inventory.contains(glory1))
								{
									continueOrNot = true;
									invyID = glory1;
								} else if(Inventory.contains(glory2))
								{
									continueOrNot = true;
									invyID = glory2;
								} else if(Inventory.contains(glory3))
								{
									continueOrNot = true;
									invyID = glory3;
								} else if(Inventory.contains(glory4))
								{
									continueOrNot = true;
									invyID = glory4;
								} else if(Inventory.contains(glory5))
								{
									continueOrNot = true;
									invyID = glory5;
								} else if(Inventory.contains(glory6))
								{
									continueOrNot = true;
									invyID = glory6;
								}
								break;
							}case(-4):
							{
								if(Inventory.contains(skills1))
								{
									continueOrNot = true;
									invyID = skills1;
								} else if(Inventory.contains(skills2))
								{
									continueOrNot = true;
									invyID = skills2;
								} else if(Inventory.contains(skills3))
								{
									continueOrNot = true;
									invyID = skills3;
								} else if(Inventory.contains(skills4))
								{
									continueOrNot = true;
									invyID = skills4;
								} else if(Inventory.contains(skills5))
								{
									continueOrNot = true;
									invyID = skills5;
								} else if(Inventory.contains(skills6))
								{
									continueOrNot = true;
									invyID = skills6;
								}
								break;
							}case(-5):
							{
								if(Inventory.contains(duel1))
								{
									continueOrNot = true;
									invyID = duel1;
								} else if(Inventory.contains(duel2))
								{
									continueOrNot = true;
									invyID = duel2;
								} else if(Inventory.contains(duel3))
								{
									continueOrNot = true;
									invyID = duel3;
								} else if(Inventory.contains(duel4))
								{
									continueOrNot = true;
									invyID = duel4;
								} else if(Inventory.contains(duel5))
								{
									continueOrNot = true;
									invyID = duel5;
								} else if(Inventory.contains(duel6))
								{
									continueOrNot = true;
									invyID = duel6;
								} else if(Inventory.contains(duel7))
								{
									continueOrNot = true;
									invyID = duel7;
								} else if(Inventory.contains(duel8))
								{
									continueOrNot = true;
									invyID = duel8;
								}
								break;
							}case(-6):
							{
								if(Inventory.contains(games1))
								{
									continueOrNot = true;
									invyID = games1;
								} else if(Inventory.contains(games2))
								{
									continueOrNot = true;
									invyID = games2;
								} else if(Inventory.contains(games3))
								{
									continueOrNot = true;
									invyID = games3;
								} else if(Inventory.contains(games4))
								{
									continueOrNot = true;
									invyID = games4;
								} else if(Inventory.contains(games5))
								{
									continueOrNot = true;
									invyID = games5;
								} else if(Inventory.contains(games6))
								{
									continueOrNot = true;
									invyID = games6;
								} else if(Inventory.contains(games7))
								{
									continueOrNot = true;
									invyID = games7;
								} else if(Inventory.contains(games8))
								{
									continueOrNot = true;
									invyID = games8;
								}
								break;
							}
							case(-7):
							{
								if(Inventory.contains(passage1))
								{
									continueOrNot = true;
									invyID = passage1;
								} else if(Inventory.contains(passage2))
								{
									continueOrNot = true;
									invyID = passage2;
								} else if(Inventory.contains(passage3))
								{
									continueOrNot = true;
									invyID = passage3;
								} else if(Inventory.contains(passage4))
								{
									continueOrNot = true;
									invyID = passage4;
								} else if(Inventory.contains(passage5))
								{
									continueOrNot = true;
									invyID = passage5;
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
								if(Bank.isOpen() || Tabs.isOpen(Tab.INVENTORY))
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
								if(Bank.contains(wealth1))
								{
									continueOrNot2 = true;
									bankID = wealth1;
								} else if(Bank.contains(wealth2))
								{
									continueOrNot2 = true;
									bankID = wealth2;
								} else if(Bank.contains(wealth3))
								{
									continueOrNot2 = true;
									bankID = wealth3;
								} else if(Bank.contains(wealth4))
								{
									continueOrNot2 = true;
									bankID = wealth4;
								} else if(Bank.contains(wealth5))
								{
									continueOrNot2 = true;
									bankID = wealth5;
								}
								break;
							}case(-3):
							{
								if(Bank.contains(glory1))
								{
									continueOrNot2 = true;
									bankID = glory1;
								} else if(Bank.contains(glory2))
								{
									continueOrNot2 = true;
									bankID = glory2;
								} else if(Bank.contains(glory3))
								{
									continueOrNot2 = true;
									bankID = glory3;
								} else if(Bank.contains(glory4))
								{
									continueOrNot2 = true;
									bankID = glory4;
								} else if(Bank.contains(glory5))
								{
									continueOrNot2 = true;
									bankID = glory5;
								} else if(Bank.contains(glory6))
								{
									continueOrNot2 = true;
									bankID = glory6;
								}
								break;
							}case(-4):
							{
								if(Bank.contains(skills1))
								{
									continueOrNot2 = true;
									bankID = skills1;
								} else if(Bank.contains(skills2))
								{
									continueOrNot2 = true;
									bankID = skills2;
								} else if(Bank.contains(skills3))
								{
									continueOrNot2 = true;
									bankID = skills3;
								} else if(Bank.contains(skills4))
								{
									continueOrNot2 = true;
									bankID = skills4;
								} else if(Bank.contains(skills5))
								{
									continueOrNot2 = true;
									bankID = skills5;
								} else if(Bank.contains(skills6))
								{
									continueOrNot2 = true;
									bankID = skills6;
								}
								break;
							}case(-5):
							{
								if(Bank.contains(duel1))
								{
									continueOrNot2 = true;
									bankID = duel1;
								} else if(Bank.contains(duel2))
								{
									continueOrNot2 = true;
									bankID = duel2;
								} else if(Bank.contains(duel3))
								{
									continueOrNot2 = true;
									bankID = duel3;
								} else if(Bank.contains(duel4))
								{
									continueOrNot2 = true;
									bankID = duel4;
								} else if(Bank.contains(duel5))
								{
									continueOrNot2 = true;
									bankID = duel5;
								} else if(Bank.contains(duel6))
								{
									continueOrNot2 = true;
									bankID = duel6;
								} else if(Bank.contains(duel7))
								{
									continueOrNot2 = true;
									bankID = duel7;
								} else if(Bank.contains(duel8))
								{
									continueOrNot2 = true;
									bankID = duel8;
								}
								break;
							}case(-6):
							{
								if(Bank.contains(games1))
								{
									continueOrNot2 = true;
									bankID = games1;
								} else if(Bank.contains(games2))
								{
									continueOrNot2 = true;
									bankID = games2;
								} else if(Bank.contains(games3))
								{
									continueOrNot2 = true;
									bankID = games3;
								} else if(Bank.contains(games4))
								{
									continueOrNot2 = true;
									bankID = games4;
								} else if(Bank.contains(games5))
								{
									continueOrNot2 = true;
									bankID = games5;
								} else if(Bank.contains(games6))
								{
									continueOrNot2 = true;
									bankID = games6;
								} else if(Bank.contains(games7))
								{
									continueOrNot2 = true;
									bankID = games7;
								} else if(Bank.contains(games8))
								{
									continueOrNot2 = true;
									bankID = games8;
								}
								break;
							}
							case(-7):
							{
								if(Bank.contains(passage1))
								{
									continueOrNot2 = true;
									bankID = passage1;
								} else if(Bank.contains(passage2))
								{
									continueOrNot2 = true;
									bankID = passage2;
								} else if(Bank.contains(passage3))
								{
									continueOrNot2 = true;
									bankID = passage3;
								} else if(Bank.contains(passage4))
								{
									continueOrNot2 = true;
									bankID = passage4;
								} else if(Bank.contains(passage5))
								{
									continueOrNot2 = true;
									bankID = passage5;
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
								if(Bank.openClosest())
								{
									if(Bank.getWithdrawMode() == BankMode.ITEM)
									{
										if(Inventory.emptySlotCount() < 1)
										{
											if(Bank.depositAll(Inventory.getItemInSlot(Inventory.getFirstFullSlot())))
											{
												MethodProvider.sleepUntil(() -> Inventory.emptySlotCount() >= 1,Sleep.calculate(2222, 2222));
											}
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
							switch(itemID)
							{
							case(-2):
							{
								itemID = wealth5;
								break;
							}
							case(-3):
							{
								itemID = glory6;
								break;
							}
							case(-4):
							{
								itemID = skills6;
								break;
							}
							case(-5):
							{
								itemID = duel8;
								break;
							}
							case(-6):
							{
								itemID = games8;
								break;
							}
							case(-7):
							{
								itemID = passage5;
								break;
							}
							default: break;
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
						if(Widgets.getWidgetChild(12, 76) != null &&
								Widgets.getWidgetChild(12, 76).isVisible())
						{
							if(Widgets.getWidgetChild(12, 113).interact("Hide worn items"))
							{
								MethodProvider.sleepUntil(Bank::isOpen, Sleep.calculate(2222, 2222));
							}
							continue;
						}
						if(Bank.openClosest())
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
			if(Widgets.getWidgetChild(12, 76) != null &&
					Widgets.getWidgetChild(12, 76).isVisible())
			{
				if(Widgets.getWidgetChild(12, 113).interact("Hide worn items"))
				{
					MethodProvider.sleepUntil(Bank::isOpen, Sleep.calculate(2222, 2222));
				}
				continue;
			}
			//have all items equipped - now check inventory
			if(inventoryList.isEmpty())
			{
				if(!strict) return true;
				else
				{
					if(Bank.openClosest())
					{
						if(Bank.depositAllItems()) MethodProvider.sleepUntil(Inventory::isEmpty, Sleep.calculate(2222, 2222));
					}
					else Sleep.sleep(666, 696);
				}
				continue;
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
				if(!notOKItems.isEmpty())
				{
					MethodProvider.log("Have some extra items in inventory!~~ ");
					for(int i : notOKItems)
					{
						MethodProvider.log("~~"+new Item(i,1).getName()+"~~");
					}
					if(Bank.openClosest())
					{
						for(int depositItem : notOKItems)
						{
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
					int invCount = 0;
					if(item == null)
					{
						if(minQty <= 0) break;
						else invCount = 0;
					}
					else 
					{
						invCount = item.getAmount();
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
						if(Bank.openClosest())
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
						if(Bank.openClosest())
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
						if(Bank.openClosest())
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
									else if(Bank.withdraw(unnotedID,neededForMax))
									{
										final int tmp = requestedID;
										MethodProvider.sleepUntil(() -> (Inventory.count(tmp) == maxQty || Inventory.isFull()), Sleep.calculate(2222, 2222));
									}
									continue;
								}
								
								//bank contains some but not all for max qty - withdraw all
								if(bankCount > 0)
								{
									if(Bank.getWithdrawMode() != correctMode) Bank.setWithdrawMode(correctMode);
									else if(Bank.withdrawAll(unnotedID))
									{
										final int tmp = unnotedID;
										MethodProvider.sleepUntil(() -> Bank.count(tmp) <= 0, Sleep.calculate(2222, 2222));
									}
								}
								continue;
							}
							//here we must withdraw an item which is not stackable like sharks
							if(bankCount >= neededForMax)
							{
								if(Bank.getWithdrawMode() != correctMode) Bank.setWithdrawMode(correctMode);
								else if(Bank.withdraw(unnotedID,neededForMax))
								{
									final int tmp = requestedID;
									MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
								}
								continue;
							}
							if(Bank.getWithdrawMode() != correctMode) Bank.setWithdrawMode(correctMode);
							else if(Bank.withdrawAll(unnotedID))
							{
								final int tmp = unnotedID;
								MethodProvider.sleepUntil(() -> Bank.count(tmp) <= 0, Sleep.calculate(2222, 2222));
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
		return false;
	}
	
	public static boolean equipItem(int ID)
	{
		if(Equipment.contains(ID)) return true;
		
		if(Tabs.isOpen(Tab.INVENTORY) || Bank.isOpen())
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
					Combat.eatFood();
					return Equipment.contains(tmp);
				}, Sleep.calculate(2222, 2222));
			}
			if(Equipment.contains(ID)) return true;
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
	public static boolean equipItem(String itemName)
	{
		if(Equipment.contains(itemName)) return true;
		if(Tabs.isOpen(Tab.INVENTORY) || Bank.isOpen())
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
					Combat.eatFood();
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
		
		allJewelry.addAll(wearablePassages);
		allJewelry.addAll(wearableGames);
		allJewelry.addAll(wearableDuel);
		allJewelry.addAll(wearableSkills);
		allJewelry.addAll(wearableGlory);
		allJewelry.addAll(wearableWealth);
		allJewelry.addAll(wearableDuel);
	}
	public static int jewelry = -10;
	public static List<Integer> allJewelry = new ArrayList<Integer>();
	
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
	
	public static final int ironDart = 807;
	public static final int steelDart = 808;
	public static final int mithDart = 809;
}
