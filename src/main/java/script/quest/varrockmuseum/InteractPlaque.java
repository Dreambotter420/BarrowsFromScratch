package script.quest.varrockmuseum;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.script.Unobfuscated;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;

import script.framework.Leaf;
import script.utilities.Sleepz;

@Unobfuscated
public class InteractPlaque extends Leaf {
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public int onLoop() {
        if (Dialogues.inDialogue() && Dialogues.continueDialogue()) {
            return Sleepz.interactionTiming();
        }

        Location location = getLocation();
        if (location != null) {
            Display display = getDisplay(location);
            if (display != null) {
                GameObject plaque = GameObjects.getTopObjectOnTile(display.getTile());
                if (plaque != null && plaque.distance() < 6) {
                    if (plaque.interact("Study")) {
                        Sleep.sleepUntil(() -> Museum.getQuizParent() != null && Museum.getQuizParent().isVisible(), 2500 + Sleepz.interactionTiming());
                    }
                    return Sleepz.sleepTiming();
                }

                WalkHandler.walkTo(6, display.getTile());
                return Sleepz.sleepTiming();
            }

            WalkHandler.walkTo(6, location.area.getCenter());
        }
        return Sleepz.sleepTiming();
    }

    public static Location getLocation() {
        return Museum.completionOrder.keySet().stream().filter(l -> !l.isLocationCompleted()).findFirst().orElse(null);
    }

    public static Display getDisplay(Location location) {
        return Museum.completionOrder.get(location).stream().filter(d -> !d.isCompleted()).findFirst().orElse(null);
    }
}
