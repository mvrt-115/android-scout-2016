var database = firebase.database();
var ref = database.ref();
var matchUrl = 'http://mvrtScouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/analytics/team/';

var avgAutonCells, avgAutonCellsMissed, avgTeleopCells, avgTeleopCellsMissed, avgAccuracy, stdDevCells, avgCycles, 
stdDevCycles, avgClimbs, totalDefenseMatches, avgInner, avgOuter, avgBottom, comments;

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
      avgInner.innerHTML = data.avgInner;
      avgUpper.innerHTML = data.avgUpper;
      avgCycles.innerHTML = data.avgCycles;
      avgClimbs.innerHTML = data.avgClimbs;
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
  totalDefenseMatches = document.getElementById('totalDefenseMatches');
  avgClimbs = document.getElementById('avgClimbs');
  avgCycles = document.getElementById('avgCycles')
  comments = document.getElementById('comments');
  avgInner = document.getElementById('avgInner');
  avgUpper = document.getElementById('avgUpper');



  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
