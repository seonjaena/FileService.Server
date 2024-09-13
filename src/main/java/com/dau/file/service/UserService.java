package com.dau.file.service;

import com.dau.file.entity.User;
import com.dau.file.exception.exception.UserIdNotFoundException;
import com.dau.file.repository.UserRepository;
import com.dau.file.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = getUser(username);
        }catch(UserIdNotFoundException e) {
            throw new UsernameNotFoundException(
                    messageSource.getMessage("error.user-id-pw.incorrect", null, LocaleContextHolder.getLocale())
            );
        }

        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole()));

        return new AuthUser(user.getUserId(), user.getUserPw(), roles, user);
    }

    public User getUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserIdNotFoundException(
                        messageSource.getMessage("error.user-id.incorrect", null, LocaleContextHolder.getLocale())
                ));
    }

}
