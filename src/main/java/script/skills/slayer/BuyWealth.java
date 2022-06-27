package script.skills.slayer;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import script.framework.Leaf;
import script.utilities.InventoryEquipment;

public class BuyWealth extends Leaf{
	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public int onLoop() {
		InventoryEquipment.clearAll();
		InventoryEquipment.setEquipItem(EquipmentSlot.RING,InventoryEquipment.wealth);
		InventoryEquipment.setEquipItem(EquipmentSlot.AMULET,InventoryEquipment.glory);
		InventoryEquipment.addInvyItem(InventoryEquipment.ironDart, 1000,1000,false, 1000);
		InventoryEquipment.addInvyItem(InventoryEquipment.coins, 0, 0, false, 0);
		if(InventoryEquipment.fulfillSetup(60000))
		{
			MethodProvider.log("fulfill boolean returned true!");
		}
		else MethodProvider.log("fulfill boolean returned false!");
		return 1000;
	}
	
}
