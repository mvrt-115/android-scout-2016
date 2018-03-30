var database = firebase.database();
var ref = database.ref();

var tournId, matchId, blueCubes, redCubes, topview;

function searchMatch(){
    var tournKey = tournId.value + "_" + matchId.value;
    console.log("Searching for match: " + tournKey);

    if(/^(\d{1,4}(.?)+){6}/.test(matchId.value)) {
        teams = matchId.value.match(/\d+/g);
        var data = { alliances: {
            red: { team_keys: teams.slice(0,3) },
            blue: { team_keys: teams.slice(3,6) }
        }};
        console.log('teams: ' + teams);
        showMatchData(data);
    } else {

        $.getJSON("https://www.thebluealliance.com/api/v3/match/" + tournKey + "/simple",
            {
                'X-TBA-Auth-Key': "yfXvcTqeqUJUpzvvMxRaAZPAM3uSfg8oc4dICKAObiMUMUFwvGDLn8WPD7nWQjIk"
            }, function(data) {
                showMatchData(data);
        });
    }
}

Object.defineProperties(Array.prototype, {
    count: {
        value: function(query) {
            /*
               Counts number of occurrences of query in array, an integer >= 0
               Uses the javascript == notion of equality.
            */
            var count = 0;
            for(let i=0; i<this.length; i++)
                if (this[i]==query)
                    count++;
            return count;
        }
    }
});

function displayTopData(data, parent, colors) {

    var allLabels = ['Yes', 'No', 'Failed'];
    var allData = [];
    var domains = [
        { x: [0, 1], y: [0, 0.33] },
        { x: [0, 1], y: [.34, .66] },
        { x: [0, 1], y: [.67, 1] }
    ]

    for(let key in data) {
        var teamData = data[key];
        console.log(teamData);

        allData.push({
          values: [teamData[4].count('y'), teamData[4].count('n'), teamData[4].count('f')],
          labels: allLabels,
          type: 'pie',
          name: 'Team ' + teamData[9],
          hoverinfo: 'none',
          marker: {
            colors: [colors[0], '#F5F5F5', '#9E9E9E']
          },
          domain: domains[key],
      });
    }

    var layout = {
        title: 'Climbing'
    };

    var element = newElement(parent);
    Plotly.newPlot(element, allData, layout, {displayModeBar: false, staticPlot: true});

}

function graphAllianceLine(data, parent, colors, title, index) {

    traces = [];

    for (let key in data) {
        var teamData = data[key];

        defaultScatter = {
            y: teamData[index],
            x: teamData[7],
            // x: Array.apply(null, Array(teamData[0].length)).map(function (_, i) {return i;}),
            line: {shape: 'spline'},
            name: '#' + teamData[9],
            type: 'scatter',
            marker: {color: colors[key]}
        };

        traces.push(defaultScatter);
    }

    var layout = {
        autosize: true,
        yaxis: { zeroline: true, showgrid: true, autorange: true },
        xaxis: { title: 'Match', zeroline: true },
        margin: { l: 30, r: 20, b: 100, t: 150, pad: 20 },
        legend: { orientation: "h", x: 0, y: 1.2 },
        title: title,
        boxmode: 'group'
    };

    element = newElement(parent);
    Plotly.newPlot(element, traces, layout, {displayModeBar: false, staticPlot: true});
}

function graphAllianceBox(data, parent, colors, title, index) {

    traces = [];

    for (let key in data) {
        var teamData = data[key];

        defaultBox = {
            y: teamData[index],
            text: teamData[7],
            boxpoints: 'all',
            name: '#' + teamData[9],
            type: 'box',
            marker: {color: colors[key]}
        };

        traces.push(defaultBox);
    }

    var layout = {
        autosize: true,
        title: title,
        yaxis: { zeroline: true, showgrid: true, autorange: true },
        margin: { l: 30, r: 0, b: 100, t: 150, pad: 20 },
        legend: { orientation: "h", x: 0, y: 1.2 },
        boxmode: 'group'
    };

    element = newElement(parent);
    Plotly.newPlot(element, traces, layout, {displayModeBar: false, staticPlot: true});
}

function showMatchData(data) {
    clearUI();

    redTeams = data.alliances.red.team_keys.map(function(key) { return parseInt(key.replace( /^\D+/g, '')) });
    blueTeams = data.alliances.blue.team_keys.map(function(key) { return parseInt(key.replace( /^\D+/g, '')) });
    console.log("Red teams: " + redTeams);
    console.log("Blue teams: " + blueTeams);

    // red alliance:
    var redData = [];
    var blueData = [];
    var redColors = ['#f44336', '#9C27B0', '#ef9a9a'];
    var blueColors = ['#3F51B5', '#00BCD4', '#2196F3'];

    for(let team of redTeams) {
        getData(team, function(data){
            redData.push(data);
            if(redData.length != 3) return;
            if(blueData.length != 3) return;

            graphAllianceBox(redData, blueCubes, redColors, 'Red Scale Scoring', 1);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Scale Scoring', 1);
            graphAllianceBox(redData, blueCubes, redColors, 'Red Switch Scoring', 2);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Switch Scoring', 2);
            graphAllianceBox(redData, blueCubes, redColors, 'Red Vault Scoring', 3);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Vault Scoring', 3);
            graphAllianceBox(redData, blueCubes, redColors, 'Red Opp Scoring', 0);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Opp Scoring', 0);

            graphAllianceLine(redData, redCubes, redColors, 'Red Scale Scoring', 1);
            graphAllianceLine(blueData, redCubes, blueColors, 'Red Scale Scoring', 1);
            graphAllianceLine(redData, redCubes, redColors, 'Red Switch Scoring', 2);
            graphAllianceLine(blueData, redCubes, blueColors, 'Blue Switch Scoring', 2);
            graphAllianceLine(redData, redCubes, redColors, 'Red Vault Scoring', 3);
            graphAllianceLine(blueData, redCubes, blueColors, 'Blue Vault Scoring', 3);
            graphAllianceLine(redData, redCubes, redColors, 'Red Opp Scoring', 0);
            graphAllianceLine(blueData, redCubes, blueColors, 'Blue Opp Scoring', 0);

            displayTopData(redData, topview, redColors);
            displayTopData(blueData, topview, blueColors);
        });
    }

    for(let team of blueTeams) {
        getData(team, function(data){
            blueData.push(data);
            if(redData.length != 3) return;
            if(blueData.length != 3) return;

            graphAllianceBox(redData, blueCubes, redColors, 'Red Scale Scoring', 1);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Scale Scoring', 1);
            graphAllianceBox(redData, blueCubes, redColors, 'Red Switch Scoring', 2);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Switch Scoring', 2);
            graphAllianceBox(redData, blueCubes, redColors, 'Red Vault Scoring', 3);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Vault Scoring', 3);
            graphAllianceBox(redData, blueCubes, redColors, 'Red Opp Scoring', 0);
            graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Opp Scoring', 0);

            graphAllianceLine(redData, redCubes, redColors, 'Red Scale Scoring', 1);
            graphAllianceLine(blueData, redCubes, blueColors, 'Red Scale Scoring', 1);
            graphAllianceLine(redData, redCubes, redColors, 'Red Switch Scoring', 2);
            graphAllianceLine(blueData, redCubes, blueColors, 'Blue Switch Scoring', 2);
            graphAllianceLine(redData, redCubes, redColors, 'Red Vault Scoring', 3);
            graphAllianceLine(blueData, redCubes, blueColors, 'Blue Vault Scoring', 3);
            graphAllianceLine(redData, redCubes, redColors, 'Red Opp Scoring', 0);
            graphAllianceLine(blueData, redCubes, blueColors, 'Blue Opp Scoring', 0);

            displayTopData(redData, document.getElementById('redtop'), redColors);
            displayTopData(blueData, document.getElementById('bluetop'), blueColors);
        });
    }
}

function newElement(parent) {
    var element = document.createElement('div');
    element.classList = 'col-xs-3 col-md-6 teamElement';
    parent.appendChild(element);
    return element;
}

function getData(team, callback){
  var oppCubesPlaced = [];
  var scaleCubesPlaced = [];
  var switchCubesPlaced = [];
  var vaultCubesPlaced = [];
  var climbResults = [];
  var parkResults = [];
  var superComments = [];
  var matches = [];
  var alliances = [];
  var start = [];
  var autonSwitch = [];
  var autonScale = [];

  ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
      var data = snapshot.val();

      if(data == undefined) {
          callback([oppCubesPlaced, scaleCubesPlaced, switchCubesPlaced, vaultCubesPlaced, climbResults, parkResults, superComments, matches, alliances, team, start, autonScale, autonSwitch]);
          return;
      }

      arrayOfSortedObjects = Object.keys(data).sort(function(a,b) {
          return data[a].match - (data[b].match);
      });

      for(index in arrayOfSortedObjects) {
        key = arrayOfSortedObjects[index];
        var entry = data[key];
        if(entry['T']) {
            // if(entry['match'] > 90) continue;
            oppCubesPlaced.push(entry['T']['Tos']);
            scaleCubesPlaced.push(entry['T']['Tsc']);
            switchCubesPlaced.push(entry['T']['Tsw']);
            vaultCubesPlaced.push(entry['T']['Tsv']);
            climbResults.push(entry['T']['Tcr']);
            parkResults.push(entry['T']['Tpk']);

            if(entry.A.Asce)start.push('c');
            else if(entry.A.Asl)start.push('l');
            else if(entry.A.Asr)start.push('r');
            autonScale.push(entry.A.Asc);
            autonSwitch.push(entry.A.Asw);
        }
        if(entry['super']) superComments.push(entry['super']);
        if(entry['P'] && entry['P']['cmnt']) superComments.push(entry['P']['cmnt']);
        matches.push(entry['match']);
        alliances.push(entry['alliance']);
      }
      callback([oppCubesPlaced, scaleCubesPlaced, switchCubesPlaced, vaultCubesPlaced, climbResults, parkResults, superComments, matches, alliances, team, start, autonScale, autonSwitch]);
  });
}

function clearUI(){
    $(blueCubes).empty();
    $(redCubes).empty();
    $(topview).empty();
}

document.addEventListener('DOMContentLoaded', function(event) {
  clearUI();

  tournId = document.getElementById("searchTourn");
  matchId = document.getElementById("searchMatch");
  blueCubes = document.getElementById("bluecubegraphs");
  redCubes = document.getElementById("redcubegraphs");
  topview = document.getElementById('topview');

  document.getElementById('searchBtn').addEventListener('click', searchMatch);

  $('#compID div a').on('click', function(){
    //$('#datebox').val($(this).text());
    alert($(this).text());
    $(this).active = true;
});

});
