var username, password, signin;

// function login(){
//   var email = username.value;
//   var pass = password.value;
//
//   firebase.auth().signInWithEmailAndPassword(email, pass).then(function(){
//     console.log('success');
//   }).catch(function(error) {
//   var errorCode = error.code;
//   var errorMessage = error.message;
//   console.log(errorMessage);
//   alert(errorMessage);
// });
// }

document.addEventListener('DOMContentLoaded', function(event) {

  firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
      console.log('logged in');
      window.location.replace('index.html');
    } else {
      console.log('logged out');
    }
  });

  // username = document.getElementById('email');
  // password = document.getElementById('pass');
  // signin = document.getElementById('signIn');
  //
  // signin.addEventListener('click', login);

  load();

});

function load() {
    var ui = new firebaseui.auth.AuthUI(firebase.auth());
    ui.start('#firebaseui-auth-container', {
        signInSuccessUrl: 'login.html',
        callbacks: {
            uiShown: function() {
                // The widget is rendered.
                // Hide the loader.
                document.getElementById('loader').style.display = 'none';
            }
        },
        signInFlow: 'redirect',
        signInOptions: [
          // Leave the lines as is for the providers you want to offer your users.
          {
              provider: firebase.auth.GoogleAuthProvider.PROVIDER_ID,
              customParameters: { prompt: 'select_account' }
          },
          {
              provider: firebase.auth.EmailAuthProvider.PROVIDER_ID,
              requireDisplayName: false
          }
        ]
        // Terms of service url.
        // tosUrl: 'mvrt.com'
    });
}
