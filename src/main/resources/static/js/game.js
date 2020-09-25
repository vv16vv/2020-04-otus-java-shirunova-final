let stompClient = null;

const websocketUrl = '/api/game-ws';

const topicGameInfo = '/topic/game-info';
const topicGameStatus = '/topic/game-status';
const topicGameTurnStart = '/topic/game-turn-start';
const topicGameTurnResult = '/topic/game-turn-result';

const topicGameHello = '/api/game-hello';
const topicGameTurnEnd = '/api/game-turn-end';
const topicGameTurnNext = '/api/game-turn-next';
const topicGameUseCheat = '/api/game-use-cheat';

let buttonsInitialized = false;

const start = () => {
    if (stompClient === null) {
        stompClient = Stomp.over(new SockJS(websocketUrl));
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            const sessionId = $("#sessionId").text().toString()
            console.log("start: sessionId = ", sessionId)
            stompClient.subscribe(`${topicGameInfo}.${sessionId}`, (gameInfo) => showInitialGameInfo(sessionId, JSON.parse(gameInfo.body)));
            stompClient.subscribe(`${topicGameStatus}.${sessionId}`, (status) => updateGameStatus(status.body));
            stompClient.subscribe(`${topicGameTurnStart}.${sessionId}`, (turnInfo) => turnStart(JSON.parse(turnInfo.body)));
            stompClient.subscribe(`${topicGameTurnResult}.${sessionId}`, (resultInfo) => turnResult(JSON.parse(resultInfo.body)));

            stompClient.send(`${topicGameHello}.${sessionId}`, {}, {})
        });
    } else {
        console.log("Stomp client already exists")
    }
}

// gameInfo - object of type UIGameInitialInfo
const showInitialGameInfo = (sessionId, gameInfo) => {
    $("#playerName1").text(gameInfo.playerName1)
    $("#money1").text(gameInfo.money1)
    $("#count1").text(gameInfo.count1)

    $("#playerName2").text(gameInfo.playerName2)
    $("#money2").text(gameInfo.money2)
    $("#count2").text(gameInfo.count2)

    $("#allTurns").text(gameInfo.turns)
    $("#currentTurn").text("0")

    const isPlayer = gameInfo.isPlayer.toLowerCase() === 'true';
    if (!isPlayer) $("#playerCheatInfo").hide()
    $("#allCheats").text(gameInfo.cheats)
    $("#availCheats").text(gameInfo.cheats)

    gameInfo.figures.forEach(figure => makeIcon(sessionId, gameInfo.gameId, figure, isPlayer))

    $("#gameBet").text(gameInfo.bet)

    if (!buttonsInitialized) {
        console.log("going to initialize buttons")
        $("#nextBtn")
            .click(() => {
                console.log("NextBtn: going to send")
                stompClient.send(`${topicGameTurnNext}.${gameInfo.gameId}`, {}, sessionId)
                $("#nextBtn").hide()
            })
        $("#useCheatBtn")
            .click(() => {
                stompClient.send(`${topicGameUseCheat}.${sessionId}.${gameInfo.gameId}`, {}, {})
                $("#useCheatBtn").hide()
            })
        buttonsInitialized = true;
    }

}

const updateGameStatus = (status) => {
    $("#gameStatus").text(status)
}

const getIcon = (figure) => {
    return $('<img/>')
        .attr({
            src: `icons/${figure.icon}.png`,
            class: 'rounded-circle',
            disabled: true,
            alt: figure.itemName
        })
}

const makeIcon = (sessionId, gameId, figure, isPlayer) => {
    const icon = getIcon(figure)
    icon.attr({
        id: `icon${figure.icon}`
    })
    if (isPlayer) {
        icon.click(() => processIconClick(sessionId, gameId, figure))
    }
    $(`#cell${figure.icon}`)
        .empty()
        .append(icon)
}

const processIconClick = (sessionId, gameId, figure) => {
    const disabled = $(`#icon${figure.icon}`).prop("disabled")
    if (disabled === undefined) {
        console.log("processIconClick on icon ", figure)
        $("#item1").append(getIcon(figure))
        stompClient.send(`${topicGameTurnEnd}.${gameId}`, {}, JSON.stringify({
            'sessionId': sessionId,
            'figureId': figure.icon
        }))
        disableAllIcons()
    }
}

const disableAllIcons = () => {
    changeIconsState(true)
}

const enableAllIcons = () => {
    changeIconsState(false)
}

const changeIconsState = (state) => {
    for (let i = 0; i < 15; i++) {
        const icon = $(`#icon${i}`)
        if (icon.length > 0) {
            if (state) {
                icon.prop("disabled", state)
            } else {
                icon.removeProp("disabled")
            }
        }
    }
}

const turnStart = (turnInfo) => {
    enableAllIcons()
    $("#item1").empty()
    $("#item2").empty()
    $("#result").empty()
    $("#currentTurn").text(turnInfo.turn)
    $("#availCheats").text(turnInfo.availCheats)
    enableAllIcons()
}

const turnResult = (resultInfo) => {
    $("#item1")
        .empty()
        .append(getIcon(resultInfo.figure1))
    $("#item2")
        .empty()
        .append(getIcon(resultInfo.figure2))
    $("#result").text(resultInfo.resultText)
    $("#money1").text(resultInfo.money1)
    $("#count1").text(resultInfo.count1)

    $("#money2").text(resultInfo.money2)
    $("#count2").text(resultInfo.count2)

    const cheats = parseInt($("#availCheats").text().toString())
    if (cheats > 0 && resultInfo.resultText === "<") {
        $("#useCheatBtn").show()
    }
    $("#nextBtn").show()
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
    $("#nextBtn").hide()
    $("#useCheatBtn").hide()
    start()
});
