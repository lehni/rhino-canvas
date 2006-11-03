function draw() {
 // var ctx = document.getElementById('canvas').getContext('2d');
  var ctx = canvas.getContext('2d');


  // create new image object to use as pattern
  var img = new Image();
  img.src = 'images/wallpaper.png';
  img.onload = function(){

    // create pattern
    var ptrn = ctx.createPattern(img,'repeat');
    ctx.fillStyle = ptrn;
    ctx.fillRect(0,0,150,150);

  }
}


canvas = new Image(150,150);
window = new Window("Wallpaper", canvas);
draw();
