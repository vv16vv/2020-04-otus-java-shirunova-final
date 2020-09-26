package ru.otus.vsh.knb.webCore;

public final class Routes {

    public static final String ROOT = "/";
    public static final String PLAYERS = "/players";
    public static final String NEW_PLAYER = "/new-player";
    public static final String LOBBY = "/lobby";
    public static final String GAME = "/game";

    public static final String API = "/api";
    public static final String API_LOGIN = "/api/login";
    public static final String API_LOGOUT = "/api/logout";

    public static final String API_GAME_WS = "/api/game-ws";
    // Accepted messages
    public static final String API_LOBBY_HELLO = "/lobby-hello.{sessionId}";
    public static final String API_GAME_START = "/game-start.{sessionId}";
    public static final String API_GAME_JOIN = "/game-join.{sessionId}";
    public static final String API_GAME_HELLO = "/game-hello.{sessionId}";
    public static final String API_GAME_TURN_END = "/game-turn-end.{gameId}";
    public static final String API_GAME_TURN_NEXT = "/game-turn-next.{gameId}";

    public static final String TOPIC = "/topic";
    // Sent messages
    public static final String TOPIC_GAMES = "/topic/games";
    public static final String TOPIC_GAMES_UPD = "/topic/games-upd";
    public static final String TOPIC_GAME_STATUS = "/topic/game-status";
    public static final String TOPIC_GAME_INFO = "/topic/game-info";
    public static final String TOPIC_GAME_UPDATE = "/topic/game-update";
    public static final String TOPIC_GAME_TURN_START = "/topic/game-turn-start";
    public static final String TOPIC_GAME_TURN_RESULT = "/topic/game-turn-result";
    public static final String TOPIC_GAME_END = "/topic/game-end";

    public static final String ERROR = "/error";
}
