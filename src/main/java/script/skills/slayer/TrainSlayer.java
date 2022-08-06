package script.skills.slayer;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Branch;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.Mobs;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.API.modes;
import script.utilities.Bankz;
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.ItemsOnGround;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

public class TrainSlayer extends Leaf{
	public static int slayerGoal = 18;
	public static boolean started = false;
	private final static Tree tree = new Tree();
	public static void onStart()
	{
		Combat.foods.clear();
        Combat.foods.add(TrainRanged.jugOfWine);
        Combat.highFoods.clear();
        Combat.highFoods.add(TrainRanged.seaTurtle);
		Main.clearCustomPaintText();
		SlayerSettings.getConfigs();
		started = true;
	}
	public static void initializeTree()
	{
		
	}
	
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_SLAYER;
	}
	
	@Override
	public int onLoop() {
		if(Skills.getRealLevel(Skill.RANGED) < 30) 
		{
    		MethodProvider.log("[SLAYER] -> Not 30 ranged! Switching to ranged training...");
            API.mode = modes.TRAIN_RANGE;
            return Timing.sleepLogNormalSleep();
    	}
		if(!started) onStart();
		TrainRanged.ranged = Skills.getRealLevel(Skill.RANGED);
		if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Slayer!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
		SlayerSettings.getConfigs();
		Main.customPaintText1 = "~~Training Slayer~~";
		if(SlayerSettings.slayerTaskQty > 0)
		{
			MethodProvider.log("Think we have slayer task: " + SlayerSettings.getTaskName() + " in qty: " +SlayerSettings.slayerTaskQty);
			if(SlayerSettings.slayerMonsterID == 10) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.zombiesEdgeville, 8,null);
			if(SlayerSettings.slayerMonsterID == 22) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.guardDogAreaHosidius, 10,null);
			if(SlayerSettings.slayerMonsterID == 12) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.forgottenSoulsLvl3, 6,ItemsOnGround.forgottenSoulsLoot);
			if(SlayerSettings.slayerMonsterID == 5) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.seagullsSarim, 6,null);
			if(SlayerSettings.slayerMonsterID == 4) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.spidersGE, 6,null);
			if(SlayerSettings.slayerMonsterID == 13) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.bearsFremmy, 6,null);
			if(SlayerSettings.slayerMonsterID == 37) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.caveCrawlersFremmyCave, 15,ItemsOnGround.caveCrawlersLoot);
			if(SlayerSettings.slayerMonsterID == 6) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.cowsArdy, 8, null);
			if(SlayerSettings.slayerMonsterID == 9) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.strongholdWolves, 8, null);
			if(SlayerSettings.slayerMonsterID == 68) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.lizards, 8, null);
			if(SlayerSettings.slayerMonsterID == 63) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.caveBugs, 8, ItemsOnGround.caveBugsLoot);
			if(SlayerSettings.slayerMonsterID == 2) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.goblins, 8, ItemsOnGround.goblinLoot);
			if(SlayerSettings.slayerMonsterID == 3) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.giantRats, 8, null);
			if(SlayerSettings.slayerMonsterID == 1) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.monkeysArdyZoo, 8, null);
			if(SlayerSettings.slayerMonsterID == 11) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.skeletonsEdgeville, 8, ItemsOnGround.skeletonLoot);
			if(SlayerSettings.slayerMonsterID == 62) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.caveSlimeArea, 8, ItemsOnGround.caveSlimeLoot);
			if(SlayerSettings.slayerMonsterID == 7) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.scorpions, 8, null);
			if(SlayerSettings.slayerMonsterID == 8) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.bats, 8, null);
			if(SlayerSettings.slayerMonsterID == 57) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.dwarfs, 8, ItemsOnGround.dwarfLoot);
			if(SlayerSettings.slayerMonsterID == 75) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.icefiends, 8, ItemsOnGround.icefiendLoot);
			if(SlayerSettings.slayerMonsterID == 76) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.strongholdWolves, 8, ItemsOnGround.icefiendLoot);
			if(SlayerSettings.slayerMonsterID == 53) Mobs.trainPlainMainlandSlayerTask(SlayerSettings.getTaskName(), Locations.kalphiteWorkersArea, 15, ItemsOnGround.icefiendLoot);
			
			
				
			Main.customPaintText4 = "Have slayer task! " + SlayerSettings.slayerTaskQty + " " + SlayerSettings.getTaskName()+"s and config value: "+SlayerSettings.slayerMonsterID;
			return Timing.sleepLogNormalInteraction();
		}
		
		Main.customPaintText4 = "Slayer task done! Getting new one~~";
		//have to grab new slayer task
		if(getAssignmentTurael()) return Timing.sleepLogNormalSleep();
		
		
		return Timing.sleepLogNormalSleep();
	}
	
	public static boolean getAssignmentTurael()
	{
		Main.customPaintText2 = "Grabbing assigment from Turael";
		
		Filter<NPC> turaelFilter = p -> 
			p != null &&
			p.getName().equals("Turael") && 
			p.hasAction("Assignment");
		NPC turael = NPCs.closest(turaelFilter);
		if(turael != null)
		{
			if(Dialogues.canContinue())
			{
				if(Dialogues.continueDialogue()) Main.customPaintText2 = "Continuing dialogue";
				Sleep.sleep(420, 696);
				return false;
			}
			if(Dialogues.isProcessing())
			{
				if(Dialogues.continueDialogue()) Main.customPaintText2 = "Dialogue processing...";
				Sleep.sleep(420, 696);
				return false;
			}
			if(Dialogues.chooseOption("What\'s a slayer?"))
			{
				Main.customPaintText2 = "Chose option \"What\'s a slayer?\"";
				Sleep.sleep(420, 696);
				return false;
			}
			if(Dialogues.chooseOption("Wow, can you teach me?"))
			{
				Main.customPaintText2 = "Chose option \"Wow, can you teach me?\"";
				Sleep.sleep(420, 696);
				return false;
			}
			if(turael.canReach())
			{
				if(turael.interact("Assignment")) 
				{
					MethodProvider.sleepUntil(Dialogues::inDialogue, () -> Players.localPlayer().isMoving(), Sleep.calculate(2222, 2222),50);
					return true;
				}
			}
			else if(Walking.shouldWalk(6) && Walking.walk(turael)) Sleep.sleep(420, 696);
		}
		else 
		{
			if(Locations.burthorpeTeleSpot.distance(Players.localPlayer().getTile()) > 50)
			{
				if(Walkz.useJewelry(InvEquip.games, "Burthorpe"))
				{
					Sleep.sleep(420, 696);
					return true;
				}
				
				if(!InvEquip.checkedBank())
				{
					Sleep.sleep(696, 420);
					return false;
				}
				final int games = InvEquip.getBankItem(InvEquip.wearableGames);
				if(games != 0)
				{
					InvEquip.withdrawOne(games, 180000);
					return false;
				}
				InvEquip.buyItem(InvEquip.games8, 2, 180000);
			} else if(Walking.shouldWalk(6) && Walking.walk(Locations.turaelArea.getCenter())) Sleep.sleep(420, 696);
		}
		return false;
	}
	public static boolean buyItemTurael(int itemID, int qty)
	{
		if(qty <= 0)
		{
			MethodProvider.log("Called BuyItemTurael with item: " + new Item(itemID,1).getName() + " in quantity 0!");
			return false;
		}
		MethodProvider.log("Grabbing equipment from Turael: " +qty + " " +new Item(itemID,1).getName());
		
		Main.customPaintText2 = "Grabbing equipment from Turael: " +qty + " "+new Item(itemID,1).getName();
		Timer timeout = new Timer(180000);
		while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleep.sleep(69, 420);
			if(!InvEquip.checkedBank()) continue;
			if((Inventory.count(itemID) + Bank.count(itemID)) >= qty) return true;
			if(Inventory.count(InvEquip.coins) >= 2000)
			{
				//inventory is full
				if(Inventory.isFull())
				{
					
					if(Inventory.count(itemID) <= 0)
					{
						MethodProvider.log("Not enough invy space for buying " + new Item(itemID,1).getName()+" from Turael!");
						
						if(GrandExchange.isOpen())
						{
							GrandExchange.close();
							Sleep.sleep(420,696);
							continue;
							
						}
						if(Shop.isOpen())
						{
							Shop.close();
							Sleep.sleep(420,696);
							continue;
						}
						if(Bank.isOpen())
						{
							if(Bank.depositAll(i -> i != null && 
									ItemsOnGround.allSlayerLoot.contains(i.getID())))
							{
								MethodProvider.log("Deposited some allSlayerLoot");
							}
							else 
							{
								MethodProvider.log("Did not deposit any allSlayerLoot");
								if(Bank.deposit(i -> i != null && 
										i.getID() == TrainRanged.jugOfWine, (1)))
								{
									MethodProvider.log("Deposited 1 jug of wine");
								}
								else 
								{
									MethodProvider.log("Did not deposit a jug of wine");
								}
							}
							MethodProvider.sleep(Timing.sleepLogNormalInteraction());
							continue;
						}
						if(Inventory.count(TrainRanged.jugOfWine) > 0) 
						{
							if(Inventory.drop(TrainRanged.jugOfWine)) Sleep.sleep(222, 696);
						}
						continue;
					}
					
				}
				if(Shop.isOpen())
				{
					if(!Shop.contains(itemID))
					{
						Shop.close();
						MethodProvider.sleep(Timing.sleepLogNormalSleep());
						continue;
					}

					MethodProvider.log("Turael shop open");
					String tmp = "Buy ";
					String buyString = tmp;
					if(qty == 1) buyString = tmp.concat("1");
					else buyString = tmp.concat("50");
					if(Shop.interact(i -> i != null && i.getID() == itemID, buyString))
					{
						Sleep.sleep(69, 696);
					}

					continue;
				}
				Filter<NPC> turaelFilter = p -> 
					p != null &&
					p.getName().equals("Turael") && 
					p.hasAction("Trade");
				NPC turael = NPCs.closest(turaelFilter);
				if(turael != null)
				{
					if(turael.canReach())
					{
						if(turael.interact("Trade")) 
						{

							MethodProvider.log("Turael Traded");
							MethodProvider.sleepUntil(Shop::isOpen,() -> Players.localPlayer().isMoving(), Sleep.calculate(2222, 2222),50);
							Sleep.sleep(69, 696);
							continue;
						}
					}
					else if(Walking.shouldWalk(6) && Walking.walk(turael)) Sleep.sleep(420, 696);
				}
				else 
				{
					if(Locations.burthorpeTeleSpot.distance(Players.localPlayer().getTile()) > 50)
					{
						if(Walkz.useJewelry(InvEquip.games, "Burthorpe"))
						{
							Sleep.sleep(420, 696);
							continue;
						}
						
						final int games = InvEquip.getBankItem(InvEquip.wearableGames);
						if(games != 0)
						{
							InvEquip.withdrawOne(games, 180000);
							continue;
						}
						InvEquip.buyItem(InvEquip.games8, 2, 180000);
					} else if(Walking.shouldWalk(6) && Walking.walk(Locations.turaelArea.getCenter())) Sleep.sleep(420, 696);
				}
				continue;
			}
			MethodProvider.log("Getting 5000 coins for buying item: " + new Item(itemID,1).getName()+" from Turael!");
			
			
			if(Bank.isOpen())
			{
				if(Bank.count(InvEquip.coins) < 5000) 
				{
					MethodProvider.log("Not enough coins (5000) for buying " + new Item(itemID,1).getName()+" from Turael!");
					ScriptManager.getScriptManager().stop();
					return false;
				}
				if(Bank.withdraw(InvEquip.coins, 5000))
				{
					MethodProvider.log("Withdrew 5000 coins!");
					MethodProvider.sleep(Timing.sleepLogNormalInteraction());
				}
				continue;
			}
			if(Bankz.openClosest(25)) MethodProvider.sleep(Timing.sleepLogNormalSleep());
			continue;
			
		
		}
		return false;
	}
	public static boolean fulfillRangedLizardsTask()
    {
    	InvEquip.clearAll();
    	
    	TrainRanged.setBestRangedEquipment();
    	
    	InvEquip.addInvyItem(TrainRanged.rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 5);
    	InvEquip.addInvyItem(InvEquip.antidote4, 1, 1, false, 5);
    	InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);
    	InvEquip.addInvyItem(InvEquip.shantayPass, 1, 1, false, 50);
    	InvEquip.addInvyItem(InvEquip.waterskin4, 5, 6, false, 50);
    	InvEquip.addInvyItem(InvEquip.iceCooler, 200, 200, false, 0);
    	
    	
    	for(int f : Combat.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : TrainRanged.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(TrainRanged.jugOfWine, 10, 17, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (shantay pass lizards task)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (shantay pass lizards task)");
			return false;
		}
    	
    }
	public static boolean fulfillRangedCaveBugsTask()
    {
    	InvEquip.clearAll();
    	
    	TrainRanged.setBestRangedEquipment();
    	if(!SlayerSettings.unlockedLumbyCave())
    	{
    		InvEquip.addInvyItem(id.rope, 1, 1, false,5);
    	}
    	InvEquip.addInvyItem(TrainRanged.rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 5);
    	InvEquip.addInvyItem(id.litCandleLantern, 1, 1, false, 1);
    	InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);
    	InvEquip.addInvyItem(Candlez.tinderbox, 1, 1, false, 1);
    	
    	for(int f : Combat.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : TrainRanged.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(TrainRanged.jugOfWine, 10, 17, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (cave bugs task)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (cave bugs task)");
			return false;
		}
    	
    }
	public static boolean fulfillRangedCaveSlimesTask()
    {
    	InvEquip.clearAll();
    	
    	TrainRanged.setBestRangedEquipment();
    	if(!SlayerSettings.unlockedLumbyCave())
    	{
    		InvEquip.addInvyItem(id.rope, 1, 1, false,5);
    	}
    	InvEquip.addInvyItem(TrainRanged.rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 5);
    	InvEquip.addInvyItem(id.litCandleLantern, 1, 1, false, 1);
    	InvEquip.addInvyItem(InvEquip.antidote4, 1, 1, false, 5);
    	InvEquip.addInvyItem(InvEquip.stamina4, 1, 1, false, 5);
    	InvEquip.addInvyItem(Candlez.tinderbox, 1, 1, false, 1);
    	
    	for(int f : Combat.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : TrainRanged.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(TrainRanged.jugOfWine, 10, 17, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (cave bugs task)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (cave bugs task)");
			return false;
		}
    	
    }
}
