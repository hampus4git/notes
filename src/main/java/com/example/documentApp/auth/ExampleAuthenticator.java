package com.example.documentApp.auth;

import com.example.documentApp.core.User;
import com.example.documentApp.dao.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;


public class ExampleAuthenticator implements Authenticator<BasicCredentials, User> {

    private final UserDAO userDAO;

    public ExampleAuthenticator(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (userDAO.findUsernamePasswordCombination(credentials.getUsername(), credentials.getPassword()) != null) {
            return Optional.of(new User(credentials.getUsername(), userDAO.findRoleFor(credentials.getUsername())));
        }
        return Optional.empty();
    }
}