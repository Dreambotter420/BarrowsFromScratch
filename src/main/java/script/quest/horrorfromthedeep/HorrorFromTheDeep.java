package script.quest.horrorfromthedeep;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.combat.CombatStyle;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.p;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.varrockmuseum.Timing;
import script.skills.magic.Casting;
import script.skills.magic.TrainMagic;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Sleep;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Horror From The Deep
 * @author Dreambotter420
 * ^_^
 */
public class HorrorFromTheDeep extends Leaf {
	public static boolean completed()
	{
		return getProgressValue() == 10 && (Bank.contains(id.bookOfLaw) || Inventory.contains(id.bookOfLaw) || Equipment.contains(id.bookOfLaw));
	}
	static List<Integer> doorItems = new ArrayList<Integer>();
	public static void onStart()
	{
		doorItems.add(id.fireRune);
		doorItems.add(id.airRune);
		doorItems.add(id.waterRune);
		doorItems.add(id.earthRune);
		doorItems.add(id.steelArrow);
		doorItems.add(id.bronzeSword);
		Collections.shuffle(doorItems);
	}
	@Override
	public boolean isValid() {
		return API.mode == API.modes.HORROR_FROM_THE_DEEP;
	}
	public static boolean needPage1 = false;
	public static boolean needPage2 = false;
	public static boolean needPage3 = false;
	public static boolean needPage4 = false;
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Horror From The Deep");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if(completed())
        {
        	MethodProvider.log("[COMPLETED] -> Horror From The Deep!");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        onStart();
        if(Dialoguez.handleDialogues()) return Sleep.calculate(420, 696);
        switch(getProgressValue())
        {
        case(10):
        {
        	WidgetChild armadylBookButton = Widgets.getWidgetChild(302, 7);
		
        	if(Questz.closeQuestCompletion()) break;
        	
        	if(Inventory.contains(id.damagedArmadylBook))
        	{
        		//close rewards shop interface
        		if(armadylBookButton != null && armadylBookButton.isVisible())
        		{
        			Widgets.getWidgetChild(302,1,11).interact();
        			break;
        		}
        		
        		if(BankLocation.GRAND_EXCHANGE.getCenter().distance() >= 25)
        		{
        			Walkz.useJewelry(InvEquip.wealth, "Grand Exchange");
        			break;
        		}
        		if(needPage1)
        		{
        			if(Bank.contains(id.armaPage1))
					{
						InvEquip.withdrawOne(id.armaPage1, 30000);
						break;
					}
					if(Inventory.contains(id.armaPage1))
					{
						final int count = Inventory.count(id.armaPage1);
						if(Inventory.get(id.armaPage1).useOn(id.damagedArmadylBook))
						{
							MethodProvider.sleepUntil(() -> Inventory.count(id.armaPage1) <= count, Sleep.calculate(2222, 2222));
							needPage1 = false;
						}
						break;
					}
					InvEquip.buyItem(id.armaPage1, 1, 180000);
					if(!Inventory.contains(id.armaPage1)) InvEquip.buyItem(id.armaPage1, 1,250000, 180000);
					break;
        		}
        		if(needPage2)
        		{
        			if(Bank.contains(id.armaPage2))
					{
						InvEquip.withdrawOne(id.armaPage2, 30000);
						break;
					}
					if(Inventory.contains(id.armaPage2))
					{
						final int count = Inventory.count(id.armaPage2);
						if(Inventory.get(id.armaPage2).useOn(id.damagedArmadylBook))
						{
							MethodProvider.sleepUntil(() -> Inventory.count(id.armaPage2) <= count, Sleep.calculate(2222, 2222));
							needPage2 = false;
						}
						break;
					}
					InvEquip.buyItem(id.armaPage2, 1, 180000);
					if(!Inventory.contains(id.armaPage2)) InvEquip.buyItem(id.armaPage2, 1,250000, 180000);
					break;
        		}
        		if(needPage3)
        		{
        			if(Bank.contains(id.armaPage3))
					{
						InvEquip.withdrawOne(id.armaPage3, 30000);
						break;
					}
					if(Inventory.contains(id.armaPage3))
					{
						final int count = Inventory.count(id.armaPage3);
						if(Inventory.get(id.armaPage3).useOn(id.damagedArmadylBook))
						{
							MethodProvider.sleepUntil(() -> Inventory.count(id.armaPage3) <= count, Sleep.calculate(2222, 2222));
							needPage3 = false;
						}
						break;
					}
					InvEquip.buyItem(id.armaPage3, 1, 180000);
					if(!Inventory.contains(id.armaPage3)) InvEquip.buyItem(id.armaPage3, 1,250000, 180000);
					break;
        		}
        		if(needPage4)
        		{
        			if(Bank.contains(id.armaPage4))
					{
						InvEquip.withdrawOne(id.armaPage4, 30000);
						break;
					}
					if(Inventory.contains(id.armaPage4))
					{
						final int count = Inventory.count(id.armaPage4);
						if(Inventory.get(id.armaPage4).useOn(id.damagedArmadylBook))
						{
							MethodProvider.sleepUntil(() -> Inventory.count(id.armaPage4) <= count, Sleep.calculate(2222, 2222));
							needPage4 = false;
						}
						break;
					}
					InvEquip.buyItem(id.armaPage4, 1, 180000);
					if(!Inventory.contains(id.armaPage4)) InvEquip.buyItem(id.armaPage4, 1,250000, 180000);
					break;
        		}
        		
        		
        		if(Tabz.open(Tab.INVENTORY))
        		{
        			if(Inventory.interact(id.damagedArmadylBook, "Check")) Sleep.sleep(696, 1111);
        		}
        		break;
        	}
        	if(Inventory.contains(id.rustyCasket))
        	{
        		if(Locations.horror_realLighthouseLvl2.contains(p.l))
        		{
        			API.talkToNPC("Jossik");
        			break;
        		}
        		if(Locations.horror_realLighthouseLvl1.contains(p.l))
        		{
        			API.interactWithGameObject("Staircase", "Climb-up", Locations.horror_realLighthouseLvl1, () -> Locations.horror_realLighthouseLvl2.contains(p.l));
        			break;
        		}
        		if(Locations.horror_caveV2.contains(p.l))
        		{
        			API.interactWithGameObject("Iron ladder", "Climb", Locations.horror_caveV2, () -> Locations.horror_realLighthouseLvl1.contains(p.l));
        			break;
        		}
            	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l))
            	{
            		if(Walkz.walkToArea(Locations.horror_larrissaLighthouse))
            		{
            			API.interactWithGameObject("Doorway","Walk-through",() -> !Locations.horror_larrissaLighthouse.contains(p.l));
            			break;
            		}
            		break;
            	}
        		walkToLighthouseIsland();
        		break;
        	}
        	if(Inventory.contains(id.damagedZamorakBook))
        	{
        		if(armadylBookButton != null && armadylBookButton.isVisible())
        		{
        			if(armadylBookButton.interact("Unlock"))
        			{
        				MethodProvider.sleepUntil(() -> Inventory.contains(id.damagedArmadylBook), Sleep.calculate(2222, 2222));
        				break;
        			}
        		}
        		if(Locations.horror_realLighthouseLvl2.contains(p.l))
        		{
        			API.talkToNPC("Jossik","Rewards");
        			break;
        		}
        		if(Locations.horror_realLighthouseLvl1.contains(p.l))
        		{
        			API.interactWithGameObject("Staircase", "Climb-up", Locations.horror_realLighthouseLvl1, () -> Locations.horror_realLighthouseLvl2.contains(p.l));
        			break;
        		}
        		if(Locations.horror_caveV2.contains(p.l))
        		{
        			API.interactWithGameObject("Iron ladder", "Climb", Locations.horror_caveV2, () -> Locations.horror_realLighthouseLvl1.contains(p.l));
        			break;
        		}
            	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l))
            	{
            		if(Walkz.walkToArea(Locations.horror_larrissaLighthouse))
            		{
            			API.interactWithGameObject("Doorway","Walk-through",() -> !Locations.horror_larrissaLighthouse.contains(p.l));
            			break;
            		}
            		break;
            	}
        		walkToLighthouseIsland();
        		break;
        	}
        }
        case(5):
        {
        	killDaggannothMother();
        	break;
        }
        case(4):
        {
        	kill1stDaggannoth();
        	break;
        }
        case(2):case(3):
        {
        	repairLighthouse();
        	break;
        }
        case(1):
        {
        	repairBridgeGetUseKey();
        	break;
        }
        case(0):
        {
        	if(Inventory.contains(id.diamondBoltsE))
        	{
        		InvEquip.equipItem(id.diamondBoltsE);
        		break;
        	}
        	if(shouldFulfillStep0())
        	{
        		fulfillStep0();
        		break;
        	}
        	talkToLarrissa();
        	break;
        }
        default:break;
        }
        return Sleep.calculate(420, 696);
    }
    public static boolean repairedBridge1()
    {
    	return PlayerSettings.getBitValue(36) == 1;
    }
    public static boolean repairedBridge2()
    {
    	return PlayerSettings.getBitValue(37) == 1;
    }
    public static boolean gotKey()
    {
    	return PlayerSettings.getBitValue(38) == 1;
    }
    public static boolean usedTar ()
    {
    	return PlayerSettings.getBitValue(46) == 1;
    }
    public static boolean usedTinderbox ()
    {
    	return PlayerSettings.getBitValue(48) == 1;
    }
    public static boolean usedGlass ()
    {
    	return PlayerSettings.getBitValue(47) == 1;
    }
    public static boolean usedAirRune ()
    {
    	return PlayerSettings.getBitValue(43) == 1;
    }
    public static boolean usedEarthRune ()
    {
    	return PlayerSettings.getBitValue(42) == 1;
    }
    public static boolean usedWaterRune ()
    {
    	return PlayerSettings.getBitValue(41) == 1;
    }
    public static boolean usedFireRune ()
    {
    	return PlayerSettings.getBitValue(40) == 1;
    }
    public static boolean usedSword ()
    {
    	return PlayerSettings.getBitValue(44) == 1;
    }
    public static boolean usedArrow ()
    {
    	return PlayerSettings.getBitValue(45) == 1;
    }
    public static boolean doorUnlocked ()
    {
    	return PlayerSettings.getBitValue(35) == 1;
    }
    public static int getProgressValue()
    {
    	return PlayerSettings.getBitValue(34);
    }
    public static void walkToBossFight()
    {
    	if(Locations.horror_caveAirlock.contains(p.l))
    	{
    		API.interactWithGameObject("Iron ladder", "Climb", () -> !Locations.horror_caveAirlock.contains(p.l));
    		return;
    	}
    	if(Locations.horror_caveLobby.contains(p.l))
    	{
    		if(Walkz.walkToArea(Locations.horror_caveDoorSouth))
    		{
    			if(!doorUnlocked())
        		{
        			if(doorItems.get(0) == id.fireRune)
        			{
        				if(usedFireRune()) doorItems.remove(0);
        				else if(Inventory.get(doorItems.get(0)).useOn(GameObjects.closest(g -> g!=null && g.getName().equals("Strange wall") && (g.getID() == 4544 || g.getID() == 4543)))) 
        				{
        					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),() -> p.l.isMoving(), Sleep.calculate(3333,2222),69);
        				}
            			return;
        			}
        			if(doorItems.get(0) == id.airRune)
        			{
        				if(usedAirRune()) doorItems.remove(0);
        				else if(Inventory.get(doorItems.get(0)).useOn(GameObjects.closest(g -> g!=null && g.getName().equals("Strange wall") && (g.getID() == 4544 || g.getID() == 4543)))) 
        				{
        					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),() -> p.l.isMoving(), Sleep.calculate(3333,2222),69);
        				}
            			return;
        			}
        			if(doorItems.get(0) == id.waterRune)
        			{
        				if(usedWaterRune()) doorItems.remove(0);
        				else if(Inventory.get(doorItems.get(0)).useOn(GameObjects.closest(g -> g!=null && g.getName().equals("Strange wall") && (g.getID() == 4544 || g.getID() == 4543)))) 
        				{
        					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),() -> p.l.isMoving(), Sleep.calculate(3333,2222),69);
        				}
            			return;
        			}
        			if(doorItems.get(0) == id.earthRune)
        			{
        				if(usedEarthRune()) doorItems.remove(0);
        				else if(Inventory.get(doorItems.get(0)).useOn(GameObjects.closest(g -> g!=null && g.getName().equals("Strange wall") && (g.getID() == 4544 || g.getID() == 4543)))) 
        				{
        					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),() -> p.l.isMoving(), Sleep.calculate(3333,2222),69);
        				}
            			return;
        			}
        			if(doorItems.get(0) == id.steelArrow)
        			{
        				if(usedArrow()) doorItems.remove(0);
        				else if(Inventory.get(doorItems.get(0)).useOn(GameObjects.closest(g -> g!=null && g.getName().equals("Strange wall") && (g.getID() == 4544 || g.getID() == 4543)))) 
        				{
        					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),() -> p.l.isMoving(), Sleep.calculate(3333,2222),69);
        				}
            			return;
        			}
        			if(doorItems.get(0) == id.bronzeSword)
        			{
        				if(usedSword()) doorItems.remove(0);
        				if(Inventory.contains(id.staffOfFire))
        				{
        					InvEquip.equipItem(id.staffOfFire);
        					return;
        				}
        				else if(Inventory.get(doorItems.get(0)).useOn(GameObjects.closest(g -> g!=null && g.getName().equals("Strange wall") && (g.getID() == 4544 || g.getID() == 4543))))
        				{
        					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),() -> p.l.isMoving(), Sleep.calculate(3333,2222),69);
        				}
            			return;
        			}
        			return;
        		}
    			//must have all items used on door now, ok to enter
    			API.interactWithGameObject("Strange wall", "Open", Locations.horror_strangeDoorEntrance, () -> Locations.horror_caveAirlock.contains(p.l));
    		}
    		return;
    	}
    	if(Locations.horror_lighthouseFakeInstanceLvl3V2.contains(p.l))
    	{
    		API.interactWithGameObject("Staircase", "Climb-down", () -> Locations.horror_lighthouseFakeInstanceLvl2.contains(p.l));
    		return;
    	}
    	if(Locations.horror_lighthouseFakeInstanceLvl2.contains(p.l))
    	{
    		API.interactWithGameObject("Staircase", "Climb-down", () -> Locations.horror_lighthouseFakeInstanceLvl1.contains(p.l));
    		return;
    	}
    	if(Locations.horror_lighthouseFakeInstanceLvl1.contains(p.l))
    	{
    		API.interactWithGameObject("Iron ladder", "Climb", () -> !Locations.horror_lighthouseFakeInstanceLvl1.contains(p.l));
    		return;
    	}
    	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l))
    	{
    		if(Walkz.walkToArea(Locations.horror_larrissaLighthouse))
    		{
    			API.interactWithGameObject("Doorway","Walk-through",() -> !Locations.horror_larrissaLighthouse.contains(p.l));
    		}
    		return;
    	}
    	walkToLighthouseIsland();
    }
    public static final int dagFire = 985;
    public static final int dagRange = 987;
    public static final int dagAir = 983;
    public static final int dagWater = 984;
    public static final int dagMelee = 988;
    public static final int dagEarth = 986;
    public static void killDaggannothMother()
    {
    	if(Locations.horror_fakeInstanceBossFightArea.contains(p.l))
    	{
    		if(HintArrow.exists())
    		{
    			if(Combatz.shouldEatFood(15)) Combatz.eatFood();
    			if(Combatz.shouldDrinkPrayPot()) Combatz.drinkPrayPot();
    			if(!Prayers.isActive(Prayer.PROTECT_FROM_MISSILES))
    			{
    				if(Tabz.open(Tab.PRAYER))
    				{
    					Prayers.toggle(true, Prayer.PROTECT_FROM_MISSILES);
    				}
    			}
    			if(Combat.isAutoRetaliateOn())
    			{
    				if(Tabz.open(Tab.COMBAT)) Combat.toggleAutoRetaliate(false);
    				return;
    			}
    			if(Locations.horror_1stDaggannothSafespot.distance() <= 2)
    			{
    				org.dreambot.api.wrappers.interactive.Character dag = HintArrow.getPointed();
					if(dag == null)
					{
						MethodProvider.log("Dagganoth mother is null in boss fight with hint arrow existing!");
						return;
					}
					if(Casting.isAutocasting()) 
					{
						Casting.voidAutocast();
						return;
					}
    				int magic = Skills.getRealLevel(Skill.MAGIC);
    				switch(dag.getID())
    				{
    				case(dagFire):
    				{
    					if(Inventory.contains(id.staffOfFire))
    					{
    						if(Tabz.open(Tab.INVENTORY))
        					{
        						 InvEquip.equipItem(id.staffOfFire);
        					}
    					}
    					if(Tabz.open(Tab.MAGIC))
    					{
    						if(magic >= 75) Magic.castSpellOn(Normal.FIRE_WAVE, dag);
    						else Magic.castSpellOn(Normal.FIRE_BLAST, dag);
    						Sleep.sleep(1111, 2222);
    					}
    					break;
    				}
    				case(dagAir):
    				{
    					if(Inventory.contains(id.staffOfFire))
    					{
    						if(Tabz.open(Tab.INVENTORY))
        					{
        						 InvEquip.equipItem(id.staffOfFire);
        					}
    					}
    					if(Tabz.open(Tab.MAGIC))
    					{
    						if(magic >= 62) Magic.castSpellOn(Normal.WIND_WAVE, dag);
    						else Magic.castSpellOn(Normal.WIND_BLAST, dag);
    						Sleep.sleep(1111, 2222);
    					}
    					break;
    				}
    				case(dagWater):
    				{
    					
    					if(Inventory.contains(id.staffOfFire))
    					{
    						if(Tabz.open(Tab.INVENTORY))
        					{
        						 InvEquip.equipItem(id.staffOfFire);
        					}
    					}if(Tabz.open(Tab.MAGIC))
    					{
    						if(magic >= 65) Magic.castSpellOn(Normal.WATER_WAVE, dag);
    						else Magic.castSpellOn(Normal.WATER_BLAST, dag);
    						Sleep.sleep(1111, 2222);
    					}
    					break;
    				}
    				case(dagEarth):
    				{
    					if(Inventory.contains(id.staffOfFire))
    					{
    						if(Tabz.open(Tab.INVENTORY))
        					{
        						 InvEquip.equipItem(id.staffOfFire);
        					}
    					}
    					if(Tabz.open(Tab.MAGIC))
    					{
    						if(magic >= 70) Magic.castSpellOn(Normal.EARTH_WAVE, dag);
    						else Magic.castSpellOn(Normal.EARTH_BLAST, dag);
    						Sleep.sleep(1111, 2222);
    					}
    					break;
    				}

    				case(dagRange):
    				{
    					if(Magic.isSpellSelected()) Magic.deselect();
    					if(Equipment.count(id.diamondBoltsE) <= 0 && 
    							Inventory.count(id.diamondBoltsE) <= 0) break;
    					if(Equipment.contains(id.adamantCrossbow) || 
    							Equipment.contains(id.runeCrossbow))
    					{
    						if(Combat.getCombatStyle() != CombatStyle.RANGED_RAPID)
    						{
    							if(Tabz.open(Tab.COMBAT))
        						{
        							Combat.setCombatStyle(CombatStyle.RANGED_RAPID);
        						}
    							break;
    						}
    						if(!p.l.isInteracting(dag))
    						{
    							dag.interact("Attack");
    							Sleep.sleep(1111,2222);
    						}
    						break;
    					}
    					if(Tabz.open(Tab.INVENTORY))
    					{
    						if(Inventory.contains(id.runeCrossbow)) InvEquip.equipItem(id.runeCrossbow);
    						else InvEquip.equipItem(id.adamantCrossbow);
    					}
    					break;
    				}
    				default:break;//dont melee lol
    				}
    			}
    			Walking.walk(Locations.horror_1stDaggannothSafespot);
    			Sleep.sleep(111, 420);
    			return;
    		}
    		API.talkToNPC("Jossik");
    		return;
    	}
    	walkToBossFight();
    }
    public static void kill1stDaggannoth()
    {
    	if(Locations.horror_fakeInstanceBossFightArea.contains(p.l))
    	{
    		if(!InvEquip.equipmentContains(InvEquip.wearableGlory) && InvEquip.invyContains(InvEquip.wearableGlory))
    		{
    			InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
    			return;
    		}
    		if(HintArrow.exists())
    		{
    			if(Combatz.shouldEatFood(15)) Combatz.eatFood();
    			if(Locations.horror_1stDaggannothSafespot.distance() <= 2)
    			{
    				if(!Casting.isAutocastingHighest())
        			{
        				Casting.setHighestAutocast();
        				return;
        			}
    				NPC dag = NPCs.closest(979);
    				if(dag != null) 
    				{
    					if(!p.l.isInteracting(dag))
    					{
    						dag.interact("Attack");
    						Sleep.sleep(1111,1111);
    	    				return;
    					}
    				}
    			}
    			Walking.walk(Locations.horror_1stDaggannothSafespot);
    			Sleep.sleep(111, 420);
    			return;
    		}
    		API.talkToNPC("Jossik");
    		return;
    	}
    	walkToBossFight();
    }
    public static void repairLighthouse()
    {
    	if(Locations.horror_lighthouseFakeInstanceLvl3.contains(p.l))
    	{
    		GameObject lightingMechanism = GameObjects.closest(g->g!=null && 
    				g.getName().equals("Lighting mechanism") && 
    				g.getID() == 4588); //main lighting mechanism ID
    		if(lightingMechanism == null)
    		{
    			MethodProvider.log("Mechanism null in lighthouse fake instanced 3rd floor!");
    			return;
    		}
    		Item i = null;
    		if(!usedTar()) i = Inventory.get(id.swampTar);
    		else if(!usedTinderbox()) i = Inventory.get(id.tinderbox);
    		else if(!usedGlass()) i = Inventory.get(id.moltenGlass);
    		if(i == null) 
    		{
    			fulfillStep1AfterPlanks();
    			return;
    		}
    		if(i.useOn(lightingMechanism)) Sleep.sleep(420,1111);
    		
    		return;
    	}
    	if(Locations.horror_lighthouseFakeInstanceLvl2.contains(p.l))
    	{
    		API.interactWithGameObject("Staircase", "Climb-up", () -> Locations.horror_lighthouseFakeInstanceLvl3.contains(p.l));
    		return;
    	}
    	if(Locations.horror_lighthouseFakeInstanceLvl1.contains(p.l))
    	{
    		API.interactWithGameObject("Staircase", "Climb-up", () -> Locations.horror_lighthouseFakeInstanceLvl2.contains(p.l));
    		return;
    	}
    	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l))
    	{
    		if(Walkz.walkToArea(Locations.horror_larrissaLighthouse))
    		{
    			API.interactWithGameObject("Doorway","Walk-through",() -> !Locations.horror_larrissaLighthouse.contains(p.l));
        		return;
    		}
    		return;
    	}
    	walkToLighthouseIsland();
    }
    public static void repairBridgeGetUseKey()
    {
    	if(!repairedBridge1())
    	{
    		if(!Inventory.contains(id.plank))
    		{
    			fulfillStep0();
    			return;
    		}
    		if(!Locations.horror_lighthouseAndBridgeArea.contains(p.l))
    		{
    			walkToLighthouseIsland();
    			return;
    		}
    		if(Walkz.walkToArea(Locations.horror_bridgeWest))
    		{
    			GameObject bridgeWest = GameObjects.closest(g -> g!=null && 
    					g.getName().equals("Broken bridge") && 
    					g.getTile().equals(Locations.horror_bridgeWestTile));
    			if(bridgeWest == null)
    			{
    				MethodProvider.log("Broken bridge is null!");
    				return;
    			}
    			if(Inventory.get(id.plank).useOn(bridgeWest))
    			{
    				MethodProvider.sleepUntil(() -> repairedBridge1(),
    						() -> p.l.isMoving(),
    						Sleep.calculate(2222, 2222),69);
    			}
    			return;
    		}
    		return;
    	}
    	if(!repairedBridge2())
    	{
    		if(!Inventory.contains(id.plank))
    		{
    			fulfillStep0();
    			return;
    		}
    		if(!Locations.horror_lighthouseAndBridgeArea.contains(p.l))
    		{
    			walkToLighthouseIsland();
    			return;
    		}
    		if(Locations.horror_bridgeEast.contains(p.l))
    		{
    			GameObject bridgeEast = GameObjects.closest(g -> g!=null && 
    					g.getName().equals("Broken bridge") && 
    					g.getTile().equals(Locations.horror_bridgeEastTile));
    			if(bridgeEast == null)
    			{
    				MethodProvider.log("Broken bridge is null!");
    				return;
    			}
    			if(Inventory.get(id.plank).useOn(bridgeEast))
    			{
    				MethodProvider.sleepUntil(() -> Locations.horror_bridgeEast.contains(p.l),
    						() -> p.l.isMoving(),
    						Sleep.calculate(3333, 2222),69);
    			}
    			return;
    		}
    		if(Walkz.walkToArea(Locations.horror_bridgeWest))
    		{
    			GameObject bridgeWest = GameObjects.closest(g -> g!=null && 
    					g.getName().equals("Broken bridge") && 
    					g.getTile().equals(Locations.horror_bridgeWestTile));
    			if(bridgeWest == null)
    			{
    				MethodProvider.log("Broken bridge is null!");
    				return;
    			}
    			if(bridgeWest.interact("Cross"))
    			{
    				MethodProvider.sleepUntil(() -> Locations.horror_bridgeEast.contains(p.l),
    						() -> p.l.isMoving(),
    						Sleep.calculate(3333, 2222),69);
    			}
    			return;
    		}
    		return;
    	}
    	if(!gotKey())
    	{
    		if(Locations.barbAgilityCourseGroundLvl.contains(p.l))
    		{
    			API.walkTalkToNPC("Gunnjorn", "Talk-to", Locations.barbAgilityCourseGroundLvl);
    			return;
    		}
    		if(Locations.horror_lighthouseAndBridgeArea.contains(p.l))
    		{
    			if(Walkz.useJewelry(InvEquip.games, "Barbarian Outpost")) Sleep.sleep(420, 696);
    			return;
    		}
    		//deductively, have to be located somewhere outside agility course, in barb outpost - walk
    		if(Locations.horror_entireLightHouseBarbarian.contains(p.l))
    		{
    			if(Walkz.walkToArea(Locations.horror_barbAgilityCoursePipeEntrance))
    			{
    				if(Skills.getBoostedLevels(Skill.AGILITY) < 35)
    				{
    					if(Tabz.open(Tab.INVENTORY))
    					{
    						if(Inventory.contains(id.summerPie1_2))
    						{
    							if(Inventory.interact(id.summerPie1_2, "Eat"))
    							{
    								MethodProvider.sleepUntil(() -> Skills.getBoostedLevels(Skill.AGILITY) >= 35, Sleep.calculate(2222,2222));
    							}
    							return;
    						}
    						if(Inventory.contains(id.summerPie))
    						{
    							if(Inventory.interact(id.summerPie, "Eat"))
    							{
    								MethodProvider.sleepUntil(() -> Skills.getBoostedLevels(Skill.AGILITY) >= 35, Sleep.calculate(2222,2222));
    							}
    							return;
    						}
    						fulfillStep1AfterPlanks();
    					}
						return;
    				}
    				//OK to pass pipe
    				API.interactWithGameObject("Obstacle pipe", "Squeeze-through");
    			}
    			return;
    		}
    	}
    	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l))
    	{
    		if(Walkz.walkToArea(Locations.horror_larrissaLighthouse))
    		{
    			API.interactWithGameObject("Doorway","Walk-through",() -> !Inventory.contains(id.lighthouseKey));
        		return;
    		}
    		return;
    	}
    	walkToLighthouseIsland();
    }
    public static void talkToLarrissa()
    {
    	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l))
    	{
    		API.walkTalkToNPC("Larrissa", "Talk-to", Locations.horror_larrissaLighthouse);
    		return;
    	}
    	walkToLighthouseIsland();
    }
    public static void walkToLighthouseIsland()
    {
    	if(Locations.barbAgilityCourseGroundLvl.contains(p.l))
    	{
    		if(!Walkz.useJewelry(InvEquip.games, "Barbarian Outpost"))
    		{
    			Walkz.walkToArea(Locations.horror_stonesSouth);
    		}
    		return;
    	}
    	//handle agility stones
    	if(Locations.horror_totalStonesArea.contains(p.l))
    	{
    		if(Locations.horror_stones1.contains(p.l))
    		{
    			API.interactWithGameObject("Basalt rock", "Jump-across", Locations.horror_stone2, () -> Locations.horror_stones2.contains(p.l) || Locations.horror_stonesSouth.contains(p.l));
    			return;
    		}
    		if(Locations.horror_stones2.contains(p.l))
    		{
    			API.interactWithGameObject("Basalt rock", "Jump-across", Locations.horror_stone3, () -> Locations.horror_stones3.contains(p.l));
    			return;
    		}
    		if(Locations.horror_stones3.contains(p.l))
    		{
    			API.interactWithGameObject("Basalt rock", "Jump-across", Locations.horror_stone4, () -> Locations.horror_stones4.contains(p.l));
    			return;
    		}
    		if(Locations.horror_stones4.contains(p.l))
    		{
    			API.interactWithGameObject("Basalt rock", "Jump-across", Locations.horror_stone5, () -> Locations.horror_stonesNorth.contains(p.l));
    			return;
    		}
    	}
    	//ready to click "Jump-to" "Beach" or "Jump-across" "Basalt rock"
    	if(Locations.horror_stonesSouth.contains(p.l))
    	{
    		if(Calculations.nextGaussianRandom(50, 25) >= 35)
    		{
    			API.interactWithGameObject("Basalt rock", "Jump-across", Locations.horror_stone1, () -> Locations.horror_stones1.contains(p.l));
    			return;
    		}
    		API.interactWithGameObject("Beach", "Jump-to", Locations.horror_stonesSouth, () -> Locations.horror_stones1.contains(p.l));
			return;
    	}
    	if(Locations.horror_entireLightHouseBarbarian.contains(p.l))
    	{
    		Walkz.walkToArea(Locations.horror_stonesSouth);
    		return;
    	}
    	Walkz.useJewelry(InvEquip.games, "Barbarian Outpost");
    }
    public static boolean shouldFulfillStep0()
    {
    	return !Equipment.contains(id.staffOfFire) || 
    			(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)) || 
    			Inventory.count(id.steelArrow) <= 0 || 
    			Inventory.count(id.moltenGlass) <= 0 || 
    			Inventory.count(id.tinderbox) <= 0 || 
    			Inventory.count(id.swampTar) <= 0 || 
    			Inventory.count(id.hammer) <= 0 || 
    			Inventory.count(id.bronzeSword) <= 0;
    }
    
    
    public static void fulfillStep0()
    {
    	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l) || 
				Locations.horror_totalStonesArea.contains(p.l))
		{
			if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") && 
					!Walkz.useJewelry(InvEquip.games, "Barbarian Outpost") && 
					!Walkz.useJewelry(InvEquip.glory, "Edgeville"))
			{
				MethodProvider.log("Shit, out of teleports in Horror from the deep lighthouse area!");
			}
			return;
		}
    	if(!InvEquip.checkedBank()) return;
    	if(TrainRanged.outOfAvas())
    	{
    		TrainRanged.buyMoreAvas();
    		return;
    	}
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.CAPE, TrainRanged.getBestCapeSlot());
    	InvEquip.setEquipItem(EquipmentSlot.CHEST, TrainMagic.getBestBodySlot());
		InvEquip.setEquipItem(EquipmentSlot.LEGS, TrainMagic.getBestLegsSlot());
		InvEquip.setEquipItem(EquipmentSlot.HAT,TrainMagic.getBestHatSlot());
		InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.WEAPON, id.staffOfFire);
		InvEquip.setEquipItem(EquipmentSlot.FEET, id.blueBoots);
    	InvEquip.addInvyItem(id.steelArrow, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.tinderbox, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.swampTar, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.moltenGlass, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.plank, 2, 2, false, 2);
    	InvEquip.addInvyItem(id.hammer, 1, 1, false, 1);
    	InvEquip.addInvyItem(InvEquip.games8, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.steelNails, 60, 60, false, 60);
    	InvEquip.addInvyItem(id.hammer, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.bronzeSword, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.stamina4, 1, 1, false, 1);
    	InvEquip.addInvyItem(InvEquip.coins, 5000, 5000, false, 0);
    	InvEquip.addInvyItem(id.prayPot4, 1, 1, false, 1);
    	if(Skills.getRealLevel(Skill.RANGED) >= 61) InvEquip.addInvyItem(id.runeCrossbow, 1, 1, false, 1); 
    	else InvEquip.addInvyItem(id.adamantCrossbow, 1, 1, false, 1);
    	int boltsQty = (int) Calculations.nextGaussianRandom(90, 20);
    	if(TrainRanged.getBestCapeSlot() == id.avasAccumulator) boltsQty = (int) Calculations.nextGaussianRandom(40, 15);
    	InvEquip.addInvyItem(id.diamondBoltsE, 15, boltsQty, false, (int) Calculations.nextGaussianRandom(130, 20));
    	InvEquip.addInvyItem(id.prayPot4, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.summerPie, 4, 4, false, 4);
    	InvEquip.addInvyItem(id.waterRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.earthRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.fireRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.airRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	if(Skills.getRealLevel(Skill.MAGIC) < 62) InvEquip.addInvyItem(id.deathRune, 200, (int) Calculations.nextGaussianRandom(250, 25), false, (int) Calculations.nextGaussianRandom(350, 25));
    	else InvEquip.addInvyItem(id.bloodRune, 150, (int) Calculations.nextGaussianRandom(185, 15), false, (int) Calculations.nextGaussianRandom(350, 25));
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 1, 15, false, (int) Calculations.nextGaussianRandom(50, 20));
    	if(InvEquip.fulfillSetup(true, 240000))
    	{
    		MethodProvider.log("[HORROR FROM THE DEEP] -> Fulfilled step0 correctly!");
    	}
    	else MethodProvider.log("[HORROR FROM THE DEEP] -> NOT fulfilled step0 correctly!");
    }
    public static void fulfillStep1AfterPlanks()
    {
    	if(Locations.horror_lighthouseAndBridgeArea.contains(p.l) || 
				Locations.horror_totalStonesArea.contains(p.l))
		{
			if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") && 
					!Walkz.useJewelry(InvEquip.games, "Barbarian Outpost") && 
					!Walkz.useJewelry(InvEquip.glory, "Edgeville"))
			{
				MethodProvider.log("Shit, out of teleports in Horror from the deep lighthouse area!");
			}
			return;
		}
    	if(!InvEquip.checkedBank()) return;
    	if(TrainRanged.outOfAvas())
    	{
    		TrainRanged.buyMoreAvas();
    		return;
    	}
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.CAPE, TrainRanged.getBestCapeSlot());
    	InvEquip.setEquipItem(EquipmentSlot.CHEST, TrainMagic.getBestBodySlot());
		InvEquip.setEquipItem(EquipmentSlot.LEGS, TrainMagic.getBestLegsSlot());
		InvEquip.setEquipItem(EquipmentSlot.HAT,TrainMagic.getBestHatSlot());
		InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.WEAPON, id.staffOfFire);
		InvEquip.setEquipItem(EquipmentSlot.FEET, id.blueBoots);
    	InvEquip.addInvyItem(id.steelArrow, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.tinderbox, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.swampTar, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.moltenGlass, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.hammer, 1, 1, false, 1);
    	InvEquip.addInvyItem(InvEquip.games8, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.steelNails, 60, 60, false, 60);
    	InvEquip.addInvyItem(id.hammer, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.bronzeSword, 1, 1, false, 1);
    	InvEquip.addInvyItem(InvEquip.coins, 5000, 5000, false, 0);
    	InvEquip.addInvyItem(id.stamina4, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.prayPot4, 1, 1, false, 1);
    	if(Skills.getRealLevel(Skill.RANGED) >= 61) InvEquip.addInvyItem(id.runeCrossbow, 1, 1, false, 1); 
    	else InvEquip.addInvyItem(id.adamantCrossbow, 1, 1, false, 1);
    	int boltsQty = (int) Calculations.nextGaussianRandom(90, 20);
    	if(TrainRanged.getBestCapeSlot() == id.avasAccumulator) boltsQty = (int) Calculations.nextGaussianRandom(40, 15);
    	InvEquip.addInvyItem(id.diamondBoltsE, 15, boltsQty, false, (int) Calculations.nextGaussianRandom(130, 20));
    	InvEquip.addInvyItem(id.prayPot4, 1, 1, false, 1);
    	InvEquip.addInvyItem(id.summerPie, 4, 4, false, 4);
    	InvEquip.addInvyItem(id.waterRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.earthRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.fireRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	InvEquip.addInvyItem(id.airRune, 500, (int) Calculations.nextGaussianRandom(700, 50), false, (int) Calculations.nextGaussianRandom(2000, 100));
    	if(Skills.getRealLevel(Skill.MAGIC) >= 62) InvEquip.addInvyItem(id.bloodRune, 150, (int) Calculations.nextGaussianRandom(185, 15), false, (int) Calculations.nextGaussianRandom(350, 25));
    	InvEquip.addInvyItem(id.deathRune, 100, (int) Calculations.nextGaussianRandom(250, 25), false, (int) Calculations.nextGaussianRandom(350, 25));
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 1, 15, false, (int) Calculations.nextGaussianRandom(50, 20));
    	if(InvEquip.fulfillSetup(true, 240000))
    	{
    		MethodProvider.log("[HORROR FROM THE DEEP] -> Fulfilled step0 correctly!");
    	}
    	else MethodProvider.log("[HORROR FROM THE DEEP] -> NOT fulfilled step0 correctly!");
    }
}
