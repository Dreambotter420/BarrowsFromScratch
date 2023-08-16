package script.utilities;

import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.utilities.Sleep;

public class Tabz {
	//Just encompasses both isOpen and open call in one
	public static boolean open(Tab tab) {
		if(Tabs.isOpen(tab)) return true;
		return Tabs.openWithFKey(tab);
	}
}
