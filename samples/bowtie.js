function drawBowtie(ctx, fillStyle) {
 
   ctx.fillStyle = "rgba(200,200,200,0.3)";
   ctx.fillRect(-30, -30, 60, 60);
 
   ctx.fillStyle = fillStyle;
   ctx.globalAlpha = 1.0;
   ctx.beginPath();
   ctx.moveTo(25, 25);
   ctx.lineTo(-25, -25);
   ctx.lineTo(25, -25);
   ctx.lineTo(-25, 25);
   ctx.closePath();
   ctx.fill();
 }
 
 function dot(ctx) {
   ctx.save();
   ctx.fillStyle = "black";
   ctx.fillRect(-2, -2, 4, 4);
   ctx.restore();
 }
 
 function draw() {
  // var canvas = document.getElementById("canvas");
   var ctx = canvas.getContext("2d");

   // note that all other translates are relative to this
   // one
   ctx.translate(45, 45);

   ctx.save();
   //ctx.translate(0, 0); // unnecessary
   drawBowtie(ctx, "red");
   dot(ctx);
   ctx.restore();
 
   ctx.save();
   ctx.translate(85, 0);
   ctx.rotate(45 * Math.PI / 180);
   drawBowtie(ctx, "green");
   dot(ctx);
   ctx.restore();
 
   ctx.save();
   ctx.translate(0, 85);
   ctx.rotate(135 * Math.PI / 180);
   drawBowtie(ctx, "blue");
   dot(ctx);
   ctx.restore();
 
   ctx.save();
   ctx.translate(85, 85);
   ctx.rotate(90 * Math.PI / 180);
   drawBowtie(ctx, "yellow");
   dot(ctx);
   ctx.restore();
 }

canvas = new CanvasFrame("bowtie", 200, 200);
draw();
