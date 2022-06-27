package script.quest.varrockmuseum;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;

public class WalkHandler {

    public static void walkTo(int distance, Tile tile) {
        if (Walking.shouldWalk(distance)) {
            Walking.walk(tile);
        }
    }

}
