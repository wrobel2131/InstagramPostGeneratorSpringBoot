package com.InstagramPostGenerator.AuthorizationServer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profile")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserProfile {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    @ToString.Exclude
    private User user;
}
