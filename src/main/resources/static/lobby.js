let stompClient = null;

const answerInputPlaceholder = 'answer-input-';
const answerButtonPlaceholder = 'answer-button-';
const trPlaceholder = 'tr-';

const websocketUrl = '/api/game-ws';
const topicEquation = '/topic/equation';
const topicResult = '/topic/result';
const topicCorrect = '/topic/correct';

const topicGames = '/topic/games'

const topicAnswer = '/api/answer';
const topicGameStart = '/api/game-start';
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
            const playerLogin = $("#playerLogin").text().toString()
            console.log("start: playerLogin = ", playerLogin)
            stompClient.subscribe(`${topicGames}.${sessionId}`, (games) => showGames(JSON.parse(games.body)));
            // stompClient.subscribe(`${topicResult}.${sessionId}`, (result) => showResult(JSON.parse(result.body)));
            // stompClient.subscribe(`${topicCorrect}.${sessionId}`, (result) => showCorrect(JSON.parse(result.body)));

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
        const tr = $('<tr></tr>')
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
            )
        ;
        $("#avail-games-div").append(tr)
    })
}

// asPlayer - true if as a player, false if as an observer
const processJoinGame = (asPlayer, game) => {
    console.log("processJoinGame: game = ", game, "; asPlayer = ", asPlayer)
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    start()
    // setConnected(false);
});
