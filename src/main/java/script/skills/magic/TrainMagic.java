package script.skills.magic;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.input.event.impl.mouse.impl.click.ClickMode;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
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
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.GrandExchangg;
import script.utilities.InvEquip;
import script.utilities.ItemsOnGround;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Sleepz;
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
    	Main.clearCustomPaintText();
    	return true;
    }
    @Override
    public int onLoop() {
        if(DecisionLeaf.taskTimer.finished())
    	{
    		Logger.log("[TIMEOUT] -> Magic!");
            if(onExit()) API.mode = null;
            return Sleepz.sleepTiming();
    	}
       	int magic = Skills.getRealLevel(Skill.MAGIC);
    	if(magic >= DecisionLeaf.mageSetpoint) {
            Logger.log("[COMPLETE] -> lvl "+DecisionLeaf.mageSetpoint+" magic!");
            if(onExit()) 
            {
            	completedMagic = true;
               	API.mode = null;
            }
            return Sleepz.sleepTiming();
        }

    	if(magic < 45) 
    	{
    		trainLesserDemon();
            return Sleepz.sleepTiming();
    	}
    	if(magic < 55) 
    	{
    		teleportCamelot();
            return Sleepz.sleepTiming();
    	}
    	
    	//55 magic and beyond decide to tele-alch camelot or to alch normally
    	if(!chancedTeleAlch)
    	{
			int chance = (int) Calculations.nextGaussianRandom(50, 25);
			if(chance > 60) //less chance to tele-alch than to alch normally
			{
				teleAlch = true;
				Logger.log("[MAGIC] -> lvl 55+, Chose to Tele-Alch~");
			}
			else Logger.log("[MAGIC] -> lvl 55+, Chose to High Alch normally~");
			chancedTeleAlch = true;
		}
    	if(teleAlch) teleAlch();
    	else highAlch();
        return Sleepz.sleepTiming();
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
	public static boolean teleAlch = false;
	public static boolean chancedTeleAlch = false;
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
		Logger.log("Done checking items and sorting by profit! ~~");
		for(Item i : profitItems)
		{
			Logger.log("Item name,profit,HA value: " +i.getName() + ","+(i.getHighAlchValue()-i.getLivePrice()-natRunePrice)+","+i.getHighAlchValue());
		}

		Logger.log("Done  ~~");
		Sleepz.sleep(6969, 4420);
	}
	public static boolean teleElseAlch = false;
	public static void teleAlch()
	{
		if(!InvEquip.checkedBank()) return;
		if(!Equipment.contains(id.staffOfFire) || 
				Inventory.count(id.natureRune) <= 0 ||
				Inventory.count(id.lawRune) <= 0 ||
				Inventory.count(id.airRune) < 5)
		{
			fulfillTeleHA();
			return;
		}
		List<Integer> profitKeys = new ArrayList(id.approvedAlchs.keySet());
		profitKeys.addAll(id.xpAlchs.keySet());
		Collections.shuffle(profitKeys);
		boolean foundAlch = false;
		int alchsCount = 0;
		boolean bankedAlchs = false;
		for(Integer i : profitKeys)
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
			Logger.log("Found banked alchs");
			if(Locations.HASpot1.contains(Players.getLocal()))
			{
				if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
				{
					if(GameObjects.closest("Ladder").interact("Climb-down"))
					{
						Sleep.sleepUntil(() -> !Locations.HASpot1.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(),
								Sleepz.calculate(2222, 2222),50);
					}
				}
				return;
			}
			if(Bankz.openClosest(85))
			{
				if(Bank.getWithdrawMode() == BankMode.NOTE)
				{
					for(Integer i : profitKeys)
					{
						if(Bank.count(i) > 0)
						{
							if(Inventory.isFull())
							{
								InvEquip.depositExtraJunk();
								return;
							}
							Logger.log("Attempting withdraw of item: "+new Item(i,1).getName());
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
		if(!foundAlch)
		{
			BuyHighAlchs.buyItems(true);
			return;
		}
		
		if(Dialogues.canContinue()) 
		{
			Dialogues.continueDialogue();
			return;
		}
		if(Dialogues.isProcessing()) return;
		
		if(shouldChangeHAThreshold())
		{
			changeHAThreshold();
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
		
		if(profitKeys.contains(new Item(Inventory.getIdForSlot(11),1).getUnnotedItemID()))
		{
			//can HA now :-)
			if(HATimer == null)
			{
				HATimer = new Timer(2147483646);
			}
			//invy must be open if HA spell selected
			if(Magic.isSpellSelected() && Magic.getSelectedSpellName().contains("High Level Alchemy"))
			{
				final Rectangle HABounds = Inventory.slotBounds(11);
				if(Players.getLocal().isAnimating())
				{
					if(!HABounds.contains(Mouse.getPosition()) && Mouse.move(HABounds))
					{
						Sleepz.sleep(111, 420);
					}
					Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), Sleepz.calculate(4444, 3333));
				}
				if(!HABounds.contains(Mouse.getPosition()) && Mouse.move(HABounds))
				{
					Sleepz.sleep(111, 420);
				}
				if(Inventory.slotBounds(11).contains(Mouse.getPosition()))
				{
					if(Mouse.click(ClickMode.LEFT_CLICK))
					{
						HACount++;
						teleElseAlch = true;
						Sleepz.sleep(42, 420);
					}
					return;
				}
				return;
			}
			if(!Tabs.isOpen(Tab.MAGIC)) Tabs.openWithFKey(Tab.MAGIC);
			//teleport spell rather than cast HA
			if(teleElseAlch)
			{
				if(Magic.castSpell(Normal.CAMELOT_TELEPORT))
				{
					Sleepz.sleep(42, 696);
					teleElseAlch = false;
				}
				return;
			}
			if(Magic.castSpell(Normal.HIGH_LEVEL_ALCHEMY))
			{
				Sleepz.sleep(42, 420);
			}
			return;
		}
		
		//item dragging function - rearrange HA item to beneath HA spell
		final List<Integer> keysFinal = profitKeys; //final required for filter
		Item alch = Inventory.get(i -> i!=null && 
				keysFinal.contains(i.getUnnotedItemID()));
		if(alch == null)
		{
			Logger.log("Alch found but first alch found null!");
			return;
		}
		if(!Tabs.isOpen(Tab.INVENTORY))
		{
			Tabz.open(Tab.INVENTORY);
			return;
		}
		
		if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
		{
			Sleepz.sleep(69, 696);
		}
		
		if(!Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
		{
			if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
			{
				Sleepz.sleep(69, 696);
			}
		}
		if(Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
		{
			if(Mouse.drag(Inventory.slotBounds(11)))
			{
				Sleepz.sleep(696, 696);
			}
		}
	}
	/**
	 * Pretend that we cannot automatically detect the HA threshold,
	 * and wait until we are prompted to change it, look at it, then proceed with HA, and change it
	 * @return
	 */
	public static boolean shouldChangeHAThreshold()
	{
		if(Widgets.get(193,2) != null && 
				Widgets.get(193,2).isVisible() && 
				Widgets.get(193,2).getText().contains("That item is considered <col=6f0000>valuable") ||
				(Dialogues.areOptionsAvailable() && Dialogues.chooseFirstOptionContaining("Proceed to cast High Alchemy on it.")))
		{
			changeVarbit = true;
		}
		return changeVarbit;
	}
	public static void changeHAThreshold()
	{
		if(PlayerSettings.getBitValue(HAWarningThresholdVarbit) >= 50000)
		{
			Logger.log("Succesfully changed HA Warnings to greater than 50k!");
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
			Logger.log("Options are available but not the set value threshold one we want in HA varbit function...");
			Map.interact(Players.getLocal().getTile());
			Sleepz.sleep(420, 696);
			return;
		}
		if(Widgets.get(162, 41) != null && 
    			Widgets.get(162, 41).isVisible() && 
    			Widgets.get(162, 41).getText().contains("Set value threshold for alchemy warnings:"))
    	{
			if(Widgets.get(162, 42).getText().length() > 1)
			{
				Keyboard.typeSpecialKey(8);
				return;
			}
			else
			{
				Keyboard.type(Integer.toString(API.roundToMultiple((int) Calculations.nextGaussianRandom(75000,20000),500)), true);
				Sleepz.sleep(1111, 4444);
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
			Sleep.sleepUntil(Dialogues::inDialogue,Sleepz.calculate(2222, 2222));
			return;
		}
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
				Logger.log("[MAGIC] -> Chose to High Alch Magic/Yew longbows~");
				}
			else Logger.log("[MAGIC] -> Chose to High Alch profitable items~");
			chancedXPAlch = true;
		}
		if(!InvEquip.checkedBank()) return;
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
				Logger.log("Found banked alchs");
				if(Locations.HASpot1.contains(Players.getLocal()))
				{
					if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
					{
						if(GameObjects.closest("Ladder").interact("Climb-down"))
						{
							Sleep.sleepUntil(() -> !Locations.HASpot1.contains(Players.getLocal()),
									() -> Players.getLocal().isMoving(),
									Sleepz.calculate(2222, 2222),50);
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
								Logger.log("Attempting withdraw of item: "+new Item(i,1).getName());
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
				if(shouldChangeHAThreshold())
				{
					changeHAThreshold();
					return;
				}
				if(xpAlch)
				{
					if(Locations.HASpot1.contains(Players.getLocal()))
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
											Sleepz.sleep(1111, 1111);
										}
										return;
									}
									if(Mouse.move(Inventory.slotBounds(11)))
									{
										Sleepz.sleep(111, 1111);
									}
									return;
								}
							}
							if(Magic.castSpell(Normal.HIGH_LEVEL_ALCHEMY))
							{
								Sleepz.sleep(42, 696);
							}
							return;
						}
						final List<Integer> keysFinal = keys; //???
						Item alch = Inventory.get(i -> i!=null && 
								keysFinal.contains(i.getUnnotedItemID()));
						if(alch == null)
						{
							Logger.log("Alch found but first alch found null!");
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
							Sleepz.sleep(69, 696);
						}
						
						if(!Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
						{
							if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
							{
								Sleepz.sleep(69, 696);
							}
						}
						if(Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
						{
							if(Mouse.drag(Inventory.slotBounds(11)))
							{
								Sleepz.sleep(696, 696);
							}
						}
						return;
					}
					if(Locations.HALadderSpot.distance() < 8)
					{
						GameObject ladder = GameObjects.closest("Ladder");
						if(ladder == null)
						{
							Logger.log("Ladder null in HA spot lower area!");
							return;
						}
						if(ladder.interact("Climb-up"))
						{
							Sleep.sleepUntil(() -> Locations.HASpot1.contains(Players.getLocal()), 
									() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),69);
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
									Sleepz.sleep(1111, 1111);
								}
								return;
							}
							if(Mouse.move(Inventory.slotBounds(11)))
							{
								Sleepz.sleep(111, 1111);
							}
							return;
						}
					}
					if(Magic.castSpell(Normal.HIGH_LEVEL_ALCHEMY))
					{
						Sleepz.sleep(42, 696);
					}
					return;
				}
				final List<Integer> keysFinal = keys; //???
				Item alch = Inventory.get(i -> i!=null && 
						keysFinal.contains(i.getUnnotedItemID()));
				if(alch == null)
				{
					Logger.log("Alch found but first alch found null!");
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
					Sleepz.sleep(69, 696);
				}
				
				if(!Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
				{
					if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
					{
						Sleepz.sleep(69, 696);
					}
				}
				if(Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
				{
					if(Mouse.drag(Inventory.slotBounds(11)))
					{
						Sleepz.sleep(696, 696);
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
	public static final Rectangle perfectHAClickSpot = new Rectangle(709,302,13,14); //only for FIXED MODE!!
	/**
	 * Specifically for Agility training!
	 * returns true if have HA items in inventory.
	 * Otherwise withdraws all from bank.
	 * Otherwise gets more longbows.
	 * @return
	 */
	public static boolean getAgilityHAItems()
	{
		if(!InvEquip.checkedBank() ||
				(quickHATimer != null && !quickHATimer.finished())) return false;
		if(Equipment.contains(id.staffOfFire) && Inventory.count(id.natureRune) > 0)
		{
			//combine profit and longbow alch lists
			List<Integer> keys = new ArrayList(id.approvedAlchs.keySet());
			keys.addAll(id.xpAlchs.keySet());
			Collections.shuffle(keys);
			boolean invyAlchs = false;
			boolean bankedAlchs = false;
			for(Integer i : keys)
			{
				final Item I = new Item(i,1);
				if(id.needAtLeast1Alchs.contains(i))
				{
					final int totalCount = Inventory.count(i) + Inventory.count(I.getNotedItemID()) + 
							Equipment.count(i) + Bank.count(i);
					if(totalCount <= 1) continue;
					
				}
				
				else if(Bank.count(i) > 0) bankedAlchs = true;
				if(id.needAtLeast1Alchs.contains(i))
				{
					if(Bank.count(i) > 1) bankedAlchs = true;
				}
				if(Inventory.count(i) > 0 || Inventory.count(new Item(i,1).getNotedItemID()) + Bank.count(i) + Equipment.count(i) > 0)
				{
					invyAlchs = true;
				}
			}
			if(bankedAlchs)
			{
				Logger.log("Found banked alchs");
				if(Locations.HASpot1.contains(Players.getLocal()))
				{
					if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
					{
						if(GameObjects.closest("Ladder").interact("Climb-down"))
						{
							Sleep.sleepUntil(() -> !Locations.HASpot1.contains(Players.getLocal()),
									() -> Players.getLocal().isMoving(),
									Sleepz.calculate(2222, 2222),50);
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
							if(id.needAtLeast1Alchs.contains(i) && Bank.count(i) > 1)
							{
								if(Inventory.isFull())
								{
									InvEquip.depositExtraJunk();
									return false;
								}
								final Item I = Bank.get(i);
								Logger.log("Attempting withdraw-all-but-1 of item: "+I.getName());
								if(Bank.needToScroll(I))
								{
									if(!Bank.scroll(i))	return false;
								}
								if(I.interact("Withdraw-All-but-1"))
								{
									Sleep.sleepUntil(() -> Bank.count(i) == 1, Sleepz.calculate(2222, 2222));
									Sleepz.sleep(69, 1111);
								}
							}
							if(Bank.count(i) > 0)
							{
								if(Inventory.isFull())
								{
									InvEquip.depositExtraJunk();
									return false;
								}
								Logger.log("Attempting withdraw of item: "+new Item(i,1).getName());
								if(Bank.withdrawAll(i))
								{
									Sleep.sleepUntil(() -> Bank.count(i) <= 0, Sleepz.calculate(2222, 2222));
									Sleepz.sleep(69, 1111);
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
			if(invyAlchs)
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
		if(shouldChangeHAThreshold())
		{
			changeHAThreshold();
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
						Sleepz.sleep(69, 111);
						return;
					}
					if(perfectHAClickSpot.contains(Mouse.getPosition()))
					{
						if(Mouse.click(ClickMode.LEFT_CLICK))
						{
							quickHATimer = new Timer((int) Calculations.nextGaussianRandom(3500, 500));
							Sleepz.sleep(69, 420);
						}
						return;
					}
					if(Mouse.move(perfectHAClickSpot))
					{
						Sleepz.sleep(111, 696);
					}
					return;
				}
			}
			if(!Tabs.isOpen(Tab.MAGIC))
			{
				Tabz.open(Tab.MAGIC);
				Sleepz.sleep(69, 111);
				return;
			}
			if(Mouse.move(perfectHAClickSpot))
			{
				Sleepz.sleep(69, 420);
			}
			if(perfectHAClickSpot.contains(Mouse.getPosition()))
			{
				if(Mouse.click(ClickMode.LEFT_CLICK))
				{
					Sleepz.sleep(69, 420);
					if(Mouse.click(ClickMode.LEFT_CLICK))
					{
						quickHATimer = new Timer((int) Calculations.nextGaussianRandom(3500, 500));
						Sleepz.sleep(69, 420);
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
			Logger.log("Alch found but first alch found null!");
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
			Sleepz.sleep(69, 696);
		}
		
		if(!Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
		{
			if(Mouse.move(Inventory.slotBounds(alch.getSlot())))
			{
				Sleepz.sleep(69, 696);
			}
		}
		if(Inventory.slotBounds(alch.getSlot()).contains(Mouse.getPosition()))
		{
			if(Mouse.drag(Inventory.slotBounds(11)))
			{
				Sleepz.sleep(696, 696);
			}
		}
	}
	
	public static void teleportCamelot()
	{
		if(!Casting.haveRunesForSpell(Normal.CAMELOT_TELEPORT) || 
				!Equipment.contains(staffOfAir) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth))
		{
			Logger.log("Missing something like wealth, staff of air, or cammy runes");
			fulfillCamelotCasting();
			return;
		}
		if(Bank.isOpen()) 
		{
			Bankz.close();
			Sleepz.sleep(420, 696);
			return;
		}
		if(Tabs.isOpen(Tab.MAGIC))
		{
			Magic.castSpell(Normal.CAMELOT_TELEPORT);
			Sleepz.sleep(696, 696);
			Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), Sleepz.calculate(2222, 2222));
			return;
		} 
		Tabz.open(Tab.MAGIC);
		Sleepz.sleep(420, 696);
	}
	public static void trainLesserDemon()
	{
		Main.paint_task = "~~Training Magic on Lesser Demon~~";
		if(!Casting.haveRunesForSpell(Casting.getSpellOfAutocastID(Casting.getHighestSpellConfig())) || 
				!Equipment.contains(staffOfAir) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth))
		{
			Logger.log("Missing something like wealth, staff of air, or autocast runes");
			fulfillLesserDemonCasting();
			return;
		}
		if(Locations.lesserDemonWizardsTower.contains(Players.getLocal()))
		{
			if(Dialogues.canContinue())
			{
				Dialogues.continueDialogue();
				Sleepz.sleep(420, 696);
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
								Logger.log("Dropped some jugs");
								return;
							}
							else if(Combatz.eatFood()) return;
						}
						else
						{
							final int count = Inventory.count(gi.getID());
							if(Magic.castSpellOn(Normal.TELEKINETIC_GRAB, gi))
							{
								Logger.log("Cast telegrab onto item: " + gi.getName());
								Sleep.sleepUntil(() -> Inventory.count(gi.getID()) > count, Sleepz.calculate(4444,2222));
							}
							if(Inventory.count(gi.getID()) > count)
							{
								Logger.log("Got item from telegrab! " + gi.getName());
							}
							return;
						}
					}
					else {
						Logger.log("Getting more law runes to grab valuable item (probably gone by the time we get back :-( ): " + gi.getName());
						fulfillLesserDemonCasting();
					}
					return;
				}
			}

			if(!Casting.isAutocastingHighest())
			{
				Main.paint_itemsCount = "Setting Autocast: " + Casting.getSpellName(Casting.getHighestSpellConfig());
				Casting.setHighestAutocast();
				return;
			}
			Main.paint_itemsCount = "Autocasting: "+Casting.getSpellName(Casting.getHighestSpellConfig());
			
			org.dreambot.api.wrappers.interactive.Character interacting = Players.getLocal().getInteractingCharacter();
			if(interacting == null || 
					!interacting.getName().equals("Lesser demon"))
			{
				NPC lesserDemon = NPCs.closest(d -> d!=null && 
						d.exists() && 
						d.getHealthPercent() > 0 &&
						d.getName().equals("Lesser demon"));
				if(lesserDemon == null)
				{
					Logger.log("In lesser demon 2nd floor area of wizzy tower and demon is null!");
					Sleepz.sleep(2222, 2222);
					return;
				}
				if(lesserDemon.interact("Attack")) Sleepz.sleep(696,420);
			}
			return;
		}
		if(Locations.wizardsTower2.contains(Players.getLocal()))
		{
			if(Walking.walk(Locations.lesserDemonWizardsTower.getCenter())) Sleepz.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTower1.contains(Players.getLocal()))
		{
			if(Locations.wizardsTowerStairTile1.canReach())
			{
				GameObject stairs = GameObjects.closest("Staircase");
				if(stairs == null)
				{
					Logger.log("In lesser demon 1st floor area of wizzy tower and stairs are null!");
					Sleepz.sleep(2222, 2222);
					return;
				}
				if(stairs.interact("Climb-up"))
				{
					Sleep.sleepUntil(() -> Locations.wizardsTower2.contains(Players.getLocal()),
							() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),50);
					return;
				}
			}
			if(Walking.walk(Locations.wizardsTowerStairTile1)) Sleepz.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTower0.contains(Players.getLocal()))
		{
			if(Locations.wizardsTowerStairTile0.canReach())
			{
				GameObject stairs = GameObjects.closest("Staircase");
				if(stairs == null)
				{
					Logger.log("In lesser demon 1st floor area of wizzy tower and stairs are null!");
					Sleepz.sleep(2222, 2222);
					return;
				}
				if(stairs.interact("Climb-up"))
				{
					Sleep.sleepUntil(() -> Locations.wizardsTower1.contains(Players.getLocal()),
							() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),50);
					return;
				}
			}
			if(Walking.walk(Locations.wizardsTowerStairTile0)) Sleepz.sleep(420, 696);
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
			Logger.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for lesser demon casting!");
			return true;
		} else 
		{
			Logger.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for lesser demon casting!");
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
			Logger.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for camelot casting!");
			return true;
		} else 
		{
			Logger.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for camelot casting!");
			return false;
		}
    	
    }
	public static boolean fulfillTeleHA()
    {
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.WEAPON, id.staffOfFire);
    	final int lawQty = (int) Calculations.nextGaussianRandom(1200,200);
    	final int refillQty = (int) Calculations.nextGaussianRandom(1500, 200);

    	final int airQty = (int) Calculations.nextGaussianRandom(6000,1000);
    	final int refillAirQty = (int) Calculations.nextGaussianRandom(8000, 1000);
    	InvEquip.addInvyItem(id.natureRune,100,(int) Calculations.nextGaussianRandom(1200,200),false,(int) Calculations.nextGaussianRandom(1500,100));
    	InvEquip.addInvyItem(id.lawRune,100,lawQty,false,refillQty);
    	InvEquip.addInvyItem(id.airRune,500,airQty,false,refillAirQty);
    	InvEquip.shuffleFulfillOrder();
		if(InvEquip.fulfillSetup(false, 180000))
		{
			Logger.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for Tele-HA!"); 
			return true;
		} else 
		{
			Logger.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for Tele-HA!");
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
			Logger.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for quick HA!");
			return true;
		} else 
		{
			Logger.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for quick HA!");
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
