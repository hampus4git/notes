package com.example.documentApp.core;

import javax.security.auth.Subject;
import java.security.Principal;

public class User implements Principal {

    private final String name;
    private final String role;

    public User(String name, String role){
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() { return role; }

    public boolean implies(Subject subject) {
        return false;
    }
}
