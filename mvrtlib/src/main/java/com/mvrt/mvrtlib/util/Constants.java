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

    public static final String INTENT_ACTION_SCOUT = "com.mvrt.scout.intent.SCOUT";

    public static final int REQUEST_QR_SCAN = 10;

    public static final String FBCONFIG_TOURN_KEY = "TOURNAMENT_KEY";

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
    public static final String JSON_POSTGAME_HIGHACCURACY = "Rha";
    public static final String JSON_POSTGAME_GEARACCURACY = "Rga";
    public static final String JSON_POSTGAME_PILOT = "Rp";
    public static final String JSON_POSTGAME_DRIVING = "Rdr";
    public static final String JSON_POSTGAME_DEFENSE = "Rdf";
    public static final String JSON_POSTGAME_ROTORS = "Rr";
    public static final String JSON_POSTGAME_COMMENTS = "cmnt";

    public static final String JSON_TELEOP_CLIMBRESULT = "Tcr";
    public static final String JSON_TELEOP_CLIMBTIME = "Tct";
    public static final String JSON_TELEOP_TOUCHPADTRIGGERED = "Tt";
    public static final String JSON_TELEOP_GEARSTAKEN = "Tgt";
    public static final String JSON_TELEOP_GEARSPLACED = "Tgp";
    public static final String JSON_TELEOP_HIGHCYCLES = "Th";
    public static final String JSON_TELEOP_LOWCYCLES = "Tl";
    public static final String JSON_TELEOP_HOPPERCYCLES = "Thp";

    public static final String CLIMB_NO = "n";
    public static final String CLIMB_PROGRESS = "c";
    public static final String CLIMB_SUCCESS = "y";
    public static final String CLIMB_FAIL = "f";


    public static final String JSON_AUTON_HIGH = "Ah";
    public static final String JSON_AUTON_LOW = "Al";
    public static final String JSON_AUTON_MOBILITY = "Am";
    public static final String JSON_AUTON_STARTGEARS = "Asg";
    public static final String JSON_AUTON_STARTBALLS = "Asb";
    public static final String JSON_AUTON_GEARS = "Ag";
    public static final String JSON_AUTON_HOPPER = "Ah";
    public static final String JSON_AUTON_GROUNDINTAKE = "Agi";

}
