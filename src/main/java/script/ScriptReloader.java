package script;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;

public class ScriptReloader implements Runnable{
	
	long sleep;
	
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
        MethodProvider.log("ScriptPauser Thread stopping!");
    }
}