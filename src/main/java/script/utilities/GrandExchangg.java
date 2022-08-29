package script.utilities;

import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.widget.Widgets;

import script.actionz.UniqueActions;
import script.actionz.UniqueActions.Actionz;

public class GrandExchangg {
	public static boolean close()
	{
		if(UniqueActions.isActionEnabled(Actionz.ESC_TO_CLOSE)) return Keyboard.closeInterfaceWithESC();
		return Widgets.getWidgetChild(465, 2 , 11) != null && 
				Widgets.getWidgetChild(465, 2 , 11).interact("Close");
	}
}
