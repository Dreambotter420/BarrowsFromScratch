package script.quest.varrockmuseum;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.settings.PlayerSettings;

import script.framework.Leaf;
import script.framework.Tree;
import script.utilities.API;

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
    public static void onStart() {
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
        if (PlayerSettings.getBitValue(3688) == 1) {
            MethodProvider.log("[COMPLETED] -> Museum Quiz");
           	completedQuiz = true;
           	API.mode = null;
            return Timing.sleepLogNormalSleep();
        }
        return tree.onLoop();
    }

	@Override
	public boolean isValid() {
		return API.mode == API.modes.VARROCK_QUIZ;
	}
}
