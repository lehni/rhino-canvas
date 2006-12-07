miterLimit = 10;

function draw() {
  var ctx = document.getElementById('canvas').getContext('2d');

        // Clear canvas
  ctx.clearRect(0,0,150,150);
  
        // Draw guides
  ctx.strokeStyle = '#09f';
  ctx.lineWidth   = 2;
  ctx.strokeRect(-5,50,160,50);
  
        // Set line styles
  ctx.strokeStyle = '#000';
  ctx.lineWidth = 10;
  
  ctx.miterLimit = miterLimit;
  
        // Draw lines
  ctx.beginPath();
  ctx.moveTo(0,100);
  for (i=0;i<24;i++){
    var dy = i%2==0 ? 25 : -25 ;
    ctx.lineTo(Math.pow(i,1.5)*2,75+dy);
  }
  ctx.stroke();
  return false;
}


draw();
