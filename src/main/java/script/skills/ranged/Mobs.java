package script.skills.ranged;

import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.CombatStyle;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.Mobs.Mob;
import script.utilities.API;
import script.utilities.API.modes;
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;

public class Mobs {
	public static Mob mob = null;
	public static enum Mob {
		BOAR,
		HILL_GIANT
	}
	public static void chooseMob()
	{
		//final int range = TrainRanged.ranged;
		mob = Mob.BOAR;
		//if(range < 30) mob = Mob.BOAR;
		//else mob = Mob.HILL_GIANT;
		
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
					if(TrainRanged.shouldEatFood(1))
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
						Walkz.exitCave();
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
	public static int trainHillGiants()
	{
		//think its config: 414: 262144 -> 393216
		if(PlayerSettings.getConfig(414) != 393216) //need config values of "not having been to kourend at all yet"
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
					if(TrainRanged.shouldEatFood(1))
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
				//if(nearbyGroundItemExists()) grabNearbyGroundItem();
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
				if(Locations.kourendGiantsCaveArea.contains(Players.localPlayer()))
    			{
					
						
					Mobs.fightMobRanged("Hill Giant", Locations.kourendGiantsKillingArea_Hill, 4);
					return Timing.sleepLogNormalSleep();
    			}
				else if(Locations.isInKourend())
				{
					if(Bank.isOpen()) Bank.close();
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
							MethodProvider.sleepUntil(() -> Locations.kourendGiantsCaveEntrance.contains(Players.localPlayer()),
									() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222), 50);
						}
						return Timing.sleepLogNormalSleep();
					}
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
					} else MethodProvider.log("[SCRIPT] -> Error! In Hill Giants cave entrance in Kourend, but cave not found!");
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
		
		if(TrainRanged.shouldEatFood(1)) Combat.eatFood();
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
        	if(Players.localPlayer().isInteracting(mobsOnMe.get(0))) 
        	{
        		MethodProvider.sleep(Timing.sleepLogNormalSleep());
            	return;
        	}
        	
    	}
    	//random wait
    	API.randomAFK();
    	//here we have no mobs attacking us already, so attack some mobs
    	Filter<NPC> newMobsFilter = mob -> 
    			mob != null &&
    			mob.exists() && 
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
				MethodProvider.sleepUntil(() -> mob.isInteracting(Players.localPlayer()), () -> Players.localPlayer().isInteracting(mob), Sleep.calculate(2222, 2222), 50);
				if(Players.localPlayer().isInteracting(mob)) MethodProvider.sleep(Timing.sleepLogNormalSleep());
	        	return;
			}
		}
    }
}
