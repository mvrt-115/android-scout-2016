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
    public static final String INTENT_EXTRA_MATCHDATA = "com.mvrt.scout.intent.MATCHDATA";

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
}
