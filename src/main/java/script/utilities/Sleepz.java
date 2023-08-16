package script.utilities;

import java.time.LocalDateTime;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.utilities.Sleep;

public class Sleepz {
	public static LocalDateTime dt;
	public static double initSleepMod;
	public static void sleep(int min, int max) {
		/**
		 * I was high on marijuana when I plotted these points onto a function on Desmos
		 * the point is to modify the delay modifier between 0.00 and 0.99 times the initial set modifier
		 * and this new dynamic modifier changes over time - all bots change speed over time the same, applied to initial modifier
		 * based on current minute of the hour (0-59)
		 */
		Sleep.sleep(calculate(min,max));
		return;
	}
	public static int calculate(int min, int max) {
		int minute = dt.getMinute();
		double sleepMod = initSleepMod * ((Math.sin((4 / 14) * (minute+1)) + 2 - 
				((1 / 2) * Math.sin(((4  / 25) * (minute+1) * (minute+1)) - 30))) / 4);
		return (int) ((double) min + API.rand2.nextInt(max) * sleepMod);
		
	}

    public static int interactionTiming() {
        return (int) (1000.0D * Timing.playerHashLogNormal());
    }

    public static int sleepTiming() {
        return (int) (250.0D * Timing.playerHashLogNormal());
    }

    public static void sleepInteraction() {
        Sleep.sleep((int) (1000.0D * Timing.playerHashLogNormal()));
    }

    public static void sleep() {
        Sleep.sleep((int) (250.0D * Timing.playerHashLogNormal()));
    }

    public static boolean sleep(boolean interaction) {
        if (interaction) {
            sleepInteraction();
        } else {
            sleep();
        }
        return true;
    }
}
