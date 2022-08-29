package script.utilities;

import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;

public class Shopz {
	public static boolean close()
	{
		if(!Shop.isOpen()) return true;
		if(UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE) && Keyboard.closeInterfaceWithESC()) Sleep.sleep(696,420);
		else
		{
			WidgetChild closeButton = Widgets.getWidgetChild(300, 1 , 11);
			if(closeButton == null || !closeButton.isVisible()) return false;
			if(closeButton.interact("Close")) Sleep.sleep(420,696);
		}
		return false;
	}
}
