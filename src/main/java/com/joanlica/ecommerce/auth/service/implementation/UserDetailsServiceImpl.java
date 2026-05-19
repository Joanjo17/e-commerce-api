package com.joanlica.ecommerce.auth.service.implementation;

import com.joanlica.ecommerce.auth.model.UserAuth;
import com.joanlica.ecommerce.auth.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAuthRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth userAuth = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email: " + username + " not found."));

        Set<GrantedAuthority> authorityList = new LinkedHashSet<>();

        // Convert role names into Spring Security authorities.
        userAuth.getRolesList().forEach(role -> {
            String roleName = role.getRoleName();
            String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_".concat(roleName);
            authorityList.add(new SimpleGrantedAuthority(authority));
        });

        return new User(userAuth.getEmail(),
                userAuth.getPassword(),
                userAuth.isEnabled(),
                userAuth.isAccountNonExpired(),
                userAuth.isCredentialsNonExpired(),
                userAuth.isAccountNonLocked(),
                authorityList);
    }
}
