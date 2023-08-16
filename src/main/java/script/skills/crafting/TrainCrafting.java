package script.skills.crafting;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.widget.helpers.ItemProcessing;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.InvEquip;
import script.utilities.Sleepz;
/**
 * Trains crafting 35-40
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainCrafting extends Leaf {
	private final static int threadMax = (25+(int)(Calculations.nextGaussianRandom(20, 10)));
	private final static int threadResupply = (75+(int)(Calculations.nextGaussianRandom(50, 10)));
	private final static int leatherResupply = (350+(int)(Calculations.nextGaussianRandom(50, 10)));
	
	private final static int needle = 1733;
	private final static int leather = 1741;
	private final static int thread = 1734;
    @Override
    public int onLoop() {
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		Logger.log("[TIMEOUT] -> Crafting!");
            API.mode = null;
            return Sleepz.sleepTiming();
    	}
    	final int crafting = Skills.getRealLevel(Skill.CRAFTING);
    	if (crafting >= DecisionLeaf.craftingSetpoint) {
            Logger.log("[COMPLETE] -> lvl "+DecisionLeaf.craftingSetpoint+" crafting!");
           	API.mode = null;
            return Sleepz.sleepTiming();
        }

    	doCrafting();
        
        return Sleepz.calculate(69,420);
    }
    public static void doCrafting()
    {
    	if(!craftLeatherThing(Skills.getRealLevel(Skill.CRAFTING)))
        {
        	InvEquip.clearAll();
            InvEquip.addInvyItem(needle, 1, 1, false, 1);
            InvEquip.addInvyItem(thread, 1, threadMax, false, threadResupply);
            InvEquip.addInvyItem(leather, 1, 26, false, leatherResupply);
            InvEquip.addInvyItem(InvEquip.coins, 0,0, false,0);
        	if(InvEquip.fulfillSetup(true,60000))
            {
            	Logger.log("Should be equipped now for crafting!");
            }
        }
    }
    public static boolean craftLeatherThing(int craftingLvl)
    {
    	String name = null;
    	if(craftingLvl < 7) name = "Leather gloves";
    	else if(craftingLvl < 9) name = "Leather boots";
    	else if(craftingLvl < 11) name = "Leather cowl";
    	else if(craftingLvl < 14) name = "Leather vambraces";
    	else if(craftingLvl < 18) name = "Leather body";
    	else if(craftingLvl < 25) name = "Leather chaps";
    	if(Inventory.containsAll(thread,needle,leather))
    	{
    		if(Bank.isOpen())
        	{
        		Bankz.close();
        		return true;
        	}
        	if(ItemProcessing.isOpen())
        	{
        		if(ItemProcessing.makeAll(name))
        		{
        			Logger.log("Starting to make all: " + name);
        			Sleep.sleepUntil(() -> Inventory.count(leather) <= 0 || Dialogues.canContinue(),() -> Players.getLocal().isAnimating(), Sleepz.calculate(2222, 2222),50);   
        		}
        		return true;
        	}
        	else
        	{
        		if(Players.getLocal().isAnimating())
        		{
        			Sleep.sleepUntil(() -> Inventory.count(leather) <= 0 || Dialogues.canContinue(), () -> Players.getLocal().isAnimating(), Sleepz.calculate(2222, 2222),50);    		
        			return true;
        		}
        		Item leatherItem = Inventory.get(leather);
        		Item needleItem = Inventory.get(needle);
        		if(leatherItem == null || needleItem == null) return false;
        		int rand = (int) Calculations.nextGaussianRandom(75, 20);
        		if(rand < 65)
        		{
        			if(leatherItem.useOn(needleItem))
            		{
            			Sleep.sleepUntil(() -> ItemProcessing.isOpen(), Sleepz.calculate(2222, 2222));
            		}
        		}
        		else
        		{
        			if(needleItem.useOn(leatherItem))
            		{
            			Sleep.sleepUntil(() -> ItemProcessing.isOpen(), Sleepz.calculate(2222, 2222));
            		}
        		}
        	}
        	return true;
    	}
    	return false;
    }
	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_CRAFTING;
	}
}
