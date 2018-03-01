var database = firebase.database();
var ref = database.ref();

var imageTable, oppSwitcPlacedList, scalePlacedList, switchPlacedList, vaultPlacedList, climbList, parkedList, commentsList;
var cubeGraph, cubeLine;

function searchTeams(){
  var number = document.getElementById('searchTeam').value;
  console.log('search team ' + number);

  clearUI();

  if(!number){
    alert('Please enter a team number!');
    return;
  }

  ref.child('teams/' + number).on('child_added', function(snapshot){
    console.log(snapshot.val());
    imageTable.append(newImageElement(snapshot.val()));
  });

  getData(parseInt(number), showTeamData);

}

function showTeamData(data) {
    console.log(data);

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
        yaxis: {
            title: 'Cube Scoring: Team #' + data[9],
            zeroline: true,
            showgrid: true,
        },
        boxmode: 'group'
    };

    Plotly.newPlot(cubeGraph, [oppTrace, scaleTrace, switchTrace, vaultTrace], layout);
    cubeGraph.hidden = false;

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
        yaxis: {
            title: 'Cube Scoring: Team #' + data[9],
            width: 500,
            zeroline: true,
            showgrid: true,
        },
        boxmode: 'group'
    };

    Plotly.newPlot(cubeLine, [oppTrace, scaleTrace, switchTrace, vaultTrace], layout);
    cubeLine.hidden = false;

    var avgOpp = data[0].reduce((x,y) => x+y)/data[0].length;
    var avgScale = data[1].reduce((x,y) => x+y)/data[1].length;
    var avgSwitch = data[2].reduce((x,y) => x+y)/data[2].length;
    var avgVault = data[3].reduce((x,y) => x+y)/data[3].length;

    var climbtxt = '';
    for(d in data[4]){
        if(data[4][d] == 'y')climbtxt = climbtxt.concat('<span class="label label-success">Yes</span> ');
        else if(data[4][d] == 'n')climbtxt = climbtxt.concat('<span class="label label-default">No</span> ');
        else if(data[4][d] == 'f')climbtxt = climbtxt.concat('<span class="label label-danger">Failed</span> ');
        else if(data[4][d] == 'c')climbtxt = climbtxt.concat('<span class="label label-warning">Cancelled (?)</span> ');
        else climbtxt = climbtxt.concat(data[4][d] + ' ');
        console.log(climbtxt);
    }
    climbList.innerHTML = climbtxt;

    var parktxt = '';
    for(d in data[5]){
        if(data[5][d] == 'y')parktxt = parktxt.concat('<span class="label label-success">Yes</span> ');
        else if(data[5][d] == 'n')parktxt = parktxt.concat('<span class="label label-default">No</span> ');
        else if(data[5][d] == 'f')parktxt = parktxt.concat('<span class="label label-danger">Failed</span> ');
        else if(data[5][d] == 'c')parktxt = parktxt.concat('<span class="label label-warning">Cancelled (?)</span> ');
        else parktxt = parktxt.concat(data[5][d] + ' ');
        console.log(parktxt);
    }
    parkedList.innerHTML = parktxt;

    for(c in data[6]){
      commentsList.append(newCommentElement(data[6][c]));
    }
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

  ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
      var data = snapshot.val();
      console.log(data);
      for(key in data) {
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
        alliances.push(entry['alliance'])
;      }
      callback([oppCubesPlaced, scaleCubesPlaced, switchCubesPlaced, vaultCubesPlaced, climbResults, parkResults, superComments, matches, alliances, team]);
  });
}

function clearUI(){
  imageTable.innerHTML = null;
  climbList.innerHTML = null;
  commentsList.innerHTML = null;
  cubeGraph.hidden = false;
  cubeLine.hidden = false;
}

function newImageElement(imageURL){
  var element = document.createElement('div');
  element.classList = 'col-xs-6 col-md-3';
  var a = document.createElement('a');
  a.href = imageURL;
  a.classList = 'thumbnail';
  var img = document.createElement('img');
  img.width = 400;
  img.height = 400;
  img.src = imageURL;
  a.append(img);
  element.append(a);
  return element;
}

function newCommentElement(comment){
  var element = document.createElement('li');
  element.classList = 'list-group-item';
  element.innerText = comment;
  return element;
}

document.addEventListener('DOMContentLoaded', function(event) {
  firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
      console.log('logged in');
    } else {
      console.log('logged out');
      window.location.replace('login.html');
    }
  });

  imageTable = document.getElementById('images');
  oppSwitchPlacedList = document.getElementById('oppSwitchPlacedList');
  scalePlacedList = document.getElementById('scalePlacedList');
  switchPlacedList = document.getElementById('switchPlacedList');
  vaultPlacedList = document.getElementById('vaultPlacedList');
  climbList = document.getElementById('climbList');
  parkedList = document.getElementById('parkingList');
  commentsList = document.getElementById('commentsList');
  cubeGraph = document.getElementById('cubegraph');
  cubeLine = document.getElementById('cubeline');
  document.getElementById('searchBtn').addEventListener('click', searchTeams);

});
