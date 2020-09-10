package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Value;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Value
public class NewPlayerReplyData implements MessageData {
    boolean isUserAdded;
}
