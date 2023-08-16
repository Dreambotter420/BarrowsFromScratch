package script.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.depositbox.DepositBox;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

import script.skills.prayer.TrainPrayer;

public class Walkz {

	public static boolean isStaminated()
	{
		if(Walking.getRunEnergy() >= 80) return true;
		return PlayerSettings.getBitValue(25) != 0;
	}
	public static void drinkStamina()
	{
		if(InvEquip.invyContains(id.staminas))
		{
			if(Tabs.isOpen(Tab.INVENTORY))
			{
				if(Inventory.interact(InvEquip.getInvyItem(id.staminas),"Drink"))
				{
					Sleepz.sleep(69,69);
				}
				return;
			}
			
			Tabz.open(Tab.INVENTORY);
			
		}
	}
	
	public static boolean goToGE(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69,69);
			//check if already in good GE area
			if(Locations.clickableGEArea.contains(Players.getLocal())) return true;
			//check if within walkable area to GE
			if(Locations.dontTeleToGEAreaJustWalk.contains(Players.getLocal()))
			{
				if(Walking.shouldWalk(6) && Walking.walk(BankLocation.GRAND_EXCHANGE.getTile()))
				{
					Sleepz.sleep(666, 1111);
				}
				continue;
			}
			//check if have ring of wealth equipped, use it
			if(InvEquip.equipmentContains(InvEquip.wearableWealth))
			{
				if(Tabs.isOpen(Tab.EQUIPMENT))
				{
					if(Equipment.interact(EquipmentSlot.RING, "Grand Exchange"))
					{
						Sleep.sleepUntil(() -> Locations.teleGE.contains(Players.getLocal()), () -> Players.getLocal().isAnimating(),Sleepz.calculate(3333,2222),50);
					}
				}
				else
				{
					if(Widgets.isOpen()) Widgets.closeAll();
					Tabz.open(Tab.EQUIPMENT);
				}
				continue;
			}
		
			//check inventory for ring of wealth / tablet, use it
			boolean ringFound = false;
			for(int ring : InvEquip.wearableWealth)
			{
				if(Inventory.contains(ring))
				{
					InvEquip.equipItem(ring);
					ringFound = true;
					break;
				}
			}
			if(ringFound) continue;
			if(Inventory.contains(id.varrockTele))
			{
				if(Bank.isOpen()) Bankz.close();
				if(Bankz.closeBankEquipment())
				{
					teleportVarrock(30000);
				}
				continue;
			}
			
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//check bank for wealth / varrock tabs
			int ring = InvEquip.getFirstInBank(InvEquip.wearableWealth);
			
			//if no ring found in equip OR invy OR bank, check bank for varrock tab
			if(ring == -1) 
			{
				if(Bank.contains(id.varrockTele))
				{
					InvEquip.withdrawOne(id.varrockTele, timeout);
					continue;
				} 
				//no options left - go to GE by walking
				else if(Locations.isInKourend()) exitKourend(240000);
				else if(Walking.shouldWalk(6) && Walking.walk(Locations.GE)) Sleepz.sleep(666, 666);
			}
			//found item in bank - withdraw it
			else
			{
				InvEquip.withdrawOne(ring, timeout);
			}
		}
		
		return false;
	}
	
	public static boolean walkToForgottenSouls()
	{
		if(Locations.forgottenSoulsLvl3.contains(Players.getLocal())) return true;
		if(Locations.forgottenSoulsLvl1.contains(Players.getLocal()))
		{
			GameObject stairs = GameObjects.closest("Staircase");
			if(stairs == null)
			{
				Logger.log("Staircase is null in lvl 1 forgotten souls!");
				return false;
			}
			if(stairs.interact("Climb-up"))
			{
				Sleep.sleepUntil(() -> Locations.forgottenSoulsLvl2.contains(Players.getLocal()),
						() -> Players.getLocal().isMoving(),
						Sleepz.calculate(2222,2222), 50);
				Sleepz.sleep(420,696);
			}
			return false;
		}
		if(Locations.forgottenSoulsLvl2.contains(Players.getLocal()))
		{
			GameObject stairs = GameObjects.closest("Staircase");
			if(stairs == null)
			{
				Logger.log("Staircase is null in lvl 2 forgotten souls!");
				return false;
			}
			if(stairs.interact("Climb-up"))
			{
				Sleep.sleepUntil(() -> Locations.forgottenSoulsLvl2.contains(Players.getLocal()),
						() -> Players.getLocal().isMoving(),
						Sleepz.calculate(2222,2222), 50);
				Sleepz.sleep(420,696);
			}
			return false;
		}
		
		if(!walkPath(Paths.path_soulWarsLobbyToForgottenSouls))
		{
			if(!walkPath(Paths.path_otherSideOfIsleOfSoulsToForgottenSouls))
			{
				Logger.log(":-( cannot find valid path to forgotten souls on Isle of Souls!");
			}
		}
		return false;
	}
	public static boolean walkBackwardsPath(Tile[] path)
	{
		List<Tile> pathTiles = Arrays.asList(path);
		Collections.reverse(pathTiles);
		return walkPath((Tile[])pathTiles.toArray());
	}
	public static boolean walkPath(Tile[] path)
	{
		if(InvEquip.invyContains(id.staminas) && !isStaminated())
		{
			drinkStamina();
			Sleepz.sleep(69,696);
		}
		if(isStaminated() && !Walking.isRunEnabled() && Walking.getRunEnergy() > 5)
		{
			if(Walking.toggleRun()) Sleepz.sleep(696,666);
		}
		List<Tile> pathTiles = new ArrayList<Tile>();
		for(Tile t : path)
		{
			pathTiles.add(t);
		}
		Collections.reverse(pathTiles);
		for(Tile t : pathTiles)
		{
			if(Map.isTileOnMap(t))
			{
				if(Walking.shouldWalk(6))
				{
					if(Walking.walk(t))
					{
						Logger.log("Walked on path(regular walk)!");
						Sleepz.sleep(696,420);
					}
					else if(Walking.clickTileOnMinimap(t))
					{
						Logger.log("Walked on path (map)!");
						Sleepz.sleep(696,420);
					}
					else if(Map.interact(t,"Walk here"))
					{
						Logger.log("Walked here on path (screen)!");
						Sleepz.sleep(696,420);
					}
					else Logger.log("Missed path walk on valid tile");
				}
				return true;
			}
		}
		Logger.log("No tiles found to path to in path :-(");
		return false;
	}
	
	public static boolean walkToHASpot()
	{
		if(Locations.haSpot == Locations.HASpot1)
		{
			//this is the one in f2p walking from duel arena to that one spot south of it at altar 2nd floor
			if(Locations.HASpot1.contains(Players.getLocal()))
			{
				
			}
		}
		return false;
	}
	public static boolean exitLumbyCave()
	{
		if(!Locations.entireLumbyCave.contains(Players.getLocal())) return true;
		if(!Bankz.openClosest(50))
		{
			if(Locations.lumbyCaveFoyer.contains(Players.getLocal()))
			{
				GameObject climbingRope = GameObjects.closest(g -> g!=null && 
						g.getName().equals("Climbing rope") && 
						g.hasAction("Climb"));
				if(climbingRope == null)
				{
					Logger.log("climbing rope null in lumby cave foyer!");
					return false;
				}
				if(climbingRope.interact("Climb"))
				{
					Sleepz.sleep(696,420);
				}
				return false;
			}
			if(Locations.caveHandSkipTile2.equals(Players.getLocal().getTile()))
			{
				if(!Walking.isRunEnabled())
				{
					if(Walking.toggleRun()) Sleepz.sleep(420, 696);
					return false;
				}
				if(Walking.walkExact(Locations.caveHandSkipTile))
				{
					Sleepz.sleep(420, 696);
				}
				return false;
			}
			if(Walking.shouldWalk(6) && Walking.walk(Locations.caveHandSkipTile2)) Sleepz.sleep(420, 696);
		}
		return false;
	}
	public static boolean enterDesert()
	{
		Timer timeout = new Timer(180000);
		while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Logger.log("Inside enter desert loop");
			
				
			Sleepz.sleep(69,420);
			if(Locations.shantayPassSouthSide.contains(Players.getLocal())) 
			{
				Logger.log("Exiting enter desert loop - in south side of shantay pass!");
				return true;
			}
			if(Locations.shantayPassArea.contains(Players.getLocal()))
			{
				//drop all useless junk
				if(Inventory.dropAll(i -> 
						i != null &&
						(i.getID() == InvEquip.waterskin0 || 
						i.getID() == InvEquip.waterskin1 || 
						i.getID() == InvEquip.waterskin2 || 
						i.getID() == InvEquip.waterskin3 || 
						i.getID() == id.jug))) 
				{
					Sleepz.sleepInteraction();
					continue;
				}
					
				if(Inventory.count(InvEquip.waterskin4) >= 5 && 
						Inventory.count(InvEquip.shantayPass) >= 1)
				{
					if(Widgets.get(565,17) != null && 
							Widgets.get(565,17).isVisible() && 
							Widgets.get(565,17).getText().contains("Proceed regardless"))
					{
						if(Widgets.get(565,17).interact("Yes"))
						{
							Sleep.sleepUntil(() -> Locations.shantayPassSouthSide.contains(Players.getLocal()), Sleepz.calculate(2222,2222));
						}
						continue;
					}
					
					GameObject shantayPass = GameObjects.closest(f -> f!=null && f.getName().contains("Shantay pass") && f.hasAction("Go-through"));
					if(shantayPass == null)
					{
						Logger.log("Shantay pass null in shantay pass area!");
						Sleepz.sleepInteraction();
						continue;
					}
					if(shantayPass.interact("Go-through"))
					{
						Sleep.sleepUntil(() -> Locations.shantayPassSouthSide.contains(Players.getLocal()) || 
								(Widgets.get(565,17) != null && 
										Widgets.get(565,17).isVisible() && 
										Widgets.get(565,17).getText().contains("Proceed regardless")),
								() -> Players.getLocal().isMoving(),
								Sleepz.calculate(2222, 2222),50);
					}
					Sleepz.sleepInteraction();
					continue;
				}
				
				if(!InvEquip.checkedBank()) continue;
				
				int neededWaterskins = 0;
				int neededPasses = 0;
				if(Inventory.count(InvEquip.shantayPass) <= 0) neededPasses = 1;
				if(Inventory.count(InvEquip.waterskin4) < 5) neededWaterskins = (5 - Inventory.count(InvEquip.waterskin4));
				
				final int neededInvySlots = neededPasses + neededWaterskins;
				final int emptySlots = Inventory.getEmptySlots();
				if(emptySlots < neededInvySlots)
				{
					Logger.log("Not enuf invy spaces for waterskins + shantay pass");
					if(Bank.isOpen())
					{
						if(Bank.depositAll(i -> i != null && 
								ItemsOnGround.allSlayerLoot.contains(i.getID())))
						{
							Logger.log("Deposited some allSlayerLoot");
						}
						else 
						{
							Logger.log("Did not deposit any allSlayerLoot");
							if(Bank.deposit(i -> i != null && 
									i.getID() == id.jugOfWine, (neededInvySlots - emptySlots)))
							{
								Logger.log("Deposited "+(neededInvySlots - emptySlots)+" jugs of wine");
							}
							else 
							{
								Logger.log("Did not deposit any jugs of wine");
							}
						}
							
						Sleepz.sleepInteraction();
						continue;
					}
					if(Bankz.openClosestNoJewelry()) Sleepz.sleep();
					continue;
				}
				
				if((neededPasses > 0 && Bank.count(InvEquip.shantayPass) > 0) || 
						(neededWaterskins > 0 && Bank.count(InvEquip.waterskin4) > 0)) 
				{
					if(Bank.isOpen())
					{
						if(neededPasses > 0) 
						{
							if(Bank.withdraw(i -> i != null && 
									i.getID() == InvEquip.shantayPass, neededPasses)) 
							{
								Sleepz.sleepInteraction();
							}
						}
						if(neededWaterskins > 0) 
						{
							if(Bank.withdraw(i -> i != null && 
									i.getID() == InvEquip.waterskin4, neededWaterskins)) 
							{
								Sleepz.sleepInteraction();
							}
						}
						continue;
					}
					if(Bankz.openClosestNoJewelry()) Sleepz.sleep();
					continue;
				}
				
				if(Inventory.count(InvEquip.coins) >= 2000)
				{
					if(Bank.isOpen()) 
					{
						if(Bankz.close())
						{
							Sleepz.sleepInteraction();
						}
						continue;
					}
					if(Shop.isOpen())
					{
						if(neededPasses > 0)
						{
							if(Shop.interact(i -> i != null && 
									i.getID() == InvEquip.shantayPass, "Buy 50"))
							{
								Logger.log("Bought 50 passes from Shantay!");
								Sleepz.sleep();
							}
							continue;
						}
						if(neededWaterskins > 0)
						{
							if(Shop.interact(i -> i != null && 
									i.getID() == InvEquip.waterskin4, "Buy 50"))
							{
								Logger.log("Filled invy with "+emptySlots+" waterskin(4) from Shantay!");
								Sleepz.sleep();
							}
							continue;
						}
					}
					
					NPC shantay = NPCs.closest(s -> s != null && 
							s.getName().equals("Shantay") && 
							s.hasAction("Trade"));
					if(shantay == null)
					{
						Logger.log("Shantay null inside shantay pass area! :-(");
						continue;
					}
					if(shantay.interact("Trade"))
					{
						Logger.log("Interacted -trade- to -shantay-");
						Sleep.sleepUntil(Shop::isOpen, () -> Players.getLocal().isMoving(),
								Sleepz.calculate(2222, 2222),50);
						continue;
					}
					
					Sleepz.sleep();
					continue;
				}
				
				if(Bank.isOpen())
				{
					if(Bank.count(InvEquip.coins) < 5000) 
					{
						Logger.log("Not enough coins (5000) for buying shantay pass / waterskins to enter desert!");
						ScriptManager.getScriptManager().stop();
						return false;
					}
					if(Bank.withdraw(InvEquip.coins, 5000))
					{
						Logger.log("Withdrew 5000 coins!");
						Sleepz.sleepInteraction();
					}
					continue;
				}
				if(Bankz.openClosestNoJewelry()) Sleepz.sleep();
				continue;
			}
			
			if(Locations.shantayPassArea.getCenter().distance() >= 150)
			{
				if(!Walkz.useJewelry(InvEquip.glory,"Al Kharid"))
				{
					if(!Walkz.useJewelry(InvEquip.duel, "PvP Arena"))
					{
						Logger.log("Walking to Al-Karid ... ");
						if(Walking.shouldWalk(6) && Walking.walk(BankLocation.SHANTAY_PASS)) Sleepz.sleep(420,696);
					}
				}
				continue;
			}
			if(Walking.shouldWalk(6) && Walking.walk(BankLocation.SHANTAY_PASS)) Sleepz.sleep(420,696);
		}
		return false;
	}
	public static boolean exitDesert()
	{
		Timer timeout = new Timer(180000);
		while(!timeout.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			if(!Locations.isInDesert())
			{
				Logger.log("Exited desert!");
				return true;
			}
			if(Locations.isInDesert())
			{
				if(InvEquip.getWaterskinCharges() <= 2)
				{
					if(Walkz.useJewelry(InvEquip.glory, "Al Kharid")) continue;
					if(Walkz.useJewelry(InvEquip.duel, "PvP Arena")) continue;
					
					if(Locations.carpetAreaShantay.contains(Players.getLocal()))
					{
						GameObject shantayPass = GameObjects.closest(f -> f!=null && f.getName().contains("Shantay pass") && f.hasAction("Go-through"));
						if(shantayPass == null)
						{
							Logger.log("Shantay pass null in carpet guy area!");
							Sleepz.sleepInteraction();
							continue;
						}
						if(shantayPass.interact("Go-through"))
						{
							Sleep.sleepUntil(() -> Locations.shantayPassArea.contains(Players.getLocal()),
									() -> Players.getLocal().isMoving(),
									Sleepz.calculate(2222, 2222),50);
						}
						Sleepz.sleepInteraction();
						continue;
					}
					
					if(Walking.shouldWalk(6) && Walking.walk(Locations.carpetAreaShantay.getCenter())) Sleepz.sleep(696, 420);
					Sleepz.sleepInteraction();
					continue;
				}
				
				Sleepz.sleepInteraction();
			}
		}
		return false;
	}
	public static boolean exitKourend(long timeout)
	{
		Timer timer = new Timer(timeout);
		String veos = "Veos";
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69,69);
			if(Locations.isInKourend() || Locations.isInDestinationShip())
			{
				if(Locations.isInDestinationShip()) 
				{
					Locations.leaveDestinationShip();
					Sleepz.sleep();
					continue;
				}
				if(useJewelry(InvEquip.wealth,"Grand Exchange")) continue;
				if(useJewelry(InvEquip.glory,"Edgeville")) continue;
				if(useJewelry(InvEquip.combat,"Ranging Guild")) continue;
				if(useJewelry(InvEquip.skills,"Fishing Guild")) continue;
				if(useJewelry(InvEquip.duel,"Castle Wars")) continue;
				if(useJewelry(InvEquip.passage,"Wizards\' Tower")) continue;
				if(useJewelry(InvEquip.games,"Barbarian Outpost")) continue;
				if(Locations.kourendGiantsCaveArea.contains(Players.getLocal()))
				{
					exitGiantsCave();
					continue;
				}
				NPC closestVeos = NPCs.closest(veos);
				if(closestVeos != null)
				{
					if(closestVeos.interact("Port Sarim"))
					{
						Sleep.sleepUntil(() -> Locations.shipSarimVeos.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(), 
								Sleepz.calculate(8888, 5555),50);
					} else if(Walking.shouldWalk(5) && Walking.walk(closestVeos)) Sleepz.sleep(69, 420);
					continue;
				}
				if(Walking.shouldWalk(6) && Walking.walk(Locations.veosLandsEnd.getCenter())) 
				{
					Logger.log("Walking to Veos: "+Locations.veosLandsEnd.getCenter().toString());
					Sleepz.sleep(111, 1111);
				}
			}
			else return true;
		}
		return false;
	}
	public static boolean exitIsleOfSouls(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69,69);
			if(Locations.isleOfSouls.contains(Players.getLocal()))
			{
				if(useJewelry(InvEquip.wealth,"Grand Exchange")) continue;
				if(useJewelry(InvEquip.glory,"Edgeville")) continue;
				if(useJewelry(InvEquip.combat,"Ranging Guild")) continue;
				if(useJewelry(InvEquip.skills,"Fishing Guild")) continue;
				if(useJewelry(InvEquip.duel,"Castle Wars")) continue;
				if(useJewelry(InvEquip.passage,"Wizards\' Tower")) continue;
				if(useJewelry(InvEquip.games,"Barbarian Outpost")) continue;
				
				GameObject portal = GameObjects.closest(p -> p!=null && p.getName().equals("Portal") && p.hasAction("Edgeville"));
				if(portal != null)
				{
					if(portal.distance() < 10 && portal.interact("Edgeville"))
					{
						Sleep.sleepUntil(() -> Locations.edgevilleSoulsPortal.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(), 
								Sleepz.calculate(5555, 3333),50);
						continue;
					}
				}
				if(!Walkz.walkBackwardsPath(Paths.soulWarsLobbyToSandcrabs) && 
							!Walkz.walkBackwardsPath(Paths.path_soulWarsLobbyToForgottenSouls) && 
							!Walkz.walkBackwardsPath(Paths.path_otherSideOfIsleOfSoulsToForgottenSouls))
				{
					Logger.log("Appear to be stuck on isle of souls, teleporting home??");
					if(Players.getLocal().isAnimating()) 
					{
						Sleepz.sleep(2222, 2222);
						return false;
					}
					Magic.castSpell(Normal.HOME_TELEPORT);
					Sleepz.sleep(3333,3333);
				}
			}
			else return true;
		}
		return false;
	}
	public static boolean exitGiantsCave()
	{
		if(Locations.kourendGiantsCaveArea.contains(Players.getLocal()))
		{
			Filter<GameObject> caveFilter = c -> 
			c != null && 
			c.exists() && 
			c.getName().contains("Cave") && 
			c.hasAction("Exit");
			GameObject cave = GameObjects.closest(caveFilter);
			if(cave != null)
			{
				if(cave.interact("Exit"))
				{
					Sleep.sleepUntil(() -> Locations.kourendGiantsCaveEntrance.contains(Players.getLocal()),
							() -> Players.getLocal().isMoving(),Sleepz.calculate(2222,2222), 50);
				} else if(Walking.shouldWalk(6) && Walking.walk(cave))
				Sleepz.sleep();
			}
			return false;
		}
		return true;
	}
	/**
	 * returns true if have jewelry in invy or equipment. If in invy, equips and then teleports
	 * both to avoid having to handle tele menu interface, and to ensure it teleports in combat.
	 * DOES NOT CHECK BANK FOR JEWELRY! Returns false if no jewelry specified in invy or equipment.
	 * @param jewelry
	 * @param teleName
	 * @return
	 */
	public static boolean useJewelry(int jewelry, String teleName)
	{
		List<Integer> wearableJewelry = new ArrayList<Integer>();
		EquipmentSlot equipSlot = null;
		if(jewelry == InvEquip.wealth) 
			{
			equipSlot = EquipmentSlot.RING;
			wearableJewelry = InvEquip.wearableWealth;
			}
		if(jewelry == InvEquip.glory) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableGlory;
			}
		if(jewelry == InvEquip.combat) 
			{
			equipSlot = EquipmentSlot.HANDS;
			wearableJewelry = InvEquip.wearableCombats;
			}
		if(jewelry == InvEquip.skills) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableSkills;
			}
		if(jewelry == InvEquip.games) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableGames;
			}
		if(jewelry == InvEquip.duel) 
			{
			equipSlot = EquipmentSlot.RING;
			wearableJewelry = InvEquip.wearableDuel;
			}
		if(jewelry == InvEquip.passage) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearablePassages;
			}
		if(InvEquip.invyContains(wearableJewelry))
		{
			final int jewelryID = InvEquip.getInvyItem(wearableJewelry);
			InvEquip.equipItem(jewelryID);
			Sleep.sleepUntil(() -> Equipment.contains(jewelryID), Sleepz.calculate(4444, 3333));
		}
		if(InvEquip.equipmentContains(wearableJewelry))
		{
			if(Shop.isOpen()) Shop.close();
			else if(Bank.isOpen()) Bankz.close();
			else if(GrandExchange.isOpen()) GrandExchangg.close();
			else if(DepositBox.isOpen()) DepositBox.close();
			Tabz.open(Tab.EQUIPMENT);
			Sleep.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT),Sleepz.calculate(3333, 2222));
			if(Tabs.isOpen(Tab.EQUIPMENT))
			{
				if(Equipment.interact(equipSlot, teleName))
				{
					Logger.log("Just used Jewelry teleport: " + teleName +" in slot: " + equipSlot);
					Sleep.sleepUntil(() -> Players.getLocal().isAnimating(),Sleepz.calculate(4444,4444));
					Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 
							() -> Players.getLocal().isAnimating(), Sleepz.calculate(3333,2222),69);
					Sleepz.sleep(111,1111);
				}
			}
			return true;
		}
		
		return false;
	}
	public static boolean goToCastleWars(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69,69);
			//check if already in good castle wars area
			if(Locations.castleWars.contains(Players.getLocal())) return true;
			//check if within reasonable walking distance
			double dist = Locations.castleWars.getCenter().distance(Players.getLocal().getTile());
			//if have ring of dueling already equipped, use it
			boolean ringFound = false;
			if(dist > 100)
			{
				if(useJewelry(InvEquip.duel,"Castle Wars")) continue;
			}
			if(dist <= 100)
			{
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.castleWars.getCenter()))
				{
					Logger.log("Distance to castle wars is: " + dist + "... walking...");
					Sleepz.sleep(666, 1111);
				}
				continue;
			}
			//check if have ring of dueling equipped or in invy, use it
			if(useJewelry(InvEquip.duel,"Castle Wars")) continue;
		
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//check bank for dueling rings
			int ring = 0;
			for(int ring2 : InvEquip.wearableDuel)
			{
				if(Bank.contains(ring2)) 
				{
					ringFound = true;
					ring = ring2;
					break;
				}	
			}
			
			//if no ring found in invy OR bank, buy at GE
			if(!ringFound) InvEquip.buyItem(InvEquip.duel8, 1, timeout);
			
			//found item in bank - withdraw it
			else
			{
				InvEquip.withdrawOne(ring, timeout);
			}
		}
		
		return false;
	}
	public static boolean teleportDraynor(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69,69);
			//check if already in good draynor teleport area
			double dist = Locations.draynorTeleSpot.distance();
			if(dist <= 4) return true;
			//check if within reasonable walking distance
			if(dist < 25) 
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.draynorTeleSpot))
				{
					Logger.log("Distance to draynor tele spot is: " + dist + "... walking...");
					Sleepz.sleep(666, 1111);
				}
				continue;
			}
			//if have glory already equipped, use it
			if(dist > 75)
			{
				if(useJewelry(InvEquip.glory,"Draynor Village")) continue;
			}
			if(dist <= 75)
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.draynorTeleSpot))
				{
					Logger.log("Distance to draynorTeleSpot is: " + dist + "... walking...");
					Sleepz.sleep(666, 1111);
				}
				continue;
			}
			//check if have ring of dueling equipped, use it
			if(useJewelry(InvEquip.glory,"Draynor Village")) continue;
		
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//check bank for glory
			int jewelry = InvEquip.getBankItem(InvEquip.wearableGlory);
			
			//if no ring found in invy OR bank, buy at GE
			if(jewelry == 0) 
			{
				InvEquip.buyItem(InvEquip.glory6, 1, timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
				
			
			//found item in bank - withdraw it
			else InvEquip.withdrawOne(jewelry, timeout);
		}
		
		return false;
	}
	public static boolean teleportWoodcuttingGuild(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69,69);
			if(!Locations.unlockedKourend)
			{
				Logger.log("Exiting woodcutting tele loop, do not have kourend unlocked");
				break;
			}
			Logger.log("In teleportWoodcuttingGuild loop");
			//check if already in good draynor teleport area
			double dist = Locations.woodcuttingGuildTeleSpot.getCenter().distance(Players.getLocal().getTile());
			if(dist <= 10) return true;
			//check if within reasonable walking distance
			if(useJewelry(InvEquip.skills,"Woodcutting Guild")) continue;
			if(!InvEquip.checkedBank()) continue;
			InvEquip.clearAll();
			InvEquip.addInvyItem(InvEquip.skills6,1,1,false,1);
			if(!InvEquip.bankContains(InvEquip.wearableWealth) && !InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth)) InvEquip.addInvyItem(InvEquip.wealth5,1,1,false,3);
			InvEquip.fulfillSetup(false, 180000);
		}
		
		return false;
	}
	public static boolean goToCamelotTrees(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			Sleepz.sleep(69,69);
			//check if already in good area
			if(Locations.camelotTrees.contains(Players.getLocal())) return true;
			//check if within reasonable walking distance
			double dist = Locations.camelotTrees.getCenter().distance(Players.getLocal().getTile());
			
			if(dist <= 150)
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.camelotTrees.getCenter()))
				{
					Logger.log("Distance to camelot trees area is: " + dist + "... walking...");
					Sleepz.sleep(666, 1111);
				}
				continue;
			}
			//check if have cammy tab in invy, use it
			if(Inventory.contains(id.cammyTele))
			{
				if(Bank.isOpen()) Bankz.close();
				else if(Inventory.interact(id.cammyTele, "Break"))
				{
					Sleep.sleepUntil(() -> Locations.camelotTrees.contains(Players.getLocal()), () -> Players.getLocal().isAnimating(), Sleepz.calculate(4444,2222),50);
				}
				continue;
			}
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//if no ring found in invy OR bank, buy at GE
			if(!Bank.contains(id.cammyTele)) 
			{
				InvEquip.buyItem(id.cammyTele, 1, timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
			//found item in bank - withdraw it
			else
			{
				InvEquip.withdrawOne(id.cammyTele, timeout);
			}
		}
		
		return false;
	}
	/**
	 * Returns true if in telespot, false otherwise. Tries to teleport using tab, withdraws or buys more.
	 * @param tabID
	 * @param teleSpot
	 * @param timeout
	 * @return
	 */
	public static boolean teleport(int tabID, Area teleSpot, long timeout)
	{
		Logger.log("Entering generic teletab function");
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			if(teleSpot.contains(Players.getLocal())) return true;
			
			Sleepz.sleep(69,69);
			
			//check if have fally tab in invy, use it
			if(Inventory.contains(tabID))
			{
				if(Bank.isOpen()) Bankz.close();
				if(Tabz.open(Tab.INVENTORY))
				{
					if(Inventory.interact(tabID, "Break"))
					{
						Sleep.sleepUntil(() -> teleSpot.contains(Players.getLocal()), () -> Players.getLocal().isAnimating(), Sleepz.calculate(4444,2222),50);
					}
				}
				continue;
			}
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//if none found in invy OR bank, stop
			if(!Bank.contains(tabID)) 
			{
				InvEquip.buyItem(tabID, (int) Calculations.nextGaussianRandom(12,3),timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
			
			//found item in bank - withdraw it
			else InvEquip.withdrawOne(tabID, timeout);
		}
		return false;
	}
	public static boolean teleportFalador(long timeout)
	{
		return teleport(id.fallyTele,Locations.fallyTeleSpot,timeout);
	}
	public static boolean teleportOutsideHouse(long timeout)
	{
		Timer timer = new Timer(timeout);
		while(!timer.finished() && Client.getGameState() == GameState.LOGGED_IN
				&& ScriptManager.getScriptManager().isRunning() && !ScriptManager.getScriptManager().isPaused())
		{
			if(Locations.isInstanced() || Locations.houseTeleSpot.contains(Players.getLocal())) return true;
			if(!Locations.unlockedHouse)
			{
				TrainPrayer.unlockHouse();
				return false;
			}
			Sleepz.sleep(69,69);
			final double dist = Locations.houseTeleSpot.getCenter().distance(Players.getLocal().getTile());
			if(dist <= 30)
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.houseTeleSpot.getCenter()))
				{
					Logger.log("Distance to tele area is: " + dist + "... walking...");
					Sleepz.sleep(666, 1111);
				}
				continue;
			}
			//check if have fally tab in invy, use it
			if(Inventory.contains(id.houseTele))
			{
				if(Bank.isOpen()) Bankz.close();
				else if(Inventory.interact(id.houseTele, "Outside"))
				{
					Sleep.sleepUntil(() -> Locations.isInstanced() || Locations.houseTeleSpot.contains(Players.getLocal()), () -> Players.getLocal().isAnimating(), Sleepz.calculate(4444,2222),50);
				}
				continue;
			}
			//check if we have opened bank one time since script start
			if(!InvEquip.checkedBank()) continue;
			
			//if none found in invy OR bank, stop
			if(!Bank.contains(id.houseTele)) 
			{
				InvEquip.buyItem(id.houseTele, (int) Calculations.nextGaussianRandom(12,5),timeout);
				if(!InvEquip.bankContains(InvEquip.wearableWealth)) InvEquip.buyItem(InvEquip.wealth5, 3, timeout);
			}
			
			//found item in bank - withdraw it
			else InvEquip.withdrawOne(id.houseTele, timeout);
		}
		return false;
	}
	public static boolean teleportVarrock(long timeout)
	{
		return teleport(id.varrockTele,Locations.varrockTeleSpot,timeout);
	}
	public static boolean teleportLumbridge(long timeout)
	{
		return teleport(id.lumbridgeTele,Locations.lumbridgeTeleSpot,timeout);
	}
	public static boolean teleportCamelot(long timeout)
	{
		return teleport(id.cammyTele,Locations.cammyTeleSpot,timeout);
	}
	public static boolean walkToArea(Area area,Tile walkableTile)
	{
		if(area.contains(Players.getLocal())) return true;
		if(Walking.shouldWalk(6)) {
			Sleepz.sleep();
			if(Walking.walk(walkableTile)) Sleepz.sleepInteraction();
		}
		return area.contains(Players.getLocal());
	}
	public static boolean walkToArea(Area area) {
		return walkToArea(area, area.getCenter());
	}
	public static boolean walkToTileInRadius(Tile walkableTile,int radius) {
		Area area = walkableTile.getArea(radius);
		if(area.contains(Players.getLocal())) return true;
		if(!Walking.isRunEnabled() &&
				Walking.getRunEnergy() > Sleepz.calculate(15, 20)) 
		{
			if(Walking.toggleRun()) Sleepz.sleep(69, 111);
		}
		if(Walking.shouldWalk())
		{
			if(Walking.walk(walkableTile)) Sleepz.sleep(666, 1111);
		}
		return area.contains(Players.getLocal());
	}
	public static boolean turnOnRun()
	{
		if(Walking.isRunEnabled()) return true;
		else if(Walking.getRunEnergy() > Sleepz.calculate(15, 20) && Walking.toggleRun()) return true;
		return false;
	}
	public static boolean turnOffRun()
	{
		if(!Walking.isRunEnabled()) return true;
		else if(Walking.toggleRun()) return true;
		return false;
	}
	
	public static boolean walkToEntityInArea(String thingName,Area area)
	{
		if(area.contains(Players.getLocal()))
		{
			//first check NPCs
			NPC npc = NPCs.closest(thingName);
			if(npc != null && npc.exists())
			{
				if(npc.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(npc.getTile()))
				{
					Sleepz.sleep(666, 1111);
				}
				return false;
			}
			//next GameObjects
			GameObject object = GameObjects.closest(thingName);
			if(object != null && object.exists())
			{
				if(object.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(object.getTile()))
				{
					Sleepz.sleep(666, 1111);
				}
				return false;
			}
			//next GroundItems
			GroundItem item = GroundItems.closest(thingName);
			if(item != null && item.exists())
			{
				if(item.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(item.getTile()))
				{
					Sleepz.sleep(666, 1111);
				}
				return false;
			}
		}
		else if(Walking.shouldWalk() && Walking.walk(area.getRandomTile()))
		{
			Sleepz.sleep(666, 1111);
		}
		return false;
	}
	
}
