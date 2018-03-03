var database = firebase.database();
var ref = database.ref();

var tournId, matchId, blueCubes, redCubes;

function searchMatch(){
    var tournKey = tournId.value + "_" + matchId.value;
    console.log("Searching for match: " + tournKey);

    $.getJSON("https://www.thebluealliance.com/api/v3/match/" + tournKey + "/simple",
        {
            'X-TBA-Auth-Key': "yfXvcTqeqUJUpzvvMxRaAZPAM3uSfg8oc4dICKAObiMUMUFwvGDLn8WPD7nWQjIk"
        }, function(data) {
            showMatchData(data);
    });
}

function showMatchData(data) {
    clearUI();

    redTeams = data.alliances.red.team_keys.map(function(key) { return parseInt(key.replace( /^\D+/g, '')) });
    blueTeams = data.alliances.blue.team_keys.map(function(key) { return parseInt(key.replace( /^\D+/g, '')) });
    console.log("Red teams: " + redTeams);
    console.log("Blue teams: " + blueTeams);

    for(var i=0;i<3;i++) {
        var e1 = newElement(redTeams[i])
        var e2 = newElement(redTeams[i])
        redCubes.appendChild(e1);
        redCubes.appendChild(e2);
        getData(redTeams[i], e1, e2, showTeamData);
        e1 = newElement(blueTeams[i])
        e2 = newElement(blueTeams[i])
        blueCubes.appendChild(e1);
        blueCubes.appendChild(e2);
        getData(blueTeams[i], e1, e2, showTeamData);
    }

}

function newElement(data) {
    var element = document.createElement('div');
    element.classList = 'col-xs-3 col-md-6 teamElement';
    element.height = "300px";
    return element;
}

function showTeamData(element, element2, data) {
    var oppTrace = {
        y: data[0],
        text: data[7].map(function(match) {return 'Match #' + match}),
        boxpoints: 'all',
        name: 'Opponent Switch',
        marker: {color: '#3F51B5'},
        type: 'box'
    };

    var scaleTrace = {
        y: data[1],
        text: data[7].map(function(match) {return 'Match #' + match}),
        boxpoints: 'all',
        name: 'Scale',
        marker: {color: '#f44336'},
        type: 'box'
    };

    var switchTrace = {
        y: data[2],
        text: data[7].map(function(match) {return 'Match #' + match}),
        boxpoints: 'all',
        name: 'Switch',
        marker: {color: '#673AB7'},
        type: 'box'
    };

    var vaultTrace = {
        y: data[3],
        text: data[7].map(function(match) {return 'Match #' + match}),
        boxpoints: 'all',
        name: 'Vault',
        marker: {color: '#FFC107'},
        type: 'box'
    };

    var layout = {
        autosize: true,
        title: 'Cube Scoring: Team #' + data[9],
        yaxis: {
            zeroline: true,
            showgrid: true,
        },
        xaxis: {
            title: 'Match'
        },
        margin: {
          l: 0,
          r: 0,
          b: 100,
          t: 100,
          pad: 20
        },
        boxmode: 'group'
    };

    Plotly.newPlot(element, [oppTrace, scaleTrace, switchTrace, vaultTrace], layout);

    var oppTrace = {
        y: data[0],
        x: data[7].map(function(match) {return 'Match #' + match}),
        line: {shape: 'spline'},
        name: 'Opponent Scale',
        type: 'scatter',
        marker: {color: '#3F51B5'}
    };

    var scaleTrace = {
        y: data[1],
        x: data[7].map(function(match) {return 'Match #' + match}),
        line: {shape: 'spline'},
        name: 'Scale',
        type: 'scatter',
        marker: {color: '#f44336'}
    };

    var switchTrace = {
        y: data[2],
        x: data[7].map(function(match) {return 'Match #' + match}),
        line: {shape: 'spline'},
        name: 'Switch',
        type: 'scatter',
        marker: {color: '#673AB7'}
    };

    var vaultTrace = {
        y: data[3],
        x: data[7].map(function(match) {return 'Match #' + match}),
        line: {shape: 'spline'},
        name: 'Vault',
        type: 'scatter',
        marker: {color: '#FFC107'}
    };

    var layout = {
        autosize: true,
        title: 'Cube Scoring: Team #' + data[9],
        yaxis: {
            width: 500,
            zeroline: true,
            showgrid: true,
        },
        xaxis: {
            title: 'Match'
        },
        margin: {
          l: 0,
          r: 0,
          b: 100,
          t: 100,
          pad: 20
        },
        boxmode: 'group'
    };

    Plotly.newPlot(element2, [oppTrace, scaleTrace, switchTrace, vaultTrace], layout);
}

function getData(team, boxElement, lineElement, callback){
  var oppCubesPlaced = [];
  var scaleCubesPlaced = [];
  var switchCubesPlaced = [];
  var vaultCubesPlaced = [];
  var climbResults = [];
  var parkResults = [];
  var superComments = [];
  var matches = [];
  var alliances = [];

  ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
      var data = snapshot.val();

      arrayOfSortedObjects = Object.keys(data).sort(function(a,b) {
          return data[a].match - (data[b].match);
      });

      for(index in arrayOfSortedObjects) {
        key = arrayOfSortedObjects[index];
        var entry = data[key];
        if(entry['T']) {

            oppCubesPlaced.push(entry['T']['Tos']);
            scaleCubesPlaced.push(entry['T']['Tsc']);
            switchCubesPlaced.push(entry['T']['Tsw']);
            vaultCubesPlaced.push(entry['T']['Tsv']);
            climbResults.push(entry['T']['Tcr']);
            parkResults.push(entry['T']['Tpk']);
        }
        if(entry['super']) superComments.push(entry['super']);
        if(entry['P'] && entry['P']['cmnt']) superComments.push(entry['P']['cmnt']);
        matches.push(entry['match']);
        alliances.push(entry['alliance']);
      }
      callback(boxElement, lineElement, [oppCubesPlaced, scaleCubesPlaced, switchCubesPlaced, vaultCubesPlaced, climbResults, parkResults, superComments, matches, alliances, team]);
  });
}

function clearUI(){
    $(blueCubes).empty();
    $(redCubes).empty();
}


document.addEventListener('DOMContentLoaded', function(event) {
    clearUI();

  firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
      console.log('logged in');
    } else {
      console.log('logged out');
      window.location.replace('login.html');
    }
  });

  tournId = document.getElementById("searchTourn");
  matchId = document.getElementById("searchMatch");
  blueCubes = document.getElementById("bluecubegraphs");
  redCubes = document.getElementById("redcubegraphs");

  document.getElementById('searchBtn').addEventListener('click', searchMatch);

  $('#compID div a').on('click', function(){
    //$('#datebox').val($(this).text());
    alert($(this).text());
    $(this).active = true;
})

});
