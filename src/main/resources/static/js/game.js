let stompClient = null;

const websocketUrl = '/api/game-ws';

const topicGameInfo = '/topic/game-info';
const topicGameStatus = "/topic/game-status";
const topicGameHello = '/api/game-hello';

const start = () => {
    if (stompClient === null) {
        stompClient = Stomp.over(new SockJS(websocketUrl));
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            const sessionId = $("#sessionId").text().toString()
            console.log("start: sessionId = ", sessionId)
            stompClient.subscribe(`${topicGameInfo}.${sessionId}`, (gameInfo) => showInitialGameInfo(JSON.parse(gameInfo.body)));
            stompClient.subscribe(`${topicGameStatus}.${sessionId}`, (status) => updateGameStatus(status.body));

            stompClient.send(`${topicGameHello}.${sessionId}`, {}, {})
        });
    } else {
        console.log("Stomp client already exists")
    }
}

// gameInfo - object of type UIGameInitialInfo
const showInitialGameInfo = (gameInfo) => {
    $("#playerName1").text(gameInfo.playerName1)
    $("#money1").text(gameInfo.money1)
    $("#count1").text(gameInfo.count1)

    $("#playerName2").text(gameInfo.playerName2)
    $("#money2").text(gameInfo.money2)
    $("#count2").text(gameInfo.count2)

    $("#allTurns").text(gameInfo.turns)
    $("#currentTurn").text(gameInfo.currentTurn)

    if(gameInfo.isPlayer.toLowerCase() !== 'true') $("#playerCheatInfo").hide()
    $("#allCheats").text(gameInfo.cheats)
    $("#availCheats").text(gameInfo.availCheats)

    gameInfo.figures.forEach(figure => makeIcon(figure))

    $("#gameBet").text(gameInfo.bet)
}

const updateGameStatus = (status) => {
    $("#gameStatus").text(status)
}

const makeIcon = (figure) => {
    const icon = $('<img/>')
        .attr({
            'src': `icons/${figure.icon}.png`,
            'class': 'rounded-circle',
            'alt': figure.title
        })
        .click(() => processIconClick(figure))
    $(`#cell${figure.icon}`)
        .empty()
        .append(icon)
}

const processIconClick = (figure) => {
    console.log("processIconClick on icon ", figure)
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    start()
});
