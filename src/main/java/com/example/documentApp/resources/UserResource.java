package com.example.documentApp.resources;


import com.codahale.metrics.annotation.Timed;
import com.example.documentApp.core.LoginForm;
import com.example.documentApp.core.Response;
import com.example.documentApp.core.User;
import com.example.documentApp.dao.NoteDAO;
import com.example.documentApp.dao.UserDAO;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;


@Path("/user")
public class UserResource {
    private final UserDAO userDAO;
    private final NoteDAO noteDAO;

    public UserResource(UserDAO userDAO, NoteDAO noteDAO) {
        this.userDAO = userDAO;
        this.noteDAO = noteDAO;
    }

    @POST
    @Path("/login")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginForm loginForm){
        Optional<String> username = Optional.of(loginForm.getUsername());
        Optional<String> password = Optional.of(loginForm.getPassword());
        if(userDAO.findUsernamePasswordCombination(username.orElse(""), password.orElse("")) == null){
            return new Response(Response.UNAUTHORIZED, null);
        }else{
            return new Response(Response.SUCCESS, null);
        }
    }

    @POST
    @Path("/register")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(LoginForm loginForm){

        Optional<String> username = getOptional(loginForm.getUsername());
        Optional<String> password = getOptional(loginForm.getPassword());
        if(!valid(username) || !valid(password)){
            return new Response(Response.INVALID_INPUT, null);
        }else if(userDAO.findUsername(username.get()) != null){
            return new Response(Response.USERNAME_ALREADY_PRESENT, null);
        }else{
            userDAO.insert(username.get(), password.get(), "USER");
            return new Response(Response.SUCCESS, null);
        }
    }

    private Optional<String> getOptional(String value){
        if(value == null){
            return Optional.empty();
        }else{
            return Optional.of(value);
        }

    }

    private boolean valid(Optional<String> usernameOrPassword){
        return usernameOrPassword.isPresent() && !usernameOrPassword.get().equals("");
    }

    @POST
    @Path("/unregister")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER"})
    public Response unregister(@Auth User user){
        userDAO.delete(user.getName());
        noteDAO.deleteNotes(user.getName());
        return new Response(Response.SUCCESS, null);
    }

}