package script.utilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.dreambot.api.methods.map.Map;

public class id {
	public static final int cammyTele = 8010;
	public static final int houseTele = 8013;
	public static final int lumbridgeTele = 8008;
	public static final int fallyTele = 8009;
	public static final int varrockTele = 8007;
	public static final int rope = 954;
	public static final int litCandle = 33;
	public static final int unlitCandle = 36;
	public static final int glassblowingPipe = 1785;
	public static final int moltenGlass = 1775;
	public static final int emptyCandleLantern = 4527;
	public static final int unlitCandleLantern = 4529;
	public static final int litCandleLantern = 4531;
	public static final int vial = 229;
	public static final int fireRune = 554;
	public static final int waterRune = 555;
	public static final int airRune = 556;
	public static final int earthRune = 557;
	public static final int mindRune = 558;
	public static final int chaosRune = 562;
	public static final int deathRune = 560;
	public static final int natureRune = 561;
	public static final int lawRune = 563;
	public static final int spade = 952;
	public static final int bucket = 1925;
	public static final int pureEss = 7936;

	public static final int prayPot1 = 143;
	public static final int prayPot2 = 141;
	public static final int prayPot3 = 139;
	public static final int prayPot4 = 2434;
	public static List<Integer> prayPots = new ArrayList<Integer>();
	public static final int superAtt1 = 149;
	public static final int superAtt2 = 147;
	public static final int superAtt3 = 145;
	public static final int superAtt4 = 2436;
	public static List<Integer> superAtts = new ArrayList<Integer>();
	public static final int superStr1 = 161;
	public static final int superStr2 = 159;
	public static final int superStr3 = 157;
	public static final int superStr4 = 2440;
	public static List<Integer> superStrs = new ArrayList<Integer>();
	public static final int antidote1 = 5958;
	public static final int antidote2 = 5956;
	public static final int antidote3 = 5954;
	public static final int antidote4 = 5952;
	public static List<Integer> antidotes = new ArrayList<Integer>();
	public static final int stamina4 = 12625;
	public static final int stamina3 = 12627;
	public static final int stamina2 = 12629;
	public static final int stamina1 = 12631;
	public static List<Integer> staminas = new ArrayList<Integer>();
	public static final int bass = 365;
	public static final int monkfish = 7946;
	public static final int swordfish = 373;
	public static final int pineapplePizza1 = 2303;
	public static final int pineapplePizza2 = 2301;
	public static final int seaTurtle = 397;
	public static final int casket = 405;
	
	public static final int runeDaggerPP = 5678;
	public static final int runeSword = 1289;
	public static final int blueDhideBody = 2499;
	public static final int earthBattlestaff = 1399;
	public static final int runeDagger = 1213;
	public static final int redDhideBody = 2501;
	public static final int adamantPlatebody = 1123;
	public static final int runeAxe = 1359;
	public static final int greenDhideBody = 1135;
	public static final int onyxBoltsE = 9245;
	public static final int mithrilPlatebody = 1121;
	public static final int rune2h = 1319;
	public static final int redDhideBodyG = 12327;
	public static final int redDhideChaps = 2495;
	public static final int blackDhideBody = 2503;
	public static final int redDhidevambs = 2489;
	public static final int mysticEarthStaff = 1407;
	public static final int initiateSallet = 5574;
	public static final int proselyteTasset = 9678;
	public static final int runePlateskirt = 1093;
	public static final int saradominPlateskirt = 3479;
	public static final int runeBattleaxe = 1373;
	public static final int runeMedHelm = 1147;
	public static final int mithrilSword = 1285;
	
	public static final int staffOfAir = 1381;
	public static final int staffOfEarth = 1385;
	public static final int staffOfWater = 1383;
	public static final int staffOfFire = 1387;
	
	public static final int magicLongbow = 859;
	public static final int yewLongbow = 855;
	

	public static final int giantFrogLegs = 4518;
	public static final int bloodRune = 565;
	public static final int cosmicRune = 564;
	public static final int knife = 946;
	public static final int saradominCape = 2412;
	public static final int zamorakCape = 2414;
	public static final int guthixCape = 2413;
	public static final int saradominStaff = 2415;
	public static final int zamorakStaff = 2417;
	public static final int guthixStaff = 2416;
	
	
	public static LinkedHashMap<Integer, Integer> approvedAlchs = new LinkedHashMap<Integer,Integer>();
	public static LinkedHashMap<Integer, Integer> xpAlchs = new LinkedHashMap<Integer,Integer>();
	public static void initializeIDLists()
	{
		xpAlchs.put(magicLongbow,18000);
		xpAlchs.put(yewLongbow,18000);
		
		approvedAlchs.put(redDhideBodyG,8);
		approvedAlchs.put(redDhideChaps,65);
		approvedAlchs.put(blackDhideBody,65);
		approvedAlchs.put(redDhidevambs,65);
		approvedAlchs.put(mysticEarthStaff,18000);
		approvedAlchs.put(initiateSallet,125);
		approvedAlchs.put(proselyteTasset,70);
		approvedAlchs.put(runePlateskirt,70);
		approvedAlchs.put(saradominPlateskirt,8);
		approvedAlchs.put(runeBattleaxe,70);
		approvedAlchs.put(runeMedHelm,70);
		approvedAlchs.put(mithrilSword,125);
		approvedAlchs.put(runeDaggerPP,70);
		approvedAlchs.put(runeSword,70);
		approvedAlchs.put(blueDhideBody,120);
		approvedAlchs.put(earthBattlestaff,18000);
		approvedAlchs.put(runeDagger,70);
		approvedAlchs.put(redDhideBody,65);
		approvedAlchs.put(adamantPlatebody,120);
		approvedAlchs.put(runeAxe,35);
		approvedAlchs.put(greenDhideBody,120);
		approvedAlchs.put(onyxBoltsE,11000);
		approvedAlchs.put(mithrilPlatebody,120);
		approvedAlchs.put(rune2h,70);

		prayPots.add(prayPot1);
		prayPots.add(prayPot2);
		prayPots.add(prayPot3);
		prayPots.add(prayPot4);
		superAtts.add(superAtt1);
		superAtts.add(superAtt2);
		superAtts.add(superAtt3);
		superAtts.add(superAtt4);
		superStrs.add(superStr1);
		superStrs.add(superStr2);
		superStrs.add(superStr3);
		superStrs.add(superStr4);
		staminas.add(stamina1);
		staminas.add(stamina2);
		staminas.add(stamina3);
		staminas.add(stamina4);
		antidotes.add(antidote1);
		antidotes.add(antidote2);
		antidotes.add(antidote3);
		antidotes.add(antidote4);
	}
}
