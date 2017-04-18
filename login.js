var username, password, signin;

function login(){
  var email = username.value;
  var pass = password.value;

  firebase.auth().signInWithEmailAndPassword(email, pass).then(function(){
    console.log('success');
  }).catch(function(error) {
  var errorCode = error.code;
  var errorMessage = error.message;
  console.log(errorMessage);
});
}

document.addEventListener('DOMContentLoaded', function(event) {

  firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
      console.log('logged in');
      window.location.replace('index.html');
    } else {
      console.log('logged out');
    }
  });

  username = document.getElementById('email');
  password = document.getElementById('pass');
  signin = document.getElementById('signIn');

  signin.addEventListener('click', login);

});
