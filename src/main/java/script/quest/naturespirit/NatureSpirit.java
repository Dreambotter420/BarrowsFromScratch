package script.quest.naturespirit;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
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
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.restlessghost.RestlessGhost;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Questz;
import script.utilities.Sleep;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.LinkedHashMap;
import java.util.List;
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
			Questz.closeQuestCompletion();
			if(Locations.natureSpirit_insideGrottoFinished.contains(Players.localPlayer()))
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
    	Player loc = Players.localPlayer();
        if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Nature Spirit");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        final int bestDart = TrainRanged.getBestDart();
        if(completed())
        {
        	MethodProvider.log("[COMPLETED] -> Nature Spirit!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
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
        		Sleep.sleep(420, 696);
        	}
        }
        if(Dialoguez.handleDialogues()) return Sleep.calculate(420, 696);
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
            if(!InvEquip.checkedBank()) return Sleep.calculate(420, 696);
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
        return Sleep.calculate(420,696);
    }
    public static void walkTalkGrotto()
    {
    	Player loc = Players.localPlayer();
    	if(Locations.natureSpirit_insideGrotto.contains(loc))
    	{
    		API.walkTalkWithGameObject("Grotto", "Search");
    		return;
    	}
    	walkToNatureSpirit();
    }
    public static void walkTalkNatureSpirit()
    {
    	Player loc = Players.localPlayer();
    	if(Locations.natureSpirit_insideGrotto.contains(loc))
    	{
    		NPC spirit = NPCs.closest("Nature Spirit");
    		if(spirit != null)	API.talkToNPC("Nature Spirit");
    		else API.walkTalkWithGameObject("Grotto", "Search");
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
    	Player loc = Players.localPlayer();
		if(Locations.natureSpiritGrotto.contains(loc))
		{
			API.interactWithGameObject("Grotto", "Enter", () -> Locations.natureSpirit_insideGrotto.contains(loc));
			return;
		}
		walkToGrotto();
		return;
    }
    public static boolean leaveUndergroundPass()
    {
    	if(Locations.PiP_undergroundPass.contains(Players.localPlayer()))
    	{
    		if(Inventory.contains(id.salveGraveyardTab))
    		{
    			Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 30000);
    		}
    		else API.interactWithGameObject("Holy barrier", "Pass-through", () -> !Locations.PiP_undergroundPass.contains(Players.localPlayer()));
    	}
    	return !Locations.PiP_undergroundPass.contains(Players.localPlayer());
    }
    public static void walkToGrotto()
    {
    	Player loc = Players.localPlayer();
    	if(!leaveUndergroundPass()) return;
    	if(Locations.entireMorytania.contains(loc))
    	{
    		if(Locations.natureSpiritGrottoBridgeSouth.contains(loc))
    		{
    			API.interactWithGameObject("Bridge", "Jump", () -> Locations.natureSpiritGrotto.contains(loc));
    			return;
    		}
        	WidgetChild enterSwampButton = Widgets.getWidgetChild(580, 17);
    		if(enterSwampButton != null && enterSwampButton.isVisible())
    		{
    			if(enterSwampButton.interact("Yes"))
    			{
    				MethodProvider.sleepUntil(() -> Locations.morytaniaSwampGateSouth.contains(loc), Sleep.calculate(4444, 4444));
    			}
    			return;
    		}
    		if(Locations.morytaniaSwampGateNorth.contains(loc) || 
    				Locations.salveGraveyard.contains(loc))
    		{
    			MethodProvider.log("Clicking gate");
    			API.interactWithGameObject("Gate", "Open",() -> Widgets.getWidgetChild(580, 17) != null && Widgets.getWidgetChild(580, 17).isVisible());
    			return;
    		}
    		Walkz.walkPath(Paths.drezelToNatureGrotto);
    		return;
    	}
    	if(Tabz.open(Tab.INVENTORY))
    	{
    		if(Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 30000))
    		{
    			MethodProvider.log("Teleported to salve graveyard to get into morytania!");
    		}
    	}
    }
    public static void walkTalkToDrezel()
    {
    	Player loc = Players.localPlayer();
    	if(Locations.PiP_undergroundPass.contains(loc)) //whole underground area
    	{
    		if(Locations.PiP_undergroundPassDrezel.contains(loc))
    		{
    			API.talkToNPC("Drezel");
    			return;
    		}
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.PiP_undergroundPassDrezel.getCenter())) Sleep.sleep(696, 666);
    		return;
    	}
    	if(Locations.natureSpirit_trapdoor.contains(loc))
    	{
    		Filter<GameObject> filter = g -> g!=null && g.getName().contains("Trapdoor") && g.hasAction("Climb-down");
    		API.walkInteractWithGameObject("Trapdoor", "Open", Locations.natureSpirit_trapdoor, () -> GameObjects.closest(filter) != null);
        	Sleep.sleep(696, 666);
        	API.walkInteractWithGameObject("Trapdoor", "Climb-down", Locations.natureSpirit_trapdoor, () -> Locations.PiP_undergroundPass.contains(loc));
        	Sleep.sleep(696, 666);
        	return;
    	}
    	if(Locations.natureSpirit_trapdoor.getCenter().distance() < 50)
    	{
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.natureSpirit_trapdoor.getCenter())) Sleep.sleep(696, 666);
    		return;
    	}
    	if(Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 180000)) Sleep.sleep(696, 666);
    	
    }
    public static void fillimansDiary()
    {
    	Player loc = Players.localPlayer();
    	if(!Locations.natureSpiritGrotto.contains(loc)) 
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
					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),
							() -> Players.localPlayer().isMoving(),
							Sleep.calculate(3333,3333),69);
				}
				return;
			}
			API.walkTalkWithGameObject("Grotto", "Enter");
    		return;
    	}
    	API.interactWithGameObject("Grotto tree", "Search", () -> Inventory.contains(id.natureSpirit_fillimansDiary));
    }
    public static void solvePuzzle()
    {
    	Player loc = Players.localPlayer();
    	if(Locations.natureSpiritGrotto.contains(loc))
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
	    				if(Locations.natureSpirit_finalPuzzleTile.contains(loc))
	    				{
	    					if(filliman.interact("Talk-to")) 
		    				{
		    					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(), Sleep.calculate(4444, 4444));
		    				}
	    					return;
	    				}
	    				if(Walking.shouldWalk(6) && Walking.walk(Locations.natureSpirit_finalPuzzleTile.getCenter())) Sleep.sleep(696, 666);
	    				return;
	    			}
	    			API.walkTalkWithGameObject("Grotto", "Enter");
	    			return;
				}
				if(Inventory.get(id.natureSpirit_usedSpell).useOn(GameObjects.closest(g->g!=null && 
						g.getName().equals("Stone") && 
						g.getID() == eastStone))) MethodProvider.sleepUntil(() -> placedFungus,Sleep.calculate(4444,4444));
				return;
			}
			if(Inventory.get(id.mortFungus).useOn(GameObjects.closest(g->g!=null && 
					g.getName().equals("Stone") && 
					g.getID() == westStone))) MethodProvider.sleepUntil(() -> placedFungus,Sleep.calculate(4444,4444));
			return;
		}
    	walkToGrotto();
    }
    
    public static void takeBowlMirror()
    {
    	Player loc = Players.localPlayer();
    	if(!Locations.natureSpiritGrotto.contains(loc)) 
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
					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),
							() -> Players.localPlayer().isMoving(),
							Sleep.calculate(3333,3333),69);
				}
				return;
			}
			API.walkTalkWithGameObject("Grotto", "Enter");
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
    	Player loc = Players.localPlayer();
    	
    	if(Locations.entireMorytania.contains(loc))
    	{
    		//1. check combat
    		if(loc.isInCombat())
    		{
    			if(Combatz.shouldEatFood(10)) Combatz.eatFood();
    			if(Combatz.shouldDrinkRangedBoost()) Combatz.drinkRangeBoost();
    			Sleep.sleep(2222, 2222);
    			return;
    		}
    		//2. check for ghasts
    		NPC visibleGhast = NPCs.closest(n -> n!=null && 
    				n.getName().equals("Ghast") && 
    				n.hasAction("Attack"));
    		if(visibleGhast != null)
    		{
    			if(visibleGhast.interact("Attack")) {
        			MethodProvider.sleepUntil(() -> loc.isInCombat(), Sleep.calculate(3333, 3333));
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
    					MethodProvider.sleepUntil(() -> loc.isInCombat(), Sleep.calculate(4444, 3434));
    				}
    				return;
    			}
    			MethodProvider.log("Invisible ghast null to put pouch on!");
    			return;
    		}
    		//4. check for at least 3 fungi to fill pouch
    		if(Inventory.count(id.mortFungus) >= 3)
    		{
    			if(Tabz.open(Tab.INVENTORY))
    			{
    				if(Inventory.interact(id.druidPouch, "Fill"))
    				{
    					MethodProvider.sleepUntil(() -> Inventory.contains(id.druidPouchFilled),Sleep.calculate(3333, 3333));
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
					MethodProvider.log("Picking fungus!");
					MethodProvider.sleepUntil(() -> Inventory.contains(id.mortFungus), Sleep.calculate(3333,3333));
				}
				return;
			}
			
    		//6. check for location of log casting and prayer, if so, cast spell
			if(Locations.natureSpirit_logSpellTile.contains(Players.localPlayer()))
    		{
    			if(Tabz.open(Tab.INVENTORY))
    			{
    				if(Combatz.shouldDrinkPrayPot()) Combatz.drinkPrayPot();
    				if(Inventory.interact(id.blessedSickle, "Cast bloom"))
    				{
    					MethodProvider.sleepUntil(() -> Players.localPlayer().isAnimating(), Sleep.calculate(3333,3333));
    					MethodProvider.sleepUntil(() -> !Players.localPlayer().isAnimating(),
    							() -> Players.localPlayer().isAnimating(),
    							Sleep.calculate(3333,3333),69);
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
    	Player loc = Players.localPlayer();
    	if(Locations.natureSpiritGrotto.contains(loc))
		{
			API.interactWithGameObject("Bridge", "Jump", () -> Locations.natureSpiritGrottoBridgeSouth.contains(loc));
			return;
		}
    	
    	if(Locations.natureSpirit_logSpellTile.getCenter().distance() < 25)
		{
			if(Locations.natureSpirit_logSpellTile.getCenter().distance() < 6)
			{
				if(Walking.shouldWalk(6) && Walking.walkExact(Locations.natureSpirit_logSpellTile.getCenter())) Sleep.sleep(696, 420);
				return;
			}
			if(Walking.shouldWalk(6) && Walking.walk(Locations.natureSpirit_logSpellTile.getCenter())) Sleep.sleep(696, 420);
			return;
		}
    	if(Locations.natureSpirit_insideGrotto.contains(loc))
    	{
    		API.interactWithGameObject("Grotto", "Exit", () -> Locations.natureSpiritGrotto.contains(loc));
    		return;
    	}
    	if(Locations.PiP_undergroundPass.contains(loc))
    	{
    		if(Inventory.contains(id.salveGraveyardTab))
    		{
    			Walkz.teleport(id.salveGraveyardTab, Locations.salveGraveyard, 30000);
    			return;
    		}
    		API.interactWithGameObject("Holy barrier", "Pass-through", () -> !Locations.PiP_undergroundPass.contains(loc));
    		return;
    	}
    	WidgetChild enterSwampButton = Widgets.getWidgetChild(580, 17);
		if(enterSwampButton != null && enterSwampButton.isVisible())
		{
			if(enterSwampButton.interact("Yes"))
			{
				MethodProvider.sleepUntil(() -> Locations.morytaniaSwampGateSouth.contains(loc), Sleep.calculate(4444, 4444));
			}
			return;
		}
    	
    	if(Locations.morytaniaSwampGateNorth.contains(loc) || 
				Locations.salveGraveyard.contains(loc))
		{
			MethodProvider.log("Clicking gate");
			API.interactWithGameObject("Gate", "Open",() -> Widgets.getWidgetChild(580, 17) != null && Widgets.getWidgetChild(580, 17).isVisible());
			return;
		}
    	
		Walkz.walkPath(Paths.drezelToNatureGrotto);
		return;
    }
    public static void harvestFirstFungus()
    {
    	Player loc = Players.localPlayer();
    	if(Locations.natureSpirit_logSpellTile.contains(Players.localPlayer()))
		{
			GameObject fungiLog = GameObjects.closest(g -> g!=null && 
					g.getName().equals("Fungi on log") && 
					g.hasAction("Pick"));
			if(fungiLog != null)
			{
				if(fungiLog.interact("Pick"))
				{
					MethodProvider.log("Picking fungus!");
					MethodProvider.sleepUntil(() -> Inventory.contains(id.mortFungus), Sleep.calculate(3333,3333));
				}
				return;
			}
			if(Tabz.open(Tab.INVENTORY))
			{
				if(Inventory.interact(id.natureSpirit_spell, "Cast"))
				{
					MethodProvider.sleepUntil(() -> Players.localPlayer().isAnimating(), Sleep.calculate(3333,3333));
					MethodProvider.sleepUntil(() -> !Players.localPlayer().isAnimating(),
							() -> Players.localPlayer().isAnimating(),
							Sleep.calculate(3333,3333),69);
					return;
				}
			}
			return;
		}
    	walkToFungusSpot();
    }
    
    public static void goTalkToFilliman()
    {
    	Player loc = Players.localPlayer();
    	if(Locations.natureSpiritGrotto.contains(loc))
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
					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(),
							() -> Players.localPlayer().isMoving(),
							Sleep.calculate(3333,3333),69);
				}
				return;
			}
			API.walkTalkWithGameObject("Grotto", "Enter");
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
			MethodProvider.log("[NATURE SPIRIT] -> Fulfilled step 0 correctly!");
		} else MethodProvider.log("[NATURE SPIRIT] -> NOT Fulfilled step 0 correctly! :-(");
	}
	public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(307);
    }
}
