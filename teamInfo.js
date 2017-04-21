var database = firebase.database();
var ref = database.ref();

var imageTable, gearsPlacedList, climbList, commentsList;

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

      var avg = data[0].reduce((x,y) => x+y)/data[0].length;
      gearsPlacedList.innerHTML = (data[0] + ' (avg: ' + avg.toFixed(2) + ')');

      var climbtxt = '';
      for(d in data[1]){
          if(data[1][d] == 'y')climbtxt = climbtxt.concat('<span class="label label-success">Yes</span> ');
          else if(data[1][d] == 'n')climbtxt = climbtxt.concat('<span class="label label-default">No</span> ');
          else if(data[1][d] == 'f')climbtxt = climbtxt.concat('<span class="label label-danger">Failed</span> ');
          else if(data[1][d] == 'c')climbtxt = climbtxt.concat('<span class="label label-warning">Cancelled (?)</span> ');
          else climbtxt = climbtxt.concat(data[1][d] + ' ');
          console.log(climbtxt);
      }
      climbList.innerHTML = climbtxt;

      for(c in data[2]){
        commentsList.append(newCommentElement(data[2][c]));
      }
  });

}

function getData(team, callback){
  var gearsPlaced = [];
  var climbResults = [];
  var superComments = [];

  ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
      var data = snapshot.val();
      for(key in data) {
        var entry = data[key];
        if(entry['T']) {
            gearsPlaced.push(entry['T']['Tgp']);
            climbResults.push(entry['T']['Tcr']);
        }
        if(entry['super']) superComments.push(entry['super']);
        if(entry['P'] && entry['P']['cmnt']) superComments.push(entry['P']['cmnt']);

      }

      callback([gearsPlaced, climbResults, superComments]);

  });
}

function clearUI(){
  imageTable.innerHTML = null;
  gearsPlacedList.innerHTML = null;
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
  gearsPlacedList = document.getElementById('gearsPlacedList');
  climbList = document.getElementById('climbList');
  commentsList = document.getElementById('commentsList');
  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
