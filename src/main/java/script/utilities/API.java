package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.dreambot.api.Client;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import script.Main;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;
import script.quest.varrockmuseum.Timing;


public class API {
	
	public static String currentBranch = "";
    public static String currentLeaf = "";
    
    public static enum modes {
		TRAIN_SLAYER,
		TRAIN_WOODCUTTING,
		TRAIN_RANGE,
		TRAIN_MAGIC,
		TRAIN_MELEE,
		TRAIN_CRAFTING,
		TRAIN_PRAYER,
		TRAIN_FIREMAKING,
		TRAIN_AGILITY,
		MAGE_ARENA_1,
		MAGE_ARENA_2,
		RESTLESS_GHOST,
		NATURE_SPIRIT,
		VARROCK_QUIZ,
		PRIEST_IN_PERIL,
		ERNEST_THE_CHIKKEN,
		ANIMAL_MAGNETISM,
		HORROR_FROM_THE_DEEP,
		WATERFALL_QUEST,
		FIGHT_ARENA,
		FREMENNIK_TRIALS,
		X_MARKS_THE_SPOT,
		BREAK,
		TEST
    }
    
    public static modes mode = null;//change to null to disable, modes.GOHOME for lumby
	public static Random rand1 = new Random();
	public static Random rand2 = new Random();
    public static boolean started = false;
    
    
    
   
	public static boolean initialized = false;
	public static double sleepMod;
	
	
	/**
	 * returns true if chatbox is clear
	 * returns false if chatbox has some contents and cleared one
	 * @return
	 */
	public static boolean clearChatWithBackspace()
	{
		String[] x = Widgets.getWidgetChild(162,55).getText().split(":", 2);
    	String[] y = x[1].split("</col>",0);
    	String[] zeChatInputBox = y[0].split("<col=0000ff>",0);
		if(zeChatInputBox != null && zeChatInputBox.length >= 2)
		{
			MethodProvider.log("Clearing chatbox of some contents");
	    	Keyboard.typeSpecialKey(8);
			Sleep.calculate(10,55);
			return false;
		}
		return true;
	}
	public static void randomLongerAFK(int chance)
	{
		int roll = API.rand2.nextInt(100);
		boolean moveMouse = false;
		boolean examineObject = false;
		int antibanChance = chance;
		if(UniqueActions.isActionEnabled(Actionz.MOVE_MOUSE_OFFSCREEN) || 
				UniqueActions.isActionEnabled(Actionz.EXAMINE_RANDOM_OBJECTS))
		{
			antibanChance = antibanChance * 3;
		}
			
		if(API.rand2.nextInt(100) <= antibanChance)
		{
			if(UniqueActions.isActionEnabled(Actionz.MOVE_MOUSE_OFFSCREEN) && 
					UniqueActions.isActionEnabled(Actionz.EXAMINE_RANDOM_OBJECTS))
			{
				if(API.rand2.nextInt(100) > 20) moveMouse = true;
				else examineObject = true;
			}
			else if(UniqueActions.isActionEnabled(Actionz.MOVE_MOUSE_OFFSCREEN))
			{
				moveMouse = true;
			}
			else if(UniqueActions.isActionEnabled(Actionz.EXAMINE_RANDOM_OBJECTS))
			{
				if(API.rand2.nextInt(100) > 90) examineObject = true;
			}
		}
		if(roll <= chance)
		{
			int tmp = API.rand2.nextInt(100);
			int sleep = 0;
			if(tmp < 2)  
			{
				MethodProvider.log("Longer AFK: 1% chance");
				sleep = Sleep.calculate(10000,160000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 100000);
			}
			else if(tmp < 6)  
			{
				MethodProvider.log("Long AFK: 5% chance");
				sleep = Sleep.calculate(10000,80000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 60000);
			}
			else if(tmp < 25)
			{
				MethodProvider.log("Longer AFK: 14% chance");
				sleep = Sleep.calculate(10000,60000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 50000);
			}
			else if(tmp < 45)  
			{
				MethodProvider.log("Longer AFK: 20% chance");
				sleep = Sleep.calculate(10000,40000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 40000);
			}
			else if(tmp < 65)  
			{
				MethodProvider.log("Longer AFK: 20% chance");
				sleep = Sleep.calculate(10000,30000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 30000);
			}
			else if(tmp < 1000)  
			{
				MethodProvider.log("Longer AFK: 35% chance");
				sleep = Sleep.calculate(10000,20000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 30000);
			}
			Timer sleepTimer = new Timer(sleep);
			final int mean = sleep / 2;
			final int sigma = sleep * 2 / 5;
			boolean examined = false;
			Timer antibanTimer = new Timer((int) Calculations.nextGaussianRandom(mean, sigma));
			while(!sleepTimer.finished() && !ScriptManager.getScriptManager().isPaused() &&
					ScriptManager.getScriptManager().isRunning())
			{
				Main.customPaintText1 = "~~~~~~~~~~~~~";
				Main.customPaintText2 = "~~~~~AFK~~~~~";
				Main.customPaintText3 = "~~ "+Timer.formatTime(sleepTimer.remaining())+" ~~";
				Main.customPaintText4 = "~~~~~~~~~~~~~";
				if(antibanTimer.finished())
				{
					if(examineObject && !examined)
					{
						UniqueActions.examineRandomObect();
						examined = true;
					}
						
					if(moveMouse && 
						Mouse.isMouseInScreen() && 
						Mouse.moveMouseOutsideScreen()) Sleep.sleep(669,1111);
				}
				Sleep.sleep(69, 69);
				
			}
		}
		Main.customPaintText1 = "";
		Main.customPaintText2 = "";
		Main.customPaintText3 = "";
		Main.customPaintText4 = "";
	}
	public static void randomAFK(int chance)
	{
		int roll = API.rand2.nextInt(100);
		boolean moveMouse = false;
		boolean examineObject = false;
		int antibanChance = chance;
		if(UniqueActions.isActionEnabled(Actionz.MOVE_MOUSE_OFFSCREEN) || 
				UniqueActions.isActionEnabled(Actionz.EXAMINE_RANDOM_OBJECTS))
		{
			antibanChance = antibanChance * 3;
		}
			
		if(API.rand2.nextInt(100) <= antibanChance)
		{
			if(UniqueActions.isActionEnabled(Actionz.MOVE_MOUSE_OFFSCREEN) && 
					UniqueActions.isActionEnabled(Actionz.EXAMINE_RANDOM_OBJECTS))
			{
				if(API.rand2.nextInt(100) > 20) moveMouse = true;
				else examineObject = true;
			}
			else if(UniqueActions.isActionEnabled(Actionz.MOVE_MOUSE_OFFSCREEN))
			{
				moveMouse = true;
			}
			else if(UniqueActions.isActionEnabled(Actionz.EXAMINE_RANDOM_OBJECTS))
			{
				if(API.rand2.nextInt(100) > 90) examineObject = true;
			}
		}
		if(roll < chance)
		{
			int tmp = API.rand2.nextInt(100);
			int sleep = 0;
			if(tmp < 2)  
			{
				MethodProvider.log("AFK: 1% chance, max 240s");
				sleep = Sleep.calculate(50,84000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 100000);
			}
			else if(tmp < 6)  
			{
				MethodProvider.log("AFK: 5% chance, max 120s");
				sleep = Sleep.calculate(50,40000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 70000);
			}
			else if(tmp < 25)
			{
				MethodProvider.log("AFK: 14% chance, max 40s");
				sleep = Sleep.calculate(50,20000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 40000);
			}
			else if(tmp < 45)  
			{
				MethodProvider.log("AFK: 20% chance, max 20s");
				sleep = Sleep.calculate(50,10000);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 20000);
			}
			else if(tmp < 65)  
			{
				MethodProvider.log("AFK: 20% chance, max 6.0s");
				sleep = Sleep.calculate(50,4500);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 6000);
			}
			else if(tmp < 1000)  
			{
				MethodProvider.log("AFK: 35% chance, max 3.2s");
				sleep = Sleep.calculate(50,2400);
				if(UniqueActions.isActionEnabled(Actionz.AFK_LONGER)) sleep += Calculations.random(1, 3500);
			}
			Timer sleepTimer = new Timer(sleep);
			final int mean = sleep / 2;
			final int sigma = sleep * 2 / 5;
			boolean examined = false;
			Timer antibanTimer = new Timer((int) Calculations.nextGaussianRandom(mean, sigma));
			while(!sleepTimer.finished() && !ScriptManager.getScriptManager().isPaused() &&
					ScriptManager.getScriptManager().isRunning())
			{
				Main.customPaintText1 = "~~~~~~~~~~~~~";
				Main.customPaintText2 = "~~~~~AFK~~~~~";
				Main.customPaintText3 = "~~ "+Timer.formatTime(sleepTimer.remaining())+" ~~";
				Main.customPaintText4 = "~~~~~~~~~~~~~";
				if(antibanTimer.finished())
				{
					if(examineObject && !examined)
					{
						UniqueActions.examineRandomObect();
						examined = true;
					}
						
					if(moveMouse && 
						Mouse.isMouseInScreen() && 
						Mouse.moveMouseOutsideScreen()) Sleep.sleep(669,1111);
				}
				Sleep.sleep(69, 69);
				
			}
		}
		Main.customPaintText1 = "";
		Main.customPaintText2 = "";
		Main.customPaintText3 = "";
		Main.customPaintText4 = "";
	}
/*
    public int randWeightedInt(int min, int max, double mean, double sigma, int weightSkew, int weightBias) {
        int ra = randBellWeight(min, max, weightSkew, weightBias);
        int sorted = Math.min(max, Math.max(min, ra));
        if (min >= 0 && max > 0)
            return Math.abs(sorted);
        else
            return sorted;
    }

    private int randBellWeight(int min, int max, int weightSkew, int weightBias) {
        if (max <= min)
            max = min + 1;
        return (int) nextSkewedBoundedDouble(min, max, weightSkew / 10d, weightBias / 10d);
    }

    private double nextSkewedBoundedDouble(double min, double max, double skew, double bias) {
        double range = max - min;
        double mid = min + range / 2.0;
        double unitGaussian = Calculations.nextGaussianRandom();
        double biasFactor = Math.exp(bias);
        return mid + (range * (biasFactor / (biasFactor + Math.exp(-unitGaussian / skew)) - 0.5));
    }
	*/
	public static void talkToNPC(String npcName)
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName);
		interactNPC(filter,null,false,false,null,null);
	}
	public static void talkToNPC(String npcName, String action)
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName) && n.hasAction(action);
		interactNPC(filter,action,false,false,null,null);
	}
	public static void talkToNPC(String npcName, String action, Area npcArea)
	{
		Filter<NPC> filter = n ->  n != null && n.getName().contains(npcName) && n.hasAction(action) && npcArea.contains(n);
		interactNPC(filter,action,false,false,npcArea,null);
	}
	public static void interactNPC(String npcName, String action, Area npcArea, boolean reachable, Condition condition)
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName) && n.hasAction(action) && npcArea.contains(n);
		interactNPC(filter, action, false, reachable,npcArea,condition);
	}
	public static void walkTalkToNPC(String npcName, String action, Area npcArea,String...waitPhrases)
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName) && n.hasAction(action) && npcArea.contains(n);
		interactNPC(filter, action, true, false,npcArea,null);
	}
	public static void walkTalkToNPC(String npcName, String action, Area npcArea)
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName) && n.hasAction(action) && npcArea.contains(n);
		interactNPC(filter, action, true, false,npcArea,null);
	}
	public static void walkInteractNPC(String npcName, String action, Area npcArea, Condition condition)
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName) && n.hasAction(action) && npcArea.contains(n);
		interactNPC(filter, action, true, false,npcArea,condition);
	}
	public static void interactNPC(String npcName, String action, int combatLevel, Area npcArea, boolean reachable, Condition condition) 
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName) && n.hasAction(action) && n.getLevel() == combatLevel && npcArea.contains(n);
		interactNPC(filter, action, false, true,npcArea,condition);
	}
	
	public static void interactNPC(String npcName, String action)
	{
		Filter<NPC> filter = n -> 
			n != null && 
			n.getName().contains(npcName) && 
			n.hasAction(action);
			interactNPC(filter, action, false, false,null,null);
	}
	public static void interactNPC(Filter<NPC> filter,String action,boolean walk,boolean reachable,Area area, Condition condition)
	{
		if(walk && !area.contains(Players.localPlayer()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(area.getCenter())) Sleep.sleep(696, 666);
			return;
		}
		NPC npc = NPCs.closest(filter);
		if(npc == null)
		{
			MethodProvider.log("NPC null!");
			return;
		}
		if(reachable && !npc.canReach())
		{
			if(Walking.walk(npc)) Sleep.sleep(420, 696);
			return;
		}
		
		boolean interacted = false;
		if(action == null)
		{
			if(npc.interact()) interacted = true;
		}
		else if(npc.interact(action)) interacted = true;
		if(interacted)
		{
			if(condition == null) condition = Dialogues::inDialogue;
			final Condition finalCondition = condition;
			MethodProvider.sleepUntil(() -> finalCondition.verify(),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(6666, 4444),69);
		}
	}
	private static void interactGameObject(Filter<GameObject> filter,String action,boolean walkToArea,boolean reachable,Area area, Condition condition)
	{
		if(walkToArea && !area.contains(Players.localPlayer()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(area.getCenter())) Sleep.sleep(696, 666);
			return;
		}
		GameObject go = GameObjects.closest(filter);
		if(go == null)
		{
			MethodProvider.log("GameObject null!");
			return;
		}
		if(reachable)
		{
			boolean reachableSurrounding = false;
			for(Tile t : go.getSurrounding())
			{
				if(t.canReach())
				{
					reachableSurrounding = true;
					break;
				}
			}
			if(reachableSurrounding == false)
			{
				if(Walking.shouldWalk(6) && Walking.walk(go)) Sleep.sleep(696, 666);
				return;
			}
			//must be able to reach it now, continue
		}
		boolean interacted = false;
		if(action == null)
		{
			if(go.interact()) interacted = true;
		}
		else if(go.interact(action)) interacted = true;
		if(interacted)
		{
			if(condition == null) condition = () -> go.getSurrounding().contains(Players.localPlayer().getTile());
			final Condition finalCondition = condition;
			MethodProvider.sleepUntil(() -> finalCondition.verify(),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(6666, 4444),69);
		}
	}
	public static void walkTalkWithGameObject(String objectName, String action) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action);
		interactGameObject(filter, action, false,true,null,Dialogues::inDialogue);
	}
	public static void walkTalkWithGameObject(String objectName) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName);
		interactGameObject(filter, null, false,true,null,Dialogues::inDialogue);
	}
	public static void walkTalkWithGameObject(String objectName, Area searchArea) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && searchArea.contains(n);
		interactGameObject(filter, null, true,true,searchArea,Dialogues::inDialogue);
	}

	public static void walkTalkWithGameObject(String objectName, String action, Area searchArea) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName)  && n.hasAction(action) && searchArea.contains(n);
		interactGameObject(filter, action, false,true,searchArea,Dialogues::inDialogue);
	}
	public static void interactWithGameObject(String objectName, String action)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName)  && n.hasAction(action);
		interactGameObject(filter, action, false,true,null,null);
	}
	public static void interactWithGameObject(String objectName, String action,Tile tile,Condition condition)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && n.getTile().equals(tile);
		interactGameObject(filter, action, false,true,null,condition);
	}
	public static void interactWithGameObject(String objectName, String action,Condition condition)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action);
		interactGameObject(filter, action, false,true,null,condition);
	}
	public static void interactWithGameObject(String objectName, String action,Tile tile)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && n.getTile().equals(tile);
		interactGameObject(filter, action, false,true,null,null);
	}
	public static void interactWithGameObject(String objectName, String action, Area gameObjectArea)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && gameObjectArea.contains(n);
		interactGameObject(filter, action, false,true,null,null);
	}
	public static void walkInteractWithGameObject(String objectName, String action, Area gameObjectArea)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && gameObjectArea.contains(n);
		interactGameObject(filter, action, true,true,gameObjectArea,null);
	}
	public static void walkInteractWithGameObject(String objectName, String action, Area gameObjectArea, Condition condition)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && gameObjectArea.contains(n);
		interactGameObject(filter, action, true,true,gameObjectArea,condition);
	}
	private static void interactGroundItem(Filter<GroundItem> filter,String action,boolean walkToArea,boolean reachable,Area area, Condition condition)
	{
		if(walkToArea && !area.contains(Players.localPlayer()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(area.getCenter())) Sleep.sleep(696, 666);
			return;
		}
		GroundItem go = GroundItems.closest(filter);
		if(go == null)
		{
			MethodProvider.log("GroundItem null!");
			return;
		}
		if(reachable)
		{
			boolean reachableSurrounding = false;
			for(Tile t : go.getSurroundingArea(1).getTiles())
			{
				if(t.canReach())
				{
					reachableSurrounding = true;
					break;
				}
			}
			if(reachableSurrounding == false)
			{
				if(Walking.shouldWalk(6) && Walking.walk(go)) Sleep.sleep(696, 666);
				return;
			}
			//must be able to reach it now, continue
		}
		boolean interacted = false;
		if(action == null)
		{
			if(go.interact()) interacted = true;
		}
		else if(go.interact(action)) interacted = true;
		if(interacted)
		{
			final int count = Inventory.count(go.getID());
			if(condition == null) condition = () -> Inventory.count(go.getID()) > count;
			final Condition finalCondition = condition;
			MethodProvider.sleepUntil(() -> finalCondition.verify(),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(6666, 4444),69);
		}
	}
	public static void walkPickupGroundItem(int groundItemID, String action, boolean reachable,Area groundItemArea)
	{
		Filter<GroundItem> filter = n -> n != null && n.getID() == groundItemID && n.hasAction(action) && groundItemArea.contains(n);
		interactGroundItem(filter, action, true,reachable,groundItemArea,null);
	}
	public static void walkPickupGroundItem(String groundItemName, String action, Area groundItemArea)
	{
		Filter<GroundItem> filter = n -> n != null && n.getName().equals(groundItemName) && n.hasAction(action) && groundItemArea.contains(n);
		interactGroundItem(filter, action, true,true,groundItemArea,null);
	}
	public static int roundToMultiple (int number,int multiple){
	    int result = multiple;
	    //If not already multiple of given number
	    if (number % multiple != 0)
	    {
	        int division = number / multiple;
	        result = division * multiple;
	    }
	    return result;
	}
	public static int getRandomP2PWorld()
	{
		List<World> verifiedWorlds = new ArrayList<World>();
		for(World tmp : Worlds.noMinimumLevel())
		{
			if(	tmp.isMembers()
					&& !tmp.isPVP()
					&& !tmp.isTournamentWorld()
					&& !tmp.isDeadmanMode()
					&& !tmp.isHighRisk() 
					&& !tmp.isLeagueWorld()
					&& !tmp.isSuspicious()
					&& !tmp.isPvpArena() 
					&& !tmp.isTargetWorld()
					&& tmp.getWorld() != 302) //just avoid popular world)
			{
				verifiedWorlds.add(tmp);
			}
		}
		Collections.shuffle(verifiedWorlds);
		return verifiedWorlds.size() > 0 ? verifiedWorlds.get(0).getWorld() : 302; // default world 302 if none found
	}
	
	
	
	
	
	
	
	
	
	
	
}
