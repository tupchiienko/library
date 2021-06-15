package com.cursor.library.service;

import com.cursor.library.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    static AccessDeniedException ACCESS_DENIED = new AccessDeniedException("Access denied");

    public UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public UserDetails login(String username, String password) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> ACCESS_DENIED);
        if (!encoder.matches(password, user.getPassword()))
            throw ACCESS_DENIED;
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow();
    }
}
