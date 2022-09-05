package script.actionz;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.Player;

import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.GrandExchangg;
import script.utilities.Sleep;


public class ReportSpammer extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return spammerToReport != null && !spammerToReport.isEmpty() && 
	    		!Inventory.isItemSelected() && !Magic.isSpellSelected();
	 }
   public static String spammerToReport = null;
    @Override
    public int onLoop() {
    	if(Widgets.getWidgetChild(553, 5) != null && 
    			Widgets.getWidgetChild(553, 5).isVisible())
    	{
    		if(Widgets.getWidgetChild(553, 5).getText().equals("<col=ffff00>|"))
    		{
    			Keyboard.type(spammerToReport,false);
    			Sleep.sleep(1111,1111);
    			return Timing.sleepLogNormalSleep();
    		}
    		String exactNameString = spammerToReport.concat("<col=ffff00>|");
    		if(Widgets.getWidgetChild(553, 5).getText().equals(exactNameString))
    		{
    			if(Widgets.getWidgetChild(553, 14 , 18).interact("Send report"))
    			{
    				Sleep.sleep(111,1111);
    			}
    			return Timing.sleepLogNormalSleep();
    		}
    		Keyboard.typeSpecialKey(8);
			return Sleep.calculate(69,420);
    	}
    	if(Bank.isOpen())
    	{
    		Bankz.close();
    		return Timing.sleepLogNormalSleep();
    	}
    	if(GrandExchange.isOpen())
    	{
    		GrandExchangg.close();
    		return Timing.sleepLogNormalSleep();
    	}
    	Player spammer = Players.closest(p -> p!=null && p.getName().contains(spammerToReport));
    	if(spammer != null && spammer.distance() <= 8)
    	{
    		if(Inventory.isItemSelected()) Inventory.deselect();
    		if(Magic.isSpellSelected())Magic.deselect();
    		if(spammer.interact("Report"))
    		{
    			MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(553, 5) != null && 
    			Widgets.getWidgetChild(553, 5).isVisible(), Sleep.calculate(2222,2222));
    		}
    		return Timing.sleepLogNormalSleep();
    	}
    	if(Widgets.getWidgetChild(162, 31).interact("Report abuse"))
    	{
    		MethodProvider.sleepUntil(() -> spammerToReport == null,Sleep.calculate(2222,2222));
    		Sleep.sleep(111,666);
    	}
    		
    	return Timing.sleepLogNormalSleep();
    }
}
