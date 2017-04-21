var storage = firebase.storage();
var ref = storage.ref();

var database = firebase.database();
var dataref = database.ref();

function loadFirebaseData(){

    dataref.child('teams').orderByChild('team').once('value', function(snapshot){
        var list = document.getElementById('uploadedList');
        snapshot.forEach(function(childSnapshot){
            list.append(newTeamElement(childSnapshot.key));
        });
    });

}

function handleFileSelect(evt) {
    evt.stopPropagation();
    evt.preventDefault();

    console.log(evt);

    var files = evt.dataTransfer.files; // FileList object.
    handleFiles(files);
}

function handleFiles(files) {
  console.log(files);

  for (var i = 0, f; f = files[i]; i++) {
    // Only process image files.
    if (!f.type.match('image.*')) continue;
    if(!teamNumber.value){
      alert('Please enter a team number!');
      return;
    }

    var fileName = generateUUID();

    var uploadTask = ref.child(fileName).put(f);
    uploadTask.on('state_changed', null, null, function() {
    // When the image has successfully uploaded, we get its download URL
    var downloadUrl = uploadTask.snapshot.downloadURL;
    dataref.child('teams/' + teamNumber.value).push(downloadUrl);
    console.log(downloadUrl);
    alert('uploaded!');
  });
  }
}

function generateUUID () { // Public Domain/MIT
    var d = new Date().getTime();
    if (typeof performance !== 'undefined' && typeof performance.now === 'function'){
        d += performance.now(); //use high-precision timer if available
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

function handleDragOver(evt) {
    evt.stopPropagation();
    evt.preventDefault();
    evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
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

});

function newTeamElement(team){
  var element = document.createElement('li');
  element.classList = 'list-group-item';
  element.innerText = team;
  return element;
}

var dropZone = document.getElementById('dragFile');
var teamNumber = document.getElementById('teamNumber');
dropZone.addEventListener('dragover', handleDragOver, false);
dropZone.addEventListener('drop', handleFileSelect, false);

loadFirebaseData();
