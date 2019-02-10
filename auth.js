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
                  var name = document.getElementById('name');
                  if(name)name.innerText = " " + user.email;
              } else {
                  console.log('logged out');
                  window.location.replace('login.html');
              }
          }).catch((error) => {
              console.log('logged out');
              console.log(error);
              window.location.replace('login.html');
          });

      } else {
        console.log('logged out');
        window.location.replace('login.html');
      }
    });
});
