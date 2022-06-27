package script;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.ScriptManager;

public class ScriptReloader implements Runnable{
	
	int sleep;
	
	public ScriptReloader (int sleep)
	{
		this.sleep = sleep;
	}
	
    @Override
    public void run() {
        ScriptManager manager = ScriptManager.getScriptManager();
        manager.pause();
        try {
            Thread.sleep(this.sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        manager.resume();
        MethodProvider.log("ScriptPauser Thread stopping!");
    }
}