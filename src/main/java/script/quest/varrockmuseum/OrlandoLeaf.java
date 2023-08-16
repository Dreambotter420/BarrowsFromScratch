package script.quest.varrockmuseum;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.Unobfuscated;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import script.utilities.Sleepz;
import script.framework.Leaf;
import script.utilities.Locations;

@Unobfuscated
public class OrlandoLeaf extends Leaf {
    @Override
    public boolean isValid() {
        return Museum.getSettingValue() < 2 || Display.allCompleted() || 
        		!Locations.museumArea.contains(Players.getLocal());
    }


    private final Area museumArea = new Area(1725, 4991, 1793, 4928);
    private final Tile stairTile = new Tile(3255, 3451, 0);

    @Override
    public int onLoop() {
        if (museumArea.contains(Players.getLocal())) {
            if (Dialogues.inDialogue()) {
                DialogueHandler.solve("Sure thing.");
                return Sleepz.sleepTiming();
            }

            NPC orlando = Museum.getOrlando();
            if (orlando != null && orlando.distance() < 6 && orlando.interact("Talk-to")) {
                Sleep.sleepUntil(Dialogues::inDialogue, 2000 + Sleepz.interactionTiming());
                return Sleepz.sleepTiming();
            }

            if (Walking.shouldWalk(6)) {
                Walking.walk(Museum.orlandoTile);
            }
            return Sleepz.sleepTiming();
        }

        GameObject gameObject = GameObjects.closest(g -> g.getID() == 24428 && g.getTile().equals(stairTile));
        if (gameObject != null && gameObject.distance() < 8 && gameObject.interact("Walk-down")) {
            Sleep.sleepUntil(() -> museumArea.contains(Players.getLocal()), 1000 + Sleepz.interactionTiming());
            return Sleepz.sleepTiming();
        }

        WalkHandler.walkTo(6, stairTile);
        return Sleepz.sleepTiming();
    }
}
