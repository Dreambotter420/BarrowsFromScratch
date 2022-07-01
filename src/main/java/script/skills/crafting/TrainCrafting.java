package script.skills.crafting;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.widget.helpers.ItemProcessing;
import org.dreambot.api.wrappers.items.Item;

import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
import script.utilities.InvEquip;
import script.utilities.Sleep;
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
	private final static int  thread = 1734;
    @Override
    public int onLoop() {
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		MethodProvider.log("[TIMEOUT] -> Crafting!");
            API.mode = null;
            return Timing.sleepLogNormalSleep();
    	}
    	final int crafting = Skills.getRealLevel(Skill.CRAFTING);
    	if (crafting >= DecisionLeaf.craftingSetpoint) {
            MethodProvider.log("[COMPLETE] -> lvl "+DecisionLeaf.craftingSetpoint+" crafting!");
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        
        InvEquip.clearAll();
        InvEquip.addInvyItem(needle, 1, 1, false, 1);
        InvEquip.addInvyItem(thread, 1, threadMax, false, threadResupply);
        InvEquip.addInvyItem(leather, 1, 26, false, leatherResupply);
        InvEquip.addInvyItem(InvEquip.coins, 0,0, false,0);
        if(!craftLeatherThing(crafting))
        {
        	if(InvEquip.fulfillSetup(false,60000))
            {
            	MethodProvider.log("Should be equipped now ;-)");
            }
        }
        
        return Sleep.calculate(69,420);
    }
    public static boolean craftLeatherThing(int crafting)
    {
    	String name = null;
    	if(crafting < 7) name = "Leather gloves";
    	else if(crafting < 9) name = "Leather boots";
    	else if(crafting < 11) name = "Leather cowl";
    	else if(crafting < 14) name = "Leather vambraces";
    	else if(crafting < 18) name = "Leather body";
    	else if(crafting < 25) name = "Leather chaps";
    	if(Inventory.containsAll(thread,needle,leather))
    	{
    		if(Bank.isOpen())
        	{
        		Bank.close();
        		return true;
        	}
        	if(ItemProcessing.isOpen())
        	{
        		if(ItemProcessing.makeAll(name))
        		{
        			MethodProvider.sleepUntil(() -> Inventory.count(leather) <= 0 || Dialogues.canContinue(),() -> Players.localPlayer().isAnimating(), Sleep.calculate(2222, 2222),50);   
        		}
        		return true;
        	}
        	else
        	{
        		if(Players.localPlayer().isAnimating())
        		{
        			MethodProvider.sleepUntil(() -> Inventory.count(leather) <= 0 || Dialogues.canContinue(), () -> Players.localPlayer().isAnimating(), Sleep.calculate(2222, 2222),50);    		
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
            			MethodProvider.sleepUntil(() -> ItemProcessing.isOpen(), Sleep.calculate(2222, 2222));
            		}
        		}
        		else
        		{
        			if(needleItem.useOn(leatherItem))
            		{
            			MethodProvider.sleepUntil(() -> ItemProcessing.isOpen(), Sleep.calculate(2222, 2222));
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
