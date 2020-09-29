let stompClient = null;

const answerInputPlaceholder = 'answer-input-';
const answerButtonPlaceholder = 'answer-button-';
const trPlaceholder = 'tr-';

const websocketUrl = '/api/game-ws';
const topicEquation = '/topic/equation';
const topicResult = '/topic/result';
const topicCorrect = '/topic/correct';

const topicGames = '/topic/games'
const topicGamesUpd = '/topic/games-upd'
const topGame = '/game'

const topicAnswer = '/api/answer';
const topicGameStart = '/api/game-start';
const topicGameJoin = '/api/game-join';
const topicLobbyHello = '/api/lobby-hello';

const currencyItem = '<i class="far fa-money-bill-alt"></i>'

const setConnected = (connected) => {
    console.log("setConnected = ", connected, ". Nothing else is done")
    // initPage(!connected)
    // if (connected) {
    //     $("#game-process").show();
    // } else {
    //     $("#game-process").hide();
    // }
}

const start = () => {
    if (stompClient === null) {
        stompClient = Stomp.over(new SockJS(websocketUrl));
        stompClient.connect({}, (frame) => {
            setConnected(true);
            console.log('Connected: ' + frame);
            const sessionId = $("#sessionId").text().toString()
            console.log("start: sessionId = ", sessionId)
            stompClient.subscribe(`${topicGames}.${sessionId}`, (games) => showGames(JSON.parse(games.body)));
            stompClient.subscribe(`${topicGamesUpd}.${sessionId}`, (game) => updateGame(JSON.parse(game.body)));

            $("#newGameBtn").click(() => {
                stompClient.send(`${topicGameStart}.${sessionId}`, {}, JSON.stringify({
                    'items': $("#chosenItems").text(),
                    'turns': $("#chosenTurns").text(),
                    'cheats': $("#nCheats").text(),
                    'bet': $("#chosenBet").text()
                }))
                disconnect()
                window.location.replace(topGame)
            })
            stompClient.send(`${topicLobbyHello}.${sessionId}`, {}, {})
        });
    } else {
        console.log("Stomp client already exists")
    }
}

// Expected state of games:
// list of game items
// Each game item contains:
// - style
// - id
// - title
// - bet
const showGames = (games) => {
    games.forEach(game => {
        console.log("showGames: game = ", JSON.stringify(game))
        $("#avail-games-div").append(formOneGameLine(game))
    })
}

const formOneGameLine = (game) => {
    return $('<tr></tr>')
        .attr({
            id: 'game' + game.id
        })
        .addClass(game.style + ' layoutTable')
        .append($('<td>' + game.title + '</td>'))
        .append($('<td>' + game.bet + currencyItem + '</td>'))
        .append($('<td></td>')
            .append($('<input/>')
                .addClass("btn btn-success")
                .attr({
                    id: 'play' + game.id,
                    disabled: game.style !== 'availableToPlay',
                    type: 'submit',
                    value: 'Играть'
                })
                .click(() => processJoinGame(true, game))
            )
        )
        .append($('<td></td>')
            .append($('<input/>')
                .addClass("btn btn-info")
                .attr({
                    id: 'observe' + game.id,
                    disabled: game.style === 'availableToPlay',
                    type: 'submit',
                    value: 'Наблюдать'
                })
                .click(() => processJoinGame(false, game))
            )
        );
}

// Each game item contains:
// - style
// - id
// - title
// - bet
const updateGame = (game) => {
    console.log("update game = ", game)
    const trId = `#game${game.id}`;
    const tr = $(trId).remove();
    $("#avail-games-div").append(formOneGameLine(game))
}

// asPlayer - true if as a player, false if as an observer
const processJoinGame = (asPlayer, game) => {
    const sessionId = $("#sessionId").text().toString()
    console.log("processJoinGame: game = ", game, "; asPlayer = ", asPlayer, "; sessionId = ", sessionId)
    stompClient.send(`${topicGameJoin}.${sessionId}`, {}, JSON.stringify({
        'isPlayer': asPlayer.toString(),
        'gameId': game.id
    }))
    disconnect()
    window.location.replace(topGame)
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const setTooltip = (prefix, item, value, label) => {
    item["title"] = `${prefix} ${value}`
    label.text(value)
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#runSettings").click(() => $("#newGameDialog").modal())
    $("#nItems").change((e) => setTooltip("Элементов", e.target, e.target.value, $("#chosenItems")));
    $("#nTurns").change((e) => {
        setTooltip("Раундов", e.target, e.target.value, $("#chosenTurns"));
        $("#nCheats").text(Math.floor(parseInt(e.target.value) / 4))
    });
    $("#nBet").change((e) => {
        setTooltip("Ставка", e.target, e.target.value, $("#chosenBet"));
    });
    $(document).ready(() => $('[data-toggle="tooltip"]').tooltip())
    start()
});
