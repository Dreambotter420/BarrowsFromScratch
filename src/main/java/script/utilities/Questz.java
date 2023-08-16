package script.utilities;

import java.awt.Point;
import java.awt.Rectangle;

import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.input.Keyboard;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Questz {
	
	
	public static boolean checkCloseQuestCompletion()
	{
		if(Widgets.get(153,16) != null && 
    			Widgets.get(153,16).isVisible()) {
    		if (Keyboard.closeInterfaceWithEsc()) {
				Sleepz.sleepTiming();
			}
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
			Logger.log("Skipping quest check function! (50% chance to skip)");
			checkQuestTimer = new Timer(Calculations.random(60000, 86400000)); //set timer between 1 minute and 24 hours to check again
			checkedQuest = false;
			return;
		}
		Logger.log("Entering function to check quest status of current quest: " + questName);
		Timer timeout = new Timer(Sleepz.calculate(24000, 11111));
		while(!timeout.finished() && 
				Client.isLoggedIn() && 
				!Players.getLocal().isInCombat() &&
				Skills.getRealLevel(Skill.HITPOINTS) > 0 && 
				!ScriptManager.getScriptManager().isPaused() &&
				ScriptManager.getScriptManager().isRunning())
			
		{
			Sleepz.sleep(111,1111);
			if(areQuestStepsOpen())
			{
				checkedQuest = true;
				if(Keyboard.closeInterfaceWithEsc()) Sleepz.sleepTiming();
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
				if(Widgets.get(629, 8) != null &&
						Widgets.get(629,8).isVisible())
				{
					if(Widgets.get(629,8).interact("Quest List")) Sleepz.sleep(666,696);
				}
				continue;
			}
			//worlds are now loaded
			if(Widgets.get(399, 1) != null &&
					Widgets.get(399, 1).isVisible() &&
					Widgets.get(399, 1).getText().contains("Quest List"))
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
					Logger.log("Quest tab open, desired quest widget null: "+questName);
					Sleepz.sleep(100, 111);
					continue;
				}
				Rectangle questRectangle = questWidget.getRectangle();
				WidgetChild questListContainer = Widgets.get(399,7);
				WidgetChild questListVisibleRectangle = Widgets.get(399,6);
				if(questListContainer == null || questListVisibleRectangle == null)
				{
					Logger.log("Quest tab open, quest list container widgets null");
				
					Sleepz.sleep(100, 111);
					continue;
				}
				Rectangle visibleQuestListRectangle1 = questListVisibleRectangle.getRectangle();
				if(questRectangle.intersects(visibleQuestListRectangle1))
				{
					//World widget is visible - clicking it
					Rectangle visibleWorldRectangle = questRectangle.intersection(visibleQuestListRectangle1);
					if(Mouse.click(visibleWorldRectangle))Sleepz.sleep(111, 1111);
					continue;
				}
				//World list needs scrolling
				double yPos = questRectangle.getCenterY();
				double yMin = questListContainer.getRectangle().getMinY();
				double yMax = questListContainer.getRectangle().getMaxY();
				double offsetRatio = ((yPos - yMin) / (yMax - yMin));
				WidgetChild scrollContainer = Widgets.get(399, 5 , 0);
				if(scrollContainer == null)
				{
					Sleepz.sleep(100, 111);
					continue;
				}
				double yScrollMin = scrollContainer.getRectangle().getMinY();
				int xRand = (int) Calculations.random(scrollContainer.getRectangle().getMinX(), scrollContainer.getRectangle().getMaxX());
				int yClickPos = (int) ((scrollContainer.getHeight() * offsetRatio) + yScrollMin);
				if(Mouse.click(new Point(xRand,yClickPos)))	Sleepz.sleep(111, 1111);
				continue;
			}
		}
	}
	public static boolean areQuestStepsOpen()
	{
		WidgetChild close1 = Widgets.get(119,205);
		if((close1 != null && close1.isVisible())) return true;
		return false;
	}
}
