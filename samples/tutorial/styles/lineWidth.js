function draw() {
//  var ctx = document.getElementById('canvas').getContext('2d');
  var ctx = canvas.getContext('2d');
  for (i=0;i<10;i++){
    ctx.lineWidth = 1+i;
    ctx.beginPath();
    ctx.moveTo(5+i*14,5);
    ctx.lineTo(5+i*14,140);
    ctx.stroke();
  }
}


canvas = new Image(150,150);
window = new Window("Line Width", canvas);
draw();