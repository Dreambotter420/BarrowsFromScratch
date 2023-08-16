package script.skills.ranged;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
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
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.skills.melee.TrainMelee;
import script.skills.slayer.Candlez;
import script.skills.slayer.SlayerSettings;
import script.skills.slayer.TrainSlayer;
import script.utilities.API;
import script.utilities.API.modes;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.ItemsOnGround;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Sleepz;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;

public class Mobs {
	public static Mob mob = null;
	public static enum Mob {
		BOAR,
		HILL_GIANT,
		GIANT_FROG,
		SANDCRAB
	}
	public static Area selectedHillGiantsArea = null;
	public static boolean cantReachThat = false;
	/**
	 * Picks and sets an appropriate mob to fight. Set boolean melee to choose melee gear + setup or false for ranged.
	 * @param melee
	 */
	public static void chooseMob(boolean melee)
	{
		final int att = Skills.getRealLevel(Skill.ATTACK);
		final int str = Skills.getRealLevel(Skill.STRENGTH);
		final int def = Skills.getRealLevel(Skill.DEFENCE);
		final int rng = Skills.getRealLevel(Skill.RANGED);
		
		if(Mobs.mob == null)
		{
			//if(true) Mobs.mob = Mob.SANDCRAB; //testing mobs
			if((int) Calculations.nextGaussianRandom(100, 50) >= 135) Mobs.mob = Mob.SANDCRAB;
		}
		if(melee)
		{
			if((att >= 15 && att < 40) || (str >= 15 && str < 40) && (int) Calculations.nextGaussianRandom(100, 50) >= 110) 
				Mobs.mob = Mob.GIANT_FROG;
			
			if(Mobs.mob == null)
			{
				if(att < 30 || str < 30 || def < 30) Mobs.mob = Mob.BOAR;
				else if(att < 45 || str < 45 || def < 45) Mobs.mob = Mob.HILL_GIANT;
				else Mobs.mob = Mob.SANDCRAB;
			}
		}
		else
		{
			if(rng >= 20 && rng <= 45 && (int) Calculations.nextGaussianRandom(100, 50) >= 110)
				Mobs.mob = Mob.GIANT_FROG;
			if(Mobs.mob == null)
			{
				if(rng < 30) Mobs.mob = Mob.BOAR;
				else mob = Mob.HILL_GIANT;
			}
		}
		Mobs.mob.name(); 
		Logger.log("Chose mob: "+Mobs.mob.name()+" using combat style: " +(melee ? "MELEE" : "RANGED"));
		if(Mobs.mob == Mob.HILL_GIANT)
		{
			if(!melee && def < 40) 
			{
				selectedHillGiantsArea = Locations.kourendGiantsKillingArea_Hill;
				Logger.log("Setting Kourend cave safespot for Hill Giants area");
			}
			else if((int) Calculations.nextGaussianRandom(100, 50) > 103) 
			{
				selectedHillGiantsArea = Locations.kourendGiantsKillingArea_Hill;
				Logger.log("Setting Kourend cave for Hill Giants area");
			}
			else 
			{
				selectedHillGiantsArea = Locations.hillGiantsValley;
				Logger.log("Setting Hill Giants Valley for Hill Giants area");
			}
		}
	}
	/**
	 * a chosen mob to fight should be already selected via Mob class. Boolean melee set to true for melee, false for ranged.
	 * @param melee
	 * @return
	 */
	public static int trainMob(boolean melee)
	{
		if(!Combat.isAutoRetaliateOn()) Combat.toggleAutoRetaliate(true);
		if(Mobs.mob == Mob.BOAR) return trainBoars(melee);
		else if(Mobs.mob == Mob.HILL_GIANT) return trainHillGiants(melee);
		else if(Mobs.mob == Mob.SANDCRAB) return trainSandCrabs(melee);
		else if(Mobs.mob == Mob.GIANT_FROG) return trainGiantFrogs(melee);
		else 
		{
			Logger.log("[SCRIPT] -> No mob selected in trainMob function!");
			return Sleepz.sleepTiming();
		}
	}
	public static int trainBoars(boolean melee)
	{
		Locations.isInKourend();
		if(!Locations.unlockedKourend) //fuck config value 414 we assume unlocked unless get teh game msg
		{
			Locations.unlockKourend();
			return Sleepz.sleepTiming();
		}
		//check for glory in invy + not equipped -> equip it
		if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
				!InvEquip.equipmentContains(InvEquip.wearableGlory)) 
		{
			InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
			Sleepz.sleep(420,696);
			return Sleepz.sleepTiming();
		}
		if(InvEquip.getInvyItem(InvEquip.wearableWealth) != 0 && 
				!InvEquip.equipmentContains(InvEquip.wearableWealth)) 
		{
			InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableWealth));
			Sleepz.sleep(420,696);
			return Sleepz.sleepTiming();
		}
		//check invy for best equipment
		if(melee)
		{
			if(Inventory.contains(TrainMelee.getBestWeapon()))
			{
				InvEquip.equipItem(TrainMelee.getBestWeapon());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainMelee.getBestWeapon()) ||
				(Combatz.shouldDrinkMeleeBoost() && !InvEquip.invyContains(id.superStrs) && !InvEquip.invyContains(id.superStrs)) ||
				(Combatz.shouldEatFood(3) && !InvEquip.invyContains(Combatz.foods)) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
				!InvEquip.equipmentContains(InvEquip.wearableGlory))
			{
				TrainMelee.fulfillMelee_Pots_Stam_Bass();
				return Sleepz.interactionTiming();
			}
		}
		else
		{
			if(Inventory.contains(TrainRanged.getBestDart()))
			{
				InvEquip.equipItem(TrainRanged.getBestDart());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainRanged.getBestDart()) ||
				(Combatz.shouldDrinkRangedBoost() && !InvEquip.invyContains(id.rangedPots)) ||
				(Combatz.shouldEatFood(3) && !InvEquip.invyContains(Combatz.foods)) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
				!InvEquip.equipmentContains(InvEquip.wearableGlory))
			{
				TrainRanged.fulfillRangedDartsStaminaGames();
				return Sleepz.interactionTiming();
			}
		}
		//have some food in invy
		if(Combatz.getFood() != null)
		{
			//should eat at 1 max hit
			if(Combatz.shouldEatFood(3))
			{
				Combatz.eatFood();
			}
		}
		//check for proper ranged lvl boost
		if(melee)
		{
			if(Combatz.shouldDrinkMeleeBoost())
			{
				if(Locations.boarZone.contains(Players.getLocal()))
				{
					Combatz.drinkMeleeBoost();
				}
			}
		}
		else
		{
			if(Combatz.shouldDrinkRangedBoost())
			{
				if(Locations.boarZone.contains(Players.getLocal()))
				{
					if(!Combatz.drinkRangeBoost()) Logger.log("Not drank ranged Potion");
				}
			}
		}
		
		//in location to kill mobs
		if(Locations.boarZone.contains(Players.getLocal()))
		{
			if(!melee) Mobs.fightMobRanged("Boar",Locations.boarZone);
			else Mobs.fightMobMelee("Boar",Locations.boarZone);
			return Sleepz.sleepTiming();
		}
		else if(Locations.isInKourend())
		{
			if(Bank.isOpen()) Bankz.close();
			if(Locations.kourendGiantsCaveArea.contains(Players.getLocal()))
			{
				if(!Walkz.exitGiantsCave()) Sleepz.sleep(420,696);
				return Sleepz.sleepTiming();
			}
			if(Locations.dreambotFuckedShayzien3.contains(Players.getLocal()))
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedShayzienDest3)) Sleepz.sleep(69, 420);
				return Sleepz.sleepTiming();
			}
			if(Locations.dreambotFuckedShayzien2.contains(Players.getLocal()))
			{
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedShayzienDest2)) Sleepz.sleep(69, 420);
				return Sleepz.sleepTiming();
			}
			if(Locations.dreambotFuckedShayzien.contains(Players.getLocal()))
			{
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedShayzienDest)) Sleepz.sleep(69, 420);
				return Sleepz.sleepTiming();
			}
			if(Walking.shouldWalk(6) && Walking.walk(Locations.boarZone.getCenter())) Sleepz.sleep(69, 420);
		} else 
		{
			Walkz.teleportWoodcuttingGuild(180000);
		}
		return Sleepz.sleepTiming();
	}
	public static int trainPlainMainlandSlayerTask(String mobName, Area killingZone, int maxHit,List<Integer> groundItemLoot)
	{
		//check for lantern on cave tasks
		if(mobName.contains("Cave bug") || mobName.contains("Cave slime"))
		{
			if(!Candlez.getLantern())
			{
				Logger.log("did not get lantern within timer limit!");
				return Sleepz.interactionTiming();
			}		
		}
		
		//check equipment for wealth, if none, get more
		if(!InvEquip.equipmentContains(InvEquip.wearableWealth)) 
		{
			if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
			else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
			else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
			else TrainRanged.fulfillRangedDartsStaminaAntidote();
			return Sleepz.sleepTiming();
		}
		
		//check invy for best darts, equip 
		if(Inventory.count(TrainRanged.getBestDart()) > 0) InvEquip.equipItem(TrainRanged.getBestDart());
		
		//check invy + equip for best darts, if have, do more checks, if not, fulfill setup
		if(Inventory.count(TrainRanged.getBestDart()) > 0 || Equipment.count(TrainRanged.getBestDart()) > 0)
		{
			//have some food in invy
			if(Combatz.getFood() != null)
			{
				//should eat at 1 max hit
				if(Combatz.shouldEatFood(maxHit))
				{
					Combatz.eatFood();
				}
			}
			else 
			{
				//no more food, fulfill setup
				if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
				else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
				else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
				else TrainRanged.fulfillRangedDartsStaminaAntidote();
				return Sleepz.sleepTiming();
			}
			
			if(mobName.contains("Cave crawler") || mobName.contains("Cave slime") || 
					mobName.contains("Kalphite Worker"))
			{
				if(!Combatz.hasAntidoteProtection())
				{
					if(!Combatz.drinkAntidote()) 
					{
						if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
						else TrainRanged.fulfillRangedDartsStaminaAntidote();
						return Sleepz.sleepTiming();
					}
				}
			}
			
			
			//check for proper ranged lvl boost
			if(Combatz.shouldDrinkRangedBoost())
			{
				//if have any ranged pots while needing a drink, wait until in killingzone to sip up
				if(InvEquip.invyContains(id.rangedPots))
				{
					if(killingZone.contains(Players.getLocal()))
					{
						if(!Combatz.drinkRangeBoost()) Logger.log("Not drank ranged Potion");
					}
				}
				else 
				{
					//need a ranged pot to drink but have none
					if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
					else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
					else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
					else TrainRanged.fulfillRangedDartsStaminaAntidote();
					return Sleepz.sleepTiming();
				}
			}
			
			if(Inventory.isFull())
			{
				if(!InvEquip.free1InvySpace())
				{
					if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
					else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
					else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
					else TrainRanged.fulfillRangedDartsStaminaAntidote();
					return Sleepz.sleepTiming();
				}
			}
			if(groundItemLoot != null && !groundItemLoot.isEmpty())
			{
				GroundItem gi = ItemsOnGround.getNearbyGroundItem(groundItemLoot,killingZone);
				if(gi != null)
				{
					ItemsOnGround.grabNearbyGroundItem(gi);
					return Sleepz.sleepTiming();
				}
			}
			
			
			//in location to kill mobs
			if(killingZone.contains(Players.getLocal()))
			{
				Logger.log("[SLAYER] -> in killingzone!");
				if(Dialoguez.handleDialogues()) return Sleepz.sleepTiming();
				if(Combat.isAutoRetaliateOn())
		    	{
		    		Combat.toggleAutoRetaliate(true);
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
						Logger.log("Need ice coolers");
						if((Inventory.count(InvEquip.iceCooler) + Bank.count(InvEquip.iceCooler) < 200))
						{
							TrainSlayer.buyItemTurael(InvEquip.iceCooler, 500);
							return Sleepz.sleepTiming();
						}
						TrainSlayer.fulfillRangedLizardsTask();
						return Sleepz.sleepTiming();
					}
					if(InvEquip.getWaterskinCharges() <= 2)
					{
						Logger.log("Have less than 2 waterskin charges: " + InvEquip.getWaterskinCharges() + ", exiting desert!");
						if(Locations.isInDesert()) Walkz.exitDesert();
						else 
						{
							TrainSlayer.fulfillRangedLizardsTask();
						}
						return Sleepz.sleepTiming();
					}
					Mobs.fightLizardsRanged(killingZone);
					return Sleepz.sleepTiming();
				}
				Mobs.fightMobRanged(mobName, killingZone);
				return Sleepz.sleepTiming();
			}
			Logger.log("Slayer not in killingzone");
			if(!Walkz.isStaminated())
			{
				Walkz.drinkStamina();
			}
			if(!Walking.isRunEnabled() && Walking.getRunEnergy() > 15) Walking.toggleRun();
			if(Locations.isInDesert())
			{
				Logger.log("In desert!");
				
				if(InvEquip.getWaterskinCharges() <= 2)
				{
					Logger.log("Have less than 2 waterskin charges: " + InvEquip.getWaterskinCharges() + ", exiting desert!");
					if(Locations.isInDesert()) Walkz.exitDesert();
					else 
					{
						TrainSlayer.fulfillRangedLizardsTask();
					}
					return Sleepz.sleepTiming();
				}
			}
			if(mobName.contains("Zombie"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				
				if(Locations.edgevilleDungeon.contains(Players.getLocal()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.zombiesEdgeville.getCenter())) Sleepz.sleep(420,696);
				}
				else
				{
					if(Locations.edgevilleTeleSpot.distance(Players.getLocal().getTile()) >= 50)
					{
						if(Walkz.useJewelry(InvEquip.glory, "Edgeville"))
						{
							return Sleepz.calculate(420,696);
						}
					}
					
					if(Walking.shouldWalk(6) && Walking.walk(Locations.zombiesEdgeville.getCenter())) Sleepz.sleep(420,696);
				}
				return Sleepz.sleepTiming();
			}
			if(mobName.contains("Skeleton"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				
				if(Locations.edgevilleDungeon.contains(Players.getLocal()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.skeletonsEdgeville.getCenter())) Sleepz.sleep(420,696);
				}
				else
				{
					if(Locations.edgevilleTeleSpot.distance(Players.getLocal().getTile()) >= 50)
					{
						if(Walkz.useJewelry(InvEquip.glory, "Edgeville"))
						{
							return Sleepz.calculate(420,696);
						}
					}
					
					if(Walking.shouldWalk(6) && Walking.walk(Locations.skeletonsEdgeville.getCenter())) Sleepz.sleep(420,696);
				}
				return Sleepz.sleepTiming();
			}
			if(mobName.contains("Seagull"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.seagullsSarim.getCenter().distance() >= 100 && Walkz.useJewelry(InvEquip.glory, "Draynor Village")) return Sleepz.interactionTiming();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.seagullsSarim.getCenter());
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Cave crawler"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.entireFremmyDungeon.contains(Players.getLocal()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.caveCrawlersFremmyCave.getCenter())) Sleepz.sleep(696, 420);
					return Sleepz.sleepTiming();
				}
				if(Locations.fremmyCaveEntrance.contains(Players.getLocal()))
				{
					GameObject caveEntrance = GameObjects.closest("Cave Entrance");
					if(caveEntrance == null) {
						Logger.log("Cave entrance to fremmy dungeon is null at entrance location!");
						return Sleepz.interactionTiming();
					}
					if(caveEntrance.interact("Enter"))
					{
						Sleep.sleepUntil(() -> Locations.entireFremmyDungeon.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(), 
								Sleepz.calculate(2222, 2222),50);
					}
					return Sleepz.sleepTiming();
				}
				if(Locations.fremmyCaveEntrance.getCenter().distance() > 175 || Locations.burthrope.contains(Players.getLocal()))
				{
					if(TrainRanged.fulfillRangedDartsStaminaAntidote())
					{
						Walkz.teleportCamelot(180000);
					}
					return Sleepz.sleepTiming();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.fremmyCaveEntrance)) Sleepz.sleep(420, 69);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Kalphite Worker"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.kalphiteCave.contains(Players.getLocal()))
				{
					if(Walking.shouldWalk(6) && Walking.walk(Locations.kalphiteWorkersArea.getCenter())) Sleepz.sleep(696, 420);
					return Sleepz.sleepTiming();
				}
				if(Locations.kalphiteCaveEntrance.contains(Players.getLocal()))
				{
					GameObject caveEntrance = GameObjects.closest("Cave");
					if(caveEntrance == null) {
						Logger.log("Cave entrance to kalphite caven is null at entrance location!");
						return Sleepz.interactionTiming();
					}
					if(caveEntrance.interact("Enter"))
					{
						Sleep.sleepUntil(() -> Locations.kalphiteCave.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(), 
								Sleepz.calculate(2222, 2222),50);
					}
					return Sleepz.sleepTiming();
				}
				if(Locations.isInDesert())
				{
					if(Walkz.walkPath(Paths.shantayPassToKalphiteCave)) Logger.log("Walked to Kalphites cave path!");
					return Sleepz.interactionTiming();
				}
				
				if(Walkz.enterDesert()) Sleepz.sleep(420, 69);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Giant spider"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.spidersGE.getCenter().distance() >= 100 && Walkz.useJewelry(InvEquip.wealth, "Grand Exchange")) return Sleepz.interactionTiming();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.spidersGE.getCenter());
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Bear Cub"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.bearsFremmy.getCenter().distance() >= 200 && Walkz.teleportCamelot(180000)) return Sleepz.interactionTiming();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.bearsFremmy.getCenter());
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Cow"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.cowsArdy.getCenter().distance() >= 150)
				{
					if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
					{
						InvEquip.buyItem(InvEquip.skills6, 1, 180000);
						return Sleepz.interactionTiming();
					}
					return Sleepz.interactionTiming();
				}
				if(Walking.shouldWalk(6)) Walking.walk(Locations.cowsArdy.getCenter());
				return Sleepz.interactionTiming();
			}
			
			if(mobName.contains("Monkey"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.monkeysArdyZoo.getCenter().distance() >= 150)
				{
					if(!Walkz.useJewelry(InvEquip.skills, "Fishing Guild"))
					{
						InvEquip.buyItem(InvEquip.skills6, 1, 180000);
						return Sleepz.interactionTiming();
					}
					return Sleepz.interactionTiming();
				}
				if(Walking.shouldWalk(6)) Walking.walk(Locations.monkeysArdyZoo.getCenter());
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Lizard"))
			{
				Logger.log("Lizards task!");
				
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				//check for ice coolers first
				if(!InvEquip.checkedBank()) return Sleepz.interactionTiming();
				
				if(Locations.isInDesert())
				{
					
					if(Locations.lizards.getCenter().distance() > 35)
					{
						Logger.log("Dist to lizards greater than 35");
						if(Inventory.count(InvEquip.iceCooler) < 200)
						{
							Logger.log("Need ice coolers");
							if((Inventory.count(InvEquip.iceCooler) + Bank.count(InvEquip.iceCooler) < 200))
							{
								TrainSlayer.buyItemTurael(InvEquip.iceCooler, 500);
								return Sleepz.sleepTiming();
							}
							TrainSlayer.fulfillRangedLizardsTask();
							return Sleepz.sleepTiming();
						}
					}
					if(Walkz.walkPath(Paths.shantayPassToLizards)) 
					{
						Logger.log("walked to lizards");
						Sleepz.sleep(420, 696);
					} else Logger.log("did not walk to lizards");
						
					return Sleepz.interactionTiming();
				}
				
				Logger.log("Not in desert");
				if(Inventory.count(InvEquip.iceCooler) < 200)
				{
					Logger.log("Need ice coolers");
					if((Inventory.count(InvEquip.iceCooler) + Bank.count(InvEquip.iceCooler) < 200))
					{
						TrainSlayer.buyItemTurael(InvEquip.iceCooler, 500);
						return Sleepz.sleepTiming();
					}
					TrainSlayer.fulfillRangedLizardsTask();
					return Sleepz.sleepTiming();
				}
				if(Locations.shantayPassArea.contains(Players.getLocal()))
				{
					Walkz.enterDesert();
					return Sleepz.interactionTiming();
				}
				if(Locations.shantayPassArea.getCenter().distance() >= 150)
				{
					if(!Walkz.useJewelry(InvEquip.glory, "Al Kharid"))
					{
						if(Walkz.useJewelry(InvEquip.duel, "PvP Arena"))
						{
							return Sleepz.interactionTiming();
						}
					}
					else return Sleepz.interactionTiming();
				}
				if(Walking.shouldWalk(6) && Walking.walk(Locations.shantayPassArea.getCenter())) return Sleepz.interactionTiming();
				return Sleepz.interactionTiming();
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
							return Sleepz.interactionTiming();
						}
						if(Locations.lumbridgeCaveEntrance.contains(Players.getLocal()))
						{
							GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
									g.getName().equals("Dark hole") && 
									g.getTile().equals(new Tile(3169,3172,0)));
							if(caveEntrance == null)
							{
								Logger.log("lumby swamp cave entrance null on top entrance location!");
								return Sleepz.interactionTiming();
							}
							if(Inventory.get(id.rope).useOn(caveEntrance))
							{
								Sleep.sleepUntil(() -> SlayerSettings.unlockedLumbyCave(), 
										() -> Players.getLocal().isMoving(),
										Sleepz.calculate(2222, 2222),50);
								return Sleepz.interactionTiming();
							}
						}
						if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
						{
							Walkz.teleportLumbridge(180000);
							return Sleepz.interactionTiming();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleepz.sleep(696, 420);
						return Sleepz.sleepTiming();
					}
					
					//here can now enter tha cave ... 
					if(Locations.entireLumbyCave.contains(Players.getLocal()))
					{
						if(Locations.caveHandSkipTile.equals(Players.getLocal().getTile()))
						{
							if(!Walking.isRunEnabled())
							{
								if(Walking.toggleRun()) Sleepz.sleep(420, 696);
								return Sleepz.interactionTiming();
							}
							if(Walking.walkExact(Locations.caveHandSkipTile2))
							{
								Sleepz.sleep(420, 696);
							}
							return Sleepz.interactionTiming();
						}
						if(Locations.lumbyCaveFoyer.contains(Players.getLocal()))
						{
							if(!Players.getLocal().isMoving() && Walking.walkExact(Locations.caveHandSkipTile)) Sleepz.sleep(420, 696);
							return Sleepz.interactionTiming();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.caveBugs.getCenter())) Sleepz.sleep(420, 696);
						return Sleepz.interactionTiming();
					}
					
					if(Locations.lumbridgeCaveEntrance.contains(Players.getLocal()))
					{
						GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
								g.getName().equals("Dark hole") && 
								g.getTile().equals(new Tile(3169,3172,0)));
						if(caveEntrance == null)
						{
							Logger.log("lumby swamp cave entrance null on top entrance location!");
							return Sleepz.interactionTiming();
						}
						if(caveEntrance.interact("Climb-down"))
						{
							Sleep.sleepUntil(() -> Locations.lumbyCaveFoyer.contains(Players.getLocal()), 
									() -> Players.getLocal().isMoving(),
									Sleepz.calculate(2222, 2222),50);
							return Sleepz.interactionTiming();
						}
					}
					if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
					{
						if(Locations.burthrope.contains(Players.getLocal()))
						{
							
						}
						Walkz.teleportLumbridge(180000);
						return Sleepz.interactionTiming();
					}
					if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleepz.sleep(696, 420);
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
							return Sleepz.interactionTiming();
						}
						if(Locations.lumbridgeCaveEntrance.contains(Players.getLocal()))
						{
							GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
									g.getName().equals("Dark hole") && 
									g.getTile().equals(new Tile(3169,3172,0)));
							if(caveEntrance == null)
							{
								Logger.log("lumby swamp cave entrance null on top entrance location!");
								return Sleepz.interactionTiming();
							}
							if(Inventory.get(id.rope).useOn(caveEntrance))
							{
								Sleep.sleepUntil(() -> SlayerSettings.unlockedLumbyCave(), 
										() -> Players.getLocal().isMoving(),
										Sleepz.calculate(2222, 2222),50);
								return Sleepz.interactionTiming();
							}
						}
						if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
						{
							Walkz.teleportLumbridge(180000);
							return Sleepz.interactionTiming();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleepz.sleep(696, 420);
						return Sleepz.sleepTiming();
					}
					
					//here can now enter tha cave ... 
					if(Locations.entireLumbyCave.contains(Players.getLocal()))
					{
						if(Locations.caveHandSkipTile.equals(Players.getLocal().getTile()))
						{
							if(!Walking.isRunEnabled())
							{
								if(Walking.toggleRun()) Sleepz.sleep(420, 696);
								return Sleepz.interactionTiming();
							}
							if(Walking.walkExact(Locations.caveHandSkipTile2))
							{
								Sleepz.sleep(420, 696);
							}
							return Sleepz.interactionTiming();
						}
						if(Locations.lumbyCaveFoyer.contains(Players.getLocal()))
						{
							if(!Players.getLocal().isMoving() && Walking.walkExact(Locations.caveHandSkipTile)) Sleepz.sleep(420, 696);
							return Sleepz.interactionTiming();
						}
						if(Walking.shouldWalk(6) && Walking.walk(Locations.caveSlimeArea.getCenter())) Sleepz.sleep(420, 696);
						return Sleepz.interactionTiming();
					}
					
					if(Locations.lumbridgeCaveEntrance.contains(Players.getLocal()))
					{
						GameObject caveEntrance = GameObjects.closest(g -> g!=null && 
								g.getName().equals("Dark hole") && 
								g.getTile().equals(new Tile(3169,3172,0)));
						if(caveEntrance == null)
						{
							Logger.log("lumby swamp cave entrance null on top entrance location!");
							return Sleepz.interactionTiming();
						}
						if(caveEntrance.interact("Climb-down"))
						{
							Sleep.sleepUntil(() -> Locations.lumbyCaveFoyer.contains(Players.getLocal()), 
									() -> Players.getLocal().isMoving(),
									Sleepz.calculate(2222, 2222),50);
							return Sleepz.interactionTiming();
						}
					}
					if(Locations.lumbridgeCaveEntrance.getCenter().distance() > 150)
					{
						Walkz.teleportLumbridge(180000);
						return Sleepz.interactionTiming();
					}
					if(Walking.shouldWalk(6) && Walking.walk(Locations.lumbridgeCaveEntrance.getCenter())) Sleepz.sleep(696, 420);
				}
				
			}
			if(mobName.contains("Goblin"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.lumbyCastle2.contains(Players.getLocal()))
				{
					GameObject door = GameObjects.closest(f -> f != null && f.getName().equals("Staircase"));
					if(door == null)
					{
						Logger.log("Lumby castle 2nd floor stairs null!");
						return Sleepz.interactionTiming();
					}
					if(!door.canReach()) 
					{
						Walking.walk(door);
						return Sleepz.interactionTiming();
					}
					if(door.interact("Climb-down"))
					{
						Sleep.sleepUntil(() -> !Locations.lumbyCastle2.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(), 
								Sleepz.calculate(2222, 2222),50);
					}
					return Sleepz.interactionTiming();
				}
				if(Locations.lumbyCastle1.contains(Players.getLocal()))
				{
					GameObject door = GameObjects.closest(f -> f != null && f.getName().equals("Staircase"));
					if(door == null)
					{
						Logger.log("Lumby castle 1st floor stairs null!");
						return Sleepz.interactionTiming();
					}
					if(!door.canReach()) 
					{
						Walking.walk(door);
						return Sleepz.interactionTiming();
					}
					if(door.interact("Climb-down"))
					{
						Sleep.sleepUntil(() -> !Locations.lumbyCastle1.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(), 
								Sleepz.calculate(2222, 2222),50);
					}
					return Sleepz.interactionTiming();
				}
				if(Locations.goblins.getCenter().distance() > 150)
				{
					if(!Walkz.teleportLumbridge(180000)) InvEquip.buyItem(id.lumbridgeTele, 10, 180000);
					return Sleepz.sleepTiming();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.goblins.getCenter())) Sleepz.sleep(420, 696);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Scorpion"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
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
								return Sleepz.sleepTiming();
							}
						}
					}
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.scorpions.getCenter())) Sleepz.sleep(420, 696);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Dwarf"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.dwarfs.getCenter().distance() > 90)
				{
					if(!Walkz.useJewelry(InvEquip.glory,"Edgeville")) TrainRanged.fulfillRangedDartsStaminaAntidote();
					return Sleepz.sleepTiming();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.dwarfs.getCenter())) Sleepz.sleep(420, 696);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Icefiend"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.icefiends.getCenter().distance() > 105)
				{
					if(!Walkz.useJewelry(InvEquip.glory,"Edgeville")) TrainRanged.fulfillRangedDartsStaminaAntidote();
					return Sleepz.sleepTiming();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.icefiends.getCenter())) Sleepz.sleep(420, 696);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Bat"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.bats.getCenter().distance() > 175)
				{
					if(!Walkz.teleportVarrock(180000)) InvEquip.buyItem(id.varrockTele, 10, 180000);
					return Sleepz.sleepTiming();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.bats.getCenter())) Sleepz.sleep(420, 696);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Giant rat"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.giantRats.getCenter().distance() > 150)
				{
					if(!Walkz.teleportLumbridge(180000)) InvEquip.buyItem(id.lumbridgeTele, 10, 180000);
					return Sleepz.sleepTiming();
				}
				
				if(Walking.shouldWalk(6) && Walking.walk(Locations.giantRats.getCenter())) Sleepz.sleep(420, 696);
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Wolf") || mobName.contains("Minotaur"))
			{
				if(Locations.isInKourend())
				{
					Walkz.exitKourend(180000);
					return Sleepz.sleepTiming();
				}
				if(Locations.strongholdLvl1.contains(Players.getLocal()))
				{
					if(Camera.getPitch() < 334) 
					{
						int tmp = (int) Calculations.nextGaussianRandom(375, 50);
						if(tmp < 340) tmp = 340;
						if(tmp > 382) tmp = 382;
						Camera.rotateToPitch(tmp);
						return Sleepz.interactionTiming();
					}
					if(Dialoguez.handleDialogues()) return Sleepz.sleepTiming();
					if(Locations.strongholdLvl1Foyer.contains(Players.getLocal()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1859,5238,0)));
						if(door == null)
						{
							Logger.log("Stronghold 1st door null!");
							return Sleepz.interactionTiming();
						}
						if(door.interact("Open"))
						{
							Sleep.sleepUntil(Dialogues::inDialogue,
									() -> Players.getLocal().isMoving(), 
									Sleepz.calculate(2222, 2222),50);
						}
						return Sleepz.interactionTiming();
					}
					if(Locations.strongholdDoor1.contains(Players.getLocal()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1859, 5235, 0)));
						if(door == null)
						{
							Logger.log("Stronghold 2nd door null!");
							return Sleepz.interactionTiming();
						}
						if(door.interact("Open"))
						{
							Sleep.sleepUntil(Dialogues::inDialogue,
									() -> Players.getLocal().isMoving(), 
									Sleepz.calculate(2222, 2222),50);
						}
						return Sleepz.interactionTiming();
					}if(Locations.strongholdGoblins.contains(Players.getLocal()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1865, 5227, 0)));
						if(door == null)
						{
							Logger.log("Stronghold 3rd door null!");
							return Sleepz.interactionTiming();
						}
						if(door.interact("Open"))
						{
							Sleep.sleepUntil(Dialogues::inDialogue,
									() -> Players.getLocal().isMoving(), 
									Sleepz.calculate(2222, 2222),50);
						}
						return Sleepz.interactionTiming();
					}
					if(Locations.strongholdDoor2.contains(Players.getLocal()))
					{
						GameObject door = GameObjects.closest(f -> f != null && f.getID() == 19207 && f.getTile().equals(new Tile(1868, 5227, 0)));
						if(door == null)
						{
							Logger.log("Stronghold 4th door null!");
							return Sleepz.interactionTiming();
						}
						if(door.interact("Open"))
						{
							Sleep.sleepUntil(Dialogues::inDialogue,
									() -> Players.getLocal().isMoving(), 
									Sleepz.calculate(2222, 2222),50);
						}
						return Sleepz.interactionTiming();
					}
					return Sleepz.interactionTiming();
				}
				if(Locations.strongholdEntrance.contains(Players.getLocal()))
				{
					GameObject entrance = GameObjects.closest(f -> f != null && f.getName().contains("Entrance") && f.hasAction("Climb-down"));
					if(entrance == null)
					{
						Logger.log("Stronghold of security entrance null when should be near it!");
						return Sleepz.interactionTiming();
					}
					if(entrance.interact("Climb-down"))
					{
						Sleep.sleepUntil(() -> Locations.strongholdLvl1Foyer.contains(Players.getLocal()),
								() -> Players.getLocal().isMoving(), 
								Sleepz.calculate(2222, 2222),50);
					}
					return Sleepz.interactionTiming();
				}
				if(Locations.strongholdEntrance.getCenter().distance() >= 150 && Walkz.useJewelry(InvEquip.glory, "Edgeville")) return Sleepz.interactionTiming();
				if(Walking.shouldWalk(6)) Walking.walk(Locations.strongholdEntrance.getCenter());
				return Sleepz.interactionTiming();
			}
			if(mobName.contains("Guard dog"))
			{
				if(Locations.isInKourend())
				{
					if(Walking.shouldWalk(6) && Walking.walk(killingZone.getCenter())) Sleepz.sleep(420,696);
				} else 
				{
					Walkz.teleportWoodcuttingGuild(180000);
					return Sleepz.interactionTiming();
				}
					
			}
			if(mobName.contains("Forgotten Soul"))
			{
				if(Locations.isInIsleOfSouls())
				{
					if(Walkz.walkToForgottenSouls())
					{
						Logger.log("Walked toward forgotten souls");
					}
					return Sleepz.sleepTiming();
				}
				if(InvEquip.invyContains(id.staminas))
				{
					if(Locations.edgevilleSoulsPortal.contains(Players.getLocal()))
					{
						Filter<GameObject> portalFilter = p -> 
							p != null && 
							p.getName().equals("Soul Wars Portal") && 
							p.hasAction("Enter");
						GameObject portal = GameObjects.closest(portalFilter);
						if(portal == null)
						{
							Logger.log("Portal is null in portal area :-( Souls wars at edgeville");
							return Sleepz.sleepTiming();
						}
						if(portal.interact("Enter"))
						{
							Sleep.sleepUntil(() -> Locations.isleOfSouls.contains(Players.getLocal()), Sleepz.calculate(4444, 2222));
							Sleepz.sleep(420, 696);
						}
						return Sleepz.sleepTiming();
					}
					
					if(Locations.edgevilleSoulsPortal.getCenter().distance() > 100)
					{
						if(Walkz.useJewelry(InvEquip.glory, "Edgeville")) 
						{
							return Sleepz.interactionTiming();
						}
					}
					
					if(Walking.shouldWalk(6) && Walking.walk(Locations.edgevilleSoulsPortal)) Sleepz.sleep(420, 696);
				}
				else
				{
					TrainRanged.fulfillRangedDartsStaminaAntidote();
				}
			}
		}
		else 
		{
			Logger.log("Not enough darts: " + new Item(TrainRanged.getBestDart(),1).getName());
			if(mobName.contains("Cave bug")) TrainSlayer.fulfillRangedCaveBugsTask();
			else if(mobName.contains("Cave slime")) TrainSlayer.fulfillRangedCaveSlimesTask();
			else if(mobName.contains("Lizard")) TrainSlayer.fulfillRangedLizardsTask();
			else TrainRanged.fulfillRangedDartsStaminaAntidote();
		}
		return Sleepz.sleepTiming();
	}
	public static void fightLizardsRanged(Area killingZone)
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
		if(Combatz.shouldEatFood(15)) Combatz.eatFood();
		if(API.mode == modes.TRAIN_RANGE && Combatz.shouldDrinkRangedBoost())
		{
			if(Combatz.drinkRangeBoost()) Sleepz.sleep(69, 420);
		}
		TrainRanged.updateAttStyle();
		org.dreambot.api.wrappers.interactive.Character lizard = Players.getLocal().getInteractingCharacter();
		if(lizard != null && 
				(lizard.getName().equals("Small Lizard") || lizard.getName().equals("Desert Lizard")))
		{
			Logger.log("interacting lizard!");
			if(lizard.getHealthPercent() <= 20)
			{
				if(Inventory.get(InvEquip.iceCooler).useOn(lizard))
				{
					Logger.log("Used icecooler on lizard!");
					Sleep.sleepUntil(() -> lizard.getInteractingCharacter() == null, Sleepz.calculate(2222,2222));
				}
			}
			
			Sleepz.sleep();
        	return;
		}
		Filter<NPC> onMeMobsFilter = mob -> 
				mob != null &&
				(mob.getName().equals("Small Lizard") || mob.getName().equals("Desert Lizard")) &&
				mob.isInteracting(Players.getLocal());
		List<NPC> mobsOnMe = NPCs.all(onMeMobsFilter);
		if(!mobsOnMe.isEmpty())
		{
			String mobsOnMeString = "";
			for(NPC mobOnMe : mobsOnMe)
			{
				mobsOnMeString = mobsOnMeString.concat(mobOnMe.getName()).concat(" ");
			}
	    	Main.paint_subTask = "~~Have mobs on me: " + mobsOnMeString+"~~";
	    	for(NPC mobOnMe : mobsOnMe)
	    	{
	    		if(Players.getLocal().isInteracting(mobOnMe)) 
		    	{
	    			if(mobOnMe.getHealthPercent() <= 20)
	    			{
	    				if(Inventory.get(InvEquip.iceCooler).useOn(mobOnMe))
	    				{
	    					Logger.log("Used icecooler on lizard!");
	    					Sleep.sleepUntil(() -> mobOnMe.getInteractingCharacter() == null, Sleepz.calculate(2222,2222));
	    				}
	    			}
	    			
	    			Sleepz.sleep();
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
			Main.paint_subTask = "~~Attacking mob: " + mob.getName()+"~~";
			if(mob.interact("Attack"))
			{
				Sleep.sleepUntil(() -> Players.getLocal().isInteracting(mob) && mob.isInteracting(Players.getLocal()), Sleepz.calculate(2222, 2222));
				if(Players.getLocal().isInteracting(mob)) Sleepz.sleep();
	        	return;
			}
		}
	}
	/**
	 * Trains at sand crabs, set melee to true for melee gear, false for ranged gear
	 * @param melee
	 * @return
	 */
	public static int trainSandCrabs(boolean melee)
	{
		//equip wealth + glory if in invy
		if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
				!InvEquip.equipmentContains(InvEquip.wearableGlory)) 
		{
			InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
			Sleepz.sleep(420,696);
			return Sleepz.sleepTiming();
		}
		if(InvEquip.getInvyItem(InvEquip.wearableWealth) != 0 && 
				!InvEquip.equipmentContains(InvEquip.wearableWealth)) 
		{
			InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableWealth));
			Sleepz.sleep(420,696);
			return Sleepz.sleepTiming();
		}
		//check for missing critical training equipment
		if(melee)
		{
			if(Inventory.contains(TrainMelee.getBestWeapon()))
			{
				InvEquip.equipItem(TrainMelee.getBestWeapon());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainMelee.getBestWeapon()) ||
				(Combatz.shouldDrinkMeleeBoost() && !InvEquip.invyContains(id.superStrs) && !InvEquip.invyContains(id.superStrs)) ||
				(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
				(!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth))  || 
				(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)))
			{
				TrainMelee.fulfillMelee_Pots_Stam_Bass();
				return Sleepz.interactionTiming();
			}
		}
		else
		{
			if(Inventory.contains(TrainRanged.getBestDart()))
			{
				InvEquip.equipItem(TrainRanged.getBestDart());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainRanged.getBestDart()) ||
				(Combatz.shouldDrinkRangedBoost() && !InvEquip.invyContains(id.rangedPots)) ||
				(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
				!InvEquip.equipmentContains(InvEquip.wearableGlory))
			{
				TrainRanged.fulfillRangedDarts();
				return Sleepz.interactionTiming();
			}
		}
		
		if(Locations.isInIsleOfSouls())
		{
			if(Combatz.shouldEatFood(6)) Combatz.eatFood();
			GroundItem gi = ItemsOnGround.getNearbyGroundItem(ItemsOnGround.sandcrabsLoot,Locations.isleOfSouls);
			if(gi != null)
			{
				ItemsOnGround.grabNearbyGroundItem(gi);
				return Sleepz.sleepTiming();
			}
			if(Locations.sandcrabSouls1.equals(Players.getLocal().getTile()) || Locations.sandcrabSouls2.equals(Players.getLocal().getTile()))
			{
				if(Combat.isAutoRetaliateOn())
		    	{
		    		Combat.toggleAutoRetaliate(true);
		    	}
				if(needReset) 
				{
					reset();
					return Sleepz.sleepTiming();
				}
				if(sandcrabIdleResetTimer == null) {
					sandcrabIdleResetTimer = new Timer((int) Calculations.nextGaussianRandom(180000, 50000));
				}
				if(sandcrabIdleResetTimer.finished())
				{
					Sleepz.sleepTiming();
					Mouse.click(Players.getLocal(), true);
					Mouse.moveOutsideScreen();
					return Sleepz.sleepTiming();
				}
				if(melee && Combatz.shouldDrinkMeleeBoost()) Combatz.drinkMeleeBoost();
				else if(Combatz.shouldDrinkRangedBoost()) Combatz.drinkRangeBoost();
				checkFightSandcrabs();
				return Sleepz.sleepTiming();
			}
			if(Locations.sandcrabsArea1.contains(Players.getLocal()))
			{
				if(Walking.shouldWalk(6) && Walking.walkExact(Locations.sandcrabSouls1)) Sleepz.sleep(666,696);
				return Sleepz.sleepTiming();
			}
			if(Locations.sandcrabsArea2.contains(Players.getLocal()))
			{
				if(Walking.shouldWalk(6) && Walking.walkExact(Locations.sandcrabSouls2)) Sleepz.sleep(666,696);
				return Sleepz.sleepTiming();
			}
			if(Locations.sandcrabsArea2.getCenter().distance() <= 25)
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.sandcrabSouls2)) Sleepz.sleep(666,696);
				return Sleepz.sleepTiming();
			}
			if(!Walkz.walkPath(Paths.soulWarsLobbyToSandcrabs))
			{
				if(Walking.shouldWalk(6) && Walking.walk(Locations.sandcrabSouls1)) Sleepz.sleep(420, 696);
			}
			return Sleepz.sleepTiming();
		}
		if(Locations.edgevilleSoulsPortal.contains(Players.getLocal()))
		{
			Filter<GameObject> portalFilter = p -> 
				p != null && 
				p.getName().equals("Soul Wars Portal") && 
				p.hasAction("Enter");
			GameObject portal = GameObjects.closest(portalFilter);
			if(portal == null)
			{
				Logger.log("Portal is null in portal area :-( Souls wars at edgeville");
				return Sleepz.sleepTiming();
			}
			if(portal.interact("Enter"))
			{
				Sleep.sleepUntil(() -> Locations.isleOfSouls.contains(Players.getLocal()), Sleepz.calculate(4444, 2222));
				Sleepz.sleep(420, 696);
			}
			return Sleepz.sleepTiming();
		}
		
		if(Locations.edgevilleSoulsPortal.getCenter().distance() > 100)
		{
			if(Locations.isInKourend())
			if(Walkz.useJewelry(InvEquip.glory, "Edgeville")) 
			{
				return Sleepz.interactionTiming();
			}
		}
		if(Walking.shouldWalk(6) && Walking.walk(Locations.edgevilleSoulsPortal)) Sleepz.sleep(420, 696);
		return Sleepz.sleepTiming();
	}
	public static Timer sandcrabIdleResetTimer = null;
	public static Timer sandcrabIdleCheckTimer = null;
	public static void checkFightSandcrabs()
	{
		NPC rockCrabOnMe = NPCs.closest(n -> n!=null&&
				n.distance() < 5 &&
				n.getName().equals("Sand Crab") && 
				n.hasAction("Attack") && 
				n.getInteractingCharacter() != null && 
				n.getInteractingCharacter().getName().equals(Players.getLocal().getName()));
		if(rockCrabOnMe != null || 
				(Players.getLocal().getInteractingCharacter() != null && 
				Players.getLocal().getInteractingCharacter().getName().equals("Sand Crab")))
		{
			sandcrabIdleCheckTimer = null;
			Sleepz.sleep(69,1111);
			Main.paint_subTask = "Have Sand Crab on me!";
			return;
		}
		
		NPC rockCrabAttackable = NPCs.closest(n -> n!=null&&
				n.distance() < 5 &&
				n.getName().equals("Sand Crab") && 
				n.hasAction("Attack"));
		if(rockCrabAttackable != null)
		{
			sandcrabIdleCheckTimer = null;
			if(rockCrabAttackable.interact("Attack"))
			{
				Main.paint_subTask = "Attacking Sand Crab!";
				Sleepz.sleep(696,1111);
			}
			return;
		}
		
		NPC rockCrabIdle = NPCs.closest(n -> n != null &&
				n.getName().equals("Sandy rocks") && 
				n.distance() <= 1);
		if(rockCrabIdle != null)
		{
			//here we are not in combat with a sandcrab, there are none wandering within 5 tiles of us, and there are some idle on the floor... reset!
			if(sandcrabIdleCheckTimer == null)
			{
				int timeout = (int) Calculations.nextGaussianRandom(15000, 5000);
				if(timeout < 5000) timeout = 5000;
				sandcrabIdleCheckTimer = new Timer(timeout);
				Sleepz.sleep(69, 2222);
				return;
			}
			if(!sandcrabIdleCheckTimer.finished())
			{
				Sleepz.sleep(69, 2222);
				return;
			}
			//10-20 sec wait timer finished, go reset!
			needReset = true;
			return;
		}
		sandcrabIdleCheckTimer = null;
		Sleepz.sleep(69,696);
		return;
	}
	public static boolean needReset = false;
	private static final Area resetWest = new Area(2247, 2780, 2242, 2793, 0);
	private static final Area resetNorth = new Area(2279, 2843, 2267, 2839, 0);
	public static void reset()
	{
		Area resetArea = null;
		if((int) Calculations.nextGaussianRandom(50, 10) > 50) resetArea = resetWest;
		else resetArea = resetNorth;
		
		Tile resetTile = resetArea.getRandomTile();
		while(resetTile == null)
		{
			Logger.log("Reset area random tile grabbed is null! Getting new one..");
			resetTile = resetArea.getRandomTile();
			Sleep.sleep(10,50);
		}
		Logger.log("Random area reset tile is: " + resetTile.toString());
		final Tile comeBackTile = Players.getLocal().getTile();
		if(Locations.sandcrabsArea1.contains(comeBackTile))
		{
			Logger.log("Entering reset function from sandcrabs 2-spot NORTH to reset area: " + (resetArea.equals(resetWest) ? "WEST" : "NORTH"));
		}
		else if(Locations.sandcrabsArea2.contains(comeBackTile))
		{
			Logger.log("Entering reset function from sandcrabs 2-spot WEST to reset area: " + (resetArea.equals(resetWest) ? "WEST" : "NORTH"));
		}
		boolean enteredResetLocation = false;
		int timeout = (int) Calculations.nextGaussianRandom(60000, 10000);
		if(timeout < 45000) timeout = 45000;
		Timer resetTimer = new Timer(timeout);
		while(!resetTimer.finished() && !resetTimer.isPaused() && 
				!ScriptManager.getScriptManager().isPaused() && 
				ScriptManager.getScriptManager().isRunning())
		{
			Sleepz.sleep(69, 696);
			if(Walking.getRunEnergy() >= 20 && !Walking.isRunEnabled())
			{
				if(Walking.toggleRun())
				{
					Sleepz.sleep(69, 696);
				}
			}
			if(resetArea.contains(Players.getLocal())) 
			{
				Logger.log("Entered reset location! Running back...");
				enteredResetLocation = true;
			}
				
			if(enteredResetLocation)
			{
				if(Locations.sandcrabsArea1.contains(Players.getLocal()))
				{
					needReset = false;
					return;
				}
				if(Locations.sandcrabsArea2.contains(Players.getLocal()))
				{
					needReset = false;
					return;
				}
				if(Players.getLocal().getTile().equals(comeBackTile)) 
				{
					needReset = false;
					return;
				}
				if(comeBackTile.distance() <= 6) 
				{
					if(Walking.shouldWalk(6) && Walking.walkExact(comeBackTile)) Sleepz.sleep(696, 420);
					continue;
				}
				if(Walking.shouldWalk(6) && Walking.walk(comeBackTile)) Sleepz.sleep(696, 420);
				continue;
			}
			if(Walking.shouldWalk(6))
			{
				if(Walking.walk(resetTile)) Sleepz.sleep(696, 420);
				else
				{
					Logger.log("Reset regular walk failed, attempting to pathz walk");
					if(resetArea.equals(resetWest))
					{
						Walkz.walkPath(Paths.sandcrabsToResetWest);
					}
					else if(resetArea.equals(resetWest))
					{
						Walkz.walkPath(Paths.sandcrabsToResetNorth);
					}
				}
			}
		}
	}
	/**
	 * Pass boolean melee as true to fight hill giants in their area, false to safespot as ranged if below 40 def
	 * @param melee
	 * @return
	 */
	public static int trainHillGiants(boolean melee)
	{
		if(selectedHillGiantsArea == Locations.kourendGiantsKillingArea_Hill)
		{
			Locations.isInKourend();
			if(!Locations.unlockedKourend) //fuck config value 414 we assume unlocked unless get teh game msg
			{
				Locations.unlockKourend();
				return Sleepz.sleepTiming();
			}else
			{
				//can travel to kourend now
				
				//check for glory in invy + not equipped -> equip it
				if(InvEquip.invyContains(InvEquip.wearableGlory) && 
						!InvEquip.equipmentContains(InvEquip.wearableGlory)) 
				{
					InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
					Sleepz.sleep(420,696);
					return Sleepz.sleepTiming();
				}
				if(InvEquip.getInvyItem(InvEquip.wearableWealth) != 0 && 
						!InvEquip.equipmentContains(InvEquip.wearableWealth)) 
				{
					InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableWealth));
					Sleepz.sleep(420,696);
					return Sleepz.sleepTiming();
				}
				//check invy for best equipment
				if(melee)
				{
					if(Inventory.contains(TrainMelee.getBestWeapon()))
					{
						InvEquip.equipItem(TrainMelee.getBestWeapon());
						return Sleepz.sleepTiming();
					}
					if(!Equipment.contains(TrainMelee.getBestWeapon()) ||
						(Combatz.shouldDrinkMeleeBoost() && !InvEquip.invyContains(id.superStrs) && !InvEquip.invyContains(id.superStrs)) ||
						(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
						!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
						!InvEquip.equipmentContains(InvEquip.wearableGlory))
					{
						TrainMelee.fulfillMelee_Pots_Stam_Bass();
						return Sleepz.interactionTiming();
					}
				}
				else
				{
					if(Inventory.contains(TrainRanged.getBestDart()))
					{
						InvEquip.equipItem(TrainRanged.getBestDart());
						return Sleepz.sleepTiming();
					}
					if(!Equipment.contains(TrainRanged.getBestDart()) ||
						(Combatz.shouldDrinkRangedBoost() && !InvEquip.invyContains(id.rangedPots)) ||
						(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
						(!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth))  || 
						(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)))
					{
						Logger.log("Test");
						TrainRanged.fulfillRangedDartsStamina();
						return Sleepz.interactionTiming();
					}
				}
				
				//in location to kill mobs
				if(Locations.kourendGiantsCaveArea.contains(Players.getLocal()))
				{
					if(Dialoguez.handleDialogues()) return Sleepz.calculate(222,696);
					if(Inventory.isFull())
					{
						if(!InvEquip.free1InvySpace())
						{
							if(melee) TrainMelee.fulfillMelee_Pots_Stam_Bass();
							else TrainRanged.fulfillRangedDartsStamina();
							return Sleepz.interactionTiming();
						}
					}
					if(Combatz.shouldEatFood(6))
					{
						Combatz.eatFood();
					}
					GroundItem gi = ItemsOnGround.getNearbyGroundItem(ItemsOnGround.hillGiantsLoot,Locations.kourendGiantsKillingArea_Hill);
					if(gi != null)
					{
						ItemsOnGround.grabNearbyGroundItem(gi);
						return Sleepz.sleepTiming();
					}
					
					//must be OK to fight hill giants now 
					//check for proper ranged lvl boost
					if(melee)
					{
						if(InvEquip.invyContains(id.superStrs) || InvEquip.invyContains(id.superAtts))
						{
							if(Combatz.shouldDrinkMeleeBoost())
							{
								Combatz.drinkMeleeBoost();
							}	
						}
					}
					else
					{
						if(InvEquip.invyContains(id.rangedPots))
						{
							if(Combatz.shouldDrinkRangedBoost())
							{
								Combatz.drinkRangeBoost();
							}	
						}
					}
					//safespot
					if(!melee && Skills.getRealLevel(Skill.DEFENCE) < 40 && selectedHillGiantsArea == Locations.kourendGiantsKillingArea_Hill)
					{
						if(!Locations.kourendGiantsSafeSpot_Hill.contains(Players.getLocal()))
						{
							final Tile closestSafespot = Locations.kourendGiantsSafeSpot_Hill.getNearestTile(Players.getLocal());
							if(Map.isTileOnScreen(closestSafespot))
							{
								if(Walking.walkExact(closestSafespot)) Sleepz.sleep(420,696);
							}
							
							else if(!Players.getLocal().isMoving() && Walking.walk(closestSafespot))
							{
								Sleepz.sleep(420,696);
							}
							return Sleepz.sleepTiming();
						}
					}
					else if(!Locations.kourendGiantsKillingArea_Hill.contains(Players.getLocal()))
					{
						if(Walking.shouldWalk(6) && Walking.walk(Locations.kourendGiantsKillingArea_Hill.getCenter())) Sleepz.sleep(696,420);
						return Sleepz.sleepTiming();
					}
					
					activateOffensivePrayers(melee);
					Sleepz.sleep(69, 2222);
					if(melee)Mobs.fightMobMelee("Hill Giant", Locations.kourendGiantsKillingArea_Hill);
					else Mobs.fightMobRanged("Hill Giant", Locations.kourendGiantsKillingArea_Hill);
					return Sleepz.sleepTiming();
				}
				else if(Locations.isInKourend())
				{
					if(Bank.isOpen()) Bankz.close();
					if(InvEquip.invyContains(id.staminas) && !Walkz.isStaminated()) 
					{
						Walkz.drinkStamina();
						return Sleepz.sleepTiming();
					}
					if(!Locations.kourendGiantsCaveEntrance.contains(Players.getLocal()))
					{
						if(!Walkz.walkPath(Paths.woodcuttingGuildToHillGiantsCave))
						{
							//backup paths to hill giants cave stuff :-(
							if(Locations.dreambotFuckedWCGuildSouth.contains(Players.getLocal()))
							{
								if(Walking.shouldWalk(6) && Walking.walk(Locations.dreambotFuckedWCGuildDestSouth))
								{
									Logger.log("Walking to cave, avoiding dreambot fucked area wc guild");
									Sleepz.sleep(69, 420);
								}
							} else 
							{
								if(Walking.shouldWalk(6) && Walking.walk(Locations.kourendGiantsCaveEntrance.getCenter())) 
								{
									Logger.log("Walking to cave");
									Sleepz.sleep(69, 420);
								}
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
								Sleep.sleepUntil(() -> Locations.kourendGiantsCaveArea.contains(Players.getLocal()),
										() -> Players.getLocal().isMoving(),Sleepz.calculate(2222,2222), 50);
							}
							return Sleepz.sleepTiming();
						}
						Logger.log("[SCRIPT] -> Error! In Hill Giants cave entrance in Kourend, but cave not found!");
					}
				} 
				else 
				{
					Logger.log("Not considered to be inside hill giants cave or Kourend! Teleporting to woodcutting guild..");
					Walkz.teleportWoodcuttingGuild(180000);
				}
			}
			return Sleepz.calculate(111,696);
		}
		
		//check invy for best equipment
		if(melee)
		{
			if(Inventory.contains(TrainMelee.getBestWeapon()))
			{
				InvEquip.equipItem(TrainMelee.getBestWeapon());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainMelee.getBestWeapon()) ||
				(Combatz.shouldDrinkMeleeBoost() && !InvEquip.invyContains(id.superStrs) && !InvEquip.invyContains(id.superStrs)) ||
				(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
				(!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth))  || 
				(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)))
			{
				TrainMelee.fulfillMelee_Pots_Stam_Bass();
				return Sleepz.interactionTiming();
			}
		}
		else
		{
			if(Inventory.contains(TrainRanged.getBestDart()))
			{
				InvEquip.equipItem(TrainRanged.getBestDart());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainRanged.getBestDart()) ||
				(Combatz.shouldDrinkRangedBoost() && !InvEquip.invyContains(id.rangedPots)) ||
				(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
				(!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth))  || 
				(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)))
			{
				Logger.log("Test2");
				TrainRanged.fulfillRangedDartsStamina();
				return Sleepz.interactionTiming();
			}
		}
		
		//in location to kill mobs
		if(selectedHillGiantsArea.contains(Players.getLocal()))
		{
			if(Dialoguez.handleDialogues()) return Sleepz.calculate(222,696);
			//check for glory in invy + not equipped -> equip it
			if(InvEquip.invyContains(InvEquip.wearableGlory) && 
					!InvEquip.equipmentContains(InvEquip.wearableGlory)) 
			{
				InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
				Sleepz.sleep(420,696);
				return Sleepz.sleepTiming();
			}
			if(InvEquip.getInvyItem(InvEquip.wearableWealth) != 0 && 
					!InvEquip.equipmentContains(InvEquip.wearableWealth)) 
			{
				InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableWealth));
				Sleepz.sleep(420,696);
				return Sleepz.sleepTiming();
			}
			if(Inventory.isFull())
			{
				if(!InvEquip.free1InvySpace())
				{
					if(melee) TrainMelee.fulfillMelee_Pots_Stam_Bass();
					else TrainRanged.fulfillRangedDartsStamina();
					return Sleepz.interactionTiming();
				}
			}
			if(Combatz.shouldEatFood(6))
			{
				Combatz.eatFood();
			}
			GroundItem gi = ItemsOnGround.getNearbyGroundItem(ItemsOnGround.hillGiantsLoot,selectedHillGiantsArea);
			if(gi != null)
			{
				ItemsOnGround.grabNearbyGroundItem(gi);
				return Sleepz.sleepTiming();
			}
			//must be OK to fight hill giants now 
			//check for proper ranged lvl boost
			if(melee)
			{
				if(InvEquip.invyContains(id.superStrs) || InvEquip.invyContains(id.superAtts))
				{
					if(Combatz.shouldDrinkMeleeBoost())
					{
						Combatz.drinkMeleeBoost();
					}	
				}
			}
			else
			{
				if(InvEquip.invyContains(id.rangedPots))
				{
					if(Combatz.shouldDrinkRangedBoost())
					{
						Combatz.drinkRangeBoost();
					}	
				}
			}
			activateOffensivePrayers(melee);
			Sleepz.sleep(69, 2222);
			if(melee) Mobs.fightMobMelee("Hill Giant", selectedHillGiantsArea);
			else Mobs.fightMobRanged("Hill Giant", selectedHillGiantsArea);
			return Sleepz.sleepTiming();
		}
		if(Locations.hillGiantsValley.getCenter().distance() > 125) 
		{
			if(!Walkz.useJewelry(InvEquip.duel,"PvP Arena"))
			{
				if(InvEquip.bankContains(InvEquip.wearableDuel))
				{
					InvEquip.withdrawOne(InvEquip.getBankItem(InvEquip.wearableDuel),180000);
					return Sleepz.calculate(420,696);
				}
				InvEquip.buyItem(InvEquip.duel8, 3, 180000);
				return Sleepz.calculate(420,696);
			}
			return Sleepz.calculate(420,696);
		}
		if(Walking.shouldWalk(6) && Walking.walk(Locations.hillGiantsValley.getCenter())) Sleepz.sleep(696,420);
		return Sleepz.sleepTiming();
	}
	public static int trainGiantFrogs(boolean melee)
	{
		if(Locations.isInKourend())
		{
			Walkz.exitKourend(180000);
			return Sleepz.sleepTiming();
		}
		
		//check for glory in invy + not equipped -> equip it
		if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
				!InvEquip.equipmentContains(InvEquip.wearableGlory)) 
		{
			InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
			Sleepz.sleep(420,696);
			return Sleepz.sleepTiming();
		}
		if(InvEquip.getInvyItem(InvEquip.wearableWealth) != 0 && 
				!InvEquip.equipmentContains(InvEquip.wearableWealth)) 
		{
			InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableWealth));
			Sleepz.sleep(420,696);
			return Sleepz.sleepTiming();
		}
		//check invy for best equipment
		if(melee)
		{
			if(Inventory.contains(TrainMelee.getBestWeapon()))
			{
				InvEquip.equipItem(TrainMelee.getBestWeapon());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainMelee.getBestWeapon()) ||
				(Combatz.shouldDrinkMeleeBoost() && !InvEquip.invyContains(id.superStrs) && !InvEquip.invyContains(id.superStrs)) ||
				(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
				(!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth))  || 
				(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)))
			{
				TrainMelee.fulfillMelee_Pots_Stam_Bass();
				return Sleepz.interactionTiming();
			}
		}
		else
		{
			if(Inventory.contains(TrainRanged.getBestDart()))
			{
				InvEquip.equipItem(TrainRanged.getBestDart());
				return Sleepz.sleepTiming();
			}
			if(!Equipment.contains(TrainRanged.getBestDart()) ||
				(Combatz.shouldDrinkRangedBoost() && !InvEquip.invyContains(id.rangedPots)) ||
				(Combatz.shouldEatFood(5) && !InvEquip.invyContains(Combatz.foods)) || 
				(!InvEquip.equipmentContains(InvEquip.wearableWealth) && !InvEquip.invyContains(InvEquip.wearableWealth))  || 
				(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory)))
			{
				TrainRanged.fulfillRangedDartsStaminaGames();
				return Sleepz.interactionTiming();
			}
		}
		
		//in location to kill mobs
		if(Locations.lumbyGiantFrogs.contains(Players.getLocal()))
		{
			if(Dialoguez.handleDialogues()) return Sleepz.calculate(222,696);
			if(Combatz.shouldEatFood(6))
			{
				Combatz.eatFood();
			}
			
			//must be OK to fight
			//check for proper lvl boost
			if(melee)
			{
				if(InvEquip.invyContains(id.superStrs) || InvEquip.invyContains(id.superAtts))
				{
					if(Combatz.shouldDrinkMeleeBoost())
					{
						Combatz.drinkMeleeBoost();
					}	
				}
			}
			else
			{
				if(InvEquip.invyContains(id.rangedPots))
				{
					if(Combatz.shouldDrinkRangedBoost())
					{
						Combatz.drinkRangeBoost();
					}	
				}
			}
			
			activateOffensivePrayers(melee);
			
			Sleepz.sleep(69, 2222);
			if(melee)Mobs.fightMobMelee("Giant frog", Locations.lumbyGiantFrogs);
			else Mobs.fightMobRanged("Giant frog", Locations.lumbyGiantFrogs);
			return Sleepz.sleepTiming();
		}
		if(Locations.lumbyGiantFrogs.getCenter().distance() > 150)
		{
			Walkz.teleportLumbridge(180000);
			return Sleepz.interactionTiming();
		} 
		else 
		{
			Walkz.walkToArea(Locations.lumbyGiantFrogs);
		}
		return Sleepz.sleepTiming();
	}
	public static void activateOffensivePrayers(boolean melee)
	{
		final int boostedPrayer = Skills.getBoostedLevel(Skill.PRAYER);
		final int prayer = Skills.getRealLevel(Skill.PRAYER);
		if(melee)
		{
			if(prayer >= 31)
			{
				if(boostedPrayer > 8 && !Prayers.isActive(Prayer.ULTIMATE_STRENGTH))
				{
					if(Prayers.isOpen())
					{
						Prayers.toggle(true, Prayer.ULTIMATE_STRENGTH);
					}
					else Tabz.open(Tab.PRAYER);
					Sleepz.sleep(69, 420);
				}
			}
			else if(prayer >= 13)
			{
				if(boostedPrayer > 8 && !Prayers.isActive(Prayer.SUPERHUMAN_STRENGTH))
				{
					if(Prayers.isOpen())
					{
						Prayers.toggle(true, Prayer.SUPERHUMAN_STRENGTH);
					}
					else Tabz.open(Tab.PRAYER);
					Sleepz.sleep(69, 420);
				}
			}
			else if(prayer >= 4)
			{
				if(boostedPrayer > 8 && !Prayers.isActive(Prayer.BURST_OF_STRENGTH))
				{
					if(Prayers.isOpen())
					{
						Prayers.toggle(true, Prayer.BURST_OF_STRENGTH);
					}
					else Tabz.open(Tab.PRAYER);
					Sleepz.sleep(69, 420);
				}
			}
		}
		else 
		{
			if(prayer >= 44)
			{
				if(boostedPrayer > 8 && !Prayers.isActive(Prayer.EAGLE_EYE))
				{
					if(Prayers.isOpen())
					{
						Prayers.toggle(true, Prayer.EAGLE_EYE);
					}
					else Tabz.open(Tab.PRAYER);
					Sleepz.sleep(69, 420);
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
					else Tabz.open(Tab.PRAYER);
					Sleepz.sleep(69, 420);
				}
			}
		}
	}
	public static void fightMobRanged(String name, Area killingZone)
    {
		if(API.mode == modes.TRAIN_RANGE && Combatz.shouldDrinkRangedBoost())
		{
			if(Combatz.drinkRangeBoost()) Sleepz.sleep(69, 420);
		}
		TrainRanged.updateAttStyle();
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
			} else Sleep.sleepUntil(() -> Players.getLocal().isInteracting(randmob) && randmob.isInteracting(Players.getLocal()),
					() -> Players.getLocal().isMoving(),
					Sleepz.calculate(2222, 2222),50);
			Sleepz.sleep(420, 696);
			cantReachThat = false;
			return;
		}
		Filter<NPC> onMeMobsFilter = mob -> 
				mob != null &&
				mob.exists() && 
    			mob.getHealthPercent() >= 0 && 
				mob.isInteracting(Players.getLocal());
    	List<NPC> mobsOnMe = NPCs.all(onMeMobsFilter);
    	if(!mobsOnMe.isEmpty())
    	{
    		String mobsOnMeString = "";
    		for(NPC mobOnMe : mobsOnMe)
    		{
    			mobsOnMeString = mobsOnMeString.concat(mobOnMe.getName()).concat(" ");
    		}
        	Main.paint_subTask = "~~Have mobs on me: " + mobsOnMeString+"~~";
        	for(NPC mobOnMe : mobsOnMe)
        	{
        		if(Players.getLocal().isInteracting(mobOnMe))
        		{
        			Sleepz.sleep();
                	return;
        		}
        	}
    	}
    	if(Players.getLocal().getInteractingCharacter() != null && 
    			Players.getLocal().getInteractingCharacter().getName().contains(name))
    	{
    		//we are already in interaction with something
    		Sleepz.sleep();
    		return;
    	}
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
			Main.paint_subTask = "~~Attacking mob: " + name+"~~";
			if(mob.interact("Attack"))
			{
				Sleep.sleepUntil(() -> Players.getLocal().isInteracting(mob) && mob.isInteracting(Players.getLocal()),
							() -> Players.getLocal().isMoving(),
							Sleepz.calculate(2222, 2222),50);
				if(Players.getLocal().isInteracting(mob)) Sleepz.sleep();
	        	return;
			}
		}
    }
	public static void fightMobMelee(String name, Area killingZone)
    {
		if(cantReachThat)
		{
			Filter<NPC> allMobsFilter = mob ->
				mob != null && 
				mob.exists() && 
    			mob.hasAction("Attack") &&
				mob.getHealthPercent() >= 0 && 
				killingZone.contains(mob) && 
				mob.getName().equals(name) && 
				mob.canReach() &&
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
			}
			else Sleep.sleepUntil(() -> Players.getLocal().isInteracting(randmob) && randmob.isInteracting(Players.getLocal()),
					() -> Players.getLocal().isMoving(),
					Sleepz.calculate(3333, 2222),50);
			Sleepz.sleep(420, 696);
			cantReachThat = false;
			return;
		}
		Filter<NPC> onMeMobsFilter = mob -> 
				mob != null &&
				mob.exists() && 
    			mob.getHealthPercent() >= 0 && 
				mob.isInteracting(Players.getLocal());
    	List<NPC> mobsOnMe = NPCs.all(onMeMobsFilter);
    	if(!mobsOnMe.isEmpty())
    	{
    		String mobsOnMeString = "";
    		for(NPC mobOnMe : mobsOnMe)
    		{
    			mobsOnMeString = mobsOnMeString.concat(mobOnMe.getName()).concat(" ");
    		}
        	Main.paint_subTask = "~~Have mobs on me: " + mobsOnMeString+"~~";
        	for(NPC mobOnMe : mobsOnMe)
        	{
        		if(Players.getLocal().isInteracting(mobOnMe))
        		{
        			Sleepz.sleep();
                	return;
        		}
        	}
    	}
    	if(Players.getLocal().getInteractingCharacter() != null && 
    			Players.getLocal().getInteractingCharacter().getName().contains(name))
    	{
    		//we are already in interaction with something
    		Sleepz.sleep();
    		return;
    	}
    	
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
			Main.paint_subTask = "~~Attacking mob: " + name+"~~";
			if(mob.interact("Attack"))
			{
				Sleep.sleepUntil(() -> Players.getLocal().isInteracting(mob) && mob.isInteracting(Players.getLocal()),
							() -> Players.getLocal().isMoving(),
							Sleepz.calculate(2222, 2222),50);
				if(Players.getLocal().isInteracting(mob)) Sleepz.sleep();
	        	return;
			}
		}
    }
}
