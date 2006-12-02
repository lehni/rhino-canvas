function draw() {
// var canvas = document.getElementById("canvas");
 var ctx = canvas.getContext("2d");

 ctx.fillStyle = "rgb(200,0,0)";
 ctx.fillRect (10.0, 10.0, 55.0, 50.0);

 ctx.fillStyle = "rgba(0, 0, 200, 0.5)";
 ctx.fillRect (30, 30, 55, 50);
}

//if(!canvas){
  canvas = new Image(100, 100);
  window = new Frame("simple", canvas);
//}
draw();
