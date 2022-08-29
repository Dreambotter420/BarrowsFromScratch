package script.actionz;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Timer;

import script.actionz.UniqueActions.Actionz;
import script.framework.Leaf;
import script.quest.varrockmuseum.Timing;
import script.utilities.Sleep;
import script.utilities.Walkz;


public class WalkDrunkenly extends Leaf
{

    @Override
    public boolean isValid() {
    	return shouldDrunkWalk();
    }

	@Override
	public int onLoop() {
		drunkWalk();
		return Timing.sleepLogNormalSleep();
	}
	public static Timer drunkWalkTimer = null;
	public static int drunkWalksLeft = 0;
	public static boolean shouldDrunkWalk()
	{
		if(!UniqueActions.isActionEnabled(Actionz.WALK_DRUNKENLY)) return false;
		if(drunkWalkTimer == null)
		{
			final int timer = Calculations.random(25000,3600000);
			MethodProvider.log("Performing drunk walk in: " + Timer.formatTime(timer));
			drunkWalkTimer = new Timer(timer); // Timer between 25 seconds and 1 hour to make another drunk walk
			return false;
		}
		if(drunkWalkTimer.finished())
		{
			final int timer = Calculations.random(25000,3600000);
			MethodProvider.log("Resetting drunk walk timer! Performing drunk walk in: " + Timer.formatTime(timer));
			drunkWalkTimer = new Timer(timer); // Timer between 25 seconds and 1 hour to make another drunk walk
			drunkWalkTimer = new Timer(Calculations.random(25000,3600000)); // Timer between 25 seconds and 1 hour to make another drunk walk
			drunkWalksLeft = Calculations.random(0, 3);
			if((int) Calculations.nextGaussianRandom(50, 25) > 60) drunkWalksLeft++;
		}
		return drunkWalksLeft > 0;
	}
	public static void drunkWalk()
	{
		final Tile translatedToDrunk = new Tile(Players.localPlayer().getX() + Calculations.random(-10, 11),
				Players.localPlayer().getY() + Calculations.random(-10, 11),
				Players.localPlayer().getZ());
		if(Walking.walk(translatedToDrunk))
		{
			if(drunkWalksLeft > 0) drunkWalksLeft--;
			int sleep = Sleep.calculate(666, 5555);
			MethodProvider.log("Performed Drunk Walk! ("+drunkWalksLeft+" walks left)... waiting " +sleep+ "ms");
			MethodProvider.sleep(sleep);
		}
	}
}
