package script.utilities;

import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Shopz {
	public static boolean close() {
		if(Shop.isOpen()) {
			Sleepz.sleep();
			Keyboard.closeInterfaceWithESC();
		}
		return !Shop.isOpen();
	}
}
