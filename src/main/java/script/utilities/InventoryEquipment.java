package script.utilities;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;

public class InventoryEquipment {
	public static Map<EquipmentSlot, Integer> equipmentMap = new HashMap<EquipmentSlot, Integer>();
	public static boolean checkedBank = false;
	public static Map<Integer,InventoryItem> inventoryList = new HashMap<Integer,InventoryItem>();
	
	public static void clearAll()
	{
		clearInventoryList();
		clearEquipmentSlots();
	}
	
	public static void clearInventoryList()
	{
		inventoryList.clear();
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
		MethodProvider.log("Added item to inventoryList: " + inventoryList.get(ID).itemRef.getName()
				+" in qty: " + inventoryList.get(ID).minQty + " - " + inventoryList.get(ID).maxQty
				+" and noted: " + inventoryList.get(ID).noted
				+" and refill qty: " + inventoryList.get(ID).refillQty);
	}
	
	
	/**
	 * fulfills equipment setup within a timed limit (in milliseconds). 
	 * Checks inventory if have items and equips it.
	 * Goes to bank, opens bank, and withdraws equipment items.
	 * if any items are found to not be in bank, checks GE.
	 * buys any items in GE for 25-50% over price
	 * @param timerLimit
	 * @return
	 */
	public static boolean fulfillSetup(int timerLimit)
	{
		Timer timeout = new Timer(timerLimit);
		boolean equippedItems = false;
		do
		{
			Sleep.sleep(69, 69);
			if(!equipmentMap.isEmpty())
			{
				Map<EquipmentSlot, Integer> missingItems = new HashMap<EquipmentSlot, Integer>();
				
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
						else 
						{
							MethodProvider.log("item");
							missingItems.put(slot.getKey(),slot.getValue());
						}
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
						else 
						{
							MethodProvider.log("item");
							missingItems.put(slot.getKey(),slot.getValue());
						}
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
						
						while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN)
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
							if(!checkedBank)
							{
								if(Bank.openClosest())
								{
									checkedBank = true;
								}
								else
								{
									Sleep.sleep(666, 1111);
								}
								continue;
							}
							
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
							if(Bank.contains(coins) || Inventory.isFull())
							{
								if(Bank.isOpen())
								{
									if(Bank.getWithdrawMode() == BankMode.ITEM)
									{
										if(Inventory.emptySlotCount() < 2)
										{
											if(Bank.depositAll(Inventory.getItemInSlot(Inventory.getFirstFullSlot())))
											{
												MethodProvider.sleepUntil(() -> Inventory.emptySlotCount() >= 2,Sleep.calculate(2222, 2222));
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
									Walkz.goToGE();
								}
								continue;
							}
							
							if(!GrandExchange.isOpen())
							{
								if(Walkz.goToGE())
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
								int price = (int)((LivePrices.getHigh(itemID) * 1.25) + (LivePrices.getHigh(itemID) * (Calculations.random(1.0, 25.0) / 100)));
								if(Inventory.count(coins) < price)
								{
									MethodProvider.log("Account needs more GP to buy item: " + itemID +", stopping script...");
									ScriptManager.getScriptManager().stop();
									return false;
								}
								if(GrandExchange.buyItem(itemID, 1, price))
								{
									MethodProvider.sleepUntil(() -> GrandExchange.isReadyToCollect(), Sleep.calculate(8888, 3333));
									Sleep.sleep(111, 111);
									continue;
								}
							}
						}
						continue;
					}
				}
				//have all correct items equipped
				else
				{
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
						else
						{
							validIDs.add(equipID);
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
						MethodProvider.log("Have some Not OK Items!");
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
							continue;
						} else Sleep.sleep(696, 1111);
					}
				}
			}
			else
			{
				if(!Equipment.isEmpty())
				{
					if(Bank.openClosest())
					{
						if(Bank.depositAllEquipment())
						{
							MethodProvider.sleepUntil(() -> Equipment.isEmpty(), Sleep.calculate(2222, 2222));
						}
					} else {
						Sleep.sleep(666, 1111);
					}
					Sleep.sleep(69, 69);
					continue;
				}
			}
			List<Integer> validIDs = new ArrayList<Integer>();
			List<Integer> notOKItems = new ArrayList<Integer>();
			//have all items equipped - now check inventory
			if(inventoryList.isEmpty())
			{
				if(Inventory.isEmpty()) return true;
				else
				{
					if(Bank.openClosest())
					{
						if(Bank.depositAllItems())
						{
							MethodProvider.sleepUntil(() -> Inventory.isEmpty(), Sleep.calculate(2222, 2222));
						}
						continue;
					}
					else 
					{
						Sleep.sleep(666, 696);
					}
				}
				continue;
			}
			else
			{
				for(Entry<Integer, InventoryItem> entry : inventoryList.entrySet())
				{
					validIDs.add(entry.getKey());
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
			}
			if(!notOKItems.isEmpty())
			{
				MethodProvider.log("Have some Not OK Items!");
				if(Widgets.getWidgetChild(12, 76) != null &&
						Widgets.getWidgetChild(12, 76).isVisible())
				{
					if(Widgets.getWidgetChild(12, 113).interact("Hide worn items"))
					{
						MethodProvider.sleepUntil(Bank::isOpen, Sleep.calculate(2222, 2222));
					}
					continue;
				}
				else if(Bank.openClosest())
				{
					for(int depositItem : notOKItems)
					{
						if(Bank.depositAll(depositItem))
						{
							MethodProvider.sleepUntil(() -> !Inventory.contains(depositItem), Sleep.calculate(2222, 2222));
						}
					}
				} else Sleep.sleep(696, 1111);
				continue;
			}
			Map<Integer,InventoryItem> missingInvyItems = new HashMap<Integer,InventoryItem>();
			
			for(Entry<Integer,InventoryItem> listedItem : inventoryList.entrySet())
			{
				int itemID = listedItem.getKey();
				int min = listedItem.getValue().minQty;
				int max = listedItem.getValue().maxQty;
				int count = Inventory.count(itemID);
				Item itemRef = listedItem.getValue().itemRef;
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
				int itemID = itemToGet.getKey();
				Item itemRef = itemToGet.getValue().itemRef;
				int minQty = itemToGet.getValue().minQty;
				int maxQty = itemToGet.getValue().maxQty;
				int refillQty = itemToGet.getValue().refillQty;
				boolean noted = itemToGet.getValue().noted;
				double priceIncrease = 1.0;
				
				while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN)
				{
					Sleep.sleep(69, 69);
					Item item = Inventory.get(itemID);
					Item itemNoted = Inventory.get(itemRef.getNotedItemID());
					//if item is in inventory in correct qty and noted form, continue to next item
					if(item != null || itemNoted != null || itemID == coins)
					{
						if(noted)
						{
							if(itemNoted != null && itemNoted.getAmount() >= minQty && 
									itemNoted.getAmount() <= maxQty) break;
						}
						else if(item != null && item.getAmount() >= minQty && 
								item.getAmount() <= maxQty) break;
						else if(itemID == coins && Inventory.count(itemID) >= minQty && Inventory.count(itemID) <= maxQty)
						{
							break;
						}
					}
					int swapDepositID = -1;
					//have noted but need unnoted
					if(itemNoted != null && !noted)
					{
						swapDepositID = itemRef.getNotedItemID();
					}
					
					//have unnoted but need noted
					if(item != null && noted)
					{
						swapDepositID = itemID;
					}
					
					BankMode correctMode = null;
					if(noted) correctMode = BankMode.NOTE;
					else correctMode = BankMode.ITEM;
					//check bank
					if(!checkedBank)
					{
						if(Bank.openClosest())
						{
							checkedBank = true;
						}
						else
						{
							Sleep.sleep(666, 1111);
						}
						continue;
					}
					
					//need to deposit coins if set coins to 0
					if(itemID == coins)
					{
						if(Bank.openClosest())
						{
							if(Bank.depositAll(coins))
							{
								final int tmp = coins;
								MethodProvider.sleepUntil(() -> !Inventory.contains(tmp),Sleep.calculate(2222, 2222));
							}
						}
						else
						{
							Sleep.sleep(666, 1111);
						}
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
						else
						{
							Sleep.sleep(666, 1111);
						}
						continue;
					}
					int invyCount = Inventory.count(itemID);
					int notedID = itemRef.getNotedItemID();
					int bankCount = Bank.count(itemID);
					int depositCount = invyCount - maxQty;
					//check bank for item
					if(bankCount > 0 || depositCount > 0) 
					{
						if(Bank.openClosest())
						{
							if(Bank.getWithdrawMode() == correctMode)
							{
								if(noted || itemRef.isStackable())
								{
									if(invyCount > 0)
									{
										//evaluate how much needed in existing inventory
										int neededForMax = maxQty - invyCount;
										if(depositCount > 0)
										{
											MethodProvider.log("Depositing some items extra");
											if(Bank.deposit(itemID,depositCount))
											{
												final int tmp = itemID;
												MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
											}
											continue;
										}
										if(bankCount >= neededForMax)
										{
											if(Bank.withdraw(itemID,neededForMax))
											{
												final int tmp = itemID;
												MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
											}
											continue;
										}
										if(bankCount > 0)
										{
											if(Bank.withdrawAll(itemID))
											{
												final int tmp = itemID;
												MethodProvider.sleepUntil(() -> Bank.count(tmp) <= 0, Sleep.calculate(2222, 2222));
											}
											continue;
										}
									}
									//no room to withdraw, so empty invy
									if(Inventory.isFull())
									{
										if(Bank.depositAllItems())
										{
											MethodProvider.sleepUntil(() -> Inventory.isEmpty(), Sleep.calculate(2222, 2222));
										}
										continue;
									}
									
									//check bank for amount of max
									if(bankCount >= maxQty)
									{
										if(Bank.withdraw(itemID,maxQty))
										{
											final int tmp = itemID;
											MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
										}
										continue;
									}
									//withdraw remaining of bank
									if(bankCount > 0)
									{
										if(Bank.withdrawAll(itemID))
										{
											final int tmp = itemID;
											MethodProvider.sleepUntil(() -> Bank.count(tmp) <= 0, Sleep.calculate(2222, 2222));
										}
										continue;
									}
								}
								//here we must withdraw an item which is not stackable like sharks
								//also we have some already
								if(invyCount > 0)
								{
									//evaluate how much needed in existing inventory
									int neededForMax = maxQty - invyCount;
									if(depositCount > 0)
									{
										if(Bank.deposit(itemID,depositCount))
										{
											final int tmp = itemID;
											MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
										}
										continue;
									}
									if(bankCount >= neededForMax)
									{
										if(Bank.withdraw(itemID,neededForMax))
										{
											final int tmp = itemID;
											MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
										}
										continue;
									}
									if(Bank.withdrawAll(itemID))
									{
										final int tmp = itemID;
										MethodProvider.sleepUntil(() -> Bank.count(tmp) <= 0, Sleep.calculate(2222, 2222));
									}
									continue;
								}
								
								//none in inventory
								if(bankCount >= maxQty)
								{
									if(Bank.withdraw(itemID,maxQty))
									{
										final int tmp = itemID;
										MethodProvider.sleepUntil(() -> Inventory.count(tmp) == maxQty, Sleep.calculate(2222, 2222));
									}
									continue;
								}
								if(Bank.withdrawAll(itemID))
								{
									final int tmp = itemID;
									MethodProvider.sleepUntil(() -> Inventory.count(tmp) == bankCount, Sleep.calculate(2222, 2222));
								}
								continue;
							}
							else
							{
								Bank.setWithdrawMode(correctMode);
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
					if(Bank.contains(coins) || Inventory.isFull())
					{
						if(Bank.isOpen())
						{
							if(Bank.getWithdrawMode() == BankMode.ITEM)
							{
								if(Inventory.emptySlotCount() < 2)
								{
									if(Bank.depositAll(Inventory.getItemInSlot(Inventory.getFirstFullSlot())))
									{
										MethodProvider.sleepUntil(() -> Inventory.emptySlotCount() >= 2,Sleep.calculate(2222, 2222));
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
							Walkz.goToGE();
						}
						continue;
					}
					
					if(!GrandExchange.isOpen())
					{
						if(Walkz.goToGE())
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
						int totalPrice = refillQty * pricePer;
						if(Inventory.count(coins) < totalPrice)
						{
							MethodProvider.log("Account needs more GP to buy item: " + itemID +" at price: " + totalPrice+" gp, stopping script...");
							ScriptManager.getScriptManager().stop();
							return false;
						}
						if(GrandExchange.buyItem(itemID, refillQty, pricePer))
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
		}
		while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN);
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
	
	//-7 represents the value of any charge of necklace of passage
	public static int passage = -7; 
	private static int passage1 = 21155;
	private static int passage2 = 21153;
	private static int passage3 = 21151;
	private static int passage4 = 21149;
	private static int passage5 = 21146;
	
	//-6 represents the value of any charge of games necklace
	public static int games = -6; 
	private static int games1 = 3867;
	private static int games2 = 3865;
	private static int games3 = 3863;
	private static int games4 = 3861;
	private static int games5 = 3859;
	private static int games6 = 3857;
	private static int games7 = 3855;
	private static int games8 = 3853;
	
	//-5 represents the value of any charge of dueling ring
	public static int duel = -5; 
	private static int duel1 = 2566;
	private static int duel2 = 2564;
	private static int duel3 = 2562;
	private static int duel4 = 2560;
	private static int duel5 = 2558;
	private static int duel6 = 2556;
	private static int duel7 = 2554;
	private static int duel8 = 2552;
	
	//-4 represents the value of any charge of skills necklace
	public static int skills = -4; 
	private static int skills0 = 11113;
	private static int skills1 = 11111;
	private static int skills2 = 11109;
	private static int skills3 = 11107;
	private static int skills4 = 11105;
	private static int skills5 = 11970;
	private static int skills6 = 11968;
		
	//-3 represents the value of any charge of glory
	public static int glory = -3; 
	private static int glory0 = 1704;
	private static int glory1 = 1706;
	private static int glory2 = 1708;
	private static int glory3 = 1710;
	private static int glory4 = 1712;
	private static int glory5 = 11976;
	private static int glory6 = 11978;
	
	//-2 represents the value of any charge of wealth
	public static int wealth = -2; 
	private static int wealth0 = 2572;
	private static int wealth1 = 11988;
	private static int wealth2 = 11986;
	private static int wealth3 = 11984;
	private static int wealth4 = 11982;
	private static int wealth5 = 11980;
	
	public static final int ironDart = 807;
	public static final int steelDart = 808;
	public static final int mithDart = 809;
}
