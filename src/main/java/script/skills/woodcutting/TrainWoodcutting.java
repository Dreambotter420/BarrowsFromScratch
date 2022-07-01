package script.skills.woodcutting;

import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleep;
import script.utilities.Walkz;
/**
 * Trains woodcutting 35-40
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainWoodcutting extends Leaf {
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_WOODCUTTING;
	}
	
	public static boolean initialized = false;
	public static boolean completedWoodcutting = false;
	public static final int ironAxe = 1349;
	public static final int steelAxe = 1353;
	public static final int blackAxe = 1361;
	public static final int mithAxe = 1355;
	public static final int addyAxe = 1357;
	public static final int oakLogs = 1521;
	public static final int logs = 1511;
	public static final int tree1 = 1278;
	public static final int tree2 = 1276;
	public static final int oakTree = 10820;
	
	public static Area wcArea = (((int) Calculations.nextGaussianRandom(1000, 50)) > 1000 ? Locations.camelotTrees : Locations.castleWarsTrees);
	
	public static final String[] objectsBlockingFires = {
			"Daisies",
			"Flower",
			"Small fern"
	};
    public void onStart() {
        instantiateTree();
        if(wcArea == Locations.camelotTrees) MethodProvider.log("[WOODCUTTING] -> Set area to Camelot!");
        else MethodProvider.log("[WOODCUTTING] -> Set area to Castle Wars!");
        initialized = true;
    }
    
    private final Tree tree = new Tree();
    private void instantiateTree() {
    	
    }
    @Override
    public int onLoop() {
    	if(!initialized) onStart();
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Woodcutting!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
    	API.randomAFK();
    	if(Dialogues.canContinue())
    	{
    		boolean skip = (Calculations.nextGaussianRandom(500,5) > 501 ? true : false);
    		if(!skip) 
    		{
    			if(Dialogues.continueDialogue()) Sleep.sleep(222,1111);
    		}
    	}
    	GroundItem birdNest = GroundItems.closest(nest -> 
    			nest != null && 
    			nest.getName().contains("Bird nest"));
    	if(birdNest != null)
    	{
    		MethodProvider.log("See bird nest! Attempting to grab it..");
    		boolean bankInstead = false;
    		final int count = Inventory.fullSlotCount();
    		if(count == 28)
    		{
    			if(Inventory.contains(logs))
    			{
    				if(Inventory.drop(logs)) MethodProvider.sleepUntil(() -> Inventory.fullSlotCount() > count, () -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222), 50);
    				return Timing.sleepLogNormalSleep();
    			}
    			if(Inventory.contains(oakLogs))
    			{
    				if(Inventory.drop(oakLogs)) MethodProvider.sleepUntil(() -> Inventory.fullSlotCount() > count, () -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222), 50);
    				return Timing.sleepLogNormalSleep();
    			}
    			else 
    			{
    				bankInstead = true;
    				MethodProvider.log("See birds nest but invy full and no logs to drop! banking...");
    			}
    		}
    		if(!bankInstead)
    		{
    			if(birdNest.distance() < 8 && birdNest.interact("Take"))
        		{
        			MethodProvider.sleepUntil(() -> Inventory.fullSlotCount() > count, () -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222), 50);
        		}
        		if(Inventory.fullSlotCount() > count)
        		{
        			MethodProvider.log("Got birds nest! Total so far (invy + bank): " +(Inventory.count("Bird nest") + Bank.count("Bird nest")));
        		}
        		return Timing.sleepLogNormalSleep();
    		}
    	}
    			
    	final int wc = Skills.getRealLevel(Skill.WOODCUTTING);
    	if (wc >= DecisionLeaf.wcSetpoint) {
            MethodProvider.log("[COMPLETE] -> lvl "+DecisionLeaf.wcSetpoint+" woodcutting!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
    	InvEquip.clearAll();
    	int careAboutAxe = 0;
    	//add axes to inventory
    	if(wc < 6) 
    	{
    		InvEquip.addInvyItem(ironAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(steelAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(blackAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(addyAxe, 1, 1, false, 1);
    		careAboutAxe = ironAxe;
    	}
    	else if(wc < 11) 
    	{
    		InvEquip.addInvyItem(steelAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(blackAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(addyAxe, 1, 1, false, 1);
    		careAboutAxe = steelAxe;
    	}
    	else if(wc < 21) 
    	{
    		InvEquip.addInvyItem(blackAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(addyAxe, 1, 1, false, 1);
    		careAboutAxe = blackAxe;
    	}
    	else if(wc < 31) 
    	{
    		InvEquip.addInvyItem(mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(addyAxe, 1, 1, false, 1);
    		careAboutAxe = mithAxe;
    	}
    	else //adamant axe max 
    	{
    		InvEquip.addInvyItem(addyAxe, 1, 1, false, 1);
    		careAboutAxe = addyAxe;
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		
		boolean careAboutOaks = (wc >= 15 ? true : false);
		
		//inventory full or not have care about axe - go get
		if(Inventory.isFull() || !Inventory.contains(careAboutAxe))
		{
			InvEquip.fulfillSetup(true,60000);
			return Timing.sleepLogNormalSleep();
		}
		if(wcArea == Locations.castleWarsTrees)
		{
			//if not in in chopping area, walk to it - give it 10 minutes timer
			if(Locations.castleWarsTrees.contains(Players.localPlayer()) || Walkz.goToCastleWars(600000))
			{
				return chopTree(careAboutOaks);
			}
		}
		if(wcArea == Locations.camelotTrees)
		{
			//if not in in chopping area, walk to it - give it 10 minutes timer
			if(Locations.camelotTrees.contains(Players.localPlayer()) || Walkz.goToCamelotTrees(600000))
			{
				//inventory not full and have care about axe - go chop
				return chopTree(careAboutOaks);
			}
		}
		return Timing.sleepLogNormalSleep();
    }
    
    public static int chopTree(boolean oaks)
    {
    	Filter<GameObject> filterTrees = (t -> 
		t != null && 
		wcArea.contains(t) &&
		((!oaks && t.getID() == tree1) || 
		(!oaks && t.getID() == tree2) || 
		(oaks && t.getID() == oakTree))
		);
    	List<GameObject> validTrees = GameObjects.all(filterTrees);
    	GameObject closestAvailableTree = null;
    	for(GameObject tree : validTrees) 
    	{
    		boolean seePlayerOnTree = false;
    		for(Tile aroundTile : tree.getSurrounding())
    		{
    			for(Player p : Players.all(p -> 
    			p != null && 
    			p.getTile() == aroundTile && 
    			p.isAnimating()))
    			{
    				seePlayerOnTree = true;
    				break;
    			}
    			if(seePlayerOnTree) break;
    		}
    		if(seePlayerOnTree) continue;
	
    		//tree is OK, check distance and add
    		if(closestAvailableTree == null) closestAvailableTree = tree;
    		else
    		{
    			double distToBeat = closestAvailableTree.distance();
    			double distChallenging = tree.distance();
    			if(distChallenging <= distToBeat) closestAvailableTree = tree;
    		}
		}
		//tree OK to chop, DO IT
		if(closestAvailableTree == null)
		{
			MethodProvider.log("Hmmmm no trees available in area! Walking to center of it");
			if(Walking.shouldWalk(6) && Walking.walk(wcArea.getCenter())) Sleep.sleep(666, 1111);
			return Timing.sleepLogNormalSleep();
		}
		else
		{
			if(Players.localPlayer().isAnimating())
			{
				final int invCount = Inventory.fullSlotCount();
				MethodProvider.sleepUntil(() -> Inventory.fullSlotCount() > invCount, Sleep.calculate(3333, 3333));
				return Timing.sleepLogNormalSleep();
			}
			if(Players.localPlayer().isMoving())
			{
				MethodProvider.sleepUntil(() -> !Players.localPlayer().isMoving(), Sleep.calculate(3333, 3333));
				return Timing.sleepLogNormalSleep();
				
			}
			Sleep.sleep(222, 1111);
			if(!Tabs.isOpen(Tab.INVENTORY))
			{
				boolean openInvy = (Calculations.nextGaussianRandom(500,5) > 491 ? false : true);
				if(openInvy) 
				{
					if(Tabs.open(Tab.INVENTORY)) Sleep.sleep(222,1111);
				}
			}
			
			if(Players.localPlayer().isAnimating() || Players.localPlayer().isMoving()) return Timing.sleepLogNormalSleep();
			if(closestAvailableTree.exists() || closestAvailableTree.distance() <= 8)
			{
				if(closestAvailableTree.interact("Chop down")) return Timing.sleepLogNormalInteraction();
			}
			if(Walking.shouldWalk() && Walking.walk(closestAvailableTree.getTile())) Sleep.sleep(666, 1111);
		}
		return Timing.sleepLogNormalSleep();
    }
}
