var database = firebase.database();
var ref = database.ref();
var matchUrl = 'http://mvrtScouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/robot/team/';

var = drivetrain, weight, height, climber,centerOfGravity, canHang, speed, innerShoot, outerShoot, bottomShoot, canRotation, canPosition, hopperCapacity, auton, generalPaths, driveteamExp;

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

      drivetrain.innerHTML = data.drivetrain;
      weight.innerHTML = data.weight;
      height.innerHTML = data.height;
      climber.innerHTML = data.climber;
      centerOfGravity.innerHTML = data.centerOfGravity;
      canHang.innerHTML = data.canHang;
      speed.innerHTML = data.speed;
      innerShoot.innerHTML = data.innerShoot;
      outerShoot.innerHTML = data.outerShoot;
      bottomShoot.innerHTML = data.bottomShoot;
      canRotation.innerHTML = data.canRotation;
      canPosition.innerHTML = data.canPosition;
      hopperCapacity.innerHTML = data.hopperCapacity;
      auton.innerHTML = data.auton;
      generalPaths.innerHTML = data.generalPaths;
      generalPaths.driveteamExp = data.driveteamExp;
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

  drivetrain = document.getElementById('drivetrain');
  weight = document.getElementById('weight');
  height = document.getElementById('height');
  climber = document.getElementById('climber');
  centerOfGravity = document.getElementById('centerOfGravity');
  canHang = document.getElementById('canHang');
  speed = document.getElementById('speed');
  innerShoot = document.getElementById('innerShoot');
  outerShoot = document.getElementById('outerShoot');
  bottomShoot = document.getElementById('bottomShoot');
  canRotation = document.getElementById('canRotation');
  canPosition = document.getElementById('canPosition');
  hopperCapacity = document.getElementById('hopperCapacity');
  auton = document.getElementById('auton');
  generalPaths = document.getElementById('generalPaths');
  driveteamExp = document.getElementById('driveteamExp');

  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
