package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;


public class API {
	
	public static String currentBranch = "";
    public static String currentLeaf = "";
    
    public static enum modes {
		TRAIN_SLAYER,
		TRAIN_WOODCUTTING,
		TRAIN_RANGE,
		TRAIN_MAGIC,
		TRAIN_MELEE,
		TRAIN_CRAFTING,
		TRAIN_PRAYER,
		TRAIN_FIREMAKING,
		TRAIN_AGILITY,
		MAGE_ARENA_1,
		MAGE_ARENA_2,
		RESTLESS_GHOST,
		NATURE_SPIRIT,
		VARROCK_QUIZ,
		PRIEST_IN_PERIL,
		ERNEST_THE_CHIKKEN,
		ANIMAL_MAGNETISM,
		HORROR_FROM_THE_DEEP,
		WATERFALL_QUEST,
		FIGHT_ARENA,
		FREMENNIK_TRIALS,
		X_MARKS_THE_SPOT,
		BREAK
    }
    
    public static modes mode = null;//change to null to disable, modes.GOHOME for lumby
	public static Random rand1 = new Random();
	public static Random rand2 = new Random();
    public static boolean started = false;
    
    
    
   
	public static boolean initialized = false;
	public static double sleepMod;
	
	/**
	 * customizes settings to be less AIDS.
	 * Returns true if no AIDS, false if still AIDS.
	 * @return
	 */
	public static boolean customizeSettings()
	{
		if(ClientSettings.isAcceptAidEnabled())
    	{
    		ClientSettings.toggleAcceptAid(false);
    		
    	}
		else if(ClientSettings.roofsEnabled())
    	{
    		ClientSettings.toggleRoofs(false);
    	}
		else if(ClientSettings.getNPCAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK)
    	{
    		ClientSettings.setNPCAttackOptionsMode(ActionMode.ALWAYS_RIGHT_CLICK);
    		
    	}
		else if(ClientSettings.getPlayerAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK)
    	{
    		ClientSettings.setPlayerAttackOptionsMode(ActionMode.ALWAYS_RIGHT_CLICK);
    		
    	}
		else if(!ClientSettings.isShiftClickDroppingEnabled())
    	{
    		ClientSettings.toggleShiftClickDropping(true);
    		
    	}
		else if(!ClientSettings.isEscInterfaceClosingEnabled())
    	{
    		ClientSettings.toggleEscInterfaceClosing(true);
    		
    	}
		else if(ClientSettings.isGameAudioOn())
    	{
    		ClientSettings.toggleGameAudio(false);
    		
    	}
    	else if(ClientSettings.isResizableActive())
    	{
    		ClientSettings.toggleResizable(false);
    		
    	}
    	else if(ClientSettings.isTradeDelayEnabled())
    	{
    		ClientSettings.toggleTradeDelay(false);
    	}
		//profanity filter set to ON
    	else if(PlayerSettings.getConfig(1074) == 0)
    	{
        	//exit button for main Settings menu visible
        	if(Widgets.getWidgetChild(134,4) != null && Widgets.getWidgetChild(134,4).isVisible())
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
        	else
        	{
        		//"All settings" button visible in Settings tab
        		if(Widgets.getWidgetChild(116,75) != null && Widgets.getWidgetChild(116,75).isVisible())
            	{
        			if(Widgets.getWidgetChild(116,75).interact("All Settings"))
        			{
        				MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(134,4) != null && Widgets.getWidgetChild(134,4).isVisible(), Sleep.calculate(2222, 2222));
        			}
            		Sleep.sleep(333,444);
                } 
        		else if(!Tabs.isOpen(Tab.OPTIONS) && Tabs.open(Tab.OPTIONS))
        		{
        			MethodProvider.sleepUntil(() -> Tabs.isOpen(Tab.OPTIONS), Sleep.calculate(2222, 2222));
        		}
        	}
    	}
		if(ClientSettings.isAcceptAidEnabled() || 
    			ClientSettings.roofsEnabled() || 
    			PlayerSettings.getConfig(1074) == 0 ||
    			ClientSettings.getNPCAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK  || 
    			ClientSettings.getPlayerAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK ||
    			!ClientSettings.isShiftClickDroppingEnabled() || 
    			!ClientSettings.isEscInterfaceClosingEnabled() || 
    			ClientSettings.isGameAudioOn() || 
    			ClientSettings.isResizableActive() || 
    			ClientSettings.isTradeDelayEnabled())
		{
			MethodProvider.log("Some settings still AIDS");
			Sleep.sleep(666, 111);
			return false;
		}
		else
		{
			return true;
		}
		
	}
	/**
	 * returns true if chatbox is clear
	 * returns false if chatbox has some contents and cleared one
	 * @return
	 */
	public static boolean clearChatWithBackspace()
	{
		String[] x = Widgets.getWidgetChild(162,55).getText().split(":", 2);
    	String[] y = x[1].split("</col>",0);
    	String[] zeChatInputBox = y[0].split("<col=0000ff>",0);
		if(zeChatInputBox != null && zeChatInputBox.length >= 2)
		{
			MethodProvider.log("Clearing chatbox of some contents");
	    	Keyboard.typeSpecialKey(8);
			Sleep.calculate(10,55);
			return false;
		}
		return true;
	}
	
	public static void randomAFK(int chance)
	{
		int roll = API.rand2.nextInt(100);
		if(roll < chance)
		{
			int tmp = API.rand2.nextInt(100);
			if(tmp < 2)  
			{
				MethodProvider.log("AFK: 1% chance, max 240s");
				Sleep.sleep(50,84000);
			}
			else if(tmp < 6)  
			{
				MethodProvider.log("AFK: 5% chance, max 120s");
				Sleep.sleep(50,40000);
			}
			else if(tmp < 25)
			{
				MethodProvider.log("AFK: 14% chance, max 40s");
				Sleep.sleep(50,20000);
			}
			else if(tmp < 45)  
			{
				MethodProvider.log("AFK: 20% chance, max 20s");
				Sleep.sleep(50,10000);
			}
			else if(tmp < 65)  
			{
				MethodProvider.log("AFK: 20% chance, max 6.0s");
				Sleep.sleep(50,4500);
			}
			else if(tmp < 1000)  
			{
				MethodProvider.log("AFK: 35% chance, max 3.2s");
				Sleep.sleep(50,2400);
			}
		}
		
	}
	public static int getP2PWorld()
	{
		List<World> verifiedWorlds = new ArrayList<World>();
		for(World tmp : Worlds.noMinimumLevel())
		{
			if(	tmp.isMembers()
					&& !tmp.isPVP()
					&& !tmp.isTournamentWorld()
					&& !tmp.isDeadmanMode()
					&& !tmp.isHighRisk() 
					&& !tmp.isLeagueWorld()
					&& !tmp.isSuspicious()
					&& !tmp.isTargetWorld()
					&& tmp.getWorld() != 301) //just avoid popular world)
			{
				verifiedWorlds.add(tmp);
			}
		}
		Collections.shuffle(verifiedWorlds);
		return verifiedWorlds.size() > 0 ? verifiedWorlds.get(0).getWorld() : 302; // default world 302 (p2p) if none found
	}
	
	
	
	
	
	
	
	
	
	
}
