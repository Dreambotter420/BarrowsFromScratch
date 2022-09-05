package script.quest.alfredgrimhand;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankType;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.quest.Quests;
import org.dreambot.api.methods.quest.book.MiniQuest;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.Main;
import script.p;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.restlessghost.RestlessGhost;
import script.quest.varrockmuseum.Timing;
import script.skills.ranged.TrainRanged;
import script.utilities.API;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Paths;
import script.utilities.Questz;
import script.utilities.Skillz;
import script.utilities.Sleep;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;

import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes Alfred Grimhands Barcrawl
 * @author Dreambotter420
 * ^_^
 */
public class AlfredGrimhandsBarcrawl extends Leaf {
	@Override
	public boolean isValid() {
		return API.mode == API.modes.ALFRED_GRIMHANDS_BARCRAWL;
	}
	public static boolean completed()
	{
		return Quests.isFinished(MiniQuest.ALFRED_GRIMHANDS_BARCRAWL);
	}
	public static boolean finishedCard = false;
    @Override
    public int onLoop() {
        if (DecisionLeaf.taskTimer.finished()) {
            MethodProvider.log("[TIMEOUT] -> Alfred Grimhand\'s Barcrawl!");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if(Dialoguez.handleDialogues()) return Sleep.calculate(420, 696);
        if(completed())
        {
        	if(Questz.closeQuestCompletion()) return Sleep.calculate(420, 1111);
        	MethodProvider.log("[COMPLETED] -> Alfred Grimhand\'s Barcrawl!");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        if(finishedCard)
        {
        	if(Locations.barbarianAgilityOutside.getCenter().distance() < 75)
        	{
        		API.walkTalkToNPC("Barbarian guard", "Talk-to", Locations.barbarianAgilityOutside);
        		return Sleep.calculate(420,1111);
        	}
        	if(Walkz.useJewelry(InvEquip.games, "Barbarian Outpost")) Sleep.sleep(420, 696);
        	return Sleep.calculate(696, 1111);
        }
        int progress = getProgressValue();
        if(progress > 0)
        {
        	if(!checkedCard)
        	{
        		checkBarsCrawled();
            	return Sleep.calculate(696, 1111);
        	}
        	if(!risingSun)
        	{
        		if(Locations.faladorBar.getCenter().distance() < 50)
            	{
            		API.walkTalkToNPC("Kaylee", "Talk-to", Locations.faladorBar);
            		return Sleep.calculate(420, 696);
            	}
            	if(Walkz.teleportFalador(30000)) Sleep.sleep(420, 696);
            	return Sleep.calculate(420, 696);
        	}
        	if(!rusty)
        	{
        		if(Locations.rustyAnchor.getCenter().distance() < 75)
            	{
            		API.walkTalkToNPC("Bartender", "Talk-to", Locations.rustyAnchor);
            		return Sleep.calculate(420, 696);
            	}
            	if(Walkz.useJewelry(InvEquip.glory, "Draynor Village")) Sleep.sleep(420, 696);
            	return Sleep.calculate(420, 696);
        	}
        	if(!karamja)
        	{
        		if(Locations.karamjaF2P.contains(p.l))
            	{
            		API.walkTalkToNPC("Zambo", "Talk-to", Locations.karamjaBar);
            		return Sleep.calculate(420, 696);
            	}
            	if(Walkz.useJewelry(InvEquip.glory, "Karamja")) Sleep.sleep(420, 696);
            	return Sleep.calculate(420, 696);
        	}
        	if(!deadman)
        	{
        		if(Locations.brimhavenBar.getCenter().distance() < 75 ||
        				Locations.karamjaF2P.contains(p.l))
            	{
            		API.walkTalkToNPC("Bartender", "Talk-to", Locations.brimhavenBar);
            		return Sleep.calculate(420, 696);
            	}
            	if(Walkz.useJewelry(InvEquip.glory, "Karamja")) Sleep.sleep(420, 696);
            	return Sleep.calculate(420, 696);
        	}
        	if(!flyingHorse)
        	{
        		if(Locations.ardougneBar.getCenter().distance() < 100)
            	{
            		API.walkTalkToNPC("Bartender", "Talk-to", Locations.ardougneBar);
            		return Sleep.calculate(420, 696);
            	}
            	if(Walkz.useJewelry(InvEquip.skills, "Fishing Guild")) Sleep.sleep(420, 696);
            	return Sleep.calculate(420, 696);
        	}
        	if(!blurberry)
        	{
        		if(Locations.grandTreeBase.contains(p.l))
        		{
        			API.interactWithGameObject("Ladder", "Climb-up", () -> p.l.getZ() == 1);
                	return Sleep.calculate(420, 696);
        		}
        		if((p.l.getZ() == 1 && 
        				Locations.gnomeStrongholdBar.getCenter().distance() < 50) || 
        				Locations.grandTreeBase.getCenter().distance() < 50)
        		{
        			API.walkTalkToNPC("Blurberry", "Talk-to", Locations.gnomeStrongholdBar);
                	return Sleep.calculate(420, 696);
        		}
        		if(Locations.gnomeStrongholdGate_South.contains(p.l))
        		{
        			API.interactWithGameObject("Gate", "Open");
                	return Sleep.calculate(420, 696);
        		}
        		if(Walkz.walkPath(Paths.outpostToGrandTree)) return Sleep.calculate(420, 696);
        		if(Walkz.useJewelry(InvEquip.passage, "The Outpost")) return Sleep.calculate(420, 696);
            	return Sleep.calculate(420, 696);
        	}
        	if(!blueMoon)
        	{
        		if(Locations.varrockBar.getCenter().distance() < 60)
        		{
        			API.walkTalkToNPC("Bartender", "Talk-to", Locations.varrockBar);
            		return Sleep.calculate(420, 696);
        		}
        		if(Walkz.teleportVarrock(30000)) Sleep.sleep(111, 1111);
        		return Sleep.calculate(420, 696);
        	}
        	if(!jolly)
        	{
        		if(Locations.lumberyardBar.getCenter().distance() < 50)
        		{
        			API.walkTalkToNPC("Bartender", "Talk-to", Locations.lumberyardBar);
            		return Sleep.calculate(420, 696);
        		}
        		if(Tabz.open(Tab.INVENTORY))
        		{
        			if(Inventory.interact(id.lumberyardTele, "Teleport")) {
        				MethodProvider.sleepUntil(() -> Locations.lumberyardBar.getCenter().distance() < 50, Sleep.calculate(4444, 4444));
        			}
        		}
        		return Sleep.calculate(420, 696);
        	}
        	if(!forester)
        	{
        		if(Locations.seersPub.getCenter().distance() < 75)
        		{
        			API.walkTalkToNPC("Bartender", "Talk-to", Locations.seersPub);
            		return Sleep.calculate(420, 696);
        		}
        		if(Walkz.useJewelry(InvEquip.combat, "Ranging Guild")) Sleep.sleep(111, 1111);
        		return Sleep.calculate(420, 696);
        	}
        	if(!dragon)
        	{
        		if(Locations.yanilleBar.getCenter().distance() < 130)
        		{
        			API.walkTalkToNPC("Bartender", "Talk-to", Locations.yanilleBar);
            		return Sleep.calculate(420, 696);
        		}
        		if(Walkz.useJewelry(InvEquip.duel, "Castle Wars")) Sleep.sleep(111, 1111);
        		return Sleep.calculate(420, 696);
        	}
        	return Sleep.calculate(420, 696);
        }
        if(progress == 0)
        {
        	checkedCard = true;
        	if(!Inventory.contains(id.varrockTele) || 
        			!Inventory.contains(InvEquip.coins) || 
        			!Inventory.contains(id.lumberyardTele) || 
        			!Inventory.contains(id.fallyTele))
        	{
        		fulfillStep0();
        		return Sleep.calculate(420,1111);
        	}
        	if(Locations.barbarianAgilityOutside.getCenter().distance() < 75)
        	{
        		API.walkTalkToNPC("Barbarian guard", "Talk-to", Locations.barbarianAgilityOutside);
        		return Sleep.calculate(420,1111);
        	}
        	if(Walkz.useJewelry(InvEquip.games, "Barbarian Outpost")) Sleep.sleep(420, 696);
        }
        return Sleep.calculate(420,1111);
    }
    public static boolean blueMoon = false;
    public static boolean blurberry = false;
    public static boolean deadman = false;
    public static boolean dragon = false;
    public static boolean flyingHorse = false;
    public static boolean forester = false;
    public static boolean jolly = false;
    public static boolean karamja = false;
    public static boolean risingSun = false;
    public static boolean rusty = false;
    public static boolean checkedCard = false;
    public static void checkBarsCrawled()
    {
    	WidgetChild cardInterfaceClose = Widgets.getWidgetChild(220,16);
    	if(cardInterfaceClose != null && cardInterfaceClose.isVisible())
    	{
    		if(Widgets.getWidgetChild(220,4).getText().contains("00ff00")) blueMoon = true;
    		if(Widgets.getWidgetChild(220,5).getText().contains("00ff00")) blurberry = true;
    		if(Widgets.getWidgetChild(220,6).getText().contains("00ff00")) deadman = true;
    		if(Widgets.getWidgetChild(220,7).getText().contains("00ff00")) dragon = true;
    		if(Widgets.getWidgetChild(220,8).getText().contains("00ff00")) flyingHorse = true;
    		if(Widgets.getWidgetChild(220,9).getText().contains("00ff00")) forester = true;
    		if(Widgets.getWidgetChild(220,10).getText().contains("00ff00")) jolly = true;
    		if(Widgets.getWidgetChild(220,11).getText().contains("00ff00")) karamja = true;
    		if(Widgets.getWidgetChild(220,12).getText().contains("00ff00")) risingSun = true;
    		if(Widgets.getWidgetChild(220,13).getText().contains("00ff00")) rusty = true;
    		if(cardInterfaceClose.interact())
    		{
    			MethodProvider.log("Updated list of bars crawled!");
    			Sleep.sleep(420, 696);
    			checkedCard = true;
    		}
    		return;
    	}
    	if(Tabz.open(Tab.INVENTORY))
    	{
    		if(Inventory.interact(id.barcrawlCard, "Read")) 
    		{
    			MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(220,16) != null && Widgets.getWidgetChild(220,16).isVisible(), Sleep.calculate(3333, 3333));
    	    }
    	}
    }
    public static void fulfillStep0()
    {
    	if(!InvEquip.checkedBank()) return;
    	InvEquip.clearAll();
		InvEquip.addInvyItem(id.stamina4, 2, 2, false, 4);
		InvEquip.addInvyItem(InvEquip.coins, 1000, API.roundToMultiple((int) Calculations.nextGaussianRandom(5000, 3000), 500), false, 0);
		InvEquip.addInvyItem(id.fallyTele, 1, 2, false, 2);
		InvEquip.addInvyItem(id.varrockTele, 1, 2, false, 2);
		InvEquip.addInvyItem(id.lumberyardTele, 1, 1, false, 1);
		int duelID = InvEquip.duel8;
		if(InvEquip.bankContains(InvEquip.wearableDuel)) duelID = InvEquip.getBankItem(InvEquip.wearableDuel);
		else if(InvEquip.invyContains(InvEquip.wearableDuel)) duelID = InvEquip.getInvyItem(InvEquip.wearableDuel);
		else if(InvEquip.equipmentContains(InvEquip.wearableDuel)) duelID = InvEquip.getEquipmentItem(InvEquip.wearableDuel);
		InvEquip.addInvyItem(duelID, 1, 1, false, 1);
		int passageID = InvEquip.passage5;
		if(InvEquip.bankContains(InvEquip.wearablePassages)) passageID = InvEquip.getBankItem(InvEquip.wearablePassages);
		else if(InvEquip.invyContains(InvEquip.wearablePassages)) passageID = InvEquip.getInvyItem(InvEquip.wearablePassages);
		else if(InvEquip.equipmentContains(InvEquip.wearablePassages)) passageID = InvEquip.getEquipmentItem(InvEquip.wearablePassages);
		InvEquip.addInvyItem(passageID, 1, 1, false, 1);
		int skillsID = InvEquip.skills6;
		if(InvEquip.bankContains(InvEquip.wearableSkills)) skillsID = InvEquip.getBankItem(InvEquip.wearableSkills);
		else if(InvEquip.invyContains(InvEquip.wearableSkills)) skillsID = InvEquip.getInvyItem(InvEquip.wearableSkills);
		else if(InvEquip.equipmentContains(InvEquip.wearableSkills)) skillsID = InvEquip.getEquipmentItem(InvEquip.wearableSkills);
		InvEquip.addInvyItem(skillsID, 1, 1, false, 1);
		InvEquip.addInvyItem(InvEquip.games8, 1, 1, false, 1);
		InvEquip.setEquipItem(EquipmentSlot.AMULET, InvEquip.glory6);
		InvEquip.setEquipItem(EquipmentSlot.HANDS, InvEquip.combat);
		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
		InvEquip.shuffleFulfillOrder();
		if(InvEquip.fulfillSetup(true, 240000))
		{
			MethodProvider.log("[ALFRED GRIMHAND\'S BARCRAWL] -> Fulfilled step 0 correctly!");
		} else MethodProvider.log("[ALFRED GRIMHAND\'S BARCRAWL] -> NOT Fulfilled step 0 correctly! :-(");
    }
	public static int getProgressValue()
    {
    	return PlayerSettings.getConfig(77);
    }
}
