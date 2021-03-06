package it.imperato.service.security.jwt.service.impl;

import it.imperato.service.security.jwt.model.User;
import it.imperato.service.security.jwt.repository.UserRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Francesco
 */

//@Service
public class CustomUserDetailsService implements UserDetailsService {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("userRepositoryImpl")
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        UserDetails user = inMemoryUserDetailsManager.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }

    public void changePassword(String oldPassword, String newPassword) {

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String username = currentUser.getName();

        if (authenticationManager != null) {
            log.debug("Re-authenticating user '"+ username + "' for password change request.");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            log.debug("No authentication manager set. can't change Password!");

            return;
        }

        log.debug("Changing password for user '"+ username + "'");

        User user = (User) loadUserByUsername(username);

        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user); TODO

    }

}
