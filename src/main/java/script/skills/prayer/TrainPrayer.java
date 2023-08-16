package script.skills.prayer;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.Menu;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Dialoguez;
import script.utilities.InvEquip;
import script.utilities.Locations;
import script.utilities.MissingAPI;
import script.utilities.Sleepz;
import script.utilities.Tabz;
import script.utilities.Walkz;
import script.utilities.id;
/**
 * Trains prayer 1-45/50
 * 
 * @author Dreambotter420
 * ^_^
 */
public class TrainPrayer extends Leaf {

	@Override
	public boolean isValid() {
		return API.mode == API.modes.TRAIN_PRAYER;
	}
	public static final int constructionBook = 8463;
	public static boolean initialized = false;
	public static int neededBones = 0;
	public static final int xpPerBone = 252;
	public static int bonesToGoal = 0;
	public static int randomBonesAdder = 0;
	public static int randomCoinsAmount = 0;
	public static boolean visitedLast = false;
	public static boolean announcedShitHouse = false;
	public static boolean forceLeaveShitHouse = false;
	public static List<Integer> usedSlots = new ArrayList<Integer>();
	
    public void onStart() {
    	randomBonesAdder = (int) Calculations.nextGaussianRandom(15, 7);
        if(randomBonesAdder <= 1 || randomBonesAdder >= 20) randomBonesAdder = 1;
        randomCoinsAmount = (int) Calculations.nextGaussianRandom(6000, 1000);
        if(randomCoinsAmount < 2500) randomCoinsAmount = 2500;
        
        initialized = true;
    }
    public boolean onExit() {
    	if(Locations.isInstanced())
    	{
    		leaveHouse();
    		return false;
    	}
    	visitedLast = false;
    	return true;
    }
    /** returns true if not inside house anymore
     * 
     */
    public static boolean leaveHouse()
    {
    	if(!Locations.isInstanced()) return true;
    	GameObject portal = GameObjects.closest("Portal");
    	if(portal != null)
    	{
    		if(portal.interact("Enter"))
    		{
    			Sleep.sleepUntil(() -> !Locations.isInstanced(), () -> Players.getLocal().isMoving(), Sleepz.calculate(2222, 2222), 50);
    		} else {
    			if(Walking.shouldWalk(6) && Walking.walk(portal)) Sleepz.sleep(420, 696);
    		}
    		return false;
    	}
    	
    	if(Widgets.get(370, 18) != null && Widgets.get(370, 18).isVisible())
    	{
    		if(Widgets.get(370,18).interact("Leave House"))
    		{
    			Sleep.sleepUntil(() -> Locations.rimmington.contains(Players.getLocal()), Sleepz.calculate(2222, 2222));
    			return true;
    		}
    		return false;
    	}
    	if(Tabs.isOpen(Tab.OPTIONS))
    	{
    		if(Widgets.get(116, 74) != null && Widgets.get(116, 74).isVisible())
        	{
        		if(Widgets.get(116, 74).interact("View House Options"))
        		{
        			Sleep.sleepUntil(() -> Widgets.get(370, 18) != null && Widgets.get(370, 18).isVisible(), Sleepz.calculate(2222, 2222));
        		}
        	}
    		if(PlayerSettings.getBitValue(9683) != 0)
    		{
    			Widgets.get(116, 106).interact("Controls");
    		}
    	}
    	else
    	{
    		Tabz.open(Tab.OPTIONS);
    	}
    	return false;
    }
    
    public static void chooseRandomAltarHouse()
    {
    	if(Inventory.getSelectedItemName() != null)
		{
    		Inventory.deselect();
		}
    	if(Worlds.getCurrentWorld() != 330)
    	{
    		if(Widgets.isOpen()) Widgets.closeAll();
    		else
    		{
    			MissingAPI.scrollHopWorld(330);
    		}
    		return;
    	}
    	if(Widgets.get(52,13) != null && 
    			Widgets.get(52,13).isVisible())
    	{
    		List<WidgetChild> validAltarGC = Widgets.getWidgets(w -> 
    				w != null &&
    				w.isVisible() &&
    				w.getParentID() == 52 && 
    				w.getID() == 13 && 
    				w.getIndex() >= 0 &&
    				w.getIndex() <= 200 && 
    				w.getText().equals("Y"));
    		if(validAltarGC.isEmpty())
    		{
    			Logger.log("Hmmm... no valid altars available on w330... waiting...");
    			Sleepz.sleep(10000, 4200);
    			Widgets.get(52,30).interact("Refresh Data");
    			Sleepz.sleep(696, 420);
    			return;
    		}
    		Collections.shuffle(validAltarGC);
    		WidgetChild enterValidHouseButton = Widgets.get(52,19,validAltarGC.get(0).getIndex());
    		
    		if(enterValidHouseButton.interact("Enter House"))
    		{
    			visitedLast = true;
    			Sleep.sleepUntil(() -> Locations.isInstanced(), Sleepz.calculate(3333, 3333));
    			Sleepz.sleep(696, 4200);
    		}
    		return;
    	}
    	//check if House Advertisement is not null
    	GameObject houseAd = GameObjects.closest("House Advertisement");
    	if(houseAd != null)
    	{
    		if(visitedLast)
    		{
    			if(houseAd.interact("Visit-last"))
        		{
        			Sleep.sleepUntil(() -> Locations.isInstanced(), () -> Players.getLocal().isMoving(), Sleepz.calculate(2222,2222),50);
        			Sleepz.sleep(420, 696);
        			if(!Locations.isInstanced()) visitedLast = false;
        		}
    			return;
    		}
    		if(houseAd.interact("View"))
    		{
    			Sleep.sleepUntil(() -> Widgets.get(52,13) != null && 
    	    			Widgets.get(52,13).isVisible(), () -> Players.getLocal().isMoving(), Sleepz.calculate(2222, 2222),50);
    		}
    		else if(Walking.shouldWalk(6) && Walking.walk(houseAd))Sleepz.sleep(69, 1111);
    		return;
    	}
    	//check if near rimmington at all
    	if(Locations.rimmington.contains(Players.getLocal()) || Locations.rimmington.distance(Players.getLocal().getTile()) < 50)
    	{
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.rimmington.getCenter())) Sleepz.sleep(696, 420);
    		return;
    	}
    }
    
    @Override
    public int onLoop() {
        if(!initialized) onStart();
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		if(onExit())
    		{
    			Logger.log("[TIMEOUT] -> Prayer!");
                API.mode = null;
    		}
            return Sleepz.sleepTiming();
    	}
        final int prayer = Skills.getRealLevel(Skill.PRAYER);
    	if (prayer >= DecisionLeaf.prayerSetpoint) {
    		if(onExit())
    		{
    			Logger.log("[COMPLETE] -> lvl "+DecisionLeaf.prayerSetpoint+" prayer!");
                API.mode = null;
    		}
            return Sleepz.sleepTiming();
        }
    	
    	int prayerGoal = DecisionLeaf.prayerSetpoint;
        int xpTotalForGoal = Skills.getExperienceForLevel(prayerGoal);
        int currentXP = Skills.getExperience(Skill.PRAYER);
        double neededXP = (xpTotalForGoal - currentXP);
        bonesToGoal = ((int) (neededXP / xpPerBone)) + randomBonesAdder;
        Main.paint_task = "Prayer lvl goal: " + prayerGoal;
        Main.paint_itemsCount = "Have to offer more bones until reached goal: " +bonesToGoal;

    	if(Locations.isInstanced())
    	{
    		if(Inventory.count(id.dBones) <= 0 || forceLeaveShitHouse) 
    		{
    			leaveHouse();
    			return Sleepz.sleepTiming();
    		}
    		
    		//cancel, do not bury the bone
    		if(Dialogues.areOptionsAvailable())
    		{
    			for(String s : Dialogues.getOptions())
        		{
        			if(s == null) continue;
        			if(s.contains("Bury the bone")) 
        			{
        				if(Dialogues.chooseOption("Cancel")) Sleepz.sleep(696,420);
        				return Sleepz.interactionTiming();
        			}
        		}
    		}
    		
    		//have bones to look to offer
    		GameObject altar = GameObjects.closest("Altar");
    		if(altar != null)
    		{
    			Filter<GameObject> litBurnersFilter = (g ->
	    			g != null && 
					g.getName().contains("Incense burner") && 
					g.hasAction("Re-light"));
    			List<GameObject> litBurners = GameObjects.all(litBurnersFilter);
    			if(litBurners.size() <= 1)
    			{
    				if(!announcedShitHouse)
    				{
    					if(API.clearChatWithBackspace())
    					{
        					int rand2 = Calculations.random(0,100);
        					String ur = rand2 > 50 ? "ur" : "your";
        					int rand6 = Calculations.random(0,1000);
        					String apostrophe = rand6 > 50 ? "\'" : "";
        					int rand4 = Calculations.random(0,1000);
        					String expletive = "";
        					if(rand4 < 100) expletive = "for fucks sake ";
        					else if(rand4 < 200) expletive = "goddamn ";
        					else if(rand4 < 300) expletive = "jesus ";
        					else if(rand4 < 400) expletive = "christ ";
        					else if(rand4 < 500) expletive = "holy hell ";
        					else if(rand4 < 600) expletive = "...";
        					else if(rand4 < 700) expletive = ":-( ";
        					else if(rand4 < 800) expletive = "D: ";
        					else if(rand4 < 900) expletive = "D:< ";
        					else expletive = "waste of bones... ";
        					
        					int rand3 = Calculations.random(0,1000);
        					String bruh = "";
        					if(rand3 < 100) bruh = "bro";
        					else if(rand3 < 200) bruh = "bruh";
        					else if(rand3 < 600) bruh = "mate";
        					else if(rand3 < 700) bruh = "man";
        					else if(rand3 < 900) bruh = "dude";
        					else bruh = "bruv";
        					
        					int rand = Calculations.random(0, 1000);
        					String indicateShitHouse = "";
        					int rand5 = Calculations.random(0,100);
        					if(rand5 < 25) 
        					{
        						indicateShitHouse.concat(expletive);
        						String addText = null;
        						if(rand < 100) addText = bruh+" light "+ur+" burners pls";
            					else if(rand < 200) addText = "light "+ur+" burners pls "+bruh+" ";
            					else if(rand < 300) addText = ur+" burners aren"+apostrophe+"t lit "+bruh+" ";
            					else if(rand < 400) addText = bruh+" "+ur+" burners aren"+apostrophe+"t lit ";
            					else if(rand < 500) addText = "waste of xp without incense "+bruh+" ";
            					else if(rand < 600) addText = bruh+" waste of xp without incense ";
            					else if(rand < 700) addText = "i"+apostrophe+"m disappointed "+bruh+" ";
            					else if(rand < 800) addText = bruh+" i"+apostrophe+"m disappointed ";
            					else if(rand < 900) addText = "i"+apostrophe+"m outta here "+bruh+" ";
            					else addText = bruh+" i"+apostrophe+"m outta here "+expletive;
        						indicateShitHouse.concat(addText);
        					}
        					else
        					{
        						if(rand5 > 75) expletive = "";
        						if(rand < 100) indicateShitHouse = bruh+" light "+ur+" burners pls"+expletive;
            					else if(rand < 200) indicateShitHouse = "light "+ur+" burners pls "+bruh+" "+expletive;
            					else if(rand < 300) indicateShitHouse = ur+" burners aren"+apostrophe+"t lit "+bruh+" "+expletive;
            					else if(rand < 400) indicateShitHouse = bruh+" "+ur+" burners aren"+apostrophe+"t lit "+expletive;
            					else if(rand < 500) indicateShitHouse = "waste of xp without incense "+bruh+" "+expletive;
            					else if(rand < 600) indicateShitHouse = bruh+" waste of xp without incense "+expletive;
            					else if(rand < 700) indicateShitHouse = "i"+apostrophe+"m disappointed "+bruh+" "+expletive;
            					else if(rand < 800) indicateShitHouse = bruh+" i"+apostrophe+"m disappointed "+expletive;
            					else if(rand < 900) indicateShitHouse = "i"+apostrophe+"m outta here "+bruh+" "+expletive;
            					else indicateShitHouse = bruh+" i"+apostrophe+"m outta here "+expletive;
        					}
        					
        					Keyboard.type(indicateShitHouse);
        					visitedLast = false;
        					forceLeaveShitHouse = true;
        					announcedShitHouse = true;
    					}
    					return Sleepz.calculate(69, 69);
    				}
    				leaveHouse();
    				return Sleepz.sleepTiming();
    			}
    			//find out if item is selected
    			if(Inventory.getSelectedItemName() == null)
    			{
    				Logger.log("Nothing selected");
    				if(Tabs.isOpen(Tab.INVENTORY))
    				{
    					List<Integer> boneSlots = new ArrayList<Integer>();
        				for(Item bones : Inventory.all())
        				{
        					if(bones != null && bones.getID() == id.dBones) 
        					{
        						boolean break1 = false;
        						for(int bannedSlot : usedSlots) {
        							if(bannedSlot == bones.getSlot()) 
        							{
        								break1 = true;
        								break;
        							}
        						}
        						if(break1) continue;
        						boneSlots.add(bones.getSlot());
        					}
        				}
        				Collections.reverse(boneSlots);
        				int slot = 0;
        				if(boneSlots.isEmpty()) slot = Inventory.get(id.dBones).getSlot();
        				else slot = boneSlots.get(0);
        				Item lastBone = Inventory.getItemInSlot(slot);
        				usedSlots.add(slot);
        				lastBone.interact("Use");
    				} else if(Widgets.isOpen()) Widgets.closeAll();
    				else Tabz.open(Tab.INVENTORY);
    				Sleepz.sleep(69, 69);
    			}
    			else if(!Inventory.getSelectedItemName().contains(new Item(id.dBones,1).getName()))
    			{
    				Logger.log("Something NOT dbones selected: "+Inventory.getSelectedItemName());
    				
    				Inventory.deselect();
    				Sleepz.sleep(69, 69);
    			}
    			else //dbones are selected
    			{
    				Logger.log("Dbones selected");
    				String useAction = "Use";
					if(Dialogues.inDialogue()) 
					{
						if(Menu.isVisible())
						{
							if(Menu.clickAction(useAction, altar)) 
							{
								Sleep.sleepUntil(() -> Players.getLocal().isMoving() || Players.getLocal().isAnimating(), Sleepz.calculate(2222, 2222));
								Sleepz.sleep(69,69);
							}
						}
						else 
						{
							Area canvas = new Area(Widgets.get(548, 9).getRectangle());
		    				Area altarBounds = new Area(altar.getModel().getHullBounds());
		    				altarBounds.intersect(canvas);
		    				if(altarBounds.isEmpty())
		    				{
		    					Camera.rotateToEntity(altar);
		    					return Sleepz.calculate(69, 69);
		    				}
		    				
		    				if(!altar.getModel().getHullBounds().contains(Mouse.getPosition()))
		    				{
		    					Mouse.move(altar);
		    					return Sleepz.calculate(69, 69);
		    				}
		    				else Menu.open();
						}
						return Sleepz.calculate(69, 69);
					}
					if(Players.getLocal().isMoving()) Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), Sleepz.calculate(2222, 2222));
		    		if(Players.getLocal().isAnimating())
		    		{
		    			Sleep.sleepUntil(() -> Inventory.count(id.dBones) <= 0 || 
		    					Dialogues.inDialogue() || 
		    					!Locations.isInstanced() ||
		    					GameObjects.all(litBurnersFilter).size() <= 1,
		    					() -> (Players.getLocal().isAnimating() || Players.getLocal().isMoving()), Sleepz.calculate(2222, 2222),50);
		    		 	if(Inventory.count(id.dBones) <= 0 || 
		    		 			GameObjects.all(litBurnersFilter).size() <= 1 || 
		    		 			Locations.isInstanced()) return Sleepz.sleepTiming();
		    		}
		    		if(Menu.isVisible())
					{
						if(Menu.clickAction(useAction, altar)) 
						{
							Sleep.sleepUntil(() -> Players.getLocal().isMoving() || Players.getLocal().isAnimating(), Sleepz.calculate(2222, 2222));
							Sleepz.sleep(69,69);
						}
					}
					else 
					{
						Area canvas = new Area(Widgets.get(548, 9).getRectangle());
	    				Area altarBounds = new Area(altar.getModel().getHullBounds());
	    				altarBounds.intersect(canvas);
	    				if(altarBounds.isEmpty())
	    				{
	    					Camera.rotateToEntity(altar);
	    					return Sleepz.calculate(69, 69);
	    				}
	    				
	    				if(!altar.getModel().getHullBounds().contains(Mouse.getPosition()))
	    				{
	    					Mouse.move(altar);
	    					return Sleepz.calculate(69, 69);
	    				}
	    				else Menu.open();
					}
					return Sleepz.sleepTiming();
    			}
    		} else leaveHouse();
    	}
    	else //not in instanced area (house) 
    	{
    		if(!unlockHouse()) return Sleepz.sleepTiming();
        	
    		//handle dialogues from Phials/Estate agent
        	if(Dialogues.isProcessing())
        	{
        		return Sleepz.sleepTiming();
        	}
        	if(Dialogues.canContinue())
        	{
        		Dialoguez.continueDialogue();
        		return Sleepz.sleepTiming();
        	}
        	if(Dialogues.areOptionsAvailable()) 
        	{ 
        		//have at least one option to exchange items, check for "all" and if none, choose "1"
        		for(String option : Dialogues.getOptions())
        		{
        			if(option == null || option.isEmpty() || option.equalsIgnoreCase("null")) continue;
        			if(option.contains("Exchange All:")) {
        				Dialogues.chooseOption(option);
        				return Sleepz.sleepTiming();
        			}
        		}
        		Dialoguez.chooseOptionIndex(1);
        		return Sleepz.sleepTiming();
        	}
        	if(Inventory.count(constructionBook) > 0)
        	{
        		if(Tabz.open(Tab.INVENTORY)) Inventory.drop(constructionBook);
        		return Sleepz.sleepTiming();
        	}
    		usedSlots.clear();
    		forceLeaveShitHouse = false;
    		announcedShitHouse = false;
    		if(Inventory.count(id.dBones) >= 1)
    		{
    			if(Locations.rimmington.contains(Players.getLocal())) 
    			{
    				chooseRandomAltarHouse();
    			}
    			else Walkz.teleportOutsideHouse(180000);
    			return Sleepz.sleepTiming();
    		} 
    		if(Inventory.count(new Item(id.dBones,1).getNotedItemID()) > 0) 
    		{
    			//check if Phials nearby
    	    	NPC phials = NPCs.closest("Phials");
    	    	if(phials != null)
    	    	{
    	    		if(!Players.getLocal().isInteracting(phials))
    	    		{
    	        		if(!Tabs.isOpen(Tab.INVENTORY))
    	        		{
    	        			Tabz.open(Tab.INVENTORY);
    	        			return Sleepz.interactionTiming();
    	        		}
    	    			Item bonesNoted = Inventory.get(new Item(id.dBones,1).getNotedItemID());
    	        		if(bonesNoted.useOn(phials))
	        			{
	        				Sleep.sleepUntil(Dialogues::inDialogue, () -> Players.getLocal().isMoving(), Sleepz.calculate(2222,2222), 50);
	        			}
    	    		}
    	    	}
    	    	else
    	    	{
    	    		if(Locations.rimmington.distance(Players.getLocal().getTile()) > 45) Walkz.teleportOutsideHouse(180000);
        			else if(Walking.shouldWalk(6) && Walking.walk(Locations.rimmington.getCenter())) Sleepz.sleep(420,1111);
    	    		
    	    	}
    	    	return Sleepz.sleepTiming();
    		} 
    		//check bank for stuff
	    	if(!InvEquip.checkedBank()) return Sleepz.sleepTiming();
	    	final int totalBones = Bank.count(id.dBones) + Inventory.count(id.dBones) + Inventory.count(new Item(id.dBones,1).getNotedItemID());
	    	if(totalBones < bonesToGoal)
	    	{
	    		neededBones = bonesToGoal - totalBones;
	    	}
	    	Main.paint_subTask = "total # bones (invy unnoted + noted + bank): " + totalBones;
	    	Main.paint_levels = "total # bones missing to goal: " + neededBones;
	    	//withdraw all dBones noted
	    	if(Bank.contains(id.dBones) || totalBones < bonesToGoal || Inventory.count(id.houseTele) > 1)
	    	{
	    		if(Locations.isInstanced())
	    		{
	    			leaveHouse();
	    			return Sleepz.sleepTiming();
	    		}
	    		InvEquip.clearAll();
	    		InvEquip.addInvyItem(id.dBones, bonesToGoal, bonesToGoal, true, neededBones);
	    		InvEquip.addInvyItem(id.houseTele, 1, 1, false, 10);
	    		InvEquip.setEquipItem(EquipmentSlot.RING, InvEquip.wealth);
	    		if(!Locations.unlockedHouse)
	    		{
	    			InvEquip.addInvyItem(id.fallyTele, 1, 1, false, 10);
	    		}
	    		InvEquip.addInvyItem(InvEquip.coins, 5, randomCoinsAmount, false, 0);
	    		InvEquip.fulfillSetup(true, 180000);
	    		return Sleepz.sleepTiming();
	    	}
	    	if(Widgets.isOpen())Widgets.closeAll();
			
	    	
	    	
	    	if(Inventory.interact(id.houseTele, "Break"))
			{
				Sleep.sleepUntil(() -> Locations.isInstanced(), () -> Players.getLocal().isAnimating(), Sleepz.calculate(4444,2222),50);
				return Sleepz.sleepTiming();
			}
    	}
		return Sleepz.sleepTiming();
    }

    public static boolean unlockHouse()
    {
    	if(Inventory.count(constructionBook) > 0 || Bank.count(constructionBook) > 0) Locations.unlockedHouse = true;
    	if(Inventory.count(constructionBook) > 0)
    	{
    		if(!Tabz.open(Tab.INVENTORY)) return false;
    		if(Inventory.dropAll(constructionBook)) Sleep.sleepTick();
    		return false;
    	}
    	if(Locations.unlockedHouse) return true;
    	
    	//handle dialogues from Phials/Estate agent
    	if(Dialoguez.handleDialogues()) return false;
    	
    	//check for estate agent around us
    	NPC estateAgent = NPCs.closest("Estate agent");
    	if(estateAgent != null)
    	{
    		if(estateAgent.canReach())
    		{
    			if(estateAgent.interact("Talk-to"))
    			{
    				Sleep.sleepUntil(() -> Dialogues.inDialogue(), Sleepz.calculate(2222,2222));
    			}
    		}
    		else
    		{
    			GameObject doorfuckery = GameObjects.closest(d -> d!=null && 
    					d.getName().equals("Door") && 
    					d.hasAction("Close") && 
    					d.getTile().equals(new Tile(2982,3370,0)));
    			if(estateAgent.getTile().equals(new Tile(2981,3370,0)) && doorfuckery != null)
    			{
    				if(doorfuckery.interact("Close")) Sleepz.sleep(696, 420);
    				{
    					Sleep.sleepTicks(2);
    					Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), Sleepz.calculate(2222,2222));
    				}
    				return false;
    			}
    			if(Walking.shouldWalk(6) && Walking.walk(Locations.estateRoom.getCenter())) Sleepz.sleep(666,1111);
    		}
    		Sleepz.sleep();
    		return false;
    	}
    	//see how close we are to estate room - walk if close
    	final double dist = Locations.estateRoom.distance(Players.getLocal().getTile());
    	if(dist <= 45)
    	{
    		if(Walking.shouldWalk(6) && Walking.walk(Locations.estateRoom.getCenter())) Sleepz.sleep(666,1111);
    		Sleepz.sleep();
    		return false;
    	}
    	if(!Walkz.teleportFalador(60000)) InvEquip.buyItem(id.fallyTele, 10, 60000);
    	Sleepz.sleep();
		return false;
    }
    
    
}
