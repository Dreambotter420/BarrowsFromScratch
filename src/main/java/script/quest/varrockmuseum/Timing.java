package script.quest.varrockmuseum;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;


public class Timing {

    public static double playerHashLogNormal() {
        return Calculations.nextLogNormalDistributionRandom(Client.seededRandom() / 4.0D, Client.seededRandom() / 3.3D);
    }

    public static int sleepLogNormalInteraction() {
        return (int) (1000.0D * playerHashLogNormal());
    }

    public static int sleepLogNormalSleep() {
        return (int) (250.0D * playerHashLogNormal());
    }

}
