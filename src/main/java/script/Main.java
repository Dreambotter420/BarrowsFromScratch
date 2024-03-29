package script;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;

import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.script.*;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.message.Message;

import script.actionz.ReportSpammer;
import script.behaviour.*;
import script.framework.Tree;
import script.paint.*;
import script.quest.alfredgrimhand.AlfredGrimhandsBarcrawl;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.ernestthechicken.ErnestTheChicken;
import script.quest.fightarena.FightArena;
import script.quest.fremenniktrials.FremennikTrials;
import script.quest.horrorfromthedeep.HorrorFromTheDeep;
import script.quest.magearena1.MageArena1;
import script.quest.magearena2.MageArena2;
import script.quest.naturespirit.NatureSpirit;
import script.quest.priestinperil.PriestInPeril;
import script.quest.restlessghost.RestlessGhost;
import script.quest.varrockmuseum.VarrockQuiz;
import script.quest.waterfallquest.WaterfallQuest;
import script.skills.agility.TrainAgility;
import script.skills.crafting.TrainCrafting;
import script.skills.herblore.TrainHerblore;
import script.skills.magic.TrainMagic;
import script.skills.melee.TrainMelee;
import script.skills.prayer.TrainPrayer;
import script.skills.ranged.TrainRanged;
import script.skills.slayer.*;
import script.skills.woodcutting.TrainWoodcutting;
import script.utilities.API;
import script.utilities.ReadMessage;
import script.utilities.Sleepz;


@ScriptManifest(
		name = "BarrowsFromScratch", 
		author = "Dreambotter420", 
		description = "Makes barrows profitable",
		version = 1.69420,
		category = Category.MISC, 
		image = "lFwvTuW.jpg")
public class Main extends AbstractScript implements PaintInfo, ChatListener
{
    public static Timer timer;
    @Override
    public void onStart()
    {
    	Logger.log("Barrows From Scratch starting!");
    	timer = new Timer(2000000000);
    	
    	Keyboard.setWordsPerMinute(150);
    	Sleepz.dt = LocalDateTime.now();
        API.started = true;
        instantiateTree();
    }
    public static Player loc = null;
    private final Tree tree = new Tree();
    
    private void instantiateTree() {
        tree.addBranches(new WaitForLogged_N_Loaded(),
        			new Initialize(),
        			new OnTutorialIsland(),
        			new CustomizeSettings(),
        			new Test(),
					new ReportSpammer(),
					new DecisionLeaf(),
					new RestlessGhost(),
					new PriestInPeril(),
					new NatureSpirit(),
					new MageArena2(),
					new AlfredGrimhandsBarcrawl(),
					new MageArena1(),
					new HorrorFromTheDeep(),
					new WaterfallQuest(),
					new FightArena(),
					new FremennikTrials(),
					new ErnestTheChicken(),
					new AnimalMagnetism(),
					new VarrockQuiz(),
					new TrainWoodcutting(),
					//new TrainWoodcuttingFiremaking(),
					new TrainCrafting(),
					new TrainHerblore(),
					new TrainMagic(),
					new TrainSlayer(),
					new TrainRanged(),
					new TrainPrayer(),
					new TrainMelee(),
					new TrainAgility());
    }

    
    @Override
    public int onLoop()
    {
       
        return tree.onLoop();
    }
    
    // Our paint info
    // Add new lines to the paint here
    public static String paint_task = "";
    public static String paint_itemsCount = "";
    public static String paint_subTask = "";
    public static String paint_levels = "";
    public static void clearCustomPaintText()
    {
    	paint_task = "";
    	paint_itemsCount = "";
    	paint_subTask = "";
    	paint_levels = "";
    }
    @Override
    public String[] getPaintInfo()
    {
    	return new String[] {
    			getManifest().name() +" "+ getManifest().version() + " by Dreambotter420 ^_^",
                "Current activity: " + API.currentLeaf,
                "Time spent on current task: " + (DecisionLeaf.taskTimer == null ? "N/A" : DecisionLeaf.taskTimer.formatTime()),
                "Time remaining until next force task switch: " + (DecisionLeaf.taskTimer == null ? "N/A" : Timer.formatTime(DecisionLeaf.taskTimer.remaining())),
				paint_task,
				paint_subTask,
				paint_itemsCount,
				paint_levels
        };
    }
   
    // Instantiate the paint object. This can be customized to your liking.
    private final CustomPaint CUSTOM_PAINT = new CustomPaint(this,
            CustomPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN,
            new Color[] {new Color(255, 251, 255)},
            "Impact",
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
    public static long lastClickMillis = 0;
    
    @Override
    public void onMessage(Message msg)
    {
    	ReadMessage.readMessage(msg);
    }
}
