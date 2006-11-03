function draw() {
  //  var ctx = document.getElementById('canvas').getContext('2d');
    var ctx = canvas.getContext('2d');
    var img = new Image();
    img.src = 'backdrop.png';
    img.onload = function(){
      ctx.drawImage(img,0,0);
      ctx.beginPath();
      ctx.moveTo(30,96);
      ctx.lineTo(70,66);
      ctx.lineTo(103,76);
      ctx.lineTo(170,15);
      ctx.stroke();
    }
  }


canvas = new Image(180,130);
new Window("drawImage1", canvas);
draw();