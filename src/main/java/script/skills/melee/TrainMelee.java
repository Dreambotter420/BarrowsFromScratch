package script.skills.melee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.combat.CombatStyle;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Branch;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.horrorfromthedeep.HorrorFromTheDeep;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.Mobs;
import script.skills.ranged.Mobs.Mob;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;

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
			if(!Walkz.useJewelry(InvEquip.glory, "Edgeville") && 
					!Walkz.useJewelry(InvEquip.wealth, "Grand Exchange") && 
					!Walkz.useJewelry(InvEquip.combat, "Ranging Guild"))
			{
				MethodProvider.log("Appear to be stuck on isle of souls, teleporting home??");
				if(Players.localPlayer().isAnimating()) 
				{
					Sleep.sleep(2222, 2222);
					return false;
				}
				Magic.castSpell(Normal.HOME_TELEPORT);
				Sleep.sleep(3333,3333);
			}
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
		if(DecisionLeaf.taskTimer.finished())
		{
			MethodProvider.log("[TIMEOUT] -> Melee training!");
            if(onExit())
            {
            	API.mode = null;
            }
			
            return Timing.sleepLogNormalSleep();
		}
		if(Equipment.contains(getBestWeapon()) && !updateAttStyle()) 
		{
			MethodProvider.log("Failed to update attack style with best melee weapon equipped!");
			return Timing.sleepLogNormalSleep();
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
			MethodProvider.log("[TRAIN MELEE] -> Fulfilled equipment correctly!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN MELEE] -> NOT fulfilled equipment correctly!");
			return false;
		}
    	
    }
	public static boolean updateAttStyle()
	{
		if(attStyleTimer != null && !attStyleTimer.isPaused() && !attStyleTimer.finished()) 
		{
			Main.customPaintText4 = "Time until check att style for switching: " + Timer.formatTime(attStyleTimer.remaining());
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
				Bank.close();
				return false;
			}
			if(GrandExchange.isOpen())
			{
				GrandExchange.close();
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
	    	for(int capeID : TrainRanged.randCapes)
	    	{
	    		if(Equipment.contains(capeID)) 
	    		{
	    			InvEquip.setEquipItem(EquipmentSlot.CAPE, capeID);
	    			foundCape = true;
	    			break;
	    		}
	    	}
	    	if(!foundCape) InvEquip.setEquipItem(EquipmentSlot.CAPE, getBestCapeSlot());
	    	InvEquip.addInvyItem(getBestWeapon(), 1, 1, false, 1);
			if(getBestWeapon() != getBestWeapon()) InvEquip.addInvyItem(getBestWeapon(), 1, 1, false, 1);
			if(getNextNextBestWeapon() != getBestWeapon() && 
					getNextNextBestWeapon() != getBestWeapon()) InvEquip.addInvyItem(getNextNextBestWeapon(), 1, 1, false, 1);
	}
	public static final int dragonMedHelm = 1149;
	public static final int runeFullHelm = 1163;
	public static final int addyFullHelm = 1161;
	public static final int mithFullHelm = 1159;
	public static final int steelFullHelm = 1157;
	public static final int ironFullHelm = 1153;
    public static int getBestHeadSlot()
    {
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 60) return dragonMedHelm;
    	if(def >= 40) return runeFullHelm;
    	if(def >= 30) return addyFullHelm;
    	if(def >= 20) return mithFullHelm;
    	if(def >= 5) return steelFullHelm;
    	return ironFullHelm;
    }

	public static final int runePlatelegs = 1079;
	public static final int addyPlatelegs = 1073;
	public static final int mithPlatelegs = 1071;
	public static final int steelPlatelegs = 1069;
	public static final int ironPlatelegs = 1067;
	public static int getBestLegSlot()
    {
		final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40) return runePlatelegs;
    	if(def >= 30) return addyPlatelegs;
    	if(def >= 20) return mithPlatelegs;
    	if(def >= 5) return steelPlatelegs;
    	return ironPlatelegs;
    }
	
	public static final int runeChainbody = 1113;
	public static final int addyPlatebody = 1123;
	public static final int mithPlatebody = 1121;
	public static final int steelPlatebody = 1119;
	public static final int ironPlatebody = 1115;
	public static int getBestBodySlot()
    {
		final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40) return runeChainbody;
    	if(def >= 30) return addyPlatebody;
    	if(def >= 20) return mithPlatebody;
    	if(def >= 5) return steelPlatebody;
    	return ironPlatebody;
    }
	
	public static int getBestCapeSlot()
	{
		if(InvEquip.equipmentContains(TrainRanged.randCapes)) return InvEquip.getEquipmentItem(TrainRanged.randCapes);
    	if(InvEquip.invyContains(TrainRanged.randCapes)) return InvEquip.getInvyItem(TrainRanged.randCapes);
    	if(InvEquip.bankContains(TrainRanged.randCapes)) return InvEquip.getBankItem(TrainRanged.randCapes);
    	return TrainRanged.randCape;
	}
	
	public static final int dragonBoots = 11840;
	public static final int runeBoots = 4131;
	public static final int addyBoots = 4129;
	public static int getBestBootSlot()
	{
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(def >= 60) return dragonBoots;
		if(def >= 40) return runeBoots;
		if(def >= 30) return addyBoots;
    	return TrainRanged.leatherBoots;
	}
	
	public static int getBestHandSlot()
	{
    	return InvEquip.combat;
	}
	
	public static int getBestAmuletSlot()
	{
		return InvEquip.glory;
	}
	
	public static final int runeKiteshield = 1201;
	public static final int addyKiteshield = 1199;
	public static final int mithKiteshield = 1197;
	public static final int steelKiteshield = 1193;
	public static final int ironKiteshield = 1191;
	public static int getBestShieldSlot()
    {
		final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40) return runeKiteshield;
    	if(def >= 30) return addyKiteshield;
    	if(def >= 20) return mithKiteshield;
    	if(def >= 5) return steelKiteshield;
    	return ironKiteshield;
    }
	
    public static final int brineSabre = 11037;
	public static final int addyScimmy = 1331;
	public static final int mithScimmy = 1329;
	public static final int steelScimmy = 1325;
	public static final int ironScimmy = 1323;
    public static int getBestWeapon()
    {
		final int att = Skills.getRealLevel(Skill.ATTACK);
    	if(att >= 40) return brineSabre;
    	if(att >= 30) return addyScimmy;
    	if(att >= 20) return mithScimmy;
    	if(att >= 5) return steelScimmy;
    	return ironScimmy;
    }
    public static int getNextBestWeapon()
    {
    	final int att = Skills.getRealLevel(Skill.ATTACK);
    	if(att >= 30) return brineSabre;
    	if(att >= 20) return addyScimmy;
    	if(att >= 5) return mithScimmy;
    	return steelScimmy;
    }
    public static int getNextNextBestWeapon()
    {
    	final int att = Skills.getRealLevel(Skill.ATTACK);
    	if(att >= 20) return brineSabre;
    	if(att >= 5) return addyScimmy;
    	return mithScimmy;
    }
    
    
}
