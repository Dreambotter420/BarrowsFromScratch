package script.quest.fightarena;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Tabz;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Completes Fight Arena
 * @author Dreambotter420
 * ^_^
 */
public class FightArena extends Leaf {
    public static final int khaliBrew = 77;
    public static final int khazardCellKeys = 76;
	public static boolean onExit() {
		return true;
	}

	@Override
	public boolean isValid() {
		return API.mode == API.modes.FIGHT_ARENA;
	}
	public static boolean completed()
	{
		if(Questz.checkCloseQuestCompletion()) return false;
    	if(!Inventory.contains(khazardCellKeys) && 
    			!Inventory.contains(khazardHelm) &&
    			!Inventory.contains(khazardPlatebody) &&
    			!Equipment.contains(khazardHelm) &&
    			!Equipment.contains(khazardPlatebody) &&
				Locations.GE.distance(Players.getLocal()) > 20) {
			if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) {
				Logger.log("Finished Fight Arena with no teleport to GE :-( Ending quest branch early...");
				return true;
			}
    		return false;
    	}
    	if(Inventory.contains(khazardCellKeys) || Inventory.contains(khazardHelm) || 
    			Inventory.contains(khazardPlatebody))
    	{
    		if(Inventory.drop(i -> i != null && 
    				(i.getID() == khazardCellKeys ||
    				i.getID() == khazardHelm || 
    				i.getID() == khazardPlatebody))) Sleepz.sleep(666, 696);
    		return false;
    	}
    	if(Equipment.contains(khazardHelm) || 
    			Equipment.contains(khazardPlatebody))
    	{
    		if(InvEquip.freeInvySpaces(2))
        	{
        		if(!Tabs.isOpen(Tab.EQUIPMENT))
        		{
        			if(Tabz.open(Tab.EQUIPMENT)) Sleepz.sleep(69, 696);
        			return false;
        		}
        		if(Equipment.unequip(i -> i!=null && 
        				(i.getID() == khazardHelm || 
        				i.getID() == khazardPlatebody)))
        		{
        			Sleepz.sleep(666, 696);
        		}
        	}
    	}
    	return false;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            Logger.log("[TIMEOUT] -> Fight Arena");
           	API.mode = null;
            return Sleepz.sleepTiming();
        }
        if (completed()) {
            Logger.log("[FINISHED] -> Fight Arena!!");
           	API.mode = null;
           	Main.paint_task = "~~~~~~~~~~";
    		Main.paint_itemsCount = "~Quest Complete~";
    		Main.paint_subTask = "~Fight Arena~";
    		Main.paint_levels = "~~~~~~~~~~";
            return Sleepz.sleepTiming();
        }
        if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("Fight Arena");
        
        if(Inventory.contains(TrainRanged.getBestDart()))
    	{
    		InvEquip.equipItem(TrainRanged.getBestDart());
    		return Sleepz.sleepTiming();
    	}
        
        if((!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth)) ||
        		!Equipment.contains(TrainRanged.getBestDart()) || 
        		(Combatz.shouldDrinkRangedBoost() && !InvEquip.invyContains(id.rangedPots)) || 
        		(Combatz.shouldDrinkPrayPot() && !InvEquip.invyContains(id.prayPots))) 
		{
        	fulfillFightArenaGear();
        	return Sleepz.sleepTiming();
		}
        
        if(Dialoguez.handleDialogues()) return Sleepz.sleepTiming();
        
        switch(getProgressValue())
        {
        case(12):
        {
        	if(Prayers.isQuickPrayerActive() && Prayers.toggleQuickPrayer(false))
        	{
        		Sleepz.sleep(1111,420);
        	}
        	talkToLadyServil();
        	return Sleepz.sleepTiming();
        }
        case(11):
        {
        	if(Locations.fightArenaFightArena.contains(Players.getLocal()))
        	{
        		GameObject door = GameObjects.closest(g ->  
        				g != null && 
        				g.getName().equals("Door") && 
        				g.getTile().equals(new Tile(2606,3152,0)));
        		if(door == null)
        		{
        			Logger.log("Door to escape khazard general is null in fight arena fight arena!");
        			Walking.walk(new Tile(2606,3152,0));
        			return Sleepz.sleepTiming();
        		}
        		if(door.interact("Open"))
        		{
        			Sleep.sleepUntil(() -> !Locations.fightArenaFightArena.contains(Players.getLocal()), 
        					() -> Players.getLocal().isMoving(),
        					Sleepz.calculate(2222, 2222),50);
        		}
        	}
        	return Sleepz.sleepTiming();
        }
        case(10):
        {
        	if(Locations.fightArenaFightArena.contains(Players.getLocal()))
        	{
        		NPC bouncer = NPCs.closest("Bouncer");
            	if(bouncer == null)
            	{
            		Logger.log("Bouncer is null in fight arena part to kill it!");
            		return Sleepz.sleepTiming();
            	}
            	if(!Locations.fightArenaBouncerCage.contains(bouncer))
            	{
            		if(Combatz.shouldEatFood(15))
            		{
            			Combatz.eatFood();
            		}
            		if(Combatz.shouldDrinkRangedBoost())
            		{
            			Combatz.drinkRangeBoost();
            		}
            		if(Combatz.shouldDrinkPrayPot())
            		{
            			Combatz.drinkPrayPot();

            			return Sleepz.sleepTiming();
            		}
            		if(Combatz.setQuickPrayEagleEyeProtectMelee())
            		{
            			if(!Prayers.isQuickPrayerActive())
            			{
            				if(Prayers.toggleQuickPrayer(true))
            				{
            					Sleepz.sleep(666, 697);
            				}
            				return Sleepz.interactionTiming();
            			}
            		}
            		if(Players.getLocal().isInteracting(bouncer)) return Sleepz.sleepTiming();
            		if(bouncer.interact("Attack"))
            		{
            			Sleep.sleepUntil(() -> bouncer.isInteracting(Players.getLocal()), 
            					() -> Players.getLocal().isMoving(),
            					Sleepz.calculate(2222, 2222),69);
            			Sleepz.sleep(696, 666);
            		}
            	}
            	return Sleepz.sleepTiming();
        	}
        	return Sleepz.sleepTiming();
        }
        case(9):
        {
        	if(Locations.fightArenaFightArena.contains(Players.getLocal()))
        	{
        		NPC scorpion = NPCs.closest("Khazard Scorpion");
            	if(scorpion == null)
            	{
            		Logger.log("Khazard scorpion is null in fight arena part to kill it!");
            		return Sleepz.sleepTiming();
            	}
            	if(!Locations.fightArenaScorpionCage.contains(scorpion))
            	{
            		if(Combatz.shouldEatFood(15))
            		{
            			Combatz.eatFood();
            		}
            		if(Combatz.shouldDrinkRangedBoost())
            		{
            			Combatz.drinkRangeBoost();
            		}
            		if(Combatz.shouldDrinkPrayPot())
            		{
            			Combatz.drinkPrayPot();
            			return Sleepz.sleepTiming();
            		}
            		if(Combatz.setQuickPrayEagleEyeProtectMelee())
            		{
            			if(!Prayers.isQuickPrayerActive())
            			{
            				if(Prayers.toggleQuickPrayer(true))
            				{
            					Sleepz.sleep(666, 697);
            				}
            				return Sleepz.interactionTiming();
            			}
            		}
            		if(Players.getLocal().isInteracting(scorpion)) return Sleepz.sleepTiming();
            		if(scorpion.interact("Attack"))
            		{
            			Sleep.sleepUntil(() -> scorpion.isInteracting(Players.getLocal()), 
            					() -> Players.getLocal().isMoving(),
            					Sleepz.calculate(2222, 2222),69);
            			Sleepz.sleep(696, 666);
            		}
            		return Sleepz.interactionTiming();
            	}
            	NPC closestMotherfucker = NPCs.closest(n -> 
            		n != null && 
					Locations.fightArenaFightArena.contains(n) && 
					(n.getName().contains("General Khazard") || 
					n.getName().contains("Justin Servil") ||
					n.getName().contains("Sammy Servil")));
				if(closestMotherfucker == null)
				{
					Logger.log("Closest motherfucker to talk to inside fight arena after ogre kill is null!");
					return Sleepz.interactionTiming();
				}
				if(closestMotherfucker.interact("Talk-to"))
				{
					Sleep.sleepUntil(Dialogues::inDialogue,
							() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),66);
				}
				return Sleepz.sleepTiming();
        	}
        	if(Locations.fightArenaHengradWaitingCell.contains(Players.getLocal()))
        	{
        		if(Prayers.isQuickPrayerActive() && Prayers.toggleQuickPrayer(false))
            	{
            		Sleepz.sleep(1111,420);
            	}
        		Sleepz.sleep(6666, 3333);
        		if(!Players.getLocal().isMoving())
        		{
        			if(Dialogues.isProcessing()) return Sleepz.interactionTiming();
        			if(Dialogues.canContinue())
        			{
        				Dialogues.continueDialogue();
        				return Sleepz.interactionTiming();
        			}
        			NPC hengrad = NPCs.closest("Hengrad");
            		if(hengrad == null)
            		{
            			Logger.log("Hengrad is null in fight arena waiting cell after ogre kill!");
            			return Sleepz.interactionTiming();
            		}
            		if(hengrad.interact("Talk-to"))
            		{
            			Sleep.sleepUntil(Dialogues::inDialogue,
        						() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),66);
        			}
        		}
        		return Sleepz.sleepTiming();
        	}
        	return Sleepz.sleepTiming();
        }
        case(8):
        {
        	if(Locations.fightArenaFightArena.contains(Players.getLocal()))
        	{
        		NPC closestMotherfucker = NPCs.closest(n -> 
        				n != null && 
        				Locations.fightArenaFightArena.contains(n) && 
        				(n.getName().contains("General Khazard") || 
        				n.getName().contains("Justin Servil") ||
        				n.getName().contains("Sammy Servil")));
        		if(closestMotherfucker == null)
        		{
        			Logger.log("Closest motherfucker to talk to inside fight arena after ogre kill is null!");
        			return Sleepz.interactionTiming();
        		}
        		if(closestMotherfucker.interact("Talk-to"))
        		{
        			Sleep.sleepUntil(Dialogues::inDialogue,
    						() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),66);
    			}
        		return Sleepz.sleepTiming();
        	}
        }
        case(6):
        {
        	NPC ogre = NPCs.closest("Khazard Ogre");
        	if(ogre == null)
        	{
        		Logger.log("Khazard ogre is null in fight arena part to kill it!");
        		return Sleepz.sleepTiming();
        	}
        	if(Locations.fightArenaOgreCage.contains(ogre) && !ogre.isMoving() && 
        			!Players.getLocal().isMoving())
        	{
        		Sleepz.sleep(2222, 2222);
        		if(Locations.fightArenaOgreCage.contains(ogre) && !ogre.isMoving() && 
            			!Players.getLocal().isMoving())
            	{
            		NPC justin = NPCs.closest("Justin Servil");
            		if(justin == null)
            		{
            			Logger.log("Justin servil is null to try to talk to to start Ogre fight in Fight arena!");
            			return Sleepz.sleepTiming();
            		}
            		if(justin.interact("Talk-to"))
            		{
            			Sleep.sleepUntil(Dialogues::inDialogue,
        						() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),66);
        			}
            		return Sleepz.sleepTiming();
            	}
        	}
        	if(!Locations.fightArenaOgreCage.contains(ogre))
        	{
        		if(Combatz.shouldEatFood(15))
        		{
        			Combatz.eatFood();
        		}
        		if(Combatz.shouldDrinkRangedBoost())
        		{
        			Combatz.drinkRangeBoost();
        		}
        		if(Combatz.shouldDrinkPrayPot())
        		{
        			Combatz.drinkPrayPot();
        			return Sleepz.sleepTiming();
        		}
        		if(Combatz.setQuickPrayEagleEyeProtectMelee())
        		{
        			if(!Prayers.isQuickPrayerActive())
        			{
        				if(Prayers.toggleQuickPrayer(true))
        				{
        					Sleepz.sleep(666, 697);
        				}
        				return Sleepz.interactionTiming();
        			}
        		}
        		if(Players.getLocal().isInteracting(ogre)) return Sleepz.sleepTiming();
        		if(ogre.interact("Attack"))
        		{
        			Sleep.sleepUntil(() -> ogre.isInteracting(Players.getLocal()), 
        					() -> Players.getLocal().isMoving(),
        					Sleepz.calculate(2222, 2222),69);
        			Sleepz.sleep(696, 666);
        		}
        	}
        	return Sleepz.sleepTiming();
        }
        case(5):
        {
        	if(Inventory.count(khazardCellKeys) > 0)
        	{
        		if(getWearKhazardArmour())
            	{
                	talkToSammy();
                }
        		return Sleepz.sleepTiming();
        	}
        	if(Bank.contains(khazardCellKeys))
        	{
        		InvEquip.withdrawOne(khazardCellKeys, 180000);
        		return Sleepz.sleepTiming();
        	}
        	if(Inventory.count(khaliBrew) > 0)
    		{
    			talkToPrisonGuard();
    			return Sleepz.sleepTiming();
    		}
    		talkToBarman();
    		return Sleepz.sleepTiming();
        }
        case(3):
        {
        	if(getWearKhazardArmour())
        	{
        		if(Inventory.count(khaliBrew) > 0)
        		{
        			talkToPrisonGuard();
        			return Sleepz.sleepTiming();
        		}
        		talkToBarman();
        		return Sleepz.sleepTiming();
        	}
        }
        case(2):
        {
        	if(getWearKhazardArmour())
        	{
        		talkToPrisonGuard();
        		return Sleepz.sleepTiming();
        	}
        	return Sleepz.sleepTiming();
        }
        case(1):
        {
        	getWearKhazardArmour();
        	return Sleepz.sleepTiming();
        }
        case(0):
        {
        	//have not started quest yet
        	talkToLadyServil();
        	return Sleepz.sleepTiming();
        }
        default: break;
        }
        return Sleepz.sleepTiming();
    }
    public static final int khazardHelm = 74;
    public static final int khazardPlatebody = 75;
    public static void talkToLadyServil()
    {
    	if(Locations.fightArenaStartArea.contains(Players.getLocal()))
        {
    		NPC ladyServil = NPCs.closest("Lady servil");
         	if(ladyServil == null || !ladyServil.exists())
         	{
         		Walking.walk(Locations.fightArenaStartArea.getRandomTile());
         		return;
         	}
         	if(ladyServil.interact("Talk-to"))
         	{
         		Sleep.sleepUntil(() -> Dialogues.inDialogue(), () -> Players.getLocal().isMoving(), Sleepz.calculate(2222, 2222),50);
         	}
         	return;
        }
        if(Locations.fightArenaStartArea.getCenter().distance() > 250)
 		{
 			if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild") &&
 					!Walkz.useJewelry(InvEquip.duel, "Castle Wars"))
 			{
 				fulfillFightArenaGear();
 			}
 			return;
 		}
        if(!Walkz.isStaminated()) Walkz.drinkStamina();
        if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaStartArea.getCenter())) Sleepz.sleep(420, 696);
    }
    public static boolean getWearKhazardArmour()
    {
    	if(Equipment.contains(khazardHelm) && 
    			Equipment.contains(khazardPlatebody)) return true;
    	if(InvEquip.equipItem(khazardHelm))
    	{
    		InvEquip.equipItem(khazardPlatebody);
    		return false;
    	}
    	
    	if(Locations.fightArenaChestHouse.contains(Players.getLocal()))
    	{
    		if(InvEquip.freeInvySpaces(2))
    		{
    			GameObject chest = GameObjects.closest(g -> 
    				g!=null && 
    				g.getName().equals("Chest") && 
    				g.getTile().equals(new Tile(2613,3189,0)));
	    		if(chest == null)
	    		{
	    			Logger.log("Something wrong, chest in chest/armoury house null in Fight Arena!");
	    			return false;
	    		}
	    		if(chest.hasAction("Open"))
	    		{
	    			if(chest.interact("Open"))
	    			{
	    				Sleep.sleepUntil(() -> chest.distance() <= 1, 
	    					() -> Players.getLocal().isMoving(),
	    					Sleepz.calculate(2222, 2222),50);
	    			}
	    			return false;
	    		}
	    		if(chest.hasAction("Search"))
	    		{
	    			if(chest.interact("Search"))
	    			{
	    				Sleep.sleepUntil(() -> chest.distance() <= 1, 
	    					() -> Players.getLocal().isMoving(),
	    					Sleepz.calculate(2222, 2222),50);
	    				Sleepz.sleep(696, 666);
	    			}
	    			return false;
	    		}
    		}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaChestHouse.getCenter())) Sleepz.sleep(696, 666);
    	return false;
    }
    public static void talkToSammy()
    {
    	if(Locations.fightArenaSammySpace.contains(Players.getLocal()))
    	{
    		GameObject jailDoor = GameObjects.closest(g -> 
    			g != null && 
    			g.getName().contains("Prison Door") && 
    			g.getTile().equals(new Tile(2617,3167,0)));
    		if(jailDoor == null)
    		{
    			Logger.log("Jail door in Fight arena for sammys cell null!");
    			return;
    		}
    		if(Inventory.get(khazardCellKeys).useOn(jailDoor))
    		{
    			Sleep.sleepUntil(Dialogues::inDialogue,
						() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),66);
			}
    	}
		if(enterPrison())
		{
			if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaSammySpace.getCenter())) Sleepz.sleep(666, 696);
		}
    }
    public static void talkToPrisonGuard()
    {
    	if(Locations.fightArenaDrunkGuardArea.contains(Players.getLocal()))
		{
			NPC drunkGuard = NPCs.closest(n -> 
				n != null && 
				n.getName().equals("Khazard Guard") && 
				Locations.fightArenaDrunkGuardArea.contains(n));
			if(drunkGuard == null || !drunkGuard.exists())
			{
				Logger.log("Guard to get drunk is null in Fight arena prison!");
				return;
			}
			if(drunkGuard.interact("Talk-to"))
			{
				Sleep.sleepUntil(Dialogues::inDialogue,
						() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),66);
			}
			return;
		}
		if(enterPrison())
		{
			if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaDrunkGuardArea.getCenter())) Sleepz.sleep(666, 696);
		}
    }
    public static final Tile northPrisonDoorTile = new Tile(2617,3171,0);
    public static final Tile westPrisonDoorTile = new Tile(2585,3141,0);
    public static boolean enterPrison()
    {
    	if(inPrisonWalkable()) return true;
    	if(Locations.fightArenaOutsideLeftJailDoor.contains(Players.getLocal()) || 
    			Locations.fightArenaOutsideNorthJailDoor.contains(Players.getLocal()))
    	{
    		GameObject door = GameObjects.closest(g -> 
    				g!= null && 
    				g.getName().equals("Door") && 
    				(g.getTile().equals(northPrisonDoorTile) ||
    						g.getTile().equals(westPrisonDoorTile)));
    		if(door == null)
    		{
    			Logger.log("Outside fight arena prison doors and door is null!");
    			return false;
    		}
    		if(door.interact("Open"))
    		{
    			Sleep.sleepUntil(Dialogues::inDialogue,
						() -> Players.getLocal().isMoving(),Sleepz.calculate(3333, 2222),66);
    		}
    	}
    	if(Locations.fightArenaStartArea.getCenter().distance() > 250)
 		{
 			if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild") &&
 					!Walkz.useJewelry(InvEquip.duel, "Castle Wars"))
 			{
 				fulfillFightArenaGear();
 			}
 			return false;
 		}
    	if(!Walkz.isStaminated()) Walkz.drinkStamina();
    	if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaDrunkGuardArea.getCenter())) Sleepz.sleep(696, 669);
    	return false;
    }
    public static boolean inPrisonWalkable()
    {
    	return Locations.fightArenaLeftWing1.contains(Players.getLocal()) || 
    			Locations.fightArenaLeftWing2.contains(Players.getLocal()) || 
    			Locations.fightArenaUpperWing1.contains(Players.getLocal()) || 
    			Locations.fightArenaUpperWing2.contains(Players.getLocal()) || 
    			Locations.fightArenaDrunkGuardArea.contains(Players.getLocal());
    }
    public static void talkToBarman()
    {
    	if(Locations.fightArenaAlcoholicsArea.contains(Players.getLocal()))
		{
    		if(!InvEquip.free1InvySpace()) return;
			NPC barman = NPCs.closest("Khazard barman");
			if(barman == null || !barman.exists())
			{
				Walking.walk(Locations.fightArenaAlcoholicsArea.getRandomTile());
				return;
			}
			if(barman.interact("Talk-to"))
			{
				Sleep.sleepUntil(Dialogues::inDialogue,
						() -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),66);
			}
			return;
		}
    	if(Locations.fightArenaStartArea.getCenter().distance() > 250)
 		{
 			if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild") &&
 					!Walkz.useJewelry(InvEquip.duel, "Castle Wars"))
 			{
 				fulfillFightArenaGear();
 			}
 			return;
 		}
		if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaAlcoholicsArea.getCenter()))
		{
			Sleepz.sleep(696, 420);
		}
    }
    public static boolean fulfillFightArenaGear()
    {
    	InvEquip.clearAll();
    	
    	TrainRanged.setBestRangedEquipment();
    	
    	InvEquip.addInvyItem(id.rangePot4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.duel, 1, 1, false, 5);
    	InvEquip.addInvyItem(id.prayPot4, 8, (int) Calculations.nextGaussianRandom(11, 3), false, (int) Calculations.nextGaussianRandom(25, 5));
    	InvEquip.addInvyItem(id.stamina4, 1, 1, false, 5);
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : id.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 5, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 5, API.roundToMultiple((int) Calculations.nextGaussianRandom(5000,3000), 100), false, 0);
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			Logger.log("[INVEQUIP] -> Fulfilled equipment correctly! (Fight Arena setup)");
			return true;
		} else 
		{
			Logger.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (Fight Arena setup)");
			return false;
		}
    	
    }
    public static boolean talkedToDrunkGuard = false;
    public static int getProgressValue()
	{
		return PlayerSettings.getConfig(17);
	}
    
}
