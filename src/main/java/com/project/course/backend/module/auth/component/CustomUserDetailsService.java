package com.project.course.backend.module.auth.component;

import com.project.course.backend.module.auth.entity.UserEntity;
import com.project.course.backend.module.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByEmail(username)
                .map(this::mapToUserDetails)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Email not found"));
    }

    private UserDetails mapToUserDetails(UserEntity u) {
        return User.withUsername(u.getUsername())
                .password(u.getPassword())          // **ต้องเข้ารหัสแล้ว**
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
