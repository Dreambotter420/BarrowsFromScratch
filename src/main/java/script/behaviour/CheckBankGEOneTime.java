package script.behaviour;

import org.dreambot.api.methods.settings.PlayerSettings;

import script.framework.Branch;
import script.framework.Leaf;


public class CheckBankGEOneTime extends Leaf
{
	public static boolean checkedBank = false;
	public static boolean checkedGE = false;
	
    @Override
    public boolean isValid() {
    	return checkedBank && checkedGE;
    }

	@Override
	public int onLoop() {
		// TODO Auto-generated method stub
		return 0;
	}

}
