let stompClient = null;

const answerInputPlaceholder = 'answer-input-';
const answerButtonPlaceholder = 'answer-button-';
const trPlaceholder = 'tr-';

const websocketUrl = '/api/game-ws';
const topicEquation = '/topic/equation';
const topicResult = '/topic/result';
const topicCorrect = '/topic/correct';

const topicAnswer = '/api/answer';
const topicGameStart = '/api/game-start';

const setConnected = (connected) => {
    initPage(!connected)
    if (connected) {
        $("#game-process").show();
    } else {
        $("#game-process").hide();
    }
}

const start = (sessionId) => {
    stompClient = Stomp.over(new SockJS(websocketUrl));
    stompClient.connect({}, (frame) => {
        $("#game-result").show()
        const number = $("#number-input").val()
        console.log("start: number = ", number)
        setConnected(true);
        $("#game-header").append("Повторим таблицу умножения на " + number);
        console.log('Connected: ' + frame);
        console.log("start: sessionId = ", sessionId)
        stompClient.subscribe(`${topicEquation}.${sessionId}`, (equation) => showEquation(JSON.parse(equation.body)));
        stompClient.subscribe(`${topicResult}.${sessionId}`, (result) => showResult(JSON.parse(result.body)));
        stompClient.subscribe(`${topicCorrect}.${sessionId}`, (result) => showCorrect(JSON.parse(result.body)));

        stompClient.send(`${topicGameStart}`, {}, JSON.stringify({
            'gameId': sessionId,
            'number': number
        }))
    });
}

// Expected values in result:
// gameId: String
// numberOfSuccess: Integer
// numberOfAll: Integer
// eqIndex: Integer
const showResult = (result) => {
    console.log("show result = ", result)
    $("#label-result").text(`Количество правильных ответов ${result.numberOfSuccess} из ${result.numberOfAll}`)
    if (result.eqIndex === result.numberOfAll) {
        gameOver(result)
    }
}

// Expected values in result:
// gameId: String
// eqIndex: Integer
// isCorrect: Boolean
const showCorrect = (result) => {
    console.log("show correct = ", result)
    if (result.eqIndex >= 0) {
        const eqTr = $(`#${trPlaceholder}${result.eqIndex}`)
        if(!result.correct){
            eqTr.addClass("mistake")
        }
    }
}

const gameOver = (result) => {
    console.log("game over = ", result)
    stompClient.unsubscribe(`${topicEquation}.${result.gameId}`);
    stompClient.unsubscribe(`${topicResult}.${result.gameId}`);
    stompClient.unsubscribe(`${topicCorrect}.${result.gameId}`);
    const gameOverDiv = $("#game-over")
    const mark = Math.round(result.numberOfSuccess / result.numberOfAll * 100);
    const letsBegin = "Начать с начала?";
    let restartTitle;
    switch (true) {
        case mark > 96: {
            restartTitle = "Отлично! Ни одной ошибки! (5) " + letsBegin;
            break;
        }
        case mark > 79: {
            restartTitle = "Очень неплохо! (4) " + letsBegin;
            break;
        }
        case mark > 49: {
            restartTitle = "Неплохо! (3) " + letsBegin;
            break;
        }
        case mark > 29: {
            restartTitle = "Можно и получше! (2) " + letsBegin;
            break;
        }
        default: {
            restartTitle = "Стоит еще подучить! (1) " + letsBegin;
            break;
        }

    }
    gameOverDiv.append(`<input id="restart" type="submit" value="${restartTitle}" onclick="restart(${result.numberOfAll})"/>`)
    $("#restart").focus();
    gameOverDiv.show()
}

const restart = (n) => {
    disconnect()
    $("#game-start").show()
    $("#game-result").hide()
    $("#game-process").hide()
    $("#game-over").hide()
    $("#restart").remove()
    $("#game-header").text("")
    for (let i = 0; i < n; i++) {
        $(`#${trPlaceholder}${i}`).remove()
    }
}

// Expected values in equation:
// gameId : String
// eqIndex : Integer
// eqText : String
const showEquation = (equation) => {
    console.log("show equation = ", equation);
    const answerInputId = answerInputPlaceholder + equation.eqIndex;
    const answerButtonId = answerButtonPlaceholder + equation.eqIndex;
    const trId = trPlaceholder + equation.eqIndex;
    $("#game-equation")
        .append(`<tr id='${trId}'><td>${equation.eqIndex + 1}).</td><th>${equation.eqText}</th><td><input id='${answerInputId}' type='number' min='0' max='100' value=''></td><td><input id='${answerButtonId}' type='submit' value='Ответить' onclick='sendAnswer("${equation.gameId}", ${equation.eqIndex})'></td></tr>`)
    setFocusAndEnter(answerInputId, answerButtonId)
}

const setFocusAndEnter = (inputId, buttonId) => {
    console.log(`setFocusAndEnter on ${inputId} for ${buttonId}`)
    const inputControl = $(`#${inputId}`)
    inputControl.focus();
    inputControl.keypress(function (event) {
        console.log(`keypress on ${buttonId} key code = `, event.keyCode)
        if (event.keyCode === 13) {
            $(`#${buttonId}`).click();
        }
    });
}

const initPage = (shown) => {
    if (shown) {
        $("#game-start").show();
        $("#number-input").focus();
    } else {
        $("#game-start").hide();
    }
}

// Expected types:
// eqIndex : Integer
// eqText : String
const sendAnswer = (gameId, eqIndex) => {
    console.log("send answer: gameId = ", gameId, "; eqIndex = ", eqIndex);
    const answerInput = $(`#${answerInputPlaceholder}${eqIndex}`);
    const answer = answerInput.val();
    console.log("send answer = ", answer);

    answerInput.parent().remove();
    $(`#${answerButtonPlaceholder}${eqIndex}`).parent().remove();
    $(`#${trPlaceholder}${eqIndex}`).append(`<td>${answer}</td>`);

    stompClient.send(`${topicAnswer}.${gameId}`, {}, JSON.stringify({
        'gameId': gameId,
        'eqIndex': eqIndex,
        'answer': answer
    }))
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
    setConnected(false);
});
