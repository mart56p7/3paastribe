        //Specify, if we are in the queue looking for a game
        var findgame = false;
        var joinqueue = false;
        var gamerunning = false;
        //token: A token is generated for each player, when registering a move or a surrender, the token must be used.
        var gametoken = "";
        //The player name
        var playername = "";
        var myturn = false;
        var playerid = null;
        var gameid = 0;

        var movefrom = -1;
        var moveto = -1;
        var fieldsdata=[];

        var playerpieces = "NONE";


        function getPlayerIcon(){
            if(playerpieces == "CROSS"){
                return "<img src='"+crossimage+"' class='playericon'>";
            }
            else{
                return "<img src='"+circleimage+"' class='playericon'>";
            }
        }

        function getOpponentIcon(){
            if(playerpieces == "CROSS"){
                return "<img src='"+circleimage+"' class='opponenticon'>";
            }
            else{
                return "<img src='"+crossimage+"' class='opponenticon'>";
            }
        }

        function displayStartScreen(){
            document.getElementById("playername").value = playername;
            document.getElementById("gamemenu").style.display = 'block';
            document.getElementById("queuejoin").style.display = 'block';
        }

        function hideStartScreen(){
            document.getElementById("queuejoin").style.display = 'none';

        }

        function displayJoinQueue(){
            hideGame()
            document.getElementById("queueinfo").style.display = 'block';
        }

        function hideJoinQueue(){
            document.getElementById("queueinfo").style.display = 'none';
        }

        function joinQueue(){
            hideGameCompleted();
            hideStartScreen();
            displayJoinQueue();
            playername = document.getElementById("playername").value;
            if(playername == ""){
                playername = "Mr Brown";
            }
            getToken();
            joinqueue = true;
        }

        function cronJoinQueue(){
            if(gametoken != ""){

                $.ajax({
                    url: '/REST/joinQueue',
                    type: 'put',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        token: gametoken,
                        playername: playername
                    }),
                    success: function() {
                        joinqueue = false;
                        findgame = true;
                    }
                });
            }
        }

        function getToken(){
            if(gametoken == ""){
                $.post("/REST/createToken",
                    {
                    },
                    function(data){
                        if(data.token){
                            gametoken = data.token;
                        }
                    }
                );
            }
        }

        function leaveQueue(){
            findgame = false;
            hideJoinQueue();
            displayStartScreen();
            joinqueue = false;

            $.ajax({
                url: '/REST/leaveQueue',
                type: 'delete',
                contentType: 'application/json',
                data: JSON.stringify({
                    token: gametoken
                }),
                success: function() {
                    displayStartScreen();
                }
            });
        }

        function displayGame(){
            document.getElementById("game").style.display = 'block';
            document.getElementById("field10").innerHTML = "<div class='cross'></div>";
            displayGameOptions();
        }

        function hideGame(){
            document.getElementById("game").style.display = 'none';
            hideGameOptions()
        }

        function displayGameOptions(){
            document.getElementById("gameplayeroptions").style.display = 'block';
        }

        function hideGameOptions(){
            document.getElementById("gameplayeroptions").style.display = 'none';
        }

        function cronFindGame(){
            if(gametoken != ""){
                $.ajax({
                    method: "GET",
                    url: "/REST/findGame/"+gametoken,
                    contentType: "JSON"
                }).done(function (data) {
                    findgame = false;
                    myturn = data.myturn;
                    playerid = data.myid;
                    gameid = data.id;
                    //Lets show the board
                    hideJoinQueue();
                    gamerunning = true;
                    var gameopponentname = "";
                    if(data.circleplayer.id == playerid){
                        gameopponentname = data.crossplayer.playername;
                        playerpieces = "CIRCLE";
                    }
                    else{
                        gameopponentname = data.circleplayer.playername;
                        playerpieces = "CROSS";
                    }
                    displayFindGameData(playername, gameopponentname, data.fields)
                    displayGame();
                });
            }
        }

        function saveMove(){
            $.post("/REST/saveMove",
                {
                    token: gametoken,
                    gameid: gameid,
                    from: movefrom,
                    to: moveto
                },
                function(){
                    myturn = false;
                    movefrom = -1;
                    moveto = -1;
                }
            );

        }

        function move(fieldid, num){
            if(myturn && moveto == -1){
                if(movefrom == -1 && fieldsdata[num] != "NONE" && fieldsdata[num] == playerpieces){
                    startdone = true;
                    for(i = 10; i < 16; i++){
                        startdone = (fieldsdata[i] == "NONE") && startdone;
                    }
                    if((startdone && num < 10) || (!startdone && num > 9)){
                        document.getElementById(fieldid).style.border = "3px solid green";
                        movefrom = num;
                    }
                }
                else if(movefrom == num && moveto == -1){
                    document.getElementById("field"+movefrom).style.border = "3px solid silver";
                    movefrom = -1;
                }
                else if(movefrom != -1 && moveto == -1 && fieldsdata[num] == "NONE"){
                    if(num > 0 && num < 10){
                        document.getElementById(fieldid).style.border = "3px solid yellow";
                        moveto = num;
                    }
                }

                if(movefrom != -1 && moveto != -1){
                    if(movefrom > 9){
                        document.getElementById("field"+movefrom).style.border = "3px solid darkgray";
                    }
                    else{
                        document.getElementById("field"+movefrom).style.border = "3px solid silver";
                    }
                    document.getElementById("field"+moveto).style.border = "3px solid silver";
                    setField("field"+movefrom, "NONE");
                    setField("field"+moveto, fieldsdata[movefrom]);
                    saveMove();
                }
            }
        }

        function setField(fieldid, piece){
            if(piece == "CIRCLE"){
                //document.getElementById(fieldid).innerHTML = "<div class='circle'></div>";
                document.getElementById(fieldid).style.backgroundImage = "url('"+circleimage+"')";
                document.getElementById(fieldid).style.backgroundRepeat = "no-repeat";
                //document.getElementById(fieldid).style.backgroundAttachment = "fixed";
                document.getElementById(fieldid).style.backgroundPosition = "center";
            }
            else if(piece == "CROSS"){
                document.getElementById(fieldid).style.backgroundImage = "url('"+crossimage+"')";
                document.getElementById(fieldid).style.backgroundRepeat = "no-repeat";
                //document.getElementById(fieldid).style.backgroundAttachment = "fixed";
                document.getElementById(fieldid).style.backgroundPosition = "center";
            }
            else{
                document.getElementById(fieldid).style.backgroundImage = "";
            }
        }

        function displayGameData(fields){
            if(Array.isArray(fields)){
                fields.forEach(
                    function(item, index){
                        fieldsdata[item.fieldnumber] = item.piece;
                        setField("field" + item.fieldnumber, item.piece);
                    }
                );
            }
            if(myturn){
                document.getElementById("gameplayername").style.fontWeight = "bold";
                document.getElementById("gameopponentname").style.fontWeight = "normal";
            }
            else{
                document.getElementById("gameplayername").style.fontWeight = "normal";
                document.getElementById("gameopponentname").style.fontWeight = "bold";
            }
        }

        function displayFindGameData(player, opponent, fields){
            document.getElementById("gameplayername").innerHTML  = getPlayerIcon() + player;
            document.getElementById("gameopponentname").innerHTML  = getOpponentIcon() + opponent;
            displayGameData(fields);
        }

        function cronGetGameData(){
            if(gametoken != "" && !myturn){
                $.ajax({
                    method: "GET",
                    url: "/REST/getGameData/"+gameid+"/"+gametoken,
                    contentType: "JSON"
                }).done(function (data) {
                    if(gamerunning){
                        if(data.winner != null){
                            displayGameData(data.fields);
                            gameCompleted(data.winner.token == gametoken);
                        }
                        else{
                            myturn = data.myturn;
                            displayGameData(data.fields);
                        }
                    }
                });
            }
        }

        function surrenderGame(){
            $.ajax({
                url: '/REST/surrenderGame',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify({
                    token: gametoken
                }),
                success: function() {
                    gameCompleted(false);
                }
            });
        }

        function gameCompleted(winner){
            gamerunning = false;
            hideGameOptions();
            displayGameCompleted(winner)
            displayStartScreen();
        }

        function displayGameCompleted(winner){
            document.getElementById("gamecompleted").style.display = 'block';
            if(winner){
                document.getElementById("gamecompletedwinner").style.display = 'block';
                document.getElementById("gamecompletedlooser").style.display = 'none';
            }
            else{
                document.getElementById("gamecompletedwinner").style.display = 'none';
                document.getElementById("gamecompletedlooser").style.display = 'block';
            }
        }

        function hideGameCompleted(){
            document.getElementById("gamecompleted").style.display = 'none';
        }

        //Init cron calls
        window.onload = function() {
            setInterval(function(){ if(findgame) cronFindGame(); }, 3000);
            setInterval(function(){ if(joinqueue) cronJoinQueue(); }, 3000);
            setInterval(function(){ if(gamerunning) cronGetGameData(); }, 1000)
        };