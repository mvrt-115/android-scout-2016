package com.mvrt.mvrtlib.util;

/**
 * @author Akhil Palla
 */
public class Constants {

    public static final String INTENT_QR = "com.google.zxing.client.android.SCAN";
    public static final String INTENT_QR_SCANRESULT_KEY = "SCAN_RESULT";
    public static final String INTENT_QR_SCANMODE_KEY = "SCAN_MODE";
    public static final String INTENT_QR_SCANMODE = "QR_CODE_MODE";

    public static final String INTENT_EXTRA_MATCHINFO = "com.mvrt.scout.intent.MATCHINFO";
    public static final String INTENT_EXTRA_FILENAME = "com.mvrt.scout.intent.FILENAME";
    public static final String INTENT_EXTRA_SCOUTID = "com.mvrt.scout.intent.SCOUTID";
    public static final String INTENT_EXTRA_BUTTONID = "com.mvrt.scout.intent.BUTTONID";

    public static final String INTENT_ACTION_SCOUT = "com.mvrt.scout.intent.SCOUT";

    public static final int REQUEST_QR_SCAN = 10;

    public static final String FBCONFIG_TOURN_KEY = "TOURNAMENT_KEY";
    public static final String TOURNAMENT_KEY = "IDBO";

    public static final String PREFS_SCOUTID_KEY = "com.mvrt.scout.prefs.SCOUTID";
    public static final String PREFS_ALLIANCE_KEY = "com.mvrt.scout.prefs.ALLIANCE";

    public static final String SHARED_PREFS_NAME_SCOUT = "com.mvrt.scout.prefs";
    public static final String SHARED_PREFS_NAME_SUPER = "com.mvrt.super.prefs";

    public static final char ALLIANCE_RED = 'r';
    public static final char ALLIANCE_BLUE = 'b';

    public static final String JSON_DATA_AUTON = "A";
    public static final String JSON_DATA_TELEOP = "T";
    public static final String JSON_DATA_POSTGAME = "P";
    public static final String JSON_DATA_MATCHINFO = "minfo";
    public static final String JSON_DATA_SCOUTID = "sctid";


    public static final String JSON_POSTGAME_DISABLED = "dsbld";
    public static final String JSON_POSTGAME_INTERFERES = "intr";
    public static final String JSON_POSTGAME_SPEED = "Rs";
    public static final String JSON_POSTGAME_CUBEACCURACY = "Rca";
    public static final String JSON_POSTGAME_CUBECYCLETIME = "Rct";
    public static final String JSON_POSTGAME_DRIVING = "Rdr";
    public static final String JSON_POSTGAME_DEFENSE = "Rdf";
    public static final String JSON_POSTGAME_CLIMBS = "Rc";
    public static final String JSON_POSTGAME_BOOST = "Rb";
    public static final String JSON_POSTGAME_FORCE = "Rf";
    public static final String JSON_POSTGAME_LEVITATE = "Rl";

    public static final String JSON_POSTGAME_COMMENTS = "cmnt";

    public static final String JSON_TELEOP_CLIMBRESULT = "Tcr";
    public static final String JSON_TELEOP_CLIMBTIME = "Tct";
    public static final String JSON_TELEOP_OPPSWITCH = "Tos";
    public static final String JSON_TELEOP_SCALE = "Tsc";
    public static final String JSON_TELEOP_SWITCH = "Tsw";
    public static final String JSON_TELEOP_VAULT = "Tsv";
    public static final String JSON_TELEOP_PARKED = "Tpk";

    public static final String CLIMB_NO = "n";
    public static final String CLIMB_PROGRESS = "c";
    public static final String CLIMB_SUCCESS = "y";
    public static final String CLIMB_FAIL = "f";

    public static final String JSON_AUTON_SCALE = "Asc";
    public static final String JSON_AUTON_SWITCH = "Asw";
    public static final String JSON_AUTON_MOBILITY = "Amb";
    public static final String JSON_AUTON_STARTCUBE = "Ascu";
    public static final String JSON_AUTON_STARTLEFT = "Asl";
    public static final String JSON_AUTON_STARTCENTER = "Asce";
    public static final String JSON_AUTON_STARTRIGHT = "Asr";

}
