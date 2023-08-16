package script.utilities;

import org.dreambot.api.input.Keyboard;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class GrandExchangg {
	public static boolean close() {
		if (GrandExchange.isOpen()) {
			Sleepz.sleep();
			Keyboard.closeInterfaceWithEsc();
		}
		return !GrandExchange.isOpen();
	}
}
