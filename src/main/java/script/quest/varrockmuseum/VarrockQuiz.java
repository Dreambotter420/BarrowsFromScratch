package script.quest.varrockmuseum;

import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;

import script.Main;
import script.utilities.Sleepz;
import script.framework.Leaf;
import script.framework.Tree;
import script.utilities.API;
import script.utilities.Locations;

import java.util.LinkedHashMap;
import java.util.List;
/**
 * Completes the Varrock Museum Quiz
 * @author LostVirt
 * copied+pasted by Dreambotter420 ^_^
 */
public class VarrockQuiz extends Leaf {
	public static boolean started = false;
	private final static Tile stairTile = new Tile(1759,4958,0);
    public static int onStart() {
        Logger.log("Generating Random Pattern: ");
        Museum.completionOrder = new LinkedHashMap<Location, List<Display>>(){{
            Museum.getShuffledLocations().forEach(location -> {
                List<Display> displays = Museum.getShuffledDisplays(location);
                StringBuilder builder = new StringBuilder();
                displays.forEach(d -> builder.append(", ").append(d.toString()));
                Logger.log(location + builder.toString());
                put(location, displays);
            });
        }};
        
        instantiateTree();
        started = true;
        return 10;
    }
    public static boolean onExit()
    {
    	if(Locations.museumArea.contains(Players.getLocal()))
    	{
    		 GameObject gameObject = GameObjects.closest(g -> g.getName().contains("Stairs") && g.hasAction("Walk-up"));
    	     if (gameObject != null && gameObject.distance() < 8 && gameObject.interact("Walk-up")) 
    	     {
    	    	 Sleep.sleepUntil(() -> Locations.museumArea.contains(Players.getLocal()), (1000 + Sleepz.interactionTiming()));
    	    	 Sleepz.sleep();
    	    	 return false;
    	     }
    	     WalkHandler.walkTo(6, stairTile);
    	}
    	else 
    	{
    		return true;
    	}
    	return false;
    }
    
    private final static Tree tree = new Tree();
    private static void instantiateTree() {
        tree.addBranches(
                new OrlandoLeaf(),
                new MuseumBranch().addLeafs(
                        new Solve(),
                        new InteractPlaque()
                )
        );
    }
    
    public static boolean completed()
    {
    	return PlayerSettings.getBitValue(3688) == 1;
    }
    @Override
    public int onLoop() {
        if(!started) return onStart();
    	if (completed()) {
    		if(onExit()) //returns true if out of museum dungeon
    		{
    			Logger.log("[COMPLETED] -> Museum Quiz");
    			Main.paint_task = "~~~~~~~~~~";
         		Main.paint_itemsCount = "~Quest Complete~";
         		Main.paint_subTask = "~Varrock Museum Quiz~";
         		Main.paint_levels = "~~~~~~~~~~";
    	        API.mode = null;
    		}
            return Sleepz.sleepTiming();
        }
    	
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.VARROCK_QUIZ;
	}
}
