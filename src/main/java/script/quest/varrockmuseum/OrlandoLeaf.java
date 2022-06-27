package script.quest.varrockmuseum;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.Unobfuscated;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import script.framework.Leaf;

@Unobfuscated
public class OrlandoLeaf extends Leaf {
    @Override
    public boolean isValid() {
        return Museum.getSettingValue() < 2 || Display.allCompleted();
    }


    private final Area museumArea = new Area(1725, 4991, 1793, 4928);
    private final Tile stairTile = new Tile(3255, 3451, 0);

    @Override
    public int onLoop() {
        if (museumArea.contains(Players.localPlayer())) {
            if (Dialogues.inDialogue()) {
                DialogueHandler.solve("Sure thing.");
                return Timing.sleepLogNormalSleep();
            }

            NPC orlando = Museum.getOrlando();
            if (orlando != null && orlando.distance() < 6 && orlando.interact("Talk-to")) {
                MethodProvider.sleepUntil(Dialogues::inDialogue, 2000 + Timing.sleepLogNormalInteraction());
                return Timing.sleepLogNormalSleep();
            }

            if (Walking.shouldWalk(6)) {
                Walking.walk(Museum.orlandoTile);
            }
            return Timing.sleepLogNormalSleep();
        }

        GameObject gameObject = GameObjects.closest(g -> g.getID() == 24428 && g.getTile().equals(stairTile));
        if (gameObject != null && gameObject.distance() < 8 && gameObject.interact("Walk-down")) {
            MethodProvider.sleepUntil(() -> museumArea.contains(Players.localPlayer()), 1000 + Timing.sleepLogNormalInteraction());
            return Timing.sleepLogNormalSleep();
        }

        WalkHandler.walkTo(6, stairTile);
        return Timing.sleepLogNormalSleep();
    }
}
