source = new Image();
source.src = 'rhino.jpg';
frame = new Image();
frame.src = 'frame.png';

function draw() {
  //  var ctx = document.getElementById('canvas').getContext('2d');
    var ctx = canvas.getContext('2d');

  // Draw slice
  ctx.drawImage(source, 33,71,104,124,21,20,87,104);

  // Draw frame
  ctx.drawImage(frame,0,0);
}


canvas = new Image(130,150);
new Window("drawImage3", canvas);
draw();