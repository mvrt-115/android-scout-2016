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

function sign_out() {
    firebase.auth().signOut();
}

document.getElementById('signoutlink').addEventListener('click', sign_out);

function b64DecodeUnicode(str) {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(atob(str).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}

document.addEventListener('DOMContentLoaded', function(event) {

  firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
        user.getIdToken().then((idToken) => {
            const payload = JSON.parse(b64DecodeUnicode(idToken.split('.')[1]));
            // Confirm the user is an Admin.
            if (!!payload['mvrt']) {
                console.log('logged in');
                window.location.replace('index.html');
            } else {
                $('#sign_out').show();
                $('#firebaseui-auth-container').hide();
            }
        })
        .catch((error) => {
            console.log(error);
            $('#sign_out').show();
            $('#firebaseui-auth-container').hide();
        });

    } else {
      console.log('logged out');
      $('#sign_out').hide();
      $('#firebaseui-auth-container').show();
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
