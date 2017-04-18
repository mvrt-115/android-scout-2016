var database = firebase.database();
var ref = database.ref();

function loadFirebaseData(){

    ref.child('matches').orderByChild('match').once('value', function(snapshot){
        var scoutData = [];
        var tableData = [];
        snapshot.forEach(function(childSnapshot){
            scoutData.push(getScoutData(childSnapshot));
            tableData.push(getTableData(childSnapshot));
        });
        populateTable(tableData);
        saveCSV(getScoutHeaders(), scoutData, 'scoutDownloadLink', 'scout.csv');
    });


}

function fixDB(){
  ref.child('matches').once('value', function(snapshot){
    snapshot.forEach(function(childSnapshot){
        const oldMI = childSnapshot.val().matchinfo;
        const newMI = oldMI.replace('AZFL', 'CASJ');
        childSnapshot.ref.child('matchinfo').set(newMI);
        childSnapshot.ref.child('minfo').set(newMI);
    });
  });
}

function getData(team){
  var gearsPlaced = [];
  var superComments = [];

  ref.child('matches').orderByChild('team').equalTo(115).once('value', function(snapshot){
      var data = snapshot.val();
      for(key in data) {
        var entry = data[key];
        if(entry['T']) {
          gearsPlaced.push(entry['T']['Tgp']);
          if(entry['super']) superComments.push(entry['super']);
          if(entry['P']['cmnt']) superComments.push(entry['P']['cmnt']);
        }
      }
      console.log(gearsPlaced);
      console.log(superComments);
  });
}

function getTableDataHeaders(){
  return ['Match', 'Team', 'Super Uploaded', 'Scout Data', 'Match Info', 'Scout ID'];
}

function getTableData(snapshot){
  var data = snapshot.val();

  if(data.match == undefined){
    data.match = data.minfo.match(/\d+(?=@)/g);
  }

  if(data.team == undefined){
    var teams = '' + data.minfo.match(/(\d+,?)+(?=])/g);
    teams = teams.split(',');
    if(teams.length == 1)data.team = teams[0];
    else data.team = teams[data.sctid];
  }

  return [data.match, data.team, data.tournament != undefined, data['T'] != undefined, data.minfo, data.sctid];
}

function getScoutHeaders(){
    return ['Team','Tournament','Match','Alliance',
    'Auton High', 'Auton Low', 'Mobility', 'Auton Start Gear',
    'Auton Start Balls', 'Auton Gears', 'Auton Hopper', 'Auton Ground Intake',
    'Climb Result', 'Climb Time', 'Touchpad Triggered', 'Gears Retrieved', 'Gears Placed', 'Gears Dropped',
     'High Goal Fuel', 'Low Goal Fuel', 'Hoppers Triggered',
    'Disabled', 'Interfere with others', 'High Accuracy Rating', 'Gear Accuracy Rating',
     'Gear Cycle Time Rating', 'Pilot Rating', 'Driver Rating', 'Defense Rating', 'Rotors Spinning',
     'Comments', 'Match Info', 'Scout ID'];
}

function getScoutData(snapshot){
    var data = snapshot.val();

    var team = data.team;
    var tournament = data.tournament;
    var match = data.match;
    var alliance = data.alliance;

    if(match == undefined){
      match = data.minfo.match(/\d+(?=@)/g);
    }

    if(team == undefined){
      var teams = '' + data.minfo.match(/(\d+,?)+(?=])/g);
      teams = teams.split(',');
      if(teams.length == 1)team = teams[0];
      else team = teams[data.sctid];
    }

    var a = data['A'];
    if(a == undefined)return [];
    var autonHigh = a['Ah'];
    var autonLow = a['Al'];
    var autonMobility = a['Am'];
    var autonStartGears = a['Asg'];
    var autonStartBalls = a['Asb'];
    var autonGears = a['Ag'];
    var autonHopper = a['Ahp'];
    var autonGroundIntake = a['Agi'];

    var t = data['T'];
    var climbResult = t['Tcr'];
    var climbTime = t['Tct'];
    var touchpad = t['Tt'];

    var gearsTaken = t['Tgt'];
    var gearsPlaced = t['Tgp'];
    var gearsDropped = t['Tgd'];

    var highBalls = t['Th'];
    var lowBalls = t['Tl'];
    var hopperCycles = t['Thp'];

    var p = data['P'];
    var disabled = p['dsbld'];
    var interferes = p['intr'];
    var highAccuracy = p['Rha'];
    var gearAccuracy = p['Rga'];
    var gearCycleTime = p['Rgt'];
    var pilotRating = p['Rp'];
    var driverRating = p['Rdr'];
    var defenseRating = p['Rdf'];
    var rotors = p['Rr'];
    var comments = p['cmnt'].replace(',', '');

    var matchInfo = data.minfo.replace(/,/g, '|');
    var scoutID = data.sctid;

    return [team, tournament, match, alliance,
    autonHigh, autonLow, autonMobility, autonStartGears, autonStartBalls, autonGears, autonHopper, autonGroundIntake,
    climbResult, climbTime, touchpad, gearsTaken, gearsPlaced, gearsDropped, highBalls, lowBalls, hopperCycles,
    disabled, interferes, highAccuracy, gearAccuracy, gearCycleTime, pilotRating, driverRating, defenseRating, rotors,
    comments, matchInfo, scoutID];
}

function populateTable(data){
  var table = document.getElementById('dataTableBody');
  if(table == undefined)return;

  for(row in data){
      var rowElement = table.insertRow();
      for(item in data[row]){
        var cell = rowElement.insertCell();
        cell.innerText = data[row][item];
      }
  }

}

function saveCSV(csvHeaders, csvData, linkId, filename){
    var csvString = getCSVString(csvHeaders, csvData);
    var blob = new Blob([csvString], {type: 'text/csv;charset=utf-8;'});

    var link = document.getElementById(linkId);
    if (link.download !== undefined) {
        // Browsers that support HTML5 download attribute
        var url = URL.createObjectURL(blob);
        link.setAttribute('href', url);
        link.setAttribute('download', filename);
        link.classList.remove('disabled');
    }
}

function getCSVString(csvHeaders, csvData){
    var csvFile = '';
    csvFile += csvHeaders.join(',') + '\n';
    var csvContent = [];
    for(data in csvData){
        csvContent.push(csvData[data].join(',').replace(/(\r\n|\n|\r)/gm,''));
    }
    csvFile += csvContent.join('\n');
    return csvFile;
}

loadFirebaseData();
