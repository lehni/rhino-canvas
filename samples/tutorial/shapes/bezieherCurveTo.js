var canvas = new Image(150,150);
new Window("bezieherCurveTo", canvas);
var ctx = canvas.getContext("2D");

// source: http://developer.mozilla.org/en/docs/Canvas_tutorial:Drawing_shapes

// Bezier curves example
ctx.beginPath();
ctx.moveTo(75,40);
ctx.bezierCurveTo(75,37,70,25,50,25);
ctx.bezierCurveTo(20,25,20,62.5,20,62.5);
ctx.bezierCurveTo(20,80,40,102,75,120);
ctx.bezierCurveTo(110,102,130,80,130,62.5);
ctx.bezierCurveTo(130,62.5,130,25,100,25);
ctx.bezierCurveTo(85,25,75,37,75,40);
ctx.fill();