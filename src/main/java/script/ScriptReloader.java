package script;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;

public class ScriptReloader implements Runnable{
	
	long sleep;

    /**
     * Creates a new ScriptReloader runnable with specified wait for thread
     * @param sleep2
     */
	public ScriptReloader (long sleep2)
	{
		this.sleep = sleep2;
	}
	
    @Override
    public void run() {
    	ScriptManager manager = ScriptManager.getScriptManager();
        manager.pause();
        try {

            Thread.sleep(sleep);
        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }
        manager.resume();
        Logger.log("ScriptPauser Thread stopping!");
    }
}