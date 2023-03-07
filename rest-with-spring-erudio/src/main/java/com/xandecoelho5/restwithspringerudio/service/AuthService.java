package com.xandecoelho5.restwithspringerudio.service;

import com.xandecoelho5.restwithspringerudio.data.vo.v1.security.AccountCredentialsVO;
import com.xandecoelho5.restwithspringerudio.repository.UserRepository;
import com.xandecoelho5.restwithspringerudio.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<?> signIn(AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            var user = repository.findByUserName(username);
            if (user != null) {
                return ResponseEntity.ok(tokenProvider.createAccessToken(username, user.getRoles()));
            } else {
                throw new UsernameNotFoundException("Username " + data.getUsername() + " not found");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    public ResponseEntity<?> refreshToken(String username, String refreshToken) {
        var user = repository.findByUserName(username);
        if (user != null) {
            return ResponseEntity.ok(tokenProvider.refreshToken(refreshToken));
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
}
