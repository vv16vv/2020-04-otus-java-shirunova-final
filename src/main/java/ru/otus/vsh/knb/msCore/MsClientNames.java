package ru.otus.vsh.knb.msCore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors
public enum MsClientNames {
    DATA_BASE("DataBaseMSClient"),
    GAME_KEEPER("GameKeeperMSClient"),
    GAME_CONTROLLER("GameControllerMSClient"),
    LOBBY_CONTROLLER("LobbyControllerMSClient"),
    NEW_PLAYER_CONTROLLER("NewPlayerControllerMSClient"),
    PLAYERS_CONTROLLER("PlayersControllerMSClient"),
    ;

    @Getter
    private final String name;
}
