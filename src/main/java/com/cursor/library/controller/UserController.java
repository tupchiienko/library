package com.cursor.library.controller;

import com.cursor.library.service.UserService;
import com.cursor.library.utils.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthenticationRequest auth) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
        final UserDetails userDetails = userService.login(auth.getUsername(), auth.getPassword());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    @Data
    public static class AuthenticationRequest implements Serializable {
        private String username;
        private String password;
    }

}
