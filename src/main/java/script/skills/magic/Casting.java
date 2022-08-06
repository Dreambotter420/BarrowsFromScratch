package script.skills.magic;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.magic.cost.Rune;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;

import script.utilities.Sleep;
import script.utilities.id;

public class Casting {
	public static int getAutocastConfig()
	{
		return PlayerSettings.getBitValue(276);
	}
	
	public static boolean isAutocasting()
	{
		return getAutocastConfig() != 0;
	}
	
	public static boolean isAutocastingHighest()
	{
		return getAutocastConfig() == getHighestSpellConfig();
	}
	public static boolean setHighestAutocast()
	{
		return setAutocast(getHighestSpellConfig());
	}
	public static boolean setAutocast(int spellConfigID)
	{
		if(getAutocastConfig() == spellConfigID) return true;
		if(!Tabs.isOpen(Tab.COMBAT))
		{
			if(Tabs.open(Tab.COMBAT))
			{
				MethodProvider.sleepUntil(() -> Tabs.isOpen(Tab.COMBAT), Sleep.calculate(2222, 2222));
			}
		}
		if(Tabs.isOpen(Tab.COMBAT))
		{
			if(Widgets.getWidgetChild(201,1,1) == null || 
					!Widgets.getWidgetChild(201,1,1).isVisible())
			{
				if(Widgets.getWidgetChild(593, 26).interact("Choose spell"))
				{
					MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(201,1,1) != null && 
							Widgets.getWidgetChild(201,1,1).isVisible(),Sleep.calculate(2222, 2222));
				}
			}
			if(Widgets.getWidgetChild(201,1,1) != null && 
					Widgets.getWidgetChild(201,1,1).isVisible())
			{
				if(Widgets.getWidgetChild(201,1,spellConfigID).interact(getSpellName(spellConfigID)))
				{
					MethodProvider.sleepUntil(() -> getAutocastConfig() == spellConfigID,Sleep.calculate(2222, 2222));
				}
				if(getAutocastConfig() == spellConfigID) return true;
			}
		}
		return false;
	}
	
	public static int getHighestSpellConfig()
	{
		final int mage = TrainMagic.magic;
		if(mage < 5) return 1;
		if(mage < 9) return 2;
		if(mage < 13) return 3;
		if(mage < 17) return 4;
		if(mage < 23) return 5;
		if(mage < 29) return 6;
		if(mage < 35) return 7;
		if(mage < 41) return 8;
		if(mage < 47) return 9;
		if(mage < 53) return 10;
		if(mage < 59) return 11;
		if(mage < 62) return 12;
		if(mage < 65) return 13;
		if(mage < 70) return 14;
		if(mage < 75) return 15;
		return 16;
	}
	
	public static String getSpellName(int configID)
	{
		if(configID == 1) return "Wind Strike";
		if(configID == 2) return "Water Strike";
		if(configID == 3) return "Earth Strike";
		if(configID == 4) return "Fire Strike";
		if(configID == 5) return "Wind Bolt";
		if(configID == 6) return "Water Bolt";
		if(configID == 7) return "Earth Bolt";
		if(configID == 8) return "Fire Bolt";
		if(configID == 9) return "Wind Blast";
		if(configID == 10) return "Water Blast";
		if(configID == 11) return "Earth Blast";
		if(configID == 12) return "Fire Blast";
		if(configID == 13) return "Wind Wave";
		if(configID == 14) return "Water Wave";
		if(configID == 15) return "Earth Wave";
		if(configID == 16) return "Fire Wave";
		return "";
	}
	public static Normal getSpellOfAutocastID(int configID)
	{
		if(configID == 1) return Normal.WIND_STRIKE;
		if(configID == 2) return Normal.WATER_STRIKE;
		if(configID == 3) return Normal.EARTH_STRIKE;
		if(configID == 4) return Normal.FIRE_STRIKE;
		if(configID == 5) return Normal.WIND_BOLT;
		if(configID == 6) return Normal.WATER_BOLT;
		if(configID == 7) return Normal.EARTH_BOLT;
		if(configID == 8) return Normal.FIRE_BOLT;
		if(configID == 9) return Normal.WIND_BLAST;
		if(configID == 10) return Normal.WATER_BLAST;
		if(configID == 11) return Normal.EARTH_BLAST;
		if(configID == 12) return Normal.FIRE_BLAST;
		if(configID == 13) return Normal.WIND_WAVE;
		if(configID == 14) return Normal.WATER_WAVE;
		if(configID == 15) return Normal.EARTH_WAVE;
		if(configID == 16) return Normal.FIRE_WAVE;
		return null;
	}
	public static boolean haveRunesForSpell(Normal spell)
	{
		for(Rune rune : spell.getCost())
		{
			if(rune.getName().equals("Air rune") && Equipment.contains(id.staffOfAir)) continue;
			if(rune.getName().equals("Water rune") && Equipment.contains(id.staffOfWater)) continue;
			if(rune.getName().equals("Earth rune") && Equipment.contains(id.staffOfEarth)) continue;
			if(rune.getName().equals("Fire rune") && Equipment.contains(id.staffOfFire)) continue;
			if(Inventory.count(rune.getName()) >= rune.getAmount()) continue;
			else 
			{
				MethodProvider.log("Missing runes: " + rune.getName() + " for spell: " + spell.toString());
				return false;
			}
		}
		return true;
	}
	
}
