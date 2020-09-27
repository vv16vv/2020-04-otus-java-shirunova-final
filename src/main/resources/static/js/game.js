let stompClient = null;

const websocketUrl = '/api/game-ws';
const lobby = '/lobby'

const topicGameInfo = '/topic/game-info';
const topicGameUpdate = '/topic/game-update';
const topicGameStatus = '/topic/game-status';
const topicGameTurnStart = '/topic/game-turn-start';
const topicGameTurnResult = '/topic/game-turn-result';
const topicGameEnd = '/topic/game-end';

const topicGameHello = '/api/game-hello';
const topicGameTurnEnd = '/api/game-turn-end';
const topicGameTurnNext = '/api/game-turn-next';
const topicGameUseCheat = '/api/game-use-cheat';
const topicGameLeaveObserver = '/api/game-leave-observer';

const start = () => {
    if (stompClient === null) {
        stompClient = Stomp.over(new SockJS(websocketUrl));
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            const sessionId = $("#sessionId").text().toString()
            console.log("start: sessionId = ", sessionId)
            // Первичная загрузка данных
            stompClient.subscribe(`${topicGameInfo}.${sessionId}`, (gameInfo) => showInitialGameInfo(sessionId, JSON.parse(gameInfo.body)));
            // Информация о втором игроке для первого игрока
            stompClient.subscribe(`${topicGameUpdate}.${sessionId}`, (updateInfo) => updateInitialGameInfo(JSON.parse(updateInfo.body)));
            // Обновление статуса игры. Ни на что больше не влияет
            stompClient.subscribe(`${topicGameStatus}.${sessionId}`, (status) => updateGameStatus(status.body));
            // Начало хода: активируем иконки, показываем номер текущего хода и кол-во доступных выручаек
            stompClient.subscribe(`${topicGameTurnStart}.${sessionId}`, (turnInfo) => turnStart(JSON.parse(turnInfo.body)));
            // Обновление экрана по результатам хода, показываем ход противника и кнопку дальше (если не последний ход)
            stompClient.subscribe(`${topicGameTurnResult}.${sessionId}`, (resultInfo) => turnResult(JSON.parse(resultInfo.body)));
            // Обновление экрана по концу игры
            stompClient.subscribe(`${topicGameEnd}.${sessionId}`, () => gameEnd());

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
    disableAllIcons()

    $("#gameBet").text(gameInfo.bet)

    if (isPlayer) {
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
    }
    const exitBtn = $("#exitBtn")
    exitBtn.click(() => {
        if (!isPlayer) {
            stompClient.send(`${topicGameLeaveObserver}.${sessionId}.${gameInfo.gameId}`, {}, {})
        }
        disconnect()
        window.location.replace(lobby)
    })
    if (isPlayer) {
        exitBtn.hide()
    }
}

const updateInitialGameInfo = (updateInfo) => {
    $("#playerName2").text(updateInfo.playerName2)
    $("#money2").text(updateInfo.money2)
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

    if (toBoolean(resultInfo.isPlayer)) {
        const cheats = parseInt($("#availCheats").text().toString())
        if (cheats > 0 && resultInfo.resultText === "<") {
            $("#useCheatBtn").show()
        }
        if (!toBoolean(resultInfo.isLastTurn)) {
            $("#nextBtn").show()
        }
    }
}

const toBoolean = (stringValue) => {
    return stringValue.toLowerCase() === "true"
}

const gameEnd = () => {
    $("#exitBtn").show()
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
