package script.actionz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;

import script.p;
import script.actionz.UniqueActions.Actionz;
import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.utilities.API;
import script.utilities.ItemsOnGround;
import script.utilities.Sleep;


public class RuneliteChatPluginPretending extends Leaf {
	private final Area GE = new Area(3159, 3495, 3170, 3484, 0);
	 @Override
	 public boolean isValid() 
	 {
	    return UniqueActions.isActionEnabled(Actionz.PRETEND_TO_HAVE_RUNELITE_CHATCOMMAND_PLUGIN_AT_GE) &&
	    		GE.contains(p.l) &&
	    		(calloutTimer == null || calloutTimer.finished());
	 }
   public static Timer calloutTimer = null;
    @Override
    public int onLoop() {
    	if(API.clearChatWithBackspace())
		{
			if(!ItemsOnGround.allSlayerLoot.isEmpty())
			{
				Collections.shuffle(ItemsOnGround.allSlayerLoot);
				String sayTxt = "!Price " + new Item(ItemsOnGround.allSlayerLoot.get(0),1).getName();
				Keyboard.type(sayTxt,true);
				int timer = (int) Calculations.nextGaussianRandom(6000000, 3000000) + Calculations.random(1000,1200000);
				MethodProvider.log("Setting timer until next Runelite plugin chat thing: " + Timer.formatTime(timer));
				calloutTimer = new Timer(timer);
				return Sleep.calculate(1111,1111);
			}
		}
    	return Timing.sleepLogNormalSleep();
    }
}
