package script.utilities;

import org.dreambot.api.wrappers.items.Item;

public class InventoryItem {
	public int maxQty;
	public int minQty;
	public int itemID;
	public boolean noted;
	public int refillQty;
	public Item itemRef;
	
	public InventoryItem (int itemID, int minQty, int maxQty, boolean noted, int refillQty)
	{
		this.minQty = minQty;
		this.maxQty = maxQty;
		this.itemID = itemID;
		this.noted = noted;
		this.refillQty = refillQty;
		this.itemRef = new Item(itemID,minQty);
	}
	
	public static InventoryItem createItem (int itemID, int minQty, int maxQty, boolean noted, int refillQty)
	{
		return new InventoryItem(itemID,minQty,maxQty,noted,refillQty);
	}
}