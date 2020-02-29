var database = firebase.database();
var ref = database.ref();
var matchUrl = 'http://mvrtScouting-env-1.zpsnzbaqbu.us-east-2.elasticbeanstalk.com/inner-rankings';

var rankings;


document.addEventListener('DOMContentLoaded', function(event) {
  firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
      console.log('logged in');
    } else {
      console.log('logged out');
      window.location.replace('login.html');
    }
  });

  rankings = document.getElementById('innerRankings');
  fetch(matchUrl)
    .then(response => {
      return response.json()
    })
    .then(data => {
      // Work with JSON data here
      //console.log(json);
      //var data = JSON.parse(json);

      console.log(data);
      rankings.innerHTML = data;
      
    })
    .catch(err => {
      // Do something for an error here
  })

});
