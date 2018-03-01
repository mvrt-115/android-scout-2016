var database = firebase.database();
var ref = database.ref();

var imageTable, oppSwitcPlacedList, scalePlacedList, switchPlacedList, vaultPlacedList, climbList, parkedList, commentsList;

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

  getData(parseInt(number), function(data){

      var avgOpp = data[0].reduce((x,y) => x+y)/data[0].length;
      var avgScale = data[1].reduce((x,y) => x+y)/data[1].length;
      var avgSwitch = data[2].reduce((x,y) => x+y)/data[2].length;
      var avgVault = data[3].reduce((x,y) => x+y)/data[3].length;      

      oppSwitchPlacedList.innerHTML = (data[0] + ' (avg: ' + avgOpp.toFixed(2) + ')');
      scalePlacedList.innerHTML = (data[1] + ' (avg: ' + avgScale.toFixed(2) + ')');
      switchPlacedList.innerHTML = (data[2] + ' (avg: ' + avgSwitch.toFixed(2) + ')');
      vaultPlacedList.innerHTML = (data[3] + ' (avg: ' + avgVault.toFixed(2) + ')');



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
  });

}

function getData(team, callback){
  var oppCubesPlaced = [];
  var scaleCubesPlaced = [];
  var switchCubesPlaced = [];
  var vaultCubesPlaced = [];
  var climbResults = [];
  var parkResults = [];
  var superComments = [];


  ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
      var data = snapshot.val();
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
      }
      callback([oppCubesPlaced, scaleCubesPlaced, switchCubesPlaced, vaultCubesPlaced, climbResults, parkResults, superComments]);
  });
}

function clearUI(){
  imageTable.innerHTML = null;
  oppSwitchPlacedList.innerHTML = null;
  climbList.innerHTML = null;
  commentsList.innerHTML = null;
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
  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
