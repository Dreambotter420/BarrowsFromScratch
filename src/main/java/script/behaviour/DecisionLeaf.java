package script.behaviour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Timer;

import script.framework.Leaf;
import script.quest.animalmagnetism.AnimalMagnetism;
import script.quest.ernestthechicken.ErnestTheChicken;
import script.quest.magearena1.MageArena1;
import script.quest.magearena2.MageArena2;
import script.quest.naturespirit.NatureSpirit;
import script.quest.priestinperil.PriestInPeril;
import script.quest.restlessghost.RestlessGhost;
import script.quest.varrockmuseum.VarrockQuiz;
import script.utilities.API;

public class DecisionLeaf extends Leaf{

	@Override
	public boolean isValid() {
		return API.mode == null;
	}

	public static int wcSetpoint;
    public static int mageSetpoint;
    public static int rangeSetpoint;
    public static int craftingSetpoint;
    public static int prayerSetpoint;
    public static int slayerSetpoint;
    public static Timer taskTimer;
    public static Timer forceBreakTimer;
    
    
    public static void resetForceBreakTimer()
    {
    	forceBreakTimer = new Timer((int)Calculations.nextGaussianRandom(12000000, 200000));
    }
    
    /**
     * sets a timer for random length.
     * enter 1 for short,
     * 2 for medium,
     * 3 for long.
     * Most likely will choose the chosen timer. But a chance to choose others.
     */
    public static void setTimer (int priorityTime)
    {
    	switch(priorityTime) {
    	case(3):
    	{
    		int rand = (int) Calculations.nextGaussianRandom(50, 20);
    		int timer = 0;
    		if(rand < 30) timer = (int)Calculations.nextGaussianRandom(1200000, 100000);
    		else if (rand < 38) timer = (int)Calculations.nextGaussianRandom(3000000, 100000);
    		else timer = (int)Calculations.nextGaussianRandom(6000000, 200000);
    		taskTimer = new Timer(timer);
    		MethodProvider.log("Set timer for: " + ((double)timer / 60000) +" minutes");
    		break;
    	}
    	case(2):
    	{
    		int rand = (int) Calculations.nextGaussianRandom(50, 20);
    		int timer = 0;
    		if(rand < 30) timer = (int)Calculations.nextGaussianRandom(6000000, 100000);
    		else if (rand < 38) timer = (int)Calculations.nextGaussianRandom(1200000, 100000);
    		else timer = (int)Calculations.nextGaussianRandom(3000000, 200000);
    		taskTimer = new Timer(timer);
    		MethodProvider.log("Set timer for: " + ((double)timer / 60000) +" minutes");
    		break;
    	}
    	case(1):
    	{
    		int rand = (int) Calculations.nextGaussianRandom(50, 20);
    		int timer = 0;
    		if(rand < 30) timer = (int)Calculations.nextGaussianRandom(6000000, 100000);
    		else if (rand < 38) timer = (int)Calculations.nextGaussianRandom(3000000, 100000);
    		else timer = (int)Calculations.nextGaussianRandom(1200000, 200000);
    		taskTimer = new Timer(timer);
    		MethodProvider.log("Set timer for: " + ((double)timer / 60000) +" minutes");
    		break;
    	}
    	default:{
    		MethodProvider.log("Whoops - enter 1,2,3 into setTimer function! :-)");
    		break;
    	}}
    }
    
    public static boolean setPoints = false;
    
	@Override
	public int onLoop() {
		if(!setPoints) initialize();
		final int ranged = Skills.getRealLevel(Skill.RANGED);
		final int wc = Skills.getRealLevel(Skill.WOODCUTTING);
		final int mage = Skills.getRealLevel(Skill.MAGIC);
		final int crafting = Skills.getRealLevel(Skill.CRAFTING);
		final int slayer = Skills.getRealLevel(Skill.SLAYER);
		final int prayer = Skills.getRealLevel(Skill.PRAYER);
		final boolean ernestDone = ErnestTheChicken.completedErnestTheChikken;
		final boolean animalMagnetized = AnimalMagnetism.completedAnimalMagnetism;
		final boolean natureSpirited = NatureSpirit.completedNatureSpirit;
		final boolean mageArena1Done = MageArena1.completedMageArena1;
		final boolean mageArena2Done = MageArena2.completedMageArena2;
		final boolean priestSaved = PriestInPeril.completedPriestInPeril;
		final boolean restedGhost = RestlessGhost.completedTheRestlessGhost;
		final boolean quizzed = VarrockQuiz.completedQuiz;
		List<API.modes> validModes = new ArrayList<API.modes>();
		
		if(priestSaved)
		{
			if(ranged < rangeSetpoint) validModes.add(API.modes.TRAIN_RANGE);
			if(mage < mageSetpoint) validModes.add(API.modes.TRAIN_MAGIC);
			if(prayer < prayerSetpoint) validModes.add(API.modes.TRAIN_PRAYER);
			if(mage >= 60 && !mageArena1Done) validModes.add(API.modes.MAGE_ARENA_1);
			if(mage >= 75 && !mageArena2Done) validModes.add(API.modes.MAGE_ARENA_2);
			if(!animalMagnetized) validModes.add(API.modes.ANIMAL_MAGNETISM);
			if(!natureSpirited) validModes.add(API.modes.NATURE_SPIRIT);
		}
		else if(slayer >= slayerSetpoint && ranged >= 30 &&
				wc >= wcSetpoint && crafting >= craftingSetpoint && 
				ernestDone && restedGhost)
		{
			if(ranged < rangeSetpoint) validModes.add(API.modes.TRAIN_RANGE);
			if(mage < mageSetpoint) validModes.add(API.modes.TRAIN_MAGIC);
			if(prayer < prayerSetpoint) validModes.add(API.modes.TRAIN_PRAYER);
			if(mage >= 60 && !mageArena1Done) validModes.add(API.modes.MAGE_ARENA_1);
			if(mage >= 75 && !mageArena2Done) validModes.add(API.modes.MAGE_ARENA_2);
			if(!priestSaved) validModes.add(API.modes.PRIEST_IN_PERIL);
		}
		else if(slayer >= slayerSetpoint && ranged >= 30)
		{
			if(wc < wcSetpoint) validModes.add(API.modes.TRAIN_WOODCUTTING);
			if(ranged < rangeSetpoint) validModes.add(API.modes.TRAIN_RANGE);
			if(mage < mageSetpoint) validModes.add(API.modes.TRAIN_MAGIC);
			if(crafting < craftingSetpoint) validModes.add(API.modes.TRAIN_CRAFTING);
			if(prayer < prayerSetpoint) validModes.add(API.modes.TRAIN_PRAYER);
			if(mage >= 60 && !mageArena1Done) validModes.add(API.modes.MAGE_ARENA_1);
			if(mage >= 75 && !mageArena2Done) validModes.add(API.modes.MAGE_ARENA_2);
			if(!ernestDone) validModes.add(API.modes.ERNEST_THE_CHIKKEN);
			if(!restedGhost) validModes.add(API.modes.RESTLESS_GHOST);
		}
		else if(slayer < slayerSetpoint && slayer >= 9 && ranged >= 30)
		{
			if(wc < wcSetpoint) validModes.add(API.modes.TRAIN_WOODCUTTING);
			if(ranged < rangeSetpoint) validModes.add(API.modes.TRAIN_RANGE);
			if(mage < mageSetpoint) validModes.add(API.modes.TRAIN_MAGIC);
			if(crafting < craftingSetpoint) validModes.add(API.modes.TRAIN_CRAFTING);
			if(prayer < prayerSetpoint) validModes.add(API.modes.TRAIN_PRAYER);
			if(mage >= 60 && !mageArena1Done) validModes.add(API.modes.MAGE_ARENA_1);
			if(mage >= 75 && !mageArena2Done) validModes.add(API.modes.MAGE_ARENA_2);
			if(!ernestDone) validModes.add(API.modes.ERNEST_THE_CHIKKEN);
			if(!restedGhost) validModes.add(API.modes.RESTLESS_GHOST);
			validModes.add(API.modes.TRAIN_SLAYER);
		}
		else if(slayer < 9 || ranged < 30)
		{
			if(wc < wcSetpoint) validModes.add(API.modes.TRAIN_WOODCUTTING);
			if(ranged < rangeSetpoint) validModes.add(API.modes.TRAIN_RANGE);
			if(mage < mageSetpoint) validModes.add(API.modes.TRAIN_MAGIC);
			if(crafting < craftingSetpoint) validModes.add(API.modes.TRAIN_CRAFTING);
			if(prayer < prayerSetpoint) validModes.add(API.modes.TRAIN_PRAYER);
			if(mage >= 60 && !mageArena1Done) validModes.add(API.modes.MAGE_ARENA_1);
			if(mage >= 75 && !mageArena2Done) validModes.add(API.modes.MAGE_ARENA_2);
			if(!ernestDone) validModes.add(API.modes.ERNEST_THE_CHIKKEN);
			if(!restedGhost) validModes.add(API.modes.RESTLESS_GHOST);
			if(!quizzed) validModes.add(API.modes.VARROCK_QUIZ);
		}
		
		if(validModes.isEmpty()) MethodProvider.log("Congratulations, you win!");
		else
		{
			if(Calculations.random(1,100) > 95 || forceBreakTimer.finished()) API.mode = API.modes.BREAK;
			else 
			{
				Collections.shuffle(validModes);
				API.mode = validModes.get(0);
			}
			MethodProvider.log("Switching mode: " + API.mode.toString());
			if(API.mode == API.modes.ANIMAL_MAGNETISM || 
					API.mode == API.modes.ERNEST_THE_CHIKKEN || 
					API.mode == API.modes.MAGE_ARENA_1 || 
					API.mode == API.modes.MAGE_ARENA_2 || 
					API.mode == API.modes.NATURE_SPIRIT || 
					API.mode == API.modes.RESTLESS_GHOST || 
					API.mode == API.modes.VARROCK_QUIZ || 
					API.mode == API.modes.PRIEST_IN_PERIL)
			{
				setTimer(3);
			}
			else if(API.mode == API.modes.TRAIN_MAGIC || 
					API.mode == API.modes.TRAIN_SLAYER || 
					API.mode == API.modes.TRAIN_RANGE)
			{
				setTimer(2);
			}
			else if(API.mode == API.modes.TRAIN_CRAFTING || 
					API.mode == API.modes.TRAIN_WOODCUTTING || 
					API.mode == API.modes.TRAIN_PRAYER)
			{
				setTimer(1);
			}
			else if(API.mode == API.modes.BREAK)
			{
				setTimer(2);
				resetForceBreakTimer();
			}
		}
		return 10;
	}
	public static void initialize()
	{
		resetForceBreakTimer();
		
		int tmp =(int) Calculations.nextGaussianRandom(21, 3);
		if(tmp >= 19 && tmp <= 24) craftingSetpoint = tmp;
		else craftingSetpoint = 19;
		
		tmp =(int) Calculations.nextGaussianRandom(20, 3);
		if(tmp >= 18 && tmp <= 21) slayerSetpoint = tmp;
		else slayerSetpoint = 18;

		tmp =(int) Calculations.nextGaussianRandom(38, 3);
		if(tmp >= 35 && tmp <= 40) wcSetpoint = tmp;
		else wcSetpoint = 35;

		tmp =(int) Calculations.nextGaussianRandom(77, 3);
		if(tmp >= 75 && tmp <= 80) rangeSetpoint = tmp;
		else rangeSetpoint = 75;

		tmp =(int) Calculations.nextGaussianRandom(83, 3);
		if(tmp >= 83 && tmp <= 85) mageSetpoint = tmp;
		else mageSetpoint = 83;
		
		tmp =(int) Calculations.nextGaussianRandom(47, 3);
		if(tmp >= 45 && tmp <= 50) mageSetpoint = tmp;
		else mageSetpoint = 83;
		setPoints = true;
		
		
	}
	
}
