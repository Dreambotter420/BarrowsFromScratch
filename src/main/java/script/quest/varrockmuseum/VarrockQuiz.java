package script.quest.varrockmuseum;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.wrappers.interactive.GameObject;

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
	public static boolean completedQuiz = false;
	private final static Tile stairTile = new Tile(1759,4958,0);
    public static int onStart() {
        MethodProvider.log("Generating Random Pattern: ");
        Museum.completionOrder = new LinkedHashMap<Location, List<Display>>(){{
            Museum.getShuffledLocations().forEach(location -> {
                List<Display> displays = Museum.getShuffledDisplays(location);
                StringBuilder builder = new StringBuilder();
                displays.forEach(d -> builder.append(", ").append(d.toString()));
                MethodProvider.log(location + builder.toString());
                put(location, displays);
            });
        }};
        
        instantiateTree();
        started = true;
        return 10;
    }
    public static boolean onExit()
    {
    	if(Locations.museumArea.contains(Players.localPlayer()))
    	{
    		 GameObject gameObject = GameObjects.closest(g -> g.getID() == 24428 && g.getTile().equals(stairTile));
    	        if (gameObject != null && gameObject.distance() < 8 && gameObject.interact("Walk-up")) {
    	            MethodProvider.sleepUntil(() -> Locations.museumArea.contains(Players.localPlayer()), 1000 + Timing.sleepLogNormalInteraction());
    	            MethodProvider.sleep(Timing.sleepLogNormalSleep());
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
    
    
    @Override
    public int onLoop() {
        if(!started) return onStart();
    	if (PlayerSettings.getBitValue(3688) == 1) {
    		if(onExit()) //returns true if out of museum dungeon
    		{
    			 MethodProvider.log("[COMPLETED] -> Museum Quiz");
    	         completedQuiz = true;
    	         API.mode = null;
    		}
            
            return Timing.sleepLogNormalSleep();
        }
    	
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.VARROCK_QUIZ;
	}
}
