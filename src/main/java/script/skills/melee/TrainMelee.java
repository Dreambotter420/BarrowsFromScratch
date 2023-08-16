package script.skills.melee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.combat.CombatStyle;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Timer;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.*;
import script.skills.ranged.Mobs;

public class TrainMelee extends Leaf{
	public static boolean started = false; 
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_MELEE;
	}
	public static boolean onExit()
	{
		started = false;
		Mobs.mob = null;
		if(Locations.isInIsleOfSouls())
		{
			Walkz.exitIsleOfSouls(240000);
			return false;
		}
		if(Locations.kourendGiantsCaveArea.contains(Players.getLocal()))
		{
			Walkz.exitGiantsCave();
			return false;
		}
		return true;
	}
	public static boolean onStart()
	{
		started = true;
		return true;
	}
	public static Timer attStyleTimer = null;
	@Override
	public int onLoop() {
		if(DecisionLeaf.taskTimer.finished()) {
			Logger.log("[TIMEOUT] -> Melee training!");
            if(onExit())
            {
            	API.mode = null;
            }
            return Sleepz.sleepTiming();
		}
		if(Equipment.contains(getBestWeapon()) && !updateAttStyle()) {
			Logger.log("Failed to update attack style with best melee weapon equipped!");
			return Sleepz.sleepTiming();
		}
		if(Mobs.mob == null) Mobs.chooseMob(true);
        return Mobs.trainMob(true);
	}
	public static boolean fulfillMelee_Pots_Stam_Bass()
    {
    	InvEquip.clearAll();

    	setBestMeleeEquipment();
    	int qty = (int) Calculations.nextGaussianRandom(5,2);
    	InvEquip.addInvyItem(id.superAtt4, 1, qty, false, (int) Calculations.nextGaussianRandom(30, 5));
    	InvEquip.addInvyItem(id.superStr4, 1, qty, false, (int) Calculations.nextGaussianRandom(30, 5));
    	if(InvEquip.bankContains(id.staminas))
    	{
    		InvEquip.addInvyItem(InvEquip.getBankItem(id.staminas), 1, 1, false, 0);
    	}
    	else InvEquip.addInvyItem(id.stamina4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(id.bass, 5, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			Logger.log("[TRAIN MELEE] -> Fulfilled equipment correctly!");
			return true;
		} else 
		{
			Logger.log("[TRAIN MELEE] -> NOT fulfilled equipment correctly!");
			return false;
		}
    	
    }
	public static Skill getMeleeSkillToTrain()
	{
		final int att = Skills.getRealLevel(Skill.ATTACK);
		final int str = Skills.getRealLevel(Skill.STRENGTH);
		final int def = Skills.getRealLevel(Skill.DEFENCE);
		int lowestStat = 0;
		//train all stats to base 40 first
		if(att < 40 || str < 40 || def < 40)
		{
			List<Integer> meleeStats = new ArrayList<Integer>();
			meleeStats.add(att);
			meleeStats.add(str);
			meleeStats.add(def);
			Collections.sort(meleeStats);
			lowestStat = meleeStats.get(0);
		}
		//then train str to 60, then att, then def to 70+
		else if(str < 60) lowestStat = str;
		else if(att < 60) lowestStat = att;
		else lowestStat = def;
		//compare lowest melee stat to all melee stats, starting with str, to consider currently needed att schtoil
		if(str == lowestStat) return Skill.STRENGTH;
		else if(att == lowestStat) return Skill.ATTACK;
		else return Skill.DEFENCE;
	}
	public static boolean updateAttStyle()
	{
		if(attStyleTimer != null && !attStyleTimer.isPaused() && !attStyleTimer.finished()) 
		{
			Main.paint_levels = "Time until check att style for switching: " + Timer.formatTime(attStyleTimer.remaining());
			return true;
		}
		if(attStyleTimer == null || attStyleTimer.isPaused() || attStyleTimer.finished())
		{
			final int att = Skills.getRealLevel(Skill.ATTACK);
			final int str = Skills.getRealLevel(Skill.STRENGTH);
			final int def = Skills.getRealLevel(Skill.DEFENCE);
			int lowestStat = 0;
			//train all stats to base 40 first
			if(att < 40 || str < 40 || def < 40)
			{
				List<Integer> meleeStats = new ArrayList<Integer>();
				meleeStats.add(att);
				meleeStats.add(str);
				meleeStats.add(def);
				Collections.sort(meleeStats);
				lowestStat = meleeStats.get(0);
			}
			//then train str to 60, then att, then def to 70+
			else if(str < 60) lowestStat = str;
			else if(att < 60) lowestStat = att;
			else lowestStat = def;
			CombatStyle neededStyle = null;
			//compare lowest melee stat to all melee stats, starting with str, to consider best att schtoil
			if(str == lowestStat) neededStyle = CombatStyle.STRENGTH;
			else if(att == lowestStat) neededStyle = CombatStyle.ATTACK;
			else neededStyle = CombatStyle.DEFENCE;
			if(Combat.getCombatStyle() == neededStyle)
			{
				int timer = 0;
				if(lowestStat >= 40) timer = (int) Calculations.nextGaussianRandom((1500000 + (100000*lowestStat)), 1000000);
				else if(lowestStat >= 30) timer = (int) Calculations.nextGaussianRandom((500000 + (10000*lowestStat)), 100000);
				else timer = (int) Calculations.nextGaussianRandom((300000 + (10000*lowestStat)), 100000);
				attStyleTimer = new Timer(timer);
				return true;
			}
			if(Bank.isOpen())
			{
				Bankz.close();
				return false;
			}
			if(GrandExchange.isOpen())
			{
				GrandExchangg.close();
				return false;
			}
			Combat.setCombatStyle(neededStyle);
		}
		return false;
	}
	
	 public static void setBestMeleeEquipment()
	 {
	    	InvEquip.setEquipItem(EquipmentSlot.SHIELD, getBestShieldSlot());
	    	InvEquip.setEquipItem(EquipmentSlot.HAT, getBestHeadSlot());
	    	InvEquip.setEquipItem(EquipmentSlot.CHEST, getBestBodySlot());
	    	InvEquip.setEquipItem(EquipmentSlot.HANDS, getBestHandSlot());
	    	InvEquip.setEquipItem(EquipmentSlot.FEET, getBestBootSlot());
	    	InvEquip.setEquipItem(EquipmentSlot.AMULET, getBestAmuletSlot());
	    	InvEquip.setEquipItem(EquipmentSlot.LEGS, getBestLegSlot());
	    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
	    	boolean foundCape = false;
	    	for(int capeID : id.randCapes)
	    	{
	    		if(Equipment.contains(capeID)) 
	    		{
	    			InvEquip.setEquipItem(EquipmentSlot.CAPE, capeID);
	    			foundCape = true;
	    			break;
	    		}
	    	}
	    	if(!foundCape) InvEquip.setEquipItem(EquipmentSlot.CAPE, getBestCapeSlot());
	    	InvEquip.setEquipItem(EquipmentSlot.WEAPON,getBestWeapon());
			if(getBestWeapon() != getBestWeapon()) InvEquip.addInvyItem(getBestWeapon(), 1, 1, false, 1);
			if(getNextNextBestWeapon() != getBestWeapon() && 
					getNextNextBestWeapon() != getBestWeapon()) InvEquip.addInvyItem(getNextNextBestWeapon(), 1, 1, false, 1);
	}
	public static int getBestHeadSlot()
    {
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 60) return id.dragonMedHelm;
    	if(def >= 40) return id.runeFullHelm;
    	if(def >= 30) return id.addyFullHelm;
    	if(def >= 20) return id.mithFullHelm;
    	if(def >= 5) return id.steelFullHelm;
    	return id.ironFullHelm;
    }

	
	public static int getBestLegSlot()
    {
		final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40) return id.runePlatelegs;
    	if(def >= 30) return id.addyPlatelegs;
    	if(def >= 20) return id.mithPlatelegs;
    	if(def >= 5) return id.steelPlatelegs;
    	return id.ironPlatelegs;
    }
	
	
	public static int getBestBodySlot()
    {
		final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40) {
    		return id.runeChainbody;
    	}
    	if(def >= 30) return id.addyPlatebody;
    	if(def >= 20) return id.mithPlatebody;
    	if(def >= 5) return id.steelPlatebody;
    	return id.ironPlatebody;
    }
	
	public static int getBestCapeSlot()
	{
		if(InvEquip.equipmentContains(id.randCapes)) return InvEquip.getEquipmentItem(id.randCapes);
    	if(InvEquip.invyContains(id.randCapes)) return InvEquip.getInvyItem(id.randCapes);
    	if(InvEquip.bankContains(id.randCapes)) return InvEquip.getBankItem(id.randCapes);
    	return id.randCape;
	}
	
	
	public static int getBestBootSlot()
	{
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(def >= 60) return id.dragonBoots;
		if(def >= 40) return id.runeBoots;
		if(def >= 30) return id.addyBoots;
    	return id.leatherBoots;
	}
	
	public static int getBestHandSlot()
	{
    	return InvEquip.combat;
	}
	
	public static int getBestAmuletSlot()
	{
		return InvEquip.glory;
	}
	
	
	public static int getBestShieldSlot()
    {
		final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40) return id.runeKiteshield;
    	if(def >= 30) return id.addyKiteshield;
    	if(def >= 20) return id.mithKiteshield;
    	if(def >= 5) return id.steelKiteshield;
    	return id.ironKiteshield;
    }
	
    
    public static int getBestWeapon()
    {
		final int att = Skills.getRealLevel(Skill.ATTACK);
    	if(att >= 40) return id.brineSabre;
    	if(att >= 30) return id.addyScimmy;
    	if(att >= 20) return id.mithScimmy;
    	if(att >= 5) return id.steelScimmy;
    	return id.ironScimmy;
    }
    public static int getNextBestWeapon()
    {
    	final int att = Skills.getRealLevel(Skill.ATTACK);
    	if(att >= 30) return id.brineSabre;
    	if(att >= 20) return id.addyScimmy;
    	if(att >= 5) return id.mithScimmy;
    	return id.steelScimmy;
    }
    public static int getNextNextBestWeapon()
    {
    	final int att = Skills.getRealLevel(Skill.ATTACK);
    	if(att >= 20) return id.brineSabre;
    	if(att >= 5) return id.addyScimmy;
    	return id.mithScimmy;
    }
    
    
}
