var database = firebase.database();
var ref = database.ref();
var matchUrl = 'http://mvrtScouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/match/team/';

var startLocations, initLineCrosses, preloads, autonUpper, autonInner, autonUpperMissed, autonBottom, 
autonBottomMissed, teleopUpper, teleopInner, teleopUpperMissed, teleopBottom, teleopBottomMissed, 
trench, defense, rotation, position, stuck, disabled, cycles, climbTime, hangFail, levelFail, 
attemptHang, attemptLevel, buddy, comments;

function searchTeams(){
  var number = document.getElementById('searchTeam').value;
  var match = document.getElementById('searchMatch').value;

  if(!number){
    alert('Please enter a team number!');
    return;
  }

  fetch(matchUrl + match + '/' + number)
    .then(response => {
      return response.json()
    })
    .then(data => {
      // Work with JSON data here
      //console.log(json);
      //var data = JSON.parse(json);

      console.log(data);

      startLocations.innerHTML = data.startLocations;
      initLineCrosses.innerHTML = data.initLineCrosses;
      preloads.innerHTML = data.preloads;
      autonUpper.innerHTML = data.autonUpper;
      autonInner.innerHTML = data.autonInner;
      autonUpperMissed.innerHTML = data.autonUpperMissed;
      autonBottom.innerHTML = data.autonBottom;
      teleopUpper.innerHTML = data.teleopUpper;
      teleopInner.innerHTML = data.teleopInner;
      teleopUpperMissed.innerHTML = data.teleopUpperMissed;
      teleopBottom.innerHTML = data.teleopBottom;
      defense.innerHTML = data.defense;
      disabled.innerHTML = data.disabled;
      cycles.innerHTML = data.cycles;
      hangFail.innerHTML = !data.hangFail;

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

  startLocations = document.getElementById('startLocations');
  initLineCrosses = document.getElementById('initLineCrosses');
  preloads = document.getElementById('preloads');
  autonUpper = document.getElementById('autonUpper');
  autonInner = document.getElementById('autonInner');
  autonUpperMissed = document.getElementById('autonUpperMissed');
  autonBottom = document.getElementById('autonBottom');
  teleopUpper = document.getElementById('teleopUpper');
  teleopInner = document.getElementById('teleopInner');
  teleopUpperMissed = document.getElementById('teleopUpperMissed');
  teleopBottom = document.getElementById('teleopBottom');
  defense = document.getElementById('defense');
  cycles = document.getElementById('startLocations');
  hangFail = document.getElementById('hangFail');
  disabled = document.getElementById('disabled');


  document.getElementById('searchBtn').addEventListener('click', searchTeams);

});
