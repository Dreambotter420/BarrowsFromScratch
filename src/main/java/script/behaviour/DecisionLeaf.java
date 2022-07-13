package script.behaviour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;

import script.framework.Leaf;
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
import script.utilities.API;
import script.utilities.API.modes;

public class DecisionLeaf extends Leaf{

	@Override
	public boolean isValid() {
		return API.mode == null;
	}

	public static int wcSetpoint;
    public static int mageSetpoint;
    public static int attSetpoint;
    public static int strSetpoint;
    public static int rangeSetpoint;
    public static int craftingSetpoint;
    public static int prayerSetpoint;
    public static int agilitySetpoint;
    public static int slayerSetpoint = 18;
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
		final int str = Skills.getRealLevel(Skill.STRENGTH);
		final int att = Skills.getRealLevel(Skill.ATTACK);
		final int crafting = Skills.getRealLevel(Skill.CRAFTING);
		final int slayer = Skills.getRealLevel(Skill.SLAYER);
		final int prayer = Skills.getRealLevel(Skill.PRAYER);
		final int def = Skills.getRealLevel(Skill.DEFENCE);
		final int agility = Skills.getRealLevel(Skill.AGILITY);
		final boolean waterfallDone = WaterfallQuest.completedWaterfallQuest;
		final boolean fightArenaDone = FightArena.completedFightArena;
		final boolean fremmyTrialsDone = FremennikTrials.completedFremennikTrials;
		final boolean horrorDone = HorrorFromTheDeep.completedHorrorFromTheDeep;
		final boolean ernestDone = ErnestTheChicken.completedErnestTheChikken;
		final boolean animalMagnetized = AnimalMagnetism.completedAnimalMagnetism;
		final boolean natureSpirited = NatureSpirit.completedNatureSpirit;
		final boolean mageArena1Done = MageArena1.completedMageArena1;
		final boolean mageArena2Done = MageArena2.completedMageArena2;
		final boolean priestSaved = PriestInPeril.completedPriestInPeril;
		final boolean restedGhost = RestlessGhost.completedTheRestlessGhost;
		final boolean quizzed = VarrockQuiz.completedQuiz;
		List<API.modes> validModes = new ArrayList<API.modes>();
		
		if(!natureSpirited)
		{
			if(slayer >= slayerSetpoint && priestSaved && restedGhost && ranged >= 30 && !validModes.contains(modes.NATURE_SPIRIT)) validModes.add(modes.NATURE_SPIRIT);
			else if(slayer < slayerSetpoint)
			{
				if(!quizzed && !validModes.contains(modes.VARROCK_QUIZ)) validModes.add(modes.VARROCK_QUIZ);
				else {
					if(!priestSaved) {
						if(ranged >= 30 && !validModes.contains(modes.TRAIN_SLAYER)) validModes.add(modes.TRAIN_SLAYER);
						else if(!validModes.contains(modes.TRAIN_RANGE)) validModes.add(modes.TRAIN_RANGE);
					}
					else {
						MethodProvider.log("Somehow the account has unlocked Morytania with Priest in Peril without");
						MethodProvider.log("getting 18 slayer! Need to get 18 slayer manually, good luck with morytania tasks.");
						return -1;
					}
				}
			} else { // slayer past point!! Can do priest in peril now :-)
				if(!priestSaved && !validModes.contains(modes.PRIEST_IN_PERIL)) validModes.add(modes.PRIEST_IN_PERIL);
				if(!restedGhost && !validModes.contains(modes.RESTLESS_GHOST)) validModes.add(modes.RESTLESS_GHOST);
			}
		}
		
		if(!animalMagnetized)
		{
			if(slayer < slayerSetpoint) {
				if(!quizzed && !validModes.contains(modes.VARROCK_QUIZ)) validModes.add(modes.VARROCK_QUIZ);
				else {
					if(!priestSaved) {
						if(ranged >= 50 && !validModes.contains(modes.TRAIN_SLAYER)) validModes.add(modes.TRAIN_SLAYER);
						else if(!validModes.contains(modes.TRAIN_RANGE)) validModes.add(modes.TRAIN_RANGE);
					}
					else 
					{
						MethodProvider.log("Somehow the account has unlocked Morytania with Priest in Peril without");
						MethodProvider.log("getting 18 slayer! Need to get 18 slayer manually, good luck with morytania tasks.");
						return -1;
					}
				}
			} else { // slayer past point!! Can do priest in peril now :-)
				if(!priestSaved && !validModes.contains(modes.PRIEST_IN_PERIL)) validModes.add(modes.PRIEST_IN_PERIL);
			}
			if(ranged < 50 && !validModes.contains(modes.TRAIN_RANGE)) validModes.add(modes.TRAIN_RANGE);
			if(wc < wcSetpoint && !validModes.contains(modes.TRAIN_WOODCUTTING)) validModes.add(modes.TRAIN_WOODCUTTING);
			if(crafting < craftingSetpoint  && !validModes.contains(modes.TRAIN_CRAFTING)) validModes.add(modes.TRAIN_CRAFTING);
			if(!ernestDone && !validModes.contains(modes.ERNEST_THE_CHIKKEN)) validModes.add(modes.ERNEST_THE_CHIKKEN);
			if(!restedGhost && !validModes.contains(modes.RESTLESS_GHOST)) validModes.add(modes.RESTLESS_GHOST);
		}
		
		if(!horrorDone)
		{
			if(ranged >= 46 && mage >= 35 && agility >= agilitySetpoint && !validModes.contains(modes.HORROR_FROM_THE_DEEP)) validModes.add(modes.HORROR_FROM_THE_DEEP);
			if(ranged < 46 && !validModes.contains(modes.TRAIN_RANGE)) validModes.add(modes.TRAIN_RANGE);
			if(mage < 35 && !validModes.contains(modes.TRAIN_MAGIC)) validModes.add(modes.TRAIN_MAGIC);
			if(agility < agilitySetpoint && !validModes.contains(modes.TRAIN_AGILITY)) validModes.add(modes.TRAIN_AGILITY);
		}
		
		if(!mageArena2Done)
		{
			if(mageArena1Done && prayer >= prayerSetpoint && mage >= 75 && def >= 40 && ranged >= 70 && !validModes.contains(modes.MAGE_ARENA_2)) validModes.add(modes.MAGE_ARENA_2);
			if(!mageArena1Done)
			{
				if(mage >= 60 && prayer >= prayerSetpoint && !validModes.contains(modes.MAGE_ARENA_1)) validModes.add(modes.MAGE_ARENA_1);
				if(mage < 60 && !validModes.contains(modes.TRAIN_MAGIC)) validModes.add(modes.TRAIN_MAGIC);
				if(prayer < prayerSetpoint && !validModes.contains(modes.TRAIN_PRAYER)) validModes.add(modes.TRAIN_PRAYER);
			}
			else //mage arena 1 done
			{
				if(mage < 75 && !validModes.contains(modes.TRAIN_MAGIC)) validModes.add(modes.TRAIN_MAGIC);
				if(prayer < prayerSetpoint && !validModes.contains(modes.TRAIN_PRAYER)) validModes.add(modes.TRAIN_PRAYER);
				if((def < 40 || ranged < 70) && !validModes.contains(modes.TRAIN_RANGE)) validModes.add(modes.TRAIN_RANGE);
			}
		}
		
		if(!fremmyTrialsDone)
		{
			if(fightArenaDone && waterfallDone && prayer >= prayerSetpoint && att >= attSetpoint && str >= strSetpoint && validModes.contains(modes.FREMENNIK_TRIALS)) validModes.add(modes.FREMENNIK_TRIALS);
			if(!fightArenaDone)
			{
				if(waterfallDone && prayer >= prayerSetpoint && !validModes.contains(modes.FIGHT_ARENA)) validModes.add(modes.FIGHT_ARENA);
				if(!waterfallDone && !validModes.contains(modes.WATERFALL_QUEST)) validModes.add(modes.WATERFALL_QUEST);
				if(prayer < prayerSetpoint && validModes.contains(modes.TRAIN_PRAYER)) validModes.add(modes.TRAIN_PRAYER);
			} else {
				if(!waterfallDone && !validModes.contains(modes.WATERFALL_QUEST)) validModes.add(modes.WATERFALL_QUEST);
				if((att < attSetpoint || str < strSetpoint) && !validModes.contains(modes.TRAIN_MELEE)) validModes.add(modes.TRAIN_MELEE);
				if(prayer < prayerSetpoint && !validModes.contains(modes.TRAIN_PRAYER)) validModes.add(modes.TRAIN_PRAYER);
			}
		}
		
		if(!ernestDone && !validModes.contains(modes.ERNEST_THE_CHIKKEN)) validModes.add(modes.ERNEST_THE_CHIKKEN);
		if(!restedGhost && !validModes.contains(modes.RESTLESS_GHOST)) validModes.add(modes.RESTLESS_GHOST);
		if(ranged < rangeSetpoint && !validModes.contains(modes.TRAIN_RANGE)) validModes.add(modes.TRAIN_RANGE);
		if(mage < mageSetpoint && !validModes.contains(modes.TRAIN_MAGIC)) validModes.add(modes.TRAIN_MAGIC);
		if(prayer < prayerSetpoint && !validModes.contains(modes.TRAIN_PRAYER)) validModes.add(modes.TRAIN_PRAYER);
		
		if(validModes.isEmpty()) MethodProvider.log("Congratulations, you win!");
		else
		{
			if(Calculations.random(1,100) > 95 || forceBreakTimer.finished()) API.mode = modes.BREAK;
			else 
			{
				Collections.shuffle(validModes);
				API.mode = validModes.get(0);
			}
			
			//testing
			
			MethodProvider.log("Switching mode: " + API.mode.toString());
			if(API.mode == modes.ANIMAL_MAGNETISM || 
					API.mode == modes.ERNEST_THE_CHIKKEN || 
					API.mode == modes.MAGE_ARENA_1 || 
					API.mode == modes.MAGE_ARENA_2 || 
					API.mode == modes.NATURE_SPIRIT || 
					API.mode == modes.RESTLESS_GHOST || 
					API.mode == modes.VARROCK_QUIZ || 
					API.mode == modes.PRIEST_IN_PERIL || 
					API.mode == modes.HORROR_FROM_THE_DEEP || 
					API.mode == modes.FREMENNIK_TRIALS || 
					API.mode == modes.FIGHT_ARENA || 
					API.mode == modes.WATERFALL_QUEST)
			{
				setTimer(3);
			}
			else if(API.mode == modes.TRAIN_MAGIC || 
					API.mode == modes.TRAIN_SLAYER || 
					API.mode == modes.TRAIN_RANGE || 
					API.mode == modes.TRAIN_MELEE)
			{
				setTimer(2);
			}
			else if(API.mode == modes.TRAIN_CRAFTING || 
					API.mode == modes.TRAIN_WOODCUTTING || 
					API.mode == modes.TRAIN_PRAYER)
			{
				setTimer(1);
			}
			else if(API.mode == modes.BREAK)
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

		tmp =(int) Calculations.nextGaussianRandom(38, 3);
		if(tmp >= 35 && tmp <= 40) wcSetpoint = tmp;
		else wcSetpoint = 35;
		
		tmp =(int) Calculations.nextGaussianRandom(76, 3);
		if(tmp >= 75 && tmp <= 78) rangeSetpoint = tmp;
		else rangeSetpoint = 75;
		
		tmp =(int) Calculations.nextGaussianRandom(50, 5);
		if(tmp >= 45 && tmp <= 55) strSetpoint = tmp;
		else strSetpoint = 45;
		
		tmp =(int) Calculations.nextGaussianRandom(50,5);
		if(tmp >= 45 && tmp <= 55) attSetpoint = tmp;
		else attSetpoint = 45;

		tmp =(int) Calculations.nextGaussianRandom(83, 3);
		if(tmp >= 83 && tmp <= 85) mageSetpoint = tmp;
		else mageSetpoint = 83;
		
		tmp =(int) Calculations.nextGaussianRandom(49, 3);
		if(tmp >= 45 && tmp <= 50) prayerSetpoint = tmp;
		else prayerSetpoint = 45;
		
		tmp =(int) Calculations.nextGaussianRandom(31, 3);
		if(tmp >= 30 && tmp <= 32) agilitySetpoint = tmp;
		else agilitySetpoint = 30;
		setPoints = true;
		
	}
	
}
