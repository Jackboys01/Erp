package com.ERP.services;

import com.ERP.entities.JwtAuthentication;
import com.ERP.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userRepository;
@Autowired
private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtAuthentication user = userRepository.findByUsername(username);
               if(user==null)throw new UsernameNotFoundException("not found username");
               JwtAuthentication authentication = new JwtAuthentication();

        return org.springframework.security.core.userdetails.User.builder()

                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRole())
                .build();
    }
}
