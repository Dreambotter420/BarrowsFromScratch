package script.quest.magearena1;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.skills.magic.Casting;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Sleepz;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;

/**
 * Completes Mage Arena 1
 * @author Dreambotter420
 * ^_^
 */
public class MageArena1 extends Leaf {
	public static boolean slashed = false;
    public void onStart() {
        
    }
    public boolean onExit() {
        if(Locations.mageArenaBank.contains(Players.getLocal()))
        {
        	if(!Walkz.useJewelry(InvEquip.glory, "Edgeville") && !Walkz.useJewelry(InvEquip.combat, "Ranging Guild"))
        	{
        		if(Walkz.teleportVarrock(30000))
        		{
        			Logger.log("Teleported to varrock!");
        		}
        	}
        }
        return true;
    }
    
    public static boolean completed()
    {
    	if(getProgressValue() == 8) //finished value
    	{
    		if(Players.getLocal().isAnimating())
        	{
        		Sleepz.sleep(69, 696);
        		return true;
        	}
        	if(Locations.mageArenaCave.contains(Players.getLocal()))
        	{
        		if(Inventory.count(id.saradominCape) < 2)
            	{
            		getCape("Saradomin");
            		return true;
            	}
            	if(Inventory.count(id.guthixCape) < 2)
            	{
            		getCape("Guthix");
            		return true;
            	}
            	if(Inventory.count(id.zamorakCape) < 2)
            	{
            		getCape("Zamorak");
            		return true;
            	}
            	API.walkTalkWithGameObject("Sparkling pool", "Step-into",Locations.mageArenaCaveSouth);
            	return true;
        	}
        	if(Locations.mageArenaBank.contains(Players.getLocal()))
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
        			return true;
        		}
        	}
    		return true;
    	}
    	return false;
    }
    
    @Override
    public int onLoop() {
    	if (completed()) {
            Logger.log("[FINISHED] -> Mage Arena 1");
            if(onExit()) 
            {
            	Main.paint_task = "~~~~~~~~~~~";
        		Main.paint_itemsCount = "~Quest Complete~";
        		Main.paint_subTask = "~Mage Arena 1~";
        		Main.paint_levels = "~~~~~~~~~~~";
            	API.mode = null;
            }
            return Sleepz.sleepTiming();
        }
    	if (DecisionLeaf.taskTimer.finished()) {
            Logger.log("[TIMEOUT] -> Mage Arena 1");
            if(onExit()) API.mode = null;
            return Sleepz.sleepTiming();
        }

    	if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("Mage Arena I");
        if(Dialoguez.handleDialogues()) return Sleepz.sleepTiming();
        switch(getProgressValue())
        {
        //just prayed at first statue
        case(7):
        {
        	if(Players.getLocal().isAnimating())
        	{
        		Sleepz.sleep(696, 696);
        		break;
        	}
        	if(Locations.mageArenaCave.contains(Players.getLocal()))
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
        	if(Players.getLocal().isAnimating())
        	{
        		Sleepz.sleep(696, 696);
        		break;
        	}
        	if(Prayers.isActive(Prayer.PROTECT_FROM_MAGIC))
        	{
        		Tabz.open(Tab.PRAYER);
        		Prayers.toggle(false, Prayer.PROTECT_FROM_MAGIC);
        		Sleepz.sleep(69, 696);
        		break;
        	}
        	if(Locations.mageArenaCave.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Statue of Saradomin", "Pray-at", Locations.mageArenaCaveStatues, Dialogues::inDialogue);
    			break;
        	}
        	if(Locations.mageArenaBank.contains(Players.getLocal()))
        	{
        		API.walkInteractWithGameObject("Sparkling pool", "Step-into", Locations.mageArenaBank, Dialogues::inDialogue);
        		break;
        	}
        	break;
        }
        case(5):case(4):case(3):case(2):case(1):
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
        
        return Sleepz.sleepTiming();
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
    		Sleep.sleepUntil(() -> Inventory.count(capeName) > count, Sleepz.calculate(2222, 2222));
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
    	if(Locations.mageArenaInnerCircle.contains(Players.getLocal()))
    	{
    		if(Combatz.shouldDrinkPrayPot()) 
    		{
    			Combatz.drinkPrayPot();
    			return;
    		}
    		if(!Prayers.isActive(Prayer.PROTECT_FROM_MAGIC))
    		{
    			Tabz.open(Tab.PRAYER);
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
    			Logger.log("Toggling autoretaliate");
        		Combat.toggleAutoRetaliate(true);
        	}
    		if(!Casting.isAutocastingHighest())
    		{
    			Casting.setHighestAutocast();
    			return;
    		}
    		if(!Players.getLocal().isInCombat())
    		{
    			API.interactNPC("Kolodion", "Attack", Locations.mageArenaInnerCircle, true, () -> Players.getLocal().isInCombat());
    		}
    		return;
    	}
    	if(goToMageArenaBank())
    	{
    		Sleepz.sleep(2222, 2222);
    		if(Locations.mageArenaBank.contains(Players.getLocal()))
    		{
    			API.talkToNPC("Kolodion");
    		}
    		return;
    	}
    }
    public static boolean goToMageArenaBank()
    {
    	slashed = false;
    	if(Locations.mageArenaBank.contains(Players.getLocal())) return true;
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
    				if(Walking.toggleRun()) Sleepz.sleep(696, 666);
    			}
    		}
    	}
    	if(Combat.isAutoRetaliateOn())
    	{
    		Combat.toggleAutoRetaliate(false);
    	}
    	if(Locations.mageArenaBankOutside.contains(Players.getLocal()))
    	{
    		GameObject lever = GameObjects.closest(g -> 
			g!=null && 
			g.getName().contains("Lever") &&
			g.hasAction("Pull") &&
			Locations.mageArenaBankLeverRoom.contains(g));
    		if(lever == null) //lever being pulled!! Go to lever tile and wait..
    		{
    			if(Locations.mageArenaBankOutsideLeverTile.equals(Players.getLocal().getTile())) return false;
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
    			if(Locations.mageArenaBankLeverAirlock.contains(Players.getLocal()))
    			{
    				if(web2 != null)
    				{
    					if(web2.interact("Slash"))
    					{
    						if(shouldPanic()) return false;
        					Sleep.sleepUntil(() -> slashed, Sleepz.calculate(2222, 2222));
        					Sleepz.sleep(420, 696);
    					}
    				}
    				return false;
    			}
    			if(Locations.mageArenaBankOutside.contains(Players.getLocal()))
    			{
    				if(web1 != null)
    				{
    					if(web1.interact("Slash"))
    					{
    						if(shouldPanic()) return false;
        					Sleep.sleepUntil(() -> slashed, Sleepz.calculate(2222, 2222));
        					Sleepz.sleep(420, 696);
    					}
    				}
    				if(web2 != null && web1 == null)
    				{
    					if(web2.interact("Slash"))
    					{
    						if(shouldPanic()) return false;
        					Sleep.sleepUntil(() -> slashed, Sleepz.calculate(2222, 2222));
        					Sleepz.sleep(420, 696);
    					}
    				}
    				
    				return false;
    			}
    			return false;
    		}
    		if(lever == null)
    		{
    			if(shouldPanic()) Walking.walkExact(Locations.mageArenaBankOutsideLeverTile);
    			else if(Walking.shouldWalk(6) && Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleepz.sleep(696, 666);
    			return false;
    		}
    		if(lever.interact("Pull"))
    		{
    			Logger.log("Pulled lever!");
    			if(shouldPanic()) return false;
    			Sleep.sleepUntil(() -> Locations.mageArenaBank.contains(Players.getLocal()), 
    					() -> Players.getLocal().isMoving(),
    					Sleepz.calculate(2222, 2222),69);
    		}
    		return false;
    	}
    	if(Locations.deepWildyEdgevilleLeverPeninsula.contains(Players.getLocal()))
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
    				Logger.log("Web too far");
    				if(shouldPanic())
    	    		{
    	    			if(Walking.walk(slashTile)) Sleepz.sleep(420, 696);
    	    		}
    	    		else if(Walking.shouldWalk(6) && Walking.walk(slashTile)) Sleepz.sleep(420, 696);
    				
    			}
    			else if(web.interact("Slash"))
    			{
    				Logger.log("Web slashed!");
    				if(shouldPanic()) return false;
    				Sleep.sleepUntil(() -> slashed, 
    					() -> Players.getLocal().isMoving(), Sleepz.calculate(2222, 2222),69);
					Sleepz.sleep(420, 696);
    				return false;
    			} else
    			{
    				Logger.log("Web walking");
    				Walking.walk(slashTile);
    			}
    			return false;
    		}
    		Logger.log("mage arena bank walking");
    		if(shouldPanic())
    		{
    			if(Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleepz.sleep(420, 696);
    		}
    		else if(Walking.shouldWalk(6) && Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleepz.sleep(420, 696);
			return false;
    	}
    	if(Combat.isInWild() && Combat.getWildernessLevel() > 30)
    	{
    		if(shouldPanic()) Walking.walk(Locations.mageArenaBankOutsideLeverTile);
    		else if(Walking.shouldWalk(6) && Walking.walk(Locations.mageArenaBankOutsideLeverTile)) Sleepz.sleep(420, 696);
    		return false;
    	}
    	if(Locations.edgevillePKLever.contains(Players.getLocal()))
    	{
    		GameObject lever = GameObjects.closest(g -> 
			g!=null && 
			g.getName().contains("Lever") &&
			g.hasAction("Pull") &&
			Locations.edgevillePKLever.contains(g));
    		if(lever == null)
    		{
    			Sleepz.sleep(696, 666);
    			return false;
    		}
    		if(lever.interact("Pull"))
    		{
    			Sleep.sleepUntil(() -> Locations.deepWildyEdgevilleLeverPeninsula.contains(Players.getLocal()),
    					() -> Players.getLocal().isMoving(),
    					Sleepz.calculate(3333, 3333),69);
    		}
    		return false;
    	}
    	if(Locations.edgevillePKLever.getCenter().distance() <= 50)
    	{
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.edgevillePKLever.getCenter())) Sleepz.sleep(696, 666);
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
		final int maxBracket = Players.getLocal().getLevel() + Combat.getWildernessLevel() + 1;
		final int minBracket = Players.getLocal().getLevel() - Combat.getWildernessLevel() - 1;
    	for(Player p2 : Players.all())
		{
			if(p2 == null || !p2.exists() || p2.getName().equals(Players.getLocal().getName()) || p2.getHealthPercent() == 0) continue;
			if(p2.distance() <= 10 && 
					p2.getLevel() >= minBracket && 
					p2.getLevel() <= maxBracket) 
			{
				Logger.log("PKer Panic!");
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
    	InvEquip.addInvyItem(id.seaTurtle, 3, 10, false, (int) Calculations.nextGaussianRandom(50, 10));
    	
    	if(InvEquip.fulfillSetup(true, 180000))
		{
			Logger.log("[Mage Arena 1] -> Fulfilled equipment correctly!");
			return true;
		} else 
		{
			Logger.log("[Mage Arena 1] -> NOT fulfilled equipment correctly!");
			return false;
		}
    	
    }
	
	public static void setEquipment()
	{
		InvEquip.setEquipItem(EquipmentSlot.CHEST, id.blackRobe);
		InvEquip.setEquipItem(EquipmentSlot.LEGS, id.zamorakMonkBottom);
		InvEquip.setEquipItem(EquipmentSlot.HAT,id.creamHat);
		InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
		InvEquip.setEquipItem(EquipmentSlot.SHIELD, id.woodenShield);
		InvEquip.setEquipItem(EquipmentSlot.WEAPON, id.staffOfAir);
		InvEquip.setEquipItem(EquipmentSlot.FEET, id.blueBoots);
	}
	
}
