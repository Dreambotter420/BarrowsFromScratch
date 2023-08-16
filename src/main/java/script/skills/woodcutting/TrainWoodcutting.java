package script.skills.woodcutting;

import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
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
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.framework.Tree;
import script.utilities.API;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.Sleepz;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;
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
        if(wcArea == Locations.camelotTrees) Logger.log("[WOODCUTTING] -> Set area to Camelot!");
        else Logger.log("[WOODCUTTING] -> Set area to Castle Wars!");
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
    		Logger.log("[TIMEOUT] -> Woodcutting!");
            API.mode = null;
            return Sleepz.sleepTiming();
    	}
    	if(Dialoguez.handleDialogues()) return Sleepz.calculate(420, 696);
    	GroundItem birdNest = GroundItems.closest(nest -> 
    			nest != null && 
    			nest.getName().contains("Bird nest"));
    	if(birdNest != null)
    	{
    		Logger.log("See bird nest! Attempting to grab it..");
    		boolean bankInstead = false;
    		final int count = Inventory.fullSlotCount();
    		if(count == 28)
    		{
    			if(Inventory.contains(id.logs))
    			{
    				if(Inventory.drop(id.logs)) Sleep.sleepUntil(() -> Inventory.fullSlotCount() > count, () -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222), 50);
    				return Sleepz.sleepTiming();
    			}
    			if(Inventory.contains(id.oakLogs))
    			{
    				if(Inventory.drop(id.oakLogs)) Sleep.sleepUntil(() -> Inventory.fullSlotCount() > count, () -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222), 50);
    				return Sleepz.sleepTiming();
    			}
    			else 
    			{
    				bankInstead = true;
    				Logger.log("See birds nest but invy full and no logs to drop! banking...");
    			}
    		}
    		if(!bankInstead)
    		{
    			if(birdNest.distance() < 8 && birdNest.interact("Take"))
        		{
        			Sleep.sleepUntil(() -> Inventory.fullSlotCount() > count, () -> Players.getLocal().isMoving(),Sleepz.calculate(2222, 2222), 50);
        		}
        		if(Inventory.fullSlotCount() > count)
        		{
        			Logger.log("Got birds nest! Total so far (invy + bank): " +(Inventory.count("Bird nest") + Bank.count("Bird nest")));
        		}
        		return Sleepz.sleepTiming();
    		}
    	}
    	
    	final int wc = Skills.getRealLevel(Skill.WOODCUTTING);
    	if (wc >= DecisionLeaf.wcSetpoint) {
            Logger.log("[COMPLETE] -> lvl "+DecisionLeaf.wcSetpoint+" woodcutting!");
            API.mode = null;
            Main.clearCustomPaintText();
            return Sleepz.sleepTiming();
        }
    	final int birdNestsCount = (Inventory.count("Bird nest") + Bank.count("Bird nest"));	
    	Main.paint_itemsCount = "~~Have bird nests (bank + invy): " + birdNestsCount;
    	Main.paint_task = "~~~Training Woodcutting~~~";

    	Main.paint_levels = "Current wc lvl: " + wc + " and training to: " +DecisionLeaf.wcSetpoint;
    	InvEquip.clearAll();
    	int careAboutAxe = 0;
    	//add axes to inventory
    	if(wc < 6) 
    	{
    		InvEquip.addInvyItem(id.ironAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.steelAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.blackAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.addyAxe, 1, 1, false, 1);
    		careAboutAxe = id.ironAxe;
    	}
    	else if(wc < 11) 
    	{
    		InvEquip.addInvyItem(id.steelAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.blackAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.addyAxe, 1, 1, false, 1);
    		careAboutAxe = id.steelAxe;
    	}
    	else if(wc < 21) 
    	{
    		InvEquip.addInvyItem(id.blackAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.addyAxe, 1, 1, false, 1);
    		careAboutAxe = id.blackAxe;
    	}
    	else if(wc < 31) 
    	{
    		InvEquip.addInvyItem(id.mithAxe, 1, 1, false, 1);
    		InvEquip.addInvyItem(id.addyAxe, 1, 1, false, 1);
    		careAboutAxe = id.mithAxe;
    	}
    	else //adamant axe max 
    	{
    		InvEquip.addInvyItem(id.addyAxe, 1, 1, false, 1);
    		careAboutAxe = id.addyAxe;
    	}
    	InvEquip.addOptionalItem(InvEquip.jewelry);
    	InvEquip.shuffleFulfillOrder();
		InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		
		boolean careAboutOaks = (wc >= 15 ? true : false);
		
		//inventory full or not have care about axe - go get
		if(Inventory.isFull() || !Inventory.contains(careAboutAxe))
		{
			InvEquip.fulfillSetup(true,60000);
			return Sleepz.sleepTiming();
		}
		if(wcArea == Locations.castleWarsTrees)
		{

	    	Main.paint_subTask = "Training location: Castle Wars";
			//if not in in chopping area, walk to it - give it 10 minutes timer
			if(Locations.castleWarsTrees.contains(Players.getLocal()) || Walkz.goToCastleWars(600000))
			{
				return chopTree(careAboutOaks);
			}
		}
		if(wcArea == Locations.camelotTrees)
		{

	    	Main.paint_subTask = "Training location: Camelot";
			//if not in in chopping area, walk to it - give it 10 minutes timer
			if(Locations.camelotTrees.contains(Players.getLocal()) || Walkz.goToCamelotTrees(600000))
			{
				//inventory not full and have care about axe - go chop
				return chopTree(careAboutOaks);
			}
		}
		return Sleepz.sleepTiming();
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
			Logger.log("Hmmmm no trees available in area! Walking to center of it");
			if(Walking.shouldWalk(6) && Walking.walk(wcArea.getCenter())) Sleepz.sleep(666, 1111);
			return Sleepz.sleepTiming();
		}
		else
		{
			if(Players.getLocal().isAnimating())
			{
				final int invCount = Inventory.fullSlotCount();
				Sleep.sleepUntil(() -> Inventory.fullSlotCount() > invCount, Sleepz.calculate(3333, 3333));
				return Sleepz.sleepTiming();
			}
			if(Players.getLocal().isMoving())
			{
				Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), Sleepz.calculate(3333, 3333));
				return Sleepz.sleepTiming();
				
			}
			Sleepz.sleep(222, 1111);
			if(!Tabs.isOpen(Tab.INVENTORY))
			{
				boolean openInvy = (Calculations.nextGaussianRandom(500,5) > 491 ? false : true);
				if(openInvy) 
				{
					if(Tabz.open(Tab.INVENTORY)) Sleepz.sleep(222,1111);
				}
			}
			
			if(Players.getLocal().isAnimating() || Players.getLocal().isMoving()) return Sleepz.sleepTiming();
			if(closestAvailableTree.exists() || closestAvailableTree.distance() <= 8)
			{
				if(closestAvailableTree.interact("Chop down")) return Sleepz.interactionTiming();
			}
			if(Walking.shouldWalk() && Walking.walk(closestAvailableTree.getTile())) Sleepz.sleep(666, 1111);
		}
		return Sleepz.sleepTiming();
    }
}
