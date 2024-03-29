package script.quest.animalmagnetism;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankType;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.quest.restlessghost.RestlessGhost;
import script.utilities.API;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Questz;
import script.utilities.Sleepz;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;

/**
 * Completes Animal Magnetism
 * @author Dreambotter420
 * ^_^
 */
public class AnimalMagnetism extends Leaf {
	@Override
	public boolean isValid() {
		return API.mode == API.modes.ANIMAL_MAGNETISM;
	}
	public static enum Lover {
		ALICE,
		HUSBAND
	}
	public static Lover talkTo = null;
	public static Tile standTile = new Tile(2971,3234,0);
	public static Tile standTileSouth = new Tile(2971,3233,0);
	public static boolean needTalkToTurael = false;
	public static boolean completed()
	{
		return getProgressValue() == 240;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            Logger.log("[TIMEOUT] -> Animal Magnetism");
           	API.mode = null;
            return Sleepz.sleepTiming();
        }
        if(completed())
        {
        	if(Questz.checkCloseQuestCompletion()) return Sleepz.calculate(420, 1111);
        	Logger.log("[COMPLETED] -> Animal Magnetism!");
           	API.mode = null;
            return Sleepz.sleepTiming();
        }
        if(coinQty == 0) coinQty = API.roundToMultiple((int) Calculations.nextGaussianRandom(10000, 2000),500);
        if(tabQty1 == 0) tabQty1 = (int) Calculations.nextGaussianRandom(10, 2);
        if(tabQty2 == 0) tabQty2 = (int) Calculations.nextGaussianRandom(6, 2);
        
        if(Dialoguez.handleDialogues()) return Sleepz.calculate(420, 696);
        switch(getProgressValue())
        {
        case(230):
        {
        	walkTalkToAva();
        	break;
        }
        case(220):
        {
        	int i = 0;
        	int j = 0;
        	int rand = Calculations.random(0, 100);
        	int rand2 = Calculations.random(0,100);
        	if(rand < 33) i = id.aPattern;
        	else if(rand < 66) i = id.hardLeather;
        	else  i = id.polishedButtons;
        	if(i == id.aPattern)
        	{
        		if(rand2 < 50) j = id.polishedButtons;
        		else j = id.hardLeather;
        	}
        	else if(i == id.hardLeather)
        	{
        		if(rand2 < 50) j = id.polishedButtons;
        		else j = id.aPattern;
        	} 
        	else 
        	{
        		if(rand2 < 50) j = id.hardLeather;
        		else j = id.aPattern;
        	}
        	Item item1 = Inventory.get(i);
        	Item item2 = Inventory.get(j);
        	if(item1.useOn(item2))
        	{
        		Sleep.sleepUntil(() -> getProgressValue() == 230, Sleepz.calculate(3333, 3333));
        	}
        	break;
        }
        case(210):
        {
        	if(Widgets.get(480,26) != null && 
					Widgets.get(480,26).isVisible()) {
				Keyboard.closeInterfaceWithESC();
        		break;
        	}
        	walkTalkToAva();
    		break;
        }
        case(200):
        {
        	if(Widgets.get(480,26) != null && 
        					Widgets.get(480,26).isVisible())
        	{
        		Sleepz.sleep(111, 2222);
        		int[] childIDs = {24,25,30,33,35,39,42,45,47};
        		boolean foundUnsolvedPiece = false;
        		for(int i : childIDs)
        		{
        			WidgetChild button = Widgets.get(480,i);
            		if(button != null && button.isVisible())
            		{
            			foundUnsolvedPiece = true;
            			if(button.interact()) Sleepz.sleep(420, 1111);
            		}
        		}
        		break;
        	}
        	if(Tabz.open(Tab.INVENTORY))
        	{
        		if(Inventory.interact(id.researchNotes, "Translate"))
        		{
        			Sleep.sleepUntil(() -> Widgets.get(480,13) != null && 
        					Widgets.get(480,13).isVisible(), Sleepz.calculate(3333, 3333));
        		}
        		break;
        	}
        	break;
        }
        case(190):
        {
        	walkTalkToAva();
    		break;
        }
        case(180):
        {
        	if(Inventory.contains(id.undeadTwigs))
        	{
        		walkTalkToAva();
        		break;
        	}
        	if(!Locations.draynorUndeadTree.contains(Players.getLocal()))
    		{
    			Walkz.teleport(id.draynorTab, Locations.draynorUndeadTree, 30000);
    			break;
    		}
    		API.interactNPC("Undead tree", "Chop", Locations.draynorUndeadTree, false, () -> Inventory.contains(id.undeadTwigs));
    		break;
        }
        case(170):
    	{
    		walkTalkToTurael();
    		break;
    	}
    	case(160):
    	{
    		if(needTalkToTurael)
    		{
    			walkTalkToTurael();
    			break;
    		}
    		walkTalkToAva();
    		break;
    	}
        case(150):
        {
        	if(!Locations.draynorUndeadTree.contains(Players.getLocal()))
    		{
    			Walkz.teleport(id.draynorTab, Locations.draynorUndeadTree, 30000);
    			break;
    		}
    		API.interactNPC("Undead tree", "Chop", Locations.draynorUndeadTree, false, () -> getProgressValue() == 160);
    		break;
        }
        case(140):
        {
        	if(Inventory.contains(id.barMagnet))
        	{
        		walkTalkToAva();
        		break;
        	}
        	if(standTile.distance() < 75)
        	{
        		Tile loc = Players.getLocal().getTile();
        		if(standTile.equals(loc))
        		{
        			if(Players.getLocal().getOrientation() == 1024)
        			{
        				if(Inventory.get(id.hammer).useOn(id.selectedIron))
        				{
        					Sleep.sleepUntil(() -> Inventory.contains(id.barMagnet), Sleepz.calculate(4444, 4444));
        				}
        				break;
        			}
        			if(Walking.walkExact(standTileSouth))
        			{
        				Sleep.sleepUntil(() -> standTileSouth.equals(Players.getLocal().getTile()), Sleepz.calculate(3333, 2222));
        			}
        			break;
        		}
        		if(standTileSouth.equals(loc))
        		{
        			if(Walking.walkExact(standTile))
        			{
        				Sleep.sleepUntil(() -> standTile.equals(Players.getLocal().getTile()), Sleepz.calculate(3333, 2222));
        			}
        			break;
        		}
        		if(Walking.shouldWalk(6) && Walking.walk(standTileSouth)) Sleepz.sleep(696, 666);
        		break;
        	}
        	Walkz.teleportOutsideHouse(180000);
        	break;
        }
        case(120):case(130):
        {
        	if(Locations.ernest_westWing.contains(Players.getLocal()))
    		{
    			API.walkInteractWithGameObject("Lever","Pull",Locations.ernest_westWing,() -> !Locations.ernest_westWing.contains(Players.getLocal()));
    			break;
    		}
        	API.walkTalkToNPC("Witch", "Talk-to", Locations.draynorMaynorWitch);
        	break;
        }
        case(100):case(110):
        {
        	if(Locations.isInstanced())
        	{
        		Sleepz.sleep(4444, 4444);
        		break;
        	}
        	if(Inventory.count(id.undeadChicken) > 1)
        	{
        		if(Inventory.contains("Iron bar") && !Inventory.contains(id.ironBar))
        		{
        			final int distBarbBank = (int)BankLocation.BARBARIAN_OUTPOST.getCenter().distance();
        			if(distBarbBank <= 50)
        			{
        				if(distBarbBank <= 8)
        				{
        					if(Tabz.open(Tab.INVENTORY))
        					{
        						if(Inventory.get(new Item(id.ironBar,1).getNotedItemID()).useOn(Bank.getClosestBank(BankType.CHEST)))
        						{
        							Sleep.sleepUntil(() -> Inventory.contains(id.ironBar), () -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222),69);
        						}
        					}
        					break;
        				}
        				if(Walking.shouldWalk(6) && Walking.walk(BankLocation.BARBARIAN_OUTPOST)) Sleepz.sleep(696, 666);
        				break;
        			}
        			Walkz.useJewelry(InvEquip.games, "Barbarian Outpost");
        			break;
        		}
        		if(Inventory.count(id.ironBar) >= 5)
        		{
        			walkTalkToAva();
        		}
        		break;
        	}
        	if(Locations.entireMorytania.contains(Players.getLocal()) && 
        			Locations.animalMagnetism_alice.getCenter().distance() < 115)
        	{
        		API.walkTalkToNPC("Alice\'s husband", "Talk-to", Locations.animalMagnetism_alicesHusband);
    			break;
        	}
        	Walkz.teleport(id.fenkensteinTab, Locations.fenkensteinTeleSpot, 30000);
        	break;
        }
        case(90):
        {
        	Sleepz.sleep(4444, 4444);
        	break;
        }
        case(76):case(80):
        {
        	if(Locations.entireMorytania.contains(Players.getLocal()) && 
        			Locations.animalMagnetism_alice.getCenter().distance() < 115)
        	{
        		API.walkTalkToNPC("Alice\'s husband", "Talk-to", Locations.animalMagnetism_alicesHusband);
    			break;
        	}
        	Walkz.teleport(id.fenkensteinTab, Locations.fenkensteinTeleSpot, 30000);
        	break;
        }
        case(70):case(73):
        {
        	if(Locations.entireMorytania.contains(Players.getLocal()) && 
        			Locations.crowsHouse.getCenter().distance() < 125)
        	{
        		API.walkTalkToNPC("Old crone", "Talk-to", Locations.crowsHouse);
        		break;
        	}
        	Walkz.teleport(id.fenkensteinTab, Locations.fenkensteinTeleSpot, 30000);
        	break;
        }
        case(10):case(20):case(30):case(40):case(50):case(60):
        {
        	if(get20Ectotokens())
        	{
        		if(!Locations.entireMorytania.contains(Players.getLocal()))
        		{
        			if(Inventory.contains(id.fenkensteinTab))
        			{
        				Walkz.teleport(id.fenkensteinTab, Locations.fenkensteinTeleSpot, 30000);
        			} else fulfillStep0();
        			break;
        		}
        		if(talkTo == null) talkTo = Lover.ALICE;
        		switch(talkTo)
        		{
        		case ALICE:
        		{
        			API.walkTalkToNPC("Alice", "Talk-to", Locations.animalMagnetism_alice);
        			break;
        		}
        		case HUSBAND:
        		{
        			API.walkTalkToNPC("Alice\'s husband", "Talk-to", Locations.animalMagnetism_alicesHusband);
        			break;
        		}
        		default:
        		{
        			Logger.log("Logging impossibility :D Animal Magnetism lover enum switch default block");
        			break;
        		}
        		}
        		
        		break;
        	}
        	break;
        }
        case(0):
        {

    		if(!RestlessGhost.getGhostspeakAmulet()) return Sleepz.calculate(420,1111);
        	if(!Inventory.contains(id.mithAxe) || 
            		!Inventory.contains(id.holySymbol) || 
            		Inventory.count(id.dBones) < 4 || 
            		!Inventory.contains(id.hammer) || 
            		!Inventory.contains(id.draynorTab) || 
            		!Inventory.contains(id.fenkensteinTab) || 
            		!Inventory.contains(id.polishedButtons) || 
            		!Inventory.contains(id.hardLeather) || 
            		!Inventory.contains(id.houseTele) || 
            		!Inventory.contains(RestlessGhost.ghostspeakAmulet))
            {
            	fulfillStep0();
                break;
            }
        	walkTalkToAva();
        	break;
        }
        default:break;
        }
        
        return Sleepz.calculate(420,1111);
    }
    public static boolean walkTalkToTurael()
	{
		Filter<NPC> turaelFilter = p -> 
			p != null &&
			p.getName().equals("Turael") && 
			p.hasAction("Talk-to");
		NPC turael = NPCs.closest(turaelFilter);
		if(turael != null)
		{
			if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("Animal Magnetism");
			if(turael.canReach())
			{
				if(turael.interact("Talk-to")) 
				{
					Sleep.sleepUntil(Dialogues::inDialogue, () -> Players.getLocal().isMoving(), Sleepz.calculate(2222, 2222),50);
					return true;
				}
			}
			else if(Walking.shouldWalk(6) && Walking.walk(turael)) Sleepz.sleep(420, 696);
		}
		else 
		{
			if(Locations.burthorpeTeleSpot.distance(Players.getLocal().getTile()) > 50)
			{
				if(Walkz.useJewelry(InvEquip.games, "Burthorpe"))
				{
					Sleepz.sleep(420, 696);
					return true;
				}
				
				if(!InvEquip.checkedBank())
				{
					Sleepz.sleep(696, 420);
					return false;
				}
				final int games = InvEquip.getBankItem(InvEquip.wearableGames);
				if(games != 0)
				{
					InvEquip.withdrawOne(games, 180000);
					return false;
				}
				InvEquip.buyItem(InvEquip.games8, 2, 180000);
			} else if(Walking.shouldWalk(6) && Walking.walk(Locations.turaelArea.getCenter())) Sleepz.sleep(420, 696);
		}
		return false;
	}
    public static void walkTalkToAva()
    {
    	if(Locations.ernest_westWing.contains(Players.getLocal()))
    	{
    		API.talkToNPC("Ava");
    		return;
    	}
    	if(Locations.ernest_westWing.getCenter().distance() > 75) 
    	{
    		Walkz.teleport(id.draynorTab, Locations.draynorMaynorTeleSpot, 180000);
    		return;
    	}
		API.walkInteractWithGameObject("Bookcase", "Search", Locations.ernest_westWingAnd, () -> Locations.ernest_westWing.contains(Players.getLocal()));
    	
    }
    public static boolean worshipped = false;
    public static boolean get20Ectotokens()
    {
    	final int pots = Inventory.count(id.pot);
    	final int slimes = Inventory.count(id.bucketOfSlime);
    	if(Inventory.count(id.ectotokens) >= 20) 
    	{
    		if(Inventory.contains(id.pot) || Inventory.contains(id.bucket) || Inventory.contains(id.bucketOfSlime))
    		{
    			Inventory.dropAll(id.pot,id.bucket,id.bucketOfSlime);
    			return false;
    		}
    		return true;
    	}
    		
    	if(Locations.ectofunctus_2ndFloor.contains(Players.getLocal()))
    	{
    		if(Inventory.contains(id.dBones) && Inventory.contains(id.pot))
    		{
    			if(Inventory.get(id.dBones).useOn(GameObjects.closest("Loader")))
    			{
    				Sleep.sleepUntil(() -> Inventory.count(id.potOfDBones) >= 4, 
    					() -> Players.getLocal().isAnimating() || Players.getLocal().isMoving(),
    					Sleepz.calculate(5555, 5555),69);
    			}
    			return false;
    		}
    		API.interactWithGameObject("Staircase", "Climb-down", () -> Locations.ectofunctus_ground.contains(Players.getLocal()));
    		return false;
    	}
    	if(Locations.ectofunctus_ground.contains(Players.getLocal()))
    	{
    		if(Inventory.contains(id.potOfDBones))
    		{
    			final int prayXP = Skills.getExperience(Skill.PRAYER);
    			API.interactWithGameObject("Ectofuntus", "Worship");
    			Sleep.sleepUntil(() -> Skills.getExperience(Skill.PRAYER) > prayXP,
    					() -> Players.getLocal().isMoving(),
    					Sleepz.calculate(3333, 3333),69);
    			worshipped = true;
    			return false;
    		}
    		if(worshipped)
    		{
    			if(!Equipment.contains(RestlessGhost.ghostspeakAmulet))
    			{
    				InvEquip.equipItem(RestlessGhost.ghostspeakAmulet);
    				return false;
    			}
    			API.talkToNPC("Ghost disciple");
    			worshipped = false;
    			return false;
    		}
    		
    		API.interactWithGameObject("Staircase", "Climb-up", () -> Locations.ectofunctus_2ndFloor.contains(Players.getLocal()));
    		return false;
    	}
    	if(Locations.phasmatys_southOfNorthGateArea.contains(Players.getLocal()))
    	{
    		if(Inventory.count(id.pot) == 4 && 
    				Inventory.count(id.bucketOfSlime) == 4)
    		{
    			API.walkInteractWithGameObject("Energy Barrier", "Pass", Locations.phasmatysNorthGates, () -> Locations.ectofunctus_ground.contains(Players.getLocal()));
    		}
    		return false;
    	}
    	if(Locations.portPhasmatysRough.contains(Players.getLocal()))
    	{
    		if(pots < 4 || slimes < 4)
    		{
    			int purchaseItem = 0;
    			if(pots < 4) purchaseItem = id.pot;
    			else purchaseItem = id.bucketOfSlime;
    			final int count = Inventory.count(purchaseItem);
    			final int id = purchaseItem;
    			if(Shop.isOpen())
    			{
    				if(Shop.count(purchaseItem) > 0)
    				{
    					if(Shop.purchase(purchaseItem, 1))
    					{
    						Sleep.sleepUntil(() -> Inventory.count(id) > count, Sleepz.calculate(3333, 2222));
    					}
    				}
					return false;
    			}
    			API.walkInteractNPC("Trader Crewmember", "Trade", Locations.portPhasmatysCharterPort, () -> Shop.isOpen());
    			return false;
    		}
    		if(pots > 4 && Inventory.drop(id.pot)) Sleepz.sleep(696, 1111);
    		if(slimes > 4 && Inventory.drop(id.bucketOfSlime)) Sleepz.sleep(696, 1111);
    		
    		if(Inventory.count(id.pot) == 4 && 
    				Inventory.count(id.bucketOfSlime) == 4)
    		{
    			API.walkInteractWithGameObject("Energy Barrier", "Pass", Locations.phasmatysNorthGates, () -> Locations.ectofunctus_ground.contains(Players.getLocal()));
    		}
    		return false;
    	}
    	if(Locations.portPhasmatysCharterShip.contains(Players.getLocal()))
    	{
    		API.interactWithGameObject("Gangplank", "Cross", () -> Locations.portPhasmatysRough.contains(Players.getLocal()));
    		return false;
    	}
    	if(Locations.karamjaF2P.contains(Players.getLocal()))
    	{
    		WidgetChild charterMapPhasmatysButton =  Widgets.get(72, 7);
    		if(charterMapPhasmatysButton != null && charterMapPhasmatysButton.isVisible())
    		{
    			if(charterMapPhasmatysButton.interact())
    			{
    				Sleep.sleepUntil(() -> Dialogues.areOptionsAvailable(), Sleepz.calculate(3333, 3333));
    			}
    			return false;
    		}
    		API.walkInteractNPC("Trader Crewmember", "Charter", Locations.karamjaCharterPort, () -> Widgets.get(72, 7) != null && Widgets.get(72, 7).isVisible());
    		return false;
    	}
    	if(!InvEquip.invyContains(InvEquip.wearableGlory) && !InvEquip.equipmentContains(InvEquip.wearableGlory))
    	{
    		fulfillStep0();
    		return false;
    	}
    	Walkz.useJewelry(InvEquip.glory, "Karamja");
    	return false;
    }
    public static int tabQty1 = 0;
    public static int tabQty2 = 0;
    public static int coinQty = 0;
    public static void setStep0()
    {
    	InvEquip.clearAll();
		InvEquip.addInvyItem(RestlessGhost.ghostspeakAmulet, 1, 1, false, 0);
		InvEquip.addInvyItem(id.stamina4, 2, 2, false, tabQty1);
		InvEquip.addInvyItem(id.mithAxe, 1, 1, false, 1);
		InvEquip.addInvyItem(id.holySymbol, 1, 1, false, 1);
		InvEquip.addInvyItem(id.ironBar, 5, 5, true, 5);
		InvEquip.addInvyItem(id.hardLeather, 1, 1, false, 1);
		InvEquip.addInvyItem(id.hammer, 1, 1, false, 1);
		InvEquip.addInvyItem(id.polishedButtons, 1, 1, false, 1);
		InvEquip.addInvyItem(id.dBones, 4, 4, false, 4);
		InvEquip.addInvyItem(InvEquip.coins,5000, coinQty, false, 0);
		InvEquip.addInvyItem(id.draynorTab, 5, tabQty1, false, tabQty1);
		InvEquip.addInvyItem(id.fenkensteinTab, 2, tabQty2, false, tabQty2);
		InvEquip.addInvyItem(InvEquip.games8, 1, 1, false, 1);
		InvEquip.addInvyItem(id.houseTele, 1, 1, false, tabQty2);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
		InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
		InvEquip.shuffleFulfillOrder();
    }
    
    public static void fulfillStep0()
	{
    	setStep0();
		if(InvEquip.fulfillSetup(true, 240000))
		{
			Logger.log("[NATURE SPIRIT] -> Fulfilled step 0 correctly!");
		} else Logger.log("[NATURE SPIRIT] -> NOT Fulfilled step 0 correctly! :-(");
	}
	public static int getProgressValue()
    {
    	return PlayerSettings.getBitValue(3185);
    }
}
