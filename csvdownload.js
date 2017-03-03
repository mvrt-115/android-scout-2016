var database = firebase.database();
var ref = database.ref();

function loadPitData(){
    ref.child('pit').once('value', function(snapshot){
        var pitData = [];
        snapshot.forEach(function(childSnapshot){
            pitData.push(getPit(childSnapshot));
        });
        saveCSV(getPitHeaders(), pitData, 'pitDownloadLink', 'pitscout.csv');
    });
}

function loadFirebaseData(){
    ref.child('matches').once('value', function(snapshot){
        var scoutData = [];
        snapshot.forEach(function(childSnapshot){
            scoutData.push(getScoutData(childSnapshot));
        });
        saveCSV(getScoutHeaders(), scoutData, 'scoutDownloadLink', 'scout.csv');
    });
}

function getScoutHeaders(){
    return ['Team','Tournament','Match','Alliance',
    "Auton High", "Auton Low", "Mobility", "Auton Start Gear",
    "Auton Start Balls", "Auton Gears", "Auton Hopper", "Auton Ground Intake",
    "Climb Result", "Climb Time", "Touchpad Triggered", "Gears Retrieved", "Gears Placed", "Gears Dropped",
     "High Goal Fuel", "Low Goal Fuel", "Hoppers Triggered",
    "Disabled", "Interfere with others", "High Accuracy Rating", "Gear Accuracy Rating",
     "Gear Cycle Time Rating", "Pilot Rating", "Driver Rating", "Defense Rating", "Rotors Spinning", "Comments"];
}

function getScoutData(snapshot){
    var team = snapshot.child('team').val();
    var tournament = snapshot.child('tournament').val();
    var match = snapshot.child('match').val();
    var alliance = snapshot.child('alliance').val();

    var a = snapshot.child('A');
    var autonHigh = a.child('Ah').val();
    var autonLow = a.child('Al').val();
    var autonMobility = a.child('Am').val();
    var autonStartGears = a.child('Asg').val();
    var autonStartBalls = a.child('Asb').val();
    var autonGears = a.child('Ag').val();
    var autonHopper = a.child('Ahp').val();
    var autonGroundIntake = a.child('Agi').val();

    var t = snapshot.child('T');
    var climbResult = t.child('Tcr').val();
    var climbTime = t.child('Tct').val();
    var touchpad = t.child('Tt').val();

    var gearsTaken = t.child('Tgt').val();
    var gearsPlaced = t.child('Tgp').val();
    var gearsDropped = t.child('Tgd').val();

    var highBalls = t.child('Th').val();
    var lowBalls = t.child('Tl').val();
    var hopperCycles = t.child('Thp').val();

    var p = snapshot.child('P');
    var disabled = p.child('dsbld').val();
    var interferes = p.child('intr').val();
    var highAccuracy = p.child('Rha').val();
    var gearAccuracy = p.child('Rga').val();
    var gearCycleTime = p.child('Rgt').val();
    var pilotRating = p.child('Rp').val();
    var driverRating = p.child('Rdr').val();
    var defenseRating = p.child('Rdf').val();
    var rotors = p.child('Rr').val();
    var comments = p.child('cmnt');

    return [team, tournament, match, alliance,
    autonHigh, autonLow, autonMobility, autonStartGears, autonStartBalls, autonGears, autonHopper, autonGroundIntake,
    climbResult, climbTime, touchpad, gearsTaken, gearsPlaced, gearsDropped, highBalls, lowBalls, hopperCycles,
    disabled, interferes, highAccuracy, gearAccuracy, gearCycleTime, pilotRating, driverRating, defenseRating, rotors, comments];
}

function getPitHeaders(){
    return ['Team',
        'Auton Break', 'Auton High', 'Auton Low', 'Auton Reach',
        'High Goal', 'Low Goal', 'Intake', 'Strategy',
        'Challenge', 'Scale',
        'Low Bar', 'Cheval de Frise', 'Portcullis',
        'Drawbridge', 'Sallyport', 'Moat', 'Ramparts',
        'Rock Wall', 'Rough Terrain',
        'DT # Motors', 'DT # Wheels', 'DT Wheel Type', 'Weight', 'Driver Regionals'];
}

function getPit(snapshot){
    var val = snapshot.val();

    var team = val['Team'];

    var autonBreak = val['Auton Break'];
    var autonHigh = val['Auton High'];
    var autonLow = val['Auton Low'];
    var autonReach = val['Auton Reach'];

    var highGoal = val['High Goal'];
    var lowGoal = val['Low Goal'];
    var intake = val['Intake'];
    var strategy = val['Strategy'];

    var challenge = val['Challenge'];
    var climber = val['Climber'];

    var lowBar = val['Low Bar'];
    var cheval = val['Cheval'];
    var portcullis = val['Portcullis'];
    var drawbridge = val['Drawbridge'];
    var sallyport = val['Sally Port'];
    var moat = val['Moat'];
    var ramparts = val['Ramparts'];
    var rockWall = val['Rock Wall'];
    var roughTerrain = val['Rough Terrain'];

    var dtMotors = val['Number of Motors'];
    var dtWheels = val['Number of Wheels'];
    var wheelType = val['Type of Wheels'];
    var weight = val['Weight'];

    var driverRegionals = val['Driver Regionals'];


    return [team,
        autonBreak, autonHigh, autonLow, autonReach,
        highGoal, lowGoal, intake, strategy,
        challenge, climber,
        lowBar, cheval, portcullis, drawbridge, sallyport, moat, ramparts, rockWall, roughTerrain,
        dtMotors, dtWheels, wheelType, weight, driverRegionals];
}

function saveCSV(csvHeaders, csvData, linkId, filename){
    var csvString = getCSVString(csvHeaders, csvData);
    var blob = new Blob([csvString], {type: 'text/csv;charset=utf-8;'});

    var link = document.getElementById(linkId);
    if (link.download !== undefined) {
        // Browsers that support HTML5 download attribute
        var url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", filename);
    }
}

function getCSVString(csvHeaders, csvData){
    var csvFile = '';
    csvFile += csvHeaders.join(',') + '\n';
    var csvContent = [];
    for(data in csvData){
        csvContent.push(csvData[data].join(',').replace(/(\r\n|\n|\r)/gm,""));
    }
    csvFile += csvContent.join('\n');
    return csvFile;
}

document.addEventListener("DOMContentLoaded", function(event) {

});

loadFirebaseData();
loadPitData();
