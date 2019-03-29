// var database = firebase.database();
// var ref = database.ref();

// var tournId, matchId, blueCubes, redCubes, topview;

// var progress = 0;

// function searchMatch(){
//     clearUI();

//     $('#progress').show();
//     progress = 50;
//     $('#progressbar').attr('aria-valuenow', progress).css('width', progress + '%');

//     var tournKey = tournId.value + "_" + matchId.value;

//     if(/^(\d{1,4}(.?)+){6}/.test(matchId.value)) {
//         teams = matchId.value.match(/\d+/g);
//         var data = { alliances: {
//             red: { team_keys: teams.slice(0,3) },
//             blue: { team_keys: teams.slice(3,6) }
//         }};
//         console.log('Matchup for teams: ' + teams);
//         showMatchData(data);

//     } else {
//         console.log("Searching for match: " + tournKey);
//         $.getJSON("https://www.thebluealliance.com/api/v3/match/" + tournKey + "/simple",
//             {
//                 'X-TBA-Auth-Key': "yfXvcTqeqUJUpzvvMxRaAZPAM3uSfg8oc4dICKAObiMUMUFwvGDLn8WPD7nWQjIk"
//             }, function(data) {
//                 showMatchData(data);
//         });
//     }

//     setTimeout(function() {
//         if(progress == 50) {
//             $('#progressbar').addClass('bg-danger');
//             progress = 100;
//             $('#progressbar').attr('aria-valuenow', progress).css('width', progress + '%');
//         }
//     }, 3000);

//     setTimeout(function() {
//         if(progress == 100) {
//             $('#progress').hide();
//             clearUI();
//         }
//     }, 6000);
// }

// Object.defineProperties(Array.prototype, {
//     count: {
//         value: function(query) {
//             /*
//                Counts number of occurrences of query in array, an integer >= 0
//                Uses the javascript == notion of equality.
//             */
//             var count = 0;
//             for(let i=0; i<this.length; i++)
//                 if (this[i]==query)
//                     count++;
//             return count;
//         }
//     }
// });

// function displayTopData(data, parent, colors) {

//     var allLabels = ['Yes', 'No', 'Failed'];
//     var allData = [];
//     var domains = [
//         { x: [0, 1], y: [0, 0.33] },
//         { x: [0, 1], y: [.34, .66] },
//         { x: [0, 1], y: [.67, 1] }
//     ];

//     var mobilityLabels = ['Yes', 'No'];
//     var mobilityData = [];

//     for(let key in data) {
//         var teamData = data[key];

//         allData.push({
//           values: [teamData[4].count('y'), teamData[4].count('n'), teamData[4].count('f')],
//           labels: allLabels,
//           type: 'pie',
//           name: 'Team ' + teamData[9],
//           hoverinfo: 'none',
//           marker: {
//             colors: [colors[0], '#F5F5F5', '#9E9E9E']
//           },
//           domain: domains[key],
//         });

//         mobilityData.push({
//           values: [teamData[13].count(true), teamData[13].count(false)],
//           labels: mobilityLabels,
//           type: 'pie',
//           name: 'Team ' + teamData[9],
//           hoverinfo: 'none',
//           marker: {
//             colors: [colors[0], '#F5F5F5']
//           },
//           domain: domains[key],
//         });
//     }

//     var layout = { title: 'Climbing' };
//     var mlayout = { title: 'Mobility' };

//     var element = newElement(parent);
//     Plotly.newPlot(element, allData, layout, {displayModeBar: false, staticPlot: true});

//     var element = newElement(parent);
//     Plotly.newPlot(element, mobilityData, mlayout, {displayModeBar: false, staticPlot: true});

// }

// function graphAllianceLine(data, parent, colors, title, index) {

//     traces = [];

//     for (let key in data) {
//         var teamData = data[key];

//         defaultScatter = {
//             y: teamData[index],
//             x: teamData[7],
//             // x: Array.apply(null, Array(teamData[0].length)).map(function (_, i) {return i;}),
//             line: {shape: 'spline'},
//             name: '#' + teamData[9],
//             type: 'scatter',
//             marker: {color: colors[key]}
//         };

//         traces.push(defaultScatter);
//     }

//     var layout = {
//         autosize: true,
//         yaxis: { zeroline: true, showgrid: true, autorange: true },
//         xaxis: { title: 'Match', zeroline: true },
//         margin: { l: 40, r: 20, b: 100, t: 150, pad: 20 },
//         legend: { orientation: "h", x: 0, y: 1.2 },
//         title: title,
//         boxmode: 'group'
//     };

//     element = newElement(parent);
//     Plotly.newPlot(element, traces, layout, {displayModeBar: false, staticPlot: true});
// }

// function graphAllianceBox(data, parent, colors, title, index) {

//     traces = [];

//     for (let key in data) {
//         var teamData = data[key];

//         defaultBox = {
//             y: teamData[index],
//             text: teamData[7],
//             boxpoints: 'all',
//             name: '#' + teamData[9],
//             type: 'box',
//             marker: {color: colors[key]}
//         };

//         traces.push(defaultBox);
//     }

//     var layout = {
//         autosize: true,
//         title: title,
//         yaxis: { zeroline: true, showgrid: true, autorange: true },
//         margin: { l: 40, r: 20, b: 100, t: 150, pad: 20 },
//         legend: { orientation: "h", x: 0, y: 1.2 },
//         boxmode: 'group'
//     };

//     element = newElement(parent);
//     Plotly.newPlot(element, traces, layout, {displayModeBar: false, staticPlot: true});
// }

// function showMatchData(data) {
//     progress = 80;
//     $('#progressbar').attr('aria-valuenow', progress).css('width', progress + '%');
//     console.log('show data');

//     redTeams = data.alliances.red.team_keys.map(function(key) { return parseInt(key.replace( /^\D+/g, '')) });
//     blueTeams = data.alliances.blue.team_keys.map(function(key) { return parseInt(key.replace( /^\D+/g, '')) });
//     console.log("Red teams: " + redTeams);
//     console.log("Blue teams: " + blueTeams);
//     $('#redteams').text(redTeams.toString());
//     $('#blueteams').text(blueTeams.toString());

//     // red alliance:
//     var redData = [];
//     var blueData = [];

//     for(let team of redTeams) {
//         getData(team, function(data){
//             redData.push(data);
//             if(redData.length != 3) return;
//             if(blueData.length != 3) return;

//             $('#matchview').show();
//             graphData(redData, blueData);
//             $('#progress').hide();
//         });
//     }

//     for(let team of blueTeams) {
//         getData(team, function(data){
//             blueData.push(data);
//             if(redData.length != 3) return;
//             if(blueData.length != 3) return;

//             $('#matchview').show();
//             graphData(redData, blueData);
//             $('#progress').hide();
//         });
//     }
// }

// function graphData(redData, blueData) {
//     var redColors = ['#f44336', '#9C27B0', '#ef9a9a'];
//     var blueColors = ['#3F51B5', '#00BCD4', '#2196F3'];

//     graphAllianceBox(redData, blueCubes, redColors, 'Red Scale Scoring', 1);
//     graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Scale Scoring', 1);
//     graphAllianceBox(redData, blueCubes, redColors, 'Red Switch Scoring', 2);
//     graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Switch Scoring', 2);
//     graphAllianceBox(redData, blueCubes, redColors, 'Red Vault Scoring', 3);
//     graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Vault Scoring', 3);
//     graphAllianceBox(redData, blueCubes, redColors, 'Red Opp Scoring', 0);
//     graphAllianceBox(blueData, blueCubes, blueColors, 'Blue Opp Scoring', 0);

//     graphAllianceLine(redData, redCubes, redColors, 'Red Scale Scoring', 1);
//     graphAllianceLine(blueData, redCubes, blueColors, 'Blue Scale Scoring', 1);
//     graphAllianceLine(redData, redCubes, redColors, 'Red Switch Scoring', 2);
//     graphAllianceLine(blueData, redCubes, blueColors, 'Blue Switch Scoring', 2);
//     graphAllianceLine(redData, redCubes, redColors, 'Red Vault Scoring', 3);
//     graphAllianceLine(blueData, redCubes, blueColors, 'Blue Vault Scoring', 3);
//     graphAllianceLine(redData, redCubes, redColors, 'Red Opp Scoring', 0);
//     graphAllianceLine(blueData, redCubes, blueColors, 'Blue Opp Scoring', 0);

//     displayTopData(redData, document.getElementById('topview'), redColors);
//     displayTopData(blueData, document.getElementById('topview'), blueColors);

//     progress = 0;
//     $('#progressbar').attr('aria-valuenow', progress).css('width', progress + '%');
// }

// function newElement(parent) {
//     var element = document.createElement('div');
//     element.classList = 'col-xs-3 col-md-6 teamElement';
//     parent.appendChild(element);
//     return element;
// }

// function getData(team, callback){
//   var hatchPreload = [];
//   var cargoPreload = [];
//   var l1Start = [];
//   var l2Start = [];
//   var cargoHatch = [];
//   var cargoCargo = [];
//   var r1Hatch = [];
//   var r1Cargo = [];
//   var r2Hatch = [];
//   var r2Cargo = [];
//   var r3Hatch = [];
//   var r3Cargo = [];
//   var cargoHatchS = [];
//   var cargoCargoS = [];
//   var r1HatchS = [];
//   var r1CargoS = [];
//   var r2HatchS = [];
//   var r2CargoS = [];
//   var r3HatchS = [];
//   var r3CargoS = [];
//   var climb1 = [];
//   var climb2 = [];
//   var climb3 = [];
//   var superComments = [];

//   var totalRocketHatchS = [];
//   var totalRocketCargoS = [];
//   var totalRocketHatch = [];
//   var totalRocketCargo = [];

//   ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
//       var data = snapshot.val();

//       for(key in data) {

//         var entry = data[key];

//         if(entry['T']) {
//             cargoHatch.push(entry['T']['Tcsh'])
//             cargoCargo.push(entry['T']['Tcsc']);
//             r1Hatch.push(entry['T']['Trh1']);
//             r1Cargo.push(entry['T']['Trc1']);
//             r2Hatch.push(entry['T']['Trh2']);
//             r2Cargo.push(entry['T']['Trc2']);
//             r3Hatch.push(entry['T']['Trh3']);
//             r3Cargo.push(entry['T']['Trc3']);
//             climb1.push(entry['T']['Tl1']);
//             climb2.push(entry['T']['Tl2']);
//             climb3.push(entry['T']['Tl3']);

//             totalRocketHatch.push(entry['T']['Ttrh']);
//             totalRocketCargo.push(entry['T']['Ttrc']);
//         }
        
//         if(entry['A']) {
//             hatchPreload.push(entry['A']['Ssh']);
//             cargoPreload.push(entry['A']['Ssc']);
//             l1Start.push(entry['A']['Ssl1']);
//             l2Start.push(entry['A']['Ssl2']);
//             cargoHatchS.push(entry['A']['Sscsh']);
//             cargoCargoS.push(entry['A']['Sscsc']);
//             r1HatchS.push(entry['A']['Ssrh1']);
//             r1CargoS.push(entry['A']['Ssrc1']);
//             r2HatchS.push(entry['A']['Ssrh2']);
//             r2CargoS.push(entry['A']['Ssrc2']);
//             r3HatchS.push(entry['A']['Ssrh3']);
//             r3CargoS.push(entry['A']['Ssrc3']);

//             totalRocketHatchS.push(entry['A']['Sstrh']);
//             totalRocketCargoS.push(entry['A']['Sstrc']);
//         }

//         if(entry['super']) superComments.push(entry['super'] + " (SUPERSCOUT)");
//         if(entry['P'] && entry['P']['cmnt']) superComments.push(entry['P']['cmnt'] + " (SCOUT)");
//       }

//       callback([cargoHatch, cargoCargo, r1Hatch, r1Cargo, r2Hatch, r2Cargo, r3Hatch, r3Cargo, climb2, climb3, 
//                 cargoHatchS, cargoCargoS, r1HatchS, r1CargoS, r2HatchS, r2CargoS, r3HatchS, r3CargoS, superComments, 
//                 hatchPreload, cargoPreload, l1Start, l2Start, climb1, totalRocketCargoS, totalRocketHatchS, 
//                 totalRocketCargo, totalRocketHatch]);
//   });
// }

// function clearUI(){
//     $(blueCubes).empty();
//     $(redCubes).empty();
//     $(topview).empty();

//     $('#progress').hide();
//     $('#matchview').hide();
//     $('#progressbar').attr('class', 'progress-bar progress-bar-striped progress-bar-animated');
//     progress = 0;
//     $('#progressbar').attr('aria-valuenow', progress).css('width', progress + '%');
// }

// document.addEventListener('DOMContentLoaded', function(event) {
//   clearUI();

//   tournId = document.getElementById("searchTourn");
//   matchId = document.getElementById("searchMatch");
//   blueCubes = document.getElementById("bluecubegraphs");
//   redCubes = document.getElementById("redcubegraphs");
//   topview = document.getElementById('topview');

//   document.getElementById('searchBtn').addEventListener('click', searchMatch);

// });

// $('#progress').hide();
// $('#matchview').hide();