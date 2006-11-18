// http://www.chris-malcolm.com/canvas/snake.html
// (c) 2006 Chris Malcolm

var arenaWidth=25;
var arenaHeight=25;
var blockLength=10;
var count = 1;
var score=0;

var positionx = 10;
var positiony = 10;
	
var arena=new Array(arenaWidth);
var snakePos = new Array(arenaWidth);

for (var i=0; i<arenaWidth; i++){
	arena[i]=new Array(arenaHeight);
	snakePos[i] = new Array(arenaHeight);
}



for (var i=0; i<arenaWidth; i++) {
	for(var j=0; j<arenaHeight; j++) {
		if (i==0 || j==0 || i==arenaWidth-1 || j==arenaHeight-1)
			arena[i][j]=1;
		else
			arena[i][j]=0;
	}
}
snakePos[10][10] = count;



var ctx;

// changed:
var canvas = new Image(250, 250);
window = new Window("Snake", canvas);

var game;
var movement = null;
var gamestatus=-1;
var tailLength=2;
var beforePauseKey;


function moveSnake() {
	if (movement == null)
		return;
		
	++count;

	switch(movement) {
		case "left":
			--positionx;
			break;
		case "right":
			++positionx;
			break;
		case "down":
			++positiony;
			break;
		case "up":
			--positiony;
			break;
	}



	checkCollision();
	snakePos[positionx][positiony] = count;
}

function checkCollision(){
	if ( snakePos[positionx][positiony] != null && snakePos[positionx][positiony] > count - tailLength )
		youLose();

	if ( arena[positionx][positiony] == 1)
		youLose();
		
	if ( arena[positionx][positiony] == 2){ // Checking for FOOOOOOD!
		tailLength++;
		score++;
		//document.getElementById("score").innerHTML="<font style='color: red; font-weight: bold'>You lose</font>";
		//document.getElementById("info").innerHTML= "Score: "+ score;
		arena[positionx][positiony] = 0;
		createFood();
	}
}

function loadMap(){

  if (gamestatus==-1)
    //	document.getElementById("mapDiv").innerHTML="<canvas id=\"map\" width=\""+arenaWidth*blockLength+"\" height=\""+arenaHeight*blockLength+"\"></canvas>";
    //	canvas=document.getElementById("map");

	//if (canvas.getContext) {
		ctx= canvas.getContext('2d');
		ctx.fillStyle=  "#FF6600";
		ctx.fillRect(0,0,arenaWidth*blockLength,arenaHeight*blockLength);
	//}

	for (var i=0; i<arenaWidth; i++) {
		for (var j=0; j<arenaHeight; j++) {
			if (arena[i][j]==1) {
				ctx.fillStyle = "#0000FF";
				ctx.fillRect(i*blockLength,j*blockLength,blockLength,blockLength);
			}
			if (arena[i][j]==2) {
				ctx.fillStyle = (count % 2) ? "#FF0000" : "#FFFFFF";
				ctx.fillRect(i*blockLength,j*blockLength,blockLength,blockLength);
			}
		}
	}
}


function loadSnake() {
	ctx.fillStyle="#0000FF";
	for (var j=0; j<arenaWidth; j++) {
		for (var k=0; k<arenaHeight; k++) {
			if (snakePos[j][k] >= count - tailLength)
				ctx.fillRect(j*blockLength, k*blockLength, blockLength, blockLength);
		}
	}
}


window.onkeydown=function(e)
{
	if (!e)
	{
		var e=window.event;
	}
	if(e.which)
	{ 
		var keycode = e.which;
	} 
	else 
	{
		var keycode = e.keyCode ;
	}
	switch(keycode)
	{
		case 37: 
			if (movement!="right")
				movement="left";
			break;
		case 38:
			if (movement!="down")
				movement="up";
			break;
		case 39:
			if (movement!="left")
				movement="right";
			break;
		case 40:
			if (movement!="up")
				movement="down";
			break;
		case 80:
			if (movement != null)
			{
				beforePauseKey = movement;
				movement = null;
				//document.getElementById("info").innerHTML="<font style='color: red; font-weight: bold'>Game Paused</font>";
			}
			else {
				movement = beforePauseKey;
				//document.getElementById("info").innerHTML="<font style='color: black; font-weight: bold'>Score: "+score+" </font>";
			}
			break;
		
		case 83: //start 's'
			if (gamestatus==-1)
			{
				//document.getElementById("info").innerHTML="<font style='color: black; font-weight: bold'>Score: 0 </font>";
				createFood();
				
				game=setInterval(main, 75);
				gamestatus=1;
			}
			if (gamestatus == 2)
			{
				//document.getElementById("info").innerHTML="<font style='color: black; font-weight: bold'>Score: 0 </font>";
				count = 1;
				score=0;
				
				snakePos = new Array(arenaWidth);
				for (var i = 0; i < arenaWidth; ++i)
					snakePos[i] = new Array(arenaHeight);
				
				movement = null;
				snakePos[10][10] = count;
				positionx = 10;
				positiony = 10;
					
				arena=new Array(arenaWidth);
				
				for (var i=0; i<arenaWidth; i++)
					arena[i]=new Array(arenaHeight);
					
				
				for (var i=0; i<arenaWidth; i++)
				{
					for(var j=0; j<arenaHeight; j++)
					{
						if (i==0 || j==0 || i==arenaWidth-1 || j==arenaHeight-1)
							arena[i][j]=1;
						else
							arena[i][j]=0;
					}
				}
				tailLength=2;
				createFood();
				game=setInterval(main, 75);
				gamestatus = 1;
			}
				
		break;
	}
}

function youLose()
{
  //	document.getElementById("info").innerHTML="Score: "+score+"<br><font style='color: red; font-weight: bold'>You lose. Press 's' to restart</font>";
	movement = null;
	gamestatus=2;
	clearInterval(game);
}

function createFood()
{
	var rx, ry;
	do
	{
		rx = Math.round( (arenaWidth-1) * Math.random());
		ry = Math.round( (arenaHeight-1) * Math.random());
	}
	while (( arena[rx][ry] == 1) || (snakePos[rx][ry] > count - tailLength))
	arena[rx][ry] = 2;

}

function main()
{
	moveSnake();
	loadMap();
	loadSnake();
}


main();

