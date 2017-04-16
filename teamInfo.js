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

var imageTable;

function searchTeams(){
  var number = document.getElementById('searchTeam').value;
  console.log('search team ' + number);

  if(!number){
    alert('Please enter a team number!');
    return;
  }

  imageTable.innerHTML = null;

  ref.child('teams/' + number).on('child_added', function(snapshot){
    console.log(snapshot.val());
    imageTable.append(newRow(snapshot.val()));
  });
}

function newRow(imageURL){
  var element = document.createElement('div');
  element.classList = 'col-xs-6 col-md-3';
  var a = document.createElement('a');
  a.href = imageURL;
  a.classList = 'thumbnail';
  var img = document.createElement('img');
  img.src = imageURL;
  a.append(img);
  element.append(a);
  return element;
}

document.addEventListener('DOMContentLoaded', function(event) {
  imageTable = document.getElementById('images');
  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
