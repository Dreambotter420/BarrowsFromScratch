package script.quest.varrockmuseum;

import org.dreambot.api.methods.map.Area;

import java.util.Arrays;

public enum Location {
    NORTH(new Area(1736, 4986, 1782, 4975)),
    WEST(new Area(1732, 4971, 1747, 4949)),
    SOUTH(new Area(1771, 4934, 1747, 4945)),
    EAST(new Area(1771, 4971, 1786, 4949));

    public Area area;

    Location(Area area) {
        this.area = area;
    }

    public boolean isLocationCompleted() {
        return Arrays.stream(Display.values()).filter(d -> area.contains(d.getTile())).allMatch(Display::isCompleted);
    }
}
