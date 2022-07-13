package script.skills.slayer;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.NPC;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Branch;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.Mobs;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Combat;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;

public class TrainSlayer extends Leaf{
	public static int slayerGoal = 18;
	public static boolean started = false;
	private final static Tree tree = new Tree();
	public static void onStart()
	{
		Combat.foods.clear();
        Combat.foods.add(TrainRanged.jugOfWine);
        Combat.highFoods.clear();
        Combat.highFoods.add(TrainRanged.seaTurtle);
		Main.clearCustomPaintText();
		SlayerSettings.getConfigs();
		started = true;
	}
	public static void initializeTree()
	{
		
	}
	
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_SLAYER &&
				Skills.getRealLevel(Skill.SLAYER) <= slayerGoal;
	}
	
	@Override
	public int onLoop() {
		if(true) 
		{
    		MethodProvider.log("[UNSCRIPTED] -> Slayer!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
		if(!started) onStart();
		TrainRanged.ranged = Skills.getRealLevel(Skill.RANGED);
		if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Slayer!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
		SlayerSettings.getConfigs();
		Main.customPaintText1 = "~~Training Slayer~~";
		if(SlayerSettings.slayerTaskQty > 0)
		{
			Main.customPaintText2 = "Have slayer task! Killcount left: " + SlayerSettings.slayerTaskQty;
			MethodProvider.log("Think we have slayer task!");
			if(SlayerSettings.slayerMonsterID == 10) Mobs.trainPlainMainlandSlayerTask("Zombie",Locations.zombiesEdgeville, 8);
			if(SlayerSettings.slayerMonsterID == 22) Mobs.trainPlainMainlandSlayerTask("Guard dog", Locations.guardDogAreaHosidius, 10);
			if(SlayerSettings.slayerMonsterID == 12) Mobs.trainPlainMainlandSlayerTask("Forgotten Soul", Locations.ghostArea, 6);
			
			return Timing.sleepLogNormalInteraction();
		}
		
		//have to grab new slayer task
		if(getAssignmentTurael()) return Timing.sleepLogNormalSleep();
		
		
		return Timing.sleepLogNormalSleep();
	}
	
	public static boolean getAssignmentTurael()
	{
		Main.customPaintText2 = "Grabbing assigment from Turael";
		Filter<NPC> turaelFilter = p -> 
			p != null &&
			p.getName().equals("Turael") && 
			p.hasAction("Assignment");
		NPC turael = NPCs.closest(turaelFilter);
		if(turael != null)
		{
			if(turael.canReach())
			{
				if(turael.interact("Assignment")) 
				{
					MethodProvider.sleepUntil(Dialogues::inDialogue, () -> Players.localPlayer().isMoving(), Sleep.calculate(2222, 2222),50);
					return true;
				}
			}
			else if(Walking.shouldWalk(6) && Walking.walk(turael)) Sleep.sleep(420, 696);
		}
		else 
		{
			if(Locations.burthorpeTeleSpot.distance(Players.localPlayer().getTile()) > 50)
			{
				if(Walkz.useJewelry(InvEquip.games, "Burthorpe"))
				{
					Sleep.sleep(420, 696);
					return true;
				}
				
				if(!InvEquip.checkedBank())
				{
					Sleep.sleep(696, 420);
					return false;
				}
				
				if(InvEquip.bankContains(InvEquip.wearableGames))
				{
					InvEquip.withdrawOne(InvEquip.getBankItem(InvEquip.wearableGames), 180000);
					return false;
				}
				InvEquip.buyItem(InvEquip.games8, 2, 180000);
			} else if(Walking.shouldWalk(6) && Walking.walk(Locations.turaelArea.getCenter())) Sleep.sleep(420, 696);
		}
		return false;
	}
}
