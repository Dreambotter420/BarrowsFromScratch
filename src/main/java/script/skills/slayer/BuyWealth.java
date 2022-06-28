package script.skills.slayer;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import script.framework.Leaf;
import script.utilities.InvEquip;

public class BuyWealth extends Leaf{
	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public int onLoop() {
		InvEquip.clearAll();
		InvEquip.setEquipItem(EquipmentSlot.RING,InvEquip.wealth);
		InvEquip.setEquipItem(EquipmentSlot.AMULET,InvEquip.glory);
		InvEquip.addInvyItem(InvEquip.ironDart, 1000,1000,false, 1000);
		InvEquip.addInvyItem(InvEquip.coins, 0, 0, false, 0);
		if(InvEquip.fulfillSetup(60000))
		{
			
		}
		
		return 1000;
	}
	
}
