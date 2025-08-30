package com.zoo.security;

import com.zoo.user.Role;
import com.zoo.user.UserEntity;
import com.zoo.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public  JpaUserDetailsService(UserRepository users) {this.users=users;}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserEntity u = users.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        var auths = u.getRoles().stream()
                .map(Role::name)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toSet());

        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(auths)
                .accountExpired(false).accountLocked(false)
                .credentialsExpired(false).disabled(false)
                .build();
    }
}
