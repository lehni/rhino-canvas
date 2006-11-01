// Looks different from the sample, need to verify

var canvas = new Image(150,150);
new Window("quadraticCurveTo", canvas);
var ctx = canvas.getContext("2D");

// source: http://developer.mozilla.org/en/docs/Canvas_tutorial:Drawing_shapes

ctx.beginPath();
ctx.moveTo(75,25);
ctx.quadraticCurveTo(25,25,25,62.5);
ctx.quadraticCurveTo(25,100,50,100);
ctx.quadraticCurveTo(50,120,30,125);
ctx.quadraticCurveTo(60,120,65,100);
ctx.quadraticCurveTo(125,100,125,62.5);
ctx.quadraticCurveTo(125,25,75,25);
ctx.stroke();