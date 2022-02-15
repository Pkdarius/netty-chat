package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Message;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    public enum MessageType {LOGIN, CHAT, GET_MESSAGE}

    public enum Status {FAILURE, SUCCESS}

    private String from;
    private String to;

    private MessageType type;
    private Status status;

    private String message;
    private List<Message> messages;
}
