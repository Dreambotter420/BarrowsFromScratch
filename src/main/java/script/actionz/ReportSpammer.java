package script.actionz;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;

import script.framework.Leaf;
import script.utilities.Bankz;
import script.utilities.GrandExchangg;
import script.utilities.Sleepz;


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
    	if(Widgets.get(553, 5) != null && 
    			Widgets.get(553, 5).isVisible())
    	{
    		if(Widgets.get(553, 5).getText().equals("<col=ffff00>|"))
    		{
    			Keyboard.type(spammerToReport,false);
    			Sleepz.sleep(1111,1111);
    			return Sleepz.sleepTiming();
    		}
    		String exactNameString = spammerToReport.concat("<col=ffff00>|");
    		if(Widgets.get(553, 5).getText().equals(exactNameString))
    		{
    			if(Widgets.get(553, 14 , 18).interact("Send report"))
    			{
    				Sleepz.sleep(111,1111);
    			}
    			return Sleepz.sleepTiming();
    		}
    		Keyboard.typeSpecialKey(8);
			return Sleepz.calculate(69,420);
    	}
    	if(Bank.isOpen())
    	{
    		Bankz.close();
    		return Sleepz.sleepTiming();
    	}
    	if(GrandExchange.isOpen())
    	{
    		GrandExchangg.close();
    		return Sleepz.sleepTiming();
    	}
    	Player spammer = Players.closest(p -> p!=null && p.getName().contains(spammerToReport));
    	if(spammer != null && spammer.distance() <= 8)
    	{
    		if(Inventory.isItemSelected()) Inventory.deselect();
    		if(Magic.isSpellSelected())Magic.deselect();
    		if(spammer.interact("Report"))
    		{
    			Sleep.sleepUntil(() -> Widgets.get(553, 5) != null && 
    			Widgets.get(553, 5).isVisible(), Sleepz.calculate(2222,2222));
    		}
    		return Sleepz.sleepTiming();
    	}
    	if(Widgets.get(162, 31).interact("Report abuse"))
    	{
    		Sleep.sleepUntil(() -> spammerToReport == null,Sleepz.calculate(2222,2222));
    		Sleepz.sleep(111,666);
    	}
    		
    	return Sleepz.sleepTiming();
    }
}
