package script.behaviour;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.p;
import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;
import script.framework.Leaf;
import script.utilities.Sleep;
import script.utilities.Tabz;


public class CustomizeSettings extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return ClientSettings.isAcceptAidEnabled() ||
				shouldToggleProfanity() ||
    			ClientSettings.roofsEnabled() || 
    			shouldToggleWarningsOff() ||
    			(UniqueActions.isActionEnabled(Actionz.LEFTMOUSECLICK_ATTACK_NPC) && ClientSettings.getNPCAttackOptionsMode() != ActionMode.LEFT_CLICK_WHERE_AVAILABLE) ||
    			(!UniqueActions.isActionEnabled(Actionz.LEFTMOUSECLICK_ATTACK_NPC) && ClientSettings.getNPCAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK) ||
    			(UniqueActions.isActionEnabled(Actionz.HIDE_PLAYER_ATTACK) && ClientSettings.getPlayerAttackOptionsMode() != ActionMode.HIDDEN) ||
    			(!UniqueActions.isActionEnabled(Actionz.HIDE_PLAYER_ATTACK) && ClientSettings.getPlayerAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK) ||
    			(UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE) && !ClientSettings.isEscInterfaceClosingEnabled()) || 
    			!ClientSettings.isShiftClickDroppingEnabled() || 
    			ClientSettings.isGameAudioOn() || 
    			ClientSettings.isResizableActive() || 
    			(UniqueActions.isActionEnabled(Actionz.TRADE_ACCEPT_DELAY_OFF) && ClientSettings.isTradeDelayEnabled());
		
	 }
    @Override
    public int onLoop() {
    	return customizeSettings();
    }
    
	public static int customizeSettings()
	{
		if(Dialogues.canContinue())
		{
			Dialogues.continueDialogue();
			return Sleep.calculate(420, 696);
		}
		if(Dialogues.areOptionsAvailable())
		{
			Walking.clickTileOnMinimap(p.l.getTile());
			return Sleep.calculate(420, 696);
		}
		if(ClientSettings.isAcceptAidEnabled()) ClientSettings.toggleAcceptAid(false);
		else if(shouldToggleProfanity()) toggleProfanity();
		else if(ClientSettings.roofsEnabled()) ClientSettings.toggleRoofs(false);
		else if(shouldToggleWarningsOff()) toggleWarnings();
		else if(UniqueActions.isActionEnabled(Actionz.LEFTMOUSECLICK_ATTACK_NPC) && ClientSettings.getNPCAttackOptionsMode() != ActionMode.LEFT_CLICK_WHERE_AVAILABLE)
			setNPCAttackOptionsMode(ActionMode.LEFT_CLICK_WHERE_AVAILABLE);
		else if(!UniqueActions.isActionEnabled(Actionz.LEFTMOUSECLICK_ATTACK_NPC) && ClientSettings.getNPCAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK)
			setNPCAttackOptionsMode(ActionMode.ALWAYS_RIGHT_CLICK);
		else if(UniqueActions.isActionEnabled(Actionz.HIDE_PLAYER_ATTACK) && ClientSettings.getPlayerAttackOptionsMode() != ActionMode.HIDDEN)
			setPlayerAttackOptionsMode(ActionMode.HIDDEN);
		else if(!UniqueActions.isActionEnabled(Actionz.HIDE_PLAYER_ATTACK) && ClientSettings.getPlayerAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK)
			setPlayerAttackOptionsMode(ActionMode.ALWAYS_RIGHT_CLICK);
		else if((UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE) && !ClientSettings.isEscInterfaceClosingEnabled()))
			toggleEscInterfaceClosing(true);
		else if(!ClientSettings.isShiftClickDroppingEnabled()) ClientSettings.toggleShiftClickDropping(true);
		else if(ClientSettings.isGameAudioOn()) ClientSettings.toggleGameAudio(false);
		else if(ClientSettings.isResizableActive()) ClientSettings.toggleResizable(false);
		else if(UniqueActions.isActionEnabled(Actionz.TRADE_ACCEPT_DELAY_OFF) && ClientSettings.isTradeDelayEnabled())
			ClientSettings.toggleTradeDelay(false);
		else 
		{
			MethodProvider.log("Testttt@22");
			return Sleep.calculate(111, 111);
		}
			
		return Sleep.calculate(420, 696);
	}
	public static boolean toggleEscInterfaceClosing(boolean trueFalse)
	{
		if(!Tabs.isOpen(Tab.OPTIONS)) Tabz.open(Tab.OPTIONS);
		else return ClientSettings.toggleEscInterfaceClosing(trueFalse);
		return false;
	}
	public static boolean setNPCAttackOptionsMode(ActionMode mode)
	{
		if(!Tabs.isOpen(Tab.OPTIONS)) Tabz.open(Tab.OPTIONS);
		else return ClientSettings.setNPCAttackOptionsMode(mode);
		return false;
	}
	public static boolean setPlayerAttackOptionsMode(ActionMode mode)
	{
		if(!Tabs.isOpen(Tab.OPTIONS)) Tabz.open(Tab.OPTIONS);
		else return ClientSettings.setPlayerAttackOptionsMode(mode);
		return false;
	}
	public static boolean shouldToggleTeleWarningsOff()
	{
		return PlayerSettings.getBitValue(teleWarningVarbit1) == 0 || 
				PlayerSettings.getBitValue(teleWarningVarbit2) == 0 || 
				PlayerSettings.getBitValue(teleWarningVarbit3) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit4) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit5) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit6) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit7) == 1;
	}
	public static boolean shouldToggleTabWarningsOff()
	{
		return PlayerSettings.getBitValue(tabWarningVarbit1) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit2) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit3) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit4) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit5) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit6) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit7) == 0;
	}
	public static boolean shouldToggleFeroxOff() 
	{
		return PlayerSettings.getBitValue(feroxWarningVarbit) == 0;
	}
	public static boolean shouldToggleWildyCanoeOff()
	{
		return PlayerSettings.getBitValue(wildyCanoeWarningVarbit) == 0;
	}
	public static boolean shouldToggleWarningsOff()
	{
		return UniqueActions.isActionEnabled(Actionz.DISABLE_ALL_WARNINGS) && 
				(shouldToggleWildyCanoeOff() || 
						shouldToggleFeroxOff() || 
						shouldToggleTabWarningsOff() ||
						shouldToggleTeleWarningsOff());
	}
	//1 -> off, 0 -> on
    public static final int teleWarningVarbit1 = 13986;
    public static final int teleWarningVarbit2 = 13985;
    //0 -> off, 1 -> on
    public static final int teleWarningVarbit3 = 6287;
    public static final int teleWarningVarbit4 = 6286;
    public static final int teleWarningVarbit5 = 6285;
    public static final int teleWarningVarbit6 = 6284;
    public static final int teleWarningVarbit7 = 236;
    
    //1 -> off, 0 -> on
    public static final int tabWarningVarbit1 = 3932;
    public static final int tabWarningVarbit2 = 3931;
    public static final int tabWarningVarbit3 = 3930;
    public static final int tabWarningVarbit4 = 2325;
    public static final int tabWarningVarbit5 = 2324;
    public static final int tabWarningVarbit6 = 2323;
    public static final int tabWarningVarbit7 = 2322;
    //1 -> off, 0 -> on
    public static final int feroxWarningVarbit = 10532;
    public static final int wildyCanoeWarningVarbit = 13988;
    public static void scrollWarningsButton(WidgetChild button)
	{
		if(button == null || !button.isVisible())
		{
			Sleep.sleep(111, 1111);
			return;
		}
		Rectangle worldRectangle = button.getRectangle();
		WidgetChild worldListContainer = Widgets.getWidgetChild(134, 18);
		Rectangle worldListRectangle = Widgets.getWidgetChild(134, 14).getRectangle();
		if(worldListContainer == null)
		{
			Sleep.sleep(111, 1111);
			return;
		}
		if(worldRectangle.intersects(worldListRectangle))
		{
			//World widget is visible - clicking it
			Rectangle visibleWorldRectangle = worldRectangle.intersection(worldListRectangle);
			Mouse.click(visibleWorldRectangle);
			Sleep.sleep(111, 1111);
			return;
		}
		//World list needs scrolling
		double yPos = worldRectangle.getCenterY();
		double yMin = worldListContainer.getRectangle().getMinY();
		double yMax = worldListContainer.getRectangle().getMaxY();
		double offsetRatio = ((yPos - yMin) / (yMax - yMin));
		WidgetChild scrollContainer = Widgets.getWidgetChild(134, 20 , 0);
		if(scrollContainer == null)
		{
			Sleep.sleep(100, 111);
			return;
		}
		double scrollMinY = scrollContainer.getRectangle().getMinY();
		int xRand = (int) Calculations.random(scrollContainer.getRectangle().getMinX(), scrollContainer.getRectangle().getMaxX());
		int yClickPos = (int) ((scrollContainer.getHeight() * offsetRatio) + scrollMinY);
		if(yPos >= yMax) 
		{
			yClickPos =+ (int) Calculations.random(5, 15);
			if(yClickPos > scrollContainer.getRectangle().getMaxY()) yClickPos = (int) scrollContainer.getRectangle().getMaxY() - 1;
		}
		else if(yPos <= yMin) 
		{
			yClickPos =- (int) Calculations.random(5, 15);
			if(yClickPos < scrollContainer.getRectangle().getMinY()) yClickPos = (int) scrollContainer.getRectangle().getMinY() + 1;
		}
		Mouse.click(new Point(xRand,yClickPos));
		Sleep.sleep(111, 1111);
		return;
	}
    public static WidgetChild teleWarningsButton()
    {
    	return Widgets.getWidgetChild(134, 18, 34);
    }
    public static WidgetChild tabWarningsButton()
    {
    	return Widgets.getWidgetChild(134, 18 , 79);
    }
    public static WidgetChild feroxWarningsButton()
    {
    	return Widgets.getWidgetChild(134, 18 , 117);
    }
    public static WidgetChild wildyCanoeWarningsButton()
    {
    	return Widgets.getWidgetChild(134, 18 , 120);
    }
    public static void toggleWarnings()
	{
		//exit button for main Settings menu visible
    	if(openSettingsMenu())
    	{
    		if(openSettingsSubTab("Warnings"))
    		{
    			if(shouldToggleTeleWarningsOff()) scrollWarningsButton(teleWarningsButton());
    			else if(shouldToggleTabWarningsOff()) scrollWarningsButton(tabWarningsButton());
    			else if(shouldToggleFeroxOff()) scrollWarningsButton(feroxWarningsButton());
    			else if(shouldToggleWildyCanoeOff()) scrollWarningsButton(wildyCanoeWarningsButton());
    		}
    	}
    	Sleep.sleep(410, 696);
	}
	// 0 -> on, 1 -> off
    public static final int profanityConfig = 1074;
    public static boolean shouldToggleProfanity()
    {
    	if(UniqueActions.isActionEnabled(Actionz.PROFANITY_FILTER_OFF))
    	{
    		if(isProfanityEnabled()) return true;
    	}
    	return false;
    }
    public static boolean isProfanityEnabled()
    {
    	return PlayerSettings.getConfig(profanityConfig) == 0;
    }
	public static void toggleProfanity()
	{
		//exit button for main Settings menu visible
    	if(openSettingsMenu())
    	{
    		//chat tab of settings window is NOT selected ("select chat" action exists)
    		if(Widgets.getWidgetChild(134, 23, 2) != null &&
    				Widgets.getWidgetChild(134, 23, 2).isVisible())
    		{
    			if(Widgets.getWidgetChild(134, 23, 2).interact("Select Chat"))
    			{
    				MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(134, 23, 2) == null || 
        					!Widgets.getWidgetChild(134, 23, 2).isVisible(), Sleep.calculate(2222, 2222));
    			}
        		Sleep.sleep(333,444);
    		}
    		//Enable profanity button toggle is visible
    		if(Widgets.getWidgetChild(134, 19, 1) != null && Widgets.getWidgetChild(134, 19, 1).isVisible())
        	{
    			if(Widgets.getWidgetChild(134, 19, 1).interact("Toggle"))
    			{
    				MethodProvider.sleepUntil(() -> PlayerSettings.getConfig(1074) == 1, Sleep.calculate(2222, 2222));
    			}
    			Sleep.sleep(420,420);
            }
    	}
	}
	
	public static boolean openSettingsSubTab(String subtabName)
	{
		Filter<WidgetChild> tabFilter = w -> w!=null && w.isVisible() && w.getID() == 23 && 
				w.getParentID() == 134 && (w.getIndex() <= 7 && w.getIndex() >= 0);
		boolean foundTab = false;
		int tabGC = 0;
		for(WidgetChild tab : Widgets.getWidgets(tabFilter))
		{
			if(tab == null || !tab.isVisible()) continue;
			String[] actions = tab.getActions();
			if(actions == null || actions.length <= 0) continue;
			for(String action : actions)
			{
				if(action == null || action.isEmpty() || action.equals("null")) continue;
				if(action.contains(subtabName)) 
				{
					foundTab = true;
					tabGC = tab.getIndex();
				}
			}
		}
		final int tabGCFinal = tabGC;
		if(!foundTab) return true;
		if(Widgets.getWidgetChild(134,4) == null || !Widgets.getWidgetChild(134,4).isVisible()) return false;
		Filter<WidgetChild> chosenTabFilter = w -> 
				w!=null && 
				w.isVisible() && 
				w.getID() == 23 && 
				w.getParentID() == 134 && 
				w.getIndex() == tabGCFinal;
		WidgetChild chosenTab = Widgets.getMatchingWidget(chosenTabFilter);
		if(chosenTab != null && chosenTab.isVisible())
		{
			if(chosenTab.interact())
			{
				MethodProvider.sleepUntil(() -> chosenTab == null || !chosenTab.isVisible(),Sleep.calculate(2222, 2222)); 
			}
		}
		return false;
	}
	public static boolean openSettingsMenu()
	{
		//exit button for main Settings menu visible
    	if(Widgets.getWidgetChild(134,4) != null && Widgets.getWidgetChild(134,4).isVisible()) return true;
    	//"All settings" button visible in Settings tab
		if(Widgets.getWidgetChild(116,75) != null && Widgets.getWidgetChild(116,75).isVisible())
    	{
			if(Widgets.getWidgetChild(116,75).interact("All Settings"))
			{
				MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(134,4) != null && Widgets.getWidgetChild(134,4).isVisible(), Sleep.calculate(2222, 2222));
			}
    		Sleep.sleep(333,444);
        } 
		else if(!Tabs.isOpen(Tab.OPTIONS) && Tabz.open(Tab.OPTIONS))
		{
			MethodProvider.sleepUntil(() -> Tabs.isOpen(Tab.OPTIONS), Sleep.calculate(2222, 2222));
		}
		return false;
	}
	public static void disableWarnings()
	{
    	if(openSettingsMenu())
    	{
    		//chat tab of settings window is NOT selected ("select chat" action exists)
    		if(Widgets.getWidgetChild(134, 23 , 7) != null &&
    				Widgets.getWidgetChild(134, 23 , 7).isVisible())
    		{
    			if(Widgets.getWidgetChild(134, 23 , 7).interact("Select Warnings"))
    			{
    				MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(134, 23, 7) == null || 
        					!Widgets.getWidgetChild(134, 23, 7).isVisible(), Sleep.calculate(2222, 2222));
    			}
        		Sleep.sleep(333,444);
    		}
    		// button toggle is visible
    		if(Widgets.getWidgetChild(134, 19, 1) != null && Widgets.getWidgetChild(134, 19, 1).isVisible())
        	{
    			final int configValPrevious = PlayerSettings.getConfig(1074);
    			if(Widgets.getWidgetChild(134, 19, 1).interact("Toggle"))
    			{
    				MethodProvider.sleepUntil(() -> PlayerSettings.getConfig(1074) != configValPrevious, Sleep.calculate(2222, 2222));
    			}
    			Sleep.sleep(420,420);
            }
    	} 
	}
	public static boolean haveWarningsEnabled()
	{
		return PlayerSettings.getBitValue(teleWarningVarbit1) == 0 || 
				PlayerSettings.getBitValue(teleWarningVarbit2) == 0 || 
				PlayerSettings.getBitValue(teleWarningVarbit3) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit4) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit5) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit6) == 1 || 
				PlayerSettings.getBitValue(teleWarningVarbit7) == 1 || 
				PlayerSettings.getBitValue(tabWarningVarbit1) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit2) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit3) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit4) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit5) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit6) == 0 || 
				PlayerSettings.getBitValue(tabWarningVarbit7) == 0 || 
				PlayerSettings.getBitValue(feroxWarningVarbit) == 0 || 
				PlayerSettings.getBitValue(wildyCanoeWarningVarbit) == 0;
	}
}
