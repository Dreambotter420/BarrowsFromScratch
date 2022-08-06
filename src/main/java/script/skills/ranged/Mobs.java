package script.skills.ranged;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.CombatStyle;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.Mobs.Mob;
import script.skills.slayer.Candlez;
import script.skills.slayer.SlayerSettings;
import script.skills.slayer.TrainSlayer;
import script.utilities.API;
import script.utilities.API.modes;
import script.utilities.Bankz;
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.ItemsOnGround;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

public class Mobs {
	public static Mob mob = null;
	public static enum Mob {
		BOAR,
		HILL_GIANT
	}
	public static boolean cantReachThat = false;
	public static void chooseMob()
	{
		if(TrainRanged.ranged < 30) mob = Mob.BOAR;
		else mob = Mob.HILL_GIANT;
	}
	public static int trainMob(Mob mob)
	{
		if(Mobs.mob == Mob.BOAR)
    	{
    		return trainBoars();
    	} 
		else if(Mobs.mob == Mob.HILL_GIANT)
		{
			return trainHillGiants();
		} 
		else 
		{
			MethodProvider.log("[SCRIPT] -> No mob selected in trainMob function!");
			return Timing.sleepLogNormalSleep();
		}
	}
	public static int trainBoars()
	{
		Locations.isInKourend();
		if(!Locations.unlockedKourend) //fuck config value 414 we assume unlocked unless get teh game msg
		{
			//handle dialogue for Veos traveling to Kourend
			if(Dialogues.inDialogue()) 
			{
				if(Dialogues.canContinue()) Dialogues.continueDialogue();
				else if(Dialogues.areOptionsAvailable()) Dialogues.chooseFirstOptionContaining("That\'s great, can you take me there please?");
    			return Timing.sleepLogNormalSleep();
			}
			
			//search for Veos
			NPC veos = NPCs.closest("Veos");
			if(veos != null)
			{
				if(!Players.localPlayer().isInteracting(veos))
	    		{
	        		if(veos.interact("Talk-to"))
        			{
        				MethodProvider.sleepUntil(Dialogues::inDialogue, () -> Players.localPlayer().isMoving(), Sleep.calculate(2222,2222), 50);
        			} else if(!Players.localPlayer().isMoving() && 
        					Walking.shouldWalk(6) && 
        					Walking.walk(veos)) Sleep.sleep(69,696);
	    		}
				return Timing.sleepLogNormalSleep();
			}
	    	else
	    	{
	    		//no veos here
	    		if(Locations.veosSarim.getCenter().distance(Players.localPlayer().getTile()) > 75) Walkz.teleportDraynor(180000);
    			else if(Walking.shouldWalk(6) && Walking.walk(Locations.veosSarim.getCenter())) Sleep.sleep(420,666);
	    		
	    	}
			return Timing.sleepLogNormalSleep();
		}
		else
		{
			//can travel to kourend now
			
			//check invy for best darts, equip 
			if(Inventory.count(TrainRanged.getBestDart()) > 0) InvEquip.equipItem(TrainRanged.getBestDart());
			
			//check invy + equip for best darts, if have, do more checks, if not, fulfill setup
			if(Inventory.count(TrainRanged.getBestDart()) > 0 || Equipment.count(TrainRanged.getBestDart()) > 0)
			{
				
				//have some food in invy
				if(Combat.getFood() != null)
				{
					//should eat at 1 max hit
					if(Combat.shouldEatFood(2))
					{
						Combat.eatFood();
					}
				}
				else 
				{
					//no more food, fulfill setup
					
					TrainRanged.fulfillRangedDarts();
					return Timing.sleepLogNormalSleep();
				}
				
				//check for proper ranged lvl boost
				if(TrainRanged.shouldDrinkBoost())
				{
					//if have any ranged pots while needing a drink, wait until in killingzone to sip up
					if(InvEquip.invyContains(TrainRanged.rangedPots))
					{
						if(Locations.boarZone.contains(Players.localPlayer()))
						{
							if(!TrainRanged.drankRangedPotion()) MethodProvider.log("Not drank ranged Potion");
						}
					}
					else 
					{
						//need a ranged pot to drink but have none
						TrainRanged.fulfillRangedDarts();
						return Timing.sleepLogNormalSleep();
					}
				}
				
				//check for glory in invy + not equipped -> equip it
				if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
						!InvEquip.equipmentContains(InvEquip.wearableGlory)) InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
				
				//in location to kill mobs
				if(Locations.boarZone.contains(Players.localPlayer()))
    			{
					Mobs.fightMobRanged("Boar", Locations.boarZone, 1);
					return Timing.sleepLogNormalSleep();
    			}
				else if(Locations.isInKourend())
				{
					if(Bank.isOpen()) Bank.close();
					if(Locations.kourendGiantsCaveArea.contains(Players.localPlayer()))
					{
						if(!Walkz.exitGiantsCave()) Sleep.sleep(420,696);
						return Timing.sleepLogNormalSleep();
					}
					if(Locations.dreambotFuckedShayzien3.contains(Players.localPlayer()))
					{
						if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedShayzienDest3)) Sleep.sleep(69, 420);
						return Timing.sleepLogNormalSleep();
					}
					if(Locations.dreambotFuckedShayzien2.contains(Players.localPlayer()))
					{
						
						if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedShayzienDest2)) Sleep.sleep(69, 420);
						return Timing.sleepLogNormalSleep();
					}
					if(Locations.dreambotFuckedShayzien.contains(Players.localPlayer()))
					{
						
						if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedShayzienDest)) Sleep.sleep(69, 420);
						return Timing.sleepLogNormalSleep();
					}
					if(Walking.shouldWalk(6) && Walking.walk(Locations.boarZone.getCenter())) Sleep.sleep(69, 420);
				} else 
				{
					Walkz.teleportWoodcuttingGuild(180000);
				}
			}
			else 
			{
				MethodProvider.log("Not enough darts: " + new Item(TrainRanged.getBestDart(),1).getName());
				TrainRanged.fulfillRangedDarts();
			}
		}
		return Timing.sleepLogNormalSleep();
	}
	public static int trainPlainMainlandSlayerTask(String mobName, Area killingZone, int maxHit,List<Integer> groundItemLoot)
	{
		//check for lantern on cave tasks
		if(mobName.contains("Cave bug") || mobName.contains("Cave slime"))
		{
			if(!Candlez.getLantern())
			{
				MethodProvider.log("did not get lantern within timer limit!");
				return Timing.sleepLogNormalInteraction();
			}
						
		}
		
		//check equipment for wealth, if none, get more
		if(!InvEquip.equipmentContains(InvEquip.wearableWealth)) 
		{
			if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
			else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
			else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
			else TrainRanged.fulfillRangedDartsStaminaAntidote();
			return Timing.sleepLogNormalSleep();
		}
		
		//check invy for best darts, equip 
		if(Inventory.count(TrainRanged.getBestDart()) > 0) InvEquip.equipItem(TrainRanged.getBestDart());
		
		//check invy + equip for best darts, if have, do more checks, if not, fulfill setup
		if(Inventory.count(TrainRanged.getBestDart()) > 0 || Equipment.count(TrainRanged.getBestDart()) > 0)
		{
			//have some food in invy
			if(Combat.getFood() != null)
			{
				//should eat at 1 max hit
				if(Combat.shouldEatFood(maxHit))
				{
					Combat.eatFood();
				}
			}
			else 
			{
				//no more food, fulfill setup
				if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
				else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
				else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
				else TrainRanged.fulfillRangedDartsStaminaAntidote();
				return Timing.sleepLogNormalSleep();
			}
			
			if(mobName.contains("Cave crawler") || mobName.contains("Cave slime") || 
					mobName.contains("Kalphite Worker"))
			{
				if(!Combat.hasAntidoteProtection())
				{
					if(!Combat.drinkAntidote()) 
					{
						if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
						else TrainRanged.fulfillRangedDartsStaminaAntidote();
						return Timing.sleepLogNormalSleep();
					}
				}
			}
			
			
			//check for proper ranged lvl boost
			if(TrainRanged.shouldDrinkBoost())
			{
				//if have any ranged pots while needing a drink, wait until in killingzone to sip up
				if(InvEquip.invyContains(TrainRanged.rangedPots))
				{
					if(killingZone.contains(Players.localPlayer()))
					{
						if(!TrainRanged.drankRangedPotion()) MethodProvider.log("Not drank ranged Potion");
					}
				}
				else 
				{
					//need a ranged pot to drink but have none
					if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
					else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
					else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
					else TrainRanged.fulfillRangedDartsStaminaAntidote();
					return Timing.sleepLogNormalSleep();
				}
			}
			
			
			if(groundItemLoot != null && !groundItemLoot.isEmpty())
			{
				GroundItem gi = ItemsOnGround.getNearbyGroundItem(groundItemLoot,killingZone);
				if(gi != null)
				{
					ItemsOnGround.grabNearbyGroundItem(gi);
					return Timing.sleepLogNormalSleep();
				}
			}
			
			
			//in location to kill mobs
			if(killingZone.contains(Players.localPlayer()))
			{
				MethodProvider.log("[SLAYER] -> in killingzone!");
				if(Dialogues.canContinue()) 
				{
					if(Dialogues.continueDialogue()) Sleep.sleep(420, 696);
					return Timing.sleepLogNormalInteraction();
				}
				//check for glory in invy + not equipped -> equip it
				if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
						!InvEquip.equipmentContains(InvEquip.wearableGlory)) InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
				
				//check for slayer specific stuff
				if(mobName.contains("Lizard"))
				{
					//need waterskins && ice coolers at all times
					if(Inventory.count(InvEquip.iceCooler) <= 0)
					{
						MethodProvider.log("Need ice coolers");
						if((Inventory.count(InvEquip.iceCooler) + Bank.count(InvEquip.iceCooler) < 200))
						{
							TrainSlayer.buyItemTurael(InvEquip.iceCooler, 500);
							return Timing.sleepLogNormalSleep();
						}
						TrainSlayer.fulfillRangedLizardsTask();
						return Timing.sleepLogNormalSleep();
					}
					if(InvEquip.getWaterskinCharges() <= 2)
					{
						MethodProvider.log("Have less than 2 waterskin charges: " + InvEquip.getWaterskinCharges() + ", exiting desert!");
						if(Locations.isInDesert()) Walkz.exitDesert();
						else 
						{
							TrainSlayer.fulfillRangedLizardsTask();
						}
						return Timing.sleepLogNormalSleep();
					}
					Mobs.fightLizardsRanged(killingZone);
					return Timing.sleepLogNormalSleep();
				}
				Mobs.fightMobRanged(mobName, killingZone, maxHit);
				return Timing.sleepLogNormalSleep();
			}
			MethodProvider.log("Slayer not in killingzone");
			if(!Walkz.isStaminated())
			{
				Walkz.drinkStamina();
			}
			if(!Walking.isRunEnabled() && Walking.getRunEnergy() > 15) Walking.toggleRun();
			if(Locations.isInDesert())
			{
				MethodProvider.log("In desert!");
				
				if(InvEquip.getWaterskinCharges() <= 2)
				{
					MethodProvider.log("Have less than 2 waterskin charges: " + InvEquip.getWaterskinCharges() + ", exiting desert!");
					if(Locations.isInDesert()) Walkz.exitDesert();
					else 
					{
						TrainSlayer.fulfillRangedLizardsTask();
					}
					return Timing.sleepLogNormalSleep();
				}
			}
			if(mobName.contains("Zombie"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				
				if(Locations.edgevilleDungeon.contains(Players.localPlayer()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.zombiesEdgeville.getCenter())) Sleep.sleep(420,696);
				}
				else
				{
					if(Locations.edgevilleTeleSpot.distance(Players.localPlayer().getTile()) >= 50)
					{
						if(Walkz.useJewelry(InvEquip.glory, "Edgeville"))
						{
							return Sleep.calculate(420,696);
						}
					}
					
					if(Walking.shouldWalk(6) && Walking.walk(Locations.zombiesEdgeville.getCenter())) Sleep.sleep(420,696);
				}
				return Timing.sleepLogNormalSleep();
			}
			if(mobName.contains("Skeleton"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				
				if(Locations.edgevilleDungeon.contains(Players.localPlayer()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.skeletonsEdgeville.getCenter())) Sleep.sleep(420,696);
				}
				else
				{
					if(Locations.edgevilleTeleSpot.distance(Players.localPlayer().getTile()) >= 50)
					{
						if(Walkz.useJewelry(InvEquip.glory, "Edgeville"))
						{
							return Sleep.calculate(420,696);
						}
					}
					
					if(Walking.shouldWalk(6) && Walking.walk(Locations.skeletonsEdgeville.getCenter())) Sleep.sleep(420,696);
				}
				return Timing.sleepLogNormalSleep();
			}
			if(mobName.contains("Seagull"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.seagullsSarim.getCenter().distance() >= 100 && Walkz.useJewelry(InvEquip.glory, "Draynor Village")) return Timing.sleepLogNormalInteraction();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.seagullsSarim.getCenter());
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Cave crawler"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.entireFremmyDungeon.contains(Players.localPlayer()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.caveCrawlersFremmyCave.getCenter())) Sleep.sleep(696, 420);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.fremmyCaveEntrance.contains(Players.localPlayer()))
				{
					GameObject caveEntrance = GameObjects.closest("Cave Entrance");
					if(caveEntrance == null) {
						MethodProvider.log("Cave entrance to fremmy dungeon is null at entrance location!");
						return Timing.sleepLogNormalInteraction();
					}
					if(caveEntrance.interact("Enter"))
					{
						MethodProvider.sleepUntil(() -> Locations.entireFremmyDungeon.contains(Players.localPlayer()),
								() -> Players.localPlayer().isMoving(), 
								Sleep.calculate(2222, 2222),50);
					}
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.fremmyCaveEntrance.getCenter().distance() > 175 || Locations.burthrope.contains(Players.localPlayer()))
				{
					if(TrainRanged.fulfillRangedDartsStaminaAntidote())
					{
						Walkz.teleportCamelot(180000);
					}
					return Timing.sleepLogNormalSleep();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.fremmyCaveEntrance)) Sleep.sleep(420, 69);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Kalphite Worker"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.kalphiteCave.contains(Players.localPlayer()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.kalphiteWorkersArea.getCenter())) Sleep.sleep(696, 420);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.kalphiteCaveEntrance.contains(Players.localPlayer()))
				{
					GameObject caveEntrance = GameObjects.closest("Cave");
					if(caveEntrance == null) {
						MethodProvider.log("Cave entrance to kalphite caven is null at entrance location!");
						return Timing.sleepLogNormalInteraction();
					}
					if(caveEntrance.interact("Enter"))
					{
						MethodProvider.sleepUntil(() -> Locations.kalphiteCave.contains(Players.localPlayer()),
								() -> Players.localPlayer().isMoving(), 
								Sleep.calculate(2222, 2222),50);
					}
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.isInDesert())
				{
					if(Walkz.walkPath(Paths.shantayPassToKalphiteCave)) MethodProvider.log("Walked to Kalphites cave path!");
					return Timing.sleepLogNormalInteraction();
				}
				
				if(Walkz.enterDesert()) Sleep.sleep(420, 69);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Giant spider"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.spidersGE.getCenter().distance() >= 100 && Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) return Timing.sleepLogNormalInteraction();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.spidersGE.getCenter());
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Bear Cub"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.bearsFremmy.getCenter().distance() >= 200 && Walkz.teleportCamelot(180000)) return Timing.sleepLogNormalInteraction();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.bearsFremmy.getCenter());
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Cow"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.cowsArdy.getCenter().distance() >= 150)
				{
					if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
					{
						InvEquip.buyItem(InvEquip.skills6, 1, 180000);
						return Timing.sleepLogNormalInteraction();
					}
					return Timing.sleepLogNormalInteraction();
				}
				if(Walking.shouldWalk(6)) Walking.walk(Locations.cowsArdy.getCenter());
				return Timing.sleepLogNormalInteraction();
			}
			
			if(mobName.contains("Monkey"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.monkeysArdyZoo.getCenter().distance() >= 150)
				{
					if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
					{
						InvEquip.buyItem(InvEquip.skills6, 1, 180000);
						return Timing.sleepLogNormalInteraction();
					}
					return Timing.sleepLogNormalInteraction();
				}
				if(Walking.shouldWalk(6)) Walking.walk(Locations.monkeysArdyZoo.getCenter());
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Lizard"))
			{
				MethodProvider.log("Lizards task!");
				
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				//check for ice coolers first
				if(!InvEquip.checkedBank()) return Timing.sleepLogNormalInteraction();
				
				if(Locations.isInDesert())
				{
					
					if(Locations.lizards.getCenter().distance() > 35)
					{
						MethodProvider.log("Dist to lizards greater than 35");
						if(Inventory.count(InvEquip.iceCooler) < 200)
						{
							MethodProvider.log("Need ice coolers");
							if((Inventory.count(InvEquip.iceCooler) + Bank.count(InvEquip.iceCooler) < 200))
							{
								TrainSlayer.buyItemTurael(InvEquip.iceCooler, 500);
								return Timing.sleepLogNormalSleep();
							}
							TrainSlayer.fulfillRangedLizardsTask();
							return Timing.sleepLogNormalSleep();
						}
					}
					if(Walkz.walkPath(Paths.shantayPassToLizards)) 
					{
						MethodProvider.log("walked to lizards");
						Sleep.sleep(420, 696);
					} else MethodProvider.log("did not walk to lizards");
						
					return Timing.sleepLogNormalInteraction();
				}
				
				MethodProvider.log("Not in desert");
				if(Inventory.count(InvEquip.iceCooler) < 200)
				{
					MethodProvider.log("Need ice coolers");
					if((Inventory.count(InvEquip.iceCooler) + Bank.count(InvEquip.iceCooler) < 200))
					{
						TrainSlayer.buyItemTurael(InvEquip.iceCooler, 500);
						return Timing.sleepLogNormalSleep();
					}
					TrainSlayer.fulfillRangedLizardsTask();
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.shantayPassArea.contains(Players.localPlayer()))
				{
					Walkz.enterDesert();
					return Timing.sleepLogNormalInteraction();
				}
				if(Locations.shantayPassArea.getCenter().distance() >= 150)
				{
					if(!Walkz.useJewelry(InvEquip.glory, "Al Kharid"))
					{
						if(Walkz.useJewelry(InvEquip.duel, "PvP Arena"))
						{
							return Timing.sleepLogNormalInteraction();
						}
					}
					else return Timing.sleepLogNormalInteraction();
				}
				if(Walking.shouldWalk(6) && Walking.walk(Locations.shantayPassArea.getCenter())) return Timing.sleepLogNormalInteraction();
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Cave bug"))
			{
				if(Candlez.getLantern())
				{
					if(!SlayerSettings.unlockedLumbyCave())
					{
						if(!Inventory.contains(id.rope))
						{
							TrainSlayer.fulfillRangedCaveBugsTask();
							return Timing.sleepLogNormalInteraction();
						}
						if(Locations.lumbridgeCaveEntrance.contains(Players.localPlayer()))
						{
							GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
									g.getName().equals("Dark hole") && 
									g.getTile().equals(new Tile(3169,3172,0)));
							if(caveEntrance == null)
							{
								MethodProvider.log("lumby swamp cave entrance null on top entrance location!");
								return Timing.sleepLogNormalInteraction();
							}
							if(Inventory.get(id.rope).useOn(caveEntrance))
							{
								MethodProvider.sleepUntil(() -> SlayerSettings.unlockedLumbyCave(), 
										() -> Players.localPlayer().isMoving(),
										Sleep.calculate(2222, 2222),50);
								return Timing.sleepLogNormalInteraction();
							}
						}
						if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
						{
							Walkz.teleportLumbridge(180000);
							return Timing.sleepLogNormalInteraction();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleep.sleep(696, 420);
						return Timing.sleepLogNormalSleep();
					}
					
					//here can now enter tha cave ... 
					if(Locations.entireLumbyCave.contains(Players.localPlayer()))
					{
						if(Locations.caveHandSkipTile.equals(Players.localPlayer().getTile()))
						{
							if(!Walking.isRunEnabled())
							{
								if(Walking.toggleRun()) Sleep.sleep(420, 696);
								return Timing.sleepLogNormalInteraction();
							}
							if(Walking.walkExact(Locations.caveHandSkipTile2))
							{
								Sleep.sleep(420, 696);
							}
							return Timing.sleepLogNormalInteraction();
						}
						if(Locations.lumbyCaveFoyer.contains(Players.localPlayer()))
						{
							if(!Players.localPlayer().isMoving() && Walking.walkExact(Locations.caveHandSkipTile)) Sleep.sleep(420, 696);
							return Timing.sleepLogNormalInteraction();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.caveBugs.getCenter())) Sleep.sleep(420, 696);
						return Timing.sleepLogNormalInteraction();
					}
					
					if(Locations.lumbridgeCaveEntrance.contains(Players.localPlayer()))
					{
						GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
								g.getName().equals("Dark hole") && 
								g.getTile().equals(new Tile(3169,3172,0)));
						if(caveEntrance == null)
						{
							MethodProvider.log("lumby swamp cave entrance null on top entrance location!");
							return Timing.sleepLogNormalInteraction();
						}
						if(caveEntrance.interact("Climb-down"))
						{
							MethodProvider.sleepUntil(() -> Locations.lumbyCaveFoyer.contains(Players.localPlayer()), 
									() -> Players.localPlayer().isMoving(),
									Sleep.calculate(2222, 2222),50);
							return Timing.sleepLogNormalInteraction();
						}
					}
					if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
					{
						if(Locations.burthrope.contains(Players.localPlayer()))
						{
							
						}
						Walkz.teleportLumbridge(180000);
						return Timing.sleepLogNormalInteraction();
					}
					if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleep.sleep(696, 420);
				}
				
			}
			if(mobName.contains("Cave slime"))
			{
				if(Candlez.getLantern())
				{
					if(!SlayerSettings.unlockedLumbyCave())
					{
						if(!Inventory.contains(id.rope))
						{
							TrainSlayer.fulfillRangedCaveBugsTask();
							return Timing.sleepLogNormalInteraction();
						}
						if(Locations.lumbridgeCaveEntrance.contains(Players.localPlayer()))
						{
							GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
									g.getName().equals("Dark hole") && 
									g.getTile().equals(new Tile(3169,3172,0)));
							if(caveEntrance == null)
							{
								MethodProvider.log("lumby swamp cave entrance null on top entrance location!");
								return Timing.sleepLogNormalInteraction();
							}
							if(Inventory.get(id.rope).useOn(caveEntrance))
							{
								MethodProvider.sleepUntil(() -> SlayerSettings.unlockedLumbyCave(), 
										() -> Players.localPlayer().isMoving(),
										Sleep.calculate(2222, 2222),50);
								return Timing.sleepLogNormalInteraction();
							}
						}
						if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
						{
							Walkz.teleportLumbridge(180000);
							return Timing.sleepLogNormalInteraction();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleep.sleep(696, 420);
						return Timing.sleepLogNormalSleep();
					}
					
					//here can now enter tha cave ... 
					if(Locations.entireLumbyCave.contains(Players.localPlayer()))
					{
						if(Locations.caveHandSkipTile.equals(Players.localPlayer().getTile()))
						{
							if(!Walking.isRunEnabled())
							{
								if(Walking.toggleRun()) Sleep.sleep(420, 696);
								return Timing.sleepLogNormalInteraction();
							}
							if(Walking.walkExact(Locations.caveHandSkipTile2))
							{
								Sleep.sleep(420, 696);
							}
							return Timing.sleepLogNormalInteraction();
						}
						if(Locations.lumbyCaveFoyer.contains(Players.localPlayer()))
						{
							if(!Players.localPlayer().isMoving() && Walking.walkExact(Locations.caveHandSkipTile)) Sleep.sleep(420, 696);
							return Timing.sleepLogNormalInteraction();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.caveSlimeArea.getCenter())) Sleep.sleep(420, 696);
						return Timing.sleepLogNormalInteraction();
					}
					
					if(Locations.lumbridgeCaveEntrance.contains(Players.localPlayer()))
					{
						GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
								g.getName().equals("Dark hole") && 
								g.getTile().equals(new Tile(3169,3172,0)));
						if(caveEntrance == null)
						{
							MethodProvider.log("lumby swamp cave entrance null on top entrance location!");
							return Timing.sleepLogNormalInteraction();
						}
						if(caveEntrance.interact("Climb-down"))
						{
							MethodProvider.sleepUntil(() -> Locations.lumbyCaveFoyer.contains(Players.localPlayer()), 
									() -> Players.localPlayer().isMoving(),
									Sleep.calculate(2222, 2222),50);
							return Timing.sleepLogNormalInteraction();
						}
					}
					if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
					{
						Walkz.teleportLumbridge(180000);
						return Timing.sleepLogNormalInteraction();
					}
					if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleep.sleep(696, 420);
				}
				
			}
			if(mobName.contains("Goblin"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.lumbyCastle2.contains(Players.localPlayer()))
				{
					GameObject door = GameObjects.closest(f -> f != null && f.getName().equals("Staircase"));
					if(door == null)
					{
						MethodProvider.log("Lumby castle 2nd floor stairs null!");
						return Timing.sleepLogNormalInteraction();
					}
					if(!door.canReach()) 
					{
						Walking.walk(door);
						return Timing.sleepLogNormalInteraction();
					}
					if(door.interact("Climb-down"))
					{
						MethodProvider.sleepUntil(() -> !Locations.lumbyCastle2.contains(Players.localPlayer()),
								() -> Players.localPlayer().isMoving(), 
								Sleep.calculate(2222, 2222),50);
					}
					return Timing.sleepLogNormalInteraction();
				}
				if(Locations.lumbyCastle1.contains(Players.localPlayer()))
				{
					GameObject door = GameObjects.closest(f -> f != null && f.getName().equals("Staircase"));
					if(door == null)
					{
						MethodProvider.log("Lumby castle 1st floor stairs null!");
						return Timing.sleepLogNormalInteraction();
					}
					if(!door.canReach()) 
					{
						Walking.walk(door);
						return Timing.sleepLogNormalInteraction();
					}
					if(door.interact("Climb-down"))
					{
						MethodProvider.sleepUntil(() -> !Locations.lumbyCastle1.contains(Players.localPlayer()),
								() -> Players.localPlayer().isMoving(), 
								Sleep.calculate(2222, 2222),50);
					}
					return Timing.sleepLogNormalInteraction();
				}
				if(Locations.goblins.getCenter().distance() > 150)
				{
					if(!Walkz.teleportLumbridge(180000)) InvEquip.buyItem(id.lumbridgeTele, 10, 180000);
					return Timing.sleepLogNormalSleep();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.goblins.getCenter())) Sleep.sleep(420, 696);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Scorpion"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.scorpions.getCenter().distance() > 175)
				{
					if(!Walkz.useJewelry(InvEquip.glory,"Al Kharid"))
					{
						if(!Walkz.useJewelry(InvEquip.duel, "PvP Arena"))
						{
							if(InvEquip.bankContains(InvEquip.wearableDuel))
							{
								InvEquip.withdrawOne(InvEquip.getBankItem(InvEquip.wearableDuel), 180000);
								return Timing.sleepLogNormalSleep();
							}
						}
					}
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.scorpions.getCenter())) Sleep.sleep(420, 696);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Dwarf"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.dwarfs.getCenter().distance() > 90)
				{
					if(!Walkz.useJewelry(InvEquip.glory,"Edgeville")) TrainRanged.fulfillRangedDartsStaminaAntidote();
					return Timing.sleepLogNormalSleep();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.dwarfs.getCenter())) Sleep.sleep(420, 696);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Icefiend"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.icefiends.getCenter().distance() > 105)
				{
					if(!Walkz.useJewelry(InvEquip.glory,"Edgeville")) TrainRanged.fulfillRangedDartsStaminaAntidote();
					return Timing.sleepLogNormalSleep();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.icefiends.getCenter())) Sleep.sleep(420, 696);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Bat"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.bats.getCenter().distance() > 175)
				{
					if(!Walkz.teleportVarrock(180000)) InvEquip.buyItem(id.varrockTele, 10, 180000);
					return Timing.sleepLogNormalSleep();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.bats.getCenter())) Sleep.sleep(420, 696);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Giant rat"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.giantRats.getCenter().distance() > 150)
				{
					if(!Walkz.teleportLumbridge(180000)) InvEquip.buyItem(id.lumbridgeTele, 10, 180000);
					return Timing.sleepLogNormalSleep();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.giantRats.getCenter())) Sleep.sleep(420, 696);
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Wolf") || mobName.contains("Minotaur"))
			{
				if(Locations.isInKourend())
				{
					Walkz.leaveKourend(180000);
					return Timing.sleepLogNormalSleep();
				}
				if(Locations.strongholdLvl1.contains(Players.localPlayer()))
				{
					if(Camera.getPitch() < 334) 
					{
						int tmp = (int) Calculations.nextGaussianRandom(375, 50);
						if(tmp < 340) tmp = 340;
						if(tmp > 382) tmp = 382;
						Camera.rotateToPitch(tmp);
						return Timing.sleepLogNormalInteraction();
					}
					if(Dialogues.canContinue()) 
					{
						if(Dialogues.continueDialogue()) Sleep.sleep(420,696);
						return Timing.sleepLogNormalInteraction();
					}
					if(Dialogues.isProcessing()) return Timing.sleepLogNormalInteraction();
					if(Dialogues.chooseOption("Politely tell them no and then use the \'Report Abuse\' button.") || 
							Dialogues.chooseOption("No way! You\'ll just take my gold for your own! Reported!") || 
							Dialogues.chooseOption("Report the incident and do not click any links.") || 
							Dialogues.chooseOption("No, you should never allow anyone to level your account.") || 
							Dialogues.chooseOption("Report the player for phising.") || 
							Dialogues.chooseOption("Do not visit the website and report the player who messaged you.") || 
							Dialogues.chooseOption("Delete it - it\'s a fake!") || 
							Dialogues.chooseOption("The birthday of a famous person or event.") || 
							Dialogues.chooseOption("Don\'t give out your password to anyone. Not even close friends.") || 
							Dialogues.chooseOption("Read the text and follow the advice given.") || 
							Dialogues.chooseOption("Report the stream as a scam. Real Jagex streams have a \'verified\' mark.") || 
							Dialogues.chooseOption("Authenticator and two-step login on my registered email.") || 
							Dialogues.chooseOption("Secure my device and reset my password.") || 
							Dialogues.chooseOption("Don\'t give them the information and send an \'Abuse report\'.") || 
							Dialogues.chooseOption("No.") || 
							Dialogues.chooseOption("Don\'t give them my password.") || 
							Dialogues.chooseOption("Nobody.") || 
							Dialogues.chooseOption("Through account settings on oldschool.runescape.com.") || 
							Dialogues.chooseOption("No, you should never buy an account.") || 
							Dialogues.chooseOption("Only on the Old School Runescape website.") || 
							Dialogues.chooseOption("Talk to any banker.") || 
							Dialogues.chooseOption("Set up 2 step authentication with my email provider.") || 
							Dialogues.chooseOption("Don\'t share your information and report the player.") || 
							Dialogues.chooseOption("Virus scan my device then change my password.") || 
							Dialogues.chooseOption("Don\'t type in my password backwards and report the player.") || 
							Dialogues.chooseOption("Use the Account Recovery System.") || 
							Dialogues.chooseOption("Me.") || 
							Dialogues.chooseOption("No way! I\'m reporting you to Jagex!") || 
							Dialogues.chooseOption("Nothing, it\'s a fake.") || 
							Dialogues.chooseOption("Decline the offer and report that player."))
					{
						return Timing.sleepLogNormalInteraction();
					}
					if(Locations.strongholdLvl1Foyer.contains(Players.localPlayer()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1859,5238,0)));
						if(door == null)
						{
							MethodProvider.log("Stronghold 1st door null!");
							return Timing.sleepLogNormalInteraction();
						}
						if(door.interact("Open"))
						{
							MethodProvider.sleepUntil(Dialogues::inDialogue,
									() -> Players.localPlayer().isMoving(), 
									Sleep.calculate(2222, 2222),50);
						}
						return Timing.sleepLogNormalInteraction();
					}
					if(Locations.strongholdDoor1.contains(Players.localPlayer()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1859, 5235, 0)));
						if(door == null)
						{
							MethodProvider.log("Stronghold 2nd door null!");
							return Timing.sleepLogNormalInteraction();
						}
						if(door.interact("Open"))
						{
							MethodProvider.sleepUntil(Dialogues::inDialogue,
									() -> Players.localPlayer().isMoving(), 
									Sleep.calculate(2222, 2222),50);
						}
						return Timing.sleepLogNormalInteraction();
					}if(Locations.strongholdGoblins.contains(Players.localPlayer()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1865, 5227, 0)));
						if(door == null)
						{
							MethodProvider.log("Stronghold 3rd door null!");
							return Timing.sleepLogNormalInteraction();
						}
						if(door.interact("Open"))
						{
							MethodProvider.sleepUntil(Dialogues::inDialogue,
									() -> Players.localPlayer().isMoving(), 
									Sleep.calculate(2222, 2222),50);
						}
						return Timing.sleepLogNormalInteraction();
					}
					if(Locations.strongholdDoor2.contains(Players.localPlayer()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1868, 5227, 0)));
						if(door == null)
						{
							MethodProvider.log("Stronghold 4th door null!");
							return Timing.sleepLogNormalInteraction();
						}
						if(door.interact("Open"))
						{
							MethodProvider.sleepUntil(Dialogues::inDialogue,
									() -> Players.localPlayer().isMoving(), 
									Sleep.calculate(2222, 2222),50);
						}
						return Timing.sleepLogNormalInteraction();
					}
					return Timing.sleepLogNormalInteraction();
				}
				if(Locations.strongholdEntrance.contains(Players.localPlayer()))
				{
					GameObject entrance = GameObjects.closest(f -> f != null && f.getName().contains("Entrance") && f.hasAction("Climb-down"));
					if(entrance == null)
					{
						MethodProvider.log("Stronghold of security entrance null when should be near it!");
						return Timing.sleepLogNormalInteraction();
					}
					if(entrance.interact("Climb-down"))
					{
						MethodProvider.sleepUntil(() -> Locations.strongholdLvl1Foyer.contains(Players.localPlayer()),
								() -> Players.localPlayer().isMoving(), 
								Sleep.calculate(2222, 2222),50);
					}
					return Timing.sleepLogNormalInteraction();
				}
				if(Locations.strongholdEntrance.getCenter().distance() >= 150 && Walkz.useJewelry(InvEquip.glory, "Edgeville")) return Timing.sleepLogNormalInteraction();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.strongholdEntrance.getCenter());
				return Timing.sleepLogNormalInteraction();
			}
			if(mobName.contains("Guard dog"))
			{
				if(Locations.isInKourend())
				{
					if(Walking.shouldWalk(6) && Walking.walk(killingZone.getCenter())) Sleep.sleep(420,696);
				} else 
				{
					Walkz.teleportWoodcuttingGuild(180000);
					return Timing.sleepLogNormalInteraction();
				}
					
			}
			if(mobName.contains("Forgotten Soul"))
			{
				if(Locations.isInIsleOfSouls())
				{
					if(Walkz.walkToForgottenSouls())
					{
						MethodProvider.log("Walked toward forgotten souls");
					}
					return Timing.sleepLogNormalSleep();
				}
				if(InvEquip.invyContains(InvEquip.staminas))
				{
					if(Locations.edgevilleSoulsPortal.contains(Players.localPlayer()))
					{
						Filter<GameObject> portalFilter = p -> 
							p != null && 
							p.getName().equals("Soul Wars Portal") && 
							p.hasAction("Enter");
						GameObject portal = GameObjects.closest(portalFilter);
						if(portal == null)
						{
							MethodProvider.log("Portal is null in portal area :-( Souls wars at edgeville");
							return Timing.sleepLogNormalSleep();
						}
						if(portal.interact("Enter"))
						{
							MethodProvider.sleepUntil(() -> Locations.isleOfSouls.contains(Players.localPlayer()), Sleep.calculate(4444, 2222));
							Sleep.sleep(420, 696);
						}
						return Timing.sleepLogNormalSleep();
					}
					
					if(Locations.edgevilleSoulsPortal.getCenter().distance() > 100)
					{
						if(Walkz.useJewelry(InvEquip.glory, "Edgeville")) 
						{
							return Timing.sleepLogNormalInteraction();
						}
					}
					
					if(Walking.shouldWalk(6) && Walking.walk(Locations.edgevilleSoulsPortal)) Sleep.sleep(420, 696);
				}
				else
				{
					TrainRanged.fulfillRangedDartsStaminaAntidote();
				}
			}
		}
		else 
		{
			MethodProvider.log("Not enough darts: " + new Item(TrainRanged.getBestDart(),1).getName());
			if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
			else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
			else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
			else TrainRanged.fulfillRangedDartsStaminaAntidote();
		}
		return Timing.sleepLogNormalSleep();
	}
	public static void fightLizardsRanged(Area killingZone)
	{
		if(Combat.shouldEatFood(15)) Combat.eatFood();
		if(API.mode == modes.TRAIN_RANGE && TrainRanged.shouldDrinkBoost())
		{
			if(TrainRanged.drankRangedPotion()) Sleep.sleep(69, 420);
		}
		if(TrainRanged.ranged < 66)
		{
			if(org.dreambot.api.methods.combat.Combat.getCombatStyle() != CombatStyle.RANGED_RAPID)
			{
				org.dreambot.api.methods.combat.Combat.setCombatStyle(CombatStyle.RANGED_RAPID);
			}
		} else if(org.dreambot.api.methods.combat.Combat.getCombatStyle() != CombatStyle.RANGED_DEFENCE)
		{
			org.dreambot.api.methods.combat.Combat.setCombatStyle(CombatStyle.RANGED_DEFENCE);
		} 
		org.dreambot.api.wrappers.interactive.Character lizard = Players.localPlayer().getInteractingCharacter();
		if(lizard != null && 
				(lizard.getName().equals("Small Lizard") || lizard.getName().equals("Desert Lizard")))
		{
			MethodProvider.log("interacting lizard!");
			if(lizard.getHealthPercent() <= 20)
			{
				if(Inventory.get(InvEquip.iceCooler).useOn(lizard))
				{
					MethodProvider.log("Used icecooler on lizard!");
					MethodProvider.sleepUntil(() -> lizard.getInteractingCharacter() == null, Sleep.calculate(2222,2222));
				}
			}
			
			MethodProvider.sleep(Timing.sleepLogNormalSleep());
        	return;
		}
		API.randomAFK(10);
		Filter<NPC> onMeMobsFilter = mob -> 
				mob != null &&
				(mob.getName().equals("Small Lizard") || mob.getName().equals("Desert Lizard")) &&
				mob.isInteracting(Players.localPlayer());
		List<NPC> mobsOnMe = NPCs.all(onMeMobsFilter);
		if(!mobsOnMe.isEmpty())
		{
			String mobsOnMeString = "";
			for(NPC mobOnMe : mobsOnMe)
			{
				mobsOnMeString = mobsOnMeString.concat(mobOnMe.getName()).concat(" ");
			}
	    	Main.customPaintText3 = "~~Have mobs on me: " + mobsOnMeString+"~~";
	    	for(NPC mobOnMe : mobsOnMe)
	    	{
	    		if(Players.localPlayer().isInteracting(mobOnMe)) 
		    	{
	    			if(mobOnMe.getHealthPercent() <= 20)
	    			{
	    				if(Inventory.get(InvEquip.iceCooler).useOn(mobOnMe))
	    				{
	    					MethodProvider.log("Used icecooler on lizard!");
	    					MethodProvider.sleepUntil(() -> mobOnMe.getInteractingCharacter() == null, Sleep.calculate(2222,2222));
	    				}
	    			}
	    			
	    			MethodProvider.sleep(Timing.sleepLogNormalSleep());
		        	return;
		    	}
	    	}
	    	return;
		}
		//here we have no mobs attacking us already, so attack some mobs
		Filter<NPC> newMobsFilter = mob -> 
				mob != null &&
				mob.exists() && 
				mob.getHealthPercent() >= 0 && 
				killingZone.contains(mob) && 
				(mob.getName().equals("Small Lizard") || mob.getName().equals("Desert Lizard")) &&
				(org.dreambot.api.methods.combat.Combat.isInMultiCombat() || 
						(!org.dreambot.api.methods.combat.Combat.isInMultiCombat() && !mob.isInteractedWith()));
		NPC mob = NPCs.closest(newMobsFilter);
		if(mob != null)
		{
			Main.customPaintText3 = "~~Attacking mob: " + mob.getName()+"~~";
			if(mob.interact("Attack"))
			{
				MethodProvider.sleepUntil(() -> Players.localPlayer().isInteracting(mob) && mob.isInteracting(Players.localPlayer()), Sleep.calculate(2222, 2222));
				if(Players.localPlayer().isInteracting(mob)) MethodProvider.sleep(Timing.sleepLogNormalSleep());
	        	return;
			}
		}
	}
	public static int trainHillGiants()
	{
		Locations.isInKourend();
		if(!Locations.unlockedKourend) //fuck config value 414 we assume unlocked unless get teh game msg
		{
			//handle dialogue for Veos traveling to Kourend
			if(Dialogues.inDialogue()) 
			{
				if(Dialogues.canContinue()) Dialogues.continueDialogue();
				else if(Dialogues.areOptionsAvailable()) Dialogues.chooseFirstOptionContaining("That\'s great, can you take me there please?");
    			return Timing.sleepLogNormalSleep();
			}
			
			//search for Veos
			NPC veos = NPCs.closest("Veos");
			if(veos != null)
			{
				if(!Players.localPlayer().isInteracting(veos))
	    		{
	        		if(veos.interact("Talk-to"))
        			{
        				MethodProvider.sleepUntil(Dialogues::inDialogue, () -> Players.localPlayer().isMoving(), Sleep.calculate(2222,2222), 50);
        			}
	    		}
				return Timing.sleepLogNormalSleep();
			}
	    	else
	    	{
	    		if(Locations.veosSarim.getCenter().distance(Players.localPlayer().getTile()) > 75) Walkz.teleportDraynor(180000);
    			else if(Walking.shouldWalk(6) && Walking.walk(Locations.veosSarim.getCenter())) Sleep.sleep(420,666);
	    	}
			return Timing.sleepLogNormalSleep();
		}
		else
		{
			//can travel to kourend now
			
			//check invy for best darts, equip 
			if(Inventory.count(TrainRanged.getBestDart()) > 0) InvEquip.equipItem(TrainRanged.getBestDart());
			
			//check invy + equip for best darts, if have, do more checks, if not, fulfill setup
			if(Inventory.count(TrainRanged.getBestDart()) > 0 || Equipment.count(TrainRanged.getBestDart()) > 0)
			{
				
				//have some food in invy
				if(Combat.getFood() != null)
				{
					//should eat at 1 max hit
					if(Combat.shouldEatFood(6))
					{
						Combat.eatFood();
					}
				}
				else 
				{
					//no more food, fulfill setup
					TrainRanged.fulfillRangedDarts();
					return Timing.sleepLogNormalSleep();
				}
				
				//check for glory in invy + not equipped -> equip it
				if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
						!InvEquip.equipmentContains(InvEquip.wearableGlory)) InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
				
				
				//in location to kill mobs
				if(Locations.kourendGiantsCaveArea.contains(Players.localPlayer()))
    			{
					if(Inventory.isFull())
					{
						if(!Tabs.isOpen(Tab.INVENTORY))
						{
							Tabs.openWithFKey(Tab.INVENTORY);
						}
						if(Inventory.count(TrainRanged.jug) > 0)
						{
							Inventory.dropAll(TrainRanged.jug);
						}
						if(Inventory.count(ItemsOnGround.bigBones) > 0)
						{
							Inventory.interact(ItemsOnGround.bigBones, "Bury");
							Sleep.sleep(69,420);
						}
						if(Combat.eatFood())
						{
							return Timing.sleepLogNormalSleep();
						}
					}
					if(Combat.shouldEatFood(8))
					{
						Combat.eatFood();
					}
					if(Inventory.count(ItemsOnGround.bigBones) > 0)
					{
						if((int) Calculations.nextGaussianRandom(50,30) > 65 && Inventory.interact(ItemsOnGround.bigBones, "Bury")) MethodProvider.sleep(Timing.sleepLogNormalSleep());
					}
					GroundItem gi = ItemsOnGround.getNearbyGroundItem(ItemsOnGround.hillGiantsLoot,Locations.kourendGiantsKillingArea_Hill);
					if(gi != null)
					{
						ItemsOnGround.grabNearbyGroundItem(gi);
						return Timing.sleepLogNormalSleep();
					}
					
					//must be OK to fight hill giants now 
					//check for proper ranged lvl boost
					if(TrainRanged.shouldDrinkBoost())
					{
						//if have any ranged pots while needing a drink, wait until in killingzone to sip up
						if(InvEquip.invyContains(TrainRanged.rangedPots))
						{
							if(!TrainRanged.drankRangedPotion()) MethodProvider.log("Not drank ranged Potion");						}
						else 
						{
							//need a ranged pot to drink but have none
							TrainRanged.fulfillRangedDarts();
							return Timing.sleepLogNormalSleep();
						}
					}
					
					if(!Locations.kourendGiantsSafeSpot_Hill.contains(Players.localPlayer()))
					{
						final Tile closestSafespot = Locations.kourendGiantsSafeSpot_Hill.getNearestTile(Players.localPlayer());
						if(Map.isTileOnScreen(closestSafespot))
						{
							if(Walking.walkExact(closestSafespot)) Sleep.sleep(420,696);
						}
						
						else if(!Players.localPlayer().isMoving() && Walking.walk(closestSafespot))
						{
							Sleep.sleep(420,696);
						}
						return Timing.sleepLogNormalSleep();
					}
					
					final int boostedPrayer = Skills.getBoostedLevels(Skill.PRAYER);
					final int prayer = Skills.getRealLevel(Skill.PRAYER);
					
					if(prayer >= 44)
					{
						if(boostedPrayer > 8 && !Prayers.isActive(Prayer.EAGLE_EYE))
						{
							if(Prayers.isOpen())
							{
								Prayers.toggle(true, Prayer.EAGLE_EYE);
							}
							else Prayers.openTab();
							Sleep.sleep(69, 420);
						}
					}
					else if(prayer >= 26)
					{
						if(boostedPrayer > 8 && !Prayers.isActive(Prayer.HAWK_EYE))
						{
							if(Prayers.isOpen())
							{
								Prayers.toggle(true, Prayer.HAWK_EYE);
							}
							else Prayers.openTab();
							Sleep.sleep(69, 420);
						}
					}
					
					Mobs.fightMobRanged("Hill Giant", Locations.kourendGiantsKillingArea_Hill, 8);
					return Timing.sleepLogNormalSleep();
    			}
				else if(Locations.isInKourend())
				{
					if(Bank.isOpen()) Bank.close();
					
					
					if(!Locations.kourendGiantsCaveEntrance.contains(Players.localPlayer()))
					{
						if(Locations.dreambotFuckedWCGuildSouth.contains(Players.localPlayer()))
						{
							if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedWCGuildDestSouth))
							{
								MethodProvider.log("Walking to cave, avoiding dreambot fucked area wc guild");
								Sleep.sleep(69, 420);
							}
						} else 
						{
							if(Walking.shouldWalk(6) && Walking.walk(Locations.kourendGiantsCaveEntrance.getCenter())) 
							{
								MethodProvider.log("Walking to cave");
								Sleep.sleep(69, 420);
							}
						}
					} else 
					{
						//check for cave entrance
						Filter<GameObject> caveFilter = c -> 
							c != null && 
							c.exists() && 
							c.getName().contains("Cave") && 
							c.hasAction("Enter");
						GameObject cave = GameObjects.closest(caveFilter);
						if(cave != null)
						{
							if(cave.interact("Enter"))
							{
								MethodProvider.sleepUntil(() -> Locations.kourendGiantsCaveArea.contains(Players.localPlayer()),
										() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222), 50);
							}
							return Timing.sleepLogNormalSleep();
						}
						MethodProvider.log("[SCRIPT] -> Error! In Hill Giants cave entrance in Kourend, but cave not found!");
					}
				} 
				else 
				{
					Walkz.teleportWoodcuttingGuild(180000);
				}
			}
			else 
			{
				MethodProvider.log("Not enough darts: " + new Item(TrainRanged.getBestDart(),1).getName());
				TrainRanged.fulfillRangedDarts();
				
			}
		}
		return Timing.sleepLogNormalSleep();
	}
	
	public static void fightMobRanged(String name, Area killingZone, int maxHit)
    {
		
		if(Combat.shouldEatFood(maxHit)) Combat.eatFood();
		if(API.mode == modes.TRAIN_RANGE && TrainRanged.shouldDrinkBoost())
		{
			if(TrainRanged.drankRangedPotion()) Sleep.sleep(69, 420);
		}
		if(TrainRanged.ranged < 66)
		{
			if(org.dreambot.api.methods.combat.Combat.getCombatStyle() != CombatStyle.RANGED_RAPID)
			{
				org.dreambot.api.methods.combat.Combat.setCombatStyle(CombatStyle.RANGED_RAPID);
			}
		} else if(org.dreambot.api.methods.combat.Combat.getCombatStyle() != CombatStyle.RANGED_DEFENCE)
		{
			org.dreambot.api.methods.combat.Combat.setCombatStyle(CombatStyle.RANGED_DEFENCE);
		}
		if(cantReachThat)
		{
			Filter<NPC> allMobsFilter = mob ->
				mob != null && 
				mob.exists() && 
    			mob.hasAction("Attack") &&
				mob.getHealthPercent() >= 0 && 
				killingZone.contains(mob) && 
				mob.getName().equals(name) && 
				(org.dreambot.api.methods.combat.Combat.isInMultiCombat() || 
						(!org.dreambot.api.methods.combat.Combat.isInMultiCombat() && !mob.isInteractedWith()));
			List<NPC> allMobs = new ArrayList<NPC>();
			allMobs = NPCs.all(allMobsFilter);
			Collections.shuffle(allMobs);
			if(allMobs.get(0) == null) return;
			NPC randmob = allMobs.get(0);
			if(!randmob.interact("Attack"))
			{
				Walking.walk(randmob);
			} else MethodProvider.sleepUntil(() -> Players.localPlayer().isInteracting(randmob) && randmob.isInteracting(Players.localPlayer()),
					() -> Players.localPlayer().isMoving(),
					Sleep.calculate(2222, 2222),50);
			Sleep.sleep(420, 696);
			cantReachThat = false;
			return;
		}
		Filter<NPC> onMeMobsFilter = mob -> 
				mob != null &&
				mob.exists() && 
    			mob.getHealthPercent() >= 0 && 
				mob.isInteracting(Players.localPlayer());
    	List<NPC> mobsOnMe = NPCs.all(onMeMobsFilter);
    	if(!mobsOnMe.isEmpty())
    	{
    		String mobsOnMeString = "";
    		for(NPC mobOnMe : mobsOnMe)
    		{
    			mobsOnMeString = mobsOnMeString.concat(mobOnMe.getName()).concat(" ");
    		}
        	Main.customPaintText3 = "~~Have mobs on me: " + mobsOnMeString+"~~";
        	for(NPC mobOnMe : mobsOnMe)
        	{
        		if(Players.localPlayer().isInteracting(mobOnMe))
        		{
        			MethodProvider.sleep(Timing.sleepLogNormalSleep());
                	return;
        		}
        	}
    	}
    	//random wait
    	API.randomAFK(10);
    	//here we have no mobs attacking us already, so attack some mobs
    	Filter<NPC> newMobsFilter = mob -> 
    			mob != null &&
    			mob.exists() && 
    			mob.hasAction("Attack") &&
    			mob.getHealthPercent() >= 0 && 
				killingZone.contains(mob) && 
				mob.getName().equals(name) &&
				(org.dreambot.api.methods.combat.Combat.isInMultiCombat() || 
						(!org.dreambot.api.methods.combat.Combat.isInMultiCombat() && !mob.isInteractedWith()));
		NPC mob = NPCs.closest(newMobsFilter);
		if(mob != null)
		{
			Main.customPaintText3 = "~~Attacking mob: " + name+"~~";
			if(mob.interact("Attack"))
			{
				MethodProvider.sleepUntil(() -> Players.localPlayer().isInteracting(mob) && mob.isInteracting(Players.localPlayer()),
							() -> Players.localPlayer().isMoving(),
							Sleep.calculate(2222, 2222),50);
				if(Players.localPlayer().isInteracting(mob)) MethodProvider.sleep(Timing.sleepLogNormalSleep());
	        	return;
			}
		}
    }
}
