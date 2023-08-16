package script.behaviour;

import java.awt.Point;
import java.awt.Rectangle;

import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.framework.Leaf;
import script.utilities.*;


public class CustomizeSettings extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return ClientSettings.isAcceptAidEnabled() ||
				shouldToggleProfanity() ||
    			!ClientSettings.areRoofsHidden() ||
    			shouldToggleWarningsOff() ||
				ClientSettings.isResizableActive() ||
    			ClientSettings.getNPCAttackOptionsMode() != ActionMode.LEFT_CLICK_WHERE_AVAILABLE ||
    			ClientSettings.getPlayerAttackOptionsMode() != ActionMode.HIDDEN ||
    			!ClientSettings.isEscInterfaceClosingEnabled() ||
    			!ClientSettings.isShiftClickDroppingEnabled() || 
    			ClientSettings.isGameAudioOn() || 
    			ClientSettings.isResizableActive() || 
    			ClientSettings.isTradeDelayEnabled();
		
	 }
    @Override
    public int onLoop() {
    	return customizeSettings();
    }
    
	public static int customizeSettings()
	{
		Sleepz.sleepTiming();
		if(Dialogues.canContinue()) {
			Dialogues.continueDialogue();
			Sleep.sleepTick();
			return Sleepz.sleepTiming();
		}
		if(Dialogues.areOptionsAvailable()) {
			Walking.clickTileOnMinimap(Players.getLocal().getTile());
			Sleep.sleepTick();
			return Sleepz.sleepTiming();
		}
		if (!GrandExchangg.close() || !Bankz.close() || !Shopz.close()) {
			Logger.log("Closing GE / Bank / Shop");
			Widgets.closeAll();
			Sleep.sleepTick();
			return Sleepz.sleepTiming();
		}
		if(ClientSettings.isAcceptAidEnabled()) {
			Logger.log("Toggling accept aid");
			ClientSettings.toggleAcceptAid(false);
		}
		else if(shouldToggleProfanity()) {
			Logger.log("Toggling profanity");
			toggleProfanity();
		}
		else if(!ClientSettings.areRoofsHidden()) {
			Logger.log("Toggling roofs");
			ClientSettings.toggleRoofs(false);
		}
		else if(shouldToggleWarningsOff()) {
			Logger.log("Toggling warnings");
			toggleWarnings();
		}
		else if(ClientSettings.isResizableActive()) {
			Logger.log("Toggling resizable");
			ClientSettings.toggleResizable(false);
		}
		else if(ClientSettings.getNPCAttackOptionsMode() != ActionMode.LEFT_CLICK_WHERE_AVAILABLE) {
			Logger.log("Setting NPC attack options mode to LEFT_CLICK_WHERE_AVAILABLE");
			setNPCAttackOptionsMode(ActionMode.LEFT_CLICK_WHERE_AVAILABLE);
		}
		else if(ClientSettings.getPlayerAttackOptionsMode() != ActionMode.HIDDEN) {
			Logger.log("Setting player attack options mode to HIDDEN");
			setPlayerAttackOptionsMode(ActionMode.HIDDEN);
		}
		else if(!ClientSettings.isEscInterfaceClosingEnabled()) {
			Logger.log("Toggling ESC interface closing");
			toggleEscInterfaceClosing(true);
		}
		else if(!ClientSettings.isShiftClickDroppingEnabled()) {
			Logger.log("Toggling shift click dropping");
			ClientSettings.toggleShiftClickDropping(true);
		}
		else if(ClientSettings.isGameAudioOn()) {
			Logger.log("Toggling game audio");
			ClientSettings.toggleGameAudio(false);
		}
		else if(ClientSettings.isResizableActive()) {
			Logger.log("Toggling resizable");
			ClientSettings.toggleResizable(false);
		}
		else if(ClientSettings.isTradeDelayEnabled()) {
			Logger.log("Toggling trade delay");
			ClientSettings.toggleTradeDelay(false);
		}
		else 
		{
			Logger.log("Testttt@22");
			return Sleepz.interactionTiming();
		}
		Sleep.sleepTick();
		return Sleepz.sleepTiming();
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
	public static boolean shouldToggleCharterShipsOff() { return PlayerSettings.getBitValue(charterShipsConfirmationVarbit) == 1;}
	public static boolean shouldToggleFeroxOff() 
	{
		return PlayerSettings.getBitValue(feroxWarningVarbit) == 0;
	}
	public static boolean shouldToggleGEBuyOff()
	{
		return PlayerSettings.getBitValue(GEBuyWarningVarbit) == 0;
	}
	public static boolean shouldToggleGESellOff()
	{
		return PlayerSettings.getBitValue(GESellWarningVarbit) == 0;
	}
	public static boolean shouldToggleWarningsOff() {
		return shouldToggleFeroxOff() ||
				shouldToggleTabWarningsOff() ||
				shouldToggleGESellOff() ||
				shouldToggleGEBuyOff() ||
				shouldToggleTeleWarningsOff();
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
    
    //0 -> off, 1 -> on
    public static final int charterShipsConfirmationVarbit = 75;
    
    //1 -> off, 0 -> on
    public static final int feroxWarningVarbit = 10532;
	public static final int GEBuyWarningVarbit = 14700;
	public static final int GESellWarningVarbit = 14701;
    public static void scrollWarningsButton(WidgetChild button) {

		Logger.log("Scroll Warnings Button");
		if(button == null || !button.isVisible()) {
			Logger.log("Called scrollWarningsButton but button passed not visible");
			Sleepz.sleep(111, 1111);
			return;
		}
		Rectangle worldRectangle = button.getRectangle();
		WidgetChild worldListContainer = Widgets.get(134, 18);
		Rectangle worldListRectangle = Widgets.get(134, 14).getRectangle();
		if(worldListContainer == null) {
			Logger.log("scrollWarningsButton - error1");
			Sleepz.sleep(111, 1111);
			return;
		}
		if(worldRectangle.intersects(worldListRectangle)) {
			//World widget is visible - clicking it
			Rectangle visibleWorldRectangle = worldRectangle.intersection(worldListRectangle);
			Mouse.click(visibleWorldRectangle);
			Sleep.sleepTick();
			Sleepz.sleep(111, 1111);
			return;
		}
		//World list needs scrolling
		double yPos = worldRectangle.getCenterY();
		double yMin = worldListContainer.getRectangle().getMinY();
		double yMax = worldListContainer.getRectangle().getMaxY();
		double offsetRatio = ((yPos - yMin) / (yMax - yMin));
		WidgetChild scrollContainer = Widgets.get(134, 20 , 0);
		if(scrollContainer == null) {
			Logger.log("scrollWarningsButton - error21");
			Sleepz.sleepInteraction();
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
		Sleepz.sleepInteraction();
	}
    public static WidgetChild teleWarningsButton()
    {
    	return Widgets.get(134, 18, 34);
    }
    public static WidgetChild tabWarningsButton()
    {
    	return Widgets.get(134, 18 , 79);
    }
    public static WidgetChild charterConfirmationButton() {
    	return getUpOneWidget(Widgets.get(w -> w.getParentID() == 134 && w.getText().equals("Charter Ships confirmation")));
    }
    public static WidgetChild feroxWarningsButton() {
		return getUpOneWidget(Widgets.get(w -> w.getParentID() == 134 && w.getText().equals("Show warning on exiting Ferox Enclave")));
    }
	public static WidgetChild GEBuyWarningButton() {
		return getUpOneWidget(Widgets.get(w -> w.getParentID() == 134 && w.getText().equals("Show warning when a buy offer price is too high in the Grand Exchange")));
	}
	public static WidgetChild GESellWarningButton() {
		return getUpOneWidget(Widgets.get(w -> w.getParentID() == 134 && w.getText().equals("Show warning when a sell offer price is too low in the Grand Exchange")));
	}
	private static WidgetChild getUpOneWidget(WidgetChild c) {
		return Widgets.get(c.getParentID(), c.getID(), c.getIndex() - 1);
	}
    public static void toggleWarnings()
	{
		//exit button for main Settings menu visible
    	if(openSettingsMenu()) {
    		if(openSettingsSubTab("Warnings")) {
    			if(shouldToggleTeleWarningsOff()) scrollWarningsButton(teleWarningsButton());
    			else if(shouldToggleTabWarningsOff()) scrollWarningsButton(tabWarningsButton());
				else if(shouldToggleCharterShipsOff()) scrollWarningsButton(charterConfirmationButton());
				else if(shouldToggleGEBuyOff()) scrollWarningsButton(GEBuyWarningButton());
				else if(shouldToggleGESellOff()) scrollWarningsButton(GESellWarningButton());
    			else if(shouldToggleFeroxOff()) scrollWarningsButton(feroxWarningsButton());
    		}
    	}
    	Sleep.sleepTick();
	}
	// 0 -> on, 1 -> off
    public static final int profanityConfig = 1074;
    public static boolean shouldToggleProfanity() {
    	return isProfanityEnabled();
    }
    public static boolean isProfanityEnabled()
    {
    	return PlayerSettings.getConfig(profanityConfig) == 0;
    }
	public static void toggleProfanity()
	{
		//exit button for main Settings menu visible
    	if(openSettingsMenu()) {
    		//chat tab of settings window is NOT selected ("select chat" action exists)
    		if(Widgets.get(134, 23, 2) != null &&
    				Widgets.get(134, 23, 2).isVisible()) {
    			if(Widgets.get(134, 23, 2).interact("Select Chat")) {
    				Sleep.sleepUntil(() -> Widgets.get(134, 23, 2) == null || 
        					!Widgets.get(134, 23, 2).isVisible(), Sleepz.calculate(2222, 2222));
					Sleepz.sleepInteraction();
    			}
    		}
    		//Enable profanity button toggle is visible
    		if(Widgets.get(134, 19, 1) != null && Widgets.get(134, 19, 1).isVisible()) {
    			if(Widgets.get(134, 19, 1).interact("Toggle")) {
    				Sleep.sleepUntil(() -> PlayerSettings.getConfig(1074) == 1, Sleepz.calculate(2222, 2222));
    			}
            }
    	}
	}
	
	public static boolean openSettingsSubTab(String subtabName) {
		Logger.log("Open Settings SubTab name: " + subtabName);
		Filter<WidgetChild> tabFilter = w -> w!=null && w.isVisible() && w.getID() == 23 && 
				w.getParentID() == 134 && (w.getIndex() <= 7 && w.getIndex() >= 0);
		boolean foundTab = false;
		int tabGC = 0;
		for(WidgetChild tab : Widgets.getAll(tabFilter))
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
		if(Widgets.get(134,4) == null || !Widgets.get(134,4).isVisible()) return false;
		Filter<WidgetChild> chosenTabFilter = w -> 
				w!=null && 
				w.isVisible() && 
				w.getID() == 23 && 
				w.getParentID() == 134 && 
				w.getIndex() == tabGCFinal;
		WidgetChild chosenTab = Widgets.get(chosenTabFilter);
		if(chosenTab != null && chosenTab.isVisible()) {
			if(chosenTab.interact()) {
				Sleep.sleepUntil(() -> chosenTab == null || !chosenTab.isVisible(),Sleepz.calculate(2222, 2222)); 
			}
		}
		return false;
	}
	public static boolean isSettingsMainMenuOpen() {
		WidgetChild mainSettingsExitButton = Widgets.get(134,4);
		return mainSettingsExitButton != null && mainSettingsExitButton.isVisible();
	}
	public static boolean openSettingsMenu() {
		Logger.log("Open Settings Menu");
		//exit button for main Settings menu visible
		if (isSettingsMainMenuOpen()) {

		}
    	//"All settings" button visible in Settings tab
		WidgetChild allSettingsButton = Widgets.get(w -> w.getParentID() == 116 &&
				w.isVisible() &&
				(w.hasAction("All Settings") || w.getText().equals("All Settings")));
		if(allSettingsButton != null && allSettingsButton.interact()) {
			Sleep.sleepUntil(() -> {
				WidgetChild mainSettingsExitButton2 = Widgets.get(134,4);
				return mainSettingsExitButton2 != null && mainSettingsExitButton2.isVisible();
			}, Sleepz.calculate(2222, 2222));
        } else if (!Tabs.isOpen(Tab.OPTIONS) && Tabz.open(Tab.OPTIONS)) {
			Sleep.sleepUntil(() -> Tabs.isOpen(Tab.OPTIONS), Sleepz.calculate(2222, 2222));
		}
		return false;
	}
	public static void disableWarnings() {
    	if(openSettingsMenu()) {
    		//chat tab of settings window is NOT selected ("select chat" action exists)
    		if(Widgets.get(134, 23 , 7) != null &&
    				Widgets.get(134, 23 , 7).isVisible()) {
    			if(Widgets.get(134, 23 , 7).interact("Select Warnings")) {
    				Sleep.sleepUntil(() -> Widgets.get(134, 23, 7) == null || 
        					!Widgets.get(134, 23, 7).isVisible(), Sleepz.calculate(2222, 2222));
    			}
    		}
    		// button toggle is visible
    		if(Widgets.get(134, 19, 1) != null && Widgets.get(134, 19, 1).isVisible())
        	{
    			final int configValPrevious = PlayerSettings.getConfig(1074);
    			if(Widgets.get(134, 19, 1).interact("Toggle")) {
    				Sleep.sleepUntil(() -> PlayerSettings.getConfig(1074) != configValPrevious, Sleepz.calculate(2222, 2222));
    			}
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
				PlayerSettings.getBitValue(feroxWarningVarbit) == 0;
	}
}
