var database = firebase.database();
var ref = database.ref();
var matchUrl = 'http://mvrtScouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/analytics/team/';

var = avgAutonCells, avgAutonCellsMissed, avgTeleopCells, avgTeleopCellsMissed, avgAccuracy, stdDevCells, avgCycles, 
stdDevCycles, avgClimbs, totalDefenseMatches, totalInner, totalOuter, totalBottom, totalMissed, comments;

function searchTeams(){
  var number = document.getElementById('searchTeam').value;
  console.log('search team ' + number);

  if(!number){
    alert('Please enter a team number!');
    return;
  }

  fetch(matchUrl + number)
    .then(response => {
      return response.json()
    })
    .then(data => {
      // Work with JSON data here
      //console.log(json);
      //var data = JSON.parse(json);

      console.log(data);

      avgAutonCells.innerHTML = data.avgAutonCells;
      avgAutonCellsMissed.innerHTML = data.avgAutonCellsMissed;
      avgTeleopCells.innerHTML = data.avgTeleopCells;
      avgTeleopCellsMissed.innerHTML = data.avgTeleopCellsMissed;
      totalInner.innerHTML = data.totalInnerAutonCells + data.totalInnerTeleopCells;
      totalOuter.innerHTML = data.totalUpperAutonCells + data.totalUpperTeleopCells;
      totalBottom.innerHTML = data.totalBottomTeleopCells + data.totalBottomAutonCells;
      totalMissed.innerHTML = data.totalBottomTeleopCellsMissed + data.totalUpperAutonCellsMissed + data.totalBottomAutonCellsMissed + data.totalUpperTeleopCellsMissed;
      avgAccuracy.innerHTML = (totalInner + totalOuter + totalBottom) / (totalInner + totalOuter + totalBottom + totalMissed;
      stdDevCells.innerHTML = data.stdDevCells;
      avgCycles.innerHTML = data.avgCycles;
      stdDevCycles.innerHTML = data.stdDevCycles;
      avgClimbs.innerHTML = data.avgClimbs;
      totalLevels.innerHTML = data.totalLevels;
      totalCycles.innerHTML = data.totalCycles;
      totalDefenseMatches.innerHTML = data.totalDefenseMatches;
      comments.innerHTML = data.comments;
    })
    .catch(err => {
      // Do something for an error here
    })

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

  avgAutonCells = document.getElementById('avgAutonCells');
  avgAutonCellsMissed = document.getElementById('avgAutonCellsMissed');
  avgTeleopCells = document.getElementById('avgTeleopCells');
  avgTeleopCellsMissed = document.getElementById('avgTeleopCellsMissed');
  avgAccuracy = document.getElementById('avgAccuracy');
  totalBottomTeleopCells = document.getElementById('totalBottomTeleopCells');
  totalPosition = document.getElementById('totalPosition');
  totalRotation = document.getElementById('totalRotation');
  totalClimbs = document.getElementById('totalClimbs');
  totalLevels = document.getElementById('totalLevels');
  totalCycles = document.getElementById('totalCycles');
  totalDefenseMatches = document.getElementById('totalDefenseMatches');
  totalDisabledMatches = document.getElementById('totalDisabledMatches');
  totalBuddyClimbs = document.getElementById('totalBuddyClimbs');
  avgInitCrosses = document.getElementById('avgInitCrosses');
  avgClimbs = document.getElementById('avgClimbs');
  avgLevels = document.getElementById('avgLevels');
  avgAutonCells = document.getElementById('avgAutonCells');
  avgAutonCellsMissed = document.getElementById('avgAutonCellsMissed');
  avgTeleopCells = document.getElementById('avgTeleopCells');
  avgTeleopCellsMissed = document.getElementById('avgTeleopCellsMissed');
  avgCycles = document.getElementById('avgCycles');
  cycles = document.getElementById('cycles');
  cells = document.getElementById('cells');
  comments = document.getElementById('comments');
  stdDevCycles = document.getElementById('stdDevCycles');
  stdDevCells = document.getElementById('stdDevCells');

  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
