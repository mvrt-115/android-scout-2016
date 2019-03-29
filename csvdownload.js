var database = firebase.database();
var ref = database.ref();

function loadFirebaseData(){

    $('#progressbar').attr('aria-valuenow', 10).css('width', 10 + '%');
    progress = 10;

    ref.child('matches').orderByChild('match').once('value', function(snapshot){
        var scoutData = [];
        var tableData = [];

        snapshot.forEach(function(childSnapshot){
            var sd = getScoutData(childSnapshot);
            if(sd != null)scoutData.push(sd);
            tableData.push(getTableData(childSnapshot));
        });

        populateTable(tableData);
        saveCSV(getScoutHeaders(), scoutData, 'scoutDownloadLink', 'scout.csv');

        progress = 100;
        $('#progressbar').attr('aria-valuenow', progress).css('width', progress + '%');

        setTimeout(function() {
            $('#table').show();
            $('#progress').hide();
            $('#scoutDownloadLink').show();
        }, 500);
    });

    id = setInterval(function() {
        progress += 30;
        $('#progressbar').attr('aria-valuenow', progress).css('width', progress + '%');
        if(progress > 60) clearInterval(id);
        console.log('interval progress ' + progress);
    }, 1500);


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

// function getData(team){
//   var gearsPlaced = [];
//   var superComments = [];
//
//   ref.child('matches').orderByChild('team').equalTo(115).once('value', function(snapshot){
//       var data = snapshot.val();
//       for(key in data) {
//         var entry = data[key];
//         if(entry['T']) {
//           gearsPlaced.push(entry['T']['Tgp']);
//           if(entry['super']) superComments.push(entry['super']);
//           if(entry['P']['cmnt']) superComments.push(entry['P']['cmnt']);
//         }
//       }
//       console.log(gearsPlaced);
//       console.log(superComments);
//   });
// }

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
    return ['team','tournament','match','alliance',

    'sandstorm_start_hatch', 'sandstorm_start_cargo', 'sandstorm_start_lvl_1',  'sandstorm_start_lvl_2',
    'sandstorm_hab', 'sandstorm_rocket_cargo_1', 'sandstorm_rocket_cargo_2', 'sandstorm_rocket_cargo_3', 
    'sandstorm_total_rocket_cargo', 'sandstorm_rocket_hatch_1', 'sandstorm_rocket_hatch_2', 'sandstorm_rocket_hatch_3',
    'sandstorm_total_rocket_hatch', 'sandstorm_cargoship_cargo', 'sandstorm_cargoship_hatch',

    'teleop_rocket_cargo_1', 'teleop_rocket_cargo_2', 'teleop_rocket_cargo_3', 'teleop_total_rocket_cargo',
    'teleop_rocket_hatch_1', 'teleop_rocket_hatch_2', 'teleop_rocket_hatch_3', 'teleop_total_rocket_hatch',
    'teleop_cargoship_cargo', 'teleop_cargoship_hatch', 'teleop_climb_1', 'teleop_climb_2', 'teleop_climb_3',

    'rating_cargo_accuracy', 'rating_hatch_accuracy', 'rating_cycles', 'rating_defense',

    'disabled', 'interfere', 'comment_scout', 'comment_super',

    'match_id', 'scout_id' ];
}

function getScoutData(snapshot){

    var data = snapshot.val();

    var team = data.team;
    var tournament = data.tournament;
    var match = data.match;
    var alliance = data.alliance;

    if(match == undefined){
      if(data.minfo)match = data.minfo.match(/\d+(?=@)/g);
      else if(data.matchinfo)match = data.matchinfo.match(/\d+(?=@)/g);
      else match = snapshot.key.match(/\d+(?=@)/g);
    }

    if(team == undefined){
      var teams = '' + data.minfo.match(/(\d+,?)+(?=])/g);
      teams = teams.split(',');
      if(teams.length == 1)team = teams[0];
      else team = teams[data.sctid];
    }

    var matchInfo = snapshot.key.replace(/,/g, ';');
    if(data.minfo)matchInfo = data.minfo.replace(/,/g, ';');
    else if(data.matchinfo)matchInfo = data.matchinfo.replace(/,/g, ';');

    var scoutID = data.sctid;

    var A = data['A'];
    if(A == undefined)return null;

    var T = data['T'];
    var P = data['P'];

    return [ team, tournament, match, alliance,
      A.Ssh, A.Ssc, A.Ssl1, A.Ssl2, A.Sshab, A.Ssrc1, A.Ssrc2, A.Ssrc3, A.Sstrc,
      A.Ssrh1, A.Ssrh2, A.Ssrh3, A.Sstrh, A.Sscsc, A.Sscsh,

      T.Trc1, T.Trc2, T.Trc3, T.Ttrc, T.Trh1, T.Trh2, T.Trh3, T.Ttrh, T.Tcsc, T.Tcsh, 
      T.Tl1, T.Tl2, T.Tl3,

      P.Rha, P.Rga, P.Rgt, P.Rdf,

      P.dsbld, P.intr,
      P.cmnt.replace(/,/g, ';'),
      ((data.super)?data.super:'').replace(/,/g, ';'),

      matchInfo, scoutID ];
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

$('#table').hide();
$('#progress').show();
$('#scoutDownloadLink').hide();

loadFirebaseData();