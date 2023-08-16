package script.quest.naturespirit;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.quest.restlessghost.RestlessGhost;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Questz;
import script.utilities.Sleepz;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Completes Nature Spirit
 * @author Dreambotter420
 * ^_^
 */
public class NatureSpirit extends Leaf {
	@Override
	public boolean isValid() {
		return API.mode == API.modes.NATURE_SPIRIT;
	}
	private static final int westStone = 3527;
	private static final int eastStone = 3529;
	public static boolean placedFungus = false;
	public static boolean placedScroll = false;
	public static boolean completed()
	{
		if(getProgressValue() == 110)
		{
			Questz.checkCloseQuestCompletion();
			if(Locations.natureSpirit_insideGrottoFinished.contains(Players.getLocal()))
			{
				if(!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
				{
					Walkz.useJewelry(InvEquip.glory, "Edgeville");
				}
				return false;
			}
			if(Inventory.contains(id.rottenFood,id.druidPouch,id.druidPouchFilled,id.pieDish,
					id.applePie,id.meatPie,id.natureSpirit_washingBowl,
					id.natureSpirit_mirror,id.natureSpirit_usedSpell,
					id.natureSpirit_spell,id.vial))
			{
				Inventory.dropAll(id.rottenFood,id.druidPouch,id.druidPouchFilled,id.pieDish,
						id.applePie,id.meatPie,id.natureSpirit_washingBowl,
						id.natureSpirit_mirror,id.natureSpirit_usedSpell,
						id.natureSpirit_spell,id.vial);
				return false;
			}
			return true;
		}
		return false;
	}
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            Logger.log("[TIMEOUT] -> Nature Spirit");
            API.mode = null;
            return Sleepz.sleepTiming();
        }
        final int bestDart = TrainRanged.getBestDart();
        if(completed())
        {
        	Logger.log("[COMPLETED] -> Nature Spirit!");
            API.mode = null;
            return Sleepz.sleepTiming();
        }
        if(Inventory.isFull())
        {
        	if(Tabz.open(Tab.INVENTORY))
        	{
        		Filter<Item> filter = i -> i!=null && 
        				(i.getID() == id.rottenFood || 
        				i.getID() == id.vial || 
        				i.getID() == id.meatPie || 
        				i.getID() == id.meatPie1_2 || 
                		i.getID() == id.applePie || 
                		i.getID() == id.applePie1_2 || 
        				i.getID() == id.pieDish);
        		Inventory.dropAll(filter);
        		Sleepz.sleep(420, 696);
        	}
        }
        if(Dialoguez.handleDialogues()) return Sleepz.calculate(420, 696);
        switch(getProgressValue())
        {
        case(105):
        {
        	walkTalkNatureSpirit();
        	break;
        }
        case(75):case(85):case(80):case(95):case(90):case(100):
        {
        	killGhasts();
        	break;
        }
        case(70):
        {
        	walkTalkNatureSpirit();
        	break;
        }
        case(60):case(65):
        {
        	walkTalkGrotto();
        	break;
        }
        case(55):
        {
        	solvePuzzle();
        	break;
        }
        case(50):
        {
        	goTalkToFilliman();
        	break;
        }
        case(40):case(45):
        {
        	harvestFirstFungus();
        	break;
        }
        case(35):
        {
        	walkTalkToDrezel();
        	break;
        }
        case(30):
        {
        	goTalkToFilliman();
        	break;
        }
        case(25):
        {
        	fillimansDiary();
        	break;
        }
        case(20):
        {
        	takeBowlMirror();
        	break;
        }
        case(10):case(15):
        {
        	goTalkToFilliman();
        	break;
        }
        case(1):case(2):case(3):case(4):case(5):
        {
        	goTalkToFilliman();
        	break;
        }
        case(0):
        {
            if(!InvEquip.checkedBank()) return Sleepz.calculate(420, 696);
        	if(!RestlessGhost.getGhostspeakAmulet()) break;
        	if(Inventory.contains(bestDart))
        	{
        		InvEquip.equipItem(bestDart);
        		break;
        	}
        	if(!InvEquip.invyContains(id.prayPots) || 
        			!InvEquip.invyContains(id.rangedPots) || 
        			!Equipment.contains(bestDart) || 
        			!Inventory.contains(id.silverSickle) || 
        			!Inventory.contains(Combatz.lowFood) || 
        			!Inventory.contains(id.salveGraveyardTab) || 
    				(!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth))  || 
    				(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)))
        	{
        		fulfillStep0();
        		break;
        	}
        	walkTalkToDrezel();
        	break;
        }
        default:break;
        }
        return Sleepz.calculate(420,696);
    }
    public static void walkTalkGrotto()
    {
    	if(Locations.natureSpirit_insideGrotto.contains(Players.getLocal()))
    	{
    		API.interactWithGameObject("Grotto", "Search");
    		return;
    	}
    	walkToNatureSpirit();
    }
    public static void walkTalkNatureSpirit()
    {
    	if(Locations.natureSpirit_insideGrotto.contains(Players.getLocal()))
    	{
    		NPC spirit = NPCs.closest("Nature Spirit");
    		if(spirit != null)	API.talkToNPC("Nature Spirit");
    		else API.interactWithGameObject("Grotto", "Search");
    		return;
    	}
    	if(!Equipment.contains(RestlessGhost.ghostspeakAmulet))
		{
			if(Inventory.contains(RestlessGhost.ghostspeakAmulet))
			{
				InvEquip.equipItem(RestlessGhost.ghostspeakAmulet);
			}
			return;
		}
		walkToNatureSpirit();
		return;
    }
    public static void walkToNatureSpirit()
    {
		if(Locations.natureSpiritGrotto.contains(Players.getLocal()))
		{
			API.interactWithGameObject("Grotto", "Enter", () -> Locations.natureSpirit_insideGrotto.contains(Players.getLocal()));
			return;
		}
		walkToGrotto();
		return;
    }
    public static boolean leaveUndergroundPass()
    {
    	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
    	{
    		if(Inventory.contains(id.salveGraveyardTab))
    		{
    			Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 30000);
    		}
    		else API.interactWithGameObject("Holy barrier", "Pass-through", () -> !Locations.PiP_undergroundPass.contains(Players.getLocal()));
    	}
    	return !Locations.PiP_undergroundPass.contains(Players.getLocal());
    }
    public static void walkToGrotto()
    {
    	if(!leaveUndergroundPass()) return;
    	if(Locations.entireMorytania.contains(Players.getLocal()))
    	{
    		if(Locations.natureSpiritGrottoBridgeSouth.contains(Players.getLocal()))
    		{
    			API.interactWithGameObject("Bridge", "Jump", () -> Locations.natureSpiritGrotto.contains(Players.getLocal()));
    			return;
    		}
        	WidgetChild enterSwampButton = Widgets.get(580, 17);
    		if(enterSwampButton != null && enterSwampButton.isVisible())
    		{
    			if(enterSwampButton.interact("Yes"))
    			{
    				Sleep.sleepUntil(() -> Locations.morytaniaSwampGateSouth.contains(Players.getLocal()), Sleepz.calculate(4444, 4444));
    			}
    			return;
    		}
    		if(Locations.morytaniaSwampGateNorth.contains(Players.getLocal()) || 
    				Locations.salveGraveyard.contains(Players.getLocal()))
    		{
    			Logger.log("Clicking gate");
    			API.interactWithGameObject("Gate", "Open",() -> Widgets.get(580, 17) != null && Widgets.get(580, 17).isVisible());
    			return;
    		}
    		Walkz.walkPath(Paths.drezelToNatureGrotto);
    		return;
    	}
    	if(Tabz.open(Tab.INVENTORY))
    	{
    		if(Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 30000))
    		{
    			Logger.log("Teleported to salve graveyard to get into morytania!");
    		}
    	}
    }
    public static void walkTalkToDrezel()
    {
    	if(Locations.PiP_undergroundPass.contains(Players.getLocal())) //whole underground area
    	{
    		if(Locations.PiP_undergroundPassDrezel.contains(Players.getLocal()))
    		{
    			API.talkToNPC("Drezel");
    			return;
    		}
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassDrezel.getCenter())) Sleepz.sleep(696, 666);
    		return;
    	}
    	if(Locations.natureSpirit_trapdoor.contains(Players.getLocal()))
    	{
    		Filter<GameObject> filter = g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down");
    		API.walkInteractWithGameObject("Trapdoor", "Open", Locations.natureSpirit_trapdoor, () -> GameObjects.closest(filter) != null);
        	Sleepz.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.natureSpirit_trapdoor, () -> Locations.PiP_undergroundPass.contains(Players.getLocal()));
        	Sleepz.sleep(696, 666);
        	return;
    	}
    	if(Locations.natureSpirit_trapdoor.getCenter().distance() < 50)
    	{
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.natureSpirit_trapdoor.getCenter())) Sleepz.sleep(696, 666);
    		return;
    	}
    	if(Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 180000)) Sleepz.sleep(696, 666);
    	
    }
    public static void fillimansDiary()
    {
    	if(!Locations.natureSpiritGrotto.contains(Players.getLocal())) 
    	{
    		goTalkToFilliman();
    		return;
    	}
    	if(Inventory.contains(id.natureSpirit_fillimansDiary))
    	{
    		NPC filliman = NPCs.closest(n -> n!=null && 
					n.getName().equals("Filliman Tarlock") && 
					n.hasAction("Talk-to"));
			if(filliman != null)
			{
				if(Inventory.get(id.natureSpirit_fillimansDiary).useOn(filliman))
				{
					Sleep.sleepUntil(() -> Dialogues.inDialogue(),
							() -> Players.getLocal().isMoving(),
							Sleepz.calculate(3333,3333),69);
				}
				return;
			}
			API.interactWithGameObject("Grotto", "Enter");
    		return;
    	}
    	API.interactWithGameObject("Grotto tree", "Search", () -> Inventory.contains(id.natureSpirit_fillimansDiary));
    }
    public static void solvePuzzle()
    {
    	if(Locations.natureSpiritGrotto.contains(Players.getLocal()))
		{
			//solve puzzle part
			if(placedFungus || !Inventory.contains(id.mortFungus))
			{
				if(placedScroll || !Inventory.contains(id.natureSpirit_usedSpell))
				{
					if(!Equipment.contains(RestlessGhost.ghostspeakAmulet))
	    			{
	    				if(Inventory.contains(RestlessGhost.ghostspeakAmulet))
	    				{
	    					InvEquip.equipItem(RestlessGhost.ghostspeakAmulet);
	    				}
	    				return;
	    			}
	    			NPC filliman = NPCs.closest(n -> n!=null && 
	    					n.getName().equals("Filliman Tarlock") && 
	    					n.hasAction("Talk-to"));
	    			if(filliman != null)
	    			{
	    				if(Locations.natureSpirit_finalPuzzleTile.contains(Players.getLocal()))
	    				{
	    					if(filliman.interact("Talk-to")) 
		    				{
		    					Sleep.sleepUntil(() -> Dialogues.inDialogue(), Sleepz.calculate(4444, 4444));
		    				}
	    					return;
	    				}
	    				if(Walking.shouldWalk(6) && Walking.walk(Locations.natureSpirit_finalPuzzleTile.getCenter())) Sleepz.sleep(696, 666);
	    				return;
	    			}
	    			API.interactWithGameObject("Grotto", "Enter");
	    			return;
				}
				if(Inventory.get(id.natureSpirit_usedSpell).useOn(GameObjects.closest(g->g!=null && 
						g.getName().equals("Stone") && 
						g.getID() == eastStone))) Sleep.sleepUntil(() -> placedScroll,Sleepz.calculate(4444,4444));
				return;
			}
			if(Inventory.get(id.mortFungus).useOn(GameObjects.closest(g->g!=null && 
					g.getName().equals("Stone") && 
					g.getID() == westStone))) Sleep.sleepUntil(() -> placedFungus,Sleepz.calculate(4444,4444));
			return;
		}
    	walkToGrotto();
    }
    
    public static void takeBowlMirror()
    {
    	
    	if(!Locations.natureSpiritGrotto.contains(Players.getLocal())) 
    	{
    		goTalkToFilliman();
    		return;
    	}
    	if(Inventory.contains(id.natureSpirit_washingBowl) && 
    	    			Inventory.contains(id.natureSpirit_mirror))
    	{
    		NPC filliman = NPCs.closest(n -> n!=null && 
					n.getName().equals("Filliman Tarlock") && 
					n.hasAction("Talk-to"));
			if(filliman != null)
			{
				if(Inventory.get(id.natureSpirit_mirror).useOn(filliman))
				{
					Sleep.sleepUntil(() -> Dialogues.inDialogue(),
							() -> Players.getLocal().isMoving(),
							Sleepz.calculate(3333,3333),69);
				}
				return;
			}
			API.interactWithGameObject("Grotto", "Enter");
    		return;
    	}
    	GameObject mirror = GameObjects.closest(g -> g!=null && 
    			g.getID() == id.natureSpirit_mirror && 
    			Locations.natureSpiritGrotto.contains(g));
    	if(!Inventory.contains(id.natureSpirit_washingBowl))
    	{
    		API.walkPickupGroundItem(id.natureSpirit_washingBowl, "Take", false, Locations.natureSpiritGrotto);
			return;
    	}
    	if(!Inventory.contains(id.natureSpirit_mirror))
    	{
    		if(mirror == null)
    		{
    			API.walkPickupGroundItem(id.natureSpirit_mirror, "Take", false, Locations.natureSpiritGrotto);
    			return;
    		}
    		API.walkPickupGroundItem(id.natureSpirit_mirror, "Take", false, Locations.natureSpiritGrotto);
			return;
    	}
    }
    public static void killGhasts()
    {
    	
    	
    	if(Locations.entireMorytania.contains(Players.getLocal()))
    	{
    		//1. check combat
    		if(Players.getLocal().isInCombat())
    		{
    			if(Combatz.shouldEatFood(10)) Combatz.eatFood();
    			if(Combatz.shouldDrinkRangedBoost()) Combatz.drinkRangeBoost();
    			Sleepz.sleep(2222, 2222);
    			return;
    		}
    		//2. check for ghasts
    		NPC visibleGhast = NPCs.closest(n -> n!=null && 
    				n.getName().equals("Ghast") && 
    				n.hasAction("Attack"));
    		if(visibleGhast != null)
    		{
    			if(visibleGhast.interact("Attack")) {
        			Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), Sleepz.calculate(3333, 3333));
        		}
    			return;
    		}
    		
    		//3. check for druidPouch charges
    		if(Inventory.contains(id.druidPouchFilled))
    		{
    			NPC ghast = NPCs.closest(n -> n!=null && 
        				n.getName().equals("Ghast"));
    			if(ghast != null) 
    			{
    				if(Inventory.get(id.druidPouchFilled).useOn(ghast))
    				{
    					Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), Sleepz.calculate(4444, 3434));
    				}
    				return;
    			}
    			Logger.log("Invisible ghast null to put pouch on!");
    			return;
    		}
    		//4. check for at least 3 fungi to fill pouch
    		if(Inventory.count(id.mortFungus) >= 3)
    		{
    			if(Tabz.open(Tab.INVENTORY))
    			{
    				if(Inventory.interact(id.druidPouch, "Fill"))
    				{
    					Sleep.sleepUntil(() -> Inventory.contains(id.druidPouchFilled),Sleepz.calculate(3333, 3333));
    				}
    			}
    			return;
    		}
    		
    		//5. check for fungi on log
    		GameObject fungiLog = GameObjects.closest(g -> g!=null && 
					g.getName().equals("Fungi on log") && 
					g.hasAction("Pick"));
			if(fungiLog != null)
			{
				if(fungiLog.interact("Pick"))
				{
					Logger.log("Picking fungus!");
					Sleep.sleepUntil(() -> Inventory.contains(id.mortFungus), Sleepz.calculate(3333,3333));
				}
				return;
			}
			
    		//6. check for location of log casting and prayer, if so, cast spell
			if(Locations.natureSpirit_logSpellTile.contains(Players.getLocal()))
    		{
    			if(Tabz.open(Tab.INVENTORY))
    			{
    				if(Combatz.shouldDrinkPrayPot()) Combatz.drinkPrayPot();
    				if(Inventory.interact(id.blessedSickle, "Cast bloom"))
    				{
    					Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), Sleepz.calculate(3333,3333));
    					Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(),
    							() -> Players.getLocal().isAnimating(),
    							Sleepz.calculate(3333,3333),69);
    					return;
    				}
    			}
    			return;
    		}
    	}
		//6. go to area
		walkToFungusSpot();
    }
    public static void walkToFungusSpot()
    {
    	
    	if(Locations.natureSpiritGrotto.contains(Players.getLocal()))
		{
			API.interactWithGameObject("Bridge", "Jump", () -> Locations.natureSpiritGrottoBridgeSouth.contains(Players.getLocal()));
			return;
		}
    	
    	if(Locations.natureSpirit_logSpellTile.getCenter().distance() < 25)
		{
			if(Locations.natureSpirit_logSpellTile.getCenter().distance() < 6)
			{
				if(Walking.shouldWalk(6) && Walking.walkExact(Locations.natureSpirit_logSpellTile.getCenter())) Sleepz.sleep(696, 420);
				return;
			}
			if(Walking.shouldWalk(6) && Walking.walk(Locations.natureSpirit_logSpellTile.getCenter())) Sleepz.sleep(696, 420);
			return;
		}
    	if(Locations.natureSpirit_insideGrotto.contains(Players.getLocal()))
    	{
    		API.interactWithGameObject("Grotto", "Exit", () -> Locations.natureSpiritGrotto.contains(Players.getLocal()));
    		return;
    	}
    	if(Locations.PiP_undergroundPass.contains(Players.getLocal()))
    	{
    		if(Inventory.contains(id.salveGraveyardTab))
    		{
    			Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 30000);
    			return;
    		}
    		API.interactWithGameObject("Holy barrier", "Pass-through", () -> !Locations.PiP_undergroundPass.contains(Players.getLocal()));
    		return;
    	}
    	WidgetChild enterSwampButton = Widgets.get(580, 17);
		if(enterSwampButton != null && enterSwampButton.isVisible())
		{
			if(enterSwampButton.interact("Yes"))
			{
				Sleep.sleepUntil(() -> Locations.morytaniaSwampGateSouth.contains(Players.getLocal()), Sleepz.calculate(4444, 4444));
			}
			return;
		}
    	
    	if(Locations.morytaniaSwampGateNorth.contains(Players.getLocal()) || 
				Locations.salveGraveyard.contains(Players.getLocal()))
		{
			Logger.log("Clicking gate");
			API.interactWithGameObject("Gate", "Open",() -> Widgets.get(580, 17) != null && Widgets.get(580, 17).isVisible());
			return;
		}
    	
		Walkz.walkPath(Paths.drezelToNatureGrotto);
		return;
    }
    public static void harvestFirstFungus()
    {
    	if(Locations.natureSpirit_logSpellTile.contains(Players.getLocal()))
		{
			GameObject fungiLog = GameObjects.closest(g -> g!=null && 
					g.getName().equals("Fungi on log") && 
					g.hasAction("Pick"));
			if(fungiLog != null)
			{
				if(fungiLog.interact("Pick"))
				{
					Logger.log("Picking fungus!");
					Sleep.sleepUntil(() -> Inventory.contains(id.mortFungus), Sleepz.calculate(3333,3333));
				}
				return;
			}
			if(Tabz.open(Tab.INVENTORY))
			{
				if(Inventory.interact(id.natureSpirit_spell, "Cast"))
				{
					Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), Sleepz.calculate(3333,3333));
					Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(),
							() -> Players.getLocal().isAnimating(),
							Sleepz.calculate(3333,3333),69);
					return;
				}
			}
			return;
		}
    	walkToFungusSpot();
    }
    
    public static void goTalkToFilliman()
    {
    	if(Locations.natureSpiritGrotto.contains(Players.getLocal()))
		{
			if(!Equipment.contains(RestlessGhost.ghostspeakAmulet))
			{
				if(Inventory.contains(RestlessGhost.ghostspeakAmulet))
				{
					InvEquip.equipItem(RestlessGhost.ghostspeakAmulet);
				}
				return;
			}
			NPC filliman = NPCs.closest(n -> n!=null && 
					n.getName().equals("Filliman Tarlock") && 
					n.hasAction("Talk-to"));
			if(filliman != null)
			{
				if(filliman.interact("Talk-to"))
				{
					Sleep.sleepUntil(() -> Dialogues.inDialogue(),
							() -> Players.getLocal().isMoving(),
							Sleepz.calculate(3333,3333),69);
				}
				return;
			}
			API.interactWithGameObject("Grotto", "Enter");
			return;
		}
    	walkToGrotto();
    }
    
	public static void fulfillStep0()
	{
		InvEquip.clearAll();
		InvEquip.addInvyItem(TrainRanged.getBestDart(), 333, 1000, false, (int) Calculations.nextGaussianRandom(2000, 666));
		InvEquip.addInvyItem(id.rangePot4, 1, 1, false, (int) Calculations.nextGaussianRandom(15, 6));
		InvEquip.addInvyItem(id.stamina4, 2, 2, false, (int) Calculations.nextGaussianRandom(8, 3));
		InvEquip.addInvyItem(id.prayPot4, 1, 1, false, (int) Calculations.nextGaussianRandom(3, 2));
		InvEquip.addInvyItem(id.silverSickle, 1, 1, false, 1);
		InvEquip.addInvyItem(RestlessGhost.ghostspeakAmulet, 1, 1, false, 0);
		InvEquip.addInvyItem(Combatz.lowFood, 5, (int) Calculations.nextGaussianRandom(8, 2), false, (int) Calculations.nextGaussianRandom(75, 30));
		final int tabQty = (int) Calculations.nextGaussianRandom(6, 2);
		InvEquip.addInvyItem(id.salveGraveyardTab, 4, tabQty, false, tabQty);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
		InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
		InvEquip.shuffleFulfillOrder();
		if(InvEquip.fulfillSetup(true, 240000))
		{
			Logger.log("[NATURE SPIRIT] -> Fulfilled step 0 correctly!");
		} else Logger.log("[NATURE SPIRIT] -> NOT Fulfilled step 0 correctly! :-(");
	}
	public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(307);
    }
}
