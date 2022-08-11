package script.behaviour;

import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.widget.Widgets;

import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
import script.utilities.Sleep;


public class ReportSpammer extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return spammerToReport != null;
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
    			Sleep.sleep(2222,2222);
    			return Timing.sleepLogNormalSleep();
    		}
    		String exactNameString = spammerToReport.concat("<col=ffff00>|");
    		if(Widgets.getWidgetChild(553, 5).getText().equals(exactNameString))
    		{
    			if(Widgets.getWidgetChild(553, 14 , 18).interact("Send report"))
    			{
    				Sleep.sleep(2222,2222);
    			}
    			return Timing.sleepLogNormalSleep();
    		}
    		Keyboard.typeSpecialKey(8);
			return Sleep.calculate(69,420);
    	}
    	if(Bank.isOpen())
    	{
    		Bank.close();
    		return Timing.sleepLogNormalSleep();
    	}
    	if(GrandExchange.isOpen())
    	{
    		GrandExchange.close();
    		return Timing.sleepLogNormalSleep();
    	}
    	if(Widgets.getWidgetChild(162, 31).interact("Report abuse")) Sleep.sleep(696,666);
    	return Timing.sleepLogNormalSleep();
    }
}
