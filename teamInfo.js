var database = firebase.database();
var ref = database.ref();

function loadFirebaseData(){

    ref.child('matches').once('value', function(snapshot){
        var scoutData = [];
        var tableData = [];
        snapshot.forEach(function(childSnapshot){
            scoutData.push(getScoutData(childSnapshot));
            tableData.push(getTableData(childSnapshot));
        });
        populateTable(tableData);
        saveCSV(getScoutHeaders(), scoutData, 'scoutDownloadLink', 'scout.csv');
    });

}

var imageTable, gearsPlacedList, commentsList;

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

      for(c in data[1]){
        commentsList.append(newCommentElement(data[1][c]));
      }
  });

}

function getData(team, callback){
  var gearsPlaced = [];
  var superComments = [];

  ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
      var data = snapshot.val();
      for(key in data) {
        var entry = data[key];
        if(entry['T']) gearsPlaced.push(entry['T']['Tgp']);
        if(entry['super']) superComments.push(entry['super']);
        if(entry['P']['cmnt']) superComments.push(entry['P']['cmnt']);

      }

      callback([gearsPlaced, superComments]);

  });
}

function clearUI(){
  imageTable.innerHTML = null;
  gearsPlacedList.innerHTML = null;
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
  imageTable = document.getElementById('images');
  gearsPlacedList = document.getElementById('gearsPlacedList');
  commentsList = document.getElementById('commentsList');
  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
