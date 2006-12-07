function draw() {
  var ctx = document.getElementById('canvas').getContext('2d');
  ctx.strokeStyle = "#fc0";
  ctx.lineWidth = 1.5;
  ctx.fillRect(0,0,300,300);

  // Uniform scaling
  ctx.save()
  ctx.translate(50,50);
  drawSpirograph(ctx,22,6,5);  // no scaling

  ctx.translate(100,0);
  ctx.scale(0.75,0.75);
  drawSpirograph(ctx,22,6,5);

  ctx.translate(133.333,0);
  ctx.scale(0.75,0.75);
  drawSpirograph(ctx,22,6,5);
  ctx.restore();

  // Non uniform scaling (y direction)
  ctx.strokeStyle = "#0cf";
  ctx.save()
  ctx.translate(50,150);
  ctx.scale(1,0.75);
  drawSpirograph(ctx,22,6,5);

  ctx.translate(100,0);
  ctx.scale(1,0.75);
  drawSpirograph(ctx,22,6,5);

  ctx.translate(100,0);
  ctx.scale(1,0.75);
  drawSpirograph(ctx,22,6,5);
  ctx.restore();

  // Non uniform scaling (x direction)
  ctx.strokeStyle = "#cf0";
  ctx.save()
  ctx.translate(50,250);
  ctx.scale(0.75,1);
  drawSpirograph(ctx,22,6,5);

  ctx.translate(133.333,0);
  ctx.scale(0.75,1);
  drawSpirograph(ctx,22,6,5);

  ctx.translate(177.777,0);
  ctx.scale(0.75,1);
  drawSpirograph(ctx,22,6,5);
  ctx.restore();
  
}


draw();


