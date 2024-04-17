package com.InstagramPostGenerator.AuthorizationServer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_coins")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserCoins {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "coins", columnDefinition = "INT DEFAULT 0")
    private Integer coins;


    /* Creates relation One To One with user entity
    * specifies which field (user_id) is used in relation
    * MapsId decorator specifies, that primary key of user_coins table is shared with user table
    *
    * */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    @ToString.Exclude
    private User user;
}
