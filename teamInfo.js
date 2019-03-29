var database = firebase.database();
var ref = database.ref();
var matches = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];

var imageTable, hatchPreload, cargoPreload, cargoPreloadList, l1Start, l2Start, climb1, climb2, climb3, cargoHatchList, cargoCargoList, r1HatchList, r1CargoList, r2HatchList, r2CargoList, r3HatchList, r3CargoList, 
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

      hatchPreload = 0;
      cargoPreload = 0;

      for(d in data[19]) {
        if(data[19][d] == true) hatchPreload++;
      }

      for(d in data[20]) {
        if(data[20][d] == true) cargoPreload++;
      }

      l1Start = 0;
      l2Start = 0;

      for(d in data[21]) {
        if(data[21][d] == true) l1Start++;
      }

      for(d in data[22]) {
        if(data[22][d] == true) l2Start++;
      }

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

      climb1 = 0;
      for(d in data[23]){
          climb1++;
          if(data[23][d] == 'y'){ 
          }
      }

      var climb2txt = '';
      climb2 = 0;
      for(d in data[8]){
          climb2++;
          if(data[8][d] == 'y'){
            climb2txt = climb2txt.concat('<span class="label label-success">Yes</span> ');
            
          }
          else if(data[8][d] == 'n')climb2txt = climb2txt.concat('<span class="label label-default">No</span> ');
          else if(data[8][d] == 'f')climb2txt = climb2txt.concat('<span class="label label-danger">Failed</span> ');
          else if(data[8][d] == 'c')climb2txt = climb2txt.concat('<span class="label label-warning">Cancelled (?)</span> ');
          else climb2txt = climb2txt.concat(data[8][d] + ' ');
          console.log(climb2txt);
      }
      climbList2.innerHTML = climb2txt;

      var climb3txt = '';
      climb3 = 0;
      for(d in data[9]){
          climb3++; 
          if(data[9][d] == 'y') {
            climb3txt = climb3txt.concat('<span class="label label-success">Yes</span> ');
            
          }
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

      var ct1 = document.getElementById("preloadGraph");
      var graph1 = new Chart(ct1, {
        type: 'pie',
        data: {
          labels: ["hatches", "cargo"],
          datasets: [
          { 
              data: [hatchPreload, cargoPreload],
              backgroundColor: [
              "#7442f4",
              "#f4dc41"
              ],
              borderColor: [
              "#7442f4",
              "#f4dc41"
              ],
              borderWidth: [1, 1, 1, 1, 1]
          }]
        },
        options: {
          title: {
            display: true,
            position: "top",
            text: "Preload",
            fontSize: 18,
            fontColor: "#111"
          }
        }
      });

      var ct2 = document.getElementById("startingLevelGraph");
      var graph2 = new Chart(ct2, {
        type: 'pie',
        data: {
          labels: ["hab 1", "hab 2"],
          datasets: [
          { 
              data: [l1Start, l2Start],
              backgroundColor: [
              "#7442f4",
              "#f4dc41"
              ],
              borderColor: [
              "#7442f4",
              "#f4dc41"
              ],
              borderWidth: [1, 1, 1, 1, 1]
          }]
        },
        options: {
          title: {
            display: true,
            position: "top",
            text: "Starting Position",
            fontSize: 18,
            fontColor: "#111"
          }
        }
      });

      var ct3 = document.getElementById("endGameGraph");
      var graph3 = new Chart(ct3, {
        type: 'pie',
        data: {
          labels: ["hab 1", "hab 2", "hab 3"],
          datasets: [
          { 
              data: [climb1, climb2, climb3],
              backgroundColor: [
              "#7442f4",
              "#f4dc41",
              "#000000"
              ],
              borderColor: [
              "#7442f4",
              "#f4dc41",
              "#000000"
              ],
              borderWidth: [1, 1, 1, 1, 1]
          }]
        },
        options: {
          title: {
            display: true,
            position: "top",
            text: "Ending Position",
            fontSize: 18,
            fontColor: "#111"
          }
        }
      });

      var sandstormTotal = [];
      for(d in data[24]) {
        sandstormTotal.push(data[24][d]);
        sandstormTotal[d] += data[25][d];
        sandstormTotal[d] += data[10][d];
        sandstormTotal[d] += data[11][d];
      }

      var ct4 = document.getElementById("sandstormGraph");
      var graph4 = new Chart(ct4, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: sandstormTotal,
              label: "Sandstorm Score",
              borderColor: "#3e95cd",
              fill: false
          }
          ]
        }
      });

      var teleopTotal = [];
      for(d in data[26]) {
        teleopTotal.push(data[26][d]);
        teleopTotal[d] += data[27][d];
        teleopTotal[d] += data[0][d];
        teleopTotal[d] += data[1][d];
      }

      var ct5 = document.getElementById("teleopGraph");
      var graph5 = new Chart(ct5, {
        type: 'line',
        data: {
          labels: matches,
          datasets: [
          { 
              data: teleopTotal,
              label: "Teleop Score",
              borderColor: "#3e95cd",
              fill: false
          }
          ]
        }
      });

      var scorePlots = document.getElementById("scorePlots");

      var hatchList = [data[0], data[2], data[4], data[6]];
      var cargoList = [data[1], data[3], data[5], data[7]];
      var cargoHatchTrace = {
        y: hatchList[0],
        x: hatchList[0].map(()=>"Hatches"),
        name: "Cargo Ship Hatches",
        type: 'box'
      }
      
      var r1HatchTrace = {
        y: hatchList[1],
        x: hatchList[1].map(()=>"Hatches"),
        name: "Rocket L1 Hatches",
        type: 'box'
      }

      var r2HatchTrace = {
        y: hatchList[2],
        x: hatchList[2].map(()=>"Hatches"),
        name: "Rocket L2 Hatches",
        type: 'box'
      }

      var r3HatchTrace = {
        y: hatchList[3],
        x: hatchList[3].map(()=>"Hatches"),
        name: "Rocket L3 Hatches",
        type: 'box'
      }

      var cargoCargoTrace = {
        y: cargoList[0],
        x: cargoList[0].map(()=>"Cargo"),
        name: "Cargo Ship Cargo",
        type: 'box'
      }
      
      var r1CargoTrace = {
        y: cargoList[1],
        x: cargoList[1].map(()=>"Cargo"),
        name: "Rocket L1 Cargo",
        type: 'box'
      }

      var r2CargoTrace = {
        y: cargoList[2],
        x: cargoList[2].map(()=>"Cargo"),
        name: "Rocket L2 Cargo",
        type: 'box'
      }

      var r3CargoTrace = {
        y: cargoList[3],
        x: cargoList[3].map(()=>"Cargo"),
        name: "Rocket L3 Cargo",
        type: 'box'
      }

      var layout = {
        width: 1000,
        height: 700,
        showLegend: false,
        yaxis: {
          zeroline: false
        },
        boxmode: 'group'
      };

      var outData = [cargoHatchTrace, r1HatchTrace, r2HatchTrace, r3HatchTrace, cargoCargoTrace, r1CargoTrace, r2CargoTrace, r3CargoTrace];

      Plotly.newPlot(scorePlots, outData, layout);
    });

}

function getData(team, callback){
  var hatchPreload = [];
  var cargoPreload = [];
  var l1Start = [];
  var l2Start = [];
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
  var climb3 = [];
  var superComments = [];

  var totalRocketHatchS = [];
  var totalRocketCargoS = [];
  var totalRocketHatch = [];
  var totalRocketCargo = [];

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
            climb1.push(entry['T']['Tl1']);
            climb2.push(entry['T']['Tl2']);
            climb3.push(entry['T']['Tl3']);

            totalRocketHatch.push(entry['T']['Ttrh']);
            totalRocketCargo.push(entry['T']['Ttrc']);
        }
        
        if(entry['A']) {
            hatchPreload.push(entry['A']['Ssh']);
            cargoPreload.push(entry['A']['Ssc']);
            l1Start.push(entry['A']['Ssl1']);
            l2Start.push(entry['A']['Ssl2']);
            cargoHatchS.push(entry['A']['Sscsh']);
            cargoCargoS.push(entry['A']['Sscsc']);
            r1HatchS.push(entry['A']['Ssrh1']);
            r1CargoS.push(entry['A']['Ssrc1']);
            r2HatchS.push(entry['A']['Ssrh2']);
            r2CargoS.push(entry['A']['Ssrc2']);
            r3HatchS.push(entry['A']['Ssrh3']);
            r3CargoS.push(entry['A']['Ssrc3']);

            totalRocketHatchS.push(entry['A']['Sstrh']);
            totalRocketCargoS.push(entry['A']['Sstrc']);
        }

        if(entry['super']) superComments.push(entry['super'] + " (SUPERSCOUT)");
        if(entry['P'] && entry['P']['cmnt']) superComments.push(entry['P']['cmnt'] + " (SCOUT)");
      }

      callback([cargoHatch, cargoCargo, r1Hatch, r1Cargo, r2Hatch, r2Cargo, r3Hatch, r3Cargo, climb2, climb3, 
                cargoHatchS, cargoCargoS, r1HatchS, r1CargoS, r2HatchS, r2CargoS, r3HatchS, r3CargoS, superComments, 
                hatchPreload, cargoPreload, l1Start, l2Start, climb1, totalRocketCargoS, totalRocketHatchS, 
                totalRocketCargo, totalRocketHatch]);
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
