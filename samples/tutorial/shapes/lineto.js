var canvas = new Image(150,150);
new Window("LineTo Sample", canvas);
var ctx = canvas.getContext("2D");

// source: http://developer.mozilla.org/en/docs/Canvas_tutorial:Drawing_shapes

// Filled triangle
ctx.beginPath();
ctx.moveTo(25,25);
ctx.lineTo(105,25);
ctx.lineTo(25,105);
ctx.fill();

// Stroked triangle
ctx.beginPath();
ctx.moveTo(125,125);
ctx.lineTo(125,45);
ctx.lineTo(45,125);
ctx.closePath();
ctx.stroke();