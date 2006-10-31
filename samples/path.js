function draw() {
 // var canvas = document.getElementById("canvas");
  var ctx = canvas.getContext("2d");

  ctx.fillStyle = "red";

  ctx.beginPath();
  ctx.moveTo(30, 30);
  ctx.lineTo(150, 150);
  ctx.quadraticCurveTo(60, 70, 70, 150);
  ctx.lineTo(30, 30);
  ctx.fill();
}

canvas = new Image(200, 200);
window = new Window("Path", canvas);
draw();

