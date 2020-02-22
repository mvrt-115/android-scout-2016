package com.mvrt.mvrtlib.util;

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
    public static final String TOURNAMENT_KEY = "CASF";

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
    public static final String JSON_POSTGAME_CARGOACCURACY = "Rha";
    public static final String JSON_POSTGAME_HATCHACCURACY = "Rga";
    public static final String JSON_POSTGAME_CYCLETIME = "Rgt";
    public static final String JSON_POSTGAME_PILOT = "Rp";
    public static final String JSON_POSTGAME_DRIVING = "Rdr";
    public static final String JSON_POSTGAME_DEFENSE = "Rdf";
    public static final String JSON_POSTGAME_ROTORS = "Rr";
    public static final String JSON_POSTGAME_COMMENTS = "cmnt";

    //old stuff
    public static final String JSON_TELEOP_CLIMBRESULT = "Tcr";
    public static final String JSON_TELEOP_CLIMBTIME = "Tct";
    public static final String JSON_TELEOP_TOUCHPADTRIGGERED = "Tt";
    public static final String JSON_TELEOP_GEARSTAKEN = "Tgt";
    public static final String JSON_TELEOP_GEARSPLACED = "Tgp";
    public static final String JSON_TELEOP_GEARSDROPPED = "Tgd";
    public static final String JSON_TELEOP_HIGHBALLS = "Th";
    public static final String JSON_TELEOP_LOWBALLS = "Tl";
    public static final String JSON_TELEOP_HOPPERCYCLES = "Thp";

    //added 2/13/2019
    public static final String JSON_TELEOP_ROCKETCARGOLEVEL1 = "Trc1";
    public static final String JSON_TELEOP_ROCKETCARGOLEVEL2 = "Trc2";
    public static final String JSON_TELEOP_ROCKETCARGOLEVEL3 = "Trc3";
    public static final String JSON_TELEOP_TOTALROCKETCARGO = "Ttrc";
    public static final String JSON_TELEOP_ROCKETHATCHLEVEL1 = "Trh1";
    public static final String JSON_TELEOP_ROCKETHATCHLEVEL2 = "Trh2";
    public static final String JSON_TELEOP_ROCKETHATCHLEVEL3 = "Trh3";
    public static final String JSON_TELEOP_TOTALROCKETHATCH = "Ttrh";
    public static final String JSON_TELEOP_CARGOSHIPCARGO = "Tcsc";
    public static final String JSON_TELEOP_CARGOSHIPHATCH = "Tcsh";
    public static final String JSON_TELEOP_LEVEL1 = "Tl1";
    public static final String JSON_TELEOP_LEVEL2 = "Tl2";
    public static final String JSON_TELEOP_LEVEL3 = "Tl3";

    public static final String CLIMB_NO = "n";
    public static final String CLIMB_PROGRESS = "c";
    public static final String CLIMB_CONFIRM = "cc";
    public static final String CLIMB_SUCCESS = "y";
    public static final String CLIMB_FAIL = "f";

    //added 2/13/2019
    public static final String JSON_SANDSTORM_STARTHATCH = "Ssh";
    public static final String JSON_SANDSTORM_STARTCARGO = "Ssc";
    public static final String JSON_SANDSTORM_STARTNONE = "Ssn";
    public static final String JSON_SANDSTORM_LEVEL1 = "Ssl1";
    public static final String JSON_SANDSTORM_LEVEL2 = "Ssl2";
    public static final String JSON_SANDSTORM_HAB = "Sshab";
    public static final String JSON_SANDSTORM_ROCKETCARGO1 = "Ssrc1";
    public static final String JSON_SANDSTORM_ROCKETCARGO2 = "Ssrc2";
    public static final String JSON_SANDSTORM_ROCKETCARGO3 = "Ssrc3";
    public static final String JSON_SANDSTORM_TOTALROCKETCARGO = "Sstrc";
    public static final String JSON_SANDSTORM_ROCKETHATCH1 = "Ssrh1";
    public static final String JSON_SANDSTORM_ROCKETHATCH2 = "Ssrh2";
    public static final String JSON_SANDSTORM_ROCKETHATCH3 = "Ssrh3";
    public static final String JSON_SANDSTORM_TOTALROCKETHATCH = "Sstrh";
    public static final String JSON_SANDSTORM_CARGOSHIPCARGO = "Sscsc";
    public static final String JSON_SANDSTORM_CARGOSHIPHATCH = "Sscsh";

    //old stuff
    public static final String JSON_AUTON_HIGH = "Ah";
    public static final String JSON_AUTON_LOW = "Al";
    public static final String JSON_AUTON_MOBILITY = "Am";
    public static final String JSON_AUTON_STARTGEARS = "Asg";
    public static final String JSON_AUTON_STARTBALLS = "Asb";
    public static final String JSON_AUTON_GEARS = "Ag";
    public static final String JSON_AUTON_HOPPER = "Ahp";
    public static final String JSON_AUTON_GROUNDINTAKE = "Agi";


    // added 2/8/20

    public static final String JSON_TELEOP_BOTTOM = "JSON_TELEOP_BOTTOM";
    public static final String JSON_TELEOP_TOTAL = "JSON_TELEOP_TOTAL";
    public static final String JSON_TELEOP_ROTATION = "JSON_TELEOP_ROTATION";
    public static final String JSON_TELEOP_POSITION = "JSON_TELEOP_POSITION";
    public static final String JSON_TELEOP_TRENCH = "JSON_TELEOP_TRENCH";
    public static final String JSON_TELEOP_DEFENSE_YES = "JSON_TELEOP_DEFENSE_YES";
    public static final String JSON_TELEOP_DEFENSE_NO = "JSON_TELEOP_DEFENSE_NO";

    // endgame
    public static final String JSON_ENDGAME_CLIMBRESULT = "JSON_ENDGAME_CLIMBRESULT";
    public static final String JSON_ENDGAME_CLIMBTIME = "JSON_ENDGAME_CLIMBTIME";
    public static final String JSON_ENDGAME_HANG_ATTEMPTED = "JSON_ENDGAME_HANG_ATTEMPTED";
    public static final String JSON_ENDGAME_HANG_SUCCESS = "JSON_ENDGAME_HANG_SUCCESS";
    public static final String JSON_ENDGAME_HANG_FAILED = "JSON_ENDGAME_HANG_FAILED";
    public static final String JSON_ENDGAME_LEVEL_ATTEMPTED = "JSON_ENDGAME_LEVEL_ATTEMPTED";
    public static final String JSON_ENDGAME_LEVEL_SUCCESS = "JSON_ENDGAME_LEVEL_SUCCESS";
    public static final String JSON_ENDGAME_LEVEL_FAILED = "JSON_ENDGAME_LEVEL_FAILED";
    public static final String JSON_ENDGAME_PARKED = "JSON_ENDGAME_PARKED";
    public static final String JSON_ENDGAME_STUCK = "JSON_ENDGAME_STUCK";
    public static final String JSON_ENDGAME_DISABLED = "JSON_ENDGAME_DISABLED";

    public static final String PitUrl = "http://mvrtscouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/robot/post?teamNum=";
    public static final String ScoutUrl = "http://mvrtscouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/match/post";
    public static final String SuperscoutUrl = "http://mvrtscouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/match/post";

}
