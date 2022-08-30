package script.skills.magic;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.input.event.impl.mouse.impl.click.ClickMode;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.GrandExchangg;
import script.utilities.InvEquip;
import script.utilities.ItemsOnGround;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Skillz;
import script.utilities.Sleep;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Trains magic 1-83
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainMagic extends Leaf {
	public static boolean initialized = false;
	public static boolean completedMagic = false;
    public void onStart() {
        Main.clearCustomPaintText();
        initialized = true;
    }
    public boolean onExit()
    {
    	chancedXPAlch = false;
    	xpAlch = false;
    	if(Magic.isSpellSelected())
    	{
    		Magic.deselect();
    		return false;
    	}
    	return true;
    }
    @Override
    public int onLoop() {
        if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Magic!");
            if(onExit()) API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
       	int magic = Skills.getRealLevel(Skill.MAGIC);
       	int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(magic >= DecisionLeaf.mageSetpoint) {
            MethodProvider.log("[COMPLETE] -> lvl "+DecisionLeaf.mageSetpoint+" magic!");
            if(onExit()) 
            {
            	completedMagic = true;
               	API.mode = null;
            }
            
            return Timing.sleepLogNormalSleep();
        }

    	if(Skillz.shouldCheckSkillInterface()) Skillz.checkSkillProgress(Skill.MAGIC);
    	if(magic < 45) trainLesserDemon();
    	else if(magic < 55) teleportCamelot();
    	else if(magic < 75) highAlch();
        return Timing.sleepLogNormalSleep();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_MAGIC;
	}
	/**
	 * cc price for items: 
	 * mith platebodies 2800
	 * green dhide bodies 4300
	 * addy platebodies 9600
	 */
	public static final int mithPlatePrice = 2800;
	public static final int addyPlatePrice = 9600;
	public static final int greenBodyPrice = 4300;
	public static int HACount = 0;
	public static boolean changeVarbit = false;

	public static int foundAlchLimit = 0;
	public static Timer HATimer = null;
	public static final int HAWarningThresholdVarbit = 6091;
	public static boolean xpAlch = false;
	public static boolean chancedXPAlch = false;
	public static void checkProfitItems () {
		final int natRunePrice = new Item(id.natureRune,1).getLivePrice();
		List<Item> profitItems = new ArrayList<Item>();
		for(int i = 1; i < 99999; i++)
		{
			Item item = new Item(i,1);
			if(item != null && 
				item.getName() != null && 
				!item.getName().isEmpty() &&
				!item.isNoted() &&
				!item.isPlaceholder() &&
				(item.getLivePrice() - natRunePrice < item.getHighAlchValue()) && 
				item.isTradable() &&
				item.isValid() && 
				(item.getID() > 0))
			{
				profitItems.add(item);
			}
		}
		Collections.sort(profitItems, new Comparator<Item>() {
	          @Override
	          public int compare(Item o1, Item o2) {
	        	  return (o2.getHighAlchValue() - o2.getLivePrice() - natRunePrice) - 
							(o1.getHighAlchValue() - o1.getLivePrice() - natRunePrice);
	          }
	    });
		MethodProvider.log("Done checking items and sorting by profit! ~~");
		for(Item i : profitItems)
		{
			MethodProvider.log("Item name,profit,HA value: " +i.getName() + ","+(i.getHighAlchValue()-i.getLivePrice()-natRunePrice)+","+i.getHighAlchValue());
		}

		MethodProvider.log("Done  ~~");
		Sleep.sleep(6969, 4420);
	}
	
	public static void highAlch()
	{
		//decide whether to alch longbows in quiet spot or profit alchs at GE
		if(!chancedXPAlch)
		{
			int chance = (int) Calculations.nextGaussianRandom(50, 25);
			if(chance > 60) 
				{
				xpAlch = true;
				MethodProvider.log("Chose to High Alch Magic/Yew longbows~");
				}
			else MethodProvider.log("Chose to High Alch profitable items~");
			chancedXPAlch = true;
		}
		if(!InvEquip.checkedBank()) return;
		API.randomAFK(10);
		if(Equipment.contains(id.staffOfFire) && Inventory.count(id.natureRune) > 0)
		{
			List<Integer> profitKeys = new ArrayList(id.approvedAlchs.keySet());
			List<Integer> longbowKeys = new ArrayList(id.xpAlchs.keySet());
			Collections.shuffle(profitKeys);
			boolean foundAlch = false;
			List<Integer> keys = null;
			if(xpAlch) 
			{
				if(foundAlchLimit == 0) foundAlchLimit = (int) Calculations.nextGaussianRandom(1200, 200);
				keys = longbowKeys;
			}
			else 
				
			{
				keys = profitKeys;
				if(foundAlchLimit == 0) foundAlchLimit = (int) Calculations.nextGaussianRandom(400, 100);
			}
			int alchsCount = 0;
			boolean bankedAlchs = false;
			for(Integer i : keys)
			{
				if(Bank.count(i) > 0) bankedAlchs = true;
				if(Inventory.count(i) > 0 || 
						Inventory.count(new Item(i,1).getNotedItemID()) > 0)
				{
					alchsCount += (Inventory.count(i) + Inventory.count(new Item(i,1).getNotedItemID()) + Bank.count(i));
					foundAlch = true;
				}
			}
			if(bankedAlchs)
			{
				MethodProvider.log("Found banked alchs");
				if(Locations.HASpot1.contains(Players.localPlayer()))
				{
					if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
					{
						if(GameObjects.closest("Ladder").interact("Climb-down"))
						{
							MethodProvider.sleepUntil(() -> !Locations.HASpot1.contains(Players.localPlayer()),
									() -> Players.localPlayer().isMoving(),
									Sleep.calculate(2222, 2222),50);
						}
					}
					return;
				}
				if(Bankz.openClosest(85))
				{
					if(Bank.getWithdrawMode() == BankMode.NOTE)
					{
						for(Integer i : keys)
						{
							if(Bank.count(i) > 0)
							{
								if(Inventory.isFull())
								{
									InvEquip.depositExtraJunk();
									return;
								}
								MethodProvider.log("Attempting withdraw of item: "+new Item(i,1).getName());
								InvEquip.withdrawAll(i, true, 180000);
							}
						}
						return;
					}
					Bank.setWithdrawMode(BankMode.NOTE);
					return;
				}
				
				return;
			}
			if(foundAlch)
			{				
				if(Dialogues.canContinue()) 
				{
					Dialogues.continueDialogue();
					return;
				}
				if(Dialogues.isProcessing()) return;
				if((Widgets.getWidgetChild(193,2) != null && 
						Widgets.getWidgetChild(193,2).isVisible() && 
						Widgets.getWidgetChild(193,2).getText().contains("That item is considered <col=6f0000>valuable") ||
						(Dialogues.areOptionsAvailable() && Dialogues.chooseFirstOptionContaining("Proceed to cast High Alchemy on it."))))
				{
					changeVarbit = true;
					return;
				}
				if(changeVarbit)
				{
					if(PlayerSettings.getBitValue(HAWarningThresholdVarbit) >= 50000)
					{
						MethodProvider.log("Succesfully changed HA Warnings to greater than 50k!");
						changeVarbit = false;
						return;
					}
					if(Dialogues.canContinue()) 
					{
						Dialogues.continueDialogue();
						return;
					}
					if(Dialogues.areOptionsAvailable())
					{
						if(Dialogues.chooseFirstOptionContaining("Set value threshold")) return;
						MethodProvider.log("Options are available but not the set value threshold one we want in HA varbit function...");
						Map.interact(Players.localPlayer().getTile());
						Sleep.sleep(420, 696);
						return;
					}
					if(Widgets.getWidgetChild(162, 41) != null && 
		        			Widgets.getWidgetChild(162, 41).isVisible() && 
		        			Widgets.getWidgetChild(162, 41).getText().contains("Set value threshold for alchemy warnings:"))
		        	{
						if(Widgets.getWidgetChild(162, 42).getText().length() > 1)
	    				{
	    					Keyboard.typeSpecialKey(8);
	    					return;
	    				}
	    				else
	    				{
	    					Keyboard.type(Integer.toString(API.roundToMultiple((int) Calculations.nextGaussianRandom(75000,20000),500)), true);
	    					Sleep.sleep(1111, 4444);
	    					return;
	    				}
		    		}
					if(!Tabs.isOpen(Tab.MAGIC))
					{
						Tabz.open(Tab.MAGIC);
						return;
					}
					if(Magic.interact(Normal.HIGH_LEVEL_ALCHEMY, "Warnings"))
					{
						MethodProvider.sleepUntil(Dialogues::inDialogue,Sleep.calculate(2222, 2222));
						return;
					}
				}
				if(xpAlch)
				{
					if(Locations.HASpot1.contains(Players.localPlayer()))
					{
						if(keys.contains(new Item(Inventory.getIdForSlot(11),1).getUnnotedItemID()))
						{
							//can HA now :-)
							if(HATimer == null)
							{
								HATimer = new Timer(2147483646);
							}
							if(Bank.isOpen()) 
							{
								Bankz.close();
								return;
							}
							if(GrandExchange.isOpen())
							{
								if(GrandExchange.isReadyToCollect()) GrandExchange.collect();
								if(!GrandExchange.isReadyToCollect()) GrandExchangg.close();
								return;
							}
							if(Magic.isSpellSelected())
							{
								if(Magic.getSelectedSpellName().contains("High Level Alchemy"))
								{
									if(Inventory.slotBounds(11).contains(Mouse.getPosition()))
									{
										if(Mouse.click(ClickMode.LEFT_CLICK))
										{
											HACount++;
											Sleep.sleep(1111, 1111);
										}
										return;
									}
									if(Mouse.move(Inventory.slotBounds(11)))
									{
										Sleep.sleep(111, 1111);
									}
									return;
								}
							}
							if(Magic.castSpell(Normal.HIGH_LEVEL_ALCHEMY))
							{
								Sleep.sleep(42, 696);
							}
							return;
						}
						final List<Integer> keysFinal = keys; //???
						Item alch = Inventory.get(i -> i!=null && 
								keysFinal.contains(i.getUnnotedItemID()));
						if(alch == null)
						{
							MethodProvider.log("Alch found but first alch found null!");
							return;
						}
						if(Bank.isOpen()) 
						{
							Bankz.close();
							return;
						}
						if(GrandExchange.isOpen())
						{
							if(GrandExchange.isReadyToCollect()) GrandExchange.collect();
							if(!GrandExchange.isReadyToCollect()) GrandExchangg.close();
							return;
						}
						if(!Tabs.isOpen(Tab.INVENTORY))
						{
							Tabz.open(Tab.INVENTORY);
							return;
						}
						
						if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
						{
							Sleep.sleep(69, 696);
						}
						
						if(!Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
						{
							if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
							{
								Sleep.sleep(69, 696);
							}
						}
						if(Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
						{
							if(Mouse.drag(Inventory.slotBounds(11)))
							{
								Sleep.sleep(696, 696);
							}
						}
						return;
					}
					if(Locations.HALadderSpot.distance() < 8)
					{
						GameObject ladder = GameObjects.closest("Ladder");
						if(ladder == null)
						{
							MethodProvider.log("Ladder null in HA spot lower area!");
							return;
						}
						if(ladder.interact("Climb-up"))
						{
							MethodProvider.sleepUntil(() -> Locations.HASpot1.contains(Players.localPlayer()), 
									() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),69);
						}
						return;
					}
					if(GrandExchange.isOpen())
					{
						if(GrandExchange.isReadyToCollect()) GrandExchange.collect();
						if(!GrandExchange.isReadyToCollect()) GrandExchangg.close();
						return;
					}
					if(Bank.isOpen())
					{
						Bankz.close();
						return;
					}
					if(!Walkz.walkPath(Paths.toHASpot1) && 
							!Walkz.walkPath(Paths.toHASpot2) && 
							!Walkz.useJewelry(InvEquip.duel, "PvP Arena"))
					{
						InvEquip.buyItem(InvEquip.duel8, (int) Calculations.nextGaussianRandom(2, 1), 180000);
					}
					return;
				}
				
				if(keys.contains(new Item(Inventory.getIdForSlot(11),1).getUnnotedItemID()))
				{
					//can HA now :-)
					if(HATimer == null)
					{
						HATimer = new Timer(2147483646);
					}
					if(Bank.isOpen()) 
					{
						Bankz.close();
						return;
					}
					if(GrandExchange.isOpen())
					{
						if(GrandExchange.isReadyToCollect()) GrandExchange.collect();
						if(!GrandExchange.isReadyToCollect()) GrandExchangg.close();
						return;
					}
					if(Magic.isSpellSelected())
					{
						if(Magic.getSelectedSpellName().contains("High Level Alchemy"))
						{
							if(Inventory.slotBounds(11).contains(Mouse.getPosition()))
							{
								if(Mouse.click(ClickMode.LEFT_CLICK))
								{
									HACount++;
									Sleep.sleep(1111, 1111);
								}
								return;
							}
							if(Mouse.move(Inventory.slotBounds(11)))
							{
								Sleep.sleep(111, 1111);
							}
							return;
						}
					}
					if(Magic.castSpell(Normal.HIGH_LEVEL_ALCHEMY))
					{
						Sleep.sleep(42, 696);
					}
					return;
				}
				final List<Integer> keysFinal = keys; //???
				Item alch = Inventory.get(i -> i!=null && 
						keysFinal.contains(i.getUnnotedItemID()));
				if(alch == null)
				{
					MethodProvider.log("Alch found but first alch found null!");
					return;
				}
				if(Bank.isOpen()) 
				{
					Bankz.close();
					return;
				}
				if(GrandExchange.isOpen())
				{
					if(GrandExchange.isReadyToCollect()) GrandExchange.collect();
					if(!GrandExchange.isReadyToCollect()) GrandExchangg.close();
					return;
				}
				if(!Tabs.isOpen(Tab.INVENTORY))
				{
					Tabz.open(Tab.INVENTORY);
					return;
				}
				
				if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
				{
					Sleep.sleep(69, 696);
				}
				
				if(!Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
				{
					if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
					{
						Sleep.sleep(69, 696);
					}
				}
				if(Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
				{
					if(Mouse.drag(Inventory.slotBounds(11)))
					{
						Sleep.sleep(696, 696);
					}
				}
			}
			else
			{
				if(xpAlch) BuyHighAlchs.buyItems(true);
				else BuyHighAlchs.buyItems(false);
			}
		} 
		else 
		{
			fulfillHA();
		}
	}
	public static Timer quickHATimer = null;
	public static final Rectangle perfectHAClickSpot = new Rectangle(709,302,13,14);
	/**
	 * returns true if have HA items in inventory.
	 * returns false otherwise and tries to withdraw/buy more
	 * @return
	 */
	public static boolean haveHAItems()
	{
		if(!InvEquip.checkedBank() ||
				(quickHATimer != null && !quickHATimer.finished())) return false;
		if(Equipment.contains(id.staffOfFire) && Inventory.count(id.natureRune) > 0)
		{
			//combine profit and longbow alch lists
			List<Integer> profitKeys = new ArrayList(id.approvedAlchs.keySet());
			List<Integer> longbowKeys = new ArrayList(id.xpAlchs.keySet());
			List<Integer> keys = new ArrayList<Integer>();
			keys.addAll(profitKeys);
			keys.addAll(longbowKeys);
			Collections.shuffle(keys);
			boolean foundAlch = false;
			boolean bankedAlchs = false;
			for(Integer i : keys)
			{
				if(Bank.count(i) > 0) bankedAlchs = true;
				if(Inventory.count(i) > 0 || 
						Inventory.count(new Item(i,1).getNotedItemID()) > 0)
				{
					foundAlch = true;
				}
			}
			if(bankedAlchs)
			{
				MethodProvider.log("Found banked alchs");
				if(Locations.HASpot1.contains(Players.localPlayer()))
				{
					if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
					{
						if(GameObjects.closest("Ladder").interact("Climb-down"))
						{
							MethodProvider.sleepUntil(() -> !Locations.HASpot1.contains(Players.localPlayer()),
									() -> Players.localPlayer().isMoving(),
									Sleep.calculate(2222, 2222),50);
						}
					}
					return false;
				}
				if(Bankz.openClosest(85))
				{
					if(Bank.getWithdrawMode() == BankMode.NOTE)
					{
						for(Integer i : keys)
						{
							if(Bank.count(i) > 0)
							{
								if(Inventory.isFull())
								{
									InvEquip.depositExtraJunk();
									return false;
								}
								MethodProvider.log("Attempting withdraw of item: "+new Item(i,1).getName());
								if(Bank.withdrawAll(i))
								{
									MethodProvider.sleepUntil(() -> Bank.count(i) <= 0, Sleep.calculate(2222, 2222));
									Sleep.sleep(69, 1111);
								}
							}
						}
						return false;
					}
					Bank.setWithdrawMode(BankMode.NOTE);
					return false;
				}
				return false;
			}
			if(foundAlch)
			{
				return true;
			}
			else BuyHighAlchs.buyItems(true);
		} 
		else 
		{
			fulfillHA();
		}
		return false;
	}
	public static void quickHighAlch()
	{
		if(Dialogues.canContinue()) 
		{
			Dialogues.continueDialogue();
			return;
		}
		if(Dialogues.isProcessing()) return;
		if((Widgets.getWidgetChild(193,2) != null && 
				Widgets.getWidgetChild(193,2).isVisible() && 
				Widgets.getWidgetChild(193,2).getText().contains("That item is considered <col=6f0000>valuable") ||
				(Dialogues.areOptionsAvailable() && Dialogues.chooseFirstOptionContaining("Proceed to cast High Alchemy on it."))))
		{
			changeVarbit = true;
			return;
		}
		if(changeVarbit)
		{
			if(PlayerSettings.getBitValue(HAWarningThresholdVarbit) >= 50000)
			{
				MethodProvider.log("Succesfully changed HA Warnings to greater than 50k!");
				changeVarbit = false;
				return;
			}
			if(Dialogues.canContinue()) 
			{
				Dialogues.continueDialogue();
				return;
			}
			if(Dialogues.areOptionsAvailable())
			{
				if(Dialogues.chooseFirstOptionContaining("Set value threshold")) return;
				MethodProvider.log("Options are available but not the set value threshold one we want in HA varbit function...");
				Map.interact(Players.localPlayer().getTile());
				Sleep.sleep(420, 696);
				return;
			}
			if(Widgets.getWidgetChild(162, 41) != null && 
        			Widgets.getWidgetChild(162, 41).isVisible() && 
        			Widgets.getWidgetChild(162, 41).getText().contains("Set value threshold for alchemy warnings:"))
        	{
				if(Widgets.getWidgetChild(162, 42).getText().length() > 1)
				{
					Keyboard.typeSpecialKey(8);
					return;
				}
				else
				{
					Keyboard.type(Integer.toString(API.roundToMultiple((int) Calculations.nextGaussianRandom(75000,20000),500)), true);
					Sleep.sleep(1111, 4444);
					return;
				}
    		}
			if(!Tabs.isOpen(Tab.MAGIC))
			{
				Tabz.open(Tab.MAGIC);
				return;
			}
			if(Magic.interact(Normal.HIGH_LEVEL_ALCHEMY, "Warnings"))
			{
				MethodProvider.sleepUntil(Dialogues::inDialogue,Sleep.calculate(2222, 2222));
				return;
			}
			return;
		}
		if(id.allAlchs.contains(new Item(Inventory.getIdForSlot(11),1).getUnnotedItemID()))
		{
			//can HA now :-)
			if(Bank.isOpen()) 
			{
				Bankz.close();
				return;
			}
			if(GrandExchange.isOpen())
			{
				if(GrandExchange.isReadyToCollect()) GrandExchange.collect();
				if(!GrandExchange.isReadyToCollect()) GrandExchangg.close();
				return;
			}
			if(Magic.isSpellSelected())
			{
				if(Magic.getSelectedSpellName().contains("High Level Alchemy"))
				{
					if(!Tabs.isOpen(Tab.INVENTORY))
					{
						Tabz.open(Tab.INVENTORY);
						Sleep.sleep(69, 111);
						return;
					}
					if(perfectHAClickSpot.contains(Mouse.getPosition()))
					{
						if(Mouse.click(ClickMode.LEFT_CLICK))
						{
							quickHATimer = new Timer((int) Calculations.nextGaussianRandom(3500, 500));
							Sleep.sleep(69, 420);
						}
						return;
					}
					if(Mouse.move(perfectHAClickSpot))
					{
						Sleep.sleep(111, 696);
					}
					return;
				}
			}
			if(!Tabs.isOpen(Tab.MAGIC))
			{
				Tabz.open(Tab.MAGIC);
				Sleep.sleep(69, 111);
				return;
			}
			if(Mouse.move(perfectHAClickSpot))
			{
				Sleep.sleep(69, 420);
			}
			if(perfectHAClickSpot.contains(Mouse.getPosition()))
			{
				if(Mouse.click(ClickMode.LEFT_CLICK))
				{
					Sleep.sleep(69, 420);
					if(Mouse.click(ClickMode.LEFT_CLICK))
					{
						quickHATimer = new Timer((int) Calculations.nextGaussianRandom(3500, 500));
						Sleep.sleep(69, 420);
					}
				}
				return;
			}
			return;
		}
		//have to drag another HA item to slot 11 to be able to double-click cast HA
		final List<Integer> keysFinal = id.allAlchs; //???
		Item alch = Inventory.get(i -> i!=null && 
				keysFinal.contains(i.getUnnotedItemID()));
		if(alch == null)
		{
			MethodProvider.log("Alch found but first alch found null!");
			return;
		}
		if(Bank.isOpen()) 
		{
			Bankz.close();
			return;
		}
		if(GrandExchange.isOpen())
		{
			if(GrandExchange.isReadyToCollect()) GrandExchange.collect();
			if(!GrandExchange.isReadyToCollect()) GrandExchangg.close();
			return;
		}
		if(!Tabs.isOpen(Tab.INVENTORY))
		{
			Tabz.open(Tab.INVENTORY);
			return;
		}
		if(Magic.isSpellSelected())
		{
			Magic.deselect();
			return;
		}
		if(Inventory.isItemSelected())
		{
			Inventory.deselect();
			return;
		}
		
		if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
		{
			Sleep.sleep(69, 696);
		}
		
		if(!Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
		{
			if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
			{
				Sleep.sleep(69, 696);
			}
		}
		if(Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
		{
			if(Mouse.drag(Inventory.slotBounds(11)))
			{
				Sleep.sleep(696, 696);
			}
		}
	}
	
	public static void teleportCamelot()
	{
		if(!Casting.haveRunesForSpell(Normal.CAMELOT_TELEPORT) || 
				!Equipment.contains(staffOfAir) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth))
		{
			MethodProvider.log("Missing something like wealth, staff of air, or cammy runes");
			fulfillCamelotCasting();
			return;
		}
		if(Bank.isOpen()) 
		{
			Bankz.close();
			Sleep.sleep(420, 696);
			return;
		}
		if(Tabs.isOpen(Tab.MAGIC))
		{
			API.randomAFK(10);
			Magic.castSpell(Normal.CAMELOT_TELEPORT);
			Sleep.sleep(696, 696);
			MethodProvider.sleepUntil(() -> !Players.localPlayer().isAnimating(), Sleep.calculate(2222, 2222));
			return;
		} 
		Tabz.open(Tab.MAGIC);
		Sleep.sleep(420, 696);
	}
	public static void trainLesserDemon()
	{
		Main.customPaintText1 = "~~Training Magic on Lesser Demon~~";
		if(!Casting.haveRunesForSpell(Casting.getSpellOfAutocastID(Casting.getHighestSpellConfig())) || 
				!Equipment.contains(staffOfAir) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth))
		{
			MethodProvider.log("Missing something like wealth, staff of air, or autocast runes");
			fulfillLesserDemonCasting();
			return;
		}
		if(Locations.lesserDemonWizardsTower.contains(Players.localPlayer()))
		{
			if(Dialogues.canContinue())
			{
				Dialogues.continueDialogue();
				Sleep.sleep(420, 696);
				return;
			}
			
			//check for glory in invy + not equipped -> equip it
			if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
					!InvEquip.equipmentContains(InvEquip.wearableGlory)) InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));

	       	int magic = Skills.getRealLevel(Skill.MAGIC);
			if(magic >= 33)
			{
				GroundItem gi = ItemsOnGround.getValuableNearbyItem(7000, Locations.lesserDemonsKillZoneWizardsTower, completedMagic);
				if(gi != null)
				{
					if(Casting.haveRunesForSpell(Normal.TELEKINETIC_GRAB))
					{
						if(Inventory.isFull())
						{
							if(Inventory.dropAll(id.jug))
							{
								MethodProvider.log("Dropped some jugs");
								return;
							}
							else if(Combatz.eatFood()) return;
						}
						else
						{
							final int count = Inventory.count(gi.getID());
							if(Magic.castSpellOn(Normal.TELEKINETIC_GRAB, gi))
							{
								MethodProvider.log("Cast telegrab onto item: " + gi.getName());
								MethodProvider.sleepUntil(() -> Inventory.count(gi.getID()) > count, Sleep.calculate(4444,2222));
							}
							if(Inventory.count(gi.getID()) > count)
							{
								MethodProvider.log("Got item from telegrab! " + gi.getName());
							}
							return;
						}
					}
					else {
						MethodProvider.log("Getting more law runes to grab valuable item (probably gone by the time we get back :-( ): " + gi.getName());
						fulfillLesserDemonCasting();
					}
					return;
				}
			}
			
			API.randomAFK(3);
			if(!Casting.isAutocastingHighest())
			{
				Main.customPaintText2 = "Setting Autocast: " + Casting.getSpellName(Casting.getHighestSpellConfig());
				Casting.setHighestAutocast();
				return;
			}
			Main.customPaintText2 = "Autocasting: "+Casting.getSpellName(Casting.getHighestSpellConfig());
			
			org.dreambot.api.wrappers.interactive.Character interacting = Players.localPlayer().getInteractingCharacter();
			if(interacting == null || 
					!interacting.getName().equals("Lesser demon"))
			{
				NPC lesserDemon = NPCs.closest(d -> d!=null && 
						d.exists() && 
						d.getHealthPercent() > 0 &&
						d.getName().equals("Lesser demon"));
				if(lesserDemon == null)
				{
					MethodProvider.log("In lesser demon 2nd floor area of wizzy tower and demon is null!");
					Sleep.sleep(2222, 2222);
					return;
				}
				if(lesserDemon.interact("Attack")) Sleep.sleep(696,420);
			}
			return;
		}
		if(Locations.wizardsTower2.contains(Players.localPlayer()))
		{
			if(Walking.walk(Locations.lesserDemonWizardsTower.getCenter())) Sleep.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTower1.contains(Players.localPlayer()))
		{
			if(Locations.wizardsTowerStairTile1.canReach())
			{
				GameObject stairs = GameObjects.closest("Staircase");
				if(stairs == null)
				{
					MethodProvider.log("In lesser demon 1st floor area of wizzy tower and stairs are null!");
					Sleep.sleep(2222, 2222);
					return;
				}
				if(stairs.interact("Climb-up"))
				{
					MethodProvider.sleepUntil(() -> Locations.wizardsTower2.contains(Players.localPlayer()),
							() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),50);
					return;
				}
			}
			if(Walking.walk(Locations.wizardsTowerStairTile1)) Sleep.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTower0.contains(Players.localPlayer()))
		{
			if(Locations.wizardsTowerStairTile0.canReach())
			{
				GameObject stairs = GameObjects.closest("Staircase");
				if(stairs == null)
				{
					MethodProvider.log("In lesser demon 1st floor area of wizzy tower and stairs are null!");
					Sleep.sleep(2222, 2222);
					return;
				}
				if(stairs.interact("Climb-up"))
				{
					MethodProvider.sleepUntil(() -> Locations.wizardsTower1.contains(Players.localPlayer()),
							() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),50);
					return;
				}
			}
			if(Walking.walk(Locations.wizardsTowerStairTile0)) Sleep.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTowerStairTile0.distance() >= 75)
		{
			if(!Walkz.useJewelry(InvEquip.passage, "Wizards\' Tower")) fulfillLesserDemonCasting();
		}
	}
	
	public static final int staffOfAir = 1381;
	public static boolean fulfillLesserDemonCasting()
    {
    	InvEquip.clearAll();

    	setBestMageArmour();
    	InvEquip.setEquipItem(EquipmentSlot.WEAPON, staffOfAir);
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.addInvyItem(id.waterRune, 500, (int) Calculations.nextGaussianRandom(3000, 500), false, (int) Calculations.nextGaussianRandom(5000, 100));
    	InvEquip.addInvyItem(id.earthRune, 500, (int) Calculations.nextGaussianRandom(3000, 500), false, (int) Calculations.nextGaussianRandom(5000, 100));
    	InvEquip.addInvyItem(id.fireRune, 500, (int) Calculations.nextGaussianRandom(5000, 500), false, (int) Calculations.nextGaussianRandom(10000, 100));
    	InvEquip.addInvyItem(id.mindRune, 500, (int) Calculations.nextGaussianRandom(2000, 500), false, (int) Calculations.nextGaussianRandom(3000, 100));
    	InvEquip.addInvyItem(id.chaosRune, 500, (int) Calculations.nextGaussianRandom(3000, 500), false, (int) Calculations.nextGaussianRandom(5000, 100));
    	InvEquip.addInvyItem(id.deathRune, 500, (int) Calculations.nextGaussianRandom(1000, 300), false, (int) Calculations.nextGaussianRandom(1750, 300));
    	InvEquip.addInvyItem(id.lawRune, 100, (int) Calculations.nextGaussianRandom(500, 300), false, (int) Calculations.nextGaussianRandom(1000, 200));
    	
    	InvEquip.addInvyItem(InvEquip.passage, 1, 1, false, 2);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 3, 10, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for lesser demon casting!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for lesser demon casting!");
			return false;
		}
    	
    }
	public static boolean fulfillCamelotCasting()
    {
    	InvEquip.clearAll();

    	setBestMageArmour();
    	InvEquip.setEquipItem(EquipmentSlot.WEAPON, staffOfAir);
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.addInvyItem(id.lawRune, 500, (int) Calculations.nextGaussianRandom(2000, 500), false, (int) Calculations.nextGaussianRandom(3000, 100));
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for camelot casting!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for camelot casting!");
			return false;
		}
    	
    }
	public static boolean fulfillHA()
    {
    	InvEquip.clearAll();

    	InvEquip.setEquipItem(EquipmentSlot.WEAPON, id.staffOfFire);
    	InvEquip.addInvyItem(id.natureRune,100,(int) Calculations.nextGaussianRandom(1200,200),false,(int) Calculations.nextGaussianRandom(1500,100));
    	InvEquip.shuffleFulfillOrder();
		if(InvEquip.fulfillSetup(false, 180000))
		{
			MethodProvider.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for quick HA!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for quick HA!");
			return false;
		}
    	
    }
	public static void setBestMageArmour()
    {
    	InvEquip.setEquipItem(EquipmentSlot.HAT, getBestHatSlot());
    	InvEquip.setEquipItem(EquipmentSlot.CHEST, getBestBodySlot());
    	InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
    	InvEquip.setEquipItem(EquipmentSlot.FEET, getBestBootSlot());
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, getBestNecklaceSlot());
    	InvEquip.setEquipItem(EquipmentSlot.LEGS, getBestLegsSlot());
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	
    }

	public static int getBestBodySlot()
	{
       	int magic = Skills.getRealLevel(Skill.MAGIC);
       	int def = Skills.getRealLevel(Skill.DEFENCE);
		if(def >= 20 && magic >= 40) return id.robeTopOfDarkness;
		if(def >= 10 && magic >= 20) return id.xericianTop;
		return id.blackRobe;
	}
	
	
	
	public static int getBestCapeSlot()
	{
		//guthix cloaks maybe wont ever buy due to extreme low traded qty :-(
		//if(Skills.getRealLevel(Skill.PRAYER) >= 40) return id.guthixCloak; 
		if(InvEquip.equipmentContains(id.randCapes)) return InvEquip.getEquipmentItem(id.randCapes);
    	if(InvEquip.invyContains(id.randCapes)) return InvEquip.getInvyItem(id.randCapes);
    	if(InvEquip.bankContains(id.randCapes)) return InvEquip.getBankItem(id.randCapes);
    	return id.randCape;
	}
	public static int getBestBootSlot()
	{
       	int magic = Skills.getRealLevel(Skill.MAGIC);
       	int def = Skills.getRealLevel(Skill.DEFENCE);
		if(def >= 20 && magic >= 40) return id.mysticBoots;
		return id.blueBoots;
	}
	public static int getBestHatSlot()
	{
       	int magic = Skills.getRealLevel(Skill.MAGIC);
       	int def = Skills.getRealLevel(Skill.DEFENCE);
		if(Skills.getRealLevel(Skill.PRAYER) >= 40 && magic >= 40) return id.ancientMitre;
		if(def >= 20 && magic >= 40) return id.hoodOfDarkness;
		return id.creamHat;
	}
	public static int getBestLegsSlot()
	{
       	int magic = Skills.getRealLevel(Skill.MAGIC);
       	int def = Skills.getRealLevel(Skill.DEFENCE);
		if(def >= 20 && magic >= 40) return id.robeBottomOfDarkness;
		if(def >= 10 && magic >= 20) return id.xericianRobe;
		return id.zamorakMonkBottom;
	}

	public static final int occultNecklace = 20137;
	public static int getBestNecklaceSlot()
	{
       	int magic = Skills.getRealLevel(Skill.MAGIC);
		if(magic >= 60) return occultNecklace;
		return InvEquip.glory;
	}
	
}
