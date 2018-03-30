document.addEventListener('DOMContentLoaded', function(event) {
    firebase.auth().onAuthStateChanged(function(user) {
      if (user) {
          user.getIdToken().then((idToken) => {
              const payload = JSON.parse(b64DecodeUnicode(idToken.split('.')[1]));
              // Confirm the user is an Admin.
              if (!!payload['mvrt']) {
                  console.log('logged in');
                  var name = document.getElementById('name');
                  if(name)name.innerText = " " + user.email;
              } else {
                  console.log('logged out');
                  window.location.replace('login.html');
              }
          }).catch((error) => {
              console.log('logged out');
              window.location.replace('login.html');
          });

      } else {
        console.log('logged out');
        window.location.replace('login.html');
      }
    });
});
