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
	    		GE.contains(Players.localPlayer()) &&
	    		(calloutTimer == null || calloutTimer.finished());
	 }
   public static Timer calloutTimer = null;
    @Override
    public int onLoop() {
    	if(API.clearChatWithBackspace())
		{
			String sayTxt = "";
			if(Calculations.nextGaussianRandom(50,20) >= 50)
			{
				List<Skill> skills = new ArrayList<Skill>();
				for(Skill skill : Skill.values())
				{
					if(Skills.getRealLevel(skill) > 1)
					{
						skills.add(skill);
					}
				}
				if(!skills.isEmpty())
				{
					Collections.shuffle(skills);
					sayTxt = "!lvl "+ skills.get(0).toString();
					Keyboard.type(sayTxt,true);
					int timer = (int) Calculations.nextGaussianRandom(6000000, 3000000) + Calculations.random(1000,1200000);
		    		MethodProvider.log("Setting timer until next Runelite plugin chat thing: " + Timer.formatTime(timer));
					calloutTimer = new Timer(timer);
					return Sleep.calculate(1111,1111);
				}
			}
			if(!ItemsOnGround.allSlayerLoot.isEmpty())
			{
				Collections.shuffle(ItemsOnGround.allSlayerLoot);
				sayTxt = "!Price "+ new Item(ItemsOnGround.allSlayerLoot.get(0),1).getName();
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
