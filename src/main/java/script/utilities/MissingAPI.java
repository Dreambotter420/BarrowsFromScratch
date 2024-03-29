package script.utilities;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class MissingAPI {
	
	public static boolean scrollHopWorld(int world)
	{
		if(Worlds.getCurrentWorld() == world) return true;
		if(Players.getLocal().isInCombat()) return false;
		Timer timeout = new Timer(Sleepz.calculate(18000, 7777));
		while(Worlds.getCurrentWorld() != world && 
				!timeout.finished() && 
				Client.isLoggedIn() &&
				ScriptManager.getScriptManager().isRunning() &&
				!ScriptManager.getScriptManager().isPaused() &&
				!Players.getLocal().isInCombat() && 
				Skills.getRealLevel(Skill.HITPOINTS) > 0) {
			Sleepz.sleep();
			if(Dialoguez.handleDialogues()) continue;
			if(Widgets.get(69,2) != null &&
					Widgets.get(69,2).isVisible() &&
					Widgets.get(69,2).getText().contains("Loading...")) {
				continue;
			}
			if(Widgets.get(182, 7) != null &&
					Widgets.get(182, 7).isVisible() &&
					Widgets.get(182, 7).getText().contains("World Switcher")) {
				Widgets.get(182, 3).interact("World Switcher");
				continue;
			}
			if(!Tabs.isOpen(Tab.LOGOUT)) {
				Tabz.open(Tab.LOGOUT);
				continue;
			}
			//worlds are now loaded
			if(Widgets.get(69,2) != null &&
					Widgets.get(69,2).isVisible() &&
					Widgets.get(69,2).getText().contains("Current world - ")) {
				//establish correct WidgetChild of desired world's clickable bar
				int gc = -1;
				for(WidgetChild w : Widgets.getWidgetChildrenContainingText(Integer.toString(world))) {
					// hardcoded correct x position lineup for world number location **FIXED MODE**
					if(w.getX() == 563) {
						gc = (w.getIndex() - 2);
						break;
					}
				}
				WidgetChild worldWidget = null;
				if(gc >= 0) worldWidget = Widgets.get(69,17,gc);
				if(worldWidget == null) {
					Sleepz.sleep(100, 111);
					continue;
				}
				Rectangle worldRectangle = worldWidget.getRectangle();
				WidgetChild worldListContainer = Widgets.get(69,17);
				if(worldListContainer == null) {
					Sleepz.sleep(100, 111);
					continue;
				}
				if(worldRectangle.intersects(Widgets.get(69,15).getRectangle())) {
					//World widget is visible - clicking it
					Rectangle visibleWorldRectangle = worldRectangle.intersection(Widgets.get(69,15).getRectangle());
					Mouse.click(visibleWorldRectangle);
				} else {
					//World list needs scrolling
					double yPos = worldRectangle.getCenterY();
					double yMin = worldListContainer.getRectangle().getMinY();
					double yMax = worldListContainer.getRectangle().getMaxY();
					double offsetRatio = ((yPos - yMin) / (yMax - yMin));
					WidgetChild scrollContainer = Widgets.get(69,18,0);
					if(scrollContainer == null) {
						Sleepz.sleep(100, 111);
						continue;
					}
					double scrollMinY = scrollContainer.getRectangle().getMinY();
					int xRand = (int) Calculations.random(scrollContainer.getRectangle().getMinX(), scrollContainer.getRectangle().getMaxX());
					int yClickPos = (int) ((scrollContainer.getHeight() * offsetRatio) + scrollMinY);
					if(yPos >= yMax) {
						yClickPos =+ (int) Calculations.random(1, 5);
						if(yClickPos > scrollContainer.getRectangle().getMaxY()) yClickPos = (int) scrollContainer.getRectangle().getMaxY() - 1;
					} else if(yPos <= yMin) {
						yClickPos =- (int) Calculations.random(1, 5);
						if(yClickPos < scrollContainer.getRectangle().getMinY()) yClickPos = (int) scrollContainer.getRectangle().getMinY() + 1;
					}
					Mouse.click(new Point(xRand,yClickPos));
				}
			}
		}
		if(Worlds.getCurrentWorld() == world) return true;
		return false;
	}
	public static List<Player> getAllPlayersInteractingWith(Player player) {
		if(player != null) {
			List<Player> targetedBy = new ArrayList<Player>();
			for(Player p : Players.all()) {
				if(p.isInteracting(player)) {
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static List<NPC> getAllNPCsInteractingWith(Player player) {
		if(player != null) {
			List<NPC> targetedBy = new ArrayList<NPC>();
			for(NPC p : NPCs.all()) {
				if(p.isInteracting(player)) {
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static List<Player> getAllPlayersInteractingWith(NPC npc)
	{
		if(npc != null)
		{
			List<Player> targetedBy = new ArrayList<Player>();
			for(Player p : Players.all())
			{
				if(p.isInteracting(npc))
				{
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static List<NPC> getAllNPCsInteractingWith(NPC npc)
	{
		if(npc != null)
		{
			List<NPC> targetedBy = new ArrayList<NPC>();
			for(NPC p : NPCs.all())
			{
				if(p.isInteracting(npc))
				{
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static boolean isInteractedByNPC()
	{
		List<NPC> targetedBy = getAllNPCsInteractingWith(Players.getLocal());
		for(NPC npc : targetedBy)
		{
			if(npc != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteracting()
	{
		return Players.getLocal().getInteractingCharacter() != null;
	}
	public static boolean isInCombat()
	{
		return isInteracting() || isInteractedByNPC();
	}
	public static boolean talkToNPC(String NPC)
	{
		NPC npc = NPCs.closest(NPC);
		if(npc == null) 
		{
			Logger.log("NPC: \""+NPC+"\" not found in TalkToNPC call");
			return false;
		}
		else
		{
			if(npc.getSurroundingArea(1).canReach())
			{
				npc.interact("Talk-to");
				Sleepz.sleep(666,111);
				Sleep.sleepUntil(
						() -> Dialogues.inDialogue(),
						Sleepz.calculate(5555,1111));
				Sleepz.sleep(111,1111);
			}
			else 
			{
				Walking.walk(npc.getTile());
			}
			
			return true;
		}
	}
	public static boolean isInteractedByAnotherPlayer()
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(Players.getLocal());
		for(Player player : targetedBy)
		{
			if(player != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteracting(NPC toCheck) {
		return toCheck.getInteractingCharacter() != null;
	}
	public static boolean isInteracting(Player toCheck) {
		return toCheck.getInteractingCharacter() != null;
	}
	public static boolean isInteractedByAnotherPlayerThanMe(Player playerToCheck)
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(playerToCheck); 
		for(Player player : targetedBy)
		{
			if(player != null && !player.equals(Players.getLocal()))
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherPlayerThanMe(NPC npcToCheck)
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(npcToCheck);
		for(Player player : targetedBy)
		{
			if(player != null && !player.equals(Players.getLocal()))
			{
				return true;
			}
		}
		return false;
	}

	private static boolean isInteractedByAnotherPlayer(NPC toCheck) {
		List<Player> targetedBy = getAllPlayersInteractingWith(toCheck);
		for(Player player : targetedBy)
		{
			if(player != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherNPC(Player playerToCheck)
	{
		List<NPC> targetedBy = getAllNPCsInteractingWith(playerToCheck);
		for(NPC NPC : targetedBy)
		{
			if(NPC != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherPlayer(Player playerToCheck)
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(playerToCheck);
		for(Player player : targetedBy)
		{
			if(player != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherNPC(NPC npcToCheck)
	{
		List<NPC> targetedBy = getAllNPCsInteractingWith(npcToCheck);
		for(NPC NPC : targetedBy)
		{
			if(NPC != null)
			{
				return true;
			}
		}
		
		return false;
	}
	public static boolean isPlayerInteractedByAnythingOtherThanMe(Player toCheck)
	{
		return isInteractedByAnotherNPC(toCheck) || isInteractedByAnotherPlayerThanMe(toCheck); 
	}
	public static boolean isNPCInteractedByAnythingOtherThanMe(NPC toCheck)
	{
		return isInteractedByAnotherNPC(toCheck) || isInteractedByAnotherPlayerThanMe(toCheck); 
	}
	public static boolean isNPCOccupiedInSingles(NPC toCheck)
	{
		return !Combat.isInMultiCombat() && 
				isNPCInteractedByAnythingOtherThanMe(toCheck) &&
				isInteracting(toCheck);
	}
	public static boolean isPlayerOccupiedInSingles(Player toCheck)
	{
		return !Combat.isInMultiCombat() && !isPlayerInteractedByAnythingOtherThanMe(toCheck) &&
				isInteracting(toCheck);
	}
	public static boolean isInteractedWith() //no argument assumes from local player
	{
		return isInteractedByAnotherPlayer(Players.getLocal()) || isInteractedByAnotherNPC(Players.getLocal());
	}
	public static boolean isInteractedWith(NPC toCheck)
	{
		return isInteractedByAnotherPlayer(toCheck) || isInteractedByAnotherNPC(toCheck);
	}
	public static boolean isInteractedWith(Player toCheck) 
	{
		return isInteractedByAnotherPlayer(toCheck) || isInteractedByAnotherNPC(toCheck);
	}
}
