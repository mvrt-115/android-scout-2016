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
        var shotData = [];
        var crossData = [];
        snapshot.forEach(function(childSnapshot){
            var shots = childSnapshot.child('sht').val();
            if(shots){
                console.log('shots: ' + JSON.stringify(shots));
                shots.shta.forEach(function(shot){
                    shotData.push(getShot(shot,childSnapshot,true));
                });
                shots.shtt.forEach(function(shot){
                    shotData.push(getShot(shot,childSnapshot,false));
                });
            }
            var crossing = childSnapshot.child('tlp/crsn').val()
            if(crossing){
                crossing = crossing.replace(/\[|\]| /g, '');
                var crossings = crossing.split(',');
                crossings.forEach(function(cross){
                    console.log(cross);
                    var defense = cross.split(':')[0];
                    var time = parseInt(cross.split(':')[1]);
                    crossData.push(getCrossing(defense,time,childSnapshot,false));
                });
                var cross = childSnapshot.child('atn/atc').val();
                if(cross){
                    crossData.push(getCrossing(cross,10000,childSnapshot,true));
                }
            }
            if(childSnapshot.child('tlp').val()){
                scoutData.push(getScoutData(childSnapshot));
            }
        });
        saveCSV(getScoutHeaders(), scoutData, 'scoutDownloadLink', 'scout.csv');
        saveCSV(getCrossingHeaders(), crossData, 'crossDownloadLink', 'crossing.csv');
        saveCSV(getShotHeaders(), shotData, 'shotDownloadLink', 'shots.csv');
    });
}

function getCrossingHeaders(){
    return ['Team', 'Tournament', 'Match', 'Alliance',
        'Defense', 'Time', 'Auton'];
}

function getCrossing(defense, time, snapshot, auton){
    var team = snapshot.child('team').val();
    var tournament = snapshot.child('tournament').val();
    var match = snapshot.child('match').val();
    var alliance = snapshot.child('alliance').val();

    return [team, tournament, match, alliance,
        defense, time, auton];
}

function getScoutHeaders(){
    return ['Team','Tournament','Match','Alliance',
        'Climb','Climb Time','Intake','Challenge',
        'Defense Rating','Intake Rating',
        'Auton Intake', 'Auton Crossing', 'Auton Reach'];
}

function getScoutData(snapshot){
    var climb = snapshot.child('tlp/clbr').val();
    var climbTime = snapshot.child('tlp/clbt').val();
    var intake = snapshot.child('tlp/in').val();
    var challenge = snapshot.child('tlp/chl').val();

    var defenseRating = snapshot.child('pst/dfns').val();
    var intakeRating = snapshot.child('pst/intk').val();

    var autonIntake = snapshot.child('atn/ati').val();
    var autonCrossing = snapshot.child('atn/atc').val();
    var autonReach = snapshot.child('atn/atr').val();

    var team = snapshot.child('team').val();
    var tournament = snapshot.child('tournament').val();
    var match = snapshot.child('match').val();
    var alliance = snapshot.child('alliance').val();

    return [team, tournament, match, alliance,
        climb, climbTime, intake, challenge,
        defenseRating, intakeRating,
        autonIntake, autonCrossing, autonReach];
}

function getShotHeaders(){
    return ['Made', 'X', 'Y', 'High', 'Auton',
    'Team', 'Tournament', 'Match', 'Alliance', 'MatchInfo'];
}

function getShot(shot, snapshot, auton){
    var make = /y\(/.test(shot);
    var high = /\)h/.test(shot);
    var coordString = shot.match(/\d+,\d+/)[0];
    console.log(coordString);
    var coords = coordString.split(/,/);
    var x = parseInt(coords[0]);
    var y = parseInt(coords[1]);

    var team = snapshot.child('team').val();
    var tournament = snapshot.child('tournament').val();
    var match = snapshot.child('match').val();
    var alliance = snapshot.child('alliance').val();
    var matchInfo = snapshot.child('minfo').val();

    matchInfo = matchInfo.substring(0, matchInfo.indexOf('('));

    return [make, x, y, high, auton,
        team, tournament, match, alliance, matchInfo];
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
