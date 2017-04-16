var storage = firebase.storage();
var ref = storage.ref();

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

    var uploadTask = ref.child('team' + teamNumber.value).put(f);
    uploadTask.on('state_changed', null, null, function() {
    // When the image has successfully uploaded, we get its download URL
    var downloadUrl = uploadTask.snapshot.downloadURL;
    console.log(downloadUrl);
  });
  }
}

function handleDragOver(evt) {
    evt.stopPropagation();
    evt.preventDefault();
    evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
}

var dropZone = document.getElementById('bullhead');
var teamNumber = document.getElementById('teamNumber');
dropZone.addEventListener('dragover', handleDragOver, false);
dropZone.addEventListener('drop', handleFileSelect, false);
