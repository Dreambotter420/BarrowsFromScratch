package script.skills.magic;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Trains magic 1-83
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainMagic extends Leaf {
	public static boolean initialized = false;
	public static boolean completedMagic = false;
	public static int magic = 0;
	public static int def = 0;
    public void onStart() {
        TrainRanged.initialize();
        instantiateTree();
        Main.clearCustomPaintText();
        initialized = true;
    }

    private final Tree tree = new Tree();
    private void instantiateTree() {
    	
    	
    }
    @Override
    public int onLoop() {
        if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Magic!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
       	magic = Skills.getRealLevel(Skill.MAGIC);
       	def = Skills.getRealLevel(Skill.DEFENCE);
    	if(magic >= DecisionLeaf.mageSetpoint) {
            MethodProvider.log("[COMPLETE] -> lvl "+DecisionLeaf.mageSetpoint+" magic!");
            completedMagic = true;
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
    	if(magic < 45) trainLesserDemon();
    	if(magic < 55) teleportCamelot();
        return Timing.sleepLogNormalSleep();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_MAGIC;
	}
	public static void teleportCamelot()
	{
		if(!AutoCasting.haveRunesForSpell(Normal.CAMELOT_TELEPORT) || 
				!Equipment.contains(staffOfAir) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth))
		{
			MethodProvider.log("Missing something like wealth, staff of air, or cammy runes");
			fulfillCamelotCasting();
			return;
		}
		if(Bank.isOpen()) 
		{
			Bank.close();
			Sleep.sleep(420, 696);
			return;
		}
		if(Tabs.isOpen(Tab.MAGIC))
		{
			API.randomAFK(17);
			Magic.castSpell(Normal.CAMELOT_TELEPORT);
			Sleep.sleep(696, 696);
			MethodProvider.sleepUntil(() -> !Players.localPlayer().isAnimating(), Sleep.calculate(2222, 2222));
			return;
		} 
		Tabs.open(Tab.MAGIC);
		Sleep.sleep(420, 696);
	}
	public static void trainLesserDemon()
	{
		Main.customPaintText1 = "~~Training Magic on Lesser Demon~~";
		if(!AutoCasting.haveRunesForSpell(AutoCasting.getSpellOfAutocastID(AutoCasting.getHighestSpellConfig())) || 
				!Equipment.contains(staffOfAir) || 
				!InvEquip.equipmentContains(InvEquip.wearableWealth))
		{
			MethodProvider.log("Missing something like wealth, staff of air, or autocast runes");
			fulfillLesserDemonCasting();
			return;
			
		}
		if(Locations.lesserDemonWizardsTower.contains(Players.localPlayer()))
		{
			if(Dialogues.canContinue())
			{
				Dialogues.continueDialogue();
				Sleep.sleep(420, 696);
				return;
			}
			//check for glory in invy + not equipped -> equip it
			if(InvEquip.getInvyItem(InvEquip.wearableGlory) != 0 && 
					!InvEquip.equipmentContains(InvEquip.wearableGlory)) InvEquip.equipItem(InvEquip.getInvyItem(InvEquip.wearableGlory));
			
			if(!AutoCasting.isAutocastingHighest())
			{
				Main.customPaintText2 = "Setting Autocast: " + AutoCasting.getSpellName(AutoCasting.getHighestSpellConfig());
				AutoCasting.setHighestAutocast();
				return;
			}
			Main.customPaintText2 = "Autocasting: "+AutoCasting.getSpellName(AutoCasting.getHighestSpellConfig());
			
			org.dreambot.api.wrappers.interactive.Character interacting = Players.localPlayer().getInteractingCharacter();
			if(interacting == null || 
					!interacting.getName().equals("Lesser demon"))
			{
				NPC lesserDemon = NPCs.closest(d -> d!=null && 
						d.exists() && 
						d.getHealthPercent() > 0 &&
						d.getName().equals("Lesser demon"));
				if(lesserDemon == null)
				{
					MethodProvider.log("In lesser demon 2nd floor area of wizzy tower and demon is null!");
					Sleep.sleep(2222, 2222);
					return;
				}
				if(lesserDemon.interact("Attack")) Sleep.sleep(696,420);
			}
			return;
		}
		if(Locations.wizardsTower2.contains(Players.localPlayer()))
		{
			if(Walking.walk(Locations.lesserDemonWizardsTower.getCenter())) Sleep.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTower1.contains(Players.localPlayer()))
		{
			if(Locations.wizardsTowerStairTile1.canReach())
			{
				GameObject stairs = GameObjects.closest("Staircase");
				if(stairs == null)
				{
					MethodProvider.log("In lesser demon 1st floor area of wizzy tower and stairs are null!");
					Sleep.sleep(2222, 2222);
					return;
				}
				if(stairs.interact("Climb-up"))
				{
					MethodProvider.sleepUntil(() -> Locations.wizardsTower2.contains(Players.localPlayer()),
							() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),50);
					return;
				}
			}
			if(Walking.walk(Locations.wizardsTowerStairTile1)) Sleep.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTower0.contains(Players.localPlayer()))
		{
			if(Locations.wizardsTowerStairTile0.canReach())
			{
				GameObject stairs = GameObjects.closest("Staircase");
				if(stairs == null)
				{
					MethodProvider.log("In lesser demon 1st floor area of wizzy tower and stairs are null!");
					Sleep.sleep(2222, 2222);
					return;
				}
				if(stairs.interact("Climb-up"))
				{
					MethodProvider.sleepUntil(() -> Locations.wizardsTower1.contains(Players.localPlayer()),
							() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),50);
					return;
				}
			}
			if(Walking.walk(Locations.wizardsTowerStairTile0)) Sleep.sleep(420, 696);
			return;
		}
		if(Locations.wizardsTowerStairTile0.distance() >= 75)
		{
			if(!Walkz.useJewelry(InvEquip.passage, "Wizards\' Tower")) fulfillLesserDemonCasting();
		}
	}
	
	public static final int staffOfAir = 1381;
	public static boolean fulfillLesserDemonCasting()
    {
    	InvEquip.clearAll();

    	setBestMageArmour();
    	InvEquip.setEquipItem(EquipmentSlot.WEAPON, staffOfAir);
    	
    	for(int f : Combat.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.addInvyItem(id.waterRune, 500, (int) Calculations.nextGaussianRandom(3000, 500), false, (int) Calculations.nextGaussianRandom(5000, 100));
    	InvEquip.addInvyItem(id.earthRune, 500, (int) Calculations.nextGaussianRandom(3000, 500), false, (int) Calculations.nextGaussianRandom(5000, 100));
    	InvEquip.addInvyItem(id.fireRune, 500, (int) Calculations.nextGaussianRandom(5000, 500), false, (int) Calculations.nextGaussianRandom(10000, 100));
    	InvEquip.addInvyItem(id.mindRune, 500, (int) Calculations.nextGaussianRandom(2000, 500), false, (int) Calculations.nextGaussianRandom(3000, 100));
    	InvEquip.addInvyItem(id.chaosRune, 500, (int) Calculations.nextGaussianRandom(3000, 500), false, (int) Calculations.nextGaussianRandom(5000, 100));
    	InvEquip.addInvyItem(id.deathRune, 500, (int) Calculations.nextGaussianRandom(1000, 300), false, (int) Calculations.nextGaussianRandom(1750, 300));
    	
    	InvEquip.addInvyItem(InvEquip.passage, 1, 1, false, 2);
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(TrainRanged.jugOfWine, 10, 27, false, (int) Calculations.nextGaussianRandom(500, 100));
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for lesser demon casting!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for lesser demon casting!");
			return false;
		}
    	
    }
	public static boolean fulfillCamelotCasting()
    {
    	InvEquip.clearAll();

    	setBestMageArmour();
    	InvEquip.setEquipItem(EquipmentSlot.WEAPON, staffOfAir);
    	
    	for(int f : Combat.foods)
    	{
    		InvEquip.addOptionalItem(f);
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.addInvyItem(id.lawRune, 500, (int) Calculations.nextGaussianRandom(2000, 500), false, (int) Calculations.nextGaussianRandom(3000, 100));
    	InvEquip.shuffleFulfillOrder();
    	InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(true, 180000))
		{
			MethodProvider.log("[TRAIN MAGIC] -> Fulfilled equipment correctly for camelot casting!");
			return true;
		} else 
		{
			MethodProvider.log("[TRAIN MAGIC] -> NOT fulfilled equipment correctly for camelot casting!");
			return false;
		}
    	
    }
	public static void setBestMageArmour()
    {
    	InvEquip.setEquipItem(EquipmentSlot.HAT, getBestHatSlot());
    	InvEquip.setEquipItem(EquipmentSlot.CHEST, getBestBodySlot());
    	InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
    	InvEquip.setEquipItem(EquipmentSlot.FEET, getBestBootSlot());
    	InvEquip.setEquipItem(EquipmentSlot.AMULET, getBestNecklaceSlot());
    	InvEquip.setEquipItem(EquipmentSlot.LEGS, getBestLegsSlot());
    	InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
    	
    }

	public static final int blackRobe = 581;
	public static final int xericianTop = 13387;
	public static final int robeTopOfDarkness = 20131;
	public static int getBestBodySlot()
	{
		if(def >= 20 && magic >= 40) return robeTopOfDarkness;
		if(def >= 10 && magic >= 20) return xericianTop;
		return blackRobe;
	}
	
	
	
	public static final int guthixCloak = 10448;
	public static int getBestCapeSlot()
	{
		if(Skills.getRealLevel(Skill.PRAYER) >= 40) return guthixCloak;
		if(InvEquip.equipmentContains(TrainRanged.randCapes)) return InvEquip.getEquipmentItem(TrainRanged.randCapes);
    	if(InvEquip.invyContains(TrainRanged.randCapes)) return InvEquip.getInvyItem(TrainRanged.randCapes);
    	if(InvEquip.bankContains(TrainRanged.randCapes)) return InvEquip.getBankItem(TrainRanged.randCapes);
    	return TrainRanged.randCape;
	}
	public static final int mysticBoots = 4097;
	public static final int blueBoots = 630;
	public static int getBestBootSlot()
	{
		if(def >= 20 && magic >= 40) return mysticBoots;
		return blueBoots;
	}
	public static final int ancientMitre = 12203;
	public static final int hoodOfDarkness = 20128;
	public static final int creamHat = 662;
	public static int getBestHatSlot()
	{
		if(Skills.getRealLevel(Skill.PRAYER) >= 40 && magic >= 40) return ancientMitre;
		if(def >= 20 && magic >= 40) return hoodOfDarkness;
		return creamHat;
	}
	public static final int robeBottomOfDarkness = 20137;
	public static final int xericianRobe = 13389;
	public static final int zamorakMonkBottom = 1033;
	public static int getBestLegsSlot()
	{
		if(def >= 20 && magic >= 40) return robeBottomOfDarkness;
		if(def >= 10 && magic >= 20) return xericianRobe;
		return zamorakMonkBottom;
	}

	public static final int occultNecklace = 20137;
	public static int getBestNecklaceSlot()
	{
		if(magic >= 60) return occultNecklace;
		return InvEquip.glory;
	}
	
}
