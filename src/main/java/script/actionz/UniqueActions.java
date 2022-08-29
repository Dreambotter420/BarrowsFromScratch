package script.actionz;

import java.awt.Point;
import java.awt.Rectangle;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.quest.book.PaidQuest;
import org.dreambot.api.methods.quest.book.Quest;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.actionz.UniqueActions.Actionz;
import script.utilities.API;
import script.utilities.Bankz;
import script.utilities.Dialoguez;
import script.utilities.GrandExchangg;
import script.utilities.Sleep;
import script.utilities.Tabz;

public class UniqueActions {
	public static enum Bedtime {
		AMERICA,
		BRITAIN
	}
	public static Bedtime bedtime = null;
	public static enum Actionz {
		CHECK_LVl_QUEST_STATUS,
		WALK_DRUNKENLY,
		USE_FKEYS,
		ESC_TO_CLOSE,
		HOLD_SPACE_WHEN_QUESTING,
		SPACE_TO_CONTINUE,
		REPORT_SPAMMERS,
		USE_CHATBOT,
		MOVE_MOUSE_OFFSCREEN,
		EXAMINE_RANDOM_OBJECTS,
		PREFER_CAMERA_UP,
		PRETEND_TO_HAVE_RUNELITE_CHATCOMMAND_PLUGIN_AT_GE,
		TAKE_MORE_BREAKS,
		TAKE_LONGER_BREAKS,
		AFK_LONGER,
		MOUSE_SQUIGGLES,
		LEFTMOUSECLICK_ATTACK_NPC,
		TRADE_ACCEPT_DELAY_OFF,
		HIDE_UNSTARTED_QUESTS,
		FORGET_INVENTORY_ITEMS,
		DISABLE_ALL_WARNINGS,
		HIDE_PLAYER_ATTACK,
		PROFANITY_FILTER_OFF,
		SELL_ALL_LOOTJUNK_AT_GE,
		SUPER_ENERGY_INSTEAD_OF_STAMINA,
		DIVINE_POTS_INSTEAD_OF_REGULAR,
		ADDYPLATEBODY_INSTEAD_OF_RUNECHAINBODY,
		MITH_DARTS_INSTEAD_OF_ADDY,
		USE_TEAMCAPE,
		HA_DURING_AGILITY,
		TYPE_OPTION_DIALOGUE,
		BASS_OR_LOBSTER_INSTEAD_OF_JUGSOFWINE;
	}
	public static LinkedHashMap<Actionz,Boolean> uniqueActions = new LinkedHashMap<Actionz,Boolean>();
	public static void initialize()
	{
		String username = Players.localPlayer().getName();
		String hash = getHash(username);
		for(int i = 0; i <= 32; i++)
		{
			switch(i)
			{
			case(0): uniqueActions.put(Actionz.CHECK_LVl_QUEST_STATUS,isHexCharTrue(hash.charAt(i)));break;
			case(1): uniqueActions.put(Actionz.WALK_DRUNKENLY,isHexCharTrue(hash.charAt(i)));break;
			case(2): uniqueActions.put(Actionz.USE_FKEYS,isHexCharTrue(hash.charAt(i)));break;
			case(3): uniqueActions.put(Actionz.ESC_TO_CLOSE,isHexCharTrue(hash.charAt(i)));break;
			case(4): uniqueActions.put(Actionz.HOLD_SPACE_WHEN_QUESTING,isHexCharTrue(hash.charAt(i)));break;
			case(5): uniqueActions.put(Actionz.SPACE_TO_CONTINUE,isHexCharTrue(hash.charAt(i)));break;
			case(6): uniqueActions.put(Actionz.REPORT_SPAMMERS,isHexCharTrue(hash.charAt(i)));break;
			case(7): uniqueActions.put(Actionz.USE_CHATBOT,isHexCharTrue(hash.charAt(i)));break;
			case(8): uniqueActions.put(Actionz.MOVE_MOUSE_OFFSCREEN,isHexCharTrue(hash.charAt(i)));break;
			case(9): uniqueActions.put(Actionz.EXAMINE_RANDOM_OBJECTS,isHexCharTrue(hash.charAt(i)));break;
			case(10): uniqueActions.put(Actionz.PREFER_CAMERA_UP,isHexCharTrue(hash.charAt(i)));break;
			case(11): uniqueActions.put(Actionz.PRETEND_TO_HAVE_RUNELITE_CHATCOMMAND_PLUGIN_AT_GE,isHexCharTrue(hash.charAt(i)));break;
			case(12): uniqueActions.put(Actionz.TAKE_MORE_BREAKS,isHexCharTrue(hash.charAt(i)));break;
			case(13): uniqueActions.put(Actionz.TAKE_LONGER_BREAKS,isHexCharTrue(hash.charAt(i)));break;
			case(14): uniqueActions.put(Actionz.AFK_LONGER,isHexCharTrue(hash.charAt(i)));break;
			case(15): uniqueActions.put(Actionz.MOUSE_SQUIGGLES,isHexCharTrue(hash.charAt(i)));break;
			case(16): uniqueActions.put(Actionz.LEFTMOUSECLICK_ATTACK_NPC,isHexCharTrue(hash.charAt(i)));break;
			case(17): uniqueActions.put(Actionz.TRADE_ACCEPT_DELAY_OFF,isHexCharTrue(hash.charAt(i)));break;
			case(18): uniqueActions.put(Actionz.HIDE_UNSTARTED_QUESTS,isHexCharTrue(hash.charAt(i)));break;
			case(19): uniqueActions.put(Actionz.FORGET_INVENTORY_ITEMS,isHexCharTrue(hash.charAt(i)));break;
			case(20): uniqueActions.put(Actionz.DISABLE_ALL_WARNINGS,isHexCharTrue(hash.charAt(i)));break;
			case(21): uniqueActions.put(Actionz.HIDE_PLAYER_ATTACK,isHexCharTrue(hash.charAt(i)));break;
			case(22): uniqueActions.put(Actionz.PROFANITY_FILTER_OFF,isHexCharTrue(hash.charAt(i)));break;
			case(23): uniqueActions.put(Actionz.SELL_ALL_LOOTJUNK_AT_GE,isHexCharTrue(hash.charAt(i)));break;
			case(24): 
			{
				if(isHexCharTrue(hash.charAt(i))) bedtime = Bedtime.AMERICA;
				else bedtime = Bedtime.BRITAIN;
				break;
			}
			case(25): uniqueActions.put(Actionz.DIVINE_POTS_INSTEAD_OF_REGULAR,isHexCharTrue(hash.charAt(i)));break;
			case(26): uniqueActions.put(Actionz.ADDYPLATEBODY_INSTEAD_OF_RUNECHAINBODY,isHexCharTrue(hash.charAt(i)));break;
			case(27): uniqueActions.put(Actionz.MITH_DARTS_INSTEAD_OF_ADDY,isHexCharTrue(hash.charAt(i)));break;
			case(28): uniqueActions.put(Actionz.BASS_OR_LOBSTER_INSTEAD_OF_JUGSOFWINE,isHexCharTrue(hash.charAt(i)));break;
			case(29): uniqueActions.put(Actionz.USE_TEAMCAPE,isHexCharTrue(hash.charAt(i)));break;
			case(30): uniqueActions.put(Actionz.HA_DURING_AGILITY,isHexCharTrue(hash.charAt(i)));break;
			case(31): uniqueActions.put(Actionz.TYPE_OPTION_DIALOGUE,isHexCharTrue(hash.charAt(i)));break;
			default:break;
			}
		}
		MethodProvider.log("Set antiban profile based on MD5 Hash of Player username:");
		MethodProvider.log(hash);
		printAllActionParameters();
	}
	
	
	public static void printAllActionParameters()
	{
		for(Entry<Actionz,Boolean> mapEntry : uniqueActions.entrySet())
		{
			MethodProvider.log("Parameter: " + mapEntry.getKey().toString()+" is " + (isActionEnabled(mapEntry.getKey()) ? "ENABLED" : "DISABLED"));
		}
	}
	public static boolean isActionEnabled(Actionz action)
	{
		if(action == null) return false;
		return uniqueActions.get(action);
	}
	
	public static String getHash(String username)
	{
		
		try
		{
			// invoking the static getInstance() method of the MessageDigest class  
			// Notice it has MD5 in its parameter.  
			MessageDigest msgDst = MessageDigest.getInstance("MD5");  
			  
			// the digest() method is invoked to compute the message digest  
			// from an input digest() and it returns an array of byte  
			byte[] msgArr = msgDst.digest(username.getBytes());  
			  
			// getting signum representation from byte array msgArr  
			BigInteger bi = new BigInteger(1, msgArr);  
			  
			// Converting into hex value  
			String hshtxt = bi.toString(16);  
			  
			while (hshtxt.length() < 32)   
			{  
			hshtxt = "0" + hshtxt;  
			}  
			
			return hshtxt; 
		}
		catch (NoSuchAlgorithmException abc)   
		{  
		throw new RuntimeException(abc);  
		}
	}
	/**
	 * Converts Hash Hex char to true/false
	 * Hex chars = 0-9, a-f.
	 * 16 possibilities, so first 8 possibilities return true, others false.
	 */
	public static boolean isHexCharTrue(char c)
	{
		if(c == "0".charAt(0) || c == "1".charAt(0) || c == "2".charAt(0) ||  c == "3".charAt(0) || 
				c == "4".charAt(0) || c == "5".charAt(0) || c == "6".charAt(0) || c == "7".charAt(0))
		{
			return true;
		}
		return false;
	}
	
	public static void printHashForUsername()
	{
		String username = Players.localPlayer().getName();
		String hash = getHash(username);
		MethodProvider.log("Hash for username: " + username+" is: " + hash);
	}
	
	public static void examineRandomObect()
	{
		if(Tabs.isOpen(Tab.INVENTORY))
		{
			if(Calculations.random(1, 10) > 5 && !Inventory.isEmpty())
			{
				List<Item> validItems = new ArrayList<Item>();
				for(Item i : Inventory.all())
				{
					if(i == null || 
							i.getID() == -1 ||
							i.getName().isEmpty() ||
							i.getName().equals("null")) continue;
					validItems.add(i);
				}
				Collections.shuffle(validItems);
				MethodProvider.log("Attempting examine of item: " +validItems.get(0).getName());
				if(validItems.get(0).interact("Examine")) Sleep.sleep(696,666);
				return;
			}
		}
		List<Entity> validObjects = new ArrayList<Entity>();
		for(GameObject g : GameObjects.all())
		{
			if(g == null ||
					!g.exists() ||
					g.getName().toLowerCase().contains("null") ||
					g.distance() > 10) continue;
			validObjects.add(g);
		}
		for(NPC g : NPCs.all())
		{
			if(g == null ||
					!g.exists() ||
					g.getName().toLowerCase().contains("null") ||
					g.distance() > 10) continue;
			validObjects.add(g);
		}
		for(GroundItem g : GroundItems.all())
		{
			if(g == null ||
					!g.exists() ||
					g.getName().toLowerCase().contains("null") ||
					g.distance() > 10) continue;
			final Tile pileTile = g.getTile();
			int playersCount = 0;
			for(Player p : Players.all(p -> p!=null && p.getTile().equals(pileTile)))
			{
				playersCount++;
			}
			if(playersCount >= 3) continue;
			validObjects.add(g);
		}
		
		if(validObjects.isEmpty())
		{
			MethodProvider.log("No valid objects to examine, skipping antiban action!");
			return;
		}
		Collections.shuffle(validObjects);
		MethodProvider.log("Attempting examine of entity: " +validObjects.get(0).getName());
		if(validObjects.get(0).interact("Examine"))
		{
			Sleep.sleep(696,666);
		}
	}
}
