package script.quest.varrockmuseum;

import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.*;
import java.util.stream.Collectors;

public class Museum {

    public static final Tile orlandoTile = new Tile(1759, 4955, 0);

    public static int getSettingValue() {
        return PlayerSettings.getConfig(1014);
    }

    public static NPC getOrlando() {
        return NPCs.closest(npc -> npc != null && npc.getName().contains("Orlando Smith"));
    }

    public static Widget getQuizParent() {
        return Widgets.getWidget(533);
    }

    public static LinkedHashMap<Location, List<Display>> completionOrder;

    public static List<Location> getShuffledLocations() {
        List<Location> locations = Arrays.asList(Location.values());
        Collections.shuffle(locations);
        return locations;
    }

    public static List<Display> getShuffledDisplays(Location location) {
        List<Display> displays = Arrays.stream(Display.values()).filter(d -> location.area.contains(d.getTile())).collect(Collectors.toList());
        Collections.shuffle(displays);
        return displays;
    }

}
