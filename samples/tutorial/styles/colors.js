
// source: http://developer.mozilla.org/en/docs/Canvas_tutorial:


function draw() {
  var ctx = document.getElementById('canvas').getContext('2d');
  for (i=0;i<6;i++){
    for (j=0;j<6;j++){
      ctx.fillStyle = 'rgb(' + Math.floor(255-42.5*i) + ',' + 
                       Math.floor(255-42.5*j) + ',0)';
      ctx.fillRect(j*25,i*25,25,25);
    }
  }
}

draw();