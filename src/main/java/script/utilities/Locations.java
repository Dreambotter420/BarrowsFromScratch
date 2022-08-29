package script.utilities;

import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.pathfinding.impl.web.WebFinder;
import org.dreambot.api.methods.walking.web.node.AbstractWebNode;
import org.dreambot.api.methods.walking.web.node.impl.BasicWebNode;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import script.quest.varrockmuseum.Timing;

public class Locations {
	public static void initialize()
	{
		sandcrabsArea1 = sandcrabSouls1.getArea(6);
		sandcrabsArea2 = sandcrabSouls2.getArea(6);
	}
	public static Area sandcrabsArea1 = null;
	public static Area sandcrabsArea2 = null;
	public static boolean unlockedKourend = true;
	public static void unlockKourend()
	{
		if(isInKourend()) return;
		//handle dialogue for Veos traveling to Kourend
		if(Dialoguez.handleDialogues()) return;
		
		if(Locations.veosSarim.getCenter().distance(Players.localPlayer().getTile()) > 75) 
		{
			Walkz.teleportDraynor(180000);
			Sleep.sleep(420,1111);
			return;
		}
		
		API.walkTalkToNPC("Veos", "Talk-to", Locations.veosSarim);
	}
	public static boolean unlockedHouse = true;
	public static final Area camelotTrees = new Area(
			new Tile(2752, 3452, 0),
			new Tile(2756, 3451, 0),
			new Tile(2760, 3451, 0),
			new Tile(2770, 3455, 0),
			new Tile(2777, 3457, 0),
			new Tile(2781, 3467, 0),
			new Tile(2776, 3480, 0),
			new Tile(2754, 3480, 0));
	public static final Area deepWildyEdgevilleLeverPeninsula = new Area(
			new Tile(3160, 3913, 0),
			new Tile(3168, 3918, 0),
			new Tile(3160, 3931, 0),
			new Tile(3169, 3946, 0),
			new Tile(3163, 3950, 0),
			new Tile(3153, 3950, 0),
			new Tile(3153, 3941, 0),
			new Tile(3144, 3936, 0),
			new Tile(3147, 3929, 0),
			new Tile(3144, 3923, 0));
	public static final Area mageArenaInnerCircle = new Area(
			new Tile(3091, 3934, 0),
			new Tile(3098, 3948, 0),
			new Tile(3113, 3948, 0),
			new Tile(3121, 3938, 0),
			new Tile(3117, 3921, 0),
			new Tile(3095, 3920, 0));
	public static final Area mageArenaCaveSouth = new Area(2501, 4680, 2516, 4707, 0);
	public static final Area mageArenaCaveStatues = new Area(2496, 4712, 2523, 4729, 0);
	public static final Area mageArenaCave = new Area(2493, 4732, 2529, 4679, 0);
	public static final Tile mageArenaBankOutsideLeverTile = new Tile(3090,3956,0);
	public static final Area mageArenaBankWeb1 = new Area(3093, 3953, 3102, 3963, 0);
	public static final Area mageArenaBankLeverRoom = new Area(
			new Tile(3092, 3958, 0),
			new Tile(3092, 3956, 0),
			new Tile(3091, 3954, 0),
			new Tile(3090, 3955, 0),
			new Tile(3090, 3958, 0));
	public static final Area mageArenaBankOutside = new Area(
			new Tile(3087, 3950, 0),
			new Tile(3092, 3950, 0),
			new Tile(3096, 3953, 0),
			new Tile(3101, 3954, 0),
			new Tile(3102, 3965, 0),
			new Tile(3087, 3967, 0));
	public static final Area mageArenaBank = new Area(2527, 4709, 2550, 4725, 0);
	public static final Area mageArenaBankLeverAirlock = new Area(3094, 3958, 3093, 3956, 0);
	public static final Area deepWildyWeb_EdgevilleLever = new Area(3152, 3946, 3164, 3956, 0);
	public static final Area edgevillePKLever = new Area(3085, 3482, 3097, 3468, 0);
	public static final Area allFremennikProvinceSeersLighthouse = new Area(
		    new Tile[] {
		        new Tile(2720, 3744, 0),
		        new Tile(2776, 3682, 0),
		        new Tile(2813, 3611, 0),
		        new Tile(2790, 3523, 0),
		        new Tile(2786, 3454, 0),
		        new Tile(2612, 3449, 0),
		        new Tile(2577, 3577, 0),
		        new Tile(2466, 3625, 0),
		        new Tile(2473, 3695, 0),
		        new Tile(2652, 3748, 0)
		    }
		);
	public static final Area strangeAltar = new Area(2624, 3604, 2628, 3596, 0);
	public static final Area seersPub = new Area(
			new Tile(2689, 3488, 0),
			new Tile(2697, 3488, 0),
			new Tile(2697, 3494, 0),
			new Tile(2700, 3495, 0),
			new Tile(2700, 3498, 0),
			new Tile(2689, 3498, 0));
	public static final Area salveGraveyard = new Area(
			new Tile(3422, 3467, 0),
			new Tile(3423, 3459, 0),
			new Tile(3427, 3457, 0),
			new Tile(3435, 3455, 0),
			new Tile(3441, 3461, 0),
			new Tile(3438, 3467, 0),
			new Tile(3431, 3472, 0));
	public static final Area natureSpirit_insideGrottoFinished = new Area(3447, 9733, 3436, 9744, 1);
	public static final Area natureSpirit_insideGrotto = new Area(3447, 9733, 3435, 9744, 0);
	public static final Area natureSpirit_finalPuzzleTile = new Area(3440, 3335, 3440, 3335, 0);
	public static final Area natureSpirit_logSpellTile = new Area(3423, 3336, 3423, 3336, 0);
	public static final Area natureSpiritGrotto_SouthStone = new Area(3440, 3335, 3440, 3335, 0);
	public static final Area natureSpiritGrotto = new Area(3435, 3331, 3447, 3343, 0);
	public static final Area natureSpiritGrottoBridgeSouth = new Area(3443, 3329, 3434, 3325, 0);
	public static final Area morytaniaSwampGateNorth = new Area(
			new Tile(3437, 3457, 0),
			new Tile(3441, 3457, 0),
			new Tile(3442, 3458, 0),
			new Tile(3445, 3458, 0),
			new Tile(3447, 3456, 0),
			new Tile(3452, 3460, 0),
			new Tile(3448, 3466, 0),
			new Tile(3442, 3466, 0),
			new Tile(3437, 3465, 0));
	public static final Area morytaniaSwampGateSouth = new Area(3443,3457,3444,3457,0);
	public static final Area entireMorytania = new Area(  new Tile[] {
		        new Tile(3429, 3472, 0),
		        new Tile(3429, 3480, 0),
		        new Tile(3420, 3482, 0),
		        new Tile(3419, 3494, 0),
		        new Tile(3405, 3514, 0),
		        new Tile(3401, 3603, 0),
		        new Tile(3727, 3572, 0),
		        new Tile(3825, 3185, 0),
		        new Tile(3494, 3139, 0),
		        new Tile(3423, 3210, 0),
		        new Tile(3421, 3269, 0),
		        new Tile(3413, 3292, 0),
		        new Tile(3418, 3308, 0),
		        new Tile(3416, 3317, 0),
		        new Tile(3401, 3322, 0),
		        new Tile(3388, 3344, 0),
		        new Tile(3402, 3399, 0),
		        new Tile(3402, 3408, 0),
		        new Tile(3407, 3415, 0),
		        new Tile(3401, 3426, 0),
		        new Tile(3405, 3438, 0),
		        new Tile(3400, 3448, 0),
		        new Tile(3409, 3451, 0),
		        new Tile(3413, 3460, 0)  } );
	public static final Area natureSpirit_trapdoor = new Area(3421, 3485, 3432, 3484, 0);
	public static final Area fremmy_koscheiArena = new Area(2637, 10100, 2676, 10062, 2);
	public static final Area fremmy_askeladden = new Area(2651, 3664, 2667, 3656, 0);
	public static final Area fremmy_peerTheSeer = new Area(2638, 3667, 2627, 3672, 0);
	public static final Area fremmy_fisherman = new Area(2641, 3700, 2640, 3690, 0);
	public static final Area fremmy_peerLeftDoor = new Area(2629, 3671, 2633, 3665, 0);
	public static final Area fremmy_peerLeftHouse = new Area(2629, 3666, 2633, 3659, 0);
	public static final Area fremmy_peerUpstairs = new Area(2629, 3665, 2639, 3660, 2);
	public static final Area fremmy_upstairsHelmetShop = new Area(2666, 3690, 2668, 3695, 1);
	public static final Area fremmy_helmetShop = new Area(2662, 3695, 2668, 3690, 0);
	public static final Area fremmy_peerRightHouse = new Area(2638, 3659, 2634, 3666, 0);
	public static final Area fremmy_yrsa = new Area(2622, 3676, 2626, 3672, 0);
	public static final Area fremmy_sailor = new Area(2629, 3697, 2632, 3690, 0);
	public static final Area fremmy_sigmund = new Area(2635, 3683, 2649, 3671, 0);
	public static final Area fremmy_swensen = new Area(2643, 3662, 2650, 3657, 0);
	public static final Area fremmy_pipeBomb = new Area(2663, 3676, 2666, 3670, 0);
	public static final Area fremmy_wholeConcert = new Area(2655, 3682, 2666, 3685, 0);
	public static final Area fremmy_concertPlatform = new Area(2655, 3685, 2662, 3682, 0);
	public static final Area fremmy_concertDoor = new Area(2663, 3689, 2671, 3679, 0);
	public static final Area fremmy_councilWorkman = new Area(2651, 3595, 2658, 3589, 0);
	public static final Area fremmy_lanzigHut = new Area(2672, 3663, 2677, 3667, 0);
	public static final Area fremmy_sigliDaHunstman = new Area(2651, 3659, 2666, 3647, 0);
	public static final Area fremmy_olafDaBeard = new Area(2667, 3679, 2679, 3693, 0);
	public static final Area fremmy_longHall = new Area(2655, 3681, 2662, 3665, 0);
	public static final Area fremmy_peerRightDoor = new Area(2634, 3664, 2638, 3669, 0);
	public static final Area draynorAgilityGround = new Area(3106, 3284, 3080, 3246, 0);
	public static final Area draynorAgility1 = new Area(3103, 3274, 3106, 3283, 0);
	public static final Area draynorAgility2 = new Area(3097, 3276, 3102, 3281, 3);
	public static final Area draynorAgility3 = new Area(
			new Tile(3090, 3277, 3),
			new Tile(3087, 3274, 3),
			new Tile(3090, 3272, 3),
			new Tile(3093, 3275, 3));
	public static final Area draynorAgility4 = new Area(3089, 3268, 3095, 3265, 3);
	public static final Area draynorAgility5 = new Area(3088, 3261, 3087, 3256, 3);
	public static final Area draynorAgility6 = new Area(3087, 3255, 3094, 3251, 3);
	public static final Area draynorAgility7 = new Area(3096, 3256, 3101, 3261, 3);
	public static final Area alkharidAgility1 = new Area(3270, 3201, 3282, 3192, 0);
	public static final Area alkharidAgility2 = new Area(3269, 3194, 3279, 3180, 3);
	public static final Area alkharidAgility3 = new Area(3272, 3173, 3265, 3160, 3);
	public static final Area alkharidAgility4 = new Area(3283, 3176, 3302, 3160, 3);
	public static final Area alkharidAgility5 = new Area(3313, 3165, 3318, 3160, 1);
	public static final Area alkharidAgility6 = new Area(3318, 3173, 3312, 3179, 2);
	public static final Area alkharidAgility7 = new Area(3318, 3180, 3312, 3186, 3);
	public static final Area alkharidAgility8 = new Area(
			new Tile(3303, 3185, 3),
			new Tile(3306, 3189, 3),
			new Tile(3300, 3194, 3),
			new Tile(3297, 3190, 3));
	public static final Area alkharidAgilityGround = new Area(3323, 3199, 3260, 3152);
	public static final Area diango = new Area(3075, 3251, 3087, 3242, 0);
	public static final Area lumbyGiantFrogs = new Area(3187, 3197, 3209, 3171, 0);
	public static final Area museumArea = new Area(1725, 4991, 1793, 4928);
	public static final Area fallyTeleSpot = new Area(2961, 3384, 2969, 3376, 0);
	public static final Area houseTeleSpot = new Area(2953, 3227, 2958, 3221, 0);
	public static final Area varrockTeleSpot = new Area(3210, 3425, 3215, 3422, 0);
	public static final Area cammyTeleSpot = new Area(2755, 3480, 2759, 3476, 0);
	public static final Area edgevilleTeleSpot = new Area(3087, 3496, 3087, 3496, 0);
	public static final Area farmingGuildTeleSpot = new Area(1245, 3720, 1252, 3714, 0);
	public static final Area woodcuttingGuildTeleSpot = new Area(1658, 3508, 1663, 3502, 0);
	public static final Area rangingGuildTeleSpot = new Area(2657, 3443, 2652, 3438, 0);
	public static final Area burthorpeTeleSpot = new Area(2896, 3555, 2902, 3550, 0);
	public static final Tile draynorTeleSpot = new Tile(3105,3251,0);
	public static final Area turaelArea = new Area(2930, 3537, 2933, 3535, 0);
	public static final Area rimmington = new Area(2944, 3209, 2960, 3229, 0);
	public static final Area estateRoom = new Area(2981, 3370, 2984, 3368, 0);
	public static final Area castleWars = new Area(2446, 3081, 2435, 3098, 0);
	public static final Area lesserDemonsKillZoneWizardsTower = new Area(3108, 3158, 3112, 3156, 2);
	public static final Area veosPisc = new Area(1829,3688, 3048, 3252, 0);
	public static final Area veosSarim = new Area(3055, 3249, 3049, 3245, 0);
	public static Area haSpot = null;
	public static final Area waterfallDungeon = new Area(2556, 9919, 2597, 9860);
	public static final Area waterfallDungeonFoyer = new Area(
			new Tile(2581, 9882, 0),
			new Tile(2581, 9856, 0),
			new Tile(2567, 9861, 0),
			new Tile(2562, 9881, 0));
	public static final Area waterfallDungeonLastRoomAirlock = new Area(2570, 9894, 2565, 9901, 0);
	public static final Area waterfallDungeonLastArea = new Area(2560, 9902, 2571, 9919, 0);
	public static final Area waterfallDungeonLastAreaChanged = new Area(2598, 9916, 2608, 9901, 0);
	public static final Tile waterfallLastCrateTile = new Tile(2589,9888,0);
	public static final Area waterfallLastCrateArea = new Area(2582, 9874, 2600, 9891, 0);
	public static final Area fightArenaStartArea = new Area(2559, 3206, 2581, 3190, 0);
	public static final Area fightArenaChestHouse = new Area(
			new Tile(2614, 3189, 0),
			new Tile(2614, 3194, 0),
			new Tile(2612, 3193, 0),
			new Tile(2611, 3195, 0),
			new Tile(2607, 3195, 0),
			new Tile(2607, 3191, 0),
			new Tile(2612, 3191, 0),
			new Tile(2612, 3189, 0));
	public static final Area fightArenaDrunkGuardArea = new Area(
			new Tile(2614, 3146, 0),
			new Tile(2609, 3145, 0),
			new Tile(2609, 3139, 0),
			new Tile(2619, 3138, 0),
			new Tile(2619, 3146, 0));
	public static final Area fightArenaAlcoholicsArea = new Area(
			new Tile(2572, 3139, 0),
			new Tile(2572, 3145, 0),
			new Tile(2571, 3144, 0),
			new Tile(2570, 3151, 0),
			new Tile(2568, 3150, 0),
			new Tile(2567, 3154, 0),
			new Tile(2563, 3154, 0),
			new Tile(2563, 3139, 0));
	public static final Area fightArenaSammySpace = new Area(
			new Tile(2617, 3162, 0),
			new Tile(2619, 3162, 0),
			new Tile(2619, 3171, 0),
			new Tile(2613, 3171, 0),
			new Tile(2617, 3170, 0));
	public static final Area fightArenaFightArena = new Area(
			new Tile(2605, 3151, 0),
			new Tile(2584, 3152, 0),
			new Tile(2581, 3154, 0),
			new Tile(2581, 3169, 0),
			new Tile(2585, 3172, 0),
			new Tile(2604, 3171, 0),
			new Tile(2606, 3168, 0),
			new Tile(2607, 3158, 0),
			new Tile(2608, 3154, 0));
	public static final Area fightArenaUpperWing1 = new Area(2619, 3147, 2613, 3151, 0);
	public static final Area fightArenaUpperWing2 = new Area(
			new Tile(2616, 3152, 0),
			new Tile(2618, 3152, 0),
			new Tile(2618, 3155, 0),
			new Tile(2619, 3155, 0),
			new Tile(2619, 3171, 0),
			new Tile(2614, 3171, 0),
			new Tile(2614, 3155, 0),
			new Tile(2615, 3155, 0));
	public static final Area fightArenaLeftWing1 = new Area(
			new Tile(2585, 3144, 0),
			new Tile(2585, 3139, 0),
			new Tile(2603, 3139, 0),
			new Tile(2604, 3141, 0),
			new Tile(2608, 3141, 0),
			new Tile(2608, 3142, 0),
			new Tile(2604, 3143, 0),
			new Tile(2602, 3144, 0));
	public static final Area fightArenaLeftWing2 = new Area(
			new Tile(2609, 3143, 0),
			new Tile(2609, 3145, 0),
			new Tile(2612, 3146, 0),
			new Tile(2612, 3151, 0),
			new Tile(2610, 3152, 0),
			new Tile(2607, 3153, 0),
			new Tile(2606, 3151, 0),
			new Tile(2607, 3149, 0),
			new Tile(2607, 3143, 0));
	public static final Area fightArenaOutsideNorthJailDoor = new Area(2607, 3168, 2622, 3180, 0);
	public static final Area fightArenaOutsideLeftJailDoor = new Area(2575, 3148, 2591, 3135, 0);
	public static final Area fightArenaHengradWaitingCell = new Area(2601, 3144, 2597, 3142, 0);
	public static final Area fightArenaOgreCage = new Area(2607, 3167, 2611, 3165, 0);
	public static final Area fightArenaScorpionCage = new Area(2607, 3158, 2611, 3161, 0);
	public static final Area fightArenaBouncerCage = new Area(2607, 3162, 2611, 3164, 0);
	public static final Area restlessGhostSkeletonCoffin = new Area(3247, 3195, 3252, 3190, 0);
	public static final Area restlessGhostUrhneyHut = new Area(3151, 3173, 3144, 3177, 0);
	public static final Area wizardTowerBasement = new Area(3093, 9579, 3123, 9551, 0);
	public static final Area wizardTowerBasementLadder = new Area(3101, 9577, 3109, 9575, 0);
	public static final Area  wizardTowerBasementAltar = new Area(3121, 9569, 3116, 9564, 0);
	public static final Area wizardTowerGroundFloorLadder = new Area(
			new Tile(3102, 3165, 0),
			new Tile(3102, 3162, 0),
			new Tile(3103, 3159, 0),
			new Tile(3107, 3159, 0),
			new Tile(3108, 3160, 0),
			new Tile(3105, 3164, 0),
			new Tile(3104, 3165, 0));
	public static final Area restlessGhostLumbyChurch = new Area(
			new Tile(3238, 3211, 0),
			new Tile(3238, 3209, 0),
			new Tile(3240, 3208, 0),
			new Tile(3240, 3204, 0),
			new Tile(3247, 3204, 0),
			new Tile(3247, 3215, 0),
			new Tile(3240, 3215, 0),
			new Tile(3240, 3212, 0));
	
	public static final Area ernest_puzzleLast = new Area(3090, 9753, 3099, 9757, 0);
	public static final Area ernest_puzzle1 = new Area(3118, 9757, 3100, 9743, 0);
	public static final Area ernest_puzzle2 = new Area(3105, 9767, 3112, 9758, 0);
	public static final Area ernest_puzzle4 = new Area(3096, 9762, 3099, 9758, 0);
	public static final Area ernest_puzzle3 = new Area(3100, 9762, 3104, 9758, 0);
	public static final Area ernest_puzzle6 = new Area(3100, 9767, 3104, 9763, 0);
	public static final Area ernest_puzzle5 = new Area(3096, 9767, 3099, 9763, 0);
	public static final Area ernest_westWing = new Area(3096, 3363, 3091, 3354, 0);
	public static final Area ernest_westWingAnd = new Area(3102, 3363, 3091, 3354, 0);
	public static final Area ernest_2ndStairs = new Area(3104, 3362, 3113, 3368, 1);
	public static final Area ernest_SkellyTube = new Area(3108, 3368, 3112, 3366, 0);
	public static final Area ernest_SkellyDoorArea = new Area(3105, 3368, 3112, 3358, 0);
	public static final Area ernest_fountain = new Area(3085, 3331, 3092, 3340, 0);
	public static final Area ernest_compost = new Area(3084, 3363, 3090, 3358, 0);
	public static final Area ernest_spade = new Area(3120, 3360, 3126, 3354, 0);
	public static final Area ernest_basementMaynor = new Area(3121, 9745, 3086, 9769, 0);
	public static final Area ernest_3rdfloorMaynor = new Area(3104, 3370, 3112, 3356, 2);
	public static final Area ernest_2ndfloorMaynor = new Area(3086, 3349, 3132, 3375, 1);
	public static final Area ernest_fishfood = new Area(3107, 3361, 3110, 3354, 1);
	public static final Area ernest_veronica = new Area(3104, 3335, 3115, 3325, 0);
	public static final Area ernest_poison = new Area(3097, 3366, 3101, 3364, 0);
	public static final Area PiP_undergroundPassDrezel = new Area(3432, 9902, 3444, 9887, 0);
	public static final Area PiP_dogArena = new Area(8208, 860, 8202, 847, 0);
	public static final Area PiP_pastramiChurchLvl2 = new Area(3423, 3496, 3406, 3481, 1);
	public static final Area PiP_pastramiChurchLvl3 = new Area(3421, 3498, 3406, 3481, 2);
	public static final Area PiP_undergroundPassMonuments = new Area(3415, 9881, 3431, 9899, 0);
	public static final Area PiP_undergroundPassLadder = new Area(3402, 9908, 3407, 9899, 0);
	public static final Area PiP_pastramiChurchLvl1 = new Area(3409, 3493, 3418, 3484, 0);
	public static final Area PiP_undergroundPass = new Area(3399, 9909, 3447, 9877);
	public static final Area PiP_dogSafespot = new Area(8206, 858, 8206, 858, 0);
	public static final Area PiP_largeDoors = new Area(3403, 3496, 3409, 3480, 0);
	public static final Area PiP_trapdoor = new Area(
			new Tile(3402, 3508, 0),
			new Tile(3407, 3508, 0),
			new Tile(3411, 3500, 0),
			new Tile(3411, 3497, 0),
			new Tile(3401, 3498, 0));
	public static final Area PiP_kingRoald = new Area(
			new Tile(3220, 3478, 0),
			new Tile(3224, 3478, 0),
			new Tile(3225, 3470, 0),
			new Tile(3224, 3469, 0),
			new Tile(3220, 3469, 0),
			new Tile(3219, 3471, 0),
			new Tile(3219, 3473, 0));
	public static final Tile HALadderSpot = new Tile(3420, 3185, 0);
	public static final Area HASpot1 = new Area(3415, 3187, 3421, 3182, 1);
	public static final Area burthrope = new Area(
		    new Tile[] {
		        new Tile(2885, 3606, 0),
		        new Tile(2851, 3608, 0),
		        new Tile(2822, 3584, 0),
		        new Tile(2810, 3558, 0),
		        new Tile(2838, 3534, 0),
		        new Tile(2858, 3528, 0),
		        new Tile(2865, 3532, 0),
		        new Tile(2881, 3520, 0),
		        new Tile(2880, 3497, 0),
		        new Tile(2936, 3497, 0),
		        new Tile(2935, 3503, 0),
		        new Tile(2938, 3507, 0),
		        new Tile(2944, 3526, 0),
		        new Tile(2952, 3568, 0),
		        new Tile(2931, 3591, 0)
		    }
		);
	public static final Area veosLandsEnd = new Area(1502, 3399, 1506, 3409, 0);
	public static final Area shipLandsEnd = new Area(1500, 3396, 1510, 3394, 1);
	public static final Area shipSarimVeos = new Area(3050, 3240, 3060, 3242, 1);
	public static final Area shipPiscVeos = new Area(1818, 3694, 1828, 3696, 1);
	public static final Area kourendCastle2ndFloor = new Area(1643, 3701, 1587, 3653, 1);
	public static final Area kourendCastle3rdFloor = new Area(1627, 3654, 1590, 3697, 2);
	public static final Area hillGiantsValley = new Area(3367, 3156, 3388, 3143, 0);
	public static final Area entireKourend = new Area(1969, 4073, 1151, 3345, 0);
	public static final Area kourendGiantsCaveEntrance = new Area(1420, 3587, 1415, 3592, 0);
	public static final Area kourendGiantsCaveExit = new Area(1429, 9906, 1436, 9914, 0);
	public static final Area kourendGiantsCaveArea = new Area(1408, 9916, 1471, 9856, 0);
	public static final Area kourendGiantsSafeSpot_Hill = new Area(1430, 9876, 1430, 9876, 0);
	public static final Area kourendGiantsKillingArea_Hill = new Area(1420, 9878, 1440, 9892, 0);
	public static final Area zombiesEdgeville = new Area(3138, 9914, 3151, 9881, 0);
	public static final Area edgevilleDungeon = new Area(3153, 9918, 3089, 9861, 0);
	public static final Area isleOfSouls = new Area(2049, 3012, 2362, 2749);
	public static final Area forgottenSoulsLvl1 = new Area(2118, 3005, 2151, 2987, 0);
	public static final Area forgottenSoulsLvl2 = new Area(2140, 2991, 2128, 3000, 1);
	public static final Area forgottenSoulsLvl3 = new Area(2127, 3000, 2140, 2991, 2);
	public static final Area seagullsSarim = new Area(3023, 3242, 3035, 3229, 0);
	public static final Area bearsFremmy = new Area(2704, 3600, 2679, 3626, 0);
	public static final Area caveCrawlersFremmyCave = new Area(2800, 10006, 2779, 9989, 0);
	public static final Area entireFremmyDungeon = new Area(2687, 9948, 2814, 10046,0);
	public static final Area fremmyCaveEntrance = new Area(2787, 3620, 2798, 3609, 0);
	public static final Area lizards = new Area(3432, 3071, 3418, 3046, 0);
	public static final Area strongholdEntrance = new Area(3076, 3426, 3087, 3416, 0);
	public static final Area strongholdLvl1 = new Area(1853, 5248, 1919, 5183,0);
	public static final Area strongholdDoor1 = new Area(1859, 5236, 1858, 5238, 0);
	public static final Area strongholdLvl1Foyer = new Area(1866, 5239, 1857, 5246, 0);
	public static final Area lumbridgeCaveEntrance = new Area(3164, 3176, 3174, 3166, 0);
	public static final Area lumbridgeTeleSpot = new Area(3218, 3215, 3226, 3222, 0);
	public static final Area shantayPassSouthSide = new Area(3302, 3115, 3306, 3112, 0);
	public static final Area lumbyCaveFoyer = new Area(3174, 9566, 3163, 9575, 0);
	public static final Tile caveHandSkipTile = new Tile(3163, 9573, 0);
	public static final Tile caveHandSkipTile2 = new Tile(3161, 9573, 0);
	public static final Area caveSlimeArea = new Area(3149, 9567, 3167, 9549, 0);
	public static final Area caveBugs = new Area(3161, 9583, 3144, 9565, 0);
	
	public static final Area icefiends = new Area(
			new Tile(2994, 3464, 0),
			new Tile(3002, 3459, 0),
			new Tile(3013, 3463, 0),
			new Tile(3014, 3492, 0),
			new Tile(3007, 3494, 0),
			new Tile(3001, 3489, 0),
			new Tile(3000, 3480, 0),
			new Tile(2990, 3470, 0));
	public static final Area bats = new Area(
			new Tile(3327, 3478, 0),
			new Tile(3332, 3471, 0),
			new Tile(3347, 3473, 0),
			new Tile(3360, 3476, 0),
			new Tile(3384, 3468, 0),
			new Tile(3396, 3483, 0),
			new Tile(3394, 3497, 0),
			new Tile(3372, 3491, 0),
			new Tile(3356, 3498, 0),
			new Tile(3346, 3500, 0));
	public static final Area dwarfs = new Area(
			new Tile(3008, 3461, 0),
			new Tile(3007, 3454, 0),
			new Tile(3030, 3455, 0),
			new Tile(3037, 3469, 0),
			new Tile(3027, 3469, 0),
			new Tile(3021, 3464, 0),
			new Tile(3014, 3466, 0));
	public static Area scorpions = null;
	public static final Area scorpionsKharidNorth = new Area(
			new Tile(3303, 3297, 0),
			new Tile(3290, 3297, 0),
			new Tile(3295, 3307, 0),
			new Tile(3293, 3310, 0),
			new Tile(3297, 3318, 0),
			new Tile(3302, 3318, 0),
			new Tile(3305, 3312, 0),
			new Tile(3304, 3307, 0),
			new Tile(3307, 3303, 0),
			new Tile(3303, 3297, 0));
	public static final Area scorpionsKharidSouth = new Area(
			new Tile(3306, 3276, 0),
			new Tile(3307, 3282, 0),
			new Tile(3303, 3286, 0),
			new Tile(3307, 3293, 0),
			new Tile(3303, 3297, 0),
			new Tile(3292, 3296, 0),
			new Tile(3295, 3290, 0),
			new Tile(3290, 3284, 0),
			new Tile(3293, 3279, 0),
			new Tile(3291, 3275, 0));
	public static final Area wizardsTower0 = new Area(3130, 3191, 3090, 3142,0);
	public static final Tile wizardsTowerStairTile0 = new Tile(3105, 3161, 0);
	public static final Area wizardsTower1 = new Area(3099, 3170, 3127, 3151, 1);
	public static final Area wizardsTowerStairTile1 = new Area(3104, 3161, 3104, 3161, 1);
	public static final Area wizardsTower2 = new Area(3096, 3172, 3119, 3152, 2);
	public static final Area lesserDemonWizardsTower = new Area(3108, 3163, 3113, 3159, 2);
	public static final Area lumbyCastle1 = new Area(3201, 3232, 3219, 3205, 1);
	public static final Area lumbyCastle2 = new Area(3202, 3232, 3222, 3205, 2);
	public static final Area almera = new Area(
			new Tile(2524, 3495, 0),
			new Tile(2523, 3492, 0),
			new Tile(2518, 3493, 0),
			new Tile(2518, 3498, 0),
			new Tile(2521, 3499, 0),
			new Tile(2521, 3502, 0),
			new Tile(2523, 3502, 0),
			new Tile(2523, 3499, 0));
	public static final Area hadleySurrounding = new Area(
		    new Tile[] {
		        new Tile(2532, 3448, 0),
		        new Tile(2532, 3409, 0),
		        new Tile(2500, 3429, 0)
		    }
		);
	public static final Area waterfallDungeon1 = new Area(2564, 9588, 2498, 9546,0);
	public static final Area gnomeStrongholdDungeonEntrance = new Area(2538, 3155, 2527, 3156, 0);
	public static final Tile gnomeStrongholdEntrance = new Tile(2504, 3191, 0);
	public static final Area hadley2 = new Area(2516, 3431, 2520, 3424, 1);
	public static final Area hadleyStairs = new Area(2516, 3432, 2520, 3427, 0);
	public static final Tile waterfallRaftTile1 = new Tile(2510,3494,0);
	public static final Area barrelDestination = new Area(2524, 3416, 2534, 3411, 0);
	public static final Tile waterfallIsland1SouthTile = new Tile(2512, 3476, 0);
	public static final Area waterfallIsland1 = new Area(2513, 3475, 2510, 3481, 0);
	public static final Area waterfallIsland2 = new Area(2512, 3468, 2513, 3465, 0);
	public static final Tile waterfallLedge = new Tile(2511, 3463, 0);
	public static final Area waterfallDungeonKeyRoom = new Area(2530, 9575, 2556, 9560);
	public static final Area waterfallDungeonBeforeGolrieRoom = new Area(2507, 9575, 2522, 9567, 0);
	public static final Area waterfallDungeonGolrieRoom = new Area(2522, 9576, 2506, 9586, 0);
	public static final Area waterfallGlarialsGravestone = new Area(2554, 3449, 2563, 3440, 0);
	public static final Area waterfallDungeon2 = new Area(2523, 9852, 2559, 9808);
	public static final Area glarialsTomb = new Area(2538, 9809, 2548, 9816, 0);
	public static final Area glarialsChest = new Area(2537, 9841, 2524, 9847, 0);
	public static final Area kalphiteCave = new Area(3342, 9544, 3264, 9472,0);
	
	public static Area kalphiteWorkersArea = null;
	public static final Area kalphiteWorkers2 = new Area(3289, 9529, 3271, 9509, 0);
	public static final Area kalphiteWorkers1 = new Area(
			new Tile(3312, 9495, 0),
			new Tile(3329, 9486, 0),
			new Tile(3340, 9502, 0),
			new Tile(3325, 9520, 0),
			new Tile(3312, 9510, 0));
	public static final Area kalphiteCaveEntrance = new Area(3317, 3115, 3325, 3127, 0);
	public static final Area skeletonsEdgeville = new Area(
			new Tile(3125, 9911, 0),
			new Tile(3125, 9907, 0),
			new Tile(3128, 9906, 0),
			new Tile(3128, 9903, 0),
			new Tile(3137, 9903, 0),
			new Tile(3137, 9916, 0),
			new Tile(3134, 9917, 0),
			new Tile(3128, 9917, 0));
	public static final Area monkeysArdyZoo = new Area(
			new Tile(2595, 3291, 0),
			new Tile(2592, 3270, 0),
			new Tile(2610, 3272, 0),
			new Tile(2610, 3290, 0),
			new Tile(2595, 3291, 0),
			new Tile(2595, 3291, 0));
	public static final Area giantRats = new Area(
			new Tile(3213, 3197, 0),
			new Tile(3191, 3196, 0),
			new Tile(3192, 3158, 0),
			new Tile(3211, 3158, 0),
			new Tile(3220, 3174, 0),
			new Tile(3225, 3189, 0));
	public static final Area goblins = new Area(
			new Tile(3266, 3222, 0),
			new Tile(3255, 3216, 0),
			new Tile(3238, 3233, 0),
			new Tile(3238, 3254, 0),
			new Tile(3264, 3254, 0),
			new Tile(3266, 3239, 0),
			new Tile(3267, 3227, 0),
			new Tile(3267, 3224, 0),
			new Tile(3267, 3223, 0));
	public static final Area entireLumbyCave = new Area(3137, 9602, 3262, 9536,0);
	public static final boolean isInDesert()
	{
		if(Locations.shantayPassSouthSide.contains(Players.localPlayer()) || 
				Locations.wholeDesert.contains(Players.localPlayer()) || 
				Locations.carpetAreaShantay.contains(Players.localPlayer())) return true;
		return false;
	}
	public static final Area strongholdWolves = new Area(
			new Tile(1868, 5224, 0),
			new Tile(1876, 5224, 0),
			new Tile(1875, 5245, 0),
			new Tile(1867, 5244, 0),
			new Tile(1867, 5230, 0));
	public static final Area strongholdGoblins = new Area(
			new Tile(1866, 5222, 0),
			new Tile(1865, 5225, 0),
			new Tile(1864, 5228, 0),
			new Tile(1860, 5235, 0),
			new Tile(1857, 5235, 0),
			new Tile(1857, 5213, 0),
			new Tile(1866, 5213, 0));
	public static final Area strongholdDoor2 = new Area(1867, 5226, 1865, 5227, 0);
	public static final Area cowsArdy = new Area(
			new Tile(2658, 3340, 0),
			new Tile(2658, 3357, 0),
			new Tile(2674, 3357, 0),
			new Tile(2675, 3355, 0),
			new Tile(2675, 3343, 0),
			new Tile(2670, 3339, 0));
	public static final Area spidersGE = new Area(
			new Tile(3128, 3493, 0),
			new Tile(3137, 3493, 0),
			new Tile(3140, 3491, 0),
			new Tile(3140, 3486, 0),
			new Tile(3138, 3482, 0),
			new Tile(3137, 3474, 0),
			new Tile(3128, 3474, 0));
	public static boolean isInIsleOfSouls() {
		if(forgottenSoulsLvl1.contains(Players.localPlayer()) || 
				forgottenSoulsLvl2.contains(Players.localPlayer()) || 
				forgottenSoulsLvl3.contains(Players.localPlayer()) || 
				isleOfSouls.contains(Players.localPlayer())) return true;
		return false;
	}
	public static final Tile sandcrabSouls1 = new Tile(2283,2804,0);
	public static final Tile sandcrabSouls2 = new Tile(2277,2792,0);
	public static final Area edgevilleSoulsPortal = new Area(
			new Tile(3089, 3483, 0),
			new Tile(3089, 3486, 0),
			new Tile(3077, 3485, 0),
			new Tile(3077, 3478, 0),
			new Tile(3078, 3476, 0),
			new Tile(3079, 3466, 0),
			new Tile(3081, 3464, 0),
			new Tile(3086, 3464, 0),
			new Tile(3086, 3468, 0),
			new Tile(3081, 3467, 0));
	public static final Area guardDogAreaHosidius = new Area(1755, 3606, 1771, 3590, 0);
	public static final Area wholeDesert = new Area(
		    new Tile[] {
		    		new Tile(3301, 3116, 0),
			        new Tile(3309, 3116, 0),
			        new Tile(3320, 3136, 0),
			        new Tile(3330, 3137, 0),
			        new Tile(3336, 3160, 0),
			        new Tile(3346, 3160, 0),
			        new Tile(3354, 3155, 0),
			        new Tile(3354, 3144, 0),
			        new Tile(3364, 3138, 0),
			        new Tile(3371, 3136, 0),
			        new Tile(3377, 3126, 0),
			        new Tile(3384, 3135, 0),
			        new Tile(3394, 3149, 0),
			        new Tile(3394, 3164, 0),
			        new Tile(3409, 3161, 0),
			        new Tile(3419, 3171, 0),
			        new Tile(3441, 3180, 0),
			        new Tile(3537, 3095, 0),
			        new Tile(3530, 2732, 0),
			        new Tile(3365, 2759, 0),
			        new Tile(3325, 2769, 0),
			        new Tile(3324, 2807, 0),
			        new Tile(3202, 2810, 0),
			        new Tile(3203, 2751, 0),
			        new Tile(3124, 2749, 0),
			        new Tile(3101, 2973, 0),
			        new Tile(3153, 3063, 0),
			        new Tile(3198, 3085, 0),
			        new Tile(3202, 3134, 0),
			        new Tile(3276, 3137, 0),
			        new Tile(3286, 3130, 0)
		    }
		);
	public static final Area carpetDestPollni = new Area(3351, 3003, 3351, 3003, 0);
	public static final Area carpetAreaShantay = new Area(3299, 3115, 3316, 3105, 0);
	public static final Area shantayPassArea = new Area(3310, 3117, 3298, 3131, 0);
	public static final Area dreambotFuckedWCGuildSouth = new Area(1614, 3438, 1670, 3513, 0);
	public static final Tile dreambotFuckedWCGuildDestSouth = new Tile(1609, 3438, 0);
	public static final Area dreambotFuckedShayzien = new Area(
			new Tile(1460, 3691, 0),
			new Tile(1470, 3689, 0),
			new Tile(1470, 3678, 0),
			new Tile(1468, 3669, 0),
			new Tile(1468, 3656, 0),
			new Tile(1480, 3651, 0),
			new Tile(1480, 3648, 0),
			new Tile(1458, 3647, 0),
			new Tile(1461, 3665, 0));
	public static final Tile dreambotFuckedShayzienDest = new Tile(1490, 3649, 0); 
	public static final Area dreambotFuckedShayzien2 = new Area(1531, 3640, 1470, 3667, 0);
	public static final Tile dreambotFuckedShayzienDest2 = new Tile(1533,3642, 0); 
	public static final Tile dreambotFuckedShayzienDest3 = new Tile(1562,3664, 0); 
	public static final Area dreambotFuckedShayzien3 = new Area(1535, 3629, 1560, 3657, 0);
	
	public static void leaveDestinationShip()
	{
		Filter<GameObject> gangplankFilter = g ->
			g != null && 
			g.exists() && 
			g.getName().equals("Gangplank") && 
			g.hasAction("Cross") && 
			g.canReach();
		GameObject gangplank = GameObjects.closest(gangplankFilter);
		if(gangplank != null)
		{
			if(gangplank.interact("Cross"))
			{
				MethodProvider.sleepUntil(() -> !isInDestinationShip(), 
					() -> Players.localPlayer().isMoving(),
					Sleep.calculate(2222,2222), 50);
			}
		}
	}
	public static boolean isInDestinationShip() {
		if(shipLandsEnd.contains(Players.localPlayer())) return true;
		if(shipSarimVeos.contains(Players.localPlayer())) return true;
		if(shipPiscVeos.contains(Players.localPlayer())) return true;
		return false;
	}
	public static boolean isInKourend() {
		if(kourendGiantsCaveArea.contains(Players.localPlayer()) || 
				entireKourend.contains(Players.localPlayer()) || 
				kourendCastle2ndFloor.contains(Players.localPlayer()) || 
				kourendCastle3rdFloor.contains(Players.localPlayer())) 
		{
			unlockedKourend = true;
			return true;
		}
		return false;
	}
	public static final Area boarZone = new Area(
			new Tile(1563, 3664, 0),
			new Tile(1562, 3660, 0),
			new Tile(1546, 3664, 0),
			new Tile(1540, 3669, 0),
			new Tile(1535, 3676, 0),
			new Tile(1546, 3682, 0),
			new Tile(1563, 3678, 0));
	public static final int edgeOfTheWorldX = 3904;
	public static boolean isInstanced() {
		if(Players.localPlayer().getX() >= edgeOfTheWorldX) return true;
		return false;
	}
	public static final Area castleWarsTrees = new Area(
			new Tile(2475, 3104, 0),
			new Tile(2465, 3120, 0),
			new Tile(2442, 3129, 0),
			new Tile(2438, 3114, 0),
			new Tile(2436, 3096, 0),
			new Tile(2436, 3081, 0),
			new Tile(2452, 3080, 0),
			new Tile(2469, 3080, 0),
			new Tile(2476, 3089, 0));
	public static final Area dontTeleToGEAreaJustWalk = new Area(
		    new Tile[] {
		        new Tile(3138, 3513, 0),
		        new Tile(3142, 3517, 0),
		        new Tile(3158, 3517, 0),
		        new Tile(3161, 3514, 0),
		        new Tile(3167, 3514, 0),
		        new Tile(3170, 3517, 0),
		        new Tile(3189, 3517, 0),
		        new Tile(3199, 3507, 0),
		        new Tile(3199, 3499, 0),
		        new Tile(3200, 3495, 0),
		        new Tile(3201, 3459, 0),
		        new Tile(3213, 3458, 0),
		        new Tile(3228, 3431, 0),
		        new Tile(3228, 3420, 0),
		        new Tile(3149, 3421, 0),
		        new Tile(3125, 3464, 0)
		    }
		);
	public static final Area teleGE = new Area(3160, 3474, 3169, 3482, 0);
	public static final Area clickableGEArea = new Area(
			new Tile(3169, 3480, 0),
			new Tile(3160, 3480, 0),
			new Tile(3155, 3486, 0),
			new Tile(3155, 3493, 0),
			new Tile(3161, 3498, 0),
			new Tile(3168, 3498, 0),
			new Tile(3174, 3493, 0),
			new Tile(3174, 3486, 0));
	public static Tile GE = new Tile(3165,3487,0);
	
	
	

	
}
