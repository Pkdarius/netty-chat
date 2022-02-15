package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 0L;

    public enum MessageType {LOGIN, CHAT, GET_MESSAGE}

    private String from;
    private String to;

    private MessageType type;
    private String message;
}
