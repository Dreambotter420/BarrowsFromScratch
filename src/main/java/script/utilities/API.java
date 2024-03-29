package script.utilities;

import java.util.*;
import java.util.function.Function;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
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
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;


public class API {
	
	public static String currentBranch = "";
    public static String currentLeaf = "";
    
    public enum modes {
		TRAIN_SLAYER,
		TRAIN_WOODCUTTING,
		TRAIN_RANGE,
		TRAIN_MAGIC,
		TRAIN_MELEE,
		TRAIN_CRAFTING,
		TRAIN_PRAYER,
		TRAIN_FIREMAKING,
		TRAIN_AGILITY,
		TRAIN_WOODCUTTING_FIREMAKING,
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
		ALFRED_GRIMHANDS_BARCRAWL,
		TRAIN_HERBLORE,
		OBOR,
		TEST, 
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
		if(Dialogues.canContinue())
		{
			Dialoguez.continueDialogue();
			return true;
		}
		String[] x = Widgets.get(162,55).getText().split(":", 2);
    	String[] y = x[1].split("</col>",0);
    	String[] zeChatInputBox = y[0].split("<col=0000ff>",0);
		if(zeChatInputBox != null && zeChatInputBox.length >= 2)
		{
			Logger.log("Clearing chatbox of some contents");
	    	Keyboard.typeSpecialKey(8);
			Sleepz.calculate(10,55);
			return false;
		}
		return true;
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
	public static void walkTalkToNPC(String npcName, String action, boolean reachable, Area npcArea)
	{
		Filter<NPC> filter = n -> n != null && n.getName().contains(npcName) && n.hasAction(action) && npcArea.contains(n);
		interactNPC(filter, action, true, true,npcArea,null);
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
		if(walk && !area.contains(Players.getLocal()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(area.getCenter())) Sleepz.sleep(696, 666);
			return;
		}
		NPC npc = NPCs.closest(filter);
		if(npc == null)
		{
			Logger.log("NPC null!");
			return;
		}
		if(reachable && !npc.canReach())
		{
			if(Walking.walk(npc)) Sleepz.sleep(420, 696);
			return;
		}
		if(npc.distance() > 10) 
		{
			Walking.walk(npc);
			Sleep.sleepTick();
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
			Sleep.sleepUntil(() -> finalCondition.verify(),
					() -> Players.getLocal().isMoving(),Sleepz.calculate(6666, 4444),69);
		}
	}
	private static void interactGameObject(Filter<GameObject> filter,String action,boolean walkToArea,boolean reachable,Area area, Condition condition)
	{
		if(walkToArea && !area.contains(Players.getLocal()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(area.getCenter())) Sleepz.sleep(696, 666);
			return;
		}
		GameObject go = GameObjects.closest(filter);
		if(go == null)
		{
			Logger.log("GameObject null!");
			return;
		}
		if(reachable) {
			if(!go.canReach()) {
				if(Walking.shouldWalk(6) && Walking.walk(go)) Sleepz.sleep(696, 666);
				return;
			}
			//must be able to reach it now, continue
		}
		if(go.distance() > 10) 
		{
			Walking.walk(go);
			Sleep.sleepTick();
			return;
		}
		boolean interacted = false;
		if(action == null)
		{
			if(go.interact()) interacted = true;
		}
		else if(go.interact(action)) interacted = true;
		if(interacted)
		{
			if(condition == null) condition = () -> go.getSurrounding().contains(Players.getLocal().getTile());
			final Condition finalCondition = condition;
			Sleep.sleepUntil(() -> finalCondition.verify(),
					() -> Players.getLocal().isMoving(),Sleepz.calculate(6666, 4444),69);
		}
	}
	public static void walkTalkWithGameObject(String objectName, String action) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action);
		interactGameObject(filter, action, true,false,null,Dialogues::inDialogue);
	}
	public static void walkTalkWithGameObject(String objectName) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName);
		interactGameObject(filter, null, true,false,null,Dialogues::inDialogue);
	}
	public static void walkTalkWithGameObject(String objectName, Area searchArea) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && searchArea.contains(n);
		interactGameObject(filter, null, true,false,searchArea,Dialogues::inDialogue);
	}
	public static void walkTalkWithGameObject(String objectName, String action, Area searchArea, boolean standableTile) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName)  && n.hasAction(action) && searchArea.contains(n);
		interactGameObject(filter, action, true,true,searchArea,Dialogues::inDialogue);
	}
	public static void walkTalkWithGameObject(String objectName, String action, Area searchArea) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName)  && n.hasAction(action) && searchArea.contains(n);
		interactGameObject(filter, action, true,false,searchArea,Dialogues::inDialogue);
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
	public static void interactWithGameObject(String objectName, String action, Area gameObjectArea,Condition condition)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && gameObjectArea.contains(n);
		interactGameObject(filter, action, false,true,null,condition);
	}
	public static void walkInteractWithGameObject(String objectName, String action, Area gameObjectArea)
	{
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && gameObjectArea.contains(n);
		interactGameObject(filter, action, true,true,gameObjectArea,null);
	}
	public static void walkInteractWithGameObject(String objectName, String action, Area gameObjectArea, Condition condition) {
		Filter<GameObject> filter = n -> n != null && n.getName().contains(objectName) && n.hasAction(action) && gameObjectArea.contains(n);
		interactGameObject(filter, action, true,true,gameObjectArea,condition);
	}
	private static void interactGroundItem(Filter<GroundItem> filter,String action,boolean walkToArea,boolean reachable,Area area, Condition condition)
	{
		GroundItem go = GroundItems.closest(filter);
		if (reachable && go != null) {
			boolean reachableSurrounding = false;
			for(Tile t : go.getSurroundingArea(1).getTiles()) {
				if(t.canReach() && t.canReach(go.getTile())) {
					reachableSurrounding = true;
					break;
				}
			}
			if(reachableSurrounding == false) {
				if(Walking.shouldWalk(6) && Walking.walk(go)) {
					Sleepz.sleepInteraction();
				}
				return;
			}
			//must be able to reach it now, continue
		}
		if(walkToArea && !area.contains(Players.getLocal())) {
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(area.getCenter())) Sleepz.sleep(696, 666);
			return;
		}
		if (go == null) {
			Logger.log("GroundItem null!");
			return;
		}
		if(go.distance() > 10) Walking.walk(go);
		boolean interacted = false;
		if(action == null) {
			if(go.interact()) interacted = true;
		}
		else if(go.interact(action)) interacted = true;
		if(interacted) {
			final int count = Inventory.count(go.getID());
			if(condition == null) condition = () -> Inventory.count(go.getID()) > count;
			final Condition finalCondition = condition;
			Sleep.sleepUntil(() -> finalCondition.verify(),
					() -> Players.getLocal().isMoving(),Sleepz.calculate(6666, 4444),69);
		}
	}
	public static void walkPickupGroundItem(int groundItemID, String action, boolean reachable,Area groundItemArea) {
		Filter<GroundItem> filter = n -> n != null && n.getID() == groundItemID && n.hasAction(action) && groundItemArea.contains(n);
		interactGroundItem(filter, action, true,reachable,groundItemArea,null);
	}
	public static void walkPickupGroundItem(String groundItemName, String action, boolean reachable,Area groundItemArea) {
		Filter<GroundItem> filter = n -> n != null && n.getName().equals(groundItemName) && n.hasAction(action) && groundItemArea.contains(n);
		interactGroundItem(filter, action, true,reachable,groundItemArea,null);
	}
	public static void walkPickupGroundItem(String groundItemName, String action, Area groundItemArea) {
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
	public static <T> void sortAscending(List<T> list, Function<T, Comparable> getter) {
		list.sort(Comparator.comparing(getter));
	}

	public static <T> void sortDescending(List<T> list, Function<T, Comparable> getter) {
		list.sort(Comparator.comparing(getter).reversed());
	}
	
}
