package script;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import org.dreambot.api.Client;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.*;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.message.Message;
import script.behaviour.*;
import script.framework.Tree;
import script.paint.*;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.ernestthechicken.ErnestTheChicken;
import script.quest.magearena1.MageArena1;
import script.quest.magearena2.MageArena2;
import script.quest.naturespirit.NatureSpirit;
import script.quest.priestinperil.PriestInPeril;
import script.quest.restlessghost.RestlessGhost;
import script.quest.varrockmuseum.VarrockQuiz;
import script.skills.crafting.TrainCrafting;
import script.skills.magic.TrainMagic;
import script.skills.ranged.TrainRanged;
import script.skills.slayer.*;
import script.skills.woodcutting.TrainWoodcutting;
import script.utilities.API;
import script.utilities.Sleep;


@ScriptManifest(
		name = "BarrowsFromScratch", 
		author = "Dreambotter420", 
		description = "Makes barrows profitable",
		version = 69.420,
		category = Category.MISC, 
		image = "lFwvTuW.jpg")
public class Main extends AbstractScript implements PaintInfo, ChatListener
{
    public static Timer timer;
    @Override
    public void onStart()
    {
    	MethodProvider.log("Barrows From Scratch starting!");
    	timer = new Timer(2000000000);
    	Walking.getAStarPathFinder().addObstacle(new PassableObstacle("Wilderness Ditch", "Cross", null, null, null));
    	Keyboard.setWordsPerMinute(150);
    	Sleep.dt = LocalDateTime.now();
        API.started = true;
        instantiateTree();
    }

    private final Tree tree = new Tree();
    
    private void instantiateTree() {
        tree.addBranches(new WaitForLogged_N_Loaded(),
        			new Initialize(),
        			new OnTutorialIsland(),
        			new OffTutorialIsland().addLeafs(
        					new DecisionLeaf(),
        					new RestlessGhost(),
        					new PriestInPeril(),
        					new NatureSpirit(),
        					new MageArena2(),
        					new MageArena1(),
        					new ErnestTheChicken(),
        					new AnimalMagnetism(),
        					new VarrockQuiz(),
        					new TrainWoodcutting(),
        					new TrainCrafting(),
        					new TrainMagic(),
        					new TrainSlayer(),
        					new TrainRanged(),
        					new LogoutBreak()
        					));
    }

    
    private final Area stuckInFerox = new Area(3123, 3629, 3125, 3628, 0);
    private final Area stuckInFerox2 = new Area(3134, 3619, 3135, 3617, 0);
    @Override
    public int onLoop()
    {
        //stuck in ferox enclave
        if(stuckInFerox.contains(getLocalPlayer()) || 
        		stuckInFerox2.contains(getLocalPlayer()))
        {
            if(Widgets.getWidgetChild(229, 1) != null && 
            		Widgets.getWidgetChild(229, 1).isVisible() &&
            		Widgets.getWidgetChild(229, 1).getText().contains("When returning to the Enclave, if you are teleblocked, you will not"))
            {
            	Dialogues.continueDialogue();
            	return Sleep.calculate(694, 111);
            }
            else if(Dialogues.areOptionsAvailable())
            {
            	Dialogues.chooseOption("Yes, and don\'t ask again.");
            	return Sleep.calculate(694, 111);
            }
        	Sleep.sleep(111, 111);
        	if(getLocalPlayer().isMoving()) return Sleep.calculate(111, 111);
        	GameObject barrier = GameObjects.closest("Barrier");
        	if(barrier == null) MethodProvider.log("Barrier null in Ferox Enclave! :-(");
        	else barrier.interact("Pass-through");
        	return Sleep.calculate(1111, 111);
        }
        
        //check wildy ditch cross sign
        if(Widgets.getWidgetChild(475,11,1) != null && 
        		Widgets.getWidgetChild(475,11,1).isVisible() &&
        		Widgets.getWidgetChild(475,11,1).getText().contains("Enter Wilderness"))
        {
        	Widgets.getWidgetChild(475,11,1).interact();
        	Sleep.sleep(3000, 111);
        }
        return tree.onLoop();
    }
    
    @Override
    public void onResume()
    {
    	
    }
    
    @Override
    public void onPause()
    {
    	
    }
    
    @Override
    public void onExit()
    {
    	
    }

    // Our paint info
    // Add new lines to the paint here
    @Override
    public String[] getPaintInfo()
    {
    	return new String[] {
    			getManifest().name() +" "+ getManifest().version() + " by Dreambotter420 ^_^",
                "Current Branch: " + API.currentBranch,
                "Current Task: " + API.currentLeaf,
                "Time spent on current task: " + (DecisionLeaf.taskTimer == null ? "N/A" : DecisionLeaf.taskTimer.formatTime()),
                "Time remaining until next force task switch: " + (DecisionLeaf.taskTimer == null ? "N/A" : Timer.formatTime(DecisionLeaf.taskTimer.remaining()))
        };
    }
   
    // Instantiate the paint object. This can be customized to your liking.
    private final CustomPaint CUSTOM_PAINT = new CustomPaint(this,
            CustomPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN,
            new Color[] {new Color(255, 251, 255)},
            "Trebuchet MS",
            new Color[] {new Color(50, 50, 50, 175)},
            new Color[] {new Color(28, 28, 29)},
            1, false, 5, 3, 0);
    private final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


    @Override
    public void onPaint(Graphics2D graphics2D)
    {
        // Set the rendering hints
        graphics2D.setRenderingHints(aa);
        // Draw the custom paint
        CUSTOM_PAINT.paint(graphics2D);
    }
    @Override
    public void onGameMessage(Message msg)
    {
    	ReadMessage.readGameMessage(msg);
    }
    
    @Override
    public void onPlayerMessage(Message msg)
    {
    	ReadMessage.readPlayerMessage(msg);
    }
}
