package com.InstagramPostGenerator.AuthorizationServer.entity;


import com.InstagramPostGenerator.AuthorizationServer.enums.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "error_logs")
public class ErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "error_id")
    private Long errorId;


    @Column(name = "error_type")
    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

    @Column(name = "message")
    private String message;

    @Column(name = "error_time")
    private LocalDateTime errorTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
