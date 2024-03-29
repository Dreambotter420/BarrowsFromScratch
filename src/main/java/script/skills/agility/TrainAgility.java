package script.skills.agility;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.skills.magic.TrainMagic;
import script.utilities.API;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleepz;
import script.utilities.Walkz;
import script.utilities.id;

public class TrainAgility extends Leaf{
	public static boolean started = false;
	public static boolean ha = false;
	public static void onStart() {
		if(Skills.getRealLevel(Skill.MAGIC) >= 55) {
			Logger.log("Magic higher than 55! Getting items to alch while doing agility...");
			ha = true;
		}
		started = true;
	}
	
	public static void onExit()
	{
		started = false;
	}
	public static void checkSetCourse()
	{
		if(courseCheckTimer == null || courseCheckTimer.finished())
		{
			final int agility = Skills.getRealLevel(Skill.AGILITY);
			course = courses.DRAYNOR;
			Logger.log("Set agility course: " + course.toString());
			courseCheckTimer = new Timer((int) Calculations.nextGaussianRandom(900000, 180000));
		}
	}
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_AGILITY;
	}
	public static courses course = null;
	public static Timer courseCheckTimer = null;
	public static enum courses {
			DRAYNOR
	}
	@Override
	public int onLoop()
	{
		if(!started) onStart();
		if(DecisionLeaf.taskTimer.finished()) {
    		Logger.log("[TIMEOUT] -> Agility training!");
            API.mode = null;
            return Sleepz.sleepTiming();
    	}
		if(Skills.getRealLevel(Skill.AGILITY) < 10) {
			Logger.log("[AGILITY] -> Not lvl 10! Gnome agility course unscripted, just wait until script chooses Fremennik trials after training for it");
			API.mode = null;
	        return Sleepz.sleepTiming();
		}
		checkSetCourse();
		if(!InvEquip.equipmentContains(InvEquip.wearableWealth) || 
				(!InvEquip.equipmentContains(InvEquip.wearableGlory) && !InvEquip.invyContains(InvEquip.wearableGlory))) {
			fulfillJewelry();
			return Sleepz.sleepTiming();
		}
		Main.paint_task = "HA while running? " + (ha ? "Yes!!" : "No...");
		Main.paint_itemsCount = "Agility course: " + course.toString();
		Main.paint_subTask = "Marks of grace: " + (Inventory.contains(id.markOfGrace) ? Inventory.count(id.markOfGrace) : "none");
		switch(course)
		{
		/*case AL_KHARID:
		{
			if(ha && !TrainMagic.haveHAItems()) break;
			if(!Walking.isRunEnabled() && Walking.getRunEnergy() >= 99 && (int) Calculations.nextGaussianRandom(100,50) > 120)
			{
				Walking.toggleRun();
				Sleep.sleep(696,666);
			}
			if(Players.getLocal().isAnimating()) 
			{
				Sleep.sleep(420,696);
				break;
			}
			if(Locations.alkharidAgility1.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility1)) break;
				maybeHA();
				API.interactWithGameObject("Rough wall", "Climb", () -> Locations.alkharidAgility2.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgility2.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility2)) break;
				maybeHA();
				API.interactWithGameObject("Tightrope", "Cross", () -> Locations.alkharidAgilityGround.contains(Players.getLocal()) || Locations.alkharidAgility3.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgility3.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility3)) break;
				maybeHA();
				API.interactWithGameObject("Cable", "Swing-across", () -> Locations.alkharidAgilityGround.contains(Players.getLocal()) || Locations.alkharidAgility4.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgility4.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility4)) break;
				maybeHA();
				API.interactWithGameObject("Zip line", "Teeth-grip", () -> Locations.alkharidAgilityGround.contains(Players.getLocal()) || Locations.alkharidAgility5.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgility5.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility5)) break;
				maybeHA();
				API.interactWithGameObject("Tropical tree", "Swing-across", () -> Locations.alkharidAgilityGround.contains(Players.getLocal()) ||Locations.alkharidAgility6.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgility6.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility6)) break;
				maybeHA();
				API.interactWithGameObject("Roof top beams", "Climb", () -> Locations.alkharidAgilityGround.contains(Players.getLocal()) ||Locations.alkharidAgility7.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgility7.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility7)) break;
				maybeHA();
				API.interactWithGameObject("Tightrope", "Cross", () -> Locations.alkharidAgilityGround.contains(Players.getLocal()) ||Locations.alkharidAgility8.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgility8.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.alkharidAgility8)) break;
				maybeHA();
				API.interactWithGameObject("Gap", "Jump", () -> Locations.alkharidAgilityGround.contains(Players.getLocal()));
				break;
			}
			if(Locations.alkharidAgilityGround.contains(Players.getLocal()))
			{
				maybeHA();
				if(Walking.shouldWalk(5) && Walking.walk(Locations.alkharidAgility1.getCenter())) Sleep.sleep(69,666);
				break;
			}
			if(Locations.alkharidAgilityGround.getCenter().distance() < 100)
			{
				if(Walking.shouldWalk(5) && Walking.walk(Locations.alkharidAgility1.getCenter())) Sleep.sleep(69,666);
				break;
			}
			Walkz.useJewelry(InvEquip.glory,"Al Kharid");
			
			break;
		}*/
		case DRAYNOR:
		{
			if(ha && !TrainMagic.getAgilityHAItems()) break;
			if(!Walking.isRunEnabled() && Walking.getRunEnergy() >= 99 && (int) Calculations.nextGaussianRandom(100,50) > 120)
			{
				Walking.toggleRun();
				Sleepz.sleep(696,666);
			}
			if(Players.getLocal().isAnimating())
			{
				Sleepz.sleep(420,696);
				break;
			}
			if(Locations.draynorAgility1.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.draynorAgility1)) break;
				maybeHA();
				API.interactWithGameObject("Rough wall", "Climb", () -> Locations.draynorAgility2.contains(Players.getLocal()));
				break;
			}
			if(Locations.draynorAgility2.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.draynorAgility2)) break;
				maybeHA();
				API.interactWithGameObject("Tightrope", "Cross", () -> Locations.draynorAgilityGround.contains(Players.getLocal()) || Locations.draynorAgility3.contains(Players.getLocal()));
				break;
			}
			if(Locations.draynorAgility3.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.draynorAgility3)) break;
				maybeHA();
				API.interactWithGameObject("Tightrope", "Cross", () -> Locations.draynorAgilityGround.contains(Players.getLocal()) || Locations.draynorAgility4.contains(Players.getLocal()));
				break;
			}
			if(Locations.draynorAgility4.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.draynorAgility4)) break;
				maybeHA();
				API.interactWithGameObject("Narrow wall", "Balance", () -> Locations.draynorAgilityGround.contains(Players.getLocal()) || Locations.draynorAgility5.contains(Players.getLocal()));
				break;
			}
			if(Locations.draynorAgility5.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.draynorAgility5)) break;
				maybeHA();
				API.interactWithGameObject("Wall", "Jump-up", () -> Locations.draynorAgility6.contains(Players.getLocal()));
				break;
			}
			if(Locations.draynorAgility6.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.draynorAgility6)) break;
				maybeHA();
				API.interactWithGameObject("Gap", "Jump", () -> Locations.draynorAgility7.contains(Players.getLocal()));
				break;
			}
			if(Locations.draynorAgility7.contains(Players.getLocal()))
			{
				if(checkTakeMarkOfGrace(Locations.draynorAgility7)) break;
				maybeHA();
				API.interactWithGameObject("Crate", "Climb-down", () -> Locations.draynorAgilityGround.contains(Players.getLocal()));
				break;
			}
			if(Locations.draynorAgilityGround.contains(Players.getLocal()))
			{
				maybeHA();
				if(Walking.shouldWalk(5) && Walking.walk(Locations.draynorAgility1.getCenter())) Sleepz.sleep(69,666);
				break;
			}
			if(Locations.draynorAgilityGround.getCenter().distance() < 100)
			{
				if(Walking.shouldWalk(5) && Walking.walk(Locations.draynorAgility1.getCenter())) Sleepz.sleep(69,666);
				break;
			}
			Walkz.useJewelry(InvEquip.glory,"Draynor Village");
			
			break;
		}
		/*case GNOME_STRONGHOLD:
		{
			Logger.log("GNOMESTRONGHOLD AGILITY Unscripted :-(");
			API.mode = null;
			break;
		}*/
		default:break;
		}
		return Sleepz.sleepTiming();
	}
	public static void maybeHA()
	{
		if((int) Calculations.nextGaussianRandom(1000, 100) >= 1010)
		{
			if(Inventory.isFull() && !Inventory.contains(InvEquip.coins))
			{
				InvEquip.free1InvySpace();
				return;
			}
			TrainMagic.quickHighAlch();
		}
	}
	public static boolean checkTakeMarkOfGrace(Area area)
	{
		GroundItem markOfGrace = GroundItems.closest(g -> g!=null && g.getID() == id.markOfGrace && area.contains(g));
		
		if(markOfGrace != null)
		{
			if(Inventory.isFull() && !Inventory.contains(id.markOfGrace))
			{
				InvEquip.free1InvySpace();
				return true;
			}
			final int count = Inventory.count(id.markOfGrace);
			if(markOfGrace.interact("Take"))
			{
				Sleep.sleepUntil(() -> Inventory.count(id.markOfGrace) > count, () -> Players.getLocal().isMoving(),
						Sleepz.calculate(2222, 2222),69);
			}
			if(Inventory.count(id.markOfGrace) > count)
			{
				Logger.log("Successfully got mark of grace!");
			}
			return true;
		}
		return false;
	}
	public static void fulfillJewelry()
	{
		InvEquip.clearAll();
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory);
		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
		InvEquip.shuffleFulfillOrder();
		InvEquip.fulfillSetup(false, 180000);
	}
}
