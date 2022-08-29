package script.skills.ranged;

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
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Timer;

import script.Main;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.horrorfromthedeep.HorrorFromTheDeep;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Combatz;
import script.utilities.GrandExchangg;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Trains prayer 1-45
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainRanged extends Leaf {
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_RANGE;
	}
	public static boolean started = false;
    public void onStart() {
        Main.customPaintText1 = "~~~ Training Ranged ~~~";
        started = true;
    }
   
    public boolean onExit() {
        Main.clearCustomPaintText();
        if(!Walkz.exitGiantsCave()) return false;
        if(Locations.isInIsleOfSouls())
		{
			Walkz.exitIsleOfSouls(240000);
			return false;
		}
        if(Locations.kourendGiantsCaveArea.contains(Players.localPlayer()))
		{
			Walkz.exitGiantsCave();
			return false;
		}
        Mobs.mob = null;
    	return true;
    }

    @Override
    public int onLoop() {
        if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Ranged!");
    		if(onExit())
    		{
    			API.mode = null;
    		}
    		return Timing.sleepLogNormalSleep();
    	}
    	if(Skills.getRealLevel(Skill.RANGED) >= 75) {
            MethodProvider.log("[COMPLETE] -> lvl 75 ranged!");
            if(onExit())
    		{
                API.mode = null;
    		}
            return Timing.sleepLogNormalSleep();
        }
    	if(!started) 
    	{
    		onStart();
    		return Timing.sleepLogNormalSleep();
    	}

        if(Equipment.contains(getBestDart()) && !updateAttStyle()) 
		{
			MethodProvider.log("Failed to update attack style with best melee weapon equipped!");
			return Timing.sleepLogNormalSleep();
		}
    	if(Mobs.mob == null) Mobs.chooseMob(false);
        return Mobs.trainMob(false);
    }
    public static boolean updateAttStyle()
	{
    	if(Combat.getCombatStyle() == CombatStyle.RANGED_RAPID) return true;
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
		Combat.setCombatStyle(CombatStyle.RANGED_RAPID);
		return Combat.getCombatStyle() == CombatStyle.RANGED_RAPID;
	}
    
   
    public static boolean fulfillRangedDarts()
    {
    	InvEquip.clearAll();

    	setBestRangedEquipment();
    	
    	InvEquip.addInvyItem(id.rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : id.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 15, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[TRAIN RANGED] -> Fulfilled equipment correctly!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN RANGED] -> NOT fulfilled equipment correctly!");
			return false;
		}
    	
    }
    public static void setBestRangedEquipment()
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
    	InvEquip.addInvyItem(getBestDart(), 500, 1000, false, 1000);
		if(getNextBestDart() != getBestDart()) InvEquip.addInvyItem(getNextBestDart(), 500, 1000, false, 1000);
		if(getNextNextBestDart() != getBestDart() && 
				getNextNextBestDart() != getNextBestDart()) InvEquip.addInvyItem(getNextNextBestDart(), 500, 1000, false, 1000);
    	
    }
    public static boolean fulfillRangedDartsStaminaGames()
    {
    	InvEquip.clearAll();
    	setBestRangedEquipment();
    	InvEquip.addInvyItem(id.rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 5);
    	if(InvEquip.bankContains(id.staminas))
    	{
    		InvEquip.addInvyItem(InvEquip.getBankItem(id.staminas), 1, 1, false, 0);
    	}
    	else InvEquip.addInvyItem(id.stamina4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : id.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 15, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (stamina + games strict)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (stamina + games strict)");
			return false;
		}
    	
    }
    public static boolean fulfillRangedDartsStamina()
    {
    	InvEquip.clearAll();
    	setBestRangedEquipment();
    	InvEquip.addInvyItem(id.rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	if(InvEquip.bankContains(id.staminas))
    	{
    		InvEquip.addInvyItem(InvEquip.getBankItem(id.staminas), 1, 1, false, 0);
    	}
    	else InvEquip.addInvyItem(id.stamina4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : id.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 15, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (stamina strict)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (stamina strict)");
			return false;
		}
    	
    }
    public static boolean fulfillRangedDartsStaminaAntidote()
    {
    	InvEquip.clearAll();
    	setBestRangedEquipment();
    	InvEquip.addInvyItem(id.rangePot4, 1, 6, false, (int) Calculations.nextGaussianRandom(20, 5));
    	InvEquip.addInvyItem(InvEquip.games, 1, 1, false, 5);
    	InvEquip.addInvyItem(id.antidote4, 1, 1, false, 5);
    	if(InvEquip.bankContains(id.staminas))
    	{
    		InvEquip.addInvyItem(InvEquip.getBankItem(id.staminas), 1, 1, false, 0);
    	}
    	else InvEquip.addInvyItem(id.stamina4, 1, 1, false, (int) Calculations.nextGaussianRandom(20, 5));
    	
    	for(int f : Combatz.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	for(int r : id.rangedPots)
    	{
    		InvEquip.addOptionalItem(r);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(Combatz.lowFood, 15, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[INVEQUIP] -> Fulfilled equipment correctly! (antidote + stamina strict)");
			return true;
		} else 
		{
			MethodProvider.log("[INVEQUIP] -> NOT Fulfilled equipment correctly! (antidote + stamina strict)");
			return false;
		}
    }
    
    
   
    public static int getBestHeadSlot()
    {
    	final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40 && ranged >= 70) return id.zamorakCoif;
    	if(def >= 30 && ranged >= 30) return id.snakeskinBandana;
    	if(ranged >= 20) return id.coif;
    	return id.cowl;
    }
    
	public static int getBestBodySlot()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
    	if(def >= 40 && ranged >= 70) return id.blackBody;
    	if(def >= 40 && ranged >= 60) return id.redBody;
    	if(def >= 40 && ranged >= 50) return id.blueBody;
    	if(def >= 30 && ranged >= 30) return id.snakeskinBody;
    	if(def >= 20 && ranged >= 20) return id.studdedBody;
    	if(def >= 10) return id.hardLeatherBody;
    	return id.leatherBody;
    }
	

   
	public static int getBestLegSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(ranged >= 70) return id.blackLegs;
    	if(ranged >= 60) return id.redLegs;
    	if(ranged >= 50) return id.blueLegs;
    	if(def >= 30 && ranged >= 30) return id.snakeskinChaps;
    	if(ranged >= 20) return id.studdedChaps;
    	return id.leatherChaps;
	}
	
	public static int getBestCapeSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
		if(ranged >= 50 && AnimalMagnetism.completedAnimalMagnetism) return id.avasAccumulator;
    	if(InvEquip.equipmentContains(id.randCapes)) return InvEquip.getEquipmentItem(id.randCapes);
    	if(InvEquip.invyContains(id.randCapes)) return InvEquip.getInvyItem(id.randCapes);
    	if(InvEquip.bankContains(id.randCapes)) return InvEquip.getBankItem(id.randCapes);
    	return id.randCape;
	}
	
	
	public static int getBestBootSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(def >= 30 && ranged >= 30) return id.snakeskinBoots;
    	return id.leatherBoots;
	}
	public static int getNextBestBootSlot()
	{
    	return id.snakeskinBoots;
	}

	public static int getBestHandSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
		if(ranged >= 70) return id.guthixBracers;
		if(ranged >= 60) return id.redVambs;
		if(ranged >= 50) return id.blueVambs;
		if(ranged >= 40) return id.greenVambs;
    	return InvEquip.combat;
	}
	
	public static int getBestAmuletSlot()
	{
		return InvEquip.glory;
	}
	
	public static int getBestShieldSlot()
	{
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	final int def = Skills.getRealLevel(Skill.DEFENCE);
		if(HorrorFromTheDeep.completedHorrorFromTheDeep) return id.bookOfLaw;
		if(def >= 40 && ranged >= 70) return id.blackShield;
		//skipping green, blue, and red dhide shields cuz less than 200 traded per day. ...
		if(def >= 30 && ranged >= 30) return id.snakeskinShield;
		if(def >= 10 && ranged >= 20) return id.hardleatherShield;
		return id.woodenShield;
	}
	
    public static int getBestDart()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	if(ranged >= 30) 
    	{
    		if(UniqueActions.isActionEnabled(Actionz.MITH_DARTS_INSTEAD_OF_ADDY)) return id.mithDart;
    		return id.addyDart;
    	}
    	if(ranged >= 20) return id.mithDart;
    	if(ranged >= 5) return id.steelDart;
    	return id.ironDart;
    }
    public static int getNextBestDart()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	if(ranged >= 20)
    	{
    		if(UniqueActions.isActionEnabled(Actionz.MITH_DARTS_INSTEAD_OF_ADDY)) return id.mithDart;
    		return id.addyDart;
    	}
    	if(ranged >= 5) return id.mithDart;
    	return id.steelDart;
    }
    public static int getNextNextBestDart()
    {
		final int ranged = Skills.getRealLevel(Skill.RANGED);
    	if(ranged >= 5)
    	{
    		if(UniqueActions.isActionEnabled(Actionz.MITH_DARTS_INSTEAD_OF_ADDY)) return id.mithDart;
    		return id.addyDart;
    	}
    	return id.mithDart;
    }
    
    
}
