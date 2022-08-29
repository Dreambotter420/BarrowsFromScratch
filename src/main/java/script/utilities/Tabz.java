package script.utilities;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;

import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;

public class Tabz {
	public static boolean open(Tab tab)
	{
		if(Tabs.isOpen(tab)) return true;
		if(UniqueActions.isActionEnabled(Actionz.USE_FKEYS)) Tabs.openWithFKey(tab);
		else Tabs.openWithMouse(tab);
		MethodProvider.sleepUntil(() -> Tabs.isOpen(tab),Sleep.calculate(2222,2222));
		return Tabs.isOpen(tab);
	}
}
