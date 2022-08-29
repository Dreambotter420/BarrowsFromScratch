package script.quest.fremenniktrials;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
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
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.melee.TrainMelee;
import script.utilities.API;
import script.utilities.Combatz;
import script.utilities.InvEquip;
import script.utilities.ItemsOnGround;
import script.utilities.Locations;
import script.utilities.MissingAPI;
import script.utilities.Questz;
import script.utilities.Shopz;
import script.utilities.Sleep;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Fremennik Trials
 * @author Dreambotter420
 * ^_^
 */
public class FremennikTrials extends Leaf {
	public static boolean started = false;
    public void onStart() {
        
        started = true;
    }
    public static boolean onExit()
    {
    	return true;
    }
    public static boolean completed()
    {
    	if(getProgressValue() == 10)
    	{
    		if(Widgets.getWidgetChild(153,16) != null && 
    				Widgets.getWidgetChild(153,16).isVisible())
    		{
    			if(Widgets.getWidgetChild(153,16).interact())
    			{
    				Sleep.sleep(666, 669);
    			}
    			return false;
    		}
    		return true;
    	}
		return false;
    }
    @Override
	public boolean isValid() {
		return API.mode == API.modes.FREMENNIK_TRIALS;
	}

    private static final Tile bookcaseLeft = new Tile(2634,3665,2);
    private static final Tile chestWater = new Tile(2632,3665,2);
    private static final Tile chestRegular = new Tile(2638,3662,2);
    private static final Tile trapdoorRight = new Tile(2636,3663,2);
    @Override
    public int onLoop() {
    	if (completed()) {
            MethodProvider.log("[COMPLETED] -> Fremennik Trials!");
            if(onExit())
            {
            	API.mode = null;
            	Main.customPaintText1 = "~~~~~~~~~~~";
        		Main.customPaintText2 = "~Quest Complete~";
        		Main.customPaintText3 = "~Fremennik Trials~";
        		Main.customPaintText4 = "~~~~~~~~~~~";
            }
            return Timing.sleepLogNormalSleep();
        }
    	if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Fremennik Trials");
            if(onExit())
            {
            	API.mode = null;
            }
            return Timing.sleepLogNormalSleep();
        }
    	if(!InvEquip.checkedBank()) return Sleep.calculate(111, 1111);
    	if(handleDialogues()) return Timing.sleepLogNormalSleep();
    	switch(getProgressValue())
    	{
    	
    	case(8):
    	{
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		API.randomAFK(2);
    		API.walkTalkToNPC("Brundt", "Talk-to", Locations.fremmy_longHall);
    		break;
    	}
    	case(7):
    	{
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		API.randomAFK(2);
    		if(Locations.fremmy_upstairsHelmetShop.contains(Players.localPlayer()))
    		{
    			if(Players.localPlayer().isAnimating())
    			{
    				Sleep.sleep(69, 696);
    				break;
    			}
    			API.walkInteractWithGameObject("Ladder", "Climb-down",Locations.fremmy_upstairsHelmetShop,() -> !Locations.fremmy_upstairsHelmetShop.contains(Players.localPlayer()));
    			break;
    		}
    		if(Locations.fremmy_peerRightHouse.contains(Players.localPlayer()))
    		{
    			if(Inventory.contains(id.fremmy_unfrozenKey))
    			{
    				API.walkInteractWithGameObject("Door", "Open", Locations.fremmy_peerRightDoor,() -> !Locations.fremmy_peerRightHouse.contains(Players.localPlayer()));
    				break;
    			}
    			if(Inventory.contains(id.fremmy_vaseLid))
    			{
    				API.walkInteractWithGameObject("Ladder", "Climb-up",Locations.fremmy_peerRightHouse,() -> !Locations.fremmy_peerRightHouse.contains(Players.localPlayer()));
        			break;
    			}
    			if(Inventory.contains(id.fremmy_oldRedDisk))
    			{
    				if(Inventory.get(id.fremmy_oldRedDisk).useOn(GameObjects.closest("Abstract mural")))
    				{
    					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_oldRedDisk), Sleep.calculate(2222,2222));
    				}
    			}
    			if(Inventory.contains(id.fremmy_redDisk))
    			{
    				if(Inventory.get(id.fremmy_redDisk).useOn(GameObjects.closest("Abstract mural")))
    				{
    					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_redDisk), Sleep.calculate(2222,2222));
    				}
    			}
    			break;
    		}
    		if(Locations.fremmy_peerUpstairs.contains(Players.localPlayer()))
    		{
    			if(Inventory.contains(id.fremmy_unfrozenKey))
    			{
    				API.interactWithGameObject("Trapdoor", "Open",trapdoorRight);
    				Sleep.sleep(1111, 1111);
    				API.interactWithGameObject("Trapdoor", "Climb-down",trapdoorRight,() -> !Locations.fremmy_peerUpstairs.contains(Players.localPlayer()));
    				break;
    			}
    			if(Inventory.contains(id.fremmy_frozenKey))
    			{
    				if(Inventory.get(id.fremmy_frozenKey).useOn(GameObjects.closest("Cooking range")))
    				{
    					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_frozenKey), Sleep.calculate(2222,2222));
    				}
    				break;
    			}
    			if(Inventory.contains(id.fremmy_wholeVase))
    			{
    				if(Inventory.get(id.fremmy_wholeVase).useOn(GameObjects.closest("Frozen table")))
    				{
    					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_wholeVase), Sleep.calculate(2222,2222));
    				}
    				break;
    			}
    			if(Inventory.contains(id.fremmy_vaseLid))
    			{
    				if(Inventory.contains(id.fremmy_fullVase))
        			{
    					if(Inventory.get(id.fremmy_fullVase).useOn(id.fremmy_vaseLid))
        				{
        					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_fullVase), Sleep.calculate(2222,2222));
        				}
    					break;
        			}
    				if(Inventory.contains(id.fremmy_vase))
    				{
    					if(Inventory.get(id.fremmy_vase).useOn(GameObjects.closest("Tap")))
        				{
        					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_vase), Sleep.calculate(2222,2222));
        				}
    					break;
    				}
    				if(Inventory.contains(id.fremmy_4_5Bucket))
    				{
    					if(Inventory.get(id.fremmy_4_5Bucket).useOn(GameObjects.closest(g -> g!=null && g.getName().contains("Chest") && g.getTile().equals(chestWater))))
        				{
        					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_4_5Bucket), Sleep.calculate(2222,2222));
        				}
    					break;
    				}
    				if(Inventory.contains(id.fremmy_2_3Jug))
        			{
    					if(Inventory.contains(id.fremmy_fullBucket))
        				{
        					if(Inventory.get(id.fremmy_fullBucket).useOn(id.fremmy_2_3Jug))
            				{
            					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_fullBucket), Sleep.calculate(2222,2222));
            				}
        					break;
        				}
    					if(Inventory.contains(id.fremmy_emptyBucket))
        				{
        					if(Inventory.get(id.fremmy_emptyBucket).useOn(GameObjects.closest("Tap")))
            				{
            					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_emptyBucket), Sleep.calculate(2222,2222));
            				}
        					break;
        				}
    					break;
        			}
    				if(Inventory.contains(id.fremmy_2_5Bucket))
        			{
    					if(Inventory.contains(id.fremmy_emptyJug))
    					{
    						if(Inventory.get(id.fremmy_2_5Bucket).useOn(id.fremmy_emptyJug))
            				{
            					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_emptyJug),Sleep.calculate(4444, 3333));
            				}
            				break;
    					}
    					if(Inventory.contains(id.fremmy_fullJug))
    					{
    						if(Inventory.get(id.fremmy_fullJug).useOn(GameObjects.closest("Drain")))
            				{
            					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_fullJug),Sleep.calculate(4444, 3333));
            				}
            				break;
    					}
    					break;
        			}
    				if(Inventory.contains(id.fremmy_fullBucket))
        			{
        				if(Inventory.get(id.fremmy_fullBucket).useOn(id.fremmy_emptyJug))
        				{
        					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_fullBucket),Sleep.calculate(4444, 3333));
        				}
        				break;
        			}
    				if(Inventory.contains(id.fremmy_emptyBucket))
    				{
    					if(Inventory.get(id.fremmy_emptyBucket).useOn(GameObjects.closest("Tap")))
        				{
        					MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_emptyBucket), Sleep.calculate(2222,2222));
        				}
    					break;
    				}
    				break;
    			}
    			if(Inventory.contains(id.fremmy_redDisk))
    			{
    				API.interactWithGameObject("Trapdoor", "Open",trapdoorRight);
    				Sleep.sleep(1111, 1111);
    				API.interactWithGameObject("Trapdoor", "Climb-down",trapdoorRight,() -> !Locations.fremmy_peerUpstairs.contains(Players.localPlayer()));
    				break;
    			}
    			if(Inventory.contains(id.fremmy_redGoop))
    			{
    				if(Inventory.get(id.fremmy_redGoop).useOn(id.fremmy_woodenDisk))
    				{
    					MethodProvider.sleepUntil(() -> Inventory.contains(id.fremmy_redDisk),Sleep.calculate(4444, 3333));
    				}
    				break;
    			}
    			if(Inventory.contains(id.fremmy_emptyBucket))
    			{
    				if(Inventory.get(id.fremmy_redHerring).useOn(GameObjects.closest("Cooking range")))
    				{
    					MethodProvider.sleepUntil(() -> Inventory.contains(id.fremmy_redGoop),Sleep.calculate(4444, 3333));
    				}
    				break;
    			}
    			if(Inventory.contains(id.fremmy_oldRedDisk))
    			{
    				API.interactWithGameObject("Cupboard", "Open");
    				Sleep.sleep(1111, 1111);
    				API.walkInteractWithGameObject("Cupboard", "Search",Locations.fremmy_peerUpstairs,() -> Inventory.contains(id.fremmy_emptyBucket));
    				break;
    			}
    			if(Inventory.contains(id.fremmy_woodenDisk))
    			{
    				API.interactWithGameObject("Unicorn\'s head", "Study");
    				break;
    			}
    			if(Inventory.contains(id.fremmy_emptyJug))
    			{
    				API.interactWithGameObject("Bull\'s head", "Study");
    				break;
    			}
    			if(Inventory.contains(id.fremmy_redHerring))
    			{
    				API.interactWithGameObject("Chest", "Open",chestRegular); 
    				Sleep.sleep(1111, 1111);
    				API.interactWithGameObject("Chest", "Search",chestRegular,() -> Inventory.contains(id.fremmy_emptyJug));
    				break;
    			}
    			API.interactWithGameObject("Bookcase", "Search", bookcaseLeft,() -> Inventory.contains(id.fremmy_redHerring));
    			break;
    		}
    		if(Locations.fremmy_peerLeftHouse.contains(Players.localPlayer()))
    		{
    			API.walkInteractWithGameObject("Ladder", "Climb-up", Locations.fremmy_peerLeftHouse, () -> !Locations.fremmy_peerLeftHouse.contains(Players.localPlayer()));
    			break;
    		}
    		
    		if(Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    		{
    			if(!Inventory.isEmpty())
    			{
    				API.walkTalkToNPC("Peer the Seer", "Talk-to", Locations.fremmy_peerTheSeer);
        			break;
    			}
    			if(!solvedRiddle)
    			{
    				if(riddleAnswer != null)
        			{
        				solvePeerPuzzle();
        				break;
        			}
        			API.walkInteractWithGameObject("Door", "Open", Locations.fremmy_peerLeftDoor, Dialogues::inDialogue);
    			}
    			API.walkInteractWithGameObject("Door", "Open", Locations.fremmy_peerLeftDoor, Dialogues::inDialogue);
    		}
    		break;
    	}
    	case(6):
    	{
    		if(!getEventRPG()) break;
    		if(Locations.fremmy_koscheiArena.contains(Players.localPlayer()))
    		{
    			if(koschei4thForm)
    			{
    				if(!Players.localPlayer().isInCombat())
    				{
    					API.interactNPC("Koschei the deathless", "Attack", Locations.fremmy_koscheiArena, false,() -> Players.localPlayer().isInCombat());
    					Sleep.sleep(2222, 2222);
    					break;
    				}
    				break;
    			}
    			if(!InvEquip.invyContains(Combatz.foods))
    			{
    				if(!Prayers.isActive(Prayer.PROTECT_FROM_MELEE))
    				{
    					if(Skills.getBoostedLevels(Skill.PRAYER) > 0)
    					{
    						Tabz.open(Tab.PRAYER);
    						Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE);
    						Sleep.sleep(666, 696);
    						break;
    					}
    					if(!InvEquip.invyContains(id.prayPots))
        				{
        					if(!Walkz.useJewelry(InvEquip.glory, "Edgeville") && 
        							!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange"))
        					{
        						MethodProvider.log("Well we gonna die maybe! No food or pray pots and no tele out");
        					}
        					break;
        				}
    				}
    				if(Combatz.shouldDrinkPrayPot()) Combatz.drinkPrayPot();
    			}
    			if(Combatz.shouldEatFood(15)) Combatz.eatFood();
    			if(Combatz.shouldDrinkMeleeBoost()) Combatz.drinkMeleeBoost();
    			
    			if(!Players.localPlayer().isInCombat())
				{
					API.interactNPC("Koschei the deathless", "Attack", Locations.fremmy_koscheiArena, false,() -> Players.localPlayer().isInCombat());
					Sleep.sleep(2222, 2222);
					break;
				}
    			break;
    		}
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		API.randomAFK(2);
    		if(!InvEquip.invyContains(Combatz.foods) || 
    				Inventory.contains(id.tinderbox) || 
    				Inventory.contains(id.fremmy_lyre) || 
    				Inventory.contains("Coins") || 
    				Equipment.fullSlotCount() > 4 || 
    				Inventory.count(id.seaTurtle) < 15)
    		{
    			fulfillKoscheiFight();
    			break;
    		}
    		if(Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    		{
    			if(talkedToThorvaldForKoschei)
    			{
    				API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.fremmy_helmetShop, () -> !Locations.fremmy_helmetShop.contains(Players.localPlayer()));
    				break;
    			}
    			API.walkTalkToNPC("Thorvald", "Talk-to", Locations.fremmy_helmetShop);
    			break;
    		}
    		
    		Walkz.teleportCamelot(180000);
    		break;
    	}
    	case(5):
    	{
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		API.randomAFK(2);
    		if(!Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    		{
    			Walkz.teleportCamelot(180000);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_askeladdenPromise))
    		{
    			API.walkTalkToNPC("Thora", "Talk-to",Locations.fremmy_longHall);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_legendaryCocktail))
    		{
    			API.walkTalkToNPC("Manni", "Talk-to",Locations.fremmy_longHall);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_championsToken))
    		{
    			API.walkTalkToNPC("Thorvald", "Talk-to",Locations.fremmy_helmetShop);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_warriorsContract))
    		{
    			API.walkTalkToNPC("Peer the Seer", "Talk-to",Locations.fremmy_peerTheSeer);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_weatherForecast))
    		{
    			API.walkTalkToNPC("Swensen", "Talk-to",Locations.fremmy_swensen);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_seaFishingMap))
    		{
    			API.walkTalkToNPC("Fisherman", "Talk-to",Locations.fremmy_fisherman);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_unusualFish))
    		{
    			API.walkTalkToNPC("Skulgrimen", "Talk-to",Locations.fremmy_helmetShop);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_customBowString))
    		{
    			API.walkTalkToNPC("Sigli", "Talk-to",Locations.fremmy_sigliDaHunstman);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_trackingMap))
    		{
    			API.walkTalkToNPC("Brundt", "Talk-to",Locations.fremmy_longHall);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_fiscalStatement))
    		{
    			API.walkTalkToNPC("Yrsa", "Talk-to",Locations.fremmy_yrsa);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_customBoots))
    		{
    			API.walkTalkToNPC("Olaf", "Talk-to",Locations.fremmy_olafDaBeard);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_fremennikBallad))
    		{
    			API.walkTalkToNPC("Sailor", "Talk-to",Locations.fremmy_sailor);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_exoticFlowers))
    		{
        		API.walkTalkToNPC("Sigmund", "Talk-to", Locations.fremmy_sigmund);
    			break;
    		}
    		if(merchant_talkedThora1)
    		{
    			API.walkTalkToNPC("Askeladden", "Talk-to",Locations.fremmy_askeladden);
    			break;
    		}
    		if(merchant_talkedManni1)
    		{
    			API.walkTalkToNPC("Thora", "Talk-to",Locations.fremmy_longHall);
    			break;
    		}
    		if(merchant_talkedThorvald1)
    		{
    			API.walkTalkToNPC("Manni", "Talk-to",Locations.fremmy_longHall);
    			break;
    		}
    		if(merchant_talkedPeerTheSeer1)
    		{
    			API.walkTalkToNPC("Thorvald", "Talk-to",Locations.fremmy_helmetShop);
    			break;
    		}
    		if(merchant_talkedSwensen1)
    		{
    			API.walkTalkToNPC("Peer the Seer", "Talk-to",Locations.fremmy_peerTheSeer);
    			break;
    		}
    		if(merchant_talkedFisherman1)
    		{
    			API.walkTalkToNPC("Swensen", "Talk-to",Locations.fremmy_swensen);
    			break;
    		}
    		if(merchant_talkedSkulgrimen1)
    		{
    			API.walkTalkToNPC("Fisherman", "Talk-to",Locations.fremmy_fisherman);
    			break;
    		}
    		if(merchant_talkedSigli1)
    		{
    			API.walkTalkToNPC("Skulgrimen", "Talk-to",Locations.fremmy_helmetShop);
    			break;
    		}
    		if(merchant_talkedBrundt1)
    		{
    			API.walkTalkToNPC("Sigli", "Talk-to",Locations.fremmy_sigliDaHunstman);
    			break;
    		}
    		if(merchant_talkedYrsa1)
    		{
    			API.walkTalkToNPC("Brundt", "Talk-to",Locations.fremmy_longHall);
    			break;
    		}
    		if(merchant_talkedOlaf1)
    		{
    			API.walkTalkToNPC("Yrsa", "Talk-to",Locations.fremmy_yrsa);
    			break;
    		}
    		if(merchant_talkedSailor1)
    		{
    			API.walkTalkToNPC("Olaf", "Talk-to",Locations.fremmy_olafDaBeard);
    			break;
    		}
    		if(merchant_talkedSigmund1)
    		{
    			API.walkTalkToNPC("Sailor", "Talk-to",Locations.fremmy_sailor);
    			break;
    		}
    		API.walkTalkToNPC("Sigmund", "Talk-to", Locations.fremmy_sigmund);
    		
    		break;
    	}
    	case(4):
    	{
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		API.randomAFK(2);
    		if(swensenPuzzle.contains(Players.localPlayer()))
			{
				solveSwensenPuzzle();
    			break;
			}
    		if(canEnterSwensenMaze)
    		{
    			if(swensenPuzzle.contains(Players.localPlayer()))
    			{
    				solveSwensenPuzzle();
        			break;
    			}
    			API.walkInteractWithGameObject("Ladder", "Climb-down", Locations.fremmy_swensen, () -> swensenPuzzle.contains(Players.localPlayer()));
    			break;
    		}
    		if(!Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    		{
    			Walkz.teleportCamelot(180000);
    			break;
    		}
    		API.walkTalkToNPC("Swensen the Navigator", "Talk-to", Locations.fremmy_swensen);
    		break;
    	}
    	case(3):
    	{
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		if(shouldFulfillStart()) break;
    		API.randomAFK(2);
    		if(Inventory.contains(id.fremmy_strangeObject1))
    		{
    			if(Inventory.get(id.tinderbox).useOn(id.fremmy_strangeObject1))
    			{
    				MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_strangeObject1), Sleep.calculate(2222,2222));
    			}
    			break;
    		}
    		if(Inventory.contains(id.fremmy_strangeObject2))
    		{
    			if(Locations.fremmy_pipeBomb.contains(Players.localPlayer()))
        		{
        			if(Inventory.get(id.fremmy_strangeObject2).useOn(GameObjects.closest(g -> g!=null && g.getName().equals("Pipe") && g.hasAction("Put-inside"))))
        			{
        				MethodProvider.sleepUntil(() -> !Inventory.contains(id.fremmy_strangeObject2),
        						() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),69);
        			}
        			break;
        		}
    			if(Locations.fremmy_wholeConcert.contains(Players.localPlayer()))
    			{
    				API.walkInteractWithGameObject("Door", "Open", Locations.fremmy_concertDoor, () -> !Locations.fremmy_wholeConcert.contains(Players.localPlayer()));
    				break;
    			}
    			if(Locations.fremmy_longHall.getCenter().distance() < 250)
    			{
    				Walkz.walkToArea(Locations.fremmy_pipeBomb);
    				break;
    			}
    			Walkz.teleportCamelot(180000);
        		break;
    		}
    		if(Locations.fremmy_longHall.contains(Players.localPlayer()))
			{
    			if(filledLowAlcoholKeg)
    			{
    				API.walkTalkToNPC("Manni the Reveller", "Talk-to", Locations.fremmy_longHall);
    				break;
    			}
    			if(!Inventory.contains(id.fremmy_kegOfBeer))
    			{
    				getKegOfBeer();
    				break;
    			}
				if(Inventory.contains(id.fremmy_kegOfBeer) && Inventory.contains(id.fremmy_lowAlcoholKeg))
				{
					if(Inventory.get(id.fremmy_kegOfBeer).useOn(id.fremmy_lowAlcoholKeg))
					{
						MethodProvider.sleepUntil(() -> filledLowAlcoholKeg, Sleep.calculate(3333, 2222));
					}
					break;
				}
    			break;
			}
			if(Locations.fremmy_longHall.getCenter().distance() < 250)
			{
				Walkz.walkToArea(Locations.fremmy_longHall);
				break;
			}
			Walkz.teleportCamelot(180000);
    		break;
    	}
    	case(2):
    	{
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		if(shouldFulfillStart()) break;

    		API.randomAFK(2);
    		if(Inventory.contains(id.fremmy_enchantedLyre))
    		{
    			if(Locations.fremmy_concertPlatform.contains(Players.localPlayer()))
    			{
    				if(Inventory.interact(id.fremmy_enchantedLyre, "Play"))
    				{
    					MethodProvider.sleepUntil(Dialogues::inDialogue, Sleep.calculate(15000, 5555));
    				}
    				break;
    			}
    			if(Locations.fremmy_wholeConcert.contains(Players.localPlayer()))
    			{
    				if(Walking.shouldWalk(6) && Walking.walk(Locations.fremmy_concertPlatform.getCenter())) Sleep.sleep(696, 666);
    				break;
    			}
    			if(Locations.fremmy_concertDoor.getCenter().distance() < 250)
    			{
    				API.walkInteractWithGameObject("Door", "Open", Locations.fremmy_concertDoor, () -> Dialogues.inDialogue());
    				break;
    			}
    		} 
    		MethodProvider.log("need enchanted lyre!");
    		break;
    	}
    	case(1):
    	{
    		if(shouldFulfillStart()) break;
    		if(Bank.contains(id.fremmy_huntersTalisman))
    		{
    			InvEquip.withdrawOne(id.fremmy_huntersTalisman,180000);
    			break;
    		}
    		if(Bank.contains(id.fremmy_huntersTalismanAbsorbedDraugen))
    		{
    			InvEquip.withdrawOne(id.fremmy_huntersTalismanAbsorbedDraugen,180000);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_huntersTalismanAbsorbedDraugen))
    		{
    			API.walkTalkToNPC("Sigli the Huntsman", "Talk-to", Locations.fremmy_sigliDaHunstman);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_strangeObject1))
    		{
    			findFightDraugen();
    			break;
    		}
    		if(Inventory.contains(id.fremmy_enchantedLyre))
    		{
    			getStrangeObject1();
    			break;
    		}
    		if(Inventory.contains(id.fremmy_lowAlcoholKeg))
    		{
    			API.walkInteractWithGameObject("Strange altar", "Summon", Locations.strangeAltar, Dialogues::inDialogue);
    			break;
    		}
    		if(Inventory.contains(id.fremmy_huntersTalisman))
    		{
    			if(!Inventory.contains(id.fremmy_lyre))
    			{
    				if(Locations.fremmy_lanzigHut.contains(Players.localPlayer()))
    				{
    					GroundItem lyreNStuff = ItemsOnGround.getNearbyGroundItem(ItemsOnGround.lanzigLoot,Locations.fremmy_lanzigHut);
            			if(lyreNStuff != null)
            			{
            				ItemsOnGround.grabNearbyGroundItem(lyreNStuff);
            				break;
            			}
            			if(Combatz.shouldDrinkMeleeBoost()) Combatz.drinkMeleeBoost();
            			if(Combatz.shouldEatFood(12))Combatz.eatFood();
            			if(!Players.localPlayer().isInCombat())
            			{
            	    		API.randomAFK(2);
            				API.walkInteractNPC("Lanzig","Attack",Locations.fremmy_lanzigHut,() -> Players.localPlayer().isInCombat());
            			}
            			Sleep.sleep(69, 2222);
            			break;
    				}
    				if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    	    		API.randomAFK(2);
    				if(Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    				{
    					API.walkInteractNPC("Lanzig","Attack",Locations.fremmy_lanzigHut,() -> Players.localPlayer().isInCombat());
            			break;
    				}
    				Walkz.teleportCamelot(180000);
    				break;
    			}
    			if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
        		API.randomAFK(2);
    			//have lyre
    			if(Locations.seersPub.getCenter().distance() <= 75)
    			{
    				API.walkTalkToNPC("Poison Salesman", "Talk-to", Locations.seersPub);
    				break;
    			}
    			Walkz.teleportCamelot(180000);
    			break;
    		}
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		API.randomAFK(2);
    		
    		
    		if(talkedToOlafTwice)
    		{
    			API.walkTalkToNPC("Sigli the Huntsman", "Talk-to", Locations.fremmy_sigliDaHunstman);
    			break;
    		}
    		if(participatedInKegContest)
    		{
    			API.walkTalkToNPC("Olaf the Bard","Talk-to", Locations.fremmy_olafDaBeard);
    			break;
    		}
    		if(Players.localPlayer().isAnimating()) 
    		{
    			Sleep.sleep(2222, 2222);
    			break;
    		}
    		if(!Inventory.contains(id.fremmy_beerTankard))
    		{
    			getBeerTankard();
    			break;
    		}
    		if(canPickupBeer && !Inventory.contains(id.fremmy_kegOfBeer))
    		{
    			API.walkPickupGroundItem(id.fremmy_kegOfBeer, "Take", false,Locations.fremmy_longHall);
    			break;
    		}
    		API.walkTalkToNPC("Manni the Reveller", "Talk-to", Locations.fremmy_longHall);
    		break;
    	}
    	case(0):
    	{
    		if(Questz.shouldCheckQuestStep()) Questz.checkQuestStep("The Fremennik Trials");
    		API.randomAFK(2);
    		if(shouldFulfillStart()) break;
        	if(Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
        	{
        		API.walkTalkToNPC("Brundt the Chieftain", "Talk-to", Locations.fremmy_longHall);
        		return Timing.sleepLogNormalSleep();
        	}
        	Walkz.teleportCamelot(180000);
        	break;
    	}
    	default:break;
    	}
        return Sleep.calculate(69, 696) + Timing.sleepLogNormalSleep();
    }
    public static boolean shouldFulfillStart()
    {
    	if(!Inventory.contains(id.tinderbox) || 
    			Inventory.count(InvEquip.coins) > 20000 || 
    			Inventory.count(InvEquip.coins) < 5500 || 
    			!Equipment.contains(TrainMelee.getBestWeapon()) || 
    			!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
    			!InvEquip.equipmentContains(InvEquip.wearableGlory) || 
    			!InvEquip.equipmentContains(InvEquip.wearableCombats) || 
    			!InvEquip.invyContains(id.superCombats) || 
    			!Inventory.contains(id.seaTurtle) || 
    			!Inventory.contains(id.cammyTele) || 
    			(!Inventory.contains(id.rawShark) && !Inventory.contains(id.fremmy_enchantedLyre) && !Inventory.contains(id.fremmy_lyre)))
    	{
    		fulfillStart();
    		return true;
    	}
    	return false;
    }
    public static void fulfillStart()
    {
    	InvEquip.clearAll();
    	TrainMelee.setBestMeleeEquipment();
    	InvEquip.addInvyItem(id.tinderbox, 1, 1, false,1);
    	InvEquip.addInvyItem(id.seaTurtle, 6, 6, false,(int) Calculations.nextGaussianRandom(50,10));
    	InvEquip.addInvyItem(id.cammyTele, 6, (int) Calculations.nextGaussianRandom(10,2), false,(int) Calculations.nextGaussianRandom(20,5));
    	InvEquip.addInvyItem(id.superCombat4, 1, 1, false,(int) Calculations.nextGaussianRandom(5, 2));
    	InvEquip.addInvyItem(id.prayPot4, 1, 1, false,(int) Calculations.nextGaussianRandom(5, 2));
    	InvEquip.addInvyItem(id.stamina4, 3, 3, false,(int) Calculations.nextGaussianRandom(10, 3));
    	InvEquip.addInvyItem(id.tinderbox, 1, 1, false,1);
    	InvEquip.addInvyItem(id.rawShark, 1, 1, false,1);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(InvEquip.coins, 15000, (int) Calculations.nextGaussianRandom(17500, 888), false,0);
    	InvEquip.fulfillSetup(true, 180000);
    }
    public static void fulfillKoscheiFight()
    {
    	InvEquip.clearAll();
    	InvEquip.setEquipItem(EquipmentSlot.WEAPON,id.eventRPG);
    	InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.glory);
    	InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
    	InvEquip.setEquipItem(EquipmentSlot.HANDS,InvEquip.combat);
    	InvEquip.addInvyItem(id.cammyTele, 1, 1, false,(int) Calculations.nextGaussianRandom(10,5));
    	InvEquip.addInvyItem(id.superCombat4, 1, 1, false,(int) Calculations.nextGaussianRandom(5, 2));
    	InvEquip.addInvyItem(id.prayPot4, 1, 1, false,(int) Calculations.nextGaussianRandom(5, 2));
    	InvEquip.addInvyItem(id.stamina4, 1, 1, false,(int) Calculations.nextGaussianRandom(10, 3));
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(id.seaTurtle, 15, 24, false,(int) Calculations.nextGaussianRandom(50,10));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false,0);
    	InvEquip.fulfillSetup(true, 180000);
    }
    public static boolean getKegOfBeer()
    {
    	if(Inventory.contains(id.fremmy_kegOfBeer)) return true;
    	if(!Inventory.contains(id.fremmy_kegOfBeer))
    	{
    		if(!Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    		{
    			Walkz.teleportCamelot(180000);
    			return false;
    		}
    		API.walkPickupGroundItem(id.fremmy_kegOfBeer, "Take", false, Locations.fremmy_longHall);
			return false;
    	}
    	return false;
    }
    public static boolean getBeerTankard()
    {
    	if(Inventory.contains(id.fremmy_beerTankard)) return true;
    	if(!Inventory.contains(id.fremmy_beerTankard))
    	{
    		if(!Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    		{
    			Walkz.teleportCamelot(180000);
    			return false;
    		}
    		API.walkPickupGroundItem(id.fremmy_beerTankard, "Take", false, Locations.fremmy_longHall);
			return false;
    	}
    	return false;
    }
    public static boolean getStrangeObject1()
    {
    	if(Inventory.contains(id.fremmy_strangeObject1)) return true;
    	if(!getBeerTankard()) return false;
    	if(Locations.fremmy_councilWorkman.contains(Players.localPlayer()))
    	{
    		if(Inventory.get(id.fremmy_beerTankard).useOn(NPCs.closest("Council workman")))
			{
				MethodProvider.log("Successfully used beer on workman :-)");
				MethodProvider.sleepUntil(() -> Inventory.contains(id.fremmy_strangeObject1),
						() -> Players.localPlayer().isMoving(),
						Sleep.calculate(2222, 2222),69);
			}
    		return false;
    	}
    	if(Locations.allFremennikProvinceSeersLighthouse.contains(Players.localPlayer()))
    	{
    		Walkz.walkToArea(Locations.fremmy_councilWorkman);
    		return false;
    	}
    	Walkz.teleportCamelot(180000);
    	return false;
    }
	public static boolean getEventRPG()
	{
		if(Equipment.contains(id.eventRPG)) return true; 
		if(!Equipment.contains(id.eventRPG) && !Inventory.contains(id.eventRPG))
		{
			if(Bank.contains(id.eventRPG))
			{
				InvEquip.withdrawOne(id.eventRPG, 180000);
				return false;
			}
		}
		if(Inventory.contains(id.eventRPG))
		{
			if(Shop.isOpen())
			{
				Shopz.close();
				Sleep.sleep(699,1111);
				return false;
			}
			InvEquip.equipItem(id.eventRPG);
			return false;
		}
		
		if(Inventory.count(InvEquip.coins) < 500)
		{
			InvEquip.clearAll();
			InvEquip.addInvyItem(InvEquip.coins, 500, 500, false, 0);
			InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
			InvEquip.fulfillSetup(true, 180000);
			return false;
		}
		if(Shop.isOpen())
		{
			if(Shop.purchaseOne(id.eventRPG))
			{
				MethodProvider.sleepUntil(() -> Inventory.contains(id.eventRPG), Sleep.calculate(2222,2222));
			}
			Sleep.sleep(111, 1111);
			return false;
		}
		if(Locations.diango.getCenter().distance() < 75)
		{
			API.walkInteractNPC("Diango", "Trade", Locations.diango,() -> Shop.isOpen());
			return false;
		}
		if(!Walkz.useJewelry(InvEquip.glory,"Draynor Village"))
		{
			API.walkInteractNPC("Diango", "Trade", Locations.diango,() -> Shop.isOpen());
			return false;
		}
		return false;
	}
	public static int getProgressValue()
	{
		return PlayerSettings.getConfig(347);
	}
	 public static enum Direction {
			SOUTH,
			SOUTHWEST,
			WEST,
			NORTHWEST,
			NORTH,
			NORTHEAST,
			EAST,
			SOUTHEAST
	    }
	 public static Direction currentDirection = null;
	 private static final Area swensenPuzzle = new Area(2624, 10044, 2675, 9996);
	 private static final Tile[] portalTiles = {
			 new Tile(2631,10002),
			 new Tile(2639,10015),
			 new Tile(2656,10004),
			 new Tile(2665,10018),
			 new Tile(2630,10023),
			 new Tile(2656,10037),
			 new Tile(2666,10029)
	 };
	 public static void initialize()
	 {
		 for(Tile t : portalTiles)
		 {
			 portalTilesList.add(t);
		 }
		 if((int) Calculations.nextGaussianRandom(100, 50) > 95) peerPuzzle1Left = true;
		 if((int) Calculations.nextGaussianRandom(100, 50) > 95) peerPuzzle2Left = true;
		 if((int) Calculations.nextGaussianRandom(100, 50) > 95) peerPuzzle3Left = true;
		 if((int) Calculations.nextGaussianRandom(100, 50) > 95) peerPuzzle4Left = true;
	 }
	 private static final List<Tile> portalTilesList = new ArrayList<Tile>();
	 public static void solveSwensenPuzzle()
	 {
		 if(!swensenPuzzle.contains(Players.localPlayer()))
		 {
			 MethodProvider.log("Outside of puzzle!");
			 return;
		 }
		 GameObject ladder = GameObjects.closest(g -> g!=null && !g.getTile().equals(new Tile(2631,10005)) && g.hasAction("Climb-up") && g.getName().contains("Ladder") && g.canReach());
		 if(ladder != null)
		 {
			 if(ladder.interact("Climb-up"))
			 {
				 MethodProvider.sleepUntil(() -> Locations.fremmy_swensen.contains(Players.localPlayer()), Sleep.calculate(5555, 3333));
			 }
			 return;
		 }
		 if(Players.localPlayer().isMoving() || Players.localPlayer().isAnimating()) 
		{
			 Sleep.sleep(2222, 2222);
			 return;
		}
		 GameObject portal = GameObjects.closest(g -> g!=null && 
				 g.hasAction("Use") && 
				 g.getName().contains("Portal") &&
				 g.canReach() && 
				 portalTilesList.contains(g.getTile()));
		 if(portal != null)
		 {
			 if(portal.interact("Use"))
			 {
				 Sleep.sleep(2222, 2222);
			 }
		 }
	 }
	 public static void findFightDraugen()
	 {
		 NPC draugen = NPCs.closest(n -> 
				 n!=null&&
				 n.getName().equals("The Draugen") &&
				 n.hasAction("Attack"));
		 if(draugen != null)
		 {
			 if(Combatz.shouldDrinkMeleeBoost()) Combatz.drinkMeleeBoost();
			 if(Combatz.shouldEatFood(20)) Combatz.eatFood();
			 if(!Players.localPlayer().isInCombat())
			 {
				 Sleep.sleep(2222, 2222);
				 if(!Players.localPlayer().isInCombat())
				 {
					if(draugen.interact("Action"))
					{
						MethodProvider.sleepUntil(() -> Players.localPlayer().isInCombat(),
								() -> Players.localPlayer().isMoving(),
								Sleep.calculate(2222, 2222),69);
					}
				 }
				 return;
			 }
			 return;
		 }
		 API.randomAFK(2);
		 if(!hoppedToRightDraugenWorld)
		 {
			 if(!Locations.fremmy_councilWorkman.contains(Players.localPlayer()))
			 {
				 if(Locations.fremmy_councilWorkman.getCenter().distance() > 250)
				 {
					 Walkz.teleportCamelot(180000);
					 return;
				 }
				 if(!Walkz.isStaminated()) Walkz.drinkStamina();
				 if(Walkz.isStaminated() && !Walking.isRunEnabled() && Walking.getRunEnergy() > 5) Walking.toggleRun();
				 Walkz.walkToArea(Locations.fremmy_councilWorkman);
				 return;
			 }
			 
			 if(currentDirection == null)
			 {
				 if(!Tabs.isOpen(Tab.INVENTORY))
				 {
					 Tabz.open(Tab.INVENTORY);
					 return;
				 }
				 if(Inventory.interact(id.fremmy_huntersTalisman, "Locate"))
				 {
					 MethodProvider.sleepUntil(() -> currentDirection != null, Sleep.calculate(2222, 2222));
				 }
				 return;
			 }
			 if(currentDirection == Direction.SOUTH || 
					 currentDirection == Direction.SOUTHEAST || 
					 currentDirection == Direction.SOUTHWEST)
			 {
				 hoppedToRightDraugenWorld = true;
				 return;
			 }
			 MissingAPI.scrollHopWorld(API.getRandomP2PWorld());
			 currentDirection = null;
			 return;
		 }
		 if(currentDirection == null)
		 {
			 if(Players.localPlayer().isMoving()) return;
			 if(!Tabs.isOpen(Tab.INVENTORY))
			 {
				 Tabz.open(Tab.INVENTORY);
				 return;
			 }
			 if(Inventory.interact(id.fremmy_huntersTalisman, "Locate"))
			 {
				 MethodProvider.sleepUntil(() -> currentDirection != null, Sleep.calculate(2222, 2222));
			 }
			 return;
		 }
		 Tile walkDirectionTile = null;
		 int xAdd = 0;
		 int yAdd = 0;
		 if(currentDirection == Direction.SOUTH) yAdd = 0 - ((int) Calculations.nextGaussianRandom(7, 2));
		 else if(currentDirection == Direction.EAST) xAdd = 0 + (int) Calculations.nextGaussianRandom(7, 2);
		 else if(currentDirection == Direction.NORTH) yAdd = (int) Calculations.nextGaussianRandom(7, 2);
		 else if(currentDirection == Direction.WEST) xAdd = 0 - ((int) Calculations.nextGaussianRandom(7, 2));
		 else if(currentDirection == Direction.SOUTHEAST) 
		{
			 yAdd = 0 - ((int) Calculations.nextGaussianRandom(5, 2));
			 xAdd = 0 + (int) Calculations.nextGaussianRandom(5, 2);
		}
		 else if(currentDirection == Direction.SOUTHWEST) 
			{
				 yAdd = 0 - ((int) Calculations.nextGaussianRandom(5, 2));
				 xAdd = 0 - ((int) Calculations.nextGaussianRandom(5, 2));
			}
		 else if(currentDirection == Direction.NORTHEAST) 
			{
				 yAdd = 0 + (int) Calculations.nextGaussianRandom(5, 2);
				 xAdd = 0 + (int) Calculations.nextGaussianRandom(5, 2);
			}
		 else if(currentDirection == Direction.NORTHWEST) 
			{
				 yAdd = 0 + (int) Calculations.nextGaussianRandom(5, 2);
				 xAdd = 0 - ((int) Calculations.nextGaussianRandom(5, 2));
			}
		 walkDirectionTile = new Tile((Players.localPlayer().getX() + xAdd),(Players.localPlayer().getY() + yAdd),0);
		 walkDirectionTile = Map.getWalkable(walkDirectionTile);
		 if(Walking.shouldWalk(6) && Walking.walk(walkDirectionTile)) 
		{
			 currentDirection = null;
			 Sleep.sleep(696, 666);
		 }
	 }
	public static boolean canPickupBeer = false;
	public static boolean participatedInKegContest = false;
	public static boolean talkedToOlafTwice = false;

	public static boolean filledLowAlcoholKeg = false;
	public static boolean hoppedToRightDraugenWorld = false;
	public static boolean canEnterSwensenMaze = false;
	public static boolean merchant_talkedSigmund1 = false;
	public static boolean merchant_talkedSailor1 = false;
	public static boolean merchant_talkedOlaf1 = false;
	public static boolean merchant_talkedYrsa1 = false;
	public static boolean merchant_talkedBrundt1 = false;
	public static boolean merchant_talkedSigli1 = false;
	public static boolean merchant_talkedSkulgrimen1 = false;
	public static boolean merchant_talkedFisherman1 = false;
	public static boolean merchant_talkedSwensen1 = false;
	public static boolean merchant_talkedPeerTheSeer1 = false;
	public static boolean merchant_talkedThorvald1 = false;
	public static boolean merchant_talkedManni1 = false;
	public static boolean merchant_talkedThora1 = false;
	public static boolean merchant_talkedAskeladden1 = false;
	public static boolean talkedToThorvaldForKoschei = false;
	public static boolean koschei4thForm = false;
	
	public static String riddleAnswer = null;
	public static boolean solvedRiddle = false;
	public static boolean peerPuzzle1Left = false;
	public static boolean peerPuzzle2Left = false;
	public static boolean peerPuzzle3Left = false;
	public static boolean peerPuzzle4Left = false;
	public static void solvePeerPuzzle()
	{
		if(solvedRiddle) return;
		if(Widgets.getWidgetChild(298,43) != null && 
			Widgets.getWidgetChild(298,43).isVisible())
		{
			WidgetChild w1 = Widgets.getWidgetChild(298,43);
			WidgetChild w2 = Widgets.getWidgetChild(298,44);
			WidgetChild w3 = Widgets.getWidgetChild(298,45);
			WidgetChild w4 = Widgets.getWidgetChild(298,46);
			WidgetChild w1Button;
			WidgetChild w2Button;
			WidgetChild w3Button;
			WidgetChild w4Button;
			if(peerPuzzle1Left) w1Button = Widgets.getWidgetChild(298,47);
			else w1Button = Widgets.getWidgetChild(298,48);
			if(peerPuzzle2Left) w2Button = Widgets.getWidgetChild(298,49);
			else w2Button = Widgets.getWidgetChild(298,50);
			if(peerPuzzle3Left) w3Button = Widgets.getWidgetChild(298,51);
			else w3Button = Widgets.getWidgetChild(298,52);
			if(peerPuzzle4Left) w4Button = Widgets.getWidgetChild(298,53);
			else w4Button = Widgets.getWidgetChild(298,54);
			String s1 = w1.getText().toLowerCase();
			String s2 = w2.getText().toLowerCase();
			String s3 = w3.getText().toLowerCase();
			String s4 = w4.getText().toLowerCase();
			if(s1.charAt(0) != riddleAnswer.charAt(0))
			{
				if(w1Button.interact())
				{
					Sleep.sleep(666, 696);
				}
				return;
			}
			if(s2.charAt(0) != riddleAnswer.charAt(1))
			{
				if(w2Button.interact())
				{
					Sleep.sleep(666, 696);
				}
				return;
			}
			if(s3.charAt(0) != riddleAnswer.charAt(2))
			{
				if(w3Button.interact())
				{
					Sleep.sleep(666, 696);
				}
				return;
			}
			if(s4.charAt(0) != riddleAnswer.charAt(3))
			{
				if(w4Button.interact())
				{
					Sleep.sleep(666, 696);
				}
				return;
			}
			if(Widgets.getWidgetChild(298,56).interact())
			{
				MethodProvider.log("Solved riddle: "+s1+s2+s3+s4);
				MethodProvider.sleepUntil(() -> solvedRiddle, Sleep.calculate(2222, 2222));
			}
			return;
		}
		API.walkInteractWithGameObject("Door", "Open", Locations.fremmy_peerLeftDoor);
	}
	
	 public static boolean handleDialogues()
		{
		 	boolean continueWait = false;
		 	if(Widgets.getWidgetChild(229, 1) != null && 
		 			Widgets.getWidgetChild(229,1).isVisible())
		 	{
		 		String dialogue = Widgets.getWidgetChild(229,1).getText();
		 		if(dialogue.contains("My first is in mage, but not in wizard"))
		 		{
		 			riddleAnswer = "mind";
		 		}
		 		if(dialogue.contains("My first is in tar, but not in a swamp"))
		 		{
		 			riddleAnswer = "tree";
		 		}
		 		if(dialogue.contains("My first is in the well, but not at sea"))
		 		{
		 			riddleAnswer = "life";
		 		}
		 		if(dialogue.contains("My first is in fish, but not in the sea"))
		 		{
		 			riddleAnswer = "fire";
		 		}
		 		if(dialogue.contains("My first is in water, and also in tea"))
		 		{
		 			riddleAnswer = "time";
		 		}
		 		if(dialogue.contains("My first is in wizard, but not in a mage"))
		 		{
		 			riddleAnswer = "wind";
		 		}
		 	}
		 	if(Dialogues.getNPCDialogue() != null && !Dialogues.getNPCDialogue().isEmpty())
		 	{
		 		String dialogue = Dialogues.getNPCDialogue();
		 		MethodProvider.log("NPC Dialogue: " + dialogue);
		 		if(dialogue.contains("As you wish outerlander; I will drink first, then you will"))
		 		{
		 			continueWait = true;
		 		}
		 		if(dialogue.contains("A maze? Is that all? Sure, it sounds simple") || 
		 				dialogue.contains("Hahahaha it is the most complex route I have ever") || 
		 				dialogue.contains("myself, and is one of the most fiendish complexity!"))
		 		{
		 			canEnterSwensenMaze = true;
		 		}
		 		if(dialogue.contains("(hic) I canna drink another drop! I alsho") || 
		 				dialogue.contains("I guessh I win then ouddaladder! (hic) Niche try,"))
		 		{
		 			participatedInKegContest = true;
		 		} 
		 		if(dialogue.contains("Okay. I don\'t think this will be too difficult. Any") || 
		 				dialogue.contains("town. A good merchant will find exactly what their") ||
		 				dialogue.contains("rare flower from across the sea, do you?") ||
		 				dialogue.contains("We are a very insular clan, so I would not expect you"))
		 		{
		 			merchant_talkedSigmund1 = true;
		 		} 
		 		if(dialogue.contains("That sounds like a fair deal to me, outerlander.") || 
		 				dialogue.contains("love ballad, do you?") || 
		 				dialogue.contains("Well, the only musician I know of in these parts would"))
		 		{
		 			merchant_talkedSailor1 = true;
		 		} 
		 		if(dialogue.contains("If you can find me a pair of sturdy boots to replace") || 
		 				dialogue.contains("some custom sturdy boots, do you?") || 
		 				dialogue.contains("I\'m sorry outerlander... If I did, I would not trouble"))
		 		{
		 			merchant_talkedOlaf1 = true;
		 		} 
		 		if(dialogue.contains("you must have the ear of the chieftain for him to") ||  
		 				dialogue.contains("guarantee of a reduction on sales taxes, do you?") || 
		 				dialogue.contains("I will make you a pair of sturdy boots for Olaf if you"))
		 		{
		 			merchant_talkedYrsa1 = true;
		 		} 
		 		if(dialogue.contains("Speak to Sigli then, and you may have my promise to") ||  
		 				dialogue.contains("map to unspoiled hunting grounds, do you?") || 
		 				dialogue.contains("Sigli the hunter is the only one who knows of such"))
		 		{
		 			merchant_talkedBrundt1 = true;
		 		}
		 		if(dialogue.contains("I have no idea. But then again, I\'m happy with my old") || 
		 				dialogue.contains("finely balanced custom bowstring, do you?"))
		 		{
		 			merchant_talkedSigli1 = true;
		 		} 
		 		if(dialogue.contains("You get me that fish, I give you the bowstring. What") ||
		 				dialogue.contains("an exotic and extremely rare fish, do you?"))
		 		{
		 			merchant_talkedSkulgrimen1 = true;
		 		} 
		 		if(dialogue.contains("By getting me his copy of that map, I will finally be self") ||
		 				dialogue.contains("map of deep sea fishing spots do you?"))
		 		{ 
		 			merchant_talkedFisherman1 = true;
		 		} 
		 		if(dialogue.contains("I just told you: from the Seer. You will need to") ||
		 				dialogue.contains("weather forecast from the Fremennik Seer do you?"))
		 		{
		 			merchant_talkedSwensen1 = true;
		 		} 
		 		if(dialogue.contains("Do not fret, outerlander; it is a fairly simple matter. I") ||
		 				dialogue.contains("brave and powerful warrior to act as a bodyguard?"))
		 		{ 
		 			merchant_talkedPeerTheSeer1 = true;
		 		} 
		 		if(dialogue.contains("Do not fret, outerlander; it is a fairly simple matter. I"))
		 		{ 
		 			merchant_talkedPeerTheSeer1 = true;
		 		} 
		 		if(dialogue.contains("If you can persuade one of the Revellers to give up") ||
		 				dialogue.contains("the token to allow your seat at the champions table?") ||
		 				dialogue.contains("token to allow a seat at the champions table, do you?"))
		 		{ 
		 			merchant_talkedThorvald1 = true;
		 		} 
		 		if(dialogue.contains("If you can persuade her to make me her legendary") ||
		 				dialogue.contains("the longhall barkeeps\' legendary cocktail, do you?"))
		 		{ 
		 			merchant_talkedManni1 = true;
		 		} 
		 		if(dialogue.contains("Knowing that little horror, he\'ll probably be willing to in")||
		 				dialogue.contains("written promise from Askeladden to stay out of the"))
		 		{ 
		 			merchant_talkedThora1 = true;
		 		} 
		 		if(dialogue.contains("Grab a keg of beer<br>from that table near the bar"))
		 		{
		 			canPickupBeer = true;
		 		}
		 		if(dialogue.contains("You may not enter the battleground with any armour") || 
		 				dialogue.contains("combat I do not trust completely. You must go down"))
		 		{
		 			talkedToThorvaldForKoschei = true;
		 		}
		 		if(dialogue.contains("longer! This time you lose your prayer however, and") || 
		 				dialogue.contains("You show some skill at combat... I will hold back no"))
		 		{ 
		 			koschei4thForm = true;
		 		} 
		 		
		 	}
			if(Dialogues.canContinue())
			{
				if(Dialogues.continueDialogue()) 
				{
					Sleep.sleep(420,696);
					if(continueWait) Sleep.sleep(5555, 4444);
				}
				return true;
			}
			if(Dialogues.isProcessing())
			{
				Sleep.sleep(420,696);
				return true;
			}
			 
			if(Dialogues.areOptionsAvailable())
			{
				if(Dialogues.chooseOption("I don\'t need a reminder"))
				{
					talkedToOlafTwice = true;
					return true;
				}
				if(riddleAnswer != null)
				{
					if(Dialogues.chooseOption("Solve the riddle"))
					{
						return true;
					}
				}
				return Dialogues.chooseOption("Why will no-one talk to me?") || 
						Dialogues.chooseOption("Yes.")|| 
						Dialogues.chooseOption("What\'s a Draugen?")|| 
						Dialogues.chooseOption("Read the riddle")|| 
						Dialogues.chooseOption("Yes.")|| 
						Dialogues.chooseOption("Present offering of a single fish.")|| 
						Dialogues.chooseOption("Raw shark.")|| 
						Dialogues.chooseOption("Talk about the Fremennik Trials")|| 
						Dialogues.chooseOption("What do I have to do again?")|| 
						Dialogues.chooseOption("Ask about the Merchant\'s trial")|| 
						Dialogues.chooseOption("Yes");
			}
			
			if(Dialogues.inDialogue()) Sleep.sleep(2222, 2222);
			return false;
		}
}
