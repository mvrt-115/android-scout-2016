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

    public static final String PREFS_TOURNAMENT_KEY = "com.mvrt.scout.prefs.TOURNAMENTKEY";
    public static final String PREFS_TOURNAMENT_DEFAULT = "SAMP";
    public static final String PREFS_SCOUTID_KEY = "com.mvrt.scout.prefs.SCOUTID";
    public static final String PREFS_ALLIANCE_KEY = "com.mvrt.scout.prefs.ALLIANCE";

    public static final String SHARED_PREFS_NAME_SCOUT = "com.mvrt.scout.prefs";
    public static final String SHARED_PREFS_NAME_SUPER = "com.mvrt.super.prefs";

    public static final String NDEF_RECORD_DOMAIN = "mvrt.com";
    public static final String NDEF_RECORD_TYPE_MATCHINFO = "MATCHINFO";
    public static final String NDEF_DATA_PATH = "/mvrt.com:MATCHINFO";

    public static final char ALLIANCE_RED = 'r';
    public static final char ALLIANCE_BLUE = 'b';

    public static final String JSON_DATA_AUTON = "atn";
    public static final String JSON_DATA_TELEOP = "tlp";
    public static final String JSON_DATA_SHOOTING = "sht";
    public static final String JSON_DATA_POSTGAME = "pst";
    public static final String JSON_DATA_MATCHINFO = "minfo";
    public static final String JSON_DATA_SCOUTID = "sctid";

    public static final String JSON_SHOOTING_SHOTS = "sht";

    public static final String JSON_POSTGAME_DISABLED = "dsbld";
    public static final String JSON_POSTGAME_INTERFERES = "intrfr";
    public static final String JSON_POSTGAME_DEFENSE = "dfns";
    public static final String JSON_POSTGAME_INTAKE = "intk";
    public static final String JSON_POSTGAME_COMMENTS = "cmnt";

    public static final String JSON_TELEOP_INTAKE = "in";
    public static final String JSON_TELEOP_CLIMBRESULT = "clbr";
    public static final String JSON_TELEOP_CLIMBTIME = "clbt";
    public static final String JSON_TELEOP_CROSSINGS = "crsn";

    public static final String CLIMB_NO = "n";
    public static final String CLIMB_PROGRESS = "c";
    public static final String CLIMB_SUCCESS = "y";
    public static final String CLIMB_FAIL = "f";

    public static final String DEFENSE_PORTCULLIS = "a1";
    public static final String DEFENSE_CHEVALDEFRISE = "a2";
    public static final String DEFENSE_MOAT = "b1";
    public static final String DEFENSE_RAMPART = "b2";
    public static final String DEFENSE_DRAWBRIDGE = "c1";
    public static final String DEFENSE_SALLYPORT = "c2";
    public static final String DEFENSE_ROCKWALL = "d1";
    public static final String DEFENSE_ROUGHTERRAIN = "d2";
    public static final String DEFENSE_LOWBAR = "e1";

    public static final String JSON_AUTON_REACH = "atr";
    public static final String JSON_AUTON_CROSSINGS = "atc";
    public static final String JSON_AUTON_INTAKE = "ati";
    public static final String JSON_AUTON_SHOOT = "ats";

}
