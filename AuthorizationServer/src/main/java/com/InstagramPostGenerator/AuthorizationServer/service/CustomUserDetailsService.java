package com.InstagramPostGenerator.AuthorizationServer.service;

import com.InstagramPostGenerator.AuthorizationServer.entity.User;
import com.InstagramPostGenerator.AuthorizationServer.model.CustomUserDetails;
import com.InstagramPostGenerator.AuthorizationServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with given email not found!"));
        return new CustomUserDetails(user);
    }


}
