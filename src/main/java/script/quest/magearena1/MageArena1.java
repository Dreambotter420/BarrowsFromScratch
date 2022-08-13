package script.quest.magearena1;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.magic.Casting;
import script.skills.magic.TrainMagic;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Mage Arena 1
 * @author Dreambotter420
 * ^_^
 */
public class MageArena1 extends Leaf {
	public static boolean completedMageArena1 = false;
	public static boolean slashed = false;
    public void onStart() {
        
    }
    public boolean onExit() {
        if(Locations.mageArenaBank.contains(Players.localPlayer()))
        {
        	if(!Walkz.useJewelry(InvEquip.glory, "Edgeville") && !Walkz.useJewelry(InvEquip.combat, "Ranging Guild"))
        	{
        		if(Walkz.teleportVarrock(30000))
        		{
        			MethodProvider.log("Teleported to varrock!");
        		}
        	}
        }
        return true;
    }
 
    @Override
    public int onLoop() {
    	if (completedMageArena1) {
            MethodProvider.log("[FINISHED] -> Mage Arena 1");
            if(onExit()) 
            {
            	Main.customPaintText1 = "~~~~~~~~~~~";
        		Main.customPaintText2 = "~Quest Complete~";
        		Main.customPaintText3 = "~Mage Arena 1~";
        		Main.customPaintText4 = "~~~~~~~~~~~";
            	API.mode = null;
            }
            return Timing.sleepLogNormalSleep();
        }
    	if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Mage Arena 1");
            if(onExit()) API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if(handleDialogues()) return Timing.sleepLogNormalSleep();
        switch(getProgressValue())
        {
        case(8):
        {
        	if(Players.localPlayer().isAnimating())
        	{
        		Sleep.sleep(69, 696);
        		break;
        	}
        	if(Locations.mageArenaCave.contains(Players.localPlayer()))
        	{
        		if(Inventory.count(id.saradominCape) < 2)
            	{
            		getCape("Saradomin");
            		break;
            	}
            	if(Inventory.count(id.guthixCape) < 2)
            	{
            		getCape("Guthix");
            		break;
            	}
            	if(Inventory.count(id.zamorakCape) < 2)
            	{
            		getCape("Zamorak");
            		break;
            	}
            	API.walkInteractWithGameObject("Sparkling pool", "Step-into",Locations.mageArenaCaveSouth, Dialogues::inDialogue);
            	break;
        	}
        	if(Locations.mageArenaBank.contains(Players.localPlayer()))
        	{
        		if(Inventory.contains(id.guthixCape) || 
        				Inventory.contains(id.guthixStaff) || 
        				Inventory.contains(id.zamorakCape) || 
        				Inventory.contains(id.zamorakStaff) || 
        				Inventory.contains(id.saradominCape) || 
        				Inventory.contains(id.saradominStaff))
        		{
        			if(Bankz.openClosest(50))
        			{
        				Bank.depositAllItems();
        			}
        			break;
        		}
        	}
        	completedMageArena1 = true;
        	break;
        }
        //just prayed at first statue
        case(7):
        {
        	if(Players.localPlayer().isAnimating())
        	{
        		Sleep.sleep(69, 696);
        		break;
        	}
        	if(Locations.mageArenaCave.contains(Players.localPlayer()))
        	{
        		if(Inventory.count(id.saradominCape) < 2)
            	{
            		getCape("Saradomin");
            		break;
            	}
            	if(Inventory.count(id.guthixCape) < 2)
            	{
            		getCape("Guthix");
            		break;
            	}
            	if(Inventory.count(id.zamorakCape) < 2)
            	{
            		getCape("Zamorak");
            		break;
            	}
            	if(!Inventory.contains(id.saradominStaff) &&
            			!Inventory.contains(id.zamorakStaff) &&
            			!Inventory.contains(id.guthixStaff))
            	{
            		API.walkTalkToNPC("Chamber guardian","Talk-to",Locations.mageArenaCaveSouth);
            	}
        	}
        	break;
        }
        //just defeated kolodion, should teleport back into mage arena bank
        case(6):
        {
        	if(Players.localPlayer().isAnimating())
        	{
        		Sleep.sleep(69, 696);
        		break;
        	}
        	if(Prayers.isActive(Prayer.PROTECT_FROM_MAGIC))
        	{
        		Prayers.toggle(false, Prayer.PROTECT_FROM_MAGIC);
        		Sleep.sleep(69, 696);
        		break;
        	}
        	if(Locations.mageArenaCave.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Statue of Saradomin", "Pray-at", Locations.mageArenaCaveStatues, Dialogues::inDialogue);
    			break;
        	}
        	if(Locations.mageArenaBank.contains(Players.localPlayer()))
        	{
        		API.walkInteractWithGameObject("Sparkling pool", "Step-into", Locations.mageArenaBank, Dialogues::inDialogue);
        		break;
        	}
        	break;
        }
        case(5):
        {
        	fightKolodion();
        	break;
        }
        case(4):
        {
        	fightKolodion();
        	break;
        }
        case(3):
        {
        	fightKolodion();
        	break;
        }
        case(2):
        {
        	fightKolodion();
        	break;
        }
        case(1):
        {
        	fightKolodion();
        	break;
        }
        case(0):
        {
        	if((Combatz.shouldDrinkPrayPot() && !InvEquip.invyContains(id.prayPots)) ||
        			!Inventory.contains(id.knife) || 
        			!InvEquip.equipmentContains(InvEquip.wearableGlory) ||
        			(Combatz.shouldEatFood(20) && !InvEquip.invyContains(Combatz.foods)) ||
        			!Equipment.contains(id.staffOfAir) || 
        			(Skills.getRealLevel(Skill.MAGIC) < 62 && !Inventory.contains(id.deathRune)) ||
        			(Skills.getRealLevel(Skill.MAGIC) >= 62 && !Inventory.contains(id.bloodRune)) || 
        			!Inventory.contains(id.earthRune) || 
        			!Inventory.contains(id.waterRune) || 
        			!Inventory.contains(id.fireRune))
        	{
        		fulfillMageArena1();
        		break;
        	}
        	if(Inventory.contains(InvEquip.coins))
        	{
        		InvEquip.depositAll(InvEquip.coins, 180000);
        		break;
        	}
        	if(goToMageArenaBank())
        	{
        		API.talkToNPC("Kolodion");
        		break;
        	}
        	break;
        }
        default:break;
        }
        
        return Timing.sleepLogNormalSleep();
    }
    public static void getCape(String godName)
    {
    	GroundItem cape = GroundItems.closest(g -> 
    			g!=null && 
    			g.getName().contains(godName) && 
    			g.getName().contains("cape"));
    	if(cape == null)
    	{
    		String statueName = "Statue of ".concat(godName);
    		API.walkInteractWithGameObject(statueName, "Pray-at", Locations.mageArenaCaveStatues, Dialogues::inDialogue);
    		return;
    	}
    	if(cape.interact("Take"))
    	{
    		String capeName = godName.concat(" cape");
    		final int count = Inventory.count(capeName);
    		MethodProvider.sleepUntil(() -> Inventory.count(capeName) > count, Sleep.calculate(2222, 2222));
    	}
    }
    public static void fightKolodion()
    {
    	if((Combatz.shouldDrinkPrayPot() && !InvEquip.invyContains(id.prayPots)) ||
    			!Inventory.contains(id.knife) || 
    			(Combatz.shouldEatFood(20) && !InvEquip.invyContains(Combatz.foods)) ||
    			!Equipment.contains(id.staffOfAir) || 
    			(Skills.getRealLevel(Skill.MAGIC) < 62 && !Inventory.contains(id.deathRune)) ||
    			(Skills.getRealLevel(Skill.MAGIC) >= 62 && !Inventory.contains(id.bloodRune)) || 
    			!Inventory.contains(id.earthRune) || 
    			!Inventory.contains(id.waterRune) || 
    			!Inventory.contains(id.fireRune))
    	{
    		fulfillMageArena1();
    		return;
    	}
    	if(Inventory.contains(InvEquip.coins))
    	{
    		InvEquip.depositAll(InvEquip.coins, 180000);
    		return;
    	}
    	if(Locations.mageArenaInnerCircle.contains(Players.localPlayer()))
    	{
    		if(Combatz.shouldDrinkPrayPot()) 
    		{
    			Combatz.drinkPrayPot();
    			return;
    		}
    		if(!Prayers.isActive(Prayer.PROTECT_FROM_MAGIC))
    		{
    			Prayers.toggle(true, Prayer.PROTECT_FROM_MAGIC);
    			return;
    		}
    		if(Combatz.shouldEatFood(20))
    		{
    			Combatz.eatFood();
    			return;
    		}
    		if(PlayerSettings.getConfig(172) == 1)
        	{
    			MethodProvider.log("Toggling autoretaliate");
        		Combat.toggleAutoRetaliate(true);
        	}
    		if(!Casting.isAutocastingHighest())
    		{
    			Casting.setHighestAutocast();
    			return;
    		}
    		if(!Players.localPlayer().isInCombat())
    		{
    			API.interactNPC("Kolodion", "Attack", Locations.mageArenaInnerCircle, true, () -> Players.localPlayer().isInCombat());
    		}
    		return;
    	}
    	if(goToMageArenaBank())
    	{
    		Sleep.sleep(2222, 2222);
    		if(Locations.mageArenaBank.contains(Players.localPlayer()))
    		{
    			API.talkToNPC("Kolodion");
    		}
    		return;
    	}
    }
    public static boolean goToMageArenaBank()
    {
    	slashed = false;
    	if(Locations.mageArenaBank.contains(Players.localPlayer())) return true;
    	if(Combatz.shouldEatFood(20))
    	{
    		Combatz.eatFood();
    	}
    	if(!Walkz.isStaminated() && InvEquip.invyContains(id.staminas))
    	{
    		Walkz.drinkStamina();
    		return false;
    	}
    	
    	if(!Walking.isRunEnabled())
    	{
    		if(shouldPanic())
    		{
    			if(Walking.getRunEnergy() >= 3)
    			{
    				Walking.toggleRun();
    			}
    		}
    		else
    		{
    			if(Walking.getRunEnergy() >= 15)
    			{
    				if(Walking.toggleRun()) Sleep.sleep(696, 666);
    			}
    		}
    	}
    	if(Combat.isAutoRetaliateOn())
    	{
    		Combat.toggleAutoRetaliate(false);
    	}
    	if(Locations.mageArenaBankOutside.contains(Players.localPlayer()))
    	{
    		GameObject lever = GameObjects.closest(g -> 
			g!=null && 
			g.getName().contains("Lever") &&
			g.hasAction("Pull") &&
			Locations.mageArenaBankLeverRoom.contains(g));
    		if(lever == null) //lever being pulled!! Go to lever tile and wait..
    		{
    			if(Locations.mageArenaBankOutsideLeverTile.equals(Players.localPlayer().getTile())) return false;
    		}
    		
    		if(!Locations.mageArenaBankOutsideLeverTile.canReach())
    		{
    			GameObject web1 = GameObjects.closest(g -> 
					g.getName().equals("Web") && 
					g.hasAction("Slash") && 
					g.getTile().equals(new Tile(3095,3957,0)));
    			GameObject web2 = GameObjects.closest(g -> 
					g.getName().equals("Web") && 
					g.hasAction("Slash") && 
					g.getTile().equals(new Tile(3092,3957,0)));
    			if(Locations.mageArenaBankLeverAirlock.contains(Players.localPlayer()))
    			{
    				if(web2 != null)
    				{
    					if(web2.interact("Slash"))
    					{
    						if(shouldPanic()) return false;
        					MethodProvider.sleepUntil(() -> slashed, Sleep.calculate(2222, 2222));
        					Sleep.sleep(420, 696);
    					}
    				}
    				return false;
    			}
    			if(Locations.mageArenaBankOutside.contains(Players.localPlayer()))
    			{
    				if(web1 != null)
    				{
    					if(web1.interact("Slash"))
    					{
    						if(shouldPanic()) return false;
        					MethodProvider.sleepUntil(() -> slashed, Sleep.calculate(2222, 2222));
        					Sleep.sleep(420, 696);
    					}
    				}
    				if(web2 != null && web1 == null)
    				{
    					if(web2.interact("Slash"))
    					{
    						if(shouldPanic()) return false;
        					MethodProvider.sleepUntil(() -> slashed, Sleep.calculate(2222, 2222));
        					Sleep.sleep(420, 696);
    					}
    				}
    				
    				return false;
    			}
    			return false;
    		}
    		if(lever == null)
    		{
    			if(shouldPanic()) Walking.walkExact(Locations.mageArenaBankOutsideLeverTile);
    			else if(Walking.shouldWalk(6) && Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleep.sleep(696, 666);
    			return false;
    		}
    		if(lever.interact("Pull"))
    		{
    			MethodProvider.log("Pulled lever!");
    			if(shouldPanic()) return false;
    			MethodProvider.sleepUntil(() -> Locations.mageArenaBank.contains(Players.localPlayer()), 
    					() -> Players.localPlayer().isMoving(),
    					Sleep.calculate(2222, 2222),69);
    		}
    		return false;
    	}
    	if(Locations.deepWildyEdgevilleLeverPeninsula.contains(Players.localPlayer()))
    	{
    		final Tile slashTile = new Tile(3158,3950,0);
    		GameObject web = GameObjects.closest(g -> 
			g.getName().equals("Web") && 
			g.hasAction("Slash") && 
			Locations.deepWildyWeb_EdgevilleLever.contains(g));
    		if(web != null)
    		{
    			if(web.distance() >= 8)
    			{
    				MethodProvider.log("Web too far");
    				if(shouldPanic())
    	    		{
    	    			if(Walking.walk(slashTile)) Sleep.sleep(420, 696);
    	    		}
    	    		else if(Walking.shouldWalk(6) && Walking.walk(slashTile)) Sleep.sleep(420, 696);
    				
    			}
    			else if(web.interact("Slash"))
    			{
    				MethodProvider.log("Web slashed!");
    				if(shouldPanic()) return false;
    				MethodProvider.sleepUntil(() -> slashed, 
    					() -> Players.localPlayer().isMoving(), Sleep.calculate(2222, 2222),69);
					Sleep.sleep(420, 696);
    				return false;
    			} else
    			{
    				MethodProvider.log("Web walking");
    				Walking.walk(slashTile);
    			}
    			return false;
    		}
    		MethodProvider.log("mage arena bank walking");
    		if(shouldPanic())
    		{
    			if(Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleep.sleep(420, 696);
    		}
    		else if(Walking.shouldWalk(6) && Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleep.sleep(420, 696);
			return false;
    	}
    	if(Combat.isInWild() && Combat.getWildernessLevel() > 30)
    	{
    		if(shouldPanic()) Walking.walk(Locations.mageArenaBankOutsideLeverTile);
    		else if(Walking.shouldWalk(6) && Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleep.sleep(420, 696);
    		return false;
    	}
    	if(Locations.edgevillePKLever.contains(Players.localPlayer()))
    	{
    		GameObject lever = GameObjects.closest(g -> 
			g!=null && 
			g.getName().contains("Lever") &&
			g.hasAction("Pull") &&
			Locations.edgevillePKLever.contains(g));
    		if(lever == null)
    		{
    			Sleep.sleep(696, 666);
    			return false;
    		}
    		if(lever.interact("Pull"))
    		{
    			MethodProvider.sleepUntil(() -> Locations.deepWildyEdgevilleLeverPeninsula.contains(Players.localPlayer()),
    					() -> Players.localPlayer().isMoving(),
    					Sleep.calculate(3333, 3333),69);
    		}
    		return false;
    	}
    	if(Locations.edgevillePKLever.getCenter().distance() <= 50)
    	{
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.edgevillePKLever.getCenter())) Sleep.sleep(696, 666);
    		return false;
    	}
    	if(!Walkz.useJewelry(InvEquip.glory, "Edgeville"))
    	{
    		fulfillMageArena1();
    	}
    	return false;
    }
    /**
     * checks for any alive players within a radius of 10, returns true if so, false otherwise
     * @return
     */
    public static boolean shouldPanic()
    {
    	if(!Combat.isInWild()) return false;
		final int maxBracket = Players.localPlayer().getLevel() + Combat.getWildernessLevel();
		final int minBracket = Players.localPlayer().getLevel() - Combat.getWildernessLevel();
    	for(Player p : Players.all())
		{
			if(p == null || !p.exists() || p.getName().equals(Players.localPlayer().getName()) || p.getHealthPercent() == 0) continue;
			if(p.distance() <= 10 && 
					p.getLevel() >= minBracket && 
					p.getLevel() <= maxBracket) 
			{
				MethodProvider.log("PKer Panic!");
				return true;
			}
		}
    	return false;
    }
    public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(267);
    }
	@Override
	public boolean isValid() {
		return API.mode == API.modes.MAGE_ARENA_1;
	}
	public static boolean fulfillMageArena1()
    {
    	InvEquip.clearAll();
    	setEquipment();
    	InvEquip.setEquipItem(EquipmentSlot.WEAPON, id.staffOfAir);
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.addInvyItem(id.waterRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.earthRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.fireRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	if(Skills.getRealLevel(Skill.MAGIC) < 62) InvEquip.addInvyItem(id.deathRune, 200, (int) Calculations.nextGaussianRandom(250, 25), false, (int) Calculations.nextGaussianRandom(350, 25));
    	else InvEquip.addInvyItem(id.bloodRune, 150, (int) Calculations.nextGaussianRandom(185, 15), false, (int) Calculations.nextGaussianRandom(350, 25));
    	InvEquip.addInvyItem(id.prayPot4, 2, Calculations.random(2, 4), false, (int) Calculations.nextGaussianRandom(10, 2));
    	if(InvEquip.bankContains(id.staminas))
    	{
    		InvEquip.addInvyItem(InvEquip.getBankItem(id.staminas), 1, 1, false, 0);
    	}
    	else InvEquip.addInvyItem(id.stamina4, 1, 1, false, (int) Calculations.nextGaussianRandom(10, 3));
    	
    	InvEquip.addInvyItem(id.knife, 1, 1, false, 1);
    	
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(TrainRanged.seaTurtle, 3, 10, false, (int) Calculations.nextGaussianRandom(50, 10));
    	
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[Mage Arena 1] -> Fulfilled equipment correctly!");
			return true;
		} else 
		{
			MethodProvider.log("[Mage Arena 1] -> NOT fulfilled equipment correctly!");
			return false;
		}
    	
    }
	public static boolean handleDialogues()
	{
		
		if(Dialogues.canContinue())
		{
			if(Dialogues.getNPCDialogue() != null && Dialogues.getNPCDialogue().contains("You step into the pool of sparkling water."))
			{
				MethodProvider.log("Saw sparkling pool dialogue");
				if(Dialogues.continueDialogue()) Sleep.sleep(2222, 2222);
				return true;
			}
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
			return Dialogues.chooseOption("Yes, and don\'t show this warning again.") || 
					 Dialogues.chooseOption("Can I fight here?") || 
					 Dialogues.chooseOption("Yes indeedy.") || 
					 Dialogues.chooseOption("Okay, let\'s fight.") || 
					 Dialogues.chooseOption("I think I\'ve had enough for now.") || 
					Dialogues.chooseOption("Yes, I\'m brave.");
		}
		return false;
	}
	public static void setEquipment()
	{
		InvEquip.setEquipItem(EquipmentSlot.CHEST, TrainMagic.blackRobe);
		InvEquip.setEquipItem(EquipmentSlot.LEGS, TrainMagic.zamorakMonkBottom);
		InvEquip.setEquipItem(EquipmentSlot.HAT,TrainMagic.creamHat);
		InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
		InvEquip.setEquipItem(EquipmentSlot.SHIELD, TrainRanged.woodenShield);
		InvEquip.setEquipItem(EquipmentSlot.WEAPON, id.staffOfAir);
		InvEquip.setEquipItem(EquipmentSlot.FEET, TrainMagic.blueBoots);
	}
	
}
