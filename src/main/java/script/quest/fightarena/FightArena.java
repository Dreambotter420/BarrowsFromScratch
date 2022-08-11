package script.quest.fightarena;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
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
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Completes Fight Arena
 * @author Dreambotter420
 * ^_^
 */
public class FightArena extends Leaf {
	public static boolean started = false;
	public static boolean completedFightArena = false;
    public static final int khaliBrew = 77;
    public static final int khazardCellKeys = 76;
	public static boolean onExit() {
		return true;
	}
	public static void onStart() {
        
        started = true;
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.FIGHT_ARENA;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Fight Arena");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if (completedFightArena) {
            MethodProvider.log("[FINISHED] -> Fight Arena!!");
           	API.mode = null;
           	Main.customPaintText1 = "~~~~~~~~~~";
    		Main.customPaintText2 = "~Quest Complete~";
    		Main.customPaintText3 = "~Fight Arena~";
    		Main.customPaintText4 = "~~~~~~~~~~";
            return Timing.sleepLogNormalSleep();
        }
        
        final int prayerLeft = Skills.getBoostedLevels(Skill.PRAYER);
        
        if(Inventory.contains(TrainRanged.getBestDart()))
    	{
    		InvEquip.equipItem(TrainRanged.getBestDart());
    		return Timing.sleepLogNormalSleep();
    	}
        
        if((!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth)) ||
        		!Equipment.contains(TrainRanged.getBestDart()) || 
        		(Combatz.shouldDrinkRangedBoost() && !InvEquip.invyContains(TrainRanged.rangedPots)) || 
        		(Combatz.shouldDrinkPrayPot() && !InvEquip.invyContains(id.prayPots))) 
		{
        	fulfillFightArenaGear();
        	return Timing.sleepLogNormalSleep();
		}
        
        if(handleDialogues()) return Timing.sleepLogNormalSleep();
        
        switch(getProgressValue())
        {
        case(14):
        {
        	if(Widgets.getWidgetChild(153,16) != null && 
        			Widgets.getWidgetChild(153,16).isVisible())
        	{
        		if(Widgets.getWidgetChild(153,16).interact("Close")) Sleep.sleep(666, 696);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(!Inventory.contains(khazardCellKeys) && 
        			!Inventory.contains(khazardHelm) &&
        			!Inventory.contains(khazardPlatebody) &&
        			!Equipment.contains(khazardHelm) &&
        			!Equipment.contains(khazardPlatebody))
        	{
        		completedFightArena = true;
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Inventory.contains(khazardCellKeys) || Inventory.contains(khazardHelm) || 
        			Inventory.contains(khazardPlatebody))
        	{
        		if(Inventory.drop(i -> i != null && 
        				(i.getID() == khazardCellKeys ||
        				i.getID() == khazardHelm || 
        				i.getID() == khazardPlatebody))) Sleep.sleep(666, 696);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Equipment.contains(khazardHelm) || 
        			Equipment.contains(khazardPlatebody))
        	{
        		if(InvEquip.freeInvySpaces(2))
            	{
            		if(!Tabs.isOpen(Tab.EQUIPMENT))
            		{
            			if(Tabs.open(Tab.EQUIPMENT)) Sleep.sleep(69, 696);
            			return Timing.sleepLogNormalSleep();
            		}
            		if(Equipment.unequip(i -> i!=null && 
            				(i.getID() == khazardHelm || 
            				i.getID() == khazardPlatebody)))
            		{
            			Sleep.sleep(666, 696);
            		}
            	}
        	}
        	
        	return Timing.sleepLogNormalSleep();
        }
        case(12):
        {
        	if(Prayers.isQuickPrayerActive() && Prayers.toggleQuickPrayer(false))
        	{
        		Sleep.sleep(1111,420);
        	}
        	talkToLadyServil();
        	return Timing.sleepLogNormalSleep();
        }
        case(11):
        {
        	if(Locations.fightArenaFightArena.contains(Players.localPlayer()))
        	{
        		GameObject door = GameObjects.closest(g ->  
        				g != null && 
        				g.getName().equals("Door") && 
        				g.getTile().equals(new Tile(2606,3152,0)));
        		if(door == null)
        		{
        			MethodProvider.log("Door to escape khazard general is null in fight arena fight arena!");
        			Walking.walk(new Tile(2606,3152,0));
        			return Timing.sleepLogNormalSleep();
        		}
        		if(door.interact("Open"))
        		{
        			MethodProvider.sleepUntil(() -> !Locations.fightArenaFightArena.contains(Players.localPlayer()), 
        					() -> Players.localPlayer().isMoving(),
        					Sleep.calculate(2222, 2222),50);
        		}
        	}
        	return Timing.sleepLogNormalSleep();
        }
        case(10):
        {
        	if(Locations.fightArenaFightArena.contains(Players.localPlayer()))
        	{
        		NPC bouncer = NPCs.closest("Bouncer");
            	if(bouncer == null)
            	{
            		MethodProvider.log("Bouncer is null in fight arena part to kill it!");
            		return Timing.sleepLogNormalSleep();
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

            			return Timing.sleepLogNormalSleep();
            		}
            		if(Combatz.setQuickPrayEagleEyeProtectMelee())
            		{
            			if(!Prayers.isQuickPrayerActive())
            			{
            				if(Prayers.toggleQuickPrayer(true))
            				{
            					Sleep.sleep(666, 697);
            				}
            				return Timing.sleepLogNormalInteraction();
            			}
            		}
            		if(Players.localPlayer().isInteracting(bouncer)) return Timing.sleepLogNormalSleep();
            		if(bouncer.interact("Attack"))
            		{
            			MethodProvider.sleepUntil(() -> bouncer.isInteracting(Players.localPlayer()), 
            					() -> Players.localPlayer().isMoving(),
            					Sleep.calculate(2222, 2222),69);
            			Sleep.sleep(696, 666);
            		}
            	}
            	return Timing.sleepLogNormalSleep();
        	}
        	return Timing.sleepLogNormalSleep();
        }
        case(9):
        {
        	if(Locations.fightArenaFightArena.contains(Players.localPlayer()))
        	{
        		NPC scorpion = NPCs.closest("Khazard Scorpion");
            	if(scorpion == null)
            	{
            		MethodProvider.log("Khazard scorpion is null in fight arena part to kill it!");
            		return Timing.sleepLogNormalSleep();
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
            			return Timing.sleepLogNormalSleep();
            		}
            		if(Combatz.setQuickPrayEagleEyeProtectMelee())
            		{
            			if(!Prayers.isQuickPrayerActive())
            			{
            				if(Prayers.toggleQuickPrayer(true))
            				{
            					Sleep.sleep(666, 697);
            				}
            				return Timing.sleepLogNormalInteraction();
            			}
            		}
            		if(Players.localPlayer().isInteracting(scorpion)) return Timing.sleepLogNormalSleep();
            		if(scorpion.interact("Attack"))
            		{
            			MethodProvider.sleepUntil(() -> scorpion.isInteracting(Players.localPlayer()), 
            					() -> Players.localPlayer().isMoving(),
            					Sleep.calculate(2222, 2222),69);
            			Sleep.sleep(696, 666);
            		}
            		return Timing.sleepLogNormalInteraction();
            	}
            	NPC closestMotherfucker = NPCs.closest(n -> 
            		n != null && 
					Locations.fightArenaFightArena.contains(n) && 
					(n.getName().contains("General Khazard") || 
					n.getName().contains("Justin Servil") ||
					n.getName().contains("Sammy Servil")));
				if(closestMotherfucker == null)
				{
					MethodProvider.log("Closest motherfucker to talk to inside fight arena after ogre kill is null!");
					return Timing.sleepLogNormalInteraction();
				}
				if(closestMotherfucker.interact("Talk-to"))
				{
					MethodProvider.sleepUntil(Dialogues::inDialogue,
							() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),66);
				}
				return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.fightArenaHengradWaitingCell.contains(Players.localPlayer()))
        	{
        		if(Prayers.isQuickPrayerActive() && Prayers.toggleQuickPrayer(false))
            	{
            		Sleep.sleep(1111,420);
            	}
        		Sleep.sleep(6666, 3333);
        		if(!Players.localPlayer().isMoving())
        		{
        			if(Dialogues.isProcessing()) return Timing.sleepLogNormalInteraction();
        			if(Dialogues.canContinue())
        			{
        				Dialogues.continueDialogue();
        				return Timing.sleepLogNormalInteraction();
        			}
        			NPC hengrad = NPCs.closest("Hengrad");
            		if(hengrad == null)
            		{
            			MethodProvider.log("Hengrad is null in fight arena waiting cell after ogre kill!");
            			return Timing.sleepLogNormalInteraction();
            		}
            		if(hengrad.interact("Talk-to"))
            		{
            			MethodProvider.sleepUntil(Dialogues::inDialogue,
        						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),66);
        			}
        		}
        		return Timing.sleepLogNormalSleep();
        	}
        	return Timing.sleepLogNormalSleep();
        }
        case(8):
        {
        	if(Locations.fightArenaFightArena.contains(Players.localPlayer()))
        	{
        		NPC closestMotherfucker = NPCs.closest(n -> 
        				n != null && 
        				Locations.fightArenaFightArena.contains(n) && 
        				(n.getName().contains("General Khazard") || 
        				n.getName().contains("Justin Servil") ||
        				n.getName().contains("Sammy Servil")));
        		if(closestMotherfucker == null)
        		{
        			MethodProvider.log("Closest motherfucker to talk to inside fight arena after ogre kill is null!");
        			return Timing.sleepLogNormalInteraction();
        		}
        		if(closestMotherfucker.interact("Talk-to"))
        		{
        			MethodProvider.sleepUntil(Dialogues::inDialogue,
    						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),66);
    			}
        		return Timing.sleepLogNormalSleep();
        	}
        }
        case(6):
        {
        	NPC ogre = NPCs.closest("Khazard Ogre");
        	if(ogre == null)
        	{
        		MethodProvider.log("Khazard ogre is null in fight arena part to kill it!");
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Locations.fightArenaOgreCage.contains(ogre) && !ogre.isMoving() && 
        			!Players.localPlayer().isMoving())
        	{
        		Sleep.sleep(2222, 2222);
        		if(Locations.fightArenaOgreCage.contains(ogre) && !ogre.isMoving() && 
            			!Players.localPlayer().isMoving())
            	{
            		NPC justin = NPCs.closest("Justin Servil");
            		if(justin == null)
            		{
            			MethodProvider.log("Justin servil is null to try to talk to to start Ogre fight in Fight arena!");
            			return Timing.sleepLogNormalSleep();
            		}
            		if(justin.interact("Talk-to"))
            		{
            			MethodProvider.sleepUntil(Dialogues::inDialogue,
        						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),66);
        			}
            		return Timing.sleepLogNormalSleep();
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
        			return Timing.sleepLogNormalSleep();
        		}
        		if(Combatz.setQuickPrayEagleEyeProtectMelee())
        		{
        			if(!Prayers.isQuickPrayerActive())
        			{
        				if(Prayers.toggleQuickPrayer(true))
        				{
        					Sleep.sleep(666, 697);
        				}
        				return Timing.sleepLogNormalInteraction();
        			}
        		}
        		if(Players.localPlayer().isInteracting(ogre)) return Timing.sleepLogNormalSleep();
        		if(ogre.interact("Attack"))
        		{
        			MethodProvider.sleepUntil(() -> ogre.isInteracting(Players.localPlayer()), 
        					() -> Players.localPlayer().isMoving(),
        					Sleep.calculate(2222, 2222),69);
        			Sleep.sleep(696, 666);
        		}
        	}
        	return Timing.sleepLogNormalSleep();
        }
        case(5):
        {
        	if(Inventory.count(khazardCellKeys) > 0)
        	{
        		if(getWearKhazardArmour())
            	{
                	talkToSammy();
                }
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Bank.contains(khazardCellKeys))
        	{
        		InvEquip.withdrawOne(khazardCellKeys, 180000);
        		return Timing.sleepLogNormalSleep();
        	}
        	if(Inventory.count(khaliBrew) > 0)
    		{
    			talkToPrisonGuard();
    			return Timing.sleepLogNormalSleep();
    		}
    		talkToBarman();
    		return Timing.sleepLogNormalSleep();
        }
        case(3):
        {
        	if(getWearKhazardArmour())
        	{
        		if(Inventory.count(khaliBrew) > 0)
        		{
        			talkToPrisonGuard();
        			return Timing.sleepLogNormalSleep();
        		}
        		talkToBarman();
        		return Timing.sleepLogNormalSleep();
        	}
        }
        case(2):
        {
        	if(getWearKhazardArmour())
        	{
        		talkToPrisonGuard();
        		return Timing.sleepLogNormalSleep();
        	}
        	return Timing.sleepLogNormalSleep();
        }
        case(1):
        {
        	getWearKhazardArmour();
        	return Timing.sleepLogNormalSleep();
        }
        case(0):
        {
        	//have not started quest yet
        	talkToLadyServil();
        	return Timing.sleepLogNormalSleep();
        }
        default: break;
        }
        return Timing.sleepLogNormalSleep();
    }
    public static final int khazardHelm = 74;
    public static final int khazardPlatebody = 75;
    public static void talkToLadyServil()
    {
    	if(Locations.fightArenaStartArea.contains(Players.localPlayer()))
        {
    		NPC ladyServil = NPCs.closest("Lady servil");
         	if(ladyServil == null || !ladyServil.exists())
         	{
         		Walking.walk(Locations.fightArenaStartArea.getRandomTile());
         		return;
         	}
         	if(ladyServil.interact("Talk-to"))
         	{
         		MethodProvider.sleepUntil(() -> Dialogues.inDialogue(), () -> Players.localPlayer().isMoving(), Sleep.calculate(2222, 2222),50);
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
        if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaStartArea.getCenter())) Sleep.sleep(420, 696);
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
    	
    	if(Locations.fightArenaChestHouse.contains(Players.localPlayer()))
    	{
    		if(InvEquip.freeInvySpaces(2))
    		{
    			GameObject chest = GameObjects.closest(g -> 
    				g!=null && 
    				g.getName().equals("Chest") && 
    				g.getTile().equals(new Tile(2613,3189,0)));
	    		if(chest == null)
	    		{
	    			MethodProvider.log("Something wrong, chest in chest/armoury house null in Fight Arena!");
	    			return false;
	    		}
	    		if(chest.hasAction("Open"))
	    		{
	    			if(chest.interact("Open"))
	    			{
	    				MethodProvider.sleepUntil(() -> chest.distance() <= 1, 
	    					() -> Players.localPlayer().isMoving(),
	    					Sleep.calculate(2222, 2222),50);
	    			}
	    			return false;
	    		}
	    		if(chest.hasAction("Search"))
	    		{
	    			if(chest.interact("Search"))
	    			{
	    				MethodProvider.sleepUntil(() -> chest.distance() <= 1, 
	    					() -> Players.localPlayer().isMoving(),
	    					Sleep.calculate(2222, 2222),50);
	    				Sleep.sleep(696, 666);
	    			}
	    			return false;
	    		}
    		}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaChestHouse.getCenter())) Sleep.sleep(696, 666);
    	return false;
    }
    public static void talkToSammy()
    {
    	if(Locations.fightArenaSammySpace.contains(Players.localPlayer()))
    	{
    		GameObject jailDoor = GameObjects.closest(g -> 
    			g != null && 
    			g.getName().contains("Prison Door") && 
    			g.getTile().equals(new Tile(2617,3167,0)));
    		if(jailDoor == null)
    		{
    			MethodProvider.log("Jail door in Fight arena for sammys cell null!");
    			return;
    		}
    		if(Inventory.get(khazardCellKeys).useOn(jailDoor))
    		{
    			MethodProvider.sleepUntil(Dialogues::inDialogue,
						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),66);
			}
    	}
		if(enterPrison())
		{
			if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaSammySpace.getCenter())) Sleep.sleep(666, 696);
		}
    }
    public static void talkToPrisonGuard()
    {
    	if(Locations.fightArenaDrunkGuardArea.contains(Players.localPlayer()))
		{
			NPC drunkGuard = NPCs.closest(n -> 
				n != null && 
				n.getName().equals("Khazard Guard") && 
				Locations.fightArenaDrunkGuardArea.contains(n));
			if(drunkGuard == null || !drunkGuard.exists())
			{
				MethodProvider.log("Guard to get drunk is null in Fight arena prison!");
				return;
			}
			if(drunkGuard.interact("Talk-to"))
			{
				MethodProvider.sleepUntil(Dialogues::inDialogue,
						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),66);
			}
			return;
		}
		if(enterPrison())
		{
			if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaDrunkGuardArea.getCenter())) Sleep.sleep(666, 696);
		}
    }
    public static final Tile northPrisonDoorTile = new Tile(2617,3171,0);
    public static final Tile westPrisonDoorTile = new Tile(2585,3141,0);
    public static boolean enterPrison()
    {
    	if(inPrisonWalkable()) return true;
    	if(Locations.fightArenaOutsideLeftJailDoor.contains(Players.localPlayer()) || 
    			Locations.fightArenaOutsideNorthJailDoor.contains(Players.localPlayer()))
    	{
    		GameObject door = GameObjects.closest(g -> 
    				g!= null && 
    				g.getName().equals("Door") && 
    				(g.getTile().equals(northPrisonDoorTile) ||
    						g.getTile().equals(westPrisonDoorTile)));
    		if(door == null)
    		{
    			MethodProvider.log("Outside fight arena prison doors and door is null!");
    			return false;
    		}
    		if(door.interact("Open"))
    		{
    			MethodProvider.sleepUntil(Dialogues::inDialogue,
						() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
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
    	if(Walking.shouldWalk(6) && Walking.walk(Locations.fightArenaDrunkGuardArea.getCenter())) Sleep.sleep(696, 669);
    	return false;
    }
    public static boolean inPrisonWalkable()
    {
    	return Locations.fightArenaLeftWing1.contains(Players.localPlayer()) || 
    			Locations.fightArenaLeftWing2.contains(Players.localPlayer()) || 
    			Locations.fightArenaUpperWing1.contains(Players.localPlayer()) || 
    			Locations.fightArenaUpperWing2.contains(Players.localPlayer()) || 
    			Locations.fightArenaDrunkGuardArea.contains(Players.localPlayer());
    }
    public static void talkToBarman()
    {
    	if(Locations.fightArenaAlcoholicsArea.contains(Players.localPlayer()))
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
				MethodProvider.sleepUntil(Dialogues::inDialogue,
						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),66);
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
			Sleep.sleep(696, 420);
		}
    }
    public static boolean fulfillFightArenaGear()
    {
    	InvEquip.clearAll();
    	
    	TrainRanged.setBestRangedEquipment();
    	
    	InvEquip.addInvyItem(TrainRanged.rangePot4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.duel, 1, 1, false, 5);
    	InvEquip.addInvyItem(id.prayPot4, 8, (int) Calculations.nextGaussianRandom(11, 3), false, (int) Calculations.nextGaussianRandom(25, 5));
    	InvEquip.addInvyItem(id.stamina4, 1, 1, false, 5);
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : TrainRanged.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(TrainRanged.jugOfWine, 5, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 5, API.roundToMultiple((int) Calculations.nextGaussianRandom(5000,3000), 100), false, 0);
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (Fight Arena setup)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (Fight Arena setup)");
			return false;
		}
    	
    }
    public static boolean talkedToDrunkGuard = false;
    public static int getProgressValue()
	{
		return PlayerSettings.getConfig(17);
	}
    public static boolean handleDialogues()
	{
		if(Dialogues.canContinue())
		{
			if(Dialogues.continueDialogue()) Sleep.sleep(69,696);
			return true;
		}
		if(Dialogues.isProcessing())
		{
			Sleep.sleep(420,696);
			return true;
		}
		 
		if(Dialogues.areOptionsAvailable())
		{
			return Dialogues.chooseOption("Yes.") || 
					Dialogues.chooseOption("I\'d like a Khali brew please.");
		}
		return false;
	}
}
