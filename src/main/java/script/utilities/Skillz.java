package script.utilities;

import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.input.event.impl.mouse.impl.click.ClickMode;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.p;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;

public class Skillz {
	public static boolean hoverSkill(Skill skill)
	{
		if(!Tabs.isOpen(Tab.SKILLS))
		{
			if(UniqueActions.isActionEnabled(Actionz.USE_FKEYS)) return Tabs.openWithFKey(Tab.SKILLS);
			return Tabs.openWithMouse(Tab.SKILLS);
		}
		return Skills.hoverSkill(skill);
	}
	public static boolean checkedSkill = false;
	public static Timer checkSkillTimer = null;
	public static boolean shouldCheckSkillInterface()
	{
		if(checkSkillTimer == null)
		{
			checkSkillTimer = new Timer(Calculations.random(60000, 86400000)); //set timer between 1 minute and 24 hours to check again
			return true;
		}
		if(checkSkillTimer.finished() && !checkedSkill) return true;
		return false;
	}
	public static void checkSkillProgress(Skill skill)
	{
		if(Calculations.random(1, 100) <= 50)
		{
			MethodProvider.log("Skipping skill check function! (50% chance to skip)");
			checkSkillTimer = new Timer(Calculations.random(60000, 86400000)); //set timer between 1 minute and 24 hours to check again
			checkedSkill = false;
			return;
		}
		MethodProvider.log("Entering function to check Skill upgrades interface of current skill: " + skill.getName());
		Timer timeout = new Timer(Sleep.calculate(18000, 5555));
		while(!timeout.finished() && 
				Client.isLoggedIn() && 
				!p.l.isInCombat() && 
				Skills.getRealLevel(Skill.HITPOINTS) > 0 && 
				!ScriptManager.getScriptManager().isPaused() &&
				ScriptManager.getScriptManager().isRunning())
			
		{
			Sleep.sleep(111,1111);
			if(isSkillInterfaceOpen())
			{
				API.randomLongerAFK(50);
				checkedSkill = true;
				if(UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE)) 
				{
					if(Keyboard.closeInterfaceWithESC()) Sleep.sleep(69,699);
					continue;
				}
				if(Widgets.getWidgetChild(214,25).interact("Close")) Sleep.sleep(69,699);
				continue;
			}
			if(checkedSkill)
			{
				checkSkillTimer = new Timer(Calculations.random(60000, 86400000)); //set timer between 1 minute and 24 hours to check again
				checkedSkill = false;
				MethodProvider.log("Checked skill and closed interface!");
				return;
			}
			if(Bank.isOpen()) 
			{
				Bankz.close();
				continue;
			}
			if(GrandExchange.isOpen()) 
			{
				GrandExchangg.close();
				continue;
			}
			if(!hoverSkill(skill))
			{
				Tabz.open(Tab.SKILLS);
				continue;
			}
			if(Mouse.click(ClickMode.LEFT_CLICK))Sleep.sleep(1111, 1111);
		}
		
	}
	public static boolean isSkillInterfaceOpen()
	{
		WidgetChild close1 = Widgets.getWidgetChild(214, 25);
		if((close1 != null && close1.isVisible())) return true;
		return false;
	}
}
