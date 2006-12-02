// (c) chris malcom, copied from http://chris-malcolm.com/canvas/breakout.html


canvas = new Image(120, 120);
document = new Frame("Breakout", canvas);

var arena=[];
var ctx;
var paddlemovement="null";
var balldir=45;
var canvas;
var ballctx;
var ballcanvas;
var level=0;
arena[0]=[1,0,0,0,0,0,0,0,0,0];
arena[1]=[1,0,0,0,0,0,0,0,0,0];
arena[2]=[1,0,0,0,0,0,0,0,0,0];
arena[3]=[1,0,0,0,0,0,0,0,0,0];
arena[4]=[1,0,0,0,0,0,0,0,0,0];
arena[5]=[1,0,0,0,0,0,0,0,0,0];
arena[6]=[1,0,0,0,0,0,0,0,0,0];
arena[7]=[1,0,0,0,0,0,0,0,0,0];
arena[8]=[1,0,0,0,0,0,0,0,0,0];
arena[9]=[1,0,0,0,0,0,0,0,0,0];
var barwidth=30;
var barheight=6;
var barx=60;
var game;
var speed=2;
var bary=114;
var ballx=80;
var bally=108;
var ballheight=6;
var ballwidth=6;
var status=-1;
var balllaunched=0;
var done=false;
var startdate;
var time;

function checkwin(){

  var datenow=new Date();
  time=datenow-startdate;
  var min=Math.floor(time/60000);
  if (min<10)
    min="0"+min;
  var sec=Math.floor((time%60000)/1000);
  if (sec<10)
    sec="0"+sec;
  time=min+":"+sec;
  done=true;
  for (var i=0; i<=9; i++){
    for (var j=0; j<=9; j++){
      if (arena[i][j]==1){
        done=false;
        break;
      }
    }
  } 
}

function gameover(){
  clearInterval(game);
  arena[0]=[1,0,0,0,0,0,0,0,0,0];
  arena[1]=[1,0,0,0,0,0,0,0,0,0];
  arena[2]=[1,0,0,0,0,0,0,0,0,0];
  arena[3]=[1,0,0,0,0,0,0,0,0,0];
  arena[4]=[1,0,0,0,0,0,0,0,0,0];
  arena[5]=[1,0,0,0,0,0,0,0,0,0];
  arena[6]=[1,0,0,0,0,0,0,0,0,0];
  arena[7]=[1,0,0,0,0,0,0,0,0,0];
  arena[8]=[1,0,0,0,0,0,0,0,0,0];
  arena[9]=[1,0,0,0,0,0,0,0,0,0];
  bary=114;
  barx=60;
  speed=2;
  balldir=45;
  ballx=80;
  bally=108;
  level=0;
  paddlemovement="null";
  balllaunched=false;
  time=0;
  status=-1;
  //document.getElementById("info").innerHTML="<font color='red'><b>game over. Press 's' to start</b></font><br>";
  //print ("game over. Press 's' to start");
  loadmap();
}

function checkcollisions(){

  //check if lost
  if (bally>=120){
    gameover();
  }

  balldir=balldir%360;
  while (balldir<0){
    balldir+=360;
  }


  //restrict paddle
  if ((paddlemovement=="right" && barx+barwidth>=120) || (paddlemovement=="left" && barx<=0)){
    paddlemovement="null";
  }


  //brick collision
  for (var i=0; i<=9; i++){
    for (var j=0; j<=9; j++){
      if (arena[i][j]==1 && ballx<i*12+12 && ballx>=i*12 && bally+ballheight>=j*12 && bally<=j*12+12){
        balldir=-balldir;
        arena[i][j]=0;
      }
    }
  }
  //top wall collision
  if (bally-ballheight<=0)
    balldir=-balldir;

  //left wall collision
  if (ballx<=0){
    if (balldir<=180 && balldir>=90){
      balldir=balldir+270;
    }
    if (balldir>180 && balldir<=270){
      balldir=balldir+90;
    }
  }
  //right wall collision
  if (ballx+ballwidth>=120){
    if (balldir<=360 && balldir>=270){ 
      balldir=balldir+270;
    }
    if (balldir>=0 && balldir<=90){
      balldir=balldir+90;
    }
  }

  //paddle collision

  if (ballx<=barx+barwidth && ballx>=barx  && bally+ballheight>=bary){
    speed+=.01;
    var offset=Math.round(ballx-((barx+barwidth)/2));
    while (balldir<0){
      balldir+=360;
    }
    if (offset<0 && balldir>270 && balldir<360){
      balldir=(balldir+180)-(30-Math.abs(offset))*3;
    }
    else if (offset<0 && balldir<=270 && balldir>=180){
      balldir=(balldir+270)-(30-Math.abs(offset))*3;
    }
    else if(offset>0 && balldir>270 && balldir<360){
      balldir=(balldir+90)+(30-Math.abs(offset))*3;
    }
    else if(offset>0 && balldir<=270 && balldir>=180){
      balldir=(balldir+180)+(30-Math.abs(offset))*3; 
    }
    else if(offset==0){
      balldir=-balldir;
    }
  }
}

function loadmap(){
  //if (status==-1)
  //  print("game over. Prss 's' to start");
    //document.getElementById("info").innerHTML="<font color='red'><b>game over. Press 's' to start</b></font><br>";
    //canvas=document.getElementById("map");
  if (canvas.getContext){
    ctx= canvas.getContext('2d');
    ctx.fillStyle="#C0C0C0";
    ctx.fillRect(0,0,120,120);
    for (var i=0; i<=9; i++){
      for (var j=0; j<=9; j++){
        if (arena[i][j]==1){
          ctx.fillStyle = 'rgb(' + Math.floor(255-25.5*i) + ',' + Math.floor(255-25.5*j) + ',0)';
          ctx.fillRect(i*12,j*12,12,12);
          ctx.strokeRect(i*12,j*12,12,12);
        }
      }
    }
    ctx.fillStyle="black";
    if(status == -1){
       ctx.textStyle.textAlign="center";
       ctx.textStyle.verticalAlign="bottom";
       ctx.drawString(60,60, "Game Over.");
      ctx.textStyle.verticalAlign="top";
       ctx.drawString(60,60, "Press 's' to start");
    }
    else if(balllaunched==false){
       ctx.textStyle.textAlign="center";
       ctx.textStyle.verticalAlign="bottom";
       ctx.drawString(60,60, "Level: "+(level+1));
       ctx.textStyle.verticalAlign="top";
       ctx.drawString(60,60, "Press 'up' to launch");
    }

  }
  //move paddle
  ctx.fillStyle="#000000";
  if (paddlemovement=="left"){
    barx=barx-5;
    if (balllaunched==false)
      ballx=ballx-5;
  }
  if (paddlemovement=="right"){
    barx+=5;
    if (balllaunched==false)
      ballx=ballx+5;
  }
  ctx.fillRect(barx, bary, barwidth, barheight);
    //ball movement
  ctx.fillStyle="red";
  ctx.fillRect(ballx, bally, ballwidth, ballheight);
  if (balllaunched==true){
    ballx = ballx + Math.cos((balldir)*Math.PI/180) * speed;
    bally = bally - Math.sin((balldir)*Math.PI/180) * speed;
  }
}

function last(){
  if (level==8 && done==true){
    alert("Wow you completed all 8 levels! Congrats..your time was:\n"+time);
    status==-1;
  }
 // if (status!=-1){
 //    print("Level: "+(level+1));
   //   document.getElementById("info").innerHTML="<b>Level:</b>"+(level+1)+"<br><b>Time:</b> "+time;
 // }
  if (done==true){
    level++;
    for (var i=0; i<=level; i++){
      for (var j=0; j<=9; j++){
        arena[j][i]=1;
      }
    }
    barx=60;
    bary=114;
    balldir=45;
    ballx=80;
    bally=108;
    speed=2;
    balllaunched=false;
    done=false;
  }
}

document.onkeydown=function(e){
///if (!e){
//var e=window.event;
//}
  if(e.which){ 
    var keycode = e.which;
  } else {
    var keycode = e.keyCode ;
  }
  if (keycode==83){ //s=start/pause
    if (status==-1){ //game not started
      startdate=new Date();
      status=1; //game in play
      game=setInterval("main()", 25);

    }
  }
  if (keycode==37){ //left=move left
    paddlemovement="left";
  }
  else if (keycode==39){ //right=move right
    paddlemovement="right";
  }
  else if (keycode==38){ //up=launch ball
    balllaunched=true;
  }
}

document.onkeyup=function(e){
   paddlemovement="null";
}


function main(){
  checkwin();
  checkcollisions();
  loadmap();
  last();
}


main();