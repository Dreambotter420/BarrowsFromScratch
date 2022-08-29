package script.utilities;

import java.awt.Point;
import java.awt.Rectangle;

import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;

public class Questz {
	
	
	public static boolean closeQuestCompletion()
	{
		if(Widgets.getWidgetChild(153,16) != null && 
    			Widgets.getWidgetChild(153,16).isVisible())
    	{
    		if(UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE) && Keyboard.closeInterfaceWithESC()) Sleep.sleep(696,666);
    		else if(Widgets.getWidgetChild(153,16).interact("Close")) Sleep.sleep(696, 666);
    		return true;
    	}
		return false;
	}
	public static boolean checkedQuest = false;
	public static Timer checkQuestTimer = null;
	public static boolean shouldCheckQuestStep()
	{
		if(checkQuestTimer == null)
		{
			checkQuestTimer = new Timer(Calculations.random(60000, 86400000)); //set timer between 1 minute and 24 hours to check again
			if((int) Calculations.nextGaussianRandom(100,50) > 95) return true;
			return false;
		}
		if(checkQuestTimer.finished() && !checkedQuest) return true;
		return false;
	}
	public static void checkQuestStep(String questName)
	{
		if(Calculations.random(1, 100) <= 50)
		{
			MethodProvider.log("Skipping quest check function! (50% chance to skip)");
			checkQuestTimer = new Timer(Calculations.random(60000, 86400000)); //set timer between 1 minute and 24 hours to check again
			checkedQuest = false;
			return;
		}
		MethodProvider.log("Entering function to check quest status of current quest: " + questName);
		Timer timeout = new Timer(Sleep.calculate(24000, 11111));
		while(!timeout.finished() && 
				Client.isLoggedIn() && 
				!Players.localPlayer().isInCombat() && 
				Skills.getRealLevel(Skill.HITPOINTS) > 0 && 
				!ScriptManager.getScriptManager().isPaused() &&
				ScriptManager.getScriptManager().isRunning())
			
		{
			Sleep.sleep(111,1111);
			if(areQuestStepsOpen())
			{
				API.randomLongerAFK(50);
				checkedQuest = true;
				if(UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE)) 
				{
					if(Keyboard.closeInterfaceWithESC()) Sleep.sleep(69,699);
					continue;
				}
				if(Widgets.getWidgetChild(119,205).interact("Close")) Sleep.sleep(69,699);
				continue;
			}
			if(checkedQuest)
			{
				checkQuestTimer = new Timer(Calculations.random(60000, 86400000)); //set timer between 1 minute and 24 hours to check again
				checkedQuest = false;
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
			if(!Tabs.isOpen(Tab.QUEST))
			{
				Tabz.open(Tab.QUEST);
				continue;
			}
			if(PlayerSettings.getBitValue(8168) != 1)
			{
				if(Widgets.getWidgetChild(629, 8) != null &&
						Widgets.getWidgetChild(629,8).isVisible())
				{
					if(Widgets.getWidgetChild(629,8).interact("Quest List")) Sleep.sleep(666,696);
				}
				continue;
			}
			//worlds are now loaded
			if(Widgets.getWidgetChild(399, 1) != null &&
					Widgets.getWidgetChild(399, 1).isVisible() &&
					Widgets.getWidgetChild(399, 1).getText().contains("Quest List"))
			{
				//establish correct WidgetChild of desired quest's clickable bar
				WidgetChild questWidget = null;
				for(WidgetChild w : Widgets.getWidgetChildrenContainingText(questName))
				{
					if(w.hasAction("Read journal:")) // correct action **FIXED MODE**
					{
						questWidget = w;
						break;
					}
				}
				if(questWidget == null) 
				{
					MethodProvider.log("Quest tab open, desired quest widget null: "+questName);
					Sleep.sleep(100, 111);
					continue;
				}
				Rectangle questRectangle = questWidget.getRectangle();
				WidgetChild questListContainer = Widgets.getWidgetChild(399,7);
				WidgetChild questListVisibleRectangle = Widgets.getWidgetChild(399,6);
				if(questListContainer == null || questListVisibleRectangle == null)
				{
					MethodProvider.log("Quest tab open, quest list container widgets null");
				
					Sleep.sleep(100, 111);
					continue;
				}
				Rectangle visibleQuestListRectangle1 = questListVisibleRectangle.getRectangle();
				if(questRectangle.intersects(visibleQuestListRectangle1))
				{
					//World widget is visible - clicking it
					Rectangle visibleWorldRectangle = questRectangle.intersection(visibleQuestListRectangle1);
					if(Mouse.click(visibleWorldRectangle))Sleep.sleep(111, 1111);
					continue;
				}
				//World list needs scrolling
				double yPos = questRectangle.getCenterY();
				double yMin = questListContainer.getRectangle().getMinY();
				double yMax = questListContainer.getRectangle().getMaxY();
				double offsetRatio = ((yPos - yMin) / (yMax - yMin));
				WidgetChild scrollContainer = Widgets.getWidgetChild(399, 5 , 0);
				if(scrollContainer == null)
				{
					Sleep.sleep(100, 111);
					continue;
				}
				double yScrollMin = scrollContainer.getRectangle().getMinY();
				int xRand = (int) Calculations.random(scrollContainer.getRectangle().getMinX(), scrollContainer.getRectangle().getMaxX());
				int yClickPos = (int) ((scrollContainer.getHeight() * offsetRatio) + yScrollMin);
				if(Mouse.click(new Point(xRand,yClickPos)))	Sleep.sleep(111, 1111);
				continue;
			}
		}
	}
	public static boolean areQuestStepsOpen()
	{
		WidgetChild close1 = Widgets.getWidgetChild(119,205);
		if((close1 != null && close1.isVisible())) return true;
		return false;
	}
}
