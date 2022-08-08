package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.dreambot.api.Client;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

import script.Main;
import script.quest.varrockmuseum.Timing;


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
		boolean moveMouse = false;
		if(API.rand2.nextInt(100) < chance)
		{
			moveMouse = true;
		}
		if(roll < chance)
		{
			int tmp = API.rand2.nextInt(100);
			int sleep = 0;
			if(tmp < 2)  
			{
				MethodProvider.log("AFK: 1% chance, max 240s");
				sleep = Sleep.calculate(50,84000);
			}
			else if(tmp < 6)  
			{
				MethodProvider.log("AFK: 5% chance, max 120s");
				sleep = Sleep.calculate(50,40000);
			}
			else if(tmp < 25)
			{
				MethodProvider.log("AFK: 14% chance, max 40s");
				sleep = Sleep.calculate(50,20000);
			}
			else if(tmp < 45)  
			{
				MethodProvider.log("AFK: 20% chance, max 20s");
				sleep = Sleep.calculate(50,10000);
			}
			else if(tmp < 65)  
			{
				MethodProvider.log("AFK: 20% chance, max 6.0s");
				sleep = Sleep.calculate(50,4500);
			}
			else if(tmp < 1000)  
			{
				MethodProvider.log("AFK: 35% chance, max 3.2s");
				sleep = Sleep.calculate(50,2400);
			}
			Timer sleepTimer = new Timer(sleep);
			final int mean = sleep / 2;
			final int sigma = sleep * 2 / 5;
			Timer mouseMoveTimer = new Timer((int) Calculations.nextGaussianRandom(mean, sigma));
			while(!sleepTimer.finished() && !ScriptManager.getScriptManager().isPaused() &&
					ScriptManager.getScriptManager().isRunning())
			{
				Main.customPaintText1 = "~~~~~~~~~~~~~";
				Main.customPaintText2 = "~~~~~AFK~~~~~";
				Main.customPaintText3 = "~~ "+Timer.formatTime(sleepTimer.remaining())+" ~~";
				Main.customPaintText4 = "~~~~~~~~~~~~~";
				if(moveMouse && 
						mouseMoveTimer.finished())
				{
					if(Mouse.isMouseInScreen() && 
							Mouse.moveMouseOutsideScreen())
					{
						Sleep.sleep(69,69);
					}
				}
				Sleep.sleep(69, 69);
				
			}
		}
		
	}
	public static void talkToNPC(String npcName)
	{
		NPC npc = NPCs.closest(npcName);
		if(npc == null)
		{
			MethodProvider.log(npcName+" null!");
			return;
		}
		if(!npc.canReach())
		{
			if(Walking.walk(npc)) Sleep.sleep(420, 696);
			return;
		}
		if(npc.interact("Talk-to"))
		{
			MethodProvider.sleepUntil(Dialogues::inDialogue,
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void talkToNPC(String npcName, String action)
	{
		NPC npc = NPCs.closest(n -> 
			n != null && 
			n.getName().contains(npcName) && 
			n.hasAction(action));
		if(npc == null)
		{
			MethodProvider.log("NPC " +npcName+" with action \'"+action+"\' null!");
			return;
		}
		if(!npc.canReach())
		{
			if(Walking.walk(npc)) Sleep.sleep(420, 696);
			return;
		}
		if(npc.interact(action))
		{
			MethodProvider.sleepUntil(Dialogues::inDialogue,
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void talkToNPC(String npcName, String action, Area npcArea)
	{
		NPC npc = NPCs.closest(n -> 
		n != null && 
		n.getName().contains(npcName) && 
		n.hasAction(action) && 
		npcArea.contains(n));
		if(npc == null)
		{
			MethodProvider.log("NPC " +npcName+" with action \'"+action+"\' in specified area null!");
			return;
		}
		if(!npc.canReach())
		{
			if(Walking.walk(npc)) Sleep.sleep(420, 696);
			return;
		}
		if(npc.interact(action))
		{
			MethodProvider.sleepUntil(Dialogues::inDialogue,
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void walkTalkToNPC(String npcName, String action, Area npcArea)
	{
		if(!npcArea.contains(Players.localPlayer()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(npcArea.getCenter())) Sleep.sleep(696, 420);
			return;
		}
		NPC npc = NPCs.closest(n -> 
		n != null && 
		n.getName().contains(npcName) && 
		n.hasAction(action) && 
		npcArea.contains(n));
		if(npc == null)
		{
			MethodProvider.log("NPC " +npcName+" with action \'"+action+"\' in specified area null!");
			return;
		}
		if(!npc.canReach())
		{
			if(Walking.walk(npc)) Sleep.sleep(420, 696);
			return;
		}
		if(npc.interact(action))
		{
			MethodProvider.sleepUntil(Dialogues::inDialogue,
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void interactNPC(String npcName, String action)
	{
		NPC npc = NPCs.closest(n -> 
			n != null && 
			n.getName().contains(npcName) && 
			n.hasAction(action));
		if(npc == null)
		{
			MethodProvider.log("NPC " +npcName+" with action \'"+action+"\' null!");
			return;
		}
		if(!npc.canReach())
		{
			if(Walking.walk(npc)) Sleep.sleep(420, 696);
			return;
		}
		if(npc.interact(action))
		{
			MethodProvider.sleepUntil(() -> Players.localPlayer().isInteracting(npc),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void interactWithGameObject(String gameObjectName, String action)
	{
		GameObject go = GameObjects.closest(n -> 
		n != null && 
		n.getName().contains(gameObjectName) && 
		n.hasAction(action));
		if(go == null)
		{
			MethodProvider.log("GameObject " +gameObjectName+" with action \'"+action+"\' null!");
			return;
		}
		if(!go.canReach())
		{
			if(Walking.walk(go)) Sleep.sleep(420, 696);
			return;
		}
		if(go.interact(action))
		{
			MethodProvider.sleepUntil(() -> go.getSurrounding().contains(Players.localPlayer().getTile()),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void interactWithGameObject(String gameObjectName, String action,Tile tile)
	{
		GameObject go = GameObjects.closest(n -> 
		n != null && 
		n.getName().contains(gameObjectName) && 
		n.hasAction(action) && 
		n.getTile().equals(tile));
		if(go == null)
		{
			MethodProvider.log("GameObject " +gameObjectName+" with action \'"+action+"\' at specified tile null!");
			return;
		}
		if(!go.canReach())
		{
			if(Walking.walk(go)) Sleep.sleep(420, 696);
			return;
		}
		if(go.interact(action))
		{
			MethodProvider.sleepUntil(() -> go.getSurrounding().contains(Players.localPlayer().getTile()),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void interactWithGameObject(String gameObjectName, String action, Area gameObjectArea)
	{
		GameObject go = GameObjects.closest(n -> 
		n != null && 
		n.getName().contains(gameObjectName) && 
		n.hasAction(action) && 
		gameObjectArea.contains(n));
		if(go == null)
		{
			MethodProvider.log("GameObject " +gameObjectName+" with action \'"+action+"\' in specified area null!");
			return;
		}
		if(!go.canReach())
		{
			if(Walking.walk(go)) Sleep.sleep(420, 696);
			return;
		}
		if(go.interact(action))
		{
			MethodProvider.sleepUntil(() -> go.getSurrounding().contains(Players.localPlayer().getTile()),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void walkInteractWithGameObject(String gameObjectName, String action, Area gameObjectArea)
	{
		if(!gameObjectArea.contains(Players.localPlayer()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(gameObjectArea.getCenter())) Sleep.sleep(696, 420);
			return;
		}
		GameObject go = GameObjects.closest(n -> 
		n != null && 
		n.getName().contains(gameObjectName) && 
		n.hasAction(action) && 
		gameObjectArea.contains(n));
		if(go == null)
		{
			MethodProvider.log("GameObject " +gameObjectName+" with action \'"+action+"\' in specified area null!");
			return;
		}
		if(!go.canReach())
		{
			if(Walking.walk(go)) Sleep.sleep(420, 696);
			return;
		}
		if(go.interact(action))
		{
			MethodProvider.sleepUntil(() -> go.getSurrounding().contains(Players.localPlayer().getTile()),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void walkInteractWithGameObject(String gameObjectName, String action, Area gameObjectArea, Condition condition)
	{
		if(!gameObjectArea.contains(Players.localPlayer()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(gameObjectArea.getCenter())) Sleep.sleep(696, 420);
			return;
		}
		GameObject go = GameObjects.closest(n -> 
		n != null && 
		n.getName().contains(gameObjectName) && 
		n.hasAction(action) && 
		gameObjectArea.contains(n));
		if(go == null)
		{
			MethodProvider.log("GameObject " +gameObjectName+" with action \'"+action+"\' in specified area null!");
			return;
		}
		if(!go.canReach())
		{
			if(Walking.walk(go)) Sleep.sleep(420, 696);
			return;
		}
		if(go.interact(action))
		{
			MethodProvider.sleepUntil(() -> condition.verify(),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static void walkPickupGroundItem(String groundItemName, String action, Area groundItemArea)
	{
		if(!groundItemArea.contains(Players.localPlayer()))
		{
			if(!Walkz.isStaminated()) Walkz.drinkStamina();
			if(Walkz.isStaminated() && Walking.getRunEnergy() > 5 && !Walking.isRunEnabled()) Walking.toggleRun();
			if(Walking.shouldWalk(6) && Walking.walk(groundItemArea.getCenter())) Sleep.sleep(696, 420);
			return;
		}
		GroundItem gi = GroundItems.closest(n -> 
		n != null && 
		n.getName().contains(groundItemName) && 
		n.hasAction(action) && 
		groundItemArea.contains(n));
		if(gi == null)
		{
			MethodProvider.log("GroundItem " +groundItemName+" with action \'"+action+"\' in specified area null!");
			return;
		}
		if(!gi.canReach())
		{
			if(Walking.walk(gi)) Sleep.sleep(420, 696);
			return;
		}
		if(gi.interact(action))
		{
			MethodProvider.sleepUntil(() -> gi == null || !gi.exists(),
					() -> Players.localPlayer().isMoving(),Sleep.calculate(3333, 2222),66);
		}
	}
	public static int roundToMultiple (int number,int multiple){
	    int result = multiple;
	    //If not already multiple of given number
	    if (number % multiple != 0)
	    {
	        int division = number / multiple;
	        result = division * multiple;
	    }
	    return result;
	}
	public static int getRandomP2PWorld()
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
					&& tmp.getWorld() != 302) //just avoid popular world)
			{
				verifiedWorlds.add(tmp);
			}
		}
		Collections.shuffle(verifiedWorlds);
		return verifiedWorlds.size() > 0 ? verifiedWorlds.get(0).getWorld() : 302; // default world 302 if none found
	}
	
	
	
	
	
	
	
	
	
	
}
