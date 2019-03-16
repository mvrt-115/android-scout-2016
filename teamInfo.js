var database = firebase.database();
var ref = database.ref();
var matches = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

var imageTable, cargoHatchList, cargoCargoList, r1HatchList, r1CargoList, r2HatchList, r2CargoList, r3HatchList, r3CargoList, 
    cargoHatchListS, cargoCargoListS, r1HatchListS, r1CargoListS, r2HatchListS, r2CargoListS, r3HatchListS, r3CargoListS, climbList2, climbList3, commentsList;

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

      var avg1 = data[0].reduce((x,y) => x+y, 0) / (data[0].length);
      var avg2 = data[1].reduce((x,y) => x+y, 0) / (data[1].length);
      var avg3 = data[2].reduce((x,y) => x+y, 0) / (data[2].length);
      var avg4 = data[3].reduce((x,y) => x+y, 0) / (data[3].length);
      var avg5 = data[4].reduce((x,y) => x+y, 0) / (data[4].length);
      var avg6 = data[5].reduce((x,y) => x+y, 0) / (data[5].length); 
      var avg7 = data[6].reduce((x,y) => x+y, 0) / (data[6].length);
      var avg8 = data[7].reduce((x,y) => x+y, 0) / (data[7].length);
      var avg9 = data[10].reduce((x,y) => x+y, 0) / (data[10].length);
      var avg10 = data[11].reduce((x,y) => x+y, 0) / (data[11].length);
      var avg11 = data[12].reduce((x,y) => x+y, 0) / (data[12].length);
      var avg12 = data[13].reduce((x,y) => x+y, 0) / (data[13].length);
      var avg13 = data[14].reduce((x,y) => x+y, 0) / (data[14].length);
      var avg14 = data[15].reduce((x,y) => x+y, 0) / (data[15].length); 
      var avg15 = data[16].reduce((x,y) => x+y, 0) / (data[16].length);
      var avg16 = data[17].reduce((x,y) => x+y, 0) / (data[17].length);

      cargoHatchList.innerHTML = data[0] + ' (avg: ' + avg1.toFixed(2) + ')';
      cargoCargoList.innerHTML = data[1] + ' (avg: ' + avg2.toFixed(2) + ')';
      r1HatchList.innerHTML = data[2] + ' (avg: ' + avg3.toFixed(2) + ')';
      r1CargoList.innerHTML = data[3] + ' (avg: ' + avg4.toFixed(2) + ')';
      r2HatchList.innerHTML = data[4] + ' (avg: ' + avg5.toFixed(2) + ')';
      r2CargoList.innerHTML = data[5] + ' (avg: ' + avg6.toFixed(2) + ')';
      r3HatchList.innerHTML = data[6] + ' (avg: ' + avg7.toFixed(2) + ')';
      r3CargoList.innerHTML = data[7] + ' (avg: ' + avg8.toFixed(2) + ')';
      cargoHatchListS.innerHTML = data[10] + ' (avg: ' + avg9.toFixed(2) + ')';
      cargoCargoListS.innerHTML = data[11] + ' (avg: ' + avg10.toFixed(2) + ')';
      r1HatchListS.innerHTML = data[12] + ' (avg: ' + avg11.toFixed(2) + ')';
      r1CargoListS.innerHTML = data[13] + ' (avg: ' + avg12.toFixed(2) + ')';
      r2HatchListS.innerHTML = data[14] + ' (avg: ' + avg13.toFixed(2) + ')';
      r2CargoListS.innerHTML = data[15] + ' (avg: ' + avg14.toFixed(2) + ')';
      r3HatchListS.innerHTML = data[16] + ' (avg: ' + avg15.toFixed(2) + ')';
      r3CargoListS.innerHTML = data[17] + ' (avg: ' + avg16.toFixed(2) + ')';


      var climb2txt = '';
      for(d in data[8]){
          if(data[8][d] == 'y')climb2txt = climb2txt.concat('<span class="label label-success">Yes</span> ');
          else if(data[8][d] == 'n')climb2txt = climb2txt.concat('<span class="label label-default">No</span> ');
          else if(data[8][d] == 'f')climb2txt = climb2txt.concat('<span class="label label-danger">Failed</span> ');
          else if(data[8][d] == 'c')climb2txt = climb2txt.concat('<span class="label label-warning">Cancelled (?)</span> ');
          else climb2txt = climb2txt.concat(data[8][d] + ' ');
          console.log(climb2txt);
      }
      climbList2.innerHTML = climb2txt;

      var climb3txt = '';
      for(d in data[9]){
          if(data[9][d] == 'y')climb3txt = climb3txt.concat('<span class="label label-success">Yes</span> ');
          else if(data[9][d] == 'n')climb3txt = climb3txt.concat('<span class="label label-default">No</span> ');
          else if(data[9][d] == 'f')climb3txt = climb3txt.concat('<span class="label label-danger">Failed</span> ');
          else if(data[9][d] == 'c')climb3txt = climb3txt.concat('<span class="label label-warning">Cancelled (?)</span> ');
          else climb3txt = climb3txt.concat(data[9][d] + ' ');
          console.log(climb3txt);
      }
      climbList3.innerHTML = climb3txt;

      for(c in data[18]){
        commentsList.append(newCommentElement(data[18][c]));
      }

      //generateMatchArray();

      var ct1 = document.getElementById("cargoGraph");
      var graph1 = new Chart(ct1, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[0],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[1],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });

      var ct2 = document.getElementById("l1Graph");
      var graph2 = new Chart(ct2, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[2],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[3],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });

      var ct3 = document.getElementById("l2Graph");
      var graph3 = new Chart(ct3, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[4],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[5],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });

      var ct4 = document.getElementById("l3Graph");
      var graph4 = new Chart(ct4, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[6],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[7],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });


      var ct5 = document.getElementById("cargoGraphS");
      var graph5 = new Chart(ct5, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[10],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[11],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });

      var ct6 = document.getElementById("l1GraphS");
      var graph6 = new Chart(ct6, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[12],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[13],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });

      var ct7 = document.getElementById("l2GraphS");
      var graph7 = new Chart(ct7, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[14],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[15],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });

      var ct8 = document.getElementById("l3GraphS");
      var graph8 = new Chart(ct8, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[16],
              label: "Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[17],
              label: "Cargo",
              borderColor: "#9741f4",
              fill: false
          }
          ]
        }
      });

      var ct9 = document.getElementById("hatchesGraph");
      var graph9 = new Chart(ct9, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[0],
              label: "Cargo Hatches",
              borderColor: "#7a42f4",
              fill: false
          },
          { 
              data: data[2],
              label: "Level 1 Hatches",
              borderColor: "#f44198",
              fill: false
          },
          { 
              data: data[4],
              label: "Level 2 Hatches",
              borderColor: "#f441f1",
              fill: false
          },
          { 
              data: data[6],
              label: "Level 3 Hatches",
              borderColor: "#5841f4",
              fill: false
          },
          { 
              data: data[10],
              label: "Cargo Sandstorm Hatches",
              borderColor: "#41cdf4",
              fill: false
          },
          { 
              data: data[12],
              label: "Level 1 Sandstorm Hatches",
              borderColor: "#3e95cd",
              fill: false
          },
          { 
              data: data[14],
              label: "Level 2 Sandstorm Hatches",
              borderColor: "#41f458",
              fill: false
          },
          { 
              data: data[16],
              label: "Level 3 Sandstorm Hatches",
              borderColor: "#f4d041",
              fill: false
          }
          ]
        }
      });

      var ct9 = document.getElementById("cargosGraph");
      var graph9 = new Chart(ct9, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: data[1],
              label: "Cargo Cargo",
              borderColor: "#7a42f4",
              fill: false
          },
          { 
              data: data[3],
              label: "Level 1 Cargo",
              borderColor: "#f44198",
              fill: false
          },
          { 
              data: data[5],
              label: "Level 2 Cargo",
              borderColor: "#f441f1",
              fill: false
          },
          { 
              data: data[7],
              label: "Level 3 Cargo",
              borderColor: "#5841f4",
              fill: false
          },
          { 
              data: data[11],
              label: "Cargo Sandstorm Cargo",
              borderColor: "#41cdf4",
              fill: false
          },
          { 
              data: data[13],
              label: "Level 1 Sandstorm Cargo",
              borderColor: "#3e95ef",
              fill: false
          },
          { 
              data: data[15],
              label: "Level 2 Sandstorm Cargo",
              borderColor: "#41f458",
              fill: false
          },
          { 
              data: data[17],
              label: "Level 3 Sandstorm Cargo",
              borderColor: "#f4d041",
              fill: false
          }
          ]
        }
      });
  });

}

function getData(team, callback){
  var cargoHatch = [];
  var cargoCargo = [];
  var r1Hatch = [];
  var r1Cargo = [];
  var r2Hatch = [];
  var r2Cargo = [];
  var r3Hatch = [];
  var r3Cargo = [];
  var cargoHatchS = [];
  var cargoCargoS = [];
  var r1HatchS = [];
  var r1CargoS = [];
  var r2HatchS = [];
  var r2CargoS = [];
  var r3HatchS = [];
  var r3CargoS = [];
  var climb1 = [];
  var climb2 = [];
  var superComments = [];


  ref.child('matches').orderByChild('team').equalTo(team).once('value', function(snapshot){
      var data = snapshot.val();

      for(key in data) {

        var entry = data[key];

        if(entry['T']) {
            cargoHatch.push(entry['T']['Tcsh'])
            cargoCargo.push(entry['T']['Tcsc']);
            r1Hatch.push(entry['T']['Trh1']);
            r1Cargo.push(entry['T']['Trc1']);
            r2Hatch.push(entry['T']['Trh2']);
            r2Cargo.push(entry['T']['Trc2']);
            r3Hatch.push(entry['T']['Trh3']);
            r3Cargo.push(entry['T']['Trc3']);

            climb1.push(entry['T']['Tl2']);
            climb2.push(entry['T']['Tl3']);

        }
        
        if(entry['A']) {

            cargoHatchS.push(entry['A']['Sscsh']);
            cargoCargoS.push(entry['A']['Sscsc']);
            r1HatchS.push(entry['A']['Ssrh1']);
            r1CargoS.push(entry['A']['Ssrc1']);
            r2HatchS.push(entry['A']['Ssrh2']);
            r2CargoS.push(entry['A']['Ssrc2']);
            r3HatchS.push(entry['A']['Ssrh3']);
            r3CargoS.push(entry['A']['Ssrc3']);

        }
        

        if(entry['super']) superComments.push(entry['super']);
        if(entry['P'] && entry['P']['cmnt']) superComments.push(entry['P']['cmnt']);
      }

      callback([cargoHatch, cargoCargo, r1Hatch, r1Cargo, r2Hatch, r2Cargo, r3Hatch, r3Cargo, climb1, climb2, 
                cargoHatchS, cargoCargoS, r1HatchS, r1CargoS, r2HatchS, r2CargoS, r3HatchS, r3CargoS, superComments]);
  });
}

function clearUI(){
  imageTable.innerHTML = null;
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

function generateMatchArray() {
  var index = 0;
  matches = [];
  for(i in cargoHatchList) {
    index++;
    matches.push(index);
  }
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

  imageTable = document.getElementById('images');
  cargoHatchList = document.getElementById('cargoHatchList');
  cargoCargoList = document.getElementById('cargoCargoList');

  r1HatchList = document.getElementById('r1HatchList');
  r1CargoList = document.getElementById('r1CargoList');
  r2HatchList = document.getElementById('r2HatchList');
  r2CargoList = document.getElementById('r2CargoList');
  r3HatchList = document.getElementById('r3HatchList');
  r3CargoList = document.getElementById('r3CargoList');

  cargoHatchListS = document.getElementById('cargoHatchListSandstorm');
  cargoCargoListS = document.getElementById('cargoCargoListSandstorm');

  r1HatchListS = document.getElementById('r1HatchListSandstorm');
  r1CargoListS = document.getElementById('r1CargoListSandstorm');
  r2HatchListS = document.getElementById('r2HatchListSandstorm');
  r2CargoListS = document.getElementById('r2CargoListSandstorm');
  r3HatchListS = document.getElementById('r3HatchListSandstorm');
  r3CargoListS = document.getElementById('r3CargoListSandstorm');

  climbList2 = document.getElementById('climbList2');
  climbList3 = document.getElementById('climbList3');
  commentsList = document.getElementById('commentsList');
  document.getElementById('searchBtn').addEventListener('click', searchTeams);
});
