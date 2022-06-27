package script.quest.varrockmuseum;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;

import java.util.Arrays;

public enum Display {
    LIZARD(3675, new Tile(1743, 4978, 0)),
    TORTOISE(3680, new Tile(1753, 4978, 0)),
    DRAGON(3672, new Tile(1768, 4978, 0)),
    WYVERN(3681, new Tile(1778, 4978, 0)),
    SNAIL(3674, new Tile(1776, 4963, 0)),
    SNAKE(3677, new Tile(1783, 4963, 0)),
    SLUG(3682, new Tile(1781, 4957, 0)),
    MONKEY(3676, new Tile(1774, 4957, 0)),
    KALPHITE(3684, new Tile(1762, 4938, 0)),
    TERRORBIRD(3683, new Tile(1755, 4940, 0)),
    PENGUIN(3673, new Tile(1742, 4957, 0)),
    MOLE(3678, new Tile(1735, 4957, 0)),
    CAMEL(3679, new Tile(1737, 4963, 0)),
    LEECH(3685, new Tile(1744, 4963, 0));

    private final int varbit;
    private final Tile tile;

    Display(int varbit, Tile tile) {
        this.varbit = varbit;
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isCompleted() {
        return PlayerSettings.getBitValue(varbit) == 3;
    }

    public static boolean allCompleted() {
        return Arrays.stream(values()).allMatch(Display::isCompleted);
    }

}
