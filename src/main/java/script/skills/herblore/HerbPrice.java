package script.skills.herblore;


import org.dreambot.api.utilities.Logger;

import script.skills.herblore.ProfitableHerblore.Herb;


public class HerbPrice {
	public int grimyLow;
	public int grimyHigh;
	public int unfLow;
	public int unfHigh;
	public int profitMargin;
	public Herb herb;
	
	public HerbPrice (Herb herb, int grimyLow, int grimyHigh, int unfLow, int unfHigh)
	{
		this.herb = herb;
		this.grimyLow = grimyLow;
		this.grimyHigh = grimyHigh;
		this.unfLow = unfLow;
		this.unfHigh = unfHigh;
		this.profitMargin = unfLow - grimyHigh;
	}
	public void printHerbPrices()
	{
		Logger.log("Herb ["+this.herb.toString()+"] grimyLow ["+grimyLow+"] grimyHigh ["+grimyHigh+"] UnfLow ["+unfLow+"] UnfHigh ["+unfHigh+"] profitMargin ["+profitMargin+"]");
	}
}