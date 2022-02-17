package org.example.model;

import lombok.*;

import java.io.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private Long id;
    private Instant sentTime = Instant.now();
    private String frm;
    private String to;
    private String content;
}
