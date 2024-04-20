package com.InstagramPostGenerator.AuthorizationServer.repository;

import com.InstagramPostGenerator.AuthorizationServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
    Optional<User> findByEmail(String email);
}
