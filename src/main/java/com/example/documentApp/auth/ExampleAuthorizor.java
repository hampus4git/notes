package com.example.documentApp.auth;

import com.example.documentApp.core.User;
import io.dropwizard.auth.Authorizer;

public class ExampleAuthorizor implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRole() != null && user.getRole().equalsIgnoreCase(role);
    }
}

