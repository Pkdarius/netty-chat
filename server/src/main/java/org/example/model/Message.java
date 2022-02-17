package org.example.model;

import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant sentTime;
    private String frm;
    @Column(name = "`to`")
    private String to;
    private String content;
}
